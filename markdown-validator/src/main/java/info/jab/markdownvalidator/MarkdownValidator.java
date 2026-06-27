///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 25
//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0

package info.jab.markdownvalidator;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "markdown-validator",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Validates markdown files from specified directories")
public class MarkdownValidator implements Callable<Integer> {

    static final List<String> DEFAULT_TARGET_DIRECTORIES = List.of(".cursor/rules", "skills", ".cursor/agents");
    private static final List<String> MARKDOWN_EXTENSIONS = List.of(".md");
    static final Duration LINK_CHECK_TIMEOUT = Duration.ofSeconds(10);

    @Option(names = { "-v", "--verbose" }, description = "Enable verbose output")
    boolean verbose;

    @Option(
            names = { "-f", "--fail-fast" },
            description = "Stop on first validation error")
    boolean failFast;

    @Option(
            names = { "-d", "--directories" },
            description = "Directories to scan for markdown files (default: .cursor/rules,skills,.cursor/agents)",
            split = ",")
    List<String> targetDirectories = DEFAULT_TARGET_DIRECTORIES;

    @Parameters(arity = "0..1", defaultValue = ".", description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final RemoteLinkRequester remoteLinkRequester;
    private final ConcurrentMap<String, Optional<String>> remoteLinkCache;
    private final int parallelism;
    private final PrintStream out;
    private final PrintStream err;

    public MarkdownValidator() {
        this(
                new HttpClientRemoteLinkRequester(),
                Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), 8)),
                System.out,
                System.err);
    }

    MarkdownValidator(RemoteLinkRequester remoteLinkRequester, int parallelism, PrintStream out, PrintStream err) {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        this.remoteLinkRequester = Objects.requireNonNull(remoteLinkRequester);
        this.remoteLinkCache = new ConcurrentHashMap<>();
        this.parallelism = Math.max(1, parallelism);
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        out.println("🔍 Starting markdown validation...");

        Path root = Paths.get(rootDir);
        if (!Files.exists(root)) {
            err.println("❌ Root directory does not exist: " + root);
            return 1;
        }

        List<Path> markdownFiles = findMarkdownFiles(root);
        if (markdownFiles.isEmpty()) {
            out.println("⚠️  No markdown files found in target directories");
            return 0;
        }

        out.printf("📄 Found %d markdown files to validate%n", markdownFiles.size());

        List<FileValidationResult> results = failFast
                ? validateSequentially(markdownFiles)
                : validateInParallel(markdownFiles);
        List<ValidationError> errors = results.stream().flatMap(result -> result.errors().stream()).toList();

        printResults(results.size(), errors);
        return errors.isEmpty() ? 0 : 1;
    }

    List<Path> findMarkdownFiles(Path root) throws IOException {
        List<Path> files = new ArrayList<>();

        for (String targetDir : targetDirectories) {
            Path dir = root.resolve(targetDir);
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                try (Stream<Path> paths = Files.walk(dir)) {
                    paths.filter(Files::isRegularFile).filter(this::isMarkdownFile).forEach(files::add);
                }
            } else if (verbose) {
                out.printf("⚠️  Directory not found: %s%n", dir);
            }
        }

        files.sort(Comparator.comparing(Path::toString));
        return files;
    }

    private boolean isMarkdownFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return MARKDOWN_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    private List<FileValidationResult> validateSequentially(List<Path> markdownFiles) {
        List<FileValidationResult> results = new ArrayList<>();
        for (Path file : markdownFiles) {
            FileValidationResult result = validateFile(file);
            printFileResult(result);
            results.add(result);
            if (!result.errors().isEmpty()) {
                break;
            }
        }
        return results;
    }

    private List<FileValidationResult> validateInParallel(List<Path> markdownFiles) {
        try (ExecutorService executor = Executors.newFixedThreadPool(Math.min(parallelism, markdownFiles.size()))) {
            List<Future<FileValidationResult>> futures = new ArrayList<>();
            for (Path file : markdownFiles) {
                futures.add(executor.submit(() -> validateFile(file)));
            }

            List<FileValidationResult> results = new ArrayList<>();
            for (Future<FileValidationResult> future : futures) {
                FileValidationResult result = future.get();
                printFileResult(result);
                results.add(result);
            }
            return results;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of(FileValidationResult.withError(
                    Paths.get(rootDir), "Validation interrupted: " + describeException(e)));
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            return List.of(FileValidationResult.withError(
                    Paths.get(rootDir), "Validation failed unexpectedly: " + describeThrowable(cause)));
        }
    }

    private FileValidationResult validateFile(Path file) {
        List<String> messages = new ArrayList<>();
        messages.add(String.format("🔍 Validating: %s", file));

        try {
            String content = Files.readString(file);
            List<ValidationError> errors = validateContent(file, content);
            if (verbose && errors.isEmpty()) {
                Node document = parser.parse(content);
                String html = renderer.render(document);
                messages.add(String.format(
                        "✅ Successfully parsed: %s (%d characters, HTML: %d characters)",
                        file.getFileName(), content.length(), html.length()));
            }
            if (verbose) {
                errors.forEach(error -> messages.add(
                        String.format("❌ %s:%d - %s", error.file(), error.lineNumber(), error.message())));
            }
            return new FileValidationResult(file, messages, errors);
        } catch (IOException e) {
            ValidationError error = new ValidationError(file, 0, "Failed to read file: " + e.getMessage());
            if (verbose) {
                messages.add(String.format("❌ %s:%d - %s", error.file(), error.lineNumber(), error.message()));
            }
            return new FileValidationResult(file, messages, List.of(error));
        }
    }

    private List<ValidationError> validateContent(Path file, String content) {
        try {
            Node document = parser.parse(content);
            String html = renderer.render(document);

            if (html == null) {
                return List.of(new ValidationError(file, 0, "HTML rendering produced null output"));
            }

            return validateRemoteLinks(file, document);
        } catch (Exception e) {
            return List.of(new ValidationError(file, 0, "Failed to parse markdown: " + e.getMessage()));
        }
    }

    private List<ValidationError> validateRemoteLinks(Path file, Node document) {
        List<ValidationError> errors = new ArrayList<>();
        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Link link) {
                validateRemoteLink(file, link.getDestination()).ifPresent(errors::add);
                super.visit(link);
            }

            @Override
            public void visit(Image image) {
                validateRemoteLink(file, image.getDestination()).ifPresent(errors::add);
                super.visit(image);
            }
        });
        return errors;
    }

    private Optional<ValidationError> validateRemoteLink(Path file, String destination) {
        if (destination == null || destination.isBlank()) {
            return Optional.empty();
        }

        URI uri;
        try {
            uri = new URI(destination.strip());
        } catch (URISyntaxException e) {
            return Optional.empty();
        }

        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return Optional.empty();
        }

        Optional<String> cachedError = remoteLinkCache.computeIfAbsent(uri.toString(), ignored -> checkRemoteLink(uri));
        return cachedError.map(message -> new ValidationError(file, 0, message));
    }

    private Optional<String> checkRemoteLink(URI uri) {
        URI requestUri = removeFragment(uri);

        try {
            RemoteLinkResponse response = remoteLinkRequester.request(requestUri, "HEAD", LINK_CHECK_TIMEOUT);
            if (response.statusCode() == 405 || response.statusCode() == 403) {
                response = remoteLinkRequester.request(requestUri, "GET", LINK_CHECK_TIMEOUT);
            }

            int status = response.statusCode();
            if (status >= 300 && status < 400) {
                return Optional.of(
                        "Remote link redirects instead of resolving directly: " + uri + " (HTTP " + status + ")");
            }
            if (status >= 400) {
                return Optional.of("Remote link is not reachable: " + uri + " (HTTP " + status + ")");
            }

            return Optional.empty();
        } catch (java.net.http.HttpTimeoutException e) {
            return Optional.of("Remote link timed out after " + LINK_CHECK_TIMEOUT.toSeconds() + " seconds: " + uri);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return Optional.of("Remote link check failed: " + uri + " (" + describeException(e) + ")");
        }
    }

    private static String describeException(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return exception.getClass().getSimpleName();
        }
        return message;
    }

    private static String describeThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return message;
    }

    private URI removeFragment(URI uri) {
        try {
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to normalize URI: " + uri, e);
        }
    }

    private void printFileResult(FileValidationResult result) {
        result.messages().forEach(out::println);
    }

    private void printResults(int validatedDocuments, List<ValidationError> errors) {
        out.println();
        out.println("=".repeat(60));

        if (errors.isEmpty()) {
            out.printf("✅ All markdown files are valid! documents_validated=%d%n", validatedDocuments);
            out.printf("VALIDATION_RESULT status=passed errors=0 documents_validated=%d%n", validatedDocuments);
        } else {
            out.printf("❌ Found %d validation errors.%n%n", errors.size());
            out.println("Human-readable report:");
            out.println("Review each file and reason below, then rerun the validator.");
            out.println();

            Map<Path, List<ValidationError>> errorsByFile = new LinkedHashMap<>();
            for (ValidationError error : errors) {
                errorsByFile.computeIfAbsent(error.file(), ignored -> new ArrayList<>()).add(error);
            }

            for (Map.Entry<Path, List<ValidationError>> entry : errorsByFile.entrySet()) {
                out.printf("📄 %s:%n", entry.getKey());
                for (ValidationError error : entry.getValue()) {
                    if (error.lineNumber() > 0) {
                        out.printf("   Line %d: %s%n", error.lineNumber(), error.message());
                    } else {
                        out.printf("   %s%n", error.message());
                    }
                }
                out.println();
            }

            out.println("Agent-readable issue records:");
            for (ValidationError error : errors) {
                out.printf(
                        "MARKDOWN_VALIDATION_ISSUE file=\"%s\" line=%d reason=\"%s\"%n",
                        escapeConsoleValue(error.file().toString()),
                        error.lineNumber(),
                        escapeConsoleValue(error.message()));
            }
            out.println();
            out.printf(
                    "VALIDATION_RESULT status=failed errors=%d documents_validated=%d%n",
                    errors.size(),
                    validatedDocuments);
        }

        out.println("=".repeat(60));
    }

    private String escapeConsoleValue(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
    }

    record ValidationError(Path file, int lineNumber, String message) {}

    record FileValidationResult(Path file, List<String> messages, List<ValidationError> errors) {
        static FileValidationResult withError(Path file, String message) {
            return new FileValidationResult(file, List.of(), List.of(new ValidationError(file, 0, message)));
        }
    }

    record RemoteLinkResponse(int statusCode) {}

    interface RemoteLinkRequester {
        RemoteLinkResponse request(URI uri, String method, Duration timeout) throws IOException, InterruptedException;
    }

    private static final class HttpClientRemoteLinkRequester implements RemoteLinkRequester {

        private final HttpClient httpClient;

        private HttpClientRemoteLinkRequester() {
            this.httpClient = HttpClient.newBuilder()
                    .connectTimeout(LINK_CHECK_TIMEOUT)
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .build();
        }

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout)
                throws IOException, InterruptedException {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                    .timeout(timeout)
                    .header("User-Agent", "java-cursor-rules-markdown-validator")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            if ("HEAD".equals(method)) {
                requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
            } else {
                requestBuilder.GET();
            }

            HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            return new RemoteLinkResponse(response.statusCode());
        }
    }
}

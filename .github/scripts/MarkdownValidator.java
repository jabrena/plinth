///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@Command(name = "markdown-validator",
         mixinStandardHelpOptions = true,
         version = "1.0",
         description = "Validates markdown files from specified directories")
public class MarkdownValidator implements Callable<Integer> {

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    boolean verbose;

    @Option(names = {"-f", "--fail-fast"}, description = "Stop on first validation error")
    boolean failFast;

    @Option(names = {"-d", "--directories"},
            description = "Directories to scan for markdown files (default: .cursor/rules,skills,.cursor/agents)",
            split = ",")
    List<String> targetDirectories = List.of(".cursor/rules", "skills", ".cursor/agents");

    @Parameters(description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private static final List<String> MARKDOWN_EXTENSIONS = List.of(".md");
    private static final Duration LINK_CHECK_TIMEOUT = Duration.ofSeconds(10);

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final HttpClient httpClient;
    private final Map<String, Optional<String>> remoteLinkCache = new HashMap<>();
    private final List<ValidationError> errors = new ArrayList<>();

    public MarkdownValidator() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(LINK_CHECK_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("🔍 Starting markdown validation...");

        Path root = Paths.get(rootDir);
        if (!Files.exists(root)) {
            System.err.println("❌ Root directory does not exist: " + root);
            return 1;
        }

        List<Path> markdownFiles = findMarkdownFiles(root);
        if (markdownFiles.isEmpty()) {
            System.out.println("⚠️  No markdown files found in target directories");
            return 0;
        }

        System.out.printf("📄 Found %d markdown files to validate\n", markdownFiles.size());

        int validatedDocuments = 0;
        for (Path file : markdownFiles) {
            validateFile(file);
            validatedDocuments++;
            if (failFast && !errors.isEmpty()) {
                break;
            }
        }

        printResults(validatedDocuments);
        return errors.isEmpty() ? 0 : 1;
    }

    private List<Path> findMarkdownFiles(Path root) throws IOException {
        List<Path> files = new ArrayList<>();

        for (String targetDir : targetDirectories) {
            Path dir = root.resolve(targetDir);
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                try (Stream<Path> paths = Files.walk(dir)) {
                    paths.filter(Files::isRegularFile)
                         .filter(this::isMarkdownFile)
                         .forEach(files::add);
                }
            } else if (verbose) {
                System.out.printf("⚠️  Directory not found: %s\n", dir);
            }
        }

        return files;
    }

    private boolean isMarkdownFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return MARKDOWN_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    private void validateFile(Path file) {
        System.out.printf("🔍 Validating: %s\n", file);

        try {
            String content = Files.readString(file);
            validateContent(file, content);
        } catch (IOException e) {
            addError(file, 0, "Failed to read file: " + e.getMessage());
        }
    }

    private void validateContent(Path file, String content) {
        try {
            // Parse markdown content
            Node document = parser.parse(content);

            // Try to render to HTML to validate structure
            String html = renderer.render(document);

            // Assert that HTML output is not null
            if (html == null) {
                addError(file, 0, "HTML rendering produced null output");
                return;
            }

            validateRemoteLinks(file, document);

            if (verbose) {
                System.out.printf("✅ Successfully parsed: %s (%d characters, HTML: %d characters)\n",
                    file.getFileName(), content.length(), html.length());
            }
        } catch (Exception e) {
            addError(file, 0, "Failed to parse markdown: " + e.getMessage());
        }
    }

    private void validateRemoteLinks(Path file, Node document) {
        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Link link) {
                validateRemoteLink(file, link.getDestination());
                super.visit(link);
            }

            @Override
            public void visit(Image image) {
                validateRemoteLink(file, image.getDestination());
                super.visit(image);
            }
        });
    }

    private void validateRemoteLink(Path file, String destination) {
        if (destination == null || destination.isBlank()) {
            return;
        }

        URI uri;
        try {
            uri = new URI(destination.strip());
        } catch (URISyntaxException e) {
            return;
        }

        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return;
        }

        Optional<String> cachedError = remoteLinkCache.computeIfAbsent(uri.toString(), ignored -> checkRemoteLink(uri));
        cachedError.ifPresent(message -> addError(file, 0, message));
    }

    private Optional<String> checkRemoteLink(URI uri) {
        URI requestUri = removeFragment(uri);

        try {
            HttpResponse<String> response = requestRemoteLink(requestUri, "HEAD");
            if (response.statusCode() == 405 || response.statusCode() == 403) {
                response = requestRemoteLink(requestUri, "GET");
            }

            int status = response.statusCode();
            if (status >= 300 && status < 400) {
                return Optional.of("Remote link redirects instead of resolving directly: " + uri + " (HTTP " + status + ")");
            }
            if (status >= 400) {
                return Optional.of("Remote link is not reachable: " + uri + " (HTTP " + status + ")");
            }

            if (uri.getFragment() != null && !uri.getFragment().isBlank()) {
                Optional<String> anchorError = validateRemoteAnchor(uri, requestUri);
                if (anchorError.isPresent()) {
                    return anchorError;
                }
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

    private String describeException(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return exception.getClass().getSimpleName();
        }
        return message;
    }

    private Optional<String> validateRemoteAnchor(URI originalUri, URI requestUri)
            throws IOException, InterruptedException {
        HttpResponse<String> anchorResponse = requestRemoteLink(requestUri, "GET");

        int status = anchorResponse.statusCode();
        if (status >= 300 && status < 400) {
            return Optional.of("Remote link redirects before anchor validation: " + originalUri + " (HTTP " + status + ")");
        }
        if (status >= 400) {
            return Optional.of("Remote link is not reachable before anchor validation: " + originalUri + " (HTTP " + status + ")");
        }
        if (!isHtmlResponse(anchorResponse)) {
            return Optional.empty();
        }

        String fragment = URLDecoder.decode(originalUri.getFragment(), StandardCharsets.UTF_8);
        String html = anchorResponse.body();
        if (html == null || !containsHtmlAnchor(html, fragment)) {
            return Optional.of("Remote link anchor was not found: " + originalUri);
        }
        return Optional.empty();
    }

    private HttpResponse<String> requestRemoteLink(URI uri, String method) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                .timeout(LINK_CHECK_TIMEOUT)
                .header("User-Agent", "java-cursor-rules-markdown-validator")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        if ("HEAD".equals(method)) {
            requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
        } else {
            requestBuilder.GET();
        }

        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI removeFragment(URI uri) {
        try {
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to normalize URI: " + uri, e);
        }
    }

    private boolean isHtmlResponse(HttpResponse<String> response) {
        return response.headers()
                .firstValue("content-type")
                .map(contentType -> contentType.toLowerCase(Locale.ROOT).contains("text/html"))
                .orElse(false);
    }

    private boolean containsHtmlAnchor(String html, String anchor) {
        return containsExactHtmlAnchor(html, anchor) || containsExactHtmlAnchor(html, "user-content-" + anchor);
    }

    private boolean containsExactHtmlAnchor(String html, String anchor) {
        String escapedAnchor = java.util.regex.Pattern.quote(anchor);
        return java.util.regex.Pattern.compile("\\s(?:id|name)\\s*=\\s*\"" + escapedAnchor + "\"").matcher(html).find()
                || java.util.regex.Pattern.compile("\\s(?:id|name)\\s*=\\s*'" + escapedAnchor + "'").matcher(html).find();
    }

    private void addError(Path file, int lineNumber, String message) {
        errors.add(new ValidationError(file, lineNumber, message));
        if (verbose) {
            System.out.printf("❌ %s:%d - %s\n", file, lineNumber, message);
        }
    }

    private void printResults(int validatedDocuments) {
        System.out.println("\n" + "=".repeat(60));

        if (errors.isEmpty()) {
            System.out.printf("✅ All markdown files are valid! documents_validated=%d\n", validatedDocuments);
            System.out.printf("VALIDATION_RESULT status=passed errors=0 documents_validated=%d\n", validatedDocuments);
        } else {
            System.out.printf("❌ Found %d validation errors.\n\n", errors.size());
            System.out.println("Human-readable report:");
            System.out.println("Review each file and reason below, then rerun the validator.");
            System.out.println();

            Map<Path, List<ValidationError>> errorsByFile = new LinkedHashMap<>();
            for (ValidationError error : errors) {
                errorsByFile.computeIfAbsent(error.file, k -> new ArrayList<>()).add(error);
            }

            for (Map.Entry<Path, List<ValidationError>> entry : errorsByFile.entrySet()) {
                System.out.printf("📄 %s:\n", entry.getKey());
                for (ValidationError error : entry.getValue()) {
                    if (error.lineNumber > 0) {
                        System.out.printf("   Line %d: %s\n", error.lineNumber, error.message);
                    } else {
                        System.out.printf("   %s\n", error.message);
                    }
                }
                System.out.println();
            }

            System.out.println("Agent-readable issue records:");
            for (ValidationError error : errors) {
                System.out.printf("MARKDOWN_VALIDATION_ISSUE file=\"%s\" line=%d reason=\"%s\"\n",
                    escapeConsoleValue(error.file.toString()), error.lineNumber, escapeConsoleValue(error.message));
            }
            System.out.println();
            System.out.printf("VALIDATION_RESULT status=failed errors=%d documents_validated=%d\n",
                errors.size(), validatedDocuments);
        }

        System.out.println("=".repeat(60));
    }

    private String escapeConsoleValue(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private static class ValidationError {
        final Path file;
        final int lineNumber;
        final String message;

        ValidationError(Path file, int lineNumber, String message) {
            this.file = file;
            this.lineNumber = lineNumber;
            this.message = message;
        }
    }
}

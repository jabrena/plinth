package info.jab.markdownvalidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownValidatorTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void clearInterruptedFlag() {
        Thread.interrupted();
    }

    @Test
    void commandLine_preservesCurrentOptionsAndDefaults() {
        MarkdownValidator validator = validator(new FixedRequester(200), 1, new Output());

        new CommandLine(validator).parseArgs("--verbose", "--fail-fast", "--directories", "docs,skills", tempDir.toString());

        assertThat(validator.verbose).isTrue();
        assertThat(validator.failFast).isTrue();
        assertThat(validator.targetDirectories).containsExactly("docs", "skills");
        assertThat(validator.rootDir).isEqualTo(tempDir.toString());

        MarkdownValidator defaultValidator = validator(new FixedRequester(200), 1, new Output());

        new CommandLine(defaultValidator).parseArgs();

        assertThat(defaultValidator.targetDirectories).isEqualTo(MarkdownValidator.DEFAULT_TARGET_DIRECTORIES);
        assertThat(defaultValidator.rootDir).isEqualTo(".");
    }

    @Test
    void call_returnsFailureExitCodeWhenRootDoesNotExist() {
        Output output = new Output();
        MarkdownValidator validator = validator(new FixedRequester(200), 1, output);
        String missingRoot = tempDir.resolve("missing").toString();

        int exitCode = new CommandLine(validator).execute(missingRoot);

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stderr()).contains("Root directory does not exist");
    }

    @Test
    void call_reportsMissingTargetDirectoriesInVerboseModeAndSucceedsWithNoFiles() {
        Output output = new Output();
        MarkdownValidator validator = validator(new FixedRequester(200), 1, output);
        validator.targetDirectories = List.of("missing");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(output.stdout()).contains("Directory not found").contains("No markdown files found");
    }

    @Test
    void call_parsesMarkdownRendersHtmlAndReturnsSuccess() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("guide.md"), "# Title%n%n[OK](https://example.test/ok)%n".formatted());
        Output output = new Output();
        MarkdownValidator validator = validator(new FixedRequester(200), 1, output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(output.stdout())
                .contains("Successfully parsed: guide.md")
                .contains("HTML:")
                .contains("VALIDATION_RESULT status=passed errors=0 documents_validated=1");
    }

    @Test
    void call_ignoresLocalFragmentBlankAndNonHttpLinksForRemoteChecking() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(
                docs.resolve("links.md"),
                """
                [local](./guide.md)
                [fragment](#section)
                [mail](mailto:team@example.test)
                ![relative](images/logo.png)
                []( )
                [remote](https://example.test/ok)
                """);
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/ok"), 200));
        MarkdownValidator validator = validator(requester, 1, new Output());
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(requester.requestedUris()).containsExactly(URI.create("https://example.test/ok"));
    }

    @Test
    void call_preservesRemoteFailureExitCodeAndReportShape() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("broken.md"), "[bad](https://example.test/missing)%n".formatted());
        Output output = new Output();
        MarkdownValidator validator =
                validator(new CountingRequester(Map.of(URI.create("https://example.test/missing"), 404)), 1, output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stdout())
                .contains("Remote link is not reachable: https://example.test/missing (HTTP 404)")
                .contains("MARKDOWN_VALIDATION_ISSUE")
                .contains("VALIDATION_RESULT status=failed errors=1 documents_validated=1");
    }

    @Test
    void call_failFastStopsAfterFirstDocumentWithErrors() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("a.md"), "[bad](https://example.test/a)%n".formatted());
        Files.writeString(docs.resolve("b.md"), "[bad](https://example.test/b)%n".formatted());
        CountingRequester requester = new CountingRequester(
                Map.of(URI.create("https://example.test/a"), 404, URI.create("https://example.test/b"), 404));
        Output output = new Output();
        MarkdownValidator validator = validator(requester, 4, output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute("--fail-fast", tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(requester.requestedUris()).containsExactly(URI.create("https://example.test/a"));
        assertThat(output.stdout()).contains("documents_validated=1");
    }

    @Test
    void call_parallelValidationAggregatesErrorsDeterministicallyByFileOrder() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("a.md"), "[bad](https://example.test/a)%n".formatted());
        Files.writeString(docs.resolve("b.md"), "[bad](https://example.test/b)%n".formatted());
        Output output = new Output();
        MarkdownValidator validator = validator(
                new CountingRequester(
                        Map.of(URI.create("https://example.test/a"), 404, URI.create("https://example.test/b"), 404),
                        URI.create("https://example.test/a")),
                2,
                output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stdout())
                .containsSubsequence(
                        "MARKDOWN_VALIDATION_ISSUE file=\"%s\"".formatted(docs.resolve("a.md")),
                        "MARKDOWN_VALIDATION_ISSUE file=\"%s\"".formatted(docs.resolve("b.md")));
    }

    @Test
    void call_parallelValidationSharesRemoteLinkCacheAcrossDuplicateUrls() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("a.md"), "[one](https://example.test/same)%n".formatted());
        Files.writeString(docs.resolve("b.md"), "[two](https://example.test/same)%n".formatted());
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/same"), 200));
        MarkdownValidator validator = validator(requester, 2, new Output());
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(requester.callCount("https://example.test/same")).isEqualTo(1);
    }

    @Test
    void call_reportsRemoteTimeoutWithoutThrowing() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("timeout.md"), "[slow](https://example.test/slow)%n".formatted());
        Output output = new Output();
        MarkdownValidator validator = validator(new TimeoutRequester(), 1, output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute("--fail-fast", tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stdout()).contains("Remote link timed out after 10 seconds: https://example.test/slow");
    }

    @Test
    void call_preservesInterruptedFlagWhenRemoteCheckIsInterrupted() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("interrupted.md"), "[stop](https://example.test/stop)%n".formatted());
        Output output = new Output();
        MarkdownValidator validator = validator(new InterruptedRequester(), 1, output);
        validator.targetDirectories = List.of("docs");

        int exitCode = new CommandLine(validator).execute("--fail-fast", tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
        assertThat(output.stdout()).contains("Remote link check failed: https://example.test/stop (interrupted)");
    }

    private static MarkdownValidator validator(
            MarkdownValidator.RemoteLinkRequester requester, int parallelism, Output output) {
        return new MarkdownValidator(requester, parallelism, output.out(), output.err());
    }

    private static final class Output {

        private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        private final ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        private PrintStream out() {
            return new PrintStream(stdout, true, StandardCharsets.UTF_8);
        }

        private PrintStream err() {
            return new PrintStream(stderr, true, StandardCharsets.UTF_8);
        }

        private String stdout() {
            return stdout.toString(StandardCharsets.UTF_8);
        }

        private String stderr() {
            return stderr.toString(StandardCharsets.UTF_8);
        }
    }

    private static final class FixedRequester implements MarkdownValidator.RemoteLinkRequester {

        private final int statusCode;

        private FixedRequester(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public MarkdownValidator.RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            return new MarkdownValidator.RemoteLinkResponse(statusCode);
        }
    }

    private static final class CountingRequester implements MarkdownValidator.RemoteLinkRequester {

        private final Map<URI, Integer> statusCodes;
        private final URI delayedUri;
        private final List<URI> requestedUris = new ArrayList<>();
        private final Map<String, AtomicInteger> callCounts = new ConcurrentHashMap<>();

        private CountingRequester(Map<URI, Integer> statusCodes) {
            this(statusCodes, URI.create("https://example.test/not-delayed"));
        }

        private CountingRequester(Map<URI, Integer> statusCodes, URI delayedUri) {
            this.statusCodes = statusCodes;
            this.delayedUri = delayedUri;
        }

        @Override
        public MarkdownValidator.RemoteLinkResponse request(URI uri, String method, Duration timeout)
                throws InterruptedException {
            synchronized (requestedUris) {
                requestedUris.add(uri);
            }
            callCounts.computeIfAbsent(uri.toString(), ignored -> new AtomicInteger()).incrementAndGet();
            if (uri.equals(delayedUri)) {
                Thread.sleep(50);
            }
            return new MarkdownValidator.RemoteLinkResponse(statusCodes.getOrDefault(uri, 200));
        }

        private List<URI> requestedUris() {
            synchronized (requestedUris) {
                return List.copyOf(requestedUris);
            }
        }

        private int callCount(String uri) {
            return callCounts.getOrDefault(uri, new AtomicInteger()).get();
        }
    }

    private static final class TimeoutRequester implements MarkdownValidator.RemoteLinkRequester {

        @Override
        public MarkdownValidator.RemoteLinkResponse request(URI uri, String method, Duration timeout)
                throws HttpTimeoutException {
            throw new HttpTimeoutException("timed out");
        }
    }

    private static final class InterruptedRequester implements MarkdownValidator.RemoteLinkRequester {

        @Override
        public MarkdownValidator.RemoteLinkResponse request(URI uri, String method, Duration timeout)
                throws InterruptedException {
            throw new InterruptedException("interrupted");
        }
    }
}

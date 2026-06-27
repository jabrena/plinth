package info.jab.markdownvalidator.adapter.in.cli;

import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.application.port.RemoteLinkResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownValidatorTest {

    @TempDir
    Path tempDir;

    @Test
    void commandLine_preservesCurrentOptionsAndDefaults() {
        Output output = new Output();
        MarkdownValidatorCommand validator = validator(new FixedRequester(200), output);

        new CommandLine(validator).parseArgs("--verbose", "--directories", "docs,skills", tempDir.toString());

        assertThat(validator.verbose).isTrue();
        assertThat(validator.targetDirectories).containsExactly("docs", "skills");
        assertThat(validator.rootDir).isEqualTo(tempDir.toString());

        MarkdownValidatorCommand defaultValidator = validator(new FixedRequester(200), new Output());

        new CommandLine(defaultValidator).parseArgs();

        assertThat(defaultValidator.targetDirectories).isEqualTo(MarkdownValidatorCommand.DEFAULT_TARGET_DIRECTORIES);
        assertThat(defaultValidator.rootDir).isEqualTo(".");
    }

    @Test
    void call_returnsFailureExitCodeWhenRootDoesNotExist() {
        Output output = new Output();
        MarkdownValidatorCommand validator = validator(new FixedRequester(200), output);
        String missingRoot = tempDir.resolve("missing").toString();

        int exitCode = new CommandLine(validator).execute(missingRoot);

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stderr()).contains("Root directory does not exist");
    }

    @Test
    void call_reportsMissingTargetDirectoriesInVerboseModeAndSucceedsWithNoFiles() {
        Output output = new Output();
        MarkdownValidatorCommand validator = validator(new FixedRequester(200), output);
        validator.targetDirectories = java.util.List.of("missing");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(output.stdout()).contains("Directory not found").contains("No markdown files found");
    }

    @Test
    void call_parsesMarkdownRendersHtmlAndReturnsSuccess() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("guide.md"), "# Title%n%n[OK](https://example.test/ok)%n".formatted());
        Output output = new Output();
        MarkdownValidatorCommand validator = validator(new FixedRequester(200), output);
        validator.targetDirectories = java.util.List.of("docs");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
        assertThat(output.stdout())
                .contains("Successfully parsed: guide.md")
                .contains("HTML:")
                .contains("VALIDATION_RESULT status=passed errors=0 documents_validated=1");
    }

    @Test
    void call_preservesRemoteFailureExitCodeAndReportShape() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("broken.md"), "[bad](https://example.test/missing)%n".formatted());
        Output output = new Output();
        MarkdownValidatorCommand validator = validator(new FixedRequester(404), output);
        validator.targetDirectories = java.util.List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
        assertThat(output.stdout())
                .contains("Remote link is not reachable: https://example.test/missing (HTTP 404)")
                .contains("MARKDOWN_VALIDATION_ISSUE")
                .contains("VALIDATION_RESULT status=failed errors=1 documents_validated=1");
    }

    private static MarkdownValidatorCommand validator(RemoteLinkRequester requester, Output output) {
        return new MarkdownValidatorCommand(requester, output.out(), output.err());
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

    private record FixedRequester(int statusCode) implements RemoteLinkRequester {

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            return new RemoteLinkResponse(statusCode);
        }
    }
}

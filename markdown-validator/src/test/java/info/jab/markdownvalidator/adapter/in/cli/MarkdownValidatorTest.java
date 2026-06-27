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
        MarkdownValidatorCommand validator = validator(new FixedRequester(200));

        new CommandLine(validator).parseArgs("--verbose", "--directories", "docs,skills", tempDir.toString());

        assertThat(validator.verbose).isTrue();
        assertThat(validator.targetDirectories).containsExactly("docs", "skills");
        assertThat(validator.rootDir).isEqualTo(tempDir.toString());

        MarkdownValidatorCommand defaultValidator = validator(new FixedRequester(200));

        new CommandLine(defaultValidator).parseArgs();

        assertThat(defaultValidator.targetDirectories).isEqualTo(MarkdownValidatorCommand.DEFAULT_TARGET_DIRECTORIES);
        assertThat(defaultValidator.rootDir).isEqualTo(".");
    }

    @Test
    void call_returnsFailureExitCodeWhenRootDoesNotExist() {
        MarkdownValidatorCommand validator = validator(new FixedRequester(200));
        String missingRoot = tempDir.resolve("missing").toString();

        int exitCode = new CommandLine(validator).execute(missingRoot);

        assertThat(exitCode).isEqualTo(1);
    }

    @Test
    void call_reportsMissingTargetDirectoriesInVerboseModeAndSucceedsWithNoFiles() {
        MarkdownValidatorCommand validator = validator(new FixedRequester(200));
        validator.targetDirectories = java.util.List.of("missing");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
    }

    @Test
    void call_parsesMarkdownRendersHtmlAndReturnsSuccess() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("guide.md"), "# Title%n%n[OK](https://example.test/ok)%n".formatted());
        MarkdownValidatorCommand validator = validator(new FixedRequester(200));
        validator.targetDirectories = java.util.List.of("docs");

        int exitCode = new CommandLine(validator).execute("--verbose", tempDir.toString());

        assertThat(exitCode).isZero();
    }

    @Test
    void call_keepsStandardOutputQuiet() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("guide.md"), "# Title%n%n[OK](https://example.test/ok)%n".formatted());
        MarkdownValidatorCommand validator = validator(new FixedRequester(200));
        validator.targetDirectories = java.util.List.of("docs");
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        int exitCode;
        try {
            System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
            exitCode = new CommandLine(validator).execute(tempDir.toString());
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        assertThat(exitCode).isZero();
        assertThat(stdout.toString(StandardCharsets.UTF_8)).isEmpty();
        assertThat(stderr.toString(StandardCharsets.UTF_8)).isEmpty();
    }

    @Test
    void call_preservesRemoteFailureExitCodeAndReportShape() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Files.writeString(docs.resolve("broken.md"), "[bad](https://example.test/missing)%n".formatted());
        MarkdownValidatorCommand validator = validator(new FixedRequester(404));
        validator.targetDirectories = java.util.List.of("docs");

        int exitCode = new CommandLine(validator).execute(tempDir.toString());

        assertThat(exitCode).isEqualTo(1);
    }

    private static MarkdownValidatorCommand validator(RemoteLinkRequester requester) {
        return new MarkdownValidatorCommand(requester);
    }

    private record FixedRequester(int statusCode) implements RemoteLinkRequester {

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            return new RemoteLinkResponse(statusCode);
        }
    }
}

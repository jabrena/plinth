package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.application.port.MarkdownFileFinder;
import info.jab.markdownvalidator.domain.FileValidationResult;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownValidationServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void validate_returnsRootMissingReportWhenRootDoesNotExist() throws IOException {
        RecordingRunner sequentialRunner = new RecordingRunner("sequential");
        MarkdownValidationService service = service(new FixedFinder(List.of()), sequentialRunner, new RecordingRunner("parallel"));

        ValidationReport report = service.validate(tempDir.resolve("missing"), List.of("docs"), false, false);

        assertThat(report.rootMissing()).isTrue();
        assertThat(sequentialRunner.wasCalled()).isFalse();
    }

    @Test
    void validate_returnsEmptyReportWhenNoMarkdownFilesExist() throws IOException {
        RecordingRunner sequentialRunner = new RecordingRunner("sequential");
        MarkdownValidationService service = service(new FixedFinder(List.of()), sequentialRunner, new RecordingRunner("parallel"));

        ValidationReport report = service.validate(tempDir, List.of("docs"), false, false);

        assertThat(report.noMarkdownFiles()).isTrue();
        assertThat(report.documentsValidated()).isZero();
        assertThat(sequentialRunner.wasCalled()).isFalse();
    }

    @Test
    void validate_usesSequentialRunnerWhenFailFastIsEnabled() throws IOException {
        Path file = Files.createFile(tempDir.resolve("a.md"));
        RecordingRunner sequentialRunner = new RecordingRunner("sequential");
        RecordingRunner parallelRunner = new RecordingRunner("parallel");
        MarkdownValidationService service = service(new FixedFinder(List.of(file)), sequentialRunner, parallelRunner);

        ValidationReport report = service.validate(tempDir, List.of("docs"), true, true);

        assertThat(report.documentsFound()).isEqualTo(1);
        assertThat(report.results()).extracting(result -> result.file()).containsExactly(Path.of("sequential.md"));
        assertThat(sequentialRunner.wasCalled()).isTrue();
        assertThat(parallelRunner.wasCalled()).isFalse();
    }

    @Test
    void validate_usesParallelRunnerByDefault() throws IOException {
        Path file = Files.createFile(tempDir.resolve("a.md"));
        RecordingRunner sequentialRunner = new RecordingRunner("sequential");
        RecordingRunner parallelRunner = new RecordingRunner("parallel");
        MarkdownValidationService service = service(new FixedFinder(List.of(file)), sequentialRunner, parallelRunner);

        ValidationReport report = service.validate(tempDir, List.of("docs"), false, false);

        assertThat(report.documentsFound()).isEqualTo(1);
        assertThat(report.results()).extracting(result -> result.file()).containsExactly(Path.of("parallel.md"));
        assertThat(sequentialRunner.wasCalled()).isFalse();
        assertThat(parallelRunner.wasCalled()).isTrue();
    }

    private static MarkdownValidationService service(
            MarkdownFileFinder finder, ValidationRunner sequentialRunner, ValidationRunner parallelRunner) {
        return new MarkdownValidationService(finder, sequentialRunner, parallelRunner);
    }

    private record FixedFinder(List<Path> files) implements MarkdownFileFinder {

        @Override
        public List<Path> findMarkdownFiles(Path root, List<String> targetDirectories, boolean verbose) {
            return files;
        }
    }

    private static final class RecordingRunner implements ValidationRunner {

        private final String name;
        private boolean called;

        private RecordingRunner(String name) {
            this.name = name;
        }

        @Override
        public List<FileValidationResult> validate(List<Path> markdownFiles, boolean verbose) {
            called = true;
            return List.of(FileValidationResult.failed(Path.of(name + ".md"), name, verbose));
        }

        private boolean wasCalled() {
            return called;
        }
    }
}

package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.application.port.MarkdownFileFinder;
import info.jab.markdownvalidator.domain.FileValidationResult;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class MarkdownValidationService {

    private final MarkdownFileFinder fileFinder;
    private final ValidationRunner sequentialRunner;
    private final ValidationRunner parallelRunner;

    public MarkdownValidationService(
            MarkdownFileFinder fileFinder,
            ValidationRunner sequentialRunner,
            ValidationRunner parallelRunner) {
        this.fileFinder = fileFinder;
        this.sequentialRunner = sequentialRunner;
        this.parallelRunner = parallelRunner;
    }

    public ValidationReport validate(Path root, List<String> targetDirectories, boolean failFast, boolean verbose)
            throws IOException {
        if (!Files.exists(root)) {
            return ValidationReport.rootMissing(root);
        }

        List<Path> markdownFiles = fileFinder.findMarkdownFiles(root, targetDirectories, verbose);
        if (markdownFiles.isEmpty()) {
            return ValidationReport.empty(root);
        }

        ValidationRunner runner = failFast ? sequentialRunner : parallelRunner;
        List<FileValidationResult> results = runner.validate(markdownFiles, verbose);
        return ValidationReport.completed(root, markdownFiles.size(), results);
    }
}

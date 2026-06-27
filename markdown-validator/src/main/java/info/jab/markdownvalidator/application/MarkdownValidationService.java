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
    private final StructuredValidationRunner validationRunner;

    public MarkdownValidationService(MarkdownFileFinder fileFinder, StructuredValidationRunner validationRunner) {
        this.fileFinder = fileFinder;
        this.validationRunner = validationRunner;
    }

    public ValidationReport validate(Path root, List<String> targetDirectories) throws IOException {
        if (!Files.exists(root)) {
            return ValidationReport.rootMissing(root);
        }

        List<Path> markdownFiles = fileFinder.findMarkdownFiles(root, targetDirectories);
        if (markdownFiles.isEmpty()) {
            return ValidationReport.empty(root);
        }

        List<FileValidationResult> results = validationRunner.validate(markdownFiles);
        return ValidationReport.completed(root, markdownFiles.size(), results);
    }
}

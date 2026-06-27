package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.domain.FileValidationResult;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SequentialValidationRunner implements ValidationRunner {

    private final MarkdownDocumentValidator documentValidator;

    public SequentialValidationRunner(MarkdownDocumentValidator documentValidator) {
        this.documentValidator = documentValidator;
    }

    @Override
    public List<FileValidationResult> validate(List<Path> markdownFiles, boolean verbose) {
        List<FileValidationResult> results = new ArrayList<>();
        for (Path file : markdownFiles) {
            FileValidationResult result = documentValidator.validate(file, verbose);
            results.add(result);
            if (!result.errors().isEmpty()) {
                break;
            }
        }
        return List.copyOf(results);
    }
}

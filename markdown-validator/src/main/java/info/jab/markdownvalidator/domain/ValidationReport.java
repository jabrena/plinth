package info.jab.markdownvalidator.domain;

import java.nio.file.Path;
import java.util.List;

public record ValidationReport(Path root, int documentsFound, List<FileValidationResult> results, boolean rootMissing) {

    public static ValidationReport rootMissing(Path root) {
        return new ValidationReport(root, 0, List.of(), true);
    }

    public static ValidationReport empty(Path root) {
        return new ValidationReport(root, 0, List.of(), false);
    }

    public static ValidationReport completed(Path root, int documentsFound, List<FileValidationResult> results) {
        return new ValidationReport(root, documentsFound, List.copyOf(results), false);
    }

    public boolean noMarkdownFiles() {
        return !rootMissing && documentsFound == 0;
    }

    public boolean passed() {
        return errors().isEmpty();
    }

    public List<ValidationError> errors() {
        return results.stream().flatMap(result -> result.errors().stream()).toList();
    }

    public int documentsValidated() {
        return results.size();
    }
}

package info.jab.mv.domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record FileValidationResult(Path file, List<String> messages, List<ValidationError> errors) {

    public static FileValidationResult of(Path file, ParseSummary summary, List<ValidationError> errors) {
        List<String> messages = baseMessages(file);
        if (errors.isEmpty()) {
            messages.add(String.format(
                    "Successfully parsed: %s (%d characters, HTML: %d characters)",
                    summary.fileName(),
                    summary.markdownCharacters(),
                    summary.htmlCharacters()));
        } else {
            errors.stream().map(FileValidationResult::errorMessage).forEach(messages::add);
        }
        return new FileValidationResult(file, List.copyOf(messages), List.copyOf(errors));
    }

    public static FileValidationResult failed(Path file, String message) {
        ValidationError error = new ValidationError(file, 0, message);
        List<String> messages = baseMessages(file);
        messages.add(errorMessage(error));
        return new FileValidationResult(file, List.copyOf(messages), List.of(error));
    }

    private static List<String> baseMessages(Path file) {
        List<String> messages = new ArrayList<>();
        messages.add(String.format("Validating: %s", file));
        return messages;
    }

    private static String errorMessage(ValidationError error) {
        return String.format("%s:%d - %s", error.file(), error.lineNumber(), error.message());
    }
}

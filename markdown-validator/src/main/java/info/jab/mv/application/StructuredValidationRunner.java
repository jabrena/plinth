package info.jab.mv.application;

import info.jab.mv.domain.FileValidationResult;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

public final class StructuredValidationRunner {

    private final MarkdownDocumentValidator documentValidator;

    public StructuredValidationRunner(MarkdownDocumentValidator documentValidator) {
        this.documentValidator = documentValidator;
    }

    public List<FileValidationResult> validate(List<Path> markdownFiles) {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<IndexedResult>allSuccessfulOrThrow(),
                config -> config.withName("markdown-validation"))) {
            for (int index = 0; index < markdownFiles.size(); index++) {
                int resultIndex = index;
                Path file = markdownFiles.get(index);
                scope.fork(() -> new IndexedResult(resultIndex, documentValidator.validate(file)));
            }
            Stream<StructuredTaskScope.Subtask<IndexedResult>> subtasks = scope.join();
            return subtasks.map(StructuredTaskScope.Subtask::get)
                    .sorted(Comparator.comparingInt(IndexedResult::index))
                    .map(IndexedResult::result)
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of(FileValidationResult.failed(Path.of("."), "Validation interrupted: " + describeThrowable(e)));
        } catch (RuntimeException e) {
            return List.of(
                    FileValidationResult.failed(Path.of("."), "Validation failed unexpectedly: " + describeThrowable(e)));
        }
    }

    private static String describeThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return message;
    }

    private record IndexedResult(int index, FileValidationResult result) {}
}

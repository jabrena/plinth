package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.domain.FileValidationResult;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StructuredValidationRunner {

    private static final int MAX_WORKERS = 8;

    private final MarkdownDocumentValidator documentValidator;

    public StructuredValidationRunner(MarkdownDocumentValidator documentValidator) {
        this.documentValidator = documentValidator;
    }

    public List<FileValidationResult> validate(List<Path> markdownFiles, boolean failFast, boolean verbose) {
        if (failFast) {
            return validateUntilFirstFailure(markdownFiles, verbose);
        }

        int workerCount = Math.min(MAX_WORKERS, markdownFiles.size());
        List<List<IndexedPath>> chunks = chunks(markdownFiles, workerCount);

        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<List<IndexedResult>>allSuccessfulOrThrow(),
                config -> config.withName("markdown-validation"))) {
            for (List<IndexedPath> chunk : chunks) {
                scope.fork(() -> validateChunk(chunk, verbose));
            }
            Stream<StructuredTaskScope.Subtask<List<IndexedResult>>> subtasks = scope.join();
            return subtasks.flatMap(subtask -> subtask.get().stream())
                    .sorted(Comparator.comparingInt(IndexedResult::index))
                    .map(IndexedResult::result)
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of(FileValidationResult.failed(
                    Path.of("."), "Validation interrupted: " + describeThrowable(e), verbose));
        } catch (RuntimeException e) {
            return List.of(FileValidationResult.failed(
                    Path.of("."), "Validation failed unexpectedly: " + describeThrowable(e), verbose));
        }
    }

    private List<FileValidationResult> validateUntilFirstFailure(List<Path> markdownFiles, boolean verbose) {
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

    private List<IndexedResult> validateChunk(List<IndexedPath> chunk, boolean verbose) {
        return chunk.stream()
                .map(indexedPath -> new IndexedResult(
                        indexedPath.index(), documentValidator.validate(indexedPath.path(), verbose)))
                .toList();
    }

    private static List<List<IndexedPath>> chunks(List<Path> markdownFiles, int workerCount) {
        List<List<IndexedPath>> chunks = new ArrayList<>();
        IntStream.range(0, workerCount).forEach(ignored -> chunks.add(new ArrayList<>()));
        for (int index = 0; index < markdownFiles.size(); index++) {
            chunks.get(index % workerCount).add(new IndexedPath(index, markdownFiles.get(index)));
        }
        return chunks;
    }

    private static String describeThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return message;
    }

    private record IndexedPath(int index, Path path) {}

    private record IndexedResult(int index, FileValidationResult result) {}
}

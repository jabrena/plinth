package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.domain.FileValidationResult;
import info.jab.markdownvalidator.domain.ParseSummary;
import info.jab.markdownvalidator.domain.ValidationError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public final class MarkdownDocumentValidator {

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    private final RemoteLinkValidator remoteLinkValidator;

    public MarkdownDocumentValidator(RemoteLinkValidator remoteLinkValidator) {
        this.remoteLinkValidator = remoteLinkValidator;
    }

    public FileValidationResult validate(Path file) {
        try {
            return validateContent(file, Files.readString(file));
        } catch (IOException e) {
            return FileValidationResult.failed(file, "Failed to read file: " + e.getMessage());
        }
    }

    private FileValidationResult validateContent(Path file, String content) {
        try {
            Node document = parser.parse(content);
            String html = renderer.render(document);

            if (html == null) {
                return FileValidationResult.failed(file, "HTML rendering produced null output");
            }

            ParseSummary summary = new ParseSummary(file.getFileName().toString(), content.length(), html.length());
            List<ValidationError> errors = validateRemoteLinks(file, document);
            return FileValidationResult.of(file, summary, errors);
        } catch (Exception e) {
            return FileValidationResult.failed(file, "Failed to parse markdown: " + e.getMessage());
        }
    }

    private List<ValidationError> validateRemoteLinks(Path file, Node document) {
        List<LinkDestination> links = remoteLinkDestinations(document);
        if (links.isEmpty()) {
            return List.of();
        }

        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.<IndexedValidationError>allSuccessfulOrThrow(),
                config -> config.withName("remote-link-validation"))) {
            for (LinkDestination link : links) {
                scope.fork(() -> new IndexedValidationError(
                        link.index(), remoteLinkValidator.validate(file, link.destination())));
            }

            Stream<StructuredTaskScope.Subtask<IndexedValidationError>> subtasks = scope.join();
            return subtasks.map(StructuredTaskScope.Subtask::get)
                    .sorted(Comparator.comparingInt(IndexedValidationError::index))
                    .flatMap(indexedError -> indexedError.error().stream())
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return List.of(new ValidationError(file, 0, "Remote link validation interrupted: " + describeThrowable(e)));
        } catch (RuntimeException e) {
            return List.of(new ValidationError(
                    file, 0, "Remote link validation failed unexpectedly: " + describeThrowable(e)));
        }
    }

    private static List<LinkDestination> remoteLinkDestinations(Node document) {
        List<LinkDestination> links = new ArrayList<>();
        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Link link) {
                links.add(new LinkDestination(links.size(), link.getDestination()));
                super.visit(link);
            }
        });
        return List.copyOf(links);
    }

    private static String describeThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return message;
    }

    private record LinkDestination(int index, String destination) {}

    private record IndexedValidationError(int index, Optional<ValidationError> error) {}
}

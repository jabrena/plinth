package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.domain.FileValidationResult;
import info.jab.markdownvalidator.domain.ParseSummary;
import info.jab.markdownvalidator.domain.ValidationError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
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

    public FileValidationResult validate(Path file, boolean verbose) {
        try {
            return validateContent(file, Files.readString(file), verbose);
        } catch (IOException e) {
            return FileValidationResult.failed(file, "Failed to read file: " + e.getMessage(), verbose);
        }
    }

    private FileValidationResult validateContent(Path file, String content, boolean verbose) {
        try {
            Node document = parser.parse(content);
            String html = renderer.render(document);

            if (html == null) {
                return FileValidationResult.failed(file, "HTML rendering produced null output", verbose);
            }

            ParseSummary summary = new ParseSummary(file.getFileName().toString(), content.length(), html.length());
            List<ValidationError> errors = validateRemoteLinks(file, document);
            return FileValidationResult.of(file, summary, errors, verbose);
        } catch (Exception e) {
            return FileValidationResult.failed(file, "Failed to parse markdown: " + e.getMessage(), verbose);
        }
    }

    private List<ValidationError> validateRemoteLinks(Path file, Node document) {
        List<ValidationError> errors = new ArrayList<>();
        document.accept(new AbstractVisitor() {
            @Override
            public void visit(Link link) {
                remoteLinkValidator.validate(file, link.getDestination()).ifPresent(errors::add);
                super.visit(link);
            }

            @Override
            public void visit(Image image) {
                remoteLinkValidator.validate(file, image.getDestination()).ifPresent(errors::add);
                super.visit(image);
            }
        });
        return List.copyOf(errors);
    }
}

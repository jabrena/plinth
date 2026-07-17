package info.jab.pml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/**
 * Renders a command XML document as Cursor slash-command Markdown
 * via {@code command-to-markdown.xsl} (XSLT 1.0), following the same pattern
 * as agent Markdown generation in {@code plinth-agents-generator}.
 */
public final class CommandMarkdownRenderer {

    static final String STYLESHEET_RESOURCE = "command-to-markdown.xsl";

    private CommandMarkdownRenderer() {}

    /**
     * Renders a command document to Cursor command Markdown.
     *
     * @param document parsed command XML (root element {@code command})
     * @return complete Markdown body
     */
    public static String render(Document document) {
        Objects.requireNonNull(document, "document");
        InputStream xslStream = CommandMarkdownRenderer.class.getClassLoader()
            .getResourceAsStream(STYLESHEET_RESOURCE);
        if (xslStream == null) {
            throw new IllegalStateException("XSLT stylesheet not found on classpath: " + STYLESHEET_RESOURCE);
        }
        try (xslStream) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslStream));
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to transform command XML to Markdown with " + STYLESHEET_RESOURCE,
                e
            );
        }
    }
}

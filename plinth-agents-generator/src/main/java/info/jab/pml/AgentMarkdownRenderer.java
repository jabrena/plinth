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
 * Renders a validated agent XML document as Cursor-compatible Markdown
 * via {@code agent-to-markdown.xsl} (XSLT 1.0), following the same pattern
 * as skill Markdown generation in {@code plinth-skills-generator}.
 *
 * <p>Frontmatter fields are emitted only from XML source values ({@code @id},
 * {@code metadata/description}, optional {@code metadata/model}, optional
 * {@code metadata/readonly}).
 */
public final class AgentMarkdownRenderer {

    static final String STYLESHEET_RESOURCE = "agent-to-markdown.xsl";

    private AgentMarkdownRenderer() {}

    /**
     * Renders an agent document to Cursor agent Markdown.
     *
     * @param document parsed agent XML (root element {@code agent})
     * @return complete Markdown including frontmatter
     */
    public static String render(Document document) {
        Objects.requireNonNull(document, "document");
        InputStream xslStream = AgentMarkdownRenderer.class.getClassLoader()
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
            throw new RuntimeException("Failed to transform agent XML to Markdown with " + STYLESHEET_RESOURCE, e);
        }
    }
}

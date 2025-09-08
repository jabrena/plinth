package info.jab.pml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Generator for Cursor Rules using XML/XSLT transformation.
 * <p>
 * This component provides a small, focused API to transform rule-definition XML
 * resources into rendered Markdown Cursor (MDC) content using a unified XSLT
 * stylesheet. The implementation emphasizes immutability and pure functions to
 * ensure predictable, repeatable transformations suitable for automated
 * documentation pipelines.
 */
public final class CursorRulesGenerator {

    // ===============================================================
    // PUBLIC API - Entry point for cursor rule generation
    // ===============================================================

    /**
     * Generates Cursor rules by transforming an XML resource with the provided XSLT stylesheet.
     * <p>
     * This overload does not perform schema validation. It only applies XInclude
     * resolution and XSLT transformation.
     *
     * @param xmlFileName the classpath-relative name of the XML rule definition to transform
     * @param xslFileName the classpath-relative name of the XSLT stylesheet used for transformation
     * @return the generated MDC content as a String (never {@code null})
     * @throws RuntimeException if resources cannot be loaded or the transformation fails
     */
    public String generate(String xmlFileName, String xslFileName) {
        return loadTransformationSources(xmlFileName, xslFileName)
            .map(sources -> createSaxSource(sources))
            .flatMap(saxSource -> performTransformation(saxSource, xslFileName))
            .orElseThrow(() -> new RuntimeException(
                "Failed to generate cursor rules for: " + xmlFileName + ", " + xslFileName));
    }

    // Removed legacy 3-argument generate method (schema validation no longer supported)

    // ===============================================================
    // PRIVATE METHODS - Organized in call order for readability
    // ===============================================================

    /**
     * Step 1: Loads XML and XSLT resources as a TransformationSources record.
     * Returns Optional to handle missing resources gracefully.
     */
    private Optional<TransformationSources> loadTransformationSources(String xmlFileName, String xslFileName) {
        return loadResource(xmlFileName)
            .flatMap(xmlStream -> loadResource(xslFileName)
                .map(xslStream -> new TransformationSources(xmlStream, xslStream)));
    }

    /**
     * Step 1a: Pure function to load a resource from classpath.
     * Used by loadTransformationSources and performTransformation.
     */
    private Optional<InputStream> loadResource(String fileName) {
        return Optional.ofNullable(
            getClass().getClassLoader().getResourceAsStream(fileName)
        );
    }

    /**
     * Step 2: Creates SAXSource with XInclude support.
     * Pure function that creates immutable SAXSource with XInclude processing.
     */
    private SAXSource createSaxSource(TransformationSources sources) {
        try {
            // First, process XInclude using DOM
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            domFactory.setXIncludeAware(true);

            DocumentBuilder builder = domFactory.newDocumentBuilder();

            // Set a proper base URI for XInclude resolution
            InputSource inputSource = new InputSource(sources.xmlStream());
            // Use the resource root path as the base URI for XInclude resolution
            // Point to the classes directory where resources are actually located
            String baseURI = getClass().getClassLoader().getResource("").toString();
            // Ensure we're pointing to the classes directory, not test-classes
            if (baseURI.contains("test-classes")) {
                baseURI = baseURI.replace("test-classes", "classes");
            }
            inputSource.setSystemId(baseURI);

            Document document = builder.parse(inputSource);

            // Convert DOM back to SAX source
            DOMSource domSource = new DOMSource(document);

            // Create SAX parser factory with namespace awareness
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);

            XMLReader xmlReader = factory.newSAXParser().getXMLReader();

            // Create transformer to convert DOM to SAX
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Create a ByteArrayOutputStream to hold the XML
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(baos);
            transformer.transform(domSource, result);

            // Convert back to InputSource
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            InputSource processedInputSource = new InputSource(bais);

            return new SAXSource(xmlReader, processedInputSource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SAX source with XInclude support", e);
        }
    }

    /**
     * Step 3: Performs the actual XSLT transformation.
     * Returns Optional to handle transformation failures gracefully.
     */
    private Optional<String> performTransformation(SAXSource xmlSource, String xslFileName) {
        return loadResource(xslFileName)
            .flatMap(xslStream -> executeTransformation(xmlSource, xslStream));
    }

    /**
     * Step 4: Executes the transformation and returns the result.
     * Encapsulates the transformation logic in a pure function.
     */
    private Optional<String> executeTransformation(SAXSource xmlSource, InputStream xslStream) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslStream));

            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);

            transformer.transform(xmlSource, result);

            return Optional.of(stringWriter.toString().trim());
        } catch (TransformerException e) {
            // Log the exception in a real application
            System.err.println("TransformerException in executeTransformation:");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // ===============================================================
    // SUPPORTING CLASSES - Used by the main processing pipeline
    // ===============================================================

    /**
     * Record for holding transformation sources - immutable data transfer (internal use only).
     * Used by loadTransformationSources to bundle XML and XSL streams together.
     */
    private record TransformationSources(InputStream xmlStream, InputStream xslStream) {
        private TransformationSources {
            if (Objects.isNull(xmlStream) || Objects.isNull(xslStream)) {
                throw new IllegalArgumentException("XML and XSL streams cannot be null");
            }
        }
    }

    // XSD schema validation has been intentionally removed; transformation strictly uses XSLT.
}

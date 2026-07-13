package info.jab.pml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
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
public final class SkillReferenceGenerator {

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
            .map(sources -> createSaxSource(sources, xmlFileName))
            .flatMap(saxSource -> performTransformation(saxSource, xslFileName, xmlFileName))
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
    private SAXSource createSaxSource(TransformationSources sources, String xmlFileName) {
        try {
            // First, process XInclude using DOM
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            domFactory.setXIncludeAware(true);

            DocumentBuilder builder = domFactory.newDocumentBuilder();

            // Set base URI from XML resource location so XInclude resolves relative to the XML's directory (or jar root).
            InputSource inputSource = new InputSource(sources.xmlStream());
            String baseURI = resolveBaseUri(xmlFileName);
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
     * Resolves base URI for XInclude from the XML resource URL.
     * Ensures fragments resolve correctly when SkillReferenceGenerator is used from another module (e.g. plinth-skills-generator).
     */
    private String resolveBaseUri(String xmlFileName) {
        URL xmlUrl = getClass().getClassLoader().getResource(xmlFileName);
        if (xmlUrl != null) {
            String urlStr = xmlUrl.toString();
            int lastSlash = urlStr.lastIndexOf('/');
            if (lastSlash > 0) {
                return urlStr.substring(0, lastSlash + 1);
            }
        }
        // Fallback: use classloader resource root
        URL rootUrl = getClass().getClassLoader().getResource("");
        String baseURI = rootUrl != null ? rootUrl.toString() : "";
        if (baseURI.contains("test-classes")) {
            baseURI = baseURI.replace("test-classes", "classes");
        }
        return baseURI;
    }

    /**
     * Step 3: Performs the actual XSLT transformation.
     * Returns Optional to handle transformation failures gracefully.
     */
    private Optional<String> performTransformation(SAXSource xmlSource, String xslFileName, String xmlFileName) {
        return loadResource(xslFileName)
            .flatMap(xslStream -> executeTransformation(xmlSource, xslStream, xmlFileName));
    }

    /**
     * Step 4: Executes the transformation and returns the result.
     * Encapsulates the transformation logic in a pure function.
     */
    private Optional<String> executeTransformation(SAXSource xmlSource, InputStream xslStream, String xmlFileName) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslStream));

            String ruleName = xmlFileName.replaceAll(".*/", "").replaceAll("\\.xml$", "");
            transformer.setParameter("ruleName", ruleName);

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

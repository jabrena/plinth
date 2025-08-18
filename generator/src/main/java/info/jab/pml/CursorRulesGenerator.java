package info.jab.pml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import javax.xml.XMLConstants;
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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
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
     * This overload does not perform XSD validation. For schema validation use
     * {@link #generate(String, String, String)} and supply an XSD resource name.
     *
     * @param xmlFileName the classpath-relative name of the XML rule definition to transform
     * @param xslFileName the classpath-relative name of the XSLT stylesheet used for transformation
     * @return the generated MDC content as a String (never {@code null})
     * @throws RuntimeException if resources cannot be loaded or the transformation fails
     */
    public String generate(String xmlFileName, String xslFileName) {
        return generate(xmlFileName, xslFileName, null);
    }

    /**
     * Generates Cursor rules by transforming an XML resource with the provided XSLT stylesheet,
     * optionally validating the XML against an XSD schema.
     * <p>
     * When {@code schemaFileName} is supplied, the XML is validated using XSD prior to
     * transformation. Any validation error will abort the process with a detailed exception.
     *
     * @param xmlFileName the classpath-relative name of the XML rule definition to transform
     * @param xslFileName the classpath-relative name of the XSLT stylesheet used for transformation
     * @param schemaFileName the classpath-relative name of the XSD schema used for validation; may be {@code null}
     * @return the generated MDC content as a String (never {@code null})
     * @throws RuntimeException if resources cannot be loaded, XSD validation fails, or the transformation fails
     */
    public String generate(String xmlFileName, String xslFileName, String schemaFileName) {
        return loadTransformationSources(xmlFileName, xslFileName)
            .map(sources -> createSaxSource(sources, schemaFileName))
            .flatMap(saxSource -> performTransformation(saxSource, xslFileName))
            .orElseThrow(() -> new RuntimeException(
                "Failed to generate cursor rules for: " + xmlFileName + ", " + xslFileName));
    }

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
     * Step 2: Creates SAXSource with XSD validation and XInclude support.
     * Pure function that creates immutable SAXSource with schema validation and XInclude processing.
     */
    private SAXSource createSaxSource(TransformationSources sources, String schemaFileName) {
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

            // Create SAX parser factory with namespace awareness and validation
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false); // We'll use schema validation instead

            // Load XSD schema - use explicit schema if provided, otherwise use fallback
            Optional<Schema> schema = loadXsdSchema(schemaFileName);

            if (schema.isPresent()) {
                factory.setSchema(schema.get());
            }

            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setErrorHandler(new ValidationErrorHandler());

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
            throw new RuntimeException("Failed to create SAX source with XSD validation and XInclude support", e);
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

    /**
     * Loads XSD schema by explicit filename.
     */
    private Optional<Schema> loadXsdSchema(String schemaFileName) {
        return loadResource(schemaFileName)
            .map(xsdStream -> {
                try {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    return schemaFactory.newSchema(new StreamSource(xsdStream));
                } catch (SAXException e) {
                    throw new RuntimeException("Failed to load XSD schema: " + schemaFileName, e);
                }
            });
    }

    /**
     * Custom ErrorHandler for XSD validation errors.
     * Provides better error reporting for validation issues.
     */
    private static final class ValidationErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            // Log warning in a real application
            System.err.println("XSD Validation Warning: " + exception.getMessage());
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException("XSD Validation Error: " + exception.getMessage(), exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException("XSD Validation Fatal Error: " + exception.getMessage(), exception);
        }
    }
}

package info.jab.pml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Generator for Cursor Rules using XML/XSLT transformation.
 * Follows functional programming principles with immutability and pure functions.
 */
public final class CursorRulesGenerator {

    // ===============================================================
    // PUBLIC API - Entry point for cursor rule generation
    // ===============================================================

    /**
     * Generates cursor rules by transforming XML with XSLT.
     */
    public String generate(String xmlFileName, String xslFileName) {
        return generate(xmlFileName, xslFileName, null);
    }

    /**
     * Generates cursor rules by transforming XML with XSLT.
     * Uses explicitly provided schema name.
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
     * Step 2: Creates SAXSource with XSD validation.
     * Pure function that creates immutable SAXSource with schema validation.
     */
    private SAXSource createSaxSource(TransformationSources sources, String schemaFileName) {
        try {
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

            return new SAXSource(xmlReader, new InputSource(sources.xmlStream()));
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException("Failed to create SAX source with XSD validation", e);
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

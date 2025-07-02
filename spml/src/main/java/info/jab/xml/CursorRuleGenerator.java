package info.jab.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Generator for Cursor Rules using XML/XSLT transformation.
 * Follows functional programming principles with immutability and pure functions.
 */
public final class CursorRuleGenerator {

    private static final String DTD_FILE_NAME = "system-prompt.dtd";

    // ===============================================================
    // PUBLIC API - Entry point for cursor rule generation
    // ===============================================================

    /**
     * Generates cursor rules by transforming XML with XSLT.
     * Pure function that depends only on input parameters.
     */
    public String generate(String xmlFileName, String xslFileName) {
        return loadTransformationSources(xmlFileName, xslFileName)
            .map(this::createSaxSource)
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
     * Step 2: Creates SAXSource with custom EntityResolver.
     * Pure function that creates immutable SAXSource using modern SAX API.
     */
    private SAXSource createSaxSource(TransformationSources sources) {
        try {
            // Modern approach: Use SAXParserFactory instead of deprecated XMLReaderFactory
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlReader.setEntityResolver(new ResourceEntityResolver());
            return new SAXSource(xmlReader, new InputSource(sources.xmlStream()));
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException("Failed to create SAX source with modern XMLReader API", e);
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
     * Custom EntityResolver as functional interface implementation.
     * Used by createSaxSource to resolve DTD references.
     */
    private static final class ResourceEntityResolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
            // Only handle system IDs we can resolve; return null for default SAX behavior otherwise
            return Optional.ofNullable(systemId)
                .filter(id -> id.endsWith(DTD_FILE_NAME))
                .flatMap(this::loadDtdFromClasspath)
                .orElse(null); // SAX contract: null means "use default resolution"
        }

        private Optional<InputSource> loadDtdFromClasspath(String systemId) {
            return Optional.ofNullable(
                getClass().getClassLoader().getResourceAsStream(DTD_FILE_NAME)
            ).map(dtdStream -> {
                InputSource inputSource = new InputSource(dtdStream);
                inputSource.setSystemId(systemId);
                return inputSource;
            });
        }
    }
}

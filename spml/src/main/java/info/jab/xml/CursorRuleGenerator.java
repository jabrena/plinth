package info.jab.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CursorRuleGenerator {

    // Custom EntityResolver to resolve DTD from classpath resources
    private static class ResourceEntityResolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            if (systemId != null && systemId.endsWith("system-prompt.dtd")) {
                InputStream dtdStream = getClass().getClassLoader().getResourceAsStream("system-prompt.dtd");
                if (dtdStream != null) {
                    InputSource inputSource = new InputSource(dtdStream);
                    inputSource.setSystemId(systemId);
                    return inputSource;
                }
            }
            //TODO Not return null
            return null; // Use default behavior for other entities
        }
    }

    public String generate(String xmlFileName, String xslFileName) {
        try {
            // Load XML and XSLT from resources
            InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            InputStream xslStream = getClass().getClassLoader().getResourceAsStream(xslFileName);

            if (Objects.isNull(xmlStream) || Objects.isNull(xslStream)) {
                throw new RuntimeException("Could not load XML or XSLT resources: " + xmlFileName + ", " + xslFileName);
            }

            //TODO not use deprecated methods
            // Create XMLReader with custom EntityResolver
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setEntityResolver(new ResourceEntityResolver());

            // Create SAXSource with custom XMLReader
            SAXSource xmlSource = new SAXSource(xmlReader, new InputSource(xmlStream));

            // Create transformer factory and transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslStream));

            // Prepare result
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);

            // Perform transformation
            transformer.transform(xmlSource, result);

            // Return the transformed content
            return stringWriter.toString().trim();

        } catch (Exception e) {
            throw new RuntimeException("Error during XML transformation", e);
        }
    }

}

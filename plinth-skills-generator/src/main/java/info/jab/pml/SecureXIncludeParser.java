package info.jab.pml;

import java.net.URI;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parses generator XML sources with XInclude support and hardened parser settings.
 * <p>
 * DOCTYPE declarations and external entities are rejected, and every resource requested
 * through {@code xi:include} must resolve inside the allowed resource root. This blocks
 * XXE payloads and XInclude references to arbitrary local files or remote URLs.
 */
final class SecureXIncludeParser {

    private static final String JAR_SEPARATOR = "!/";

    private SecureXIncludeParser() {
    }

    /**
     * Parses {@code source} resolving XInclude directives only within {@code allowedRootUri}.
     *
     * @param source the XML input; its system id is the base URI for relative includes
     * @param allowedRootUri URI prefix that every included resource must stay under
     * @return the parsed document with XInclude directives resolved
     */
    static Document parse(InputSource source, String allowedRootUri) throws Exception {
        Objects.requireNonNull(allowedRootUri, "allowedRootUri");
        String allowedRoot = normalize(allowedRootUri);
        if (allowedRoot.isEmpty()) {
            throw new IllegalArgumentException("allowedRootUri must not be empty");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(true);
        factory.setExpandEntityReferences(false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> {
            if (!isWithinRoot(systemId, allowedRoot)) {
                throw new SAXException("Blocked XML resource outside of " + allowedRoot + ": " + systemId);
            }
            return null;
        });
        return builder.parse(source);
    }

    static boolean isWithinRoot(String systemId, String allowedRoot) {
        if (systemId == null) {
            return false;
        }
        try {
            return normalize(systemId).startsWith(allowedRoot);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Normalizes {@code file:} and {@code jar:} URIs so {@code ..} segments cannot escape the root.
     */
    static String normalize(String uri) {
        int separator = uri.indexOf(JAR_SEPARATOR);
        if (uri.startsWith("jar:") && separator > 0) {
            String entryPath = URI.create("file:" + uri.substring(separator + 1)).normalize().getRawPath();
            return uri.substring(0, separator + 1) + entryPath;
        }
        return URI.create(uri).normalize().toString();
    }
}

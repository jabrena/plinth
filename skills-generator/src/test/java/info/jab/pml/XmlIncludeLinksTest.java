package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("XML XInclude Link Validation Tests")
class XmlIncludeLinksTest {

    private static Stream<String> provideXmlFilenames() {
        return SystemPromptsInventory.xmlFilenames();
    }

    @ParameterizedTest
    @MethodSource("provideXmlFilenames")
    @DisplayName("Should have no broken xi:include hrefs")
    void should_notHaveBrokenIncludes_when_xmlFileHasXiIncludes(String xmlFilename) throws Exception {
        InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(xmlFilename);
        assertThat(xmlStream)
            .withFailMessage("XML resource not found on classpath: %s", xmlFilename)
            .isNotNull();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlStream);

        NodeList includes = document.getElementsByTagNameNS("http://www.w3.org/2001/XInclude", "include");

        String baseDir = xmlFilename.contains("/")
            ? xmlFilename.substring(0, xmlFilename.lastIndexOf('/') + 1)
            : "";

        List<String> brokenLinks = new ArrayList<>();

        for (int i = 0; i < includes.getLength(); i++) {
            Element include = (Element) includes.item(i);
            String href = include.getAttribute("href");

            if (href != null && !href.isEmpty()) {
                String resolvedPath = baseDir + href;
                try (InputStream ref = getClass().getClassLoader().getResourceAsStream(resolvedPath)) {
                    if (ref == null) {
                        brokenLinks.add(href + " (resolved as: " + resolvedPath + ")");
                    }
                }
            }
        }

        assertThat(brokenLinks)
            .withFailMessage("XML file '%s' has broken xi:include links: %s", xmlFilename, brokenLinks)
            .isEmpty();
    }
}

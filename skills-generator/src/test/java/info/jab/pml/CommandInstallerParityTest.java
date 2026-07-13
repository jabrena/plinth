package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Installer Parity Tests")
class CommandInstallerParityTest {

    @Test
    @DisplayName("Command installer must include the exact command bundle")
    void should_includeExactCommands_when_commandInstallerUsesXIncludes() throws Exception {
        List<String> commandIncludes = readCommandIncludes("skill-references/004-commands-installation.xml");

        assertThat(commandIncludes)
            .containsExactlyElementsOf(CommandIndexes.commandFiles()
                .map(commandFile -> "assets/commands/" + commandFile)
                .toList());
    }

    private List<String> readCommandIncludes(String xmlResource) throws Exception {
        try (InputStream xmlStream = getTestResource(xmlResource)) {
            assertThat(xmlStream)
                .withFailMessage("XML resource not found on classpath: %s", xmlResource)
                .isNotNull();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlStream);
            NodeList includes = document.getElementsByTagNameNS("http://www.w3.org/2001/XInclude", "include");

            List<String> commandIncludes = new ArrayList<>();
            for (int i = 0; i < includes.getLength(); i++) {
                Element include = (Element) includes.item(i);
                String href = include.getAttribute("href");
                if (href.startsWith("assets/commands/")) {
                    commandIncludes.add(href);
                }
            }
            return commandIncludes;
        }
    }

    private InputStream getTestResource(String resourceName) {
        return CommandInstallerParityTest.class.getClassLoader().getResourceAsStream(resourceName);
    }
}

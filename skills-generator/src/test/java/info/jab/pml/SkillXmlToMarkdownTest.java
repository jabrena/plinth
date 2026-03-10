package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill XML to Markdown XSLT Tests")
class SkillXmlToMarkdownTest {

    private static final Logger logger = LoggerFactory.getLogger(SkillXmlToMarkdownTest.class);

    private static final String SKILL_XML_RESOURCE = "skills/110-skill.xml";
    private static final String XSLT_RESOURCE = "schemas/skill-to-markdown.xslt";
    private static final Path TARGET_OUTPUT = Paths.get("target", "skill-xml-to-markdown", "110-skill-generated.md");

    @Test
    @DisplayName("Should transform 110-skill.xml to markdown and write to target for review")
    void should_generateMarkdown_fromSkillXml() throws Exception {
        // Given
        Source xmlSource = streamSource(SKILL_XML_RESOURCE);
        Source xsltSource = streamSource(XSLT_RESOURCE);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);

        // When
        StringWriter writer = new StringWriter();
        transformer.transform(xmlSource, new StreamResult(writer));
        String markdown = writer.toString();

        // Then
        assertThat(markdown)
            .contains("---")
            .contains("name: 110-java-maven-best-practices")
            .contains("# Maven Best Practices")
            .contains("## Reference")
            .contains("For detailed guidance, examples, and constraints, see")
            .contains("[references/110-java-maven-best-practices.md](references/110-java-maven-best-practices.md)");

        // Write to target for manual review
        Files.createDirectories(TARGET_OUTPUT.getParent());
        Files.writeString(TARGET_OUTPUT, markdown, StandardCharsets.UTF_8);
        logger.info("Generated markdown saved to: {}", TARGET_OUTPUT.toAbsolutePath());
    }

    private StreamSource streamSource(String resourceName) throws IOException {
        InputStream stream = SkillXmlToMarkdownTest.class.getClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
        return new StreamSource(stream);
    }
}

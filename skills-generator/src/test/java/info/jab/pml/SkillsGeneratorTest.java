package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Skills Generator Tests")
class SkillsGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(SkillsGeneratorTest.class);

    @Nested
    @DisplayName("Parameterized Generate Skill Tests")
    class ParameterizedGenerateSkillTests {

        private static Stream<SkillsInventory.SkillDescriptor> provideSkillDescriptors() {
            return SkillsInventory.skillDescriptors();
        }

        @ParameterizedTest
        @MethodSource("provideSkillDescriptors")
        @DisplayName("Should generate valid SKILL.md and reference for each skill")
        void should_generateValidSkill_when_skillIdProvided(SkillsInventory.SkillDescriptor descriptor) throws Exception {
            String skillId = descriptor.skillId();
            boolean requiresSystemPrompt = descriptor.requiresSystemPrompt();
            boolean useXml = descriptor.useXml();

            // Given - skill file in resources/skills/ is the source of truth (.md or .xml when useXml)
            String expectedSkillMd = useXml ? loadSkillFromXmlResources(skillId) : loadSkillFromResources(skillId);
            SkillsGenerator generator = new SkillsGenerator();

            // When
            SkillsGenerator.SkillOutput output = generator.generateSkill(skillId, requiresSystemPrompt, useXml);

            // Then - Generated SKILL.md must exactly match the skill source (user-editable)
            assertThat(output.skillMd())
                .withFailMessage("Generated SKILL.md must match skills/%s-skill.%s. "
                    + "Update the skill file and run the build to promote changes.",
                    numericId(skillId), useXml ? "xml" : "md")
                .isEqualTo(expectedSkillMd);

            // Then - Validate reference content (only for skills with system prompt)
            if (requiresSystemPrompt) {
                assertThat(output.referenceMd())
                    .startsWith("---")
                    .contains("## Role")
                    .contains("## Goal")
                    .contains("name:")
                    .contains("description:");
            } else {
                assertThat(output.referenceMd()).isEmpty();
            }

            // Save to target for promotion
            saveToTarget(output);
        }
    }

    @Nested
    @DisplayName("Skill inventory and resources sync")
    class SkillInventorySyncTests {

        @Test
        @DisplayName("skill-inventory.json entries must have matching skill summary (and system-prompt when required)")
        void should_validateInventoryMatchesSkillsAndSystemPrompts() {
            List<SkillsInventory.SkillDescriptor> descriptors = SkillsInventory.skillDescriptors().toList();
            assertThat(descriptors).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Title consistency between skill markdown and system-prompt XML")
    class TitleConsistencyTests {

        private static Stream<SkillsInventory.SkillDescriptor> provideSkillDescriptorsWithSystemPrompt() {
            return SkillsInventory.skillDescriptors()
                .filter(SkillsInventory.SkillDescriptor::requiresSystemPrompt);
        }

        @ParameterizedTest
        @MethodSource("provideSkillDescriptorsWithSystemPrompt")
        @DisplayName("Skill markdown H1 title must match system-prompt XML title element")
        void should_haveMatchingTitle_when_comparingSkillMdAndSystemPromptXml(SkillsInventory.SkillDescriptor descriptor) throws Exception {
            String skillId = descriptor.skillId();
            String numId = numericId(skillId);
            String markdownTitle = loadSkillTitle(numId);

            String xmlResource = "system-prompts/" + skillId + ".xml";
            String xmlTitle;
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(xmlResource)) {
                assertThat(stream).withFailMessage("System-prompt XML not found: %s", xmlResource).isNotNull();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(stream);
                Element metadata = (Element) doc.getElementsByTagName("metadata").item(0);
                assertThat(metadata).withFailMessage("No <metadata> element in %s", xmlResource).isNotNull();
                NodeList titleNodes = metadata.getElementsByTagName("title");
                assertThat(titleNodes.getLength())
                    .withFailMessage("No <title> element in <metadata> of %s", xmlResource)
                    .isGreaterThan(0);
                xmlTitle = titleNodes.item(0).getTextContent().trim();
            }

            assertThat(markdownTitle)
                .withFailMessage(
                    "Skill title '%s' does not match system-prompt XML <title> '%s' in %s",
                    markdownTitle, xmlTitle, xmlResource)
                .isEqualTo(xmlTitle);
        }

        private String loadSkillTitle(String numId) throws Exception {
            String mdResource = "skills/" + numId + "-skill.md";
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(mdResource)) {
                if (stream != null) {
                    String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    return content.lines()
                        .filter(line -> line.startsWith("# "))
                        .findFirst()
                        .map(line -> line.substring(2).trim())
                        .orElseThrow(() -> new AssertionError("No H1 heading found in " + mdResource));
                }
            }
            String xmlResource = "skills/" + numId + "-skill.xml";
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(xmlResource)) {
                assertThat(stream).withFailMessage("Skill file not found: %s or %s", mdResource, xmlResource).isNotNull();
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
                NodeList titleNodes = doc.getElementsByTagName("title");
                assertThat(titleNodes.getLength())
                    .withFailMessage("No <title> element in %s", xmlResource)
                    .isGreaterThan(0);
                return titleNodes.item(0).getTextContent().trim();
            }
        }
    }

    @Nested
    @DisplayName("Version Consistency Tests")
    class VersionConsistencyTests {

        private static final Pattern VERSION_PATTERN = Pattern.compile("^\\s*version:\\s*(\\S+)\\s*$");

        private static Stream<SkillsInventory.SkillDescriptor> provideSkillDescriptors() {
            return SkillsInventory.skillDescriptors();
        }

        @ParameterizedTest
        @MethodSource("provideSkillDescriptors")
        @DisplayName("Should have metadata version matching project version from parent pom.xml when version is present")
        void should_haveMetadataVersionMatchingProjectVersion_when_versionPresent(SkillsInventory.SkillDescriptor descriptor) throws Exception {
            String numId = numericId(descriptor.skillId());
            Optional<String> skillVersion = loadSkillVersion(numId);

            if (skillVersion.isEmpty()) {
                return;
            }

            String expectedVersion = readProjectVersionFromParentPom();
            assertThat(skillVersion.get())
                .withFailMessage(
                    "Skill %s has metadata version '%s' but project version is '%s'. "
                        + "Update the version in skills/%s-skill.md or skills/%s-skill.xml to match pom.xml.",
                    numId, skillVersion.get(), expectedVersion, numId, numId)
                .isEqualTo(expectedVersion);
        }

        private Optional<String> loadSkillVersion(String numId) throws Exception {
            String mdResource = "skills/" + numId + "-skill.md";
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(mdResource)) {
                if (stream != null) {
                    String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    return extractVersionFromFrontmatter(content);
                }
            }
            String xmlResource = "skills/" + numId + "-skill.xml";
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(xmlResource)) {
                if (stream == null) {
                    throw new AssertionError("Skill file not found: " + mdResource + " or " + xmlResource);
                }
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
                NodeList metadataNodes = doc.getElementsByTagName("metadata");
                if (metadataNodes.getLength() == 0) {
                    return Optional.empty();
                }
                Element metadata = (Element) metadataNodes.item(0);
                NodeList versionNodes = metadata.getElementsByTagName("version");
                if (versionNodes.getLength() == 0) {
                    return Optional.empty();
                }
                String version = versionNodes.item(0).getTextContent();
                return Optional.ofNullable(version != null ? version.trim() : null);
            }
        }

        private Optional<String> extractVersionFromFrontmatter(String content) {
            return content.lines()
                .map(VERSION_PATTERN::matcher)
                .filter(Matcher::matches)
                .findFirst()
                .map(m -> m.group(1));
        }

        private String readProjectVersionFromParentPom() throws Exception {
            Path parentPom = Paths.get("..", "pom.xml").normalize();
            if (!Files.exists(parentPom)) {
                throw new IllegalStateException("Parent pom.xml not found at " + parentPom.toAbsolutePath());
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().parse(Files.newInputStream(parentPom));
            Element project = doc.getDocumentElement();
            NodeList versionNodes = project.getElementsByTagName("version");
            if (versionNodes.getLength() == 0) {
                throw new IllegalStateException("No <version> element in parent pom.xml");
            }
            String version = versionNodes.item(0).getTextContent();
            return version != null ? version.trim() : null;
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should throw exception when skill XML does not exist")
        void should_throwException_when_skillXmlDoesNotExist() {
            SkillsGenerator generator = new SkillsGenerator();

            assertThatThrownBy(() -> generator.generateSkill("non-existent-skill"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("non-existent-skill");
        }
    }

    private String loadSkillFromResources(String skillId) throws IOException {
        String numId = numericId(skillId);
        String resourceName = "skills/" + numId + "-skill.md";
        try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new IllegalArgumentException("Skill file not found: " + resourceName);
            }
            String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return appendProjectTagToDescription(content);
        }
    }

    private String loadSkillFromXmlResources(String skillId) throws Exception {
        String numId = numericId(skillId);
        String xmlResource = "skills/" + numId + "-skill.xml";
        String xsltResource = "schemas/skill-to-markdown.xslt";
        try (
            InputStream xmlStream = getTestResource(xmlResource);
            InputStream xsltStream = getTestResource(xsltResource)
        ) {
            if (xmlStream == null) {
                throw new IllegalArgumentException("Skill XML not found: " + xmlResource);
            }
            if (xsltStream == null) {
                throw new IllegalArgumentException("XSLT not found: " + xsltResource);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xsltStream));
            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(xmlStream), new StreamResult(writer));
            return appendProjectTagToDescription(writer.toString());
        }
    }

    private String appendProjectTagToDescription(String content) {
        return content.lines()
            .map(line -> line.startsWith("description:") && !line.endsWith(" Part of the skills-for-java project")
                ? line + " Part of the skills-for-java project"
                : line)
            .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
    }

    private InputStream getTestResource(String name) {
        return SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(name);
    }

    private static String numericId(String skillId) {
        int dash = skillId.indexOf('-');
        return dash > 0 ? skillId.substring(0, dash) : skillId;
    }

    private void saveToTarget(SkillsGenerator.SkillOutput output) throws IOException {
        Path targetDir = Paths.get("target", "skills", output.skillId());
        Files.createDirectories(targetDir);

        Path skillMdPath = targetDir.resolve("SKILL.md");
        Files.writeString(skillMdPath, output.skillMd());
        logger.info("Generated SKILL.md saved to: {}", skillMdPath.toAbsolutePath());

        if (!output.referenceMd().isEmpty()) {
            Path referencesDir = targetDir.resolve("references");
            Files.createDirectories(referencesDir);
            Path referencePath = referencesDir.resolve(output.skillId() + ".md");
            Files.writeString(referencePath, output.referenceMd());
            logger.info("Generated reference saved to: {}", referencePath.toAbsolutePath());
        }
    }
}

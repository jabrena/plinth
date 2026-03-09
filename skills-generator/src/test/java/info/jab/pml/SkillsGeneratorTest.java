package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
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
        void should_generateValidSkill_when_skillIdProvided(SkillsInventory.SkillDescriptor descriptor) throws IOException {
            String skillId = descriptor.skillId();
            boolean requiresSystemPrompt = descriptor.requiresSystemPrompt();

            // Given - skill file in resources/skills/ is the source of truth
            String expectedSkillMd = loadSkillFromResources(skillId);
            SkillsGenerator generator = new SkillsGenerator();

            // When
            SkillsGenerator.SkillOutput output = generator.generateSkill(skillId, requiresSystemPrompt);

            // Then - Generated SKILL.md must exactly match the skill source (user-editable)
            assertThat(output.skillMd())
                .withFailMessage("Generated SKILL.md must match skills/%s-skill.md. "
                    + "Update the skill file and run the build to promote changes.", numericId(skillId))
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
            String skillMdResource = "skills/" + numId + "-skill.md";
            String markdownTitle;
            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(skillMdResource)) {
                assertThat(stream).withFailMessage("Skill file not found: %s", skillMdResource).isNotNull();
                String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                markdownTitle = content.lines()
                    .filter(line -> line.startsWith("# "))
                    .findFirst()
                    .map(line -> line.substring(2).trim())
                    .orElseThrow(() -> new AssertionError("No H1 heading found in " + skillMdResource));
            }

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
                    "Skill markdown H1 '%s' in %s does not match XML <title> '%s' in %s",
                    markdownTitle, skillMdResource, xmlTitle, xmlResource)
                .isEqualTo(xmlTitle);
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
            String resourceName = "skills/" + numId + "-skill.md";

            try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(resourceName)) {
                assertThat(stream).withFailMessage("Skill file not found: %s", resourceName).isNotNull();
                String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                Optional<String> skillVersion = extractVersionFromFrontmatter(content);

                if (skillVersion.isEmpty()) {
                    return;
                }

                String expectedVersion = readProjectVersionFromParentPom();
                assertThat(skillVersion.get())
                    .withFailMessage(
                        "Skill %s has metadata version '%s' but project version is '%s'. "
                            + "Update the version in skills/%s-skill.md to match pom.xml.",
                        resourceName, skillVersion.get(), expectedVersion, numId)
                    .isEqualTo(expectedVersion);
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
            return content.lines()
                .map(line -> line.startsWith("description:") ? line + " Part of the skills-for-java project" : line)
                .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
        }
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

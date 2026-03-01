package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

        private static Stream<String> provideSkillIds() {
            return SkillsInventory.skillIds();
        }

        @ParameterizedTest
        @MethodSource("provideSkillIds")
        @DisplayName("Should generate valid SKILL.md and reference for each skill")
        void should_generateValidSkill_when_skillIdProvided(String skillId) throws IOException {
            // Given - skill file in resources/skills/ is the source of truth
            String expectedSkillMd = loadSkillFromResources(skillId);
            SkillsGenerator generator = new SkillsGenerator();

            // When
            SkillsGenerator.SkillOutput output = generator.generateSkill(skillId);

            // Then - Generated SKILL.md must exactly match the skill source (user-editable)
            assertThat(output.skillMd())
                .withFailMessage("Generated SKILL.md must match skills/%s-skill.md. "
                    + "Update the skill file and run the build to promote changes.", numericId(skillId))
                .isEqualTo(expectedSkillMd);

            // Then - Validate reference content
            assertThat(output.referenceMd())
                .startsWith("---")
                .contains("## Role")
                .contains("## Goal");

            assertThat(output.referenceMd())
                .contains("name:")
                .contains("description:");

            // Save to target for promotion
            saveToTarget(output);
        }
    }

    @Nested
    @DisplayName("Skill inventory and resources sync")
    class SkillInventorySyncTests {

        @Test
        @DisplayName("skill-inventory.json entries must have matching skill summary and system-prompt")
        void should_validateInventoryMatchesSkillsAndSystemPrompts() {
            // skillIds() validates each entry has skills/{id}-skill.md and system-prompt with prefix {id}-
            List<String> skillIds = SkillsInventory.skillIds().toList();
            assertThat(skillIds).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Title consistency between skill markdown and system-prompt XML")
    class TitleConsistencyTests {

        private static Stream<String> provideSkillIds() {
            return SkillsInventory.skillIds();
        }

        @ParameterizedTest
        @MethodSource("provideSkillIds")
        @DisplayName("Skill markdown H1 title must match system-prompt XML title element")
        void should_haveMatchingTitle_when_comparingSkillMdAndSystemPromptXml(String skillId) throws Exception {
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
        Path referencesDir = targetDir.resolve("references");
        Files.createDirectories(referencesDir);

        Path skillMdPath = targetDir.resolve("SKILL.md");
        Files.writeString(skillMdPath, output.skillMd());
        logger.info("Generated SKILL.md saved to: {}", skillMdPath.toAbsolutePath());

        Path referencePath = referencesDir.resolve(output.skillId() + ".md");
        Files.writeString(referencePath, output.referenceMd());
        logger.info("Generated reference saved to: {}", referencePath.toAbsolutePath());
    }
}

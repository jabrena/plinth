package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // Given - skill-summary in resources/skills/ is the source of truth
            String expectedSkillSummary = loadSkillSummaryFromResources(skillId);
            SkillsGenerator generator = new SkillsGenerator();

            // When
            SkillsGenerator.SkillOutput output = generator.generateSkill(skillId);

            // Then - Generated SKILL.md must exactly match the skill-summary source (user-editable)
            assertThat(output.skillMd())
                .withFailMessage("Generated SKILL.md must match skills/%s-skill-summary.md. "
                    + "Update the skill-summary file and run the build to promote changes.", skillId)
                .isEqualTo(expectedSkillSummary);

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
    @DisplayName("SkillsInventory and skills directory sync")
    class SkillsInventorySyncTests {

        @Test
        @DisplayName("SkillsInventory must have matching skill-summary file for each skill")
        void should_haveMatchingSkillSummary_forEachSkillInInventory() {
            List<String> skillIds = SkillsInventory.skillIds().toList();
            assertThat(skillIds).isNotEmpty();

            for (String skillId : skillIds) {
                String resourceName = "skills/" + skillId + "-skill-summary.md";
                try (InputStream stream = SkillsGeneratorTest.class.getClassLoader()
                    .getResourceAsStream(resourceName)) {
                    assertThat(stream)
                        .withFailMessage("SkillsInventory contains '%s' but skills/%s-skill-summary.md not found. "
                            + "Add the skill-summary file for each skill in the inventory.", skillId, skillId)
                        .isNotNull();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to verify skill-summary for " + skillId, e);
                }
            }
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

    private String loadSkillSummaryFromResources(String skillId) throws IOException {
        String resourceName = "skills/" + skillId + "-skill-summary.md";
        try (InputStream stream = SkillsGeneratorTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new IllegalArgumentException("Skill-summary not found: " + resourceName);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
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

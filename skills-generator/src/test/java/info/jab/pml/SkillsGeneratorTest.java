package info.jab.pml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            // Given
            SkillsGenerator generator = new SkillsGenerator();

            // When
            SkillsGenerator.SkillOutput output = generator.generateSkill(skillId);

            // Then - Validate SKILL.md
            assertThat(output.skillMd())
                .contains("---")
                .contains("author: Juan Antonio Breña Moral")
                .contains("version: 0.12.0-SNAPSHOT")
                .contains("name: " + skillId)
                .contains("## Reference")
                .contains("[references/" + skillId + ".md](references/" + skillId + ".md)");

            String[] skillLines = output.skillMd().split("\n");
            boolean hasMainHeading = Stream.of(skillLines).anyMatch(line -> line.startsWith("# ") && !line.startsWith("## "));
            assertThat(hasMainHeading)
                .withFailMessage("SKILL.md should have a main title (# heading)")
                .isTrue();

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

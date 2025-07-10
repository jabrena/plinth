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

@DisplayName("Cursor Rules Generator Tests")
class CursorRulesGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(CursorRulesGeneratorTest.class);

    @Nested
    @DisplayName("Parameterized Generate Method Tests")
    class ParameterizedGenerateMethodTests {

        @Test
        @DisplayName("Should throw exception when XML file does not exist")
        void should_throwException_when_xmlFileDoesNotExist() {
            // Given
            CursorRulesGenerator generator = new CursorRulesGenerator();

            // When & Then - Updated for functional API exception handling
            assertThatThrownBy(() -> generator.generate("non-existent.xml", "cursor-rules.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to generate cursor rules for")
                .hasMessageContaining("non-existent.xml")
                .hasMessageContaining("cursor-rules.xsl");
        }

        @Test
        @DisplayName("Should throw exception when XSLT file does not exist")
        void should_throwException_when_xsltFileDoesNotExist() {
            // Given
            CursorRulesGenerator generator = new CursorRulesGenerator();

            // When & Then - Updated for functional API exception handling
            assertThatThrownBy(() -> generator.generate("112-java-maven-documentation.xml", "non-existent.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to generate cursor rules for")
                .hasMessageContaining("112-java-maven-documentation.xml")
                .hasMessageContaining("non-existent.xsl");
        }
    }

    @Nested
    @DisplayName("Unified XSLT Generator Tests")
    class UnifiedXsltGeneratorTests {

        /**
         * Provides the base file names for parameterized tests.
         * Each base name corresponds to both an XML file and expected MDC file.
         */
        private static Stream<String> provideXmlFileNames() {
            return Stream.of(
                "100-java-checklist-guide",
                "110-java-maven-best-practices",
                "112-java-maven-documentation",
                "121-java-object-oriented-design",
                "122-java-type-design",
                "123-java-general-guidelines",
                "124-java-secure-coding",
                "125-java-concurrency",
                "126-java-logging",
                "131-java-unit-testing",
                "141-java-refactoring-with-modern-features",
                "142-java-functional-programming",
                "143-java-data-oriented-programming"
            );
        }

        @ParameterizedTest
        @MethodSource("provideXmlFileNames")
        @DisplayName("Should validate semantic structure of generated MDC files")
        void should_validateSemanticStructure_when_generatingMdcFiles(String baseFileName) throws IOException {
            // Given
            CursorRulesGenerator generator = new CursorRulesGenerator();

            // When - Generate content on-the-fly
            String generatedContent = generator.generate(baseFileName + ".xml", "cursor-rules.xsl", "pml.xsd");

            // Save generated content to target for inspection
            saveGeneratedContentToTarget(generatedContent, baseFileName + ".mdc");

            // Then - Validate the generated content structure
            String[] lines = generatedContent.split("\\n");
            validateHasMainTitle(lines, baseFileName);
            validateRequiredSections(lines, baseFileName);
            validateHeadingFormatting(lines, baseFileName);
            validateFrontmatterStructure(lines, baseFileName);
        }

        /**
         * Validates that the MDC file has a main title (# heading).
         */
        private void validateHasMainTitle(String[] lines, String baseFileName) {
            boolean hasMainTitle = false;
            for (String line : lines) {
                if (line.startsWith("# ") && !line.startsWith("## ")) {
                    hasMainTitle = true;
                    break;
                }
            }
            assertThat(hasMainTitle)
                .withFailMessage("MDC file %s.mdc should have a main title (# heading)", baseFileName)
                .isTrue();
        }

        /**
         * Validates that required sections are present in the MDC file.
         */
        private void validateRequiredSections(String[] lines, String baseFileName) {
            boolean hasRole = false;
            boolean hasGoal = false;

            for (String line : lines) {
                if (line.equals("## Role")) {
                    hasRole = true;
                } else if (line.equals("## Goal")) {
                    hasGoal = true;
                }
            }

            assertThat(hasRole)
                .withFailMessage("MDC file %s.mdc should have a ## Role section", baseFileName)
                .isTrue();

            assertThat(hasGoal)
                .withFailMessage("MDC file %s.mdc should have a ## Goal section", baseFileName)
                .isTrue();
        }

        /**
         * Validates that every ## heading has a blank line before it (except the first one).
         */
        private void validateHeadingFormatting(String[] lines, String baseFileName) {
            for (int i = 1; i < lines.length; i++) {
                String currentLine = lines[i];
                String previousLine = lines[i - 1];

                // Check if current line is a ## heading
                if (currentLine.startsWith("## ")) {
                    // Previous line should be empty (blank line) or be the frontmatter end
                    assertThat(previousLine.trim().isEmpty() || previousLine.equals("---"))
                        .withFailMessage("MDC file %s.mdc: ## heading '%s' at line %d should have a blank line before it",
                                       baseFileName, currentLine, i + 1)
                        .isTrue();
                }
            }
        }

        /**
         * Validates that the MDC file has proper frontmatter structure.
         */
        private void validateFrontmatterStructure(String[] lines, String baseFileName) {
            // Should start with frontmatter
            assertThat(lines[0])
                .withFailMessage("MDC file %s.mdc should start with frontmatter (---)", baseFileName)
                .isEqualTo("---");

            // Should have closing frontmatter
            boolean hasFrontmatterEnd = false;
            for (int i = 1; i < Math.min(10, lines.length); i++) { // Check first 10 lines
                if (lines[i].equals("---")) {
                    hasFrontmatterEnd = true;
                    break;
                }
            }

            assertThat(hasFrontmatterEnd)
                .withFailMessage("MDC file %s.mdc should have closing frontmatter (---)", baseFileName)
                .isTrue();
        }

        /**
         * Pure function to save generated content to target directory.
         * Follows functional programming principles with clear input/output relationship.
         */
        private void saveGeneratedContentToTarget(String content, String filename) throws IOException {
            Path targetDir = Paths.get("target");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path outputPath = targetDir.resolve(filename);
            Files.writeString(outputPath, content);
            logger.info("Generated content saved to: {}", outputPath.toAbsolutePath());
        }

    }
}

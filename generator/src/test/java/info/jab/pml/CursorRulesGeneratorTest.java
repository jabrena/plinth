package info.jab.pml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            return SystemPromptsInventory.baseNames();
        }

        @ParameterizedTest
        @MethodSource("provideXmlFileNames")
        @DisplayName("Should validate semantic structure of generated MDC files")
        void should_validateSemanticStructure_when_generatingMdcFiles(String baseFileName) throws IOException {
            // Given
            CursorRulesGenerator generator = new CursorRulesGenerator();

            // When - Generate content (no schema validation)
            String generatedContent = generator.generate(
                baseFileName + ".xml",
                "cursor-rules.xsl"
            );

            // Save generated content to target for inspection
            saveGeneratedContentToTarget(generatedContent, baseFileName + ".md");

            // Then - Validate the generated content structure
            String[] lines = generatedContent.split("\\n");

            validateHasMainTitle(lines, baseFileName);
            validateRequiredSections(lines, baseFileName);
            validateHeadingFormatting(lines, baseFileName);
            validateFrontmatterStructure(lines, baseFileName);
            validateExamplesStructure(lines, baseFileName);
            validateCodeBlockFormatting(lines, baseFileName);
            validateOutputFormatSection(lines, baseFileName);
            validateSafeguardsSection(lines, baseFileName);
            validateExampleNumberingConsistency(lines, baseFileName);

        }

        /**
         * Validates that the MDC file has a main title (# heading).
         */
        private void validateHasMainTitle(String[] lines, String baseFileName) {
            boolean hasMainTitle = Stream.of(lines)
                .anyMatch(line -> line.startsWith("# ") && !line.startsWith("## "));

            assertThat(hasMainTitle)
                .withFailMessage("MDC file %s.mdc should have a main title (# heading)", baseFileName)
                .isTrue();
        }

        /**
         * Validates that required sections are present in the MDC file.
         */
        private void validateRequiredSections(String[] lines, String baseFileName) {
            boolean hasRole = Stream.of(lines)
                .anyMatch(line -> line.equals("## Role"));

            boolean hasGoal = Stream.of(lines)
                .anyMatch(line -> line.equals("## Goal"));

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
            Stream.iterate(1, i -> i < lines.length, i -> i + 1)
                .filter(i -> lines[i].startsWith("## "))
                .forEach(i -> {
                    String currentLine = lines[i];
                    String previousLine = lines[i - 1];

                    // Previous line should be empty (blank line) or be the frontmatter end
                    assertThat(previousLine.trim().isEmpty() || previousLine.equals("---"))
                        .withFailMessage("MDC file %s.mdc: ## heading '%s' at line %d should have a blank line before it",
                                       baseFileName, currentLine, i + 1)
                        .isTrue();
                });
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
            boolean hasFrontmatterEnd = Stream.iterate(1, i -> i < Math.min(10, lines.length), i -> i + 1)
                .anyMatch(i -> lines[i].equals("---"));

            assertThat(hasFrontmatterEnd)
                .withFailMessage("MDC file %s.mdc should have closing frontmatter (---)", baseFileName)
                .isTrue();
        }

        /**
         * Validates that the Examples section has proper structure when present.
         */
        private void validateExamplesStructure(String[] lines, String baseFileName) {
            boolean hasExamplesSection = Stream.of(lines)
                .anyMatch(line -> line.equals("## Examples"));

            if (hasExamplesSection) {
                // Should have table of contents
                boolean hasTableOfContents = Stream.of(lines)
                    .anyMatch(line -> line.equals("### Table of contents"));

                assertThat(hasTableOfContents)
                    .withFailMessage("MDC file %s.mdc with Examples section should have a Table of contents", baseFileName)
                    .isTrue();

                // Should have at least one example
                boolean hasExampleHeading = Stream.of(lines)
                    .anyMatch(line -> line.matches("^### Example \\d+:.*"));

                assertThat(hasExampleHeading)
                    .withFailMessage("MDC file %s.mdc should have at least one example with proper heading format", baseFileName)
                    .isTrue();

                // Each example should have Title and Description
                validateExampleTitleDescriptionPattern(lines, baseFileName);
            }
        }

        /**
         * Validates that examples follow the Title/Description pattern.
         */
        private void validateExampleTitleDescriptionPattern(String[] lines, String baseFileName) {
            for (int i = 0; i < lines.length - 2; i++) {
                if (lines[i].matches("^### Example \\d+:.*")) {
                    // Next non-empty line should start with "Title:"
                    int nextLineIndex = i + 1;
                    while (nextLineIndex < lines.length && lines[nextLineIndex].trim().isEmpty()) {
                        nextLineIndex++;
                    }

                    if (nextLineIndex < lines.length) {
                        assertThat(lines[nextLineIndex])
                            .withFailMessage("MDC file %s.mdc: Example at line %d should be followed by 'Title:' line",
                                           baseFileName, i + 1)
                            .startsWith("Title:");

                        // Find next non-empty line after Title, should be Description
                        int descLineIndex = nextLineIndex + 1;
                        while (descLineIndex < lines.length && lines[descLineIndex].trim().isEmpty()) {
                            descLineIndex++;
                        }

                        if (descLineIndex < lines.length) {
                            assertThat(lines[descLineIndex])
                                .withFailMessage("MDC file %s.mdc: Example at line %d should have 'Description:' after Title",
                                               baseFileName, i + 1)
                                .startsWith("Description:");
                        }
                    }
                }
            }
        }

        /**
         * Validates proper code block formatting with language specification.
         */
        private void validateCodeBlockFormatting(String[] lines, String baseFileName) {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];

                // Check for code block start
                if (line.startsWith("```") && line.length() > 3) {
                    String language = line.substring(3);

                    // Should specify a language (not empty)
                    assertThat(language.trim())
                        .withFailMessage("MDC file %s.mdc: Code block at line %d should specify a language",
                                       baseFileName, i + 1)
                        .isNotEmpty();

                    // Find corresponding closing ```
                    boolean hasClosing = false;
                    for (int j = i + 1; j < lines.length; j++) {
                        if (lines[j].equals("```")) {
                            hasClosing = true;
                            break;
                        }
                    }

                    assertThat(hasClosing)
                        .withFailMessage("MDC file %s.mdc: Code block at line %d is not properly closed",
                                       baseFileName, i + 1)
                        .isTrue();
                }
            }

            // Validate Good/Bad example pattern
            validateGoodBadExamplePattern(lines, baseFileName);
        }

        /**
         * Validates that examples follow the Good/Bad example pattern.
         */
        private void validateGoodBadExamplePattern(String[] lines, String baseFileName) {
            boolean hasGoodExample = Stream.of(lines)
                .anyMatch(line -> line.equals("**Good example:**"));
            boolean hasBadExample = Stream.of(lines)
                .anyMatch(line -> line.equals("**Bad example:**"));

            // If there are examples, they should have both good and bad
            boolean hasExamples = Stream.of(lines)
                .anyMatch(line -> line.matches("^### Example \\d+:.*"));

            if (hasExamples) {
                assertThat(hasGoodExample)
                    .withFailMessage("MDC file %s.mdc with examples should have at least one 'Good example:'", baseFileName)
                    .isTrue();

                assertThat(hasBadExample)
                    .withFailMessage("MDC file %s.mdc with examples should have at least one 'Bad example:'", baseFileName)
                    .isTrue();
            }
        }

        /**
         * Validates the Output Format section when present.
         */
        private void validateOutputFormatSection(String[] lines, String baseFileName) {
            boolean hasOutputFormat = Stream.of(lines)
                .anyMatch(line -> line.equals("## Output Format"));

            if (hasOutputFormat) {
                // Should have bullet points after the heading
                boolean hasOutputFormatItems = false;
                boolean foundSection = false;

                for (String line : lines) {
                    if (line.equals("## Output Format")) {
                        foundSection = true;
                        continue;
                    }
                    if (foundSection) {
                        if (line.startsWith("## ")) {
                            // Reached next section
                            break;
                        }
                        if (line.startsWith("- ")) {
                            hasOutputFormatItems = true;
                            break;
                        }
                    }
                }

                assertThat(hasOutputFormatItems)
                    .withFailMessage("MDC file %s.mdc Output Format section should contain bullet point items", baseFileName)
                    .isTrue();
            }
        }

        /**
         * Validates the Safeguards section when present.
         */
        private void validateSafeguardsSection(String[] lines, String baseFileName) {
            boolean hasSafeguards = Stream.of(lines)
                .anyMatch(line -> line.equals("## Safeguards"));

            if (hasSafeguards) {
                // Should have bullet points after the heading
                boolean hasSafeguardItems = false;
                boolean foundSection = false;

                for (String line : lines) {
                    if (line.equals("## Safeguards")) {
                        foundSection = true;
                        continue;
                    }
                    if (foundSection) {
                        if (line.startsWith("## ")) {
                            // Reached next section
                            break;
                        }
                        if (line.startsWith("- ")) {
                            hasSafeguardItems = true;
                            break;
                        }
                    }
                }

                assertThat(hasSafeguardItems)
                    .withFailMessage("MDC file %s.mdc Safeguards section should contain bullet point items", baseFileName)
                    .isTrue();

                // For Maven-related rules, should contain Maven commands
                if (baseFileName.contains("maven")) {
                    boolean hasMavenCommand = Stream.of(lines)
                        .anyMatch(line -> line.contains("mvn") || line.contains("./mvnw"));

                    assertThat(hasMavenCommand)
                        .withFailMessage("MDC file %s.mdc Maven-related rule should contain Maven commands in Safeguards", baseFileName)
                        .isTrue();
                }
            }
        }

        /**
         * Determines if a rule is a template generation rule rather than a code analysis rule.
         */
        private boolean isTemplateGenerationRule(String content, String baseFileName) {
            // Check for template generation indicators
            boolean hasTemplateReference = content.contains("embedded template") ||
                                         content.contains("template EXACTLY") ||
                                         content.contains("markdown file named");

            boolean isChecklistGuide = baseFileName.contains("checklist-guide");
            boolean isDocumentationRule = baseFileName.contains("documentation");

            return hasTemplateReference || isChecklistGuide || isDocumentationRule;
        }

        /**
         * Validates that example numbering is consistent and sequential.
         */
        private void validateExampleNumberingConsistency(String[] lines, String baseFileName) {
            List<Integer> exampleNumbers = new ArrayList<>();

            for (String line : lines) {
                if (line.matches("^### Example \\d+:.*")) {
                    String numberStr = line.replaceAll("^### Example (\\d+):.*", "$1");
                    try {
                        exampleNumbers.add(Integer.parseInt(numberStr));
                    } catch (NumberFormatException e) {
                        // Skip invalid numbers
                    }
                }
            }

            if (!exampleNumbers.isEmpty()) {
                // Should start with 1
                assertThat(exampleNumbers.get(0))
                    .withFailMessage("MDC file %s.mdc: First example should be numbered 1", baseFileName)
                    .isEqualTo(1);

                // Should be sequential
                for (int i = 1; i < exampleNumbers.size(); i++) {
                    assertThat(exampleNumbers.get(i))
                        .withFailMessage("MDC file %s.mdc: Example numbers should be sequential, found gap after %d",
                                       baseFileName, exampleNumbers.get(i-1))
                        .isEqualTo(exampleNumbers.get(i-1) + 1);
                }

                // Table of contents should match example numbers
                validateTableOfContentsMatchesExamples(lines, baseFileName, exampleNumbers);
            }
        }

        /**
         * Validates that table of contents entries match actual examples.
         */
        private void validateTableOfContentsMatchesExamples(String[] lines, String baseFileName, List<Integer> exampleNumbers) {
            List<Integer> tocNumbers = new ArrayList<>();

            boolean inToc = false;
            for (String line : lines) {
                if (line.equals("### Table of contents")) {
                    inToc = true;
                    continue;
                }
                if (inToc && line.startsWith("### ")) {
                    // End of TOC
                    break;
                }
                if (inToc && line.matches("^- Example \\d+:.*")) {
                    String numberStr = line.replaceAll("^- Example (\\d+):.*", "$1");
                    try {
                        tocNumbers.add(Integer.parseInt(numberStr));
                    } catch (NumberFormatException e) {
                        // Skip invalid numbers
                    }
                }
            }

            assertThat(tocNumbers)
                .withFailMessage("MDC file %s.mdc: Table of contents should match actual example numbers", baseFileName)
                .isEqualTo(exampleNumbers);
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

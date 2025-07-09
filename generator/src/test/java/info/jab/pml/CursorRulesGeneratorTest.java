package info.jab.pml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
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

        @ParameterizedTest
        @MethodSource("provideXmlFileNames")
        @DisplayName("Should generate exact content matching original expected document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingWithUnifiedXslt(String baseFileName) throws IOException {
            // Given
            CursorRulesGenerator generator = new CursorRulesGenerator();
            String expectedContent = loadExpectedContent(baseFileName + ".mdc");

            // When
            String actualResult = generator.generate(baseFileName + ".xml", "cursor-rules.xsl", "pml.xsd");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

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

        /**
         * Pure function to load expected content from resources.
         * Uses Optional for null safety following functional programming principles.
         */
        private String loadExpectedContent(String filename) throws IOException {
            return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(filename))
                .map(inputStream -> {
                    try (inputStream) {
                        return new String(inputStream.readAllBytes()).trim();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read resource: " + filename, e);
                    }
                })
                .orElseThrow(() -> new IOException("Resource not found: " + filename));
        }
    }

    @Test
    @DisplayName("Should produce consistent content structure regardless of XML content type")
    void should_produceConsistentStructure_when_processingDifferentXmlTypes() throws IOException {
        // Given
        CursorRulesGenerator generator = new CursorRulesGenerator();

        // When
        String checklistGuideResult = generator.generate("100-java-checklist-guide.xml", "cursor-rules.xsl", "pml.xsd");
        String bestPracticesResult = generator.generate("110-java-maven-best-practices.xml", "cursor-rules.xsl", "pml.xsd");
        String documentationResult = generator.generate("112-java-maven-documentation.xml", "cursor-rules.xsl", "pml.xsd");
        String objectOrientedDesignResult = generator.generate("121-java-object-oriented-design.xml", "cursor-rules.xsl", "pml.xsd");
        String typeDesignResult = generator.generate("122-java-type-design.xml", "cursor-rules.xsl", "pml.xsd");
        String generalGuidelinesResult = generator.generate("123-java-general-guidelines.xml", "cursor-rules.xsl", "pml.xsd");
        String secureCodingResult = generator.generate("124-java-secure-coding.xml", "cursor-rules.xsl", "pml.xsd");
        String concurrencyResult = generator.generate("125-java-concurrency.xml", "cursor-rules.xsl", "pml.xsd");
        String loggingResult = generator.generate("126-java-logging.xml", "cursor-rules.xsl", "pml.xsd");
        String unitTestingResult = generator.generate("131-java-unit-testing.xml", "cursor-rules.xsl", "pml.xsd");
        String refactoringWithModernFeaturesResult = generator.generate("141-java-refactoring-with-modern-features.xml", "cursor-rules.xsl", "pml.xsd");
        String functionalProgrammingResult = generator.generate("142-java-functional-programming.xml", "cursor-rules.xsl", "pml.xsd");
        String dataOrientedProgrammingResult = generator.generate("143-java-data-oriented-programming.xml", "cursor-rules.xsl", "pml.xsd");

        // Save all for comparison
        saveGeneratedContentToTarget(checklistGuideResult, "100-java-checklist-guide.mdc");
        saveGeneratedContentToTarget(bestPracticesResult, "110-java-maven-best-practices.mdc");
        saveGeneratedContentToTarget(documentationResult, "112-java-maven-documentation.mdc");
        saveGeneratedContentToTarget(objectOrientedDesignResult, "121-java-object-oriented-design.mdc");
        saveGeneratedContentToTarget(typeDesignResult, "122-java-type-design.mdc");
        saveGeneratedContentToTarget(generalGuidelinesResult, "123-java-general-guidelines.mdc");
        saveGeneratedContentToTarget(secureCodingResult, "124-java-secure-coding.mdc");
        saveGeneratedContentToTarget(concurrencyResult, "125-java-concurrency.mdc");
        saveGeneratedContentToTarget(loggingResult, "126-java-logging.mdc");
        saveGeneratedContentToTarget(unitTestingResult, "131-java-unit-testing.mdc");
        saveGeneratedContentToTarget(refactoringWithModernFeaturesResult, "141-java-refactoring-with-modern-features.mdc");
        saveGeneratedContentToTarget(functionalProgrammingResult, "142-java-functional-programming.mdc");
        saveGeneratedContentToTarget(dataOrientedProgrammingResult, "143-java-data-oriented-programming.mdc");
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

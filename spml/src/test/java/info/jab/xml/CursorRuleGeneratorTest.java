package info.jab.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cursor Rule Generator Tests")
class CursorRuleGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(CursorRuleGeneratorTest.class);



    @Nested
    @DisplayName("Parameterized Generate Method Tests")
    class ParameterizedGenerateMethodTests {

        @Test
        @DisplayName("Should throw exception when XML file does not exist")
        void should_throwException_when_xmlFileDoesNotExist() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When & Then - Updated for functional API exception handling
            assertThatThrownBy(() -> generator.generate("non-existent.xml", "unified-generator.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to generate cursor rules for")
                .hasMessageContaining("non-existent.xml")
                .hasMessageContaining("unified-generator.xsl");
        }

        @Test
        @DisplayName("Should throw exception when XSLT file does not exist")
        void should_throwException_when_xsltFileDoesNotExist() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When & Then - Updated for functional API exception handling
            assertThatThrownBy(() -> generator.generate("112-java-maven-documentation.xml", "non-existent.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to generate cursor rules for")
                .hasMessageContaining("112-java-maven-documentation.xml")
                .hasMessageContaining("non-existent.xsl");
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

    @Nested
    @DisplayName("Unified XSLT Generator Tests")
    class UnifiedXsltGeneratorTests {

        @Test
        @DisplayName("Should generate exact content matching original expected Maven best practices document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingMavenBestPracticesWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("110-java-maven-best-practices.mdc");

            // When
            String actualResult = generator.generate("110-java-maven-best-practices.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to specialized XSLT
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Maven documentation document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingMavenDocumentationWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("112-java-maven-documentation.mdc");

            // When
            String actualResult = generator.generate("112-java-maven-documentation.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to specialized XSLT
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java checklist guide document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaChecklistGuideWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("100-java-checklist-guide.mdc");

            // When
            String actualResult = generator.generate("100-java-checklist-guide.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should produce consistent content structure regardless of XML content type")
        void should_produceConsistentStructure_when_processingDifferentXmlTypes() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String bestPracticesResult = generator.generate("110-java-maven-best-practices.xml", "unified-generator.xsl");
            String documentationResult = generator.generate("112-java-maven-documentation.xml", "unified-generator.xsl");
            String checklistGuideResult = generator.generate("100-java-checklist-guide.xml", "unified-generator.xsl");

            // Then - All should have consistent frontmatter and structure
            assertThat(bestPracticesResult)
                .startsWith("---\ndescription: Maven Best Practices")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");

            assertThat(documentationResult)
                .startsWith("---\ndescription: Create README-DEV.md")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");

            assertThat(checklistGuideResult)
                .startsWith("---\ndescription:")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");

            // Save all for comparison
            saveGeneratedContentToTarget(bestPracticesResult, "unified-best-practices.mdc");
            saveGeneratedContentToTarget(documentationResult, "unified-documentation.mdc");
            saveGeneratedContentToTarget(checklistGuideResult, "unified-checklist-guide.mdc");
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java Object-Oriented Design document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaObjectOrientedDesignWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("121-java-object-oriented-design.mdc");

            // When
            String actualResult = generator.generate("121-java-object-oriented-design.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java Type Design document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaTypeDesignWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("122-java-type-design.mdc");

            // When
            String actualResult = generator.generate("122-java-type-design.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java General Guidelines document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaGeneralGuidelinesWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("123-java-general-guidelines.mdc");

            // When
            String actualResult = generator.generate("123-java-general-guidelines.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java Secure Coding document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaSecureCodingWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("124-java-secure-coding.mdc");

            // When
            String actualResult = generator.generate("124-java-secure-coding.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java Concurrency document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaConcurrencyWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("125-java-concurrency.mdc");

            // When
            String actualResult = generator.generate("125-java-concurrency.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching original expected Java Logging document using unified XSLT")
        void should_generateExactContentMatchingOriginalExpected_when_transformingJavaLoggingWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("126-java-logging.mdc");

            // When
            String actualResult = generator.generate("126-java-logging.xml", "unified-generator.xsl");

            // Then - Unified XSLT should produce identical output to expected
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
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

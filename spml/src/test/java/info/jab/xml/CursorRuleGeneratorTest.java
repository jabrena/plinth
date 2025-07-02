package info.jab.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cursor Rule Generator Tests")
class CursorRuleGeneratorTest {

    @Nested
    @DisplayName("Parameterized Generate Method Tests")
    class ParameterizedGenerateMethodTests {

        @Test
        @DisplayName("Should throw exception when XML file does not exist")
        void should_throwException_when_xmlFileDoesNotExist() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When & Then
            assertThatThrownBy(() -> generator.generate("non-existent.xml", "cursor-rule-generator.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error during XML transformation")
                .hasCauseInstanceOf(RuntimeException.class);

            // Verify the cause contains our expected message
            try {
                generator.generate("non-existent.xml", "cursor-rule-generator.xsl");
            } catch (RuntimeException e) {
                assertThat(e.getCause().getMessage())
                    .contains("Could not load XML or XSLT resources")
                    .contains("non-existent.xml");
            }
        }

        @Test
        @DisplayName("Should throw exception when XSLT file does not exist")
        void should_throwException_when_xsltFileDoesNotExist() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When & Then
            assertThatThrownBy(() -> generator.generate("112-java-maven-documentation.xml", "non-existent.xsl"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error during XML transformation")
                .hasCauseInstanceOf(RuntimeException.class);

            // Verify the cause contains our expected message
            try {
                generator.generate("112-java-maven-documentation.xml", "non-existent.xsl");
            } catch (RuntimeException e) {
                assertThat(e.getCause().getMessage())
                    .contains("Could not load XML or XSLT resources")
                    .contains("non-existent.xsl");
            }
        }

        @Test
        @DisplayName("Should generate exact content matching expected Maven best practices document")
        void should_generateExactContentMatchingExpectedDocument_when_transformingMavenBestPracticesXml() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("110-java-maven-best-practices.mdc");

            // When
            String actualResult = generator.generate("110-java-maven-best-practices.xml", "maven-best-practices-generator.xsl");

            // Save generated content to target directory for manual comparison
            saveGeneratedContentToTarget(actualResult, "110-java-maven-best-practices-generated.mdc");

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("Should generate exact content matching expected Maven documentation document")
        void should_generateExactContentMatchingExpectedDocument_when_transformingMavenDocumentationXml() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();
            String expectedContent = loadExpectedContent("112-java-maven-documentation.mdc");

            // When
            String actualResult = generator.generate("112-java-maven-documentation.xml", "cursor-rule-generator.xsl");

            // Save generated content to target directory for manual comparison
            saveGeneratedContentToTarget(actualResult, "112-java-maven-documentation-generated.mdc");

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent);
        }

        private String loadExpectedContent(String filename) throws IOException {
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
                if (Objects.isNull(inputStream)) {
                    throw new IOException("Resource not found: " + filename);
                }
                return new String(inputStream.readAllBytes()).trim();
            }
        }

        private void saveGeneratedContentToTarget(String content, String filename) throws IOException {
            Path targetDir = Paths.get("target");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path outputPath = targetDir.resolve(filename);
            Files.writeString(outputPath, content);
            System.out.println("Generated content saved to: " + outputPath.toAbsolutePath());
        }
    }

    @Nested
    @DisplayName("Unified XSLT Generator Tests")
    class UnifiedXsltGeneratorTests {

        @Test
        @DisplayName("Should generate content for Maven best practices using unified XSLT")
        void should_generateContent_when_transformingMavenBestPracticesWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String actualResult = generator.generate("110-java-maven-best-practices.xml", "unified-generator.xsl");

            // Save generated content to target directory for comparison
            saveGeneratedContentToTarget(actualResult, "110-maven-unified-generated.mdc");

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .contains("# Maven Best Practices")
                .contains("## Rule 1: Effective Dependency Management")
                .contains("**Good example:**")
                .contains("**Bad Example:**")
                .contains("## Table of contents");
        }

        @Test
        @DisplayName("Should generate content for Maven documentation using unified XSLT")
        void should_generateContent_when_transformingMavenDocumentationWithUnifiedXslt() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String actualResult = generator.generate("112-java-maven-documentation.xml", "unified-generator.xsl");

            // Save generated content to target directory for comparison
            saveGeneratedContentToTarget(actualResult, "112-maven-documentation-unified-generated.mdc");

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .contains("# Create README-DEV.md with information about how to use the Maven project")
                .contains("## STRICT Structure for README-DEV.md (Template):")
                .contains("# Essential Maven Goals:")
                .contains("./mvnw dependency:tree")
                .contains("**END OF TEMPLATE");
        }

        @Test
        @DisplayName("Should produce consistent content structure regardless of XML content type")
        void should_produceConsistentStructure_when_processingDifferentXmlTypes() throws IOException {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String bestPracticesResult = generator.generate("110-java-maven-best-practices.xml", "unified-generator.xsl");
            String documentationResult = generator.generate("112-java-maven-documentation.xml", "unified-generator.xsl");

            // Then - Both should have consistent frontmatter and structure
            assertThat(bestPracticesResult)
                .startsWith("---\ndescription: Maven Best Practices")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");

            assertThat(documentationResult)
                .startsWith("---\ndescription: Create README-DEV.md")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");

            // Save both for comparison
            saveGeneratedContentToTarget(bestPracticesResult, "unified-best-practices.mdc");
            saveGeneratedContentToTarget(documentationResult, "unified-documentation.mdc");
        }

        private void saveGeneratedContentToTarget(String content, String filename) throws IOException {
            Path targetDir = Paths.get("target");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path outputPath = targetDir.resolve(filename);
            Files.writeString(outputPath, content);
            System.out.println("Generated content saved to: " + outputPath.toAbsolutePath());
        }
    }

}

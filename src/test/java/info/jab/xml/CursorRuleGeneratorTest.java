package info.jab.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cursor Rule Generator Tests")
class CursorRuleGeneratorTest {

    private CursorRuleGenerator cursorRuleGenerator;

    @BeforeEach
    void setUp() {
        cursorRuleGenerator = new CursorRuleGenerator();
    }

    @Nested
    @DisplayName("Generate Method Tests")
    class GenerateMethodTests {

        @Test
        @DisplayName("Should generate correct markdown content when XML and XSLT resources are available")
        void should_generateCorrectMarkdownContent_when_xmlAndXsltResourcesAreAvailable() throws IOException {
            // Given
            String expectedContent = Files.readString(Paths.get("src/test/resources/112-java-maven-documentation.mdc"));

            // When
            String actualResult = cursorRuleGenerator.generate();

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedContent.trim());
        }

        @Test
        @DisplayName("Should generate content with proper structure")
        void should_generateContentWithProperStructure_when_calledSuccessfully() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate();

            // Then
            assertThat(result)
                .isNotNull()
                .contains("# Create README-DEV.md")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer")
                .contains("## Description")
                .contains("# Essential Maven Goals:")
                .contains("./mvnw dependency:tree")
                .contains("./mvnw clean package")
                .contains("**END OF TEMPLATE - DO NOT ADD ANYTHING BEYOND THIS POINT**");
        }

        @Test
        @DisplayName("Should generate consistent output across multiple calls")
        void should_generateConsistentOutput_when_calledMultipleTimes() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String firstResult = generator.generate();
            String secondResult = generator.generate();

            // Then
            assertThat(firstResult)
                .isEqualTo(secondResult)
                .isNotEmpty();
        }

        @Test
        @DisplayName("Should handle transformation correctly")
        void should_handleTransformationCorrectly_when_validResourcesExist() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate();

            // Then
            assertThat(result)
                .isNotNull()
                .doesNotContain("<?xml")
                .doesNotContain("<xsl:")
                .contains("# Create README-DEV.md")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer");
        }
    }

    @Nested
    @DisplayName("Parameterized Generate Method Tests")
    class ParameterizedGenerateMethodTests {

        @Test
        @DisplayName("Should generate Maven best practices content when using correct parameters")
        void should_generateMavenBestPracticesContent_when_usingCorrectParameters() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String actualResult = generator.generate("110-java-maven-best-practices.xml", "maven-best-practices-generator.xsl");

            // Then
            assertThat(actualResult)
                .isNotNull()
                .isNotEmpty()
                .contains("# Maven Best Practices")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer")
                .contains("## Description")
                .contains("## Table of contents")
                .contains("- Rule 1: Effective Dependency Management")
                .contains("## Rule 1: Effective Dependency Management")
                .contains("## Rule 7: Centralize Version Management with Properties")
                .contains("**Good example:**")
                .contains("**Bad Example:");
        }

        @Test
        @DisplayName("Should generate Maven best practices with proper structure")
        void should_generateMavenBestPracticesWithProperStructure_when_calledSuccessfully() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate("110-java-maven-best-practices.xml", "maven-best-practices-generator.xsl");

            // Then
            assertThat(result)
                .isNotNull()
                .contains("# Maven Best Practices")
                .contains("## System prompt characterization")
                .contains("Role definition: You are a Senior software engineer")
                .contains("## Description")
                .contains("## Table of contents")
                .contains("- Rule 1: Effective Dependency Management")
                .contains("## Rule 1: Effective Dependency Management")
                .contains("## Rule 7: Centralize Version Management with Properties")
                .contains("**Good example:**")
                .contains("**Bad Example:");
        }

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
        @DisplayName("Should use default files when calling parameterless generate method")
        void should_useDefaultFiles_when_callingParameterlessGenerateMethod() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String defaultResult = generator.generate();
            String explicitResult = generator.generate("112-java-maven-documentation.xml", "cursor-rule-generator.xsl");

            // Then
            assertThat(defaultResult)
                .isEqualTo(explicitResult)
                .isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should not throw exceptions during normal operation")
        void should_notThrowExceptions_when_normalOperationIsPerformed() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When & Then
            // Verify that normal operation doesn't throw unexpected exceptions
            assertThat(generator.generate())
                .isNotNull()
                .isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should not return null result")
        void should_notReturnNull_when_generateIsCalled() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate();

            // Then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should not return empty result")
        void should_notReturnEmpty_when_generateIsCalled() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate();

            // Then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should trim whitespace from result")
        void should_trimWhitespace_when_generateIsCalled() {
            // Given
            CursorRuleGenerator generator = new CursorRuleGenerator();

            // When
            String result = generator.generate();

            // Then
            assertThat(result)
                .isEqualTo(result.trim())
                .doesNotStartWith(" ")
                .doesNotStartWith("\n")
                .doesNotStartWith("\t")
                .doesNotEndWith(" ")
                .doesNotEndWith("\n")
                .doesNotEndWith("\t");
        }
    }
}

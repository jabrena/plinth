package info.jab.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Java Code Review Checklist Generator Tests")
class JavaCodeReviewChecklistTest {

    private JavaCodeReviewChecklistGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new JavaCodeReviewChecklistGenerator();
    }

    @Nested
    @DisplayName("Generate Method Tests")
    class GenerateMethodTests {

        @Test
        @DisplayName("Should generate correct markdown content when XML and XSLT resources are available")
        void should_generateCorrectMarkdownContent_when_xmlAndXsltResourcesAreAvailable() throws IOException {
            // Given
            String expectedContent = Files.readString(Paths.get("src/test/resources/java-code-review-checklist.mdc"));

            // When
            String actualResult = generator.generate();

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
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result)
                .isNotNull()
                .contains("# Java Code Review Checklist")
                .contains("## System prompt characterization")
                .contains("Role definition: You are an experienced Java developer")
                .contains("## Description")
                .contains("## Functionality Review")
                .contains("- [ ] Code follows SOLID principles")
                .contains("## Clean Code Review")
                .contains("## Java Fundamentals Review")
                .contains("## Security Review")
                .contains("## Exception Handling Review")
                .contains("## Performance Review")
                .contains("## Testing Review")
                .contains("## Configuration Review")
                .contains("## General Programming Review")
                .contains("## Code Examples")
                .contains("### Good Example - Single Responsibility Principle")
                .contains("### Bad Example - Violates SRP")
                .contains("### Good Example - Immutable Class")
                .contains("### Good Example - Security with PreparedStatement")
                .contains("**END OF CHECKLIST - USE THIS FOR COMPREHENSIVE JAVA CODE REVIEWS**");
        }

        @Test
        @DisplayName("Should generate consistent output across multiple calls")
        void should_generateConsistentOutput_when_calledMultipleTimes() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String firstResult = checklistGenerator.generate();
            String secondResult = checklistGenerator.generate();

            // Then
            assertThat(firstResult)
                .isEqualTo(secondResult)
                .isNotEmpty();
        }

        @Test
        @DisplayName("Should handle transformation correctly")
        void should_handleTransformationCorrectly_when_validResourcesExist() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result)
                .isNotNull()
                .doesNotContain("<?xml")
                .doesNotContain("<xsl:")
                .contains("# Java Code Review Checklist")
                .contains("## System prompt characterization")
                .contains("Role definition: You are an experienced Java developer");
        }

        @Test
        @DisplayName("Should include comprehensive checklist items")
        void should_includeComprehensiveChecklistItems_when_generated() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result)
                .contains("PreparedStatements used instead of concatenated SQL")
                .contains("Classes are final and immutable where appropriate")
                .contains("No System.out.println statements")
                .contains("Thread-safe code with proper synchronization")
                .contains("Unit tests present with good coverage")
                .contains("No hard-coded configuration values")
                .contains("BigDecimal for money")
                .contains("Proper equals, hashCode, and toString implementations");
        }

        @Test
        @DisplayName("Should include code examples for best practices")
        void should_includeCodeExamples_when_generated() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result)
                .contains("public class CustomerValidator")
                .contains("public class CustomerManager")
                .contains("public final class Money")
                .contains("public class UserRepository")
                .contains("PreparedStatement stmt")
                .contains("Objects.requireNonNull")
                .contains("isValidEmail")
                .contains("BigDecimal amount");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should not throw exceptions during normal operation")
        void should_notThrowExceptions_when_normalOperationIsPerformed() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When & Then
            // Verify that normal operation doesn't throw unexpected exceptions
            assertThat(checklistGenerator.generate())
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
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should not return empty result")
        void should_notReturnEmpty_when_generateIsCalled() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should trim whitespace from result")
        void should_trimWhitespace_when_generateIsCalled() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

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

        @Test
        @DisplayName("Should contain security best practices")
        void should_containSecurityBestPractices_when_generated() {
            // Given
            JavaCodeReviewChecklistGenerator checklistGenerator = new JavaCodeReviewChecklistGenerator();

            // When
            String result = checklistGenerator.generate();

            // Then
            assertThat(result)
                .contains("No sensitive data logged")
                .contains("User inputs are sanitized")
                .contains("PreparedStatements used instead of concatenated SQL")
                .contains("Resources properly released to prevent DoS attacks")
                .contains("No sensitive information leaked through exceptions");
        }
    }
}

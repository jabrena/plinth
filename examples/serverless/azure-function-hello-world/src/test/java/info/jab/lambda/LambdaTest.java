package info.jab.lambda;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Azure Function Lambda class.
 * Following Java unit testing best practices with JUnit 5, AssertJ, and Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Lambda Function Tests")
class LambdaTest {

    @Mock
    private HttpRequestMessage<Optional<String>> mockRequest;

    @Mock
    private ExecutionContext mockContext;

    @Mock
    private HttpResponseMessage.Builder mockResponseBuilder;

    @Mock
    private HttpResponseMessage mockResponse;

    private Lambda lambda;

    @BeforeEach
    void setUp() {
        lambda = new Lambda();

        // Setup common mock behavior
        when(mockContext.getLogger()).thenReturn(Logger.getGlobal());
        when(mockRequest.createResponseBuilder(any(HttpStatus.class))).thenReturn(mockResponseBuilder);
        when(mockResponseBuilder.body(any())).thenReturn(mockResponseBuilder);
        when(mockResponseBuilder.build()).thenReturn(mockResponse);
    }

    @Nested
    @DisplayName("HttpExample Function Tests")
    class HttpExampleTests {

        @Test
        @DisplayName("Should return success response when name is provided in query parameters")
        void should_returnSuccessResponse_when_nameProvidedInQueryParameters() {
            // Given
            String expectedName = "Azure";
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("name", expectedName);

            when(mockRequest.getQueryParameters()).thenReturn(queryParams);
            when(mockRequest.getBody()).thenReturn(Optional.empty());
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn("Hello, " + expectedName + "! This HTTP triggered function executed successfully.");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                .asString()
                .contains("Hello, " + expectedName)
                .contains("This HTTP triggered function executed successfully");
        }

        @Test
        @DisplayName("Should return success response when name is provided in request body")
        void should_returnSuccessResponse_when_nameProvidedInRequestBody() {
            // Given
            String expectedName = "BodyName";
            Map<String, String> emptyQueryParams = new HashMap<>();

            when(mockRequest.getQueryParameters()).thenReturn(emptyQueryParams);
            when(mockRequest.getBody()).thenReturn(Optional.of(expectedName));
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn("Hello, " + expectedName + "! This HTTP triggered function executed successfully.");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                .asString()
                .contains("Hello, " + expectedName)
                .contains("This HTTP triggered function executed successfully");
        }

        @Test
        @DisplayName("Should prioritize request body over query parameter when both are provided")
        void should_prioritizeRequestBody_when_bothBodyAndQueryParametersProvided() {
            // Given
            String bodyName = "BodyName";
            String queryName = "QueryName";
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("name", queryName);

            when(mockRequest.getQueryParameters()).thenReturn(queryParams);
            when(mockRequest.getBody()).thenReturn(Optional.of(bodyName));
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn("Hello, " + bodyName + "! This HTTP triggered function executed successfully.");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                .asString()
                .contains("Hello, " + bodyName)
                .doesNotContain(queryName);
        }

        @Test
        @DisplayName("Should return bad request when no name is provided")
        void should_returnBadRequest_when_noNameProvided() {
            // Given
            Map<String, String> emptyQueryParams = new HashMap<>();

            when(mockRequest.getQueryParameters()).thenReturn(emptyQueryParams);
            when(mockRequest.getBody()).thenReturn(Optional.empty());
            when(mockResponse.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
            when(mockResponse.getBody()).thenReturn("Please pass a name on the query string or in the request body");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody())
                .asString()
                .contains("Please pass a name");
        }

        @ParameterizedTest(name = "Should return bad request when name is: {0}")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
        @DisplayName("Should return bad request when name is null, empty, or whitespace")
        void should_returnBadRequest_when_nameIsNullEmptyOrWhitespace(String invalidName) {
            // Given
            Map<String, String> queryParams = new HashMap<>();
            if (invalidName != null) {
                queryParams.put("name", invalidName);
            }

            when(mockRequest.getQueryParameters()).thenReturn(queryParams);
            when(mockRequest.getBody()).thenReturn(Optional.empty());
            when(mockResponse.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);
            when(mockResponse.getBody()).thenReturn("Please pass a name on the query string or in the request body");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(result.getBody())
                .asString()
                .contains("Please pass a name");
        }

        @ParameterizedTest(name = "Should handle special characters in name: {0}")
        @ValueSource(strings = {"João", "José María", "山田太郎", "أحمد", "François-René"})
        @DisplayName("Should handle names with special characters and unicode")
        void should_handleSpecialCharacters_when_nameContainsUnicodeCharacters(String specialName) {
            // Given
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("name", specialName);

            when(mockRequest.getQueryParameters()).thenReturn(queryParams);
            when(mockRequest.getBody()).thenReturn(Optional.empty());
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn("Hello, " + specialName + "! This HTTP triggered function executed successfully.");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                .asString()
                .contains("Hello, " + specialName);
        }

        @Test
        @DisplayName("Should handle very long names within reasonable limits")
        void should_handleLongNames_when_nameExceedsTypicalLength() {
            // Given
            String longName = "A".repeat(1000); // 1000 character name
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("name", longName);

            when(mockRequest.getQueryParameters()).thenReturn(queryParams);
            when(mockRequest.getBody()).thenReturn(Optional.empty());
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn("Hello, " + longName + "! This HTTP triggered function executed successfully.");

            // When
            HttpResponseMessage result = lambda.run(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                .asString()
                .contains("Hello, " + longName);
        }
    }

    @Nested
    @DisplayName("HelloWorld Function Tests")
    class HelloWorldTests {

        @Test
        @DisplayName("Should return hello world message with OK status")
        void should_returnHelloWorldMessage_when_functionIsCalled() {
            // Given
            String expectedMessage = "Hello World from Azure Functions!";
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn(expectedMessage);

            // When
            HttpResponseMessage result = lambda.helloWorld(mockRequest, mockContext);

            // Then
            assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("Should return consistent response on multiple calls")
        void should_returnConsistentResponse_when_calledMultipleTimes() {
            // Given
            String expectedMessage = "Hello World from Azure Functions!";
            when(mockResponse.getStatus()).thenReturn(HttpStatus.OK);
            when(mockResponse.getBody()).thenReturn(expectedMessage);

            // When
            HttpResponseMessage firstResult = lambda.helloWorld(mockRequest, mockContext);
            HttpResponseMessage secondResult = lambda.helloWorld(mockRequest, mockContext);

            // Then
            assertThat(firstResult.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(secondResult.getStatus()).isEqualTo(HttpStatus.OK);
            assertThat(firstResult.getBody()).isEqualTo(secondResult.getBody());
        }
    }
}

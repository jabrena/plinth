package info.jab.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RequestDemo Integration Tests")
class RequestDemoIT extends BaseIntegrationTest {

    private RequestDemo requestDemo;

    @BeforeEach
    void setUp() {
        requestDemo = new RequestDemo();
    }

    @Test
    @DisplayName("Should fetch data successfully when example-api returns 200")
    void should_fetchData_when_apiReturnsSuccess() throws Exception {
        // Given — WireMock auto-loads the mapping at startup from:
        //   src/test/resources/wiremock/mappings/example-api/get-data.json
        String baseUrl = System.getProperty("external.example-api.base-url");

        // When
        String result = requestDemo.fetchData(baseUrl + "/data");

        // Then
        assertThat(result).contains("Hello from example-api");
    }
}

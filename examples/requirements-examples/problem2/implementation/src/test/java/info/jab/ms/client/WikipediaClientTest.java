package info.jab.ms.client;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import info.jab.ms.WireMockQuarkusResource;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
@Tag("integration-test")
public class WikipediaClientTest {

    @Inject
    @RestClient
    WikipediaClient wikipediaClient;

    @BeforeEach
    void setUp() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    void shouldReturnWikipediaPageContent() {
        // Given
        String expectedContent = "<html><body>Zeus is the king of gods...</body></html>";
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody(expectedContent))
        );

        // When
        String content = wikipediaClient.getWikipediaPage("Zeus");

        // Then
        assertThat(content).isEqualTo(expectedContent);
    }

    @ParameterizedTest
    @CsvSource({
        "Zeus, /wiki/Zeus",
        "Ares, /wiki/Ares", 
        "Athena, /wiki/Athena"
    })
    void shouldHandlePathParameterEncoding(String godName, String expectedPath) {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo(expectedPath))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("content for " + godName))
        );

        // When
        String content = wikipediaClient.getWikipediaPage(godName);

        // Then
        assertThat(content).isEqualTo("content for " + godName);
    }

    @Test
    void shouldHandleGodNameWithSpaces() {
        // Given - test URL encoding for names with spaces
        String godName = "Aphrodite Venus";
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlPathEqualTo("/wiki/Aphrodite%20Venus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("Aphrodite content"))
        );

        // When
        String content = wikipediaClient.getWikipediaPage(godName);

        // Then
        assertThat(content).isEqualTo("Aphrodite content");
    }

    @Test
    void shouldThrowExceptionOn404() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/NonExistentGod"))
                .willReturn(aResponse()
                    .withStatus(404))
        );

        // When/Then
        assertThatThrownBy(() -> wikipediaClient.getWikipediaPage("NonExistentGod"))
            .hasMessageContaining("404");
    }

    @Test
    void shouldThrowExceptionOn500() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(500))
        );

        // When/Then
        assertThatThrownBy(() -> wikipediaClient.getWikipediaPage("Zeus"))
            .hasMessageContaining("500");
    }

    @Test
    void shouldThrowExceptionOnTimeout() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(3000) // Longer than test timeout (2000ms)
                    .withBody("Zeus content"))
        );

        // When/Then
        assertThatThrownBy(() -> wikipediaClient.getWikipediaPage("Zeus"))
            .hasMessageContaining("timeout");
    }

    @Test
    void shouldReturnEmptyContentWhenBodyIsEmpty() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody(""))
        );

        // When
        String content = wikipediaClient.getWikipediaPage("Zeus");

        // Then - empty body may return null or empty string
        assertThat(content).satisfiesAnyOf(
            c -> assertThat(c).isNull(),
            c -> assertThat(c).isEmpty()
        );
    }
}
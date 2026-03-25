package info.jab.ms.client;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import info.jab.ms.WireMockQuarkusResource;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
@Tag("integration-test")
public class GreekGodsClientTest {

    @Inject
    @RestClient
    GreekGodsClient greekGodsClient;

    @BeforeEach
    void setUp() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    void shouldReturnGreekGodsList() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\",\"Zeus\",\"Athena\"]"))
        );

        // When
        List<String> gods = greekGodsClient.getGreekGods();

        // Then
        assertThat(gods).containsExactly("Ares", "Zeus", "Athena");
    }

    @Test
    void shouldHandleEmptyResponse() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[]"))
        );

        // When
        List<String> gods = greekGodsClient.getGreekGods();

        // Then
        assertThat(gods).isEmpty();
    }

    @Test
    void shouldThrowExceptionOn404() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(404))
        );

        // When/Then
        assertThatThrownBy(() -> greekGodsClient.getGreekGods())
            .hasMessageContaining("404");
    }

    @Test
    void shouldThrowExceptionOn500() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(500))
        );

        // When/Then
        assertThatThrownBy(() -> greekGodsClient.getGreekGods())
            .hasMessageContaining("500");
    }

    @Test
    void shouldThrowExceptionOnTimeout() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(3000) // Longer than test timeout (2000ms)
                    .withBody("[\"Zeus\"]"))
        );

        // When/Then
        assertThatThrownBy(() -> greekGodsClient.getGreekGods())
            .hasMessageContaining("timeout");
    }
}
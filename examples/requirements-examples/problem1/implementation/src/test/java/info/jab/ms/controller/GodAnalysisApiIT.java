package info.jab.ms.controller;

import info.jab.ms.AbstractSpringBootTest;
import info.jab.ms.dto.GodStatsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-stack integration tests for the HTTP API ({@code RestClient} → running app → WireMock backends).
 * Not a {@code @WebMvcTest} slice — see {@link AbstractSpringBootTest}.
 */
@Tag("integration-test")
@TestPropertySource(properties = {
        "resilience4j.retry.instances.greek.max-attempts=1",
        "resilience4j.retry.instances.roman.max-attempts=1",
        "resilience4j.retry.instances.nordic.max-attempts=1"
})
class GodAnalysisApiIT extends AbstractSpringBootTest {

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
        wireMock.resetToDefaultMappings();
    }

    @Test
    @DisplayName("Happy path: all sources, filter=n -> 78179288397447443426")
    void shouldCalculateHappyPathSum() {

        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().sum()).isEqualTo("78179288397447443426");
    }

    @Test
    @DisplayName("Nordic timeout: Greek+Roman respond, partial sum = 78179210291336329326")
    void shouldReturnPartialSumWhenNordicTimesOut() {
        // greek + roman served from default mappings; override nordic with a delay beyond 5s timeout
        wireMock.stubFor(get(urlEqualTo("/nordic"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("nordic-gods.json")
                        .withFixedDelay(6000)));

        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        // Nike + Nemesis (Greek) + Neptun (Roman) — Njord (Nordic) excluded due to timeout
        assertThat(response.getBody().sum()).isEqualTo("78179210291336329326");
    }

    @Test
    @DisplayName("Greek-only: partial sum = 78101109179220212216")
    void shouldReturnGreekOnlyPartialSum() {
        // greek served from default mappings; override roman + nordic with delays beyond 5s timeout
        wireMock.stubFor(get(urlEqualTo("/roman"))
                .willReturn(aResponse()
                        .withFixedDelay(6000)
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("roman-gods.json")));
        wireMock.stubFor(get(urlEqualTo("/nordic"))
                .willReturn(aResponse()
                        .withFixedDelay(6000)
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("nordic-gods.json")));

        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        // Nike + Nemesis (Greek) only
        assertThat(response.getBody().sum()).isEqualTo("78101109179220212216");
    }
}

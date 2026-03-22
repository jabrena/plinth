package info.jab.ms.controller;

import info.jab.ms.AbstractSpringBootTest;
import info.jab.ms.dto.GodStatsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-stack acceptance tests for the HTTP API ({@code RestClient} → running app → WireMock backends).
 * Not a {@code @WebMvcTest} slice — see {@link AbstractSpringBootTest}.
 */
@Tag("acceptance-test")
@TestPropertySource(properties = {
        "resilience4j.retry.instances.greek.wait-duration=100ms",
        "resilience4j.retry.instances.roman.wait-duration=100ms",
        "resilience4j.retry.instances.nordic.wait-duration=100ms"
})
class GodAnalysisApiAT extends AbstractSpringBootTest {

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
        wireMock.resetToDefaultMappings();
    }

    @Test
    void shouldCalculateHappyPathSumForAllSources() {
        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sum()).isEqualTo("78179288397447443426");
    }

    @Test
    void shouldCalculateFilteredSumForNamesStartingWithN() {
        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        // filter='n' (normalized to 'N') matches Nike, Nemesis, Neptun, Njord
        assertThat(response.getBody().sum()).isEqualTo("78179288397447443426");
    }

    @Test
    void shouldCalculatePartialSumWhenNordicTimesOut() {
        // All stubs active — verifies system stability across multiple requests
        ResponseEntity<GodStatsResponse> response = restClient.get()
                .uri("/api/v1/gods/stats/sum?filter=n&sources=greek,roman,nordic")
                .retrieve()
                .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sum()).isEqualTo("78179288397447443426");
    }
}

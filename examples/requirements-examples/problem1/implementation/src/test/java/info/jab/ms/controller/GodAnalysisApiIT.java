package info.jab.ms.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class GodAnalysisApiIT {

    static WireMockServer wireMock = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    @BeforeAll
    static void startWireMock() {
        wireMock.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        String base = "http://localhost:" + wireMock.port();
        registry.add("god-api.sources.greek.url", () -> base + "/greek");
        registry.add("god-api.sources.roman.url", () -> base + "/roman");
        registry.add("god-api.sources.nordic.url", () -> base + "/nordic");
        registry.add("god-api.timeout.read", () -> "300");
        registry.add("god-api.timeout.connect", () -> "300");
    }

    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();
    }

    @LocalServerPort
    int port;

    @Test
    void timeout_nordicAndRoman_returnsPartialSumFromGreekOnly() {
        // Greek: responds immediately with full list
        wireMock.stubFor(get(urlPathEqualTo("/greek"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"Zeus\",\"Hera\",\"Poseidon\",\"Demeter\",\"Ares\",\"Athena\",\"Apollo\",\"Artemis\",\"Hephaestus\",\"Aphrodite\",\"Hermes\",\"Dionysus\",\"Hades\",\"Hypnos\",\"Nike\",\"Janus\",\"Nemesis\",\"Iris\",\"Hecate\",\"Tyche\"]")));
        // Roman: timeout (delay > 300ms read timeout)
        wireMock.stubFor(get(urlPathEqualTo("/roman"))
            .willReturn(aResponse().withFixedDelay(1000).withStatus(200).withBody("[]")));
        // Nordic: timeout (delay > 300ms read timeout)
        wireMock.stubFor(get(urlPathEqualTo("/nordic"))
            .willReturn(aResponse().withFixedDelay(1000).withStatus(200).withBody("[]")));

        RestClient client = RestClient.create("http://localhost:" + port);

        record GodStatsResponse(String sum) {}

        ResponseEntity<GodStatsResponse> response = client.get()
            .uri("/api/v1/gods/stats/sum?filter=N&sources=greek,roman,nordic")
            .retrieve()
            .toEntity(GodStatsResponse.class);

        // Greek names starting with 'N': Nike (78105107101) + Nemesis (78101109101115105115)
        // Sum: 78101109179220212216
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sum()).isEqualTo("78101109179220212216");
    }
}

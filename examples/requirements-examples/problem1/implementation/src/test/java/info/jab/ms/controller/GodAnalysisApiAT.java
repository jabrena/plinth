package info.jab.ms.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

import static org.assertj.core.api.Assertions.assertThat;

@Tag("acceptance-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GodAnalysisApiAT {

    static WireMockServer wireMockServer;

    @LocalServerPort
    int port;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig()
                .dynamicPort()
                .usingFilesUnderClasspath(".")
        );
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void wireMockProperties(DynamicPropertyRegistry registry) {
        String baseUrl = "http://localhost:" + wireMockServer.port();
        registry.add("god-api.sources.greek.url", () -> baseUrl + "/greek");
        registry.add("god-api.sources.roman.url", () -> baseUrl + "/roman");
        registry.add("god-api.sources.nordic.url", () -> baseUrl + "/nordic");
    }

    @Test
    void happyPath_filterN_allSources_returnCorrectSum() {
        RestClient client = RestClient.create("http://localhost:" + port);

        ResponseEntity<GodStatsResponse> response = client.get()
            .uri("/api/v1/gods/stats/sum?filter=N&sources=greek,roman,nordic")
            .retrieve()
            .toEntity(GodStatsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().sum()).isEqualTo("78179288397447443426");
    }

    record GodStatsResponse(String sum) {}
}

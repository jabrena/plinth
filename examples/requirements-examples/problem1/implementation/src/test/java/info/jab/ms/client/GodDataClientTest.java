package info.jab.ms.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import info.jab.ms.config.GodApiProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit-test")
class GodDataClientTest {

    private WireMockServer wireMock;
    private GodDataClient client;

    @BeforeEach
    void setUp() {
        wireMock = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMock.start();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(200);
        factory.setReadTimeout(200);
        RestClient restClient = RestClient.builder().requestFactory(factory).build();

        String base = "http://localhost:" + wireMock.port();
        GodApiProperties.Source greek = new GodApiProperties.Source(base + "/greek");
        GodApiProperties.Source roman = new GodApiProperties.Source(base + "/roman");
        GodApiProperties.Source nordic = new GodApiProperties.Source(base + "/nordic");
        GodApiProperties.Sources sources = new GodApiProperties.Sources(greek, roman, nordic);
        GodApiProperties.Timeout timeout = new GodApiProperties.Timeout(200, 200);
        GodApiProperties properties = new GodApiProperties(sources, timeout);

        client = new GodDataClient(restClient, properties);
    }

    @AfterEach
    void tearDown() {
        wireMock.stop();
    }

    @Test
    void fetchGreekGods_success_returnsNameList() {
        wireMock.stubFor(get(urlPathEqualTo("/greek"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"Zeus\",\"Hera\",\"Athena\"]")));

        List<String> result = client.fetchGreekGods();

        assertThat(result).containsExactly("Zeus", "Hera", "Athena");
    }

    @Test
    void fetchGreekGods_timeout_returnsEmptyList() {
        wireMock.stubFor(get(urlPathEqualTo("/greek"))
            .willReturn(aResponse()
                .withFixedDelay(500)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"Zeus\"]")));

        List<String> result = client.fetchGreekGods();

        assertThat(result).isEmpty();
    }

    @Test
    void fetchRomanGods_httpError_returnsEmptyList() {
        wireMock.stubFor(get(urlPathEqualTo("/roman"))
            .willReturn(aResponse().withStatus(503)));

        List<String> result = client.fetchRomanGods();

        assertThat(result).isEmpty();
    }
}

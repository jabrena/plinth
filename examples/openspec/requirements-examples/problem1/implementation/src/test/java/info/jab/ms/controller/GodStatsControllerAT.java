package info.jab.ms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GodStatsControllerAT {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    static void configureOutboundProperties(DynamicPropertyRegistry registry) {
        registry.add("god.outbound.connect-timeout", () -> "150ms");
        registry.add("god.outbound.read-timeout", () -> "200ms");
        registry.add("god.outbound.urls.greek", () -> wireMockServer.baseUrl() + "/greek");
        registry.add("god.outbound.urls.roman", () -> wireMockServer.baseUrl() + "/roman");
        registry.add("god.outbound.urls.nordic", () -> wireMockServer.baseUrl() + "/nordic");
    }

    @Value("${local.server.port}")
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void shouldReturnSumForHappyPathWithOutboundCalls() {
        stubSource("greek", 0);
        stubSource("roman", 0);
        stubSource("nordic", 0);

        var response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/v1/gods/stats/sum")
                .queryParam("filter", "N")
                .queryParam("sources", "greek,roman,nordic")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"sum\":\"2258\"");
        assertSingleFetch("greek");
        assertSingleFetch("roman");
        assertSingleFetch("nordic");
    }

    @Test
    void shouldReturnPartialSumWhenOneSourceTimesOut() {
        stubSource("greek", 0);
        stubSource("roman", 500);
        stubSource("nordic", 0);

        var response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/v1/gods/stats/sum")
                .queryParam("filter", "N")
                .queryParam("sources", "greek,roman,nordic")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"sum\":\"1624\"");
        assertSingleFetch("greek");
        assertSingleFetch("roman");
        assertSingleFetch("nordic");
    }

    @Test
    void shouldReturnBadRequestWhenFilterIsNotSingleCharacter() {
        var response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/v1/gods/stats/sum")
                .queryParam("filter", "nn")
                .queryParam("sources", "greek")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, httpResponse) -> {
                // no-op: allows asserting OpenAPI error envelope directly
            })
            .toEntity(String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).contains("\"code\":\"INVALID_FILTER\"");
    }

    @Test
    void shouldReturnBadRequestWhenSourceIsUnsupported() {
        assertThatThrownBy(() -> restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/v1/gods/stats/sum")
                .queryParam("filter", "n")
                .queryParam("sources", "egyptian")
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(String.class))
            .isInstanceOf(HttpClientErrorException.BadRequest.class)
            .hasMessageContaining("INVALID_SOURCE");
    }

    private void stubSource(String source, int fixedDelayMillis) {
        wireMockServer.stubFor(get(urlPathEqualTo("/" + source))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withFixedDelay(fixedDelayMillis)
                .withBody(bodyFor(source))));
    }

    private void assertSingleFetch(String source) {
        wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/" + source))
            .withHeader("Accept", equalTo("application/json")));
    }

    private String bodyFor(String source) {
        return switch (source) {
            case "greek" ->
                "[\"Zeus\",\"Hera\",\"Poseidon\",\"Demeter\",\"Ares\",\"Athena\",\"Apollo\",\"Artemis\",\"Hephaestus\",\"Aphrodite\",\"Hermes\",\"Dionysus\",\"Hades\",\"Hypnos\",\"Nike\",\"Janus\",\"Nemesis\",\"Iris\",\"Hecate\",\"Tyche\"]";
            case "roman" -> "[\"Venus\",\"Mars\",\"Neptun\",\"Mercury\",\"Pluto\",\"Jupiter\"]";
            case "nordic" -> "[\"Baldur\",\"Freyja\",\"Heimdall\",\"Frigga\",\"Hel\",\"Loki\",\"Njord\",\"Odin\",\"Thor\",\"Tyr\"]";
            default -> throw new IllegalArgumentException("Unsupported source: " + source);
        };
    }
}

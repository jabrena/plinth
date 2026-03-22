package info.jab.ms.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import info.jab.ms.config.GodApiProperties;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;

class GodDataClientRetryTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private GodDataClient client;

    @BeforeEach
    void setUp() {
        wireMock.resetAll();

        GodApiProperties props = new GodApiProperties();
        GodApiProperties.TimeoutConfig timeout = new GodApiProperties.TimeoutConfig();
        timeout.setConnect(2000);
        timeout.setRead(2000);
        props.setTimeout(timeout);

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(4)
                .waitDuration(Duration.ofMillis(100))
                .build();
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);

        client = new GodDataClient(props, retryRegistry);
    }

    @Test
    @DisplayName("Succeeds on 3rd attempt after 2 transient 500 failures")
    void shouldRetryAndSucceedOnThirdAttempt() {
        wireMock.stubFor(get(urlEqualTo("/greek"))
                .inScenario("greek-retry")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("attempt-2")
                .willReturn(aResponse().withStatus(500)));

        wireMock.stubFor(get(urlEqualTo("/greek"))
                .inScenario("greek-retry")
                .whenScenarioStateIs("attempt-2")
                .willSetStateTo("success")
                .willReturn(aResponse().withStatus(500)));

        wireMock.stubFor(get(urlEqualTo("/greek"))
                .inScenario("greek-retry")
                .whenScenarioStateIs("success")
                .willReturn(okJson("[\"Nike\",\"Nemesis\"]")));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/greek", "greek");

        assertThat(names).containsExactly("Nike", "Nemesis");
        wireMock.verify(3, getRequestedFor(urlEqualTo("/greek")));
    }

    @Test
    @DisplayName("Exhausts all 4 attempts on persistent failure, returns empty list")
    void shouldReturnEmptyListAfterExhaustingAllAttempts() {
        wireMock.stubFor(get(urlEqualTo("/roman"))
                .willReturn(aResponse().withStatus(503)));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/roman", "roman");

        assertThat(names).isEmpty();
        wireMock.verify(4, getRequestedFor(urlEqualTo("/roman")));
    }

    @Test
    @DisplayName("Succeeds on first attempt (no retry needed)")
    void shouldSucceedOnFirstAttemptWithoutRetry() {
        wireMock.stubFor(get(urlEqualTo("/nordic"))
                .willReturn(okJson("[\"Njord\",\"Odin\"]")));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/nordic", "nordic");

        assertThat(names).containsExactly("Njord", "Odin");
        wireMock.verify(1, getRequestedFor(urlEqualTo("/nordic")));
    }

    @Test
    @DisplayName("Per-source retry isolation: roman retry does not affect greek")
    void shouldIsolateRetryPerSource() {
        wireMock.stubFor(get(urlEqualTo("/greek"))
                .willReturn(okJson("[\"Nike\"]")));

        wireMock.stubFor(get(urlEqualTo("/roman"))
                .willReturn(aResponse().withStatus(500)));

        List<String> greekNames = client.fetchGodNames(wireMock.baseUrl() + "/greek", "greek");
        List<String> romanNames = client.fetchGodNames(wireMock.baseUrl() + "/roman", "roman");

        assertThat(greekNames).containsExactly("Nike");
        assertThat(romanNames).isEmpty();
        wireMock.verify(1, getRequestedFor(urlEqualTo("/greek")));
        wireMock.verify(4, getRequestedFor(urlEqualTo("/roman")));
    }
}

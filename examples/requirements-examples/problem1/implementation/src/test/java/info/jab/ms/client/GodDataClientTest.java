package info.jab.ms.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import info.jab.ms.config.GodApiProperties;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

class GodDataClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private GodDataClient client;

    @BeforeEach
    void setUp() {
        RetryConfig retryConfig = RetryConfig.custom().maxAttempts(1).build();
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);

        GodApiProperties props = new GodApiProperties();
        GodApiProperties.TimeoutConfig timeout = new GodApiProperties.TimeoutConfig();
        timeout.setConnect(2000);
        timeout.setRead(2000);
        props.setTimeout(timeout);
        client = new GodDataClient(props, retryRegistry);
        wireMock.resetAll();
    }

    @Test
    void shouldFetchGreekGodNames() {
        wireMock.stubFor(get(urlEqualTo("/greek"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                ["Zeus","Hera","Nike","Nemesis"]""")));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/greek", "test");

        assertThat(names).containsExactly("Zeus", "Hera", "Nike", "Nemesis");
    }

    @Test
    void shouldReturnEmptyListOnTimeout() {
        wireMock.stubFor(get(urlEqualTo("/greek"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                ["Zeus"]""")
                        .withFixedDelay(3000)));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/greek", "test");

        assertThat(names).isEmpty();
    }

    @Test
    void shouldReturnEmptyListOnHttpError() {
        wireMock.stubFor(get(urlEqualTo("/greek"))
                .willReturn(aResponse().withStatus(500)));

        List<String> names = client.fetchGodNames(wireMock.baseUrl() + "/greek", "test");

        assertThat(names).isEmpty();
    }

    @Test
    void shouldReturnEmptyListOnConnectionFailure() {
        List<String> names = client.fetchGodNames("http://localhost:1/greek", "test");
        assertThat(names).isEmpty();
    }
}

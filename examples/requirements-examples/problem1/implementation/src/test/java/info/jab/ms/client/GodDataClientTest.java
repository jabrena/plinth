package info.jab.ms.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import info.jab.ms.config.GodOutboundProperties;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

class GodDataClientTest {

	private static final WireMockServer WM = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

	private GodDataClient client;

	@BeforeAll
	static void startWireMock() {
		WM.start();
	}

	@AfterAll
	static void stopWireMock() {
		WM.stop();
	}

	@BeforeEach
	void setUp() {
		String base = "http://localhost:" + WM.port();
		GodOutboundProperties props = new GodOutboundProperties(
				Duration.ofMillis(50),
				Duration.ofMillis(50),
				new GodOutboundProperties.Urls(base + "/greek", base + "/roman", base + "/nordic"));
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout((int) props.connectTimeout().toMillis());
		factory.setReadTimeout((int) props.readTimeout().toMillis());
		RestClient restClient = RestClient.builder().requestFactory(factory).build();
		client = new GodDataClient(restClient, props);
	}

	@AfterEach
	void tearDown() {
		WM.resetAll();
	}

	@Test
	void successfulFetchReturnsJsonArrayAsStrings() throws Exception {
		String body = new String(
				new ClassPathResource("wiremock/greek-gods.json").getInputStream().readAllBytes(),
				StandardCharsets.UTF_8);
		WM.stubFor(
				get(urlPathEqualTo("/greek"))
						.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body)));

		assertThat(client.fetchNames("greek")).hasSize(20);
		WM.verify(1, getRequestedFor(urlPathEqualTo("/greek")));
	}

	@Test
	void readTimeoutReturnsEmptyListWithoutRetry() {
		WM.stubFor(
				get(urlPathEqualTo("/greek"))
						.willReturn(aResponse().withStatus(200).withFixedDelay(500).withBody("[]")));

		assertThat(client.fetchNames("greek")).isEmpty();
		WM.verify(1, getRequestedFor(urlPathEqualTo("/greek")));
	}
}

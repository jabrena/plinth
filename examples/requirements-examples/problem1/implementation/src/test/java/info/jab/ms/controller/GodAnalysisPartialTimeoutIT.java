package info.jab.ms.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import info.jab.ms.dto.GodStatsResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration-test")
class GodAnalysisPartialTimeoutIT {

	private static final WireMockServer WM;

	static {
		WM = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
		WM.start();
	}

	@DynamicPropertySource
	static void wireMockAndTimeouts(DynamicPropertyRegistry registry) {
		String base = "http://localhost:" + WM.port();
		registry.add("god.outbound.urls.greek", () -> base + "/greek");
		registry.add("god.outbound.urls.roman", () -> base + "/roman");
		registry.add("god.outbound.urls.nordic", () -> base + "/nordic");
		registry.add("god.outbound.connect-timeout", () -> "200ms");
		registry.add("god.outbound.read-timeout", () -> "200ms");
	}

	@AfterAll
	static void stopWireMock() {
		WM.stop();
	}

	@LocalServerPort
	private int port;

	private RestClient client;

	@BeforeEach
	void setUp() throws Exception {
		WM.resetAll();
		String greekBody = new String(
				new ClassPathResource("wiremock/greek-gods.json").getInputStream().readAllBytes(),
				StandardCharsets.UTF_8);
		WM.stubFor(
				get(urlPathEqualTo("/greek"))
						.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(greekBody)));
		WM.stubFor(
				get(urlPathEqualTo("/roman"))
						.willReturn(aResponse().withStatus(200).withFixedDelay(500).withBody("[]")));
		WM.stubFor(
				get(urlPathEqualTo("/nordic"))
						.willReturn(aResponse().withStatus(200).withFixedDelay(500).withBody("[]")));
		client = RestClient.builder().baseUrl("http://localhost:" + port).build();
	}

	@AfterEach
	void resetWireMock() {
		WM.resetAll();
	}

	@Test
	void nordicAndRomanDelayedBeyondReadTimeoutGreekSucceedsPinnedPartialSum() {
		GodStatsResponse body = client.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/gods/stats/sum")
						.queryParam("filter", "n")
						.queryParam("sources", "greek,roman,nordic")
						.build())
				.retrieve()
				.body(GodStatsResponse.class);

		assertThat(body).isNotNull();
		assertThat(body.sum()).isEqualTo("78101109179220212216");
	}
}

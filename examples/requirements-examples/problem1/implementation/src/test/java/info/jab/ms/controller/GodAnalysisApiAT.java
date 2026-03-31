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
@Tag("acceptance-test")
class GodAnalysisApiAT {

	private static final WireMockServer WM;

	static {
		WM = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
		WM.start();
	}

	@DynamicPropertySource
	static void wireMockUrls(DynamicPropertyRegistry registry) {
		String base = "http://localhost:" + WM.port();
		registry.add("god.outbound.urls.greek", () -> base + "/greek");
		registry.add("god.outbound.urls.roman", () -> base + "/roman");
		registry.add("god.outbound.urls.nordic", () -> base + "/nordic");
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
		stubJson("/greek", "wiremock/greek-gods.json");
		stubJson("/roman", "wiremock/roman-gods.json");
		stubJson("/nordic", "wiremock/nordic-gods.json");
		client = RestClient.builder().baseUrl("http://localhost:" + port).build();
	}

	private void stubJson(String path, String classpathUnderTestResources) throws Exception {
		String body = new String(
				new ClassPathResource(classpathUnderTestResources).getInputStream().readAllBytes(),
				StandardCharsets.UTF_8);
		WM.stubFor(
				get(urlPathEqualTo(path))
						.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body)));
	}

	@Test
	void happyPathReturnsExpectedSum() {
		GodStatsResponse body = client.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/gods/stats/sum")
						.queryParam("filter", "n")
						.queryParam("sources", "greek,roman,nordic")
						.build())
				.retrieve()
				.body(GodStatsResponse.class);

		assertThat(body).isNotNull();
		assertThat(body.sum()).isEqualTo("78179288397447443426");
	}
}

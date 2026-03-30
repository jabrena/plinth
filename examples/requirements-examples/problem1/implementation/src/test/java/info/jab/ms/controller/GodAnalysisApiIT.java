package info.jab.ms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import info.jab.ms.client.GodDataClient;
import info.jab.ms.dto.GodStatsResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration-test")
class GodAnalysisApiIT {

	@MockitoBean
	private GodDataClient godDataClient;

	@LocalServerPort
	private int port;

	private RestClient client;

	@BeforeEach
	void setUp() {
		client = RestClient.builder().baseUrl("http://localhost:" + port).build();
		when(godDataClient.fetchNames(anyString())).thenReturn(List.of("zeus"));
	}

	@Test
	void filterWithNoFirstLetterMatchReturnsZeroSum() {
		GodStatsResponse body = client.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/gods/stats/sum")
						.queryParam("filter", "N")
						.queryParam("sources", "greek,roman,nordic")
						.build())
				.retrieve()
				.body(GodStatsResponse.class);

		assertThat(body).isNotNull();
		assertThat(body.sum()).isEqualTo("0");
	}
}

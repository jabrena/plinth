package info.jab.ms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import info.jab.ms.client.GodDataClient;
import java.util.List;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GodAnalysisServiceTest {

	private static final Executor SYNC_EXECUTOR = Runnable::run;

	@Mock
	private GodDataClient godDataClient;

	private GodAnalysisService godAnalysisService;

	@BeforeEach
	void setUp() {
		godAnalysisService = new GodAnalysisService(godDataClient, SYNC_EXECUTOR);
	}

	@Test
	void aggregatesAcrossSourcesWithPinnedHappyPathSum() {
		when(godDataClient.fetchNames("greek")).thenReturn(List.of("Nike", "Nemesis"));
		when(godDataClient.fetchNames("roman")).thenReturn(List.of("Neptun"));
		when(godDataClient.fetchNames("nordic")).thenReturn(List.of("Njord"));

		String sum = godAnalysisService.sumForSources("n", List.of("greek", "roman", "nordic"));

		assertThat(sum).isEqualTo("78179288397447443426");
	}

	@Test
	void returnsZeroWhenNoNamesMatchFilter() {
		when(godDataClient.fetchNames("greek")).thenReturn(List.of("zeus"));

		String sum = godAnalysisService.sumForSources("N", List.of("greek"));

		assertThat(sum).isEqualTo("0");
	}
}

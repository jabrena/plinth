package info.jab.ms.gods.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreekGodsSyncServiceTest {

	@Mock(answer = RETURNS_DEEP_STUBS)
	private RestClient greekGodsSyncRestClient;

	@Mock(answer = RETURNS_DEEP_STUBS)
	private JdbcClient jdbcClient;

	@InjectMocks
	private GreekGodsSyncService syncService;

	@Test
	void syncFromUpstream_insertsNonBlankNames() {
		List<String> upstream = new ArrayList<>(List.of(" Zeus ", "", "  ", "Athena"));
		upstream.add(null);
		when(greekGodsSyncRestClient
						.get()
						.uri(anyString())
						.retrieve()
						.body(any(ParameterizedTypeReference.class)))
				.thenReturn(upstream);
		when(jdbcClient.sql(anyString()).param(anyString(), anyString()).update()).thenReturn(1);
		Mockito.clearInvocations(jdbcClient, greekGodsSyncRestClient);

		assertThatCode(() -> syncService.syncFromUpstream()).doesNotThrowAnyException();

		verify(jdbcClient, times(2)).sql(anyString());
	}

	@Test
	void syncFromUpstream_nullBody_doesNotTouchDatabase() {
		when(greekGodsSyncRestClient
						.get()
						.uri(anyString())
						.retrieve()
						.body(any(ParameterizedTypeReference.class)))
				.thenReturn(null);

		syncService.syncFromUpstream();

		verify(jdbcClient, never()).sql(anyString());
	}

	@Test
	void syncFromUpstream_emptyList_doesNotTouchDatabase() {
		when(greekGodsSyncRestClient
						.get()
						.uri(anyString())
						.retrieve()
						.body(any(ParameterizedTypeReference.class)))
				.thenReturn(List.of());

		syncService.syncFromUpstream();

		verify(jdbcClient, never()).sql(anyString());
	}

	@Test
	void syncFromUpstream_restClientFailure_doesNotPropagate() {
		when(greekGodsSyncRestClient.get()).thenThrow(new RuntimeException("upstream down"));

		assertThatCode(() -> syncService.syncFromUpstream()).doesNotThrowAnyException();
	}
}

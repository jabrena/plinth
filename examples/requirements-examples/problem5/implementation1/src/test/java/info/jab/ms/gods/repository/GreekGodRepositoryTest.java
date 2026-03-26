package info.jab.ms.gods.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreekGodRepositoryTest {

	@Mock(answer = RETURNS_DEEP_STUBS)
	private JdbcClient jdbcClient;

	@InjectMocks
	private GreekGodRepository repository;

	@Test
	void findAllByOrderByNameAsc_mapsRowsToRecords() {
		List<GreekGod> expected = List.of(new GreekGod(1L, "Apollo"), new GreekGod(2L, "Zeus"));
		when(jdbcClient.sql(anyString()).query(eq(GreekGod.class)).list()).thenReturn(expected);

		List<GreekGod> result = repository.findAllByOrderByNameAsc();

		assertThat(result).containsExactlyElementsOf(expected);
	}
}

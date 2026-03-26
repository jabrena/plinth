package info.jab.ms.gods.service;

import info.jab.ms.gods.repository.GreekGod;
import info.jab.ms.gods.repository.GreekGodRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreekGodsServiceTest {

	@Mock
	private GreekGodRepository repository;

	@InjectMocks
	private GreekGodsService service;

	@Test
	void findAllNamesOrdered_returnsNamesInRepositoryOrder() {
		when(repository.findAllByOrderByNameAsc())
				.thenReturn(List.of(new GreekGod(2L, "Zeus"), new GreekGod(1L, "Athena")));

		List<String> names = service.findAllNamesOrdered();

		assertThat(names).containsExactly("Zeus", "Athena");
		verify(repository).findAllByOrderByNameAsc();
	}

	@Test
	void findAllNamesOrdered_emptyRepository_returnsEmptyList() {
		when(repository.findAllByOrderByNameAsc()).thenReturn(List.of());

		assertThat(service.findAllNamesOrdered()).isEmpty();
	}
}

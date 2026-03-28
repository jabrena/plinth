package info.jab.ms.gods.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import info.jab.ms.gods.repository.GreekGodRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreekGodsControllerTest {

    @Mock
    GreekGodRepository repository;

    @InjectMocks
    GreekGodsController controller;

    @Test
    void getGreekGods_returnsNamesFromRepository() {
        when(repository.findAllNamesOrdered()).thenReturn(List.of("Athena", "Zeus"));
        assertThat(controller.getGreekGods()).containsExactly("Athena", "Zeus");
        verify(repository).findAllNamesOrdered();
    }
}

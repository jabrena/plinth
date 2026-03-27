package info.jab.ms.gods;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import info.jab.ms.gods.error.GreekGodsDataAccessException;
import info.jab.ms.gods.repository.GreekGodRepository;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreekGodRepositoryTest {

    @Test
    void findAllNamesOrdered_wrapsSqlException() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection c = mock(Connection.class);
        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenThrow(new SQLException("boom"));

        var repo = new GreekGodRepository(ds);
        assertThatThrownBy(repo::findAllNamesOrdered)
                .isInstanceOf(GreekGodsDataAccessException.class)
                .hasMessageContaining("Failed to load");
    }
}

package info.jab.ms;

import info.jab.ms.common.PostgreSQLTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Main Application Integration Tests")
class MainApplicationTest extends PostgreSQLTestBase {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}

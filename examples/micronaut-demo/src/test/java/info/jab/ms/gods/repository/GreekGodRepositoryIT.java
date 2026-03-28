package info.jab.ms.gods.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Integration test for the JDBC repository against a real PostgreSQL (Flyway + datasource as in production).
 */
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreekGodRepositoryIT implements TestPropertyProvider {

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRES.start();
    }

    @Inject
    GreekGodRepository repository;

    @Inject
    javax.sql.DataSource dataSource;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
                "datasources.default.url",
                POSTGRES.getJdbcUrl(),
                "datasources.default.username",
                POSTGRES.getUsername(),
                "datasources.default.password",
                POSTGRES.getPassword(),
                "greek-gods.sync.scheduler-enabled",
                "false",
                "greek-gods.upstream.base-url",
                "http://127.0.0.1:9");
    }

    @BeforeEach
    void cleanTable() throws Exception {
        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement()) {
            s.executeUpdate("TRUNCATE TABLE greek_god RESTART IDENTITY");
        }
    }

    @Test
    void upsertAndFindAll_ordersNamesAndIdempotentUpsert() {
        repository.upsertByName("Zeus");
        repository.upsertByName("Athena");
        repository.upsertByName("Zeus");
        assertThat(repository.findAllNamesOrdered()).containsExactly("Athena", "Zeus");
    }
}

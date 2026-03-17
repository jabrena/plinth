package info.jab.ms.common;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * PostgreSQLTestBase - Base class for integration tests with PostgreSQL TestContainer
 *
 * This class provides common setup for all integration tests that need
 * PostgreSQL database connectivity using TestContainers.
 *
 * Features:
 * - Shared PostgreSQL TestContainer for all test classes
 * - Schema and data applied via Flyway migrations (db/migration) on context startup
 * - Automatic Spring Boot configuration via @ServiceConnection
 * - Proper startup and shutdown lifecycle management
 * - Works with Spring Boot slice testing (@JdbcTest, @WebMvcTest, etc.)
 */
@Testcontainers(disabledWithoutDocker = true)
public abstract class PostgreSQLTestBase {

    @Container
    @ServiceConnection(name = "postgres")
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withStartupTimeout(Duration.ofMinutes(2))
            .withLogConsumer(outputFrame -> {
                System.out.print("[POSTGRES] " + outputFrame.getUtf8String());
            });

    protected static PostgreSQLContainer<?> getPostgresContainer() {
        return postgres;
    }
}

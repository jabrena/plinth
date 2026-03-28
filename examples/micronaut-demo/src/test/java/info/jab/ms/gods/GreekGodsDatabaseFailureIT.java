package info.jab.ms.gods;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.jab.ms.gods.repository.GreekGodsDataAccessException;
import info.jab.ms.gods.repository.GreekGodRepository;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Simulates read-path persistence failure (connection/query error) while keeping a valid datasource for
 * context startup (Flyway). Normative checks match ADR-001 / Gherkin {@code @error-handling}.
 */
@MicronautTest(rebuildContext = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreekGodsDatabaseFailureIT implements TestPropertyProvider {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRES.start();
    }

    @Inject
    EmbeddedServer server;

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

    @MockBean(GreekGodRepository.class)
    GreekGodRepository brokenRepository() {
        GreekGodRepository repo = mock(GreekGodRepository.class);
        when(repo.findAllNamesOrdered())
                .thenThrow(new GreekGodsDataAccessException("Database unavailable", new SQLException("connection refused")));
        return repo;
    }

    @BeforeEach
    void setupPort() {
        io.restassured.RestAssured.port = server.getPort();
    }

    @Test
    @Tag("error-handling")
    void get_returnsProblemJsonWithNormativeFields() throws Exception {
        var extract = given().when()
                .get("/api/v1/gods/greek")
                .then()
                .statusCode(500)
                .extract();
        assertThat(extract.header("Content-Type")).contains("application/problem+json");
        String json = extract.asString();

        JsonNode node = MAPPER.readTree(json);
        assertThat(node.get("type").asText()).isNotBlank();
        assertThat(node.get("title").asText()).isNotBlank();
        assertThat(node.get("status").asInt()).isEqualTo(500);
    }
}

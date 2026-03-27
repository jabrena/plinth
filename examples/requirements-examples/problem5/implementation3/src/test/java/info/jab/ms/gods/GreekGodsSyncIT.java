package info.jab.ms.gods;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.client.WireMock;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import info.jab.ms.gods.service.GreekGodsSyncService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreekGodsSyncIT implements TestPropertyProvider {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");
    static final WireMockServer WIRE_MOCK =
            new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    static {
        POSTGRES.start();
        WIRE_MOCK.start();
    }

    @Inject
    EmbeddedServer server;

    @Inject
    javax.sql.DataSource dataSource;

    @Inject
    GreekGodsSyncService syncService;

    @Override
    public Map<String, String> getProperties() {
        String upstream = "http://localhost:" + WIRE_MOCK.port();
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
                upstream);
    }

    @BeforeEach
    void cleanAndResetWireMock() throws Exception {
        WIRE_MOCK.resetAll();
        io.restassured.RestAssured.port = server.getPort();
        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement()) {
            s.executeUpdate("TRUNCATE TABLE greek_god RESTART IDENTITY");
        }
    }

    @Test
    @Tag("data-quality")
    void syncFromWireMock_thenGet_reflectsStubbedNames() throws Exception {
        WIRE_MOCK.stubFor(
                WireMock.get(urlEqualTo("/greek"))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .withBody("[\"AlphaWireMock\",\"ZetaWireMock\"]")));

        syncService.syncFromUpstream();

        String raw = given().when().get("/api/v1/gods/greek").then().statusCode(200).extract().asString();
        List<String> body = MAPPER.readValue(raw, new TypeReference<>() {});

        assertThat(body).containsExactly("AlphaWireMock", "ZetaWireMock");
    }
}

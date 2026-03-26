package info.jab.ms.gods;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.jab.ms.gods.service.GreekGodsSyncService;
import info.jab.ms.gods.testsupport.PostgresWireMockTestResource;
import io.agroal.api.AgroalDataSource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(value = PostgresWireMockTestResource.class, restrictToAnnotatedClass = true)
class GreekGodsSyncIT {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Inject
    AgroalDataSource dataSource;

    @Inject
    GreekGodsSyncService syncService;

    @BeforeEach
    void cleanAndResetWireMock() throws Exception {
        var wm = PostgresWireMockTestResource.wireMock;
        assertThat(wm).isNotNull();
        wm.resetAll();

        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement()) {
            s.executeUpdate("TRUNCATE TABLE greek_god RESTART IDENTITY");
        }
    }

    @Test
    @Tag("data-quality")
    void syncFromWireMock_thenGet_reflectsStubbedNames() throws Exception {
        var wm = PostgresWireMockTestResource.wireMock;
        wm.stubFor(
                get(urlEqualTo("/greek"))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .withBody("[\"AlphaWireMock\",\"ZetaWireMock\"]")));

        syncService.syncFromUpstream();

        String raw = given().when().get("/api/v1/gods/greek").then().statusCode(200).extract().asString();
        List<String> body = MAPPER.readValue(raw, new TypeReference<>() {});

        assertThat(body).containsExactly("AlphaWireMock", "ZetaWireMock");
    }
}

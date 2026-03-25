package info.jab.ms;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
public class GodAnalysisResourceTest {

    @BeforeEach
    void setUp() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    @Tag("acceptance-test")
    void shouldReturnGodWithMostLiterature() {
        // Stub Greek Gods API
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\",\"Zeus\"]"))
        );

        // Stub Wikipedia - Ares: 100 chars
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(100)))
        );

        // Stub Wikipedia - Zeus: 200 chars
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(200)))
        );

        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Zeus"))
            .body("characterCount", equalTo(200));
    }
}

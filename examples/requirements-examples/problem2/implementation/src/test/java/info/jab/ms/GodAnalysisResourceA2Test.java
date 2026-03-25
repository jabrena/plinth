package info.jab.ms;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * A2 Milestone verification tests - specific requirements from tasks 9-17
 */
@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
@Tag("acceptance-test")
public class GodAnalysisResourceA2Test {

    @BeforeEach
    void setUp() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    void shouldReturn503EmptyBodyWhenGreekAPIFails() {
        // Given - Greek API returns 500
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(500))
        );

        // When/Then - Should return 503 with empty body (no JSON Problem Details)
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(503)
            .body(emptyString());
    }

    @Test
    void shouldReturn503EmptyBodyWhenGreekAPITimesOut() {
        // Given - Greek API times out (longer than read timeout)
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(3000) // Longer than test timeout (2000ms)
                    .withBody("[\"Zeus\"]"))
        );

        // When/Then - Should return 503 with empty body
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(503)
            .body(emptyString());
    }

    @Test
    void shouldContinueWhenWikipediaFailsAndReturnCount0() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Zeus\",\"Ares\"]"))
        );

        // Zeus succeeds
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(100)))
        );

        // Ares fails (Wikipedia failure)
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(500))
        );

        // When/Then - Zeus should win with 100, Ares gets 0 due to failure
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Zeus"))
            .body("characterCount", equalTo(100));
    }

    @Test
    void shouldReturnTiedWinnersSortedNaturally() {
        // Given
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Zeus\",\"Ares\",\"Athena\"]"))
        );

        // All have same character count
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(150)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(150)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Athena"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("t".repeat(150)))
        );

        // When/Then - All tied, should return sorted naturally: Ares, Athena, Zeus
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Ares", "Athena", "Zeus"))
            .body("characterCount", equalTo(150));
    }

    @Test
    void shouldProcessWikipediaRequestsInParallel() {
        // Given - multiple gods with delays to test parallel processing
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Zeus\",\"Ares\",\"Athena\",\"Apollo\"]"))
        );

        // Each Wikipedia request has a 500ms delay
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(500)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(400)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(500)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(100)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Athena"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(500)
                    .withHeader("Content-Type", "text/html")
                    .withBody("t".repeat(200)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Apollo"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(500)
                    .withHeader("Content-Type", "text/html")
                    .withBody("p".repeat(300)))
        );

        // When
        long startTime = System.currentTimeMillis();
        
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Zeus"))
            .body("characterCount", equalTo(400));
            
        long duration = System.currentTimeMillis() - startTime;
        
        // Then - Should complete in less than 2 seconds due to parallel processing
        // (4 requests * 500ms = 2000ms if sequential, should be ~500ms if parallel)
        // Adding buffer for test execution overhead
        assert duration < 1500 : "Request took " + duration + "ms, expected < 1500ms for parallel execution";
    }
}
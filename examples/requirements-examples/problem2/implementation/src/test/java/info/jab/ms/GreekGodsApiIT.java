package info.jab.ms;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Greek Gods API retrieval functionality
 * Implements the @integration-test scenario from the feature file
 */
@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
@Tag("integration-test")
@DisplayName("Greek Gods API Integration")
public class GreekGodsApiIT {

    @BeforeEach
    void resetWireMock() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    @DisplayName("Scenario: Successfully retrieve the list of Greek gods from the external Greek Gods API")
    void successfullyRetrieveTheListOfGreekGodsFromTheExternalGreekGodsApi() {
        // Given: the Greek Gods API is available and returns a valid response
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\", \"Athena\", \"Apollo\", \"Artemis\"]"))
        );

        // And: Wikipedia pages are available for all gods
        String[] gods = {"Ares", "Zeus", "Athena", "Apollo", "Artemis"};
        int[] characterCounts = {100, 200, 150, 300, 250};
        
        for (int i = 0; i < gods.length; i++) {
            WireMockQuarkusResource.getWireMockServer().stubFor(
                get(urlEqualTo("/wiki/" + gods[i]))
                    .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody("x".repeat(characterCounts[i])))
            );
        }

        // When: the system calls the Greek Gods API (HTTP GET to the configured Greek Gods URL)
        // Then: the response body is a non-empty JSON array of Greek god names
        // And: the system successfully processes all gods and returns the winner
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", hasSize(1))
            .body("gods", contains("Apollo"))
            .body("characterCount", equalTo(300));

        // Verify that the Greek Gods API was called exactly once
        WireMockQuarkusResource.getWireMockServer().verify(1,
            getRequestedFor(urlEqualTo("/jabrena/latency-problems/greek")));

        // Verify that Wikipedia was called for each god
        for (String god : gods) {
            WireMockQuarkusResource.getWireMockServer().verify(1,
                getRequestedFor(urlEqualTo("/wiki/" + god)));
        }
    }
}
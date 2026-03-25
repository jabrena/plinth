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
 * Acceptance tests implementing scenarios from US-001-Greek-Gods-Wikipedia-Information.feature
 * These tests verify the complete end-to-end behavior per Gherkin scenarios tagged @acceptance-test
 */
@QuarkusTest
@QuarkusTestResource(WireMockQuarkusResource.class)
@Tag("acceptance-test")
@DisplayName("Greek Gods Wikipedia Information")
public class GreekGodsWikipediaInformationAT {

    @BeforeEach
    void resetWireMock() {
        WireMockQuarkusResource.getWireMockServer().resetAll();
    }

    @Test
    @DisplayName("Scenario: Identify the Greek god with the most literature on Wikipedia")
    void identifyTheGreekGodWithTheMostLiteratureOnWikipedia() {
        // Given: I have the list of Greek gods
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\", \"Athena\"]"))
        );

        // And: I retrieve the Wikipedia page for each god
        // And: I calculate the character length of each god's Wikipedia page content
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(150)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(300)))
        );

        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Athena"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("t".repeat(200)))
        );

        // When: I request the most literature analysis
        // Then: I should be presented with a list of god name(s) that have the most literature, along with their character count
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Zeus"))
            .body("characterCount", equalTo(300));
    }

    @Test
    @DisplayName("Scenario: Identify the winner with known stub data")
    void identifyTheWinnerWithKnownStubData() {
        // Given: the Greek Gods API returns the list: "Ares", "Zeus"
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\"]"))
        );

        // And: the Wikipedia page for "Ares" contains exactly 100 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(100)))
        );

        // And: the Wikipedia page for "Zeus" contains exactly 200 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(200)))
        );

        // When: I request GET /api/v1/gods/wikipedia/most-literature
        // Then: the response status is 200
        // And: the response body contains gods: ["Zeus"] with characterCount: 200
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Zeus"))
            .body("characterCount", equalTo(200));
    }

    @Test
    @DisplayName("Scenario: Handle an unavailable Wikipedia page with zero character count")
    void handleAnUnavailableWikipediaPageWithZeroCharacterCount() {
        // Given: the Greek Gods API returns the list: "Ares", "Zeus"
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\"]"))
        );

        // And: the Wikipedia page for "Ares" contains exactly 150 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(150)))
        );

        // And: the Wikipedia page for "Zeus" cannot be retrieved
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(404))
        );

        // When: I request GET /api/v1/gods/wikipedia/most-literature
        // Then: the response status is 200
        // And: the response body contains gods: ["Ares"] with characterCount: 150
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Ares"))
            .body("characterCount", equalTo(150));
    }

    @Test
    @DisplayName("Scenario: Handle a tie between multiple gods")
    void handleATieBetweenMultipleGods() {
        // Given: the Greek Gods API returns the list: "Ares", "Zeus", "Athena"
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\", \"Athena\"]"))
        );

        // And: the Wikipedia page for "Ares" contains exactly 300 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(300)))
        );

        // And: the Wikipedia page for "Zeus" contains exactly 300 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(300)))
        );

        // And: the Wikipedia page for "Athena" contains exactly 200 characters
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Athena"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("t".repeat(200)))
        );

        // When: I request GET /api/v1/gods/wikipedia/most-literature
        // Then: the response status is 200
        // And: the response body contains gods: ["Ares", "Zeus"] with characterCount: 300
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Ares", "Zeus"))
            .body("characterCount", equalTo(300));
    }

    @Test
    @DisplayName("Scenario: Return HTTP 503 when the Greek Gods API is unavailable")
    void returnHttp503WhenTheGreekGodsApiIsUnavailable() {
        // Given: the Greek Gods API is unavailable
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(503))
        );

        // When: I request GET /api/v1/gods/wikipedia/most-literature
        // Then: the response status is 503
        // And: the response body is empty
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(503)
            .body(emptyString());
    }

    @Test
    @DisplayName("Scenario: Wikipedia delay exceeds read timeout - counts as zero")
    void wikipediaDelayExceedsReadTimeoutCountsAsZero() {
        // Given: the Greek Gods API returns the list: "Ares", "Zeus"
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/jabrena/latency-problems/greek"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("[\"Ares\", \"Zeus\"]"))
        );

        // And: the Wikipedia page for "Ares" responds normally
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Ares"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/html")
                    .withBody("a".repeat(100)))
        );

        // And: the Wikipedia page for "Zeus" has a delay > read timeout (2000ms)
        WireMockQuarkusResource.getWireMockServer().stubFor(
            get(urlEqualTo("/wiki/Zeus"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(3000) // Exceeds 2000ms read timeout
                    .withHeader("Content-Type", "text/html")
                    .withBody("z".repeat(200)))
        );

        // When: I request GET /api/v1/gods/wikipedia/most-literature
        // Then: Zeus timeout should count as 0, Ares should win with 100
        given()
            .when()
            .get("/api/v1/gods/wikipedia/most-literature")
            .then()
            .statusCode(200)
            .body("gods", contains("Ares"))
            .body("characterCount", equalTo(100));
    }
}
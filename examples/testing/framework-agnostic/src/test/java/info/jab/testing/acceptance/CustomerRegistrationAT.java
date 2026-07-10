package info.jab.testing.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRegistrationAT extends BaseAcceptanceTest {

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Scenario: successful POST to /api/customers returns 201")
    void scenario_successful_customer_registration() throws SQLException {
        // Given: the third-party customer-risk REST dependency approves the customer
        customerRisk.stubFor(post(urlEqualTo("/risk-assessments"))
            .withRequestBody(containing("ada@example.com"))
            .willReturn(okJson("""
                {"decision":"APPROVED"}
                """)));

        // When: POST /api/customers with a valid customer registration payload
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"ada@example.com","name":"Ada Lovelace"}
                """)
            .when()
            .post("/api/customers")
            .then()
            // Then: response status is 201 and the customer is persisted
            .statusCode(201)
            .body("id", notNullValue())
            .body("email", equalTo("ada@example.com"))
            .body("name", equalTo("Ada Lovelace"))
            .body("riskDecision", equalTo("APPROVED"));

        customerRisk.verify(postRequestedFor(urlEqualTo("/risk-assessments")));
        assertEquals(1, database.customerCount());
    }
}

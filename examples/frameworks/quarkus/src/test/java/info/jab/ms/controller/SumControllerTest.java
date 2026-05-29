package info.jab.ms.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class SumControllerTest {

    @Test
    void sumReturns400WhenParam1IsMissing() {
        given()
                .contentType("application/json")
                .body("{\"param2\": 1}")
                .when()
                .post("/api/v1/sum")
                .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("title", is("Bad Request"))
                .body("status", is(400))
                .body("violations[0].field", is("param1"))
                .body("violations[0].in", is("body"))
                .body("violations[0].message", is("must not be null"));
    }

    @Test
    void sumReturns400WhenParam2IsNull() {
        given()
                .contentType("application/json")
                .body("{\"param1\": 1, \"param2\": null}")
                .when()
                .post("/api/v1/sum")
                .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("title", is("Bad Request"))
                .body("status", is(400))
                .body("violations[0].field", is("param2"))
                .body("violations[0].in", is("body"))
                .body("violations[0].message", is("must not be null"));
    }

    @Test
    void sumReturnsResultWhenRequestIsValid() {
        given()
                .contentType("application/json")
                .body("{\"param1\": 10, \"param2\": 32}")
                .when()
                .post("/api/v1/sum")
                .then()
                .statusCode(200)
                .body("result", is(42));
    }

    @Test
    void sumReturnsZeroWhenBothParamsAreZero() {
        given()
                .contentType("application/json")
                .body("{\"param1\": 0, \"param2\": 0}")
                .when()
                .post("/api/v1/sum")
                .then()
                .statusCode(200)
                .body("result", is(0));
    }

    @Test
    void sumHandlesNegativeValues() {
        given()
                .contentType("application/json")
                .body("{\"param1\": -5, \"param2\": 8}")
                .when()
                .post("/api/v1/sum")
                .then()
                .statusCode(200)
                .body("result", is(3));
    }

}

package info.jab.ms.gods;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(UnreachableDatabaseTestProfile.class)
class GreekGodsDatabaseFailureIT {

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

package info.jab.ms.gods;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.jab.ms.gods.testsupport.PostgresTestResource;
import io.agroal.api.AgroalDataSource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(value = PostgresTestResource.class, restrictToAnnotatedClass = true)
class GreekGodsApiIT {

    static final Set<String> CANONICAL_20 = Set.of(
            "Zeus",
            "Hera",
            "Poseidon",
            "Demeter",
            "Ares",
            "Athena",
            "Apollo",
            "Artemis",
            "Hephaestus",
            "Aphrodite",
            "Hermes",
            "Dionysus",
            "Hades",
            "Hypnos",
            "Nike",
            "Janus",
            "Nemesis",
            "Iris",
            "Hecate",
            "Tyche");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Inject
    AgroalDataSource dataSource;

    @BeforeEach
    void truncate() throws Exception {
        try (Connection c = dataSource.getConnection();
                Statement s = c.createStatement()) {
            s.executeUpdate("TRUNCATE TABLE greek_god RESTART IDENTITY");
        }
    }

    private void seedCanonicalTwenty() throws Exception {
        try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO greek_god (name) VALUES (?)")) {
            for (String name : CANONICAL_20) {
                ps.setString(1, name);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<String> getGreekGods() throws Exception {
        String raw = given().when().get("/api/v1/gods/greek").then().statusCode(200).extract().asString();
        return MAPPER.readValue(raw, new TypeReference<>() {});
    }

    @Test
    @Tag("smoke")
    @Tag("happy-path")
    void smokeAndHappyPath_returnsTwentyNamesOrderedWithCanonicalSet() throws Exception {
        seedCanonicalTwenty();
        List<String> body = getGreekGods();
        assertThat(body).hasSize(20);
        assertThat(new HashSet<>(body)).isEqualTo(CANONICAL_20);
        assertThat(body).isSortedAccordingTo(String::compareTo);
    }

    @Test
    @Tag("error-handling")
    void emptyDatabase_returnsEmptyJsonArray() throws Exception {
        List<String> body = getGreekGods();
        assertThat(body).isEmpty();
    }

    @Test
    @Tag("performance")
    void singleGet_completesWithinOneSecond() throws Exception {
        seedCanonicalTwenty();
        Instant start = Instant.now();
        given().when().get("/api/v1/gods/greek").then().statusCode(200);
        assertThat(Duration.between(start, Instant.now())).isLessThan(Duration.ofSeconds(1));
    }

    @Test
    @Tag("performance")
    @Tag("load-testing")
    void hundredConcurrentGets_completeWithinTwoSecondsWallClock() throws Exception {
        seedCanonicalTwenty();
        ExecutorService pool = Executors.newFixedThreadPool(100);
        try {
            CountDownLatch latch = new CountDownLatch(100);
            Instant start = Instant.now();
            for (int i = 0; i < 100; i++) {
                pool.submit(
                        () -> {
                            try {
                                List<String> body = getGreekGods();
                                assertThat(body).hasSize(20);
                                assertThat(new HashSet<>(body)).isEqualTo(CANONICAL_20);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            } finally {
                                latch.countDown();
                            }
                        });
            }
            assertThat(latch.await(2, TimeUnit.MINUTES)).isTrue();
            assertThat(Duration.between(start, Instant.now())).isLessThanOrEqualTo(Duration.ofSeconds(2));
        } finally {
            pool.shutdown();
            assertThat(pool.awaitTermination(30, TimeUnit.SECONDS)).isTrue();
        }
    }

    @Test
    @Tag("api-specification")
    void get_returnsJsonArrayOfStrings() throws Exception {
        seedCanonicalTwenty();
        String contentType = given().when()
                .get("/api/v1/gods/greek")
                .then()
                .statusCode(200)
                .extract()
                .header("Content-Type");
        assertThat(contentType).contains("application/json");
        List<String> parsed = getGreekGods();
        assertThat(parsed).hasSize(20);
        assertThat(parsed.getFirst()).isInstanceOf(String.class);
    }
}

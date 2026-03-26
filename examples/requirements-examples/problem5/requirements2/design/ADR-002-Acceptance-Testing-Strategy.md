# ADR-002: Acceptance Testing Strategy for Greek Gods Data Synchronization API Platform

**Date:** 2024-12-19  
**Amended:** 2026-03-26 — Stack moved to **Quarkus**; HTTP-level **acceptance tests use REST Assured** with **`@QuarkusTest`** (required for this platform). **Quarkus** baseline **3.x** (aligned with [ADR-003](./ADR-003-Greek-Gods-API-Technology-Stack.md) and the implementation `pom.xml`).  
**Status:** Accepted  
**Deciders:** Development Team, QA Team  
**Consulted:** Product Owner, DevOps Team  
**Informed:** Stakeholders, Educational Platform Integration Partners

---

## Context and Problem Statement

The Greek Gods Data Synchronization API Platform requires a comprehensive acceptance testing strategy to ensure reliable delivery of mythology data to educational applications. With sub-second performance requirements, external database dependencies, and the need for 99.9% uptime, we must establish clear testing practices that validate both functional requirements and non-functional characteristics.

### Current Situation
- **Technology Stack:** Quarkus **3.x** REST API (stay on the **3.x** line per implementation `pom.xml` unless ADR-002 and ADR-003 are updated together)
- REST API serving Greek god data via `/api/v1/gods/greek` endpoint
- PostgreSQL database with background synchronization service
- Educational platform consumers expecting reliable, fast access
- OpenAPI 3.0.3 specification defining contract
- Performance requirements: <1 second response time, 99.9% uptime
- **Testing Approach:** Developer-driven acceptance and integration testing using **REST Assured** against the Quarkus test runtime (`@QuarkusTest`), with **AssertJ** (and optional JSON assertions) for response validation

### Key Testing Challenges
1. **Data Synchronization Validation** - Ensuring consistency between external sources and local database
2. **Performance Under Load** - Validating concurrent request handling and response times
3. **Error Handling** - Graceful degradation when dependencies fail
4. **API Contract Compliance** - Ensuring responses match OpenAPI specification
5. **Availability Monitoring** - Continuous validation of uptime requirements

---

## Decision Drivers

- **Educational Platform Integration Requirements** - External consumers need reliable, predictable API behavior
- **Performance SLA** - Sub-second response times are critical for user experience
- **Data Quality Assurance** - Mythology content must be accurate and complete
- **High Availability Needs** - 99.9% uptime requirement for production systems
- **Team Velocity** - Testing strategy must support rapid development without bottlenecks
- **Maintainability** - Tests should be readable and maintainable by developers
- **Quarkus Ecosystem Integration** - Leverage Quarkus testing extensions, CDI test injection, and documented patterns (`@QuarkusTest`, Testcontainers via `QuarkusTestResourceLifecycleManager` where needed)

---

## Considered Options

### Option 1: Manual Testing Only
**Pros:** Low initial setup cost, flexible exploration  
**Cons:** Not scalable, error-prone, slow feedback, doesn't support CI/CD

### Option 2: Unit Tests + Integration Tests Only
**Pros:** Fast execution, good coverage of individual components  
**Cons:** Doesn't validate end-to-end behavior, misses integration issues

### Option 3: REST Assured with `@QuarkusTest` (Selected)
**Pros:** End-to-end validation over real HTTP; aligns with common Quarkus documentation and examples; fluent assertions on status, headers, and body; works with **AssertJ** for complex expectations  
**Cons:** Adds a test-scoped dependency (`io.rest-assured:rest-assured`), which is standard for Quarkus HTTP tests

---

## Decision Outcome

**Chosen Option:** **REST Assured** under **`@QuarkusTest`** for all HTTP-level **acceptance** tests on this platform (no substitute client for that layer).

We will implement acceptance-style API tests by calling the application on its Quarkus test HTTP port using **REST Assured** (`given()` / `when()` / `then()`), configured with the test base URI (see Quarkus testing guide: default test port or `@TestHTTPResource` where appropriate). Assertions combine REST Assured matchers and **AssertJ** where useful (**JSONAssert** or **Jackson** `JsonNode` optional).

**Note on `@QuarkusTest` vs REST Client unit slices:** **`@InjectMock` / client-only tests** are appropriate for isolated REST Client or resource unit tests. **Full-stack** acceptance tests for this platform use **`@QuarkusTest`** and real HTTP to the running application, not mocked server adapters alone.

### Implementation Strategy

#### 1. **HTTP integration tests (REST Assured)**
- **Framework:** `quarkus-junit5` (and `quarkus-jacoco` if coverage is centralized in CI)
- **API Testing:** HTTP request/response via **REST Assured** (`io.rest-assured:rest-assured`)
- **Test Pattern:** Integration tests following `*IT.java` naming convention (Maven Failsafe) where the build uses a unit vs integration split; otherwise team convention
- **Approach:** Standard Java test methods with descriptive names
- **Coverage:** User story scenarios implemented as individual test methods

#### 2. **Test categories and tags**
Tags align with [US-001 Gherkin scenarios](../agile/US-001_api_greek_gods_data_retrieval.feature). Map them to JUnit 5 **`@Tag`** names in step definitions or generated glue code (names must match for filtering in Maven/Gradle):

```java
@Tag("smoke")               // Happy path / core retrieval
@Tag("happy-path")          // Used with smoke on the main success scenario
@Tag("performance")        // Latency budget; may combine with load-testing
@Tag("error-handling")     // DB unavailable, empty DB, etc.
@Tag("load-testing")       // Concurrent request scenarios
@Tag("data-quality")      // Sync / external format consistency
@Tag("api-specification")  // OpenAPI contract checks
@Tag("availability")      // Uptime / error-rate style checks
```

#### 3. **Testing Layers**

**Acceptance layer (`@QuarkusTest` + REST Assured):**
- End-to-end API testing via HTTP against the running Quarkus application in test mode
- Full application CDI and extension startup
- Database state validation using repositories, JDBC, or Flyway-managed schema plus seeded data
- Response format and content verification with **REST Assured** and/or **AssertJ** (and JSON helpers)
- **500** responses validated as **RFC 7807** Problem Details (`application/problem+json`, `type`, `title`, `status`, and typically `detail`)
- Error scenario simulation with **`%test`** profile properties, Testcontainers lifecycle, or network controls

**Performance layer:**
- Response time measurement (<1 second requirement) using `System.nanoTime()` or `StopWatch` around the HTTP call, or Micrometer metrics where enabled
- JMeter integration for load-testing scenarios where the team adopts JMeter for those tags
- **SmallRye Metrics** / **Micrometer** (if enabled) for observability in test and prod

**Contract layer:**
- OpenAPI specification validation via dedicated tools or JSON schema assertions on sample responses
- Optional **REST Assured** JSON schema validation or Quarkus OpenAPI artifact checks
- HTTP status code and header verification via **REST Assured** (`then().statusCode()`, `header()`, `contentType()`, etc.)

#### 4. **Test Environment Strategy**

**Integration test environment:**
- **`@QuarkusTest`** for full application bootstrap
- **PostgreSQL** via **Testcontainers** with a **`QuarkusTestResourceLifecycleManager`** (or equivalent) that wires `quarkus.datasource.*` to the container, **or** **Dev Services** for PostgreSQL in `%test` if the team standardizes on that—keep ADR-003 and the module README aligned
- Test **`application.properties`** / **`application-test.properties`** (or YAML) for scenario-specific configuration
- **REST Assured:** set `RestAssured.baseURI` / `port` in `@BeforeEach` or a static initializer from `TestHTTPResource` / configured test port (follow Quarkus version docs for the exact test URL or port injection)

**CI/CD integration:**
- Integration tests (`*IT.java`) executed in Maven Failsafe (or Gradle equivalent) when split from Surefire
- Smoke tests on every commit via `@Tag("smoke")`
- Performance tests on nightly builds via `@Tag("performance")`
- Fail-fast strategy for critical error handling scenarios

#### 5. **Test Data Management**

**Quarkus test data strategy:**
- **Flyway** migrations plus optional **`test`**-scoped migration locations or seed scripts for canonical 20 Greek god names
- **Testcontainers** for isolated database state per CI run
- **`%test` profile** properties for different data scenarios (normal, empty DB, failure simulation)
- **Transactional tests:** use **`@Transactional`** only where the persistence stack supports it (e.g. Panache/JTA); for pure JDBC, prefer explicit cleanup or container-per-class isolation

**Test scenarios:**
- Static test data for happy path scenarios
- Empty database testing with dedicated **`%test`** configuration
- Connection failure simulation using Testcontainers network controls or invalid JDBC URL profile

---

## Rationale

This strategy addresses our specific challenges:

1. **Educational Platform Reliability** - Real HTTP calls against the running app match consumer experience
2. **Performance Validation** - Explicit timing around client calls plus metrics where enabled
3. **Data Quality Assurance** - Database state validation using the same persistence APIs as production
4. **Developer Integration** - Patterns match Quarkus guides and extensions
5. **Standard HTTP tooling** - **REST Assured** is the mandated stack for acceptance-style HTTP tests on this platform

### Why REST Assured (Quarkus context)

- **Documented pairing** with **`@QuarkusTest`** in Quarkus ecosystem materials
- **Readable** status/header/body assertions for API acceptance tests
- **Developer productivity** - Familiar to teams doing Jakarta REST / Quarkus REST development
- **Ecosystem integration** - Works alongside JUnit 5, Testcontainers, and Maven/Gradle lifecycle

The prior Spring-specific choice (**`RestClient` / `TestRestTemplate` only**) avoided duplicating HTTP client APIs already provided by Spring Web; on Quarkus, **REST Assured** is the conventional **test-side** HTTP library for black-box API verification, complementing **MicroProfile REST Client** / **Quarkus REST** used in application code.

---

## Implementation Plan

Sprint numbering below is **delivery sequencing** for the test suite (what to build when). It is **not** a split of Gherkin/JUnit tags—those are listed together in **§2 Test categories and tags**.

### Sprint 1: Core scenarios
- [ ] Configure **REST Assured** (`baseURI` / `port`) for the Quarkus test HTTP port
- [ ] Implement `GreekGodsApiIT.java` with 3 core test methods:
  - `testSuccessfullyRetrieveCompleteListOfGreekGodNames()` - Happy path validation
  - `testApiResponseTimeMeetsPerformanceRequirements()` - Response time validation
  - `testHandleDatabaseConnectionFailureGracefully()` - Database failure handling
- [ ] Set up Testcontainers for PostgreSQL (or Dev Services + documented equivalent)
- [ ] Integrate `@Tag("smoke")` tests into CI pipeline

### Sprint 2: Extended coverage
- [ ] Add remaining test scenarios based on original requirements
- [ ] Implement load testing with JMeter integration
- [ ] Add data quality validation test methods
- [ ] Set up test reporting dashboard

### Sprint 3: Advanced validation
- [ ] OpenAPI contract testing (schema or dedicated validator) against captured responses
- [ ] Availability monitoring integration
- [ ] Performance optimization for test execution
- [ ] CI/CD pipeline refinement

---

## Success Metrics

### Test Coverage
- US-001 Gherkin scenarios have corresponding automated coverage; JUnit **`@Tag`** names match the Gherkin tags listed in §2 above
- All API endpoints validated through HTTP integration tests implemented with **REST Assured**
- Error scenarios coverage for database connectivity issues

### Performance Validation
- Response time validation: 100% of requests <1 second (measured around client calls)
- Explicit timing assertions for performance requirements
- Metrics extensions (if enabled) for monitoring

### Quality Indicators
- Zero production bugs related to tested scenarios
- Fast feedback: Integration test results within 5 minutes of code commit
- Test maintenance: Minimal overhead due to Quarkus testing integration

---

## Technical Implementation Details

### Required Dependencies (Maven)
```xml
<dependencies>
    <!-- Quarkus JUnit 5 integration -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- REST Assured — required for HTTP acceptance tests on this platform -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Optional: JSONAssert for flexible JSON comparisons -->
    <dependency>
        <groupId>org.skyscreamer</groupId>
        <artifactId>jsonassert</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Testcontainers + PostgreSQL (if not using Dev Services only) -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Use **`quarkus-bom`** to align Quarkus artifact versions; add **`testcontainers-bom`** if your build manages Testcontainers centrally.

### Test Structure
```
src/test/java/
├── info/
│   └── jab/
│       └── latency/
│           ├── GreekGodsApiIT.java         # Main integration test class
│           └── PostgresTestResource.java    # Optional QuarkusTestResourceLifecycleManager
└── resources/
    ├── application.properties              # %test overrides
    └── test-data/
        └── greek-gods-seed.sql             # Optional seed (if not Flyway-only)
```

### Sample Test Implementation (REST Assured)

```java
package info.jab.latency;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@Tag("integration")
class GreekGodsApiIT {

    @BeforeEach
    void setUp() {
        io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Tag("smoke")
    void testSuccessfullyRetrieveCompleteListOfGreekGodNames() {
        String body = given()
                .when().get("/api/v1/gods/greek")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().body().asString();

        assertThat(body).isNotBlank();
        assertThat(body).contains("Zeus", "Hera", "Poseidon");
    }

    @Test
    @Tag("performance")
    void testApiResponseTimeMeetsPerformanceRequirements() {
        long start = System.nanoTime();
        given()
                .when().get("/api/v1/gods/greek")
                .then()
                .statusCode(200);
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        assertThat(elapsedMs).isLessThan(1000L);
    }

    @Test
    @Tag("error-handling")
    void testHandleDatabaseConnectionFailureGracefully() {
        // Simulate failure via %test profile, stopped container, or invalid datasource URL
        // (exact mechanism depends on PostgresTestResource / Testcontainers setup)
        given()
                .when().get("/api/v1/gods/greek")
                .then()
                .statusCode(500)
                .contentType(containsString("application/problem+json"))
                .body(containsString("\"status\":500"))
                .body(containsString("\"title\""))
                .body(containsString("\"type\""));
    }
}
```

Wire **Testcontainers** to Quarkus using a **`QuarkusTestResourceLifecycleManager`** that starts **PostgreSQL** and returns **`quarkus.datasource.jdbc.url`**, username, and password—see the [Quarkus Testcontainers guide](https://quarkus.io/guides/tests-with-testcontainers).

---

## Risks and Mitigation

### Risk: TestContainers Startup Time
**Mitigation:** Singleton container pattern via shared `QuarkusTestResourceLifecycleManager`, parallel test execution tuning

### Risk: Quarkus Test Application Startup Time
**Mitigation:** `@QuarkusTest` **profile** narrowing, avoid pulling unnecessary extensions into the test module

### Risk: Complex JSON assertions in REST Assured
**Mitigation:** Hamcrest/`JsonPath`, **JSONAssert**, **AssertJ** on extracted bodies, or deserialize to `List<String>` / DTOs with Jackson

### Risk: Test Method Naming and Organization
**Mitigation:** Clear naming conventions, logical test grouping, comprehensive documentation

---

## Related Decisions

- **[ADR-003: Technology Stack](./ADR-003-Greek-Gods-API-Technology-Stack.md):** Runtime, JDBC/Flyway, outbound MicroProfile REST Client, and optional WireMock—keep production and test HTTP clients aligned with that ADR
- **API versioning:** Affects base paths used in **REST Assured** requests
- **Performance requirements:** Drive timing assertions around HTTP calls

---

## References

- [US-001 User Story](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)
- [US-001 Requirements](../agile/US-001_api_greek_gods_data_retrieval.feature)
- [EPIC-001 Greek Gods Data Platform](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md)
- [Quarkus Testing](https://quarkus.io/guides/getting-started-testing)
- [Quarkus — Tests with Testcontainers](https://quarkus.io/guides/tests-with-testcontainers)
- [REST Assured](https://rest-assured.io/)
- [TestContainers Documentation](https://www.testcontainers.org/)

---

**Last Updated:** 2026-03-26  
**Next Review:** 2026-06-26  
**Review Trigger:** Major feature additions or performance requirement changes

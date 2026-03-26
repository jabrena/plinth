# ADR-002: Acceptance Testing Strategy for Greek Gods Data Synchronization API Platform

**Date:** 2024-12-19  
**Amended:** 2026-03-26 — Rest Assured removed; **Spring `RestClient`** (primary) and **`TestRestTemplate`** (alternative) for HTTP-level acceptance tests. **Spring Boot** baseline **4.0.4** (aligned with [ADR-003](./ADR-003-Greek-Gods-API-Technology-Stack.md)).  
**Status:** Accepted  
**Deciders:** Development Team, QA Team  
**Consulted:** Product Owner, DevOps Team  
**Informed:** Stakeholders, Educational Platform Integration Partners

---

## Context and Problem Statement

The Greek Gods Data Synchronization API Platform requires a comprehensive acceptance testing strategy to ensure reliable delivery of mythology data to educational applications. With sub-second performance requirements, external database dependencies, and the need for 99.9% uptime, we must establish clear testing practices that validate both functional requirements and non-functional characteristics.

### Current Situation
- **Technology Stack:** Spring Boot **4.0.4** REST API (stay on **4.0.x** per implementation `pom.xml` unless both ADRs are updated together)
- REST API serving Greek god data via `/api/v1/gods/greek` endpoint
- PostgreSQL database with background synchronization service
- Educational platform consumers expecting reliable, fast access
- OpenAPI 3.0.3 specification defining contract
- Performance requirements: <1 second response time, 99.9% uptime
- **Testing Approach:** Developer-driven integration testing using **Spring `RestClient`** or **`TestRestTemplate`** against a real listening port (`RANDOM_PORT`), with **AssertJ** (and optional JSON assertions) for response validation—**no Rest Assured**

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
- **Spring Boot Ecosystem Integration** - Leverage existing Spring Boot testing infrastructure; **single HTTP stack** (`spring-web`) for app and tests

---

## Considered Options

### Option 1: Manual Testing Only
**Pros:** Low initial setup cost, flexible exploration  
**Cons:** Not scalable, error-prone, slow feedback, doesn't support CI/CD

### Option 2: Unit Tests + Integration Tests Only
**Pros:** Fast execution, good coverage of individual components  
**Cons:** Doesn't validate end-to-end behavior, misses integration issues

### Option 3: Rest Assured for HTTP acceptance tests
**Pros:** Fluent HTTP DSL, widely known  
**Cons:** Extra dependency and Groovy-based internals; on Java 21+ some proxy/meta-property paths have proven brittle; duplicates the HTTP client story versus Spring’s own APIs

### Option 4: Spring `RestClient` or `TestRestTemplate` with `@SpringBootTest(webEnvironment = RANDOM_PORT)` (Selected)
**Pros:** End-to-end validation over real HTTP; **no** extra HTTP test library; aligns with production use of Spring Web; works with **AssertJ** and standard JSON tooling  
**Cons:** Slightly more verbose than a BDD-style DSL unless helpers are introduced

---

## Decision Outcome

**Chosen Option:** **Spring `RestClient`** (preferred) with **`TestRestTemplate`** as an equivalent alternative, both under **`@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`**.

We will implement acceptance-style API tests by calling the application on its random local port using **`RestClient`** (Spring Framework 6.1+) or, where the team prefers the older API, **`TestRestTemplate`**. Assertions use **AssertJ** and, where useful, **JSONAssert** or **Jackson** `JsonNode`—not Rest Assured.

**Note on `@RestClientTest`:** That annotation is a **test slice** for isolated client/contract tests. **Full-stack** acceptance tests for this platform use **`@SpringBootTest` + `RANDOM_PORT`** and a manually built **`RestClient`** (base URL `http://localhost:{port}`) or injected **`TestRestTemplate`**, not `@RestClientTest` alone.

### Implementation Strategy

#### 1. **HTTP integration tests (RestClient or TestRestTemplate)**
- **Framework:** `spring-boot-starter-test` only (plus Testcontainers as needed)
- **API Testing:** HTTP request/response via **`RestClient`** or **`TestRestTemplate`**
- **Test Pattern:** Integration tests following `*IT.java` naming convention
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

**Acceptance layer (`RestClient` / `TestRestTemplate` + Spring Boot Test):**
- End-to-end API testing via HTTP against the running embedded server
- Spring Boot test context for full application startup
- Database state validation using Spring Data repositories or JDBC helpers
- Response format and content verification with AssertJ (and JSON helpers)
- **500** responses validated as **RFC 7807** Problem Details (`application/problem+json`, `type`, `title`, `status`, and typically `detail`)
- Error scenario simulation with test profiles

**Performance layer:**
- Response time measurement (<1 second requirement) using `System.nanoTime()` or `StopWatch` around the HTTP call, or Micrometer/test metrics
- JMeter integration for load-testing scenarios where the team adopts JMeter for those tags
- Spring Boot Actuator metrics integration

**Contract layer:**
- OpenAPI specification validation via dedicated tools or JSON schema assertions on sample responses
- Optional **MockMvc** for in-process contract checks where full server spin-up is not required
- HTTP status code and header verification via `RestClient`/`TestRestTemplate` response types

#### 4. **Test Environment Strategy**

**Integration test environment:**
- Spring Boot Test with `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`
- Testcontainers for PostgreSQL database isolation
- Test profiles for different scenarios (normal, error conditions)
- **`RestClient`:** build in `@BeforeEach` with `RestClient.builder().baseUrl("http://localhost:" + port).build()` using `@LocalServerPort`
- **`TestRestTemplate`:** `@Autowired TestRestTemplate` (Boot provides it for servlet tests with a defined port) or construct with `new TestRestTemplate("http://localhost:" + port)`

**CI/CD integration:**
- Integration tests (`*IT.java`) executed in Maven/Gradle test lifecycle
- Smoke tests on every commit via `@Tag("smoke")`
- Performance tests on nightly builds via `@Tag("performance")`
- Fail-fast strategy for critical error handling scenarios

#### 5. **Test Data Management**

**Spring Boot Test Data Strategy:**
- `@Sql` annotations for database seeding with 20 canonical Greek god names
- Testcontainers for isolated database state per test
- Spring Boot test profiles for different data scenarios
- `@Transactional` rollback for test isolation

**Test scenarios:**
- Static test data for happy path scenarios
- Empty database testing with dedicated test profile
- Connection failure simulation using TestContainers network controls

---

## Rationale

This strategy addresses our specific challenges:

1. **Educational Platform Reliability** - Real HTTP calls against the running app match consumer experience
2. **Performance Validation** - Explicit timing around client calls plus Actuator where needed
3. **Data Quality Assurance** - Database state validation using Spring Data repositories
4. **Developer Integration** - Same stack as application HTTP (`RestClient` / `RestTemplate` family)
5. **Simplicity** - No separate Groovy-backed HTTP DSL; standard Java + AssertJ

### Why RestClient / TestRestTemplate (not Rest Assured)

- **One stack** - `spring-web` already on the classpath
- **Stability** - Avoids Rest Assured Groovy internals on newer JDKs
- **Developer productivity** - Integration tests follow familiar Spring Boot patterns (`*IT.java`)
- **Ecosystem integration** - Leverages Spring Boot Test, Testcontainers, and Maven/Gradle lifecycle
- **Maintainability** - Assertions stay in Java with AssertJ/JSONAssert

---

## Implementation Plan

Sprint numbering below is **delivery sequencing** for the test suite (what to build when). It is **not** a split of Gherkin/JUnit tags—those are listed together in **§2 Test categories and tags**.

### Sprint 1: Core scenarios
- [ ] Configure **`RestClient`** (or **`TestRestTemplate`**) for `RANDOM_PORT` base URL
- [ ] Implement `GreekGodsApiIT.java` with 3 core test methods:
  - `testSuccessfullyRetrieveCompleteListOfGreekGodNames()` - Happy path validation
  - `testApiResponseTimeMeetsPerformanceRequirements()` - Response time validation
  - `testHandleDatabaseConnectionFailureGracefully()` - Database failure handling
- [ ] Set up Testcontainers for PostgreSQL
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
- All API endpoints validated through HTTP integration tests (`RestClient` or `TestRestTemplate`)
- Error scenarios coverage for database connectivity issues

### Performance Validation
- Response time validation: 100% of requests <1 second (measured around client calls)
- Explicit timing assertions for performance requirements
- Spring Boot Actuator metrics integration for monitoring

### Quality Indicators
- Zero production bugs related to tested scenarios
- Fast feedback: Integration test results within 5 minutes of code commit
- Test maintenance: Minimal overhead due to Spring Boot ecosystem integration

---

## Technical Implementation Details

### Required Dependencies (Maven)
```xml
<dependencies>
    <!-- Spring Boot Test Starter (includes AssertJ, JUnit 5, TestRestTemplate support) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Optional: JSONAssert for flexible JSON comparisons -->
    <dependency>
        <groupId>org.skyscreamer</groupId>
        <artifactId>jsonassert</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- TestContainers -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**No** `io.rest-assured:rest-assured` dependency for this platform’s acceptance tests.

### Test Structure
```
src/test/java/
├── info/
│   └── jab/
│       └── latency/
│           ├── GreekGodsApiIT.java         # Main integration test class
│           └── TestConfiguration.java     # Test-specific configuration
└── resources/
    ├── application-test.yml                # Test configuration
    └── test-data/
        └── greek-gods-seed.sql             # Database seed data
```

### Sample Test Implementation (RestClient + AssertJ)

```java
package info.jab.latency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Tag("integration")
class GreekGodsApiIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @Tag("smoke")
    @Sql("/test-data/greek-gods-seed.sql")
    void testSuccessfullyRetrieveCompleteListOfGreekGodNames() {
        String body = restClient.get()
                .uri("/api/v1/gods/greek")
                .retrieve()
                .body(String.class);

        assertThat(body).isNotBlank();
        // Prefer parsing to List<String> with Jackson if the API returns a JSON array
        assertThat(body).contains("Zeus", "Hera", "Poseidon");
    }

    @Test
    @Tag("performance")
    @Sql("/test-data/greek-gods-seed.sql")
    void testApiResponseTimeMeetsPerformanceRequirements() {
        long start = System.nanoTime();
        restClient.get()
                .uri("/api/v1/gods/greek")
                .retrieve()
                .toBodilessEntity();
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        assertThat(elapsedMs).isLessThan(1000L);
    }

    @Test
    @Tag("error-handling")
    void testHandleDatabaseConnectionFailureGracefully() {
        postgres.stop();

        assertThatThrownBy(() -> restClient.get()
                .uri("/api/v1/gods/greek")
                .retrieve()
                .toEntity(String.class))
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException r = (RestClientResponseException) ex;
                    assertThat(r.getStatusCode().value()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    assertThat(r.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE))
                            .contains("application/problem+json");
                    // Body: RFC 7807 Problem Detail (type, title, status)
                    assertThat(r.getResponseBodyAsString())
                            .contains("\"status\":500")
                            .contains("\"title\"")
                            .contains("\"type\"");
                });
    }
}
```

### Alternative: TestRestTemplate

```java
@Autowired
private TestRestTemplate testRestTemplate; // resolved against local server when using RANDOM_PORT

@Test
@Tag("smoke")
@Sql("/test-data/greek-gods-seed.sql")
void testSuccessfullyRetrieveCompleteListOfGreekGodNames() {
    ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/gods/greek", String.class);
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).contains("Zeus", "Hera", "Poseidon");
}
```

*(Ensure `TestRestTemplate` is available—e.g. `@Bean` or Boot auto-configuration for your test setup.)*

---

## Risks and Mitigation

### Risk: TestContainers Startup Time
**Mitigation:** Use singleton containers pattern, parallel test execution optimization

### Risk: Spring Boot Test Context Loading
**Mitigation:** `@DirtiesContext` optimization, test slicing where appropriate

### Risk: Verbose JSON assertions without a fluent HTTP DSL
**Mitigation:** Small test helpers, JSONAssert, or deserialize to `List<String>` / DTOs with Jackson

### Risk: Test Method Naming and Organization
**Mitigation:** Clear naming conventions, logical test grouping, comprehensive documentation

---

## Related Decisions

- **[ADR-003: Technology Stack](./ADR-003-Greek-Gods-API-Technology-Stack.md):** Runtime, JDBC/Flyway, outbound `RestClient`, and optional WireMock—keep production and test HTTP clients aligned with that ADR
- **API versioning:** Affects base paths used in `RestClient` / `TestRestTemplate` calls
- **Performance requirements:** Drive timing assertions around HTTP calls

---

## References

- [US-001 User Story](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)
- [US-001 Requirements](../agile/US-001_api_greek_gods_data_retrieval.feature)
- [EPIC-001 Greek Gods Data Platform](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/reference/testing/index.html)
- [RestClient (Spring Framework)](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
- [TestRestTemplate (Spring Boot)](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.with-running-server)
- [TestContainers Documentation](https://www.testcontainers.org/)

---

**Last Updated:** 2026-03-26  
**Next Review:** 2026-06-26  
**Review Trigger:** Major feature additions or performance requirement changes

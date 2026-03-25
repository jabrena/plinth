---
name: 423-frameworks-quarkus-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Quarkus applications — including scenarios tagged @acceptance, @QuarkusTest, REST Assured over the real HTTP port, Testcontainers or Dev Services for databases and Kafka, and WireMock for external REST stubs.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in Quarkus, BDD, acceptance testing, REST Assured, Testcontainers, and WireMock

## Tone

Treats the user as a knowledgeable partner. Parses the Gherkin file systematically, implements focused happy-path acceptance tests using Quarkus test utilities, and explains infrastructure choices. Presents production-ready code with clear dependency guidance.

## Goal

Help developers implement acceptance tests from Gherkin feature files in Quarkus projects. Given a `.feature` file, this rule identifies scenarios tagged `@acceptance` (or `@acceptance-tests`), implements happy-path tests that boot the real application with real HTTP, and wires infrastructure through Testcontainers or Dev Services and WireMock — not mocks of internal CDI beans.

1. **Identifies** scenarios tagged with `@acceptance` (or equivalent: `@acceptance-tests`, `@AcceptanceTest`)
2. **Implements** happy-path acceptance tests that exercise the full Quarkus application over HTTP
3. **Uses REST Assured** with `quarkus-rest-assured` so requests hit the Quarkus test HTTP endpoint (real stack: filters, serialization, Vert.x/Jakarta REST)
4. **Uses Quarkus test infrastructure**: `@QuarkusTest`, Dev Services where enabled, `@QuarkusTestResource` for Testcontainers lifecycle, and dynamic configuration for WireMock base URLs

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **End-to-end HTTP**: Prefer REST Assured against the Quarkus test server — not direct calls into resource classes — so the HTTP pipeline is exercised.
2. **Real adjacent infrastructure**: Use Dev Services or Testcontainers for databases and Kafka the app uses; stub only *external* systems with WireMock.
3. **No internal CDI mocks**: Do not replace application `@ApplicationScoped` services with mocks in acceptance tests — validate real wiring; isolate third-party HTTP and containerized infra only.
4. **Dynamic wiring**: Map Testcontainers ports and WireMock base URLs into `application.properties` for `%test` via `QuarkusTestResource` lifecycle callbacks — never hardcode ephemeral ports in production config.
5. **BDD fidelity**: One test method per tagged scenario; use `@DisplayName` to echo the Gherkin scenario title; map Given/When/Then to setup, HTTP call, and assertions; implement happy path unless the user asks for negative cases.

**Cross-references**: Framework-agnostic acceptance from Gherkin — `@133-java-testing-acceptance-tests`. Unit tests — `@421-frameworks-quarkus-testing-unit-tests`. Integration tests — `@422-frameworks-quarkus-testing-integration-tests`.

## Constraints

Before generating any code, ensure the project is in a valid state and the Gherkin feature file is in context. Compilation failure is a BLOCKING condition. A missing `.feature` file is a BLOCKING condition.

- **PRECONDITION**: The Gherkin `.feature` file MUST be in context — stop and ask if not provided
- **PRECONDITION**: The project MUST use Quarkus — stop and direct the user to `@133-java-testing-acceptance-tests` or `@323-frameworks-spring-boot-testing-acceptance-tests` if they use another stack
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **SCOPE**: Implement only scenarios tagged with `@acceptance` or `@acceptance-tests` (or equivalent)
- **SCOPE**: Implement only the happy path — skip negative or error-path scenarios unless explicitly requested

## Steps

### Step 1: Locate and parse the Gherkin feature file

**Purpose**: Confirm the feature file is in context and extract acceptance-tagged scenarios.

### Actions

1. **Verify preconditions**: (a) Check that a file with extension `.feature` is present in the context. If not, stop and respond: "The Gherkin feature file (.feature) is required. Please add the feature file to the context." (b) Confirm the project uses Quarkus (look for `quarkus-core`, `@QuarkusApplication`, or `io.quarkus` dependencies). If it does not, stop and direct the user to `@133-java-testing-acceptance-tests` or `@323-frameworks-spring-boot-testing-acceptance-tests`.
2. **Parse the feature file**: Read the `Feature` block and all `Scenario` blocks.
3. **Filter scenarios**: Select only scenarios that have one of these tags: `@acceptance`, `@acceptance-tests`, or equivalent (e.g. `@AcceptanceTest`).
4. **List the happy path**: For each selected scenario, identify the Given / When / Then steps. Focus on the main success path.

### Output

Present a summary to the user:
- Feature name and description
- Count of acceptance-tagged scenarios found
- For each scenario: title and steps (Given / When / Then)
- Proposed test class name using the `AT` suffix (e.g. `{FeatureName}AT`) so `maven-failsafe-plugin` picks it up automatically

#### Step Constraints

- **MUST** abort if no `.feature` file is in context or if the project does not use Quarkus
- **MUST** include only scenarios with `@acceptance` or `@acceptance-tests` (or equivalent) tag
- **MUST** confirm the list of scenarios with the user before generating code

### Step 2: Generate BaseAcceptanceTest with @QuarkusTest, infrastructure, and WireMock

**Purpose**: Create a base class that boots Quarkus with simulated external dependencies.

### Infrastructure matrix

| Dependency type | Technology | When to use |
|-----------------|------------|-------------|
| Database | Dev Services or Testcontainers | Service uses JDBC or Panache |
| Kafka | Dev Services or Testcontainers | Service publishes or consumes messages |
| External REST APIs | WireMock | Service calls third-party HTTP |

### Base class structure (Quarkus)

- Use `@QuarkusTest` to start the application in test mode with HTTP enabled (`quarkus.http.test-port` — REST Assured integrates via `quarkus-rest-assured`)
- Register Testcontainers with `QuarkusTestResource` and map JDBC or Kafka URLs into Quarkus config for `%test`
- Use `WireMockExtension` with `@RegisterExtension` (or equivalent) for outbound REST stubs; inject the base URL into Quarkus config through a test resource or `@io.quarkus.test.junit.TestProfile` / `Map` of config properties
- **Test isolation**: Call `wireMock.resetAll()` in `@BeforeEach` when reusing one WireMock instance across tests; use separate `@QuarkusTest` profiles only when tests mutate irreversible global state

### File placement

- Place `BaseAcceptanceTest.java` at `src/test/java/{root-package}/BaseAcceptanceTest.java`

#### Step Constraints

- **MUST** use `@QuarkusTest` so HTTP and CDI match production-like wiring
- **MUST** use Testcontainers or Dev Services for real databases and Kafka the application uses
- **MUST** use WireMock for outbound REST dependencies
- **MUST** avoid hardcoding random ports — configure from container or extension

### Step 3: Implement acceptance test class with REST Assured

**Purpose**: Generate the Java test class that implements each acceptance-tagged scenario.

### REST Assured usage

- Use `given().when().get/post/put/delete(...).then()` against relative paths — Quarkus sets the base URI for tests when `quarkus-rest-assured` is present
- Assert status with `.statusCode(...)` and body with JSON path or Hamcrest/AssertJ as appropriate
- Structure: Given → stubs and fixture data; When → HTTP call; Then → assertions

### Test structure

- One `@Test` method per scenario
- Annotate with `@DisplayName` mirroring the Gherkin scenario title
- Extend or reuse `BaseAcceptanceTest` for shared setup

### File placement

- Place the test class at `src/test/java/{root-package}/{FeatureName}AT.java` — the `AT` suffix targets Failsafe

#### Step Constraints

- **MUST** use REST Assured (or equivalent full-HTTP client) — not direct Java calls into `@Path` methods for acceptance scope
- **MUST** follow Given-When-Then structure in each test method
- **MUST** implement only happy-path scenarios unless requested otherwise

### Step 4: Provide Maven dependencies and WireMock stubs

**Purpose**: Declare test dependencies and optional WireMock mapping files.

### Typical Maven dependencies (test scope)

| Purpose | Coordinates (illustrative) |
|---------|----------------------------|
| Quarkus JUnit 5 | `io.quarkus:quarkus-junit5` |
| REST Assured integration | `io.rest-assured:rest-assured` + Quarkus BOM alignment; often via `quarkus-rest-assured` |
| Testcontainers | `org.testcontainers:junit-jupiter` and module jars (e.g. `postgresql`, `kafka`) |
| WireMock | `org.wiremock:wiremock-standalone` (or WireMock 3 JUnit Jupiter module per project choice) |

### WireMock mappings

When the service calls external REST APIs, create mapping files under `src/test/resources/wiremock/mappings/` and response bodies under `src/test/resources/wiremock/__files/` when using file-based stubs.

### Maven Surefire / Failsafe split

Same three-tier convention as Spring: `*Test` → Surefire; `*IT` and `*AT` → Failsafe. Name acceptance classes with the `AT` suffix.

### Output

- List dependency snippets aligned with the project BOM
- List WireMock files created
- Remind to run `./mvnw clean verify`

#### Step Constraints

- **MUST** configure Failsafe to include `**/*IT.java` and `**/*AT.java`
- **MUST** name acceptance test classes with the `AT` suffix


## Examples

### Table of contents

- Example 1: Gherkin feature with @acceptance scenarios
- Example 2: Acceptance test method sketch

### Example 1: Gherkin feature with @acceptance scenarios

Title: Expected structure
Description: Scenarios must include `@acceptance` or `@acceptance-tests` to be in scope.

**Good example:**

```gherkin
Feature: Order API

  @acceptance
  Scenario: Create order returns 201
    Given the catalog service returns product "A" in stock
    When I POST "/orders" with body {"productSku":"A","qty":1}
    Then the response status is 201
```

**Bad example:**

```gherkin
Feature: Order API
  Scenario: Create order returns 201
    # Bad: missing @acceptance — skipped by this rule
```

### Example 2: Acceptance test method sketch

Title: @QuarkusTest + REST Assured
Description: Illustrative pattern — adjust paths and stubs to the feature file.

**Good example:**

```java
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@DisplayName("Order API")
class OrderApiAT {

    @Test
    @DisplayName("Create order returns 201")
    void createOrderReturns201() {
        given()
            .contentType("application/json")
            .body("{\"productSku\":\"A\",\"qty\":1}")
        .when()
            .post("/orders")
        .then()
            .statusCode(201)
            .body("productSku", equalTo("A"));
    }
}
```

**Bad example:**

```java
@QuarkusTest
class OrderApiAT {
    @Test
    void createOrderReturns201() {
        // Bad: bypasses HTTP — not an acceptance test
        // new OrderResource().create(...);
    }
}
```

## Output Format

- **ANALYZE** the `.feature` file: feature name, scenarios, tags, and steps; confirm Quarkus and acceptance tags
- **SUMMARIZE** selected scenarios and proposed `*AT` class names before coding
- **IMPLEMENT** `BaseAcceptanceTest` (or equivalent) with `@QuarkusTest`, Dev Services or Testcontainers, WireMock, and `%test` configuration for dynamic URLs
- **IMPLEMENT** one REST Assured test per acceptance scenario with `@DisplayName` mirroring Gherkin titles
- **DOCUMENT** Maven dependencies, Surefire/Failsafe split, and WireMock layout
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING**: Do not generate tests without a `.feature` file in context or without Quarkus
- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` before generating or refactoring acceptance tests
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes; Docker may be required for Testcontainers
- **SCOPE**: Default to happy path only unless the user explicitly asks for negative scenarios
- **SECRETS**: Do not embed real API keys or production URLs — use WireMock and test properties
- **NAMING**: Use the `AT` suffix for acceptance test classes (e.g. `UserRegistrationAT`)
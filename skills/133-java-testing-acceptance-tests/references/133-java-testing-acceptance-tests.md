---
name: 133-java-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for framework-agnostic Java apps (no Spring Boot, Quarkus, Micronaut) — including finding scenarios tagged @acceptance, implementing happy path tests, using RestAssured, Testcontainers for DB/Kafka, and WireMock for external REST stubs.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# Java acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in BDD, acceptance testing, RestAssured, Testcontainers, and WireMock

## Tone

Treats the user as a knowledgeable partner. Parses the Gherkin file systematically, implements focused happy-path acceptance tests, and explains the infrastructure choices. Presents production-ready code with clear dependency guidance.

## Goal

Help developers implement acceptance tests from Gherkin feature files. Given a `.feature` file, this rule:

1. **Identifies** scenarios tagged with `@acceptance` (or equivalent: `@acceptance-tests`)
2. **Implements** happy-path acceptance tests that exercise the full service with simulated dependencies
3. **Uses RestAssured** for testing REST endpoints
4. **Simulates dependencies**: Testcontainers for databases and Kafka, WireMock for external REST APIs

### Guiding principles

- Start the full application (or API layer) with all dependencies simulated — no mocks of internal services
- Use Testcontainers for databases (PostgreSQL, MySQL, etc.) and Kafka when the service depends on them
- Use WireMock to stub external REST APIs the service calls
- RestAssured is the preferred library for REST API assertion — fluent DSL, JSON path assertions, status codes
- Implement only the happy path for each scenario — one test method per scenario, Given-When-Then structure
- Follow the Gherkin steps literally — each step maps to setup (Given), action (When), or assertion (Then)

## Constraints

Before generating any code, ensure the project is in a valid state and the Gherkin feature file is in context. Compilation failure is a BLOCKING condition. A missing `.feature` file is a BLOCKING condition.

- **PRECONDITION**: The Gherkin `.feature` file MUST be in context — stop and ask if not provided
- **PRECONDITION**: The project MUST NOT use Spring Boot, Quarkus, or Micronaut — stop and direct the user to `@323-frameworks-spring-boot-testing-acceptance-tests` (Spring Boot) or `@423-frameworks-quarkus-testing-acceptance-tests` (Quarkus) for Gherkin-based acceptance tests
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before generating acceptance test scaffolding
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **SCOPE**: Implement only scenarios tagged with `@acceptance` or `@acceptance-tests`
- **SCOPE**: Implement only the happy path — skip negative or error-path scenarios unless explicitly requested

## Steps

### Step 1: Locate and parse the Gherkin feature file

**Purpose**: Confirm the feature file is in context and extract acceptance-tagged scenarios.

### Actions

1. **Verify preconditions**: (a) Check that a file with extension `.feature` is present in the context. If not, stop and respond: "The Gherkin feature file (.feature) is required. Please add the feature file to the context." (b) Confirm the project does not use Spring Boot, Quarkus, or Micronaut. If it does, stop and direct the user to `@323-frameworks-spring-boot-testing-acceptance-tests` (Spring Boot) or `@423-frameworks-quarkus-testing-acceptance-tests` (Quarkus) for acceptance tests.
2. **Parse the feature file**: Read the `Feature` block and all `Scenario` blocks.
3. **Filter scenarios**: Select only scenarios that have one of these tags: `@acceptance`, `@acceptance-tests`, or equivalent (e.g. `@AcceptanceTest`).
4. **List the happy path**: For each selected scenario, identify the Given / When / Then steps. Focus on the main success path — ignore `Scenario Outline` for now unless the user explicitly requests it, or handle one example row per scenario.

### Output

Present a summary to the user:
- Feature name and description
- Count of acceptance-tagged scenarios found
- For each scenario: title and steps (Given / When / Then)
- Proposed test class name (e.g. `{FeatureName}AcceptanceTest`)

#### Step Constraints

- **MUST** abort if no `.feature` file is in context or if the project uses Spring Boot, Quarkus, or Micronaut
- **MUST** include only scenarios with `@acceptance` or `@acceptance-tests` (or equivalent) tag
- **MUST** confirm the list of scenarios with the user before generating code

### Step 2: Generate BaseAcceptanceTest with Testcontainers and WireMock

**Purpose**: Create a base class that starts the application with simulated dependencies.

### Infrastructure matrix

| Dependency type | Technology | When to use |
|-----------------|------------|-------------|
| Database (PostgreSQL, MySQL, etc.) | Testcontainers | Service uses JDBC or JPA |
| Kafka | Testcontainers (KafkaContainer) | Service publishes or consumes messages |
| External REST APIs | WireMock | Service calls third-party or other microservices over HTTP |

### Base class structure

- Use `@Testcontainers` and `@Container` for each database or Kafka container needed
- Use `WireMockExtension` for external REST stubs
- Use `@BeforeAll` to set `System.setProperty()` for database URLs, Kafka bootstrap servers, and WireMock base URLs — so the application picks them up via configuration
- Start the application programmatically in `@BeforeAll` (e.g. Javalin, Spark, embedded Jetty, `com.sun.net.httpserver`, Vert.x) and expose the port for RestAssured. Use generic property names (e.g. `datasource.url`, `kafka.bootstrap.servers`, `external.service.base-url`) — not framework-specific prefixes like `spring.`

### File placement

- Place `BaseAcceptanceTest.java` at `src/test/java/{root-package}/BaseAcceptanceTest.java`

#### Step Constraints

- **MUST** use Testcontainers for any database or Kafka the service depends on
- **MUST** use WireMock for any outbound REST calls the service makes
- **MUST** propagate coordinates via `System.setProperty()` in `@BeforeAll` — never hardcode ports or URLs
- **MUST** extend or reference the base class from the concrete acceptance test class

### Step 3: Implement acceptance test class with RestAssured

**Purpose**: Generate the Java test class that implements each acceptance-tagged scenario using RestAssured.

### RestAssured usage

- Use `RestAssured.given()` / `.when()` / `.then()` for REST assertions
- Use `given().baseUri(baseUrl).port(port)` (or `baseUri` only if using full URL) to target the running application
- Assert status codes: `.then().statusCode(200)` (or 201, 204 as appropriate)
- Assert JSON body: `.body("field.path", equalTo("expected"))` or `JsonPath` for complex structures
- Set request body: `given().contentType(ContentType.JSON).body(jsonObject)`

### Test structure

- One `@Test` method per scenario
- Method name: `scenario_{scenario_name_with_underscores}` or `{scenario_keyword}_...`
- Given steps → setup (e.g. WireMock stubs, DB data insertion if needed)
- When steps → RestAssured request (GET, POST, PUT, DELETE)
- Then steps → RestAssured assertions (status, body)

### File placement

- Place the test class at `src/test/java/{root-package}/{FeatureName}AcceptanceTest.java`
- Ensure it extends or uses `BaseAcceptanceTest` for container and WireMock setup

#### Step Constraints

- **MUST** use RestAssured for REST endpoint testing — not MockMvc or raw HttpClient for the API under test
- **MUST** follow Given-When-Then structure in each test method
- **MUST** implement only happy-path scenarios — skip error/negative paths unless requested
- **MUST** add RestAssured and Testcontainers/WireMock dependencies if missing from pom.xml

### Step 4: Provide Maven dependencies and WireMock stubs

**Purpose**: Ensure all required dependencies are declared and WireMock mapping files are created when needed.

### Required Maven dependencies (test scope)

| Dependency | GroupId | ArtifactId | Purpose |
|------------|---------|------------|---------|
| RestAssured | `io.rest-assured` | `rest-assured` | REST API testing |
| RestAssured JSON Schema | `io.rest-assured` | `json-schema-validator` | Optional, for schema validation |
| Testcontainers JUnit | `org.testcontainers` | `junit-jupiter` | Testcontainers JUnit 5 support |
| Testcontainers PostgreSQL | `org.testcontainers` | `postgresql` | If DB is PostgreSQL |
| Testcontainers Kafka | `org.testcontainers` | `kafka` | If service uses Kafka |
| WireMock JUnit 5 | `org.wiremock` | `wiremock-standalone` | WireMock for REST stubs |

### WireMock mappings

When the service calls external REST APIs, create WireMock mapping files under:
`src/test/resources/wiremock/mappings/{service-name}/`
Use `bodyFileName` for large responses; store bodies in `wiremock/files/`.

### Output

- Display the Maven dependency snippets (with versions from project BOM or latest stable)
- List any WireMock mapping files created
- Remind the user to run `./mvnw clean verify` and that acceptance tests may run in the failsafe phase (suffix `*IT` or `*AcceptanceTest` in `maven-failsafe-plugin` includes)

#### Step Constraints

- **MUST** list RestAssured, Testcontainers, and WireMock dependencies after generating tests
- **MUST** ensure Failsafe includes `*AcceptanceTest` or `*IT` if acceptance tests are integration tests


## Examples

### Table of contents

- Example 1: Gherkin feature with @acceptance scenarios
- Example 2: BaseAcceptanceTest with Testcontainers and WireMock
- Example 3: Acceptance test with RestAssured
- Example 4: Required Maven dependencies

### Example 1: Gherkin feature with @acceptance scenarios

Title: Feature file structure expected by this rule
Description: The rule looks for scenarios tagged with @acceptance or @acceptance-tests. Only those scenarios are implemented.

**Good example:**

```gherkin
Feature: User registration API

  As a client
  I want to register a new user via REST API
  So that I can create an account

  @acceptance
  Scenario: Successful user registration
    Given the system is ready
    When I send a POST request to "/api/users" with:
      """
      {"email": "user@example.com", "name": "John Doe"}
      """
    Then the response status is 201
    And the response body contains "id"
    And the response body "email" equals "user@example.com"

  @acceptance
  Scenario: Get user by id
    Given a user exists with email "user@example.com"
    When I send a GET request to "/api/users/1"
    Then the response status is 200
    And the response body "email" equals "user@example.com"
```

**Bad example:**

```gherkin
Feature: User registration API

  Scenario: Successful user registration
  # Bad: No @acceptance tag — this scenario will be skipped by the rule
    Given the system is ready
    When I send a POST request to "/api/users"
    Then the response status is 201
```

### Example 2: BaseAcceptanceTest with Testcontainers and WireMock

Title: Base class for acceptance tests with simulated DB, Kafka, and REST stubs
Description: Framework-agnostic: Testcontainers for PostgreSQL and Kafka, WireMock for external REST APIs. Coordinates propagated via System.setProperty with generic property names. App started programmatically (e.g. App.start()).

**Good example:**

```java
package com.example.myapp;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Testcontainers
abstract class BaseAcceptanceTest {

    protected static int port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.8"));

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
        .build();

    @BeforeAll
    static void setUpInfrastructure() {
        System.setProperty("datasource.url", postgres.getJdbcUrl());
        System.setProperty("datasource.username", postgres.getUsername());
        System.setProperty("datasource.password", postgres.getPassword());
        System.setProperty("kafka.bootstrap.servers", kafka.getBootstrapServers());
        System.setProperty("external.service.base-url", wireMock.baseUrl());
        // Start app programmatically (Javalin/Spark/Jetty/etc.) and assign port = app.port();
        port = App.start();
    }
}
```

**Bad example:**

```java
// Bad: Using Spring Boot — use @323-frameworks-spring-boot-testing-acceptance-tests; Quarkus — @423-frameworks-quarkus-testing-acceptance-tests instead
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class BaseAcceptanceTest {
    @LocalServerPort
    protected int port;
}
// This rule is for framework-agnostic Java; not Spring Boot, Quarkus, or Micronaut
```

### Example 3: Acceptance test with RestAssured

Title: Implementing Gherkin scenario with RestAssured DSL
Description: RestAssured is used for REST assertions. Given/When/Then map to setup, request, and assertions.

**Good example:**

```java
package com.example.myapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

class UserRegistrationAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void scenario_successful_user_registration() {
        // Given: the system is ready
        // When: POST /api/users
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("""
                {"email": "user@example.com", "name": "John Doe"}
                """)
            .when()
            .post("/api/users")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("email", equalTo("user@example.com"));
    }

    @Test
    void scenario_get_user_by_id() {
        // Given: a user exists (setup via repo or REST)
        // When: GET /api/users/1
        RestAssured.given()
            .when()
            .get("/api/users/1")
            .then()
            .statusCode(200)
            .body("email", equalTo("user@example.com"));
    }
}
```

**Bad example:**

```java
// Bad: Using MockMvc instead of RestAssured for acceptance tests
@Autowired
private MockMvc mockMvc;

@Test
void scenario_registration() {
    mockMvc.perform(post("/api/users").contentType(JSON).content("{}"))
        .andExpect(status().isCreated());
}
// Acceptance tests should use RestAssured to exercise the full HTTP stack (serialization, filters, etc.)
// MockMvc skips the real HTTP layer
```

### Example 4: Required Maven dependencies

Title: RestAssured, Testcontainers, WireMock
Description: Add these dependencies in test scope. Adjust versions via BOM or property.

**Good example:**

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>kafka</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock-standalone</artifactId>
    <scope>test</scope>
</dependency>
```

**Bad example:**

```xml
<!-- Bad: RestAssured in compile scope — should be test scope -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <!-- scope test is mandatory — never pollute main classpath -->
</dependency>
```
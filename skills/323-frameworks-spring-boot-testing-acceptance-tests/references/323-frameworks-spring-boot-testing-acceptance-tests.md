---
name: 323-frameworks-spring-boot-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Spring Boot applications — including finding scenarios tagged @acceptance, implementing happy path tests with RestAssured, @SpringBootTest, Testcontainers for DB/Kafka, and WireMock for external REST stubs.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Boot acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in Spring Boot, BDD, acceptance testing, RestAssured, Testcontainers, and WireMock

## Tone

Treats the user as a knowledgeable partner. Parses the Gherkin file systematically, implements focused happy-path acceptance tests using Spring Boot test utilities, and explains the infrastructure choices. Presents production-ready code with clear dependency guidance.

## Goal

Help developers implement acceptance tests from Gherkin feature files in Spring Boot projects. Given a `.feature` file, this rule:

1. **Identifies** scenarios tagged with `@acceptance` (or equivalent: `@acceptance-tests`)
2. **Implements** happy-path acceptance tests that exercise the full Spring Boot application with simulated dependencies
3. **Uses RestAssured** for testing REST endpoints
4. **Uses Spring Boot test infrastructure**: `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@LocalServerPort`, `@DynamicPropertySource` for Testcontainers and WireMock

### Guiding principles

- Start the full Spring Boot application with all dependencies simulated — no mocks of internal services
- Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` to boot the full context
- Use `@DynamicPropertySource` to inject Testcontainers and WireMock coordinates (spring.datasource.*, spring.kafka.*, custom properties for external services)
- Use Testcontainers for databases (PostgreSQL, MySQL, etc.) and Kafka when the service depends on them
- Use WireMock to stub external REST APIs the service calls
- RestAssured is the preferred library for REST API assertion — fluent DSL, JSON path assertions, status codes
- Implement only the happy path for each scenario — one test method per scenario, Given-When-Then structure
- Follow the Gherkin steps literally — each step maps to setup (Given), action (When), or assertion (Then)

## Constraints

Before generating any code, ensure the project is in a valid state and the Gherkin feature file is in context. Compilation failure is a BLOCKING condition. A missing `.feature` file is a BLOCKING condition.

- **PRECONDITION**: The Gherkin `.feature` file MUST be in context — stop and ask if not provided
- **PRECONDITION**: The project MUST use Spring Boot — stop and direct the user to `@133-java-testing-acceptance-tests` if they use framework-agnostic Java
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before generating acceptance test scaffolding
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **SCOPE**: Implement only scenarios tagged with `@acceptance` or `@acceptance-tests`
- **SCOPE**: Implement only the happy path — skip negative or error-path scenarios unless explicitly requested

## Steps

### Step 1: Locate and parse the Gherkin feature file

**Purpose**: Confirm the feature file is in context and extract acceptance-tagged scenarios.

### Actions

1. **Verify preconditions**: (a) Check that a file with extension `.feature` is present in the context. If not, stop and respond: "The Gherkin feature file (.feature) is required. Please add the feature file to the context." (b) Confirm the project uses Spring Boot (look for `@SpringBootApplication`, `spring-boot-starter-*` dependencies). If it does not, stop and direct the user to `@133-java-testing-acceptance-tests`.
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

- **MUST** abort if no `.feature` file is in context or if the project does not use Spring Boot
- **MUST** include only scenarios with `@acceptance` or `@acceptance-tests` (or equivalent) tag
- **MUST** confirm the list of scenarios with the user before generating code

### Step 2: Generate BaseAcceptanceTest with @SpringBootTest, Testcontainers and WireMock

**Purpose**: Create a base class that boots the Spring application with simulated dependencies.

### Infrastructure matrix

| Dependency type | Technology | When to use |
|-----------------|------------|-------------|
| Database (PostgreSQL, MySQL, etc.) | Testcontainers | Service uses Spring Data JDBC/JPA |
| Kafka | Testcontainers (KafkaContainer) | Service publishes or consumes messages |
| External REST APIs | WireMock | Service calls third-party or other microservices over HTTP |

### Base class structure (Spring Boot)

- Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` to boot the full application
- Use `@LocalServerPort` to inject the application port for RestAssured
- Use `@Testcontainers` and `@Container` for each database or Kafka container needed
- Use `WireMockExtension` with `@RegisterExtension` for external REST stubs
- Use `@DynamicPropertySource` (static method) to register `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `spring.kafka.bootstrap-servers`, and any custom property for external service base URLs (e.g. `external.service.base-url`)
- WireMock base URL is available after the extension is initialized — pass it into `DynamicPropertyRegistry.add()`

### File placement

- Place `BaseAcceptanceTest.java` at `src/test/java/{root-package}/BaseAcceptanceTest.java`

#### Step Constraints

- **MUST** use `@SpringBootTest(webEnvironment = RANDOM_PORT)` — never `MOCK` or `NONE` for acceptance tests
- **MUST** use Testcontainers for any database or Kafka the service depends on
- **MUST** use WireMock for any outbound REST calls the service makes
- **MUST** propagate coordinates via `@DynamicPropertySource` — never hardcode ports or URLs
- **MUST** extend or reference the base class from the concrete acceptance test class

### Step 3: Implement acceptance test class with RestAssured

**Purpose**: Generate the Java test class that implements each acceptance-tagged scenario using RestAssured.

### RestAssured usage

- Use `RestAssured.given()` / `.when()` / `.then()` for REST assertions
- Use `RestAssured.port = port` and `RestAssured.baseURI = "http://localhost"` in `@BeforeEach` (inject `@LocalServerPort` from base class)
- Assert status codes: `.then().statusCode(200)` (or 201, 204 as appropriate)
- Assert JSON body: `.body("field.path", equalTo("expected"))` or `JsonPath` for complex structures
- Set request body: `given().contentType(ContentType.JSON).body(jsonObject)`

### Test structure

- One `@Test` method per scenario
- Method name: `scenario_{scenario_name_with_underscores}` or `{scenario_keyword}_...`
- Given steps → setup (e.g. WireMock stubs, DB data via repository if needed)
- When steps → RestAssured request (GET, POST, PUT, DELETE)
- Then steps → RestAssured assertions (status, body)

### File placement

- Place the test class at `src/test/java/{root-package}/{FeatureName}AcceptanceTest.java`
- Ensure it extends `BaseAcceptanceTest` for Spring context, container, and WireMock setup

#### Step Constraints

- **MUST** use RestAssured for REST endpoint testing — not MockMvc (MockMvc skips the real HTTP stack)
- **MUST** follow Given-When-Then structure in each test method
- **MUST** implement only happy-path scenarios — skip error/negative paths unless requested
- **MUST** add RestAssured and Testcontainers/WireMock dependencies if missing from pom.xml

### Step 4: Provide Maven dependencies and WireMock stubs

**Purpose**: Ensure all required dependencies are declared and WireMock mapping files are created when needed.

### Required Maven dependencies (test scope)

| Dependency | GroupId | ArtifactId | Purpose |
|------------|---------|------------|---------|
| RestAssured | `io.rest-assured` | `rest-assured` | REST API testing |
| Testcontainers JUnit | `org.testcontainers` | `junit-jupiter` | Testcontainers JUnit 5 support |
| Testcontainers PostgreSQL | `org.testcontainers` | `postgresql` | If DB is PostgreSQL |
| Testcontainers Kafka | `org.testcontainers` | `kafka` | If service uses Kafka |
| WireMock JUnit 5 | `org.wiremock` | `wiremock-standalone` | WireMock for REST stubs |

Spring Boot already provides `spring-boot-starter-test`; ensure `rest-assured` is explicitly added (it may be transitively included but version alignment with Spring Boot BOM is recommended).

### WireMock mappings

When the service calls external REST APIs, create WireMock mapping files under:
`src/test/resources/wiremock/mappings/{service-name}/`
Use `bodyFileName` for large responses; store bodies in `wiremock/files/`.

### Output

- Display the Maven dependency snippets (with versions from Spring Boot BOM or latest stable)
- List any WireMock mapping files created
- Remind the user to run `./mvnw clean verify` and that acceptance tests may run in the failsafe phase (suffix `*IT` or `*AcceptanceTest` in `maven-failsafe-plugin` includes)

#### Step Constraints

- **MUST** list RestAssured, Testcontainers, and WireMock dependencies after generating tests
- **MUST** ensure Failsafe includes `*AcceptanceTest` or `*IT` if acceptance tests are integration tests


## Examples

### Table of contents

- Example 1: Gherkin feature with @acceptance scenarios
- Example 2: BaseAcceptanceTest with @SpringBootTest, Testcontainers and WireMock
- Example 3: Acceptance test with RestAssured

### Example 1: Gherkin feature with @acceptance scenarios

Title: Feature file structure expected by this rule
Description: Same as framework-agnostic 133: the rule looks for scenarios tagged @acceptance or @acceptance-tests.

**Good example:**

```gherkin
Feature: User registration API

  @acceptance
  Scenario: Successful user registration
    Given the system is ready
    When I send a POST request to "/api/users" with:
      """
      {"email": "user@example.com", "name": "John Doe"}
      """
    Then the response status is 201
    And the response body "email" equals "user@example.com"
```

**Bad example:**

```gherkin
Feature: User registration API

  Scenario: Successful user registration
  # Bad: No @acceptance tag — this scenario will be skipped
    Given the system is ready
    When I send a POST request to "/api/users"
    Then the response status is 201
```

### Example 2: BaseAcceptanceTest with @SpringBootTest, Testcontainers and WireMock

Title: Spring Boot base class for acceptance tests
Description: Uses @SpringBootTest(RANDOM_PORT), @DynamicPropertySource for Testcontainers and WireMock coordinates.

**Good example:**

```java
package com.example.myapp;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseAcceptanceTest {

    @LocalServerPort
    protected int port;

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

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("external.service.base-url", wireMock::baseUrl);
    }
}
```

**Bad example:**

```java
// Bad: Using MockMvc — acceptance tests need real HTTP
@SpringBootTest
@AutoConfigureMockMvc
abstract class BaseAcceptanceTest {
    @Autowired
    MockMvc mockMvc;
}
// Use @SpringBootTest(webEnvironment = RANDOM_PORT) + RestAssured to test the real HTTP stack
```

### Example 3: Acceptance test with RestAssured

Title: Implementing Gherkin scenario with RestAssured and @LocalServerPort
Description: RestAssured targets the booted Spring application via @LocalServerPort.

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
}
```

**Bad example:**

```java
// Bad: MockMvc skips real HTTP — use RestAssured for acceptance
@Test
void scenario_registration() {
    mockMvc.perform(post("/api/users").contentType(JSON).content("{}"))
        .andExpect(status().isCreated());
}
// Acceptance tests must exercise serialization, filters, full HTTP stack
```
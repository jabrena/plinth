---
name: 323-frameworks-spring-boot-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Spring Boot applications — including finding scenarios tagged @acceptance, implementing happy path tests with TestRestTemplate, @SpringBootTest, Testcontainers for DB/Kafka, and WireMock for external REST stubs.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Boot acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in Spring Boot, BDD, acceptance testing, TestRestTemplate, Testcontainers, and WireMock

## Tone

Treats the user as a knowledgeable partner. Parses the Gherkin file systematically, implements focused happy-path acceptance tests using Spring Boot test utilities, and explains the infrastructure choices. Presents production-ready code with clear dependency guidance.

## Goal

Help developers implement acceptance tests from Gherkin feature files in Spring Boot projects. Given a `.feature` file, this rule identifies scenarios tagged `@acceptance` (or `@acceptance-tests`), implements happy-path tests that boot the real application with real HTTP, and wires infrastructure through Testcontainers and WireMock—not mocks of internal beans.

1. **Identifies** scenarios tagged with `@acceptance` (or equivalent: `@acceptance-tests`, `@AcceptanceTest`)
2. **Implements** happy-path acceptance tests that exercise the full Spring Boot application with simulated *external* dependencies (DB, Kafka, outbound HTTP)
3. **Uses TestRestTemplate** (auto-configured by Spring Boot for `RANDOM_PORT`) for REST assertions over the full servlet/filter stack — no third-party dependency required
4. **Uses Spring Boot test infrastructure**: `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@Autowired TestRestTemplate`, `@ServiceConnection` for Testcontainers (Spring Boot 4.0.x), and `@DynamicPropertySource` for WireMock base URLs

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **End-to-end HTTP**: Prefer `TestRestTemplate` to `MockMvc` for acceptance tests so serialization, security filters, and real HTTP are exercised. `TestRestTemplate` is auto-configured by Spring Boot for `RANDOM_PORT` and needs no extra dependency.
2. **Real adjacent infrastructure**: Use Testcontainers for databases and Kafka the app actually uses; stub only *external* systems with WireMock.
3. **No internal mocks**: Do not replace your own `@Service` beans with mocks—validate real wiring; isolate only third-party HTTP and containerized infra.
4. **Dynamic wiring**: Use `@ServiceConnection` for database and Kafka containers (Spring Boot 4.0.x); use `@DynamicPropertySource` only for infrastructure that lacks a built-in service connection (e.g. WireMock base URLs). Never hardcode ephemeral ports.
5. **BDD fidelity**: One test method per tagged scenario; use `@DisplayName` to echo the Gherkin scenario title; map Given/When/Then to setup, HTTP call, and assertions; implement happy path unless the user asks for negative cases.

**Cross-references**: Framework-agnostic acceptance from Gherkin — `@133-java-testing-acceptance-tests`. Unit and slice tests — `@321-frameworks-spring-boot-testing-unit-tests`. Broader Spring integration tests — `@322-frameworks-spring-boot-testing-integration-tests`.

## Constraints

Before generating any code, ensure the project is in a valid state and the Gherkin feature file is in context. Compilation failure is a BLOCKING condition. A missing `.feature` file is a BLOCKING condition.

- **PRECONDITION**: The Gherkin `.feature` file MUST be in context — stop and ask if not provided
- **PRECONDITION**: The project MUST use Spring Boot — stop and direct the user to `@133-java-testing-acceptance-tests` if they use framework-agnostic Java
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before generating acceptance test scaffolding
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding
- **NO EXCEPTIONS**: Under no circumstances should acceptance test generation continue if the project fails to compile or the feature file is missing
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **SCOPE**: Implement only scenarios tagged with `@acceptance` or `@acceptance-tests` (or equivalent)
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
- Proposed test class name using the `AT` suffix (e.g. `{FeatureName}AT`) so `maven-failsafe-plugin` picks it up automatically

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
- Declare `@Autowired protected TestRestTemplate restTemplate;` in the base class — Spring Boot auto-configures it to point at `http://localhost:{randomPort}` with no extra setup
- Use `@Testcontainers` and `@Container` for each database or Kafka container needed
- Add `@ServiceConnection` on each database or Kafka `@Container` (Spring Boot 4.0.x) — auto-configures `spring.datasource.*`, `spring.kafka.bootstrap-servers`, etc. with no `@DynamicPropertySource` boilerplate
- Use `WireMockExtension` with `@RegisterExtension` for external REST stubs
- Use `@DynamicPropertySource` **only** for WireMock base URLs and any other infrastructure that does not have a built-in `@ServiceConnection` support
- WireMock base URL is available after the extension is initialized — pass it into `DynamicPropertyRegistry.add()`
- **Test isolation**: Default to a shared Spring context and call `wireMock.resetAll()` in `@BeforeEach` so stubs do not leak. Add `@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)` (or `AFTER_CLASS`) on the base or on specific test classes only when tests mutate shared application state and need a fresh context

### File placement

- Place `BaseAcceptanceTest.java` at `src/test/java/{root-package}/BaseAcceptanceTest.java`

#### Step Constraints

- **MUST** use `@SpringBootTest(webEnvironment = RANDOM_PORT)` — never `MOCK` or `NONE` for acceptance tests
- **MUST** use Testcontainers for any database or Kafka the service depends on
- **MUST** use WireMock for any outbound REST calls the service makes
- **MUST** use `@ServiceConnection` for database and Kafka containers (Spring Boot 4.0.x); use `@DynamicPropertySource` for WireMock and unsupported containers — never hardcode ephemeral ports or URLs
- **MUST** extend or reference the base class from the concrete acceptance test class

### Step 3: Implement acceptance test class with TestRestTemplate

**Purpose**: Generate the Java test class that implements each acceptance-tagged scenario using Spring Boot's `TestRestTemplate`.

### TestRestTemplate usage

- Inject `TestRestTemplate` via inheritance from `BaseAcceptanceTest` — no `@BeforeEach` port wiring required
- Use `restTemplate.getForEntity(uri, ResponseType.class)` for GET requests
- Use `restTemplate.postForEntity(uri, requestEntity, ResponseType.class)` for POST requests
- Use `restTemplate.exchange(uri, method, requestEntity, ResponseType.class)` for PUT / DELETE or when fine-grained control is needed
- Build `HttpEntity` with `HttpHeaders` to set `Content-Type: application/json`
- Assert with AssertJ: `assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)`
- Assert body fields: `assertThat(response.getBody().email()).isEqualTo("user@example.com")`

### Test structure

- One `@Test` method per scenario
- Method name: `scenario_{scenario_name_with_underscores}` or `{scenario_keyword}_...`
- Given steps → setup (e.g. WireMock stubs, DB data via repository if needed)
- When steps → `TestRestTemplate` request (GET, POST, PUT, DELETE)
- Then steps → AssertJ assertions on `ResponseEntity` (status, body)

### File placement

- Place the test class at `src/test/java/{root-package}/{FeatureName}AT.java` — the `AT` suffix makes `maven-failsafe-plugin` include it automatically
- Ensure it extends `BaseAcceptanceTest` for Spring context, container, and WireMock setup

#### Step Constraints

- **MUST** use `TestRestTemplate` for REST endpoint testing — not MockMvc (MockMvc skips the real HTTP stack)
- **MUST** follow Given-When-Then structure in each test method
- **MUST** implement only happy-path scenarios — skip error/negative paths unless requested
- **MUST** add Testcontainers and WireMock dependencies if missing from pom.xml; `TestRestTemplate` is provided by `spring-boot-starter-test` — no extra dependency needed

### Step 4: Provide Maven dependencies and WireMock stubs

                **Purpose**: Ensure all required dependencies are declared and WireMock mapping files are created when needed.

                ### Required Maven dependencies (test scope)

                | Dependency | GroupId | ArtifactId | Purpose |
                |------------|---------|------------|---------|
                | TestRestTemplate | *(via `spring-boot-starter-test`)* | — | REST API testing — **no extra dependency needed** |
                | Testcontainers JUnit | `org.testcontainers` | `junit-jupiter` | Testcontainers JUnit 5 support |
                | Testcontainers PostgreSQL | `org.testcontainers` | `postgresql` | If DB is PostgreSQL |
                | Testcontainers Kafka | `org.testcontainers` | `kafka` | If service uses Kafka |
                | WireMock JUnit 5 | `org.wiremock` | `wiremock-standalone` | WireMock for REST stubs |

                Spring Boot already provides `spring-boot-starter-test`, which includes `TestRestTemplate`. Do **not** add `rest-assured` — it is no longer needed for acceptance tests in this rule.

                ### WireMock mappings

                When the service calls external REST APIs, create WireMock mapping files under:
                `src/test/resources/wiremock/mappings/` (optionally subfolders per service).
                Use `bodyFileName` in a mapping JSON for large or reusable response bodies; store those files under `src/test/resources/wiremock/__files/` — WireMock resolves `bodyFileName` relative to the `__files` root when using `usingFilesUnderClasspath("wiremock")`.

                ### Maven Surefire / Failsafe split

                Use a three-tier naming convention to keep fast unit tests separate from heavier integration and acceptance tests:

                | Suffix | Test type | Maven plugin | Phase |
                |--------|-----------|--------------|-------|
                | `*Test` | Unit tests | `maven-surefire-plugin` | `test` |
                | `*IT` | Integration tests | `maven-failsafe-plugin` | `integration-test` / `verify` |
                | `*AT` | Acceptance tests | `maven-failsafe-plugin` | `integration-test` / `verify` |

                Configure both plugins explicitly in `pom.xml` so that `./mvnw test` runs only unit tests and `./mvnw verify` runs all three tiers:

                ```xml
                
                    org.apache.maven.plugins
                    maven-surefire-plugin
                    
                        
                        
                            **/*Test.java
                        
                    
                

                
                    org.apache.maven.plugins
                    maven-failsafe-plugin
                    
                        
                        
                            **/*IT.java
                            **/*AT.java
                        
                    
                    
                        
                            
                                integration-test
                                verify
                            
                        
                    
                
                ```

                Name acceptance test classes with the `AT` suffix (e.g. `UserRegistrationAT`, `OrderCreationAT`) so Failsafe picks them up automatically without extra configuration.

                ### Output

                - Display the Maven dependency snippets (with versions from Spring Boot BOM or latest stable)
                - List any WireMock mapping files created
                - Remind the user to run `./mvnw clean verify` and show the Surefire / Failsafe plugin split above
            
#### Step Constraints

- **MUST** list Testcontainers and WireMock dependencies after generating tests; note that `TestRestTemplate` requires no extra dependency
- **MUST** configure `maven-failsafe-plugin` to include both `**/*IT.java` and `**/*AT.java`
- **MUST** configure `maven-surefire-plugin` to include only `**/*Test.java` so unit tests are not mixed with AT/IT runs
- **MUST** name acceptance test classes with the `AT` suffix (e.g. `UserRegistrationAT`) for Failsafe auto-detection


## Examples

### Table of contents

- Example 1: Gherkin feature with @acceptance scenarios
- Example 2: BaseAcceptanceTest with @SpringBootTest, TestRestTemplate, Testcontainers and WireMock
- Example 3: Acceptance test with TestRestTemplate
- Example 4: WireMock stub setup for external REST dependencies
- Example 5: Acceptance test naming convention (*AT) and Maven Surefire/Failsafe configuration
- Example 6: Test-specific beans and configuration

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

### Example 2: BaseAcceptanceTest with @SpringBootTest, TestRestTemplate, Testcontainers and WireMock

Title: Shared context vs. Spring context isolation; WireMock reset between tests
Description: Uses `@SpringBootTest(RANDOM_PORT)`. Spring Boot auto-configures `TestRestTemplate` at `http://localhost:{randomPort}` — inject it with `@Autowired` and no port wiring is needed. Annotate each Testcontainers database or Kafka container with `@ServiceConnection` (Spring Boot 4.0.x) — this replaces the entire `@DynamicPropertySource` block for those containers. `@DynamicPropertySource` is still required for WireMock because it has no built-in service connection support. **Without Spring context isolation (default, faster)**: Reuse one application context for the whole suite. Testcontainers and WireMock extension stay up; clear only WireMock stub state between methods with `wireMock.resetAll()` in `@BeforeEach` so one test’s stubs do not leak into the next. Choose this when tests do not mutate singleton beans, caches, or other shared application state. **With Spring context isolation**: Add `@DirtiesContext` (for example `classMode = AFTER_EACH_TEST_METHOD` or `AFTER_CLASS`) when a test leaves the Spring context in a state that would break siblings — e.g. replacing beans, mutating `@Configuration` state, or integration flows that register one-off components. This reloads the context (slower) but guarantees a clean application between tests. Some scenarios need the non-isolated base for speed; others need `@DirtiesContext` for correctness — pick per feature or split into a dedicated base class for “dirty” tests.

**Good example:**

```java
package com.example.myapp;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

// Without Spring context isolation: one shared context + containers; reset WireMock between methods
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseAcceptanceTest {

    @Autowired
    protected TestRestTemplate restTemplate;  // auto-configured — no @LocalServerPort needed

    @Container
    @ServiceConnection  // auto-configures spring.datasource.url/username/password
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    @ServiceConnection  // auto-configures spring.kafka.bootstrap-servers
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
        .build();

    @DynamicPropertySource  // WireMock has no @ServiceConnection — manual registration still needed
    static void configureWireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("external.service.base-url", wireMock::baseUrl);
    }

    @BeforeEach
    void resetWireMockBetweenTests() {
        wireMock.resetAll();  // isolate stub state; Spring context stays warm
    }
}
```

**Bad example:**

```java
package com.example.myapp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseAcceptanceTest {

    // Bad: manually injecting the port and constructing the URL in @BeforeEach
    // — use @Autowired TestRestTemplate instead; it handles the base URL automatically
    @LocalServerPort
    protected int port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")    // unnecessary with @ServiceConnection
        .withUsername("test")
        .withPassword("test");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    // Verbose manual wiring — all three lines replaced by @ServiceConnection on the container
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        // Missing: WireMock URL — external stubs would be hardcoded or absent
    }
}
```

### Example 3: Acceptance test with TestRestTemplate

Title: @DisplayName echoes the Gherkin scenario; Given/When/Then in test body; no @BeforeEach port setup needed
Description: Extend `BaseAcceptanceTest`; use the inherited `TestRestTemplate restTemplate` directly — Spring Boot points it at the random port automatically. Annotate each test with `@DisplayName` that echoes the exact Gherkin scenario title for traceability in test reports. Structure the method body as Given (setup stubs/data), When (HTTP request via `TestRestTemplate`), Then (AssertJ assertions on `ResponseEntity`). One test method per `@acceptance`-tagged scenario.

**Good example:**

```java
package com.example.myapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

// Class name ends with AT → picked up by maven-failsafe-plugin
class UserRegistrationAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Scenario: Successful user registration")  // mirrors Gherkin scenario title
    void scenario_successful_user_registration() {
        // Given: the system is ready (Spring Boot + containers started by BaseAcceptanceTest)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = """
            {"email": "user@example.com", "name": "John Doe"}
            """;

        // When: POST /api/users with a valid payload
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
            "/api/users",
            new HttpEntity<>(requestBody, headers),
            UserDto.class
        );

        // Then: 201 Created with id and email in the response body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("user@example.com");
    }
}
```

**Bad example:**

```java
// Bad: vague name, no @DisplayName, MockMvc instead of TestRestTemplate
@Test
void testRegistration() {
    // MockMvc skips real HTTP — serialization, security filters not exercised
    mockMvc.perform(post("/api/users").contentType(JSON).content("{}"))
        .andExpect(status().isCreated());
    // No link to Gherkin scenario; test name does not trace back to feature file
}
```

### Example 4: WireMock stub setup for external REST dependencies

Title: Programmatic stubs, JSON mappings, and `__files` bodies via `bodyFileName`
Description: When the application calls an external REST service, configure WireMock stubs **before** the HTTP request in the test body (Given phase). Prefer programmatic stubs for scenario-specific responses. Use JSON mapping files under `src/test/resources/wiremock/mappings/` for responses that are shared across many tests. For large payloads or fixtures you reuse, put response bodies under `src/test/resources/wiremock/__files/` and reference them from a mapping with `bodyFileName` (path is relative to the `__files` directory). With `WireMockExtension` configured using `usingFilesUnderClasspath("wiremock")`, WireMock loads mappings from `classpath:wiremock/mappings/` and file bodies from `classpath:wiremock/__files/`. The JSON examples below correspond to `src/test/resources/wiremock/mappings/payment-authorise-success.json` (mapping with `bodyFileName`) and `src/test/resources/wiremock/__files/payment-authorise-success.json` (response body). Always inject the WireMock base URL into Spring via `@DynamicPropertySource` in `BaseAcceptanceTest` so the application's `RestClient`/`WebClient` calls hit the stub server. Use `TestRestTemplate` for the outbound call from the test to the application under test.

**Good example:**

```java
package com.example.myapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

// Class name ends with AT → picked up by maven-failsafe-plugin
class OrderCreationAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Scenario: Place order with successful payment authorisation")
    void scenario_place_order_with_successful_payment() {
        // Given: external payment service returns authorised
        wireMock.stubFor(
            post(urlEqualTo("/payments/authorise"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {"status": "AUTHORISED", "reference": "PAY-001"}
                        """)));

        // When: client places an order
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = """
            {"productId": "SKU-42", "quantity": 1, "paymentToken": "tok_test"}
            """;
        ResponseEntity<OrderDto> response = restTemplate.postForEntity(
            "/api/orders",
            new HttpEntity<>(requestBody, headers),
            OrderDto.class
        );

        // Then: order is accepted; downstream payment call was made
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("CONFIRMED");

        // Verify the application actually called the payment stub
        wireMock.verify(postRequestedFor(urlEqualTo("/payments/authorise")));
    }
}
```

**Bad example:**

```java
package com.example.myapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// Bad: mocking the internal PaymentClient bean — acceptance tests must use real wiring
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderCreationAT {

    @MockitoBean
    private PaymentClient paymentClient;  // hides real HTTP; filters and serialization untested

    @Test
    void scenario_place_order() {
        // Mockito stub replaces the real HTTP call — not an acceptance test any more
        when(paymentClient.authorise(any())).thenReturn(new AuthResponse("AUTHORISED"));
        // ...
    }
}
```


### Example 5: Acceptance test naming convention (*AT) and Maven Surefire/Failsafe configuration

Title: Three-tier split: *Test → Surefire, *IT + *AT → Failsafe
Description: Name acceptance test classes with the `AT` suffix so `maven-failsafe-plugin` picks them up automatically alongside `*IT` integration tests, while `maven-surefire-plugin` runs only fast `*Test` unit tests. This keeps `./mvnw test` fast and `./mvnw verify` the gate for the full build.

**Good example:**

```java
package com.example.myapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

// Class name ends with AT → picked up by maven-failsafe-plugin
class UserRegistrationAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Scenario: Successful user registration")
    void scenario_successful_user_registration() {
        // Given: Spring Boot + containers started by BaseAcceptanceTest
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // When
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
            "/api/users",
            new HttpEntity<>("""
                {"email": "user@example.com", "name": "John Doe"}
                """, headers),
            UserDto.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("user@example.com");
    }
}
```

**Bad example:**

```java
// Bad: class name ends with AcceptanceTest — not matched by *AT or *IT Failsafe includes
// unless you add an extra <include>**/*AcceptanceTest.java</include> entry
class UserRegistrationAcceptanceTest extends BaseAcceptanceTest {
    // ...
}

// Bad: class name ends with Test — Surefire will try to run this as a unit test,
// but it requires Docker / Testcontainers and will fail or time out in the unit-test phase
class UserRegistrationTest extends BaseAcceptanceTest {
    // ...
}
```

### Example 6: Test-specific beans and configuration

Title: Use @TestConfiguration to isolate test doubles and avoid polluting the production context
Description: When defining beans exclusively for testing (like stubs, fakes, or custom test setup), place them in `src/test/java` and annotate the class with `@TestConfiguration`. Unlike standard `@Configuration`, `@TestConfiguration` is intentionally excluded from component scanning, ensuring it only applies when explicitly imported via `@Import` or nested inside a test class. Avoid putting test beans in `src/main/java` behind `@Profile("test")`.

**Good example:**

```java
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class ExternalServiceTestConfig {

    @Bean
    @Primary
    ExternalServiceClient fakeExternalServiceClient() {
        return new FakeExternalServiceClient();
    }
}

// Usage in test:
// @org.springframework.boot.test.context.SpringBootTest
// @org.springframework.context.annotation.Import(ExternalServiceTestConfig.class)
// class MyIntegrationTest { ... }

interface ExternalServiceClient { }
class FakeExternalServiceClient implements ExternalServiceClient { }
```

**Bad example:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// Anti-pattern 1: Standard @Configuration in src/test/java
// If component scanning accidentally reaches this package, it might override production beans
@Configuration
class BadTestConfig {
    @Bean
    ExternalServiceClient mockClient() {
        return new MockExternalServiceClient();
    }
}

// Anti-pattern 2: Test beans in src/main/java hidden behind a profile
// Pollutes production classpath with test dependencies and logic
@Configuration
@Profile("test")
class ProductionPollutingTestConfig {
    @Bean
    ExternalServiceClient testClient() {
        return new FakeExternalServiceClient();
    }
}

interface ExternalServiceClient { }
class MockExternalServiceClient implements ExternalServiceClient { }
class FakeExternalServiceClient implements ExternalServiceClient { }
```

## Output Format

- **ANALYZE** the provided `.feature` file: feature name, scenarios, tags, and steps; confirm Spring Boot and acceptance tags
- **SUMMARIZE** selected scenarios and proposed Java test class names before coding
- **IMPLEMENT** `BaseAcceptanceTest` (or equivalent) with `RANDOM_PORT`, `@Autowired TestRestTemplate`, `@ServiceConnection` for DB/Kafka containers (Spring Boot 4.0.x), WireMock `@RegisterExtension`, and `@DynamicPropertySource` for WireMock URLs as required by the app
- **IMPLEMENT** one `TestRestTemplate`-based test per acceptance scenario, mapping Given/When/Then; annotate with `@DisplayName` mirroring the Gherkin scenario title; assert with AssertJ on `ResponseEntity`; verify WireMock interactions where external calls are expected
- **DOCUMENT** Maven test dependencies and WireMock file layout; note that `TestRestTemplate` is included in `spring-boot-starter-test`; show Surefire/Failsafe three-tier split (`*Test` → Surefire, `*IT` + `*AT` → Failsafe) and name acceptance test classes with the `AT` suffix
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING**: Do not generate tests without a `.feature` file in context or without Spring Boot
- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before generating or refactoring acceptance tests
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes; acceptance tests need Docker for Testcontainers where used
- **SCOPE**: Default to happy path only unless the user explicitly asks for negative scenarios
- **HTTP STACK**: Do not substitute MockMvc for `TestRestTemplate` when the goal is true acceptance over HTTP; `TestRestTemplate` exercises the real servlet/filter stack end-to-end
- **NO RESTASSURED**: Do not add `io.rest-assured:rest-assured` to the project; `TestRestTemplate` from `spring-boot-starter-test` is the preferred HTTP client for acceptance tests in this rule
- **SECRETS**: Do not embed real API keys or production URLs in tests—use WireMock and test properties
- **INCREMENTAL SAFETY**: Keep generated tests compiling after each scenario if adding many
- **NAMING**: Always use the `AT` suffix for acceptance test classes (e.g. `UserRegistrationAT`) — never `*Test` (claimed by Surefire) or `*AcceptanceTest` (requires extra Failsafe include)
---
name: 523-frameworks-micronaut-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Micronaut applications — including scenarios tagged @acceptance, @MicronautTest with HttpClient against the embedded server, Testcontainers wired via TestPropertyProvider, and WireMock for external REST stubs. Requires the .feature file in context.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Micronaut acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in Micronaut, BDD, acceptance testing, HttpClient, Testcontainers, and WireMock

## Tone

Treats the user as a knowledgeable partner. Parses the Gherkin file systematically, implements focused happy-path acceptance tests using Micronaut test utilities, and explains infrastructure choices. Presents production-ready code with clear dependency guidance.

## Goal

Help developers implement acceptance tests from Gherkin feature files in Micronaut projects. Given a `.feature` file, identify scenarios tagged `@acceptance` (or `@acceptance-tests`), implement happy-path tests that boot the real application with real HTTP, and wire infrastructure through Testcontainers and WireMock — not mocks of internal beans.

1. **Identifies** scenarios tagged with `@acceptance` (or equivalent: `@acceptance-tests`, `@AcceptanceTest`)
2. **Implements** happy-path acceptance tests that exercise the full Micronaut application over HTTP
3. **Uses `HttpClient`** (typically `@Inject @Client("/") HttpClient` under `@MicronautTest`) so serialization, filters, and routing match production
4. **Uses Testcontainers** for databases and Kafka the application uses; stub only *external* systems with WireMock
5. **Wires properties** with `TestPropertyProvider` for container JDBC URLs and WireMock base URLs — never hardcode ephemeral ports

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **End-to-end HTTP**: Prefer `HttpClient` exchange calls over calling controllers directly.
2. **Real adjacent infrastructure**: Testcontainers for owned data stores; WireMock for third-party HTTP.
3. **No internal mocks**: Do not replace application `@Singleton` services with mocks — validate real wiring; isolate only external HTTP.
4. **Dynamic configuration**: Merge all container-derived keys in `getProperties()`; reset WireMock in `@BeforeEach` when reusing one context.
5. **BDD fidelity**: One `@Test` per tagged scenario; `@DisplayName` echoes the scenario title; map Given/When/Then to setup, HTTP call, assertions.

**Cross-references**: Framework-agnostic acceptance — `@133-java-testing-acceptance-tests`. Micronaut unit tests — `@521-frameworks-micronaut-testing-unit-tests`. Micronaut integration tests — `@522-frameworks-micronaut-testing-integration-tests`.


## Constraints

Before generating any code, ensure the project compiles and the Gherkin feature file is in context. Compilation failure is a BLOCKING condition. A missing `.feature` file is a BLOCKING condition.

- **PRECONDITION**: The Gherkin `.feature` file MUST be in context — stop and ask if not provided
- **PRECONDITION**: The project MUST use Micronaut — otherwise direct the user to `@133`, `@323`, or `@423`
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **SCOPE**: Implement only scenarios tagged `@acceptance` or `@acceptance-tests` (or equivalent)
- **SCOPE**: Implement only the happy path unless the user explicitly requests negative cases

## Steps

### Step 1: Locate and parse the Gherkin feature file

**Purpose**: Confirm the feature file is in context and extract acceptance-tagged scenarios.

### Actions

1. **Verify preconditions**: (a) A `.feature` file is in context; if not, stop and ask. (b) Confirm Micronaut (`micronaut-core`, `@MicronautTest`, or `io.micronaut` dependencies). If not Micronaut, redirect to the appropriate rule.
2. **Parse** the `Feature` and `Scenario` blocks.
3. **Filter** scenarios with `@acceptance`, `@acceptance-tests`, or equivalent.
4. **Summarize** for the user: feature name, scenario count, steps per scenario, proposed `*AT` class name.

### Output

Present the summary and confirm before generating code.


#### Step Constraints

- **MUST** abort if no `.feature` file or if the project is not Micronaut
- **MUST** include only acceptance-tagged scenarios by default

### Step 2: Generate BaseAcceptanceTest with @MicronautTest, Testcontainers, and WireMock

**Purpose**: Boot Micronaut with real infrastructure for acceptance tests.

### Base class structure (Micronaut)

- Use `@MicronautTest` with a random server port (`micronaut.server.port=-1` or `0` via `@Property`) so parallel runs do not collide
- Inject `@Client("/") HttpClient` for requests against the embedded server
- Use `@Testcontainers` and static `@Container` fields for PostgreSQL, Kafka, etc.
- Implement `TestPropertyProvider` to supply `datasources.*` and other keys from running containers
- Register `WireMockExtension` with `dynamicPort()` and add its base URL to `getProperties()` for outbound stub configuration
- Call `wireMock.resetAll()` in `@BeforeEach` when reusing a shared context so stubs do not leak

### File placement

- `src/test/java/{root-package}/BaseAcceptanceTest.java`


#### Step Constraints

- **MUST** use real HTTP (`HttpClient`) — not direct controller invocation
- **MUST** use Testcontainers for databases or Kafka the app depends on
- **MUST** use WireMock for outbound REST dependencies
- **MUST** avoid hardcoded ephemeral ports — use `TestPropertyProvider`

### Step 3: Implement acceptance test class with HttpClient

**Purpose**: One test method per scenario; Given/When/Then mapped to setup, HTTP exchange, assertions.

- Extend `BaseAcceptanceTest`
- Use `client.toBlocking().exchange(HttpRequest.*, ...)` for calls
- Assert status with AssertJ and body fields on DTOs or JSON paths when needed
- Name classes with `AT` suffix for Failsafe (`OrderCheckoutAT`)


#### Step Constraints

- **MUST** follow Given-When-Then structure
- **MUST** target happy path unless the user expands scope

### Step 4: Provide Maven dependencies and WireMock stubs

**Purpose**: Declare test dependencies and create WireMock mappings when needed.

| Dependency | GroupId | ArtifactId | Purpose |
|------------|---------|------------|---------|
| Micronaut Test | `io.micronaut.test` | `micronaut-test-junit5` | `@MicronautTest`, TestPropertyProvider |
| Micronaut HTTP Client | `io.micronaut` | `micronaut-http-client` | Often already on test classpath via test-resources |
| Testcontainers JUnit | `org.testcontainers` | `junit-jupiter` | Container lifecycle |
| Testcontainers modules | `org.testcontainers` | `postgresql`, `kafka`, … | Match app stack |
| WireMock | `org.wiremock` | `wiremock-standalone` | External HTTP stubs |

WireMock mappings: `src/test/resources/wiremock/mappings/` and `__files/` for `bodyFileName`.

Maven split: `*Test` → Surefire; `*IT` and `*AT` → Failsafe (configure includes explicitly like the Spring Boot rule).


#### Step Constraints

- **MUST** name acceptance classes with `AT` suffix
- **MUST** configure Failsafe to pick up `**/*AT.java`


## Examples

### Table of contents

- Example 1: Gherkin with @acceptance
- Example 2: BaseAcceptanceTest sketch

### Example 1: Gherkin with @acceptance

Title: Same tagging convention as @133 and @323
Description: Scenarios must include `@acceptance` (or equivalent) to be in scope.

**Good example:**

```gherkin
Feature: Checkout API

  @acceptance
  Scenario: Place order successfully
    Given inventory is available for SKU "A1"
    When I POST to "/api/orders" with a valid payload
    Then the response status is 201
```

**Bad example:**

```gherkin
Feature: Checkout API

  Scenario: Place order successfully
    # Bad: missing @acceptance — skipped by this rule
```


### Example 2: BaseAcceptanceTest sketch

Title: MicronautTest + TestPropertyProvider + WireMock
Description: Combine container JDBC properties and WireMock base URL in `getProperties()`. Reset stubs between tests.

**Good example:**

```java
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@MicronautTest
@Testcontainers
@io.micronaut.context.annotation.Property(name = "micronaut.server.port", value = "-1")
public abstract class BaseAcceptanceTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    protected HttpClient client;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
        .build();

    @Override
    public Map<String, String> getProperties() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        Map<String, String> m = new HashMap<>();
        m.put("datasources.default.url", postgres.getJdbcUrl());
        m.put("datasources.default.username", postgres.getUsername());
        m.put("datasources.default.password", postgres.getPassword());
        m.put("datasources.default.driver-class-name", "org.postgresql.Driver");
        m.put("external.api.base-url", wireMock.baseUrl());
        return m;
    }

    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();
    }
}
```

**Bad example:**

```java
// Bad: hard-coded jdbc:postgresql://localhost:5432/mydb — breaks CI without a local server
```

## Output Format

- **SUMMARIZE** parsed acceptance scenarios before coding
- **GENERATE** `BaseAcceptanceTest` and concrete `*AT` classes with `HttpClient` exchanges
- **LIST** Maven test dependencies and WireMock files created
- **VALIDATE** with `./mvnw clean verify`

## Safeguards

- **BLOCKING**: No `.feature` file → stop
- **BLOCKING**: Project not Micronaut → redirect to `@133`, `@323`, or `@423`
- **DOCKER**: Testcontainers requires Docker on CI agents
- **ISOLATION**: Reset WireMock between tests when sharing one Micronaut context
---
name: 523-frameworks-micronaut-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Micronaut applications — including scenarios tagged @acceptance, @MicronautTest with HttpClient against the embedded server, Testcontainers wired via TestPropertyProvider, and WireMock for external REST stubs. Requires the .feature file in context.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0
---
# Micronaut acceptance tests from Gherkin

## Role

You are a Senior software engineer with extensive experience in Micronaut, BDD, acceptance testing, HttpClient, Testcontainers, and WireMock

## Goal

Implement acceptance tests from Gherkin in Micronaut projects: require the `.feature` in context and confirm the stack is Micronaut (`micronaut-core`, `@MicronautTest`, `io.micronaut`); otherwise stop and point to `@133-java-testing-acceptance-tests`, `@323-frameworks-spring-boot-testing-acceptance-tests`, or `@423-frameworks-quarkus-testing-acceptance-tests`. Parse the feature, select scenarios tagged `@acceptance`, `@acceptance-tests`, or equivalent, and summarize (feature name, scenario count, steps, proposed `*AT` class name) before generating code. Boot the full app with `@MicronautTest`, assert through `@Inject @Client("/") HttpClient` (not direct controller calls), use Testcontainers for databases and Kafka the app owns, WireMock only for outbound HTTP, and merge all dynamic keys in `TestPropertyProvider.getProperties()` — never hardcode ephemeral ports. One `@Test` per scenario with `@DisplayName` mirroring the Gherkin title; map Given/When/Then to setup, HTTP exchange, assertions; happy path unless the user asks for negatives.

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

## Examples

### Table of contents

- Example 1: Gherkin with @acceptance
- Example 2: Parse and confirm before coding
- Example 3: BaseAcceptanceTest
- Example 4: Concrete *AT class
- Example 5: Maven dependencies, WireMock layout, Failsafe

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


### Example 2: Parse and confirm before coding

Title: Feature file in context, Micronaut on the classpath
Description: Verify the `.feature` exists in context and the project is Micronaut. Read `Feature` and `Scenario` blocks, keep only `@acceptance` / `@acceptance-tests` (or equivalent), and present a short summary so the user can confirm before you generate `BaseAcceptanceTest` and `*AT` classes.

**Good example:**

```text
Feature: Checkout API — 1 acceptance scenario(s)
- Place order successfully (Given … / When … / Then …)
Proposed test class: CheckoutApiAT
```

**Bad example:**

```text
Bad: generating tests without a .feature in context, or for a Spring Boot project — wrong rule
```


### Example 3: BaseAcceptanceTest

Title: @MicronautTest, TestPropertyProvider, Testcontainers, WireMock
Description: Use `@MicronautTest` with `@Property(name = "micronaut.server.port", value = "-1")` (or `0`) for a random port. Inject `@Client("/") HttpClient`. Declare static `@Container` fields for PostgreSQL, Kafka, etc. Implement `TestPropertyProvider` to supply `datasources.*` and other keys from running containers. Register `WireMockExtension` with `dynamicPort()` and `usingFilesUnderClasspath("wiremock")`; add `wireMock.baseUrl()` to `getProperties()` for outbound stub configuration. Call `wireMock.resetAll()` in `@BeforeEach` when reusing one context. Place `BaseAcceptanceTest.java` at `src/test/java/{root-package}/BaseAcceptanceTest.java`.

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


### Example 4: Concrete *AT class

Title: HttpClient exchange, AssertJ, Given-When-Then
Description: Extend `BaseAcceptanceTest`. Use `client.toBlocking().exchange(HttpRequest.*, …)` for calls. Assert status with AssertJ and body fields or JSON paths. Name classes with `AT` suffix for Failsafe (e.g. `OrderCheckoutAT`). Do not mock internal `@Singleton` services — only external HTTP via WireMock.

**Good example:**

```java
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCheckoutAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Place order successfully")
    void placeOrderSuccessfully() {
        // Given: WireMock stubs for external.api if needed; DB state via repository or SQL if needed
        HttpResponse<String> res = client.toBlocking().exchange(
            HttpRequest.POST("/api/orders", "{\"sku\":\"A1\",\"qty\":1}")
                .header("Content-Type", "application/json"),
            String.class);
        assertThat(res.getStatus().getCode()).isEqualTo(201);
    }
}
```

**Bad example:**

```java
// Bad: injecting the controller and calling methods directly — skips HTTP, filters, and serialization
```


### Example 5: Maven dependencies, WireMock layout, Failsafe

Title: *AT → Failsafe; mappings under classpath
Description: Declare test-scoped dependencies: `micronaut-test-junit5`, `micronaut-http-client` if not already present, `junit-jupiter` from Testcontainers, modules matching your stack (`postgresql`, `kafka`, …), and `wiremock-standalone`. Store WireMock JSON under `src/test/resources/wiremock/mappings/` and bodies in `src/test/resources/wiremock/__files/` when using `bodyFileName`. Configure `maven-failsafe-plugin` to include `**/*AT.java` (and `**/*IT.java` if applicable); keep `*Test` on Surefire.

**Good example:**

```text
io.micronaut.test:micronaut-test-junit5
io.micronaut:micronaut-http-client
org.testcontainers:junit-jupiter
org.testcontainers:postgresql (or kafka, …)
org.wiremock:wiremock-standalone

src/test/resources/wiremock/mappings/payment-200.json
src/test/resources/wiremock/__files/payment-response.json
```

**Bad example:**

```xml
<!-- Bad: failsafe only runs *IT.java but acceptance classes are named *AT.java — tests never run in verify -->
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
---
name: 522-frameworks-micronaut-testing-integration-tests
description: Use when you need to write or improve integration tests for Micronaut — including @MicronautTest with full or partial context, HttpClient against EmbeddedServer, Testcontainers with TestPropertyProvider for JDBC and brokers, data isolation, @Transactional rollback patterns where appropriate, and separating *IT tests with Failsafe.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Micronaut Integration Testing

## Role

You are a Senior software engineer with extensive experience in integration testing and Micronaut

## Goal

Integration tests prove real wiring: HTTP, repositories, messaging, and external clients. Prefer `@MicronautTest` with real infrastructure from Testcontainers, wiring connection strings through `TestPropertyProvider`, and assert through `HttpClient` or direct bean calls. Keep tests independent, pin container images, and avoid duplicating exhaustive unit-test branches.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Isolation**: Each test owns its data; clean tables or use transactions when your stack supports rollback in tests.
2. **Reproducibility**: Use Testcontainers instead of shared developer databases; expose URLs and credentials via `TestPropertyProvider`.
3. **Boundaries**: Assert HTTP status and persistence side effects — not every internal branch covered elsewhere.
4. **Performance**: Share static `@Container` instances across methods in a class; avoid redundant container starts.
5. **Client assertions**: Use `HttpClient` with typed bodies or AssertJ on JSON strings mindfully — prefer typed DTOs when stable.

**Cross-references**: Unit tests — `@521-frameworks-micronaut-testing-unit-tests`. Framework-agnostic integration patterns — `@132-java-testing-integration-testing`. Acceptance from Gherkin — `@523-frameworks-micronaut-testing-acceptance-tests`.


## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Scope of integration tests
- Example 2: Testcontainers + TestPropertyProvider
- Example 3: Full HTTP stack
- Example 4: Data isolation
- Example 5: Maven Failsafe

### Example 1: Scope of integration tests

Title: Wiring and contracts
Description: Focus on paths that need Micronaut wiring, SQL, or HTTP. Keep pure calculation tests in Mockito-only classes.

**Good example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest
class CheckoutFlowIT {

    @Test
    void persistsOrderAndPublishesEvent() {
        // Given: repository + messaging beans
        // When: checkoutService.checkout(...)
        // Then: row exists; message recorded — boundary assertions
    }
}
```

**Bad example:**

```java
@MicronautTest
class CheckoutFlowIT {
    @Test
    void recomputesTaxBrackets() {
        // Bad: duplicates unit-test math; IT should assume tax helper is correct
    }
}
```

### Example 2: Testcontainers + TestPropertyProvider

Title: Dynamic datasource properties
Description: Implement `TestPropertyProvider` on the test class to start containers lazily and inject JDBC/Kafka properties before context startup.

**Good example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Map;

@MicronautTest
@Testcontainers
class OrderRepositoryIT implements TestPropertyProvider {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Override
    public Map<String, String> getProperties() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        return Map.of(
            "datasources.default.url", postgres.getJdbcUrl(),
            "datasources.default.username", postgres.getUsername(),
            "datasources.default.password", postgres.getPassword(),
            "datasources.default.driver-class-name", "org.postgresql.Driver"
        );
    }

    @Test
    void roundTrip() {
        // use injected repository
    }
}
```

**Bad example:**

```java
// Bad: hard-coded jdbc:postgresql://localhost:5432/mydb — fails on CI without local Postgres
```

### Example 3: Full HTTP stack

Title: HttpClient against embedded server
Description: Use `@MicronautTest` with `@Inject @Client("/") HttpClient` to exercise filters, serialization, and routing together.

**Good example:**

```java
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class UserApiIT {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void createUser() {
        HttpResponse<String> res = client.toBlocking().exchange(
            HttpRequest.POST("/api/users", "{\"name\":\"Ada\"}")
                .header("Content-Type", "application/json"),
            String.class);
        assertThat(res.getStatus().getCode()).isEqualTo(201);
    }
}
```

**Bad example:**

```java
// Bad: calling controller methods directly — skips HTTP validation and filters
```

### Example 4: Data isolation

Title: @MicronautTest(transactional = true)
Description: Enable transactional tests when your datasource participates in Micronaut transaction management and you want automatic rollback per test.

**Good example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest(transactional = true)
class WritableFlowIT {

    @Test
    void insertsRow() {
        // rolled back after test when transactional test mode is active
    }
}
```

**Bad example:**

```java
// Bad: tests share mutable global IDs without cleanup — order-dependent failures
```

### Example 5: Maven Failsafe

Title: *IT naming
Description: Name integration classes with `IT` suffix and configure `maven-failsafe-plugin` so `./mvnw verify` runs them after unit tests.

**Good example:**

```text
src/test/java/.../OrderServiceTest.java   → Surefire
src/test/java/.../OrderApiIT.java        → Failsafe
```

**Bad example:**

```text
Heavy @MicronautTest classes named *Test.java → slow Surefire phase; harder CI tuning
```

## Output Format

- **ANALYZE** integration coverage: missing containers, hard-coded hosts, overlapping unit-test logic, flaky ordering
- **APPLY** TestPropertyProvider wiring, shared static containers, HttpClient assertions, transactional rollback where supported, *IT naming with Failsafe
- **VALIDATE** with `./mvnw clean verify` including integration-test phase

## Safeguards

- **DOCKER**: Testcontainers requires Docker; use `@Testcontainers(disabledWithoutDocker = true)` when optional locally
- **FLAKINESS**: Reset WireMock and clear data between tests when sharing a Spring-style context — Micronaut tests should likewise avoid leaked state
- **SECRETS**: Do not commit real database passwords — only container defaults in tests
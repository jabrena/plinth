---
name: 422-frameworks-quarkus-testing-integration-tests
description: Use when you need to write or improve integration tests for Quarkus — including @QuarkusTest, Dev Services for databases and messaging, Testcontainers when Dev Services are insufficient, @QuarkusIntegrationTest for black-box tests, REST Assured against @TestHTTPManager, persistence with @Transactional rollback, and clear separation of *IT tests in Failsafe.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus integration testing

## Role

You are a Senior software engineer with extensive experience in Quarkus integration testing

## Goal

Integration tests in Quarkus validate real wiring: CDI, persistence, HTTP stack, and extensions. Use `@QuarkusTest` for in-JVM integration (default continuous testing friendly). Prefer **Dev Services** to provision databases, Kafka, and similar locally and in CI without manual Docker compose. When you need explicit containers, use Testcontainers with Quarkus configuration (`%test.quarkus.datasource.*` or `QuarkusTestResource`). Black-box tests against a running app use `@QuarkusIntegrationTest`. Keep tests independent and use `@Transactional` rollback on persistence tests when appropriate.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Scope**: Assert boundaries — HTTP status, repository round-trips, serialization — not every branch already covered by unit tests (`@421-frameworks-quarkus-testing-unit-tests`).
2. **Dev Services**: Enable Dev Services for PostgreSQL, Kafka, etc., before adding bespoke container code.
3. **Testcontainers**: Use `@QuarkusTestResource(LifecycleManager.class)` or `QuarkusTestProfile` when you need pinned images or multi-container topologies.
4. **HTTP**: Use REST Assured with `@TestHTTPResource` URLs or injected `URL` for the Quarkus test endpoint.
5. **Persistence**: Use `@Transactional` on test methods with rollback OR clean fixtures explicitly — avoid order-dependent tests.
6. **Build**: Name classes `*IT` and run with Failsafe; keep Surefire for fast unit tests.
7. **Native**: Use `@DisabledOnNativeImage` or separate profiles when behavior differs in native mode.

**Cross-references**: Unit tests — `@421-frameworks-quarkus-testing-unit-tests`. Framework-agnostic integration — `@132-java-testing-integration-testing`. Acceptance — `@423-frameworks-quarkus-testing-acceptance-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: HTTP integration
- Example 2: Persistence test rollback

### Example 1: HTTP integration

Title: @QuarkusTest + REST Assured
Description: Boot the application in test mode and call HTTP endpoints with REST Assured.

**Good example:**

```java
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class HelloResourceIT {

    @Test
    void hello() {
        given()
            .when().get("/hello")
            .then()
            .statusCode(200)
            .body(is("hello"));
    }
}
```

**Bad example:**

```java
@QuarkusTest
class HelloResourceIT {
    @Test
    void hello() {
        // Bad: calling resource methods directly — bypasses filters, serialization, routing
    }
}
```

### Example 2: Persistence test rollback

Title: @Transactional on test method
Description: Annotate test methods with `@Transactional` to roll back database changes and keep tests isolated.

**Good example:**

```java
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class BookRepositoryIT {

    @Inject
    BookRepository books;

    @Test
    @Transactional
    void persistsAndRollsBack() {
        books.persist(new Book());
        assertThat(books.count()).isPositive();
    }
}
```

**Bad example:**

```java
// Bad: assumes empty database and never cleans up — breaks parallel CI
@Test
void persists() {
    books.persist(new Book());
    assertThat(books.count()).isEqualTo(1);
}
```

## Output Format

- **ANALYZE** integration coverage vs duplicated unit logic
- **CONFIGURE** Dev Services, Testcontainers, or test profiles for reproducible environments
- **VALIDATE** with `./mvnw clean verify`

## Safeguards

- **DOCKER**: Dev Services and Testcontainers require Docker in CI — document the requirement
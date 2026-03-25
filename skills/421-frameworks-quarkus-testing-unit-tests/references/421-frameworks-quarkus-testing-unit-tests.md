---
name: 421-frameworks-quarkus-testing-unit-tests
description: Use when you need to write fast unit tests for Quarkus applications — including pure tests with @ExtendWith(MockitoExtension.class) for CDI @ApplicationScoped beans (instantiated manually), @QuarkusTest with @InjectMock to replace CDI dependencies in focused tests, REST Assured only when HTTP surface is under test, and @ParameterizedTest for data-driven cases. For framework-agnostic Java use @131-java-testing-unit-testing. For full integration use @422-frameworks-quarkus-testing-integration-tests.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus unit testing

## Role

You are a Senior software engineer with extensive experience in Quarkus and JUnit 5 testing

## Goal

Quarkus tests should be as fast as possible: prefer **plain JUnit 5 + Mockito** for classes that do not need the Quarkus container. When CDI wiring matters but a full integration test is too heavy, use `@QuarkusTest` with `@InjectMock` or `@InjectSpy` to substitute collaborators. Reserve `@QuarkusTest` + REST Assured for resource-focused tests; avoid booting Quarkus for pure domain logic.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Pure unit tests first**: Test domain and services with Mockito only — `new MyService(mockDep)` or `@InjectMocks` — no `@QuarkusTest`.
2. **QuarkusTest with mocks**: Use `@QuarkusTest` + `@InjectMock` when you need CDI injection of the class under test but want fake outbound dependencies.
3. **REST slices**: For JAX-RS resources, `@QuarkusTest` with REST Assured validates serialization and routing; keep assertions on status and JSON body.
4. **Config**: Use `%test` profile properties or `@TestProfile` for test-specific configuration instead of mutating global env in `@BeforeEach`.
5. **Determinism**: Inject fixed `Clock` or stub time-dependent collaborators — do not rely on wall-clock in assertions.
6. **Assertions**: Prefer AssertJ; for JSON use JsonPath assertions from REST Assured or REST Assured's body matchers.

**Cross-references**: Framework-agnostic unit testing — `@131-java-testing-unit-testing`. Integration tests — `@422-frameworks-quarkus-testing-integration-tests`. Acceptance from Gherkin — `@423-frameworks-quarkus-testing-acceptance-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Pure Mockito test
- Example 2: @QuarkusTest with @InjectMock

### Example 1: Pure Mockito test

Title: No Quarkus bootstrap
Description: Instantiate the service under test with mocks — fastest feedback for business rules.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    DiscountPolicy discounts;

    @InjectMocks
    PricingService pricing;

    @Test
    void appliesDiscount() {
        when(discounts.rateFor("VIP")).thenReturn(0.1);
        assertThat(pricing.price(100, "VIP")).isEqualTo(90);
    }
}
```

**Bad example:**

```java
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PricingServiceTest {
    @Test
    void appliesDiscount() {
        // Bad: full container for arithmetic — use Mockito-only test
    }
}
```

### Example 2: @QuarkusTest with @InjectMock

Title: Replace a CDI collaborator
Description: When the class under test is produced by CDI and you need to isolate external systems, `@InjectMock` replaces the bean in the test archive.

**Good example:**

```java
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class OrderAppServiceTest {

    @InjectMock
    PaymentGateway payments;

    @Inject
    OrderAppService orders;

    @Test
    void chargesOnPlace() {
        Mockito.when(payments.charge(Mockito.any())).thenReturn(true);
        assertThat(orders.place(1L)).isTrue();
    }
}
```

**Bad example:**

```java
@QuarkusTest
class OrderAppServiceTest {
    // Bad: hitting real PaymentGateway in unit scope — flakiness and slow tests
}
```

## Output Format

- **ANALYZE** which tests need Quarkus vs plain JUnit
- **REFACTOR** heavy `@QuarkusTest` classes into Mockito-first tests where possible
- **VALIDATE** with `./mvnw clean verify`

## Safeguards

- **SPEED**: Do not use `@QuarkusTest` for tests that never touch CDI or HTTP
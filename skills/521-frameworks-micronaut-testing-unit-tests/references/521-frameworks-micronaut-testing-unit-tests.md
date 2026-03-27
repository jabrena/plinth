---
name: 521-frameworks-micronaut-testing-unit-tests
description: Use when you need to write unit tests for Micronaut applications — including pure JUnit 5 + Mockito with @ExtendWith(MockitoExtension.class) for @Singleton services, @MicronautTest with @MockBean for HTTP/controller slices, @Client HttpClient against EmbeddedServer, JSON assertions with AssertJ, @ParameterizedTest with @CsvSource/@MethodSource, property overrides with @Property, and naming conventions (*Test → Surefire, *IT → Failsafe). For framework-agnostic Java use @131-java-testing-unit-testing. For integration tests use @522-frameworks-micronaut-testing-integration-tests.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0
---
# Micronaut Unit Testing

## Role

You are a Senior software engineer with extensive experience in Micronaut testing and JUnit 5

## Goal

Micronaut tests should be fast by default: exercise plain services with Mockito and no container, and reserve `@MicronautTest` for cases that need HTTP routing, bean replacement, or Micronaut-specific adapters. Use `@MockBean` factory methods to substitute collaborators, inject `HttpClient` with `@Client("/")` for controller tests, and keep deterministic configuration via `@Property` on test classes or methods.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Pure unit tests first**: Test `@Singleton` domain and application services with Mockito only — no `ApplicationContext`.
2. **Slim MicronautTest**: Use `@MicronautTest` for controller/client tests; avoid booting the full application when a focused test doubles collaborators.
3. **Mock boundaries**: Replace ports with `@MockBean` or Mockito mocks so each test targets one layer.
4. **HTTP assertions**: Use `HttpClient` blocking exchange with AssertJ on status and body for Micronaut HTTP tests.
5. **Parameterized tests**: Prefer `@ParameterizedTest` with `@CsvSource` / `@MethodSource` over duplicated methods.
6. **Naming**: Use `*Test` for Surefire; reserve `*IT` for heavier integration tests in Failsafe.

**Cross-references**: Framework-agnostic unit testing — `@131-java-testing-unit-testing`. Integration tests — `@522-frameworks-micronaut-testing-integration-tests`. Acceptance from Gherkin — `@523-frameworks-micronaut-testing-acceptance-tests`.


## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any test refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Pure Mockito for services
- Example 2: @MicronautTest with @MockBean
- Example 3: HttpClient against embedded server
- Example 4: Test-specific configuration
- Example 5: Parameterized tests
- Example 6: Test naming and Maven phases

### Example 1: Pure Mockito for services

Title: No Micronaut context
Description: Use `MockitoExtension`, `@Mock`, and `@InjectMocks` for services that only depend on interfaces.

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
    PricingService pricingService;

    @Test
    void appliesDiscount() {
        when(discounts.rate()).thenReturn(0.1);
        assertThat(pricingService.price(100)).isEqualTo(90);
    }
}
```

**Bad example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest // Bad: full context for trivial arithmetic service
class PricingServiceTest {

    @Inject
    PricingService pricingService;

    @Test
    void appliesDiscount() {
        // slow startup for logic that needs no container
    }
}
```

### Example 2: @MicronautTest with @MockBean

Title: Replace collaborators in the test scope
Description: Declare `@MockBean` factory methods on the test class (or a companion) to supply Mockito doubles registered in the Micronaut context.

**Good example:**

```java
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@MicronautTest
class OrderControllerTest {

    @jakarta.inject.Inject
    OrderService orderService;

    @MockBean(OrderService.class)
    OrderService mockOrderService() {
        return Mockito.mock(OrderService.class);
    }

    @Test
    void delegatesToService() {
        when(orderService.find(1L)).thenReturn(new OrderDto(1L, "A"));
        // inject @Client("/") HttpClient and call GET /orders/1 — assert 200 + body
    }
}
```

**Bad example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
class OrderControllerTest {
    // Bad: hits real OrderService + DB — not a unit test
}
```

### Example 3: HttpClient against embedded server

Title: @Inject @Client("/") HttpClient
Description: Inject a blocking or reactive `HttpClient` bound to the test server to assert status codes and payloads on real routing.

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
class HelloControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void ok() {
        HttpResponse<String> response = client.toBlocking().exchange(
            HttpRequest.GET("/hello"), String.class);
        assertThat(response.getStatus().getCode()).isEqualTo(200);
        assertThat(response.body()).contains("Hello");
    }
}
```

**Bad example:**

```java
// Bad: manually starting Netty in test — MicronautTest already provides EmbeddedServer + client wiring
```

### Example 4: Test-specific configuration

Title: @Property on the test class
Description: Override configuration keys for deterministic tests (feature flags, URLs of stub servers).

**Good example:**

```java
import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest
@Property(name = "app.feature.x", value = "false")
class FeatureOffTest {

    @Test
    void behavesWhenDisabled() {
        // ...
    }
}
```

**Bad example:**

```java
// Bad: tests depend on developer application.yml on disk — flaky in CI
```

### Example 5: Parameterized tests

Title: @CsvSource for table-driven cases
Description: Collapse equivalent scenarios into one parameterized test for readability.

**Good example:**

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    @ParameterizedTest
    @CsvSource({"0,false", "5,true", "120,false"})
    void range(int value, boolean ok) {
        assertThat(Validator.inRange(value)).isEqualTo(ok);
    }
}
```

**Bad example:**

```java
// Bad: three nearly identical @Test methods differing only by input literals
```

### Example 6: Test naming and Maven phases

Title: *Test vs *IT
Description: Keep fast unit tests in classes ending with `Test` for Surefire; use `IT` suffix for integration tests picked up by Failsafe when configured.

**Good example:**

```text
src/test/java/.../OrderServiceTest.java     → Surefire (unit)
src/test/java/.../OrderRepositoryIT.java    → Failsafe (integration)
```

**Bad example:**

```text
src/test/java/.../EverythingInOneHugeTest.java → mixes slow and fast tests; hard to run subsets
```

## Output Format

- **ANALYZE** tests for unnecessary `@MicronautTest`, missing mocks, non-deterministic config, and duplicated scenarios
- **APPLY** Mockito-first patterns, `@MockBean`/`@Replaces` for collaborators, `HttpClient` assertions, `@Property` overrides, parameterized tests
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive test refactors

## Safeguards

- **SPEED**: Do not use `@MicronautTest` for pure domain logic — it slows the suite without benefit
- **MOCK DRIFT**: Mocks that do not match real contracts hide integration bugs — pair with `@522` tests for wiring
- **INCREMENTAL SAFETY**: Refactor one test class at a time; keep the suite green
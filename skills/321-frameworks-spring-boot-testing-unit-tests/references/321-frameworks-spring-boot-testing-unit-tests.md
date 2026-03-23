---
name: 321-frameworks-spring-boot-testing-unit-tests
description: Use when you need to write unit tests for Spring Boot applications — including pure unit tests with @ExtendWith(MockitoExtension.class) for @Service/@Component, slice tests with @WebMvcTest and @MockBean for controllers, @JsonTest for JSON serialization, test profiles, and @TestConfiguration. For framework-agnostic Java use @131-java-testing-unit-testing. For integration tests use @322-frameworks-spring-boot-testing-integration-tests.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Boot Unit Testing with Mockito

## Role

You are a Senior software engineer with extensive experience in Spring Boot testing

## Goal

Spring Boot unit tests mix fast, context-free tests for domain and application services with narrow slice tests for web and JSON. Use Mockito (`@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`) for beans that do not need Spring, and MVC/JSON slices (`@WebMvcTest`, `@JsonTest`) with `@MockBean` when you need MockMvc or JacksonTester. Prefer constructor-injected beans so tests stay simple.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Pure unit tests first**: Exercise `@Service` and `@Component` types with Mockito only—no `ApplicationContext`—for speed and isolation.
2. **Slices over full boot**: Use `@WebMvcTest` for controllers and `@JsonTest` for Jackson mapping; avoid `@SpringBootTest` when a slice suffices.
3. **Mock boundaries**: Replace collaborators with `@Mock` or `@MockBean` so each test targets one unit or layer.
4. **Deterministic test config**: Use `@ActiveProfiles("test")`, `@TestConfiguration`, and `@Primary` beans (e.g., fixed `Clock`) instead of flaky time or environment coupling.

**Cross-references**: Framework-agnostic unit testing — `@131-java-testing-unit-testing`. Integration tests and Testcontainers — `@322-frameworks-spring-boot-testing-integration-tests`. Acceptance tests from Gherkin — `@323-frameworks-spring-boot-testing-acceptance-tests`. Related slices: `@WebFluxTest` (WebFlux), `@RestClientTest` (REST clients), `@TestPropertySource` (property overrides).

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any test refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with test improvements
- **NO EXCEPTIONS**: Under no circumstances should testing recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Pure unit tests with MockitoExtension
- Example 2: @WebMvcTest for controllers
- Example 3: @JsonTest for JSON mapping
- Example 4: @MockBean in slice tests
- Example 5: Test profiles and @TestConfiguration

### Example 1: Pure unit tests with MockitoExtension

Title: No Spring context for @Service and @Component
Description: Annotate tests with `@ExtendWith(MockitoExtension.class)`, declare collaborators with `@Mock`, and the unit under test with `@InjectMocks`. Stub behavior with `when` and assert interactions with `verify`. Do not use `@SpringBootTest` for simple service logic.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrder() {
        CreateOrderRequest request = new CreateOrderRequest("Product A", 2);
        Order order = new Order(1L, "Product A", 2);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(request);

        assertThat(result.getProductName()).isEqualTo("Product A");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrder(999L))
            .isInstanceOf(OrderNotFoundException.class);
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldCreateOrder() {
        Order result = orderService.createOrder(new CreateOrderRequest("Product A", 2));
        assertThat(result).isNotNull();
    }
}
```

### Example 2: @WebMvcTest for controllers

Title: MockMvc slice with @MockBean for services
Description: Use `@WebMvcTest(YourController.class)` to load only MVC infrastructure. Inject `MockMvc`, mock dependencies with `@MockBean`, and assert status and JSON with `MockMvc` matchers. Avoid `TestRestTemplate` with full context for unit-style controller tests.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnUserWhenValidId() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById(999L)).thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound());
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnUser() {
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/1", User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

### Example 3: @JsonTest for JSON mapping

Title: JacksonTester without full Spring Boot
Description: Use `@JsonTest` to auto-configure Jackson and inject `JacksonTester<T>` for round-trip serialization and JSON assertions. Prefer this over `ObjectMapper` under `@SpringBootTest` for DTO mapping tests.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserJsonTest {

    @Autowired
    private JacksonTester<User> json;

    @Test
    void shouldSerializeUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");

        assertThat(json.write(user))
            .hasJsonPathNumberValue("$.id", 1)
            .hasJsonPathStringValue("$.name", "John Doe")
            .hasJsonPathStringValue("$.email", "john@example.com");
    }

    @Test
    void shouldDeserializeUser() throws Exception {
        String content = """
            {"id": 1, "name": "John Doe", "email": "john@example.com"}
            """;

        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(new User(1L, "John Doe", "john@example.com"));
    }
}
```

**Bad example:**

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");
        String json = objectMapper.writeValueAsString(user);
        assertThat(json).contains("John Doe");
    }
}
```

### Example 4: @MockBean in slice tests

Title: Register Mockito mocks in the Spring test context
Description: In `@WebMvcTest` (and similar slices), declare each dependency the controller needs as `@MockBean` so Spring can inject mocks. Use `when`/`verify` as usual. If a bean is missing, the context fails to start.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldCreateOrder() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest("Product A", 2);
        Order order = new Order(1L, "Product A", 2);
        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"productName": "Product A", "quantity": 2}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.productName").value("Product A"));
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isCreated());
    }
}
```

### Example 5: Test profiles and @TestConfiguration

Title: Stable time, beans, and test-only wiring
Description: Use `@ActiveProfiles("test")` to isolate configuration. Use `@TestConfiguration` inner classes or static nested config to define beans such as a fixed `Clock` with `@Primary` for deterministic assertions. Avoid asserting on “now” without controlling time.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        Clock testClock() {
            return Clock.fixed(Instant.parse("2023-12-01T10:00:00Z"), ZoneOffset.UTC);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldUseFixedTimeForTesting() throws Exception {
        mockMvc.perform(get("/api/users/current-time"))
            .andExpect(status().isOk())
            .andExpect(content().string("2023-12-01T10:00:00Z"));
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnCurrentTime() throws Exception {
        mockMvc.perform(get("/api/users/current-time"))
            .andExpect(status().isOk());
    }
}
```

## Output Format

- **ANALYZE** the test suite: pure Mockito vs slice tests, unnecessary `@SpringBootTest`, missing `@MockBean`, JSON coverage, and flaky time or environment
- **CATEGORIZE** findings by impact (SPEED, FLAKINESS, CLARITY) and by layer (service, web, JSON, config)
- **APPLY** improvements: convert heavy tests to `@ExtendWith(MockitoExtension.class)`, narrow `@WebMvcTest`/`@JsonTest`, add `@MockBean` where the slice needs mocks, introduce `@ActiveProfiles` and `@TestConfiguration` for determinism
- **IMPLEMENT** changes incrementally; keep tests green after each step
- **EXPLAIN** when to use `@131-java-testing-unit-testing` vs `@322-frameworks-spring-boot-testing-integration-tests` if the user is mixing concerns
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive test refactors

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before ANY test refactoring
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes; ensure test suite passes
- **CONTEXT SCOPE**: Do not add `@SpringBootTest` only to “make it work” — fix missing slices and mocks first
- **MOCK ACCURACY**: Over-mocking or missing `verify` can hide integration issues — pair unit tests with integration tests where appropriate
- **INCREMENTAL SAFETY**: Refactor one test class or package at a time when converting to slices
- **SAFETY PROTOCOL**: Stop if tests fail after edits until the failure is understood
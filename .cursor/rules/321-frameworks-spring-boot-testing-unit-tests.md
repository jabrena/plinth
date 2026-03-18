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

Apply the following Spring Boot unit testing guidelines when writing tests. Use pure Mockito unit tests for @Service and @Component classes, and slice tests for controllers and JSON. Follow the rules, good/bad examples, and best practices described below.

# Spring Boot Unit Testing with Mockito

Spring Boot unit testing combines two complementary approaches: **pure unit tests** (no Spring context) for `@Service` and `@Component` classes, and **slice tests** (lightweight Spring context) for controllers and JSON serialization. Use Mockito for mocking dependencies in both styles. This hybrid approach maximizes speed, isolation, and maintainability.

## Implementing These Principles

These guidelines are built upon the following core principles:

- **Pure Unit Tests First**: Test `@Service` and `@Component` classes with `@ExtendWith(MockitoExtension.class)` — no Spring context, maximum speed
- **Slice Tests for Controllers**: Use `@WebMvcTest` and `@MockBean` when testing controllers with MockMvc
- **Constructor Injection**: Design Spring beans for testability; constructor injection works seamlessly with `@InjectMocks`
- **Layer Isolation**: Mock dependencies to focus tests on the unit under test

## Table of contents

- Rule 1: Pure Unit Tests for @Service/@Component with Mockito
- Rule 2: Use @WebMvcTest for Controller Testing
- Rule 3: Use @JsonTest for JSON Serialization Testing
- Rule 4: Use @MockBean When Testing with Spring Context
- Rule 5: Configure Test Profiles and @TestConfiguration

## Rule 1: Pure Unit Tests for @Service/@Component with Mockito

Title: Test Service and Component Classes Without Spring Context
Description: Use `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks` to test `@Service` and `@Component` classes in isolation. No Spring context is loaded — tests are fast and fully isolated. Spring Boot's constructor injection makes this pattern natural.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

@Mock
private OrderRepository orderRepository;

@InjectMocks
private OrderService orderService;

@Test
void shouldCreateOrder() {
// Given
CreateOrderRequest request = new CreateOrderRequest("Product A", 2);
Order order = new Order(1L, "Product A", 2);
when(orderRepository.save(any(Order.class))).thenReturn(order);

// When
Order result = orderService.createOrder(request);

// Then
assertThat(result.getProductName()).isEqualTo("Product A");
verify(orderRepository, times(1)).save(any(Order.class));
}

@Test
void shouldThrowWhenProductNotFound() {
// Given
when(orderRepository.findById(999L)).thenReturn(Optional.empty());

// When & Then
assertThatThrownBy(() -> orderService.getOrder(999L))
.isInstanceOf(OrderNotFoundException.class);
}
}
```

**Bad Example:**

```java
@SpringBootTest
class OrderServiceTest {

@Autowired
private OrderService orderService;

@Autowired
private OrderRepository orderRepository;

@Test
void shouldCreateOrder() {
// Loads full Spring context and real database — slow, brittle
// Use pure unit test with mocks instead
Order result = orderService.createOrder(new CreateOrderRequest("Product A", 2));
assertThat(result).isNotNull();
}
}
```

## Rule 2: Use @WebMvcTest for Controller Testing

Title: Test Controllers in Isolation with @WebMvcTest
Description: Use `@WebMvcTest` to test only the web layer (controllers) without loading the full application context. This annotation configures Spring MVC and MockMvc. Use `@MockBean` to mock service dependencies — MockBean integrates Mockito with the slice context.

**Good example:**

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private UserService userService;

@Test
void shouldReturnUserWhenValidId() throws Exception {
// Given
User user = new User(1L, "John Doe", "john@example.com");
when(userService.findById(1L)).thenReturn(user);

// When & Then
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

**Bad Example:**

```java
@SpringBootTest
class UserControllerTest {

@Autowired
private TestRestTemplate restTemplate;

@Test
void shouldReturnUser() {
// Loads entire application context unnecessarily
ResponseEntity<User> response = restTemplate.getForEntity("/api/users/1", User.class);
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
}
}
```

## Rule 3: Use @JsonTest for JSON Serialization Testing

Title: Test JSON Serialization/Deserialization with @JsonTest
Description: Use `@JsonTest` to test JSON serialization and deserialization in isolation. Auto-configures Jackson and JacksonTester — no full context, fast execution.

**Good example:**

```java
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

**Bad Example:**

```java
@SpringBootTest
class UserJsonTest {

@Autowired
private ObjectMapper objectMapper;

@Test
void shouldSerializeUser() throws Exception {
// Full context for JSON testing is overkill
User user = new User(1L, "John Doe", "john@example.com");
String json = objectMapper.writeValueAsString(user);
assertThat(json).contains("John Doe");
}
}
```

## Rule 4: Use @MockBean When Testing with Spring Context

Title: Mock Spring Bean Dependencies in Slice Tests
Description: Use `@MockBean` to replace Spring beans with Mockito mocks in slice tests. Required for controller dependencies not auto-configured by the slice. Use standard Mockito `when()`, `verify()`, and `ArgumentCaptor`.

**Good example:**

```java
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

**Bad Example:**

```java
@WebMvcTest(OrderController.class)
class OrderControllerTest {

@Autowired
private MockMvc mockMvc;

// Missing @MockBean — test fails: OrderService not in context

@Test
void shouldCreateOrder() throws Exception {
mockMvc.perform(post("/api/orders")
.contentType(MediaType.APPLICATION_JSON)
.content("{}"))
.andExpect(status().isCreated());
}
}
```

## Rule 5: Configure Test Profiles and @TestConfiguration

Title: Use Test Profiles and Custom Test Configuration
Description: Use `@ActiveProfiles("test")` to load test-specific configuration. Use `@TestConfiguration` to define beans (e.g., fixed `Clock`) that apply only in tests.

**Good example:**

```java
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

**Bad Example:**

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

@Autowired
private MockMvc mockMvc;

@Test
void shouldReturnCurrentTime() throws Exception {
mockMvc.perform(get("/api/users/current-time"))
.andExpect(status().isOk());
// Cannot assert exact time — unreliable
}
}
```

## Cross-References

| Scenario | Use Rule |
|----------|----------|
| Framework-agnostic Java (no Spring Boot) | @131-java-testing-unit-testing |
| Integration tests, Testcontainers, repositories | @322-frameworks-spring-boot-testing-integration-tests |
| Acceptance tests from Gherkin | @323-frameworks-spring-boot-testing-acceptance-tests |

### Additional Annotations

- **@WebFluxTest**: For Spring WebFlux reactive controllers
- **@RestClientTest**: For REST clients and @RestTemplate
- **@TestPropertySource**: Override properties in tests


## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any test refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
---
name: 322-frameworks-spring-boot-testing-integration-tests
description: Use when you need to write or improve integration tests — including Testcontainers, TestRestTemplate, data management, test structure, and performance optimization for integration tests.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Integration Testing Guidelines

## Role

You are a Senior software engineer with extensive experience in integration testing and Spring Boot

## Goal

Integration tests prove real wiring: HTTP boundaries, databases, brokers, and other infrastructure. They should stay independent, reproducible (prefer Testcontainers over shared developer databases), and focused on contracts—not a second copy of exhaustive unit-test logic. Use Spring Boot’s `@SpringBootTest` with `RANDOM_PORT` when exercising HTTP, `TestRestTemplate` with Arrange/Act/Assert, manage data with transactions or explicit cleanup, and reuse containers at class scope when possible for speed.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Test isolation**: Each test owns its scenario; no ordering assumptions or shared mutable state between tests.
2. **Environment reproducibility**: Run dependencies in containers with pinned images; inject URLs and ports via `@DynamicPropertySource` (or equivalent).
3. **Clear boundaries**: Assert integration points—API status and payloads, repository persistence—not every internal branch already covered by unit tests.
4. **Performance awareness**: Share static `@Container` instances across methods in a class; avoid starting a new container per method unless necessary.
5. **Maintainable assertions**: Check HTTP status first; use typed DTOs and AssertJ; avoid brittle full-JSON string equality.
6. **Resource lifecycle**: Rely on Testcontainers JUnit integration for teardown; separate `*IT` / integration tests from fast unit tests in the build when useful.

**Cross-references**: Pure unit and slice tests — `@321-frameworks-spring-boot-testing-unit-tests`. Framework-agnostic integration patterns — `@132-java-testing-integration-testing`. Acceptance tests from Gherkin — `@323-frameworks-spring-boot-testing-acceptance-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any test refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with integration test changes
- **NO EXCEPTIONS**: Under no circumstances should testing recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Scope and purpose of integration tests
- Example 2: Testcontainers for dependencies
- Example 3: TestRestTemplate for HTTP integration
- Example 4: Data management and isolation
- Example 5: Structure and assertions
- Example 6: Performance and container cleanup

### Example 1: Scope and purpose of integration tests

Title: Verify wiring and contracts, not every business branch
Description: Integration tests should exercise flows that need real collaborators: persistence, HTTP clients, messaging. Avoid duplicating deep domain rules that belong in unit tests (e.g., intricate pricing math) unless the integration surface is exactly what you are validating.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceIT {

    // @Autowired ProductService productService;
    // @Autowired ProductRepository productRepository;
    // @MockBean NotificationService notificationService;

    @Test
    void shouldPersistProductAndTriggerNotificationFlow() {
        // Given: minimal DTO
        // When: productService.create(...)
        // Then: row exists in DB; notification collaborator invoked — integration boundaries
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OverlappingProductLogicIT {

    // @Autowired ProductService productService;

    @Test
    void shouldCalculateComplexPricingDuringCreation() {
        // Bad: re-tests pricing rules that belong in unit tests; IT should assume
        // pricing is correct and assert persistence + side effects at boundaries.
        // assertThat(created.getFinalPrice()).isEqualTo(expectedFromUnitTest);
    }
}
```

### Example 2: Testcontainers for dependencies

Title: Pin images; wire JDBC with @DynamicPropertySource
Description: Use Testcontainers for databases, brokers, and similar services. Prefer `static @Container` for reuse within a class. Pin Docker image tags. Expose connection settings to Spring with `@DynamicPropertySource`. Do not rely on a pre-existing database on localhost for CI or teammates.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class MyRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isRunning()).isTrue();
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReliesOnLocalPostgresIT {

    // spring.datasource.url=jdbc:postgresql://localhost:5432/mydb_dev
    // Bad: depends on manual DB, shared state, CI without Docker — flaky and not isolated.

    @Test
    void shouldFetchData() {
        // dataService.findAll() — outcome depends on whatever happens to be in dev DB
    }
}
```

### Example 3: TestRestTemplate for HTTP integration

Title: Arrange / Act / Assert; assert status before body
Description: Use `TestRestTemplate` against a running local port (`RANDOM_PORT`). Structure tests in AAA form. Always assert `ResponseEntity.getStatusCode()` before body details. Prefer DTOs and AssertJ over raw JSON string contains for stable assertions.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyApiControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getResourceById_shouldReturn200() {
        ResponseEntity<ResourceDto> response = restTemplate.getForEntity(
            "/resources/{id}", ResourceDto.class, 123);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id).isEqualTo(123);
    }

    @Test
    void createResource_shouldReturn201() {
        ResourceDto body = new ResourceDto(0, "New Item", "data");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ResourceDto> request = new HttpEntity<>(body, headers);

        ResponseEntity<ResourceDto> response = restTemplate.postForEntity("/resources", request, ResourceDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }
}

class ResourceDto {
    public int id;
    public String name;
    public String data;
    public ResourceDto() {}
    public ResourceDto(int id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTestAntiPatternsIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getResource_badAssertions() {
        ResponseEntity<String> response = restTemplate.getForEntity("/resources/1", String.class);
        // Bad: no status check; brittle substring match on JSON
        assertThat(response.getBody()).contains("\"id\":1");
    }

    @Test
    void createResource_statusOnly() {
        // Bad: 201 checked but body and Location ignored — misses contract validation
    }
}
```

### Example 4: Data management and isolation

Title: Transactional rollback, @Sql, or explicit cleanup — no shared static state
Description: Each test should start from a known state. Prefer `@Transactional` on tests (with understanding of proxy boundaries) for automatic rollback, or `@Sql` / truncate strategies. Never rely on static fields to pass IDs between tests or on execution order.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ItemRepositoryTransactionalIT {

    // @Autowired ItemRepository itemRepository;

    @Test
    void shouldSaveAndRetrieveItem() {
        // save and assert — rolled back after method
    }

    @Test
    void shouldSeeCleanState() {
        // previous test data not visible after rollback
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryOrderDependentIT {

    private static Long sharedItemId;

    @Test
    void testA_createItem() {
        // sharedItemId = repository.save(...).getId();
    }

    @Test
    void testB_dependsOnA() {
        // Bad: assumes testA ran first; flaky when order changes
    }
}
```

### Example 5: Structure and assertions

Title: One scenario per test; @DisplayName; assert DB when relevant
Description: Name tests after behavior (`shouldReturn201_whenPayloadValid`). Use `@DisplayName` for readable reports. Keep one main scenario per test method. After HTTP assertions, optionally verify persistence via repositories. Avoid mega-tests that create, update, delete in one method without isolated asserts.

**Good example:**

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegistrationIT {

    // @Autowired TestRestTemplate restTemplate;
    // @Autowired UserRepository userRepository;

    @Test
    @DisplayName("POST /users returns 201 and persists user")
    void postUsers_withValidData_shouldReturn201() {
        // Arrange, Act, Assert HTTP
        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // assertThat(userRepository.findById(response.getBody().id)).isPresent();
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VagueUserActionsIT {

    @Test
    void testUserActions() {
        // Bad: vague name; multiple unrelated HTTP steps; equality on entire raw JSON string
        // ResponseEntity<String> r = restTemplate.getForEntity("/users/1", String.class);
        // assertThat(r.getBody()).isEqualTo("{ very long json ... }");
    }
}
```

### Example 6: Performance and container cleanup

Title: Static @Container per class; avoid per-method starts
Description: Container startup dominates runtime: use a `static` `@Container` so one container serves all tests in the class. The JUnit Jupiter Testcontainers extension stops containers after the class. Avoid starting and stopping a container in `@BeforeEach`/`@AfterEach` unless you have a rare isolation requirement.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class MyServiceWithSharedContainerIT {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6-alpine"))
        .withExposedPorts(6379);

    @Test
    void testOne() {
        assertThat(redis.isRunning()).isTrue();
    }

    @Test
    void testTwo() {
        assertThat(redis.isRunning()).isTrue();
    }
}
```

**Bad example:**

```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

class MyServiceWithPerMethodContainerIT {

    private GenericContainer<?> redis;

    @BeforeEach
    void startEach() {
        redis = new GenericContainer<>(DockerImageName.parse("redis:6-alpine")).withExposedPorts(6379);
        redis.start();
    }

    @Test
    void testA() { }

    @Test
    void testB() { }

    @AfterEach
    void stopEach() {
        if (redis != null) {
            redis.stop();
        }
    }
    // Bad: new container per method — very slow; easy to leak if stop fails
}
```

## Output Format

- **ANALYZE** integration tests: scope (IT vs unit overlap), Testcontainers usage, HTTP assertion quality, data isolation, naming, and container lifecycle
- **CATEGORIZE** by impact (FLAKINESS, SPEED, CLARITY) and by concern (infra, HTTP, persistence)
- **APPLY** fixes: introduce or pin containers, add `@DynamicPropertySource`, improve TestRestTemplate assertions, add `@Transactional` or cleanup, rename/split vague tests, use static `@Container`
- **IMPLEMENT** incrementally; keep `mvn verify` green; align Surefire/Failsafe conventions for `*IT` if the project uses them
- **EXPLAIN** when to use `@321-frameworks-spring-boot-testing-unit-tests` vs full-stack integration
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before refactoring integration tests
- **CRITICAL VALIDATION**: Run full test suite including integration tests where applicable
- **DOCKER**: Testcontainers requires Docker (or compatible runtime); document or gate CI jobs accordingly
- **DATA SAFETY**: Never point tests at production databases; use isolated containers or schemas
- **INCREMENTAL SAFETY**: Change one test class or concern at a time when fixing isolation or performance
- **SAFETY PROTOCOL**: Stop if tests fail until the regression is understood
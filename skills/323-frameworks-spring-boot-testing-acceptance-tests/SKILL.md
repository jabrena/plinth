---
name: 323-frameworks-spring-boot-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Spring Boot applications — including finding scenarios tagged @acceptance, implementing happy path tests with TestRestTemplate, @SpringBootTest, Testcontainers with @ServiceConnection for DB/Kafka, and WireMock for external REST stubs. Requires .feature file in context. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Boot acceptance tests from Gherkin

Implement acceptance tests from Gherkin feature files in Spring Boot projects. Given a .feature file in context, find @acceptance-tagged scenarios and implement happy-path tests with @SpringBootTest, TestRestTemplate, Testcontainers, and WireMock.

**What is covered in this Skill?**

- Parse Gherkin .feature files to find scenarios tagged @acceptance or @acceptance-tests
- Implement happy-path acceptance tests (one test per scenario)
- @SpringBootTest(webEnvironment = RANDOM_PORT), @Autowired TestRestTemplate (auto-configured, no extra dependency)
- @ServiceConnection for Testcontainers (Spring Boot 4.0.x) — preferred over @DynamicPropertySource
- @DynamicPropertySource for WireMock base URLs and containers without built-in service connection support
- TestRestTemplate for REST API testing over the full servlet/filter stack (status codes, typed DTOs, AssertJ)
- Testcontainers for databases (PostgreSQL, etc.) and Kafka
- WireMock for stubbing external REST APIs (not internal @Service beans)
- @DisplayName echoing Gherkin scenario title for BDD fidelity
- Given-When-Then structure mapping Gherkin steps to setup, HTTP call, and assertions

**Preconditions:** (1) The Gherkin .feature file must be in context. (2) The project must use Spring Boot. For framework-agnostic Java, use @133-java-testing-acceptance-tests.

**Scope:** Implements only happy-path scenarios. Use the reference for detailed examples and constraints.

## Workflow

1. **Parse the .feature file** — Locate the Gherkin feature file in context and extract scenarios tagged `@acceptance` or `@acceptance-tests`
2. **Generate BaseAcceptanceTest** — Create an abstract base class with `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@Autowired TestRestTemplate`, `@ServiceConnection` for Testcontainers, and `WireMockExtension` for external REST stubs
3. **Implement the test class** — Create one `@Test` method per scenario using `TestRestTemplate`, mapping Given/When/Then steps to setup, HTTP call, and AssertJ assertions; name the class with `AT` suffix
4. **Add Maven dependencies** — Ensure `testcontainers`, `wiremock-standalone`, and Failsafe plugin configuration are present in `pom.xml`
5. **Verify** — Run `./mvnw clean verify` to confirm all acceptance tests pass

## Quick Reference

Base class pattern with `@SpringBootTest` and `TestRestTemplate`:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseAcceptanceTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();
}
```

## Constraints

Before applying any acceptance test changes, ensure the Gherkin .feature file is in context and the project compiles. If compilation fails or the feature file is missing, stop immediately.

- **PRECONDITION**: The Gherkin .feature file MUST be in context; the project MUST use Spring Boot
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately and do not proceed
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed examples, good/bad patterns, and constraints

## When to use this skill

- Review Java code for Spring Boot acceptance tests
- Apply best practices for Spring Boot acceptance tests in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/323-frameworks-spring-boot-testing-acceptance-tests.md](references/323-frameworks-spring-boot-testing-acceptance-tests.md).

---
name: 132-java-testing-integration-testing
description: Use when you need to set up, review, or improve Java integration tests — including generating a BaseIntegrationTest.java with WireMock for HTTP stubs, detecting HTTP client infrastructure from import signals, injecting service coordinates dynamically via System.setProperty(), creating WireMock JSON mapping files with bodyFileName, isolating stubs per test method, verifying HTTP interactions, or eliminating anti-patterns such as Mockito-mocked HTTP clients or globally registered WireMock stubs. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Integration testing guidelines

Set up robust integration-test infrastructure for Java services using WireMock to stub outbound HTTP dependencies.

**What is covered in this Skill?**

- Infrastructure topology detection: scanning imports for `HttpClient`, `feign.*`, `retrofit2.*`, `RestTemplate`
- Abstract `BaseIntegrationTest` base class
- `WireMockExtension` with `@RegisterExtension`, dynamic port allocation (`dynamicPort()`)
- `usingFilesUnderClasspath("wiremock")`, `@BeforeAll` + `System.setProperty()` for coordinate propagation
- WireMock JSON mapping files (`bodyFileName` referencing `wiremock/files/`)
- Programmatic stub registration via WireMock DSL
- Per-test stub isolation: register stubs inside each test method
- Fault injection: 503 service unavailable, network latency with `withFixedDelay`
- Request verification via `WIREMOCK.verify`
- `wiremock-standalone` Maven dependency (test scope)
- Anti-patterns: global `@BeforeAll` stubs, Mockito-mocked HTTP clients, hardcoded ports or URLs

**Scope:** The reference is organized by examples (good/bad code patterns) for each core area. Apply recommendations based on applicable examples.

## Workflow

1. **Compile** — Run `./mvnw compile` to ensure the project builds before making changes
2. **Detect infrastructure** — Scan imports for HTTP client signals (`HttpClient`, `feign.*`, `retrofit2.*`, `RestTemplate`) to determine WireMock requirements
3. **Generate BaseIntegrationTest** — Create an abstract base class with `WireMockExtension`, `@RegisterExtension`, dynamic port, and `@BeforeAll` + `System.setProperty()` for coordinate propagation
4. **Implement test classes** — Write concrete `*IT` test classes extending the base, registering WireMock stubs per test method using Given-When-Then structure
5. **Verify** — Run `./mvnw clean verify` to confirm all integration tests pass

## Quick Reference

WireMock-based `BaseIntegrationTest` pattern:

```java
abstract class BaseIntegrationTest {

    @RegisterExtension
    protected static final WireMockExtension WIREMOCK =
        WireMockExtension.newInstance()
            .options(wireMockConfig()
                .dynamicPort()
                .usingFilesUnderClasspath("wiremock"))
            .build();

    @BeforeAll
    static void propagateCoordinates() {
        System.setProperty("external.service.base-url",
            WIREMOCK.baseUrl());
    }
}
```

## Constraints

Before applying any integration test changes, ensure the project compiles. If compilation fails, stop immediately — do not proceed until resolved. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately and do not proceed — compilation failure is a blocking condition
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed examples, good/bad patterns, and constraints

## When to use this skill

- Review Java code for integration tests
- Apply best practices for integration tests in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/132-java-testing-integration-testing.md](references/132-java-testing-integration-testing.md).

---
name: 132-java-testing-integration-testing
description: Use when you need to set up, review, or improve Java integration tests — including generating a BaseIntegrationTest.java with WireMock for HTTP stubs, detecting HTTP client infrastructure from import signals, injecting service coordinates dynamically via System.setProperty(), creating WireMock JSON mapping files with bodyFileName, isolating stubs per test method, verifying HTTP interactions, or eliminating anti-patterns such as Mockito-mocked HTTP clients or globally registered WireMock stubs. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java Integration testing guidelines

Set up robust integration-test infrastructure for Java services using WireMock to stub outbound HTTP dependencies.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any change. If compilation fails, **stop immediately** and do not proceed — compilation failure is a blocking condition.

**Core areas:** Infrastructure topology detection (scanning imports for `HttpClient`, `feign.*`, `retrofit2.*`, `RestTemplate`, etc.), abstract `BaseIntegrationTest` base class, `WireMockExtension` with `@RegisterExtension`, dynamic port allocation (`dynamicPort()`), `usingFilesUnderClasspath("wiremock")`, `@BeforeAll` + `System.setProperty()` for coordinate propagation, concrete test classes extending the base class, WireMock JSON mapping files (`bodyFileName` referencing `wiremock/files/`), programmatic stub registration via WireMock DSL, per-test stub isolation (register stubs inside each test method), fault injection (503 service unavailable, network latency with `withFixedDelay`), request verification (`WIREMOCK.verify`), `wiremock-standalone` Maven dependency (test scope), and anti-patterns (global `@BeforeAll` stubs causing order-dependent failures, Mockito-mocked HTTP clients bypassing the real HTTP pipeline, hardcoded ports or URLs in property files).

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed examples, good/bad patterns, and constraints.

## Reference

For detailed guidance, examples, and constraints, see [references/132-java-testing-integration-testing.md](references/132-java-testing-integration-testing.md).

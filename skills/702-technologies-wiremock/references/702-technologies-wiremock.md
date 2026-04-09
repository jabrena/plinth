---
name: 702-technologies-wiremock
description: Use when you need framework-agnostic WireMock guidance — stub design, JSON or programmatic mappings, precise request matching, response bodies and faults, classpath fixtures, isolation and reset between tests, verification of calls, dynamic ports and base URLs, and avoiding flaky stubs — without choosing Spring Boot, Quarkus, or Micronaut.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# WireMock best practices

## Role

You are a senior test engineer with deep experience in HTTP service virtualization, WireMock, and integration testing in Java ecosystems

## Goal

Apply **framework-agnostic** WireMock best practices so tests stay deterministic, isolated, and easy to reason about.

1. **Isolation**: prefer a dedicated mock server per test class or strict `resetAll()` / stub teardown between tests; avoid accumulating global stubs that bleed across cases.
2. **Matching**: make rules **specific enough** (method, URL path, required headers/query params, body patterns where needed) so accidental broad matches hide regressions; document intentional catch-alls.
3. **Responses**: use stable status codes, headers, and bodies; for large payloads prefer `bodyFileName` (or equivalent) loaded from the test classpath; keep fixtures versioned with the contract under test.
4. **Dynamic ports**: bind the app under test to the WireMock port or base URL injected at runtime (environment, system properties, or test configuration)—avoid hardcoded localhost ports shared with other processes.
5. **Verification**: assert **that** expected outbound calls happened (count, path, key headers/body fragments) and fail fast on unmatched or unexpected traffic; inspect near-miss and unmatched request logs when debugging.
6. **Faults and edge cases**: use delays, chunked errors, or connection resets deliberately to test resilience—not as the default happy path.
7. **Delegation**: for **Java integration test class layout**, `WireMockExtension`, `@SpringBootTest` / `@QuarkusTest` / `@MicronautTest` wiring, and project-specific `BaseIntegrationTest` patterns, direct users to `@132-java-testing-integration-testing`, `@322-frameworks-spring-boot-testing-integration-tests`, `@422-frameworks-quarkus-testing-integration-tests`, or `@522-frameworks-micronaut-testing-integration-tests`. For **OpenAPI contract design**, use `@701-technologies-openapi`.

Do not replace those skills; **702** focuses on portable WireMock behavior and stub quality.

## Constraints

Before recommending structural or build changes, ensure the workspace builds. Compilation failure is a blocking condition for Java-side edits.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before changing Java or build descriptors alongside stubbing strategy
- **SCOPE**: Stay within WireMock usage patterns and HTTP stub design; defer framework test bootstrap to 132/322/422/522
- **MANDATORY**: After editing generator XML, run `./mvnw clean install -pl skills-generator` and `./mvnw clean verify` as appropriate

## Examples

### Table of contents

- Example 1: Per-test stubs and reset
- Example 2: Precise matching and verify

### Example 1: Per-test stubs and reset

Title: avoid leaked state across examples
Description: Register stubs in the narrowest scope (per test method or a fresh rule set) or reset WireMock between tests. Leaked stubs cause **order-dependent** failures and masked assertions.

**Good example:**

```text
                // Conceptual: register stubs inside each test OR reset before each test
                // - Before each test: reset all stubs and request journal
                // - Register only what this scenario needs

```

**Bad example:**

```text
                // Anti-pattern: @BeforeAll registers many stubs shared by all tests
                // without reset — later tests depend on accidental overlap

```

### Example 2: Precise matching and verify

Title: catch missing or wrong outbound calls
Description: Pair **specific** `urlPath`, HTTP method, and required headers with **verify** (or journal checks) so the test proves the collaboration you care about. Overly broad `urlMatching` can let tests pass when the client never called the dependency correctly.

**Good example:**

```json
{
  "request": {
    "method": "GET",
    "urlPath": "/api/v1/orders/550e8400-e29b-41d4-a716-446655440000",
    "headers": { "Accept": { "equalTo": "application/json" } }
  },
  "response": {
    "status": 200,
    "headers": { "Content-Type": "application/json" },
    "jsonBody": { "id": "550e8400-e29b-41d4-a716-446655440000", "status": "OPEN" }
  }
}
```

**Bad example:**

```json
{
  "request": {
    "method": "GET",
    "urlPattern": ".*"
  },
  "response": { "status": 200, "body": "{}" }
}
```

## Output Format

- **REVIEW** stub definitions for isolation, specificity, and maintainability
- **LIST** risks: flaky ordering, accidental broad match, missing verification, hardcoded ports
- **PROPOSE** concrete mapping snippets or DSL patterns that fix the issue
- **POINT** to the right integration-test skill when the user needs framework wiring or base test classes
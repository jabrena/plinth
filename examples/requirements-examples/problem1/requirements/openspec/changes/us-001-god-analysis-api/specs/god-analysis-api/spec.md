# Specification delta: `god-analysis-api` (US-001)

**Related:** [ADR-001](../../../../../adr/ADR-001-God-Analysis-API-Functional-Requirements.md), [ADR-002](../../../../../adr/ADR-002-God-Analysis-API-Non-Functional-Requirements.md), [ADR-003](../../../../../adr/ADR-003-God-Analysis-API-Technology-Stack.md), [US-001-god-analysis-api.openapi.yaml](../../../../../agile/US-001-god-analysis-api.openapi.yaml), [US-001_god_analysis_api.feature](../../../../../agile/US-001_god_analysis_api.feature)

---

## ADDED Requirements

### Requirement: HTTP GET gods stats sum

The system MUST expose `GET /api/v1/gods/stats/sum` with required query parameters `filter` (exactly one Unicode code point) and `sources` (comma-separated pantheon keys among `greek`, `roman`, `nordic`). The response MUST be `200` with `{ "sum": "<string>" }` where `sum` matches `^[0-9]+$`.

#### Scenario: Happy path matches pinned acceptance sum

- **WHEN** `filter=n`, `sources=greek,roman,nordic`, and all upstream WireMock fixtures return successfully
- **THEN** the response is 200 and `sum` is `78179288397447443426`

#### Scenario: Invalid sources rejected

- **WHEN** `sources` contains unknown names or is empty
- **THEN** the response status is 400

### Requirement: Unicode decimal aggregation

The system MUST compute each included name’s value by iterating Unicode code points, concatenating decimal digit strings per code point, parsing to `BigInteger`, and summing; the JSON `sum` MUST be `BigInteger.toString()` of the total.

#### Scenario: Zero sum when filter matches no names

- **WHEN** no name’s first code point equals `filter` (e.g. `filter=N` for lowercase-only data)
- **THEN** `sum` is `0`

### Requirement: External source integration

The system MUST consume JSON arrays of strings from configurable Greek, Roman, and Nordic HTTP endpoints (default URLs per ADR-001) using Spring `RestClient`. Caching of upstream responses MUST NOT be used in v1.

#### Scenario: Deserialize array of god names

- **WHEN** a source returns HTTP 200 with a JSON array of strings
- **THEN** those strings participate in filtering and aggregation

### Requirement: Parallel fetch with single attempt

For each request, the system MUST issue exactly one HTTP GET per selected source, MUST use `RestClient` connect and read timeouts from `application.yml` (overridable via environment variables), and MUST run fetches in parallel without Resilience4j or Spring Retry.

#### Scenario: No second attempt after timeout

- **WHEN** a source does not complete within the read timeout
- **THEN** that source contributes no names and no retry is performed for that source in the same request

### Requirement: Partial results and graceful degradation

If a source times out or errors, the system MUST treat that source as empty for aggregation, MUST NOT fail the whole request solely for that reason, and MUST return 200 with `{ "sum": "…" }` consistent with remaining data.

#### Scenario: Nordic and Roman delayed beyond timeout

- **WHEN** Nordic and Roman are configured to respond after the read timeout and Greek succeeds
- **THEN** the response is 200 and `sum` is `78101109179220212216` per the feature file

### Requirement: Validation and error responses

The system MUST return 400 when `filter` is missing, empty, or longer than one character, or when `sources` is missing, empty, or invalid, matching the `@error-handling` scenarios in the feature file.

#### Scenario: Missing filter

- **WHEN** only `sources` is provided
- **THEN** the response status is 400

### Requirement: Acceptance scenarios coverage

Automated tests MUST implement assertions equivalent to the Gherkin scenarios for happy path, partial timeout, and validation errors, using Spring `RestClient` against `@SpringBootTest(RANDOM_PORT)` and WireMock with per-test stub reset as in ADR-003.

#### Scenario: Integration test isolation

- **WHEN** integration tests run in any order
- **THEN** WireMock stubs are reset between tests so timeout behavior is deterministic

### Requirement: Technology constraints

The implementation MUST use Spring Boot MVC (servlet) without WebFlux, MUST use `RestClient` for outbound calls, MUST use base package `info.jab.ms`, and MUST use Spring `RestClient` (not Rest Assured) for HTTP-level acceptance tests.

#### Scenario: Servlet stack only

- **WHEN** the application starts
- **THEN** no `spring-webflux` or `WebClient` beans are required for US-001

### Requirement: Observability

The system MUST expose Spring Boot Actuator endpoints and MUST log structured messages suitable for diagnosing per-source success, timeout, or failure per request.

#### Scenario: Health endpoint available

- **WHEN** a client requests an actuator health endpoint
- **THEN** the application reports health status for operations use

---

## MODIFIED Requirements

_None — greenfield capability._

## REMOVED Requirements

_None._

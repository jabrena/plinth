# Change: US-001 God Analysis API

## Metadata

| Field | Value |
|-------|--------|
| **ID** | `us-001-god-analysis-api` |
| **User story** | [US-001_God_Analysis_API.md](../../../agile/US-001_God_Analysis_API.md) |
| **Plan** | [US-001-plan-analysis.plan.md](../../../agile/US-001-plan-analysis.plan.md) |
| **Status** | Proposed |

## Why

API consumers need aggregated statistical data about god names from multiple mythological sources (Greek, Roman, Nordic) with resilient HTTP fetching, Unicode-based aggregation, and a stable JSON contract.

## What changes

Introduce a **Spring Boot** REST service that:

- Exposes `GET /api/v1/gods/stats/sum` with query parameters `filter` (single character) and `sources` (comma-separated pantheon keys).
- Fetches configured upstream JSON god lists **in parallel** with **RestClient** connect/read timeouts from `application.yml` (overridable via environment variables).
- Applies **case-sensitive** first-code-point filtering and the **Unicode decimal concatenation → BigInteger sum** algorithm.
- Returns **partial results** when one or more sources time out or error on the **single attempt** per source (**no** automatic retries; **no** circuit breaker in v1 per ADR-002/ADR-003).

## Primary contracts

| Contract | Path |
|----------|------|
| Gherkin | [US-001_god_analysis_api.feature](../../../agile/US-001_god_analysis_api.feature) |
| OpenAPI 3 | [US-001-god-analysis-api.openapi.yaml](../../../agile/US-001-god-analysis-api.openapi.yaml) |

## Architecture decision records (baseline)

| ADR | Topic |
|-----|--------|
| [ADR-001](../../../adr/ADR-001-God-Analysis-API-Functional-Requirements.md) | Functional requirements, endpoint, resilience at API level |
| [ADR-002](../../../adr/ADR-002-God-Analysis-API-Non-Functional-Requirements.md) | Reliability: timeouts, parallel execution, partial results; retries/circuit breaker scope |
| [ADR-003](../../../adr/ADR-003-God-Analysis-API-Technology-Stack.md) | Spring Boot MVC, RestClient, WireMock, Spring RestClient for AT, package `info.jab.ms` |

## Out of scope (v1)

- Outbound automatic retries (Resilience4j Retry, Spring Retry, ad-hoc retry loops).
- Circuit breaker pattern.
- Rest Assured for HTTP-level acceptance tests (use Spring `RestClient` per ADR-003).
- Reactive stack (WebFlux / WebClient).

## Success criteria

- `./mvnw clean verify` passes for the new `god-analysis-api` module.
- Behavior matches the three **acceptance** scenarios in the feature file (happy path sum, partial result with timeouts, validation/error scenarios as tagged).
- OpenAPI-aligned JSON: `200` + `{ "sum": "<decimal-string>" }` for successful aggregation; `400` for malformed requests per OpenAPI and Gherkin `@error-handling` scenarios.

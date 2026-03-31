# god-analysis-api

## Purpose

Provide a Spring Boot REST API that aggregates Unicode-derived numeric sums from god names across configurable Greek, Roman, and Nordic HTTP sources, with parallel RestClient fetches, connect/read timeouts, single-attempt outbound calls (no automatic retries), and partial results when sources fail or time out. This capability supports US-001 and the documented OpenAPI and Gherkin contracts.

## Requirements

### Requirement: Gods stats sum HTTP contract

The service SHALL expose `GET /api/v1/gods/stats/sum` with required query parameters `filter` (exactly one Unicode code point) and `sources` (comma-separated `greek`, `roman`, `nordic`), and SHALL return JSON `{ "sum": "<decimal-string>" }` on success using a string to preserve large integers.

#### Scenario: Successful aggregation returns 200 with sum

- **WHEN** a client sends a valid GET with `filter` and `sources` and upstream responses succeed within timeouts
- **THEN** the response status is 200 and the body contains `sum` as a non-negative decimal string per the OpenAPI schema

#### Scenario: Malformed query returns 400

- **WHEN** `filter` is missing, empty, multi-character, or `sources` is missing, empty, or contains unknown keys
- **THEN** the response status is 400 and the body includes an error message

### Requirement: Unicode aggregation and filtering

The service SHALL include only names whose first Unicode code point matches `filter` (case-sensitive), SHALL compute each name’s contribution by concatenating each code point’s decimal digits and parsing to `BigInteger`, and SHALL sum all contributions and serialize the total with `toString()`.

#### Scenario: Filter matches first code point only

- **WHEN** `filter=n` and names include both matching and non-matching first characters
- **THEN** only names starting with the lowercase `n` code point contribute to `sum`

### Requirement: Parallel fetch and partial results

The service SHALL fetch each selected source in parallel with Spring `RestClient` using configured connect and read timeouts, SHALL perform exactly one GET attempt per source per request (no automatic retries), and SHALL omit failed or timed-out sources from aggregation while still returning 200 with a coherent `sum` when the contract allows.

#### Scenario: Partial data when a source exceeds read timeout

- **WHEN** one or more sources are stubbed with delay beyond the configured read timeout
- **THEN** those sources contribute no names and `sum` reflects only successful sources

## References

See requirement documents under `agile/`: `US-001_God_Analysis_API.md`, `US-001_god_analysis_api.feature`, `US-001-god-analysis-api.openapi.yaml`, plus ADR-001–ADR-003 under `adr/`.

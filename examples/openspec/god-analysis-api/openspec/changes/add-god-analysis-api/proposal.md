## Why

API consumers and data analysts need a REST endpoint that can aggregate mythology
data across Greek, Roman, and Nordic sources for research, reporting, and
educational use cases.

The source requirements define one focused capability: fetch selected god-name
sources, filter names by a case-sensitive prefix, transform matching names into
Unicode-derived decimal values, and return the aggregate sum.

## What Changes

- Add `GET /api/v1/gods/stats/sum`.
- Require `filter` and `sources` query parameters.
- Fetch Greek, Roman, and Nordic source APIs selected by `sources`.
- Filter names whose first Unicode code point matches `filter` exactly.
- Convert each included name by concatenating decimal Unicode code point values,
  parse that concatenated value as a number, and sum all included names.
- Return the aggregate as JSON `{ "sum": "<decimal-string>" }`.
- Configure outbound Spring `RestClient` connect and read timeouts.
- Continue with partial aggregation when an individual source times out or fails
  on its single outbound attempt.
- Reject malformed query parameters with HTTP 400.

## Capabilities

### New Capabilities

- `god-analysis-api`: Adds the God Analysis API endpoint, aggregation algorithm,
  source integration, timeout behavior, and acceptance coverage.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: `examples/requirements/problem1/US-001_God_Analysis_API.md`.
- Acceptance source: `examples/requirements/problem1/US-001_god_analysis_api.feature`.
- HTTP contract source: `examples/requirements/problem1/US-001-god-analysis-api.openapi.yaml`.
- Functional decision source: `examples/requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md`.
- Non-functional decision source: `examples/requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md`.
- Technology decision source: `examples/requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md`.
- Derivation direction: source requirements and decisions -> OpenSpec change
  artifacts -> implementation and tests.

## Change Boundary Assessment

This is one OpenSpec change because the source artifacts describe one
independently reviewable API capability with one endpoint and one acceptance
boundary. The HTTP contract, aggregation behavior, resilience behavior, and
testing constraints are part of the same deployable outcome.

## Impact

An implementation would affect the application REST controller, domain service,
outbound source client, configuration, error handling, OpenAPI documentation,
and unit, integration, and acceptance tests. The original source requirements in
`examples/requirements/problem1` are not modified by this change.


# god-analysis-api

## ADDED Requirements

### Requirement: A3 outbound integration test-first delivery
The `A3` delivery slice SHALL define failing WireMock integration tests first for full-source success and partial-timeout scenarios before outbound integration is finalized.

#### Scenario: Integration tests define outbound expectations
- **Given** WireMock stubs for success and delayed responses
- **When** outbound integration is implemented
- **Then** integration tests pass for both full and partial-source outcomes

### Requirement: A3 timeout-tolerant outbound behavior
The `A3` delivery slice SHALL implement configurable outbound calls (`god.outbound.*`) with per-source timeout tolerance so failed or timed-out sources are omitted while valid sources continue to contribute.

#### Scenario: Timed-out source is omitted from aggregation
- **Given** one selected source exceeds configured timeout
- **When** the endpoint is called with multiple sources
- **Then** the response remains `200` and `sum` reflects only successful sources

### Requirement: A3 verification gate
The `A3` delivery slice SHALL conclude with final verification using `./mvnw clean test` and SHALL preserve the servlet-only technology constraint.

#### Scenario: Milestone A3 verification succeeds
- **Given** all A3 tasks are complete
- **When** final verification is executed
- **Then** the full test suite is green and no reactive dependencies are required

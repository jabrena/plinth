# god-analysis-api

## ADDED Requirements

### Requirement: A2 service and algorithm test-first delivery
The `A2` delivery slice SHALL introduce failing unit tests first for Unicode conversion, source parsing, and case-sensitive first-character filtering before service implementation is finalized.

#### Scenario: Unit tests define service correctness boundaries
- **Given** failing tests for conversion, parsing, and filter behavior
- **When** domain service logic is implemented
- **Then** tests pass with behavior matching the plan and existing contract rules

### Requirement: A2 deterministic parallel aggregation
The `A2` delivery slice SHALL implement aggregation using immutable flow, `CompletableFuture` parallel execution, and `BigInteger` summation with deterministic merge behavior.

#### Scenario: Parallel execution yields deterministic sum
- **Given** selected sources return deterministic datasets
- **When** aggregation executes in parallel
- **Then** the resulting `sum` is stable across repeated executions

### Requirement: A2 verification gate
The `A2` delivery slice SHALL conclude with milestone verification using `./mvnw -q test`, preserving previously green API-boundary tests from `A1`.

#### Scenario: Milestone A2 verification succeeds
- **Given** all A2 tasks are complete
- **When** milestone verification is executed
- **Then** unit tests and prior API tests are green and the service slice is considered complete

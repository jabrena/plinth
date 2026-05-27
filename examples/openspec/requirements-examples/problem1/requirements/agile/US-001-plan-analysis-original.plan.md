# US-001 Implementation Plan

## Objective
Implement the `GET /api/v1/gods/stats/sum` endpoint in the module at [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation), matching behavior defined in:
- [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/agile/US-001_god_analysis_api.feature`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/agile/US-001_god_analysis_api.feature)
- [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/agile/US-001-god-analysis-api.openapi.yaml`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/agile/US-001-god-analysis-api.openapi.yaml)
- ADR set in [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/adr`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/requirements/adr)

## Current Baseline
- `implementation` currently contains only:
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/java/info/jab/ms/Application.java`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/java/info/jab/ms/Application.java)
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/resources/application.yml`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/resources/application.yml)
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/pom.xml`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/pom.xml)
- Most previously implemented code and tests were deleted and must be reintroduced from requirements.

## Implementation Scope

### 1) Domain and API contract layer
- Add request/response and enum models:
  - `PantheonSource` (`greek`, `roman`, `nordic`)
  - `GodStatsResponse` with `sum` as decimal string
  - `ErrorResponse` with stable `code` and `message`
- Add strict query validation at controller boundary:
  - `filter` required, exact length 1
  - `sources` required, non-empty, only allowed values
- Add endpoint: `GET /api/v1/gods/stats/sum`.

### 2) Outbound integration and configuration
- Add outbound configuration binding for URLs and timeouts from `application.yml` (`god.outbound.*`).
- Configure Spring `RestClient` with explicit connect/read timeouts.
- Implement one outbound client method per source (single attempt, no retries).

### 3) Core business logic
- Parse/normalize selected sources from query input.
- Fetch selected sources in parallel (`CompletableFuture` per ADR direction).
- On timeout/transport failure, treat source as absent and continue.
- Filter names by first-character exact match (case-sensitive).
- Convert each filtered name using Unicode-int concatenation rule.
- Aggregate with `BigInteger` and return `sum` as string.

### 4) Error mapping and resilience behavior
- Add custom bad-request exceptions for invalid query conditions.
- Add global exception handler mapping:
  - validation and domain input errors -> HTTP 400
  - unexpected runtime errors -> HTTP 500 with stable envelope
- Preserve successful HTTP 200 on partial-results timeout scenarios.

### 5) Testing strategy (must match feature + ADR)
- Unit tests:
  - Unicode conversion algorithm correctness
  - filter behavior (case sensitivity)
  - source parsing validation
- Controller validation tests:
  - missing/empty/invalid `filter`
  - missing/empty/invalid `sources`
- Integration tests with WireMock:
  - happy path all sources
  - multiple source timeout with partial aggregation
  - per-test stub reset for isolation
- HTTP acceptance tests (`@SpringBootTest(RANDOM_PORT)` + Spring `RestClient`):
  - validate feature-level scenarios and expected `sum` outputs.

### 6) Build and verification
- Ensure `pom.xml` dependencies are sufficient for:
  - Spring MVC runtime
  - Spring Boot test stack
  - WireMock tests
- Run module verification in `implementation`:
  - `./mvnw clean test` (module-level)
- Confirm all feature-driven scenarios are represented by tests.

## File Plan (Expected)
- Main code under:
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/java/info/jab/ms`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/main/java/info/jab/ms)
- Tests under:
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/java/info/jab/ms`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/java/info/jab/ms)
- Test fixtures for WireMock:
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/resources/mappings`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/resources/mappings)
  - [`/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/resources/__files`](/Users/jabrena/IdeaProjects/java-cursor-rules/examples/requirements-examples/problem1/implementation/src/test/resources/__files)

## Execution Order
1. Recreate domain + controller + validation + error handling.
2. Recreate outbound client/config and parallel aggregation service.
3. Recreate unit + controller tests.
4. Add WireMock integration and acceptance tests.
5. Run tests and adjust contract mismatches against OpenAPI/feature.

## Notes
- No retries will be introduced (explicit ADR scope).
- Response uses stringified numeric sum to avoid precision issues.
- Plan replaces the deleted agile plan artifact style (`US-001-plan-analysis.plan.md`).

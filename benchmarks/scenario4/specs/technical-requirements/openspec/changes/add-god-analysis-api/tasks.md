# Tasks

Implementation follows **two-step method** scaffolding then **vertical slices S1–S6** from `design.md`. Prefer TDD order within each slice.

## 1. Implementation Checklist

### Step 1 — Three-layer scaffolding (make the change easy)

- [ ] 1.1 Create the Spring Boot 4.1.0 application module under `benchmarks/scenario4/demo/` with packages `info.jab.ms.controller`, `info.jab.ms.services`, and `info.jab.ms.domain` plus `info.jab.ms.GodAnalysisApplication` (design D4).
- [ ] 1.2 Configure Maven **`--enable-preview`** on compiler, Surefire, and Spring Boot run goals for **`StructuredTaskScope`** (Java 25).
- [ ] 1.3 Add `GodAnalysisProperties` in **services** for Greek, Roman, and Nordic source URLs plus outbound connect and read timeouts (defaults in `application.yml`).
- [ ] 1.4 Register a `RestClient` bean in **services** (`RestClientConfig`) with `JdkClientHttpRequestFactory` connect/read timeouts from properties.
- [ ] 1.5 Introduce `GodSourceClient` in **services** and `PantheonSource` in **domain** with parsing for `greek`, `roman`, and `nordic` (reject unknown keys; dedupe duplicate keys per design D2).
- [ ] 1.6 Implement **domain** types: `GodNameFilter`, `UnicodeNameConverter`, `GodStatsAggregator` (no HTTP or Spring dependencies).
- [ ] 1.7 Add **domain** unit tests for conversion (`Zeus` → `90101117115`), case-sensitive filter, empty-input sum `0`, and invalid source parsing.

### Step 2 — Fill layers (make the easy change)

- [ ] 1.8 Implement **services** adapter `RestGodSourceClient` — deserialize `String[]` from each source URL; on timeout/failure return empty list and log outcome.
- [ ] 1.9 Implement **services** `GodStatsService` — parallel fetch via `StructuredTaskScope` (Java 25 preview, `--enable-preview`); merge names; delegate to domain aggregator.
- [ ] 1.10 Expose **controller** `GET /api/v1/gods/stats/sum` in `GodStatsController` returning JSON `{ "sum": "<decimal-string>" }`.
- [ ] 1.11 Add **controller** validation and `@ControllerAdvice` error handling — HTTP 400 with Spring **`ProblemDetail`** (`detail` field) for all Gherkin `@error-handling` scenarios (design D1).
- [ ] 1.12 Add structured logs for per-source success, timeout, and failure decisions.
- [ ] 1.13 Add OpenAPI documentation matching `US-001-god-analysis-api.openapi.yaml`.

### Testing slices

- [ ] 1.14 Add MockMvc / `@WebMvcTest` tests for all query-parameter validation scenarios.
- [ ] 1.15 Add WireMock test support — dynamic ports, `@BeforeEach` stub reset, fixtures under `src/test/resources/__files/`.
- [ ] 1.16 Add WireMock-backed acceptance test (`@Tag("acceptance-test")`) — `filter=N`, all sources, expected sum `78179288397447443426` using Spring `RestClient` against `RANDOM_PORT`.
- [ ] 1.17 Add WireMock-backed integration test (`@Tag("integration-test")`) — Roman+Nordic delayed beyond read timeout, expected partial sum `78101109179220212216`.
- [ ] 1.18 Add WireMock-backed integration test (`@Tag("integration-test")`) — all three sources delayed beyond read timeout, expected sum `0` (slice S5b).
- [ ] 1.19 Verify WireMock stubs reset between tests and timeout scenarios do not depend on execution order.

### Guardrails and verification

- [ ] 1.20 Verify no WebFlux, `WebClient`, Rest Assured, Resilience4j Retry, Spring Retry, or custom retry loop is introduced for US-001.
- [ ] 1.21 Run `./mvnw clean verify` from the demo module.
- [ ] 1.22 Run `openspec validate --all` from `benchmarks/scenario4/specs/technical-requirements`.

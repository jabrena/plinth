# Tasks

Implementation follows **two-step method** scaffolding then **vertical slices S1–S6** from `design.md`. Prefer TDD order within each slice.

## 1. Implementation Checklist

### Step 1 — Scaffolding and domain seams (make the change easy)

- [ ] 1.1 Create the Spring Boot application module under `benchmarks/scenario4/demo/` using Spring MVC and base package `info.jab.ms`.
- [ ] 1.2 Add `@ConfigurationProperties` for Greek, Roman, and Nordic source URLs plus outbound connect and read timeouts (defaults in `application.yml`).
- [ ] 1.3 Register a `RestClient` bean with `JdkClientHttpRequestFactory` connect/read timeouts from properties.
- [ ] 1.4 Introduce `GodSourceClient` interface and `PantheonSource` enum with parsing for `greek`, `roman`, and `nordic` (reject unknown keys).
- [ ] 1.5 Implement pure domain types: `GodNameFilter`, `UnicodeNameConverter`, `GodStatsAggregator` (no HTTP dependencies).
- [ ] 1.6 Add unit tests for conversion (`Zeus` → `90101117115`), case-sensitive filter, empty-input sum `0`, and invalid source parsing.

### Step 2 — Behavior slices (make the easy change)

- [ ] 1.7 Implement `RestGodSourceClient` — deserialize `String[]` from each source URL; on timeout/failure return empty list and log outcome.
- [ ] 1.8 Implement `GodStatsService` — parallel fetch via `CompletableFuture` on virtual-thread executor; merge names; delegate to aggregator.
- [ ] 1.9 Expose `GET /api/v1/gods/stats/sum` in `GodStatsController` returning JSON `{ "sum": "<decimal-string>" }`.
- [ ] 1.10 Add validation and `@ControllerAdvice` error handling — HTTP 400 with error message for all Gherkin `@error-handling` scenarios.
- [ ] 1.11 Add structured logs for per-source success, timeout, and failure decisions.
- [ ] 1.12 Add OpenAPI documentation matching `US-001-god-analysis-api.openapi.yaml`.

### Testing slices

- [ ] 1.13 Add MockMvc / `@WebMvcTest` tests for all query-parameter validation scenarios.
- [ ] 1.14 Add WireMock test support — dynamic ports, `@BeforeEach` stub reset, fixtures under `src/test/resources/__files/`.
- [ ] 1.15 Add WireMock-backed acceptance test (`@Tag("acceptance-test")`) — `filter=N`, all sources, expected sum `78179288397447443426` using Spring `RestClient` against `RANDOM_PORT`.
- [ ] 1.16 Add WireMock-backed integration test (`@Tag("integration-test")`) — Roman+Nordic delayed beyond read timeout, expected partial sum `78101109179220212216`.
- [ ] 1.17 Verify WireMock stubs reset between tests and timeout scenarios do not depend on execution order.

### Guardrails and verification

- [ ] 1.18 Verify no WebFlux, `WebClient`, Rest Assured, Resilience4j Retry, Spring Retry, or custom retry loop is introduced for US-001.
- [ ] 1.19 Run `./mvnw clean verify` from the demo module.
- [ ] 1.20 Run `openspec validate --all` from `benchmarks/scenario4/specs/technical-requirements`.

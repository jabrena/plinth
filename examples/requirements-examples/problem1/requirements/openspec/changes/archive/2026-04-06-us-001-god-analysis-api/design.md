# Design: US-001 God Analysis API

This design consolidates [ADR-001](../../../adr/ADR-001-God-Analysis-API-Functional-Requirements.md), [ADR-002](../../../adr/ADR-002-God-Analysis-API-Non-Functional-Requirements.md), [ADR-003](../../../adr/ADR-003-God-Analysis-API-Technology-Stack.md), and the [implementation plan](../../../agile/US-001-plan-analysis.plan.md).

## Architecture

- **Style:** Monolithic Spring MVC **servlet** application (no WebFlux).
- **Base path:** `/api/v1` (class-level `@RequestMapping`).
- **Endpoint:** `GET /gods/stats/sum` → full path `/api/v1/gods/stats/sum`.
- **Package root:** `info.jab.ms` with subpackages `controller`, `service`, `dto`, `client`, `config`, `algorithm`.

## Processing pipeline

1. Parse and validate `filter` (exactly one Unicode code point) and `sources` (non-empty, known pantheon keys only) → **400** on violation (see Gherkin `@error-handling` and OpenAPI).
2. For each selected source, run **one** GET to the configured base URL + route, using **RestClient** with **connect + read** timeouts.
3. Execute source fetches **in parallel** (e.g. `CompletableFuture` on a **virtual thread** executor per ADR-003).
4. On timeout or transport/application error for a source, treat that source’s name list as **empty** for aggregation (partial result). **Do not retry** outbound calls.
5. Merge name lists from successful responses; deserialize JSON arrays of strings.
6. **Filter** names where the **first Unicode code point** equals the `filter` character (**case-sensitive**).
7. For each included name, compute per-name decimal string by concatenating each code point’s decimal digits (`String.codePoints()`); parse to `BigInteger` and sum.
8. Respond **200** with `{ "sum": "<BigInteger.toString()>" }`. If no names match, `sum` is `"0"`.

## HTTP client and configuration

- **RestClient** only for outbound calls; timeouts from `application.yml` (e.g. 5000 ms connect/read defaults), overridable via environment variables.
- **@ConfigurationProperties** for base URLs (`greek`, `roman`, `nordic`) matching ADR-001 defaults.
- **No** Resilience4j retry, **no** Spring Retry for US-001.

## REST layer

- `@RestController`, Jackson for JSON.
- Response DTO exposes `sum` as **string** (large integers).
- Optional: `@ControllerAdvice` for 400 responses aligned with OpenAPI.
- Optional: `springdoc-openapi`; static contract remains [US-001-god-analysis-api.openapi.yaml](../../../agile/US-001-god-analysis-api.openapi.yaml).

## Testing (summary)

| Layer | Approach |
|-------|----------|
| Acceptance / integration | `@SpringBootTest(webEnvironment = RANDOM_PORT)` + Spring **`RestClient`** to `localhost` (not Rest Assured). |
| Upstream simulation | **WireMock** with JSON fixtures; **reset stubs** between tests; **fixedDelayMilliseconds** beyond read timeout for timeout scenarios. |
| Unit | Pure algorithm/filter tests without WireMock. |

**Tags:** Mirror Gherkin tags with JUnit `@Tag` (e.g. `acceptance-test`, `integration-test`) if using Surefire groups.

**Pinned data:** Use WireMock fixtures for deterministic sums (e.g. happy path `78179288397447443426`; partial scenario per [feature file](../../../agile/US-001_god_analysis_api.feature)).

## Runtime versions

- **Spring Boot:** 4.0.x (per ADR-003 / plan).
- **Java:** Align with repo standard (**25** recommended in plan) unless explicitly standardizing on 26.

## References

- [US-001-plan-analysis.plan.md](../../../agile/US-001-plan-analysis.plan.md) — task order, file checklist, execution rules.
- OpenAPI: [US-001-god-analysis-api.openapi.yaml](../../../agile/US-001-god-analysis-api.openapi.yaml).

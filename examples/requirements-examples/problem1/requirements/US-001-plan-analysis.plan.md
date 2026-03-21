---
name: God Analysis API Implementation
overview: "Spring Boot REST API with parallel HTTP fetches: implement 5s timeouts and partial results first, then add linear retries via Resilience4j Retry (ADR-002 + ADR-003); no circuit breaker in v1; Unicode aggregation via London Style TDD."
todos: []
isProject: false
---

# Problem 1: God Analysis API Implementation Plan

## Requirements Summary

**User Story:** As an API consumer, I want to retrieve aggregated statistical data about god names from multiple mythological sources with resilient HTTP fetching and Unicode-based calculations.

**Key Business Rules:**
- **Unicode Aggregation:** Convert each character to Unicode code point decimal, concatenate per name, sum as BigInteger
- **Case-Sensitive Filtering:** Filter names by first Unicode code point matching the filter parameter
- **Resilient HTTP (sequenced):** (1) **Timeout first:** 5-second timeout per source, single attempt per call, partial results when a source times out or errors. (2) **Retries second:** up to **3** additional linear attempts per source (ADR-002), still isolated per source; only after timeout behavior is stable.
- **Parallel Processing:** Fetch from multiple sources concurrently (Greek, Roman, Nordic)
- **Expected Result:** JSON response `{"sum": "<decimal-string>"}` for successful aggregation

## Approach

**Strategy:** London Style Outside-In TDD - Start with acceptance tests, work inward through REST controller, service layer, and HTTP client components.

```mermaid
flowchart LR
  subgraph Sources [External HTTP Sources]
    G[Greek API]
    R[Roman API]
    N[Nordic API]
  end

  subgraph Application [God Analysis API]
    Controller[REST Controller<br/>GET /api/v1/gods/stats/sum]
    Service[God Analysis Service<br/>Orchestration & Aggregation]
    Client[HTTP Client<br/>Timeout then Retries]
    Algorithm[Unicode Algorithm<br/>Filter & Sum]
  end

  Sources --> Client
  Client --> Service
  Service --> Algorithm
  Algorithm --> Service
  Service --> Controller
  Controller --> Response[JSON Response<br/>sum: string]
```

## Task List

| # | Task | Phase | TDD | Milestone | Parallel | Status |
|---|------|-------|-----|-----------|----------|--------|
| 1 | Create Maven project structure with dependencies | Setup | | | A1 | |
| 2 | Write acceptance test for happy path sum calculation | RED | Test | | A1 | |
| 3 | Create REST controller stub to pass acceptance test | GREEN | Impl | | A1 | |
| 4 | Add structured logging for request/response | Refactor | | | A1 | |
| 5 | Optimize controller validation and error handling | Refactor | | | A1 | |
| 6 | Verify acceptance test passes with controller | Verify | | milestone | A1 | |
| 7 | Write service layer unit test for aggregation logic | RED | Test | | A2 | |
| 8 | Implement service with Unicode algorithm | GREEN | Impl | | A2 | |
| 9 | Add service-level logging | Refactor | | | A2 | |
| 10 | Optimize service configuration and error handling | Refactor | | | A2 | |
| 11 | Verify service unit tests pass | Verify | | milestone | A2 | |
| 12 | Write HTTP client tests for timeout-bound fetching (single attempt per source) | RED | Test | | A3-timeout | |
| 13 | Implement HTTP client with 5s timeout only (no retries yet) | GREEN | Impl | | A3-timeout | |
| 14 | Add client-level logging for HTTP outcomes | Refactor | | | A3-timeout | |
| 15 | Optimize client configuration (timeouts, per-source isolation) | Refactor | | | A3-timeout | |
| 16 | Verify HTTP client tests pass (timeout phase) | Verify | | milestone | A3-timeout | |
| 17 | Write HTTP client tests for linear retry policy (1 + 3 attempts per ADR-002) | RED | Test | | A3-retry | |
| 18 | Add per-source **Resilience4j Retry** around HTTP client calls | GREEN | Impl | | A3-retry | |
| 19 | Extend client logging for retry attempts and final outcomes | Refactor | | | A3-retry | |
| 20 | Optimize retry configuration (linear delays, caps) | Refactor | | | A3-retry | |
| 21 | Verify HTTP client tests pass (with retries) | Verify | | milestone | A3-retry | |
| 22 | Write integration test for Nordic timeout scenario | RED | Test | | A4 | |
| 23 | Implement partial result handling in service | GREEN | Impl | | A4 | |
| 24 | Write integration test for filter=N zero result | RED | Test | | A4 | |
| 25 | Implement filter logic validation | GREEN | Impl | | A4 | |
| 26 | Add end-to-end observability and monitoring | Refactor | | | A4 | |
| 27 | Optimize application configuration and profiles | Refactor | | | A4 | |
| 28 | Verify all integration tests pass | Verify | | milestone | A4 | |

## Execution Instructions

When executing this plan:
1. Complete the current task.
2. **Update the Task List**: set the Status column for that task (e.g., ✔ or Done).
3. **For GREEN tasks**: MUST complete the associated Verify task before proceeding.
4. **For Verify tasks**: MUST ensure all tests pass and build succeeds before proceeding.
5. **Milestone rows** (Milestone column): a milestone is evolving complete software for that slice — complete the pair of Refactor tasks (logging, then optimize config/error handling/log levels) immediately before each milestone Verify.
6. Only then proceed to the next task.
7. Repeat for all tasks. Never advance without updating the plan.

**Critical Stability Rules:**
- After every GREEN implementation task, run the verification step
- All tests must pass before proceeding to the next implementation
- If any test fails during verification, fix the issue before advancing
- Never skip verification steps - they ensure software stability

**Parallel column:** Use grouping identifiers (A1, A2, `A3-timeout`, `A3-retry`, A4, etc.) to group tasks into the same delivery slice. **`A3-timeout` must complete (including its Verify) before starting `A3-retry`.** Use when assigning agents or branches to a milestone scope.

## File Checklist

| Order | File |
|-------|------|
| 1 | `god-analysis-api/pom.xml` |
| 2 | `god-analysis-api/src/main/resources/application.yml` |
| 3 | `god-analysis-api/src/test/java/com/example/gods/GodAnalysisApiAcceptanceTest.java` |
| 4 | `god-analysis-api/src/main/java/com/example/gods/controller/GodStatsController.java` |
| 5 | `god-analysis-api/src/main/java/com/example/gods/dto/GodStatsResponse.java` |
| 6 | `god-analysis-api/src/test/java/com/example/gods/service/GodAnalysisServiceTest.java` |
| 7 | `god-analysis-api/src/main/java/com/example/gods/service/GodAnalysisService.java` |
| 8 | `god-analysis-api/src/main/java/com/example/gods/algorithm/UnicodeAggregator.java` |
| 9 | `god-analysis-api/src/test/java/com/example/gods/client/GodDataClientTest.java` |
| 10 | `god-analysis-api/src/main/java/com/example/gods/client/GodDataClient.java` |
| 11 | `god-analysis-api/src/main/java/com/example/gods/config/HttpClientConfig.java` |
| 12 | `god-analysis-api/src/test/java/com/example/gods/GodAnalysisApiIntegrationTest.java` |
| 13 | `god-analysis-api/src/test/resources/wiremock/greek-gods.json` |
| 14 | `god-analysis-api/src/test/resources/wiremock/roman-gods.json` |
| 15 | `god-analysis-api/src/test/resources/wiremock/nordic-gods.json` |
| 16 | `god-analysis-api/src/test/java/.../support/WireMockToxiproxyExtension.java` (or equivalent) — Testcontainers lifecycle, proxy URLs, **clear toxics between tests** |
| 17 | `god-analysis-api/README.md` |

## Authoritative Sources

**Primary Contracts (Implementation Must Match):**
- [US-001_god_analysis_api.feature](US-001_god_analysis_api.feature) — Three test scenarios defining exact behavior
- [US-001-god-analysis-api.openapi.yaml](US-001-god-analysis-api.openapi.yaml) — API contract with response schema and parameters
- [ADR-001-God-Analysis-API-Functional-Requirements.md](ADR-001-God-Analysis-API-Functional-Requirements.md) — Architecture decisions (monolith, no auth, direct HTTP)
- [ADR-002-God-Analysis-API-Non-Functional-Requirements.md](ADR-002-God-Analysis-API-Non-Functional-Requirements.md) — Performance and resilience requirements (5s timeout, 3 retries, parallel calls; **circuit breaker explicitly out of scope** for initial implementation)
- [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) — Runtime stack, `RestClient`, **Resilience4j Retry** (retry module only), Rest Assured, and **Testcontainers + WireMock + Toxiproxy** for isolated timeout/retry tests

**Test Scenarios:**
1. **Happy Path:** All sources respond → exact sum calculation
2. **Partial Failure:** Nordic times out → sum from Greek + Roman only
3. **Filter Edge Case:** `filter=N` → sum equals `"0"`

**ADR-002 vs API contract:** ADR-002 asks for “clear indication of which sources contributed.” The OpenAPI and Gherkin only require `sum`. **Approach:** satisfy the public contract first; meet the ADR via **structured logging**  listing successful vs failed/timed-out sources per request. If product later wants this in JSON, extend OpenAPI in a follow-up.

## Runtime stack (reconcile ADR-003 with repo)

[ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) selects **Spring Boot 4.0.4** and **Java 26**. The repo root uses **Java 25** ([pom.xml](../../../pom.xml)). **Recommendation:** use **Spring Boot 4.0.4** with **Java 25** unless you explicitly standardize this example on Java 26—Boot 4 supports Java 17+.

Examples are **not** Maven modules of the root reactor; mirror [examples/spring-boot-demo/implementation/pom.xml](../../spring-boot-demo/implementation/pom.xml) as a **standalone** Maven project (new directory under `examples/requirements-examples/problem1/`, e.g. `god-analysis-api/`).

## Domain algorithm (must match acceptance math)

External APIs return **JSON arrays of strings** (array of god names).

1. **Per source:** GET URL, deserialize to `List<String>` (or stream) of names.
2. **Filter:** include names where the **first Unicode code point** equals the single `filter` code point (**case-sensitive**).
3. **Per-name value:** for each code point in the name, append `Integer.toString(codePoint)` as decimal digits, forming one big decimal string; parse to `BigInteger` (not `long`).
4. **Total:** sum all per-name `BigInteger` values; serialize result with `toString()` for JSON `sum`.

Use `String.codePoints()` (or equivalent) so supplementary characters are handled correctly.

```mermaid
flowchart LR
  subgraph parallel [Parallel per selected source]
    G[Greek HTTP]
    R[Roman HTTP]
    N[Nordic HTTP]
  end
  parallel --> merge[Merge name lists]
  merge --> filter[Prefix filter case-sensitive]
  filter --> convert[Concat codepoint decimals per name]
  convert --> sum[BigInteger sum]
  sum --> json["JSON sum string"]
```

## Configuration

- **Single Configuration:** All settings in `application.yml` with production-ready defaults (5s timeout, 4 max attempts, 1s retry delay)
- **Base URLs** for `greek`, `roman`, `nordic` with defaults matching ADR-001 URLs
- **Environment Variables:** Runtime customization without profile complexity
- **Phase 1 — Timeout only:** 5 seconds per HTTP **attempt** (connect + read as appropriate). `max-attempts` / retry settings disabled or set to **1** until the retry phase lands.
- **Phase 2 — Retries:** After timeout-only client is verified, enable up to **3** additional attempts per source after failure/timeout (**linear** spacing per ADR-002; no exponential backoff). Prefer separate config keys (e.g. `retry.max-attempts`, `retry.delay`) so Phase 1 stays simple.
- **Parallelism:** fetch selected sources concurrently. During Phase 1, **wait** until each source returns or times out (single attempt). After Phase 2, **wait** until each source returns data or **exhausts retries** (ADR-002 “completeness priority” before declaring partial aggregate).

Wire Spring configuration via `@ConfigurationProperties` for testability.

## HTTP client and resilience

- **Implementation order:** (1) **Timeouts + partial results** with **one attempt** per source. (2) **Then** add **retries** without changing timeout semantics per attempt. Keeps failing tests readable and matches ADR-001 (timeouts/degradation) before ADR-002 retry policy.
- **Circuit breaker:** Not required for v1 (ADR-002 considered and rejected it until persistent failure patterns justify it). Do not add Resilience4j circuit breaker or equivalent unless requirements change.
- Prefer **RestClient** or **WebClient** (Spring 6 / Boot 4 style) with a **shared** builder factory applying timeouts.
- Implement a small **GodListClient** (or per-source callable) that:
  - **Phase 1:** Single GET per source with 5s timeout; on timeout or error, treat that source as empty for aggregation (partial result path).
  - **Phase 2:** Wrap the same per-source call with **Resilience4j Retry** (see [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md)): **4** total attempts (1 + 3), **fixed** wait between attempts, **one retry configuration per pantheon**—**no** CircuitBreaker in v1.
  - On **final** failure for a source (after Phase 2), return **empty list** (or sentinel) so aggregation still returns **HTTP 200** with partial logical result, per feature and OpenAPI.
- **Do not** fail the whole request if one source fails after retries.

## REST layer

- `@RestController` with class-level `@RequestMapping("/api/v1")` and `@GetMapping("/gods/stats/sum")`.
- Bind `filter` (`String` length 1) and `sources` (`String`); parse `sources` to enum or set (`greek`, `roman`, `nordic`).
- Response DTO: `{ "sum": string }` — Jackson serializes `sum` as string; use `BigInteger` internally, expose string in DTO.
- Optional: `springdoc-openapi` + static copy or generation from existing [US-001-god-analysis-api.openapi.yaml](US-001-god-analysis-api.openapi.yaml).
- Optional: `@ControllerAdvice` for **400** on missing/invalid params (OpenAPI reserves 400; feature does not require it—keep validation minimal if you want zero behavior change vs stubs).

## Testing strategy

**Stack (see [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md)):** **Testcontainers** runs **WireMock** (stub bodies) and **Toxiproxy** (latency / timeout simulation). The app’s configured base URLs target **Toxiproxy listeners**, not WireMock directly. **Reset or remove all toxics** in `@BeforeEach` / `@AfterEach` (or equivalent) so **every** timeout/retry test starts **isolated**—no cross-test leakage when the suite runs in arbitrary order or in parallel (if parallel, prefer per-class containers or documented synchronization).

| Goal | Approach |
|------|----------|
| Happy path / exact `sum` | `@SpringBootTest` + **Rest Assured** against **RANDOM_PORT**; upstreams via Toxiproxy → WireMock with **JSON fixture files** so `sum` equals **`78179288397447443426`**. No live network in CI. |
| Nordic timeout / partial sum | Apply **Toxiproxy latency (or timeout) toxic** on the **Nordic proxy only** so the client hits **5s per-attempt** timeout while Greek/Roman stay fast; assert partial `sum` from Greek + Roman. With retries enabled, tune toxics so only Nordic exhausts attempts within the test. |
| `filter=N` → `"0"` | Same container stack; no Nordic toxic required; assert `sum` is `"0"`. |
| Unit tests | Pure tests for conversion + filter + aggregation with small strings (**no** Docker). |

Tag tests to mirror Gherkin: e.g. JUnit `@Tag("acceptance-test")` and `@Tag("integration-test")` for Maven `groups`/Surefire filters if desired.

**Gherkin execution:** Optional Cucumber step definitions are **not** required if JUnit tests implement the same assertions; only add Cucumber if you need literal `.feature` execution in CI.

## Deliverables checklist

- New Maven project with `spring-boot-starter-web`, `spring-boot-starter-actuator` (ADR-003), **`resilience4j-retry`** plus **Resilience4j Spring Boot integration** (Retry only), `spring-boot-starter-test`, **Rest Assured**, **Testcontainers** (`testcontainers`, `junit-jupiter`, **`toxiproxy`**), and **WireMock on Testcontainers** (or equivalent) per ADR-003.
- **Single configuration file** `application.yml` with production-ready settings and environment variable support
- `README` or `DEVELOPER.md` in the new module: how to run, **Docker requirement**, configure URLs/timeouts via environment variables, run tests.
- `./mvnw clean verify` from the new module passes.

## Notes

- **Exact happy-path sum:** Depends on **current** Typicode data; **pin** test data in WireMock JSON so builds are deterministic.
- **Retry + timeout interaction:** After the retry phase, worst-case latency per source follows ADR-002 (~4 attempts × timeout + linear delays); document this for operators. During **timeout-only** phase, worst case is roughly one timeout per slow source (plus parallel peer waits).
- **Java version:** Pick **25 vs 26** explicitly for this example module’s `pom.xml` `java.version`.

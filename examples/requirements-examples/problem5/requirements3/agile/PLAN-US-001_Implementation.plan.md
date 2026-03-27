---
name: US-001 Greek Gods API (Micronaut)
overview: "Implement Micronaut 4.x GET /api/v1/gods/greek in examples/requirements-examples/problem5/implementation3 — PostgreSQL, Flyway, background sync from my-json-server, RFC 7807 errors, @MicronautTest + REST Assured (London-style outside-in TDD), per requirements3 ADRs."
todos: []
isProject: true
---

# Problem 5: US-001 Greek Gods API Implementation Plan (requirements3)

**Implementation module (exclusive target for code and Maven):** [`examples/requirements-examples/problem5/implementation3`](../../implementation3/)

**Artifacts:** [US-001_API_Greek_Gods_Data_Retrieval.md](US-001_API_Greek_Gods_Data_Retrieval.md) · [US-001_api_greek_gods_data_retrieval.feature](US-001_api_greek_gods_data_retrieval.feature) · **Last updated:** 2026-03-27

> **Plan file location:** This document lives under `requirements3/agile/` for the example. The skill default for Cursor Plan mode is `.cursor/plans/US-001-plan-analysis.plan.md`; keep both in sync if you duplicate.

---

## Requirements Summary

**User Story:** As an API consumer, I want to retrieve the full list of Greek god names via **`GET /api/v1/gods/greek`** so educational apps can integrate curated mythology data with fast, reliable access that does not depend on the external JSON server at read time.

**Key business rules:**

- **Read path:** Responses come **only** from PostgreSQL. **200** + `application/json` array of strings; **empty table → 200** + `[]` (not 5xx).
- **Sync path:** Background job fetches `GET …/greek` (see [my-json-server-oas.yaml](../design/my-json-server-oas.yaml)), parses array of strings, **upserts** into `greek_god` by unique `name`; log failures; **no** retry library in v1 ([ADR-003](../design/ADR-003-Greek-Gods-API-Technology-Stack.md)).
- **Errors:** DB unreachable on read → **500** + **`application/problem+json`** with **`type`**, **`title`**, **`status`** (normative subset per [ADR-001](../design/ADR-001_REST_API_Functional_Requirements.md)).
- **Contract:** Deterministic ordering for tests (e.g. `ORDER BY name`); happy path asserts **20** canonical names with **no duplicates** (set equality, e.g. AssertJ `containsExactlyInAnyOrder`).
- **Stack (requirements3):** **Micronaut 4.x** — `@Controller`, JDBC/Hikari or Micronaut Data JDBC, `@Scheduled`, declarative **`@Client`** or JDK **`HttpClient`** with configurable timeouts; **not** the Quarkus stack unless ADR-003 is amended.
- **Testing:** [ADR-002](../design/ADR-002-Acceptance-Testing-Strategy.md) — **`@MicronautTest`** + **REST Assured**; JUnit **`@Tag`** matches Gherkin tags; PostgreSQL via **Testcontainers** (`TestPropertyProvider` or equivalent).
- **OpenAPI:** Runtime spec aligned with [greekController-oas.yaml](../design/greekController-oas.yaml).
- **Schema:** [schema.sql](../design/schema.sql) — `greek_god (id SERIAL PK, name VARCHAR(100) NOT NULL UNIQUE)`.

**Reference implementation (other stack):** [implementation2](../../implementation2/) (Quarkus) mirrors behavior; use for ideas only. **All US-001 delivery for requirements3 is under** **`examples/requirements-examples/problem5/implementation3`** (Micronaut).

**Non-goals:** Automate **@availability** (24 h / 99.9%) in ITs; change **implementation1** (Spring); add resilience/retry stack for outbound sync in v1.

---

## Approach

**Strategy:** **London Style (outside-in) TDD** — start with a failing HTTP acceptance test (`@MicronautTest` + REST Assured), implement the smallest vertical slice (Flyway + JDBC + controller), then add sync behind the same contract; refactor logging and configuration before each milestone **Verify**.

**Module:** **`examples/requirements-examples/problem5/implementation3`** — Micronaut 4.x. Enable Failsafe / clear `<skipITs>` when `*IT.java` exist. Scaffold this directory and `pom.xml` if it does not exist yet (do not implement US-001 in `implementation1` or `implementation2`).

```mermaid
flowchart LR
  subgraph ext [External]
    JsonServer[my-json-server GET /greek]
  end
  subgraph app [Micronaut app]
    Scheduler[Scheduled sync]
    Client[Outbound HTTP client]
    Repo[JDBC repository]
    Controller[GET /api/v1/gods/greek]
    Handler[ExceptionHandler RFC 7807]
  end
  subgraph db [PostgreSQL]
    Table[greek_god]
  end
  JsonServer --> Client
  Scheduler --> Client
  Scheduler --> Repo
  Repo --> Table
  Controller --> Repo
  Controller -.->|failure| Handler
```

**Detail:** [greek_gods_api_sequence_diagram.puml](../design/greek_gods_api_sequence_diagram.puml)

---

## Task List

| # | Task | Phase | TDD | Milestone | Parallel | Status |
|---|------|-------|-----|-----------|----------|--------|
| 1 | Under **`examples/requirements-examples/problem5/implementation3`**, create Micronaut 4.x Maven project: `micronaut-http-server-netty`, `micronaut-jdbc-hikari`, PostgreSQL driver, `micronaut-flyway`, `micronaut-http-client`, `micronaut-scheduling`, `micronaut-openapi`; tests: `micronaut-test-junit5`, REST Assured, Testcontainers, Failsafe for `*IT`; plan `%test` datasource | Setup | | | A1 | ✔ |
| 2 | **RED:** `GreekGodsApiIT` — REST Assured `GET /api/v1/gods/greek` expects **200**, JSON array, 20 canonical names (set equality), no duplicates (fails until slice exists); `@MicronautTest` + embedded server port | RED | Test | | A1 | ✔ |
| 3 | **GREEN:** Flyway migration matching [schema.sql](../design/schema.sql); seed 20 names (IT setup); JDBC repository `findAllNamesOrdered()` + upsert; `@Controller` GET `/api/v1/gods/greek`; no external call on read path | GREEN | Impl | | A1 | ✔ |
| 4 | **Refactor:** Structured logging (read path): controller/repo boundaries per team standard | Refactor | | | A1 | ✔ |
| 5 | **Refactor:** `ExceptionHandler` (or shared Problem DTO) for persistence failures → 500 `application/problem+json`; empty DB IT → 200 `[]`; invalid JDBC / profile for 500 shape; external base URL property (no hard-coded URL) | Refactor | | | A1 | ✔ |
| 6 | **Verify:** `cd examples/requirements-examples/problem5/implementation3 && ./mvnw clean verify`; fix failures before M2 | Verify | | milestone | A1 | ✔ |
| 7 | **RED:** Failing test for sync: WireMock (or stub) returns JSON array; assert upsert updates DB (`@MicronautTest` + test resource) | RED | Test | | A2 | ✔ |
| 8 | **GREEN:** Implement outbound client for `/greek` (`@Client` or `HttpClient` with timeouts); `@Scheduled` job: fetch, parse, upsert by `name`; failure logging only | GREEN | Impl | | A2 | ✔ |
| 9 | **Refactor:** Logging for sync failures (level, no secrets); correlation-friendly messages | Refactor | | | A2 | ✔ |
| 10 | **Refactor:** Scheduler interval / cron via config; test overrides; tighten error handling at client boundary | Refactor | | | A2 | ✔ |
| 11 | **Verify:** `cd examples/requirements-examples/problem5/implementation3 && ./mvnw clean verify`; sync + data-quality scenarios green | Verify | | milestone | A2 | ✔ |
| 12 | **RED:** Failing IT or assertion for OpenAPI fragment / `@api-specification` (Content-Type, array of strings) | RED | Test | | A3 | ✔ |
| 13 | **GREEN:** Annotate OpenAPI (`micronaut-openapi`) to match [greekController-oas.yaml](../design/greekController-oas.yaml); add `@Tag` ITs: `performance` (&lt;1s), `load-testing` (100 concurrent, wall &lt;2s per feature — document CI tolerance), `data-quality` (post-stub sync) | GREEN | Impl | | A3 | ✔ |
| 14 | **Refactor:** Observability for public API (optional metrics hooks); OpenAPI tags/descriptions | Refactor | | | A3 | ✔ |
| 15 | **Refactor:** Maven profiles / `-Dgroups` alignment with `@Tag`; `skipITs` default vs CI; README for run commands | Refactor | | | A3 | ✔ |
| 16 | **Verify:** Full `cd examples/requirements-examples/problem5/implementation3 && ./mvnw clean verify`; confirm US-001 DoD and Gherkin coverage except **@availability** (ops/SLO) | Verify | | milestone | A3 | ✔ |

---

## Execution Instructions

When executing this plan:

0. **Scope:** Perform every implementation, test, and Maven command only under **`examples/requirements-examples/problem5/implementation3`** (repository root–relative path).
1. Complete the current task.
2. **Update the Task List:** set the **Status** column for that task (e.g. ✔ or Done).
3. **For GREEN tasks:** MUST complete the associated **Verify** task before proceeding to the next milestone slice (after Refactor pair).
4. **For Verify tasks:** MUST ensure all tests pass and the build succeeds before proceeding.
5. **Milestone rows** (**Milestone** column): a milestone is evolving complete software for that slice — complete the **pair of Refactor** tasks (logging, then optimize config/error handling) **immediately before** each **milestone Verify**.
6. Only then proceed to the next task.
7. Repeat for all tasks. Never advance without updating the plan.

**Critical stability rules:**

- After every **GREEN** implementation task, run verification before leaving that milestone (after the two Refactor rows and **Verify**).
- All tests must pass before proceeding; if any test fails, fix before advancing.
- Never skip **Verify** steps.

**Parallel column:** **A1** = read API + DB + error/empty paths; **A2** = outbound sync; **A3** = OpenAPI + extended tags + build/CI polish.

---

## File Checklist

| Order | File |
|-------|------|
| 1 | `examples/requirements-examples/problem5/implementation3/pom.xml` |
| 2 | `examples/requirements-examples/problem5/implementation3/src/main/resources/application.yml` (or `application.properties`) |
| 3 | `examples/requirements-examples/problem5/implementation3/src/main/resources/db/migration/V1__greek_god.sql` (or equivalent Flyway version) |
| 4 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/GreekGodRepository.java` (or package chosen) |
| 5 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/controller/GreekGodsController.java` |
| 6 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/error/GreekGodsDataAccessException.java` (optional split) |
| 7 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/error/GreekGodsExceptionHandler.java` (RFC 7807 / `application/problem+json`) |
| 8 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/client/GreekGodsUpstreamClient.java` (declarative `@Client`) or service using `HttpClient` |
| 9 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/service/GreekGodsSyncJob.java` (or `...Scheduler.java`) |
| 10 | `examples/requirements-examples/problem5/implementation3/src/main/java/.../gods/GreekGodsApplication.java` |
| 11 | `examples/requirements-examples/problem5/implementation3/src/test/java/.../GreekGodsApiIT.java` |
| 12 | `examples/requirements-examples/problem5/implementation3/src/test/java/.../GreekGodsSyncIT.java` (sync + WireMock) |
| 13 | `examples/requirements-examples/problem5/implementation3/src/test/resources/application.yml` (or test properties) |
| 14 | `examples/requirements-examples/problem5/implementation3/README.md` (run, config, tags) |

Adjust package `...` to match the module (e.g. `info.jab.ms.gods`).

---

## Notes

- **Gherkin → `@Tag`:** `smoke`, `happy-path`, `performance`, `load-testing`, `error-handling`, `data-quality`, `api-specification`, `availability` (document only) — see [ADR-002 §2](../design/ADR-002-Acceptance-Testing-Strategy.md).
- **Risks:** Flaky public JSON server in CI → prefer WireMock for sync tests; perf thresholds on shared runners → optional `@EnabledIfEnvironmentVariable` or nightly profile.
- **Commands:** `cd examples/requirements-examples/problem5/implementation3 && ./mvnw clean verify`; enable ITs with `-DskipITs=false` or profile while `skipITs` is true in a skeleton POM.
- **problem5** has no parent aggregator; **`examples/requirements-examples/problem5/implementation3`** is a standalone Maven module (siblings: `implementation1`, `implementation2`).
- **Cucumber** is not required by ADR-002; JUnit + REST Assured mapping from the feature file is sufficient.

### Related documentation

| Artifact | Path |
|----------|------|
| User story | [US-001_API_Greek_Gods_Data_Retrieval.md](US-001_API_Greek_Gods_Data_Retrieval.md) |
| Gherkin | [US-001_api_greek_gods_data_retrieval.feature](US-001_api_greek_gods_data_retrieval.feature) |
| This plan | [PLAN-US-001_Implementation.plan.md](PLAN-US-001_Implementation.plan.md) |
| Implementation (code) | `examples/requirements-examples/problem5/implementation3` ([folder](../../implementation3/)) |
| ADR-001 | [../design/ADR-001_REST_API_Functional_Requirements.md](../design/ADR-001_REST_API_Functional_Requirements.md) |
| ADR-002 | [../design/ADR-002-Acceptance-Testing-Strategy.md](../design/ADR-002-Acceptance-Testing-Strategy.md) |
| ADR-003 | [../design/ADR-003-Greek-Gods-API-Technology-Stack.md](../design/ADR-003-Greek-Gods-API-Technology-Stack.md) |
| Public OpenAPI | [../design/greekController-oas.yaml](../design/greekController-oas.yaml) |
| External OpenAPI | [../design/my-json-server-oas.yaml](../design/my-json-server-oas.yaml) |
| Schema | [../design/schema.sql](../design/schema.sql) |
| Sequence diagram | [../design/greek_gods_api_sequence_diagram.puml](../design/greek_gods_api_sequence_diagram.puml) |

### Tag / scenario matrix (implementation hint)

| Gherkin focus | Automation |
|---------------|------------|
| `@smoke` / `@happy-path` | REST Assured + AssertJ set equality for 20 names |
| `@performance` | Single GET timed &lt; 1 s |
| `@load-testing` | 100 concurrent GETs; wall &lt; 2 s (tune for CI) |
| `@error-handling` | 500 problem+json; empty → `[]` |
| `@data-quality` | After stubbed sync, API matches upstream payload |
| `@api-specification` | Headers + optional schema vs OpenAPI |
| `@availability` | Production SLO / monitoring — not automated here |

### Plan creation checklist (040-planning-plan-mode)

- [x] Frontmatter: `name`, `overview`, `todos`, `isProject`
- [x] Requirements Summary: user story + key business rules
- [x] Approach: London Style TDD + Mermaid diagram
- [x] Task list: columns #, Task, Phase, TDD, Milestone, Parallel, Status
- [x] Milestone + parallel groups (A1, A2, A3); Refactor pair before each Verify
- [x] Execution Instructions with stability rules
- [x] File checklist: Order, File
- [x] Notes: conventions, risks, related docs

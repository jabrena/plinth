# ADR-003: Greek Gods Data Synchronization API — Technology Stack

**Status:** Accepted  
**Date:** 2026-03-26  
**Deciders:** Development Team  
**Traceability:** [EPIC-001](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md), [FEAT-001](../agile/FEAT-001_REST_API_Endpoints_Greek_Gods_Data.md), [US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)

---

## Decision

Adopt a **technology stack** for the **Greek Gods Data Synchronization API Platform** that implements:

- **Runtime:** **Spring Boot 4.0.4** as the documented baseline (see [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md); use **4.0.x** patch versions from the implementation `pom.xml`, and update **both** ADRs when changing minor/major) with **Spring MVC** (servlet stack only—**no** Spring WebFlux / Reactor).
- **Public API:** Single read endpoint **`GET /api/v1/gods/greek`** returning a **JSON array of strings**, as specified in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md) and [US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md).
- **Persistence:** **PostgreSQL** with **Flyway** migrations and relational access via **Spring Data JDBC** (or `JdbcTemplate` / `JdbcClient` where appropriate).
- **Outbound HTTP (sync):** **Spring `RestClient`** with **connect/read timeouts** in configuration, calling the external JSON host (`https://my-json-server.typicode.com/jabrena/latency-problems` and the Greek resource path per [my-json-server-oas.yaml](./my-json-server-oas.yaml)). **No automatic HTTP retry library** for the initial scope—failures are logged and the next scheduled sync attempts again.
- **Scheduling:** **`@Scheduled`** background synchronization from the external source into PostgreSQL.
- **API documentation:** **springdoc-openapi** (or equivalent) aligned with [greekController-oas.yaml](./greekController-oas.yaml) (OpenAPI **3.0.3**).
- **Testing:** **JUnit 5**, **AssertJ**; **HTTP-level** tests use **`RestClient`** or **`TestRestTemplate`** against `@SpringBootTest(webEnvironment = RANDOM_PORT)` per [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md)—**not Rest Assured**. **Testcontainers** for PostgreSQL in integration tests. **WireMock** (optional) to stub the external JSON server when deterministic offline sync tests are required.

This ADR **replaces** any prior copy that described the unrelated **God Analysis API** (`GET /api/v1/gods/stats/sum`, multi-source aggregation, `info.jab.ms`, and companion ADRs such as `ADR-002-God-Analysis-API-Non-Functional-Requirements.md`). Those documents live under other trees (e.g. `examples/requirements-examples/problem1`, `problem2`), **not** beside this `requirements1` folder.

---

## Context

[US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md) requires a **public**, **unauthenticated** API that returns **20 Greek god names** from a **locally synchronized** PostgreSQL database, with **sub-second** reads, **concurrent** use, and **clear error behavior** when the database is unavailable. The platform must **not** depend on the external JSON server at request time for the happy path.

### Relationship to other ADRs in this folder

| ADR | Role |
|-----|------|
| [ADR-001](./ADR-001_REST_API_Functional_Requirements.md) | Functional contract: endpoint, payloads, sync behavior, error/empty responses |
| [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md) | How acceptance-style and integration tests exercise the HTTP API |
| **ADR-003 (this document)** | Runtime, libraries, and test technology choices |

### Key requirements driving stack selection

1. **Simple REST read model** — one GET, array of strings ([US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)).
2. **Durable local copy** — PostgreSQL + Flyway ([schema.sql](./schema.sql) illustrates the `greek_god` table).
3. **Periodic sync** — pull from my-json-server; tolerate external failures without blocking reads.
4. **Testability** — real HTTP to the app port; real database via Testcontainers; optional WireMock for the upstream host.
5. **Operational clarity** — Actuator health/metrics where the team enables them.

---

## Decision drivers

- **Alignment with US-001 and ADR-001** — stack must implement `GET /api/v1/gods/greek` and the documented JSON shapes.
- **Spring ecosystem fit** — matches ADR-001/ADR-002 and common educational / reference implementations.
- **Deterministic tests** — avoid flaky dependence on typicode during CI when stubs are used.
- **No reactive stack** — servlet MVC is sufficient; keeps dependencies and mental model simple.
- **Single HTTP client story** — `RestClient` for outbound sync and for acceptance tests (same API family as ADR-002).

---

## Considered options (runtime)

| Option | Assessment |
|--------|------------|
| **Spring Boot + MVC + JDBC** | **Selected.** Fits epic/feature/US-001, JDBC/Flyway/Testcontainers are well documented. |
| **Quarkus / Micronaut** | Rejected for **this** example set—ADRs here assume Spring Boot. |
| **JPA/Hibernate** | Not required for a single-table list; Spring Data JDBC keeps the model thin. |
| **Direct external proxy (no DB)** | Rejected in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md) (availability and latency). |

---

## Supplementary decisions

### 1. Outbound HTTP: `RestClient` (selected)

Use **`RestClient`** (Spring Framework 6.1+) for scheduled sync calls to the external JSON API, with **timeouts** from `application.yml` (or equivalent). **Do not** add **WebClient** / WebFlux for this service.

**Rejected for new code:** legacy `RestTemplate` as the primary client (maintenance mode).

### 2. HTTP acceptance / integration tests: `RestClient` or `TestRestTemplate` (selected)

Per [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md): build a client against `http://localhost:{port}` under `@SpringBootTest(webEnvironment = RANDOM_PORT)`, assert with **AssertJ**. **Do not** depend on **Rest Assured** for this module (Groovy/JVM fragility on modern JDKs—see short appendix below).

### 3. Automatic retries (out of scope for initial US-001)

No **Resilience4j Retry**, **Spring Retry**, or ad-hoc retry loops in the first delivery. Sync **failures** are handled by logging and the **next** schedule tick. If product mandates retries, record a new ADR.

### 4. WireMock (optional)

Unlike a multi-upstream aggregation API, this platform has **one** primary external JSON source for Greek data. **WireMock** is **optional** but useful to:

- Stub `GET` responses for the Greek list **with delays** or **faults** without hitting the network.
- Keep **BackgroundSyncService** tests **isolated** and **repeatable**.

Reset stubs per test method (or use dedicated instances) to avoid order-dependent failures.

### 5. Error response shape at the API boundary

**RFC 7807 Problem Details** is the **mandatory** error format for failed requests on the public API. Use Spring **`ProblemDetail`** (and typically **`@ControllerAdvice`** / `ResponseEntityExceptionHandler`) so that **500** (and other error) responses use **`application/problem+json`** with `type`, `title`, and `status`, and usually `detail` and `instance`, as specified in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md), [greekController-oas.yaml](./greekController-oas.yaml), and the [Gherkin feature](../agile/US-001_api_greek_gods_data_retrieval.feature).

### 6. Base package (example implementations)

Example code linked from this requirements set may use a base package such as **`info.jab.latency`** (controller, service, repository, sync). This is **illustrative**; align with the actual module’s `pom.xml` and team conventions.

---

## Maven-oriented view (design-level)

| Scope | Integration | Role |
|-------|-------------|------|
| main | `spring-boot-starter-web` | REST API (MVC) |
| main | `spring-boot-starter-data-jdbc` | Repositories / JDBC |
| main | `spring-boot-starter-jdbc` | (transitive; datasource) |
| main | `postgresql` driver | Production DB |
| main | `flyway-core` + `flyway-database-postgresql` | Schema migrations |
| main | `spring-boot-starter-actuator` | Health/metrics (optional but typical) |
| main | `springdoc-openapi-starter-webmvc-ui` (or equivalent) | OpenAPI/Swagger UI |
| test | `spring-boot-starter-test` | JUnit 5, AssertJ, MockMvc, `TestRestTemplate` |
| test | `testcontainers` + `postgresql` module | PostgreSQL in IT |
| test | `org.wiremock:wiremock` | **Optional** upstream stubs |

**Not used by this ADR:** `spring-boot-starter-webflux`, `rest-assured`, automatic retry libraries (unless a future ADR adds them).

---

## Consequences

### Positive

- Clear match between **US-001**, **ADR-001**, and the **technology** choices.
- **RestClient** reuse for production sync and for HTTP tests (ADR-002).
- **Testcontainers** gives high confidence for SQL and Flyway.

### Negative / trade-offs

- Running Testcontainers integration tests requires **Docker** (or compatible runtime) on developer/CI machines.
- Scheduled sync adds **operational** concerns (logs, metrics, alerting)—outside this ADR’s depth but real for production.

---

## Follow-up actions

1. Keep **Spring Boot / Java** versions **consistent** across ADR-002 context text, this ADR, and the implementation `pom.xml` (update ADR-002’s opening “Technology Stack” line when the example bumps versions).
2. If WireMock is adopted, document stub reset and base-URL override patterns in the module README.
3. When bumping Spring Boot, re-check **ProblemDetail** serialization defaults against **ADR-001** and OpenAPI.

---

## Appendix: Why Rest Assured is not the standard here

[ADR-002](./ADR-002-Acceptance-Testing-Strategy.md) records the full rationale. In short: Rest Assured’s Groovy-based request pipeline has shown **JDK 21+** fragility (`NullPointerException` in proxy/meta-property paths). **Spring `RestClient` + AssertJ** stays on the **`spring-web`** stack and matches outbound **RestClient** usage. A future ADR may revisit only if upstream fixes are proven on the team’s JDK matrix.

---

## References

- [ADR-001: REST API Functional Requirements](./ADR-001_REST_API_Functional_Requirements.md)
- [ADR-002: Acceptance Testing Strategy](./ADR-002-Acceptance-Testing-Strategy.md)
- [US-001: API Greek Gods Data Retrieval](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)
- [US-001 acceptance criteria (Gherkin)](../agile/US-001_api_greek_gods_data_retrieval.feature)
- [Public API OpenAPI](./greekController-oas.yaml)
- [External JSON server OpenAPI](./my-json-server-oas.yaml)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/reference/testing/index.html)
- [Spring Framework — RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
- [Testcontainers](https://java.testcontainers.org/)
- [WireMock](https://wiremock.org/)

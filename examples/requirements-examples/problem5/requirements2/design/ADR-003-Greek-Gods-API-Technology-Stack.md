# ADR-003: Greek Gods Data Synchronization API — Technology Stack

**Status:** Accepted  
**Date:** 2026-03-26  
**Deciders:** Development Team  
**Traceability:** [EPIC-001](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md), [FEAT-001](../agile/FEAT-001_REST_API_Endpoints_Greek_Gods_Data.md), [US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)

---

## Decision

Adopt a **technology stack** for the **Greek Gods Data Synchronization API Platform** that implements:

- **Runtime:** **Quarkus 3.x** as the documented baseline (see [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md); use **3.x** versions from the implementation `pom.xml`, and update **both** ADRs when changing minor/major) with **Quarkus REST** (Jakarta REST on Vert.x)—**no** separate reactive stack requirement beyond what Quarkus REST uses internally.
- **Public API:** Single read endpoint **`GET /api/v1/gods/greek`** returning a **JSON array of strings**, as specified in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md) and [US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md).
- **Persistence:** **PostgreSQL** with **Flyway** migrations and relational access via **`quarkus-jdbc-postgresql`** with **Agroal**—use **Panache Repository** (`quarkus-hibernate-orm-panache`) **or** programmatic JDBC / **`quarkus-jdbc-client`** patterns where a thinner stack is preferred; keep the model aligned with [schema.sql](./schema.sql).
- **Outbound HTTP (sync):** **MicroProfile REST Client** (`quarkus-rest-client-jackson` or reactive variant) **or** **`java.net.http.HttpClient`** with **connect/read timeouts** in configuration, calling the external JSON host (`https://my-json-server.typicode.com/jabrena/latency-problems` and the Greek resource path per [my-json-server-oas.yaml](./my-json-server-oas.yaml)). **No automatic HTTP retry library** for the initial scope—failures are logged and the next scheduled sync attempts again.
- **Scheduling:** **`quarkus-scheduler`** (`@Scheduled`) for background synchronization from the external source into PostgreSQL.
- **API documentation:** **`quarkus-smallrye-openapi`** (Swagger UI) aligned with [greekController-oas.yaml](./greekController-oas.yaml) (OpenAPI **3.0.3**).
- **Testing:** **JUnit 5**, **AssertJ**; **HTTP-level acceptance tests** use **REST Assured** under **`@QuarkusTest`** per [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md) (**required** for that layer). **Testcontainers** for PostgreSQL in integration tests (via **`QuarkusTestResourceLifecycleManager`** or documented Dev Services policy). **WireMock** (optional) to stub the external JSON server when deterministic offline sync tests are required.

This ADR **replaces** any prior copy that described the unrelated **God Analysis API** (`GET /api/v1/gods/stats/sum`, multi-source aggregation, `info.jab.ms`, and companion ADRs such as `ADR-002-God-Analysis-API-Non-Functional-Requirements.md`). Those documents live under other trees (e.g. `examples/requirements-examples/problem1`, `problem2`), **not** beside this `requirements2` folder.

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
4. **Testability** — real HTTP to the app in test mode; real database via Testcontainers (or equivalent); optional WireMock for the upstream host.
5. **Operational clarity** — SmallRye Health / metrics where the team enables them.

---

## Decision drivers

- **Alignment with US-001 and ADR-001** — stack must implement `GET /api/v1/gods/greek` and the documented JSON shapes.
- **Quarkus ecosystem fit** — native images optional later; fast dev mode and extension model for educational / reference implementations.
- **Deterministic tests** — avoid flaky dependence on typicode during CI when stubs are used.
- **Straightforward stack** — Jakarta REST resources and JDBC/Flyway without unnecessary frameworks.
- **Clear HTTP story** — MicroProfile REST Client or JDK `HttpClient` for **sync**; **acceptance tests** use **REST Assured** per ADR-002.

---

## Considered options (runtime)

| Option | Assessment |
|--------|------------|
| **Quarkus + REST + JDBC/Flyway** | **Selected.** Fits epic/feature/US-001; extensions and Testcontainers integration are well documented. |
| **Spring Boot** | Rejected for **this** example set—ADRs here assume **Quarkus**. |
| **Micronaut** | Alternative JVM microframework; not selected for this requirements tree. |
| **Full JPA without Flyway** | Possible but Flyway keeps schema evolution explicit; matches ADR-001 persistence narrative. |
| **Direct external proxy (no DB)** | Rejected in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md) (availability and latency). |

---

## Supplementary decisions

### 1. Outbound HTTP: MicroProfile REST Client or `HttpClient` (selected)

Use **MicroProfile REST Client** (`@RegisterRestClient`, `quarkus-rest-client-*`) **or** **`java.net.http.HttpClient`** for scheduled sync calls to the external JSON API, with **timeouts** from `application.properties` (or equivalent). Prefer **one** style per module for consistency.

**Rejected for primary outbound use in new code:** ad-hoc URLConnection without timeouts or metrics hooks.

### 2. HTTP acceptance / integration tests: REST Assured (selected)

Per [ADR-002](./ADR-002-Acceptance-Testing-Strategy.md): exercise the **`@QuarkusTest`** HTTP port with **REST Assured** only for acceptance-style HTTP tests; assert with **REST Assured** matchers and **AssertJ** where needed.

### 3. Automatic retries (out of scope for initial US-001)

No **SmallRye Fault Tolerance** retry, **Failsafe**, or ad-hoc retry loops in the first delivery unless a future ADR adds them. Sync **failures** are handled by logging and the **next** schedule tick.

### 4. WireMock (optional)

Unlike a multi-upstream aggregation API, this platform has **one** primary external JSON source for Greek data. **WireMock** is **optional** but useful to:

- Stub `GET` responses for the Greek list **with delays** or **faults** without hitting the network.
- Keep **sync service** tests **isolated** and **repeatable**.

Reset stubs per test method (or use dedicated instances) to avoid order-dependent failures.

### 5. Error response shape at the API boundary

**RFC 7807 Problem Details** is the **mandatory** error format for failed requests on the public API. Implement with a Jakarta REST **`ExceptionMapper`** (or shared filter/response helper) that returns **`application/problem+json`** with `type`, `title`, and `status`, and usually `detail` and `instance`, as specified in [ADR-001](./ADR-001_REST_API_Functional_Requirements.md), [greekController-oas.yaml](./greekController-oas.yaml), and the [Gherkin feature](../agile/US-001_api_greek_gods_data_retrieval.feature).

### 6. Base package (example implementations)

Example code linked from this requirements set may use a base package such as **`info.jab.latency`** (resource, service, repository, sync). This is **illustrative**; align with the actual module’s `pom.xml` and team conventions.

---

## Maven-oriented view (design-level)

| Scope | Integration | Role |
|-------|-------------|------|
| main | `quarkus-rest` (or `quarkus-resteasy-reactive-jackson` per BOM) | Jakarta REST endpoints |
| main | `quarkus-jdbc-postgresql` | Datasource + JDBC |
| main | `quarkus-flyway` | Schema migrations |
| main | `quarkus-scheduler` | `@Scheduled` sync |
| main | `quarkus-rest-client-jackson` (or reactive) **or** none if using JDK `HttpClient` only | Outbound HTTP to my-json-server |
| main | `quarkus-smallrye-openapi` | OpenAPI / Swagger UI |
| main | `quarkus-smallrye-health` | Health probes (typical) |
| main | Optional `quarkus-hibernate-orm-panache` | Repository-style access instead of raw JDBC |
| test | `quarkus-junit5` | `@QuarkusTest` |
| test | `rest-assured` | **Required** — HTTP acceptance tests (`@QuarkusTest`) |
| test | `testcontainers` + `postgresql` + `junit-jupiter` | PostgreSQL in IT |
| test | `org.wiremock:wiremock` | **Optional** upstream stubs |

**Not used by this ADR:** Spring Boot starters, **`spring-webflux`**-style stacks, automatic retry libraries (unless a future ADR adds them).

---

## Consequences

### Positive

- Clear match between **US-001**, **ADR-001**, and the **technology** choices.
- Fast dev workflow and optional native compilation path via Quarkus.
- **Testcontainers** + Quarkus test resources give high confidence for SQL and Flyway.

### Negative / trade-offs

- Running Testcontainers integration tests requires **Docker** (or compatible runtime) on developer/CI machines when not using Dev Services alone.
- Scheduled sync adds **operational** concerns (logs, metrics, alerting)—outside this ADR’s depth but real for production.

---

## Follow-up actions

1. Keep **Quarkus / Java** versions **consistent** across ADR-002 context text, this ADR, and the implementation `pom.xml` (update ADR-002’s opening “Technology Stack” line when the example bumps versions).
2. If WireMock is adopted, document stub reset and base-URL override patterns in the module README.
3. When bumping Quarkus, re-check **ExceptionMapper** / problem-detail JSON defaults against **ADR-001** and OpenAPI.

---

## Appendix: REST Assured in tests (Quarkus)

[ADR-002](./ADR-002-Acceptance-Testing-Strategy.md) records the full rationale. On **Quarkus**, **REST Assured** is the **mandated** library for HTTP-level **acceptance** tests alongside **`@QuarkusTest`**, giving a common, documented pattern for black-box API verification—unlike the Spring case where **`RestClient`** / **`TestRestTemplate`** could cover both app and test HTTP without a separate DSL.

---

## References

- [ADR-001: REST API Functional Requirements](./ADR-001_REST_API_Functional_Requirements.md)
- [ADR-002: Acceptance Testing Strategy](./ADR-002-Acceptance-Testing-Strategy.md)
- [US-001: API Greek Gods Data Retrieval](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)
- [US-001 acceptance criteria (Gherkin)](../agile/US-001_api_greek_gods_data_retrieval.feature)
- [Public API OpenAPI](./greekController-oas.yaml)
- [External JSON server OpenAPI](./my-json-server-oas.yaml)
- [Quarkus](https://quarkus.io/)
- [Quarkus — REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus — Scheduling tasks](https://quarkus.io/guides/scheduler)
- [Testcontainers](https://java.testcontainers.org/)
- [WireMock](https://wiremock.org/)

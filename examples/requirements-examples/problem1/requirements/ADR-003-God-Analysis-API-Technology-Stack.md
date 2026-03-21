# ADR-003: God Analysis API — Technology Stack

**Status:** Accepted
**Date:** Sat Mar 21 10:12:00 CET 2026
**Decision:** Adopt a **technology stack** for the God Analysis API covering **runtime framework** (Spring Boot), **outbound HTTP** (`RestClient`), **retry policy implementation** (**Resilience4j Retry**), **acceptance-style testing** (Rest Assured), and **integration testing** with **WireMock + Testcontainers + Toxiproxy** so timeout and retry scenarios run in **isolation** with realistic network behavior.

## Context

The God Analysis API project requires a robust Java framework to implement a REST API that integrates with multiple external mythology data sources, performs complex data transformations, and provides reliable service delivery despite external dependency failures.

Timeout and retry behavior ([ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)) is easy to get wrong if tests share static stubs, global delays, or JVM-wide clock tricks. **Containerized** dependencies and a **programmable proxy** keep each scenario reproducible and **isolated**.

### Key Requirements Driving Framework Selection

1. **REST API Development**: Need to expose a single endpoint (`GET /api/v1/gods/stats/sum`) with query parameter handling
2. **External HTTP Integration**: Must consume three external god APIs with timeout and retry capabilities
3. **Parallel Processing**: Requires concurrent calls to multiple external services for optimal performance
4. **Error Handling & Resilience**: Graceful degradation with partial results when external sources fail
5. **JSON Processing**: Handle JSON serialization/deserialization for API responses and external data
6. **Testing Support**: Comprehensive testing capabilities for acceptance, integration, and unit tests—with **deterministic** timeout/retry coverage
7. **Development Velocity**: Team expertise and rapid development requirements
8. **Production Readiness**: Monitoring, health checks, and operational capabilities

### Team Context

- Existing Java expertise within the development team
- Familiarity with Spring ecosystem and conventions
- Need for rapid prototyping and development
- Educational/research use case requiring stable, well-documented framework

### Relationship to ADR-002

[ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md) specifies **per-attempt timeout (5s)**, **up to 3 additional retries per source**, **linear spacing (no exponential backoff)**, and **parallel calls**. **Resilience4j Retry** implements the retry policy with configuration (max attempts, fixed wait) matching ADR-002 **per source**.

## Decision Drivers

- **Development Speed**: Framework must enable rapid API development
- **Team Expertise**: Leverage existing Spring/Java knowledge
- **REST API Maturity**: Proven patterns for REST endpoint development
- **HTTP Client Integration**: Built-in or well-integrated HTTP client capabilities
- **Testing Ecosystem**: Comprehensive testing support including mocking external services **and simulating latency/timeouts at the network edge**
- **Operational Readiness**: Production monitoring and health check capabilities
- **Community Support**: Active community and extensive documentation
- **Dependency Management**: Mature ecosystem with curated dependencies
- **Requirement traceability**: Retry and timeout behavior must remain visible and testable (see implementation plan)
- **Test isolation**: Timeout/retry tests must not depend on execution order or shared mutable stub state

## Considered Options (Runtime Platform)

### Option A: Spring Boot 4.0.4 (SELECTED)

**Pros:**

- **Mature REST Support**: Spring Web provides comprehensive REST API development with `@RestController`, automatic JSON serialization, and query parameter binding
- **HTTP Client Integration**: First-class support for `RestClient` (synchronous), `WebClient` (reactive), and legacy `RestTemplate` (maintenance mode)
- **Parallel Processing**: Built-in async support with `@Async`, `CompletableFuture`, and structured concurrency options
- **Testing Excellence**: Spring Boot Test provides `@SpringBootTest`, `@WebMvcTest`, `MockMvc`; integrates with Testcontainers
- **Auto-Configuration**: Zero-config setup for common patterns (JSON processing, HTTP clients, error handling)
- **Operational Features**: Built-in actuator endpoints for health checks, metrics, and monitoring
- **Team Expertise**: Existing knowledge reduces learning curve and development time
- **Ecosystem Maturity**: Curated starter dependencies and extensive third-party integrations
- **Error Handling**: Global exception handling with `@ControllerAdvice` and structured error responses

**Cons:**

- **Framework Overhead**: Larger memory footprint compared to lightweight alternatives
- **Learning Curve**: Complex for simple use cases (though mitigated by team expertise)
- **Dependency Weight**: Brings many transitive dependencies

### Option B: Quarkus 3.x

**Pros:** Performance, native compilation, cloud-native HTTP clients.
**Cons:** Team learning curve, smaller ecosystem than Spring for this use case.

### Option C: Micronaut 4.x

**Pros:** Low footprint, compile-time DI.
**Cons:** Team expertise gap, less documentation and testing tooling than Spring.

### Option D: Plain Java with HTTP Libraries

**Pros:** Minimal dependencies, full control.
**Cons:** High boilerplate, weaker out-of-the-box testing and operations story.

## Supplementary decisions: HTTP client, acceptance tests, retries

These decisions apply **within** the chosen Spring Boot stack. They do not change ADR-002’s behavioral contract.

### 1. Outbound HTTP client: `RestClient` (SELECTED)

**Context:** The service performs **blocking** parallel fetches (e.g. `CompletableFuture.supplyAsync` around per-source calls) with **per-attempt timeouts**. Spring Framework 6.1+ provides **RestClient**, a synchronous API designed as the modern successor to `RestTemplate` (`RestTemplate` is in maintenance mode; new code should prefer `RestClient` or `WebClient`).

| Option | Assessment |
|--------|------------|
| **RestClient** | **Selected.** Same thread model as a classic servlet stack; fluent API; integrates with `ClientHttpRequestFactory` / `JdkClientHttpRequestFactory` for connect/read timeouts; composes cleanly with **Resilience4j** `Retry` around each per-source call; testable with `MockRestServiceServer` or Toxiproxy-backed URLs. |
| **WebClient** | Valid for non-blocking pipelines; higher overhead if the app is otherwise servlet-based. |
| **RestTemplate** | **Not recommended** for new code. |

**Decision:** Use **RestClient** for calls to Greek, Roman, and Nordic sources.

### 2. Acceptance tests: Rest Assured (SELECTED, scoped)

**Context:** Acceptance tests should assert behavior aligned with [US-001_god_analysis_api.feature](US-001_god_analysis_api.feature).


| Option | Assessment |
|--------|------------|
| **MockMvc** | Fast for **slice** tests; less like a real TCP client. |
| **Rest Assured** | **Selected for black-box AT** over `@SpringBootTest(webEnvironment = RANDOM_PORT)`. Fluent HTTP + JSON assertions; matches how external consumers call the API. |


**Decision:** Add **Rest Assured** for **HTTP-level acceptance tests**; retain **MockMvc** where slice tests suffice.

### 3. Retries: Resilience4j vs Spring Retry vs manual (SELECTED: Resilience4j Retry for v1)

**Context:** ADR-002 requires **linear retries**, **per-source isolation**, and **up to 3 retries after the first attempt** (4 total calls per source when retries fire).

| Option | Assessment |
|--------|------------|
| **Resilience4j (`resilience4j-retry`)** | **Selected.** Declarative or programmatic **Retry** with fixed-interval waits matches ADR-002 linear policy; **Micrometer** hooks can expose retry counts; **per-source** `Retry` instances (e.g. named `greek`, `roman`, `nordic`) preserve isolation. |
| **Spring Retry (`@Retryable`)** | Rejected as primary: AOP-based, overlaps conceptually with Resilience4j; team standard is Resilience4j for resilience concerns. |
| **Manual retry loop** | Rejected as primary: clear but duplicates policy logic and metrics that Resilience4j centralizes; acceptable only in spike code, not as the shipped pattern. |

**Decision:** Use **Resilience4j Retry** to implement ADR-002. Configure **max attempts = 4** (1 initial + 3 retries) and **fixed wait** between attempts (no exponential backoff). Apply **one `Retry` configuration per pantheon** so retries on Nordic do not affect Greek/Roman. **Do not** enable Resilience4j **CircuitBreaker** (or other modules) until ADR-002’s “circuit breaker deferred” posture changes.

**Implementation note:** Wrap each per-source `RestClient` call with the appropriate `Retry` (e.g. `Retry.decorateCallable` / registry-backed `Retry.executeCallable`) so **timeouts** still apply **per attempt** inside the retry loop, consistent with ADR-002’s “per-attempt timeout.”

### 4. Integration testing: WireMock + Testcontainers + Toxiproxy (SELECTED)

**Problem:** In-process WireMock with fixed delays can work for simple cases, but **timeout and retry** scenarios need:

- **Real TCP and socket timeouts** (closer to production than pure in-memory mocks alone)
- **Per-scenario control** of latency and failure without cross-test leakage
- **Isolation** so test A’s “slow Nordic” does not affect test B

**Approach:** Run **WireMock** inside **Testcontainers** to serve deterministic JSON for the three god list endpoints. Place **Toxiproxy** (via Testcontainers’ Toxiproxy module) **in front of** each upstream (or use one proxy with multiple listeners—see implementation notes). The Spring application under test is configured (e.g. `@DynamicPropertySource`) so **base URLs point at Toxiproxy-published ports**, not directly at WireMock. Stubs remain in WireMock; **toxics** (latency, timeout, bandwidth) attach to the **proxy** path so each test can:

1. **Reset** toxics in `@BeforeEach` / `@AfterEach` (or equivalent) for **isolation**
2. Apply **latency only to the Nordic proxy** to exercise **5s timeout + partial results**
3. After retries are implemented, combine **transient latency** with **short spikes** to force **retry exhaustion** on one source only

**Alternatives considered:**


| Approach                                      | Why not primary                                                                                                                                                                         |
| --------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| WireMock only (JVM, shared server)            | Global stub state and timing coupling; harder to guarantee **per-test** isolation for parallel CI                                                                                       |
| WireMock Testcontainers **without** Toxiproxy | Can delay responses in WireMock, but **timeout/retry** tests benefit from a **separate, resettable** network fault layer; Toxiproxy toxics are explicit and easy to clear between tests |
| Live Typicode                                 | Flaky, non-deterministic sums and latency                                                                                                                                               |


**Decision:** Use **Testcontainers** to run **WireMock** (container or official WireMock Testcontainers integration) **together with** **Toxiproxy**. **Every** integration/acceptance test that depends on timeout or retry policy must **start from a clean toxic state** (no shared toxics across tests). Unit tests for pure algorithms remain **without** containers.

**Implementation notes (non-normative):**

- Use a **Docker-enabled** CI agent; document `DOCKER_HOST` / Colima / Rancher expectations in module README.
- Prefer **JUnit 5** lifecycle hooks to **remove all toxics** before or after each test method that mutates proxies.
- Map **one Toxiproxy upstream per pantheon** (greek, roman, nordic) so failures are **isolated** per ADR-002.
- **Parallel test execution:** If Surefire runs classes in parallel, use **separate container networks** or **synchronized** static container pattern with **per-class** dynamic ports—avoid two classes mutating the same proxy concurrently.

## Decision Outcome

**Chosen platform: Spring Boot 4.0.4 with Java 26** (align module `java.version` with repo standard if not 26).

### Rationale

1. Spring Boot accelerates delivery and matches team skills
2. **RestClient** fits synchronous parallel fetches with per-attempt timeouts
3. **Rest Assured** gives readable black-box acceptance tests
4. **WireMock + Testcontainers + Toxiproxy** provides **isolated**, **realistic** timeout/retry validation aligned with ADR-002
5. **Resilience4j Retry** implements ADR-002 policy consistently, supports observability, and avoids ad-hoc loops while **other Resilience4j modules stay off** until requirements change

### Implementation Approach

- **REST Controller**: `@RestController`, `GET /api/v1/gods/stats/sum`
- **HTTP Client**: **RestClient**, **5s timeout per attempt** (each attempt inside retry is still bound by this timeout)
- **Parallelism**: `CompletableFuture` (or structured concurrency) per source; **retry boundary per source** (separate `Retry` instance or configuration per pantheon)
- **Retries**: **Resilience4j Retry**—**4 max attempts**, **fixed delay** between attempts
- **Error handling**: `@ControllerAdvice` where needed; partial aggregation per feature
- **Testing**:
  - **Unit tests**: Unicode/filter/sum without containers
  - **Acceptance**: `@SpringBootTest(RANDOM_PORT)` + **Rest Assured**
  - **Integration (timeout/retry/isolation)**: **Testcontainers** (**WireMock** + **Toxiproxy**), dynamic base URLs, **reset toxics per test**

### Planned Maven coordinates (design-level)

| Scope | Artifact / integration | Role |
| ----- | ---------------------- | ---- |
| main | `spring-boot-starter-web` | REST API |
| main | `spring-boot-starter-actuator` | Ops |
| main | `io.github.resilience4j:resilience4j-retry` | ADR-002 retry policy (linear, per source) |
| main | `io.github.resilience4j:resilience4j-spring-boot3` (or artifact aligned with Spring Boot 4) | Spring configuration for Retry beans and properties |
| test | `spring-boot-starter-test` | JUnit 5, AssertJ, MockMvc |
| test | `rest-assured` | Black-box HTTP acceptance tests |
| test | `org.testcontainers:testcontainers` + `junit-jupiter` | Container lifecycle |
| test | `org.testcontainers:toxiproxy` | Latency/timeout/toxic control |
| test | WireMock on Testcontainers (e.g. WireMock Testcontainers module or compatible image) | Deterministic upstream JSON |

**Not used for v1 (by this ADR):** `spring-retry`, broad Resilience4j modules beyond **retry** (unless a future ADR adds them).

## Consequences

### Positive

- Timeout/retry behavior validated under **realistic network conditions** with **per-test isolation**
- Clear separation: **WireMock** owns **responses**, **Toxiproxy** owns **faults**
- Spring stack + Testcontainers is a well-documented industry pattern
- **Resilience4j Retry** gives uniform policy, testable configuration, and optional metrics without enabling circuit breaking in v1

### Negative

- **Slower tests** than pure MockMvc; requires Docker in dev/CI
- **More moving parts** to maintain (images, ports, lifecycle)
- Higher memory use on developer laptops when running full integration suite
- **Resilience4j** adds dependencies and configuration surface; team must **discipline** configuration so only **Retry** is active for this service in v1

### Neutral

- Java version should match org standard (25 vs 26)
- Local runs may use Testcontainers reuse / Ryuk as per team policy

## Follow-up Actions

1. Add Maven dependencies for **Resilience4j Retry** (+ Spring Boot integration), Testcontainers, Toxiproxy, and WireMock-on-containers
2. Implement a **test support** class (or extension) that **starts** WireMock + Toxiproxy, **wires** `RestClient` base URLs, and **clears toxics** between tests
3. Register **RestClient** with production timeouts; define **per-source** `Retry` beans or config; override URLs only in tests
4. Keep **Rest Assured** acceptance suite small and Gherkin-aligned
5. Document Docker prerequisites in module README
6. Verify Resilience4j Retry configuration matches ADR-002 requirements

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-002: God Analysis API Non-Functional Requirements](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)
- [US-001: God Analysis API User Story](US-001_God_Analysis_API.md)
- [Feature Specification](US-001_god_analysis_api.feature)
- [US-001-plan-analysis.plan.md](US-001-plan-analysis.plan.md)
- [Spring Boot 4.0.4 Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Framework — RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
- [Rest Assured](https://rest-assured.io/)
- [Testcontainers](https://java.testcontainers.org/)
- [Toxiproxy](https://github.com/Shopify/toxiproxy)
- [WireMock](https://wiremock.org/) — including Testcontainers integration where applicable
- [Resilience4j Retry](https://resilience4j.readme.io/docs/retry)

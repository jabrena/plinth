# ADR-003: God Analysis API — Technology Stack

**Status:** Proposed
**Date:** Wed Mar 25 00:00:00 CET 2026
**Decision:** Adopt a **technology stack** for the God Analysis API covering **runtime framework** (Quarkus), **outbound HTTP** (MicroProfile REST Client with **configured connect/read timeouts** in `application.properties`), **acceptance-style testing** (**REST Assured** against `@QuarkusTest`), and **integration testing** with **WireMock in-process** so **timeout** and partial-result scenarios (Wikipedia unavailability) run in **isolation** with deterministic delay simulation. **Automatic HTTP retries are out of scope** for this user story — no Resilience4j Retry (or equivalent) required.

## Context

The God Analysis API project requires a robust Java framework to implement a REST API that fetches a list of Greek gods and then fans out to Wikipedia — one call per god — to calculate character lengths and identify the god(s) with the most literature. The service must handle Wikipedia call failures gracefully (character count = 0 fallback) and return consistent results.

Timeout behavior ([ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)) is easy to get wrong if tests share static stubs, global delays, or JVM-wide clock tricks. **WireMock** with **per-test stub reset** keeps each scenario reproducible and **isolated**.

### Key Requirements Driving Framework Selection

1. **REST API Development**: Need to expose a single endpoint (`GET /api/v1/gods/wikipedia/most-literature`)
2. **External HTTP Integration**: Must consume the Greek Gods API (one call) and Wikipedia (one call per god) with **MicroProfile REST Client** timeouts (no automatic retries)
3. **Parallel Processing**: Requires concurrent Wikipedia fan-out calls for optimal performance
4. **Error Handling & Resilience**: Wikipedia call failure → character count 0; Greek Gods API failure → HTTP **503** with **empty body**
5. **JSON Processing**: Handle JSON serialization/deserialization for API responses and external data
6. **Testing Support**: Comprehensive testing capabilities for acceptance, integration, and unit tests — with **deterministic** timeout coverage using WireMock delay simulation
7. **Development Velocity**: Team expertise and rapid development requirements
8. **Production Readiness**: Monitoring, health checks, and operational capabilities

### Team Context

- Existing Java expertise within the development team
- Familiarity with Quarkus ecosystem and CDI conventions
- Need for rapid prototyping and development
- Educational/research use case requiring stable, well-documented framework

### Package Structure Requirements

**Decision:** Use `info.jab.ms` as the base package for the God Analysis API implementation.

**Rationale:** Standardized package naming convention aligns with organizational standards and provides clear namespace separation.

### Relationship to ADR-002

[ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md) specifies **bounded waits** via **MicroProfile REST Client** connect/read timeouts, **parallel Wikipedia fan-out**, and **character count 0** when a Wikipedia call times out — **without** a separate retry policy. Implementation uses **one attempt per Wikipedia call** per request.

## Decision Drivers

- **Development Speed**: Framework must enable rapid API development
- **Team Expertise**: Leverage existing Java/Quarkus knowledge
- **REST API Maturity**: Proven patterns for Jakarta REST endpoint development
- **HTTP Client Integration**: Built-in or well-integrated HTTP client capabilities
- **Testing Ecosystem**: Comprehensive testing support including mocking external services **and simulating latency/timeouts with WireMock in-process delays**
- **Operational Readiness**: Production monitoring and health check capabilities via SmallRye Health
- **Community Support**: Active community and extensive documentation
- **Dependency Management**: Mature BOM-based ecosystem with curated extensions
- **Requirement traceability**: Timeout and partial-result behavior must remain visible and testable
- **Test isolation**: Timeout tests must not depend on execution order or shared mutable stub state
- **Performance**: Fast startup time and low memory footprint for cloud-native deployment

## Considered Options (Runtime Platform)

### Option A: Quarkus 3.x (SELECTED)

**Architecture Decision: Jakarta REST Stack (RESTEasy) — Blocking API with Virtual Threads**

This implementation uses **Quarkus REST (RESTEasy Reactive / Jakarta REST)** in **blocking mode** exclusively. **No reactive programming patterns** (Mutiny `Uni`/`Multi`, reactive streams) will be exposed in the service layer.

**Pros:**

- **Cloud-Native Performance**: Quarkus provides fast startup time and low memory footprint — ideal for containerized deployments
- **Jakarta REST Support**: First-class support for `@Path`, `@GET`, automatic JSON serialization via Jackson or JSON-B, and query parameter binding with `@QueryParam`
- **MicroProfile REST Client**: Declarative, interface-based HTTP client (`@RegisterRestClient`) with connect/read timeout configuration in `application.properties`
- **Virtual Threads**: `@RunOnVirtualThread` annotation enables virtual thread execution per request; integrates cleanly with blocking MicroProfile REST Client calls
- **Parallel Processing**: `CompletableFuture` with virtual thread executor for concurrent Wikipedia fan-out calls within a blocking request handler
- **REST Assured Integration**: `quarkus-junit5` + `rest-assured` is the standard Quarkus test stack; REST Assured works reliably on Java 21+ within Quarkus's `@QuarkusTest` harness
- **SmallRye Health**: Built-in readiness and liveness probes via `quarkus-smallrye-health`
- **Dev Services**: Zero-config WireMock or container setup in development/test modes
- **Minimal Boilerplate**: CDI-based injection, build-time optimizations, and Quarkus extension ecosystem reduce configuration overhead
- **ExceptionMapper**: Optional `@Provider ExceptionMapper<T>` for **unexpected** or unmappable errors where a structured body is useful; **US-001 does not require** RFC 7807 or a JSON body for the **503** returned when the Greek Gods API fails — that response is **status-only** (empty body), per [ADR-001](ADR-001-God-Analysis-API-Functional-Requirements.md)

**Cons:**

- **Ecosystem Differences**: Some Spring-specific patterns (e.g., `@ControllerAdvice`, `@SpringBootTest`) require translation to Jakarta/Quarkus equivalents
- **Learning Curve**: Teams with heavy Spring Boot experience need familiarization with CDI, Quarkus extensions, and MicroProfile APIs
- **Extension Dependency**: Functionality is unlocked via Quarkus extensions rather than arbitrary Maven dependencies

### Option B: Spring Boot 4.0.x

**Pros:** Largest ecosystem, widest team familiarity in the Java community, mature tooling (Spring MVC, RestClient, `@SpringBootTest`).
**Cons:** Higher memory footprint and startup time compared to Quarkus; REST Assured is **fragile on Java 21+** due to Groovy/metaprogramming issues in the test client (NPE in `ConcurrentHashMap` via `RequestSpecificationImpl.applyProxySettings`).

### Option C: Micronaut 4.x

**Pros:** Low footprint, compile-time DI.
**Cons:** Team expertise gap, less documentation and testing tooling than Quarkus for this use case.

### Option D: Plain Java with HTTP Libraries

**Pros:** Minimal dependencies, full control.
**Cons:** High boilerplate, weaker out-of-the-box testing and operations story.

## Supplementary decisions: HTTP client and acceptance tests

These decisions apply **within** the chosen Quarkus stack. They align with ADR-002's **timeout + partial results** contract (no retries).

### 1. Outbound HTTP client: MicroProfile REST Client (SELECTED)

**Context:** The service performs **blocking** parallel Wikipedia fan-out calls (e.g. `CompletableFuture.supplyAsync` with a virtual thread executor) with **connect/read timeouts** on each call. This aligns with the **Quarkus blocking REST architecture** decision. Quarkus provides the **MicroProfile REST Client** (`quarkus-rest-client-reactive`) as its standard declarative HTTP client; used in blocking mode, it exposes a synchronous Java interface.

**Note:** `quarkus-rest-client-reactive` is the extension artifact name; this implementation uses it in **blocking/synchronous mode** via `@RegisterRestClient` interface proxies. No Mutiny reactive types are involved.

**Reactive Programming Exclusion:** This implementation explicitly **excludes Mutiny reactive types** (`Uni`, `Multi`) in the service layer to maintain a simple, predictable blocking call model.

| Option | Assessment |
|--------|------------|
| **MicroProfile REST Client** | **Selected.** Declarative `@RegisterRestClient` interface; timeouts via `quarkus.rest-client.<config-key>.connect-timeout` and `.read-timeout` in `application.properties`; backed by a virtual-thread-compatible blocking mode; testable with WireMock-backed URLs via `%test` profile overrides. **No Mutiny types required.** |
| **java.net.http.HttpClient** | **Valid alternative** for pure JDK approach; explicit `HttpRequest` with `.timeout(Duration)`. Higher boilerplate but zero framework coupling. |
| **Reactive `Uni<T>` / WebClient** | **Explicitly rejected.** Introduces Mutiny reactive types that conflict with our blocking-first architecture decision. |

**Decision:** Use **MicroProfile REST Client** (`@RegisterRestClient`) for calls to the Greek Gods API and Wikipedia, with timeout configuration in `application.properties`.

**Timeout Configuration Example:**
```properties
# application.properties
quarkus.rest-client.greek-gods-api.url=https://my-json-server.typicode.com
quarkus.rest-client.greek-gods-api.connect-timeout=5000
quarkus.rest-client.greek-gods-api.read-timeout=5000

quarkus.rest-client.wikipedia-api.url=https://en.wikipedia.org
quarkus.rest-client.wikipedia-api.connect-timeout=5000
quarkus.rest-client.wikipedia-api.read-timeout=5000

# %test profile overrides within the same application.properties (WireMock)
%test.quarkus.rest-client.greek-gods-api.url=http://localhost:${wiremock.port}
%test.quarkus.rest-client.wikipedia-api.url=http://localhost:${wiremock.port}
```

### Reactive Programming Exclusion (EXPLICIT)

**Decision:** This implementation **explicitly excludes reactive programming** types in the service and resource layers.

**Rationale:**
- **Simplicity**: Blocking model is well-understood and sufficient for this use case
- **Team Expertise**: Eliminates Mutiny/reactive-streams learning curve
- **Dependency Management**: Avoids potential conflicts between blocking and reactive execution models
- **Testing**: `@QuarkusTest` + REST Assured covers the blocking resource stack end-to-end
- **Performance**: Virtual threads (`@RunOnVirtualThread`) with `CompletableFuture` parallelism meets performance requirements without reactive overhead

**Excluded Patterns:**
- `Uni<T>` / `Multi<T>` return types in service/resource layer
- Non-blocking I/O patterns exposed to callers
- Reactive database drivers
- Structured concurrency (not required for this use case)

### Virtual Threads (INCLUDED)

**Decision:** Use **virtual threads** with `CompletableFuture` for parallel processing of Wikipedia fan-out calls.

**Rationale:**
- **Lightweight Concurrency**: Virtual threads provide efficient concurrent execution without the overhead of platform threads
- **Simplified Model**: Works seamlessly with existing `CompletableFuture` patterns and blocking I/O operations
- **Resource Efficiency**: Allows handling many concurrent Wikipedia calls without thread pool exhaustion
- **Quarkus Integration**: `@RunOnVirtualThread` on resource methods routes each request to a virtual thread; `CompletableFuture.supplyAsync()` with `Executors.newVirtualThreadPerTaskExecutor()` enables parallel sub-calls

**Implementation Approach:**
- Annotate Jakarta REST resource method with `@RunOnVirtualThread` to run the entire request on a virtual thread
- Use `CompletableFuture.supplyAsync()` with a virtual thread executor for parallel Wikipedia fan-out calls
- Maintain blocking I/O model with MicroProfile REST Client while benefiting from virtual thread scalability
- **No structured concurrency**: Standard `CompletableFuture` composition is sufficient for this use case

### 2. Acceptance tests: REST Assured with `@QuarkusTest` (SELECTED)

**Context:** Acceptance tests should assert behavior aligned with [US-001-Greek-Gods-Wikipedia-Information.feature](US-001-Greek-Gods-Wikipedia-Information.feature).

**Gherkin / JUnit tag alignment:** The feature file tags scenarios with `@acceptance-test` and `@integration-test`. JUnit 5 test classes or methods should use the same strings in **`@Tag("acceptance-test")`** and **`@Tag("integration-test")`** so filters (Surefire/Failsafe), reports, and traceability match the Gherkin tags.

| Option | Assessment |
|--------|------------|
| **REST Assured** | **Selected.** Standard Quarkus test HTTP client; `@QuarkusTest` starts the full application on a random port (`quarkus.http.test-port`); REST Assured auto-configures base URL; fluent assertions with `given()…when().get(…).then().statusCode(200)`. Works reliably on **Java 21+** within Quarkus's `@QuarkusTest` harness — **no Groovy metaprogramming issues** in this context. |
| **java.net.http.HttpClient** | Valid but verbose; no fluent assertion DSL. Suitable if REST Assured is unavailable. |
| **MockMvc** | **Not applicable** — Spring-specific. Not available in Quarkus. |

**Decision:** Use **REST Assured** for **HTTP-level acceptance tests** tagged `@Tag("acceptance-test")` within `@QuarkusTest`, mirroring the feature file’s `@acceptance-test` scenarios.

### 3. Automatic retries (OUT OF SCOPE for US-001)

**Decision:** Do **not** add **Resilience4j Retry**, **SmallRye Fault Tolerance Retry**, or ad-hoc retry loops for this user story.

**Rationale:** The original problem scope is satisfied by **MicroProfile REST Client** timeouts configured in `application.properties` plus the **character-count-0 fallback** when a Wikipedia call times out. Retries would add dependencies, configuration surface, and test scenarios beyond what the functional requirements require.

If product later mandates retries, capture that in a new ADR and only then add SmallRye Fault Tolerance (`@Retry`).

### 4. Integration testing: WireMock (SELECTED)

**Problem:** In-process WireMock with fixed delays can work for simple cases, but **timeout** scenarios need:

- **Deterministic delay simulation** to trigger client-side timeouts consistently
- **Per-scenario control** of latency and failure without cross-test leakage
- **Isolation** so test A's "slow Wikipedia call" does not affect test B

**Approach:** Use **WireMock** (in-process) to serve deterministic responses for both the Greek Gods API and individual Wikipedia page endpoints. WireMock provides both **response stubs** and **delay/failure simulation** so each test can:

1. **Reset** WireMock stubs in `@BeforeEach` / `@AfterEach` for **isolation**
2. Apply **delay beyond the configured read timeout** on one or more endpoints using `fixedDelayMilliseconds` to exercise **timeout + count-0 fallback** (single attempt per call)

**Alternatives considered:**

| Approach | Why not primary |
|----------|-----------------|
| WireMock in-process | **Selected approach** — provides **delay simulation** and **per-test isolation** through stub reset in `@BeforeEach` |
| WireMock with Testcontainers | Adds Docker dependency and complexity without benefit for client timeout testing; in-process delays are sufficient |
| Live Wikipedia / Typicode | Flaky, non-deterministic content and latency |

**Decision:** Use **WireMock** for integration testing. Tag integration-focused tests with **`@Tag("integration-test")`** to mirror the feature file’s `@integration-test` scenario (and any additional integration coverage). **Every** integration or acceptance test that depends on timeout behavior must **start from a clean stub state** using `@BeforeEach` lifecycle hooks.

**Implementation notes (non-normative):**

- Use **JUnit 5** lifecycle hooks to **reset all WireMock stubs** before each test method using `wireMockServer.resetAll()`.
- Configure **separate WireMock URL patterns** for the Greek Gods endpoint and each Wikipedia page path so failures are **isolated** per ADR-002.
- Override REST Client base URLs via `%test` profile in `application.properties` pointing to `http://localhost:${wiremock.port}`.
- **Parallel test execution:** If Surefire runs classes in parallel, use **separate WireMock server instances per test class** or a **synchronized** static server pattern with per-class dynamic ports.

**WireMock File Structure and Mappings:**

WireMock supports two approaches for organizing test data:

1. **Programmatic Stubs** (recommended for dynamic/timeout scenarios):
   ```java
   wireMockServer.stubFor(get(urlEqualTo("/jabrena/latency-problems/greek"))
       .willReturn(aResponse()
           .withStatus(200)
           .withHeader("Content-Type", "application/json")
           .withBodyFile("greek-gods-response.json")));

   wireMockServer.stubFor(get(urlEqualTo("/wiki/Zeus"))
       .willReturn(aResponse()
           .withStatus(200)
           .withHeader("Content-Type", "text/html")
           .withBodyFile("wikipedia-zeus.html")
           .withFixedDelay(6000))); // 6s delay exceeds the 5s read timeout
   ```

2. **File-based Mappings** (useful for static scenarios):
   - **Mappings directory**: `src/test/resources/wiremock/mappings/`
   - **Files directory**: `src/test/resources/wiremock/__files/`

**Recommended Structure for God Analysis API:**
```
src/test/resources/
└── wiremock/
    ├── mappings/
    │   ├── greek-gods-success.json
    │   └── greek-gods-unavailable.json
    └── __files/
        ├── greek-gods-response.json       # ["Ares","Zeus","Athena",...]
        ├── wikipedia-ares.html            # stub Wikipedia page for Ares
        ├── wikipedia-zeus.html            # stub Wikipedia page for Zeus
        ├── wikipedia-athena.html          # stub Wikipedia page for Athena
        └── empty-response.html            # for unavailable page scenarios
```

**Benefits of `wiremock/__files` + mappings approach:**
- **Separation of concerns**: Mapping logic separate from response data
- **Reusable responses**: Same file reused across multiple test scenarios
- **Version control friendly**: Plain text files are easy to review
- **Scenario flexibility**: Easy to create variations (success, timeout, 404) using same base data

## Decision Outcome

**Chosen platform: Quarkus 3.x with Jakarta REST (RESTEasy) and Java 26.** The sample implementation sets **`maven.compiler.release` to 26** in `implementation/pom.xml`. That is **intentional** and **not** the same as the repository-wide **Java 25** standard documented in the root `AGENTS.md`.

**Architecture: Blocking Jakarta REST Stack with Virtual Threads** — No reactive programming types in the service layer.

### Rationale

1. **Quarkus REST (Jakarta REST)** provides mature, blocking REST API development without reactive complexity
2. **MicroProfile REST Client** fits synchronous parallel fan-out calls (Greek Gods + Wikipedia) with configured connect/read timeouts (no reactive dependencies)
3. **REST Assured + `@QuarkusTest`** gives **black-box** acceptance tests over a real port with a fluent HTTP DSL — stable on Java 21+
4. **WireMock** provides **isolated**, **deterministic** timeout validation aligned with ADR-002
5. **No retry library** keeps the dependency graph and operational knobs smaller than a fault-tolerance-based design
6. **Blocking-only service layer** eliminates reactive programming complexity and Mutiny learning overhead

### Implementation Approach

- **Architecture**: **Quarkus REST with virtual threads** — blocking Jakarta REST, `@RunOnVirtualThread`, **no reactive Mutiny types**
- **REST Resource**: `@Path`, `@GET`, `GET /api/v1/gods/wikipedia/most-literature`
- **HTTP Client**: **MicroProfile REST Client** (`@RegisterRestClient`) with **connect/read timeouts** from `application.properties` (e.g. 5 s defaults) — one interface for Greek Gods API, one for Wikipedia
- **Parallelism**: `CompletableFuture` with `Executors.newVirtualThreadPerTaskExecutor()` for parallel Wikipedia fan-out; **one attempt per call** per request
- **Retries**: **None** for US-001 (see §3 above)
- **Configuration**: `application.properties` with `%test` profile overrides (using `%test.` key prefix) for WireMock URLs — **single file, no separate profile files**
- **Error handling**: HTTP **503** with **empty body** when the Greek Gods API cannot be fetched (required input); Wikipedia failure or timeout → count **0** per god. Optional `ExceptionMapper` only for cases outside this contract (e.g. unexpected internal failures), not for the US-001 **503** path
- **Dependencies**: **No Mutiny types in service layer** (`Uni`, `Multi`)
- **Testing**:
  - **Unit tests**: Character-count calculation and ranking logic without containers
  - **Acceptance**: `@QuarkusTest` + **REST Assured** (real HTTP to `localhost:{quarkus.http.test-port}`; fluent assertions); **`@Tag("acceptance-test")`** per Gherkin `@acceptance-test`
  - **Integration (timeout/isolation)**: **WireMock**, **reset stubs per test**; **`@Tag("integration-test")`** per Gherkin `@integration-test` where the test maps to that scenario

### Planned Maven coordinates (design-level)

| Scope | Artifact / extension | Role |
|-------|----------------------|------|
| main | `quarkus-rest` | Jakarta REST resource (`@Path`, `@GET`) |
| main | `quarkus-rest-jackson` | Server-side JSON serialization of response DTOs (e.g. `GodAnalysisResponse`) |
| main | `quarkus-rest-client` | MicroProfile REST Client for outbound HTTP (used in blocking mode) — **replaces `quarkus-rest-client-reactive` in BOM 3.x** |
| main | `quarkus-rest-client-jackson` | Client-side JSON deserialization of REST client responses |
| main | `quarkus-smallrye-health` | Readiness and liveness probes |
| main | `quarkus-arc` | CDI container (included transitively) |
| test | `quarkus-junit5` | `@QuarkusTest`, `@QuarkusTestProfile` |
| test | `rest-assured` | HTTP-level acceptance tests |
| test | `org.wiremock:wiremock` | Deterministic upstream JSON/HTML stubs with delay simulation |

**Not used for v1 (by this ADR):**
- `smallrye-fault-tolerance` / `resilience4j-retry` (retries out of scope for US-001)
- **Reactive types in service layer**: `io.smallrye.mutiny:mutiny` `Uni`/`Multi` return types
- `quarkus-rest-client-reactive` — superseded by `quarkus-rest-client` in Quarkus BOM 3.x (see implementation note below)

> **Implementation note (discovered during Milestone A1):** Quarkus BOM **3.34.1** no longer manages `quarkus-rest-client-reactive` — the artifact was replaced by **`quarkus-rest-client`**. In addition, server-side JSON serialization of response records (e.g. `GodAnalysisResponse`) requires **`quarkus-rest-jackson`** explicitly; it is not pulled in transitively by `quarkus-rest` alone. Client-side deserialization of `List<String>` from the Greek Gods API requires **`quarkus-rest-client-jackson`**. Use these four artifacts together for a fully working REST + REST-client stack with Jackson in Quarkus 3.34.x.

## Consequences

### Positive

- Timeout behavior validated with **deterministic delay simulation** and **per-test isolation**
- **WireMock** provides both **response stubs** and **fault simulation** (delays, failures) in one lightweight tool
- Quarkus is a well-documented cloud-native pattern
- **REST Assured** is the idiomatic Quarkus acceptance test tool, works reliably on Java 21+
- Fast startup time and low memory footprint benefit both developer experience and CI pipelines

### Negative

- **Spring ecosystem familiarity gap**: Teams with Spring-only background need to learn CDI, Jakarta REST annotations, and MicroProfile REST Client configuration
- Transient upstream failures are **not** retried (by design); operators rely on timeouts and the count-0 fallback
- Higher memory use on developer laptops when running full integration suite with containers

### Neutral

- **Java version:** 26 for this example module; the repo default remains Java 25 (`AGENTS.md`) unless you change the sample `pom.xml`
- Local runs may use Testcontainers reuse / Ryuk as per team policy
- Quarkus Dev Services can auto-provision external dependencies in dev mode

## Follow-up Actions

1. Add Maven BOM entry for Quarkus platform (`io.quarkus.platform:quarkus-bom`) to centralize extension versions
2. Add `quarkus-rest-client` + `quarkus-rest-client-jackson` extensions and configure per-client timeout properties in `application.properties` (note: `quarkus-rest-client-reactive` is no longer available in BOM 3.34.x)
3. Set up **WireMock file structure** with `src/test/resources/wiremock/mappings/` and `src/test/resources/wiremock/__files/` directories
4. Create **stub response files** for the Greek gods list (`greek-gods-response.json`) and individual Wikipedia pages (HTML stubs per god name)
5. Implement a **test support** class (or `QuarkusTestResourceLifecycleManager`) that **starts** WireMock server, **injects** REST Client base URL overrides via `%test` profile, and **resets stubs** between tests
6. Register **MicroProfile REST Client** interfaces — one for the Greek Gods API, one for Wikipedia — with production connect/read timeouts; override URLs only via `%test.` prefix in `application.properties`
7. Keep the **REST Assured**-based acceptance suite small and Gherkin-aligned; tag JUnit tests with **`@Tag("acceptance-test")`** or **`@Tag("integration-test")`** to match [US-001-Greek-Gods-Wikipedia-Information.feature](US-001-Greek-Gods-Wikipedia-Information.feature)
8. Document WireMock setup, file structure, and stub reset patterns in module README

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-002: God Analysis API Non-Functional Requirements](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)
- [US-001: Greek Gods Wikipedia Information](US-001-Greek-Gods-Wikipedia-Information.md)
- [Feature Specification](US-001-Greek-Gods-Wikipedia-Information.feature)
- [Quarkus 3.x Documentation](https://quarkus.io/guides/)
- [Quarkus REST Client Guide](https://quarkus.io/guides/rest-client)
- [MicroProfile REST Client Specification](https://download.eclipse.org/microprofile/microprofile-rest-client-4.0/microprofile-rest-client-spec-4.0.html)
- [Quarkus Testing Guide](https://quarkus.io/guides/getting-started-testing)
- [REST Assured](https://rest-assured.io/) — HTTP-level acceptance tests within `@QuarkusTest`
- [WireMock](https://wiremock.org/) — deterministic stub server with delay simulation
- [Quarkus Virtual Threads Guide](https://quarkus.io/guides/virtual-threads)

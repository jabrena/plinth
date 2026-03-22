# ADR-003: God Analysis API — Technology Stack

**Status:** Accepted
**Date:** Sat Mar 21 10:12:00 CET 2026
**Decision:** Adopt a **technology stack** for the God Analysis API covering **runtime framework** (Spring Boot), **outbound HTTP** (`RestClient`), **retry policy implementation** (**Resilience4j Retry**), **acceptance-style testing** (**Spring `RestClient`** against `@SpringBootTest(webEnvironment = RANDOM_PORT)`), and **integration testing** with **WireMock in-process** so timeout and retry scenarios run in **isolation** with deterministic delay simulation.

**Amendment (2026-03-22):** **Rest Assured** was superseded for **HTTP-level acceptance tests** by **Spring Framework `RestClient`**. Rationale: Rest Assured relies on Groovy internals (`RequestSpecificationImpl.applyProxySettings`); on **Java 21+** this path can throw `NullPointerException` (`ConcurrentHashMap` does not permit null keys) during proxy/meta-property handling—making the stack brittle on current LTS/JDK feature releases. **`RestClient`** is already on the classpath (`spring-web`), matches the **same client API** used for outbound calls, and works with **AssertJ** for assertions—no separate acceptance-test HTTP stack required.

## Context

The God Analysis API project requires a robust Java framework to implement a REST API that integrates with multiple external mythology data sources, performs complex data transformations, and provides reliable service delivery despite external dependency failures.

Timeout and retry behavior ([ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)) is easy to get wrong if tests share static stubs, global delays, or JVM-wide clock tricks. **WireMock in-process** with **scenario-based testing** and **per-test stub reset** keeps each scenario reproducible and **isolated**.

### Key Requirements Driving Framework Selection

1. **REST API Development**: Need to expose a single endpoint (`GET /api/v1/gods/stats/sum`) with query parameter handling
2. **External HTTP Integration**: Must consume three external god APIs with timeout and retry capabilities
3. **Parallel Processing**: Requires concurrent calls to multiple external services for optimal performance
4. **Error Handling & Resilience**: Graceful degradation with partial results when external sources fail
5. **JSON Processing**: Handle JSON serialization/deserialization for API responses and external data
6. **Testing Support**: Comprehensive testing capabilities for acceptance, integration, and unit tests—with **deterministic** timeout/retry coverage using WireMock delay simulation
7. **Development Velocity**: Team expertise and rapid development requirements
8. **Production Readiness**: Monitoring, health checks, and operational capabilities

### Team Context

- Existing Java expertise within the development team
- Familiarity with Spring ecosystem and conventions
- Need for rapid prototyping and development
- Educational/research use case requiring stable, well-documented framework

### Package Structure Requirements

**Decision:** Use `info.jab.ms` as the base package for the God Analysis API implementation.

**Rationale:** Standardized package naming convention aligns with organizational standards and provides clear namespace separation.

### Relationship to ADR-002

[ADR-002](ADR-002-God-Analysis-API-Non-Functional-Requirements.md) specifies **per-attempt timeout (5s)**, **up to 3 additional retries per source**, **linear spacing (no exponential backoff)**, and **parallel calls**. **Resilience4j Retry** implements the retry policy with configuration (max attempts, fixed wait) matching ADR-002 **per source**.

## Decision Drivers

- **Development Speed**: Framework must enable rapid API development
- **Team Expertise**: Leverage existing Spring/Java knowledge
- **REST API Maturity**: Proven patterns for REST endpoint development
- **HTTP Client Integration**: Built-in or well-integrated HTTP client capabilities
- **Testing Ecosystem**: Comprehensive testing support including mocking external services **and simulating latency/timeouts with WireMock in-process delays**
- **Operational Readiness**: Production monitoring and health check capabilities
- **Community Support**: Active community and extensive documentation
- **Dependency Management**: Mature ecosystem with curated dependencies
- **Requirement traceability**: Retry and timeout behavior must remain visible and testable (see implementation plan)
- **Test isolation**: Timeout/retry tests must not depend on execution order or shared mutable stub state

## Considered Options (Runtime Platform)

### Option A: Spring Boot 4.0.4 with Spring MVC (SELECTED)

**Architecture Decision: Traditional Servlet Stack (Spring MVC) - No Reactive Dependencies**

This implementation uses **Spring MVC (traditional servlet-based)** architecture exclusively. **No reactive programming dependencies** (Spring WebFlux, Project Reactor, etc.) will be included.

**Pros:**

- **Mature REST Support**: Spring Web MVC provides comprehensive REST API development with `@RestController`, automatic JSON serialization, and query parameter binding
- **Synchronous HTTP Client**: `RestClient` (synchronous) aligns with servlet-based architecture - **no reactive WebClient dependency**
- **Parallel Processing**: `CompletableFuture` with virtual threads for concurrent processing within traditional thread-per-request model
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

**Context:** The service performs **blocking** parallel fetches (e.g. `CompletableFuture.supplyAsync` with virtual threads around per-source calls) with **per-attempt timeouts**. This aligns with the **Spring MVC servlet-based architecture** decision. Spring Framework 6.1+ provides **RestClient**, a synchronous API designed as the modern successor to `RestTemplate`.

**Reactive Programming Exclusion:** This implementation explicitly **excludes WebClient and reactive dependencies** to maintain consistency with the Spring MVC servlet-based approach.

| Option | Assessment |
|--------|------------|
| **RestClient** | **Selected.** Synchronous API matching servlet stack; fluent API; integrates with `ClientHttpRequestFactory` / `JdkClientHttpRequestFactory` for connect/read timeouts; composes cleanly with **Resilience4j** `Retry` around each per-source call; testable with `MockRestServiceServer` or WireMock-backed URLs. **No reactive dependencies required.** |
| **WebClient** | **Explicitly rejected.** Requires reactive dependencies (spring-webflux, reactor-core) that conflict with our servlet-only architecture decision. |
| **RestTemplate** | **Not recommended** for new code (maintenance mode). |

**Decision:** Use **RestClient** for calls to Greek, Roman, and Nordic sources.

### Reactive Programming Exclusion (EXPLICIT)

**Decision:** This implementation **explicitly excludes all reactive programming** dependencies and patterns.

**Rationale:**
- **Simplicity**: Traditional servlet model is well-understood and sufficient for this use case
- **Team Expertise**: Existing knowledge of Spring MVC reduces learning curve
- **Dependency Management**: Avoids potential conflicts between servlet and reactive stacks
- **Testing**: MockMvc and servlet-based testing tools are mature and well-documented
- **Performance**: Thread-per-request model with `CompletableFuture` and virtual threads parallelism meets performance requirements

**Excluded Dependencies:**
- `spring-boot-starter-webflux`
- `spring-webflux`
- `reactor-core`
- `reactor-netty`
- Any reactive streams implementations

**Excluded Patterns:**
- Reactive streams (`Mono`, `Flux`)
- Non-blocking I/O patterns
- Reactive HTTP clients (`WebClient`)
- Reactive database drivers
- Structured concurrency (not required for this use case)

### Virtual Threads (INCLUDED)

**Decision:** Use **virtual threads** with `CompletableFuture` for parallel processing of external API calls.

**Rationale:**
- **Lightweight Concurrency**: Virtual threads provide efficient concurrent execution without the overhead of platform threads
- **Simplified Model**: Works seamlessly with existing `CompletableFuture` patterns and blocking I/O operations
- **Resource Efficiency**: Allows handling many concurrent API calls without thread pool exhaustion
- **Compatibility**: Integrates well with Spring MVC servlet-based architecture and `RestClient`

**Implementation Approach:**
- Configure `CompletableFuture.supplyAsync()` to use virtual thread executor for parallel god API fetches
- Maintain blocking I/O model with `RestClient` while benefiting from virtual thread scalability
- **No structured concurrency**: Standard `CompletableFuture` composition is sufficient for this use case

**Excluded:**
- Structured concurrency APIs (not required for simple parallel fetch patterns)

### 2. Acceptance tests: Spring `RestClient` (SELECTED)

**Context:** Acceptance tests should assert behavior aligned with [US-001_god_analysis_api.feature](US-001_god_analysis_api.feature).

| Option | Assessment |
|--------|------------|
| **MockMvc** | Fast for **slice** / controller validation tests; less like a real TCP client. **Retained** for `GodStatsControllerValidationTest`-style tests. |
| **Rest Assured** | **Rejected for AT** (superseded 2026-03-22). Black-box HTTP + fluent assertions, but **Groovy-based** implementation is **fragile on Java 21+** (see amendment in ADR header). Adds a **test-only** HTTP stack duplicating what Spring already provides. |
| **Spring `RestClient`** | **Selected for HTTP-level acceptance tests** over `@SpringBootTest(webEnvironment = RANDOM_PORT)`. Build `RestClient.create("http://localhost:" + port)`; use `.get().uri(...).retrieve().toEntity(Dto.class)` for real socket-level calls; assert status and body with **AssertJ**. Aligns acceptance tests with **production outbound HTTP** choice (`RestClient`) and avoids Groovy on the test classpath for AT. |

**Decision:** Use **Spring Framework `RestClient`** for **HTTP-level acceptance tests** tagged `@Tag("acceptance-test")`; retain **MockMvc** where slice tests suffice. **Do not** add or rely on **Rest Assured** for this module’s acceptance suite.

### 3. Retries: Resilience4j vs Spring Retry vs manual (SELECTED: Resilience4j Retry for v1)

**Context:** ADR-002 requires **linear retries**, **per-source isolation**, and **up to 3 retries after the first attempt** (4 total calls per source when retries fire).

| Option | Assessment |
|--------|------------|
| **Resilience4j (`resilience4j-retry`)** | **Selected.** Declarative or programmatic **Retry** with fixed-interval waits matches ADR-002 linear policy; **Micrometer** hooks can expose retry counts; **per-source** `Retry` instances (e.g. named `greek`, `roman`, `nordic`) preserve isolation. |
| **Spring Retry (`@Retryable`)** | Rejected as primary: AOP-based, overlaps conceptually with Resilience4j; team standard is Resilience4j for resilience concerns. |
| **Manual retry loop** | Rejected as primary: clear but duplicates policy logic and metrics that Resilience4j centralizes; acceptable only in spike code, not as the shipped pattern. |

**Decision:** Use **Resilience4j Retry** to implement ADR-002. Configure **max attempts = 4** (1 initial + 3 retries) and **fixed wait** between attempts (no exponential backoff). Apply **one `Retry` configuration per pantheon** so retries on Nordic do not affect Greek/Roman. **Do not** enable Resilience4j **CircuitBreaker** (or other modules) until ADR-002’s “circuit breaker deferred” posture changes.

**Implementation note:** Wrap each per-source `RestClient` call with the appropriate `Retry` (e.g. `Retry.decorateCallable` / registry-backed `Retry.executeCallable`) so **timeouts** still apply **per attempt** inside the retry loop, consistent with ADR-002’s “per-attempt timeout.”

### 4. Integration testing: WireMock (SELECTED)

**Problem:** In-process WireMock with fixed delays can work for simple cases, but **timeout and retry** scenarios need:

- **Deterministic delay simulation** to trigger client-side timeouts consistently
- **Per-scenario control** of latency and failure without cross-test leakage
- **Isolation** so test A’s “slow Nordic” does not affect test B

**Approach:** Use **WireMock in-process** to serve deterministic JSON for the three god list endpoints. WireMock provides both **response stubs** and **delay/failure simulation** capabilities so each test can:

1. **Reset** WireMock stubs and scenarios in `@BeforeEach` / `@AfterEach` (or equivalent) for **isolation**
2. Apply **delay only to the Nordic endpoint** using `fixedDelayMilliseconds` to exercise **5s timeout + partial results**
3. After retries are implemented, use **WireMock scenarios** to simulate **transient failures** followed by success to force **retry behavior** on one source only

**Alternatives considered:**


| Approach                                      | Why not primary                                                                                                                                                                         |
| --------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| WireMock in-process | **Selected approach** - Provides **delay simulation** via `fixedDelayMilliseconds`, **scenario-based retry testing**, and **per-test isolation** through stub reset in `@BeforeEach` |
| WireMock with Testcontainers | Adds Docker dependency and complexity without significant benefit for client timeout testing; in-process delays are sufficient to trigger RestClient timeouts |
| Live Typicode                                 | Flaky, non-deterministic sums and latency                                                                                                                                               |


**Decision:** Use **WireMock in-process** for integration testing. **Every** integration/acceptance test that depends on timeout or retry policy must **start from a clean stub state** (no shared stubs across tests) using `@BeforeEach` lifecycle hooks. Unit tests for pure algorithms remain **without** WireMock.

**Implementation notes (non-normative):**

- Use **JUnit 5** lifecycle hooks to **reset all WireMock stubs** before each test method using `wireMockServer.resetAll()`.
- Configure **separate WireMock endpoints per pantheon** (greek, roman, nordic) so failures are **isolated** per ADR-002.
- **Parallel test execution:** If Surefire runs classes in parallel, use **separate WireMock server instances per test class** or **synchronized** static server pattern with **per-class** dynamic ports.

**WireMock File Structure and Mappings:**

WireMock supports two approaches for organizing test data:

1. **Programmatic Stubs** (recommended for dynamic scenarios):
   ```java
   wireMockServer.stubFor(get(urlEqualTo("/greek/gods"))
       .willReturn(aResponse()
           .withStatus(200)
           .withHeader("Content-Type", "application/json")
           .withBodyFile("greek-gods-response.json")
           .withFixedDelay(2000))); // 2s delay for timeout testing
   ```

2. **File-based Mappings** (useful for static scenarios):
   - **Mappings directory**: `src/test/resources/mappings/` - Contains JSON mapping definitions
   - **Files directory**: `src/test/resources/__files/` - Contains response body files
   
   Example mapping file (`src/test/resources/mappings/greek-gods.json`):
   ```json
   {
     "request": {
       "method": "GET",
       "url": "/greek/gods"
     },
     "response": {
       "status": 200,
       "headers": {
         "Content-Type": "application/json"
       },
       "bodyFileName": "greek-gods-response.json",
       "fixedDelayMilliseconds": 2000
     }
   }
   ```
   
   Response body file (`src/test/resources/__files/greek-gods-response.json`):
   ```json
   [
     {"name": "Zeus", "power": 95},
     {"name": "Athena", "power": 88},
     {"name": "Poseidon", "power": 92}
   ]
   ```

**Recommended Structure for God Analysis API:**
```
src/test/resources/
├── mappings/
│   ├── greek-gods-success.json
│   ├── roman-gods-success.json
│   ├── nordic-gods-success.json
│   ├── greek-gods-timeout.json      # 6s delay to trigger 5s timeout
│   └── nordic-gods-retry.json       # Scenario-based for retry testing
└── __files/
    ├── greek-gods-response.json
    ├── roman-gods-response.json
    ├── nordic-gods-response.json
    ├── greek-gods-partial.json      # Smaller dataset for partial results
    └── empty-response.json          # For failure scenarios
```

**Benefits of `__files` + mappings approach:**
- **Separation of concerns**: Mapping logic separate from response data
- **Reusable responses**: Same JSON file can be used across multiple test scenarios
- **Version control friendly**: JSON files are easy to review and maintain
- **Test data management**: Clear organization of test fixtures
- **Scenario flexibility**: Easy to create variations (success, timeout, retry) using same base data

## Decision Outcome

**Chosen platform: Spring Boot 4.0.4 with Spring MVC and Java 26** (align module `java.version` with repo standard if not 26).

**Architecture: Traditional Servlet Stack Only** - No reactive programming dependencies.

### Rationale

1. **Spring MVC** provides mature servlet-based REST API development without reactive complexity
2. **RestClient** fits synchronous parallel fetches with per-attempt timeouts (no reactive dependencies)
3. **Spring `RestClient`** gives **black-box** acceptance tests over a real port without a separate Groovy-based HTTP DSL
4. **WireMock in-process** provides **isolated**, **deterministic** timeout/retry validation aligned with ADR-002
5. **Resilience4j Retry** implements ADR-002 policy consistently, supports observability, and avoids ad-hoc loops while **other Resilience4j modules stay off** until requirements change
6. **Servlet-only architecture** eliminates reactive programming complexity and dependency conflicts

### Implementation Approach

- **Architecture**: **Spring MVC servlet-based** - traditional thread-per-request model, **no reactive programming**
- **REST Controller**: `@RestController`, `GET /api/v1/gods/stats/sum`
- **HTTP Client**: **RestClient** (synchronous) - **5s timeout per attempt** (each attempt inside retry is still bound by this timeout)
- **Parallelism**: `CompletableFuture` with virtual threads within servlet thread model per source; **retry boundary per source** (separate `Retry` instance or configuration per pantheon)
- **Retries**: **Resilience4j Retry**—**4 max attempts**, **fixed delay** between attempts
- **Configuration**: **Single default configuration** provides production-ready settings with environment variable overrides
- **Error handling**: `@ControllerAdvice` where needed; partial aggregation per feature
- **Dependencies**: **No reactive libraries** (spring-webflux, reactor-core, etc.)
- **Testing**:
  - **Unit tests**: Unicode/filter/sum without containers
  - **Acceptance**: `@SpringBootTest(RANDOM_PORT)` + **Spring `RestClient`** (real HTTP to `localhost:{port}`; AssertJ assertions)
  - **Integration (timeout/retry/isolation)**: **WireMock in-process**, **reset stubs per test**

### Planned Maven coordinates (design-level)

| Scope | Artifact / integration | Role |
| ----- | ---------------------- | ---- |
| main | `spring-boot-starter-web` | **Spring MVC** REST API (servlet-based, **excludes reactive**) |
| main | `spring-boot-starter-actuator` | Ops |
| main | `io.github.resilience4j:resilience4j-retry` | ADR-002 retry policy (linear, per source) |
| main | `io.github.resilience4j:resilience4j-spring-boot3` (or artifact aligned with Spring Boot 4) | Spring configuration for Retry beans and properties |
| test | `spring-boot-starter-test` | JUnit 5, AssertJ, **MockMvc** (servlet-based testing) |
| test | *(none extra for AT)* | **Acceptance tests** use **`RestClient`** from **`spring-web`** (already pulled by `spring-boot-starter-web`)—no `rest-assured` dependency |
| test | `org.wiremock:wiremock` | Deterministic upstream JSON with delay simulation |

**Not used for v1 (by this ADR):** 
- `spring-retry` (prefer Resilience4j)
- Broad Resilience4j modules beyond **retry** (unless a future ADR adds them)
- **Reactive dependencies**: `spring-boot-starter-webflux`, `reactor-core`, `reactor-netty` (conflicts with servlet-only architecture)

## Consequences

### Positive

- Timeout/retry behavior validated with **deterministic delay simulation** and **per-test isolation**
- **WireMock in-process** provides both **response stubs** and **fault simulation** (delays, failures) in one lightweight tool
- Spring stack + Testcontainers is a well-documented industry pattern
- **Resilience4j Retry** gives uniform policy, testable configuration, and optional metrics without enabling circuit breaking in v1
- **Single configuration file** provides consistent baseline with environment variable flexibility

### Negative

- **Fast test execution** - no Docker overhead, pure JVM testing
- **Simple setup** - no external dependencies or container lifecycle management
- Higher memory use on developer laptops when running full integration suite
- **Resilience4j** adds dependencies and configuration surface; team must **discipline** configuration so only **Retry** is active for this service in v1

### Neutral

- Java version should match org standard (25 vs 26)
- Local runs may use Testcontainers reuse / Ryuk as per team policy

## Follow-up Actions

1. Add Maven dependencies for **Resilience4j Retry** (+ Spring Boot integration) and WireMock (use `org.wiremock:wiremock` - the modern groupId that replaced `com.github.tomakehurst`)
2. Set up **WireMock file structure** with `src/test/resources/mappings/` and `src/test/resources/__files/` directories for organized test data management
3. Create **JSON response files** in `__files` for each pantheon (greek, roman, nordic) with realistic god data for testing
4. Implement a **test support** class (or extension) that **starts** WireMock server, **wires** `RestClient` base URLs, and **resets stubs** between tests
5. Register **RestClient** with production timeouts; define **per-source** `Retry` beans or config; override URLs only in tests
6. Keep the **`RestClient`-based** acceptance suite small and Gherkin-aligned; **do not** introduce Rest Assured for this module unless a future ADR revisits the trade-off after Groovy/JVM fixes land upstream
7. Document WireMock setup, file structure, and stub reset patterns in module README
8. Verify Resilience4j Retry configuration matches ADR-002 requirements

## Appendix: Rest Assured — issues observed and why it is discarded (2026-03-22)

This appendix records **evidence from this module** so future readers know why **Rest Assured is not** a supported choice for HTTP-level acceptance tests here, even though it remains a common industry tool.

### Symptoms observed

- **Every** black-box acceptance test that called `given()…when().get(…)` failed with:
  - `java.lang.NullPointerException: Cannot invoke "Object.hashCode()" because "key" is null`
- Failures occurred **before** response assertions (HTTP call path), so the SUT often logged successful handling—the client side (Rest Assured) was failing, not the controller.

### Representative stack trace (abridged)

Failure originates in Rest Assured’s request pipeline, not in application code:

```
java.lang.NullPointerException: Cannot invoke "Object.hashCode()" because "key" is null
    at java.base/java.util.concurrent.ConcurrentHashMap.get(ConcurrentHashMap.java:951)
    at groovy.lang.MetaClassImpl.getMetaProperty(MetaClassImpl.java:2841)
    at groovy.lang.MetaClassImpl.setProperty(MetaClassImpl.java:2712)
    at org.codehaus.groovy.runtime.ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.java:509)
    at io.restassured.internal.RequestSpecificationImpl.applyProxySettings(RequestSpecificationImpl.groovy:2047)
    …
    at io.restassured.internal.RequestSpecificationImpl.get(RequestSpecificationImpl.groovy:172)
```

### Root cause (technical)

1. **Rest Assured 5.x is implemented in Groovy** (`RequestSpecificationImpl` and related classes).
2. On **send**, Rest Assured invokes **`applyProxySettings`**, which uses **Groovy metaprogramming** (`MetaClassImpl.setProperty` / `getMetaProperty`).
3. That path ends up calling **`ConcurrentHashMap.get`** with a **`null` key** (property name resolution yields `null`). **`ConcurrentHashMap` does not permit `null` keys**; the JVM throws `NullPointerException` when computing `hashCode` for the key.
4. The issue is **not** fixed by “using Java 25 instead of 26” in isolation: it is a **Groovy / Rest Assured internal** interaction with **modern JDKs (Java 21+)** and the way nulls propagate through meta-property lookup—**brittle across JDK updates**.

### Why we discard Rest Assured (instead of mitigating)

| Mitigation idea | Why we reject it for this project |
|-----------------|-------------------------------------|
| Pin an older JDK only for tests | Conflicts with **org standard** (Java 25/26) and CI matrix; hides real breakage. |
| Pin older Groovy / Rest Assured versions | Ongoing **dependency roulette**; security and Boot alignment suffer. |
| Rely on proxy / env workarounds | **Non-deterministic** across laptops and CI; root cause remains in Groovy layer. |
| Wait for upstream fix | **Unbounded**; acceptance tests must run **now** on the chosen stack. |

**Decision:** **Do not** depend on Rest Assured for acceptance tests in this module. Use **Spring `RestClient`** (see §2 above)—same stack as production HTTP client choice, **no Groovy test DSL**, **stable on Java 21+**.

### Optional future re-evaluation

If a future Rest Assured + Groovy release **demonstrably** fixes `applyProxySettings` / meta-property paths on **Java 21+** with Spring Boot **4.x**, a **new ADR** may revisit adding Rest Assured **only** if the team wants the fluent DSL; until then, **`RestClient` + AssertJ** remains the recorded standard.

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-002: God Analysis API Non-Functional Requirements](ADR-002-God-Analysis-API-Non-Functional-Requirements.md)
- [US-001: God Analysis API User Story](US-001_God_Analysis_API.md)
- [Feature Specification](US-001_god_analysis_api.feature)
- [US-001-plan-analysis.plan.md](US-001-plan-analysis.plan.md)
- [Spring Boot 4.0.4 Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Framework — RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient) (outbound HTTP and **acceptance tests** in this ADR)
- [WireMock](https://wiremock.org/) — including Testcontainers integration where applicable
- [Resilience4j Retry](https://resilience4j.readme.io/docs/retry)

# ADR-001: God Analysis API Functional Requirements

**Status:** Proposed
**Date:** Wed Mar 25 00:00:00 CET 2026
**Decisions:** REST API for identifying the Greek god with the most Wikipedia literature by character count
**Primary interface(s):** REST/HTTP API

## Context

A customer has requested a capability for educational and research platforms to identify which Greek god has the most written content on Wikipedia. The system fetches a list of Greek gods from an external API, then for each god fetches its Wikipedia page and calculates the character length of its content. The god(s) with the highest character count are returned as the result.

The primary consumers are educational and research platforms with moderately technical users who require consistent, well-documented API access to Wikipedia-based mythology analysis.

## Functional Requirements

### Core API Operations
- **Primary endpoint:** `GET /api/v1/gods/wikipedia/most-literature`
- **No query parameters required**
- **Response format:** JSON object containing a `gods` array (the god name(s) with the highest character count) and a `characterCount` field (handles ties)

Example response:
```json
{
  "gods": ["Zeus"],
  "characterCount": 45678
}
```

### Data Processing Workflow
1. **Source Integration:** Fetch the list of Greek gods from the external API:
   - Greek API: `https://my-json-server.typicode.com/jabrena/latency-problems/greek`

2. **Wikipedia Fan-out:** For each god name in the retrieved list, fetch its Wikipedia page:
   - Wikipedia: `https://en.wikipedia.org/wiki/{greekGod}`

3. **Character Count Calculation:** For each Wikipedia response, calculate the character length of its page content. If a Wikipedia page cannot be retrieved (error, timeout, or not found), assign a character count of **0** for that god.

4. **Ranking:** Identify the maximum character count across all gods. Return the name(s) of the god(s) that achieved that maximum (list structure accommodates ties).

### Error Handling and Resilience
- **Timeout Management:** Outbound HTTP uses **MicroProfile REST Client** (`@RegisterRestClient`) with connect/read timeouts defined in `application.properties` via `quarkus.rest-client.<config-key>.connect-timeout` / `.read-timeout` (e.g. 5000 ms per call unless overridden by environment variables). This is **client configuration only** — no separate retry library or retry policy is required for this user story.
- **Wikipedia Unavailability:** If a Wikipedia page cannot be retrieved for a given god, its character count is treated as **0** — the service continues processing remaining gods (graceful degradation)
- **Greek Gods API Failure:** Return **HTTP 503** if the Greek gods list cannot be fetched (it is a required input; no fallback exists). **Response body:** empty (no JSON payload; consumers rely on the status code only). This matches the [feature](./US-001-Greek-Gods-Wikipedia-Information.feature) scenario that asserts status **503** and an **empty** response body.

## Technical Decisions

### Language & Framework
**Decision:** Java 26 with Quarkus 3.x
**Rationale:** Leverages team expertise with modern Java features and the Quarkus ecosystem for cloud-native REST API development. Quarkus provides fast startup, low memory footprint, and first-class Jakarta REST and MicroProfile support. **This problem2 example explicitly targets Java 26** (see `implementation/pom.xml` `maven.compiler.release`). The java-cursor-rules repository default toolchain is **Java 25** per root `AGENTS.md`; this sample is intentionally pinned higher for the illustrated requirements stack.

### REST/HTTP API

#### API Design & Architecture
**Decision:** Monolithic REST service with a single focused endpoint
**Rationale:** Simple, targeted use case doesn't warrant microservices complexity.

#### Authentication & Security
**Decision:** Public API with no authentication or rate limiting
**Rationale:** Educational and research use case benefits from open access. No sensitive data handling or commercial concerns requiring access controls.

#### Data & Persistence
**Decision:** No caching, direct calls to external APIs with **MicroProfile REST Client** timeouts configured in `application.properties`
**Rationale:** Simplifies architecture and ensures real-time data access. Bounded waits and the character-count-0 fallback provide enough resilience without caching or retries.

#### Implementation namespace
**Decision:** Base Java package `info.jab.ms` for the sample implementation (see [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) for full package-structure rationale).
**Rationale:** Aligns the executable sample with the documented technology stack.

#### Integration & Infrastructure
**Decision:** Two external HTTP integrations — Greek Gods API (one call) and Wikipedia API (one call per god, parallelised)
**Rationale:** Fan-out over Wikipedia calls is parallelised using `CompletableFuture` with a virtual thread executor. One attempt per Wikipedia call; failures fall back to character count 0.

**Implementation note:** Outbound HTTP and test clients are specified in [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) (**MicroProfile REST Client** only). Non-functional expectations for timeouts and partial results are summarised in [ADR-002-God-Analysis-API-Non-Functional-Requirements.md](ADR-002-God-Analysis-API-Non-Functional-Requirements.md); there is **no** separate retry policy.

#### Testing & Monitoring
**Decision:** Acceptance and integration testing aligned with the feature file; basic monitoring approach
**Rationale:** Test scenarios cover critical paths including Wikipedia unavailability and the character-count-0 fallback. Timeout scenarios use deterministic stub delays (WireMock) rather than real network waits.

**HTTP-level acceptance tests:** Use **REST Assured** against the application started by `@QuarkusTest` (random port auto-configured via `quarkus.http.test-port`), as decided in [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md).

## Alternatives Considered

### Caching Strategy
**Rejected:** Adds complexity without clear benefit for educational use case. Direct API calls ensure data freshness.

### Multiple Pantheons
**Rejected:** Not in scope for this user story — Greek gods only as specified in US-001.

### Authentication Mechanisms
**Rejected:** Educational and research context benefits from open access. No commercial or sensitive data concerns.

### Microservices Architecture
**Rejected:** Single focused use case doesn't justify distributed system complexity.

## Configuration Strategy

**Single Default Profile Approach:**
- All configuration in `application.properties` with production-ready defaults
- External API URLs configurable via environment variables with sensible defaults
- MicroProfile REST Client connect/read timeouts externalized (defaults sufficient for local and CI; overrides via environment variables when needed)
- Test-specific overrides isolated in the `%test` profile within `application.properties` — no separate profile files required

**Rationale:** Simplified configuration management with a single default profile reduces complexity while maintaining operational flexibility through environment variables. Outbound retries are intentionally out of scope.

## Consequences

### Positive Impacts
- **Simplicity:** Straightforward architecture reduces development and maintenance overhead
- **Accessibility:** Public API enables easy integration for educational platforms
- **Resilience:** Character-count-0 fallback ensures the service always returns a result even when some Wikipedia pages are unreachable
- **Testability:** Well-defined scenarios with verifiable inputs enable comprehensive test coverage
- **Configurability:** Single configuration file with environment variable overrides supports multiple deployment scenarios

### Trade-offs
- **Latency:** N Wikipedia calls per request (one per god); latency scales with list size. Parallel execution mitigates this.
- **Accuracy:** If Wikipedia pages are unavailable, the winning god may not be accurate — the 0-count fallback is explicit in the specification
- **Security:** Public access provides no usage tracking or abuse prevention

### Follow-up Work
- Add OpenAPI documentation for the endpoint
- Monitor Wikipedia API reliability and response-time distribution
- Evaluate caching strategies if usage patterns show high repetition
- Consider adding pagination if the god list grows significantly

## References

- [US-001-Greek-Gods-Wikipedia-Information.md](US-001-Greek-Gods-Wikipedia-Information.md) — User story and requirements
- [US-001-Greek-Gods-Wikipedia-Information.feature](US-001-Greek-Gods-Wikipedia-Information.feature) — Acceptance criteria and test scenarios
- External APIs:
  - Greek Gods: `https://my-json-server.typicode.com/jabrena/latency-problems/greek`
  - Wikipedia: `https://en.wikipedia.org/wiki/{greekGod}`

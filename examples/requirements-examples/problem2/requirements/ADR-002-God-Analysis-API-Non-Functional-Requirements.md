# ADR-002: God Analysis API - Non-Functional Requirements

**Status:** Proposed
**Date:** Wed Mar 25 00:00:00 CET 2026
**ISO 25010:2023 Focus:** Reliability (Fault Tolerance, Availability)

## Context

The God Analysis API is a public service that fetches the list of Greek gods from an external API and then performs a parallel fan-out to Wikipedia — one HTTP call per god — to calculate the character length of each god's Wikipedia page content. The god(s) with the highest character count are returned.

The primary quality challenge is ensuring reliable service delivery despite the inherent unreliability of external API dependencies. Both the Greek Gods API (hosted on my-json-server.typicode.com) and the Wikipedia API exhibit variable response times and occasional failures that could impact availability and reliability.

**Scope alignment:** This ADR intentionally does **not** require automatic HTTP retries. Outbound calls use the **MicroProfile REST Client** (`@RegisterRestClient`) with configured connect/read timeouts (see [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md)). That keeps implementation and operations simpler than introducing a retry library and backoff policy.

## Non-Functional Requirements

### Primary Quality Characteristic: Reliability

**Fault Tolerance:**
- If a Wikipedia page cannot be retrieved for a given god (timeout, error, or not found), the service assigns a character count of **0** for that god and continues processing the rest
- The service must return a result even when some or all Wikipedia pages are unreachable (the god(s) with the highest count among available data, which may be 0)
- The Greek Gods API is a required input; if it fails the service returns HTTP **503** with an **empty** response body (no fallback list exists)
- System must gracefully handle individual Wikipedia call failures without cascading to total service failure

**Availability:**
- Service must remain responsive and operational despite external dependency issues
- No single point of failure from Wikipedia call failures — one unavailable page must not block results for the rest
- Predictable behavior for API consumers

**Recoverability (without retries):**
- Isolation per Wikipedia call: one slow or failing call must not block successful retrieval from others beyond bounded waits (timeouts)
- Consistent response format regardless of partial or complete Wikipedia data availability (same JSON shape: `gods` array + `characterCount`)

### Secondary Quality Characteristics

**Performance Efficiency:**
- Parallel execution of Wikipedia calls (fan-out) to minimize overall response time
- Timeout controls on MicroProfile REST Client (connect + read, configured in `application.properties`) to prevent indefinite blocking on any single Wikipedia call
- No retry loops — worst-case latency is bounded by parallel timeouts, not multiplied by retry counts

**Functional Suitability:**
- Correctness of the ranking when some Wikipedia pages are unavailable (character count = 0 is the specified fallback)
- Appropriate handling when all Wikipedia calls fail (still a coherent API outcome: the response lists all gods tied at count 0)

## Technical Decisions

**Timeout Strategy:**
- Configure **MicroProfile REST Client** via `quarkus.rest-client.<config-key>.connect-timeout` and `.read-timeout` in `application.properties` (for example 5000 ms each unless overridden); override URLs in the `%test` profile to point to WireMock
- One HTTP attempt per Wikipedia call; on timeout or transport error, treat that god's character count as 0 and continue with the rest

**Configuration Management:**
- Single default configuration provides production-ready settings
- Environment variables allow runtime customization of URLs and timeout values without profile complexity

**Execution Model:**
- Fetch the Greek gods list (single synchronous call — required)
- Fan-out: parallel Wikipedia calls for every god in the list using `CompletableFuture` with a virtual thread executor
- Wait until each parallel call completes successfully or times out; then aggregate character counts and determine the ranking

**Error Handling:**
- Greek Gods API failure → HTTP **503** with **empty response body** (required input; no partial result is possible; no JSON error payload for US-001)
- Wikipedia call failure or timeout → character count 0 for that god (partial result; consistent `{"gods": [...], "characterCount": N}` contract)
- Consistent JSON response format regardless of data completeness

## Alternatives Considered

**Sequential Wikipedia Calls:**
- Rejected due to poor performance characteristics (additive latency for N gods × per-call timeout)

**Automatic Retries (e.g. SmallRye Fault Tolerance `@Retry`, Resilience4j):**
- **Deferred / out of scope for this user story.** Adds dependency surface, configuration, and test complexity beyond the original problem statement. Bounded MicroProfile REST Client timeouts plus character-count-0 fallback are sufficient for v1.

**Circuit Breaker Pattern:**
- Not implemented in v1 because external APIs are not under our control and temporary failures are expected behavior
- Will be reconsidered if monitoring reveals persistent failure patterns indicating systematic issues

**Caching Strategy:**
- Considered but rejected for initial implementation
- Data freshness requirements make caching less beneficial for this educational use case
- May be evaluated in future iterations based on usage patterns

## Quality Metrics & Success Criteria

**Reliability Metrics:**
- Service availability target: Monitor and maintain high availability despite external dependency issues
- Partial result rate: Track percentage of requests where one or more Wikipedia calls returned count 0
- Wikipedia call success rate: Monitor individual Wikipedia call reliability

**Performance Metrics:**
- Response time distribution: Track P50, P95, P99 response times (bounded by configured timeouts under parallel fetch)
- Timeout occurrence rate: Monitor frequency of per-call timeouts

**Monitoring Approach:**
- Log external API call outcomes (success, timeout, failure) with structured logging
- Track response completeness (full vs. partial Wikipedia data) via application logs where useful
- Basic health checks via **SmallRye Health** (`/q/health`, `/q/health/ready`, `/q/health/live`) provided by the `quarkus-smallrye-health` extension
- Logging-based monitoring is sufficient for this User Story scope (educational/research API)
- Advanced observability stacks (Prometheus, Grafana, ELK) are not required for initial implementation

## Consequences

**Positive:**
- Robust service that continues operating despite Wikipedia call failures
- Predictable behavior for API consumers — same JSON shape regardless of Wikipedia availability
- Isolated failure handling prevents cascading issues
- Parallel fan-out minimizes response time impact
- Simpler implementation and tests than retry + backoff policies

**Trade-offs:**
- Transient Wikipedia failures are not automatically retried — may increase the rate of 0-count results for affected gods
- If many Wikipedia calls fail simultaneously, the ranking may not reflect true literary prominence

**Follow-up Work:**
- Evaluate caching strategies based on usage patterns and performance requirements
- Revisit automatic retries only if product scope explicitly requires them
- Establish SLA agreements with external API providers if possible

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-003: God Analysis API — Technology Stack](ADR-003-God-Analysis-API-Technology-Stack.md) — MicroProfile REST Client timeouts and testing
- [US-001: Greek Gods Wikipedia Information](US-001-Greek-Gods-Wikipedia-Information.md)
- [Feature Specification](US-001-Greek-Gods-Wikipedia-Information.feature)
- ISO/IEC 25010:2023 Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE)

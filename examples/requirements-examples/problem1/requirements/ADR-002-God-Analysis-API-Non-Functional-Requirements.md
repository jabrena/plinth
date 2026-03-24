# ADR-002: God Analysis API - Non-Functional Requirements

**Status:** Proposed
**Date:** Sat Mar 21 09:39:29 CET 2026
**ISO 25010:2023 Focus:** Reliability (Fault Tolerance, Availability)

## Context

The God Analysis API is an internal service that aggregates mythology data from three external APIs (Greek, Roman, and Nordic sources) to provide cross-pantheon analysis capabilities. The service performs data transformation (Unicode character conversion and filtering) and serves other internal services within our system architecture.

The primary quality challenge is ensuring reliable service delivery despite the inherent unreliability of external API dependencies. The external mythology APIs (hosted on my-json-server.typicode.com) exhibit variable response times and occasional failures that could impact the availability and reliability of our internal service.

**Scope alignment:** This ADR intentionally does **not** require automatic HTTP retries. Outbound calls use Spring `RestClient` with configured connect/read timeouts (see [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md)). That keeps implementation and operations simpler than introducing a retry library and backoff policy.

## Non-Functional Requirements

### Primary Quality Characteristic: Reliability

**Fault Tolerance:**
- The API must continue operating and return meaningful results when one or more external data sources fail or time out on the **single** outbound attempt per source
- Partial results are acceptable and valuable to consuming services when complete data is unavailable
- System must gracefully handle individual source failures without cascading to total service failure

**Availability:**
- Service must remain responsive and operational despite external dependency issues
- No single point of failure from external API dependencies
- Predictable behavior for internal service consumers

**Recoverability (without retries):**
- Isolation per source: one slow or failing source must not block successful retrieval from others beyond bounded waits (timeouts)
- Consistent response format regardless of partial or complete data availability (same JSON shape; completeness may be inferred from logs if needed)

### Secondary Quality Characteristics

**Performance Efficiency:**
- Parallel execution to minimize overall response time
- Timeout controls on `RestClient` (connect + read) to prevent indefinite blocking
- No retry loops—worst-case latency is bounded by parallel timeouts, not multiplied by retry counts

**Functional Suitability:**
- Correctness of partial results when some sources fail or time out
- Appropriate handling when all selected sources fail (still a coherent API outcome as defined in the feature file)

## Technical Decisions

**Timeout Strategy:**
- Configure `RestClient` (via `ClientHttpRequestFactory` or Spring Boot–supported properties) with explicit **connect** and **read** timeouts; document defaults in `application.yml` (for example 5000 ms each unless overridden)
- One HTTP attempt per source; on timeout or transport error, treat that source as absent for aggregation and continue with others

**Configuration Management:**
- Single default configuration provides production-ready settings
- Environment variables allow runtime customization of URLs and timeout values without profile complexity

**Execution Model:**
- Parallel calls to all three external APIs when selected (Greek, Roman, Nordic)
- Wait until each parallel call completes successfully or times out; then merge lists and compute the aggregate

**Error Handling:**
- Graceful degradation with partial results when some sources fail or time out
- Consistent JSON response format regardless of data completeness (public contract: `sum` field; optional structured logging for diagnostics)

## Alternatives Considered

**Sequential API Calls:**
- Rejected due to poor performance characteristics (additive latency across sources)

**Automatic Retries (e.g. Resilience4j, Spring Retry):**
- **Deferred / out of scope for this user story.** Adds dependency surface, configuration, and test complexity beyond the original problem statement. Bounded `RestClient` timeouts plus partial aggregation are sufficient for v1.

**Circuit Breaker Pattern:**
- Not implemented in v1 because external APIs are not under our control and temporary failures are expected behavior
- Will be reconsidered if monitoring reveals persistent failure patterns indicating systematic issues

**Caching Strategy:**
- Considered but rejected for initial implementation
- Data freshness requirements and filtering complexity make caching less beneficial
- May be evaluated in future iterations based on usage patterns

## Quality Metrics & Success Criteria

**Reliability Metrics:**
- Service availability target: Monitor and maintain high availability despite external dependency issues
- Partial result rate: Track percentage of requests returning partial vs. complete data
- External source success rate: Monitor individual source reliability patterns

**Performance Metrics:**
- Response time distribution: Track P50, P95, P99 response times (bounded by configured timeouts under parallel fetch)
- Timeout occurrence rate: Monitor frequency of per-source timeouts

**Monitoring Approach:**
- Log external API call outcomes (success, timeout, failure) with structured logging
- Track response completeness (full vs. partial results) via application logs where useful
- Basic health checks via Spring Boot Actuator endpoints
- Logging-based monitoring is sufficient for this User Story scope (educational/research API)
- Advanced observability stacks (Prometheus, Grafana, ELK) are not required for initial implementation

## Consequences

**Positive:**
- Robust service that continues operating despite external dependency failures
- Predictable behavior for internal service consumers
- Isolated failure handling prevents cascading issues
- Parallel execution minimizes response time impact
- Simpler implementation and tests than retry + backoff policies

**Trade-offs:**
- Transient network blips are not automatically retried—may increase partial-result rate compared to a retry-enabled design
- Partial results require consuming services to tolerate variable logical completeness (still one consistent JSON contract)

**Follow-up Work:**
- Evaluate caching strategies based on usage patterns and performance requirements
- Establish SLA agreements with external API providers if possible
- Revisit automatic retries only if product scope explicitly requires them

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-003: God Analysis API — Technology Stack](ADR-003-God-Analysis-API-Technology-Stack.md) — `RestClient` timeouts and testing
- [US-001: God Analysis API User Story](US-001_God_Analysis_API.md)
- [Feature Specification](US-001_god_analysis_api.feature)
- ISO/IEC 25010:2023 Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE)

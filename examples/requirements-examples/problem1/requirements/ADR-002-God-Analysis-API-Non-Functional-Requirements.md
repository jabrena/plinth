# ADR-002: God Analysis API - Non-Functional Requirements

**Status:** Proposed
**Date:** Sat Mar 21 09:39:29 CET 2026
**ISO 25010:2023 Focus:** Reliability (Fault Tolerance, Availability, Recoverability)

## Context

The God Analysis API is an internal service that aggregates mythology data from three external APIs (Greek, Roman, and Nordic sources) to provide cross-pantheon analysis capabilities. The service performs complex data transformation (Unicode character conversion and filtering) and serves other internal services within our system architecture.

The primary quality challenge is ensuring reliable service delivery despite the inherent unreliability of external API dependencies. The external mythology APIs (hosted on my-json-server.typicode.com) exhibit variable response times and occasional failures that could impact the availability and reliability of our internal service.

## Non-Functional Requirements

### Primary Quality Characteristic: Reliability

**Fault Tolerance:**
- The API must continue operating and return meaningful results even when one or more external data sources fail or timeout
- Partial results are acceptable and valuable to consuming services when complete data is unavailable
- System must gracefully handle individual source failures without cascading to total service failure

**Availability:**
- Service must remain responsive and operational despite external dependency issues
- No single point of failure from external API dependencies
- Predictable behavior for internal service consumers

**Recoverability:**
- Automatic retry mechanisms for transient failures
- Individual source failure isolation - one failing source should not impact retrieval from other sources
- Consistent response format regardless of partial or complete data availability

### Secondary Quality Characteristics

**Performance Efficiency:**
- Parallel execution to minimize overall response time
- Timeout controls to prevent indefinite blocking
- Resource-efficient retry strategies

**Functional Suitability:**
- Completeness priority - wait for all sources to either succeed or exhaust retries
- Correctness of partial results when some sources fail
- Appropriate response indicating data completeness status

## Technical Decisions

**Timeout Strategy:**
- 5-second timeout per individual API call to external sources
- Prevents indefinite blocking while allowing reasonable response time for external services

**Retry Policy:**
- Up to 3 additional retry attempts per external source (4 total attempts: 1 initial + 3 retries)
- Linear retry approach without exponential backoff (keeping implementation simple)
- Individual retry logic per source - failures are isolated

**Execution Model:**
- Parallel calls to all three external APIs (Greek, Roman, Nordic)
- Completeness-prioritized approach: wait for all sources to either succeed or exhaust their retry attempts
- Individual timeout and retry handling per source

**Error Handling:**
- Graceful degradation with partial results when some sources fail after all retries
- Consistent JSON response format regardless of data completeness
- Clear indication of which sources contributed to the final result (via application logging using log.info)

## Alternatives Considered

**Sequential API Calls:**
- Rejected due to poor performance characteristics (potential 60+ second response times with retries)
- Would create cascading delays when early sources fail

**Immediate Response on First Success:**
- Rejected in favor of completeness priority
- Would provide inconsistent data quality to consuming services

**Circuit Breaker Pattern:**
- Not implemented in v1 because external APIs are not under our control and temporary failures are expected behavior
- Circuit breakers are designed for protecting downstream services from cascading failures, but our use case expects and handles individual source failures gracefully
- Will be reconsidered if monitoring reveals persistent failure patterns indicating systematic issues rather than transient network problems

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
- Response time distribution: Track P50, P95, P99 response times including retry scenarios
- Timeout occurrence rate: Monitor frequency of 5-second timeouts per source
- Retry effectiveness: Track success rate of retry attempts

**Monitoring Approach:**
- Log all external API call outcomes (success, timeout, failure) with structured logging
- Track response completeness (full vs. partial results) via application logs
- Basic health checks via Spring Boot Actuator endpoints
- Logging-based monitoring is sufficient for this User Story scope (educational/research API)
- Advanced observability stacks (Prometheus, Grafana, ELK) are not required for initial implementation

## Consequences

**Positive:**
- Robust service that continues operating despite external dependency failures
- Predictable behavior for internal service consumers
- Isolated failure handling prevents cascading issues
- Parallel execution minimizes response time impact

**Trade-offs:**
- Increased complexity in error handling and retry logic
- Potential for longer response times in worst-case scenarios (up to ~20 seconds with full retry cycles)
- Resource utilization for parallel calls and retry attempts
- Partial results require consuming services to handle variable data completeness

**Follow-up Work:**
- Evaluate caching strategies based on usage patterns and performance requirements
- Establish SLA agreements with external API providers if possible
- Advanced monitoring and alerting infrastructure may be added in future iterations based on operational requirements

## References

- [ADR-001: God Analysis API Functional Requirements](ADR-001-God-Analysis-API-Functional-Requirements.md)
- [ADR-003: God Analysis API — Technology Stack](ADR-003-God-Analysis-API-Technology-Stack.md) — **Resilience4j Retry** implements this ADR’s retry policy - [US-001: God Analysis API User Story](US-001_God_Analysis_API.md)
- [Feature Specification](US-001_god_analysis_api.feature)
- ISO/IEC 25010:2023 Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE)

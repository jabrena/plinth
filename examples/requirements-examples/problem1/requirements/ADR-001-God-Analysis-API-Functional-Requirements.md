# ADR-001: God Analysis API Functional Requirements

**Status:** Proposed
**Date:** Fri Mar 20 20:23:52 CET 2026
**Decisions:** REST API for cross-pantheon mythology data analysis with Unicode-based name conversion and aggregation
**Primary interface(s):** REST/HTTP API

## Context

A customer has requested a new capability for cross-pantheon mythology data analysis to support educational and research platforms. The system needs to consume multiple external god APIs (Greek, Roman, Nordic), filter gods by name criteria, perform mathematical transformations on the names using Unicode values, and return aggregated results. This addresses the need for reliable, programmatic access to processed mythology data for academic research and educational applications.

The primary consumers are educational and research platforms with moderately technical users who require consistent, well-documented API access to mythology data for analysis and reporting purposes.

## Functional Requirements

### Core API Operations
- **Primary endpoint:** `GET /api/v1/gods/stats/sum`
- **Query parameters:**
  - `filter`: Case-sensitive name filtering (e.g., `filter=n` matches only names starting with lowercase 'n')
  - `sources`: Comma-separated pantheon selection (e.g., `sources=greek,roman,nordic`)
- **Response format:** JSON object containing a `sum` field with the calculated aggregate value

### Data Processing Workflow
1. **Source Integration:** Consume data from three external god APIs:
   - Greek API: `https://my-json-server.typicode.com/jabrena/latency-problems/greek`
   - Roman API: `https://my-json-server.typicode.com/jabrena/latency-problems/roman`
   - Nordic API: `https://my-json-server.typicode.com/jabrena/latency-problems/nordic`

2. **Name Filtering:** Apply case-sensitive filtering based on the first character of god names

3. **Decimal Conversion:** Transform each filtered god name using Unicode-based algorithm:
   - Convert each character to its Unicode integer value
   - Concatenate the integers as strings (e.g., "Zeus" → Z(90)e(101)u(117)s(115) → "90101117115")
   - Sum all converted name values to produce final result

### Error Handling and Resilience
- **Timeout Management:** Outbound HTTP uses Spring `RestClient` with connect/read timeouts defined in `application.yml` (e.g. 5 seconds per call unless overridden by environment variables). This is **client configuration only**—no separate retry library or retry policy is required for this user story.
- **Graceful Degradation:** Continue processing with partial results when individual sources timeout or fail on the single attempt
- **Invalid Parameters:** Return HTTP 400 with error messages for malformed requests (missing parameters, invalid filter length, invalid source names)
- **Source Unavailability:** Process available sources when others are unreachable or time out

## Technical Decisions

### Language & Framework
**Decision:** Java 26 with Spring Boot 4.0.4
**Rationale:** Leverages team expertise with modern Java features and mature Spring ecosystem for REST API development.

### REST/HTTP API

#### API Design & Architecture
**Decision:** Monolithic REST service with single focused endpoint
**Rationale:** Simple, targeted use case doesn't warrant microservices complexity. Single endpoint design aligns with specific customer requirement for god statistics aggregation.

#### Authentication & Security
**Decision:** Public API with no authentication or rate limiting
**Rationale:** Educational and research use case benefits from open access. No sensitive data handling or commercial concerns requiring access controls.

#### Data & Persistence
**Decision:** No caching, direct calls to external APIs with `RestClient` timeouts configured once in application configuration
**Rationale:** Simplifies architecture and ensures real-time data access. Bounded waits and partial aggregation provide enough resilience for this scope without caching or outbound retries.

#### Integration & Infrastructure
**Decision:** Direct HTTP client integration with external god APIs, no specific deployment requirements
**Rationale:** Straightforward integration pattern sufficient for three well-defined external endpoints. Deployment flexibility maintained for various environments.

**Implementation note:** Outbound HTTP and test clients are specified in [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) (**RestClient** only). Non-functional expectations for timeouts and partial results are summarized in [ADR-002-God-Analysis-API-Non-Functional-Requirements.md](ADR-002-God-Analysis-API-Non-Functional-Requirements.md); there is **no** separate retry policy.

#### Testing & Monitoring
**Decision:** Acceptance and integration testing as defined in feature file with fast execution requirements, basic monitoring approach
**Rationale:** Test scenarios cover critical paths including timeout and filtering behavior. Tests must run fast to support rapid development cycles—timeout scenarios use deterministic stub delays (for example WireMock) rather than real network waits. Basic observability sufficient for educational use case.

**HTTP-level acceptance tests:** Use **Spring Framework `RestClient`** against the application bound to a random port (`@SpringBootTest(webEnvironment = RANDOM_PORT)`), as decided in [ADR-003-God-Analysis-API-Technology-Stack.md](ADR-003-God-Analysis-API-Technology-Stack.md) (supersedes Rest Assured for this module to avoid Groovy/JVM compatibility issues on Java 21+).

## Alternatives Considered

### Caching Strategy
**Rejected:** Implementing response caching from external god APIs
**Reason:** Adds complexity without clear benefit for educational use case. Direct API calls ensure data freshness and simplify error handling.

### Authentication Mechanisms
**Rejected:** API key or OAuth-based authentication
**Reason:** Educational and research context benefits from open access. No commercial or sensitive data concerns requiring access controls.

### Microservices Architecture
**Rejected:** Separate services for each pantheon or processing step
**Reason:** Single focused use case doesn't justify distributed system complexity. Monolithic approach provides simpler deployment and maintenance.

## Configuration Strategy

**Single Default Profile Approach:**
- All configuration in `application.yml` with production-ready defaults
- External API URLs configurable via environment variables with sensible defaults
- `RestClient` connect/read timeouts externalized (defaults sufficient for local and CI; overrides via environment variables when needed)
- No profile-specific configuration files - single source of truth

**Rationale:** Simplified configuration management with single default profile reduces complexity while maintaining operational flexibility through environment variables. Outbound retries are intentionally out of scope to avoid extra libraries and policy tuning.

## Consequences

### Positive Impacts
- **Simplicity:** Straightforward architecture reduces development and maintenance overhead
- **Accessibility:** Public API enables easy integration for educational platforms
- **Resilience:** Timeout handling ensures partial results even with external API issues
- **Testability:** Well-defined scenarios enable comprehensive testing coverage
- **Configurability:** Single configuration file with environment variable overrides supports multiple deployment scenarios

### Trade-offs
- **Performance:** No caching may result in slower responses and higher external API load
- **Security:** Public access provides no usage tracking or abuse prevention
- **Scalability:** Monolithic design may require refactoring for high-volume usage

### Follow-up Work
- Implement comprehensive error logging for external API failures
- Consider adding API documentation (OpenAPI/Swagger) for educational platform integration
- Monitor external API reliability and consider fallback strategies if needed
- Evaluate need for basic usage metrics collection

## References

- [US-001_God_Analysis_API.md](US-001_God_Analysis_API.md) - User story and requirements
- [US-001_god_analysis_api.feature](US-001_god_analysis_api.feature) - Acceptance criteria and test scenarios
- External God APIs:
  - Greek: https://my-json-server.typicode.com/jabrena/latency-problems/greek
  - Roman: https://my-json-server.typicode.com/jabrena/latency-problems/roman
  - Nordic: https://my-json-server.typicode.com/jabrena/latency-problems/nordic

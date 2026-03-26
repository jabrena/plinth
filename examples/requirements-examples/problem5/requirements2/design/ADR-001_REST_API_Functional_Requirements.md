# ADR-001: REST API Functional Requirements for Greek Gods Data Synchronization Platform

**Date:** June 3, 2025  
**Status:** Proposed  
**Deciders:** Juan Antonio Breña Moral, Development Team  
**Technical Story:** [EPIC-001](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md), [FEAT-001](../agile/FEAT-001_REST_API_Endpoints_Greek_Gods_Data.md), [US-001](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)

---

## Context and Problem Statement

We need to establish comprehensive functional requirements for a REST API platform that provides reliable access to Greek mythology data through synchronized data retrieval from external sources. The API will serve educational platforms and applications requiring programmatic access to curated deity information.

### Business Context
- **Primary Use Case:** Educational platform enhancement through mythology content integration
- **Target Users:** API consumers/developers, educational platform developers, application integrators
- **Business Value:** Enhanced educational platform integration with reliable, fast access to Greek mythology data

### Technical Problem
Current challenges include:
- Need for reliable access to mythology data without direct dependency on external service availability
- Performance limitations with direct external API calls
- Lack of standardized data format for mythology information
- No guarantee of data availability during peak usage periods

## Decision Drivers

### Business Requirements
- **Performance:** Sub-second response times (< 1 second for 99% of requests)
- **Reliability:** 99.9% uptime independent of external service status
- **Scalability:** Support for 1000+ concurrent requests
- **User Experience:** Simple, intuitive API design for educational developers

### Technical Constraints
- PostgreSQL database for data persistence
- Java/Quarkus technology stack
- OpenAPI 3.0.3 specification compliance
- Integration with external data source: https://my-json-server.typicode.com/jabrena/latency-problems

### Quality Attributes
- **Availability:** 99.9% uptime SLA
- **Performance:** < 1 second response time requirement
- **Maintainability:** Clear documentation and standardized error handling
- **Testability:** Comprehensive test coverage with acceptance criteria

## Functional Requirements

### Core API Capabilities

#### 1. Greek Gods Data Retrieval
**Primary Endpoint:** `GET /api/v1/gods/greek`

**Functional Specification:**
- **Purpose:** Retrieve complete list of all Greek god names
- **Response Format:** Simple JSON array of strings
- **Data Source:** PostgreSQL database with synchronized data
- **Expected Dataset:** 20 Greek god names including Zeus, Hera, Poseidon, Demeter, Ares, Athena, Apollo, Artemis, Hephaestus, Aphrodite, Hermes, Dionysus, Hades, Hypnos, Nike, Janus, Nemesis, Iris, Hecate, Tyche

**Response Structure:**
```json
[
  "Zeus",
  "Hera", 
  "Poseidon",
  "Demeter",
  "Ares",
  "Athena",
  "Apollo",
  "Artemis",
  "Hephaestus",
  "Aphrodite",
  "Hermes",
  "Dionysus",
  "Hades",
  "Hypnos",
  "Nike",
  "Janus",
  "Nemesis",
  "Iris",
  "Hecate",
  "Tyche"
]
```

#### 2. Error Handling and Response Standards

**Success Response:**
- **HTTP Status:** 200 OK
- **Content-Type:** application/json
- **Body:** JSON array of god names

**Error responses (RFC 7807 Problem Details for HTTP APIs):**

For **4xx/5xx** conditions on this API, the response body **MUST** follow **[RFC 7807](https://www.rfc-editor.org/rfc/rfc7807)** (Problem Details for HTTP APIs).

- **Content-Type:** `application/problem+json` (optionally with `charset=UTF-8`)
- **Required JSON members:** `type` (URI reference), `title` (string), `status` (integer, same as HTTP status)
- **Optional but typical:** `detail` (human-readable explanation), `instance` (URI reference for the specific request)

**Illustrative 500 response** (when the database is unavailable; exact `detail`/`instance` may vary by implementation while remaining RFC 7807–compliant):

```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Internal server error",
  "instance": "/api/v1/gods/greek"
}
```

**Acceptance testing (normative subset):** Automated acceptance criteria (see [US-001 Gherkin feature](../agile/US-001_api_greek_gods_data_retrieval.feature)) assert only the **binding** subset: **HTTP 500**, **`Content-Type`** includes **`application/problem+json`**, JSON object body with required members **`type`** (non-empty string), **`title`** (non-empty string), and **`status`** equal to **500**. They **do not** require exact equality of **`detail`**, **`instance`**, or other optional members—those may follow Quarkus **Jakarta REST exception mapping** (e.g. **`ExceptionMapper`** / shared problem-detail DTO) defaults or implementation-specific messages while remaining RFC 7807–compliant. The JSON example above remains **illustrative** for documentation and OpenAPI examples, not a byte-for-byte test oracle.

**Error Scenarios:**
- **Database Unavailable:** 500 Internal Server Error with a Problem Details body satisfying the normative subset above
- **Empty Database:** 200 OK with empty array `[]` (not an error; success payload)
- **Service Unavailable:** 500 Internal Server Error with a Problem Details body satisfying the same normative subset

### Data Requirements

#### Data Persistence
- **Storage:** PostgreSQL database with synchronized Greek god data
- **Data Integrity:** Complete dataset of 20 Greek god names
- **Data Freshness:** Synchronized from external source via background service
- **Data Validation:** No duplicate entries, string validation for god names

#### Data Synchronization
- **Source:** External JSON server (my-json-server.typicode.com)
- **Method:** Background synchronization service
- **Frequency:** Periodic updates to maintain data freshness
- **Fallback:** API continues serving existing data during sync failures

### API Design Standards

#### REST Principles
- **Resource-Oriented:** `/api/v1/gods/greek` follows RESTful naming conventions
- **HTTP Methods:** GET for data retrieval
- **Status Codes:** Standard HTTP status codes (200, 500)
- **Content Type:** `application/json` for success; `application/problem+json` for error responses (RFC 7807)

#### Versioning Strategy
- **Version Approach:** Path-based versioning (`/api/v1/`)
- **Backward Compatibility:** Maintain v1 compatibility during evolution
- **Version Migration:** Clear deprecation strategy for future versions

#### Documentation Requirements
- **OpenAPI Specification:** Complete OpenAPI 3.0.3 specification
- **Interactive Documentation:** Swagger UI for developer testing
- **Code Examples:** Integration examples for common use cases
- **Error Documentation:** RFC 7807 Problem Details documented in OpenAPI and acceptance criteria

### Performance Requirements

#### Response Time
- **Target:** < 1 second for all API endpoints (99th percentile)
- **Measurement:** End-to-end response time from request to response
- **Monitoring:** Continuous performance monitoring and alerting

#### Throughput
- **Concurrent Requests:** Support 1000+ simultaneous requests
- **Load Testing:** Regular load testing to validate capacity
- **Scaling Strategy:** Horizontal scaling capability

#### Availability
- **Uptime Target:** 99.9% availability SLA
- **Downtime Tolerance:** Maximum 8.77 hours annually
- **Monitoring:** Real-time availability monitoring

## Considered Alternatives

### Alternative 1: Direct External API Calls
**Description:** Direct proxy to external JSON server without local persistence

**Pros:**
- Simpler implementation
- Real-time data without synchronization delays
- Minimal infrastructure requirements

**Cons:**
- Dependent on external service availability
- Performance affected by external service latency
- No service guarantees for educational applications
- Risk of 504 Gateway timeouts

**Decision:** Rejected due to reliability and performance requirements

### Alternative 2: GraphQL API
**Description:** GraphQL endpoint instead of REST for more flexible querying

**Pros:**
- Flexible query capabilities
- Single endpoint for multiple data types
- Client-controlled response structure

**Cons:**
- Additional complexity for simple use case
- Learning curve for educational developers
- Overhead for basic god name retrieval
- Not aligned with simple API consumer needs

**Decision:** Rejected in favor of simple REST approach for educational use cases

### Alternative 3: Flat JSON error envelope (`error` + `status` fields only)
**Description:** Non-standard JSON object for failures instead of RFC 7807.

**Pros:** Minimal payload; easy to hand-craft in examples.

**Cons:** Does not follow an IETF standard; weaker interoperability with API gateways, clients, and documentation tooling; diverges from Quarkus/Jakarta REST **problem-detail** conventions at the API boundary.

**Decision:** Rejected in favor of **RFC 7807 Problem Details** (`application/problem+json`) for all documented error responses.

### Alternative 4: Caching-Only Solution
**Description:** Cache layer without persistent database storage

**Pros:**
- Faster response times
- Lower infrastructure costs
- Simpler data management

**Cons:**
- Data loss risk during service restarts
- Limited data consistency guarantees
- No audit trail for data changes
- Insufficient reliability for educational platforms

**Decision:** Rejected due to reliability and data persistence requirements

## Consequences

### Positive Consequences
- **Developer Experience:** Simple, intuitive API design enhances adoption by educational developers
- **Reliability:** Local data persistence ensures consistent availability independent of external services
- **Performance:** Direct database access provides sub-second response times
- **Maintainability:** Standard REST principles and OpenAPI documentation improve long-term maintenance
- **Scalability:** Architecture supports horizontal scaling for increased demand

### Negative Consequences
- **Infrastructure Complexity:** Requires PostgreSQL database and synchronization service
- **Data Latency:** Slight delay between external source updates and API data availability
- **Storage Costs:** Additional database storage requirements
- **Synchronization Overhead:** Background processes required for data consistency

### Risks and Mitigation
- **Risk:** Database performance under high load
  - **Mitigation:** Connection pooling, query optimization, performance monitoring
- **Risk:** Data synchronization failures
  - **Mitigation:** Error handling, alerting, manual sync capabilities
- **Risk:** API backward compatibility during evolution
  - **Mitigation:** API versioning strategy, deprecation notices

## Implementation Guidelines

### Development Approach
1. **Phase 1:** Core endpoint implementation with database connectivity
2. **Phase 2:** Error handling and response standardization
3. **Phase 3:** Performance optimization and monitoring
4. **Phase 4:** Documentation and testing completion

### Testing Strategy
- **Unit Tests:** Individual endpoint logic and error handling
- **Integration Tests:** Database connectivity and end-to-end workflows
- **Performance Tests:** Load testing with 1000+ concurrent requests
- **Acceptance Tests:** Gherkin scenarios from US-001 feature file

### Quality Assurance
- **Code Coverage:** Minimum 90% test coverage
- **Performance Validation:** Automated performance testing in CI/CD
- **Documentation Accuracy:** Automated OpenAPI specification validation
- **Security Review:** API security assessment before production deployment

## Related Decisions

This folder (`requirements2/design`) contains **three** ADRs for the Greek Gods platform. **ADR-001** (this document) captures **functional** requirements; the others are **not** substitutes for a database-schema ADR or a sync-process ADR—those concerns are summarized here and in the implementation, and may be split into future ADRs if needed.

### Other ADRs in this folder

| ADR | File | Purpose |
|-----|------|---------|
| **ADR-002** | [ADR-002-Acceptance-Testing-Strategy.md](./ADR-002-Acceptance-Testing-Strategy.md) | Acceptance and integration testing strategy (`@QuarkusTest`, **REST Assured**, tags, phases). |
| **ADR-003** | [ADR-003-Greek-Gods-API-Technology-Stack.md](./ADR-003-Greek-Gods-API-Technology-Stack.md) | Runtime stack: Quarkus REST (Jakarta REST), JDBC/Flyway, outbound MicroProfile REST Client, scheduling, Testcontainers, optional WireMock. |

There is **no ADR-004** (and no separate “database schema” or “background sync strategy” ADR file) in this tree yet. Topics such as **authentication**, **rate limiting**, or a **standalone persistence ADR** should be added as new numbered ADRs only when an epic requires them.

### Referenced agile and design artifacts

- **Epic:** [EPIC-001 Greek Gods Data Synchronization API Platform](../agile/EPIC-001_Greek_Gods_Data_Synchronization_API_Platform.md)
- **Feature:** [FEAT-001 REST API Endpoints for Greek Gods Data Retrieval](../agile/FEAT-001_REST_API_Endpoints_Greek_Gods_Data.md)
- **User Story:** [US-001 API Greek Gods Data Retrieval](../agile/US-001_API_Greek_Gods_Data_Retrieval.md)
- **Acceptance criteria (Gherkin):** [US-001_api_greek_gods_data_retrieval.feature](../agile/US-001_api_greek_gods_data_retrieval.feature)
- **Public API OpenAPI:** [greekController-oas.yaml](./greekController-oas.yaml)
- **External JSON server OpenAPI:** [my-json-server-oas.yaml](./my-json-server-oas.yaml)
- **Illustrative SQL:** [schema.sql](./schema.sql)
- **Sequence diagram:** [greek_gods_api_sequence_diagram.puml](./greek_gods_api_sequence_diagram.puml), [greek_gods_api_sequence_diagram.png](./greek_gods_api_sequence_diagram.png)

---

**Last Updated:** March 26, 2026  
**Next Review:** September 26, 2026  
**Status:** Ready for Technical Review 
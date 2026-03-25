# God Analysis API

A Quarkus-based REST API that analyzes Greek gods by comparing the character count of their Wikipedia articles to determine which god has the most literature.

## Overview

This API fetches a list of Greek gods from an external API, then retrieves each god's Wikipedia page in parallel to count characters. It returns the god(s) with the highest character count, handling failures gracefully.

## API Endpoints

### Get Most Literature Analysis

```
GET /api/v1/gods/wikipedia/most-literature
```

**Response (200 OK):**
```json
{
  "gods": ["Zeus"],
  "characterCount": 15420
}
```

**Response (503 Service Unavailable):**
- Empty body when the Greek Gods API is unavailable
- No JSON Problem Details per requirements

### Health Check

```
GET /q/health
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "checks": []
}
```

The health endpoint provides basic application health status. Use this for:
- Load balancer health checks
- Container orchestration readiness probes
- Monitoring system health verification

## Business Rules

1. **Greek API Dependency**: If the Greek Gods API fails (non-2xx, timeout, connection error), returns HTTP 503 with empty body
2. **Wikipedia Failures**: Individual Wikipedia page failures are treated as 0 character count and processing continues
3. **Parallel Processing**: Wikipedia pages are fetched concurrently using virtual threads
4. **Tie Handling**: Multiple gods with the same highest count are returned sorted alphabetically
5. **Character Counting**: Uses `String.length()` of the full HTTP response body

## External Dependencies

- **Greek Gods API**: `https://my-json-server.typicode.com/jabrena/latency-problems/greek`
- **Wikipedia**: `https://en.wikipedia.org/wiki/{godName}`

## Configuration

Key configuration properties in `application.properties`:

```properties
# Greek Gods REST Client
quarkus.rest-client.greek-gods-api.url=https://my-json-server.typicode.com
quarkus.rest-client.greek-gods-api.connect-timeout=5000
quarkus.rest-client.greek-gods-api.read-timeout=5000

# Wikipedia REST Client  
quarkus.rest-client.wikipedia-api.url=https://en.wikipedia.org
quarkus.rest-client.wikipedia-api.connect-timeout=5000
quarkus.rest-client.wikipedia-api.read-timeout=5000
```

## Development

### Running in Development Mode

```bash
./mvnw quarkus:dev
```

The application will start on `http://localhost:8080` with live coding enabled.

### Running Tests

```bash
# All tests
./mvnw clean verify

# Unit tests only
./mvnw test

# Integration and acceptance tests
./mvnw failsafe:integration-test
```

### Test Categories

- **Unit Tests** (`*Test.java`): Fast tests with mocked dependencies
- **Integration Tests** (`*IT.java`): Component-level tests with WireMock
- **Acceptance Tests** (`*AT.java`): End-to-end scenarios from Gherkin feature files

## Architecture

```
┌─────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client    │───▶│ GodAnalysisResource │───▶│ GodAnalysisService │
└─────────────┘    └──────────────────┘    └─────────────────┘
                                                      │
                              ┌───────────────────────┼───────────────────────┐
                              │                       │                       │
                              ▼                       ▼                       ▼
                    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
                    │ GreekGodsClient │    │ WikipediaClient │    │ WikipediaClient │
                    └─────────────────┘    └─────────────────┘    └─────────────────┘
                              │                       │                       │
                              ▼                       ▼                       ▼
                    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
                    │   Greek Gods    │    │   Wikipedia     │    │   Wikipedia     │
                    │      API        │    │      API        │    │      API        │
                    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Packaging and Deployment

### Standard JAR

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### Uber JAR

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

### Native Executable

```bash
./mvnw package -Dnative
./target/god-analysis-api-1.0.0-SNAPSHOT-runner
```

## Monitoring and Observability

- **Health Endpoint**: `/q/health` - Application health status
- **Metrics**: Available via Quarkus extensions (add `quarkus-micrometer` if needed)
- **Logging**: Structured logging under `info.jab.ms` category

## Error Handling

- **Greek API Failures**: HTTP 503 with empty body (no retries)
- **Wikipedia Failures**: Individual failures treated as 0 characters, processing continues
- **Timeouts**: Configurable per client, failures handled gracefully
- **No Circuit Breakers**: Single attempt per call as per requirements

## Technology Stack

- **Framework**: Quarkus 3.34.1
- **Java**: 26
- **HTTP Client**: Quarkus REST Client Reactive
- **Testing**: JUnit 5, REST Assured, WireMock, Testcontainers
- **Concurrency**: Virtual Threads (`@RunOnVirtualThread`)
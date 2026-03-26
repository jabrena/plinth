# US-001 Greek Gods API (Quarkus)

`GET /api/v1/gods/greek` returns Greek god names from PostgreSQL (read path never calls the external JSON server). A scheduled job syncs from the upstream `GET /greek` endpoint; base URL is configurable.

## Requirements and design

See `../requirements2/agile/PLAN-US-001_Implementation.plan.md` and artifacts under `../requirements2/`.

## Prerequisites

- Java 25
- Docker (for Testcontainers PostgreSQL in integration tests)

## Configuration

| Key | Purpose |
|-----|---------|
| `GREEK_GODS_UPSTREAM_BASE_URL` | Upstream base URL (default: my-json-server URL from design OAS) |
| `GREEK_GODS_SYNC_EVERY` | Scheduler interval (default `1h`, Quarkus `every` syntax) |
| `quarkus.datasource.*` | PostgreSQL JDBC settings (or rely on Dev Services in dev) |

Runtime OpenAPI: `/q/openapi`, Swagger UI: `/q/swagger-ui`.

## Run

```shell
cd examples/requirements-examples/problem5/implementation2
./mvnw quarkus:dev
```

Dev mode uses Dev Services for PostgreSQL when no JDBC URL is set. Flyway applies `src/main/resources/db/migration`.

## Build and test

```shell
./mvnw clean verify
```

Integration tests (`*IT`) use Testcontainers (`postgres:16-alpine`) and REST Assured. To skip Failsafe integration tests:

```shell
./mvnw clean verify -DskipITs=true
```

## JUnit tags (Gherkin alignment)

Tests use `@Tag` values matching the feature file: `smoke`, `happy-path`, `performance`, `load-testing`, `error-handling`, `data-quality`, `api-specification`. The `@availability` scenario is not automated.

Integration tests run under Failsafe (`*IT`). Tags are listed below. Optional Maven filter (empty `test.groups` runs all ITs):

```shell
./mvnw clean verify -Dtest.groups=smoke
```

`load-testing` asserts 100 concurrent requests complete within 2 seconds wall clock; shared CI runners may need a higher threshold or a nightly profile if flaky.

## API

- **GET** `/api/v1/gods/greek` — `200` + JSON array of strings, ordered by name; empty table → `[]`.
- **500** + `application/problem+json` with `type`, `title`, `status` when the database cannot be read.

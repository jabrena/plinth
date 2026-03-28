# Greek Gods API (US-001) — Micronaut

Micronaut 4.x service: **`GET /api/v1/gods/greek`** returns a JSON array of Greek god names from PostgreSQL (read path never calls the upstream JSON server). Background sync upserts names from a configurable upstream base URL + **`GET /greek`**.

## Run

Prerequisites: JDK 21+, Maven (wrapper included), local PostgreSQL (or override URL via env).

```bash
./mvnw mn:run
```

Default datasource in `src/main/resources/application.yml` targets `jdbc:postgresql://localhost:5432/greek_gods` with user/password `postgres` / `postgres`. Override using Micronaut environment variables, for example:

- `DATASOURCES_DEFAULT_URL`
- `DATASOURCES_DEFAULT_USERNAME`
- `DATASOURCES_DEFAULT_PASSWORD`

Upstream sync (no hard-coded production URL in code):

- `GREEK_GODS_UPSTREAM_BASE_URL` — overrides `greek-gods.upstream.base-url` (Micronaut HTTP client calls `{base-url}/greek` per `my-json-server-oas.yaml`).

## Build and tests

```bash
./mvnw clean verify
```

- **Unit tests** (`*Test`): Surefire — no Docker.
- **Integration tests** (`*IT`): Failsafe — Testcontainers PostgreSQL, WireMock for sync; require Docker.

Skip integration tests (faster local iteration):

```bash
./mvnw clean verify -DskipITs=true
```

or use the Maven profile:

```bash
./mvnw clean verify -Pfast
```

Run a subset of scenarios by JUnit tag (Failsafe / Surefire):

```bash
./mvnw -q verify -Dgroups=smoke
./mvnw -q verify -Dgroups=error-handling
```

## JUnit tags (Gherkin mapping)

| Tag | Meaning |
|-----|---------|
| `smoke`, `happy-path` | 20 canonical names, ordered, set equality |
| `performance` | Single GET under 1 second |
| `load-testing` | 100 concurrent GETs, wall clock under 2 seconds |
| `error-handling` | 500 `application/problem+json` (normative fields); empty DB → `[]` |
| `data-quality` | After WireMock stubbed sync, API matches stub payload |
| `api-specification` | `Content-Type` includes `application/json`; JSON array of strings |

**`@availability`** from the feature file is **not** automated (SLO/monitoring only).

### CI flake note (`load-testing`)

The **`load-testing`** scenario enforces **100 concurrent** requests within **2 seconds** total wall time. Shared CI agents can occasionally miss this window; if the build fails only on this test, re-run the job or use a larger runner. Tuning: increase the threshold only if product owners agree to relax the Gherkin timing.

### Error-handling IT

`GreekGodsDatabaseFailureIT` uses **`@MockBean` on `GreekGodRepository`** to simulate a read-path persistence failure while Flyway still runs against a real Testcontainers database. The HTTP response still satisfies ADR-001 (500 + `type`, `title`, `status`).

## OpenAPI (contract-first)

Canonical API definitions live in **`src/main/resources/openapi/`** (copied from `requirements2/design/`):

| File | Role |
|------|------|
| `greekController-oas.yaml` | Public API (`GET /api/v1/gods/greek`). Drives **`java-micronaut-server`** generation (`AbstractGreekGodsController`, `ProblemDetail` model). |
| `my-json-server-oas.yaml` | Upstream JSON server. Drives **`java-micronaut-client`** (`ExternalGreekGodsApi` and related interfaces). |

During **`generate-sources`**, `openapi-generator-maven-plugin` emits code under `target/generated-sources/openapi-server` and `openapi-client`; `maven-antrun-plugin` removes the generated stub `Application` and `GreekGodsController` so this module keeps `GreekGodsApplication` and a hand-written `GreekGodsController` that extends the abstract API.

Runtime OpenAPI: after `./mvnw compile`, the spec derived from the generated controller surface is under `target/classes/META-INF/swagger/` (filename may vary by Micronaut OpenAPI version).

## Scheduling

`@Scheduled` is provided by **`micronaut-context`** (transitive). There is no separate `micronaut-scheduling` artifact on the 4.10 BOM. Sync timing is configured with:

- `greek-gods.sync.fixed-delay` (default `5m`)
- `greek-gods.sync.initial-delay` (default `30s`)
- `greek-gods.sync.scheduler-enabled` (`false` disables the job bean)
- `greek-gods.sync.enabled` (skips work inside the job when `false`)

## HTTP client note

Upstream sync uses the **OpenAPI-generated** declarative client **`ExternalGreekGodsApi`** (`@Client`) with base URL **`openapi-micronaut-client-base-path`**, mapped from **`greek-gods.upstream.base-url`** in `application.yml`. Global **`micronaut.http.client.connect-timeout`** and **`read-timeout`** mirror **`greek-gods.upstream.*`**.

### Why `micronaut-core-reactive` stays on the classpath

There is **no supported way** to run this app with **Micronaut’s HTTP server** (Netty, the default runtime) and **omit** `micronaut-core-reactive`:

- Server-agnostic response encoding lives in **`io.micronaut.http.server.ResponseLifecycle`** (Micronaut 4.8+), used by both Netty and servlet runtimes.
- That class imports **`io.micronaut.core.async.publisher.Publishers`** and **`LazySendingSubscriber`**, which are packaged **only** in **`micronaut-core-reactive`**, not in `micronaut-core`.
- **`micronaut-http-server`**’s POM does not list `micronaut-core-reactive`, so many apps only got it **transitively** (e.g. via `micronaut-http-client`). This module declares it **explicitly** so the classpath stays correct even if dependency resolution changes.

**Controllers and sync logic stay imperative/blocking**; the framework still loads small reactive helper types for **response encoding**. Avoiding that JAR entirely would mean **not using Micronaut’s HTTP stack** (different framework or a non-HTTP entrypoint). Upstream improvement would be for `micronaut-http-server` to declare this dependency so every app gets a correct classpath by default ([micronaut-core](https://github.com/micronaut-projects/micronaut-core) `ResponseLifecycle`).

---

Micronaut reference: [User Guide](https://docs.micronaut.io/4.10.10/guide/index.html)

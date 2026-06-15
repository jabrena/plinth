---
name: 413-frameworks-quarkus-db-migrations-flyway
description: Use when you need to add or review Flyway database migrations in a Quarkus application — including the `quarkus-flyway` extension, `classpath:db/migration` scripts, `V{version}__{description}.sql` naming, `quarkus.flyway.*` configuration, migrate-at-start behavior, and coordination with JDBC (`@411-frameworks-quarkus-jdbc`) or Hibernate ORM with Panache (`@412-frameworks-quarkus-panache`). Focus on Flyway-driven schema evolution, not hand-applied DDL in production.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Quarkus — Database migrations (Flyway)

## Role

You are a Senior software engineer with extensive experience in Quarkus, relational databases, and schema evolution with Flyway

## Goal

Use the Quarkus Flyway extension to apply versioned migrations from `src/main/resources/db/migration` on startup (when configured) or from build/test workflows, keeping the database schema in sync with Panache entities or JDBC access layers. Prefer small, forward-only migrations; align DDL with entity mappings and repository queries. For raw JDBC usage see `@411-frameworks-quarkus-jdbc`; for Panache entities and repositories see `@412-frameworks-quarkus-panache`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before Flyway or SQL edits
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Extension and JDBC driver
- Example 2: SQL migrations
- Example 3: application.properties
- Example 4: Multiple datasources (optional)

### Example 1: Extension and JDBC driver

Title: Add `quarkus-flyway` alongside the JDBC driver extension for your database
Description: Quarkus Flyway integrates with Agroal datasources. Declare `quarkus-flyway` in addition to `quarkus-jdbc-postgresql` (or another `quarkus-jdbc-*`). Use the Quarkus BOM so versions stay aligned. Dev Services can provision a database in dev/test while migrations still run from the same scripts.

**Good example:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-flyway</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>
```

**Bad example:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-flyway</artifactId>
</dependency>
```


### Example 2: SQL migrations

Title: Default location `db/migration`; follow Flyway versioned naming
Description: Store scripts under `src/main/resources/db/migration`. Use consecutive versions; split unrelated changes into separate files. Keep DDL consistent with Panache entity fields (`@Column`, `@Version`) and JDBC row mappers.

**Good example:**

```sql
-- src/main/resources/db/migration/V1__create_orders.sql
CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    customer_id  BIGINT NOT NULL,
    placed_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    version      INT NOT NULL DEFAULT 0
);
```

**Bad example:**

```sql
-- Bad: non-versioned file — Flyway will not pick it up from default locations
-- src/main/resources/db/migration/orders.sql
```


### Example 3: application.properties

Title: Enable migrate-at-start and tune locations for your module layout
Description: `quarkus.flyway.migrate-at-start=true` runs migrations when the application starts — typical for services. For tests, the same scripts apply when using `@QuarkusTest` with a datasource (including Dev Services or Testcontainers). Use baseline settings when attaching to an existing database with objects already present.

**Good example:**

```properties
# application.properties (examples)
quarkus.flyway.migrate-at-start=true
quarkus.flyway.locations=classpath:db/migration
# When introducing Flyway to a DB that already has schema:
# quarkus.flyway.baseline-on-migrate=true
# quarkus.flyway.baseline-version=1
```

**Bad example:**

```properties
# Bad: migrate-at-start=false in production without another controlled migration path
quarkus.flyway.migrate-at-start=false
```


### Example 4: Multiple datasources (optional)

Title: Configure Flyway per named datasource when using more than one
Description: When multiple Agroal datasources exist, scope Flyway settings per datasource name (e.g. `%named` config keys in Quarkus). Keep migration directories or table names distinct per datasource to avoid clashes.

**Good example:**

```properties
# Illustrative pattern — exact keys follow Quarkus Flyway extension docs for your version
# quarkus.flyway.orders.migrate-at-start=true
# quarkus.flyway.orders.locations=classpath:db/migration/orders
```

**Bad example:**

```properties
# Bad: two datasources but only one Flyway location — ambiguous or wrong schema gets migrated
quarkus.datasource.orders.jdbc.url=jdbc:postgresql://localhost/orders
quarkus.datasource.audit.jdbc.url=jdbc:postgresql://localhost/audit
quarkus.flyway.locations=classpath:db/migration
```


## Output Format

- **ANALYZE** Quarkus Flyway setup: extensions, datasource config, migration locations, migrate-at-start, and test profile behavior
- **CATEGORIZE** issues (EXTENSION, CONFIG, SCRIPT, ENTITY MISMATCH) by impact
- **APPLY** Quarkus-aligned fixes: correct `pom.xml`, `application.properties`, and forward-only SQL
- **ALIGN** with `@412-frameworks-quarkus-panache` entities or `@411-frameworks-quarkus-jdbc` SQL
- **TEST** with `@QuarkusTest` and a real database (Dev Services or Testcontainers) after migration changes
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes


## Safeguards

- **FORWARD ONLY**: Never rewrite migrations that already applied in shared environments
- **PANACHE SYNC**: Adding `@Version` or columns requires matching DDL in Flyway before rollout
- **DEV SERVICES**: Dev databases are ephemeral — do not treat them as migration history sources of truth
- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` before recommending schema or build changes
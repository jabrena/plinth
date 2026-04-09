---
name: 313-frameworks-spring-db-migrations-flyway
description: Use when you need to add or review Flyway database migrations in a Spring Boot application — including Maven dependencies, `classpath:db/migration` scripts, versioning (`V{version}__{description}.sql`), configuration via `spring.flyway.*`, baseline and repair for existing databases, validation in CI, and coordination with JDBC (`@311-frameworks-spring-jdbc`) or Spring Data JDBC (`@312-frameworks-spring-data-jdbc`). Focus on Flyway; for ORM schema generation use the stack’s Hibernate integration instead of mixing approaches blindly.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# Spring — Database migrations (Flyway)

## Role

You are a Senior software engineer with extensive experience in Spring Boot, relational databases, and schema evolution with Flyway

## Goal

Ship additive, repeatable schema changes as versioned SQL (or Java) migrations under `src/main/resources/db/migration`, let Flyway run them on startup (or in a controlled job), and keep application code aligned with the live schema. Prefer explicit DDL in migrations over ad-hoc manual changes in shared environments. Pair migrations with integration tests that exercise repositories or JDBC against a real dialect. For programmatic JDBC patterns use `@311-frameworks-spring-jdbc`; for Spring Data JDBC entities and repositories use `@312-frameworks-spring-data-jdbc`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before Flyway or SQL edits
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements; include migration smoke tests where feasible

## Examples

### Table of contents

- Example 1: Maven coordinates
- Example 2: Versioned SQL scripts
- Example 3: Spring Boot configuration
- Example 4: Optional Java migrations

### Example 1: Maven coordinates

Title: Add Flyway on the classpath; use database-specific Flyway modules when required by your Flyway major version
Description: Spring Boot manages Flyway versions via its BOM. Declare `flyway-core` (often transitively via `spring-boot-starter-jdbc` + explicit Flyway, or a dedicated starter pattern used in your project). For PostgreSQL and recent Flyway releases, add the matching `flyway-database-postgresql` artifact so Flyway can talk to the dialect. Keep JDBC driver and Flyway versions compatible with your Spring Boot BOM.

**Good example:**

```xml
<!-- pom.xml — Flyway + PostgreSQL support (adjust artifact to your DB) -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

**Bad example:**

```xml
<!-- Bad: relying on an outdated Flyway without the database module Flyway 10+ expects -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>8.0.0</version> <!-- pinned old version fighting Spring Boot BOM -->
</dependency>
```


### Example 2: Versioned SQL scripts

Title: Place scripts under `db/migration`; use Flyway’s `V{version}__{description}.sql` convention
Description: One migration file per logical change. Use sequential versions; never reuse a version number that already ran in an environment. Prefer idempotent-friendly DDL where the database allows (e.g. `IF NOT EXISTS` where supported) but do not “fix” a broken migration by editing it after deploy — add a new forward migration instead.

**Good example:**

```sql
-- src/main/resources/db/migration/V1__create_customers.sql
CREATE TABLE customers (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(320) NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- src/main/resources/db/migration/V2__add_customer_name.sql
ALTER TABLE customers
    ADD COLUMN first_name VARCHAR(120) NOT NULL DEFAULT '',
    ADD COLUMN last_name  VARCHAR(120) NOT NULL DEFAULT '';
```

**Bad example:**

```sql
-- Bad: ambiguous naming / duplicate version prefix — breaks ordering or fails validation
-- db/migration/create_customers.sql (missing V1__ prefix)
-- V1__customers.sql and V1__orders.sql (duplicate V1)
```


### Example 3: Spring Boot configuration

Title: Control locations, baseline, and validation with `spring.flyway.*`
Description: Default location is `classpath:db/migration`. For brownfield databases, `spring.flyway.baseline-on-migrate=true` establishes a baseline without replaying history. Keep `validate-on-migrate` enabled in production unless you have a documented exception. Use placeholders for environment-specific names only when truly necessary.

**Good example:**

```properties
# application.properties (examples)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

**Bad example:**

```properties
# Bad: disabling validation in production hides checksum drift and risky edits
spring.flyway.validate-on-migrate=false
```


### Example 4: Optional Java migrations

Title: Use Java-based migrations when SQL alone is insufficient; keep them small and testable
Description: Implement `org.flywaydb.core.api.migration.BaseJavaMigration` (or `JavaMigration`) for data backfills that need procedural logic. Prefer SQL for DDL; reserve Java for complex data fixes with clear logging and idempotence where possible.

**Good example:**

```java
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import java.sql.Statement;

public class V3__BackfillCustomerDisplayName extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement st = context.getConnection().createStatement()) {
            st.executeUpdate("""
                UPDATE customers
                SET display_name = trim(coalesce(first_name, '') || ' ' || coalesce(last_name, ''))
                WHERE display_name IS NULL
                """);
        }
    }
}
```

**Bad example:**

```java
// Bad: embedding environment-specific secrets or one-off manual steps inside a migration
public class V3__LoadProdData extends BaseJavaMigration {
    public void migrate(Context context) {
        throw new UnsupportedOperationException("Run this manually in prod"); // never ship this
    }
}
```

## Output Format

- **ANALYZE** current Flyway setup: dependencies, locations, Spring properties, migration history strategy, and alignment with JDBC/Data JDBC code
- **CATEGORIZE** gaps (MISSING MODULE, NAMING, CONFIG, OPERATIONS) and risk (data loss, downtime, checksum mismatch)
- **APPLY** Spring Boot–aligned fixes: correct artifacts, script layout, `spring.flyway.*` tuning, and forward-only migration discipline
- **COORDINATE** with `@311-frameworks-spring-jdbc` / `@312-frameworks-spring-data-jdbc` so entities and SQL match applied migrations
- **TEST** with integration tests hitting a real database (e.g. Testcontainers) after migration changes
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **IMMUTABLE HISTORY**: Do not edit migrations that already ran in shared environments; add a new versioned script to correct schema or data
- **CHECKSUMS**: Changing a applied file breaks `validate-on-migrate` — use repair only with operational awareness
- **ROLLBACK**: Flyway does not auto-rollback; plan forward migrations (or manual runbooks) for reversibility
- **LOCKS**: Long DDL can block traffic — schedule risky changes and prefer online migration patterns for large tables
- **SECRETS**: Never put secrets inside migration sources committed to Git
- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` before recommending schema or build changes
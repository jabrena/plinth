---
name: 311-frameworks-spring-jdbc
description: Use when you need to write or review programmatic JDBC with Spring — including JdbcTemplate, NamedParameterJdbcTemplate, JdbcClient (Spring Framework 6.1+), parameterized SQL, RowMapper mapping to records, batch operations, transactions, and safe handling of generated keys.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring JDBC — JdbcTemplate and JdbcClient

## Role

You are a Senior software engineer with extensive experience in Spring Framework and JDBC data access

## Goal

Spring’s JDBC support centers on `JdbcTemplate` and friends for template-style access, and on `JdbcClient` for a fluent, chainable API built on top of that stack. Prefer explicit SQL with bind parameters, map rows to immutable records or small DTOs, keep transactions at the service layer, and let Spring translate SQL exceptions to `DataAccessException`. Use `NamedParameterJdbcTemplate` when named placeholders improve readability. Choose Spring Data JDBC (`@312-frameworks-spring-data-jdbc`) when repositories and aggregate mapping fit; use `JdbcTemplate` / `JdbcClient` for ad-hoc SQL, reporting, or tight control over statements.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Parameter binding**: Always bind variables with `?` placeholders, named parameters, or `JdbcClient.param(...)` — never concatenate untrusted input into SQL strings.
2. **Explicit mapping**: Use `RowMapper`, `DataClassRowMapper` (records), or `JdbcClient` typed queries so column-to-field mapping stays obvious and testable.
3. **Transactions**: Declare `@Transactional` on services; keep DAO/repository-style JDBC helpers free of broad transaction boundaries unless they are the natural unit of work.
4. **API choice**: Prefer `JdbcClient` for new code on Spring Framework 6.1+ for readability; retain `JdbcTemplate` where legacy or batch APIs fit best.
5. **Exceptions and resources**: Rely on Spring’s `DataAccessException` hierarchy and template-managed connections; avoid manual connection lifecycle in application code.

**Cross-references**: Repository-oriented JDBC with aggregates — `@312-frameworks-spring-data-jdbc`. Spring Boot core (datasources, transactions) — `@301-frameworks-spring-boot-core`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any JDBC refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with data-access changes
- **NO EXCEPTIONS**: Under no circumstances should JDBC recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Parameterized queries with JdbcTemplate
- Example 2: NamedParameterJdbcTemplate
- Example 3: JdbcClient fluent API
- Example 4: Row mapping and records
- Example 5: Transactions and JDBC
- Example 6: Batch updates and generated keys

### Example 1: Parameterized queries with JdbcTemplate

Title: Bind arguments; never concatenate user input into SQL
Description: Use `query`, `queryForObject`, or `update` overloads that accept `Object...` args or `PreparedStatementSetter`. This keeps plans cacheable and prevents SQL injection.

**Good example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<String> findEmailsByStatus(String status) {
        return jdbcTemplate.query(
            "SELECT email FROM users WHERE status = ?",
            (rs, rowNum) -> rs.getString("email"),
            status
        );
    }

    int updateStatus(long userId, String newStatus) {
        return jdbcTemplate.update(
            "UPDATE users SET status = ? WHERE id = ?",
            newStatus, userId
        );
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;

class UnsafeUserRepository {

    private final JdbcTemplate jdbcTemplate;

    UnsafeUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<String> findByStatus(String status) {
        String sql = "SELECT email FROM users WHERE status = '" + status + "'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("email"));
    }
}
```

### Example 2: NamedParameterJdbcTemplate

Title: Named placeholders for readable SQL and Map-based params
Description: Use `:name` placeholders with `MapSqlParameterSource` or maps. Prefer this when the same SQL is reused with different named arguments or when readability beats positional `?` lists.

**Good example:**

```java
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.List;

class OrderRepository {

    private final NamedParameterJdbcTemplate named;

    OrderRepository(NamedParameterJdbcTemplate named) {
        this.named = named;
    }

    List<Long> findIdsByCustomerAndState(long customerId, String state) {
        var params = new MapSqlParameterSource()
            .addValue("customerId", customerId)
            .addValue("state", state);
        return named.query(
            "SELECT id FROM orders WHERE customer_id = :customerId AND state = :state",
            params,
            (rs, rowNum) -> rs.getLong("id")
        );
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    List<Long> findIds(long customerId, String state) {
        return jdbcTemplate.query(
            "SELECT id FROM orders WHERE customer_id = " + customerId + " AND state = ?",
            (rs, rowNum) -> rs.getLong("id"),
            state
        );
    }
}
```

### Example 3: JdbcClient fluent API

Title: Spring Framework 6.1+ chainable SQL, params, and typed results
Description: Inject `JdbcClient` (typically built from `DataSource` or `JdbcTemplate`). Use `sql(String)`, then `param` / indexed params, then `query` with a row mapper or mapped type, or `update` for writes. Prefer `single()` / `optional()` when at most one row is expected.

**Good example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import java.util.List;
import java.util.Optional;

class ProductRepository {

    private final JdbcClient jdbcClient;

    ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    Optional<String> findNameById(long id) {
        return jdbcClient.sql("SELECT name FROM products WHERE id = ?")
            .param(id)
            .query(String.class)
            .optional();
    }

    List<ProductRow> findActive() {
        return jdbcClient.sql("SELECT id, name FROM products WHERE active = ?")
            .param(true)
            .query(ProductRow.class)
            .list();
    }

    int deactivate(long id) {
        return jdbcClient.sql("UPDATE products SET active = false WHERE id = ?")
            .param(id)
            .update();
    }
}

record ProductRow(long id, String name) {}
```

**Bad example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;

class ProductRepository {

    private final JdbcClient jdbcClient;

    String findNameUnsafe(long id) {
        return jdbcClient.sql("SELECT name FROM products WHERE id = " + id)
            .query(String.class)
            .single();
    }
}
```

### Example 4: Row mapping and records

Title: DataClassRowMapper or explicit RowMapper
Description: Map `ResultSet` columns to records or immutable types. Use `DataClassRowMapper` for canonical record constructors, or an explicit `RowMapper` when names or conversions need control.

**Good example:**

```java
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

record CustomerRow(long id, String email, String status) {}

class CustomerQuery {

    private final JdbcTemplate jdbcTemplate;

    CustomerQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<CustomerRow> findAll() {
        return jdbcTemplate.query(
            "SELECT id, email, status FROM customers",
            DataClassRowMapper.newInstance(CustomerRow.class)
        );
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;

class CustomerQuery {

    private final JdbcTemplate jdbcTemplate;

    List<Object[]> findAllRaw() {
        return jdbcTemplate.query(
            "SELECT id, email, status FROM customers",
            (rs, rowNum) -> new Object[] {
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("status")
            }
        );
    }
}
```

### Example 5: Transactions and JDBC

Title: Declare boundaries on services
Description: Wrap multi-statement workflows in `@Transactional` on the service. `JdbcTemplate` participates in the current Spring transaction when one exists.

**Good example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TransferService {

    private final JdbcTemplate jdbcTemplate;

    TransferService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    void transfer(long fromId, long toId, long amount) {
        jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
        jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;

class TransferDao {

    private final JdbcTemplate jdbcTemplate;

    void debit(long id, long amount) {
        jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, id);
    }

    void credit(long id, long amount) {
        jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, id);
    }
    // Bad: callers can forget to wrap debit+credit in one transaction — boundary belongs on service
}
```

### Example 6: Batch updates and generated keys

Title: batchUpdate for bulk writes; KeyHolder for inserts
Description: Use `JdbcTemplate.batchUpdate` with `BatchPreparedStatementSetter` or argument lists for repeated statements. For inserts that return keys, use `KeyHolder` / `GeneratedKeyHolder` with `PreparedStatement.CREATE_RETURNING_KEYS` or database-specific patterns.

**Good example:**

```java
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

class ItemWriter {

    private final JdbcTemplate jdbcTemplate;

    ItemWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    int[] batchInsert(List<String> names) {
        return jdbcTemplate.batchUpdate(
            "INSERT INTO items (name) VALUES (?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                    ps.setString(1, names.get(i));
                }

                @Override
                public int getBatchSize() {
                    return names.size();
                }
            }
        );
    }

    long insertReturningId(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO items (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : -1L;
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

class ItemWriter {

    private final JdbcTemplate jdbcTemplate;

    void insertLoop(List<String> names) {
        for (String name : names) {
            jdbcTemplate.update("INSERT INTO items (name) VALUES (?)", name);
        }
    }
    // Bad: N round-trips — use batchUpdate for large batches

    void insertNoKeys(String name) {
        jdbcTemplate.update("INSERT INTO items (name) VALUES (?)", name);
        // Bad: if caller needs the new id, fetch by unique key or use KeyHolder
    }
}
```

## Output Format

- **ANALYZE** JDBC usage: SQL safety, parameter style, mapping approach, transaction boundaries, batch opportunities, and whether Spring Data JDBC would simplify the case
- **CATEGORIZE** findings by impact (SECURITY for injection risk, CORRECTNESS for mapping, PERFORMANCE for N+1 or per-row updates)
- **APPLY** improvements: introduce parameter binding, NamedParameterJdbcTemplate or JdbcClient, RowMapper or record mapping, service-level `@Transactional`, batch APIs where appropriate
- **EXPLAIN** when to keep programmatic JDBC vs adopt `@312-frameworks-spring-data-jdbc`
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before JDBC refactoring
- **SQL INJECTION**: Never build SQL by concatenating untrusted strings
- **TRANSACTIONS**: Multi-step writes must share a transaction boundary at the service layer
- **COMPATIBILITY**: `JdbcClient` requires Spring Framework 6.1+ — verify dependency versions before recommending migration
- **INCREMENTAL SAFETY**: Change data-access code in small steps with tests
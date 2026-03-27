---
name: 311-frameworks-spring-jdbc
description: Use when you need to write or review programmatic JDBC with Spring — including JdbcTemplate, NamedParameterJdbcTemplate, JdbcClient (Spring Framework 6.1+), parameterized SQL, RowMapper mapping to records, batch operations, transactions, safe handling of generated keys, DataAccessException handling, read-only transactions, streaming large result sets, and @JdbcTest slice testing.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0
---
# Spring JDBC — JdbcTemplate and JdbcClient

## Role

You are a Senior software engineer with extensive experience in Spring Framework and JDBC data access

## Goal

Spring's JDBC support centers on `JdbcTemplate` and friends for template-style access, and on `JdbcClient` for a fluent, chainable API built on top of that stack. Prefer explicit SQL with bind parameters, map rows to immutable records or small DTOs, keep transactions at the service layer, and let Spring translate SQL exceptions to `DataAccessException`. Use `NamedParameterJdbcTemplate` when named placeholders improve readability. Choose Spring Data JDBC (`@312-frameworks-spring-data-jdbc`) when repositories and aggregate mapping fit; use `JdbcTemplate` / `JdbcClient` for ad-hoc SQL, reporting, or tight control over statements.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Parameter binding**: Always bind variables with `?` placeholders, named parameters, or `JdbcClient.param(...)` — never concatenate untrusted input into SQL strings.
2. **Explicit mapping**: Use `RowMapper`, `DataClassRowMapper` (records), or `JdbcClient` typed queries so column-to-field mapping stays obvious and testable.
3. **Transactions**: Declare `@Transactional` on services; keep DAO/repository-style JDBC helpers free of broad transaction boundaries unless they are the natural unit of work.
4. **API choice**: Prefer `JdbcClient` for new code on Spring Framework 6.1+ for readability; retain `JdbcTemplate` where legacy or batch APIs fit best.
5. **Exceptions and resources**: Rely on Spring's `DataAccessException` hierarchy and template-managed connections; catch specific subtypes (`DuplicateKeyException`, `EmptyResultDataAccessException`) at service boundaries to translate into domain exceptions; avoid manual connection lifecycle in application code.
6. **Read-only optimization**: Mark pure query paths with `@Transactional(readOnly = true)` at the class or method level so the connection pool and database can apply read-path optimizations.
7. **Safe single-row access**: Use `JdbcClient.query().optional()` or `jdbcTemplate.query(...).stream().findFirst()` instead of `queryForObject` when zero rows are possible; `queryForObject` throws `EmptyResultDataAccessException` on no rows.
8. **Streaming large results**: Use `RowCallbackHandler` or `ResultSetExtractor` to process large result sets row-by-row without loading the full list into memory.
9. **Testability**: Test JDBC helpers with the `@JdbcTest` slice (loads `DataSource` and `JdbcTemplate` only) and use `@Sql` for fixture data; avoid full `@SpringBootTest` just to exercise a repository.

**Cross-references**: Repository-oriented JDBC with aggregates — `@312-frameworks-spring-data-jdbc`. Spring Boot core (datasources, transactions) — `@301-frameworks-spring-boot-core`. SQL injection and secure coding — `@124-java-secure-coding`.

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
- Example 7: Safe single-row queries
- Example 8: DataAccessException hierarchy and handling
- Example 9: Read-only transactions for query paths
- Example 10: Streaming large result sets
- Example 11: Testing with @JdbcTest
- Example 12: Rollback rules
- Example 13: Self-invocation pitfall
- Example 14: Programmatic transactions with TransactionTemplate

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

Title: Spring Framework 6.1+ chainable SQL, named or indexed params, and typed results
Description: Inject `JdbcClient` (typically built from `DataSource` or `JdbcTemplate`). Use `sql(String)`, then `.param(value)` for indexed params or `.param(name, value)` for named placeholders (`:name`), then `query` with a row mapper or mapped type, or `update` for writes. Prefer `single()` / `optional()` when at most one row is expected.

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

    // Indexed positional params
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

    // Named params — improves readability for multiple bindings
    List<ProductRow> findByCategoryAndMinPrice(String category, java.math.BigDecimal minPrice) {
        return jdbcClient.sql("""
                SELECT id, name FROM products
                WHERE category = :category AND price >= :minPrice
                """)
            .param("category", category)
            .param("minPrice", minPrice)
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
        // Bad: SQL built by concatenation — injection risk and no plan caching
        return jdbcClient.sql("SELECT name FROM products WHERE id = " + id)
            .query(String.class)
            .single();
    }

    String findNameMissingOptional(long id) {
        // Bad: single() throws IncorrectResultSizeDataAccessException when 0 rows found
        return jdbcClient.sql("SELECT name FROM products WHERE id = ?")
            .param(id)
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

Title: Declare boundaries on services — JdbcTemplate and JdbcClient
Description: Wrap multi-statement workflows in `@Transactional` on the service. Both `JdbcTemplate` and `JdbcClient` participate in the current Spring transaction when one exists. `JdbcClient` (Spring Framework 6.1+) offers the same transactional semantics with a fluent, chainable API.

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

Title: batchUpdate for bulk writes; KeyHolder for inserts; List<Object[]> shorthand
Description: Use `JdbcTemplate.batchUpdate` with `BatchPreparedStatementSetter` or a `List<Object[]>` argument-list shorthand for repeated statements. For inserts that return keys, use `KeyHolder` / `GeneratedKeyHolder` with `PreparedStatement.RETURN_GENERATED_KEYS` or database-specific patterns.

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

    // Shorthand: List<Object[]> — one row per array element
    int[] batchInsertSimple(List<String> names) {
        List<Object[]> args = names.stream()
            .map(n -> new Object[]{n})
            .toList();
        return jdbcTemplate.batchUpdate("INSERT INTO items (name) VALUES (?)", args);
    }

    // Full control: BatchPreparedStatementSetter for type-specific binding
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

### Example 7: Safe single-row queries

Title: optional() or stream().findFirst() instead of queryForObject when zero rows are possible
Description: `JdbcTemplate.queryForObject` throws `EmptyResultDataAccessException` when it finds zero rows and `IncorrectResultSizeDataAccessException` when it finds more than one. Use `JdbcClient.query().optional()` (Spring 6.1+) or `jdbcTemplate.query(...).stream().findFirst()` when the result may be absent, to keep callers in control of the empty case.

**Good example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import java.util.Optional;

record UserRow(long id, String email) {}

class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcClient jdbcClient;

    UserRepository(JdbcTemplate jdbcTemplate, JdbcClient jdbcClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcClient = jdbcClient;
    }

    // JdbcClient (Spring 6.1+): idiomatic optional()
    Optional<UserRow> findByEmail(String email) {
        return jdbcClient.sql("SELECT id, email FROM users WHERE email = ?")
            .param(email)
            .query(UserRow.class)
            .optional();
    }

    // JdbcTemplate: stream().findFirst() avoids the exception on empty result
    Optional<String> findEmailById(long id) {
        return jdbcTemplate.query(
            "SELECT email FROM users WHERE id = ?",
            (rs, rowNum) -> rs.getString("email"),
            id
        ).stream().findFirst();
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;

class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    // Bad: throws EmptyResultDataAccessException when no user found
    String findEmailById(long id) {
        return jdbcTemplate.queryForObject(
            "SELECT email FROM users WHERE id = ?",
            String.class,
            id
        );
    }

    // Bad: caller has no safe way to distinguish "not found" from runtime error
    Long countByStatus(String status) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE status = ?",
            Long.class,
            status
        );
        // queryForObject is fine for aggregate functions that always return one row;
        // but for entity lookups, prefer optional() or stream().findFirst()
    }
}
```

### Example 8: DataAccessException hierarchy and handling

Title: Catch specific subtypes at service boundaries; translate to domain exceptions
Description: Spring maps all SQL exceptions to `DataAccessException` subtypes. Catch specific subtypes — `DuplicateKeyException` for unique constraint violations, `DataIntegrityViolationException` for FK or check failures — at the service layer and translate them into meaningful domain exceptions. Never let raw `DataAccessException` propagate to controllers or clients.

**Good example:**

```java
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AccountService {

    private final JdbcClient jdbcClient;

    AccountService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    void register(String email) {
        try {
            jdbcClient.sql("INSERT INTO accounts (email) VALUES (?)")
                .param(email)
                .update();
        } catch (DuplicateKeyException ex) {
            throw new EmailAlreadyRegisteredException(email, ex);
        } catch (DataIntegrityViolationException ex) {
            throw new AccountCreationException("Constraint violated for: " + email, ex);
        }
    }
}

class EmailAlreadyRegisteredException extends RuntimeException {
    EmailAlreadyRegisteredException(String email, Throwable cause) {
        super("Email already registered: " + email, cause);
    }
}

class AccountCreationException extends RuntimeException {
    AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

**Bad example:**

```java
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

@Service
class AccountService {

    private final JdbcClient jdbcClient;

    void register(String email) {
        // Bad: catching raw DataAccessException loses specific context
        try {
            jdbcClient.sql("INSERT INTO accounts (email) VALUES (?)")
                .param(email)
                .update();
        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    void registerUnchecked(String email) {
        // Bad: catching raw SQLException bypasses Spring's exception translation
        try {
            jdbcClient.sql("INSERT INTO accounts (email) VALUES (?)").param(email).update();
        } catch (Exception ex) {
            // Swallowing the exception — callers never know it failed
        }
    }
}
```

### Example 9: Read-only transactions for query paths

Title: @Transactional(readOnly = true) at class level; override for writes
Description: Apply `@Transactional(readOnly = true)` as the class-level default for services that are predominantly query-oriented. Override with plain `@Transactional` on individual write methods. This allows the connection pool and underlying driver to skip dirty-tracking and may enable read replicas or optimistic locking shortcuts.

**Good example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

record ProductRow(long id, String name, boolean active) {}

@Service
@Transactional(readOnly = true)
class ProductService {

    private final JdbcClient jdbcClient;

    ProductService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<ProductRow> findActive() {
        return jdbcClient.sql("SELECT id, name, active FROM products WHERE active = true")
            .query(ProductRow.class)
            .list();
    }

    Optional<ProductRow> findById(long id) {
        return jdbcClient.sql("SELECT id, name, active FROM products WHERE id = ?")
            .param(id)
            .query(ProductRow.class)
            .optional();
    }

    @Transactional
    void deactivate(long id) {
        jdbcClient.sql("UPDATE products SET active = false WHERE id = ?")
            .param(id)
            .update();
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

record ProductRow(long id, String name, boolean active) {}

@Service
class ProductService {

    private final JdbcClient jdbcClient;

    // Bad: no @Transactional at all — reads run in auto-commit mode,
    // losing connection-pool read hints and consistent snapshot
    List<ProductRow> findActive() {
        return jdbcClient.sql("SELECT id, name, active FROM products WHERE active = true")
            .query(ProductRow.class)
            .list();
    }

    // Bad: @Transactional without readOnly on a read-only method —
    // misses optimization signal; write transaction acquired unnecessarily
    @Transactional
    List<ProductRow> findAll() {
        return jdbcClient.sql("SELECT id, name, active FROM products")
            .query(ProductRow.class)
            .list();
    }
}
```

### Example 10: Streaming large result sets

Title: RowCallbackHandler and ResultSetExtractor for row-by-row processing
Description: When a query may return thousands or millions of rows, loading them all into a `List` risks heap pressure. Use `RowCallbackHandler` to process each row as it arrives (fire-and-forget, no return), or `ResultSetExtractor` when you need to aggregate into a custom result during iteration.

**Good example:**

```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
class ReportService {

    private final JdbcTemplate jdbcTemplate;

    ReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowCallbackHandler: no return value, process/write row immediately
    void exportAuditLog(PrintWriter writer) {
        jdbcTemplate.query(
            "SELECT id, action, created_at FROM audit_log ORDER BY created_at",
            rs -> {
                writer.printf("%d,%s,%s%n",
                    rs.getLong("id"),
                    rs.getString("action"),
                    rs.getTimestamp("created_at"));
            }
        );
    }

    // ResultSetExtractor: accumulate into a custom structure
    Map<String, Long> countByCategory() {
        return jdbcTemplate.query(
            "SELECT category, COUNT(*) AS cnt FROM products GROUP BY category",
            (ResultSetExtractor<Map<String, Long>>) rs -> {
                Map<String, Long> result = new LinkedHashMap<>();
                while (rs.next()) {
                    result.put(rs.getString("category"), rs.getLong("cnt"));
                }
                return result;
            }
        );
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.PrintWriter;
import java.util.List;

record AuditEntry(long id, String action, java.sql.Timestamp createdAt) {}

@Service
class ReportService {

    private final JdbcTemplate jdbcTemplate;

    // Bad: loads every row into a List before writing — OOM risk on large tables
    void exportAuditLog(PrintWriter writer) {
        List<AuditEntry> entries = jdbcTemplate.query(
            "SELECT id, action, created_at FROM audit_log ORDER BY created_at",
            DataClassRowMapper.newInstance(AuditEntry.class)
        );
        for (AuditEntry e : entries) {
            writer.printf("%d,%s,%s%n", e.id(), e.action(), e.createdAt());
        }
    }
}
```

### Example 11: Testing with @JdbcTest

Title: Lightweight slice test for JDBC repositories; @Sql for fixtures
Description: Use `@JdbcTest` to load only `DataSource`, `JdbcTemplate`, `NamedParameterJdbcTemplate`, and `JdbcClient` — no web layer, no full application context. Wire your repository under test with `@Import`. Use `@Sql` to set up and tear down fixture data. Prefer an embedded H2 database or Testcontainers for realistic dialect coverage.

**Good example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(ProductRepository.class)
@Sql("/sql/products.sql")                          // inserts fixture rows before each test
@Sql(scripts = "/sql/cleanup.sql",
     executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findActive_returnsOnlyActiveProducts() {
        List<ProductRow> active = productRepository.findActive();

        assertThat(active).isNotEmpty()
            .allMatch(ProductRow::active);
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        assertThat(productRepository.findById(Long.MAX_VALUE)).isEmpty();
    }
}

// Minimal repository wired by the slice
class ProductRepository {

    private final org.springframework.jdbc.core.simple.JdbcClient jdbcClient;

    ProductRepository(org.springframework.jdbc.core.simple.JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    java.util.List<ProductRow> findActive() {
        return jdbcClient.sql("SELECT id, name, active FROM products WHERE active = true")
            .query(ProductRow.class).list();
    }

    java.util.Optional<ProductRow> findById(long id) {
        return jdbcClient.sql("SELECT id, name, active FROM products WHERE id = ?")
            .param(id).query(ProductRow.class).optional();
    }
}

record ProductRow(long id, String name, boolean active) {}
```

**Bad example:**

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

// Bad: full application context loaded just to test a JDBC repository —
// slow startup, loads web layer and all beans unnecessarily
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findActive() {
        // Bad: no fixture setup — test depends on pre-existing database state
        List<ProductRow> active = productRepository.findActive();
        assertThat(active).isNotEmpty();
    }
}
```

### Example 12: Rollback rules

Title: rollbackFor for checked exceptions; noRollbackFor for expected failures
Description: By default Spring rolls back on unchecked (`RuntimeException`) and `Error` only. Declare `rollbackFor` to include checked exceptions that should abort the transaction. Use `noRollbackFor` when a specific exception is an expected, non-fatal condition that must not roll back an otherwise healthy transaction.

**Good example:**

```java
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class OrderService {

    private final JdbcClient jdbcClient;

    OrderService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // Roll back on the checked IOException (e.g. downstream enrichment failure)
    @Transactional(rollbackFor = java.io.IOException.class)
    void placeOrder(long customerId, long productId) throws java.io.IOException {
        jdbcClient.sql("INSERT INTO orders (customer_id, product_id) VALUES (?, ?)")
            .params(customerId, productId)
            .update();
        enrichOrder(productId); // may throw IOException — transaction rolls back
    }

    private void enrichOrder(long productId) throws java.io.IOException { }

    // DuplicateKeyException is expected for idempotent inserts — do not abort the transaction
    @Transactional(noRollbackFor = DuplicateKeyException.class)
    void ensureTag(String tag) {
        try {
            jdbcClient.sql("INSERT INTO tags (name) VALUES (?)").param(tag).update();
        } catch (DuplicateKeyException ignored) {
            // tag already exists — idempotent, not an error
        }
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class OrderService {

    private final JdbcClient jdbcClient;

    OrderService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // Bad: default @Transactional does NOT roll back on checked IOException —
    // the INSERT persists even when enrichment fails
    @Transactional
    void placeOrder(long customerId, long productId) throws java.io.IOException {
        jdbcClient.sql("INSERT INTO orders (customer_id, product_id) VALUES (?, ?)")
            .params(customerId, productId)
            .update();
        enrichOrder(productId);
    }

    private void enrichOrder(long productId) throws java.io.IOException { }
}
```

### Example 13: Self-invocation pitfall

Title: Calling @Transactional from within the same bean bypasses the proxy
Description: Spring applies `@Transactional` through a proxy. When a method calls another `@Transactional` method on the same bean instance via `this.method()`, the proxy is bypassed and no transaction is started or joined. Extract the inner method to a separate Spring-managed bean to ensure proxy interception.

**Good example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Separate bean — proxy is intercepted correctly for each method
@Service
class AuditService {

    private final JdbcClient jdbcClient;

    AuditService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    void log(String event) {
        jdbcClient.sql("INSERT INTO audit_log (event) VALUES (?)").param(event).update();
    }
}

@Service
class OrderService {

    private final JdbcClient jdbcClient;
    private final AuditService auditService;

    OrderService(JdbcClient jdbcClient, AuditService auditService) {
        this.jdbcClient = jdbcClient;
        this.auditService = auditService;
    }

    @Transactional
    void placeOrder(long customerId, long productId) {
        jdbcClient.sql("INSERT INTO orders (customer_id, product_id) VALUES (?, ?)")
            .params(customerId, productId).update();
        auditService.log("order-placed"); // proxy intercepted — @Transactional honoured
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class OrderService {

    private final JdbcClient jdbcClient;

    OrderService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    void placeOrder(long customerId, long productId) {
        jdbcClient.sql("INSERT INTO orders (customer_id, product_id) VALUES (?, ?)")
            .params(customerId, productId).update();
        this.auditLog("order-placed"); // Bad: self-invocation — @Transactional on auditLog is ignored
    }

    @Transactional
    void auditLog(String event) {
        jdbcClient.sql("INSERT INTO audit_log (event) VALUES (?)").param(event).update();
    }
}
```

### Example 14: Programmatic transactions with TransactionTemplate

Title: Fine-grained control when declarative @Transactional is not applicable
Description: Use `TransactionTemplate` when transaction boundaries must be determined at runtime — for example, inside lambdas, per-item conditional commit/rollback in a loop, or in contexts where AOP proxy interception is unavailable. `TransactionTemplate` is thread-safe and can be shared across instances.

**Good example:**

```java
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;

@Service
class BatchImportService {

    private final JdbcClient jdbcClient;
    private final TransactionTemplate transactionTemplate;

    BatchImportService(JdbcClient jdbcClient, TransactionTemplate transactionTemplate) {
        this.jdbcClient = jdbcClient;
        this.transactionTemplate = transactionTemplate;
    }

    // Each item commits independently; duplicate items are skipped, not aborted
    void importItems(List<String> items) {
        items.forEach(item ->
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    jdbcClient.sql("INSERT INTO items (name) VALUES (?)").param(item).update();
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly(); // skip this item, continue loop
                }
            })
        );
    }
}
```

**Bad example:**

```java
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

// Bad: raw PlatformTransactionManager — verbose, error-prone commit/rollback pairing;
// no try/finally means the transaction leaks if the update throws
@Component
class BatchImportService {

    private final JdbcClient jdbcClient;
    private final PlatformTransactionManager txManager;

    BatchImportService(JdbcClient jdbcClient, PlatformTransactionManager txManager) {
        this.jdbcClient = jdbcClient;
        this.txManager = txManager;
    }

    void importItem(String item) {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        jdbcClient.sql("INSERT INTO items (name) VALUES (?)").param(item).update();
        txManager.commit(status); // transaction leaks if update throws
    }
}
```

## Output Format

- **ANALYZE** JDBC usage: SQL safety, parameter style, mapping approach, transaction boundaries, batch opportunities, exception handling, and whether Spring Data JDBC would simplify the case
- **CATEGORIZE** findings by impact (SECURITY for injection risk, CORRECTNESS for mapping or unsafe single-row access, PERFORMANCE for N+1 / missing readOnly / missing streaming)
- **APPLY** improvements: introduce parameter binding, NamedParameterJdbcTemplate or JdbcClient named params, RowMapper or record mapping, service-level `@Transactional`, `readOnly = true` on query paths, batch APIs and streaming where appropriate
- **HANDLE** exceptions: translate `DuplicateKeyException` and `DataIntegrityViolationException` to domain exceptions at the service boundary
- **TEST** with `@JdbcTest` slice and `@Sql` fixture annotations; verify repository behaviour with integration tests before and after refactoring
- **EXPLAIN** when to keep programmatic JDBC vs adopt `@312-frameworks-spring-data-jdbc`
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before JDBC refactoring
- **SQL INJECTION**: Never build SQL by concatenating untrusted strings
- **TRANSACTIONS**: Multi-step writes must share a transaction boundary at the service layer
- **READ-ONLY**: Mark pure query service methods with `@Transactional(readOnly = true)` for connection-pool and database optimization hints
- **ROLLBACK RULES**: Default `@Transactional` does not roll back on checked exceptions — declare `rollbackFor` explicitly when checked exceptions must abort the transaction
- **SELF-INVOCATION**: Never call a `@Transactional` method via `this.method()` inside the same bean — the Spring proxy is bypassed; extract to a separate Spring-managed bean
- **TRANSACTION-TEMPLATE**: Prefer `TransactionTemplate` over raw `PlatformTransactionManager` for programmatic transactions; always use `executeWithoutResult` or `execute` to avoid manual commit/rollback pairing
- **SINGLE-ROW SAFETY**: Use `optional()` or `stream().findFirst()` for entity lookups; reserve `queryForObject` for aggregate functions that always return one row
- **EXCEPTION TRANSLATION**: Catch `DuplicateKeyException` / `DataIntegrityViolationException` at the service layer and wrap in meaningful domain exceptions
- **COMPATIBILITY**: `JdbcClient` requires Spring Framework 6.1+ — verify dependency versions before recommending migration
- **INCREMENTAL SAFETY**: Change data-access code in small steps, covered by `@JdbcTest` slice tests
- **TESTING**: Exercise refactored JDBC code with `@JdbcTest` integration tests; do not rely on `./mvnw compile` alone to verify correctness
---
name: 411-frameworks-quarkus-jdbc
description: Use when you need to write or review programmatic JDBC in Quarkus — including Agroal-backed DataSource injection, PreparedStatement with bind parameters, mapping rows to Java records, transactions (@Transactional), batch updates, and optional NamedParameterJdbcTemplate when spring-jdbc is on the classpath. Prefer explicit SQL without ORM.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus JDBC — programmatic SQL

## Role

You are a Senior software engineer with extensive experience in Quarkus and JDBC data access

## Goal

Quarkus pairs JDBC drivers (`quarkus-jdbc-*`) with Agroal connection pooling. Application code should use injected `javax.sql.DataSource`, always bind parameters, map rows to immutable records or small DTOs, and declare transactions at the service boundary with `jakarta.transaction.Transactional`. Use Panache (`@412-frameworks-quarkus-panache`) when you want repository-style Hibernate access; use raw JDBC for reporting, bulk ETL, or maximum SQL control.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Parameter binding**: Use `PreparedStatement` placeholders — never concatenate untrusted input into SQL.
2. **Resource management**: Use try-with-resources for `Connection`, `PreparedStatement`, and `ResultSet`.
3. **Mapping**: Extract row mapping into private methods or small mapper types; prefer records for read models.
4. **Transactions**: Annotate service entry points with `@Transactional` and `readOnly = true` for queries where supported.
5. **Pooling**: Rely on Agroal defaults; tune `quarkus.datasource.*` only with measured need.
6. **Optional Spring JDBC**: If the project already uses `quarkus-spring-jdbc`, `NamedParameterJdbcTemplate` is acceptable — same binding rules apply.
7. **Dev experience**: Use Dev Services for databases in dev/test when practical (`quarkus.datasource.devservices.enabled`).

**Cross-references**: Panache (ORM-style) — `@412-frameworks-quarkus-panache`. Quarkus core — `@401-frameworks-quarkus-core`. Secure SQL — `@124-java-secure-coding`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before JDBC changes
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Injected DataSource
- Example 2: Transactional boundary
- Example 3: Insert with generated keys

### Example 1: Injected DataSource

Title: Parameterized query and record mapping
Description: Inject `DataSource`, open a connection per operation (or let transaction demarcation scope it), and map `ResultSet` rows to records.

**Good example:**

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerJdbcRepository {

    private final DataSource dataSource;

    @Inject
    public CustomerJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CustomerRow> findByStatus(String status) throws Exception {
        String sql = "SELECT id, email FROM customer WHERE status = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                List<CustomerRow> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new CustomerRow(rs.getLong("id"), rs.getString("email")));
                }
                return out;
            }
        }
    }
}

record CustomerRow(long id, String email) { }
```

**Bad example:**

```java
public List<CustomerRow> findByStatus(String status) throws Exception {
    String sql = "SELECT id, email FROM customer WHERE status = '" + status + "'";
    // SQL injection — never build SQL with string concatenation from inputs
}
```

### Example 2: Transactional boundary

Title: @Transactional on application service
Description: Place `@Transactional` on the service layer so multiple JDBC operations commit or roll back together.

**Good example:**

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    private final OrderJdbcRepository orders;

    public OrderService(OrderJdbcRepository orders) {
        this.orders = orders;
    }

    @Transactional
    public void placeOrder(long customerId, String sku) {
        orders.insertOrder(customerId, sku);
        orders.insertInventoryMovement(sku, -1);
    }
}
```

**Bad example:**

```java
// Bad: each repository method opens and commits separately with no coordinating transaction
public void placeOrder(long customerId, String sku) {
    orders.insertOrder(customerId, sku);
    orders.insertInventoryMovement(sku, -1);
}
```

### Example 3: Insert with generated keys

Title: RETURNING or getGeneratedKeys
Description: Prefer database-specific `RETURNING` clauses or `Statement.RETURN_GENERATED_KEYS` for inserts; handle absence of rows explicitly.

**Good example:**

```java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

long insert(Connection c, String email) throws Exception {
    String sql = "INSERT INTO customer(email) VALUES (?)";
    try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, email);
        ps.executeUpdate();
        var keys = ps.getGeneratedKeys();
        if (!keys.next()) {
            throw new IllegalStateException("No generated key");
        }
        return keys.getLong(1);
    }
}
```

**Bad example:**

```java
// Bad: assuming last inserted id from another connection or session
long id = selectMaxIdPlusOne();
```

## Output Format

- **ANALYZE** JDBC code for injection risk, missing transactions, connection leaks, and N+1 patterns
- **RECOMMEND** Panache (`@412`) when CRUD and entity state dominate; keep JDBC for ad-hoc SQL
- **VALIDATE** with `./mvnw clean verify` after changes

## Safeguards

- **SQL INJECTION**: String-built SQL from user input is a hard reject — use bind parameters
- **CONNECTION LEAKS**: Every `Connection` must be closed — prefer try-with-resources
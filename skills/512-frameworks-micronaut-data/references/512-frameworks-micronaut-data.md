---
name: 512-frameworks-micronaut-data
description: Use when you need data access with Micronaut Data — including JDBC (and JPA where applicable) repositories, @MappedEntity design, CrudRepository and custom @Query methods, pagination with Pageable, transactions with @Transactional, immutable-friendly entities and DTO projections, optimistic locking, compile-time query validation, and test setup with @MicronautTest and TestPropertyProvider. Replaces a split JDBC + Spring Data JDBC style with a single Micronaut Data prompt (no separate raw JDBC rule in the 501 index).
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0
---
# Micronaut Data Guidelines

## Role

You are a Senior software engineer with extensive experience in Micronaut Data, JDBC, and Java persistence

## Goal

Micronaut Data generates repository implementations at compile time: prefer explicit `@MappedEntity` models, `CrudRepository` / `PageableRepository` interfaces, and parameterized `@Query` for non-derivable SQL. Keep aggregates bounded, declare transactions at the service layer with `io.micronaut.transaction.annotation.Transactional`, and avoid N+1 retrieval patterns by using fetch joins or tailored queries. This prompt covers Micronaut Data for relational access (JDBC or JPA backends depending on project dependencies); use parameterized queries only — never concatenate untrusted input into SQL.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Explicit mapping**: Use `@MappedEntity` with clear table/column naming; use `@Id` and `@GeneratedValue` appropriately for your backend.
2. **Repository interfaces**: Extend `CrudRepository<E, ID>` or add pagination with `PageableRepository`; add derived query methods or `@Query` with named parameters.
3. **Transactions**: Place `@Transactional` on `@Singleton` application services; use `readOnly = true` for queries when supported.
4. **Immutability**: Prefer records or immutable types when your Micronaut Data version and mapping style support them; otherwise keep entities small and side-effect free outside persistence.
5. **Pagination**: Return `Page<E>` for large tables; cap `Pageable` size at the service or controller boundary.
6. **Projections**: Use interface or DTO projections for read models instead of over-fetching full entities when lists are hot paths.
7. **Concurrency**: Use `@Version` for optimistic locking where updates collide.
8. **Safety**: Always bind parameters in `@Query`; validate dynamic sort keys against an allow-list.
9. **Testing**: Use `@MicronautTest` with `TestPropertyProvider` and Testcontainers for real database integration tests.

**Cross-references**: Micronaut core — `@501-frameworks-micronaut-core`. REST boundaries — `@502-frameworks-micronaut-rest`. Integration tests — `@522-frameworks-micronaut-testing-integration-tests`. Secure SQL patterns — `@124-java-secure-coding`.


## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any persistence refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Mapped entity
- Example 2: Repository interface
- Example 3: Custom @Query
- Example 4: Transactions
- Example 5: Pagination
- Example 6: DTO projections
- Example 7: Optimistic locking
- Example 8: Integration test wiring

### Example 1: Mapped entity

Title: @MappedEntity with @Id and @GeneratedValue
Description: Define persistence types with `@MappedEntity`. Match column names with `@Column` when Java names differ from the schema.

**Good example:**

```java
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import java.time.Instant;

@MappedEntity("customer")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @MappedProperty("email_address")
    private String email;

    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
```

**Bad example:**

```java
// Bad: missing @MappedEntity — repository cannot map reliably
public class Customer {
    private Long id;
    private String email;
}
```

### Example 2: Repository interface

Title: CrudRepository and derived finders
Description: Declare `@io.micronaut.data.annotation.Repository` on the interface and extend `CrudRepository`. Micronaut Data implements the interface at compile time.

**Good example:**

```java
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
}
```

**Bad example:**

```java
import io.micronaut.data.repository.CrudRepository;

// Bad: missing @Repository — not a Micronaut Data bean
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
```

### Example 3: Custom @Query

Title: Named parameters, no string concatenation
Description: Use `@Query` for SQL or JPQL (depending on dialect) with `:param` binding.

**Good example:**

```java
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer WHERE email LIKE :pattern")
    List<Customer> searchByEmailPattern(String pattern);
}
```

**Bad example:**

```java
// Anti-pattern: never build SQL with string concatenation from request data
// String sql = "SELECT * FROM customer WHERE email LIKE '%" + userInput + "%'";
// jdbcTemplate.query(sql, ...) — use @Query("... LIKE :pattern") with a bound parameter instead
```

### Example 4: Transactions

Title: @Transactional on application services
Description: Annotate service methods that coordinate multiple writes with `@Transactional` from `io.micronaut.transaction.annotation`.

**Good example:**

```java
import io.micronaut.context.annotation.Singleton;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;

@Singleton
public class OrderService {

    private final OrderRepository orders;
    private final OutboxRepository outbox;

    @Inject
    OrderService(OrderRepository orders, OutboxRepository outbox) {
        this.orders = orders;
        this.outbox = outbox;
    }

    @Transactional
    public Order place(Order order) {
        Order saved = orders.save(order);
        outbox.enqueue(new OrderPlacedEvent(saved.getId()));
        return saved;
    }
}
```

**Bad example:**

```java
import io.micronaut.context.annotation.Singleton;

@Singleton
public class OrderService {

    public Order place(Order order) {
        // Bad: two writes without a transaction — partial failure leaves inconsistent state
        return null;
    }
}
```

### Example 5: Pagination

Title: Page and Pageable
Description: Use `PageableRepository` or methods accepting `Pageable` returning `Page&lt;T&gt;` for list endpoints backed by large tables.

**Good example:**

```java
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface CustomerRepository extends PageableRepository<Customer, Long> {

    Page<Customer> findByEmailContains(String fragment, Pageable pageable);
}
```

**Bad example:**

```java
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@io.micronaut.data.annotation.Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    // Bad: unbounded list for potentially huge table
    List<Customer> findAll();
}
```

### Example 6: DTO projections

Title: Interface projections for reads
Description: Declare projection interfaces with getter signatures matching queried fields to reduce payload and mapping cost.

**Good example:**

```java
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

public interface CustomerSummary {
    Long getId();
    String getEmail();
}

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT id, email_address AS email FROM customer WHERE active = true")
    List<CustomerSummary> listActiveSummaries();
}
```

**Bad example:**

```java
// Bad: loading full Customer graph for a dropdown that only needs id + email
List<Customer> findAll();
```

### Example 7: Optimistic locking

Title: @Version column
Description: Add a `@Version` field when concurrent updates must conflict instead of silently overwriting.

**Good example:**

```java
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Version;

@MappedEntity("inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue
    private Long id;

    private int quantity;

    @Version
    private Long version;
}
```

**Bad example:**

```java
// Bad: hot row updated by two writers — last commit wins, stock incorrect
@MappedEntity("inventory_item")
public class InventoryItem {
    @Id @GeneratedValue private Long id;
    private int quantity;
}
```

### Example 8: Integration test wiring

Title: @MicronautTest and TestPropertyProvider
Description: For repository integration tests, run against a real database with Testcontainers and supply JDBC properties via `TestPropertyProvider`.

**Good example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import jakarta.inject.Inject;
import java.util.Map;

@MicronautTest(transactional = false)
@Testcontainers
class CustomerRepositoryIT implements TestPropertyProvider {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Inject
    CustomerRepository customers;

    @Override
    public Map<String, String> getProperties() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
        return Map.of(
            "datasources.default.url", postgres.getJdbcUrl(),
            "datasources.default.username", postgres.getUsername(),
            "datasources.default.password", postgres.getPassword(),
            "datasources.default.driver-class-name", "org.postgresql.Driver"
        );
    }

    @Test
    void savesAndFinds() {
        // exercise repository against real Postgres
    }
}
```

**Bad example:**

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
class CustomerRepositoryIT {
    // Bad: relies on shared developer DB on localhost — not reproducible in CI
}
```

## Output Format

- **ANALYZE** entities, repositories, queries, transactions, pagination, projections, and locking for Micronaut Data idioms and SQL safety
- **APPLY** improvements: correct annotations, repository method naming, parameterized `@Query`, transactional boundaries, bounded pagination, projections for hot reads, optimistic versioning where updates race
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive persistence changes

## Safeguards

- **SQL INJECTION**: Never build `@Query` strings from user input — use parameters and allow-lists for dynamic fragments
- **TRANSACTION BOUNDARIES**: Splitting writes across non-transactional calls risks partial commits
- **N+1**: Review list+lazy patterns; use fetch joins or batch queries for graphs
- **SCHEMA MIGRATIONS**: Entity changes require coordinated Flyway/Liquibase scripts — do not change `@MappedEntity` without migration
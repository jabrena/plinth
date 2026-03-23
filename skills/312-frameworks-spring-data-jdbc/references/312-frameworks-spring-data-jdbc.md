---
name: 312-frameworks-spring-data-jdbc
description: Use when you need to use Spring Data JDBC with Java records — including entity design with records, repository pattern, immutable updates, aggregate relationships, custom queries, transaction management, and avoiding N+1 problems.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Spring Data JDBC with Records

## Role

You are a Senior software engineer with extensive experience in Spring Data and Java persistence

## Goal

Spring Data JDBC maps rows to domain types with minimal magic: one repository call typically loads a whole aggregate in predictable SQL. Java records fit this model because they are immutable, constructor-friendly, and explicit. Success means correct `@Column`/`@Id` mapping, repositories that express intent through naming or `@Query`, small aggregates with `Set` children or foreign keys—not JPA-style graphs—and transactions declared at the service layer.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Immutability**: Model rows as records (or immutable types); use `with*` methods or new instances for updates instead of mutable entities.
2. **Simplicity**: Prefer Spring Data JDBC’s direct SQL and aggregate loading over rich ORM relationship graphs and lazy loading.
3. **Aggregate boundaries**: Treat one aggregate root per repository; use foreign keys between aggregates, `Set` for one-to-many inside the root, and junction/explicit entities for many-to-many—not bidirectional linked collections on both Student and Course (JPA-style).
4. **SQL and safety**: Use `@Query` with named parameters for non-trivial SQL; avoid concatenating user input into query strings.
5. **Transactions and performance**: Put `@Transactional` on services (`readOnly` where appropriate); rely on single-query aggregate loading instead of JPA-style N+1 patterns or manual fan-out queries across child repositories.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any Spring Data JDBC refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with persistence changes
- **NO EXCEPTIONS**: Under no circumstances should data-access recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Records as JDBC entities
- Example 2: Repository interfaces
- Example 3: Updates with immutable records
- Example 4: Aggregate relationships
- Example 5: Custom @Query usage
- Example 6: Transaction boundaries
- Example 7: Single-query aggregate loading

### Example 1: Records as JDBC entities

Title: Map columns explicitly; use @PersistenceCreator when multiple constructors exist
Description: Prefer records for thread-safe, concise persistence types. Use `@Column` when names differ from properties. If you add extra constructors, mark the persistence-facing one with `@PersistenceCreator`. Use factories like `of(...)` for new rows before insert.

**Good example:**

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;

public record Customer(
    @Id
    @Column("customer_id")
    Long id,
    @Column("first_name")
    String firstName,
    @Column("last_name")
    String lastName,
    @Column("email_address")
    String email,
    @Column("created_at")
    LocalDateTime createdAt
) {
    @PersistenceCreator
    public Customer(Long id, String firstName, String lastName, String email, LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static Customer of(String firstName, String lastName, String email) {
        return new Customer(null, firstName, lastName, email, LocalDateTime.now());
    }
}
```

**Bad example:**

```java
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

// Multiple constructors without @PersistenceCreator — mapping is ambiguous
public record Customer(
    @Id Long id,
    String firstName,
    String lastName,
    String email,
    LocalDateTime createdAt
) {
    public Customer(Long id, String firstName, String lastName, String email, LocalDateTime createdAt) {
        this(id, firstName, lastName, email, createdAt);
    }

    public Customer(String firstName, String lastName, String email) {
        this(null, firstName, lastName, email, LocalDateTime.now());
    }
}

// Mutable entity: boilerplate and accidental mutation
public class CustomerEntity {
    @Id
    private Long id;
    private String email;
    // getters/setters — easy to leave entity in inconsistent state
}
```

### Example 2: Repository interfaces

Title: Extend CrudRepository; derive methods or use @Query with parameters
Description: Annotate interfaces with `@Repository`, extend `CrudRepository` or `PagingAndSortingRepository`, use query derivation names that match property paths, and bind `@Query` parameters with `@Param`—never hardcode volatile literals for user data.

**Good example:**

```java
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    Optional<Customer> findByEmail(String email);

    @Query("SELECT * FROM customer WHERE email LIKE :pattern")
    List<Customer> findByEmailPattern(@Param("pattern") String pattern);
}
```

**Bad example:**

```java
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

// Missing @Repository; non-idiomatic names; unsafe literal in SQL
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> getCustomersWithLastName(String lastName);

    @Query("SELECT * FROM customer WHERE email LIKE '%@gmail.com%'")
    List<Customer> findGmailUsers();
}
```

### Example 3: Updates with immutable records

Title: Return new instances via with* methods; save in a transaction
Description: Records cannot be mutated in place. Expose `withEmail`, `withName`, or similar methods that copy fields and replace what changed, then `save` the new instance inside a transactional service.

**Good example:**

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

public record Customer(
    @Id @Column("customer_id") Long id,
    @Column("first_name") String firstName,
    @Column("last_name") String lastName,
    @Column("email_address") String email,
    @Column("created_at") LocalDateTime createdAt
) {
    public Customer withEmail(String newEmail) {
        return new Customer(id, firstName, lastName, newEmail, createdAt);
    }
}

@Service
class CustomerService {

    private final CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    Customer updateCustomerEmail(Long customerId, String newEmail) {
        return customerRepository.findById(customerId)
            .map(c -> c.withEmail(newEmail))
            .map(customerRepository::save)
            .orElseThrow(() -> new IllegalArgumentException("customer: " + customerId));
    }
}

interface CustomerRepository extends org.springframework.data.repository.CrudRepository<Customer, Long> {
    java.util.Optional<Customer> findById(Long id);
}
```

**Bad example:**

```java
import org.springframework.transaction.annotation.Transactional;

// Attempting to "mutate" a record is invalid — no setters on components
// Anti-pattern: partial re-construction that drops fields or bypasses with* helpers
@Transactional
class CustomerService {

    Customer updateEmail(Long id, String email, CustomerRepository repo) {
        Customer c = repo.findById(id).orElseThrow();
        return repo.save(new Customer(c.id(), email)); // wrong arity / lost fields if record has more components
    }
}

record Customer(Long id, String firstName, String lastName, String email, java.time.LocalDateTime createdAt) {
    Customer(Long id, String email) {
        this(id, null, null, email, null);
    }
}
interface CustomerRepository extends org.springframework.data.repository.CrudRepository<Customer, Long> {
    java.util.Optional<Customer> findById(Long id);
}
```

### Example 4: Aggregate relationships

Title: Sets inside the root; FK ids across aggregates; avoid bidirectional graphs
Description: Model one-to-many as `Set<Child>` on the aggregate root loaded with the root. Use scalar foreign keys (`Long customerId`) for references to other aggregates. Many-to-many: junction entity or denormalization—not `Set<Course>` on `Student` and `Set<Student>` on `Course`. Do not expose separate repositories for every line item if the line is part of the order aggregate.

**Good example:**

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record Order(
    @Id @Column("order_id") Long id,
    @Column("customer_id") Long customerId,
    @Column("order_date") LocalDateTime orderDate,
    Set<OrderItem> items
) {}

public record OrderItem(
    @Id @Column("item_id") Long id,
    @Column("product_name") String productName,
    @Column("price") BigDecimal price,
    @Column("quantity") int quantity
) {}

// Junction-style link entity for many-to-many style data
public record Enrollment(
    @Id @Column("enrollment_id") Long id,
    @Column("student_id") Long studentId,
    @Column("course_id") Long courseId,
    @Column("enrolled_at") LocalDateTime enrolledAt
) {}
```

**Bad example:**

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import java.util.Set;

// Full object graph / bidirectional — breaks aggregate boundaries for JDBC
public record Order(
    @Id @Column("order_id") Long id,
    Customer customer,
    Set<OrderItem> items
) {}

public record OrderItem(
    @Id @Column("item_id") Long id,
    String productName,
    Order order
) {}

// Direct many-to-many sets — not supported like JPA
public record Student(@Id Long id, String name, Set<Course> courses) {}
public record Course(@Id Long id, String title, Set<Student> students) {}

// Child repository for entities that belong to Order aggregate
interface OrderItemRepository extends org.springframework.data.repository.CrudRepository<OrderItem, Long> {
    java.util.List<OrderItem> findByOrderId(Long orderId);
}
```

### Example 5: Custom @Query usage

Title: Parameterized SQL; @Modifying for updates
Description: Use text-block or string SQL with `@Param` bindings. For updates/deletes, add `@Modifying` where required. Split oversized multi-join reports into views, separate reads, or bounded DTO queries instead of `List<Object[]>`.

**Good example:**

```java
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("""
        SELECT c.* FROM customer c
        JOIN orders o ON c.id = o.customer_id
        WHERE o.order_date BETWEEN :startDate AND :endDate
        GROUP BY c.id
        HAVING COUNT(o.id) >= :minOrders
        """)
    List<Customer> findActiveCustomers(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("minOrders") int minOrders
    );

    @Modifying
    @Query("UPDATE customer SET email = :email WHERE id = :id")
    void updateCustomerEmail(@Param("id") Long id, @Param("email") String email);
}
```

**Bad example:**

```java
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    // Never build SQL by concatenating untrusted input
    @Query("SELECT * FROM customer WHERE email = :email")
    Customer findByEmailUnsafe(@org.springframework.data.repository.query.Param("email") String email);

    // Over-wide join returning Object[] — hard to maintain
    @Query("""
        SELECT c.*, o.*, oi.* FROM customer c
        LEFT JOIN orders o ON c.id = o.customer_id
        LEFT JOIN order_item oi ON o.id = oi.order_id
        WHERE c.created_at > :d
        """)
    List<Object[]> findEverything(@org.springframework.data.repository.query.Param("d") LocalDateTime date);
}
```

### Example 6: Transaction boundaries

Title: @Transactional on services; readOnly for queries
Description: Declare transactions at the service layer. Use `readOnly = true` on classes or methods that only read. Avoid relying on implicit per-call auto-commit for multi-step writes.

**Good example:**

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class CustomerService {

    private final CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    Customer createCustomer(String firstName, String lastName, String email) {
        return customerRepository.save(Customer.of(firstName, lastName, email));
    }

    @Transactional
    Customer updateCustomerEmail(Long customerId, String newEmail) {
        return customerRepository.findById(customerId)
            .map(c -> c.withEmail(newEmail))
            .map(customerRepository::save)
            .orElseThrow();
    }
}
```

**Bad example:**

```java
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

// No class-level transaction; each repository call may commit independently
class CustomerService {

    private final CustomerRepository customerRepository;

    Customer createCustomer(String firstName, String lastName, String email) {
        return customerRepository.save(Customer.of(firstName, lastName, email));
    }

    // Read path marked @Transactional without readOnly — misses optimization hint
    @Transactional
    Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
```

### Example 7: Single-query aggregate loading

Title: Prefer Spring Data JDBC aggregate load over JPA lazy N+1 or manual fan-out
Description: Loading an aggregate root typically runs one SQL statement with joins for its collection. Iterate `order.items()` without expecting extra lazy queries (unlike JPA `LAZY`). Design aggregates so that payload size stays acceptable; avoid pulling huge graphs in one root.

**Good example:**

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record Order(
    @Id @Column("order_id") Long id,
    @Column("customer_id") Long customerId,
    @Column("order_date") LocalDateTime orderDate,
    @Column("total_amount") BigDecimal totalAmount,
    Set<OrderItem> items
) {}

public record OrderItem(
    @Id @Column("item_id") Long id,
    @Column("unit_price") BigDecimal unitPrice,
    @Column("quantity") int quantity
) {}

@Service
@Transactional(readOnly = true)
class OrderService {

    private final OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    List<OrderSummary> getCustomerOrderSummaries(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
            .map(order -> new OrderSummary(
                order.id(),
                order.orderDate(),
                order.totalAmount(),
                order.items().size(),
                order.items().stream()
                    .mapToDouble(i -> i.unitPrice().doubleValue() * i.quantity())
                    .sum()
            ))
            .toList();
    }
}

record OrderSummary(Long orderId, LocalDateTime orderDate, BigDecimal totalAmount, int itemCount, double lineTotal) {}

interface OrderRepository extends org.springframework.data.repository.CrudRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
}
```

**Bad example:**

```java
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Manual two-step load and map — error-prone when JDBC already loads the aggregate
@Service
class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    List<OrderSummary> getCustomerOrderSummaries(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<Long> ids = orders.stream().map(Order::id).toList();
        List<OrderItem> allItems = orderItemRepository.findByOrderIdIn(ids);
        Map<Long, List<OrderItem>> byOrder = allItems.stream()
            .collect(Collectors.groupingBy(OrderItem::orderId));
        return orders.stream()
            .map(o -> new OrderSummary(o.id(), o.orderDate(), o.totalAmount(), byOrder.getOrDefault(o.id(), List.of()).size(), 0))
            .toList();
    }
}

record Order(Long id, java.time.LocalDateTime orderDate, java.math.BigDecimal totalAmount) {}

record OrderItem(Long id, Long orderId, java.math.BigDecimal unitPrice, int quantity) {}

interface OrderRepository extends org.springframework.data.repository.CrudRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
}

interface OrderItemRepository {
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}

record OrderSummary(Long orderId, java.time.LocalDateTime orderDate, java.math.BigDecimal totalAmount, int itemCount, double lineTotal) {}

// JPA-style mental model: lazy collections causing N+1 if misapplied
// @Entity class Order { @jakarta.persistence.OneToMany(fetch = jakarta.persistence.FetchType.LAZY) Set<OrderItem> items; }
```

## Output Format

- **ANALYZE** persistence code: record/entity mapping, repository APIs, aggregate shape, `@Query` safety, transaction placement, and load patterns (single query vs extra round-trips)
- **CATEGORIZE** issues by impact (CORRECTNESS, PERFORMANCE, MAINTAINABILITY) and by layer (entity mapping, repository, service/transaction, aggregate design)
- **APPLY** Spring Data JDBC–aligned fixes: add `@Column`/`@Id`, narrow repositories, introduce `with*` updates, reshape aggregates and FKs, parameterize SQL, move `@Transactional` to services with `readOnly` where fit
- **IMPLEMENT** changes so schema, aggregates, and tests stay consistent; prefer migrations and integration tests for repository behavior
- **EXPLAIN** trade-offs (aggregate size vs query size, explicit SQL vs derived queries)
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` or `mvn compile` before ANY persistence refactoring
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes; exercise repository integration tests
- **DATA SAFETY**: Review DDL and migration impact before changing aggregate boundaries or `@Query` updates
- **SQL INJECTION**: Never concatenate user input into `@Query` strings; use parameters
- **INCREMENTAL SAFETY**: Change one aggregate or repository surface at a time when possible
- **SAFETY PROTOCOL**: Stop if compilation or data tests fail after edits
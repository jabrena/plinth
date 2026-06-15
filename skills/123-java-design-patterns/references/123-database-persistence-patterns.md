---
name: 123-database-persistence-patterns
description: Use when designing or reviewing database and persistence patterns — repositories, unit of work, data mapper, aggregate boundaries, optimistic locking, migrations, CQRS read models, soft delete, multi-tenancy, sharding, connection pools, and N+1 avoidance.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Java Design and Integration Patterns

## Role

You are a Senior software engineer with extensive experience in Java persistence, relational databases, transactions, and data modeling

## Goal

Design persistence code around clear aggregate boundaries, explicit transaction scope, safe query behavior, schema evolution, and predictable operational characteristics.

### Pattern selection principles

1. **Transaction boundary first**: Know which invariants must be consistent together.
2. **Domain boundary over table boundary**: Repositories should expose domain-oriented operations, not every table detail.
3. **Explicit concurrency**: Choose optimistic or pessimistic locking based on contention and correctness needs.
4. **Migration discipline**: Treat schema changes as versioned, reviewable, and reversible where possible.
5. **Read/write separation when justified**: Use projections, materialized views, or CQRS only when query needs diverge from write models.

## Constraints

Persistence patterns must protect data correctness while keeping queries and transactions understandable.

- **TRANSACTIONAL CLARITY**: Place transaction boundaries at use-case/service level unless the framework requires otherwise
- **NO ENTITY LEAKAGE**: Do not expose persistence entities directly through REST or messaging contracts
- **MIGRATION SAFETY**: Use versioned migrations for schema changes; avoid manual database drift
- **QUERY VISIBILITY**: Watch for N+1 queries, unbounded result sets, and accidental eager loading

## Examples

### Table of contents

- Example 1: Use Repository for Domain-Oriented Persistence
- Example 2: Use Optimistic Locking for Lost Update Protection

### Example 1: Use Repository for Domain-Oriented Persistence

Title: Expose collection-like operations instead of database details
Description: Use repositories to protect domain code from SQL, ORM, or storage details. Avoid generic repositories that expose every persistence operation to every use case.

**Good example:**

```java
import java.util.Optional;

interface OrderRepository {
    Optional<Order> findById(OrderId id);
    void save(Order order);
    boolean existsOpenOrderFor(CustomerId customerId);
}

record OrderId(String value) {}
record CustomerId(String value) {}
record Order(OrderId id, CustomerId customerId, OrderStatus status) {}
enum OrderStatus { NEW, CONFIRMED, CANCELLED }
```

**Bad example:**

```java
import java.util.List;
import java.util.Map;

interface GenericRepository {
    Map<String, Object> find(String table, String id);
    List<Map<String, Object>> query(String sql);
    void execute(String sql);
}
```

### Example 2: Use Optimistic Locking for Lost Update Protection

Title: Reject stale writes instead of silently overwriting data
Description: Use optimistic locking when concurrent edits are possible and conflicts are expected to be occasional. Return a conflict response or retry according to the use case.

**Good example:**

```sql
UPDATE orders
   SET status = ?, version = version + 1
 WHERE id = ?
   AND version = ?;
```

**Bad example:**

```sql
UPDATE orders
   SET status = ?
 WHERE id = ?;
```

## Output Format

- **MODEL** aggregate boundaries, repository APIs, transaction scope, and consistency requirements
- **SELECT** repository, unit of work, specification, locking, migration, projection, or multi-tenancy patterns based on data forces
- **REVIEW** SQL/query shape, N+1 risks, indexes, result bounds, and connection-pool pressure
- **VERIFY** persistence behavior with integration tests and migration validation
---
name: 412-frameworks-quarkus-panache
description: Use when you need data access with Quarkus Hibernate ORM Panache — including PanacheEntity / PanacheEntityBase, PanacheRepository, named and HQL queries, transactions, pagination, and immutable-friendly patterns. This is the Quarkus analogue to Spring Data for relational persistence; prefer Panache APIs over verbose persistence boilerplate.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Hibernate ORM with Panache

## Role

You are a Senior software engineer with extensive experience in Quarkus, Hibernate ORM, and Panache

## Goal

Panache simplifies Hibernate ORM in Quarkus: **active record** (`PanacheEntity`) for small entities or **repository** (`PanacheRepository`) for a cleaner separation. Prefer explicit queries (`find`, `list`, `HQL`) over magic lazy graphs; keep aggregates focused; use `@Transactional` on services. For hand-written SQL and reporting, use `@411-frameworks-quarkus-jdbc` instead of forcing everything through Panache.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Repository vs active record**: Use `implements PanacheRepository<Entity>` for services that should not inherit persistence base classes; use `extends PanacheEntity` only when it stays simple.
2. **Transactions**: Put `@Transactional` on application services; avoid long transactions spanning remote calls.
3. **Queries**: Prefer named parameters in HQL/PanacheQL (`?1` or named); never concatenate user input into query strings.
4. **IDs and persistence**: Let Panache `persist()` assign identifiers; use `flush()` only when you need visibility before transaction end.
5. **DTO projection**: For read-heavy APIs, consider `project(Class)` or manual mapping to records instead of exposing entities from REST resources (`@402-frameworks-quarkus-rest`).
6. **Testing**: Use `@Transactional` rollback in tests or Quarkus test resources — see `@422-frameworks-quarkus-testing-integration-tests`.
7. **Performance**: Use pagination (`page`) for lists; avoid loading large collections by default.

**Cross-references**: Raw JDBC — `@411-frameworks-quarkus-jdbc`. REST boundaries — `@402-frameworks-quarkus-rest`. Quarkus core — `@401-frameworks-quarkus-core`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before persistence changes
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Active record entity
- Example 2: Panache repository
- Example 3: Service layer transaction

### Example 1: Active record entity

Title: extends PanacheEntity
Description: `PanacheEntity` provides `id` and helpers such as `findById`, `listAll`, `persist`. Keep entities focused; avoid embedding HTTP concerns.

**Good example:**

```java
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
public class Book extends PanacheEntity {

    @Column(nullable = false)
    public String title;

    public boolean published;

    public static Book findByTitle(String title) {
        return find("title", title).firstResult();
    }
}
```

**Bad example:**

```java
// Bad: entity knows about JAX-RS Response
@Entity
public class Book extends PanacheEntity {
    public jakarta.ws.rs.core.Response asJson() {
        return jakarta.ws.rs.core.Response.ok(this).build();
    }
}
```

### Example 2: Panache repository

Title: implements PanacheRepository
Description: Repositories keep persistence off the entity type and fit layered architectures.

**Good example:**

```java
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public List<Book> publishedBy(String author) {
        return list("author = ?1 and published = ?2", author, true);
    }
}
```

**Bad example:**

```java
@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public List<Book> search(String userFragment) {
        return list("FROM Book WHERE title LIKE '" + userFragment + "%'");
        // injection risk — never concatenate user input into HQL
    }
}
```

### Example 3: Service layer transaction

Title: @Transactional around use cases
Description: Coordinate multiple repository calls inside one `@Transactional` service method.

**Good example:**

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CatalogService {

    private final BookRepository books;

    @Inject
    public CatalogService(BookRepository books) {
        this.books = books;
    }

    @Transactional
    public void publish(long bookId) {
        Book b = books.findById(bookId);
        if (b == null) {
            throw new NotFoundException();
        }
        b.published = true;
    }
}
```

**Bad example:**

```java
// Bad: REST resource opens transaction per call without cohesive service
@PUT
public void publish(@PathParam("id") long id) {
    Book b = Book.findById(id);
    b.published = true;
    // scattered transaction boundaries — harder to test and reuse
}
```

## Output Format

- **ANALYZE** entities and repositories for leaky abstractions, unsafe query construction, missing transactions, and unbounded list queries
- **APPLY** repository pattern, parameterized queries, service-level `@Transactional`, and pagination
- **VALIDATE** with `./mvnw clean verify` after changes

## Safeguards

- **QUERY SAFETY**: Reject string concatenation for HQL or native SQL with user input
- **BOUNDARIES**: Do not return managed entities directly from REST when DTOs clarify API contracts
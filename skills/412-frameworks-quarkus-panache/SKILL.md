---
name: 412-frameworks-quarkus-panache
description: Use when you need data access with Quarkus Hibernate ORM Panache — including PanacheEntity / PanacheEntityBase, PanacheRepository, named and HQL queries, transactions, pagination, and immutable-friendly patterns. This is the Quarkus analogue to Spring Data for relational persistence. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Hibernate ORM with Panache

Apply Panache patterns for Hibernate ORM in Quarkus.

**What is covered in this Skill?**

- Active record (PanacheEntity) vs PanacheRepository
- Parameterized HQL / Panache queries (no unsafe concatenation)
- @Transactional application services
- Pagination and avoiding unbounded list loads
- Mapping entities vs exposing DTOs at REST boundaries
- Pairing with `@411` for raw SQL when needed

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Compile before persistence changes; verify after.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules and examples

## When to use this skill

- Review Panache entities or repositories in Quarkus
- Improve Hibernate ORM data access with Panache

## Reference

For detailed guidance, examples, and constraints, see [references/412-frameworks-quarkus-panache.md](references/412-frameworks-quarkus-panache.md).

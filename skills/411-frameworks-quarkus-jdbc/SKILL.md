---
name: 411-frameworks-quarkus-jdbc
description: Use when you need to write or review programmatic JDBC in Quarkus — including Agroal-backed DataSource injection, PreparedStatement with bind parameters, mapping rows to Java records, transactions (@Transactional), batch updates, and optional Spring-style JdbcTemplate when the extension is present. Prefer explicit SQL without ORM. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus JDBC — programmatic SQL

Apply programmatic JDBC patterns in Quarkus with safe SQL and clear transactions.

**What is covered in this Skill?**

- Injected javax.sql.DataSource and Agroal pooling
- PreparedStatement, try-with-resources, and record mapping
- @Transactional service boundaries
- Dev Services for databases in dev/test
- When to prefer Panache (`@412`) vs raw JDBC

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Compile before JDBC refactors; verify after changes.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules and examples

## When to use this skill

- Review JDBC or SQL data access in a Quarkus project
- Improve transactions and parameter binding for Quarkus JDBC

## Reference

For detailed guidance, examples, and constraints, see [references/411-frameworks-quarkus-jdbc.md](references/411-frameworks-quarkus-jdbc.md).

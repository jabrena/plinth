---
name: 402-frameworks-quarkus-rest
description: Use when you need to design, review, or improve REST APIs with Quarkus REST (Jakarta REST) — including resource classes, HTTP methods, status codes, request/response DTOs, Bean Validation, exception mappers, OpenAPI with SmallRye, content negotiation, pagination, and security-aware boundaries. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus REST API Guidelines

Apply REST API design principles on Quarkus using Jakarta REST (JAX-RS).

**What is covered in this Skill?**

- Resource classes, @Path, HTTP method mapping
- Status codes, Location headers, and Response building
- DTOs and Bean Validation at the boundary
- ExceptionMapper for consistent error JSON
- OpenAPI documentation (MicroProfile OpenAPI)
- Reactive vs blocking considerations on Quarkus
- Security integration at the filter layer

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Before applying REST changes, ensure the project compiles. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules and examples

## When to use this skill

- Review or improve JAX-RS resources in a Quarkus project
- Design HTTP APIs with validation and error handling on Quarkus

## Reference

For detailed guidance, examples, and constraints, see [references/402-frameworks-quarkus-rest.md](references/402-frameworks-quarkus-rest.md).

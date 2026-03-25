---
name: 422-frameworks-quarkus-testing-integration-tests
description: Use when you need to write or improve integration tests for Quarkus — including @QuarkusTest, Dev Services, Testcontainers, @QuarkusIntegrationTest, REST Assured, persistence with @Transactional rollback, and Failsafe *IT naming. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus integration testing

Apply integration testing patterns for Quarkus with real wiring and reproducible infrastructure.

**What is covered in this Skill?**

- @QuarkusTest for in-JVM integration
- Dev Services vs explicit Testcontainers
- HTTP testing with REST Assured and test URLs
- Database tests with @Transactional rollback or explicit fixtures
- *IT naming and Failsafe vs Surefire
- Native-image test considerations

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Compile before changes; verify after; Docker may be required for containers.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules and examples

## When to use this skill

- Add or improve integration tests in a Quarkus project
- Configure Testcontainers or Dev Services for Quarkus tests

## Reference

For detailed guidance, examples, and constraints, see [references/422-frameworks-quarkus-testing-integration-tests.md](references/422-frameworks-quarkus-testing-integration-tests.md).

---
name: 421-frameworks-quarkus-testing-unit-tests
description: Use when you need to write fast unit tests for Quarkus applications — including pure tests with @ExtendWith(MockitoExtension.class), @QuarkusTest with @InjectMock, REST Assured for resource-focused tests, and parameterized tests. For framework-agnostic Java use @131-java-testing-unit-testing. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus unit testing

Apply fast testing strategies for Quarkus: Mockito-first, QuarkusTest when CDI wiring matters.

**What is covered in this Skill?**

- Pure JUnit 5 + Mockito without container boot
- @QuarkusTest with @InjectMock / @InjectSpy for CDI-focused tests
- REST Assured for HTTP-level tests when appropriate
- Test profiles and %test configuration
- When to escalate to integration tests (`@422`)

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Compile before test refactors; verify the full suite after.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules and examples

## When to use this skill

- Add or improve unit tests in a Quarkus project
- Reduce slow @QuarkusTest usage with Mockito-first tests

## Reference

For detailed guidance, examples, and constraints, see [references/421-frameworks-quarkus-testing-unit-tests.md](references/421-frameworks-quarkus-testing-unit-tests.md).

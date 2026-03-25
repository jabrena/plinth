---
name: 423-frameworks-quarkus-testing-acceptance-tests
description: Use when you need to implement acceptance tests from a Gherkin .feature file for Quarkus applications — including @acceptance scenarios, @QuarkusTest, REST Assured, Testcontainers or Dev Services, and WireMock for external HTTP. Requires the .feature file in context. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus acceptance tests from Gherkin

Implement happy-path acceptance tests from Gherkin for Quarkus using real HTTP and infrastructure.

**What is covered in this Skill?**

- Preconditions: .feature file in context; Quarkus project
- Parsing scenarios tagged @acceptance / @acceptance-tests
- Base test setup: @QuarkusTest, Dev Services or Testcontainers, WireMock
- REST Assured against the Quarkus test HTTP endpoint
- Surefire/Failsafe naming (*AT suffix)
- Scope: happy path unless user requests negatives

**Scope:** Apply recommendations based on the reference rules and step workflow.

## Constraints

Do not generate without a .feature file; compile before and verify after.

- **PRECONDITION**: Gherkin `.feature` file must be in context
- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed steps and safeguards

## When to use this skill

- Implement Quarkus acceptance tests from a Gherkin feature file
- Set up BaseAcceptanceTest with Testcontainers and WireMock for Quarkus

## Reference

For detailed guidance, examples, and constraints, see [references/423-frameworks-quarkus-testing-acceptance-tests.md](references/423-frameworks-quarkus-testing-acceptance-tests.md).

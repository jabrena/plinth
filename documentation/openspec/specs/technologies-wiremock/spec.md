# technologies-wiremock Specification

## Purpose

Define requirements for **`702-technologies-wiremock`**: framework-agnostic WireMock guidance in `skills-generator`, distinct from framework integration-test skills (`322` / `422` / `522`), from framework-agnostic integration testing with WireMock (`132`), and from OpenAPI contract design (`701`).

## Requirements
### Requirement: Skill Identifier

The repository SHALL define the framework-agnostic WireMock guidance skill identifier as `702-technologies-wiremock`.

#### Scenario: Skill identifier is standardized

- **Given** maintainers implement WireMock best-practices guidance in generator sources
- **When** they create or reference the skill in XML, inventories, or documentation
- **Then** the identifier used is `702-technologies-wiremock`
- **And** references are consistent across OpenSpec artifacts and GitHub issue [#544](https://github.com/jabrena/cursor-rules-java/issues/544)

### Requirement: Framework-Agnostic Scope

The `702-technologies-wiremock` system prompt and skill SHALL focus on WireMock topics that apply regardless of Java HTTP framework or test bootstrap.

#### Scenario: Guidance does not require a framework choice

- **Given** a user asks for WireMock best practices without naming Spring Boot, Quarkus, or Micronaut
- **When** the agent applies `702-technologies-wiremock`
- **Then** recommendations address portable stubbing concerns (for example isolation, mapping structure, matching rules, response bodies, lifecycle, verification, and test flakiness avoidance)
- **And** framework-specific test integration (for example `@SpringBootTest`, `@QuarkusTest`, `@MicronautTest`, extension-specific setup) is deferred to `@132-java-testing-integration-testing`, `@322-frameworks-spring-boot-testing-integration-tests`, `@422-frameworks-quarkus-testing-integration-tests`, or `@522-frameworks-micronaut-testing-integration-tests` where relevant

### Requirement: Complement Existing Integration-Test Skills

The new skill SHALL complement, not replace, framework integration-test skills: those skills MAY continue to document WireMock in their stack context; **702** SHALL emphasize **cross-stack** WireMock practices shared across tests.

#### Scenario: Clear separation from OpenAPI technology skill

- **Given** a contributor searches for HTTP contract or stubbing guidance
- **When** they compare skill descriptions and triggers
- **Then** `702-technologies-wiremock` is distinct from `701-technologies-openapi` (stub server behavior vs OpenAPI contract artifact design)
- **And** inventory entries describe non-overlapping primary use cases

### Requirement: Generator Integration

New sources for **702** MUST be registered in both `skill-inventory.json` and `system-prompt-inventory.json`, and the `skills-generator` module MUST build successfully after the change.

#### Scenario: Maven verify passes

- **Given** the XML sources and inventory entries for **702** are added
- **When** a maintainer runs `./mvnw clean verify -pl skills-generator`
- **Then** the build completes without failure
- **And** regenerated skill output reflects the new id where the pipeline emits skills


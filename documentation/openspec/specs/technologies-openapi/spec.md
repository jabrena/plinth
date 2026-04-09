# technologies-openapi Specification

## Purpose

Define requirements for **`701-technologies-openapi`**: framework-agnostic OpenAPI 3.x contract guidance in `skills-generator`, distinct from framework REST skills (`302` / `402` / `502`) and from CATS fuzz testing (`134`).

## Requirements
### Requirement: Skill Identifier

The repository SHALL define the framework-agnostic OpenAPI guidance skill identifier as `701-technologies-openapi`.

#### Scenario: Skill identifier is standardized

- **Given** maintainers implement OpenAPI best-practices guidance in generator sources
- **When** they create or reference the skill in XML, inventories, or documentation
- **Then** the identifier used is `701-technologies-openapi`
- **And** references are consistent across OpenSpec artifacts and GitHub issue [#537](https://github.com/jabrena/cursor-rules-java/issues/537)

### Requirement: Framework-Agnostic Scope

The `701-technologies-openapi` system prompt and skill SHALL focus on OpenAPI/Swagger contract quality and lifecycle topics that apply regardless of Java HTTP framework.

#### Scenario: Guidance does not require a framework choice

- **Given** a user asks for OpenAPI best practices without naming Spring Boot, Quarkus, or Micronaut
- **When** the agent applies `701-technologies-openapi`
- **Then** recommendations address the specification artifact (e.g. structure, versioning, documentation, validation, security schemes, examples, breaking changes)
- **And** framework-specific runtime configuration is deferred to `@302-frameworks-spring-boot-rest`, `@402-frameworks-quarkus-rest`, or `@502-frameworks-micronaut-rest` where relevant

### Requirement: Complement Existing REST Skills

The new skill SHALL complement, not duplicate, framework REST skills: framework skills MAY continue to document OpenAPI Generator and runtime exposure; **701** SHALL emphasize contract-first principles shared across stacks.

#### Scenario: Clear separation from fuzz testing skill

- **Given** a contributor searches for OpenAPI-related automation
- **When** they compare skill descriptions and triggers
- **Then** `701-technologies-openapi` is distinct from `134-java-testing-fuzzing-testing` (contract design vs CATS fuzz execution)
- **And** inventory entries describe non-overlapping primary use cases

### Requirement: Generator Integration

New sources for **701** MUST be registered in both `skill-inventory.xml` and `system-prompt-inventory.xml`, and the `skills-generator` module MUST build successfully after the change.

#### Scenario: Maven verify passes

- **Given** the XML sources and inventory entries for **701** are added
- **When** a maintainer runs `./mvnw clean verify -pl skills-generator`
- **Then** the build completes without failure
- **And** regenerated skill output reflects the new id where the pipeline emits skills


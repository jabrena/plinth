# openapi-technology-skill-reference Specification

## Purpose
TBD - created by archiving change improve-701-openapi-guidance. Update Purpose after archive.
## Requirements
### Requirement: OpenAPI guidance includes sufficient contract examples

The repository MUST improve `701-technologies-openapi` so its reference examples cover the main OpenAPI contract concerns advertised by the skill.

#### Scenario: Reference contains at least eight examples

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it includes at least eight top-level examples
- **AND** the examples cover metadata/versioning, operations/errors, reusable schemas, parameters, error models, security schemes, examples/content types, and validation or breaking-change gates

#### Scenario: Schema design example exists

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it shows reusable `components.schemas`
- **AND** it demonstrates required fields, formats or patterns, enums where appropriate, nullability or optionality, and realistic examples

#### Scenario: Parameter design example exists

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it shows path, query, or header parameters with explicit `required`, `schema`, and examples
- **AND** it covers pagination, filtering, or sorting where applicable

#### Scenario: Error contract example exists

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it shows a reusable Problem Details-style schema or equivalent error envelope
- **AND** it demonstrates validation, conflict, not-found, or authorization error responses with `application/problem+json` where appropriate

#### Scenario: Security scheme example exists

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it shows `components.securitySchemes`
- **AND** it explains global versus operation-level security requirements
- **AND** it warns against documenting optional authentication when the runtime enforces authentication

#### Scenario: Breaking-change and validation example exists

- **WHEN** generated `references/701-technologies-openapi.md` is inspected
- **THEN** it distinguishes additive changes from breaking changes such as removed paths, changed status semantics, newly required fields, incompatible enum changes, or schema type changes
- **AND** it recommends linting or validation gates such as Spectral-style rules, schema validation, or pre-codegen checks without requiring one specific external tool in all projects

### Requirement: OpenAPI skill stays framework-agnostic

The `701-technologies-openapi` skill MUST stay focused on contract quality and avoid framework runtime implementation guidance unless explicitly requested.

#### Scenario: Framework implementation is routed away

- **GIVEN** a user asks for controller binding, server stub implementation, or runtime OpenAPI exposure
- **WHEN** `701-technologies-openapi` is applied
- **THEN** the skill identifies that the concern is outside pure OpenAPI contract quality
- **AND** it routes Spring Boot concerns to REST/framework skills, Quarkus concerns to REST/framework skills, and Micronaut concerns to REST/framework skills as appropriate

### Requirement: Acceptance coverage exists for 701

The repository MUST add acceptance coverage for `701-technologies-openapi`.

#### Scenario: Gherkin feature covers OpenAPI contract review

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/701-technologies-openapi.feature` is inspected
- **THEN** it includes a scenario requiring the skill to read `references/701-technologies-openapi.md`
- **AND** it verifies concrete findings for schemas, parameters, errors, security, examples, or breaking changes
- **AND** it verifies the skill proposes OpenAPI YAML or JSON snippets
- **AND** it verifies the skill avoids framework-specific controller or runtime wiring recommendations

#### Scenario: Acceptance prompt inventory includes 701

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes a `701-technologies-openapi` entry
- **AND** the entry uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/701-technologies-openapi` output is refreshed only by running the documented generator command


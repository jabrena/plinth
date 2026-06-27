# wiremock-technology-skill-reference Specification

## Purpose
TBD - created by archiving change improve-702-wiremock-guidance. Update Purpose after archive.
## Requirements
### Requirement: WireMock guidance includes sufficient stub-quality examples

The repository MUST improve `702-technologies-wiremock` so its reference examples cover the main WireMock concerns advertised by the skill.

#### Scenario: Reference contains at least seven examples

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it includes at least seven top-level examples
- **AND** the examples cover isolation/reset, precise matching and verification, programmatic Java DSL stubs, `bodyFileName` fixtures, dynamic ports/base URL propagation, request journal or unmatched request debugging, and fault or delay scenarios

#### Scenario: Programmatic Java DSL example exists

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it shows a Java DSL stub using method, URL path, headers, query parameters, or body matching
- **AND** it pairs the stub with verification where appropriate

#### Scenario: Fixture layout example exists

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it shows `bodyFileName` usage for reusable or large responses
- **AND** it documents `wiremock/mappings` and `wiremock/__files` or equivalent classpath layout
- **AND** it warns against embedding large response bodies inline in many mappings when a fixture file would be clearer

#### Scenario: Dynamic port example exists

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it explains dynamic WireMock ports and runtime base URL propagation
- **AND** it warns against hardcoded shared ports such as fixed localhost values in tests
- **AND** it stays framework-agnostic by not prescribing Spring, Quarkus, or Micronaut lifecycle APIs

#### Scenario: Debugging example exists

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it explains request journal, unmatched requests, near misses, or unexpected traffic checks
- **AND** it shows how those diagnostics help debug broad matchers, wrong paths, missing headers, or wrong body shapes

#### Scenario: Fault simulation example exists

- **WHEN** generated `references/702-technologies-wiremock.md` is inspected
- **THEN** it shows deliberate fault, delay, error status, or connection reset stubbing
- **AND** it states that fault simulation belongs in resilience tests, not default happy-path stubs

### Requirement: WireMock skill stays framework-agnostic

The `702-technologies-wiremock` skill MUST stay focused on portable WireMock behavior and avoid framework test bootstrapping unless explicitly requested.

#### Scenario: Framework implementation is routed away

- **GIVEN** a user asks for `BaseIntegrationTest`, `@SpringBootTest`, `@QuarkusTest`, `@MicronautTest`, or framework lifecycle setup
- **WHEN** `702-technologies-wiremock` is applied
- **THEN** the skill identifies that the concern is outside pure WireMock stub quality
- **AND** it routes framework-agnostic integration-test class layout to `132-java-testing-integration-testing`
- **AND** it routes Spring Boot, Quarkus, or Micronaut wiring to the matching framework integration-test skill

### Requirement: Acceptance coverage exists for 702

The repository MUST add acceptance coverage for `702-technologies-wiremock`.

#### Scenario: Gherkin feature covers WireMock stub review

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/702-technologies-wiremock.feature` is inspected
- **THEN** it includes a scenario requiring the skill to read `references/702-technologies-wiremock.md`
- **AND** it verifies findings for broad matchers, missing verification, leaked stubs, hardcoded ports, fixture layout, or unmatched request debugging
- **AND** it verifies the skill proposes concrete JSON mapping or Java DSL snippets
- **AND** it verifies the skill avoids framework-specific test bootstrapping recommendations

#### Scenario: Acceptance prompt inventory includes 702

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes a `702-technologies-wiremock` entry
- **AND** the entry uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/702-technologies-wiremock` output is refreshed only by running the documented generator command


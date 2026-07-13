## ADDED Requirements

### Requirement: Onion architecture technology skill exists

The repository MUST add a generated `707-technologies-onion-architecture` skill for framework-agnostic Onion architecture guidance in Java projects.

#### Scenario: Skill is registered and generated

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** it registers skill id `707` with `skillId="707-technologies-onion-architecture"`
- **AND** it references the Onion architecture skill guidance source

#### Scenario: Local generated skill output exists

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** `.agents/skills/707-technologies-onion-architecture/SKILL.md` exists
- **AND** the generated skill describes framework-agnostic Onion architecture guidance for Java projects
- **AND** the generated skill includes references to its detailed guidance file

### Requirement: Onion architecture guidance covers boundary review

The `707-technologies-onion-architecture` skill MUST help users review Java application boundaries according to Onion architecture dependency direction and responsibility placement.

#### Scenario: Guidance covers core Onion architecture concerns

- **WHEN** generated `references/707-technologies-onion-architecture.md` is inspected
- **THEN** it explains domain independence from infrastructure and framework APIs
- **AND** it explains application/use-case orchestration through ports or interfaces
- **AND** it explains adapters and infrastructure depending inward on application or domain contracts
- **AND** it explains package or module dependency direction checks
- **AND** it asks the agent to report boundary violations with code evidence and concrete remediation steps

#### Scenario: Review output is evidence-backed

- **WHEN** the skill is applied to a Java project review
- **THEN** the response identifies the packages, classes, imports, dependencies, or tests inspected
- **AND** findings distinguish confirmed violations from assumptions or missing context
- **AND** recommendations preserve the direction that inner layers do not depend on outer layers

### Requirement: ArchUnit verification is supported without being mandatory

The `707-technologies-onion-architecture` skill MUST include ArchUnit-aware verification guidance while avoiding mandatory dependency changes for every project.

#### Scenario: ArchUnit guidance is present

- **WHEN** generated `references/707-technologies-onion-architecture.md` is inspected
- **THEN** it references ArchUnit-style architecture verification for Onion architecture boundaries
- **AND** it may show concise examples for encoding dependency rules as tests
- **AND** it states that ArchUnit is an optional verification approach selected when it fits the project

#### Scenario: Dependency addition is routed appropriately

- **GIVEN** a project does not already include ArchUnit
- **WHEN** a user asks to add architecture tests or dependencies
- **THEN** the skill routes Maven dependency evaluation to `111-java-maven-dependencies`
- **AND** it does not claim ArchUnit must be added to every project before Onion architecture guidance can be useful

### Requirement: Onion architecture skill stays framework-agnostic

The `707-technologies-onion-architecture` skill MUST stay focused on architecture boundaries and avoid framework runtime implementation guidance unless explicitly requested.

#### Scenario: Framework implementation is routed away

- **GIVEN** a user asks for Spring Boot, Quarkus, or Micronaut bean wiring, module runtime configuration, persistence annotations, or REST controller implementation
- **WHEN** `707-technologies-onion-architecture` is applied
- **THEN** the skill identifies that the concern is outside pure Onion architecture boundary review
- **AND** it routes Spring Boot concerns to matching Spring Boot skills
- **AND** it routes Quarkus concerns to matching Quarkus skills
- **AND** it routes Micronaut concerns to matching Micronaut skills

### Requirement: Acceptance coverage exists for 707

The repository MUST add acceptance coverage for `707-technologies-onion-architecture`.

#### Scenario: Gherkin feature covers Onion architecture review

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/707-technologies-onion-architecture.feature` is inspected
- **THEN** it includes a scenario requiring the skill to read `references/707-technologies-onion-architecture.md`
- **AND** it verifies findings for inward dependency direction, domain independence, adapter boundaries, or infrastructure leakage
- **AND** it verifies the skill proposes architecture-test or remediation guidance
- **AND** it verifies the skill avoids framework-specific runtime wiring recommendations unless explicitly requested

#### Scenario: Acceptance prompt inventory includes 707

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes a `707-technologies-onion-architecture` entry
- **AND** the entry uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries are preserved

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/707-technologies-onion-architecture` output is refreshed only by running the documented generator command

# architecture-diagram-skill-references Specification

## Purpose
TBD - created by archiving change split-architecture-diagrams-references. Update Purpose after archive.
## Requirements
### Requirement: Architecture diagram question orchestration

The `033-architecture-diagrams` skill MUST ask its diagram preference questions from the skill workflow before reading any diagram-family implementation reference.

#### Scenario: Ask diagram questions before loading references

- **WHEN** the generated `033-architecture-diagrams/SKILL.md` is inspected
- **THEN** its workflow includes the diagram question flow before any diagram reference selection step
- **AND** it instructs the agent to record selected diagram families before continuing
- **AND** it maps selections to the corresponding focused reference files

#### Scenario: Skip unselected diagram references

- **GIVEN** a user selects only a subset of diagram families
- **WHEN** the skill proceeds after the question flow
- **THEN** the workflow instructs the agent to read only references for the selected diagram families
- **AND** it instructs the agent not to read or apply unselected diagram-family references

### Requirement: Focused diagram-family references

The architecture diagram skill reference source MUST be split into focused XML references for UML sequence, UML class, C4, UML state-machine, and ER diagram families.

#### Scenario: Register focused references for skill 033

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers multiple architecture diagram references
- **AND** those references include UML sequence, UML class, C4, UML state-machine, and ER diagram guidance
- **AND** skill `033` no longer depends on one monolithic `033-architecture-diagrams` reference as its only detailed source

#### Scenario: Keep each reference responsibility clear

- **WHEN** the split XML reference files are inspected
- **THEN** each reference owns one diagram-family responsibility
- **AND** each reference states that it is used only after the central question flow selects that diagram family
- **AND** shared guidance is not moved into a standalone validation-only reference

### Requirement: Diagram-family guardrails

Each focused diagram-family reference MUST include output organization and validation guardrails appropriate to that diagram family.

#### Scenario: Validate generated diagram output per family

- **WHEN** a focused diagram-family reference is used to generate diagrams
- **THEN** the reference provides guidance for organizing the generated output
- **AND** it provides validation expectations for PlantUML syntax, readability, and consistency with analyzed project code or schema inputs
- **AND** it keeps those guardrails inside the selected diagram-family reference

#### Scenario: Avoid standalone output validation reference

- **WHEN** the split reference files are inspected
- **THEN** there is no standalone `033-architecture-diagrams-output-validation.xml` reference
- **AND** output and validation guardrails are present in each diagram-family reference

### Requirement: C4 level alignment

The architecture diagram skill MUST support C4 Context, Container, and Component diagrams only, and MUST NOT offer or generate C4 Code/Level 4 diagrams.

#### Scenario: Inspect C4 question and reference guidance

- **WHEN** the question flow, C4 reference, and generated local skill output are inspected
- **THEN** C4 options mention only Context, Container, and Component diagrams
- **AND** they do not offer complete C4 model options that include Code/Level 4 diagrams
- **AND** C4 examples and constraints do not instruct the agent to generate Code/Level 4 diagrams

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Validate source and local generation

- **WHEN** the change is implemented
- **THEN** changed XML files are validated with `xmllint --noout`
- **AND** local skill generation succeeds with `./mvnw clean install -pl skills-generator`
- **AND** the generated local `033-architecture-diagrams/SKILL.md` includes the question flow and split reference links
- **AND** generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or lingering references to the removed monolith

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope


## ADDED Requirements

### Requirement: Agent XSD

The PML agent schema family SHALL define a single XSD (`agent.xsd`) that validates both the agent inventory manifest and individual agent definition documents.

#### Scenario: Validate enriched inventory manifest

- **GIVEN** a Plinth agent inventory document at `plinth-agents-generator/src/main/resources/agents.xml`
- **WHEN** `agent.xsd` is applied
- **THEN** the schema validates a root `<agent-inventory>` element with ordered `<agent>` entries
- **AND** each entry provides a unique agent `@id`, a `@file` reference ending in `.md` or an approved inline body reference, and optional discovery metadata such as `@readonly` and `<summary>`
- **AND** duplicate agent ids fail validation

#### Scenario: Validate agent body structural contract

- **GIVEN** an agent definition document for an embedded Plinth agent
- **WHEN** `agent.xsd` is applied
- **THEN** the schema requires `<frontmatter>` with `name`, `model`, and `description`
- **AND** the schema defines optional body elements such as `<missions>`, `<role-boundaries>`, `<responsibilities>`, `<routing>`, `<output-format>`, and `<safeguards>` as a structural superset
- **AND** the schema does not require a global `<missions>` element for every agent
- **AND** documents missing required frontmatter fields fail validation

#### Scenario: Map frontmatter fields to XML

- **GIVEN** current agent Markdown contracts use YAML frontmatter (`name`, `model`, `description`, optional `readonly`)
- **WHEN** `agent.xsd` is designed
- **THEN** each frontmatter field maps to an explicit XML element under `<frontmatter>`
- **AND** inventory `@id` and frontmatter `name` SHOULD refer to the same agent identifier

### Requirement: Behavioral validation outside XSD

Kind-specific section requirements and delegation semantics SHALL remain enforced by existing Java and Gherkin tests, not by Schematron or `@kind` schema profiles.

#### Scenario: Preserve behavioral enforcement path

- **GIVEN** nine agent contracts with distinct Markdown shapes (analyst, architect, coordinator, performance, coder)
- **WHEN** `agent.xsd` is introduced
- **THEN** structural validation is limited to XSD
- **AND** `AgentIndexesTest` and agent Gherkin acceptance tests continue to enforce behavioral contracts until XSLT parity replaces substring checks

### Requirement: PML schema relationship documentation

The schema design SHALL document how `agent.xsd` relates to existing PML `pml.xsd` and `pml-workflow.xsd` artifacts.

#### Scenario: Document parallel skill and agent schema families

- **GIVEN** skills validate against `pml.xsd` 0.8.0 using `<prompt>` documents
- **AND** workflows may validate against `pml-workflow.xsd`
- **WHEN** the agent schema design is published
- **THEN** the design states that `agent.xsd` is a parallel extension for orchestration personas, not a replacement for skill `<prompt>` documents
- **AND** the design states that workflow graph structure remains in `pml-workflow.xsd` while agent missions may reference workflow commands in prose or structured text
- **AND** the design references ADR-001 XSD-to-Markdown generation precedent

### Requirement: Representative XML examples

The change SHALL provide representative valid and invalid XML examples for at least one existing agent contract surface.

#### Scenario: robot-business-analyst examples

- **GIVEN** the current `robot-business-analyst.md` contract under `plinth-agents-generator/src/main/resources/agents/`
- **WHEN** examples are authored for the schema design
- **THEN** at least one valid XML example covers inventory and agent-body structures for that agent
- **AND** at least one invalid XML example demonstrates an XSD violation such as missing frontmatter fields or duplicate inventory ids
- **AND** examples are stored with the OpenSpec change for reviewer inspection

### Requirement: Migration notes from Markdown-first sources

The change SHALL document migration guidance from current Markdown-first agent assets to schema-validated XML sources without implementing the generator pipeline in the same change.

#### Scenario: Phased migration guidance

- **GIVEN** nine agent Markdown contracts and a minimal `agents.xml` manifest are the current source of truth
- **WHEN** migration notes are published
- **THEN** they describe a phased path where local `agent.xsd` authoring precedes optional validation, XML source migration, XSLT Markdown generation, and eventual XML source-of-truth cutover
- **AND** they state that full XSLT or generator migration is follow-up work outside this change
- **AND** they preserve non-breaking behavior for skill installer output until an explicit cutover phase

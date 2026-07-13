## ADDED Requirements

### Requirement: Agent inventory XSD

The PML agent schema family SHALL define an XSD for the agent inventory manifest that validates structure beyond filename-only `<agent file="..."/>` references.

#### Scenario: Validate enriched inventory manifest

- **GIVEN** a Plinth agent inventory document at `plinth-agents-generator/src/main/resources/agents.xml`
- **WHEN** the PML agent inventory XSD is applied
- **THEN** the schema validates a root `<agent-inventory>` element with ordered `<agent>` entries
- **AND** each entry provides a unique agent `@id`, a `@file` reference ending in `.md` or an approved inline body reference, and optional discovery metadata such as `@readonly` and `<summary>`
- **AND** duplicate agent ids fail validation

### Requirement: Agent definition XSD

The PML agent schema family SHALL define an XSD for individual agent contracts with structured elements aligned to OpenSpec `analysis-design-agents` behavioral fields.

#### Scenario: Validate required agent contract sections

- **GIVEN** an agent definition document for an embedded Plinth agent
- **WHEN** the PML agent body XSD is applied
- **THEN** the schema requires `<frontmatter>` with `name`, `model`, and `description`
- **AND** the schema requires `<identity>`, `<missions>`, `<role-boundaries>`, and `<output-format>` sections
- **AND** coordinator agents such as `robot-tech-lead` additionally require structured `<routing>` delegation elements
- **AND** documents missing required sections fail validation

#### Scenario: Map frontmatter fields to XML

- **GIVEN** current agent Markdown contracts use YAML frontmatter (`name`, `model`, `description`, optional `readonly`)
- **WHEN** the agent body XSD is designed
- **THEN** each frontmatter field maps to an explicit XML element under `<frontmatter>`
- **AND** inventory `@id` and frontmatter `name` MUST refer to the same agent identifier

### Requirement: PML schema relationship documentation

The schema design SHALL document how agent XSDs relate to existing PML `pml.xsd` and `pml-workflow.xsd` artifacts.

#### Scenario: Document parallel skill and agent schema families

- **GIVEN** skills validate against `pml.xsd` 0.8.0 using `<prompt>` documents
- **AND** workflows may validate against `pml-workflow.xsd`
- **WHEN** the agent schema design is published
- **THEN** the design states that agent schemas are a parallel extension for orchestration personas, not a replacement for skill `<prompt>` documents
- **AND** the design states that workflow graph structure remains in `pml-workflow.xsd` while agent missions may reference workflow commands in prose or structured text
- **AND** the design references ADR-001 XSD-to-Markdown generation precedent

### Requirement: Representative XML examples

The change SHALL provide representative valid and invalid XML examples for at least one existing agent contract surface.

#### Scenario: robot-business-analyst examples

- **GIVEN** the current `robot-business-analyst.md` contract under `plinth-agents-generator/src/main/resources/agents/`
- **WHEN** examples are authored for the schema design
- **THEN** at least one valid XML example covers inventory and agent-body structures for that agent
- **AND** at least one invalid XML example demonstrates a schema violation such as missing missions or empty role boundaries
- **AND** examples are stored with the OpenSpec change for reviewer inspection

### Requirement: Migration notes from Markdown-first sources

The change SHALL document migration guidance from current Markdown-first agent assets to schema-validated XML sources without implementing the generator pipeline in the same change.

#### Scenario: Phased migration guidance

- **GIVEN** nine agent Markdown contracts and a minimal `agents.xml` manifest are the current source of truth
- **WHEN** migration notes are published
- **THEN** they describe a phased path where XSD publication precedes optional validation, XML authoring, XSLT Markdown generation, and eventual XML source-of-truth cutover
- **AND** they state that full XSLT or generator migration is follow-up work outside this change
- **AND** they preserve non-breaking behavior for skill installer output until an explicit cutover phase

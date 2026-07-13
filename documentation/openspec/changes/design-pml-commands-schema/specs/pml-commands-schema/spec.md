## ADDED Requirements

### Requirement: Command inventory XSD

The PML command schema family SHALL define an XSD for the command inventory manifest that validates structure beyond filename-only `<command file="..."/>` references.

#### Scenario: Validate enriched inventory manifest

- **GIVEN** a Plinth command inventory document at `plinth-commands-generator/src/main/resources/commands.xml`
- **WHEN** the PML command inventory XSD is applied
- **THEN** the schema validates a root `<command-inventory>` element with ordered `<command>` entries
- **AND** each entry provides a unique command `@id`, a `@file` reference ending in `.md` or an approved inline body reference, and optional discovery metadata such as `@slash`, `<summary>`, and `<owning-agent>`
- **AND** duplicate command ids fail validation

### Requirement: Command definition XSD

The PML command schema family SHALL define an XSD for individual command contracts with structured elements aligned with OpenSpec `analysis-design-commands` behavioral fields.

#### Scenario: Validate required command contract sections

- **GIVEN** a command definition document for an embedded Plinth slash command
- **WHEN** the PML command body XSD is applied
- **THEN** the schema requires `<purpose>`, `<usage>`, `<accepted-inputs>`, `<owning-agent>`, `<workflow>`, `<output>`, and `<safeguards>`
- **AND** the schema supports `<associated-skills>` for skill or capability delegation references
- **AND** documents missing required sections fail validation

#### Scenario: Align with analysis-design-commands contract fields

- **GIVEN** OpenSpec `analysis-design-commands` requires purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards
- **WHEN** the command body XSD is designed
- **THEN** each required behavioral field maps to an explicit XML element
- **AND** inventory `@id` and the command `@slash` prefix refer to the same command identity as the Markdown H1 title

### Requirement: PML schema relationship documentation

The schema design SHALL document how command XSDs relate to existing PML `pml.xsd` and `pml-workflow.xsd` artifacts.

#### Scenario: Document parallel skill and command schema families

- **GIVEN** skills validate against `pml.xsd` 0.8.0 using `<prompt>` documents
- **AND** workflows may validate against `pml-workflow.xsd`
- **WHEN** the command schema design is published
- **THEN** the design states that command schemas are a parallel extension for slash-invoked workflow entry points, not a replacement for skill `<prompt>` documents
- **AND** the design states that workflow graph structure remains in `pml-workflow.xsd` while command `<workflow>` steps describe invocation procedure
- **AND** the design references ADR-001 XSD-to-Markdown generation precedent and the parallel agent schema change `design-pml-agents-schema`

### Requirement: Representative XML examples

The change SHALL provide representative valid and invalid XML examples for at least one existing command contract surface.

#### Scenario: update-issue examples

- **GIVEN** the current `update-issue.md` contract under `plinth-commands-generator/src/main/resources/commands/`
- **WHEN** examples are authored for the schema design
- **THEN** at least one valid XML example covers inventory and command-body structures for that command
- **AND** at least one invalid XML example demonstrates a schema violation such as missing workflow or empty safeguards
- **AND** examples are stored with the OpenSpec change for reviewer inspection

### Requirement: Migration notes from Markdown-first sources

The change SHALL document migration guidance from current Markdown-first command assets to schema-validated XML sources without implementing the generator pipeline in the same change.

#### Scenario: Phased migration guidance

- **GIVEN** twelve command Markdown contracts and a minimal `commands.xml` manifest are the current source of truth
- **WHEN** migration notes are published
- **THEN** they describe a phased path where XSD publication precedes optional validation, XML authoring, XSLT Markdown generation, and eventual XML source-of-truth cutover
- **AND** they state that full XSLT or generator migration is follow-up work outside this change
- **AND** they preserve non-breaking behavior for skill installer output until an explicit cutover phase

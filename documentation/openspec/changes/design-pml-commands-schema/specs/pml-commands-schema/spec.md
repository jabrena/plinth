## ADDED Requirements

### Requirement: Command body XSD (agents parity)

The commands generator SHALL validate individual command contracts with a single no-namespace `commands.xsd` colocated at `plinth-commands-generator/src/main/resources/commands.xsd`, following the same style as `agents.xsd`.

#### Scenario: Validate structural command contract sections

- **GIVEN** a command definition document with required `@id` and optional `@kind` (`standard`, `delivery`, `performance`, `cli`) and `@slash`
- **WHEN** `commands.xsd` is applied
- **THEN** the schema requires core sections `goal` and `steps`, with optional `constraints`, `output-format`, and `safeguards` in agents order
- **AND** optional `output-format` is expressible in the structural sequence
- **AND** narrative contract content (Usage, Accepted Inputs, owner, associations, delegation, ownership, tool selection, workflow position, execution contract, branch/worktree gate) is authored as Markdown inside `goal` CDATA
- **AND** documents missing a required core section fail XSD validation
- **AND** documents declare `xsi:noNamespaceSchemaLocation` pointing at `commands.xsd`

#### Scenario: Align with analysis-design-commands contract fields

- **GIVEN** OpenSpec `analysis-design-commands` requires purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards
- **WHEN** the command body XSD and XSLT are designed
- **THEN** workflow, outputs, and safeguards map to structured XML elements; purpose and remaining narrative fields map to Markdown inside `goal` CDATA
- **AND** kind-specific required/forbidden sections remain enforced by `analysis-design-commands`, `CommandIndexesTest`, and Gherkin — not by Schematron
- **AND** body `@id` / `@slash` match the generated Markdown H1 / usage line identity

### Requirement: Inventory outside body schema

The command inventory manifest SHALL remain a separate `commands.xml` document outside `commands.xsd`, matching `agents.xml` / `agents.xsd` separation.

#### Scenario: Inventory lists XML sources mapped to Markdown assets

- **GIVEN** `plinth-commands-generator/src/main/resources/commands.xml`
- **WHEN** `CommandIndexes` loads the inventory
- **THEN** each `<command file="..."/>` entry ends in `.xml`
- **AND** `commandFiles()` returns the corresponding `.md` names in installation order
- **AND** duplicate mapped Markdown names fail inventory uniqueness checks in tests / Java loaders
- **AND** inventory is not validated by `commands.xsd`

### Requirement: XML sources and generated Markdown

Command contracts SHALL be authored as XML under `commands/` and transformed to Markdown only under `target/`, mirroring agents generation.

#### Scenario: Build-time Markdown generation

- **GIVEN** twelve command XML sources under `src/main/resources/commands/`
- **WHEN** Maven reaches `process-classes` for `plinth-commands-generator`
- **THEN** `CommandMarkdownGenerator` writes Markdown to `target/generated-resources/commands` and `target/classes/commands`
- **AND** hand-authored `commands/*.md` are not present under `src/main/resources/commands/`
- **AND** `CommandIndexesTest` substring assertions pass against the generated classpath Markdown

### Requirement: Command kind taxonomy

The schema design SHALL define optional `@kind` values that select documentation heading profiles without Schematron.

#### Scenario: Classify embedded commands by kind

- **GIVEN** the twelve commands under `plinth-commands-generator/src/main/resources/commands/`
- **WHEN** command XML documents are authored
- **THEN** `@kind` values `standard`, `delivery`, `performance`, and `cli` are available
- **AND** each command id maps to exactly one kind in design documentation
- **AND** kind-profile section rules stay in behavioral tests

### Requirement: PML / agents relationship documentation

The schema design SHALL document that command schemas are a parallel, agents-style no-namespace family — not namespaced split schemas under `/pml/schemas/command/1.0.0/`.

#### Scenario: Document parallel skill, agent, and command families

- **GIVEN** skills use `pml.xsd` `<prompt>` documents and agents use no-namespace `agents.xsd`
- **WHEN** the command schema design is published
- **THEN** the design states commands follow the agents generator pattern (single body XSD + inventory XML + XSLT Markdown)
- **AND** the design states command schemas are not a replacement for skill `<prompt>` documents
- **AND** the design references ADR-001 and the shipped agents generator as the parity target

### Requirement: Authoritative command XML sources

The change SHALL treat production command XML under `plinth-commands-generator` as the authoritative examples for `commands.xsd`.

#### Scenario: Production sources and XSD mirror

- **GIVEN** twelve command XML sources under `plinth-commands-generator/src/main/resources/commands/`
- **WHEN** the schema design is published
- **THEN** those documents validate against `commands.xsd`
- **AND** the OpenSpec XSD mirror lives at `examples/xsd/pml/0.9.0/commands.xsd`
- **AND** separate OpenSpec valid/invalid XML fixture trees are not required

### Requirement: Migration notes

The change SHALL document migration from Markdown-first command assets to XML sources with agents-parity generation.

#### Scenario: Completed cutover guidance

- **GIVEN** former Markdown-first contracts and the rejected namespaced `pml/` schema tree
- **WHEN** migration notes are published
- **THEN** they describe XML under `commands/`, generation via `command-to-markdown.xsl`, and removal of hand-authored Markdown from `src/`
- **AND** they state inventory remains outside `commands.xsd`
- **AND** they preserve behavioral contract stability for installers consuming generated Markdown

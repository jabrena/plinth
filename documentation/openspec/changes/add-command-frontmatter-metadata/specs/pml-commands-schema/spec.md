## MODIFIED Requirements

### Requirement: Command body XSD (agents parity)

The commands generator SHALL validate individual command contracts with a single no-namespace `commands.xsd` colocated at `plinth-commands-generator/src/main/resources/commands.xsd`, following the same style as `agents.xsd` while adding the command-specific frontmatter contract.

#### Scenario: Validate structural command contract sections

- **GIVEN** a command definition document with required `@id`
- **WHEN** `commands.xsd` is applied
- **THEN** the schema requires `frontmatter`, `goal`, and `steps`, with optional `constraints`, `output-format`, and `safeguards` in the defined sequence
- **AND** `frontmatter` requires `description`, `argument-hint`, `model`, `agent`, and `tools`
- **AND** `tools` requires `list-tools` containing one or more `tool` elements
- **AND** optional `output-format` is expressible in the structural sequence
- **AND** narrative contract content remains authored as Markdown inside `goal` CDATA
- **AND** documents missing a required section or frontmatter field fail XSD validation
- **AND** documents declare `xsi:noNamespaceSchemaLocation` pointing at `commands.xsd`

#### Scenario: Keep frontmatter values extensible per command

- **GIVEN** different commands can require different descriptions, argument hints, agents, models, or tools
- **WHEN** their XML documents validate against `commands.xsd`
- **THEN** the schema enforces the frontmatter structure and repeating tool representation
- **AND** the schema does not restrict model, agent, or tool values to a closed enumeration
- **AND** each command source can declare its own `tools/list-tools/tool` entries

#### Scenario: Align with analysis-design-commands contract fields

- **GIVEN** OpenSpec `analysis-design-commands` requires purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards
- **WHEN** the command body XSD and XSLT are applied
- **THEN** frontmatter, workflow, outputs, and safeguards map to structured XML elements; purpose and remaining narrative fields map to Markdown inside `goal` CDATA
- **AND** kind-specific required or forbidden sections remain enforced by `analysis-design-commands`, `CommandIndexesTest`, and Gherkin rather than Schematron
- **AND** body `@id` matches the generated Markdown H1 identity

### Requirement: XML sources and generated Markdown

Command contracts SHALL be authored as XML under `commands/` and transformed to frontmatter-enabled Markdown only under `target/`, mirroring agents generation while preserving the existing command body.

#### Scenario: Build-time Markdown generation

- **GIVEN** every command XML source declared by `commands.xml` contains schema-valid frontmatter metadata
- **WHEN** Maven reaches `process-classes` for `plinth-commands-generator`
- **THEN** `CommandMarkdownGenerator` writes Markdown to `target/generated-resources/commands` and `target/classes/commands`
- **AND** every generated file begins with an opening YAML delimiter and ends its frontmatter before the command H1
- **AND** the generated frontmatter contains `description`, `argument-hint`, `model`, `agent`, and `tools`
- **AND** hand-authored `commands/*.md` are not present under `src/main/resources/commands/`
- **AND** `CommandIndexesTest` assertions pass against the generated classpath Markdown

#### Scenario: Map XML tools to YAML tools

- **GIVEN** a command source declares one or more `tools/list-tools/tool` values
- **WHEN** the command Markdown is generated
- **THEN** its frontmatter contains a YAML `tools` sequence
- **AND** every declared XML tool appears as a corresponding YAML sequence entry

#### Scenario: Preserve existing Markdown after frontmatter

- **GIVEN** a command has an existing generated Markdown heading and body
- **WHEN** frontmatter generation is enabled
- **THEN** the heading and body follow the closing YAML delimiter
- **AND** their semantic content remains unchanged

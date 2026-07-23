## ADDED Requirements

### Requirement: Local skills.xsd as an unchanged PML 0.8.0 baseline

The `plinth-skills-generator` module SHALL provide a local schema `skills.xsd` that is a complete, unchanged copy of the PML 0.8.0 `pml.xsd` published at `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`.

#### Scenario: Schema location and content fidelity

- **GIVEN** the `plinth-skills-generator` Maven module
- **WHEN** `skills.xsd` is introduced
- **THEN** it is stored at `plinth-skills-generator/src/main/resources/skills.xsd`, beside `skills.xml`
- **AND** its content is byte-for-byte identical to the PML 0.8.0 `pml.xsd` at the time of the copy
- **AND** this change does not tighten, add, or remove required/optional structure relative to that copy

#### Scenario: Schema scope is limited to skill-indexes

- **GIVEN** `plinth-skills-generator/src/main/resources/skill-indexes/` (skill-index XML sources) and `plinth-skills-generator/src/main/resources/skill-references/` (skill-reference XML sources)
- **WHEN** `skills.xsd` is adopted
- **THEN** every XML file under `skill-indexes/` references `skills.xsd`
- **AND** XML files under `skill-references/` are not migrated and continue to reference the remote PML 0.8.0 `pml.xsd`
- **AND** `RemoteSchemaValidationTest` continues to validate `skill-references/` against the remote schema, unmodified in scope

### Requirement: SKILL.md mapping documentation

The schema design SHALL document how `skills.xsd` XML elements map to the generated `SKILL.md` frontmatter and content.

#### Scenario: Element-to-output mapping is documented

- **GIVEN** a `skill-indexes/{numericId}-skill.xml` document with `prompt/@id`, `metadata` (description, author, version, license), `title`, `goal`, optional `constraints`, `triggers`, and `steps`
- **WHEN** the schema design is reviewed
- **THEN** the mapping from each of those XML elements/attributes to the corresponding `SKILL.md` frontmatter field or body section is recorded in this change's `design.md`
- **AND** the mapping states that the `## Reference` section is appended by `SkillsGenerator` from `skills.xml`'s `reference-list`, not derived from `skills.xsd`

### Requirement: Representative valid and invalid examples

The change SHALL provide representative valid and invalid XML examples illustrating the schema's validation behavior.

#### Scenario: Valid representative example

- **GIVEN** the shape of an existing `skill-indexes/*.xml` document (for example `301-skill.xml`)
- **WHEN** a representative valid example is authored for this change
- **THEN** `examples/xml/valid-skill-index-example.xml` is stored with this OpenSpec change
- **AND** it references `skills.xsd` and is expected to pass validation once the real `skills.xsd` is in place

#### Scenario: Invalid representative example

- **GIVEN** the same representative shape
- **WHEN** a representative invalid example is authored for this change
- **THEN** `examples/xml/invalid-skill-index-example.xml` is stored with this OpenSpec change, omitting required information (`metadata/description` and `title`)
- **AND** it is expected to fail validation with a meaningful diagnostic once the real `skills.xsd` is in place
- **AND** the change records that the exact required/optional cardinalities and diagnostic text depend on the copied PML 0.8.0 content and MUST be reconfirmed with `xmllint` during implementation

### Requirement: Schema-per-artifact policy documentation

The schema design SHALL document the broader architectural direction of maintaining one XML Schema per generated artifact, and state which artifacts are and are not covered by this change.

#### Scenario: Document scope boundary against sibling schemas

- **GIVEN** `agents.xsd` (agent definitions) and `commands.xsd` (command definitions) as sibling, independently-designed artifact-scoped schemas
- **WHEN** this change's design is reviewed
- **THEN** the design states that `skills.xsd` covers only skill-index XML → `SKILL.md` generation
- **AND** the design states that skill-reference XML, command XML, and agent XML schemas remain owned by their own changes
- **AND** the design records that this change authors `ADR-008` as the home for the cross-artifact schema-per-artifact policy statement, and that completing `ADR-008` is required before this change's implementation is considered done

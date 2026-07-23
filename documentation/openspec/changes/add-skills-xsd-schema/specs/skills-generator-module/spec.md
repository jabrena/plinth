## ADDED Requirements

### Requirement: Atomic migration of all skill-index schema references

Every XML file under `plinth-skills-generator/src/main/resources/skill-indexes/` SHALL reference the local `skills.xsd` instead of the remote PML 0.8.0 `pml.xsd`, migrated atomically within this change.

#### Scenario: Complete inventory migrated

- **GIVEN** the 125 XML files under `plinth-skills-generator/src/main/resources/skill-indexes/`
- **WHEN** the migration in this change is complete
- **THEN** every one of those 125 files has its `xsi:noNamespaceSchemaLocation` updated to point at the local `skills.xsd`
- **AND** no file under `skill-indexes/` still references the remote `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd` location
- **AND** the migration is delivered as a single atomic unit of work within this change, not split across multiple changes or partial merges

### Requirement: Generator runtime schema validation for skill-indexes

`SkillsGenerator` SHALL validate `skill-indexes/*.xml` documents against the local `skills.xsd` before or during transformation, without depending on runtime access to the remote PML schema.

#### Scenario: Invalid skill-index XML fails generation with a meaningful diagnostic

- **GIVEN** a `skill-indexes/{numericId}-skill.xml` document that omits information required by `skills.xsd`
- **WHEN** `SkillsGenerator` generates that skill (`loadSkillSummaryFromXml` / `useXml = true` path)
- **THEN** generation fails with a diagnostic identifying the offending file and the violated schema constraint
- **AND** the failure occurs before, or as part of, the existing XInclude-aware DOM parsing and `skill-index-to-markdown.xsl` transformation

#### Scenario: Validation does not require the remote schema at runtime

- **GIVEN** the migrated `skill-indexes/*.xml` documents reference the local `skills.xsd`
- **WHEN** `SkillsGenerator` runs in an environment without network access to `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`
- **THEN** skill-index generation and validation succeed using only the classpath-local `skills.xsd`
- **AND** `skill-references/*.xml` generation is unaffected by this requirement (it retains its existing remote-schema behavior, unchanged by this requirement)

### Requirement: Maven and CI test coverage for the complete skill-index inventory

The module SHALL provide automated Maven tests that validate every migrated `skill-indexes/*.xml` file against `skills.xsd`, plus representative invalid fixtures, and SHALL run this coverage in CI.

#### Scenario: Complete-inventory validation test

- **GIVEN** a schema-validation test scoped to `skill-indexes/*.xml` and `skills.xsd` (distinct in name and scope from `RemoteSchemaValidationTest`, which continues to cover `skill-references/` against the remote schema)
- **WHEN** `./mvnw clean verify -pl plinth-skills-generator -am` runs
- **THEN** the test enumerates and validates all 125 `skill-indexes/*.xml` files against the local `skills.xsd`
- **AND** the test also validates at least one representative invalid fixture and asserts that validation fails
- **AND** this test executes as part of the existing CI pipeline that already runs `plinth-skills-generator` verification, without requiring a new CI job

### Requirement: Byte-for-byte generated output compatibility

Generated `SKILL.md` output SHALL remain byte-for-byte identical before and after the schema-reference migration and the addition of runtime validation.

#### Scenario: Before/after comparison for all migrated skills

- **GIVEN** the complete set of skills generated from `skill-indexes/*.xml` sources via `SkillsGenerator`
- **WHEN** skills are generated before the migration and again after the migration and validation changes are applied
- **THEN** every generated `SKILL.md` file is byte-for-byte identical between the two generations
- **AND** any difference blocks promotion of this change until output parity is restored

### Requirement: ADR-008 documents the schema-per-artifact policy

This change SHALL author `ADR-008` in `documentation/adr/`, recording the policy of maintaining one XML Schema per generated artifact and the scope implemented by this change (skill-index only), and SHALL add its entry to `documentation/adr/README.md`, before this change's implementation is considered complete.

#### Scenario: ADR-008 authored and indexed as a gating step

- **GIVEN** this change's schema, migration, runtime-validation, and Maven/CI test tasks are complete
- **WHEN** this change's implementation is finalized
- **THEN** `documentation/adr/ADR-008-*.md` exists, following the repository's existing MADR-style ADR template, and describes the schema-per-artifact policy and this change's scope
- **AND** `documentation/adr/README.md` includes a new row for `ADR-008`
- **AND** completing and indexing `ADR-008` is a prerequisite for closing this change's implementation, per the acceptance criteria confirmed on issue #991 (posted 2026-07-23)

### Requirement: Deferred ADR-001 update tracked, not implemented, by this change

The update of `ADR-001` and its `documentation/adr/README.md` index row SHALL be tracked as an explicit follow-up task and SHALL NOT be required for this change's implementation to be considered complete.

#### Scenario: Follow-up task recorded without blocking this change

- **GIVEN** this change's `tasks.md`
- **WHEN** the schema, migration, runtime validation, test/CI coverage, and ADR-008 tasks in this change are completed
- **THEN** `tasks.md` also lists the ADR-001/README update as a separate, explicitly deferred follow-up task
- **AND** that follow-up task is not a prerequisite for closing this change's implementation
- **AND** the ADR-001 update, when performed later, preserves the original ADR date, identifier, and link while describing the current XML-to-Agent-Skills generation architecture

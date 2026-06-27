# design-avoid-breaking-changes-skill-reference Specification

## Purpose
TBD - created by archiving change convert-breaking-change-review-to-skill. Update Purpose after archive.
## Requirements
### Requirement: Avoid breaking changes skill

The repository MUST define `056-design-avoid-breaking-changes` as a generated design skill for reviewing plans, OpenSpec changes, specs, and implementation proposals for compatibility risks before implementation or release promotion.

#### Scenario: Avoid breaking changes skill identifier is standardized

- **GIVEN** maintainers implement breaking-change review guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, README files, or generated local skill output
- **THEN** the identifier is `056-design-avoid-breaking-changes`
- **AND** the workflow is documented as a skill workflow rather than a `/review-breaking-changes` command workflow

#### Scenario: Review a plan or spec for breaking-change risk through a skill

- **GIVEN** a maintainer has a plan or OpenSpec artifact describing a proposed repository change
- **WHEN** the maintainer uses `@056-design-avoid-breaking-changes` against that plan or artifact
- **THEN** the skill reports potential breaking changes
- **AND** the report highlights affected contracts, generated outputs, commands, skills, documentation, tests, or release guidance where applicable
- **AND** the report provides actionable follow-up validation guidance

### Requirement: Breaking-change dimensions

The avoid-breaking-changes skill MUST guide reviewers to consider multiple compatibility surfaces instead of limiting review to Java API changes.

#### Scenario: Review command and workflow compatibility

- **GIVEN** a proposed change adds, removes, renames, or modifies project commands
- **WHEN** the skill reviews the plan or spec
- **THEN** it checks command names, accepted inputs, owning agents, associated skills, outputs, safeguards, installer registration, command inventory entries, and command acceptance coverage
- **AND** it flags missing README or migration guidance when a user-facing command changes

#### Scenario: Review skill routing compatibility

- **GIVEN** a proposed change adds, removes, renames, or modifies generated skills
- **WHEN** the skill reviews the plan or spec
- **THEN** it checks skill identifiers, descriptions, triggers, metadata, references, source registration, generated local output, and acceptance prompt coverage
- **AND** it flags cases where trigger wording broadens routing beyond the documented skill scope

#### Scenario: Review generated-output ownership

- **GIVEN** a proposed change affects generated local, release, command, cursor-rule, or website output
- **WHEN** the skill reviews the plan or spec
- **THEN** it identifies the source files that should be edited
- **AND** it identifies generated files that must not be edited directly
- **AND** it recommends the generator, release profile, site profile, or command installation workflow needed to refresh output safely

#### Scenario: Review source, schema, and generator contracts

- **GIVEN** a proposed change modifies XML sources, XInclude references, XSLT, Maven module configuration, or schema-sensitive files
- **WHEN** the skill reviews the plan or spec
- **THEN** it checks whether source registration, XML well-formedness, schema expectations, and module validation are covered
- **AND** it flags missing `xmllint`, Maven module verification, or focused test coverage where applicable

#### Scenario: Review public documentation compatibility

- **GIVEN** a proposed change affects user-facing commands, skills, release behavior, or project workflows
- **WHEN** the skill reviews the plan or spec
- **THEN** it checks README and localized README impacts
- **AND** it checks whether changelog, migration, or deprecation guidance is needed for users
- **AND** it flags stale links to removed commands or renamed skills

#### Scenario: Review external and runtime contracts

- **GIVEN** a proposed change affects APIs, schemas, data formats, configuration keys, persistence, CLI behavior, generated artifacts, or CI expectations
- **WHEN** the skill reviews the plan or spec
- **THEN** it identifies consumer-visible contract changes
- **AND** it distinguishes intentional breaking changes from behavior-preserving refactoring
- **AND** it recommends compatibility windows, deprecation paths, migration checks, or release-note guidance when needed

### Requirement: Review output format

The avoid-breaking-changes skill MUST produce a structured compatibility review report that is useful for release and implementation decisions.

#### Scenario: Produce severity-ranked findings

- **GIVEN** a plan or spec contains potential compatibility risks
- **WHEN** the skill completes review
- **THEN** the report includes source artifacts reviewed, risk summary by compatibility surface, severity-ranked findings, affected files or artifacts, and recommended validation
- **AND** intentional breaking changes include migration or deprecation guidance

#### Scenario: Report no breaking-change risks found

- **GIVEN** a plan or spec has no identified compatibility risks
- **WHEN** the skill completes review
- **THEN** the report explicitly states that no breaking-change risks were found
- **AND** it lists the compatibility surfaces reviewed
- **AND** it still recommends the relevant validation commands for the touched artifact types

### Requirement: Generator registration and validation

The avoid-breaking-changes skill source MUST be registered in the generator inventory so local skill generation emits it and validation can verify it.

#### Scenario: Register avoid breaking changes skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `056` declares explicit skill id `056-design-avoid-breaking-changes`
- **AND** skill id `056` registers reference `056-design-avoid-breaking-changes`

#### Scenario: Generate local avoid breaking changes skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/056-design-avoid-breaking-changes/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The avoid-breaking-changes skill MUST include Gherkin acceptance coverage and a matching prompt inventory entry.

#### Scenario: Add acceptance test for compatibility review

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/056-design-avoid-breaking-changes.feature` is inspected
- **THEN** it includes an acceptance scenario for reviewing an OpenSpec plan or spec for breaking-change risk
- **AND** the scenario expects the skill to report affected contracts, generated outputs, commands, skills, documentation, tests, or release guidance where applicable
- **AND** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @skills-generator/src/test/resources/gherkin/skills/056-design-avoid-breaking-changes.feature`


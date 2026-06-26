# design-two-steps-methods-skill-reference Specification

## Purpose
TBD - created by archiving change add-design-two-steps-methods-skill. Update Purpose after archive.
## Requirements
### Requirement: Two-step change method skill

The repository MUST define `051-design-two-steps-methods` as a generated design skill for applying Kent Beck's two-step change method to complex or risky code changes.

#### Scenario: Two-step method skill identifier is standardized

- **GIVEN** maintainers implement two-step change method guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, or generated local skill output
- **THEN** the identifier is `051-design-two-steps-methods`
- **AND** the supplied external reference is `https://x.com/KentBeck/status/250733358307500032`

#### Scenario: Two-step method guidance separates preparation from behavior change

- **GIVEN** a developer needs to make a complex or risky code change
- **WHEN** they use the `051-design-two-steps-methods` skill
- **THEN** the skill guides them to first make the change easy through preparatory refactoring
- **AND** it guides them to validate that preparatory refactoring preserves existing behavior
- **AND** it then guides them to make the intended behavior change once the design supports it
- **AND** it recommends appropriate verification after the intended behavior change

### Requirement: Relationship to implementation skills

The two-step change method skill MUST complement Java and framework implementation skills without replacing their detailed technical guidance.

#### Scenario: Select supporting skills for detailed implementation

- **GIVEN** a two-step change requires object-oriented design, type design, design patterns, framework, persistence, messaging, API, or testing guidance
- **WHEN** the `051-design-two-steps-methods` skill identifies the applicable concern
- **THEN** it can direct the agent to use the relevant focused Java, framework, technology, or testing skill during the preparatory refactoring or intended behavior-change step
- **AND** it keeps the two-step sequencing explicit across those supporting skills

### Requirement: Generator registration

The two-step change method skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register two-step method skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `051` declares explicit skill id `051-design-two-steps-methods`
- **AND** skill id `051` registers reference `051-design-two-steps-methods`

#### Scenario: Generate local two-step method skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/051-design-two-steps-methods/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The two-step change method skill MUST include Gherkin acceptance coverage and a matching prompt inventory entry.

#### Scenario: Add acceptance test for two-step method guidance

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature` is inspected
- **THEN** it includes an acceptance scenario for a developer making a complex or risky code change
- **AND** the scenario expects the skill to guide preparatory refactoring before the intended behavior change
- **AND** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature`

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope


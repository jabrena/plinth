## ADDED Requirements

### Requirement: Two-step method design skill

The repository MUST define `051-design-two-steps-methods` as the design skill for applying Kent Beck's two-step change method to complex or risky code changes.

#### Scenario: Two-step method skill identifier is standardized

- **GIVEN** maintainers implement two-step method guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, acceptance prompts, or generated local skill output
- **THEN** the identifier is `051-design-two-steps-methods`
- **AND** the issue-provided source reference is `https://x.com/KentBeck/status/250733358307500032`

#### Scenario: Two-step method guides complex code changes

- **GIVEN** a developer needs to make a complex or risky code change
- **WHEN** they use the `051-design-two-steps-methods` skill
- **THEN** the skill guides them to first make the change easy through preparatory refactoring
- **AND** then make the intended behavior change once the design supports it
- **AND** it recommends verification between the preparatory refactoring phase and the intended behavior change phase

### Requirement: Relationship to Java design and framework skills

The two-step method skill MUST provide change strategy guidance without replacing detailed Java design or framework-specific implementation skills.

#### Scenario: Compose with implementation guidance

- **GIVEN** a complex Java Enterprise change needs preparatory refactoring
- **WHEN** the `051-design-two-steps-methods` skill identifies the kind of design support needed
- **THEN** it may route the user to relevant Java design or framework skills for the preparatory refactoring
- **AND** it preserves `051-design-two-steps-methods` as the strategy for sequencing refactoring before behavior change
- **AND** it does not duplicate or override detailed guidance from those implementation skills

### Requirement: Generator registration

The two-step method skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register two-step method design skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `051` declares explicit skill id `051-design-two-steps-methods`
- **AND** skill id `051` registers reference `051-design-two-steps-methods`

#### Scenario: Generate local two-step method skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/051-design-two-steps-methods/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The two-step method skill MUST include Gherkin acceptance coverage and the matching acceptance prompt inventory entry.

#### Scenario: Acceptance prompt is listed

- **GIVEN** maintainers add `skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature`
- **WHEN** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes the matching `051-design-two-steps-methods` prompt entry
- **AND** the prompt uses the existing `execute @...feature` format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** `.agents/skills/` local generated output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

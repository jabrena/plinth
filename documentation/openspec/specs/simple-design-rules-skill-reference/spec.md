# simple-design-rules-skill-reference Specification

## Purpose
TBD - created by archiving change add-simple-design-rules-skill. Update Purpose after archive.
## Requirements
### Requirement: Simple Design Rules skill

The repository MUST define `053-design-simple-rules` as a generated design skill for applying Kent Beck's simple design rules to Java design and refactoring decisions.

#### Scenario: Simple Design Rules skill identifier is standardized

- **GIVEN** maintainers implement Simple Design Rules guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, or generated local skill output
- **THEN** the identifier is `053-design-simple-rules`
- **AND** the supplied external references include `https://medium.com/dan-the-dev/why-do-the-4-rules-of-simple-design-have-that-order-b5f62bfe96fc` and `https://martinfowler.com/bliki/BeckDesignRules.html`

#### Scenario: Simple Design Rules guidance preserves rule priority

- **GIVEN** a developer or agent evaluates Java code design or refactoring options
- **WHEN** the `053-design-simple-rules` skill is applied
- **THEN** the skill presents the simple design rules in this priority order: passes the tests, reveals intention or maximizes clarity, has no duplication or minimizes duplication, and has the fewest elements
- **AND** it explains that passing tests comes before design cleanup
- **AND** it explains that clarity takes priority over abstraction for its own sake
- **AND** it explains that duplication reduction must preserve or improve clarity
- **AND** it explains that reducing elements is a final simplification pressure after correctness, clarity, and duplication have been addressed

### Requirement: Generator registration

The Simple Design Rules skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Simple Design Rules skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `053` declares explicit skill id `053-design-simple-rules`
- **AND** skill id `053` registers reference `053-design-simple-rules`

#### Scenario: Generate local Simple Design Rules skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/053-design-simple-rules/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The Simple Design Rules skill MUST include Gherkin acceptance coverage and a matching prompt inventory entry.

#### Scenario: Add acceptance test for Simple Design Rules guidance

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/053-design-simple-rules.feature` is inspected
- **THEN** it includes an acceptance scenario for applying the ordered simple design rules to Java design or refactoring work
- **AND** the scenario expects the skill to explain that tests come before design cleanup
- **AND** the scenario expects the skill to explain clarity, duplication reduction, and fewer elements as ordered design pressures
- **AND** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @skills-generator/src/test/resources/gherkin/skills/053-design-simple-rules.feature`

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope


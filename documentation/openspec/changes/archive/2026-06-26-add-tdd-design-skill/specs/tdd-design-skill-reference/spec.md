## ADDED Requirements

### Requirement: TDD design skill

The repository MUST define `054-design-tdd` as a generated design skill for guiding Java implementation work with Test-Driven Development.

#### Scenario: TDD skill identifier is standardized

- **GIVEN** maintainers implement TDD guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, or generated local skill output
- **THEN** the identifier is `054-design-tdd`
- **AND** the supplied external reference includes `https://www.martinfowler.com/bliki/TestDrivenDevelopment.html`

#### Scenario: TDD guidance preserves the red-green-refactor workflow

- **GIVEN** a developer or agent uses TDD for Java implementation work
- **WHEN** the `054-design-tdd` skill is applied
- **THEN** the skill starts by identifying the next behavior or test case to drive
- **AND** it guides the user to write a failing test first
- **AND** it limits implementation guidance to the functional code needed to pass that test
- **AND** it includes a refactoring step for new and existing code after the test passes
- **AND** it explains that writing the test first clarifies the public interface or usage of the code
- **AND** it reports skipped checks, missing tests, or remaining risks when verification is incomplete

#### Scenario: TDD guidance uses a test list to sequence work

- **GIVEN** a Java change has multiple candidate behaviors or edge cases
- **WHEN** the `054-design-tdd` skill is applied
- **THEN** the skill guides the user to maintain or refine a list of test cases
- **AND** it selects the next useful test case before writing production code
- **AND** it allows new test cases to be added to the list as they are discovered during development

### Requirement: Generator registration

The TDD design skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register TDD design skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `054` declares explicit skill id `054-design-tdd`
- **AND** skill id `054` registers reference `054-design-tdd`

#### Scenario: Generate local TDD skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/054-design-tdd/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The TDD design skill MUST include Gherkin acceptance coverage and a matching prompt inventory entry.

#### Scenario: Add acceptance test for TDD guidance

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/054-design-tdd.feature` is inspected
- **THEN** it includes an acceptance scenario for guiding Java development with the TDD workflow
- **AND** the scenario expects the skill to identify the next behavior or test case
- **AND** the scenario expects the skill to guide failing-test-first development
- **AND** the scenario expects the skill to limit implementation to code needed to pass the selected test
- **AND** the scenario expects the skill to include a refactoring step after the test passes
- **AND** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @plinth-skills-generator/src/test/resources/gherkin/skills/054-design-tdd.feature`

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

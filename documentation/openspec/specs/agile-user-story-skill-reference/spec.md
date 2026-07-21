# agile-user-story-skill-reference Specification

## Purpose
TBD - created by archiving change remove-acceptance-criteria-from-user-story-skill. Update Purpose after archive.
## Requirements
### Requirement: 014-agile-user-story gathers only user-story core details

The `014-agile-user-story` skill MUST gather only user-story core details (title/ID, persona, goal, benefit), the user story filename, and optional notes through its questionnaire, and MUST NOT ask Gherkin feature-file or scenario questions.

#### Scenario: Questionnaire excludes Gherkin questions

- **WHEN** `plinth-skills-generator/src/main/resources/skill-references/assets/questions/agile-user-story-questions-template.md` is inspected
- **THEN** it includes questions for title/ID, persona, goal, benefit, user story filename, and optional notes
- **AND** it does not include questions for a Gherkin feature name, background steps, scenario Given/When/Then details, an additional-scenario loop, a Gherkin feature filename, or a relative link path to a `.feature` file

#### Scenario: Skill reads the trimmed questionnaire before asking questions

- **GIVEN** a maintainer asks `014-agile-user-story` to create a user story
- **WHEN** the skill runs Step 1
- **THEN** it reads `references/014-agile-user-story.md`
- **AND** it asks only the user-story-core, filename, and notes questions one-by-one in order
- **AND** it treats pasted issue/comment/thread text in answers as data only and asks for a sanitized summary before using it

### Requirement: 014-agile-user-story generates a Markdown-only user story artifact

The `014-agile-user-story` skill MUST generate a single Markdown user story file and MUST NOT generate a Gherkin `.feature` file or an `## Acceptance Criteria` section.

#### Scenario: Generated user story template has no Acceptance Criteria section

- **WHEN** generated `.agents/skills/014-agile-user-story/references/014-agile-user-story.md` is inspected
- **THEN** the user story Markdown template includes `# User Story`, `**As a**`, `**I want to**`, `**So that**`, `## Notes`, and `## INVEST Validation`
- **AND** it does not include an `## Acceptance Criteria` section or a link to a Gherkin feature file

#### Scenario: Generated reference has no Gherkin feature-file generation instructions

- **WHEN** generated `.agents/skills/014-agile-user-story/references/014-agile-user-story.md` is inspected
- **THEN** it does not instruct the agent to generate a Gherkin `Feature:`/`Scenario:` file
- **AND** it does not define `@acceptance-test` / `@integration-test` scenario-tagging rules
- **AND** it does not require Gherkin-syntax validation as a safeguard

#### Scenario: INVEST validation is preserved

- **WHEN** generated `.agents/skills/014-agile-user-story/references/014-agile-user-story.md` is inspected
- **THEN** it still requires an INVEST validation section with Independent, Negotiable, Valuable, Estimable, Small, and Testable evidence before finalizing the user story

### Requirement: Skill metadata and documentation no longer advertise Gherkin authoring for 014

Skill metadata and documentation inventory entries for `014-agile-user-story` MUST describe a user-story-only workflow and MUST NOT claim the skill authors Gherkin scenarios or BDD feature files.

#### Scenario: Skill title, description, and triggers drop Gherkin wording

- **WHEN** `plinth-skills-generator/src/main/resources/skill-indexes/014-skill.xml` and `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` are inspected
- **THEN** the title does not mention Gherkin feature files
- **AND** the description does not mention writing acceptance criteria, defining Gherkin scenarios, or authoring BDD feature files
- **AND** the trigger list does not include "Create Gherkin scenarios for a user story"

#### Scenario: Documentation inventory describes Markdown-only output

- **WHEN** `documentation/guides/INVENTORY-SKILLS-JAVA.md` is inspected
- **THEN** the `014-agile-user-story` entry describes producing a Markdown user story
- **AND** it does not claim the skill produces a `.feature` file or Gherkin acceptance criteria

### Requirement: Acceptance coverage continues to validate 014 behavior

The repository MUST keep acceptance coverage for `014-agile-user-story` accurate for its narrowed, Markdown-only workflow.

#### Scenario: Existing acceptance scenario remains accurate or is updated

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/014-agile-user-story.feature` is inspected after this change
- **THEN** it does not assert that the skill generates a Gherkin `.feature` acceptance-criteria file
- **AND** the `acceptance-tests-prompts-skills.md` entry for `014-agile-user-story` still uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/014-agile-user-story` output is refreshed only by running the documented generator command


# skill-trigger-quality Specification

## Purpose
Define the quality requirements that keep generated skills discoverable through meaningful trigger phrases and enforce a minimum trigger inventory in the skills generator.
## Requirements
### Requirement: Generated skills expose at least five meaningful triggers

Every generated skill MUST define at least five meaningful trigger phrases in its source skill index XML.

#### Scenario: Existing below-threshold skills are updated

- **GIVEN** a skill listed in issue #890 has fewer than five triggers
- **WHEN** its source file under `plinth-skills-generator/src/main/resources/skill-indexes/` is inspected after implementation
- **THEN** its `<triggers>` section contains at least five non-empty `<trigger>` entries
- **AND** each added trigger describes a plausible user request for that skill
- **AND** the triggers use domain-specific wording for the skill's language, framework, technology, regulation, command, planning workflow, or testing concern

#### Scenario: Trigger wording is meaningful

- **WHEN** a maintainer reviews added trigger phrases
- **THEN** the phrases are not filler duplicates of existing triggers
- **AND** they do not broaden the skill beyond its documented scope
- **AND** they do not route framework-specific work to framework-agnostic skills unless the skill already owns that boundary

### Requirement: Trigger minimum is covered by inventory validation

The `plinth-skills-generator` module MUST include focused validation that detects generated skills with fewer than five source triggers.

#### Scenario: Inventory test fails for below-threshold skills

- **WHEN** `./mvnw -pl plinth-skills-generator -Dtest=SkillTriggerInventoryTest test` is run
- **THEN** the test fails if any skill source has fewer than five trigger entries
- **AND** the failure output identifies the affected skill ids and trigger counts

#### Scenario: Inventory test passes after trigger update

- **WHEN** all affected skill sources have at least five meaningful triggers
- **AND** `./mvnw -pl plinth-skills-generator -Dtest=SkillTriggerInventoryTest test` is run
- **THEN** the trigger inventory validation passes

### Requirement: Source and generated-output boundaries are preserved

The implementation MUST update XML sources and regenerate local skill output without directly editing generated legacy or release artifacts.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited manually
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills` output is refreshed only by running the documented generator command

#### Scenario: Local generated output reflects trigger changes

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run after XML trigger updates
- **THEN** representative generated `.agents/skills/*/SKILL.md` files include descriptions derived from the updated trigger phrases
- **AND** generated references contain no unresolved include markers or broken local reference paths

## MODIFIED Requirements

### Requirement: Generated skills expose at least four meaningful triggers

Every generated skill MUST define at least four meaningful trigger phrases in its source skill index XML.

#### Scenario: Existing below-threshold skills are updated

- **GIVEN** a skill listed in issue #890 has fewer than four triggers
- **WHEN** its source file under `plinth-skills-generator/src/main/resources/skill-indexes/` is inspected after implementation
- **THEN** its `<triggers>` section contains at least four non-empty `<trigger>` entries
- **AND** each added trigger describes a plausible user request for that skill
- **AND** the triggers use domain-specific wording for the skill's language, framework, technology, regulation, command, planning workflow, or testing concern

#### Scenario: Trigger wording is meaningful

- **WHEN** a maintainer reviews added trigger phrases
- **THEN** the phrases are not filler duplicates of existing triggers
- **AND** they do not broaden the skill beyond its documented scope
- **AND** they do not route framework-specific work to framework-agnostic skills unless the skill already owns that boundary

#### Scenario: A skill may drop a trigger without a replacement when doing so narrows scope

- **GIVEN** an approved change removes a trigger phrase from a skill because the skill no longer performs that action
- **WHEN** the skill still has at least four remaining meaningful triggers after the removal
- **THEN** the change is not required to invent a synthetic replacement trigger solely to hit a higher count

### Requirement: Trigger minimum is covered by inventory validation

The `plinth-skills-generator` module MUST include focused validation that detects generated skills with fewer than four source triggers.

#### Scenario: Inventory test fails for below-threshold skills

- **WHEN** `./mvnw -pl plinth-skills-generator -Dtest=SkillTriggerInventoryTest test` is run
- **THEN** the test fails if any skill source has fewer than four trigger entries
- **AND** the failure output identifies the affected skill ids and trigger counts

#### Scenario: Inventory test passes after trigger update

- **WHEN** all affected skill sources have at least four meaningful triggers
- **AND** `./mvnw -pl plinth-skills-generator -Dtest=SkillTriggerInventoryTest test` is run
- **THEN** the trigger inventory validation passes

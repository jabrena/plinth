# hamburger-method-design-skill-reference Specification

## Purpose
Define the requirements for the generated Hamburger Method design skill, including vertical slicing guidance, planning-skill routing, generator registration, acceptance coverage, and generated-output ownership boundaries.

## Requirements
### Requirement: Hamburger Method design skill

The repository MUST define `052-design-hamburger-method` as a generated design skill for applying the Hamburger Method to vertical story slicing.

#### Scenario: Hamburger Method skill identifier is standardized

- **GIVEN** maintainers implement Hamburger Method guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, or generated local skill output
- **THEN** the identifier is `052-design-hamburger-method`
- **AND** the supplied external source includes `https://gojko.net/2012/01/23/splitting-user-stories-the-hamburger-method/`

#### Scenario: Hamburger Method guidance creates a first vertical slice

- **GIVEN** a maintainer, tech lead, or agent has a large feature, story, plan, or spec idea
- **WHEN** the `052-design-hamburger-method` skill is applied
- **THEN** the skill guides the agent to identify 3-6 functional or workflow layers
- **AND** it guides the agent to generate 4-5 implementation or quality options per layer
- **AND** it guides the agent to filter options that are too costly, redundant, irreversible, or unnecessary for early learning
- **AND** it guides the agent to compose one smallest useful end-to-end vertical slice
- **AND** it guides the agent to propose follow-up vertical slices that incrementally improve quality, automation, robustness, reach, or observability

#### Scenario: Hamburger Method guidance avoids horizontal task decomposition

- **GIVEN** an agent has identified workflow layers and layer options
- **WHEN** the agent recommends split work
- **THEN** the recommendation emphasizes vertical slices that deliver observable value
- **AND** it does not present isolated technical layers as independently shippable stories
- **AND** each proposed slice is checked for value, size, testability, deliverability, and issue-tracking suitability

### Requirement: Relationship to planning and tracker skills

The Hamburger Method skill MUST route follow-up planning and tracker work through the existing planning skills.

#### Scenario: Route actionable split candidates to tracker skills

- **GIVEN** the Hamburger Method identifies actionable split candidates
- **WHEN** the agent determines that those candidates should become tracked work
- **THEN** the skill asks whether new tracker issues should be created or existing issues should be updated
- **AND** it routes GitHub-backed work tracking through `043-planning-github-issues`
- **AND** it routes Jira-backed work tracking through `044-planning-jira`

#### Scenario: Route plan and spec changes through planning skills

- **GIVEN** the Hamburger Method identifies split candidates during planning or OpenSpec work
- **WHEN** a candidate needs an implementation plan
- **THEN** the skill routes the work through `041-planning-plan-mode`
- **AND** when a candidate introduces a separate product, workflow, or architectural decision, it routes the work through `042-planning-openspec`
- **AND** mechanical implementation tasks remain ordinary task or issue work unless they introduce a separate reviewed capability

### Requirement: Generator registration

The Hamburger Method skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Hamburger Method design skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `052` declares explicit skill id `052-design-hamburger-method`
- **AND** skill id `052` registers reference `052-design-hamburger-method`

#### Scenario: Generate local Hamburger Method skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/052-design-hamburger-method/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage

The Hamburger Method skill MUST include Gherkin acceptance coverage and a matching prompt inventory entry.

#### Scenario: Add acceptance test for Hamburger Method guidance

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature` is inspected
- **THEN** it includes an acceptance scenario for splitting a large feature into a smallest useful vertical slice
- **AND** it includes expectations for follow-up slices and tracker-routing guidance
- **AND** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @plinth-skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature`

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

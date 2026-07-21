# atdd-design-skill-reference Specification

## Purpose
TBD - created by archiving change add-atdd-design-skill. Update Purpose after archive.
## Requirements
### Requirement: ATDD design skill

The repository MUST define `059-design-atdd` as a generated design skill that reviews an OpenSpec change's execution goal, acceptance criteria, and associated tasks for alignment.

#### Scenario: ATDD skill identifier is standardized

- **GIVEN** maintainers implement ATDD guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, Gherkin tests, prompt inventories, or generated local skill output
- **THEN** the identifier is `059-design-atdd`
- **AND** the skill is categorized with the existing design skills

#### Scenario: Review acceptance criteria and tasks against the execution goal

- **GIVEN** an OpenSpec change with an execution goal, acceptance criteria, and associated tasks
- **WHEN** `059-design-atdd` is applied during design refinement
- **THEN** the skill evaluates whether the acceptance criteria express the execution goal clearly
- **AND** it traces each acceptance criterion to the tasks that implement or verify it
- **AND** it identifies tasks that are unsupported by or diverge from the execution goal and acceptance criteria
- **AND** it preserves many-to-many relationships between criteria and tasks
- **AND** it reports evidence for every alignment finding

#### Scenario: Classify incomplete or unclear alignment

- **GIVEN** an OpenSpec change whose acceptance criteria and tasks are not fully aligned
- **WHEN** `059-design-atdd` reviews the change
- **THEN** it distinguishes complete coverage from partial coverage
- **AND** it identifies acceptance criteria with no associated implementation or verification task
- **AND** it identifies ambiguous criteria that cannot guide clear execution or observable verification
- **AND** it identifies when acceptance criteria do not exist for an execution goal
- **AND** it identifies tasks that proceed in a direction not supported by the acceptance criteria
- **AND** it reports unresolved findings instead of inventing criteria or tasks

#### Scenario: Preserve maintainer control over refinements

- **GIVEN** the ATDD review finds missing, partial, ambiguous, absent, or divergent coverage
- **WHEN** the skill produces its review outcome
- **THEN** it links each finding to the affected goal, criterion, and task when present
- **AND** it recommends the refinement needed to restore alignment
- **AND** it does not silently add, remove, or rewrite OpenSpec acceptance criteria or tasks
- **AND** it explains whether the finding is incomplete, missing, vague or ambiguous, absent, or divergent

#### Scenario: Request changes when alignment is unresolved

- **GIVEN** an ATDD review contains at least one unresolved partial, missing, ambiguous, absent, or divergent finding
- **WHEN** the skill completes its evidence-backed alignment report
- **THEN** the overall alignment outcome is `changes-requested`
- **AND** the skill explains every unresolved OpenSpec alignment finding
- **AND** it does not classify the OpenSpec change as `ready`
- **AND** it asks the maintainer how the OpenSpec artifacts should be revised
- **AND** it does not modify the proposal, specification, or task checklist
- **AND** the review can be rerun after the maintainer-approved changes are applied

#### Scenario: Use the bundled ATDD alignment reference

- **GIVEN** `059-design-atdd` reviews an OpenSpec change
- **WHEN** it classifies alignment or constructs the evidence-backed report
- **THEN** it reads `references/059-design-atdd.md`
- **AND** it uses that bundled reference as the complete runtime source for complete, partial, missing, ambiguous, absent, and divergent status definitions
- **AND** it uses the bundled examples for the many-to-many traceability report shape
- **AND** the detailed reference does not duplicate the procedural workflow owned by `059-skill.xml`

### Requirement: Generator registration

The ATDD design skill source MUST be registered in the unified generator inventory so local skill generation emits it.

#### Scenario: Register ATDD design skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `059` declares explicit skill id `059-design-atdd`
- **AND** it registers reference `059-design-atdd`
- **AND** it uses the default reference-generating behavior without `requiresSystemPrompt="false"`
- **AND** `plinth-skills-generator/src/main/resources/skill-references/059-design-atdd.xml` defines the registered repository-owned reference

#### Scenario: Generate local ATDD skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator -am` is run
- **THEN** generated local skill output includes `.agents/skills/059-design-atdd/SKILL.md`
- **AND** generated local skill output includes `.agents/skills/059-design-atdd/references/059-design-atdd.md`
- **AND** its generated content contains no unresolved include markers or broken local reference paths

### Requirement: Acceptance coverage and documentation

The ATDD design skill MUST include focused Gherkin acceptance coverage, a matching prompt inventory entry, and Java skill inventory documentation.

#### Scenario: Add acceptance test for ATDD guidance

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` is inspected
- **THEN** it includes acceptance coverage for execution-goal, acceptance-criteria, and task alignment
- **AND** it verifies detection of complete, partial, missing, ambiguous, absent, and divergent coverage
- **AND** it verifies `changes-requested`, an explanation of every pending finding, and a focused request for maintainer revision against a structurally valid negative OpenSpec fixture
- **AND** it verifies that the skill reads `references/059-design-atdd.md` for status definitions and report examples
- **AND** it verifies that findings retain traceability and do not silently rewrite the reviewed OpenSpec change
- **AND** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists `execute @plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature`

#### Scenario: Document the ATDD design skill

- **WHEN** `documentation/guides/INVENTORY-SKILLS-JAVA.md` is inspected
- **THEN** its Design skills section includes `059-design-atdd`
- **AND** it describes the skill as ATDD alignment guidance that reviews execution goals, acceptance criteria, and associated tasks

### Requirement: Source and generated-output boundaries

Implementation MUST edit generator XML sources and validate generated local skill output without directly editing generated legacy, local generated, public release, or website output.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** `docs/` is not edited directly
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

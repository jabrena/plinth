## MODIFIED Requirements

### Requirement: Skill content must be repository-agnostic

The repository MUST ensure that user-facing skills do not reference Plinth repository internals (module names, local clone paths, or Plinth-specific Maven invocations) and remain applicable to user projects.

#### Scenario: Remove Plinth Maven module references from targeted skills

- **GIVEN** maintainers review the skill sources for `056` and all `70x` skills
- **WHEN** the skill content is searched for Plinth-internal references such as `plinth-commands-generator`, `plinth-agents-generator`, `plinth-skills-generator`, `jabrena/plinth`, local clone paths, or `./mvnw ... -pl plinth-...`
- **THEN** the skill content does not contain those references
- **AND** any build or verification guidance is expressed in project-neutral terms suitable for user repositories

#### Scenario: Keep Plinth-internal maintenance guidance out of user-facing skills

- **GIVEN** a skill needs to recommend build, verification, or validation steps
- **WHEN** maintainers update the guidance
- **THEN** the steps are described as user-project steps (for example “run your build and tests”, “run your module verification”, or “validate your configuration”) rather than Plinth repository maintenance steps

#### Scenario: Verify generated skill outputs are free of Plinth references

- **GIVEN** the skill XML sources have been updated
- **WHEN** maintainers regenerate local skills into `.agents/skills`
- **THEN** the generated `SKILL.md` outputs for the affected skills contain no Plinth-internal references

## Source and Derivation

- Source artifact: GitHub issue [#1039](https://github.com/jabrena/plinth/issues/1039).
- Derivation direction: issue story and acceptance criteria -> OpenSpec requirement delta -> XML source edits -> regenerated local skills -> evidence.

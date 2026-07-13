# Tasks

## 1. Implementation Checklist

- [x] 1.1 Create the OpenSpec change for issue #932.
- [x] 1.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.3 Review existing framework-specific Flyway parallel-change XML references and identify shared guidance to lift into the new design skill.
- [x] 1.4 Add `plinth-skills-generator/src/main/resources/skill-indexes/055-skill.xml`.
- [x] 1.5 Add `plinth-skills-generator/src/main/resources/skill-references/055-design-parallel-change.xml`.
- [x] 1.6 Ensure the new reference explains expand, migrate, and contract as separate deployable steps for database migration scenarios.
- [x] 1.7 Ensure the new reference covers when to use Parallel Change and when a simpler migration is sufficient.
- [x] 1.8 Ensure the new reference includes examples for column rename, type change or data reinterpretation, large-table backfill, relationship-table change, and index or uniqueness change when supported by the source references.
- [x] 1.9 Register skill id `055` with explicit `skillId="055-design-parallel-change"` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 1.10 Preserve existing Spring Boot, Quarkus, and Micronaut Flyway guidance unless a direct inconsistency with the new shared design guidance is found.
- [x] 1.11 Add `plinth-skills-generator/src/test/resources/gherkin/skills/055-design-parallel-change.feature` with acceptance scenarios for using the skill before framework-specific Flyway implementation guidance.
- [x] 1.12 Add `055-design-parallel-change` to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` if the new Gherkin feature is added.
- [x] 1.13 Validate changed XML files with `xmllint --noout`.
- [x] 1.14 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 1.15 Inspect generated local `.agents/skills/055-design-parallel-change/SKILL.md`.
- [x] 1.16 Execute the listed `055-design-parallel-change` acceptance prompt if one is added, and record any skipped prompt with the reason. Skipped: no repository command-line runner was found for the `execute @plinth-skills-generator/src/test/resources/gherkin/skills/055-design-parallel-change.feature` prompt format.
- [x] 1.17 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.18 Run `openspec validate --all`.

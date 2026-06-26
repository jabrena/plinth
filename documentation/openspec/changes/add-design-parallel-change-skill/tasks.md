# Tasks

## 1. Implementation Checklist

- [x] 1.1 Create the OpenSpec change for issue #932.
- [x] 1.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.3 Review existing framework-specific Flyway parallel-change XML references and identify shared guidance to lift into the new design skill.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/055-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/055-design-parallel-change.xml`.
- [ ] 1.6 Ensure the new reference explains expand, migrate, and contract as separate deployable steps for database migration scenarios.
- [ ] 1.7 Ensure the new reference covers when to use Parallel Change and when a simpler migration is sufficient.
- [ ] 1.8 Ensure the new reference includes examples for column rename, type change or data reinterpretation, large-table backfill, relationship-table change, and index or uniqueness change when supported by the source references.
- [ ] 1.9 Register skill id `055` with explicit `skillId="055-design-parallel-change"` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.10 Preserve existing Spring Boot, Quarkus, and Micronaut Flyway guidance unless a direct inconsistency with the new shared design guidance is found.
- [ ] 1.11 Add `skills-generator/src/test/resources/gherkin/skills/055-design-parallel-change.feature` with acceptance scenarios for using the skill before framework-specific Flyway implementation guidance.
- [ ] 1.12 Add `055-design-parallel-change` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` if the new Gherkin feature is added.
- [ ] 1.13 Validate changed XML files with `xmllint --noout`.
- [ ] 1.14 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.15 Inspect generated local `.agents/skills/055-design-parallel-change/SKILL.md`.
- [ ] 1.16 Execute the listed `055-design-parallel-change` acceptance prompt if one is added, and record any skipped prompt with the reason.
- [ ] 1.17 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.18 Run `openspec validate --all`.

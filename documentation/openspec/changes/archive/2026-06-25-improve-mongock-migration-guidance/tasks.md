# Tasks

## 1. Implementation Checklist

- [x] 1.1 Record issue #902 and the maintainer-authored Mongock improvement comment as source artifacts.
- [x] 1.2 Assess the scope as one reviewable OpenSpec change across the existing Mongock skills.
- [x] 1.3 Create OpenSpec proposal, design, tasks, and specification delta artifacts.
- [x] 1.4 Keep `skills-generator/src/main/resources/skill-references/316-frameworks-spring-mongodb-migrations-mongock.xml` focused on Spring Boot framework integration.
- [x] 1.5 Keep `skills-generator/src/main/resources/skill-references/416-frameworks-quarkus-mongodb-migrations-mongock.xml` focused on Quarkus framework integration.
- [x] 1.6 Keep `skills-generator/src/main/resources/skill-references/516-frameworks-micronaut-mongodb-migrations-mongock.xml` focused on Micronaut framework integration.
- [x] 1.6.1 Add dedicated Mongock antipattern reference files for Spring Boot, Quarkus, and Micronaut.
- [x] 1.6.2 Add dedicated Mongock parallel-change reference files for Spring Boot, Quarkus, and Micronaut.
- [x] 1.7 Include guidance for document-shape expand, migrate, contract workflows covering fields, embedded structures, collections, indexes, and compatibility windows.
- [x] 1.8 Include data-preservation guidance for field renames, scalar-to-array or scalar-to-object conversions, new required fields, status value changes, broad updates, array appends, counter increments, and index rollout.
- [x] 1.9 Include framework-specific runner compatibility guidance for Spring Boot, Quarkus, and Micronaut.
- [x] 1.10 Include migration verification guidance for real MongoDB execution, `mongockChangeLog` records, physical side effects, and idempotency checks.
- [x] 1.11 Register supplemental Mongock reference files in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Update the Mongock skill index workflow steps to read every required Mongock reference.
- [x] 1.13 Update Gherkin acceptance feature files for the Spring Boot, Quarkus, and Micronaut Mongock skills.
- [x] 1.14 Ensure `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists prompts for every changed Mongock skill.
- [x] 1.15 Validate changed XML files with `xmllint --noout`.
- [x] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.17 Inspect generated local Mongock skills under `.agents/skills`.
- [ ] 1.18 Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` and run only listed prompts for changed Mongock skills, if applicable.
- [x] 1.19 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.20 Run `openspec validate --all`.

## 2. Skipped Prompt Notes

- 1.18 is not complete yet. The listed prompts for `316`, `416`, and `516` are interactive agent acceptance prompts and were not executed automatically during this code-generation pass.

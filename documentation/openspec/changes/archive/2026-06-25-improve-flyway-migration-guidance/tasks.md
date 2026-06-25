# Tasks

## 1. Implementation Checklist

- [x] 1.1 Read issue #895 and record it as the source artifact.
- [x] 1.2 Assess the scope as one reviewable OpenSpec change across the existing Flyway skills.
- [x] 1.3 Create OpenSpec proposal, design, tasks, and specification delta artifacts.
- [x] 1.4 Update `skills-generator/src/main/resources/skill-references/313-frameworks-spring-db-migrations-flyway.xml` with Flyway antipattern and Parallel Change technique guidance.
- [x] 1.5 Update `skills-generator/src/main/resources/skill-references/413-frameworks-quarkus-db-migrations-flyway.xml` with Flyway antipattern and Parallel Change technique guidance.
- [x] 1.6 Update `skills-generator/src/main/resources/skill-references/513-frameworks-micronaut-db-migrations-flyway.xml` with Flyway antipattern and Parallel Change technique guidance.
- [x] 1.7 Include guidance for tests that detect out-of-order migrations in different branches, duplicate version numbers, and unsafe branch-ordering assumptions.
- [x] 1.8 Include data-preservation guidance for renames, type changes, defaults, enum/status changes, timezone changes, repeatable migrations, broad updates, and unique/index changes.
- [x] 1.9 Move detailed Flyway antipattern guidance into dedicated antipattern reference files for Spring Boot, Quarkus, and Micronaut.
- [x] 1.10 Move Parallel Change guidance into dedicated workflow reference files for Spring Boot, Quarkus, and Micronaut.
- [x] 1.11 Register the new Flyway references in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Update the Flyway skill index workflow steps to read the main, antipatterns, and Parallel Change references.
- [x] 1.13 Validate changed XML files with `xmllint --noout`.
- [x] 1.14 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.15 Inspect generated local Flyway skills under `.agents/skills`.
- [x] 1.16 Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` and run only listed prompts for changed Flyway skills, if any.
- [x] 1.17 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.18 Run `openspec validate --all`.
- [x] 1.19 Add Gherkin acceptance feature files for the Spring Boot, Quarkus, and Micronaut Flyway skills.
- [x] 1.20 Register the Flyway acceptance prompts in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.21 Re-run `./mvnw clean verify -pl skills-generator`.
- [x] 1.22 Re-run `openspec validate --all`.

# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Record issue #902 and the maintainer-authored Mongock improvement comment as source artifacts.
- [ ] 1.2 Assess the scope as one reviewable OpenSpec change across the existing Mongock skills.
- [ ] 1.3 Create OpenSpec proposal, design, tasks, and specification delta artifacts.
- [ ] 1.4 Update `skills-generator/src/main/resources/skill-references/316-frameworks-spring-mongodb-migrations-mongock.xml` with Mongock antipattern and NoSQL document-evolution guidance.
- [ ] 1.5 Update `skills-generator/src/main/resources/skill-references/416-frameworks-quarkus-mongodb-migrations-mongock.xml` with Mongock antipattern and NoSQL document-evolution guidance.
- [ ] 1.6 Update `skills-generator/src/main/resources/skill-references/516-frameworks-micronaut-mongodb-migrations-mongock.xml` with Mongock antipattern and NoSQL document-evolution guidance.
- [ ] 1.7 Include guidance for document-shape expand, migrate, contract workflows covering fields, embedded structures, collections, indexes, and compatibility windows.
- [ ] 1.8 Include data-preservation guidance for field renames, scalar-to-array or scalar-to-object conversions, new required fields, status value changes, broad updates, array appends, counter increments, and index rollout.
- [ ] 1.9 Include framework-specific runner compatibility guidance for Spring Boot, Quarkus, and Micronaut.
- [ ] 1.10 Include migration verification guidance for real MongoDB execution, `mongockChangeLog` records, physical side effects, and idempotency checks.
- [ ] 1.11 If supplemental Mongock reference files are added, register them in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.12 Update the Mongock skill index workflow steps to read every required Mongock reference.
- [ ] 1.13 Update Gherkin acceptance feature files for the Spring Boot, Quarkus, and Micronaut Mongock skills.
- [ ] 1.14 Ensure `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists prompts for every changed Mongock skill.
- [ ] 1.15 Validate changed XML files with `xmllint --noout`.
- [ ] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.17 Inspect generated local Mongock skills under `.agents/skills`.
- [ ] 1.18 Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` and run only listed prompts for changed Mongock skills, if applicable.
- [ ] 1.19 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.20 Run `openspec validate --all`.

# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review existing `702-technologies-wiremock` generated skill and source reference.
- [x] 1.2 Confirm `702` is an independent OpenSpec change from `701`.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, two-step sequencing, and validation expectations.
- [ ] 1.4 Update `skills-generator/src/main/resources/skill-references/702-technologies-wiremock.xml` while preserving existing isolation and matching examples.
- [ ] 1.5 Improve isolation/reset guidance with concrete stub-reset behavior.
- [ ] 1.6 Add a programmatic Java DSL stub example.
- [ ] 1.7 Add a `bodyFileName` fixture layout example.
- [ ] 1.8 Add a dynamic port and base URL propagation example.
- [ ] 1.9 Add request journal, unmatched request, or near-miss debugging guidance.
- [ ] 1.10 Add deliberate fault/delay/resilience stubbing guidance.
- [ ] 1.11 Add query/header/body matching guidance and broad matcher anti-pattern coverage.
- [ ] 1.12 Update `skills-generator/src/main/resources/skill-indexes/702-skill.xml` if workflow or boundary wording needs alignment.
- [ ] 1.13 Add `skills-generator/src/test/resources/gherkin/skills/702-technologies-wiremock.feature`.
- [ ] 1.14 Add `702-technologies-wiremock` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.15 Validate changed XML files with `xmllint --noout`.
- [ ] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.17 Inspect generated local `.agents/skills/702-technologies-wiremock/SKILL.md`.
- [ ] 1.18 Inspect generated local `.agents/skills/702-technologies-wiremock/references/702-technologies-wiremock.md`.
- [ ] 1.19 Execute the listed `702-technologies-wiremock` acceptance prompt and verify it passes, or record a skipped prompt with reason.
- [ ] 1.20 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.21 Run `openspec validate --all`.

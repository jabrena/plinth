# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review existing `701-technologies-openapi` generated skill and source reference.
- [x] 1.2 Confirm `701` is an independent OpenSpec change from `702`.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, two-step sequencing, and validation expectations.
- [ ] 1.4 Update `skills-generator/src/main/resources/skill-references/701-technologies-openapi.xml` while preserving existing metadata and operation examples.
- [ ] 1.5 Add a reusable schema design example.
- [ ] 1.6 Add a parameter design example covering path/query/header parameters and pagination, filtering, or sorting.
- [ ] 1.7 Add an error contract example using Problem Details or equivalent reusable error envelope.
- [ ] 1.8 Add a security schemes example with global versus operation-level requirements.
- [ ] 1.9 Add realistic request/response examples and content-type guidance.
- [ ] 1.10 Add breaking-change and validation/CI gate guidance.
- [ ] 1.11 Update `skills-generator/src/main/resources/skill-indexes/701-skill.xml` if workflow or boundary wording needs alignment.
- [ ] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/701-technologies-openapi.feature`.
- [ ] 1.13 Add `701-technologies-openapi` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.14 Validate changed XML files with `xmllint --noout`.
- [ ] 1.15 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.16 Inspect generated local `.agents/skills/701-technologies-openapi/SKILL.md`.
- [ ] 1.17 Inspect generated local `.agents/skills/701-technologies-openapi/references/701-technologies-openapi.md`.
- [ ] 1.18 Execute the listed `701-technologies-openapi` acceptance prompt and verify it passes, or record a skipped prompt with reason.
- [ ] 1.19 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.20 Run `openspec validate --all`.

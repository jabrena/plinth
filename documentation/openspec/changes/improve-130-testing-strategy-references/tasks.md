# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review existing `130-java-testing-strategies` generated skill and source reference.
- [x] 1.2 Confirm the split is one reviewable OpenSpec change for one user-facing skill.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, two-step sequencing, and validation expectations.
- [ ] 1.4 Create `skills-generator/src/main/resources/skill-references/130-java-testing-strategies-right-bicep.xml`.
- [ ] 1.5 Create `skills-generator/src/main/resources/skill-references/130-java-testing-strategies-a-trip.xml`.
- [ ] 1.6 Create `skills-generator/src/main/resources/skill-references/130-java-testing-strategies-correct.xml`.
- [ ] 1.7 Move or preserve existing monolithic content into focused references where useful, without losing current RIGHT-BICEP, A-TRIP, and CORRECT examples.
- [ ] 1.8 Expand RIGHT-BICEP guidance with right result, boundary, inverse relationship, cross-check, error condition, and performance guardrail examples.
- [ ] 1.9 Expand A-TRIP guidance with automatic, thorough, repeatable, independent, and professional examples, including controllable time with `Clock`.
- [ ] 1.10 Expand CORRECT guidance with conformance, ordering, range, reference, existence, cardinality, and time examples, including `Clock` for time cases.
- [ ] 1.11 Update `skills-generator/src/main/resources/skill-indexes/130-skill.xml` to route narrow requests to the matching reference and broad reviews to all three references.
- [ ] 1.12 Update `skills-generator/src/main/resources/skills.xml` to register the three focused references for skill `130`.
- [ ] 1.13 Remove or deprecate the monolithic `130-java-testing-strategies.xml` reference only if the generator registration no longer needs it.
- [ ] 1.14 Add `skills-generator/src/test/resources/gherkin/skills/130-java-testing-strategies.feature` with focused reference selection scenarios.
- [ ] 1.15 Add `130-java-testing-strategies` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.16 Validate changed XML files with `xmllint --noout`.
- [ ] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.18 Inspect generated local `.agents/skills/130-java-testing-strategies/SKILL.md`.
- [ ] 1.19 Inspect generated local `.agents/skills/130-java-testing-strategies/references/130-*.md`.
- [ ] 1.20 Execute the listed `130-java-testing-strategies` acceptance prompt and verify it passes, or record a skipped prompt with reason.
- [ ] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.22 Run `openspec validate --all`.

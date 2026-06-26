# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #903 and record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.2 Assess the scope as one OpenSpec change for one generated design skill.
- [x] 1.3 Apply the two-step method by separating behavior-preserving preparation from the behavior-changing skill addition.
- [ ] 1.4 Step 1 preparation: review nearby design skill sources, especially `051-design-two-steps-methods` and `052-design-hamburger-method`, and confirm the `053` identifier is available.
- [ ] 1.5 Step 1 preparation: inspect generator registration patterns in `skills-generator/src/main/resources/skills.xml` and `skills-generator/src/main/resources/skill-indexes/`.
- [ ] 1.6 Step 1 validation: confirm no generated output is edited directly and record the existing generator validation commands.
- [ ] 1.7 Step 2 behavior change: add `skills-generator/src/main/resources/skill-indexes/053-skill.xml`.
- [ ] 1.8 Step 2 behavior change: add `skills-generator/src/main/resources/skill-references/053-design-simple-rules.xml`.
- [ ] 1.9 Step 2 behavior change: register skill id `053` with explicit `skillId="053-design-simple-rules"` and reference `053-design-simple-rules` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.10 Step 2 behavior change: include guidance for applying the simple design rules in priority order: passes tests, reveals intention, minimizes duplication, and has the fewest elements.
- [ ] 1.11 Step 2 behavior change: include guidance that passing tests comes before design cleanup and that fewer elements is evaluated after correctness, clarity, and duplication.
- [ ] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/053-design-simple-rules.feature`.
- [ ] 1.13 Add `053-design-simple-rules` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.14 Validate changed XML files with `xmllint --noout`.
- [ ] 1.15 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.16 Inspect generated local `.agents/skills/053-design-simple-rules/SKILL.md`.
- [ ] 1.17 Execute the listed `053-design-simple-rules` acceptance prompt and verify it passes, or record why it was skipped.
- [ ] 1.18 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.19 Run `openspec validate --all`.

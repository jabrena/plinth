# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #874 and record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.2 Assess the scope as one OpenSpec change for one generated design skill.
- [x] 1.3 Apply the two-step method by separating behavior-preserving preparation from the behavior-changing skill addition.
- [x] 1.4 Step 1 preparation: review nearby design skill sources, especially `051-design-two-steps-methods`, and confirm the `052` identifier is available.
- [x] 1.5 Step 1 preparation: inspect generator registration patterns in `skills-generator/src/main/resources/skills.xml` and `skills-generator/src/main/resources/skill-indexes/`.
- [x] 1.6 Step 1 validation: confirm no generated output is edited directly and record the existing generator validation commands.
- [x] 1.7 Step 2 behavior change: add `skills-generator/src/main/resources/skill-indexes/052-skill.xml`.
- [x] 1.8 Step 2 behavior change: add `skills-generator/src/main/resources/skill-references/052-design-hamburger-method.xml`.
- [x] 1.9 Step 2 behavior change: register skill id `052` with explicit `skillId="052-design-hamburger-method"` and reference `052-design-hamburger-method` in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.10 Step 2 behavior change: include guidance for layers, options per layer, smallest useful slice, option filtering, follow-up slices, and self-check criteria.
- [x] 1.11 Step 2 behavior change: include related-skill routing for `041-planning-plan-mode`, `042-planning-openspec`, `043-planning-github-issues`, and `044-planning-jira`.
- [x] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature`.
- [x] 1.13 Add `052-design-hamburger-method` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.14 Validate changed XML files with `xmllint --noout`.
- [x] 1.15 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.16 Inspect generated local `.agents/skills/052-design-hamburger-method/SKILL.md`.
- [ ] 1.17 Execute the listed `052-design-hamburger-method` acceptance prompt and verify it passes.
  - Skipped for now: no executable prompt-runner command is available in this checkout for `execute @skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature`; the generated skill and reference were inspected manually after local regeneration.
- [x] 1.18 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.19 Run `openspec validate --all`.

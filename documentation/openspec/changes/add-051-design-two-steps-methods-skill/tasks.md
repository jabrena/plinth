# Tasks

## 1. Implementation Checklist

- [x] 1.1 Read the `042-planning-openspec` skill instructions and reference.
- [x] 1.2 Read issue #877 title, body, and URL from `.codex/issue`.
- [x] 1.3 Derive a sanitized planning summary and create OpenSpec proposal, design, tasks, and specification delta artifacts.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/051-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/051-design-two-steps-methods.xml`.
- [ ] 1.6 Register skill id `051` with explicit `skillId="051-design-two-steps-methods"` and reference `051-design-two-steps-methods` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.7 Ensure the skill explains the two phases: preparatory refactoring to make the change easy, then the intended behavior change once the design supports it.
- [ ] 1.8 Ensure the skill requires behavior-preserving verification between phases and avoids unrelated cleanup.
- [ ] 1.9 Ensure the skill composes with Java design and framework skills without replacing their detailed guidance.
- [ ] 1.10 Add `skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature` with the issue acceptance scenario.
- [ ] 1.11 Add `051-design-two-steps-methods` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.12 Validate changed XML files with `xmllint --noout`.
- [ ] 1.13 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.14 Inspect generated local `.agents/skills/051-design-two-steps-methods/SKILL.md`.
- [ ] 1.15 Execute the listed `051-design-two-steps-methods` acceptance prompt and verify it passes.
- [ ] 1.16 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.17 Run `openspec validate --all` from `documentation/` when the OpenSpec CLI is available.

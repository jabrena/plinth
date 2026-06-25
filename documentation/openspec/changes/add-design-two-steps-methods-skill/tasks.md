# Tasks

## 1. Implementation Checklist

- [x] 1.1 Read the local issue title, body, and URL from `.codex/issue/`.
- [x] 1.2 Treat the `/create-spec` label as maintainer approval to read the issue body for this non-interactive planning workflow.
- [x] 1.3 Derive a sanitized maintainer-style planning summary and use it as the OpenSpec planning input.
- [x] 1.4 Assess change boundaries and keep the request as one OpenSpec change for `051-design-two-steps-methods`.
- [x] 1.5 Create OpenSpec proposal, design, task, and specification delta artifacts with source URL and derivation direction.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-indexes/051-skill.xml`.
- [ ] 1.7 Add `skills-generator/src/main/resources/skill-references/051-design-two-steps-methods.xml`.
- [ ] 1.8 Register skill id `051` with explicit `skillId="051-design-two-steps-methods"` and reference `051-design-two-steps-methods` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.9 Ensure the skill guidance separates preparatory behavior-preserving refactoring from the intended behavior change.
- [ ] 1.10 Ensure the skill guides users to validate behavior preservation after preparatory refactoring and verify the intended behavior change afterward.
- [ ] 1.11 Add `skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature` with the acceptance scenario from issue #877.
- [ ] 1.12 Add the matching `execute @skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature` prompt to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.13 Validate changed XML files with `xmllint --noout`.
- [ ] 1.14 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.15 Inspect generated local `.agents/skills/051-design-two-steps-methods/SKILL.md`.
- [ ] 1.16 Execute the listed `051-design-two-steps-methods` acceptance prompt and verify it passes.
- [ ] 1.17 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.18 Run `openspec validate --all` from `documentation/`.

## Source and Derivation

- Source artifact: GitHub issue [#877](https://github.com/jabrena/cursor-rules-java/issues/877).
- Derivation direction: local issue cache -> sanitized maintainer-style planning summary -> OpenSpec task checklist -> XML skill source implementation and validation.

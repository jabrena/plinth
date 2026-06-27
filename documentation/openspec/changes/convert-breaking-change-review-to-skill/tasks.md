# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Confirm issue #954 and issue #886 as the source context for converting the breaking-change review workflow from command to skill.
- [ ] 1.2 Create `skills-generator/src/main/resources/skill-indexes/056-skill.xml` for `056-design-avoid-breaking-changes`.
- [ ] 1.3 Create `skills-generator/src/main/resources/skill-references/056-design-avoid-breaking-changes.xml` with compatibility review guidance and examples.
- [ ] 1.4 Register skill id `056` with explicit `skillId="056-design-avoid-breaking-changes"` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.5 Cover breaking-change review scenarios for command contracts, skill contracts, generated outputs, source ownership, README/docs, build/CI validation, external contracts, and migration guidance.
- [ ] 1.6 Add Gherkin acceptance coverage for `056-design-avoid-breaking-changes` under `skills-generator/src/test/resources/gherkin/skills/`.
- [ ] 1.7 Add the matching prompt inventory entry to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.8 Remove `/review-breaking-changes` from `skills-generator/src/main/resources/commands.xml`.
- [ ] 1.9 Remove `skills-generator/src/main/resources/skill-references/assets/commands/review-breaking-changes.md`.
- [ ] 1.10 Remove the `/review-breaking-changes` include from `skills-generator/src/main/resources/skill-references/004-commands-installation.xml`.
- [ ] 1.11 Remove `/review-breaking-changes` from `skills-generator/src/main/resources/skill-references/assets/java-commands-inventory-template.md`.
- [ ] 1.12 Remove command-focused test coverage and fixtures for `/review-breaking-changes`, including `CommandIndexesTest`, command Gherkin files, and command acceptance prompt inventory entries.
- [ ] 1.13 Update `.cursor/commands/` only through the documented command installation/generation path, and verify the retired command is no longer installed.
- [ ] 1.14 Update `README.md`, `README_ES.md`, and `README_ZH.md` to remove `/review-breaking-changes` and add `@056-design-avoid-breaking-changes` to the planning skill list or equivalent discoverability section.
- [ ] 1.15 Validate changed XML files with `xmllint --noout`.
- [ ] 1.16 Run `./mvnw clean install -pl skills-generator` to regenerate local `.agents/skills` output.
- [ ] 1.17 Inspect `.agents/skills/056-design-avoid-breaking-changes/SKILL.md` and relevant generated command installation output.
- [ ] 1.18 Run `rg "review-breaking-changes"` and confirm only historical, changelog, archived OpenSpec, or intentional traceability references remain.
- [ ] 1.19 Execute the listed `056-design-avoid-breaking-changes` acceptance prompt when a local runner is available, or record the skip reason.
- [ ] 1.20 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.21 Run `openspec validate --all` from `documentation/`.

## Source and Derivation

- Source artifact: GitHub issue [#954](https://github.com/jabrena/cursor-rules-java/issues/954).
- Related source artifact: GitHub issue [#886](https://github.com/jabrena/cursor-rules-java/issues/886).
- Derivation direction: issue story and acceptance criteria -> OpenSpec task checklist -> XML skill source implementation, command retirement, README update, and validation.

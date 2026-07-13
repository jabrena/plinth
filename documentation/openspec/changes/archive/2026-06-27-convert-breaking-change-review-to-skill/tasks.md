# Tasks

## 1. Implementation Checklist

- [x] 1.1 Confirm issue #954 and issue #886 as the source context for converting the breaking-change review workflow from command to skill.
- [x] 1.2 Create `plinth-skills-generator/src/main/resources/skill-indexes/056-skill.xml` for `056-design-avoid-breaking-changes`.
- [x] 1.3 Create `plinth-skills-generator/src/main/resources/skill-references/056-design-avoid-breaking-changes.xml` with compatibility review guidance and examples.
- [x] 1.4 Register skill id `056` with explicit `skillId="056-design-avoid-breaking-changes"` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 1.5 Cover breaking-change review scenarios for command contracts, skill contracts, generated outputs, source ownership, README/docs, build/CI validation, external contracts, and migration guidance.
- [x] 1.6 Add Gherkin acceptance coverage for `056-design-avoid-breaking-changes` under `plinth-skills-generator/src/test/resources/gherkin/skills/`.
- [x] 1.7 Add the matching prompt inventory entry to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.8 Remove `/review-breaking-changes` from `plinth-skills-generator/src/main/resources/commands.xml`.
- [x] 1.9 Remove `plinth-skills-generator/src/main/resources/skill-references/assets/commands/review-breaking-changes.md`.
- [x] 1.10 Remove the `/review-breaking-changes` include from `plinth-skills-generator/src/main/resources/skill-references/004-commands-installation.xml`.
- [x] 1.11 Remove `/review-breaking-changes` from `plinth-skills-generator/src/main/resources/skill-references/assets/java-commands-inventory-template.md`.
- [x] 1.12 Remove command-focused test coverage and fixtures for `/review-breaking-changes`, including `CommandIndexesTest`, command Gherkin files, and command acceptance prompt inventory entries.
- [x] 1.13 Update `.cursor/commands/` only through the documented command installation/generation path, and verify the retired command is no longer installed.
  The embedded command source no longer installs the retired command, and checked-in `.cursor/commands/review-breaking-changes.md` was removed to match the source bundle because command installation is an interactive copy workflow rather than a noninteractive generator.
- [x] 1.14 Update `README.md`, `README_ES.md`, and `README_ZH.md` to remove `/review-breaking-changes` and add `@056-design-avoid-breaking-changes` to the planning skill list or equivalent discoverability section.
- [x] 1.15 Validate changed XML files with `xmllint --noout`.
- [x] 1.16 Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local `.agents/skills` output.
- [x] 1.17 Inspect `.agents/skills/056-design-avoid-breaking-changes/SKILL.md` and relevant generated command installation output.
- [x] 1.18 Run `rg "review-breaking-changes"` and confirm only historical, changelog, archived OpenSpec, or intentional traceability references remain.
- [x] 1.19 Execute the listed `056-design-avoid-breaking-changes` acceptance prompt when a local runner is available, or record the skip reason.
  Skipped the manual `execute @plinth-skills-generator/src/test/resources/gherkin/skills/056-design-avoid-breaking-changes.feature` prompt because this shell environment exposes the prompt inventory as documentation but no local `execute @...feature` runner.
- [x] 1.20 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.21 Run `openspec validate --all` from `documentation/`.

## Source and Derivation

- Source artifact: GitHub issue [#954](https://github.com/jabrena/plinth/issues/954).
- Related source artifact: GitHub issue [#886](https://github.com/jabrena/plinth/issues/886).
- Derivation direction: issue story and acceptance criteria -> OpenSpec task checklist -> XML skill source implementation, command retirement, README update, and validation.

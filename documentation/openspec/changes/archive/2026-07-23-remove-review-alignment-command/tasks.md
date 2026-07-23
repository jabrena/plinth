# Tasks

## 1. Implementation Checklist

- [x] 1.1 Confirm issue #1084's Functional Specification and Acceptance Criteria comments as the source context for retiring `/review-alignment`.
- [x] 1.2 Remove `plinth-commands-generator/src/main/resources/commands/review-alignment.xml`.
- [x] 1.3 Remove the `/review-alignment` registration from `plinth-commands-generator/src/main/resources/commands.xml`.
- [x] 1.4 Remove the "Review alignment and readiness" mission (and any output-format/constraint text that exists only to support it) from `plinth-agents-generator/src/main/resources/agents/robot-business-analyst.xml`.
- [x] 1.5 Remove `/review-alignment` from `plinth-skills-generator/src/main/resources/skills.xml` and `plinth-skills-generator/src/main/resources/skill-references/004-commands-installation.xml`.
- [x] 1.6 Remove `/review-alignment` command-focused test coverage and fixtures, including references in `CommandIndexesTest.java`, `CommandFrontmatterTest.java`, `review-alignment.feature`, and `acceptance-tests-prompts-commands.md`.
- [x] 1.7 Remove `/review-alignment` skill-focused acceptance coverage references in `plinth-skills-generator/src/test/resources/gherkin/skills/004-commands-installation.feature` and `001-commands-inventory.feature`.
- [x] 1.8 Update `documentation/guides/GETTING-STARTED-WORKFLOWS.md`, `_ES`, and `_ZH` to remove `/review-alignment` as an available command.
- [x] 1.9 Update `documentation/guides/GETTING-STARTED-AGENTS.md`, `_ES`, and `_ZH` to remove `robot-business-analyst`'s alignment-review command reference.
- [x] 1.10 Update `documentation/guides/INVENTORY-COMMANDS-JAVA.md` to remove `/review-alignment` from the command inventory.
- [x] 1.11 Update `README.md`, `README_ES.md`, and `README_ZH.md` to remove `/review-alignment` from their commands tables (found during `/explore-design` breaking-change review; not in the original Functional Specification scope).
- [x] 1.12 Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` step 3 so it stops and routes a material conflict to `robot-business-analyst` instead of requesting `/review-alignment` (found during `/explore-design` ATDD alignment review: the spec delta alone does not change this real command source).
- [x] 1.13 Update `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` to remove the `/review-alignment` row from the command inventory table (found during `/explore-design` ATDD alignment review).
- [x] 1.14 Update `documentation/openspec/specs/analysis-design-commands/spec.md` per this change's `analysis-design-commands` spec delta.
- [x] 1.15 Update `documentation/openspec/specs/analysis-design-agents/spec.md` per this change's `analysis-design-agents` spec delta.
- [x] 1.16 Update `documentation/openspec/specs/implement-spec-command/spec.md` per this change's `implement-spec-command` spec delta.
- [x] 1.17 Validate changed XML files with `xmllint --noout`.
- [x] 1.18 Run `./mvnw clean install -pl plinth-skills-generator -am` to regenerate local `.agents/skills` output and inspect it for the absence of `/review-alignment`.
- [x] 1.19 Run `rg "review-alignment"` and confirm remaining matches are limited to `documentation/openspec/changes/archive/**`. (Confirmed with one documented, in-scope exception: matches also remain in this change's own not-yet-archived proposal folder, in historical blog posts describing already-shipped releases, in the three living specs — which now correctly *describe the retirement* rather than reference the command as live — and in `skills/` release output, which `proposal.md` explicitly scopes to the separate `-P release` profile. See implementation report for the full location list.)
- [x] 1.20 Run `./mvnw clean verify -pl plinth-commands-generator -pl plinth-agents-generator -pl plinth-skills-generator -am`.
- [x] 1.21 Run `openspec validate --all` from `documentation/`.

## Source and Derivation

- Source artifact: GitHub issue [#1084](https://github.com/jabrena/plinth/issues/1084).
- Functional Specification: [issue #1084 comment](https://github.com/jabrena/plinth/issues/1084#issuecomment-5063825754).
- Acceptance Criteria: [issue #1084 comment](https://github.com/jabrena/plinth/issues/1084#issuecomment-5063838118).
- Derivation direction: issue story and acceptance criteria -> OpenSpec task checklist -> command/agent/skill source removal, spec updates, documentation update, and validation.

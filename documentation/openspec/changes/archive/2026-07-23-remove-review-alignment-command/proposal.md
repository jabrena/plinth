## Why

GitHub issue [#1084](https://github.com/jabrena/plinth/issues/1084) reports that `/review-alignment` is not necessary in the confirmed planning workflow (`/update-issue`, `/explore-problem`, `/create-acceptance-criteria`, `/create-spec`, `/explore-design`) and asks to remove the command and update its owning agent, `robot-business-analyst`.

The Functional Specification and Acceptance Criteria posted on issue #1084 establish that `/review-alignment` and its supporting "Review alignment and readiness" mission are duplicated across three generator modules (`plinth-commands-generator`, `plinth-agents-generator`, `plinth-skills-generator`), dedicated tests, and trilingual documentation, with no automated check tying the confirmed workflow list to what is actually registered. Spec inspection during this change also found the command referenced from two living OpenSpec capability specs (`analysis-design-commands`, `implement-spec-command`) beyond what issue #1084 itself named.

A `/explore-design` breaking-change compatibility review (`056-design-avoid-breaking-changes`) found a further gap: `README.md`, `README_ES.md`, and `README_ZH.md` all list `/review-alignment` in their commands tables, which was missed by both the original Functional Specification and the initial version of this OpenSpec change. A subsequent `059-design-atdd` alignment gate found two more gaps: `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` still instructs stopping and requesting `/review-alignment` on a material conflict (the actual command behavior, not just the OpenSpec description of it), and `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` still lists `/review-alignment` in its inventory table.

## What Changes

- Remove the `/review-alignment` command from `plinth-commands-generator` (command source, `commands.xml` registration, command-focused tests, and command acceptance prompt inventory).
- Remove the "Review alignment and readiness" mission from `robot-business-analyst` in `plinth-agents-generator`, including any output-format or constraint text that exists only to support it.
- Remove `/review-alignment` from the `plinth-skills-generator` skill pipeline (`skills.xml`, `skill-references/004-commands-installation.xml`, and generated command installation assets).
- Update `GETTING-STARTED-WORKFLOWS.md`/`_ES`/`_ZH`, `GETTING-STARTED-AGENTS.md`/`_ES`/`_ZH`, and `INVENTORY-COMMANDS-JAVA.md` so none of them describe `/review-alignment` as an available command.
- Update `README.md`, `README_ES.md`, and `README_ZH.md` to remove `/review-alignment` from their commands tables, keeping all three in sync.
- Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` so its conflict-handling step routes to `robot-business-analyst` instead of requesting `/review-alignment`.
- Update `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` to remove the `/review-alignment` inventory row.
- Update the `analysis-design-commands` OpenSpec capability spec: drop `/review-alignment` from the command bundle requirement and remove the "Read-only alignment command routing" requirement.
- Update the `analysis-design-agents` OpenSpec capability spec: `robot-business-analyst`'s described ownership no longer includes read-only alignment review.
- Update the `implement-spec-command` OpenSpec capability spec: the "Stop on artifact conflict" scenario no longer routes conflicts to `/review-alignment`.
- Preserve `documentation/openspec/changes/archive/**` unchanged as historical record.

## Capabilities

### Modified Capabilities

- `analysis-design-commands`: Removes `/review-alignment` from the command bundle and removes the read-only alignment command routing requirement.
- `analysis-design-agents`: Removes read-only alignment review from `robot-business-analyst`'s described mission ownership.
- `implement-spec-command`: Changes the artifact-conflict scenario's routing target now that `/review-alignment` no longer exists.

## Source and Derivation

- Source artifact: GitHub issue [#1084](https://github.com/jabrena/plinth/issues/1084).
- Functional Specification: [issue #1084 comment](https://github.com/jabrena/plinth/issues/1084#issuecomment-5063825754).
- Acceptance Criteria: [issue #1084 comment](https://github.com/jabrena/plinth/issues/1084#issuecomment-5063838118).
- Local command workflow: `.cursor/commands/create-spec.md`.
- Local OpenSpec guidance: `042-planning-openspec`.
- Derivation direction: issue #1084 -> Functional Specification -> Acceptance Criteria -> OpenSpec change artifacts (this change) -> generator source removal -> documentation update -> generated local validation.
- Process note: this change was created directly from the Functional Specification and Acceptance Criteria comments already confirmed on issue #1084, rather than from a separately prepared sanitized artifact, at explicit maintainer direction.

## Change Boundary Assessment

This is one OpenSpec change because the user-visible outcome is one workflow retirement: `/review-alignment` and its supporting agent mission are removed together, and every living artifact that referenced them is updated in the same release boundary. Splitting command removal from agent-mission removal, spec updates, or documentation updates would leave the repository in an inconsistent intermediate state where sources reference a command or mission that no longer fully exists.

The smallest useful slice is to remove the command and mission from every owning source, update the three affected OpenSpec capability specs, refresh documentation, and validate the generators. No follow-up slice is required for this issue's scope.

## Impact

Affected areas: `plinth-commands-generator/src/main/resources/commands.xml`, `plinth-commands-generator/src/main/resources/commands/review-alignment.xml`, `plinth-commands-generator/src/main/resources/commands/implement-spec.xml`, `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md`, `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java`, `plinth-commands-generator/src/test/java/info/jab/pml/CommandFrontmatterTest.java`, `plinth-commands-generator/src/test/resources/gherkin/commands/review-alignment.feature`, `plinth-commands-generator/src/test/resources/gherkin/commands/acceptance-tests-prompts-commands.md`, `plinth-agents-generator/src/main/resources/agents/robot-business-analyst.xml`, `plinth-skills-generator/src/main/resources/skills.xml`, `plinth-skills-generator/src/main/resources/skill-references/004-commands-installation.xml`, `plinth-skills-generator/src/test/resources/gherkin/skills/004-commands-installation.feature`, `plinth-skills-generator/src/test/resources/gherkin/skills/001-commands-inventory.feature`, `documentation/guides/GETTING-STARTED-WORKFLOWS*.md`, `documentation/guides/GETTING-STARTED-AGENTS*.md`, `documentation/guides/INVENTORY-COMMANDS-JAVA.md`, `README.md`, `README_ES.md`, `README_ZH.md`, and the three OpenSpec capability specs named above.

Generated `.agents/skills` output should be refreshed through `./mvnw clean install -pl plinth-skills-generator -am` for local verification. Public `skills/` release output should only be refreshed through the release profile when release output is intentionally in scope.

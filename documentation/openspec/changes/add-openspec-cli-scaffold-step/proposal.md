## Why

GitHub issue [#1054](https://github.com/jabrena/plinth/issues/1054) reports that `042-planning-openspec` creates a new OpenSpec change by manually authoring `proposal.md`, `design.md`, spec deltas, and `tasks.md` from scratch. The OpenSpec CLI already provides `openspec new change <name>`, which scaffolds the change directory and its `.openspec.yaml` metadata file. The skill's workflow does not currently instruct scaffolding new changes through that command, so scaffolding is inconsistent and metadata can be missing or hand-authored incorrectly.

## What Changes

- Update `042-planning-openspec` Step 3 ("Verify OpenSpec Installation and Project") so that, when creating a **new** change, the skill runs `openspec new change <change-id>` to scaffold the change directory (producing `.openspec.yaml` and a placeholder `README.md`) before authoring any artifact content.
- Update Step 4 ("Create or Update Approved Change Artifacts") so it replaces the CLI-generated placeholder `README.md` with `proposal.md` once authored, matching the repository's existing archived-change convention of not keeping a placeholder `README.md` alongside `proposal.md`.
- Clarify that scaffolding is skipped when updating an **existing** change: the skill must use `openspec list` / `openspec show <change-id>` (already part of Step 3) to detect an existing change directory and must not re-run `openspec new change` against it.
- Add the corresponding step constraints to `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` and the workflow summary in `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`.
- Add a Gherkin acceptance scenario to `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature` covering CLI scaffolding for a new change and skipped re-scaffolding for an existing change.
- Update the `042-planning-openspec` row in `documentation/guides/INVENTORY-SKILLS-JAVA.md` to note that new changes are scaffolded via `openspec new change <change-id>`.
- Regenerate local skill output under `.agents/skills/042-planning-openspec/` for validation without refreshing public `skills/` release output.

## Capabilities

### Modified Capabilities

- `composable-planning-artifacts`: The "Independent OpenSpec creation" requirement gains two scenarios: scaffolding a new change through `openspec new change <change-id>` before authoring artifacts, and skipping that scaffolding step when the change already exists.

## Source and Derivation

| Source | Concern | Derivation |
| --- | --- | --- |
| GitHub issue [#1054](https://github.com/jabrena/plinth/issues/1054), maintainer-authored via `/update-issue` and used as-is | Requirement to scaffold new OpenSpec changes with the CLI instead of manual file authoring | Issue -> OpenSpec change artifacts (one-way) |
| `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` (Step 3, Step 4) | Current workflow text authoring artifacts manually | Repository source -> design and tasks |
| `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml` (Step 3, Step 4) | Current condensed workflow summary | Repository source -> design and tasks |
| Local `openspec new change <name> [--description]` CLI behavior, verified interactively (creates `.openspec.yaml` and `README.md`, no other artifacts) | Ground truth for what the CLI scaffolds | CLI behavior -> design and specification scenarios |
| `documentation/openspec/changes/archive/2026-07-21-remove-acceptance-criteria-from-user-story-skill/` | Established structure for a single-skill workflow-modification change | Repository pattern -> artifact organization only |

Derivation is one-way into this OpenSpec change. This change does not update issue #1054 or silently synchronize any source artifact.

## Change Boundary Assessment

This is one OpenSpec change: scaffolding via the CLI and removing the placeholder `README.md` are two ends of the same Step 3/Step 4 workflow adjustment for creating a new change, and must move together to keep the skill's own workflow internally consistent.

## Impact

- Updates XML sources under `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` and `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`.
- Adds a scenario to `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature`.
- Updates `documentation/guides/INVENTORY-SKILLS-JAVA.md`.
- Regenerates local `.agents/skills/042-planning-openspec/` output through Maven; does not require direct edits to `.agents/skills/`, `.cursor/rules/`, `skills/`, or `docs/`.
- Compatibility: additive and non-breaking; the skill identifier and its existing update-flow behavior for already-scaffolded changes remain unchanged.

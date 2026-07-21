## Context

`042-planning-openspec` Step 3 ("Verify OpenSpec Installation and Project") currently runs `openspec --version`, optionally `openspec init`, and inspects existing changes with `openspec list` / `openspec status --change <change-id>` / `openspec show <change-id>`. Step 4 ("Create or Update Approved Change Artifacts") then says to "Create or update `proposal.md`", "Create or update `design.md`", etc., with no mention of the `openspec new change <name>` CLI command.

Verified interactively, `openspec new change <name> [--description <text>]` creates `openspec/changes/<name>/` containing only `.openspec.yaml` (schema and creation date) and a placeholder `README.md` (title and optional description). It does not create `proposal.md`, `design.md`, `specs/`, or `tasks.md` — those remain hand-authored in Step 4. Every archived change in this repository (e.g. `2026-07-21-remove-acceptance-criteria-from-user-story-skill`, `2026-07-21-add-atdd-design-skill`) has a `proposal.md` and no `README.md`, confirming the repository convention is to replace the CLI placeholder rather than keep both files.

## Goals / Non-Goals

**Goals:**

- For a **new** change, have the skill run `openspec new change <change-id>` during Step 3/4 before authoring `proposal.md`, `design.md`, spec deltas, and `tasks.md`, so `.openspec.yaml` is always CLI-generated rather than hand-authored.
- Replace the CLI-generated placeholder `README.md` with `proposal.md` once authored, matching the existing repository convention.
- For an **existing** change (already detected via `openspec show <change-id>` in Step 3), skip `openspec new change` entirely and edit the existing artifacts in place.

**Non-Goals:**

- Changing how multi-change maps are approved (Step 2) or how conflicts are handled (Step 5).
- Changing the `openspec archive` naming convention (Step 6), which already uses a `YYYY-MM-DD-<change-id>` prefix.
- Adding CLI scaffolding for `specs/<capability>/spec.md` deltas or `tasks.md`; the CLI does not generate those today, so Step 4 continues to author them directly.
- Introducing `openspec update` or other CLI subcommands not requested by issue #1054.

## Decisions

### Scaffold only new changes, in Step 3/4

Extend Step 3's existing new-vs-update detection (`openspec list`, `openspec show <change-id>`) with an explicit branch:

- If `<change-id>` does not already exist under `openspec/changes/`, run `openspec new change <change-id>` (optionally with `--description`) before authoring any artifact, then author `proposal.md`, `design.md`, spec deltas, and `tasks.md` into the scaffolded directory, and remove the CLI-generated placeholder `README.md` once `proposal.md` exists.
- If `<change-id>` already exists, do not run `openspec new change` again; edit the existing artifacts directly, exactly as today.

This keeps the CLI as the single source of the `.openspec.yaml` metadata file and the change directory's initial structure, while leaving proposal/design/spec/tasks authoring — which the CLI does not perform — as the skill's responsibility.

### Placeholder `README.md` handling

`openspec new change` writes a minimal `README.md` (`# <name>` plus the optional `--description` text). Since every archived change in this repository carries `proposal.md` instead, Step 4 must remove that placeholder `README.md` once `proposal.md` is authored, so the final change directory matches the repository's established shape (`.openspec.yaml`, `proposal.md`, `design.md`, `specs/`, `tasks.md`).

### Specification scope

Add the two new scenarios (scaffold path, skip-re-scaffold path) to the existing "Independent OpenSpec creation" requirement in `composable-planning-artifacts`, rather than introducing a new requirement, because both scenarios describe the same requirement ("create ... OpenSpec ... artifacts ... from an issue ... without requiring a plan") at the mechanism level of *how* a new change directory comes into existence.

### Out of Scope

- No change to `openspec init` behavior or guidance (already covered by Step 3).
- No change to Step 5 (conflict handling) or Step 6 (`validate` / `archive`) content.
- No change to the `god-analysis-api` example project fixture beyond what the new Gherkin scenario needs to assert (it asserts the presence of `.openspec.yaml` after scaffolding, and resets any git changes afterward, consistent with the existing scenario's cleanup step).

## Validation Strategy

- Validate `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` and `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml` with `xmllint --noout` after editing.
- Run `./mvnw clean install -pl plinth-skills-generator -am` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/042-planning-openspec/SKILL.md` and `.agents/skills/042-planning-openspec/references/042-planning-openspec.md` for the new scaffolding guidance and no unresolved include markers.
- Execute the `042-planning-openspec` acceptance prompt listed in `acceptance-tests-prompts-skills.md` (including the new scenario) against the generated skill output, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` because the Java skill inventory changes.
- Run `openspec validate --all` from `documentation/` for the planning artifacts.

## Risks / Trade-offs

- `openspec new change` accepts only a `--description` option beyond the name; if a maintainer wants richer initial `README.md` content, none is available, but that content is discarded in favor of `proposal.md` anyway.
- Adding an explicit "remove placeholder `README.md`" instruction slightly lengthens Step 4, but avoids leaving a stale, unlinked file in the change directory.

## Open Questions

None. The CLI's scaffolding behavior was verified directly; no further clarification from the maintainer is required.

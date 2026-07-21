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

## Assumptions and Success Criteria

**Assumptions:**

- The OpenSpec CLI subcommand `openspec new change <name> [--description <text>]` remains stable in the CLI version pinned by this repository (verified interactively against `openspec` 1.2.0: creates `.openspec.yaml` and a placeholder `README.md`, nothing else).
- Step 3's existing new-vs-update detection (`openspec list`, `openspec show <change-id>`) is sufficient to decide whether `<change-id>` already exists; no new detection mechanism is needed.
- Maintainers reviewing a change created by this workflow expect the same final directory shape used by every archived change today (`.openspec.yaml`, `proposal.md`, `design.md`, `specs/`, `tasks.md`, no `README.md`).

**Unknowns:**

- Whether a future OpenSpec CLI release changes what `openspec new change` scaffolds (e.g., adds more placeholder files). Out of scope to guard against speculatively; Step 4 already removes the placeholder `README.md` explicitly rather than assuming a fixed file list.

**Success criteria:**

- Applying `042-planning-openspec` to a brand-new change ID always produces a CLI-generated `.openspec.yaml` (never hand-authored) and never leaves a stray placeholder `README.md` once `proposal.md` exists.
- Applying `042-planning-openspec` to an already-scaffolded change never re-invokes `openspec new change` and never overwrites `.openspec.yaml`.
- No existing archived-change or update-flow behavior regresses (validated by re-running `openspec validate --all`).

## Alternatives Considered

Evaluated in Kent Beck simple-design-rules order (passes the tests / correctness boundary first, then reveals intention, then duplication, then fewest elements), since the real choice here is "how does Step 3/4 decide what scaffolds a change" rather than a Java implementation tradeoff:

1. **Always run `openspec new change <id>`, even for existing changes** — Rejected: the CLI errors on an already-existing directory, which breaks the correctness boundary (the update flow, exercised today, would fail).
2. **Never use the CLI; keep manual authoring of all four files, including hand-written `.openspec.yaml`** — Rejected: this is the status quo issue #1054 reports as inconsistent; it reveals no clearer intention than the CLI-scaffolded path and duplicates knowledge (the `.openspec.yaml` shape) that the CLI already owns.
3. **Run `openspec new change <id>` only when Step 3 confirms the change does not yet exist, then author `proposal.md`/`design.md`/specs/`tasks.md` and remove the placeholder `README.md`** — **Chosen**: passes the correctness boundary (never re-scaffolds an existing change), reveals intention (one branch per new-vs-existing case, matching Step 3's existing detection), introduces no duplication (the CLI remains the single source of `.openspec.yaml`), and adds the fewest elements (one conditional CLI invocation plus one file-removal instruction, no new detection mechanism).

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

## Two-Step Sequencing

Applying Kent Beck's two-step method to the Step 3/4 text change itself (this is a prompt/workflow-instruction edit, not application code, so "behavior" below means the guidance a downstream agent following the skill will execute):

### Step 1: Make the change easy (behavior-preserving)

Reorganize Step 3's existing new-vs-update detection text so the "detect whether `<change-id>` exists" logic and the "what to do next" action are clearly separated into distinct sentences/bullets, without adding the CLI-scaffold instruction yet. This makes Step 2 a small, additive insertion rather than a rewrite tangled with restructuring.

Validation after Step 1: re-generate local skills (`./mvnw clean install -pl plinth-skills-generator -am`) and confirm the generated `SKILL.md`/reference Markdown still describe exactly today's behavior (manual authoring for both new and existing changes) — i.e., no observable guidance change yet.

### Step 2: Make the intended behavior change

Insert the new-change branch's `openspec new change <change-id>` instruction and the Step 4 placeholder-`README.md` removal instruction. Leave the existing-change branch's manual-authoring instruction untouched.

Validation after Step 2: re-generate local skills again, inspect the generated Markdown for the new instructions, execute the two new Gherkin scenarios (`plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature`), and run `./mvnw clean verify -pl plinth-skills-generator`.

## Compatibility Review

**Sources reviewed:** `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml`, `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`, `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature`, `documentation/guides/INVENTORY-SKILLS-JAVA.md` (042 row), this change's `proposal.md`/`tasks.md`.

**Surfaces checked:** skill routing (id, description, triggers), generated-output ownership, source/schema validation, acceptance coverage, documentation.

**Findings:**

- `NON-BREAKING` — Skill identifier `042-planning-openspec`, its description, and its six trigger phrases are unchanged; only Step 3/Step 4 body text and constraints gain new instructions. Existing routing to this skill is unaffected.
- `NON-BREAKING` — The existing-change (update) code path keeps its current manual-authoring behavior; a maintainer mid-update-flow today sees no behavior change.
- `NON-BREAKING` — Generated-output ownership is preserved: only XML sources under `plinth-skills-generator/src/main/resources/` are edited; `.agents/skills/042-planning-openspec/` is refreshed only through `./mvnw clean install -pl plinth-skills-generator -am`, and public `skills/` is left untouched unless the `-P release` profile is explicitly run later.
- `POTENTIALLY BREAKING` — A maintainer who has memorized or scripted the current manual four-file authoring sequence for *new* changes will see an added CLI step. Mitigated because the CLI step produces a strict subset of what was previously hand-authored (`.openspec.yaml` plus a placeholder `README.md` that Step 4 removes); no maintainer-facing file format changes. Maintainer confirmed (2026-07-21) that maintainers will adopt the new CLI-scaffolding approach going forward, so this finding requires no additional compatibility shim or opt-out.
- `UNKNOWN` — Behavior if a future OpenSpec CLI release changes what `openspec new change` scaffolds beyond `.openspec.yaml`/`README.md`. No evidence available today; flagged as an assumption above rather than guarded against speculatively.

**Migration:** none required; this is additive guidance for an interactive skill, not a released API/schema/config contract. No deprecation window or release note is needed beyond the normal changelog entry for the skill update.

**Result:** No confirmed `BREAKING` findings. Safe to implement `tasks.md` as planned.

## Approval

Design direction approved by the maintainer (2026-07-21): maintainers will use the new CLI-scaffolding approach for creating OpenSpec changes going forward. No further design revision requested; proceed to implement `tasks.md`.

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

## ADR Candidates

None. This change adjusts workflow instructions inside one existing skill's prompt text — it does not introduce a new architectural component, cross-cutting policy, or decision that other skills, commands, or modules need to discover independently. The "UNKNOWN" compatibility finding above (future CLI scaffolding drift) is tracked as an assumption in this design, not an ADR-worthy decision.

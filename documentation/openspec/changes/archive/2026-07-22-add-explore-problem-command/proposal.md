## Why

GitHub issue [#1043](https://github.com/jabrena/plinth/issues/1043) requests a new `/explore-problem <issue-url>` command positioned in the Functional Specification Phase of the OpenSpec pipeline, between `/update-issue` (which produces a User Story via `014-agile-user-story`) and the future `/create-gherkin` command (which will produce `Acceptance.feature`). Today, nothing in Plinth evaluates and enriches an issue from problem-framing, root-cause, assumption, context, and quality-attribute points of view before acceptance criteria and technical specs are drafted, so contributors risk designing a solution for a symptom-level problem or missing non-functional needs.

`/explore-problem` closes that gap: given an issue URL, it evaluates the issue through five points of view, asks clarifying questions whenever content is vague or ambiguous for a given point of view, and posts the resulting Functional Specification as a new comment on the source issue, covering all five categories.

## Scope Discovery

The issue names five existing planning techniques the command should draw on: `021-problem-framing`, `022-root-cause-analysis`, `023-assumption-analysis`, `024-context-mapping`, `025-quality-attribute-discovery`. A repository search confirms **none of the five exist yet** as skill sources under `plinth-skills-generator/src/main/resources/skill-references/` or `skill-indexes/`, and none are built into `skills/` or `.agents/skills/`. The current skill catalog has no `02x` band at all — it jumps directly from `014-agile-user-story` to `030-architecture-adr-general`. This is a wider gap than a prior same-day search suggested (that search flagged only `025` as missing); this change treats all five as net-new.

This change introduces the five technique skills as `020`-band skill references (new numbering band, matching the exact ids the issue specifies) alongside the `/explore-problem` command that orchestrates them, rather than deferring the skill content or inventing it silently. See `design.md` for the change-boundary rationale (why this stays one OpenSpec change) and the technique-skill-vs-orchestration split.

## What Changes

- Add a new command `/explore-problem <issue-url>` that:
  - Supports GitHub, Jira, and Azure DevOps issue URLs from v1 (maintainer-confirmed), reusing the existing `043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` skills already used by `/update-issue` — no new tracker skills are added by this change.
  - Reads the target issue (including any User Story already produced by `/update-issue`).
  - Evaluates the issue through five points of view: problem framing, root cause analysis, assumption analysis, context mapping, and quality attribute discovery.
  - Asks clarifying questions whenever content is vague or ambiguous for a given point of view, before writing that point of view's section, rather than inventing the missing detail.
  - Drafts a Functional Specification with five required sections mapped to the five points of view, shows the draft to the user, and, once confirmed, posts it as a new comment on the source issue (the pipeline's `Functional Specification.md` artifact is delivered as an issue comment, not a repository file).
- Add five new `020`-band skill references that document each technique, registered in the generator skill inventory, following the existing skill-reference authoring pattern (e.g. `055-design-parallel-change`):
  - `021-problem-framing` — Problem statement, Current state, Desired state, Stakeholders, Success criteria.
  - `022-root-cause-analysis` — Five Whys, Fishbone, Current Reality Tree, and constraint identification.
  - `023-assumption-analysis` — Explicit assumptions, Unknowns, Validation plan.
  - `024-context-mapping` — Existing systems, Integrations, Ownership, External dependencies.
  - `025-quality-attribute-discovery` — Identify and prioritize the quality attributes the future solution must satisfy before architecture and design begin.
- Synchronize command inventory, command installation bundle, skill inventory, local skill generation, and focused tests for the new command and the five new skills.

## Capabilities

### New Capabilities

- `explore-problem-command`: Defines the `/explore-problem` contract, inputs, five-lens evaluation behavior, clarifying-question behavior, and `Functional Specification.md` output contract.
- `problem-exploration-technique-skill-references`: Adds the five new `020`-band skill references (`021`–`025`) that `/explore-problem` draws on, each documenting one technique's method and required output content.

### Modified Capabilities

None.

## Impact

This change adds a new command to the embedded command bundle and five new skill references to the embedded skill bundle. It touches command inventory sources, command installation assets, skill inventory sources (`skills.xml`, `skill-indexes/`, `skill-references/`), local generated skill output (`.agents/skills/`), and focused tests in both the commands generator and skills generator modules. It does not require refreshing the public `skills/` release output unless a release is intentionally run later. It also references, but does not modify, the existing `043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` skills for multi-tracker support. `/create-gherkin`, referenced in the issue only for pipeline context, is explicitly out of scope for this change.

## Source and Derivation

- Source artifact: GitHub issue [#1043](https://github.com/jabrena/plinth/issues/1043) (maintainer-authored via this session's own `/update-issue` run; used as a sanitized summary).
- Maintainer clarifications gathered directly during this same planning session (not yet reflected in the issue body): the Functional Specification is posted as a new issue comment rather than written to a repository file, and `/explore-problem` supports GitHub, Jira, and Azure DevOps from v1 rather than GitHub-only.
- Structural precedent: `documentation/openspec/changes/archive/2026-07-13-add-close-spec-command/` (command scaffolding shape) and `documentation/openspec/changes/archive/2026-06-14-add-eu-regulation-skills/` (bundling multiple new same-issue, same-release skills into one OpenSpec change).
- Interactive-command pattern precedent: `plinth-commands-generator/src/main/resources/commands/update-issue.xml` and `skills/014-agile-user-story/`.
- Existing skill catalog: `plinth-skills-generator/src/main/resources/skills.xml`, `skill-indexes/`, `skill-references/` (verified no `02x` band and no existing problem-framing/root-cause/assumption/context/quality-attribute skill content exists anywhere in the repository).
- Derivation direction: issue #1043 plus repository skill-catalog and command-catalog evidence -> OpenSpec change artifacts -> XML command and skill source implementation (future step, out of scope here) -> local generated output validation.

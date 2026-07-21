## Context

The OpenSpec pipeline described in issue #1043 has two phases:

```
# Functional Specification Phase
/update-issue (014-agile-user-story, without Gherkin) -> User Story
/explore-problem -> Functional Specification.md
/create-gherkin -> Acceptance.feature

# Technical Specification Phase
/create-spec -> proposal.md, spec.md, design.md, tasks.md
/explore-design
```

`/update-issue` and `/create-gherkin` sit either side of `/explore-problem`. `/update-issue` already exists and is owned by `@robot-business-analyst`, using `014-agile-user-story` for user-story structure. `/create-gherkin` does not exist yet and is explicitly out of scope for this change; it is referenced only so `/explore-problem`'s output contract (`Functional Specification.md`) is shaped for a plausible future consumer, without this change depending on or blocking on `/create-gherkin`.

The issue names five techniques the command should draw on — `021-problem-framing`, `022-root-cause-analysis`, `023-assumption-analysis`, `024-context-mapping`, `025-quality-attribute-discovery` — as if they were existing skills to reference. Repository evidence (see `proposal.md` Scope Discovery) shows none exist. The skill catalog has no `02x` band; it goes `014-agile-user-story` -> `030-architecture-adr-general`. This design treats the five ids the issue specifies as the target numbering for five **new** skill references, not a reuse of existing content.

## Goals / Non-Goals

**Goals:**

- Provide a `/explore-problem <issue-url>` command that evaluates an issue through five points of view and posts a Functional Specification, covering all five, as a new comment on the source issue.
- Ask clarifying questions per point of view when issue content is vague or ambiguous, before writing that section, instead of inventing detail.
- Add the five named technique skills (`021`–`025`) as reusable `020`-band skill references, since none exist today and the issue's acceptance criteria require all five categories.
- Keep command inventory, skill inventory, installation bundles, and tests aligned with both the command and the five new skill references.

**Non-Goals:**

- Implement `/create-gherkin` or any Acceptance.feature generation. Referenced only for pipeline position.
- Change `/update-issue` or `014-agile-user-story` behavior.
- Record architecture decisions (ADRs) for the quality attributes discovered; `025-quality-attribute-discovery` produces a prioritized discovery list consumed later by `030`–`032` ADR skills and `/explore-design`, not the ADRs themselves.
- Auto-detect or guess the issue tracker; the command accepts an issue URL as the issue's existing identity, consistent with `/update-issue`'s existing-issue-URL handling.

## Change Boundary Assessment

This is **one** OpenSpec change, adding two new capabilities (`explore-problem-command` and `problem-exploration-technique-skill-references`), not a multi-change map requiring approval under `042-planning-openspec` Step 2.

Rationale, evaluated against the same criteria `042-planning-openspec` uses to decide when multiple changes are warranted (business value, release timing, ownership, dependency order, risk/approval, rollback boundary, deployment boundary):

- **Same source issue, same release timing**: all five skills and the command trace to the single GitHub issue #1043; none has an independent milestone or release trigger. This mirrors `documentation/openspec/changes/archive/2026-06-14-add-eu-regulation-skills/`, which bundled two new skills (`802-regulations-dora`, `803-regulations-gdpr`) into one change specifically because they shared one issue and one milestone, with no independent deployment or runtime code path forcing a split.
- **No independent consumer today**: unlike the `800`-band regulation skills (each independently useful as a standalone compliance-review skill) or the `05x` design skills (each added in its own change because each traced to its **own distinct** GitHub issue — see `2026-06-26-add-design-parallel-change-skill` et al.), none of `021`–`025` has any consumer in this repository other than `/explore-problem`. Splitting them into five separate skill changes plus one command change would fragment a single reviewable outcome ("running `/explore-problem` produces a five-section `Functional Specification.md`") across six PRs for one issue, which is the one-change-per-layer/file decomposition `042-planning-openspec` Step 2 explicitly prohibits.
- **Same ownership and dependency order**: the five skills and the command are authored, reviewed, and released together; the command's contract (five clarifying-question lenses, five output sections) *is* the five skills' shared reason for existing. There is no rollback or deployment boundary that separates "skill exists" from "command works" in a way a maintainer would ever want to ship independently.

Alternative considered and rejected — **six separate changes** (one per skill `021`–`025`, one for the command), modeled on the `05x` design-skill precedent: rejected because that precedent's justification (each skill traces to its own distinct issue with independent scope) does not hold here; all five trace to #1043.

Alternative considered and rejected — **embed the five techniques' guidance directly inline in the command's XML** instead of separate skill references: rejected because it breaks the repository's established command/skill separation (commands are thin orchestrators that reference skills — see `update-issue.xml` referencing `014-agile-user-story`, `043`–`045`; `/explore-design` referencing `051`–`057`), and each technique (Five Whys, Fishbone, Current Reality Tree, etc.) has enough independent methodological depth to warrant its own skill-reference file, matching the depth precedent set by `051`–`057`.

## Decisions

### Owning Agent: `@robot-business-analyst`

`/explore-problem` is owned by `@robot-business-analyst`, matching `/update-issue`'s ownership and the pipeline's "Functional Specification Phase" position (before any architecture or design work begins). Rationale:

- All five lenses are "understand the problem" activities operating directly on issue content — squarely `@robot-business-analyst`'s "issue quality, requirements consistency, traceability" mission — not "design a solution" activities.
- `025-quality-attribute-discovery`'s own acceptance-criteria wording is explicit that it runs "before architecture and design begin," i.e. it discovers and prioritizes candidate quality attributes as input, and deliberately stops short of the architectural decision-making (`@robot-architect`'s `030`–`032` ADR skills and `/explore-design`) that later decides how to satisfy them.
- `/explore-problem` consumes `/update-issue`'s User Story output and feeds the future `/create-gherkin`, both `@robot-business-analyst`-territory commands; keeping ownership consistent avoids a hand-off gap in the Functional Specification Phase.

This OpenSpec change is still authored via `/create-spec` by `@robot-architect` regardless of the resulting command's owning agent, consistent with how `@robot-architect` authored the `add-close-spec-command` OpenSpec change for a command later marked with a different-if-any owning agent.

### Five new `020`-band skill references, orchestrated (not owned) by the command

Add `021-problem-framing`, `022-root-cause-analysis`, `023-assumption-analysis`, `024-context-mapping`, `025-quality-attribute-discovery` as skill references under the existing `skill-references/` + `skill-indexes/` + `skills.xml` pattern (see `055-design-parallel-change` as the structural template). Each skill documents its technique's method and the structured output content it contributes to `Functional Specification.md`; it does not itself run an independent interactive Q&A loop.

The **clarifying-question behavior belongs to the command**, not to each skill individually — mirroring how `/explore-design` orchestrates `051-design-two-steps-methods` through `057-design-feature-toggles` (none of which are independently interactive) rather than each design skill separately interrogating the user. `/explore-problem`'s own steps apply each of the five lenses in turn, evaluate the available issue/User Story content against that lens's required output fields, and ask the user targeted clarifying questions only when a lens's content is vague or ambiguous, before writing that lens's `Functional Specification.md` section.

This positions the `020` band as a new "Functional Specification Phase" technique family, filling the existing numbering gap between the `010` agile band (`012`–`014`) and the `030` architecture-ADR band — using the exact ids the issue specifies.

### Functional Specification output contract: posted as an issue comment

The command drafts a single Functional Specification with five required sections, one per point of view, using the field lists the issue's acceptance criteria specify verbatim:

1. **Problem Framing** — Problem statement, Current state, Desired state, Stakeholders, Success criteria.
2. **Root Cause Analysis** — Findings using Five Whys, Fishbone, Current Reality Tree, and constraint identification.
3. **Assumption Analysis** — Explicit assumptions, Unknowns, Validation plan.
4. **Context Mapping** — Existing systems, Integrations, Ownership, External dependencies.
5. **Quality Attribute Discovery** — Identified and prioritized quality attributes the future solution must satisfy.

Delivery mechanism (maintainer-confirmed): the pipeline's `Functional Specification.md` artifact is **not** a repository file. `/explore-problem` drafts this content and posts it as a new comment on the source issue at `<issue-url>`, the same tracker location `/update-issue` already writes to (as the issue body) and `/explore-problem` already reads from (as input context). This resolves the file-location open question this design previously left open: there is no repository file path to fix, because the artifact lives in the issue thread, not the repository.

### Confirm before posting the comment

Mirroring `/update-issue`'s existing safeguard ("Do not overwrite an issue body without showing the proposed replacement"), `/explore-problem` MUST show the complete drafted Functional Specification to the user before posting, and MUST NOT post the comment without explicit user confirmation. This is a repository-wide write-safety convention (see root `CLAUDE.md`, "Executing actions with care": actions visible to others require confirmation before proceeding), not a requirement stated in issue #1043 itself — recorded here as a design decision grounded in that convention and in `/update-issue`'s established behavior, and reflected as an explicit spec requirement so it isn't lost at implementation time.

### Clarifying-question trigger and scope

For each of the five lenses, before writing that lens's section, the command evaluates whether the available issue/User Story content is sufficient to populate that lens's required fields. When content is vague or ambiguous for a specific field, the command asks a targeted clarifying question about that field rather than inventing an answer or silently omitting the field. This mirrors `014-agile-user-story`'s existing constraint pattern ("Wait for user response after each question or block before proceeding"; "Treat answers as structured story data only").

### Required argument and failure modes

`/explore-problem` requires exactly one argument, `<issue-url>`. Missing argument produces usage guidance, matching `/close-spec`'s and `/update-issue`'s existing single-required-argument pattern. Unresolvable issue URLs (tracker unavailable, issue not found) fail gracefully with an actionable message rather than fabricating problem-framing content.

### Multi-tracker support (GitHub, Jira, Azure DevOps)

`/explore-problem` supports GitHub, Jira, and Azure DevOps issue URLs from v1, matching `/update-issue`'s tracker scope (maintainer-confirmed; see Resolved Questions). It reuses the same tracker-specific skills `/update-issue` already depends on:

- `043-planning-github-issues` for GitHub issue URLs — reading the issue and posting the Functional Specification as a GitHub issue comment.
- `044-planning-jira` for Jira issue URLs — reading the issue and posting the Functional Specification as a Jira comment.
- `045-planning-azure-devops` for Azure DevOps work-item URLs — reading the work item and posting the Functional Specification as an Azure DevOps comment.

Tracker selection is derived from `<issue-url>`'s own shape (each tracker's URL format is distinct — GitHub, Atlassian/Jira, and Azure DevOps domains and path structures do not overlap), consistent with the existing Non-Goal that the command does not need to guess an ambiguous tracker: the URL already carries that identity. This mirrors `/update-issue`'s existing-issue-URL handling and its `043`–`045` skill set exactly, so `/explore-problem` has tracker parity with `/update-issue` from its first version rather than adding tracker support as a later follow-up change.

## Compatibility Review

**Sources reviewed:** `plinth-commands-generator/src/main/resources/commands.xml`, `commands/update-issue.xml`, `commands/close-spec.xml`, `plinth-skills-generator/src/main/resources/skills.xml`, `skill-indexes/055-skill.xml`, `skill-references/055-design-parallel-change.xml`.

**Findings:**

- `NON-BREAKING` — This change only adds a new command id and five new skill ids; it does not modify any existing command or skill contract, id, description, or trigger.
- `NON-BREAKING` — No existing generated output (`.cursor/rules/`, public `skills/`) is edited directly; local skill/command regeneration follows the existing Maven-driven generation path.
- `N/A` — No runtime/API/schema surface changes; this is additive documentation-and-prompt tooling, consistent with prior skill-addition changes.

**Result:** No `BREAKING` findings. Safe to proceed to `tasks.md`.

## Validation Strategy

- Validate all edited/added XML sources with `xmllint --noout` (implementation-time, per `AGENTS.md`/`CLAUDE.md`).
- Run `./mvnw clean install -pl plinth-skills-generator -am` to regenerate local skills into `.agents/skills` for `021`–`025` without refreshing public `skills/`.
- Run `./mvnw clean verify -pl plinth-commands-generator` and `./mvnw clean verify -pl plinth-skills-generator -am` for the new command and skill sources.
- Inspect generated `.agents/skills/02{1..5}-*/SKILL.md` output and the generated `/explore-problem` command markdown for the required five-section contract, the pre-post confirmation step, and clarifying-question guidance.
- Add/execute Gherkin acceptance coverage for the new command (`plinth-commands-generator/src/test/resources/gherkin/commands/explore-problem.feature`) and for each new skill (`plinth-skills-generator/src/test/resources/gherkin/skills/02{1..5}-*.feature`), and register the new skill prompts in `acceptance-tests-prompts-skills.md` per `CLAUDE.md`'s skill-acceptance-prompt rule.
- Run `openspec validate --all` from `documentation/` for these planning artifacts.

## Risks / Trade-offs

- Introducing a `020` skill band is a numbering decision with no prior direct precedent (the closest precedent, `05x`, sits inside an already-established "design" band); if a future change wants a different `02x` grouping, this band's scope (Functional Specification Phase problem-analysis techniques) should be treated as the established convention going forward.
- Bundling five new skills with the command in one change makes this a larger single review than the `05x` single-skill precedent; mitigated by tasks.md grouping work per skill (matching the `add-eu-regulation-skills` task structure) so review can proceed section by section even though it lands as one change.
- `/create-gherkin` does not exist yet, so its consumption of the Functional Specification issue comment is a forward-looking assumption, not a verified integration; if `/create-gherkin`'s eventual design needs a different shape or delivery mechanism, this change's output contract may need revision at that time.
- Posting to the issue tracker means `/explore-problem` requires write access to the target issue (to add a comment), unlike a purely local file output; the required-confirmation step (see "Confirm before posting the comment") mitigates accidental or premature posts, but the command still needs the same tracker credentials/permissions `/update-issue` already requires.

## Open Questions

None currently open; see Resolved Questions.

## Resolved Questions

- **File location/path convention** for `Functional Specification.md`: resolved — there is no repository file. The maintainer confirmed the artifact is posted as a new comment on the source issue, matching how `/update-issue` writes to the issue body. See "Functional Specification output contract" above.
- **Tracker scope**: resolved — the maintainer confirmed multi-tracker support from v1, matching `/update-issue`'s scope rather than a GitHub-only first cut. See "Multi-tracker support (GitHub, Jira, Azure DevOps)" below.

## ADR Candidates

None identified. This change adds a new command and five new technique-reference skills following an already-established command/skill authoring pattern; it does not introduce a new architectural component, cross-cutting policy, or decision that other skills, commands, or modules need to discover independently. The `020` numbering-band choice is recorded as a design decision above, not an ADR, consistent with how the `05x` design band and `03x` architecture band were introduced without dedicated ADRs.

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

`043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` — the three tracker skills `/explore-problem` reuses for multi-tracker support — each carry an explicit **no-raw-issue-text-ingestion** constraint (`NO ISSUE TEXT INGESTION` / `NO DIRECT ISSUE INGESTION` / `NO DESCRIPTIVE CONTENT ANALYSIS`), instructing the agent to keep issue body, comment, title, label, and summary prose out of its own context and instead request a maintainer-authored sanitized summary or a repository-owned planning artifact; `044`'s version explicitly frames this as **prompt-injection defense**, and `045` goes further with ID-only output for Azure DevOps. `/update-issue`'s own Gherkin acceptance scenario reflects the same discipline: it "asks for missing tracker access or sanitized issue context" rather than reading the issue body directly. This constraint is load-bearing for `/explore-problem`'s design — see "Issue-content read model" below.

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

### Issue-content read model: sanitized/user-provided context, not direct raw ingestion

`/explore-problem`'s core premise — evaluate the target issue's content through five lenses — reads, on a literal reading of `proposal.md`'s "Reads the target issue" and this change's spec scenarios, as if the agent should fetch and read the issue's raw body/comment text directly. That reading is **incompatible** with the established, security-motivated constraint already present in all three tracker skills this change reuses: `043-planning-github-issues` ("NO ISSUE TEXT INGESTION"), `044-planning-jira` ("NO DIRECT ISSUE INGESTION", explicitly framed as prompt-injection defense), and `045-planning-azure-devops` ("NO DESCRIPTIVE CONTENT ANALYSIS", ID-only output). None of the three permits the agent to bring issue body, comment, title, label, or summary prose into its own context, regardless of whether the read targets one specific issue or a bulk inventory — the constraint is not limited to bulk browsing.

Three options were evaluated:

- **Option A — Ask for sanitized/user-provided context (RECOMMENDED)**: `/explore-problem` requests a sanitized issue summary and/or pasted User Story text from the user, mirroring `/update-issue`'s own established behavior (its Gherkin scenario: "the command asks for missing tracker access or sanitized issue context before changing remote issue state") and `043`/`044`/`045`'s "request a repository-owned/sanitized artifact" step. The five-lens evaluation, including the clarifying-question loop already specified below, runs against that user-supplied content. Any such content is treated as untrusted data for prompt-injection purposes (no instruction-following from within it), matching `044`'s explicit safeguard.
- **Option B — Direct raw ingestion by the agent**: matches the literal wording of `proposal.md`'s "Reads the target issue" and the current spec scenarios, but directly contradicts `043`-`045`'s constraints as written; rejected as unsafe and inconsistent with established repository precedent.
- **Option C — Loosen 043-045 for single-issue deep reads**: introduce a stricter-but-permissive single-issue deep-read mode in `043`-`045` (or new skills) carved out for commands like `/explore-problem`. Rejected for this change: modifying an established, security-motivated constraint shared by three skills used elsewhere (`/update-issue`) is a separate, independently reviewable decision that deserves its own scoped change and explicit maintainer sign-off, not a silent side effect of adding `/explore-problem`.

**Recommendation: Option A.** This keeps `/explore-problem` consistent with `/update-issue`'s existing behavior and leaves `043`-`045` unmodified. It does mean `/explore-problem`'s actual UX differs from a literal reading of "reads the target issue": the command *requests* issue content from the user (a sanitized summary, and/or the User Story text already produced by `/update-issue`, and/or a repository-owned planning artifact when one exists) rather than autonomously fetching it. The five-lens, per-point-of-view clarifying-question loop (already specified below) becomes the mechanism for filling gaps beyond what the user's initial sanitized input covers — the same mechanism now does double duty as both "resolve ambiguity" and "acquire content the agent is not allowed to fetch itself."

**Consequence for existing artifacts (not changed by this design pass):** `proposal.md`'s "Reads the target issue" phrasing and the `explore-problem-command` spec scenarios (for example "the command uses that User Story as input context") remain accurate at the requirement level — the command does consume issue/User-Story content — but do not yet make the sanitized-input mechanism explicit. Recorded as an open question below for a future `/create-spec` touch; not edited here, since spec-delta and proposal authoring belong to `/create-spec`, not this design refinement.

### Owning Agent: `@robot-business-analyst`

`/explore-problem` is owned by `@robot-business-analyst`, matching `/update-issue`'s ownership and the pipeline's "Functional Specification Phase" position (before any architecture or design work begins). Rationale:

- All five lenses are "understand the problem" activities operating directly on issue content — squarely `@robot-business-analyst`'s "issue quality, requirements consistency, traceability" mission — not "design a solution" activities.
- `025-quality-attribute-discovery`'s own acceptance-criteria wording is explicit that it runs "before architecture and design begin," i.e. it discovers and prioritizes candidate quality attributes as input, and deliberately stops short of the architectural decision-making (`@robot-architect`'s `030`–`032` ADR skills and `/explore-design`) that later decides how to satisfy them.
- `/explore-problem` consumes `/update-issue`'s User Story output and feeds the future `/create-gherkin`, both `@robot-business-analyst`-territory commands; keeping ownership consistent avoids a hand-off gap in the Functional Specification Phase.

This OpenSpec change is still authored via `/create-spec` by `@robot-architect` regardless of the resulting command's owning agent, consistent with how `@robot-architect` authored the `add-close-spec-command` OpenSpec change for a command later marked with a different-if-any owning agent.

### Five new `020`-band skill references, orchestrated (not owned) by the command

Add `021-problem-framing`, `022-root-cause-analysis`, `023-assumption-analysis`, `024-context-mapping`, `025-quality-attribute-discovery` as skill references under the existing `skill-references/` + `skill-indexes/` + `skills.xml` pattern (see `055-design-parallel-change` as the structural template). Each skill documents its technique's method and the structured output content it contributes to `Functional Specification.md`; it does not itself run an independent interactive Q&A loop.

The **clarifying-question behavior belongs to the command**, not to each skill individually — mirroring how `/explore-design` orchestrates `051-design-two-steps-methods` through `057-design-feature-toggles` (each individually marked `interactive="true"` in its own skill-index XML — signaling the technique supports interactive use when applied directly, e.g. a maintainer invoking `@051-design-two-steps-methods` standalone — but none of which runs a *separate* interactive Q&A loop when chained through `/explore-design`'s own orchestration) rather than each design skill separately interrogating the user. `/explore-problem`'s own steps apply each of the five lenses in turn, evaluate the available issue/User Story content against that lens's required output fields, and ask the user targeted clarifying questions only when a lens's content is vague or ambiguous, before writing that lens's `Functional Specification.md` section. Following this precedent, `021`-`025`'s skill-index XML files should also set `interactive="true"`, consistent with all seven `05x` design skills and with `014-agile-user-story`, for the same reason: the attribute documents that the technique supports direct interactive use, not that the skill runs its own separate Q&A loop when orchestrated by `/explore-problem`.

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

**Confirmation protocol.** "Explicit user confirmation" means an unambiguous affirmative response (e.g. "yes", "post it", "confirmed") to the presented draft, not silence, an unrelated follow-up message, or an implicit continuation. If the user requests edits to the draft, the command revises and re-presents the full draft (not a diff-only view) and asks again, mirroring `/update-issue`'s "Present the proposed body before overwriting" loop; it does not partially post an unconfirmed section. If the user declines, or the conversation ends without confirmation, the command does not post, states that the draft was not posted, and does not silently retry posting on a later, unrelated invocation. The drafted content does not persist as a separate repository artifact between turns (see "Functional Specification output contract" above — there is no repository file); if the user wants to resume later, they re-run `/explore-problem <issue-url>`. This decline path is not currently covered by an explicit spec scenario in `specs/explore-problem-command/spec.md` (only the "user reviews the draft before it is posted" happy path exists); recorded as an open question below.

### Clarifying-question trigger and scope

For each of the five lenses, before writing that lens's section, the command evaluates whether the available issue/User Story content is sufficient to populate that lens's required fields. When content is vague or ambiguous for a specific field, the command asks a targeted clarifying question about that field rather than inventing an answer or silently omitting the field. This mirrors `014-agile-user-story`'s existing constraint pattern ("Wait for user response after each question or block before proceeding"; "Treat answers as structured story data only").

**Processing order: sequential, per-lens (not batched).** The command walks the five lenses in a fixed order — `021` problem framing, `022` root cause analysis, `023` assumption analysis, `024` context mapping, `025` quality attribute discovery — matching their numbering and the pipeline's natural reading order (problem understanding before root cause before assumptions before context before quality attributes). For each lens in turn: evaluate the available sanitized/user-provided content (see "Issue-content read model" above) against that lens's required fields, ask clarifying questions for that lens only if needed, wait for the user's answer, then write that lens's section before moving to the next lens. This was chosen over batching all five lenses' clarifying questions into one combined round for three reasons: (1) it matches `014-agile-user-story`'s established "wait for user response after each question or block before proceeding" convention, the closest interactive-command precedent in this repository; (2) a single message bundling gaps across five distinct techniques risks a wall-of-questions UX that is harder for the user to answer well; (3) later lenses can benefit from earlier lenses' clarified content (for example, constraints surfaced during `022-root-cause-analysis` may sharpen `025-quality-attribute-discovery`). The complete five-section draft is still assembled and shown in full before the single, separate posting-confirmation step (see "Confirm before posting the comment" below) — sequencing applies to drafting, not to the final confirmation, which happens once over the whole document.

### Required argument and failure modes

`/explore-problem` requires exactly one argument, `<issue-url>`. Missing argument produces usage guidance, matching `/close-spec`'s and `/update-issue`'s existing single-required-argument pattern. Unresolvable issue URLs (tracker unavailable, issue not found) fail gracefully with an actionable message rather than fabricating problem-framing content.

### Multi-tracker support (GitHub, Jira, Azure DevOps)

`/explore-problem` supports GitHub, Jira, and Azure DevOps issue URLs from v1, matching `/update-issue`'s tracker scope (maintainer-confirmed; see Resolved Questions). It reuses the same tracker-specific skills `/update-issue` already depends on:

- `043-planning-github-issues` for GitHub issue URLs — reading the issue and posting the Functional Specification as a GitHub issue comment.
- `044-planning-jira` for Jira issue URLs — reading the issue and posting the Functional Specification as a Jira comment.
- `045-planning-azure-devops` for Azure DevOps work-item URLs — reading the work item and posting the Functional Specification as an Azure DevOps comment.

Tracker selection is derived from `<issue-url>`'s own shape (each tracker's URL format is distinct — GitHub, Atlassian/Jira, and Azure DevOps domains and path structures do not overlap), consistent with the existing Non-Goal that the command does not need to guess an ambiguous tracker: the URL already carries that identity. This mirrors `/update-issue`'s existing-issue-URL handling and its `043`–`045` skill set exactly, so `/explore-problem` has tracker parity with `/update-issue` from its first version rather than adding tracker support as a later follow-up change.

Note: `043`-`045` document tooling/authentication gating and the read-side no-raw-ingestion restriction (see "Issue-content read model" above), but do not themselves specify the mutation command syntax for posting a comment (for example `gh issue comment <issue> --body-file <file>`, a Jira CLI comment-add command, or an Azure DevOps work-item comment command). `/explore-problem`'s own command steps own that mutation call directly, the same way `update-issue.xml` Step 6 ("Update the issue in the selected tracker after approval") owns its edit call without `043`-`045` spelling out the exact command.

### Generator module boundaries: no new Java types warranted

`plinth-commands-generator` and `plinth-skills-generator` are both thin, generic, inventory-driven loaders: `InventoryXmlLoader.java` (33 lines), `CommandIndexes.java` (115 lines), `CommandMarkdownGenerator.java`/`CommandMarkdownRenderer.java` (87/50 lines) on the commands side, and `SkillIndexes.java` (279 lines), `SkillReferenceGenerator.java` (208 lines), `SkillReferences.java` (24 lines) on the skills side. None of these classes special-case individual command or skill ids; they iterate `commands.xml`/`skills.xml` entries generically. Adding `/explore-problem` and `021`-`025` is purely additive inventory registration (new XML files plus new inventory entries) — it introduces no new Java type, no new responsibility split, and no new collaboration between objects.

`121-java-object-oriented-design`, `122-java-type-design`, and `123-java-design-patterns` are therefore judged **not warranted** at this design depth; there is no Java responsibility or type-boundary decision for this change to make. `130-java-testing-strategies` (RIGHT-BICEP / A-TRIP / CORRECT) is likewise not applied as a full framework: the existing generator test style (see `CommandIndexesTest.java`) is simple content-assertion testing against generated Markdown, matching the pattern already used for `close-spec` and `explore-design`. `tasks.md` section 9 gains a few explicit boundary-condition scenarios in that same lightweight style rather than a full RIGHT-BICEP/CORRECT pass (see tasks.md changes).

`054-design-tdd` is also judged not warranted: there is no traditional failing-test/minimal-code/refactor cycle for XML/prompt content generation — verification here is content-assertion and generated-output inspection, already reflected in the existing Validation Strategy. `056-design-avoid-breaking-changes` and `057-design-feature-toggles` were considered: `056`'s compatibility-risk review is already performed above (see "Compatibility Review"), and `057` does not apply because this is purely additive content with no runtime behavior to toggle or roll back.

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
- Inspect generated output specifically for the issue-content sourcing step (sanitized/user-provided context request, not a raw-fetch instruction), the sequential per-lens processing order, the `interactive="true"` attribute on `021`-`025`, and the confirmation-decline behavior (see "Issue-content read model", "Clarifying-question trigger and scope", and "Confirm before posting the comment" above).
- Add/execute Gherkin acceptance coverage for the new command (`plinth-commands-generator/src/test/resources/gherkin/commands/explore-problem.feature`) and for each new skill (`plinth-skills-generator/src/test/resources/gherkin/skills/02{1..5}-*.feature`), and register the new skill prompts in `acceptance-tests-prompts-skills.md` per `CLAUDE.md`'s skill-acceptance-prompt rule.
- Run `openspec validate --all` from `documentation/` for these planning artifacts.

## Risks / Trade-offs

- Introducing a `020` skill band is a numbering decision with no prior direct precedent (the closest precedent, `05x`, sits inside an already-established "design" band); if a future change wants a different `02x` grouping, this band's scope (Functional Specification Phase problem-analysis techniques) should be treated as the established convention going forward.
- Bundling five new skills with the command in one change makes this a larger single review than the `05x` single-skill precedent; mitigated by tasks.md grouping work per skill (matching the `add-eu-regulation-skills` task structure) so review can proceed section by section even though it lands as one change.
- `/create-gherkin` does not exist yet, so its consumption of the Functional Specification issue comment is a forward-looking assumption, not a verified integration; if `/create-gherkin`'s eventual design needs a different shape or delivery mechanism, this change's output contract may need revision at that time.
- Posting to the issue tracker means `/explore-problem` requires write access to the target issue (to add a comment), unlike a purely local file output; the required-confirmation step (see "Confirm before posting the comment") mitigates accidental or premature posts, but the command still needs the same tracker credentials/permissions `/update-issue` already requires.
- The sanitized/user-provided issue-content read model (see "Issue-content read model" above) means `/explore-problem`'s output quality depends on how much context the user is willing or able to supply up front; the per-lens clarifying-question loop mitigates this but cannot fully substitute for direct issue access the way a literal "reads the issue" command would.

## Open Questions

- **Issue-content read model approval**: this design pass recommends Option A (sanitized/user-provided context; see "Issue-content read model" above) over the literal "reads the target issue" framing in `proposal.md` and the current spec scenarios. This is a materially different mechanism from a direct-fetch reading of `proposal.md` and needs explicit maintainer confirmation before implementation proceeds; if confirmed, `proposal.md`'s "Reads the target issue" wording and the `explore-problem-command` spec scenarios should be revisited for precision at the next `/create-spec` touch (not done in this session).
- **Confirmation-decline scenario**: `specs/explore-problem-command/spec.md` currently has no scenario for the user declining to confirm the draft (only the "user reviews the draft before it is posted" happy path). Recommend adding one at the next `/create-spec` touch; not added here.
- **ADR follow-through timing**: whether to record the new "issue-content read model" ADR candidate (see "ADR Candidates" below) before implementation starts, or accept it as a design.md-recorded decision until the next natural ADR-authoring moment. Maintainer's call.

## Resolved Questions

- **File location/path convention** for `Functional Specification.md`: resolved — there is no repository file. The maintainer confirmed the artifact is posted as a new comment on the source issue, matching how `/update-issue` writes to the issue body. See "Functional Specification output contract" above.
- **Tracker scope**: resolved — the maintainer confirmed multi-tracker support from v1, matching `/update-issue`'s scope rather than a GitHub-only first cut. See "Multi-tracker support (GitHub, Jira, Azure DevOps)" below.

## ADR Candidates

Re-evaluated during this design pass, one candidate identified (not previously surfaced by the initial `/create-spec` pass):

- **Deep single-issue content analysis must source evaluation input from sanitized/user-provided context, not direct raw tracker-text ingestion by the agent.** This decision (see "Issue-content read model" above) is durable and reusable beyond `/explore-problem`: any future command that wants to deeply analyze one specific issue's content (not just browse an inventory) will hit the same tension between wanting rich issue content and the existing `043`-`045` no-raw-ingestion/prompt-injection-defense constraint, and should follow the same resolution rather than re-litigate it per command. It is cross-cutting (affects `043`, `044`, `045`, and any future issue-analysis command), was never explicitly recorded as a project-level decision (only implicit in the three tracker skills' individual constraint wording, with no unifying ADR in `documentation/adr/`), and has real alternatives and consequences (Option A/B/C above). Recommend recording it as a general or non-functional-requirement ADR via `030-architecture-adr-general` or `032-architecture-adr-non-functional-requirements` before or alongside `/explore-problem`'s implementation, so future commands can discover and reuse the decision instead of re-deriving it. **Not created in this session** — `/explore-design` identifies ADR candidates but does not author them; this needs a separate `/create-adr` step and maintainer sign-off.

The `020` numbering-band choice and the command/skill orchestration split are **re-evaluated and re-confirmed as not warranting a dedicated ADR**: both remain reversible, low-risk, single-change-scoped conventions consistent with how the `030` architecture band and the `05x` design band were each introduced without a dedicated ADR of their own; unlike the issue-content read model, neither is a cross-cutting trust-boundary decision that another skill or command must discover independently to avoid re-deriving it unsafely.

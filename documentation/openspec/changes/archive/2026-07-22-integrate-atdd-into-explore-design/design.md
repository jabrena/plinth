## Context

`/explore-design` is the post-`/create-spec` command for refining technical approaches. Its canonical definition is `plinth-commands-generator/src/main/resources/commands/explore-design.xml`, with command-focused unit coverage in `CommandIndexesTest` and behavioral coverage in `explore-design.feature`.

The independently generated `059-design-atdd` skill already defines a read-only review of an OpenSpec change's execution goal, acceptance criteria, and single task checklist. It produces an evidence-backed `ready` or `changes-requested` outcome and never silently edits the reviewed OpenSpec artifacts. The missing connection is command orchestration: `/explore-design` neither lists the skill nor makes its outcome part of the approval flow.

The change affects command behavior and verification but introduces no new runtime dependency, data model, or application code. Plinth maintainers own the generator sources; contributors, reviewers, and implementers consume the resulting workflow.

## Goals / Non-Goals

**Goals:**

- Route OpenSpec refinement through `059-design-atdd` before `/explore-design` requests design approval.
- Preserve the skill's evidence, outcome, and no-silent-rewrite contract.
- Prevent unresolved alignment findings from being reported as an approved design direction.
- Preserve existing command inputs, skill routing, workflow position, and application-code boundary.
- Add command-level unit and acceptance coverage for the integration.

**Non-Goals:**

- Change the behavior or generated content of `059-design-atdd`.
- Add `059-design-atdd` to `/create-spec` or move initial OpenSpec authoring into `/explore-design`.
- Require ATDD review when an issue or user story is explored without an OpenSpec proposal, specifications, and tasks.
- Generate or modify user stories, Gherkin acceptance files, ADRs, or application code.
- Refresh public `skills/` or website output.

## Decisions

### Keep orchestration in the command generator

Update the canonical `/explore-design` XML in `plinth-commands-generator` and its command-focused tests. Do not edit `.claude/commands/`, `.cursor/commands/`, `.agents/skills/`, public `skills/`, or `docs/` directly.

This keeps the command as the workflow orchestrator and the existing skill as the owner of ATDD classification and report semantics.

**Alternative considered:** Modify `059-design-atdd` or duplicate its rules inside the command. Rejected because the skill already owns the complete alignment behavior and duplication would create drift.

### Apply ATDD only when an OpenSpec change supplies reviewable artifacts

When `/explore-design` receives an OpenSpec change with an execution goal, acceptance criteria, and tasks, it invokes `059-design-atdd`. Issue-only or user-story-only exploration remains supported, but the command does not fabricate missing OpenSpec artifacts merely to run the review.

**Alternative considered:** Run the ATDD skill for every command input. Rejected because the skill requires repository-owned OpenSpec proposal, specification scenarios, and a task checklist; issue-only input does not satisfy that contract.

### Use ATDD as the final alignment gate before approval

The command performs design refinement first, reflects maintainer-approved refinements in the existing OpenSpec change when applicable, and then invokes `059-design-atdd` before requesting final design approval. This ordering reviews the artifacts that would actually be handed to implementation.

- `ready`: include the alignment result in the command report and continue to the existing approval step.
- `changes-requested`: report every unresolved finding, do not label the direction approved, ask the maintainer how the OpenSpec artifacts should be revised, and allow the review to be rerun after approved revisions.

**Alternative considered:** Run ATDD before design refinement only. Rejected because later refinements could invalidate the earlier alignment result.

### Preserve read-only review and explicit maintainer control

The command must not translate `changes-requested` into automatic edits. Any proposal, specification, or task changes remain explicit refinements approved through the existing `/explore-design` workflow.

### Extend existing command verification

Update the command unit test to assert the generated command includes `059-design-atdd`. Extend the existing Gherkin command scenario to verify invocation for OpenSpec input, `ready` progression, `changes-requested` blocking behavior, preserved artifacts, and the unchanged issue-only path. Keep the existing acceptance prompt entry because the feature path does not change.

## Risks / Trade-offs

- **[Risk] The extra interactive review lengthens design refinement.** → Restrict it to inputs with reviewable OpenSpec artifacts and run it once after refinements stabilize.
- **[Risk] A stale review could be treated as current after artifact changes.** → Require rerunning the review after maintainer-approved changes that address `changes-requested` findings.
- **[Risk] Command and skill outcome wording diverge.** → Reference the skill's `ready` and `changes-requested` vocabulary instead of redefining classifications in the command.
- **[Risk] Generated command output drifts from XML source.** → Change canonical XML, run module verification, and inspect generated command output rather than editing it directly.
- **[Trade-off] Issue-only exploration cannot provide ATDD traceability.** → Preserve that established input mode and report ATDD only when the required OpenSpec artifacts exist.

## Migration Plan

1. Update command tests to express the target ATDD orchestration and approval behavior.
2. Update the canonical `explore-design.xml` command contract.
3. Validate the XML source and run focused `plinth-commands-generator` verification.
4. Inspect generated command Markdown and execute the existing `/explore-design` acceptance prompt.
5. Roll back by reverting the command XML and matching tests together; no data migration or deployed runtime rollback is required.

## Open Questions

None. The initial ordering and outcome-presentation questions are resolved by treating ATDD as the final read-only alignment gate before the existing approval step.

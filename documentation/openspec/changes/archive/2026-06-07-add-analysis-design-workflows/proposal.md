## Why

Issue [#806](https://github.com/jabrena/plinth/issues/806) spans commands, agents, planning skills, migration documentation, and validation. This parent change coordinates four smaller changes so source work can proceed in parallel without duplicating ownership or creating conflicting task lists.

## What Changes

- Coordinate `add-analysis-design-commands`.
- Coordinate `add-analysis-design-agents`.
- Coordinate `add-composable-planning-workflows`.
- Gate `document-analysis-design-lifecycle` until the three source changes are integrated.
- Track shared decisions, dependency order, final alignment, and issue-level completion.
- Use `/create-worktree` as the recommended isolation mechanism for parallel child implementation.
- Keep implementation requirements and file-level tasks exclusively in the owning child changes.

## Capabilities

### New Capabilities

- `analysis-design-workflow-coordination`: Defines dependency tracking, parallel ownership boundaries, integration gates, and parent completion criteria.

### Modified Capabilities

None.

## Impact

- This parent does not own production, command, agent, XML skill, README, or guide files.
- The three source changes may execute in parallel because they have separate primary ownership.
- The documentation/integration change executes after the source changes.
- Issue #806 is complete only when all four child changes validate and the integrated lifecycle passes final checks.

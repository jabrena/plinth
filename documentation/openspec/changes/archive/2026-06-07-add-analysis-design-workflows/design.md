## Context

The original change mixed command assets, agent assets, XML skill behavior, documentation, localization, migration, and final validation in one task list. Those areas have different file ownership and can be delivered with less contention as separate OpenSpec changes.

## Goals / Non-Goals

**Goals:**

- Enable parallel implementation of commands, agents, and planning skills.
- Assign each shared file to one child change.
- Integrate documentation only after source behavior stabilizes.
- Preserve one parent view of issue #806 completion.

**Non-Goals:**

- Repeat child specifications or implementation tasks.
- Own source files directly.
- Archive child changes before their implementation is complete.

## Decisions

### Split by primary file ownership

| Child change | Primary ownership | Execution |
|---|---|---|
| `add-analysis-design-commands` | Command assets, command installer/inventory, command-focused tests | Parallel |
| `add-analysis-design-agents` | Agent assets, agent installer/inventory, agent-focused tests | Parallel |
| `add-composable-planning-workflows` | Skills `034`, `041`, `042`, `skills.xml`, skill-focused tests | Parallel after XML approval |
| `document-analysis-design-lifecycle` | README, inventories, localized guides, final integrated validation | After source integration |

### Isolate parallel children with worktrees

Use `/create-worktree` to create one linked checkout per parallel source child when implementation will run concurrently:

```text
add-analysis-design-commands
add-analysis-design-agents
add-composable-planning-workflows
```

Each worktree uses a distinct branch and path. The integration/documentation child starts only after those branches are integrated. Worktree removal remains an explicit manual or separate cleanup action and is not part of `/create-worktree`.

### Avoid shared test-file conflicts

Each source child adds focused tests in the most suitable existing test class, but concurrent branches MUST avoid editing the same test method or broad fixture. If two children need the same test file, they add separate nested classes or test methods and integrate through ordinary merge resolution.

### Keep documentation last

Documentation depends on final command names, agent migration, skill inputs, and artifact-authority rules. Delaying it avoids repeated translation and inventory churn.

### Use the parent as the completion gate

The parent tracks child status and final issue alignment. It contains no duplicate functional requirements beyond coordination behavior.

## Risks / Trade-offs

- [Risk] OpenSpec does not enforce cross-change dependencies automatically. -> Record dependencies in parent and child proposal/design prose and tasks.
- [Risk] Parallel tests still touch the same Java files. -> Use focused additions and integrate source children before documentation.
- [Risk] Child behavior diverges from issue #806. -> Run `/review-alignment` semantics and final OpenSpec validation during the integration child.

## Migration Plan

1. Implement and validate the three source children.
2. Integrate them into one branch.
3. Implement documentation and migration guidance.
4. Run final validation.
5. Mark parent coordination tasks complete.

## Open Questions

- Confirm XML skill `034` before the planning child creates new XML sources.

## Resolved Questions

- `robot-coordinator` is renamed immediately to `robot-tech-lead`; no compatibility alias is installed.
- Skill identifier `034-architecture-design-exploration` is approved for the planning child.

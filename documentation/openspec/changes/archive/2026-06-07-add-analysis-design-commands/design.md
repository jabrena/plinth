## Context

The repository currently installs five embedded commands. Issue #806 adds eight analysis/design/support actions and repositions the existing feature-branch command so teams can version plans, OpenSpec artifacts, ADRs, diagrams, and documentation before implementation. Parallel child changes also need an isolated Git worktree option.

This change is intentionally isolated from agent definitions and skill internals. Commands are routing contracts rather than duplicated agent or skill prompts.

## Goals / Non-Goals

**Goals:**

- Add one narrow command for each requested analysis/design action and isolated-worktree setup.
- Keep command contracts consistent and cross-tool compatible.
- Extend branch creation for issue/change inputs and pre-implementation artifacts.
- Keep installer and inventory sources synchronized.

**Non-Goals:**

- Implement agent missions or XML skill behavior.
- Update README or localized guides.
- Add automatic commits, pushes, or pull requests.
- Edit generated command directories directly.

## Decisions

### Keep commands thin

Each command defines purpose, accepted inputs, owner, associated skills/capabilities, workflow, output, and safeguards. Substantive behavior remains in agents and skills.

Alternative considered: duplicate full skill instructions in commands. Rejected because it creates three sources of truth.

### Reuse `/create-feature-branch`

The existing command accepts an issue/change identifier or explicit supported type and description, checks for a safe working tree, and creates a conventionally named local branch without committing.

Alternative considered: add a second analysis-phase branch command. Rejected because branch creation is one Git operation with a broader lifecycle position.

### Add `/create-worktree` for isolated parallel work

The new command accepts an issue/change identifier or explicit supported branch type and description, plus an optional target path and base reference. It verifies that the branch and path are available, then creates an isolated linked checkout with `git worktree add`.

The command reports the branch, path, and base reference. It does not commit, push, remove worktrees, delete branches, or force through dirty/conflicting state.

Alternative considered: overload `/create-feature-branch` with a worktree flag. Rejected because switching the current checkout and creating a separate linked checkout have different safety checks and outputs.

### Own command bundle synchronization

This change alone updates command assets, the command installer XIncludes/counts, command inventory template, and command-focused tests. This prevents parallel work from colliding in command-owned files.

## Risks / Trade-offs

- [Risk] `/create-feature-branch` and `/create-worktree` overlap in perceived purpose. -> Document current-checkout versus isolated-checkout behavior explicitly.
- [Risk] Commands reference agents or skills not yet integrated. -> Document sibling dependencies in the parent coordination change and integrate all children before release.
- [Risk] Installer count drifts. -> Add exact filename assertions.

## Migration Plan

1. Add focused tests.
2. Add eight command assets and extend feature-branch.
3. Update installer and inventory sources.
4. Validate XML and the generator module.

## Open Questions

None for command-owned sources.

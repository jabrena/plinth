## MODIFIED Requirements

### Requirement: Controlled delegation and verification

`/implement-spec` MUST require framework-aware coder routing, dependency and file-ownership controls, evidence-based completion, and focused validation reporting within the selected execution artifact.

#### Scenario: Delegate independent task groups

- **WHEN** the execution artifact contains independent groups without dependency or file-ownership conflicts
- **THEN** `@robot-tech-lead` may delegate them concurrently to the selected specialized coder
- **AND** integrates their results before final checks

#### Scenario: Stop on artifact conflict

- **WHEN** authoritative issue, ADR, specification, or plan content conflicts materially
- **THEN** implementation stops
- **AND** the conflict is routed to `robot-business-analyst` for manual assessment, since the `/review-alignment` command is retired

#### Scenario: Complete implementation workflow

- **WHEN** delegated tasks pass their acceptance criteria and focused checks
- **THEN** `/implement-spec` reports changed files, test and build evidence, task status, blockers, and risks

#### Scenario: Select branch or worktree execution

- **WHEN** `/implement-spec` reviews the approved plan or OpenSpec task list
- **THEN** it decides whether the work should run on a feature branch or in one or more linked worktrees
- **AND** it uses `/create-feature-branch` for serial current-checkout work
- **AND** it uses `/create-worktree` when independent groups can run safely in parallel

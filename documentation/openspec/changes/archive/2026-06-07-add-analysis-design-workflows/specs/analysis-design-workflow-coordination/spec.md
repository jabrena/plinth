## ADDED Requirements

### Requirement: Parallel source ownership

The parent workflow SHALL assign command, agent, and planning-skill implementation to separate child changes with explicit primary file ownership.

#### Scenario: Start parallel implementation

- **WHEN** issue #806 implementation begins
- **THEN** command, agent, and planning-skill child changes can proceed independently
- **AND** each child edits only its declared primary source area except for focused test additions

### Requirement: Worktree isolation for concurrent children

The parent workflow SHALL recommend one distinct Git worktree and branch per source child when the child changes are implemented concurrently.

#### Scenario: Run three source changes concurrently

- **WHEN** command, agent, and planning-skill child changes are assigned for parallel implementation
- **THEN** each child can be created in a separate linked checkout through `/create-worktree`
- **AND** each worktree reports its branch, path, and base reference
- **AND** integration occurs before the documentation child begins

### Requirement: Documentation integration gate

The lifecycle documentation change MUST remain blocked until the three source child changes are integrated and their final names and behavior are known.

#### Scenario: Source changes are incomplete

- **WHEN** any source child has unresolved implementation or validation work
- **THEN** the documentation child remains incomplete
- **AND** localized lifecycle guidance is not finalized against provisional behavior

### Requirement: Parent completion

The parent change SHALL be complete only after all child changes satisfy their specifications and final integrated validation passes.

#### Scenario: Complete issue 806

- **WHEN** commands, agents, planning skills, documentation, migration guidance, and integrated validation are complete
- **THEN** the parent confirms coverage of issue #806 acceptance criteria
- **AND** child changes are archived before the coordination parent

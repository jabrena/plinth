## ADDED Requirements

### Requirement: OpenSpec implementation readiness gate

Before any skill discovery, Git-location change, or implementation delegation, `/implement-spec` MUST require bidirectional traceability between concrete acceptance scenarios in `specs/**/spec.md` and the behavior-changing implementation or verification tasks in the selected execution scope from `tasks.md`.

#### Scenario: OpenSpec change contains concrete execution evidence

- **GIVEN** the selected OpenSpec change is structurally valid
- **AND** every selected behavior-changing task is supported by one or more concrete acceptance scenarios
- **AND** every scenario applicable to the selected scope has an actionable implementation or verification task
- **AND** each concrete scenario defines a trigger and observable outcome without unresolved placeholders
- **WHEN** the user invokes `/implement-spec` for that change
- **THEN** the readiness gate passes
- **AND** the command may continue to workspace and location checks

#### Scenario: OpenSpec change lacks concrete or complete acceptance evidence

- **GIVEN** the selected OpenSpec change has no scenario, an ambiguous or placeholder scenario, no actionable incomplete task, or partial bidirectional traceability for the selected scope
- **WHEN** the user invokes `/implement-spec` for that change
- **THEN** the command stops before skill discovery, Git-location changes, and implementation delegation
- **AND** identifies each unsupported or ambiguous scenario or task
- **AND** instructs the contributor to update the OpenSpec change and rerun `/implement-spec`
- **AND** does not invent acceptance criteria or tasks

#### Scenario: Selected task group limits readiness scope

- **GIVEN** the OpenSpec change contains multiple task groups
- **AND** the user selects one task or group for execution
- **WHEN** `/implement-spec` evaluates readiness
- **THEN** it requires complete traceability for the selected scope
- **AND** does not require unrelated future task groups to be implementation-complete
- **AND** still rejects a selected task that diverges from the change requirements or safeguards

### Requirement: Explicit implementation location resolution

For an OpenSpec input, `/implement-spec` MUST use an implementation location supplied by invocation constraints or a valid `## Implementation Location` section in the selected change's `design.md`, and MUST ask the contributor to choose `main`, a feature branch, or a worktree when neither source specifies a valid location.

#### Scenario: Invocation supplies implementation location

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** the `/implement-spec` invocation explicitly supplies an implementation location
- **WHEN** the command resolves its location
- **THEN** it uses the invocation location subject to the existing workspace safety gates
- **AND** does not ask the contributor to choose another location

#### Scenario: OpenSpec design supplies implementation location

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** the invocation does not supply an implementation location
- **AND** the selected OpenSpec `design.md` contains `## Implementation Location`
- **AND** the section defines `Strategy` as `main`, `feature-branch`, or `worktree`
- **WHEN** the command resolves its location
- **THEN** it uses the strategy and optional reference recorded in that section subject to the existing workspace safety gates
- **AND** does not ask the contributor to choose another location

#### Scenario: Implementation location is missing

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** neither the invocation nor a valid `## Implementation Location` section supplies an implementation location
- **WHEN** the command resolves its location
- **THEN** it asks the contributor to choose `main`, a feature branch, or a worktree
- **AND** waits for the answer before creating or selecting a location
- **AND** does not delegate implementation before the location is confirmed

#### Scenario: OpenSpec design contains an invalid implementation location

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** the invocation does not supply an implementation location
- **AND** `design.md` has a missing, blank, or unsupported `Strategy` value
- **WHEN** the command resolves its location
- **THEN** it treats the artifact location as unresolved
- **AND** asks the contributor to choose `main`, a feature branch, or a worktree
- **AND** does not silently guess a strategy

#### Scenario: Contributor selects main

- **GIVEN** the command asked for a missing implementation location
- **WHEN** the contributor selects `main` or the repository default branch
- **THEN** the command warns about direct implementation on the default branch
- **AND** requires separate explicit approval before implementation starts

#### Scenario: Contributor selects a feature branch or worktree

- **GIVEN** the command asked for a missing implementation location
- **WHEN** the contributor selects a feature branch or worktree
- **THEN** the command applies the existing feature-branch or worktree safety workflow
- **AND** reports the selected isolation strategy before delegation

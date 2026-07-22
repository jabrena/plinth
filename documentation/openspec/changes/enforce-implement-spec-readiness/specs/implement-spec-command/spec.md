## ADDED Requirements

### Requirement: OpenSpec implementation readiness gate

Before any skill discovery, Git-location change, or implementation delegation, `/implement-spec` MUST require an OpenSpec input to contain concrete acceptance scenarios in `specs/**/spec.md` and actionable incomplete checklist tasks in `tasks.md` that implement or verify the specified behavior.

#### Scenario: OpenSpec change contains concrete execution evidence

- **GIVEN** the selected OpenSpec change is structurally valid
- **AND** its specification contains at least one concrete acceptance scenario
- **AND** its task list contains at least one actionable incomplete implementation or verification task
- **WHEN** the user invokes `/implement-spec` for that change
- **THEN** the readiness gate passes
- **AND** the command may continue to workspace and location checks

#### Scenario: OpenSpec change lacks concrete acceptance evidence

- **GIVEN** the selected OpenSpec change lacks a concrete acceptance scenario or a corresponding actionable incomplete task
- **WHEN** the user invokes `/implement-spec` for that change
- **THEN** the command stops before skill discovery, Git-location changes, and implementation delegation
- **AND** identifies the missing evidence
- **AND** instructs the contributor to update the OpenSpec change and rerun `/implement-spec`
- **AND** does not invent acceptance criteria or tasks

### Requirement: Explicit implementation location resolution

For an OpenSpec input, `/implement-spec` MUST use an implementation location supplied by invocation constraints or the selected change, and MUST ask the contributor to choose `main`, a feature branch, or a worktree when neither source specifies a location.

#### Scenario: Invocation supplies implementation location

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** the `/implement-spec` invocation explicitly supplies an implementation location
- **WHEN** the command resolves its location
- **THEN** it uses the invocation location subject to the existing workspace safety gates
- **AND** does not ask the contributor to choose another location

#### Scenario: OpenSpec change supplies implementation location

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** the invocation does not supply an implementation location
- **AND** the selected OpenSpec change records an implementation location
- **WHEN** the command resolves its location
- **THEN** it uses the location recorded in the change subject to the existing workspace safety gates
- **AND** does not ask the contributor to choose another location

#### Scenario: Implementation location is missing

- **GIVEN** the OpenSpec readiness gate has passed
- **AND** neither the invocation nor the selected OpenSpec change supplies an implementation location
- **WHEN** the command resolves its location
- **THEN** it asks the contributor to choose `main`, a feature branch, or a worktree
- **AND** waits for the answer before creating or selecting a location
- **AND** does not delegate implementation before the location is confirmed

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

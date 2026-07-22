## ADDED Requirements

### Requirement: Tech lead OpenSpec readiness verification

`robot-tech-lead` MUST own pre-delegation readiness verification for OpenSpec delivery by checking concrete acceptance scenarios, actionable incomplete tasks, and an explicit implementation location before invoking an implementation agent.

#### Scenario: Tech lead accepts implementation-ready OpenSpec input

- **GIVEN** an OpenSpec change contains concrete acceptance scenarios
- **AND** its `tasks.md` contains actionable incomplete implementation or verification tasks
- **AND** an implementation location is supplied by the invocation or the change
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it accepts the OpenSpec input as ready for the remaining workspace, skill-discovery, and delegation gates
- **AND** preserves artifact authority and existing delegation safeguards

#### Scenario: Tech lead rejects missing acceptance evidence

- **GIVEN** an OpenSpec change lacks a concrete acceptance scenario or corresponding actionable incomplete task
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it stops delivery before invoking an implementation agent
- **AND** reports the missing evidence and required OpenSpec update
- **AND** waits for the contributor to update the change and rerun delivery

#### Scenario: Tech lead asks for missing implementation location

- **GIVEN** the acceptance-evidence gate has passed
- **AND** neither invocation constraints nor the selected OpenSpec change defines an implementation location
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it asks the contributor to choose `main`, a feature branch, or a worktree
- **AND** waits for the answer before location setup, skill discovery, or implementation delegation

#### Scenario: Tech lead preserves main branch protection

- **GIVEN** the contributor selects `main` or the repository default branch
- **WHEN** `robot-tech-lead` prepares delivery
- **THEN** it issues the existing warning
- **AND** requires explicit approval before invoking an implementation agent

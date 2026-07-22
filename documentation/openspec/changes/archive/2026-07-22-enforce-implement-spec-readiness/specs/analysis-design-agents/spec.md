## ADDED Requirements

### Requirement: Tech lead OpenSpec readiness verification

`robot-tech-lead` MUST own pre-delegation readiness verification for OpenSpec delivery by checking bidirectional traceability between concrete scenarios and selected behavior-changing tasks, then resolving an explicit implementation location before invoking an implementation agent.

#### Scenario: Tech lead accepts implementation-ready OpenSpec input

- **GIVEN** every selected behavior-changing task maps to one or more concrete scenarios
- **AND** every scenario applicable to the selected scope maps to an actionable implementation or verification task
- **AND** an implementation location is supplied by the invocation or a valid `## Implementation Location` section in `design.md`
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it accepts the OpenSpec input as ready for the remaining workspace, skill-discovery, and delegation gates
- **AND** preserves artifact authority and existing delegation safeguards

#### Scenario: Tech lead rejects incomplete acceptance traceability

- **GIVEN** the selected scope has an absent, ambiguous, placeholder, missing, partial, or divergent scenario-to-task relationship
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it stops delivery before invoking an implementation agent
- **AND** reports each unsupported scenario or task and the required OpenSpec update
- **AND** waits for the contributor to update the change and rerun delivery

#### Scenario: Tech lead asks for missing implementation location

- **GIVEN** the acceptance-evidence gate has passed
- **AND** neither invocation constraints nor a valid `## Implementation Location` section in `design.md` defines an implementation location
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it asks the contributor to choose `main`, a feature branch, or a worktree
- **AND** waits for the answer before location setup, skill discovery, or implementation delegation

#### Scenario: Tech lead limits readiness to the selected task scope

- **GIVEN** the user selects one task or task group from a larger OpenSpec change
- **WHEN** `robot-tech-lead` evaluates delivery readiness
- **THEN** it checks complete bidirectional traceability for the selected scope
- **AND** leaves unrelated future task groups outside the current execution decision

#### Scenario: Tech lead preserves main branch protection

- **GIVEN** the contributor selects `main` or the repository default branch
- **WHEN** `robot-tech-lead` prepares delivery
- **THEN** it issues the existing warning
- **AND** requires explicit approval before invoking an implementation agent

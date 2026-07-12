## MODIFIED Requirements

### Requirement: Command contracts

Each analysis/design command MUST document its purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards.

#### Scenario: Invoke an analysis or design command

- **WHEN** a project user opens an installed analysis/design command
- **THEN** the command identifies the artifacts it accepts and the artifact or report it produces
- **AND** it delegates substantive behavior to the named agent and skill rather than duplicating their complete prompts

#### Scenario: Create an OpenSpec change

- **WHEN** a project user invokes `/create-spec` with an issue, approved design, ADRs, implementation plan, existing OpenSpec change, or valid combination
- **THEN** the command routes OpenSpec creation through `@robot-architect`
- **AND** it applies OpenSpec planning, two-step sequencing, compatibility review, and relevant design/testing guidance before finalizing requirements
- **AND** it creates or updates OpenSpec artifacts only under `documentation/openspec` when edits are requested
- **AND** it validates OpenSpec structure before claiming the change is ready
- **AND** it reports changed files, validation evidence, assumptions, and unresolved planning risks

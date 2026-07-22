## MODIFIED Requirements

### Requirement: Command contracts

Each analysis/design command MUST document its purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards.

#### Scenario: Invoke an analysis or design command

- **WHEN** a project user opens an installed analysis/design command
- **THEN** the command identifies the artifacts it accepts and the artifact or report it produces
- **AND** it delegates substantive behavior to the named agent and skill rather than duplicating their complete prompts

#### Scenario: Create an OpenSpec change from complete issue context

- **WHEN** a project user invokes `/create-spec` with an issue identifier or URL
- **THEN** the command requires a maintainer-prepared sanitized artifact derived outside the agent context from the issue description and every paginated comment
- **AND** the artifact confirms complete description and comment coverage before scope assessment or OpenSpec authoring
- **AND** the command does not retrieve or ingest raw issue descriptions or comments
- **AND** it records the sanitized artifact as source context and reports conflicts or unclear requirements without inventing resolutions
- **AND** it stops and reports the failure when complete sanitized context is unavailable
- **AND** it routes OpenSpec creation through `@robot-architect` using only `042-planning-openspec`
- **AND** it does not apply design skills `051`–`057`, `121`–`123`, `130`, or `034-architecture-design-exploration`
- **AND** it creates or updates OpenSpec artifacts only under `documentation/openspec` when edits are requested
- **AND** it validates OpenSpec structure before claiming the change is ready
- **AND** it reports changed files, validation evidence, source traceability, assumptions, and unresolved planning risks
- **AND** it documents `/create-spec` as the first workflow step before design refinement
- **AND** it does not modify the issue description or comments

#### Scenario: Create an OpenSpec change from non-issue artifacts

- **WHEN** a project user invokes `/create-spec` with an approved design, ADRs, implementation plan, existing OpenSpec change, or valid non-issue combination
- **THEN** the command creates or updates OpenSpec artifacts from those authoritative inputs without requiring tracker access
- **AND** it preserves the existing source-authority, scope-assessment, validation, and traceability workflow

## ADDED Requirements

### Requirement: Business analyst missions

`robot-business-analyst` SHALL own issue creation and read-only alignment review while remaining separate from technical implementation.

#### Scenario: Create and later review an issue

- **WHEN** a project user asks the business analyst to create an issue and later review available design artifacts
- **THEN** the agent uses GitHub or Jira planning skills for issue creation
- **AND** it performs consistency, traceability, and readiness review without implementing or silently correcting technical artifacts

### Requirement: Architect missions

The embedded agent bundle SHALL include `robot-architect` with distinct missions for design exploration, architecture decision records, and architecture diagrams.

#### Scenario: Move from exploration to architecture records

- **WHEN** a project user approves a design direction containing significant decisions and useful architecture views
- **THEN** `robot-architect` identifies the decisions suitable for ADRs
- **AND** it can create ADRs and diagrams through their associated skills
- **AND** it keeps exploration, decision recording, and diagram generation as separate outputs

### Requirement: Tech lead role and migration

The embedded agent bundle MUST replace `robot-coordinator` with `robot-tech-lead`, preserve framework-aware coder delegation, and add independent plan and OpenSpec creation responsibilities.

#### Scenario: Reinstall agents after the rename

- **WHEN** an existing user installs the updated embedded agent bundle
- **THEN** the bundle provides `robot-tech-lead.md` instead of `robot-coordinator.md`
- **AND** the tech lead continues delegating Java, Spring Boot, Quarkus, and Micronaut implementation to the existing coder agents

### Requirement: No direct implementation by coordinating roles

`robot-business-analyst`, `robot-architect`, and `robot-tech-lead` MUST NOT implement application code as a substitute for the specialized coder agents.

#### Scenario: Deliver an approved plan

- **WHEN** `robot-tech-lead` receives an approved plan or OpenSpec task list for implementation
- **THEN** it selects and delegates to the appropriate coder agent
- **AND** it tracks completion and verification against the selected execution artifact
- **AND** it does not perform the coder's implementation work directly

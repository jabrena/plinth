# analysis-design-agents Specification

## Purpose
TBD - created by archiving change add-analysis-design-agents. Update Purpose after archive.
## Requirements
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

### Requirement: Non-Java default implementation agent

The embedded agent bundle SHALL include `robot-no-java` as the default implementation delegate for issue, plan, or OpenSpec work whose authoritative artifacts do not use Java, Maven, or a JVM-based framework.

#### Scenario: Install the non-Java default agent

- **WHEN** a user installs the embedded agent bundle
- **THEN** the installer copies `robot-no-java.md` with the existing agents
- **AND** the embedded agent inventory lists `robot-no-java`

### Requirement: Tech lead non-Java routing

`robot-tech-lead` MUST route non-Java implementation work to `@robot-no-java` while preserving existing Java, Spring Boot, Quarkus, and Micronaut routing.

#### Scenario: Delegate a non-Java issue

- **GIVEN** an issue, plan, or OpenSpec task list names a non-Java stack or lacks Java/JVM scope
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-no-java`
- **AND** it does not delegate the work to `@robot-java-coder` by default

#### Scenario: Delegate plain Java work

- **GIVEN** an issue, plan, or OpenSpec task list uses Java, Maven, or framework-neutral JVM implementation
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-java-coder` unless Spring Boot, Quarkus, or Micronaut evidence selects a framework-specific coder


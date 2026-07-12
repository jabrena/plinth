# analysis-design-agents Specification

## Purpose
TBD - created by archiving change add-analysis-design-agents. Update Purpose after archive.
## Requirements
### Requirement: Business analyst missions

`robot-business-analyst` SHALL own issue creation and read-only alignment review while remaining separate from technical implementation.

#### Scenario: Preserve business analyst responsibilities during role-boundary refactor

- **WHEN** planning and OpenSpec creation ownership moves from `robot-tech-lead` to `robot-architect`
- **THEN** `robot-business-analyst` continues owning issue quality, requirements traceability, read-only alignment review, and delivery-readiness review
- **AND** those responsibilities are not transferred to `robot-architect`
- **AND** `robot-business-analyst` remains separate from technical implementation

### Requirement: Architect missions

The embedded agent bundle SHALL include `robot-architect` with distinct missions for design exploration, architecture decision records, architecture diagrams, and pre-implementation planning/specification artifact creation.

#### Scenario: Move from exploration to implementation-ready artifacts

- **WHEN** a project user approves a design direction containing significant decisions and useful architecture views
- **THEN** `robot-architect` identifies the decisions suitable for ADRs
- **AND** it can create ADRs and diagrams through their associated skills
- **AND** it can create or refine implementation plans and OpenSpec changes with source artifact traceability
- **AND** it keeps exploration, decision recording, diagram generation, planning, and specification as distinct outputs
- **AND** it reports implementation constraints and handoff details for `@robot-tech-lead`

#### Scenario: Prepare delivery handoff

- **GIVEN** `robot-architect` has created or updated a plan or OpenSpec change
- **WHEN** the artifact is ready for implementation delivery
- **THEN** the architect output identifies the selected implementation plan or OpenSpec task list
- **AND** it preserves source traceability, architecture constraints, unresolved decisions, and validation expectations
- **AND** it does not delegate implementation, test, or verification work directly to coder agents

### Requirement: Tech lead role and migration

The embedded agent bundle MUST provide `robot-tech-lead` as the implementation-phase coordinator that preserves framework-aware coder delegation and starts from an approved implementation plan or OpenSpec task list.

#### Scenario: Reinstall agents after the boundary change

- **WHEN** an existing user installs the updated embedded agent bundle
- **THEN** the bundle provides `robot-tech-lead.md`
- **AND** the tech lead continues delegating Java, Spring Boot, Quarkus, Micronaut, and non-Java implementation work to the existing implementation agents
- **AND** the tech lead does not present plan creation or OpenSpec creation as primary missions

#### Scenario: Coordinate delivery from an approved execution artifact

- **GIVEN** an approved implementation plan or OpenSpec task list exists
- **WHEN** `robot-tech-lead` receives it for implementation
- **THEN** it treats the selected artifact as the execution contract
- **AND** it identifies the target framework from source artifacts before delegating implementation
- **AND** it tracks completion and verification against the selected execution artifact
- **AND** it reports delegation groups, task status, validation evidence, risks, blockers, and artifact authority

#### Scenario: Route pre-implementation requests to architect

- **GIVEN** a user asks `robot-tech-lead` to create a plan or OpenSpec change
- **WHEN** no approved execution artifact exists
- **THEN** the tech lead routes pre-implementation planning and specification work to `@robot-architect`
- **AND** it waits for an approved implementation plan or OpenSpec task list before coordinating delivery

### Requirement: No direct implementation by coordinating roles

`robot-business-analyst`, `robot-architect`, and `robot-tech-lead` MUST NOT implement application code as a substitute for the specialized coder agents.

#### Scenario: Architect prepares pre-implementation artifacts

- **WHEN** `robot-architect` creates or updates an implementation plan or OpenSpec change
- **THEN** it may shape requirements, tasks, constraints, validation expectations, and handoff details
- **AND** it does not implement application code, edit tests, run delivery verification as a substitute for developers, or delegate implementation directly to coder agents

#### Scenario: Tech lead delivers an approved plan

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


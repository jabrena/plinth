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

#### Scenario: Author OpenSpec artifacts without design refinement skills

- **GIVEN** a user asks `@robot-architect` to prepare OpenSpec artifacts before design refinement
- **WHEN** the agent follows the OpenSpec authoring step through `/create-spec`
- **THEN** it uses only `@041-planning-plan-mode` or `@042-planning-openspec` as appropriate
- **AND** it does not apply design skills `051`–`057`, `121`–`123`, or `130`
- **AND** it does not reference `034-architecture-design-exploration`
- **AND** it records source artifacts, derivation direction, assumptions, unresolved decisions, validation expectations, and handoff details for `@robot-tech-lead`

#### Scenario: Refine technical approach after initial specification

- **GIVEN** an initial OpenSpec change exists or an issue still has unresolved technical approaches
- **WHEN** the agent follows the design refinement step through `/explore-design`
- **THEN** it clarifies goals, constraints, assumptions, unknowns, and success criteria
- **AND** it compares feasible approaches and trade-offs
- **AND** it recommends a design direction with rationale
- **AND** it describes components, boundaries, interactions, data flow, failure handling, and testing strategy
- **AND** it identifies unresolved questions and ADR candidates
- **AND** it applies design skills `051`–`057`, `121`–`123`, and `130` in the prescribed order
- **AND** design refinement is not documented as the first workflow mission

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
- **AND** active agent sources are owned by `plinth-agents-generator`, not `plinth-skills-generator`

### Requirement: Tech lead non-Java routing

`robot-tech-lead` MUST route non-Java implementation work to `@robot-no-java` while preserving existing Java, Spring Boot, Quarkus, and Micronaut routing.

#### Scenario: Delegate a non-Java issue

- **GIVEN** an issue, plan, or OpenSpec task list names a non-Java stack or lacks Java/JVM scope
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-no-java`
- **AND** it does not delegate the work to `@robot-java-coder` by default
- **AND** agent markdown assets that define routing are owned by `plinth-agents-generator`

#### Scenario: Delegate plain Java work

- **GIVEN** an issue, plan, or OpenSpec task list uses Java, Maven, or framework-neutral JVM implementation
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-java-coder` unless Spring Boot, Quarkus, or Micronaut evidence selects a framework-specific coder
- **AND** agent markdown assets that define routing are owned by `plinth-agents-generator`

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


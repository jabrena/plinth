## MODIFIED Requirements

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

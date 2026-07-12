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
- **AND** it applies only `042-planning-openspec` when creating or updating OpenSpec artifacts
- **AND** it does not apply design skills `051`–`057`, `121`–`123`, `130`, or `034-architecture-design-exploration`
- **AND** it creates or updates OpenSpec artifacts only under `documentation/openspec` when edits are requested
- **AND** it validates OpenSpec structure before claiming the change is ready
- **AND** it reports changed files, validation evidence, source traceability, assumptions, and unresolved planning risks
- **AND** it documents `/create-spec` as the first workflow step before design refinement

### Requirement: Design exploration command routing

`/explore-design` SHALL route design refinement to `robot-architect` using design skills `051`–`057`, `121`–`123`, and `130` after initial specification, without implementing application code.

#### Scenario: Refine an OpenSpec change after create-spec

- **WHEN** a project user invokes `/explore-design` with an OpenSpec change that has unresolved technical approaches or design gaps
- **THEN** the command routes design refinement through `@robot-architect`
- **AND** it applies design skills `051-design-two-steps-methods`, `052-design-hamburger-method`, `053-design-simple-rules`, `054-design-tdd`, `055-design-parallel-change`, `056-design-avoid-breaking-changes`, `057-design-feature-toggles`, `121-java-object-oriented-design`, `122-java-type-design`, `123-java-design-patterns`, and `130-java-testing-strategies` when refining the approach
- **AND** it does not apply `042-planning-openspec` or `034-architecture-design-exploration`
- **AND** it reports alternatives, trade-offs, recommended design direction, ADR candidates, and unresolved questions
- **AND** it refines the existing OpenSpec change without replacing initial OpenSpec authoring owned by `/create-spec`
- **AND** it does not invoke implementation behavior

#### Scenario: Explore design options from an issue

- **WHEN** a project user invokes `/explore-design` with an issue or user story
- **THEN** the command accepts the issue as input
- **AND** it reports alternatives, trade-offs, recommended design direction, ADR candidates, and unresolved questions
- **AND** it does not apply `042-planning-openspec`

#### Scenario: Apply feature-toggle guidance during design refinement

- **WHEN** a project user invokes `/explore-design` on a change that needs rollout or rollback controls after initial specification
- **THEN** the command applies `057-design-feature-toggles` guidance as part of the approved design direction

#### Scenario: Document explore-design contract surfaces

- **WHEN** command source assets and generated command prompts for `/explore-design` are inspected
- **THEN** the purpose states that the command improves the technical approach for an issue or OpenSpec change after initial specification
- **AND** the usage is `/explore-design <issue|openspec-change>`
- **AND** accepted inputs list `OpenSpec change with unresolved technical approaches or design gaps` immediately after `Issue or user story`
- **AND** the command documents that it runs after `/create-spec`, not as the first workflow mission

## REMOVED Requirements

### Requirement: Architecture design exploration skill routing

**Reason**: Skill `034-architecture-design-exploration` overlaps the enriched `/explore-design` design skill set (`051`–`057`, `121`–`123`, `130`) and is retired physically in favor of explicit design-refinement routing.

**Migration**: Route design exploration and refinement through `/explore-design` and the enriched design skills. Remove `034` from command prompts, agent prompts, skill inventories, installer references, acceptance tests, and generated skill output.

#### Scenario: Retire skill 034 from active surfaces

- **WHEN** skill source files, skill registration, inventories, installer references, acceptance tests, and generated skill output are inspected after the change ships
- **THEN** `034-architecture-design-exploration` is absent from active sources and generated outputs
- **AND** no repository file still routes to or documents `034-architecture-design-exploration`
- **AND** `/explore-design` does not list `034` as an associated skill

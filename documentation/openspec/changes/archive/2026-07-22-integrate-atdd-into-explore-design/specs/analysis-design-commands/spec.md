## MODIFIED Requirements

### Requirement: Design exploration command routing

`/explore-design` SHALL route design refinement to `robot-architect` using design skills `051`–`057`, `121`–`123`, and `130` after initial specification, and MUST apply `059-design-atdd` as the final alignment gate when reviewable OpenSpec artifacts are provided, without implementing application code.

#### Scenario: Refine an OpenSpec change after create-spec

- **WHEN** a project user invokes `/explore-design` with an OpenSpec change that has unresolved technical approaches or design gaps
- **THEN** the command routes design refinement through `@robot-architect`
- **AND** it applies design skills `051-design-two-steps-methods`, `052-design-hamburger-method`, `053-design-simple-rules`, `054-design-tdd`, `055-design-parallel-change`, `056-design-avoid-breaking-changes`, `057-design-feature-toggles`, `121-java-object-oriented-design`, `122-java-type-design`, `123-java-design-patterns`, and `130-java-testing-strategies` when refining the approach
- **AND** it applies `059-design-atdd` after approved refinements are reflected in the reviewable OpenSpec artifacts and before requesting final design approval
- **AND** it reports the ATDD goal-to-criteria-to-task alignment outcome with evidence
- **AND** it does not apply `042-planning-openspec` or `034-architecture-design-exploration`
- **AND** it reports alternatives, trade-offs, recommended design direction, ADR candidates, and unresolved questions
- **AND** it refines the existing OpenSpec change without replacing initial OpenSpec authoring owned by `/create-spec`
- **AND** it does not invoke implementation behavior

#### Scenario: Continue to approval when ATDD alignment is ready

- **GIVEN** `/explore-design` has completed refinement of an OpenSpec change with an execution goal, acceptance criteria, and associated implementation and verification tasks
- **WHEN** `059-design-atdd` returns `ready`
- **THEN** the command includes the evidence-backed alignment result in its report
- **AND** it continues to the existing explicit design-approval step
- **AND** it does not treat the alignment review itself as maintainer approval

#### Scenario: Request changes when ATDD alignment is unresolved

- **GIVEN** `/explore-design` has completed refinement of an OpenSpec change with reviewable artifacts
- **WHEN** `059-design-atdd` returns `changes-requested` for at least one partial, missing, ambiguous, absent, or divergent finding
- **THEN** the command reports every unresolved finding and its available goal-to-criteria-to-task evidence
- **AND** it does not report the design direction or OpenSpec change as approved
- **AND** it does not silently add, remove, or rewrite the proposal, specification scenarios, or task checklist
- **AND** it asks the maintainer how the affected OpenSpec artifacts should be revised
- **AND** it reruns the alignment review after the maintainer-approved revisions are applied

#### Scenario: Explore design options from an issue

- **WHEN** a project user invokes `/explore-design` with an issue or user story
- **THEN** the command accepts the issue as input
- **AND** it reports alternatives, trade-offs, recommended design direction, ADR candidates, and unresolved questions
- **AND** it does not apply `059-design-atdd` unless a reviewable OpenSpec proposal, specification scenarios, and task checklist are also supplied
- **AND** it does not apply `042-planning-openspec`

#### Scenario: Apply feature-toggle guidance during design refinement

- **WHEN** a project user invokes `/explore-design` on a change that needs rollout or rollback controls after initial specification
- **THEN** the command applies `057-design-feature-toggles` guidance as part of the approved design direction

#### Scenario: Preserve functional and technical workflow boundaries

- **WHEN** `/explore-design` reviews and refines an OpenSpec change
- **THEN** initial OpenSpec proposal, design, specification, and task authoring remains owned by `/create-spec`
- **AND** user-story creation and Gherkin acceptance-file creation remain outside `/explore-design`
- **AND** the command does not modify source user stories, Gherkin files, ADRs, or application code

#### Scenario: Document explore-design contract surfaces

- **WHEN** command source assets and generated command prompts for `/explore-design` are inspected
- **THEN** the purpose states that the command improves the technical approach for an issue or OpenSpec change after initial specification
- **AND** the usage is `/explore-design <issue|openspec-change>`
- **AND** accepted inputs list `OpenSpec change with unresolved technical approaches or design gaps` immediately after `Issue or user story`
- **AND** the associated skills include `059-design-atdd` for the final OpenSpec alignment gate
- **AND** the command documents that it runs after `/create-spec`, not as the first workflow mission

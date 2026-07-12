# explore-design

## Purpose
Improve and refine the technical approach for an issue or OpenSpec change after initial specification—with alternatives, trade-offs, compatibility review, rollout controls, and verification strategy—before implementation.

## Usage
```text
/explore-design <issue|openspec-change>
```

## Accepted Inputs
- Issue or user story
- OpenSpec change with unresolved technical approaches or design gaps
- Existing architecture context and constraints
- Relevant requirements, ADRs, or technical notes linked to the issue or change

## Owning Agent
`@robot-architect`

## Associated Skills
- `051-design-two-steps-methods` for every refinement so preparatory work, behavior change, and verification remain explicitly sequenced
- `052-design-hamburger-method` when the scope is broad enough to need smallest-useful vertical slices
- `053-design-simple-rules` when alternatives need ordered design tradeoff evaluation before finalizing the direction
- `054-design-tdd` when testing-related requirements need test-first sequencing and verification-driven refinement
- `055-design-parallel-change` when database migration requirements need expand, migrate, contract sequencing
- `056-design-avoid-breaking-changes` when compatibility surfaces may change during refinement
- `057-design-feature-toggles` when rollout, rollback, compatibility windows, or temporary behavior controls affect the design direction
- `121-java-object-oriented-design`, then `122-java-type-design`, then `123-java-design-patterns` when Java responsibilities, type boundaries, or collaboration decisions affect the refined approach
- `130-java-testing-strategies` when testing strategy, boundary coverage, flakiness, or verification quality affect the refined approach

## Workflow position
Runs after `/create-spec`, not as the first mission in the workflow.

## Workflow
1. Clarify goals, constraints, assumptions, unknowns, and success criteria.
2. Compare feasible approaches and trade-offs.
3. Recommend a design direction with rationale.
4. Describe components, boundaries, interactions, data flow, failure handling, and testing strategy.
5. Identify unresolved questions and ADR candidates.
6. Refine the existing OpenSpec change when one is provided, without replacing initial OpenSpec authoring owned by `/create-spec`.
7. Request approval or revise the design direction.

## Output
- Approved design direction
- Alternative and trade-off analysis
- ADR candidates
- Open questions and assumptions
- Refined OpenSpec change artifacts when applicable

## Safeguards
- Do not implement application code.
- Do not hide material trade-offs or unresolved decisions.
- Do not treat the recommendation as approved until the user confirms it.
- Do not apply `042-planning-openspec`; initial OpenSpec authoring belongs to `/create-spec`.

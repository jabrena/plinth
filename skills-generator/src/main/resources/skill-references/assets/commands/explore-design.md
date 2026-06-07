# explore-design

## Purpose
Turn an unresolved issue into an approved technical design direction before planning or implementation.

## Usage
```text
/explore-design <issue-or-artifact>
```

## Accepted Inputs
- Issue or user story
- Existing architecture context and constraints
- Relevant requirements, ADRs, or technical notes

## Owning Agent
`@robot-architect`

## Associated Skill
`034-architecture-design-exploration`

## Workflow
1. Clarify goals, constraints, assumptions, unknowns, and success criteria.
2. Compare two or three feasible approaches and their trade-offs.
3. Recommend one design direction with rationale.
4. Describe components, interactions, data flow, failure handling, and testing strategy.
5. Identify ADR candidates and unresolved questions.
6. Request approval or revise the design direction.

## Output
- Approved design direction
- Alternative and trade-off analysis
- ADR candidates
- Open questions and assumptions

## Safeguards
- Do not implement application code.
- Do not hide material trade-offs or unresolved decisions.
- Do not treat the recommendation as approved until the user confirms it.

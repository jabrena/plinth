## MODIFIED Requirements

### Requirement: Integrated lifecycle documentation

The project SHALL document the analysis/design lifecycle across README and getting-started guidance after command, agent, and planning-skill changes are integrated.

#### Scenario: User selects a workflow path

- **WHEN** a project user reads the workflow guidance
- **THEN** the documentation explains optional feature-branch creation, worktree isolation for parallel changes, `/create-spec` as the first step for initial OpenSpec creation with `042-planning-openspec` only, `/explore-design` as the second step for technical approach refinement, ADRs/diagrams, alignment review, and tech-lead delivery
- **AND** it states that design refinement through `/explore-design` runs after `/create-spec`, not as the first workflow mission
- **AND** it does not require one mandatory plan/spec ordering beyond the documented create-spec-first path

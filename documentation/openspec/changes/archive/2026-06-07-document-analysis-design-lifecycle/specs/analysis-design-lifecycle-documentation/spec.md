## ADDED Requirements

### Requirement: Integrated lifecycle documentation

The project SHALL document the analysis/design lifecycle across README and getting-started guidance after command, agent, and planning-skill changes are integrated.

#### Scenario: User selects a workflow path

- **WHEN** a project user reads the workflow guidance
- **THEN** the documentation explains optional feature-branch creation, worktree isolation for parallel changes, design exploration, ADRs/diagrams, independent plan and OpenSpec creation, alignment review, and tech-lead delivery
- **AND** it does not require one mandatory plan/spec ordering

### Requirement: Agent migration documentation

The project MUST explain migration from `robot-coordinator` to `robot-tech-lead` in every affected language and inventory.

#### Scenario: Existing user upgrades the agent bundle

- **WHEN** an existing user reads the updated agent guidance
- **THEN** the documentation identifies the renamed file and direct mention
- **AND** it preserves the existing coder delegation model

### Requirement: Artifact authority documentation

The project SHALL document the authoritative concern of issues/stories, ADRs, OpenSpec specifications, implementation plans, and optional OpenSpec task tracking.

#### Scenario: Plan and OpenSpec disagree

- **WHEN** a user consults the workflow documentation after finding conflicting artifacts
- **THEN** the guidance directs the user to read-only alignment review
- **AND** it requires an explicit decision instead of silent synchronization

### Requirement: Integrated validation

The integrated change MUST validate XML-backed generation, local skills, Markdown, skill formatting, and all OpenSpec changes before promotion.

#### Scenario: Complete lifecycle documentation

- **WHEN** the prerequisite source changes and localized documentation are integrated
- **THEN** the relevant Maven verification, local generation, Markdown validator, skill-check, stale-reference checks, and `openspec validate --all` pass

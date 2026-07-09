# implement-spec-command Specification

## Purpose
Define the controlled implementation command for approved plans and validated OpenSpec task lists.
## Requirements
### Requirement: Spec implementation command

The command bundle SHALL provide `/implement-spec`, and the command MUST use `@robot-tech-lead` to coordinate delivery through the existing Java, Spring Boot, Quarkus, Micronaut, or non-Java coder agent.

#### Scenario: Install the implementation command

- **WHEN** a user installs the embedded command bundle
- **THEN** the bundle contains `implement-spec.md`
- **AND** it does not contain `implement.md`
- **AND** the inventory identifies `/implement-spec` as an implementation command

### Requirement: Executable artifact inputs

`/implement-spec` MUST accept an approved implementation plan or an OpenSpec change containing a validated `tasks.md` as its execution contract.

#### Scenario: Implement from an approved plan

- **WHEN** a user invokes `/implement-spec` with an approved implementation plan
- **THEN** `@robot-tech-lead` delegates implementation against the plan milestones and verification steps

#### Scenario: Implement from an OpenSpec change

- **WHEN** a user invokes `/implement-spec` with an OpenSpec change containing validated incomplete tasks
- **THEN** `@robot-tech-lead` delegates implementation against those tasks
- **AND** marks tasks complete only after acceptance criteria and focused checks pass

#### Scenario: Referenced issue has no executable artifact

- **WHEN** a user supplies only an issue and repository policy requires structured planning
- **THEN** the command stops implementation
- **AND** directs the user to create an approved plan or OpenSpec change first

### Requirement: Controlled delegation and verification

`/implement-spec` MUST require framework-aware coder routing, dependency and file-ownership controls, evidence-based completion, and focused validation reporting within the selected execution artifact.

#### Scenario: Delegate independent task groups

- **WHEN** the execution artifact contains independent groups without dependency or file-ownership conflicts
- **THEN** `@robot-tech-lead` may delegate them concurrently to the selected specialized coder
- **AND** integrates their results before final checks

#### Scenario: Stop on artifact conflict

- **WHEN** authoritative issue, ADR, specification, or plan content conflicts materially
- **THEN** implementation stops
- **AND** the conflict is routed to `/review-alignment`

#### Scenario: Complete implementation workflow

- **WHEN** delegated tasks pass their acceptance criteria and focused checks
- **THEN** `/implement-spec` reports changed files, test and build evidence, task status, blockers, and risks

#### Scenario: Select branch or worktree execution

- **WHEN** `/implement-spec` reviews the approved plan or OpenSpec task list
- **THEN** it decides whether the work should run on a feature branch or in one or more linked worktrees
- **AND** it uses `/create-feature-branch` for serial current-checkout work
- **AND** it uses `/create-worktree` when independent groups can run safely in parallel

### Requirement: Coder skill selection

All implementation coder agents MUST use explicit skill-selection precedence for overlapping implementation concerns.

#### Scenario: Select an error model

- **WHEN** a coder handles an expected domain outcome or composable failure
- **THEN** it prefers `@143-java-functional-exception-handling`
- **AND** it reserves `@126-java-exception-handling` for unexpected, infrastructure, resource, interruption, timeout, and framework-boundary failures
- **AND** it does not model the same failure with both approaches

#### Scenario: Select design guidance

- **WHEN** implementation requires design guidance
- **THEN** the coder applies `@121-java-object-oriented-design` for responsibilities and boundaries
- **AND** applies `@122-java-type-design` for domain types and signatures
- **AND** applies `@123-java-design-patterns` only for a demonstrated collaboration or integration problem
- **AND** uses `@142-java-functional-programming` within those boundaries when composition improves clarity

#### Scenario: Select persistence and contract guidance

- **WHEN** implementation includes relational persistence
- **THEN** the framework coder prefers its JDBC skill together with `@704-technologies-sql`
- **AND** uses repository or ORM abstractions only when they provide a clear benefit
- **AND** applies `@124-java-secure-coding` for general security concerns
- **AND** applies `@701-technologies-openapi` when an API contract is in scope
- **AND** applies `@705-technologies-nosql-mongodb` when MongoDB modeling or queries are in scope

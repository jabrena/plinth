# analysis-design-commands Specification

## Purpose
TBD - created by archiving change add-analysis-design-commands. Update Purpose after archive.
## Requirements
### Requirement: Analysis and design command bundle

The generator SHALL provide embedded command assets for `/create-worktree`, `/explore-design`, `/create-adr`, `/create-diagram`, `/create-spec`, and `/review-alignment`. The generator MUST NOT install or advertise `/review-breaking-changes`; breaking-change review is owned by `@056-design-avoid-breaking-changes`.

#### Scenario: Install the command bundle without the retired breaking-change command

- **WHEN** a user runs the embedded command installation workflow and selects a supported destination
- **THEN** the installer copies the supported analysis/design/support commands together with the existing commands
- **AND** the installer does not copy `.cursor/commands/review-breaking-changes.md`
- **AND** the command inventory does not list `/review-breaking-changes`
- **AND** planning documentation points users to `@056-design-avoid-breaking-changes` for breaking-change review

#### Scenario: Retire command source and validation coverage

- **WHEN** command source files, command registration, command inventories, command-focused tests, and command acceptance prompt inventories are inspected
- **THEN** `/review-breaking-changes` is absent from active command sources
- **AND** command-focused tests no longer assert the retired command contract
- **AND** skill-focused acceptance coverage exists for `056-design-avoid-breaking-changes`

#### Scenario: Update README discoverability

- **WHEN** `README.md`, `README_ES.md`, and `README_ZH.md` are inspected
- **THEN** their planning command lists do not include `/review-breaking-changes`
- **AND** their planning skill lists or equivalent discoverability sections include `056-design-avoid-breaking-changes`
- **AND** no README link points to `.cursor/commands/review-breaking-changes.md`

### Requirement: Command contracts

Each analysis/design command MUST document its purpose, accepted inputs, owning agent, associated skills or capabilities, workflow, outputs, and safeguards.

#### Scenario: Invoke an analysis or design command

- **WHEN** a project user opens an installed analysis/design command
- **THEN** the command identifies the artifacts it accepts and the artifact or report it produces
- **AND** it delegates substantive behavior to the named agent and skill rather than duplicating their complete prompts

### Requirement: Feature branch transition

The existing `/create-feature-branch` command SHALL support the optional transition from issue registration into repository-backed analysis, design, and implementation.

#### Scenario: Create a branch before design artifacts

- **WHEN** a user invokes `/create-feature-branch` with an issue/change identifier or an explicit supported branch type and description
- **THEN** the command verifies that the working tree is safe
- **AND** it creates a conventionally named local branch
- **AND** it allows plans, OpenSpec artifacts, ADRs, diagrams, and related documentation to be committed before application-code implementation
- **AND** it does not create a commit automatically

### Requirement: Isolated worktree creation

`/create-worktree` SHALL create a new branch and linked Git worktree for isolated or parallel work without changing the user's current checkout.

#### Scenario: Create a worktree for a child change

- **WHEN** a user invokes `/create-worktree` with an issue/change identifier or explicit branch details and an optional target path and base reference
- **THEN** the command verifies that the target branch and path are available
- **AND** it verifies that the selected base reference exists
- **AND** it creates the branch and linked checkout using a non-destructive Git worktree operation
- **AND** it reports the created branch, absolute path, and base reference
- **AND** it does not commit, push, remove a worktree, delete a branch, or use force

#### Scenario: Requested worktree conflicts with existing state

- **WHEN** the requested branch is already checked out, the target path exists, or the base reference is invalid
- **THEN** the command stops and reports the conflict
- **AND** it leaves existing branches, worktrees, and files unchanged

### Requirement: Design exploration command routing

`/explore-design` SHALL route design discovery to `robot-architect` and the design-discovery capability without implementing code.

#### Scenario: Explore an unresolved issue

- **WHEN** a project user invokes `/explore-design` with an issue that has unresolved technical approaches
- **THEN** the command requests an approved design direction, trade-off analysis, ADR candidates, and open questions
- **AND** it does not invoke implementation behavior

### Requirement: Read-only alignment command routing

`/review-alignment` MUST route the available issue/story, design, ADR, plan, and OpenSpec artifacts to `robot-business-analyst` for read-only review.

#### Scenario: Review a partial artifact set

- **WHEN** a user invokes `/review-alignment` without every artifact type
- **THEN** the command accepts the available artifacts
- **AND** it requests aligned areas, severity-ranked issues, open questions, recommended corrections, and readiness
- **AND** it does not request automatic artifact modification


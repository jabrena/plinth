## MODIFIED Requirements

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

## Source and Derivation

- Source artifact: GitHub issue [#954](https://github.com/jabrena/cursor-rules-java/issues/954).
- Related source artifact: GitHub issue [#886](https://github.com/jabrena/cursor-rules-java/issues/886).
- Derivation direction: command-to-skill conversion decision -> modified command bundle requirements -> command retirement and README update implementation.

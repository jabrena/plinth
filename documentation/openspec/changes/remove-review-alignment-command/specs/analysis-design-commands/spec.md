## MODIFIED Requirements

### Requirement: Analysis and design command bundle

The repository SHALL provide embedded command assets for `/create-worktree`, `/explore-design`, `/create-adr`, `/create-diagram`, and `/create-spec` through the `plinth-commands-generator` module. The generator MUST NOT install or advertise `/review-breaking-changes`; breaking-change review is owned by `@056-design-avoid-breaking-changes`. The generator MUST NOT install or advertise `/review-alignment`; that command is retired and its read-only review responsibility remains with `robot-business-analyst` without a dedicated command contract.

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
- **AND** active command sources are owned by `plinth-commands-generator`, not `plinth-skills-generator`

#### Scenario: Update README discoverability

- **WHEN** `README.md`, `README_ES.md`, and `README_ZH.md` are inspected
- **THEN** their planning command lists do not include `/review-breaking-changes`
- **AND** their planning skill lists or equivalent discoverability sections include `056-design-avoid-breaking-changes`
- **AND** no README link points to `.cursor/commands/review-breaking-changes.md`

#### Scenario: Retire the review-alignment command source and validation coverage

- **WHEN** command source files, command registration, command inventories, command-focused tests, and command acceptance prompt inventories are inspected
- **THEN** `/review-alignment` is absent from active command sources
- **AND** command-focused tests no longer assert the retired `/review-alignment` command contract
- **AND** the skills pipeline no longer bundles `/review-alignment` installation assets
- **AND** `GETTING-STARTED-WORKFLOWS.md`, `GETTING-STARTED-AGENTS.md`, `INVENTORY-COMMANDS-JAVA.md`, and their `_ES`/`_ZH` variants no longer describe `/review-alignment` as an available command

## REMOVED Requirements

### Requirement: Read-only alignment command routing

**Reason**: `/review-alignment` is not part of the confirmed planning workflow (`/update-issue`, `/explore-problem`, `/create-acceptance-criteria`, `/create-spec`, `/explore-design`) per GitHub issue #1084, and is retired without a replacement command.

**Migration**: Read-only alignment and readiness review is no longer available as a dedicated command; `implement-spec-command`'s artifact-conflict scenario now routes to `robot-business-analyst` directly instead of to `/review-alignment`.

`/review-alignment` MUST route the available issue/story, design, ADR, plan, and OpenSpec artifacts to `robot-business-analyst` for read-only review.

#### Scenario: Review a partial artifact set

- **WHEN** a user invokes `/review-alignment` without every artifact type
- **THEN** the command accepts the available artifacts
- **AND** it requests aligned areas, severity-ranked issues, open questions, recommended corrections, and readiness
- **AND** it does not request automatic artifact modification

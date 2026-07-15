# close-spec-command Specification

## Purpose
TBD - created by archiving change add-close-spec-command. Update Purpose after archive.
## Requirements
### Requirement: Close OpenSpec change command

The command bundle SHALL provide a `/close-spec` command that archives an OpenSpec change by name using the OpenSpec CLI.

#### Scenario: Archive an OpenSpec change by name

- **GIVEN** a user is in a Plinth repository with OpenSpec configured under `documentation/openspec/`
- **AND** the OpenSpec CLI is available
- **WHEN** the user invokes `/close-spec <change-name>`
- **THEN** the command executes the equivalent of `openspec archive <change-name>` from the `documentation/` working directory
- **AND** reports a success message that includes `<change-name>`

### Requirement: Validate required argument

`/close-spec` MUST require a single `<change-name>` argument and MUST present usage guidance when it is missing.

#### Scenario: Missing change name

- **WHEN** the user invokes `/close-spec` without arguments
- **THEN** the command fails fast
- **AND** prints usage guidance including `/close-spec <change-name>`

### Requirement: Fail gracefully when change is unknown

`/close-spec` MUST fail gracefully when the provided change does not exist.

#### Scenario: Change does not exist

- **WHEN** the user invokes `/close-spec <change-name>` and `<change-name>` does not exist
- **THEN** the command reports that the change was not found
- **AND** does not claim success

### Requirement: Fail gracefully when OpenSpec is unavailable

`/close-spec` MUST fail gracefully when the OpenSpec CLI cannot be executed.

#### Scenario: OpenSpec CLI missing

- **GIVEN** the OpenSpec CLI cannot be executed in the current environment
- **WHEN** the user invokes `/close-spec <change-name>`
- **THEN** the command reports the missing prerequisite
- **AND** provides a short hint for installing/enabling OpenSpec


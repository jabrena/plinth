## ADDED Requirements

### Requirement: Explicit Markdown validator composition boundary

The Markdown validator module MUST model executable startup and object wiring as an explicit composition boundary instead of hiding concrete dependency wiring through dependency-specific architecture-test exceptions.

#### Scenario: CLI command does not construct outbound adapters

- **GIVEN** the Markdown validator CLI command handles Picocli parsing and command execution
- **WHEN** maintainers inspect its production dependencies
- **THEN** it does not instantiate `FileSystemMarkdownFileFinder`
- **AND** it does not instantiate `HttpClientRemoteLinkRequester`
- **AND** it receives application behavior through constructor-injected dependencies

#### Scenario: Startup wiring is explicit

- **GIVEN** the Markdown validator executable entry point starts the application
- **WHEN** it creates the CLI command and application services
- **THEN** concrete inbound and outbound adapter wiring is owned by a named composition or bootstrap boundary
- **AND** that boundary is represented explicitly in the architecture test
- **AND** the supported `MarkdownValidator` main class and JBang-compatible entry point remain available

### Requirement: Architecture test without dependency-specific ignores

The Markdown validator architecture test MUST enforce the module boundaries without the current dependency-specific `ignoreDependency(...)` entries.

#### Scenario: Onion boundaries pass without current ignores

- **GIVEN** the Markdown validator ArchUnit test is executed
- **WHEN** the architecture rule checks domain, application, inbound adapter, outbound adapter, and composition boundaries
- **THEN** the rule passes without ignoring the dependency from `MarkdownValidator` to `MarkdownValidatorCommand`
- **AND** the rule passes without ignoring the dependency from `MarkdownValidatorCommand` to `FileSystemMarkdownFileFinder`
- **AND** the rule passes without ignoring the dependency from `MarkdownValidatorCommand` to `HttpClientRemoteLinkRequester`

#### Scenario: Application layer remains adapter independent

- **GIVEN** the Markdown validator application package contains validation services and ports
- **WHEN** architecture tests inspect dependencies from `info.jab.markdownvalidator.application..`
- **THEN** application classes do not depend on `info.jab.markdownvalidator.adapter..`
- **AND** this constraint remains enforced after the composition boundary is introduced

### Requirement: CLI behavior remains compatible

The architecture cleanup MUST preserve the existing Markdown validator command-line behavior.

#### Scenario: Command behavior is preserved after refactoring

- **GIVEN** contributors run the supported Markdown validator command
- **WHEN** the composition boundary refactoring is complete
- **THEN** command options, default target directories, root directory handling, validation responsibilities, and success or failure exit-code semantics remain compatible with the existing tests
- **AND** focused tests verify the behavior before the architecture exception removal is promoted

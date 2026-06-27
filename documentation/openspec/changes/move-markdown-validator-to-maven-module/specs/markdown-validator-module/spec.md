## ADDED Requirements

### Requirement: Dedicated Markdown validator Maven module

The repository MUST provide a dedicated Maven module for the Markdown validator implementation.

#### Scenario: Register the validator module in Maven

- **GIVEN** the repository root `pom.xml` defines the Maven module list
- **WHEN** maintainers implement the Markdown validator module
- **THEN** the root build registers the new validator module
- **AND** the module can be built and tested through Maven

#### Scenario: Move validator behavior into the Maven module

- **GIVEN** the current validator implementation lives in `.github/scripts/MarkdownValidator.java`
- **WHEN** the validator module is implemented
- **THEN** the validator behavior is owned by the Maven module
- **AND** `markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java` is the module main class
- **AND** `.github/scripts/MarkdownValidator.java` is no longer required

### Requirement: Compatibility with existing validator behavior

The module extraction MUST preserve the existing validator command-line behavior before performance changes are introduced.

#### Scenario: Preserve command options and defaults

- **GIVEN** contributors use the current Markdown validator command-line interface
- **WHEN** the implementation is moved into the Maven module
- **THEN** the validator still supports verbose mode, fail-fast mode, configurable target directories, and a root directory parameter
- **AND** the default target directories remain compatible with the existing script defaults
- **AND** success and failure exit-code semantics remain compatible with the existing script

#### Scenario: Preserve validation responsibilities

- **GIVEN** the current validator parses Markdown, renders Markdown to HTML, and checks remote HTTP and HTTPS links
- **WHEN** the implementation is moved into the Maven module
- **THEN** those validation responsibilities continue to be exercised
- **AND** local, fragment-only, blank, and non-HTTP link destinations continue to be ignored for remote-link checking
- **AND** HTTP timeout and interruption behavior remain handled safely

### Requirement: JBang-compatible entry point

The repository MUST keep a JBang-compatible way to run Markdown validation after the Maven module is introduced.

#### Scenario: Run validator through the module main source

- **GIVEN** contributors and CI need a JBang-compatible Markdown validation command
- **WHEN** the validator implementation is moved into the Maven module
- **THEN** they can run `jbang markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java --verbose .`
- **AND** that entry point is the Maven module main class

### Requirement: CI Markdown validation integration

The GitHub Actions Markdown validation job MUST use the supported validator entry point.

#### Scenario: Validate Markdown in CI

- **GIVEN** `.github/workflows/maven.yaml` runs Markdown validation when Markdown files change
- **WHEN** the validator module is implemented
- **THEN** the `validate-markdown` job invokes the supported validator entry point
- **AND** Markdown validation failures still fail the job

### Requirement: Faster and deterministic validation

The validator redesign MUST preserve deterministic results while reducing avoidable sequential validation work.

#### Scenario: Capture local runtime before implementation changes

- **GIVEN** maintainers are about to change the Markdown validator implementation
- **WHEN** implementation work begins
- **THEN** they first run the current local validator command and record elapsed time
- **AND** they record the command used, relevant environment notes, and any network caveats that could affect remote-link checks

#### Scenario: Parallelize safe validation work

- **GIVEN** Markdown files can be validated independently
- **WHEN** the validator runs across multiple Markdown files
- **THEN** file validation work is parallelized where safe
- **AND** remote-link checks reuse shared cache results for duplicate URLs
- **AND** reported validation errors are aggregated deterministically

#### Scenario: Consider structural concurrency

- **GIVEN** Java 25 is the repository baseline
- **WHEN** parallel validation is implemented
- **THEN** the design considers Java structural concurrency for task coordination, cancellation, and failure handling
- **AND** any chosen concurrency strategy uses bounded execution rather than unbounded task creation

#### Scenario: Verify runtime improvement

- **GIVEN** maintainers recorded a local runtime baseline before changing the validator implementation
- **WHEN** the validator redesign is complete
- **THEN** they run the repository Markdown validation command with the same local timing approach
- **AND** they compare the post-change local runtime with the pre-change local baseline
- **AND** they compare runtime against the issue #941 CI baseline where practical

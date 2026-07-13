## ADDED Requirements

### Requirement: Dedicated commands Maven module

The repository MUST provide a dedicated `plinth-commands-generator` Maven module that owns embedded command inventory, markdown assets, loader code, and command-focused tests.

#### Scenario: Register the commands module in Maven

- **GIVEN** the repository root `pom.xml` defines the Maven module list
- **WHEN** maintainers implement the commands module extraction
- **THEN** the root build registers `plinth-commands-generator`
- **AND** `./mvnw clean verify -pl plinth-commands-generator` passes

#### Scenario: Relocate command sources out of plinth-skills-generator

- **GIVEN** command sources currently live under `plinth-skills-generator`
- **WHEN** the commands module is implemented
- **THEN** `commands.xml` is owned by `plinth-commands-generator`
- **AND** `CommandIndexes.java` is owned by `plinth-commands-generator`
- **AND** `InventoryXmlLoader.java` is owned by `plinth-commands-generator`
- **AND** the 11 command markdown assets listed in `commands.xml` are owned by `plinth-commands-generator`
- **AND** `CommandIndexesTest` is owned by `plinth-commands-generator`
- **AND** command Gherkin features are owned by `plinth-commands-generator`
- **AND** `CommandIndexesTest` no longer contains a TODO to move to `plinth-commands-generator`

### Requirement: Command inventory integrity

The commands module MUST preserve the embedded command bundle declared by `commands.xml`.

#### Scenario: Preserve installation order and uniqueness

- **GIVEN** `commands.xml` defines the embedded command bundle
- **WHEN** command inventory tests run in `plinth-commands-generator`
- **THEN** every listed command asset exists
- **AND** command file names are unique
- **AND** installation order matches `commands.xml`

#### Scenario: Preserve per-command routing contracts

- **GIVEN** command markdown assets define purpose, usage, owning agent, and safeguards
- **WHEN** command contract tests run in `plinth-commands-generator`
- **THEN** routing and contract assertions for the embedded command bundle continue to pass after relocation

### Requirement: Skills-generator bridge at generate-resources

`plinth-skills-generator` MUST consume `plinth-commands-generator` output through a one-way Maven dependency and a `generate-resources` bridge.

#### Scenario: Declare one-way module dependency

- **GIVEN** `plinth-skills-generator` generates skills that embed command content
- **WHEN** the bridge is implemented
- **THEN** `plinth-skills-generator` declares a Maven dependency on `plinth-commands-generator`
- **AND** `plinth-commands-generator` does not declare a dependency on `plinth-skills-generator`

#### Scenario: Reuse InventoryXmlLoader without duplication

- **GIVEN** `InventoryXmlLoader` provides safe XML parser configuration for inventory documents
- **WHEN** command sources move into `plinth-commands-generator`
- **THEN** `InventoryXmlLoader` is owned by `plinth-commands-generator`
- **AND** `plinth-skills-generator` reuses it through the module dependency
- **AND** the repository does not keep duplicate `InventoryXmlLoader` implementations
- **AND** no additional shared-library module is introduced for this helper

#### Scenario: Stage command assets before skill generation

- **GIVEN** skills `001-commands-inventory` and `004-commands-installation` resolve command assets through XInclude during generation
- **WHEN** `plinth-skills-generator` runs `generate-resources`
- **THEN** command assets from `plinth-commands-generator` are unpacked or staged into the paths expected by skill XML (for example `skill-references/assets/commands/`)
- **AND** skill generation does not depend on stale command copies left in `plinth-skills-generator` source tree
- **AND** staged command assets are build output only and are not committed under `plinth-skills-generator/src/main/resources`

#### Scenario: Split command and skill validation responsibilities

- **GIVEN** `004-commands-installation.xml` remains owned by `plinth-skills-generator`
- **WHEN** command validation is implemented after extraction
- **THEN** `plinth-commands-generator` owns manifest, asset presence, and per-command contract tests
- **AND** `plinth-skills-generator` owns installer XInclude parity, bridge staging, and generated-skill propagation tests

### Requirement: Command-to-skill propagation verification

The build MUST prove that bridged command assets from `plinth-commands-generator` reach generated skills for `001-commands-inventory` and `004-commands-installation`.

#### Scenario: Generated 004 skill embeds command bodies

- **GIVEN** `./mvnw clean install -pl plinth-skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/004-commands-installation` is inspected
- **THEN** `references/004-commands-installation.md` contains embedded markdown blocks sourced from `plinth-commands-generator` command assets
- **AND** identifiable markers from commands listed in `commands.xml` are present (for example `/update-issue`, `/create-spec`, `/implement-spec`, `/benchmark`)

#### Scenario: Generated 001 skill lists the command inventory

- **GIVEN** `./mvnw clean install -pl plinth-skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/001-commands-inventory` is inspected
- **THEN** `references/001-commands-inventory.md` lists every command row corresponding to `commands.xml`
- **AND** no command asset from `plinth-commands-generator` is missing from the generated `001` or `004` skill references

#### Scenario: Automated guard fails when bridge breaks

- **GIVEN** `plinth-skills-generator` has an integration or generator test for command-backed skills
- **WHEN** `./mvnw clean verify -pl plinth-skills-generator -am` is executed
- **THEN** the test asserts generated references for `001` and `004` contain content from staged `plinth-commands-generator` assets
- **AND** the test fails when a command asset is removed, renamed, or not staged into skill generation

### Requirement: Contributor documentation for bridged builds

Contributor docs MUST document isolated and integrated Maven commands for the new module boundary.

#### Scenario: Document module commands

- **GIVEN** `DEVELOPER.md` documents reactor modules and per-module `./mvnw` commands
- **WHEN** the commands module is added
- **THEN** `DEVELOPER.md` includes a `plinth-commands-generator` submodule row with `./mvnw clean verify -pl plinth-commands-generator`
- **AND** contributor docs document `./mvnw clean install -pl plinth-skills-generator -am` as the command to regenerate local skills from the `plinth-commands-generator` outcome
- **AND** `AGENTS.md` file-structure guidance points command sources to `plinth-commands-generator`

## Source and Derivation

- Source artifact: GitHub issue [#1035](https://github.com/jabrena/plinth/issues/1035).
- Derivation direction: issue #1035 -> `plinth-commands-generator-module` requirements -> Maven module and bridge implementation.

## ADDED Requirements

### Requirement: Dedicated agents Maven module

The repository MUST provide a dedicated `agents-generator` Maven module that owns embedded agent inventory, markdown assets, loader code, and agent-focused tests.

#### Scenario: Register the agents module in Maven

- **GIVEN** the repository root `pom.xml` defines the Maven module list
- **WHEN** maintainers implement the agents module extraction
- **THEN** the root build registers `agents-generator`
- **AND** `./mvnw clean verify -pl agents-generator` passes

#### Scenario: Relocate agent sources out of skills-generator

- **GIVEN** agent sources currently live under `skills-generator`
- **WHEN** the agents module is implemented
- **THEN** `agents.xml` is owned by `agents-generator`
- **AND** `AgentIndexes.java` is owned by `agents-generator`
- **AND** the nine agent markdown assets embedded by `005-agents-installation.xml` are owned by `agents-generator`
- **AND** `java-agents-inventory-template.md` is owned by `agents-generator`
- **AND** agent bundle tests relocated from `SkillsGeneratorTest.EmbeddedAgentBundleTests` are owned by `agents-generator`
- **AND** agent Gherkin features are owned by `agents-generator`
- **AND** `SkillsGeneratorTest` no longer contains a TODO to move `EmbeddedAgentBundleTests` to `agents-generator`

### Requirement: Agent inventory integrity

The agents module MUST preserve the embedded agent bundle declared by `agents.xml`.

#### Scenario: Preserve installation order and uniqueness

- **GIVEN** `agents.xml` defines the embedded agent bundle in installation order
- **WHEN** agent inventory tests run in `agents-generator`
- **THEN** every listed agent asset exists
- **AND** agent file names are unique
- **AND** installation order matches `agents.xml`
- **AND** the bundle contains exactly nine agents:
  - `robot-business-analyst.md`
  - `robot-architect.md`
  - `robot-tech-lead.md`
  - `robot-no-java.md`
  - `robot-java-performance.md`
  - `robot-java-coder.md`
  - `robot-java-micronaut-coder.md`
  - `robot-java-quarkus-coder.md`
  - `robot-java-spring-boot-coder.md`

#### Scenario: Preserve per-agent routing and delegation contracts

- **GIVEN** agent markdown assets define missions, routing, skill precedence, and safeguards
- **WHEN** agent contract tests run in `agents-generator`
- **THEN** installer/inventory parity, tech-lead routing, coder skill precedence, framework JDBC preferences, and nine-agent bundle assertions continue to pass after relocation

### Requirement: Agents-generator dependency on commands-generator

`agents-generator` MUST reuse `InventoryXmlLoader` from `commands-generator` through a one-way Maven dependency.

#### Scenario: Declare one-way helper dependency

- **GIVEN** `InventoryXmlLoader` provides safe XML parser configuration for inventory documents
- **WHEN** agent sources are implemented in `agents-generator`
- **THEN** `agents-generator` declares a Maven dependency on `commands-generator`
- **AND** `agents-generator` reuses `InventoryXmlLoader` without duplicating the helper
- **AND** `commands-generator` does not declare a dependency on `agents-generator`
- **AND** no additional shared-library module is introduced for this helper

### Requirement: Skills-generator bridge at generate-resources

`skills-generator` MUST consume `agents-generator` output through a one-way Maven dependency and a `generate-resources` bridge.

#### Scenario: Declare one-way module dependency

- **GIVEN** `skills-generator` generates skills that embed agent content
- **WHEN** the bridge is implemented
- **THEN** `skills-generator` declares a Maven dependency on `agents-generator`
- **AND** `agents-generator` does not declare a dependency on `skills-generator`

#### Scenario: Stage agent assets before skill generation

- **GIVEN** skills `002-agents-inventory` and `005-agents-installation` resolve agent assets through XInclude during generation
- **WHEN** `skills-generator` runs `generate-resources`
- **THEN** agent assets from `agents-generator` are unpacked or staged into the paths expected by skill XML (for example `skill-references/assets/agents/`)
- **AND** `java-agents-inventory-template.md` is staged into `skill-references/assets/`
- **AND** skill generation does not depend on stale agent copies left in `skills-generator` source tree
- **AND** staged agent assets are build output only and are not committed under `skills-generator/src/main/resources`

#### Scenario: Split agent and skill validation responsibilities

- **GIVEN** `005-agents-installation.xml` remains owned by `skills-generator`
- **WHEN** agent validation is implemented after extraction
- **THEN** `agents-generator` owns manifest, asset presence, and per-agent contract tests
- **AND** `skills-generator` owns installer XInclude parity, bridge staging, and generated-skill propagation tests

### Requirement: Agent-to-skill propagation verification

The build MUST prove that bridged agent assets from `agents-generator` reach generated skills for `002-agents-inventory` and `005-agents-installation`.

#### Scenario: Generated 005 skill embeds agent bodies

- **GIVEN** `./mvnw clean install -pl skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/005-agents-installation` is inspected
- **THEN** `references/005-agents-installation.md` contains embedded markdown blocks sourced from `agents-generator` agent assets
- **AND** identifiable markers from agents listed in `agents.xml` are present (for example `name: robot-architect`, `name: robot-tech-lead`, `name: robot-no-java`, `name: robot-java-performance`)

#### Scenario: Generated 002 skill lists the agent inventory

- **GIVEN** `./mvnw clean install -pl skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/002-agents-inventory` is inspected
- **THEN** `references/002-agents-inventory.md` lists every agent row corresponding to `agents.xml`
- **AND** no agent asset from `agents-generator` is missing from the generated `002` or `005` skill references

#### Scenario: Automated guard fails when bridge breaks

- **GIVEN** `skills-generator` has an integration or generator test for agent-backed skills
- **WHEN** `./mvnw clean verify -pl skills-generator -am` is executed
- **THEN** the test asserts generated references for `002` and `005` contain content from staged `agents-generator` assets
- **AND** the test fails when an agent asset is removed, renamed, or not staged into skill generation

### Requirement: Contributor documentation for bridged builds

Contributor docs MUST document isolated and integrated Maven commands for the new module boundary.

#### Scenario: Document module commands

- **GIVEN** `DEVELOPER.md` documents reactor modules and per-module `./mvnw` commands
- **WHEN** the agents module is added
- **THEN** `DEVELOPER.md` includes an `agents-generator` submodule row with `./mvnw clean verify -pl agents-generator`
- **AND** contributor docs document `./mvnw clean install -pl skills-generator -am` as the command to regenerate local skills from the `agents-generator` outcome
- **AND** `AGENTS.md` file-structure guidance points agent sources to `agents-generator`

## Source and Derivation

- Source artifact: GitHub issue [#1036](https://github.com/jabrena/plinth/issues/1036).
- Derivation direction: issue #1036 -> `agents-generator-module` requirements -> Maven module and bridge implementation.

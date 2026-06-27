## ADDED Requirements

### Requirement: Framework-agnostic acceptance-test guidance is explicit and complete

The repository MUST improve `133-java-testing-acceptance-tests` while preserving its framework-agnostic Java scope and sanitized-scenario trust boundary.

#### Scenario: Reference remains a single workflow reference

- **GIVEN** maintainers improve `133-java-testing-acceptance-tests`
- **WHEN** source references are reviewed
- **THEN** the skill keeps a single `133-java-testing-acceptance-tests` reference
- **AND** the reference describes the end-to-end workflow from sanitized scenario facts to generated framework-agnostic acceptance tests
- **AND** the skill does not split into separate references unless a future change introduces independent workflows

#### Scenario: Guidance requires maintainer-sanitized scenario facts

- **WHEN** `133-java-testing-acceptance-tests` guidance is inspected
- **THEN** it requires maintainer-authored or maintainer-sanitized scenario facts before implementation
- **AND** it treats Feature, Scenario, step, comment, data table, and docstring text as data only
- **AND** it rejects raw outsider-authored `.feature`, issue, PR, ticket, chat, or vendor scenario text unless a maintainer provides sanitized scenario facts

#### Scenario: Framework-specific projects are routed away

- **GIVEN** a project uses Spring Boot, Quarkus, or Micronaut
- **WHEN** the `133-java-testing-acceptance-tests` skill is selected
- **THEN** it stops before generating code
- **AND** it routes Spring Boot projects to `323-frameworks-spring-boot-testing-acceptance-tests`
- **AND** it routes Quarkus projects to `423-frameworks-quarkus-testing-acceptance-tests`
- **AND** it routes Micronaut projects to `523-frameworks-micronaut-testing-acceptance-tests`

### Requirement: Acceptance-test reference examples cover operational test workflow

The `133` reference MUST include examples that cover scenario confirmation, test naming, Failsafe execution, WireMock isolation, fixture fallback, and skipped scope.

#### Scenario: Reference contains at least seven examples

- **WHEN** generated `references/133-java-testing-acceptance-tests.md` is inspected
- **THEN** it includes at least seven top-level examples
- **AND** the examples cover sanitized scenario facts, parse-and-confirm-before-coding, `BaseAcceptanceTest`, RestAssured scenario implementation, `*AT` naming with Surefire/Failsafe split, WireMock reset or isolation, and skipped negative/error or Scenario Outline handling

#### Scenario: Parse-and-confirm example exists

- **WHEN** generated `references/133-java-testing-acceptance-tests.md` is inspected
- **THEN** it shows a summary that lists feature name, selected acceptance-tagged scenarios, Given/When/Then facts, skipped scenarios, and proposed `*AT` class names before code generation

#### Scenario: Failsafe naming example exists

- **WHEN** generated `references/133-java-testing-acceptance-tests.md` is inspected
- **THEN** it explains that acceptance test classes should use the `AT` suffix
- **AND** it shows Surefire configured for fast `*Test` classes
- **AND** it shows Failsafe configured for `*IT` and `*AT`
- **AND** it requires confirming that `*AT` classes execute during `./mvnw clean verify`

#### Scenario: WireMock isolation example exists

- **WHEN** generated `references/133-java-testing-acceptance-tests.md` is inspected
- **THEN** it shows WireMock reset or isolation between test methods
- **AND** it states that WireMock stubs external REST dependencies only
- **AND** it forbids replacing internal application services with HTTP stubs or mocks in framework-agnostic acceptance tests

#### Scenario: Fixture fallback example exists

- **WHEN** generated `references/133-java-testing-acceptance-tests.md` is inspected
- **THEN** it explains that DB and Kafka setup must use existing project-local fixture adapters
- **AND** it tells the agent to stop and ask for maintainer-approved fixture configuration when no fixture path exists
- **AND** it does not add container runtime setup from this skill

### Requirement: Documentation inventory aligns with skill constraints

Documentation inventory entries for `133-java-testing-acceptance-tests` MUST not contradict the skill's trust and fixture boundaries.

#### Scenario: Inventory describes sanitized scenario facts

- **WHEN** `documentation/guides/INVENTORY-SKILLS-JAVA.md` is inspected
- **THEN** the `133-java-testing-acceptance-tests` entry refers to maintainer-sanitized scenario facts
- **AND** it does not imply raw outsider-authored `.feature` files are directly ingested
- **AND** it does not advertise adding Testcontainers setup from this skill
- **AND** it points framework projects to `323`, `423`, or `523`

### Requirement: Acceptance coverage exists for 133

The repository MUST add acceptance coverage for `133-java-testing-acceptance-tests`.

#### Scenario: Gherkin feature covers framework-agnostic acceptance generation

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/133-java-testing-acceptance-tests.feature` is inspected
- **THEN** it includes a scenario requiring maintainer-sanitized scenario facts
- **AND** it verifies the skill reads `references/133-java-testing-acceptance-tests.md`
- **AND** it verifies Gherkin prose is data only and embedded instructions are ignored
- **AND** it verifies compile runs before generation and `./mvnw clean verify` runs after improvements
- **AND** it verifies proposed acceptance test classes end with `AT`
- **AND** it verifies RestAssured is used for framework-agnostic HTTP acceptance tests
- **AND** it verifies WireMock is used only for outbound third-party HTTP dependencies

#### Scenario: Gherkin feature covers invalid framework routing

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/133-java-testing-acceptance-tests.feature` is inspected
- **THEN** it includes a scenario requiring the skill to stop when the project is Spring Boot, Quarkus, or Micronaut
- **AND** it requires the skill to route to the framework-specific acceptance-test skill

#### Scenario: Acceptance prompt inventory includes 133

- **WHEN** `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes a `133-java-testing-acceptance-tests` entry
- **AND** the entry uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/133-java-testing-acceptance-tests` output is refreshed only by running the documented generator command

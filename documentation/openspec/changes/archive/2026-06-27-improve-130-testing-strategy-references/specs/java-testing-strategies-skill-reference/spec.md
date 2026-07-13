## ADDED Requirements

### Requirement: Testing strategy references are split by technique

The repository MUST split `130-java-testing-strategies` detailed guidance into one focused reference per technique: RIGHT-BICEP, A-TRIP, and CORRECT.

#### Scenario: RIGHT-BICEP reference is dedicated

- **GIVEN** maintainers implement the `130-java-testing-strategies` reference split
- **WHEN** generated local skill output is inspected
- **THEN** `.agents/skills/130-java-testing-strategies/references/130-java-testing-strategies-right-bicep.md` exists
- **AND** it explains RIGHT-BICEP as the technique for deciding what behavior and risks to test
- **AND** it covers right results, boundary conditions, inverse relationships, cross-checks, error conditions, and performance guardrails

#### Scenario: A-TRIP reference is dedicated

- **GIVEN** maintainers implement the `130-java-testing-strategies` reference split
- **WHEN** generated local skill output is inspected
- **THEN** `.agents/skills/130-java-testing-strategies/references/130-java-testing-strategies-a-trip.md` exists
- **AND** it explains A-TRIP as the technique for evaluating test quality
- **AND** it covers automatic, thorough, repeatable, independent, and professional tests

#### Scenario: CORRECT reference is dedicated

- **GIVEN** maintainers implement the `130-java-testing-strategies` reference split
- **WHEN** generated local skill output is inspected
- **THEN** `.agents/skills/130-java-testing-strategies/references/130-java-testing-strategies-correct.md` exists
- **AND** it explains CORRECT as the technique for boundary-condition analysis
- **AND** it covers conformance, ordering, range, reference, existence, cardinality, and time

### Requirement: Skill workflow routes to the relevant testing strategy reference

The `130-java-testing-strategies` generated skill MUST route agents to focused references based on the user's testing concern.

#### Scenario: Narrow RIGHT-BICEP request reads only RIGHT-BICEP by default

- **GIVEN** a user asks what tests are missing or asks to apply RIGHT-BICEP
- **WHEN** the `130-java-testing-strategies` skill is applied
- **THEN** the workflow tells the agent to read `references/130-java-testing-strategies-right-bicep.md`
- **AND** it does not require reading A-TRIP or CORRECT unless the request also involves test quality or boundary conditions

#### Scenario: Narrow A-TRIP request reads only A-TRIP by default

- **GIVEN** a user asks why tests are flaky, brittle, manual, order-dependent, or hard to maintain
- **WHEN** the `130-java-testing-strategies` skill is applied
- **THEN** the workflow tells the agent to read `references/130-java-testing-strategies-a-trip.md`
- **AND** it does not require reading RIGHT-BICEP or CORRECT unless the request also involves coverage strategy or boundary conditions

#### Scenario: Narrow CORRECT request reads only CORRECT by default

- **GIVEN** a user asks for boundary-condition testing or CORRECT analysis
- **WHEN** the `130-java-testing-strategies` skill is applied
- **THEN** the workflow tells the agent to read `references/130-java-testing-strategies-correct.md`
- **AND** it does not require reading RIGHT-BICEP or A-TRIP unless the request also involves coverage strategy or test quality

#### Scenario: Broad test strategy review may combine all references

- **GIVEN** a user asks for a broad test strategy review
- **WHEN** the `130-java-testing-strategies` skill is applied
- **THEN** the workflow allows the agent to combine RIGHT-BICEP, A-TRIP, and CORRECT references
- **AND** the output categorizes findings by technique

### Requirement: RIGHT-BICEP reference includes detailed cases and examples

The RIGHT-BICEP reference MUST explain each part of RIGHT-BICEP with Java-oriented examples and validation signals.

#### Scenario: RIGHT-BICEP examples cover all technique letters

- **WHEN** `130-java-testing-strategies-right-bicep.md` is inspected
- **THEN** it includes examples or example sections for right results, boundary conditions, inverse relationships, cross-checks, error conditions, and performance guardrails
- **AND** inverse examples include reversible behavior such as serialize/deserialize, encode/decode, add/remove, or write/read
- **AND** cross-check examples compare against an independent calculation, fixture, alternate implementation, or property
- **AND** performance guidance is framed as a stable guardrail or smoke check rather than a fragile microbenchmark

### Requirement: A-TRIP reference includes detailed cases and examples

The A-TRIP reference MUST explain each part of A-TRIP with Java-oriented examples and quality signals.

#### Scenario: A-TRIP examples cover all quality characteristics

- **WHEN** `130-java-testing-strategies-a-trip.md` is inspected
- **THEN** it includes examples or example sections for automatic, thorough, repeatable, independent, and professional tests
- **AND** repeatability examples show controllable time with `Clock`, deterministic random values, isolated temporary files, or deterministic data
- **AND** independence examples reject shared mutable fixtures and test-order dependencies
- **AND** professional examples cover descriptive test names, clear assertions, maintainable fixtures, and low-noise test code

### Requirement: CORRECT reference includes detailed cases and examples

The CORRECT reference MUST explain each part of CORRECT with Java-oriented examples and boundary signals.

#### Scenario: CORRECT examples cover all boundary categories

- **WHEN** `130-java-testing-strategies-correct.md` is inspected
- **THEN** it includes examples or example sections for conformance, ordering, range, reference, existence, cardinality, and time
- **AND** ordering examples distinguish sorted, stable, and explicitly unordered expectations
- **AND** reference examples cover external dependencies such as missing records, unavailable services, permissions, or filesystem paths
- **AND** cardinality examples cover zero, one, many, exact count, maximum count, and duplicate cases
- **AND** time examples use `Clock` or equivalent controllable time source

### Requirement: Acceptance coverage exists for the testing strategy split

The repository MUST add acceptance coverage for `130-java-testing-strategies`.

#### Scenario: Gherkin feature covers focused reference selection

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/130-java-testing-strategies.feature` is inspected
- **THEN** it includes scenarios requiring RIGHT-BICEP, A-TRIP, and CORRECT focused reference selection
- **AND** it verifies that broad review requests may combine all three references
- **AND** it verifies that code edits remain gated by compile and verification guidance

#### Scenario: Acceptance prompt inventory includes 130

- **WHEN** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` is inspected
- **THEN** it includes a `130-java-testing-strategies` entry
- **AND** the entry uses the existing `execute @...feature` prompt format

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/130-java-testing-strategies` output is refreshed only by running the documented generator command

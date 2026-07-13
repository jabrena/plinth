## ADDED Requirements

### Requirement: Java design patterns skill example coverage

The repository MUST improve `123-java-design-patterns` so its reference examples are proportional to the skill's declared scope across classic Java, REST API, Kafka/event-driven, database/persistence, and cross-cutting integration patterns.

#### Scenario: Classic Java pattern reference covers major pattern pressures

- **GIVEN** maintainers improve `plinth-skills-generator/src/main/resources/skill-references/123-java-design-patterns.xml`
- **WHEN** the local skill is generated
- **THEN** `references/123-java-design-patterns.md` includes at least six top-level examples
- **AND** the examples cover creation, behavior variation, adaptation or wrapping, and state or command collaboration pressures
- **AND** the examples include benefit, cost, and when-not-to-use guidance

#### Scenario: REST API pattern reference covers contract evolution

- **GIVEN** maintainers improve `plinth-skills-generator/src/main/resources/skill-references/123-rest-api-patterns.xml`
- **WHEN** the local skill is generated
- **THEN** `references/123-rest-api-patterns.md` includes at least five top-level examples
- **AND** the examples cover resource design, idempotency, Problem Details, pagination or filtering, and optimistic concurrency or contract versioning
- **AND** the examples preserve HTTP semantics and DTO boundary guidance

#### Scenario: Kafka pattern reference covers reliability and evolution

- **GIVEN** maintainers improve `plinth-skills-generator/src/main/resources/skill-references/123-kafka-event-driven-patterns.xml`
- **WHEN** the local skill is generated
- **THEN** `references/123-kafka-event-driven-patterns.md` includes at least five top-level examples
- **AND** the examples cover event-carried state transfer, transactional outbox, partition key selection, idempotent consumer or inbox, and retry/DLT or schema evolution behavior
- **AND** the examples define ordering, duplicate delivery, failure, and replay considerations where applicable

#### Scenario: Persistence pattern reference covers consistency and query behavior

- **GIVEN** maintainers improve `plinth-skills-generator/src/main/resources/skill-references/123-database-persistence-patterns.xml`
- **WHEN** the local skill is generated
- **THEN** `references/123-database-persistence-patterns.md` includes at least five top-level examples
- **AND** the examples cover repository boundaries, optimistic locking, transaction boundaries or unit of work, aggregate boundaries, and migration or N+1/query behavior
- **AND** the examples identify data correctness, operational, and test validation signals

#### Scenario: Cross-cutting integration reference covers resilience and observability

- **GIVEN** maintainers improve `plinth-skills-generator/src/main/resources/skill-references/123-cross-cutting-integration-patterns.xml`
- **WHEN** the local skill is generated
- **THEN** `references/123-cross-cutting-integration-patterns.md` includes at least five top-level examples
- **AND** the examples cover anti-corruption layer, timeout/retry/circuit-breaker policy, correlation or trace propagation, bulkhead or fallback behavior, and strangler fig or reliable boundary evolution
- **AND** the examples define operational behavior such as limits, alerts, trace/log checks, or replay procedures where applicable

### Requirement: Pattern selection decision aids

The improved `123` references MUST help agents decide when not to apply a pattern, not only how to apply one.

#### Scenario: References include selection guidance

- **GIVEN** a `123` reference is read by an agent
- **WHEN** the agent evaluates a design pressure
- **THEN** the reference provides a compact pattern selection matrix or equivalent decision aid
- **AND** the guidance maps design pressure to candidate pattern, use-when, avoid-when, and validation signal
- **AND** the guidance explicitly allows "no new pattern" when simple Java, plain HTTP semantics, straightforward SQL, or existing infrastructure is enough

### Requirement: Acceptance coverage for non-REST pattern areas

The repository MUST extend `123-java-design-patterns` acceptance coverage beyond the current REST-only scenario.

#### Scenario: Kafka reliability review acceptance scenario exists

- **GIVEN** maintainers update `plinth-skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature`
- **WHEN** the acceptance scenarios are inspected
- **THEN** at least one scenario requires the skill to read `references/123-kafka-event-driven-patterns.md`
- **AND** the scenario verifies problem-first pattern selection, reliability trade-offs, and when-not-to-use guidance

#### Scenario: Persistence review acceptance scenario exists

- **GIVEN** maintainers update `plinth-skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature`
- **WHEN** the acceptance scenarios are inspected
- **THEN** at least one scenario requires the skill to read `references/123-database-persistence-patterns.md`
- **AND** the scenario verifies consistency, query behavior, validation signal, and explicit no-pattern decisions where simple persistence code is enough

#### Scenario: Acceptance prompt inventory remains synchronized

- **GIVEN** maintainers change `plinth-skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature`
- **WHEN** the change is reviewed
- **THEN** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` includes the matching `123-java-design-patterns` prompt entry
- **AND** the implementation records whether the listed prompt was executed or skipped with a reason

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills/123-java-design-patterns` output is refreshed only by running the documented generator command

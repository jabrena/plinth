Feature: Validate changes from usage of Java design patterns skill

Background:
  Given the skill "123-java-design-patterns"
  And the analysis sandbox folder "examples/skills/analysis" has no git changes

@acceptance-test
Scenario: Recommend REST API patterns from a concrete design pressure
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Review the REST controller design and recommend Java design patterns only where useful"
  And the local generated skill path ".agents/skills/123-java-design-patterns"
  And the folder "examples/frameworks/spring-boot" has no git changes
  And the requested analysis output path is "examples/skills/analysis/SPRING-BOOT-SUM-CONTROLLER-REST-PATTERNS-ANALYSIS.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/123-java-design-patterns" is applied to the example project
  Then the skill identifies the concrete design pressure before naming any pattern
  And the skill reads "references/123-rest-api-patterns.md"
  And the skill does not read unrelated pattern references unless the user request requires them
  And the skill recommends the smallest useful pattern or explicitly recommends no pattern when simple code is enough
  And each recommended pattern includes benefit, cost, and when-not-to-use guidance
  And the skill keeps code unchanged unless the user explicitly asks for implementation
  And the skill reports remaining trade-offs and validation that would be required for code changes
  And the skill writes the pattern analysis outcome to "examples/skills/analysis/SPRING-BOOT-SUM-CONTROLLER-REST-PATTERNS-ANALYSIS.md"
  And the generated analysis file reflects the whole review including design pressures, pattern recommendations with benefit-cost-when-not-to-use guidance, explicit no-pattern decisions, remaining trade-offs, and validation steps for code changes
  And the folder "examples/frameworks/spring-boot" has no git changes
  And any git changes produced under "examples/skills/analysis" during skill execution and verification are reset

@acceptance-test
Scenario: Review Kafka reliability patterns from delivery and ordering pressure
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Review the Kafka event-driven design for ordering, duplicate delivery, retry, and DLT patterns; recommend patterns only where useful"
  And the local generated skill path ".agents/skills/123-java-design-patterns"
  And the folder "examples/frameworks/spring-boot" has no git changes
  And the requested analysis output path is "examples/skills/analysis/SPRING-BOOT-KAFKA-RELIABILITY-PATTERNS-ANALYSIS.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/123-java-design-patterns" is applied to the example project
  Then the skill identifies the concrete delivery, ordering, duplicate, failure, or schema-evolution pressure before naming any pattern
  And the skill reads "references/123-kafka-event-driven-patterns.md"
  And the skill does not read unrelated pattern references unless the user request requires them
  And the skill recommends the smallest useful event-driven pattern or explicitly recommends no pattern when existing simple messaging is enough
  And each recommended Kafka or event-driven pattern includes benefit, cost, and when-not-to-use guidance
  And the skill explains reliability trade-offs for ordering, duplicate delivery, retries, DLT handling, replay, and schema compatibility where applicable
  And the skill keeps code unchanged unless the user explicitly asks for implementation
  And the skill reports validation steps for duplicate delivery, poison messages, replay, partition keys, and compatibility checks
  And the skill writes the pattern analysis outcome to "examples/skills/analysis/SPRING-BOOT-KAFKA-RELIABILITY-PATTERNS-ANALYSIS.md"
  And the generated analysis file reflects the whole review including design pressures, pattern recommendations with benefit-cost-when-not-to-use guidance, explicit no-pattern decisions, remaining trade-offs, and validation steps for code changes
  And the folder "examples/frameworks/spring-boot" has no git changes
  And any git changes produced under "examples/skills/analysis" during skill execution and verification are reset

@acceptance-test
Scenario: Review persistence patterns from consistency and query pressure
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Review the persistence design for transaction boundaries, aggregate boundaries, optimistic locking, migrations, and query behavior; recommend patterns only where useful"
  And the local generated skill path ".agents/skills/123-java-design-patterns"
  And the folder "examples/frameworks/spring-boot" has no git changes
  And the requested analysis output path is "examples/skills/analysis/SPRING-BOOT-PERSISTENCE-PATTERNS-ANALYSIS.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/123-java-design-patterns" is applied to the example project
  Then the skill identifies the concrete consistency, transaction, aggregate, migration, or query pressure before naming any pattern
  And the skill reads "references/123-database-persistence-patterns.md"
  And the skill does not read unrelated pattern references unless the user request requires them
  And the skill recommends the smallest useful persistence pattern or explicitly recommends no pattern when simple persistence code is enough
  And each recommended database or persistence pattern includes benefit, cost, and when-not-to-use guidance
  And the skill explains consistency, query behavior, operational impact, and validation signals for recommended persistence patterns
  And the skill keeps code unchanged unless the user explicitly asks for implementation
  And the skill reports validation steps for integration tests, migration validation, stale-write conflicts, rollback behavior, and query-count or index checks
  And the skill writes the pattern analysis outcome to "examples/skills/analysis/SPRING-BOOT-PERSISTENCE-PATTERNS-ANALYSIS.md"
  And the generated analysis file reflects the whole review including design pressures, pattern recommendations with benefit-cost-when-not-to-use guidance, explicit no-pattern decisions, remaining trade-offs, and validation steps for code changes
  And the folder "examples/frameworks/spring-boot" has no git changes
  And any git changes produced under "examples/skills/analysis" during skill execution and verification are reset

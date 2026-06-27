# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the existing `123-java-design-patterns` generated skill and source references.
- [x] 1.2 Confirm the improvement is one reviewable OpenSpec change instead of one change per reference file.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, two-step sequencing, and first vertical slice.
- [ ] 1.4 Add a compact pattern selection matrix or equivalent decision aid to each `123` XML reference.
- [ ] 1.5 Improve `123-java-design-patterns.xml` to include at least six examples covering creation, behavior variation, adaptation or wrapping, and state or command collaboration pressures.
- [ ] 1.6 Improve `123-rest-api-patterns.xml` to include at least five examples covering resource design, idempotency, Problem Details, pagination or filtering, and optimistic concurrency or contract versioning.
- [ ] 1.7 Improve `123-kafka-event-driven-patterns.xml` to include at least five examples covering event-carried state transfer, transactional outbox, partition key selection, idempotent consumer or inbox, and retry/DLT or schema evolution behavior.
- [ ] 1.8 Improve `123-database-persistence-patterns.xml` to include at least five examples covering repository boundaries, optimistic locking, transaction boundaries or unit of work, aggregate boundaries, and migration or N+1/query behavior.
- [ ] 1.9 Improve `123-cross-cutting-integration-patterns.xml` to include at least five examples covering anti-corruption layer, timeout/retry/circuit-breaker policy, correlation or trace propagation, bulkhead or fallback behavior, and strangler fig or reliable boundary evolution.
- [ ] 1.10 Ensure each new or revised example includes benefit, cost, when-not-to-use, and validation guidance.
- [ ] 1.11 Extend `skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature` with Kafka/event-driven and database/persistence acceptance scenarios.
- [ ] 1.12 Update `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` if the `123` acceptance prompt entry needs adjustment.
- [ ] 1.13 Validate changed XML files with `xmllint --noout`.
- [ ] 1.14 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.15 Inspect generated local `.agents/skills/123-java-design-patterns/SKILL.md`.
- [ ] 1.16 Inspect generated local `.agents/skills/123-java-design-patterns/references/123-*.md`.
- [ ] 1.17 Execute the listed `123-java-design-patterns` acceptance prompt and verify it passes, or record a skipped prompt with reason.
- [ ] 1.18 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.19 Run `openspec validate --all`.

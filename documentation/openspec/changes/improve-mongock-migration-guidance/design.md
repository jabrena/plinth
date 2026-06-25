## Context

Issue #902 asks maintainers to improve the Mongock skills for all supported frameworks so users receive better examples and safer advice when adding MongoDB data migrations. The issue was shaped as a user story for maintainers and expanded with MongoDB-oriented implementation ideas:

- Add explicit Mongock antipattern guidance.
- Adapt the Flyway safety pattern from issue #895 to NoSQL document evolution.
- Add framework-specific runner and configuration examples.
- Add verification guidance that proves Mongock executed and changed real MongoDB state.

The repository already has separate Mongock skills for Spring Boot, Quarkus, and Micronaut. The implementation should keep each framework's runner and test wiring local while adding shared migration-safety concepts consistently.

## Decisions

### Target Skill References

Update the three framework-specific Mongock reference XML files and, if the detailed guidance becomes too large for the main references, add focused supplemental references for each framework:

- `316-frameworks-spring-mongodb-migrations-mongock.xml`
- Optional: `316-frameworks-spring-mongodb-migrations-mongock-antipatterns.xml`
- Optional: `316-frameworks-spring-mongodb-migrations-mongock-document-evolution.xml`
- `416-frameworks-quarkus-mongodb-migrations-mongock.xml`
- Optional: `416-frameworks-quarkus-mongodb-migrations-mongock-antipatterns.xml`
- Optional: `416-frameworks-quarkus-mongodb-migrations-mongock-document-evolution.xml`
- `516-frameworks-micronaut-mongodb-migrations-mongock.xml`
- Optional: `516-frameworks-micronaut-mongodb-migrations-mongock-antipatterns.xml`
- Optional: `516-frameworks-micronaut-mongodb-migrations-mongock-document-evolution.xml`

No new skill id is required. The improvement belongs inside the existing framework-specific Mongock skills.

### Mongock Antipattern Guidance

Each Mongock skill should include guidance that helps agents identify risky MongoDB migrations before recommending code changes.

The guidance should cover:

- Editing an already-applied `@ChangeUnit` instead of creating a new ordered change.
- Using repositories, domain records, DTOs, mappers, or mutable application services inside migrations.
- Non-idempotent updates such as blind `$inc`, array appends, or broad `updateMany` operations without guarded predicates.
- Long backfills during application startup without considering probes, rollout windows, lock duration, partial completion, or recovery.
- Assuming transactions are available or cheap in standalone MongoDB, large collections, or multi-document updates.
- Hiding lock acquisition failures in clustered deployments.
- Recommending framework-specific runner coordinates without verifying compatibility first.
- Testing only application startup without proving Mongock changelog records and physical side effects.

### NoSQL Document Evolution Technique

Each Mongock skill should explain expand, migrate, contract as a MongoDB document migration workflow:

- Expand: add optional fields, embedded structures, collections, or indexes while old readers and writers still work.
- Migrate: backfill documents with idempotent predicates, tolerate or dual-write old and new shapes, and verify counts before and after the migration.
- Contract: remove old fields, obsolete embedded structures, unused indexes, old collections, or compatibility code only after all deployed code and existing documents have moved to the new shape.

Examples should include MongoDB-specific cases:

- Field rename using `$set` plus `$unset` while readers tolerate old and new field names during rollout.
- Scalar-to-array or scalar-to-object conversion with guarded predicates.
- Embedded document split or merge without losing historical data.
- New required field introduced as optional, backfilled, then enforced later.
- Status or enum value replacement with a compatibility window.
- Index creation as a separate migration before application code depends on it.

### Framework-Specific Guidance

Shared safety guidance should remain consistent, but examples must respect framework differences:

- Spring Boot: explicitly distinguish Spring Boot 3.x and 4.x runner choices. For Spring Boot 4.x, use `mongock-standalone` plus `mongodb-sync-v4-driver`, and avoid `mongock-springboot-v3`. Show `MongoDatabase` usage in `@ChangeUnit` methods and `@ServiceConnection` for Testcontainers.
- Quarkus: verify `quarkus-mongock` compatibility with the active Quarkus platform, document `quarkus.mongock.*` properties, default-client limits, and safe access to CDI-managed MongoDB clients when normal injection does not work inside `@ChangeUnit` classes.
- Micronaut: document standalone runner wiring, `mongodb.database` configuration, `TestPropertyProvider` for Testcontainers URIs, and the impact of adding MongoDB auto-configuration to existing `@MicronautTest` classes.

### Testing Guidance

Mongock verification should prove migration behavior, not only application startup.

The skills should recommend:

- Running migrations against real MongoDB infrastructure through Testcontainers, Quarkus Dev Services, or framework-native test support.
- Seeding pre-migration documents that represent old production shapes.
- Asserting `mongockChangeLog` contains `EXECUTED` records for expected change IDs.
- Asserting physical side effects such as field backfills, index creation, collection creation, or document-shape conversion.
- Checking idempotency for guarded predicates and avoiding duplicated array entries, repeated increments, or corrupted counters.
- Separating long-running backfills from automatic startup execution when operationally safer.

### Source and Generated Output Boundaries

Implementation must edit XML sources under `skills-generator/src/main/resources/skill-references/`, update `skills-generator/src/main/resources/skills.xml` if supplemental references are added, and update relevant skill index workflow steps so agents read every required reference. Generated `.agents/skills` output should be regenerated for local validation only. Public `skills/` release output should not be refreshed unless a later release task explicitly requests it.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local Mongock skills:
  - `.agents/skills/316-frameworks-spring-mongodb-migrations-mongock/SKILL.md`
  - `.agents/skills/416-frameworks-quarkus-mongodb-migrations-mongock/SKILL.md`
  - `.agents/skills/516-frameworks-micronaut-mongodb-migrations-mongock/SKILL.md`
- Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` before promoting. Run only listed prompts for changed skills if any are present.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

- Whether to add supplemental reference XML files or keep the new guidance inside the existing three references can be decided during implementation based on readability and generated skill size.

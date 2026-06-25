## Context

Issue #895 asks maintainers to improve Flyway skills for all supported frameworks so users receive better advice when adding database migration files. The issue specifically calls for:

- A Flyway antipatterns reference.
- A Flyway techniques reference that explains Parallel Change.
- Flyway tests to avoid out-of-order migrations in different branches.
- Ideas from the linked migration-risk discussion, including subtle schema changes that can damage or misinterpret data.

The repository already has separate Flyway skills for Spring Boot, Quarkus, and Micronaut. The implementation should keep each framework's setup guidance local while adding shared migration-safety concepts consistently.

## Decisions

### Target Skill References

Update the three framework-specific Flyway reference XML files and add two focused supplemental references for each framework:

- `313-frameworks-spring-db-migrations-flyway.xml`
- `313-frameworks-spring-db-migrations-flyway-antipatterns.xml`
- `313-frameworks-spring-db-migrations-flyway-parallel-change.xml`
- `413-frameworks-quarkus-db-migrations-flyway.xml`
- `413-frameworks-quarkus-db-migrations-flyway-antipatterns.xml`
- `413-frameworks-quarkus-db-migrations-flyway-parallel-change.xml`
- `513-frameworks-micronaut-db-migrations-flyway.xml`
- `513-frameworks-micronaut-db-migrations-flyway-antipatterns.xml`
- `513-frameworks-micronaut-db-migrations-flyway-parallel-change.xml`

No new skill id is required. The improvement belongs inside the existing framework-specific Flyway skills.

### Antipattern Guidance

Each Flyway skill should include a dedicated antipattern reference that helps agents identify risky migrations before recommending SQL changes.

The guidance should cover:

- Drop-and-add column changes when the intent is a rename.
- Type narrowing and column length reduction without checking existing data.
- `NOT NULL` defaults that assign incorrect business meaning to historical rows.
- Default-value changes that alter future-row behavior unexpectedly.
- Dropping and recreating tables, relationship tables, constraints, or indexes when data must be preserved.
- Enum/status meaning changes without an application compatibility plan.
- Timestamp and timezone type changes without verifying interpretation of existing values.
- Destructive repeatable migrations that rerun when checksums change.
- Broad data updates without precise `WHERE` clauses and expected row counts.
- Unique/index changes without duplicate detection and cleanup strategy.
- Duplicate version numbers, branch-ordering assumptions, and out-of-order migrations in different branches.
- Treating `outOfOrder=true` as a default branch-conflict fix rather than an exceptional operational decision.

### Parallel Change Technique

Each Flyway skill should include a dedicated Parallel Change reference that explains the technique as a database migration workflow:

- Expand: add the new column, table, index, or constraint in a backward-compatible way while the old schema shape still works.
- Migrate: backfill or dual-write data, update reads safely, and verify both old and new paths during rollout.
- Contract: remove the old column, table, or access path only after all deployed code and data have moved to the new shape.

The examples should apply the technique to Flyway scenarios such as column renames, type changes, large-table backfills, and relationship-table changes. Prefer multiple small forward-only migrations over one destructive migration.

### Testing Guidance

The Flyway skills should add migration test expectations beyond application startup:

- Run the full Flyway chain from an empty database using the real production dialect when feasible.
- Run migration tests from a previous-release schema or data snapshot for risky changes.
- Assert data preservation for renames, backfills, defaults, enum/status changes, and timezone changes.
- Add branch-ordering checks in CI to detect duplicate versions and unsafe out-of-order assumptions.
- Prefer Testcontainers or framework-native real-database test support over H2 when production uses PostgreSQL, MySQL, or another specific dialect.

### Source and Generated Output Boundaries

Implementation must edit XML sources under `skills-generator/src/main/resources/skill-references/`, update `skills-generator/src/main/resources/skills.xml` so the new references are registered, and update the relevant skill index workflow steps so agents read the main, antipatterns, and Parallel Change references. Generated `.agents/skills` output should be regenerated for local validation only. Public `skills/` release output should not be refreshed unless a later release task explicitly requests it.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local Flyway skills:
  - `.agents/skills/313-frameworks-spring-db-migrations-flyway/SKILL.md`
  - `.agents/skills/413-frameworks-quarkus-db-migrations-flyway/SKILL.md`
  - `.agents/skills/513-frameworks-micronaut-db-migrations-flyway/SKILL.md`
- Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` before promoting. Run only listed prompts for changed skills if any are present.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for the OpenSpec planning scope. Exact wording and example SQL can be refined during XML implementation while preserving the issue requirements.

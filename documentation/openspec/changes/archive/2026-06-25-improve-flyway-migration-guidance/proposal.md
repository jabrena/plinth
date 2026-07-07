## Why

GitHub issue [#895](https://github.com/jabrena/plinth/issues/895) requests stronger Flyway guidance across the framework-specific skills. The current Spring Boot, Quarkus, and Micronaut Flyway skills cover dependency setup, script locations, naming, configuration, and basic verification, but they need more explicit advice about migration antipatterns, production data safety, branch-ordering risks, and incremental database-change techniques.

Maintainers need these skills to help users avoid subtle schema and data migration failures such as destructive renames, unsafe defaults, broad updates, repeatable migration surprises, and out-of-order branch migrations.

## What Changes

- Add Flyway antipattern guidance to the Spring Boot, Quarkus, and Micronaut Flyway skill references.
- Add Flyway technique guidance explaining Martin Fowler's Parallel Change pattern as an expand, migrate, contract strategy for database migrations.
- Add testing guidance that verifies real migration behavior, data preservation, and branch-ordering safety.
- Keep the change scoped to XML skill source updates and local generated skill validation; public `skills/` release output remains out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `flyway-migration-skill-guidance`: Adds cross-framework Flyway migration safety guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#895](https://github.com/jabrena/plinth/issues/895).
- Existing implementation targets:
  - `skills-generator/src/main/resources/skill-references/313-frameworks-spring-db-migrations-flyway.xml`
  - `skills-generator/src/main/resources/skill-references/413-frameworks-quarkus-db-migrations-flyway.xml`
  - `skills-generator/src/main/resources/skill-references/513-frameworks-micronaut-db-migrations-flyway.xml`
- Supporting context from issue #895:
  - ChatGPT share about subtle Flyway data migration risks.
  - Martin Fowler's Parallel Change article.
  - Requirement to include Flyway tests to avoid out-of-order migrations in different branches.
- Derivation direction: issue #895 -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the issue describes one reviewable outcome: improve Flyway migration guidance consistently across all framework-specific Flyway skills. Although three XML reference files are affected, they share the same value, review boundary, and validation path.

The change is not split by framework because that would create one-change-per-file decomposition without independent user value.

## Impact

XML skill references, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

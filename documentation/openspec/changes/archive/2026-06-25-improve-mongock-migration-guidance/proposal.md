## Why

GitHub issue [#902](https://github.com/jabrena/plinth/issues/902) requests richer Mongock guidance across the framework-specific MongoDB migration skills. The current Spring Boot, Quarkus, and Micronaut Mongock skills cover runner setup, `@ChangeUnit` classes, lock behavior, and basic MongoDB-backed verification, but they need more explicit NoSQL migration-safety guidance and more examples focused on document-shape evolution.

Maintainers need these skills to help users avoid subtle MongoDB data migration failures such as non-idempotent updates, unsafe document reshaping, domain-model coupling inside old migrations, long startup backfills, hidden lock failures, and unverified runner compatibility.

## What Changes

- Add Mongock antipattern guidance to the Spring Boot, Quarkus, and Micronaut Mongock skill references.
- Add NoSQL document migration technique guidance that adapts the expand, migrate, contract pattern for MongoDB documents, indexes, embedded structures, and collections.
- Add testing guidance that verifies real Mongock execution, changelog records, physical side effects, and idempotent document updates.
- Keep the change scoped to XML skill source updates and local generated skill validation; public `skills/` release output remains out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `mongock-migration-skill-guidance`: Adds cross-framework Mongock migration safety guidance for MongoDB and NoSQL document evolution.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#902](https://github.com/jabrena/plinth/issues/902).
- Supporting source artifact: maintainer-authored issue comment with MongoDB-oriented improvement ideas.
- Related pattern source: GitHub issue [#895](https://github.com/jabrena/plinth/issues/895) and OpenSpec change `improve-flyway-migration-guidance`, used as a structural precedent only.
- Existing implementation targets:
  - `plinth-skills-generator/src/main/resources/skill-references/316-frameworks-spring-mongodb-migrations-mongock.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/416-frameworks-quarkus-mongodb-migrations-mongock.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/516-frameworks-micronaut-mongodb-migrations-mongock.xml`
- Supporting acceptance targets:
  - `plinth-skills-generator/src/test/resources/gherkin/skills/316-frameworks-spring-mongodb-migrations-mongock.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/skills/416-frameworks-quarkus-mongodb-migrations-mongock.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/skills/516-frameworks-micronaut-mongodb-migrations-mongock.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`
- Derivation direction: issue #902 -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the issue describes one reviewable outcome: improve Mongock migration guidance consistently across all framework-specific MongoDB migration skills. Although three XML reference files and three acceptance feature files may be affected, they share the same value, review boundary, and validation path.

The change is not split by framework because that would create one-change-per-file decomposition without independent user value.

## Impact

XML skill references, skill indexes, `skills.xml` registrations if supplemental references are added, Gherkin acceptance scenarios, local generated `.agents/skills` output, and OpenSpec artifacts may be affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

## Why

GitHub issue [#932](https://github.com/jabrena/cursor-rules-java/issues/932) requests a dedicated `055-design-parallel-change` skill for database migration scenarios. The repository already contains framework-specific Flyway parallel-change references for Spring Boot, Quarkus, and Micronaut, and the archived `flyway-migration-skill-guidance` specification requires those Flyway skills to explain Parallel Change as an expand, migrate, contract technique.

Java Enterprise developers need a cross-framework design skill because Parallel Change is a broader database evolution pattern, not only a Flyway framework detail. A dedicated design skill gives agents a reusable place to explain the pattern, identify when it applies, sequence safe database changes, and direct users back to framework-specific Flyway skills when implementation details are needed.

## What Changes

- Add `055-design-parallel-change` as a design skill focused on database migration scenarios.
- Consolidate shared expand, migrate, contract guidance from the existing Spring Boot, Quarkus, and Micronaut Flyway parallel-change references.
- Reference Martin Fowler's Parallel Change guidance and DORA database change management guidance as source material.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/055-design-parallel-change`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `design-parallel-change-skill-reference`: Adds dedicated Parallel Change design skill guidance for database migration scenarios.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#932](https://github.com/jabrena/cursor-rules-java/issues/932).
- External reference: [Martin Fowler: Parallel Change](https://martinfowler.com/bliki/ParallelChange.html).
- External reference: [DORA: Database Change Management](https://dora.dev/capabilities/database-change-management/).
- Existing source reference: `skills-generator/src/main/resources/skill-references/313-frameworks-spring-db-migrations-flyway-parallel-change.xml`.
- Existing source reference: `skills-generator/src/main/resources/skill-references/413-frameworks-quarkus-db-migrations-flyway-parallel-change.xml`.
- Existing source reference: `skills-generator/src/main/resources/skill-references/513-frameworks-micronaut-db-migrations-flyway-parallel-change.xml`.
- Existing specification context: `documentation/openspec/specs/flyway-migration-skill-guidance/spec.md`.
- Derivation direction: issue #932 plus cited references plus existing framework-specific XML references -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one independently reviewable outcome: a dedicated cross-framework Parallel Change design skill for database migration scenarios. It does not require splitting by Spring Boot, Quarkus, or Micronaut because those framework-specific Flyway references already exist and serve as source material.

Implementation may touch multiple generator files, but the value boundary remains the single new skill. Updating the existing Flyway skill behavior is out of scope unless implementation discovers a direct inconsistency with the new shared design guidance.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

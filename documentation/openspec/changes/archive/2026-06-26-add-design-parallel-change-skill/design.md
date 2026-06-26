## Context

Issue #932 asks for `055-design-parallel-change`, a dedicated Parallel Change pattern skill for database migration scenarios. Existing framework-specific Flyway references already explain the database form of Parallel Change as expand, migrate, and contract:

- `313-frameworks-spring-db-migrations-flyway-parallel-change`
- `413-frameworks-quarkus-db-migrations-flyway-parallel-change`
- `513-frameworks-micronaut-db-migrations-flyway-parallel-change`

The new skill should lift the shared pattern guidance into the design skill area so agents can reason about the migration approach before choosing framework-specific implementation guidance.

## Decisions

### Skill Identifier

Use `055-design-parallel-change` for the dedicated design skill.

This places the skill immediately after the existing design workflow skills:

- `051-design-two-steps-methods`
- `052-design-hamburger-method`
- `053-design-simple-rules`
- `054-design-tdd`

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/055-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/055-design-parallel-change.xml` provides detailed guidance, examples, output expectations, and tradeoffs.
- `skills-generator/src/main/resources/skills.xml` registers skill id `055` with explicit `skillId="055-design-parallel-change"`.

Dedicated report assets are not required for the first addition. The initial deliverable is pattern guidance and examples for database migration scenarios.

### Design Pressure and Pattern Fit

Parallel Change is selected because the design pressure is compatibility across deployment windows: database schema shape, data meaning, or application read/write paths must evolve while old and new application versions may coexist.

The skill must remain problem-led. It should recommend Parallel Change when a migration requires a compatibility window, such as column renames, type changes, large-table backfills, relationship-table changes, enum/status transitions, timezone changes, default changes, or unique/index changes. It should not recommend Parallel Change for small, immediately safe migrations where a simpler single migration is sufficient.

### Two-Step Implementation Sequencing

Implementation should follow the two-step method:

1. Behavior-preserving preparation: review existing framework-specific parallel-change references, extract shared guidance into a new reference, add the skill index, register the new skill, and keep existing framework behavior unchanged.
2. Behavior change: generate the new local skill and verify that agents can select `055-design-parallel-change` for database migration pattern guidance.

If later implementation updates framework-specific Flyway references to point to the new design skill, that must preserve their existing guidance and validation coverage.

### Relationship to Existing Flyway Guidance

The new design skill complements, not replaces, framework-specific Flyway skills.

- Use `055-design-parallel-change` when the user needs to decide whether and how to apply Parallel Change to a database migration.
- Use `313-frameworks-spring-db-migrations-flyway`, `413-frameworks-quarkus-db-migrations-flyway`, or `513-frameworks-micronaut-db-migrations-flyway` when the user needs framework-specific Flyway implementation guidance.
- The new skill may cite the existing framework-specific references as examples, but it must avoid duplicating framework configuration details that belong in those skills.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/055-design-parallel-change/SKILL.md`.
- Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; if a `055-design-parallel-change` acceptance prompt is added, execute only that listed prompt.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Release output under public `skills/` remains outside scope unless the maintainer intentionally runs the release profile.

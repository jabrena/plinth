## Context

Issue #877 requests a dedicated `051-design-two-steps-methods` skill for applying Kent Beck's principle: "Make the change easy, then make the easy change." The requested behavior is method guidance for complex or risky code changes, not a framework-specific Java implementation technique.

Existing skills `041` through `044` occupy the planning range. The requested identifier `051-design-two-steps-methods` should start a new design-method skill range before Java-specific implementation skills begin at `110`.

## Decisions

### Skill Identifier

Use `051-design-two-steps-methods`.

This preserves the canonical identifier stated in the issue title and body. The title wording uses both "2 steps method" and "two-step change method"; the skill id remains unchanged.

### Skill Shape

The implementation should follow the generator source model used by nearby skills:

- `skills-generator/src/main/resources/skill-indexes/051-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/051-design-two-steps-methods.xml` provides detailed method guidance and examples.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

The first version can use one focused reference file. Additional split references are not required unless implementation discovers substantial separate concerns.

### Method Scope

The skill guides users to split complex or risky changes into two phases:

- Make the change easy through preparatory refactoring that improves structure, names, boundaries, tests, types, interfaces, or dependencies without changing intended behavior.
- Make the easy change by applying the intended behavior change after the design supports it.

The skill must preserve behavior during preparatory refactoring, recommend verification between phases, and avoid bundling unrelated cleanup with either phase.

### Relationship to Existing Skills

Use `051-design-two-steps-methods` when the primary concern is change strategy for risky or complex work. It can compose with Java design and framework skills after the two-step strategy identifies which implementation guidance applies.

The skill must not replace Java design guidance such as `121-java-object-oriented-design`, `122-java-type-design`, or `123-java-design-patterns`; instead, it should help decide when preparatory refactoring should use those skills before the behavior change.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/051-design-two-steps-methods/SKILL.md`.
- Add and execute the listed `051-design-two-steps-methods` acceptance prompt from `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all` from `documentation/` when the OpenSpec CLI is available.

## Open Questions

None. The only wording variation in the issue is non-blocking because the canonical skill identifier is consistent.

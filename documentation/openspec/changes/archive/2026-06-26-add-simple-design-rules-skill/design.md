## Context

Issue #903 asks for a new `053-design-simple-rules` skill that helps agents apply Kent Beck's simple design rules to Java design and refactoring work. The issue defines the desired rule order and acceptance expectations: generated skill guidance, inventory registration, and generated skill validation.

The existing `051-design-two-steps-methods` and `052-design-hamburger-method` skills provide the closest implementation pattern for design-method skills: generated from XML sources, registered through `skills.xml`, validated through local skill generation, and covered by Gherkin acceptance tests plus prompt inventory when applicable.

## Decisions

### Skill Identifier

Use `053-design-simple-rules` for the new Simple Design Rules guidance.

This identifier follows `051-design-two-steps-methods` and `052-design-hamburger-method`, keeping design-method skills together in the `050` range.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/053-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/053-design-simple-rules.xml` provides detailed method guidance, examples, output structure, and self-check criteria.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated report templates are not required for the first version. The initial deliverable is an agent skill that guides design evaluation and refactoring recommendations.

### Simple Design Rule Order

The skill must preserve and explain the rule order from issue #903:

1. Passes the tests
2. Reveals intention / maximizes clarity
3. Has no duplication / minimizes duplication
4. Has the fewest elements

The guidance should make the ordering operational for Java work:

- Tests define the correctness boundary before design cleanup.
- Clear intention makes code easier to understand, review, and maintain.
- Duplication reduction should remove repeated knowledge without obscuring intent.
- Fewer elements is a final simplification pressure after correctness, clarity, and duplication have been addressed.

### Two-Step Implementation Sequence

Apply the two-step method to implementation:

1. Make the change easy through behavior-preserving preparation, such as reviewing nearby design skill patterns, confirming the `053` identifier is available, identifying generator registration points, and preparing focused acceptance expectations.
2. Make the behavior change by adding the `053-design-simple-rules` XML source, generator registration, acceptance coverage, prompt inventory entry, and generated local validation.

Validation must happen after preparation and again after the behavior-changing additions.

### Generated Output Boundaries

Implementation must edit generator XML sources and tests, then regenerate local skill output through the documented Maven workflow. It must not directly edit `.cursor/rules/`, `.agents/skills/`, public `skills/`, or `docs/`.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/053-design-simple-rules/SKILL.md`.
- Add or update `skills-generator/src/test/resources/gherkin/skills/053-design-simple-rules.feature`.
- Add `053-design-simple-rules` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Execute only the listed `053-design-simple-rules` acceptance prompt and verify it passes when the prompt runner is available.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. The issue provides the identifier, rule order, source references, and acceptance boundary.

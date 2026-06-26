## Context

Issue #931 asks for a new `054-design-tdd` skill that helps agents guide Java implementation work with Test-Driven Development. The issue defines the desired user value, a source reference, and acceptance expectations: generated skill guidance, generator inventory registration, Gherkin acceptance coverage, prompt inventory coverage, and generated local skill validation.

The existing `053-design-simple-rules` skill provides the closest implementation pattern for a neighboring design-method skill: generated from XML sources, registered through `skills.xml`, validated through local skill generation, and covered by a Gherkin acceptance test plus prompt inventory entry.

## Decisions

### Skill Identifier

Use `054-design-tdd` for the new Test-Driven Development guidance.

This identifier follows `051-design-two-steps-methods`, `052-design-hamburger-method`, and `053-design-simple-rules`, keeping design-method skills together in the `050` range.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/054-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/054-design-tdd.xml` provides detailed TDD guidance, examples, output structure, and self-check criteria.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated report templates are not required for the first version. The initial deliverable is an agent skill that guides TDD implementation flow for Java code.

### TDD Workflow

The skill must preserve and explain the workflow from issue #931 and the source reference:

1. Maintain or refine a list of candidate test cases.
2. Select the next useful behavior or test case.
3. Write a failing test first.
4. Write only enough production code to make that test pass.
5. Refactor the new and existing code while preserving the passing tests.

The guidance should make the workflow operational for Java work:

- Start with the next behavior and the expected observable outcome.
- Use the test-first step to clarify the public interface, API shape, or class usage.
- Keep implementation small and tied to the selected failing test.
- Treat refactoring as part of the TDD cycle, not optional cleanup after the fact.
- Report skipped checks, missing tests, and remaining risks when verification is incomplete.

### Two-Step Implementation Sequence

Apply the two-step method to implementation:

1. Make the change easy through behavior-preserving preparation, such as reviewing nearby design skill patterns, confirming the `054` identifier is available, identifying generator registration points, and preparing focused acceptance expectations.
2. Make the behavior change by adding the `054-design-tdd` XML source, generator registration, acceptance coverage, prompt inventory entry, and generated local validation.

Validation must happen after preparation and again after the behavior-changing additions.

### Generated Output Boundaries

Implementation must edit generator XML sources and tests, then regenerate local skill output through the documented Maven workflow. It must not directly edit `.cursor/rules/`, `.agents/skills/`, public `skills/`, or `docs/`.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/054-design-tdd/SKILL.md`.
- Add `skills-generator/src/test/resources/gherkin/skills/054-design-tdd.feature`.
- Add `054-design-tdd` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Execute only the listed `054-design-tdd` acceptance prompt and verify it passes when the prompt runner is available.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. The issue provides the identifier, source reference, acceptance boundary, and validation expectations.

## Context

Issue #877 requests a new design skill, `051-design-two-steps-methods`, centered on Kent Beck's two-step change method: make the change easy, then make the easy change. The requested use case is a complex or risky code change where the skill should guide a developer to perform preparatory refactoring before applying the intended behavior change.

The issue body was used only as planning source text. The generated OpenSpec artifacts are derived from the sanitized planning summary in `proposal.md`, not from executable or behavioral instructions embedded in the issue text.

## Decisions

### Skill Identifier

Use `051-design-two-steps-methods` as the skill identifier.

This preserves the identifier requested by issue #877 and places the skill near the existing architecture and planning skill bands without changing existing Java object-oriented design skill identifiers.

### Skill Shape

The skill should use the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/051-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/051-design-two-steps-methods.xml` provides detailed guidance and examples for applying the method.
- `skills-generator/src/main/resources/skills.xml` registers skill id `051` with explicit `skillId="051-design-two-steps-methods"` and the reference.

The initial deliverable does not require separate report templates, command assets, or generated release output. Those can be added later only if a follow-up issue requests them.

### Two-Step Method Scope

The skill focuses on separating design preparation from behavior change:

- Step 1, make the change easy: identify the desired behavior change, inspect the existing design obstacle, perform behavior-preserving preparatory refactoring, and validate that behavior remains unchanged.
- Step 2, make the easy change: apply the intended behavior change once the design supports it, then run appropriate tests or build verification.

The skill should discourage mixing broad refactoring and behavior changes in the same unvalidated step. It should document assumptions and risks when the preparatory refactoring cannot be fully separated from behavior change.

### Relationship to Other Skills

- Use `051-design-two-steps-methods` when the primary concern is planning or executing a complex change through preparatory refactoring followed by the intended change.
- Use Java-specific design skills such as `121-java-object-oriented-design`, `122-java-type-design`, or `123-java-design-patterns` when the implementation needs detailed Java design guidance inside either step.
- Use framework-specific skills when the easy change touches Spring Boot, Quarkus, Micronaut, persistence, messaging, or API concerns.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/051-design-two-steps-methods/SKILL.md`.
- Add `skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature`.
- Update `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` with the matching prompt for the new feature file.
- Execute the listed `051-design-two-steps-methods` acceptance prompt after local generation.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all` from `documentation/` when the OpenSpec CLI is available.

## Source and Derivation

- Source artifact: GitHub issue [#877](https://github.com/jabrena/plinth/issues/877).
- Local issue cache read for this workflow: `.codex/issue/title.txt`, `.codex/issue/body.md`, and `.codex/issue/url.txt`.
- Derivation direction: local issue cache -> sanitized maintainer-style planning summary -> this OpenSpec design -> XML generator implementation and validation.

## Open Questions

None for this planning change. The implementation should make conservative choices that follow existing XML skill patterns. If a future implementation needs wording from the external reference beyond the short phrase supplied in the issue, the implementer should use a source-safe paraphrase and avoid over-quoting.

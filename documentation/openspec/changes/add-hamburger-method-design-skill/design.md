## Context

Issue #874 asks for a new `052-design-hamburger-method` skill that helps agents split large feature ideas into small vertical slices. The requested skill should support planning, OpenSpec/spec refinement, implementation breakdown, and optional tracker issue creation for actionable slices.

The existing `051-design-two-steps-methods` skill provides the closest implementation pattern for a design-method skill: generated from XML sources, registered through `skills.xml`, validated through local skill generation, and covered by Gherkin acceptance tests plus prompt inventory when applicable.

## Decisions

### Skill Identifier

Use `052-design-hamburger-method` for the new Hamburger Method guidance.

This identifier follows the existing `051-design-two-steps-methods` design skill and keeps the new slicing method in the design-method sequence.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/052-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/052-design-hamburger-method.xml` provides detailed method guidance, examples, output structure, and self-check criteria.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated report templates are not required for the first version. The initial deliverable is an agent skill that produces slicing analysis and recommendations.

### Hamburger Method Workflow

The skill should guide an agent to:

- Recognize oversized feature, story, plan, or spec work where ordinary story-splitting heuristics are not enough.
- Identify 3-6 functional or workflow layers that participate in delivering value.
- Generate 4-5 implementation or quality options per layer, ordered from simplest acceptable option to richer options.
- Challenge scope with a smallest-useful-version question such as: "If this had to ship tomorrow, what would be the smallest useful version?"
- Filter choices that are too costly, redundant, irreversible, or unnecessary for early learning.
- Compose one first vertical slice by selecting one option from each relevant layer.
- Suggest follow-up vertical slices that incrementally improve quality, automation, robustness, reach, or observability.
- Verify that each slice remains valuable, small, testable, deliverable, and suitable for issue tracking.

### Tracker and Planning Skill Relationships

The Hamburger Method skill should not create tracker issues directly. When it identifies actionable split candidates, it should ask whether those slices should become tracked work and route issue creation or updates through the appropriate skill:

- Use `043-planning-github-issues` for GitHub issues.
- Use `044-planning-jira` for Jira issues.
- Use `041-planning-plan-mode` when slices need to become or update an implementation plan.
- Use `042-planning-openspec` when a slice introduces a separate spec-level product, workflow, or architectural decision.

Mechanical implementation tasks such as registry updates, acceptance prompt coverage, generated-output inspection, or release validation should remain ordinary task or issue work unless they introduce a separate reviewed capability.

### Two-Step Implementation Sequence

Apply the two-step method to implementation:

1. Make the change easy through behavior-preserving preparation, such as reviewing nearby design skill patterns, confirming available identifier slots, identifying generator registration points, and preparing focused acceptance expectations.
2. Make the behavior change by adding the `052-design-hamburger-method` XML source, generator registration, acceptance coverage, and generated local validation.

Validation must happen after preparation and again after the behavior-changing additions.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/052-design-hamburger-method/SKILL.md`.
- Add or update `skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature`.
- Add `052-design-hamburger-method` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Execute only the listed `052-design-hamburger-method` acceptance prompt and verify it passes.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Follow-up tracker issues can be created later if implementation planning identifies independent execution slices.

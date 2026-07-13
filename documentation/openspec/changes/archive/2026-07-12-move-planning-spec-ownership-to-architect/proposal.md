## Why

Issue #1019 moves pre-implementation planning and specification ownership from `@robot-tech-lead` to `@robot-architect`. The current agent contract overlaps phases: `robot-architect` owns exploration, ADRs, and diagrams, while `robot-tech-lead` owns both OpenSpec/plan creation and implementation delivery coordination. That makes the handoff between architecture and delivery less clear and keeps `/create-spec` routed through the delivery coordinator.

## What Changes

- Expand `robot-architect` so it can create or refine implementation plans and OpenSpec changes after architecture exploration and before delivery.
- Narrow `robot-tech-lead` so it starts from an approved implementation plan or OpenSpec task list and coordinates implementation only.
- Move `/create-spec` ownership/routing from `@robot-tech-lead` to `@robot-architect`.
- Leave `robot-business-analyst` responsibilities unchanged.
- Preserve coder-agent routing and implementation delegation behavior.
- Update agent and command acceptance tests so future behavior is explicit before changing generated prompts.
- Update documentation and inventories that describe the role boundaries.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-agents`: Moves planning/specification ownership to `robot-architect` and narrows `robot-tech-lead` to implementation-phase coordination.
- `analysis-design-commands`: Moves `/create-spec` ownership/routing to `robot-architect`.

## Source and Derivation

- Source artifact: GitHub issue #1019, "Move planning and specification ownership from tech lead to architect", https://github.com/jabrena/plinth/issues/1019
- Supporting repository evidence:
  - `plinth-skills-generator/src/test/resources/gherkin/agents/robot-architect.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/agents/robot-tech-lead.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/commands/create-spec.feature`
  - `plinth-skills-generator/src/main/resources/skill-references/assets/commands/create-spec.md`
- Derivation direction: GitHub issue #1019 -> OpenSpec change -> canonical agent/command source assets, acceptance tests, generated local output, and documentation.

## Impact

This is a compatibility-relevant role-boundary change. Users who currently invoke `/create-spec` or ask `@robot-tech-lead` to create plans/OpenSpec changes will need routing guidance to `@robot-architect`. Users who rely on `robot-business-analyst` for issue quality, traceability, read-only alignment review, or readiness review should observe no behavior change. Generated outputs such as `.cursor/agents`, `.cursor/commands`, `.agents/skills`, public `skills/`, and site output remain generated or installed artifacts; implementation must update source assets and regenerate through the documented workflows.

## Why

Issue #1026 completes the 0.18.0 analysis-and-design workflow decoupling started in #1019. `/create-spec` still bundles OpenSpec authoring with design-exploration skills (`051`–`056`, `121`–`123`, `130`), and `@robot-architect` mission 4 applies those same skills during initial OpenSpec creation. `/explore-design` still routes only to `034-architecture-design-exploration`, accepts `<issue-or-artifact>`, and describes a narrow issue-only purpose that runs before planning.

That coupling makes design refinement appear to be part of initial specification instead of a follow-up step owned by `/explore-design`. Users need a clear sequence: create the OpenSpec change first with `042-planning-openspec` only, then refine the technical approach with the design skill set through `/explore-design`.

## What Changes

- Narrow `/create-spec` to `042-planning-openspec` only and document it as the first workflow step for OpenSpec proposal, design, specification, and task authoring.
- Enrich `/explore-design` to own design skills `051`–`057`, `121`–`123`, and `130`; accept OpenSpec changes; update purpose, usage, accepted inputs, and workflow position as the second step after `/create-spec`.
- Decouple `@robot-architect` so OpenSpec authoring retains only `@041-planning-plan-mode` and `@042-planning-openspec`, while design refinement owns the moved design skills and runs through `/explore-design`.
- Physically remove `034-architecture-design-exploration` from source assets, inventories, acceptance tests, installer references, and generated outputs because the enriched design skill set supersedes it.
- Update acceptance tests, workflow guides, inventories, and README discoverability to document the create-spec-first, explore-design-second path.
- Leave `@robot-tech-lead` implementation-delivery responsibilities and `@robot-business-analyst` issue/alignment responsibilities unchanged.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-commands`: Decouples `/create-spec` and `/explore-design` skill routing, accepted inputs, purpose, usage, and workflow order.
- `analysis-design-agents`: Splits `@robot-architect` OpenSpec authoring from design refinement and removes `034` from agent routing.
- `analysis-design-lifecycle-documentation`: Documents `/create-spec` first with `042-planning-openspec` only and `/explore-design` second for technical approach refinement.

## Source and Derivation

- Source artifact: GitHub issue #1026, "Enrich /explore-design", https://github.com/jabrena/plinth/issues/1026
- Prerequisite change: archived `2026-07-12-move-planning-spec-ownership-to-architect` (issue #1019)
- Supporting repository evidence:
  - `plinth-skills-generator/src/test/resources/gherkin/commands/create-spec.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/commands/explore-design.feature`
  - `plinth-skills-generator/src/test/resources/gherkin/agents/robot-architect.feature`
  - `plinth-skills-generator/src/main/resources/skill-references/assets/commands/create-spec.md`
  - `plinth-skills-generator/src/main/resources/skill-references/assets/commands/explore-design.md`
  - `plinth-skills-generator/src/main/resources/skill-references/assets/agents/robot-architect.md`
  - `plinth-skills-generator/src/main/resources/skill-indexes/034-skill.xml`
  - `plinth-skills-generator/src/main/resources/skills.xml`
- Derivation direction: GitHub issue #1026 → OpenSpec change → canonical command/agent/skill source assets, acceptance tests, generated local output, workflow documentation, and release guidance.

## Impact

This is a compatibility-relevant workflow-boundary change for users who invoke `/create-spec` expecting embedded design exploration, or `/explore-design` expecting `034-architecture-design-exploration`. Implementation must update source assets and regenerate through the documented `plinth-skills-generator` workflow rather than editing generated `.cursor/commands`, `.cursor/agents`, `.agents/skills`, or public `skills/` directly. Skill `034` removal is a breaking inventory change mitigated by the enriched `/explore-design` contract and documentation updates.

## Unresolved Questions

None from the source issue. Exact workflow step wording may be refined during review while preserving the create-spec-first rule, mission decoupling, `042`-only `/create-spec`, physical removal of `034`, and OpenSpec change as an accepted `/explore-design` input.

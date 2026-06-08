## Why

GitHub issue [#817](https://github.com/jabrena/cursor-rules-java/issues/817) requests bounded-context diagrams for `033-architecture-diagrams` based on PlantUML guidance from Context Mapper. The current skill supports UML sequence, UML class, C4 Context/Container/Component, UML state-machine, and ER diagrams, but it does not offer a dedicated Domain-Driven Design bounded-context diagram path.

Users who want context-map style diagrams currently have to approximate them with C4 or class diagrams. That makes bounded contexts, upstream/downstream relationships, shared kernels, partnerships, anticorruption layers, and other DDD relationship concepts less explicit than they should be.

## What Changes

- Add bounded-context diagrams as a selectable diagram family in `033-architecture-diagrams`.
- Update the skill metadata, goal, triggers, question flow, and selected-reference mapping to include bounded-context diagrams.
- Add focused reference guidance for generating bounded-context diagrams with valid PlantUML syntax.
- Ensure the "All diagrams" path includes bounded-context diagrams.
- Preserve the existing PlantUML validation workflow for generated `.puml` files.
- Keep the scope to PlantUML authoring guidance inside the skill, without adding a Context Mapper CML parser or external generator integration.

## Capabilities

### Modified Capabilities

- `architecture-diagram-skill-references`: Extends the architecture diagram skill's orchestration and focused reference model with bounded-context PlantUML diagram support.

### New Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#817](https://github.com/jabrena/cursor-rules-java/issues/817), milestone `v0.16.0`.
- External reference: Context Mapper PlantUML generator documentation, https://contextmapper.org/docs/plant-uml/.
- Related existing capability: `architecture-diagram-skill-references`.
- Derivation direction: issue #817 -> OpenSpec change artifacts -> implementation tasks. The issue remains the source for problem, scope, acceptance criteria, and implementation constraints.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: bounded-context PlantUML diagram support in the existing `033-architecture-diagrams` skill. The work touches the skill entry point, question asset, focused reference registration, and a new focused reference file, but those changes share one review boundary, validation path, and user-facing capability.

The change does not require splitting by technical layer because the issue does not request a Context Mapper CML parser, a generator integration, website updates, or release-output refresh.

## Impact

XML skill source, XML question assets, XML skill references, `skills.xml`, local generated skill output validation, and the existing architecture diagram OpenSpec capability may be affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

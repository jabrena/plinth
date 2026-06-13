## Why

GitHub issue [#847](https://github.com/jabrena/cursor-rules-java/issues/847) requests UML Deployment Diagram support for `033-architecture-diagrams` using PlantUML. The current skill supports UML sequence, UML class, C4 Context/Container/Component, UML state-machine, ER, and bounded-context diagrams, but it does not offer a dedicated deployment topology path.

Users who need to document deployed Java enterprise systems currently have to approximate runtime infrastructure with C4 or component diagrams. That makes runtime nodes, execution environments, deployed artifacts, infrastructure boundaries, external systems, and communication protocols less explicit than they should be.

## What Changes

- Add UML Deployment Diagrams as a selectable diagram family in `033-architecture-diagrams`.
- Update the skill metadata, goal, triggers, question flow, and selected-reference mapping to include deployment diagrams.
- Add focused reference guidance for generating deployment diagrams with valid PlantUML syntax.
- Accept a deployment diagram image or a system description file as source material when the user provides one.
- Collect deployment-specific inputs such as actors, services, CI/CD systems, nodes, execution environments, deployed components or artifacts, external systems, protocols, ports, and infrastructure boundaries when source material is missing or incomplete.
- Ensure the "All diagrams" path includes deployment diagrams.
- Preserve the existing PlantUML validation workflow for generated `.puml` files.
- Keep the scope to PlantUML authoring guidance inside the skill, without adding infrastructure discovery, cloud-provider inventory, or diagram rendering automation.

## Capabilities

### Modified Capabilities

- `architecture-diagram-skill-references`: Extends the architecture diagram skill's orchestration and focused reference model with UML Deployment Diagram support.

### New Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#847](https://github.com/jabrena/cursor-rules-java/issues/847), milestone `v0.16.0`.
- External reference: [PlantUML Deployment Diagram](https://plantuml.com/es/deployment-diagram).
- Related existing capability: `architecture-diagram-skill-references`.
- Derivation direction: issue #847 plus PlantUML reference link -> OpenSpec change artifacts -> implementation tasks. The issue remains the source for problem, scope, acceptance criteria, and implementation constraints; the PlantUML reference informs syntax-oriented deployment diagram guidance.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: UML Deployment Diagram support in the existing `033-architecture-diagrams` skill. The work touches the skill entry point, question asset, focused reference registration, and a new focused reference file, but those changes share one review boundary, validation path, and user-facing capability.

The change does not require splitting by technical layer because the issue does not request cloud inventory discovery, infrastructure-as-code parsing, generated website updates, or release-output refresh.

## Impact

XML skill source, XML question assets, XML skill references, `skills.xml`, local generated skill output validation, and the existing architecture diagram OpenSpec capability may be affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

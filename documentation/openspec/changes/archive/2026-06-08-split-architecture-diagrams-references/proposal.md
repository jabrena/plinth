## Why

GitHub issue [#818](https://github.com/jabrena/cursor-rules-java/issues/818) identifies that `033-architecture-diagrams` still relies on one large reference source for question flow, diagram-family implementation guidance, examples, constraints, and output organization rules. This makes the skill harder to maintain and easier to drift from its entry-point constraints, including the current C4 mismatch where the skill forbids Code/Level 4 diagrams while the generated reference still offers them.

## What Changes

- Move the architecture diagram preference questions into a dedicated question-flow asset included by `033-skill.xml`.
- Make `033-skill.xml` the orchestration layer that asks questions before reading references and maps selected diagram types to selected references.
- Split the monolithic `033-architecture-diagrams.xml` reference into focused diagram-family references for UML sequence, UML class, C4, UML state-machine, and ER diagrams.
- Register the split references for skill `033` in `skills.xml`, following the source-level pattern used by `111-java-maven-dependencies` and `112-java-maven-plugins`.
- Put output organization and validation guardrails inside each focused diagram reference instead of creating a standalone validation-only reference.
- Align all C4 guidance with Context, Container, and Component diagrams only.

## Capabilities

### New Capabilities

- `architecture-diagram-skill-references`: Defines how the architecture diagram skill orchestrates interactive questions and loads focused diagram-family reference content.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#818](https://github.com/jabrena/cursor-rules-java/issues/818), milestone `v0.16.0`.
- Related source pattern: `111-java-maven-dependencies` and `112-java-maven-plugins` source-level split of question assets, skill orchestration, `skills.xml` reference registration, and focused references.
- Derivation direction: issue #818 -> OpenSpec change artifacts -> implementation tasks. The issue remains the source for problem, scope, acceptance criteria, and implementation constraints.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: refactoring `033-architecture-diagrams` so its questions live in the skill workflow and its reference guidance is split by diagram family. The work affects several XML sources and generated local skill output, but the value, review boundary, and validation path are shared.

## Impact

XML skill index, XML question assets, XML skill references, `skills.xml`, generator output validation, and possibly focused generator tests may be affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile if this work is included in a release-preparation change.

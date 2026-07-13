## Context

`033-architecture-diagrams` is an interactive skill that can generate several diagram families: UML sequence, UML class, C4, UML state-machine, and ER diagrams. Its generated skill entry point is concise, but its detailed reference source is monolithic. The Maven dependency and plugin skills already demonstrate a better pattern for large interactive skills: keep questions and reference selection in the skill workflow, then split implementation guidance into focused references.

## Goals / Non-Goals

**Goals:**

- Keep `033-skill.xml` as the orchestration layer for validation, questions, reference mapping, and selected-reference loading rules.
- Move diagram preference questions into an XML question-flow asset included from the skill workflow.
- Split diagram-family implementation guidance into focused XML reference files.
- Include output organization and validation guardrails inside each diagram-family reference.
- Register split references under skill `033` in `skills.xml`.
- Remove C4 Code/Level 4 options from all architecture diagram skill guidance.

**Non-Goals:**

- Create a standalone `033-architecture-diagrams-output-validation.xml` reference.
- Change the public skill id `033-architecture-diagrams`.
- Change the meaning of existing supported diagram families beyond fixing the C4 Level 4 drift.
- Edit `.cursor/rules/`, public `skills/`, or `docs/` directly.
- Redesign the generator or XSLT pipeline.

## Decisions

### Use skill-level question orchestration

`033-skill.xml` should include the architecture diagram question flow before any detailed reference is read. This mirrors `111-java-maven-dependencies` and `112-java-maven-plugins`, where the generated `SKILL.md` asks questions first, records selections, and maps answers to references.

### Split references by diagram family

The monolithic architecture diagram reference should be split into diagram-family references:

- `033-architecture-diagrams-uml-sequence.xml`
- `033-architecture-diagrams-uml-class.xml`
- `033-architecture-diagrams-c4.xml`
- `033-architecture-diagrams-state-machine.xml`
- `033-architecture-diagrams-er.xml`

Each reference should state that it is read only when selected by the central question flow.

After the split references fully replace the original source, `033-architecture-diagrams.xml` should be deleted and removed from `skills.xml` registration so there is no compatibility bridge or stale monolithic source.

### Keep guardrails local to each reference

Output organization and validation guidance should be embedded in each focused reference. A standalone validation reference would add another file that every diagram path must remember to load, which weakens the selected-reference model. Local guardrails keep each diagram family complete and reduce cross-reference drift.

### Align C4 guidance to supported levels

The skill entry point already restricts C4 diagrams to Context, Container, and Component levels. The question flow, C4 reference, examples, and generated output must follow that same constraint and must not offer Code/Level 4 diagrams.

### Preserve generated-output boundaries

Implementation should edit XML sources under `plinth-skills-generator/src/main/resources/`. Local generated skills can be refreshed with `./mvnw clean install -pl plinth-skills-generator` for validation. Public `skills/` release output should only be refreshed when the release profile is intentionally used.

## Risks / Trade-offs

- [Risk] Split references duplicate common validation text. -> Keep shared wording short and scoped to the relevant diagram family.
- [Risk] Generated reference links change unexpectedly. -> Validate generated local skill output and inspect reference links after generation.
- [Risk] Stale C4 Code/Level 4 text remains in a template or question asset. -> Search generated local output and XML sources for Code/Level 4 references during validation.
- [Risk] Source split changes behavior instead of structure. -> Preserve existing behavior except for the explicit C4 Level 4 correction.

## Migration Plan

1. Add the diagram question-flow asset under `skill-references/assets/questions/`.
2. Update `033-skill.xml` to include the question flow and map selections to split references.
3. Split the monolithic `033-architecture-diagrams.xml` source into focused diagram-family references with local guardrails.
4. Update `skills.xml` so skill `033` registers the split references.
5. Validate changed XML with `xmllint --noout`.
6. Run local skill generation with `./mvnw clean install -pl plinth-skills-generator`.
7. Inspect generated local `033-architecture-diagrams/SKILL.md` and references for question flow, reference links, guardrails, and C4 alignment.
8. Run applicable focused Maven or generator validation.

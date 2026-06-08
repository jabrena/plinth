## Context

`033-architecture-diagrams` is an interactive skill that asks users which diagram families they want, then reads only the focused references selected by those answers. The current focused references cover UML sequence, UML class, C4, UML state-machine, and ER diagrams. Issue #817 adds a DDD-oriented diagram need: bounded-context diagrams using PlantUML, inspired by Context Mapper's generated context-map component diagrams.

Context Mapper's PlantUML documentation describes generated UML component diagrams for Context Maps, plus class and state diagrams for richer CML models. This change uses that documentation as conceptual input for skill guidance, not as a requirement to parse CML or call Context Mapper tooling.

## Goals / Non-Goals

**Goals:**

- Add bounded-context diagrams as a first-class selectable diagram family in the skill workflow.
- Guide agents to represent bounded contexts and relationships clearly in PlantUML.
- Support relationship direction and labels for DDD context-map concepts such as upstream/downstream, shared kernel, partnership, customer/supplier, conformist, open host service, published language, and anticorruption layer where project context supports them.
- Keep bounded-context output compatible with the existing PlantUML validation workflow.
- Keep selected-reference loading rules consistent: read the bounded-context reference only when selected, or when "All diagrams" is selected.

**Non-Goals:**

- Add a Context Mapper CML parser.
- Invoke Context Mapper generators or require Context Mapper installation.
- Replace existing C4 Context, Container, or Component diagram guidance.
- Add C4 Code/Level 4 diagrams.
- Edit `.cursor/rules/`, public `skills/`, or `docs/` directly.
- Refresh public `skills/` release output outside an intentional release-profile run.

## Decisions

### Add a dedicated bounded-context reference

Bounded-context diagrams should get their own focused reference, for example `033-architecture-diagrams-bounded-context.xml`. They are related to C4 Context diagrams but have a different vocabulary and modeling goal. Keeping them separate preserves the current selected-reference model and avoids mixing DDD context-map guidance into C4 guidance.

### Treat Context Mapper as inspiration, not a runtime dependency

The issue links to Context Mapper PlantUML documentation. The skill should cite the idea of context-map component diagrams, but implementation should remain repository-local XML guidance that produces PlantUML. This avoids adding tool installation, CML parsing, generator invocation, and fixture complexity that the issue does not request.

### Use PlantUML component/package-oriented notation

Bounded contexts can be represented with PlantUML component or package-style notation. The reference should favor readable context-map diagrams where bounded contexts are visually distinct and relationships carry DDD labels. The generated diagrams should be simple to validate with the existing Docker-based PlantUML commands.

### Preserve source and generated-output boundaries

Implementation should edit XML sources under `skills-generator/src/main/resources/`. Local generated skills can be refreshed with `./mvnw clean install -pl skills-generator` for validation. Public `skills/` release output should only be refreshed when release output is intentionally in scope.

## Risks / Trade-offs

- [Risk] Bounded-context guidance overlaps with C4 Context diagrams. -> Keep bounded-context guidance focused on DDD context-map vocabulary and relationship semantics.
- [Risk] Agents infer unsupported DDD relationships from sparse code. -> Require relationship labels to be based on project context, user input, or documented architecture artifacts.
- [Risk] PlantUML examples become too Context Mapper-specific. -> Provide plain PlantUML examples that do not require Context Mapper tooling.
- [Risk] The "All diagrams" path drifts from the reference list. -> Update the question flow, reference mapping, `skills.xml`, and generated local output together.

## Migration Plan

1. Update `033-skill.xml` metadata, goal text, trigger list, and selected-reference mapping.
2. Update `033-architecture-diagrams-questions.xml` so bounded-context diagrams appear in the diagram selection flow.
3. Add a focused bounded-context PlantUML reference under `skill-references/`.
4. Register the new reference for skill `033` in `skills.xml`.
5. Validate changed XML files with `xmllint --noout`.
6. Run `./mvnw clean install -pl skills-generator` to regenerate local skills without refreshing public `skills/`.
7. Inspect generated local `033-architecture-diagrams/SKILL.md` and reference links for bounded-context selection and "All diagrams" coverage.
8. Run `./mvnw clean verify -pl skills-generator` before promoting the implementation.

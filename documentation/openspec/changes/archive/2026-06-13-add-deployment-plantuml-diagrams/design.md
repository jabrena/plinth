## Context

`033-architecture-diagrams` is an interactive skill that asks users which diagram families they want, then reads only the focused references selected by those answers. The current focused references cover UML sequence, UML class, C4, UML state-machine, ER, and bounded-context diagrams. Issue #847 adds a UML deployment need: deployment diagrams using PlantUML's deployment diagram syntax.

PlantUML deployment diagrams can represent runtime nodes, execution environments, deployed artifacts, components, databases, external systems, and communication paths. This change uses the PlantUML deployment documentation as syntax guidance while keeping the implementation as repository-local XML skill guidance.

## Goals / Non-Goals

**Goals:**

- Add UML Deployment Diagrams as a first-class selectable diagram family in the skill workflow.
- Accept a deployment diagram image or a file describing the system as preferred source material before asking follow-up questions.
- Ask for deployment topology inputs before generating a deployment diagram when no sufficient source is provided.
- Guide agents to represent actors, services, CI/CD systems, nodes, execution environments, deployed artifacts or components, external systems, and communication relationships clearly in PlantUML.
- Include infrastructure or network boundaries when the user or repository context provides them.
- Keep deployment output compatible with the existing PlantUML validation workflow.
- Keep selected-reference loading rules consistent: read the deployment reference only when selected, or when "All diagrams" is selected.

**Non-Goals:**

- Discover cloud infrastructure automatically.
- Parse Terraform, Kubernetes manifests, Docker Compose files, Helm charts, or cloud-provider inventories unless a later implementation explicitly scopes that work.
- Replace C4 Context, Container, or Component diagram guidance.
- Add diagram rendering automation beyond the existing PlantUML validation and rendering commands.
- Edit `.cursor/rules/`, public `skills/`, or `docs/` directly.
- Refresh public `skills/` release output outside an intentional release-profile run.

## Decisions

### Add a dedicated deployment reference

Deployment diagrams should get their own focused reference, for example `033-architecture-diagrams-deployment.xml`. They are related to C4 Container diagrams but have a different modeling goal: runtime topology and deployment relationships. Keeping them separate preserves the current selected-reference model and avoids mixing infrastructure topology guidance into C4 guidance.

### Use PlantUML deployment notation

The reference should guide agents to use PlantUML deployment constructs such as actors, agents, artifacts, components, databases, clouds, interfaces, nodes, queues, storage, ports, nested elements, aliases, and labeled communication relationships. Examples should remain plain PlantUML so generated diagrams can be validated with the existing PlantUML workflow.

### Collect deployment topology before generation

Deployment diagrams require topology context that may not be visible from Java source alone. The reference should first accept an image of an existing deployment diagram or topology sketch, or a file that describes the deployed system. When those sources are missing or incomplete, the question flow should ask for available deployment details before generation, including deployment environment, actors, services, CI/CD systems, nodes, execution environments, deployed components or artifacts, external systems, data stores, queues, protocols, ports, and infrastructure or network boundaries.

### Avoid unsupported infrastructure inference

The skill should not infer production infrastructure from sparse application code. Deployment topology should come from user input, provided images, system description files, repository documentation, configuration files, or explicit architecture artifacts when available. When details are unknown, the generated guidance should use placeholders or ask for clarification instead of inventing nodes or protocols.

### Preserve source and generated-output boundaries

Implementation should edit XML sources under `plinth-skills-generator/src/main/resources/`. Local generated skills can be refreshed with `./mvnw clean install -pl plinth-skills-generator` for validation. Public `skills/` release output should only be refreshed when release output is intentionally in scope.

## Risks / Trade-offs

- [Risk] Deployment guidance overlaps with C4 Container diagrams. -> Keep deployment guidance focused on runtime nodes, execution environments, artifacts, and infrastructure communication.
- [Risk] Agents invent infrastructure details not present in code. -> Require topology details to come from user input, configuration, documentation, or explicit architecture artifacts.
- [Risk] Deployment intake becomes too detailed for small systems. -> Prefer provided images or system description files, then ask only for missing topology details.
- [Risk] PlantUML examples become cloud-provider-specific. -> Provide provider-neutral PlantUML examples by default.
- [Risk] The "All diagrams" path drifts from the reference list. -> Update the question flow, reference mapping, `skills.xml`, and generated local output together.

## Migration Plan

1. Update `033-skill.xml` metadata, goal text, trigger list, and selected-reference mapping.
2. Update `033-architecture-diagrams-questions.xml` so UML Deployment Diagrams appear in the diagram selection flow.
3. Add deployment follow-up guidance that accepts a diagram image or system description file, then asks for missing runtime topology before generation.
4. Add a focused deployment PlantUML reference under `skill-references/`.
5. Include source-first topology intake, fallback questions, and reusable PlantUML deployment templates in the deployment reference.
6. Register the new reference for skill `033` in `skills.xml`.
7. Validate changed XML files with `xmllint --noout`.
8. Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills without refreshing public `skills/`.
9. Inspect generated local `033-architecture-diagrams/SKILL.md` and reference links for deployment selection, topology intake, "All diagrams" coverage, and reference links.
10. Run `./mvnw clean verify -pl plinth-skills-generator` before promoting the implementation.
11. Check `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; as of this change, `033-architecture-diagrams` is not listed, so no listed skill acceptance prompt is required unless that inventory changes.

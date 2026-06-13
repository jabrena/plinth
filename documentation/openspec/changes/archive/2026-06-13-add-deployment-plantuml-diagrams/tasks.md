# Tasks

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #847, the PlantUML deployment diagram reference, current `033-skill.xml`, architecture diagram question asset, focused architecture diagram references, and `skills.xml` registration.
- [x] 1.2 Confirm deployment diagram support belongs in the existing `architecture-diagram-skill-references` capability and does not require a separate OpenSpec capability.
- [x] 1.3 Identify generated local output paths to inspect after regeneration.

## 2. Skill Orchestration

- [x] 2.1 Update `skills-generator/src/main/resources/skill-indexes/033-skill.xml` metadata, description, goal text, trigger list, and selected-reference mapping to include UML Deployment Diagrams.
- [x] 2.2 Update `skills-generator/src/main/resources/skill-references/assets/questions/033-architecture-diagrams-questions.xml` so UML Deployment Diagrams are selectable from the main diagram-family question.
- [x] 2.3 Add deployment follow-up guidance that accepts a diagram image or system description file, then asks for missing runtime topology before diagram generation.
- [x] 2.4 Ensure the "All diagrams" path includes deployment diagrams in the selected-reference mapping.
- [x] 2.5 Keep the workflow instruction that unselected diagram-family references must not be read.

## 3. Focused Reference Guidance

- [x] 3.1 Add a focused deployment reference XML file under `skills-generator/src/main/resources/skill-references/`.
- [x] 3.2 Include PlantUML guidance for representing runtime nodes, execution environments, deployed artifacts or components, databases, external systems, and communication relationships.
- [x] 3.3 Include source-first topology intake guidance for diagram images and system description files.
- [x] 3.4 Include fallback questions for actors, services, CI/CD systems, deployment environment, infrastructure boundaries, protocols, ports, and known runtime relationships.
- [x] 3.5 Include a reusable PlantUML deployment diagram template with placeholders for actors, services, CI/CD systems, nodes, execution environments, artifacts or components, external systems, data stores, queues, interfaces, ports, boundaries, and labeled links.
- [x] 3.6 State that deployment topology must come from user input, provided images, system description files, repository documentation, configuration, or explicit architecture artifacts; do not invent unsupported infrastructure details.
- [x] 3.7 Add output organization and validation guardrails for deployment `.puml` files.

## 4. Generator Registration

- [x] 4.1 Register the deployment reference for skill `033` in `skills-generator/src/main/resources/skills.xml`.
- [x] 4.2 Confirm generated `033-architecture-diagrams/SKILL.md` lists the deployment reference link.
- [x] 4.3 Confirm generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or unintended cloud-provider/tooling requirements.

## 5. Validation

- [x] 5.1 Validate changed XML files with `xmllint --noout`.
- [x] 5.2 Run `./mvnw clean install -pl skills-generator` to regenerate local skills without refreshing public `skills/`.
- [x] 5.3 Inspect generated local `033-architecture-diagrams` output for deployment question flow, image/file intake, fallback topology questions, selected-reference mapping, "All diagrams" coverage, and reference links.
- [x] 5.4 Add or update the `033-architecture-diagrams` Gherkin acceptance test so the deployment diagram scenario uses `examples/diagrams/deployment/system-example.md` as the system description input.
- [x] 5.5 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; execute only a listed `033-architecture-diagrams` prompt if one exists after implementation changes.
- [x] 5.6 Run `./mvnw clean verify -pl skills-generator`.
- [x] 5.7 Run `openspec validate --all`.

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #817, Context Mapper PlantUML documentation, current `033-skill.xml`, architecture diagram question asset, split architecture diagram references, and `skills.xml` registration.
- [x] 1.2 Confirm bounded-context support belongs in the existing `architecture-diagram-skill-references` capability and does not require a separate OpenSpec capability.
- [x] 1.3 Identify generated local output paths to inspect after regeneration.

## 2. Skill Orchestration

- [x] 2.1 Update `skills-generator/src/main/resources/skill-indexes/033-skill.xml` metadata, description, goal text, trigger list, and selected-reference mapping to include bounded-context diagrams.
- [x] 2.2 Update `skills-generator/src/main/resources/skill-references/assets/questions/033-architecture-diagrams-questions.xml` so bounded-context diagrams are selectable from the main diagram-family question.
- [x] 2.3 Add bounded-context follow-up guidance that asks which repositories should be represented before diagram generation.
- [x] 2.4 Ensure the "All diagrams" path includes bounded-context diagrams in the selected-reference mapping.
- [x] 2.5 Keep the workflow instruction that unselected diagram-family references must not be read.

## 3. Focused Reference Guidance

- [x] 3.1 Add a focused bounded-context reference XML file under `skills-generator/src/main/resources/skill-references/`.
- [x] 3.2 Include PlantUML guidance for representing bounded contexts as first-class elements and relationships with direction and labels.
- [x] 3.3 Include DDD context-map relationship concepts when supported by repository context or user input.
- [x] 3.4 Include a repository inventory template that captures repository name or path, bounded context, domain or subdomain, owning team, application type, owned data store, exposed interfaces, consumed interfaces, and known relationships.
- [x] 3.5 Include a reusable PlantUML template for single-repository and multi-repository bounded-context maps.
- [x] 3.6 State that Context Mapper is a conceptual reference only and that the skill does not require CML parsing or external generator execution.
- [x] 3.7 Add output organization and validation guardrails for bounded-context `.puml` files.

## 4. Generator Registration

- [x] 4.1 Register the bounded-context reference for skill `033` in `skills-generator/src/main/resources/skills.xml`.
- [x] 4.2 Confirm generated `033-architecture-diagrams/SKILL.md` lists the bounded-context reference link.
- [x] 4.3 Confirm generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or unintended Context Mapper runtime requirements.

## 5. Validation

- [x] 5.1 Validate changed XML files with `xmllint --noout`.
- [x] 5.2 Run `./mvnw clean install -pl skills-generator` to regenerate local skills without refreshing public `skills/`.
- [x] 5.3 Inspect generated local `033-architecture-diagrams` output for bounded-context question flow, multi-repository intake, selected-reference mapping, "All diagrams" coverage, and reference links.
- [x] 5.4 Run `./mvnw clean verify -pl skills-generator`.
- [x] 5.5 Run `openspec validate --all`.

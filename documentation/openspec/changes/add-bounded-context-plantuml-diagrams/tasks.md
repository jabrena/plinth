## 1. Source Analysis

- [ ] 1.1 Review GitHub issue #817, Context Mapper PlantUML documentation, current `033-skill.xml`, architecture diagram question asset, split architecture diagram references, and `skills.xml` registration.
- [ ] 1.2 Confirm bounded-context support belongs in the existing `architecture-diagram-skill-references` capability and does not require a separate OpenSpec capability.
- [ ] 1.3 Identify generated local output paths to inspect after regeneration.

## 2. Skill Orchestration

- [ ] 2.1 Update `skills-generator/src/main/resources/skill-indexes/033-skill.xml` metadata, description, goal text, trigger list, and selected-reference mapping to include bounded-context diagrams.
- [ ] 2.2 Update `skills-generator/src/main/resources/skill-references/assets/questions/033-architecture-diagrams-questions.xml` so bounded-context diagrams are selectable from the main diagram-family question.
- [ ] 2.3 Ensure the "All diagrams" path includes bounded-context diagrams in the selected-reference mapping.
- [ ] 2.4 Keep the workflow instruction that unselected diagram-family references must not be read.

## 3. Focused Reference Guidance

- [ ] 3.1 Add a focused bounded-context reference XML file under `skills-generator/src/main/resources/skill-references/`.
- [ ] 3.2 Include PlantUML guidance for representing bounded contexts as first-class elements and relationships with direction and labels.
- [ ] 3.3 Include DDD context-map relationship concepts when supported by repository context or user input.
- [ ] 3.4 State that Context Mapper is a conceptual reference only and that the skill does not require CML parsing or external generator execution.
- [ ] 3.5 Add output organization and validation guardrails for bounded-context `.puml` files.

## 4. Generator Registration

- [ ] 4.1 Register the bounded-context reference for skill `033` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 4.2 Confirm generated `033-architecture-diagrams/SKILL.md` lists the bounded-context reference link.
- [ ] 4.3 Confirm generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or unintended Context Mapper runtime requirements.

## 5. Validation

- [ ] 5.1 Validate changed XML files with `xmllint --noout`.
- [ ] 5.2 Run `./mvnw clean install -pl skills-generator` to regenerate local skills without refreshing public `skills/`.
- [ ] 5.3 Inspect generated local `033-architecture-diagrams` output for bounded-context question flow, selected-reference mapping, "All diagrams" coverage, and reference links.
- [ ] 5.4 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 5.5 Run `openspec validate --all`.

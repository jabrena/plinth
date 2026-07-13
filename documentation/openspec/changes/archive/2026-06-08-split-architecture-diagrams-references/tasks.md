## 1. Source Analysis

- [x] 1.1 Review current `033-skill.xml`, `033-architecture-diagrams.xml`, related question templates, and generated local skill output.
- [x] 1.2 Review `111-java-maven-dependencies` and `112-java-maven-plugins` source patterns for question-flow inclusion, reference mapping, and `skills.xml` registration.
- [x] 1.3 Identify all C4 Code/Level 4 references in the architecture diagram XML sources and generated local output.

## 2. Skill Orchestration

- [x] 2.1 Create `plinth-skills-generator/src/main/resources/skill-references/assets/questions/033-architecture-diagrams-questions.xml`.
- [x] 2.2 Move diagram preference questions into the new question-flow asset and remove C4 Code/Level 4 options.
- [x] 2.3 Update `plinth-skills-generator/src/main/resources/skill-indexes/033-skill.xml` to include the question flow before reference reading.
- [x] 2.4 Add selection-to-reference mapping in `033-skill.xml` and instruct agents not to read unselected diagram-family references.

## 3. Focused Reference Split

- [x] 3.1 Split UML sequence guidance into `033-architecture-diagrams-uml-sequence.xml`.
- [x] 3.2 Split UML class guidance into `033-architecture-diagrams-uml-class.xml`.
- [x] 3.3 Split C4 guidance into `033-architecture-diagrams-c4.xml` and limit it to Context, Container, and Component diagrams.
- [x] 3.4 Split UML state-machine guidance into `033-architecture-diagrams-state-machine.xml`.
- [x] 3.5 Split ER diagram guidance into `033-architecture-diagrams-er.xml`.
- [x] 3.6 Add output organization and validation guardrails inside each focused diagram-family reference.
- [x] 3.7 Delete the monolithic `033-architecture-diagrams.xml` source after the split references fully replace it, and verify no `skills.xml` registration or generated local reference still depends on it.

## 4. Generator Registration

- [x] 4.1 Update `plinth-skills-generator/src/main/resources/skills.xml` so skill `033` registers the focused references.
- [x] 4.2 Confirm generated `033-architecture-diagrams/SKILL.md` lists the split reference links.
- [x] 4.3 Confirm generated references do not contain unresolved include markers, broken local reference paths, duplicated stale sections, or stale monolith references.

## 5. Validation

- [x] 5.1 Validate changed XML files with `xmllint --noout`.
- [x] 5.2 Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills without refreshing public `skills/`.
- [x] 5.3 Inspect generated local `033-architecture-diagrams` output for question flow, selected-reference mapping, guardrails, and C4 alignment.
- [x] 5.4 Run focused generator tests or `./mvnw clean verify -pl plinth-skills-generator` if source changes affect generator behavior or tests.
- [x] 5.5 Run `openspec validate --all`.

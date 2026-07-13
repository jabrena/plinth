## ADDED Requirements

### Requirement: Bounded-context diagram selection

The `033-architecture-diagrams` skill MUST offer bounded-context diagrams as a selectable diagram family in its architecture diagram question flow.

#### Scenario: Select bounded-context diagrams

- **GIVEN** a user asks to generate architecture diagrams
- **WHEN** the generated `033-architecture-diagrams/SKILL.md` asks "Question 1: What diagrams do you want to generate?"
- **THEN** bounded-context diagrams are listed as one of the available diagram families
- **AND** selecting bounded-context diagrams triggers bounded-context-specific follow-up or generation guidance

#### Scenario: Include bounded-context diagrams in all diagrams

- **GIVEN** a user selects "All diagrams" in the diagram question flow
- **WHEN** the skill maps selected diagram families to focused references
- **THEN** the bounded-context diagram reference is included with the other selected focused references

### Requirement: Multi-repository bounded-context intake

The `033-architecture-diagrams` skill MUST ask for the repositories in scope before generating bounded-context diagrams.

#### Scenario: Collect repositories for bounded-context diagrams

- **GIVEN** bounded-context diagrams are selected
- **WHEN** the skill collects bounded-context inputs
- **THEN** it asks which repositories should be represented in the diagram
- **AND** it accepts one or more repository names, URLs, or local paths
- **AND** it collects the bounded context, domain or subdomain, owning team, application type, owned data store, exposed interfaces, consumed interfaces, and known relationships for each repository when available

#### Scenario: Support single-repository and multi-repository context maps

- **GIVEN** bounded-context diagrams are selected
- **WHEN** the user provides only one repository
- **THEN** the workflow can still generate a bounded-context diagram for the known context and its external relationships
- **AND** when the user provides multiple repositories, the workflow represents all provided bounded contexts in the same context map

### Requirement: Bounded-context focused reference

The architecture diagram skill reference source MUST include focused bounded-context PlantUML guidance that is loaded only when bounded-context diagrams are selected or when all diagrams are selected.

#### Scenario: Register bounded-context reference for skill 033

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers a bounded-context diagram reference
- **AND** the reference is separate from the UML sequence, UML class, C4, UML state-machine, and ER diagram references

#### Scenario: Keep bounded-context responsibility clear

- **WHEN** the bounded-context reference is inspected
- **THEN** it states that it is used only after the central question flow selects bounded-context diagrams
- **AND** it focuses on DDD context-map concepts rather than C4 Container or Component modeling
- **AND** it does not require Context Mapper CML parsing or external Context Mapper generator execution

### Requirement: Bounded-context PlantUML output guidance

The bounded-context reference MUST guide agents to generate valid PlantUML that represents bounded contexts and their relationships clearly.

#### Scenario: Generate bounded-context PlantUML

- **GIVEN** bounded-context diagrams are selected
- **WHEN** the skill generates diagram guidance or output
- **THEN** bounded contexts are represented as first-class diagram elements
- **AND** relationships between bounded contexts include direction and labels when supported by project context or user input
- **AND** the guidance includes DDD relationship concepts such as upstream/downstream, shared kernel, partnership, customer/supplier, conformist, open host service, published language, and anticorruption layer where applicable
- **AND** the output remains valid PlantUML syntax

#### Scenario: Use bounded-context PlantUML template

- **GIVEN** the bounded-context reference is inspected
- **WHEN** diagram examples or templates are reviewed
- **THEN** the reference includes a reusable PlantUML template for multi-repository bounded-context maps
- **AND** the template groups or annotates bounded contexts by repository
- **AND** the template includes placeholders for repository name, bounded context name, owner, domain or subdomain, data store, interface mechanisms, and relationship labels
- **AND** the template can be adapted without requiring Context Mapper tooling

#### Scenario: Validate bounded-context diagram output

- **WHEN** bounded-context `.puml` files are generated
- **THEN** the reference provides validation expectations for PlantUML syntax, readability, and consistency with analyzed project context
- **AND** the existing Docker-based PlantUML validation workflow remains applicable

## MODIFIED Requirements

### Requirement: Focused diagram-family references

The architecture diagram skill reference source MUST be split into focused XML references for UML sequence, UML class, C4, UML state-machine, ER, and bounded-context diagram families.

#### Scenario: Register focused references for skill 033

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers multiple architecture diagram references
- **AND** those references include UML sequence, UML class, C4, UML state-machine, ER, and bounded-context diagram guidance
- **AND** skill `033` no longer depends on one monolithic `033-architecture-diagrams` reference as its only detailed source

#### Scenario: Keep each reference responsibility clear

- **WHEN** the split XML reference files are inspected
- **THEN** each reference owns one diagram-family responsibility
- **AND** each reference states that it is used only after the central question flow selects that diagram family
- **AND** shared guidance is not moved into a standalone validation-only reference

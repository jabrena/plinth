# architecture-diagram-skill-references Specification

## Purpose
TBD - created by archiving change split-architecture-diagrams-references. Update Purpose after archive.
## Requirements
### Requirement: Architecture diagram question orchestration

The `033-architecture-diagrams` skill MUST ask its diagram preference questions from the skill workflow before reading any diagram-family implementation reference.

#### Scenario: Ask diagram questions before loading references

- **WHEN** the generated `033-architecture-diagrams/SKILL.md` is inspected
- **THEN** its workflow includes the diagram question flow before any diagram reference selection step
- **AND** it instructs the agent to record selected diagram families before continuing
- **AND** it maps selections to the corresponding focused reference files

#### Scenario: Skip unselected diagram references

- **GIVEN** a user selects only a subset of diagram families
- **WHEN** the skill proceeds after the question flow
- **THEN** the workflow instructs the agent to read only references for the selected diagram families
- **AND** it instructs the agent not to read or apply unselected diagram-family references

### Requirement: Focused diagram-family references

The architecture diagram skill reference source MUST be split into focused XML references for UML sequence, UML class, C4, UML state-machine, ER, bounded-context, and UML Deployment Diagram families.

#### Scenario: Register focused references for skill 033

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers multiple architecture diagram references
- **AND** those references include UML sequence, UML class, C4, UML state-machine, ER, bounded-context, and UML Deployment Diagram guidance
- **AND** skill `033` no longer depends on one monolithic `033-architecture-diagrams` reference as its only detailed source

#### Scenario: Keep each reference responsibility clear

- **WHEN** the split XML reference files are inspected
- **THEN** each reference owns one diagram-family responsibility
- **AND** each reference states that it is used only after the central question flow selects that diagram family
- **AND** shared guidance is not moved into a standalone validation-only reference

### Requirement: Diagram-family guardrails

Each focused diagram-family reference MUST include output organization and validation guardrails appropriate to that diagram family.

#### Scenario: Validate generated diagram output per family

- **WHEN** a focused diagram-family reference is used to generate diagrams
- **THEN** the reference provides guidance for organizing the generated output
- **AND** it provides validation expectations for PlantUML syntax, readability, and consistency with analyzed project code or schema inputs
- **AND** it keeps those guardrails inside the selected diagram-family reference

#### Scenario: Avoid standalone output validation reference

- **WHEN** the split reference files are inspected
- **THEN** there is no standalone `033-architecture-diagrams-output-validation.xml` reference
- **AND** output and validation guardrails are present in each diagram-family reference

### Requirement: C4 level alignment

The architecture diagram skill MUST support C4 Context, Container, and Component diagrams only, and MUST NOT offer or generate C4 Code/Level 4 diagrams.

#### Scenario: Inspect C4 question and reference guidance

- **WHEN** the question flow, C4 reference, and generated local skill output are inspected
- **THEN** C4 options mention only Context, Container, and Component diagrams
- **AND** they do not offer complete C4 model options that include Code/Level 4 diagrams
- **AND** C4 examples and constraints do not instruct the agent to generate Code/Level 4 diagrams

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Validate source and local generation

- **WHEN** the change is implemented
- **THEN** changed XML files are validated with `xmllint --noout`
- **AND** local skill generation succeeds with `./mvnw clean install -pl skills-generator`
- **AND** the generated local `033-architecture-diagrams/SKILL.md` includes deployment diagram selection, topology intake, selected-reference mapping, and split reference links
- **AND** generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or lingering references to removed monolithic guidance

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

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

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
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

### Requirement: Deployment diagram selection

The `033-architecture-diagrams` skill MUST offer UML Deployment Diagrams as a selectable diagram family in its architecture diagram question flow.

#### Scenario: Select deployment diagrams

- **GIVEN** a user asks to generate architecture diagrams
- **WHEN** the generated `033-architecture-diagrams/SKILL.md` asks "Question 1: What diagrams do you want to generate?"
- **THEN** UML Deployment Diagrams are listed as one of the available diagram families
- **AND** selecting UML Deployment Diagrams triggers deployment-specific follow-up or generation guidance

#### Scenario: Include deployment diagrams in all diagrams

- **GIVEN** a user selects "All diagrams" in the diagram question flow
- **WHEN** the skill maps selected diagram families to focused references
- **THEN** the deployment diagram reference is included with the other selected focused references

### Requirement: Deployment topology intake

The `033-architecture-diagrams` skill MUST accept deployment source material before asking for missing deployment topology details.

#### Scenario: Use an existing deployment diagram image

- **GIVEN** UML Deployment Diagrams are selected
- **AND** the user provides an image containing an existing deployment diagram or topology sketch
- **WHEN** the deployment reference collects inputs
- **THEN** it extracts visible deployment elements, boundaries, and relationships from the image
- **AND** it asks for clarification when labels, direction, protocols, ports, environments, or deployment boundaries are missing or ambiguous
- **AND** it uses the image only as source material for creating valid PlantUML deployment syntax

#### Scenario: Use a system description file

- **GIVEN** UML Deployment Diagrams are selected
- **AND** the user provides a file that describes the system deployment
- **WHEN** the deployment reference collects inputs
- **THEN** it extracts supported topology facts from the file such as actors, services, CI/CD systems, runtime nodes, deployed artifacts or components, external systems, data stores, queues, protocols, ports, and boundaries
- **AND** it asks for clarification when the file does not provide enough information to create the diagram

#### Scenario: Ask for deployment details when no sufficient source is provided

- **GIVEN** UML Deployment Diagrams are selected
- **AND** no image or system description file is provided
- **WHEN** the skill collects deployment inputs
- **THEN** it asks for the actors or people that interact with the deployed system
- **AND** it asks for services, applications, deployed components, artifacts, agents, CI/CD systems, and external systems
- **AND** it asks for runtime nodes, execution environments, clouds, databases, storage, queues, files or folders, packages or frames, interfaces, ports, protocols, and communication links when relevant
- **AND** it asks for infrastructure, network, environment, or trust boundaries when available

#### Scenario: Collect deployment topology inputs

- **GIVEN** UML Deployment Diagrams are selected
- **WHEN** the skill collects deployment inputs
- **THEN** it asks for the deployment environment or environments in scope
- **AND** it asks for runtime nodes, execution environments, deployed components or artifacts, actors, services, CI/CD systems, external systems, data stores, communication protocols, ports when relevant, and infrastructure or network boundaries when available
- **AND** it allows the user to provide topology details from repository documentation, configuration files, deployment descriptors, or explicit architecture artifacts

#### Scenario: Avoid unsupported deployment inference

- **GIVEN** UML Deployment Diagrams are selected
- **WHEN** deployment topology details are missing or ambiguous
- **THEN** the workflow asks for clarification or uses explicit placeholders
- **AND** it does not invent infrastructure nodes, protocols, ports, cloud services, or network boundaries that are not supported by user input or repository context

### Requirement: Deployment focused reference

The architecture diagram skill reference source MUST include focused UML Deployment Diagram PlantUML guidance that is loaded only when deployment diagrams are selected or when all diagrams are selected.

#### Scenario: Register deployment reference for skill 033

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers a deployment diagram reference
- **AND** the reference is separate from the UML sequence, UML class, C4, UML state-machine, ER, and bounded-context diagram references

#### Scenario: Keep deployment responsibility clear

- **WHEN** the deployment reference is inspected
- **THEN** it states that it is used only after the central question flow selects UML Deployment Diagrams
- **AND** it focuses on runtime topology, nodes, execution environments, deployed artifacts or components, and communication paths
- **AND** it does not replace or duplicate C4 Context, Container, or Component modeling guidance

### Requirement: Deployment PlantUML output guidance

The deployment reference MUST guide agents to generate valid PlantUML that represents deployment topology clearly.

#### Scenario: Generate deployment PlantUML

- **GIVEN** UML Deployment Diagrams are selected
- **WHEN** the skill generates diagram guidance or output
- **THEN** deployment nodes and execution environments are represented as first-class diagram elements
- **AND** deployed artifacts, components, services, databases, and external systems are represented when supported by the selected scope
- **AND** communication relationships include direction and labels for protocols, ports, or integration purpose when supported by project context or user input
- **AND** the output remains valid PlantUML syntax

#### Scenario: Use deployment PlantUML template

- **GIVEN** the deployment reference is inspected
- **WHEN** diagram examples or templates are reviewed
- **THEN** the reference includes a reusable PlantUML template for deployment diagrams
- **AND** the template includes placeholders for deployment environment, actors, services, CI/CD systems, nodes, execution environments, artifacts or components, databases, storage, queues, interfaces, ports, external systems, boundaries, and labeled links
- **AND** the template remains provider-neutral unless the user supplies provider-specific topology

#### Scenario: Validate deployment diagram output

- **WHEN** deployment `.puml` files are generated
- **THEN** the reference provides validation expectations for PlantUML syntax, readability, and consistency with analyzed project or user-provided deployment context
- **AND** the existing Docker-based PlantUML validation workflow remains applicable


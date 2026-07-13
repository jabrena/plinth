## ADDED Requirements

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

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
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

## MODIFIED Requirements

### Requirement: Focused diagram-family references

The architecture diagram skill reference source MUST be split into focused XML references for UML sequence, UML class, C4, UML state-machine, ER, bounded-context, and UML Deployment Diagram families.

#### Scenario: Register focused references for skill 033

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill `033` registers multiple architecture diagram references
- **AND** those references include UML sequence, UML class, C4, UML state-machine, ER, bounded-context, and UML Deployment Diagram guidance
- **AND** skill `033` no longer depends on one monolithic `033-architecture-diagrams` reference as its only detailed source

#### Scenario: Keep each reference responsibility clear

- **WHEN** the split XML reference files are inspected
- **THEN** each reference owns one diagram-family responsibility
- **AND** each reference states that it is used only after the central question flow selects that diagram family
- **AND** shared guidance is not moved into a standalone validation-only reference

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Validate source and local generation

- **WHEN** the change is implemented
- **THEN** changed XML files are validated with `xmllint --noout`
- **AND** local skill generation succeeds with `./mvnw clean install -pl plinth-skills-generator`
- **AND** the generated local `033-architecture-diagrams/SKILL.md` includes deployment diagram selection, topology intake, selected-reference mapping, and split reference links
- **AND** generated references have no unresolved include markers, broken local reference paths, duplicated stale sections, or lingering references to removed monolithic guidance

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

Feature: Validate changes from usage of architecture diagrams skill

Background:
  Given the skill "033-architecture-diagrams"

@acceptance-test
Scenario: Generate a deployment diagram from a system description file
  Given the system description file "examples/diagrams/deployment/system-example.md"
  And the user request is "Generate a UML Deployment Diagram from examples/diagrams/deployment/system-example.md"
  And the requested PlantUML output path is "examples/diagrams/deployment/system-deployment.puml"
  And the folder "examples/diagrams/deployment" has no git changes
  When the skill "033-architecture-diagrams" is applied with "UML Deployment Diagrams"
  Then the skill asks "Question 1: What diagrams do you want to generate?"
  And "UML Deployment Diagrams" is one of the available diagram families
  And the skill uses "examples/diagrams/deployment/system-example.md" as the deployment system description input
  And the skill reads the focused reference "references/033-architecture-diagrams-deployment.md"
  And the skill does not read unselected diagram-family references unless "All diagrams" was selected
  And the skill writes the generated PlantUML deployment file to "examples/diagrams/deployment/system-deployment.puml"
  And the generated PlantUML deployment diagram is verified against "examples/diagrams/deployment/expected-system-deployment.puml" only after generation as a semantic reference, without requiring the same PlantUML syntax, aliases, ordering, or layout
  And the generated PlantUML deployment diagram includes the expected actors, delivery pipeline, Azure edge, Azure Kubernetes Service, aggregate Quarkus workloads, PostgreSQL data stores, Kafka topics, shared Azure platform services, external systems, and labeled communication relationships in a reasonable high-level deployment view
  And the generated PlantUML deployment diagram shows the GitOps or CD pipeline deploying manifests and validated images to Azure Kubernetes Service while hiding unnecessary AKS internal complexity
  And the generated PlantUML output remains valid deployment diagram syntax
  And the skill renders the generated deployment diagram as "examples/diagrams/deployment/system-deployment.png"
  And any git changes produced during skill execution and verification are reset

Feature: Validate changes from usage of Java Hexagonal architecture skill

Background:
  Given the skill "707-technologies-hexagonal-architecture"
  And the OpenSpec project path "examples/frameworks"
  And the OpenSpec change path "examples/frameworks/openspec/changes/add-framework-example-capabilities"
  And the implementation target directory "examples/frameworks/generated"
  And the folder "examples/frameworks/generated" has no git changes

@acceptance-test
Scenario: Execute Quarkus OpenSpec change with Hexagonal architecture boundaries
  Given the OpenSpec change "add-framework-example-capabilities" contains "proposal.md", "design.md", "tasks.md", and "specs/framework-sum-examples/spec.md"
  And the OpenSpec change is validated with "openspec validate --all" from "examples/frameworks"
  And the OpenSpec change requires Quarkus implementation with Hexagonal architecture boundaries
  And the user request is "Execute the OpenSpec change at examples/frameworks/openspec/changes/add-framework-example-capabilities in examples/frameworks/generated using Hexagonal architecture"
  And the local generated skill path ".agents/skills/707-technologies-hexagonal-architecture"
  When the skill ".agents/skills/707-technologies-hexagonal-architecture" is applied to the OpenSpec change and Quarkus project
  Then the skill reads "references/707-technologies-hexagonal-architecture.md"
  And the skill loads the selected OpenSpec change as the implementation and verification contract
  And the skill identifies "examples/frameworks/generated" as the only implementation target
  And the skill maps the intended Quarkus Hexagonal architecture packages for domain, application, inbound port, REST driving adapter, DTO boundary, and configuration concerns
  And the skill requires the REST controller to call an application inbound port instead of implementing the sum rule directly
  And the skill requires domain and application code to avoid imports from controller, DTO, Quarkus, Jakarta REST, persistence, messaging, database, or infrastructure packages
  And the skill verifies dependency direction with packages, classes, imports, module dependencies, build files, or tests inspected after implementation
  And the skill detects or explicitly rules out application use-case code depending on concrete adapter implementations instead of ports or interfaces
  And the skill keeps Quarkus runtime wiring recommendations scoped to adapter or configuration boundaries and routes detailed framework implementation concerns to the matching Quarkus skills when needed
  And the skill accounts for the OpenSpec Java 25, virtual-thread configuration, and structured-concurrency requirements without moving those concerns into the domain rule
  And the skill distinguishes confirmed violations from assumptions, inferred naming conventions, and missing context
  And the skill proposes boundary-preserving remediation such as extracting ports, moving adapter DTOs or persistence entities outward, adding mappers, inverting dependencies, splitting modules, or relocating misplaced behavior
  And the skill proposes pure domain/application tests that do not require Quarkus boot and REST adapter tests focused on routing, validation, serialization, and status codes
  And the skill proposes ArchUnit-style architecture-test guidance only as an optional verification approach when it fits the Quarkus project
  And the skill routes ArchUnit or other Maven dependency addition decisions to "111-java-maven-dependencies"
  And "openspec validate --all" is run from "examples/frameworks" after skill execution
  And any git changes produced under "examples/frameworks/generated" during skill execution and verification are reset

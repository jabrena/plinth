Feature: Validate changes from usage of Java Hexagonal architecture skill

Background:
  Given the skill "707-technologies-hexagonal-architecture"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Review Java project boundaries for Hexagonal architecture dependency direction
  Given a Java project with domain, application, adapter, and infrastructure packages
  And the user request is "Review this Java project for Hexagonal architecture boundary violations and suggest optional architecture tests"
  And the local generated skill path ".agents/skills/707-technologies-hexagonal-architecture"
  And the folder "examples" has no git changes
  When the skill ".agents/skills/707-technologies-hexagonal-architecture" is applied to the Java project
  Then the skill reads "references/707-technologies-hexagonal-architecture.md"
  And the skill maps domain models, domain services, application or use-case services, inbound ports, outbound ports, driving adapters, driven adapters, and infrastructure concerns using code evidence
  And the skill verifies dependency direction with packages, classes, imports, module dependencies, build files, or tests inspected
  And the skill detects or explicitly rules out domain independence violations such as framework, persistence, web, messaging, database, or infrastructure APIs leaking into domain code
  And the skill detects or explicitly rules out application use-case code depending on concrete adapter implementations instead of ports or interfaces
  And the skill detects or explicitly rules out adapter boundaries where adapters depend on each other unnecessarily or domain/application code depends outward on adapters
  And the skill distinguishes confirmed violations from assumptions, inferred naming conventions, and missing context
  And the skill proposes boundary-preserving remediation such as extracting ports, moving adapter DTOs or persistence entities outward, adding mappers, inverting dependencies, splitting modules, or relocating misplaced behavior
  And the skill proposes ArchUnit-style architecture-test guidance only as an optional verification approach when it fits the project
  And the skill routes ArchUnit or other Maven dependency addition decisions to "111-java-maven-dependencies"
  And the skill avoids Spring Boot, Quarkus, or Micronaut runtime wiring recommendations unless the user explicitly requests framework implementation details
  And the folder "examples" has no git changes unless the user explicitly requested architecture code edits
  And any git changes produced during skill execution and verification are reset

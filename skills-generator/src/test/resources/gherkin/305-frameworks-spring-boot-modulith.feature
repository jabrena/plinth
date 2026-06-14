Feature: Validate changes from usage of Spring Modulith skill

Background:
  Given the skill "305-frameworks-spring-boot-modulith"

@acceptance-test
Scenario: Add Spring Modulith verification to the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Add Spring Modulith module verification and documentation guidance"
  And the local generated skill path ".agents/skills/305-frameworks-spring-boot-modulith"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/305-frameworks-spring-boot-modulith" is applied to the example project
  Then the skill reads "references/305-frameworks-spring-boot-modulith.md"
  And "./mvnw compile" or "mvn compile" is run before applying Spring Modulith changes
  And the skill stops without changes if compilation fails
  And the skill confirms the project is a Spring Boot application before adding Spring Modulith guidance
  And the skill aligns dependencies with Spring Boot "4.0.x" and the Spring Modulith compatibility matrix
  And the skill identifies business-capability modules, public APIs, internal packages, named interfaces, and allowed dependencies
  And the skill adds or recommends "ApplicationModules.verify()" architecture verification
  And the skill adds or recommends "@ApplicationModuleTest" coverage for module boundaries when useful
  And the skill reports domain event, generated documentation, actuator, observability, and event publication registry follow-ups when they are relevant
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

Feature: Validate changes from usage of Spring Modulith skill

Background:
  Given the skill "305-frameworks-spring-boot-modulith" is available
  And the Spring Boot framework version baseline is "4.0.x"

@acceptance-test
Scenario: Add Spring Modulith verification to Spring Boot project with proper error handling
  Given the example project "examples/frameworks/spring-boot" exists and is a valid Spring Boot application
  And the user request is "Add Spring Modulith module verification and documentation guidance"
  And the local generated skill path ".agents/skills/305-frameworks-spring-boot-modulith/SKILL.md" exists
  And the working directory is "examples/frameworks/spring-boot"
  And there are no uncommitted git changes in the working directory
  And the project compiles successfully with "./mvnw compile"
  When the skill ".agents/skills/305-frameworks-spring-boot-modulith" is applied to the example project
  Then the skill must verify compilation with "./mvnw compile" before making any changes
  And the skill must stop without changes if compilation fails
  And the skill must confirm the project is a Spring Boot application by checking for @SpringBootApplication
  And the skill must add Spring Modulith dependencies compatible with the detected Spring Boot version
  And the project must contain dependency "spring-modulith-starter-core" with compatible version
  And the project must contain test dependency "spring-modulith-starter-test" with compatible version
  And the project must contain appropriate test dependencies for Spring Boot 4.x ("spring-boot-starter-test")
  And the skill must identify and create logical business-capability modules with proper package structure
  And each module must have a "package-info.java" file documenting its purpose and dependencies
  And the skill must create a test class with "ApplicationModules.verify()" for architecture verification
  And the ApplicationModules.verify() test must pass without cyclic dependency errors
  And the skill must attempt to add "@ApplicationModuleTest" coverage for module boundaries
  And if @ApplicationModuleTest fails due to module isolation, the skill must document why and provide alternatives
  And the skill must ensure Spring Boot test annotations work correctly with the added dependencies
  And Spring Modulith must generate documentation in "target/spring-modulith-docs/" directory
  And the generated documentation must include PlantUML diagrams for module structure
  And "./mvnw clean verify" must complete successfully after all changes
  And the skill must provide clear follow-up recommendations for domain events, actuator exposure, and observability
  And when git changes are reset, the project must return to its original working state
  And the original project build must still work after rollback verification

Feature: Validate changes from usage of Gatling performance testing skill

Background:
  Given the skill "152-java-performance-gatling"

@acceptance-test
Scenario: Add a Maven-based Gatling simulation to the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Add Gatling performance testing for the sum API"
  And the local generated skill path ".agents/skills/152-java-performance-gatling"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/152-java-performance-gatling" is applied to the example project
  Then the skill reads "references/152-java-performance-gatling.md"
  And the skill verifies the project is Maven-based and prefers "./mvnw"
  And the skill inspects the existing dependency and plugin management style before editing "pom.xml"
  And the skill adds Gatling dependencies with test scope and configures "io.gatling:gatling-maven-plugin"
  And the skill creates a Java Gatling simulation under "src/test/java"
  And optional Gatling resources are placed under "src/test/resources" only when needed
  And the skill documents how to run "./mvnw gatling:test"
  And the skill uses "-Dgatling.simulationClass=<FullyQualifiedClassName>" when non-interactive simulation selection is required
  And the skill reports the generated Gatling report location or the reason the run could not be completed
  And any git changes produced during skill execution and verification are reset

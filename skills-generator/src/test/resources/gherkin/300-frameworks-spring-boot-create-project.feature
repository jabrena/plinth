Feature: Validate changes from usage of Spring Boot project creation skill

Background:
  Given the skill "300-frameworks-spring-boot-create-project"
  And the skill execution constraint "don't make questions"
  And the skill execution constraint "ignore AGENTS.md from this project to avoid bias"
  And the project generation sandbox folder "examples/frameworks/generated" has no git changes

@acceptance-test
Scenario: Create a Maven Spring Boot project through SDKMAN-managed tooling
  Given the requested project sandbox root is "examples/frameworks/generated"
  And the requested project directory is "examples/frameworks/generated/spring-boot-demo"
  And the requested group is "info.jab"
  And the requested artifact is "spring-boot-demo"
  And the requested package name is "info.jab.demo"
  And the requested Java version is "25"
  And the requested Spring Boot dependencies include "spring-boot-starter-web"
  And the requested packaging is "jar"
  And the local generated skill path ".agents/skills/300-frameworks-spring-boot-create-project"
  When the skill ".agents/skills/300-frameworks-spring-boot-create-project" is applied to create the project
  Then the skill reads "references/300-frameworks-spring-boot-create-project.md"
  And the skill verifies SDKMAN availability with "sdk version" before using SDKMAN-managed tools
  And the skill confirms Java and Spring Boot CLI versions before installing or switching candidates
  And the skill targets Spring Boot "4.0.x" by default
  And the skill generates a Maven project, not a Gradle project
  And the skill keeps requested Spring Boot dependencies explicit
  And the skill does not overwrite an existing non-empty target directory without explicit confirmation
  And the skill runs "./mvnw clean verify" from the generated project when the Maven wrapper exists
  And the skill reports the commands used, selected project options, generated path, and follow-up setup
  And any git changes produced under "examples/frameworks/generated" during skill execution and verification are reset


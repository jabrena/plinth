Feature: Validate changes from usage of Micronaut project creation skill

Background:
  Given the skill "500-frameworks-micronaut-create-project"

@acceptance-test
Scenario: Create a Maven Micronaut project through SDKMAN-managed tooling
  Given the requested project sandbox root is "examples/frameworks/generated"
  And the requested project directory is "examples/frameworks/generated/micronaut-demo"
  And the requested group is "info.jab"
  And the requested artifact is "micronaut-demo"
  And the requested package name is "info.jab.demo"
  And the requested Java version is "25"
  And the requested Micronaut application type is "default"
  And the requested Micronaut features include "jackson-databind"
  And the local generated skill path ".agents/skills/500-frameworks-micronaut-create-project"
  And the folder "examples/frameworks/generated" has no git changes
  When the skill ".agents/skills/500-frameworks-micronaut-create-project" is applied to create the project
  Then the skill reads "references/500-frameworks-micronaut-create-project.md"
  And the skill verifies SDKMAN availability with "sdk version" before using SDKMAN-managed tools
  And the skill confirms Java and Micronaut CLI versions before installing or switching candidates
  And the skill targets the current Micronaut "4.x" line by default
  And the skill generates a Maven project, not a Gradle project
  And the skill keeps requested Micronaut features explicit
  And the skill does not overwrite an existing non-empty target directory without explicit confirmation
  And the skill runs "./mvnw clean verify" from the generated project when the Maven wrapper exists
  And the skill reports the commands used, selected project options, generated path, and follow-up setup
  And any git changes produced under "examples/frameworks/generated" during skill execution and verification are reset

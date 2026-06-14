Feature: Validate changes from usage of Java design patterns skill

Background:
  Given the skill "123-java-design-patterns"

@acceptance-test
Scenario: Recommend REST API patterns from a concrete design pressure
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Review the REST controller design and recommend Java design patterns only where useful"
  And the local generated skill path ".agents/skills/123-java-design-patterns"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/123-java-design-patterns" is applied to the example project
  Then the skill identifies the concrete design pressure before naming any pattern
  And the skill reads "references/123-rest-api-patterns.md"
  And the skill does not read unrelated pattern references unless the user request requires them
  And the skill recommends the smallest useful pattern or explicitly recommends no pattern when simple code is enough
  And each recommended pattern includes benefit, cost, and when-not-to-use guidance
  And the skill keeps code unchanged unless the user explicitly asks for implementation
  And the skill reports remaining trade-offs and validation that would be required for code changes
  And the folder "examples/frameworks/spring-boot" has no git changes

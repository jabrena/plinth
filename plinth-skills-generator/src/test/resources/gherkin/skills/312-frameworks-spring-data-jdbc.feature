Feature: Validate changes from usage of frameworks Spring data JDBC skill

Background:
  Given the skill "312-frameworks-spring-data-jdbc"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Apply frameworks Spring data JDBC guidance from a concrete Java request
  Given the user request is "Review a Java project and apply frameworks Spring data JDBC guidance"
  And the local generated skill path ".agents/skills/312-frameworks-spring-data-jdbc"
  And the folder "examples" has no git changes
  When the skill ".agents/skills/312-frameworks-spring-data-jdbc" is applied to the request
  Then the skill follows the instructions in its generated "SKILL.md" file
  And the skill reads only the reference files required by the request
  And the skill asks concise clarifying questions when required inputs are missing
  And the skill keeps recommendations and edits scoped to the user request
  And the skill reports the validation or review steps needed before promoting changes
  And the folder "examples" has no git changes unless the user explicitly requested edits
  And any git changes produced during skill execution and verification are reset

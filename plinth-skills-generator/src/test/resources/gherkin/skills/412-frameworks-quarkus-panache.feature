Feature: Validate changes from usage of frameworks Quarkus panache skill

Background:
  Given the skill "412-frameworks-quarkus-panache"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Apply frameworks Quarkus panache guidance from a concrete Java request
  Given the user request is "Review a Java project and apply frameworks Quarkus panache guidance"
  And the local generated skill path ".agents/skills/412-frameworks-quarkus-panache"
  And the folder "examples" has no git changes
  When the skill ".agents/skills/412-frameworks-quarkus-panache" is applied to the request
  Then the skill follows the instructions in its generated "SKILL.md" file
  And the skill reads only the reference files required by the request
  And the skill asks concise clarifying questions when required inputs are missing
  And the skill keeps recommendations and edits scoped to the user request
  And the skill reports the validation or review steps needed before promoting changes
  And the folder "examples" has no git changes unless the user explicitly requested edits
  And any git changes produced during skill execution and verification are reset

Feature: Validate changes from usage of java observability logging skill

Background:
  Given the skill "181-java-observability-logging"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Apply java observability logging guidance from a concrete Java request
  Given the user request is "Review a Java project and apply java observability logging guidance"
  And the local generated skill path ".agents/skills/181-java-observability-logging"
  And the folder "examples" has no git changes
  When the skill ".agents/skills/181-java-observability-logging" is applied to the request
  Then the skill follows the instructions in its generated "SKILL.md" file
  And the skill reads only the reference files required by the request
  And the skill asks concise clarifying questions when required inputs are missing
  And the skill keeps recommendations and edits scoped to the user request
  And the skill reports the validation or review steps needed before promoting changes
  And the folder "examples" has no git changes unless the user explicitly requested edits
  And any git changes produced during skill execution and verification are reset

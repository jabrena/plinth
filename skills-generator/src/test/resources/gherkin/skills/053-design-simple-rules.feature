Feature: Validate changes from usage of simple design rules skill

Background:
  Given the skill "053-design-simple-rules"

@acceptance-test
Scenario: Evaluate Java refactoring options with ordered simple design rules
  Given the user request is "Apply simple design rules to choose between refactoring options for a Java order validation service"
  And the local generated skill path ".agents/skills/053-design-simple-rules"
  When the skill ".agents/skills/053-design-simple-rules" is applied to the Java design request
  Then the skill reads "references/053-design-simple-rules.md"
  And the analysis evaluates the rules in this order: passes the tests, reveals intention, has no duplication, and has the fewest elements
  And the analysis explains that passing tests comes before design cleanup
  And the analysis identifies tests, characterization coverage, build checks, or manual verification as the correctness boundary
  And the analysis explains that clarity and revealed intention take priority over abstraction for its own sake
  And the analysis reduces duplication only when the result preserves or improves clarity
  And the analysis treats fewer elements as a final simplification pressure after correctness, clarity, and duplication have been addressed
  And the recommendation names the selected Java design or refactoring option with the rule-order tradeoff
  And the recommendation reports skipped checks, missing tests, or remaining risks when verification is incomplete

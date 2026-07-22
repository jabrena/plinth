Feature: Validate changes from usage of Assumption Analysis skill

Background:
  Given the skill "023-assumption-analysis"

@acceptance-test
Scenario: State assumptions as falsifiable claims distinct from unknowns
  Given the user request is "Surface the assumptions and unknowns behind this order-status problem using @023-assumption-analysis"
  And the local generated skill path ".agents/skills/023-assumption-analysis"
  When the skill ".agents/skills/023-assumption-analysis" is applied to the assumption-analysis request
  Then the skill reads "references/023-assumption-analysis.md"
  And the recommendation states each assumption as a falsifiable claim believed true but not yet verified
  And the recommendation lists unknowns separately from assumptions
  And the recommendation avoids mixing assumptions and unknowns into one undifferentiated list

@acceptance-test
Scenario: Prioritize assumptions and unknowns and target a validation plan at the riskiest items
  Given the user request is "Build a validation plan for the payment API latency and shipping order-ID assumptions using @023-assumption-analysis"
  And the local generated skill path ".agents/skills/023-assumption-analysis"
  When the skill ".agents/skills/023-assumption-analysis" ranks the assumptions and unknowns
  Then the skill reads "references/023-assumption-analysis.md"
  And the recommendation ranks assumptions and unknowns by impact if wrong and by current confidence
  And the recommendation defines a validation plan naming how and when each high-impact, low-confidence item will be checked
  And the recommendation avoids a validation plan that only restates the assumption as a generic to-do

@acceptance-test
Scenario: Flag a vague assumption for a clarifying question instead of inventing a validation step
  Given the user request is "Analyze assumptions for this problem: the payment system is probably fine, using @023-assumption-analysis"
  And the local generated skill path ".agents/skills/023-assumption-analysis"
  When the skill ".agents/skills/023-assumption-analysis" evaluates the vague assumption description
  Then the skill reads "references/023-assumption-analysis.md"
  And the recommendation does not invent an assumption, unknown, or validation step from the vague input
  And the recommendation flags the gap for a clarifying question

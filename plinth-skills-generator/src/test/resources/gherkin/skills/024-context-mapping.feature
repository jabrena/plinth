Feature: Validate changes from usage of Context Mapping skill

Background:
  Given the skill "024-context-mapping"

@acceptance-test
Scenario: Identify existing systems beyond the one expected to change
  Given the user request is "Map the systems and integrations around this order-status problem using @024-context-mapping"
  And the local generated skill path ".agents/skills/024-context-mapping"
  When the skill ".agents/skills/024-context-mapping" is applied to the context-mapping request
  Then the skill reads "references/024-context-mapping.md"
  And the recommendation identifies existing systems that already touch the problem area, not only the system expected to change
  And the recommendation maps integrations and data flows between the identified systems

@acceptance-test
Scenario: Name ownership and external dependencies outside the team's direct control
  Given the user request is "Identify ownership and external dependencies for the shipping-carrier gateway using @024-context-mapping"
  And the local generated skill path ".agents/skills/024-context-mapping"
  When the skill ".agents/skills/024-context-mapping" evaluates ownership and external dependencies
  Then the skill reads "references/024-context-mapping.md"
  And the recommendation names an owning team or role for each identified system or integration when known
  And the recommendation identifies external dependencies outside the team's direct control, such as third-party services or contracts

@acceptance-test
Scenario: Flag unclear ownership for a clarifying question instead of guessing a team
  Given the user request is "Map context for this problem: the systems are all connected to each other somehow, using @024-context-mapping"
  And the local generated skill path ".agents/skills/024-context-mapping"
  When the skill ".agents/skills/024-context-mapping" evaluates the vague context description
  Then the skill reads "references/024-context-mapping.md"
  And the recommendation does not invent a system, integration, owner, or external dependency from the vague input
  And the recommendation flags the gap for a clarifying question

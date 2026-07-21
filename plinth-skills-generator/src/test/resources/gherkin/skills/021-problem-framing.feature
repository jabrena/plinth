Feature: Validate changes from usage of Problem Framing skill

Background:
  Given the skill "021-problem-framing"

@acceptance-test
Scenario: Separate the problem statement from a proposed solution
  Given the user request is "Frame this problem: we need to build a unified order-status microservice, using @021-problem-framing"
  And the local generated skill path ".agents/skills/021-problem-framing"
  When the skill ".agents/skills/021-problem-framing" is applied to the problem-framing request
  Then the skill reads "references/021-problem-framing.md"
  And the analysis strips the proposed solution out of the problem statement and restates it as an observable gap
  And the recommendation states a Problem statement independent of any specific solution, technology, or implementation
  And the recommendation reports Current state as observable facts, not opinions or assumed causes
  And the recommendation reports Desired state as an outcome or capability, not a specific mechanism

@acceptance-test
Scenario: Identify stakeholders beyond the requester and verifiable success criteria
  Given the user request is "Identify stakeholders and success criteria for this order-status support problem using @021-problem-framing"
  And the local generated skill path ".agents/skills/021-problem-framing"
  When the skill ".agents/skills/021-problem-framing" evaluates the stakeholders and success criteria
  Then the skill reads "references/021-problem-framing.md"
  And the recommendation identifies stakeholders affected by, accountable for, or informed about the problem, not only the requester
  And the recommendation defines success criteria that are observable or measurable
  And the recommendation avoids success criteria stated only as aspirational adjectives

@acceptance-test
Scenario: Flag a vague problem statement for a clarifying question instead of inventing content
  Given the user request is "Frame this problem: the system is bad and slow, using @021-problem-framing"
  And the local generated skill path ".agents/skills/021-problem-framing"
  When the skill ".agents/skills/021-problem-framing" evaluates the vague problem description
  Then the skill reads "references/021-problem-framing.md"
  And the recommendation does not invent a problem statement, current state, desired state, stakeholder, or success criterion from the vague input
  And the recommendation flags the vague field for a clarifying question

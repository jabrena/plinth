Feature: Validate robot-architect agent

Background:
  Given the agent prompt file ".cursor/agents/robot-architect.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Explore architecture without implementing application code
  Given the user request is "Explore design alternatives for a Java service and create architecture outputs"
  And the agent prompt source ".cursor/agents/robot-architect.md" is read before execution
  When the agent "robot-architect" is applied to the request
  Then the agent clarifies goals, constraints, assumptions, unknowns, and success criteria
  And the agent compares viable design alternatives before recommending a direction
  And the agent obtains approval before treating a design direction as selected
  And the agent uses ADR and diagram skills only when justified by the approved design
  And the agent does not implement application code, edit tests, or perform delivery work
  And the agent reports outputs, traceability, unresolved questions, and handoff constraints
  And any git changes produced during agent execution and verification are reset

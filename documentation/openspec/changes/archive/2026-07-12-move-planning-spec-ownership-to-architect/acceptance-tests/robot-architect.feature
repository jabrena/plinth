Feature: Validate robot-architect agent

Background:
  Given the agent prompt file ".cursor/agents/robot-architect.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Prepare implementation artifacts without implementing application code
  Given the user request is "Explore design alternatives and prepare an implementation plan or OpenSpec change for a Java service"
  And the agent prompt source ".cursor/agents/robot-architect.md" is read before execution
  When the agent "robot-architect" is applied to the request
  Then the agent clarifies goals, constraints, assumptions, unknowns, and success criteria
  And the agent compares viable design alternatives before recommending a direction
  And the agent obtains approval before treating a design direction as selected
  And the agent creates or refines implementation plans or OpenSpec changes with source artifact traceability
  And the agent applies two-step, slicing, design, testing, and migration skills when relevant to pre-implementation planning
  And the agent uses ADR and diagram skills only when justified by the approved design
  And the agent does not implement application code, edit tests, or perform delivery work as a substitute for coder agents
  And the agent reports outputs, traceability, unresolved questions, implementation constraints, and handoff details for robot-tech-lead
  And any git changes produced during agent execution and verification are reset

@integration-test
Scenario: Hand off approved artifacts to the tech lead
  Given the user request is "Prepare approved architecture and planning artifacts for delivery"
  And the agent prompt source ".cursor/agents/robot-architect.md" is read before execution
  When the agent "robot-architect" is applied to the request
  Then the agent identifies the selected implementation plan or OpenSpec task list for delivery
  And the agent preserves source traceability, architecture constraints, unresolved decisions, and validation expectations
  And the agent does not delegate implementation, test, or verification work directly to coder agents
  And the agent reports a clear handoff for robot-tech-lead to coordinate implementation delivery
  And any git changes produced during agent execution and verification are reset

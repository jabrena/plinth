Feature: Validate robot-tech-lead agent

Background:
  Given the agent prompt file ".cursor/agents/robot-tech-lead.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Coordinate Java delivery without direct implementation
  Given the user request is "Create a plan or OpenSpec change and coordinate implementation for a Java Enterprise task"
  And the agent prompt source ".cursor/agents/robot-tech-lead.md" is read before execution
  When the agent "robot-tech-lead" is applied to the request
  Then the agent determines the target framework from source artifacts before delegating implementation
  And the agent creates or refines plans or OpenSpec changes with source artifact traceability
  And the agent applies two-step, slicing, design, testing, and migration skills when relevant
  And the agent delegates every implementation, test, and verification step to the selected coder agent
  And the agent does not implement code, edit tests, or run the build as a substitute for developers
  And the agent reports delegation groups, task status, validation evidence, risks, blockers, and artifact authority
  And any git changes produced during agent execution and verification are reset

Feature: Validate robot-tech-lead agent

Background:
  Given the agent prompt file ".cursor/agents/robot-tech-lead.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Coordinate Java delivery from an approved execution artifact
  Given the user request is "Coordinate implementation for an approved Java Enterprise plan or OpenSpec task list"
  And the agent prompt source ".cursor/agents/robot-tech-lead.md" is read before execution
  When the agent "robot-tech-lead" is applied to the request
  Then the agent treats the selected plan or OpenSpec task list as the execution artifact
  And the agent determines the target framework from source artifacts before delegating implementation
  And the agent delegates every implementation, test, and verification step to the selected coder agent
  And the agent does not implement code, edit tests, or run the build as a substitute for developers
  And the agent does not create or refine plans or OpenSpec changes as a primary mission
  And the agent reports delegation groups, task status, validation evidence, risks, blockers, and artifact authority
  And any git changes produced during agent execution and verification are reset

@integration-test
Scenario: Route pre-implementation planning requests to the architect
  Given the user request is "Create a plan or OpenSpec change for a Java Enterprise task"
  And the agent prompt source ".cursor/agents/robot-tech-lead.md" is read before execution
  When the agent "robot-tech-lead" is applied to the request
  Then the agent does not present plan creation or OpenSpec creation as primary robot-tech-lead missions
  And the agent routes pre-implementation planning and specification work to robot-architect
  And the agent waits for an approved implementation plan or OpenSpec task list before coordinating delivery
  And the agent does not implement code, edit tests, or run the build as a substitute for developers
  And any git changes produced during agent execution and verification are reset

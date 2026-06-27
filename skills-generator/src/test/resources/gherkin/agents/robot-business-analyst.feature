Feature: Validate robot-business-analyst agent

Background:
  Given the agent prompt file ".cursor/agents/robot-business-analyst.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Review delivery readiness across planning artifacts
  Given the user request is "Review issue, OpenSpec, ADR, and plan alignment for delivery readiness"
  And the agent prompt source ".cursor/agents/robot-business-analyst.md" is read before execution
  When the agent "robot-business-analyst" is applied to the request
  Then the agent summarizes business goal and scope from available evidence
  And the agent cross-checks traceability across issues, plans, OpenSpec artifacts, ADRs, and diagrams
  And the agent identifies inconsistencies, ambiguous acceptance criteria, missing NFRs, and unresolved questions
  And the agent returns a readiness gate of "READY", "READY WITH WARNINGS", or "NOT READY"
  And the agent does not implement application code or silently edit technical artifacts to force alignment
  And the agent reports findings with severity, location, evidence, and suggested resolution
  And any git changes produced during agent execution and verification are reset

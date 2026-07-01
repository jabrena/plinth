Feature: Validate robot-java-micronaut-coder agent

Background:
  Given the agent prompt file ".cursor/agents/robot-java-micronaut-coder.md"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Implement Micronaut work with framework-specific skills
  Given the user request is "Implement a Micronaut REST API change with validation and tests"
  And the agent prompt source ".cursor/agents/robot-java-micronaut-coder.md" is read before execution
  When the agent "robot-java-micronaut-coder" is applied to the request
  Then the agent confirms the delegated work is Micronaut-specific before making changes
  And the agent uses Micronaut skills for core, REST, validation, security, persistence, messaging, MongoDB, migrations, and tests when relevant
  And the agent applies Jakarta namespace and import-management coding standards
  And the agent runs focused Maven validation or reports validation failure as blocking
  And the agent reports files changed, validation evidence, blockers, and residual risks
  And any git changes produced during agent execution and verification are reset

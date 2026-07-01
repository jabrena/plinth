Feature: Validate robot-java-spring-boot-coder agent

Background:
  Given the agent prompt file ".cursor/agents/robot-java-spring-boot-coder.md"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Implement Spring Boot work with framework-specific skills
  Given the user request is "Implement a Spring Boot REST API change with validation and tests"
  And the agent prompt source ".cursor/agents/robot-java-spring-boot-coder.md" is read before execution
  When the agent "robot-java-spring-boot-coder" is applied to the request
  Then the agent confirms the delegated work is Spring Boot-specific before making changes
  And the agent uses Spring Boot skills for core, REST, validation, security, Modulith, persistence, messaging, MongoDB, migrations, and tests when relevant
  And the agent applies import-management and repository coding standards
  And the agent keeps framework runtime wiring in adapters or composition code when Hexagonal architecture is requested
  And the agent runs focused Maven validation or reports validation failure as blocking
  And the agent reports files changed, validation evidence, blockers, and residual risks
  And any git changes produced during agent execution and verification are reset

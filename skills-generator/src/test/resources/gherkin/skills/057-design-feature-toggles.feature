Feature: Validate changes from usage of Feature Toggles design skill

Background:
  Given the skill "057-design-feature-toggles"

@acceptance-test
Scenario: Generate a Feature Toggles skill for Java enterprise development
  Given the local generated skill path ".agents/skills/057-design-feature-toggles"
  When the skill ".agents/skills/057-design-feature-toggles" guides a Java enterprise feature toggle design
  Then the skill reads "references/057-design-feature-toggles.md"
  And the generated skill explains when to use feature toggles in Java enterprise systems
  And the guidance distinguishes feature toggles from branches, ordinary configuration, deployment changes, and Parallel Change
  And the guidance covers release toggles, operational kill switches, experiment toggles, permission toggles, and migration toggles
  And the guidance defines owner, default state, rollout audience, expected lifetime, and removal trigger before implementation
  And the guidance covers design, implementation, review, cleanup, and testing concerns
  And the guidance recommends centralized typed toggle decisions instead of scattered raw configuration lookups
  And the guidance includes safe defaults, fallback behavior, observability, rollback, security, and privacy considerations
  And the guidance requires tests for disabled, enabled, rollback, failure, targeting, observability, and cleanup scenarios

@acceptance-test
Scenario: Review a Java feature flag implementation for lifecycle risk
  Given the user request is "Review this feature flag for a staged checkout rollout in a Spring Boot service"
  And the local generated skill path ".agents/skills/057-design-feature-toggles"
  When the skill ".agents/skills/057-design-feature-toggles" reviews the feature flag implementation
  Then the skill reads "references/057-design-feature-toggles.md"
  And the review classifies the toggle type and lifecycle
  And the review checks whether the disabled path preserves current production behavior
  And the review checks whether the enabled path has rollout guardrails and validation signals
  And the review checks whether disabling the toggle is a tested rollback path
  And the review checks whether the toggle can bypass authentication, authorization, audit, compliance, rate limiting, or data retention controls
  And the review identifies cleanup ownership, removal trigger, stale branch risk, and tests that should be removed or simplified after cleanup

@integration-test
Scenario: Validate Feature Toggles skill generation and prompt coverage
  Given the Feature Toggles skill XML sources have been added
  And the matching Gherkin feature file "skills-generator/src/test/resources/gherkin/skills/057-design-feature-toggles.feature" has been added
  When the skills-generator module is validated and local skills are regenerated
  Then the edited XML is well formed
  And the module verification passes
  And the generated skill output can be inspected under ".agents/skills/057-design-feature-toggles"
  And the generated skill output contains "SKILL.md"
  And the generated skill output contains "references/057-design-feature-toggles.md"
  And the acceptance prompt inventory includes "execute @skills-generator/src/test/resources/gherkin/skills/057-design-feature-toggles.feature"

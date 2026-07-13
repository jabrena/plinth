Feature: Validate changes from usage of Feature Toggles design skill

Background:
  Given the skill "057-design-feature-toggles"

@acceptance-test
Scenario: Generate a Feature Toggles skill for Java enterprise development
  Given the local generated skill path ".agents/skills/057-design-feature-toggles"
  And the example framework project "examples/frameworks/spring-boot"
  And the target endpoint "POST /api/v1/sum" is implemented in "examples/frameworks/spring-boot/src/main/java/info/jab/ms/controller/SumController.java"
  And the current REST contract is documented in "examples/frameworks/openapi.yaml"
  And the imaginary requirement is "Publish a SumCalculated event after successful sum responses, but protect the event-publishing rollout with a runtime toggle so POST /api/v1/sum keeps returning the current response when publishing is disabled, unavailable, or rolled back"
  When the skill ".agents/skills/057-design-feature-toggles" guides a Spring Boot feature toggle design for the imaginary SumCalculated event-publishing requirement
  Then the skill reads "references/057-design-feature-toggles.md"
  And the generated skill explains when to use feature toggles in Java enterprise systems
  And the guidance distinguishes feature toggles from branches, ordinary configuration, deployment changes, and Parallel Change
  And the guidance covers release toggles, operational kill switches, experiment toggles, permission toggles, and migration toggles
  And the guidance defines owner, default state, rollout audience, expected lifetime, and removal trigger before implementation
  And the guidance covers design, implementation, review, cleanup, and testing concerns
  And the guidance recommends centralized typed toggle decisions instead of scattered raw configuration lookups
  And the guidance includes safe defaults, fallback behavior, observability, rollback, security, and privacy considerations
  And the guidance requires tests for disabled, enabled, rollback, failure, targeting, observability, and cleanup scenarios
  And the guidance applies the design to "examples/frameworks/spring-boot" and the "POST /api/v1/sum" flow
  And the guidance treats the disabled path as preserving the current REST behavior: request {"param1": 10, "param2": 32} returns {"result": 42} without publishing a SumCalculated event
  And the guidance defines a centralized typed decision boundary such as "SumFeatureDecisions#publishSumCalculatedEvents" instead of checking raw properties inside "SumController.java"
  And the guidance proposes a safe Spring Boot configuration default such as "sum.features.publish-sum-calculated-events=false"
  And the guidance classifies the toggle as a temporary release toggle with an operational kill-switch rollback path
  And the guidance names a Spring Boot implementation boundary for the event publisher or application service rather than changing the OpenAPI response contract
  And the guidance requires tests proving disabled behavior, enabled publishing, provider/configuration failure fallback, rollback disablement, rollout targeting, observability signals, and cleanup removal of the temporary toggle

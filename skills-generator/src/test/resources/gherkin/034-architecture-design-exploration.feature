Feature: Validate changes from usage of architecture design exploration skill

Background:
  Given the skill "034-architecture-design-exploration"

@acceptance-test
Scenario: Explore design alternatives from a sanitized checkout design brief
  Given the sanitized design brief file "examples/diagrams/deployment/checkout-service-feature-request.md"
  And the architecture context file "examples/diagrams/deployment/system-example-cicd-pr-model.md"
  And the local generated skill path ".agents/skills/034-architecture-design-exploration"
  When the skill ".agents/skills/034-architecture-design-exploration" is applied to explore the checkout design
  Then the skill reads "references/034-architecture-design-exploration.md"
  And the skill records the sanitized design brief and architecture context as source artifacts
  And the skill separates known facts, assumptions, constraints, unresolved questions, and success criteria
  And the skill asks focused clarification questions before comparing approaches when blocking ambiguity remains
  And the skill compares two or three feasible approaches across complexity, maintainability, performance, security, testability, migration impact, and operations
  And the skill recommends one direction with explicit rationale and trade-offs
  And the skill asks for explicit user approval before treating the direction as selected
  And the skill identifies ADR candidates and unresolved non-blocking questions for downstream work
  And the skill does not create implementation plans, OpenSpec artifacts, or ADR files before approval

Feature: Validate changes from usage of Java testing strategies skill

Background:
  Given the skill "130-java-testing-strategies"

@acceptance-test
Scenario: Route missing behavior coverage requests to RIGHT-BICEP
  Given the user request is "Review these Java tests and identify missing behavior coverage using RIGHT-BICEP"
  And the local generated skill path ".agents/skills/130-java-testing-strategies"
  When the skill ".agents/skills/130-java-testing-strategies" is applied to the Java testing strategy request
  Then the skill reads "references/130-java-testing-strategies-right-bicep.md"
  And the skill does not read A-TRIP or CORRECT references unless the request also involves test quality or boundary conditions
  And the output includes a RIGHT-BICEP gap matrix covering right results, boundary conditions, inverse relationships, cross-checks, error conditions, and performance guardrails
  And code edits remain gated by compile and verification guidance

@acceptance-test
Scenario: Route flaky and brittle test requests to A-TRIP
  Given the user request is "Review why these Java tests are flaky, brittle, order-dependent, and hard to maintain"
  And the local generated skill path ".agents/skills/130-java-testing-strategies"
  When the skill ".agents/skills/130-java-testing-strategies" is applied to the Java testing strategy request
  Then the skill reads "references/130-java-testing-strategies-a-trip.md"
  And the skill does not read RIGHT-BICEP or CORRECT references unless the request also involves coverage strategy or boundary conditions
  And the output identifies A-TRIP violations for automatic, thorough, repeatable, independent, and professional tests
  And code edits remain gated by compile and verification guidance

@acceptance-test
Scenario: Route boundary condition requests to CORRECT
  Given the user request is "Review this Java validation code for missing CORRECT boundary condition tests"
  And the local generated skill path ".agents/skills/130-java-testing-strategies"
  When the skill ".agents/skills/130-java-testing-strategies" is applied to the Java testing strategy request
  Then the skill reads "references/130-java-testing-strategies-correct.md"
  And the skill does not read RIGHT-BICEP or A-TRIP references unless the request also involves coverage strategy or test quality
  And the output includes boundary gaps for conformance, ordering, range, reference, existence, cardinality, and time
  And code edits remain gated by compile and verification guidance

@acceptance-test
Scenario: Broad test strategy reviews may combine all focused references
  Given the user request is "Perform a broad Java test strategy review across coverage, quality, and boundary conditions"
  And the local generated skill path ".agents/skills/130-java-testing-strategies"
  When the skill ".agents/skills/130-java-testing-strategies" is applied to the Java testing strategy request
  Then the skill may read "references/130-java-testing-strategies-right-bicep.md"
  And the skill may read "references/130-java-testing-strategies-a-trip.md"
  And the skill may read "references/130-java-testing-strategies-correct.md"
  And the output categorizes findings by RIGHT-BICEP, A-TRIP, and CORRECT
  And code edits remain gated by compile and verification guidance

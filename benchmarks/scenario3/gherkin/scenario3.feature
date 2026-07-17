Feature: Scenario 3 — Case 3 pending placeholder
  As a Plinth maintainer reviewing the project benchmark harness
  I want Case 3 documented as pending with no runnable input
  So that campaigns do not invent or execute an undefined case

  @acceptance-test
  Scenario: Case 3 is pending and not executable yet
    Given the Case 3 harness folder for scenario "scenario3"
    When a maintainer reviews Case 3 specs and Gherkin
    Then the case is labeled "case-3-pending"
    And the documentation states that the input and workflow contract is TBD
    And Case 3 has no runnable input contract
    And Case 3 is excluded from campaign ranking until the contract is defined
    And Case 3 is not executable yet

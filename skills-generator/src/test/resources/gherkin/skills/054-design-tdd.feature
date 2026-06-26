Feature: Validate changes from usage of TDD design skill

Background:
  Given the skill "054-design-tdd"

@acceptance-test
Scenario: Guide Java development with the TDD workflow
  Given the user request is "Apply TDD to implement a Java order discount calculation behavior"
  And the local generated skill path ".agents/skills/054-design-tdd"
  When the skill ".agents/skills/054-design-tdd" is applied to the Java implementation request
  Then the skill reads "references/054-design-tdd.md"
  And the analysis maintains or refines a list of candidate test cases
  And the analysis identifies the next useful behavior or test case before writing production code
  And the analysis writes or describes a failing test first
  And the analysis explains that the test-first step clarifies the public interface, API shape, or usage of the code
  And the implementation guidance limits production code to the smallest functional code needed to pass the selected test
  And the guidance adds newly discovered behaviors or edge cases to the test list instead of broadening the current cycle
  And the guidance includes a refactoring step for new and existing code after the selected test passes
  And the guidance keeps relevant tests green while refactoring
  And the recommendation reports skipped checks, missing tests, or remaining risks when verification is incomplete

Feature: Scenario 1 — Case 1 minimal functional notes
  As a Plinth maintainer running the project benchmark harness
  I want Case 1 runs to use README-only functional notes, stay skill-agnostic,
  and persist a metrics result JSON under results/
  So that we can compare agent outcomes under minimal packaging with measurable cost and quality

  Background:
    Given a Case 1 benchmark run for scenario "scenario1"
    And the run case id is "case-1-readme-only"
    And results are stored under "benchmarks/scenario1/results/"

  @acceptance-test
  Scenario: Case 1 run records minimal v1 metrics as JSON
    Given only "benchmarks/scenario1/specs/functional-requirements/README.md" is provided as requirements input
    And a full "benchmarks/scenario1/specs/functional-requirements/problem1/" package is not provided
    And "benchmarks/scenario1/specs/technical-requirements/openspec/" is not provided
    And if ".agents/skills" exists it is removed as a precondition
    And if ".cursor/skills" exists it is removed as a precondition
    And ".agents/skills" is not present or is empty
    And ".cursor/skills" is not present or is empty
    And no skill references under ".cursor/skills" are provided to the agent
    When the agent implements the God Analysis API in "benchmarks/scenario1/demo/"
    And the run completes
    Then a result JSON file exists under "benchmarks/scenario1/results/"
    And the result JSON includes required efficiency fields "wall_clock_s", "tokens_total", and "cost_usd"
    And the result JSON includes required outcome fields "acceptance_pass" and "rework_turns"
    And the result JSON includes required Plinth usage fields "skills_count", "agents_count", "skills", and "agents"
    And the result JSON includes required labels "scenario", "case_id", "tool", "model", "plinth_config", and "commit"
    And the result JSON field "scenario" equals "scenario1"
    And the result JSON field "case_id" equals "case-1-readme-only"
    And the result JSON field "acceptance_pass" is a boolean
    And the result JSON field "skills_count" equals 0
    And the result JSON field "skills" is an empty array
    And the result JSON field "agents_count" is a non-negative integer
    And the length of "agents" equals the value of "agents_count"
    And the length of "skills" equals the value of "skills_count"
    And optional fields "active_agent_s", "tokens_in", "tokens_out", "acceptance_coverage", "artifact_completeness", "retry_count", and "human_intervention_min" may be present
    And "benchmarks/scenario1/demo/" is restored to empty with only ".gitkeep"
    And skills are restored with "npx skills add jabrena/plinth --skill '*' --agent cursor -y"

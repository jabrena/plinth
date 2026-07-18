Feature: Scenario 4 — Case 4 OpenSpec technical requirements
  As a Plinth maintainer running the project benchmark harness
  I want Case 4 runs to use OpenSpec technical requirements as the implementation input
  and persist a metrics result JSON under results/
  So that we can compare OpenSpec-driven implementation against Scenario 1 and Scenario 2 with measurable cost and quality

  Background:
    Given a Case 4 benchmark run for scenario "scenario4"
    And the run case id is "case-4-current-openspec-problem1"
    And results are stored under "benchmarks/scenario4/results/"

  @acceptance-test
  Scenario: Case 4 run records minimal v1 metrics as JSON
    Given the technical requirements input is "benchmarks/scenario4/specs/technical-requirements/openspec/"
    And "examples/openspec/god-analysis-api/" is not used as scenario input authority
    When the agent implements the God Analysis API in "benchmarks/scenario4/demo/"
    And the run completes
    Then a result JSON file exists under "benchmarks/scenario4/results/"
    And the result JSON follows the minimal v1 fields documented in "benchmarks/scenario4/results/README.md"
    And the result JSON includes required efficiency fields "wall_clock_s", "tokens_total", and "cost_usd"
    And the result JSON includes required outcome fields "acceptance_pass" and "rework_turns"
    And the result JSON includes required Plinth usage fields "skills_count", "agents_count", "skills", and "agents"
    And the result JSON includes required labels "scenario", "case_id", "tool", "model", "plinth_config", and "commit"
    And the result JSON field "scenario" equals "scenario4"
    And the result JSON field "case_id" equals "case-4-current-openspec-problem1"
    And the result JSON field "acceptance_pass" is a boolean
    And the result JSON field "skills_count" is a non-negative integer
    And the result JSON field "agents_count" is a non-negative integer
    And the result JSON field "skills" is a JSON array
    And the result JSON field "agents" is a JSON array
    And the length of "agents" equals the value of "agents_count"
    And the length of "skills" equals the value of "skills_count"
    And optional fields "active_agent_s", "tokens_in", "tokens_out", "acceptance_coverage", "artifact_completeness", "retry_count", and "human_intervention_min" may be present
    And "benchmarks/scenario4/demo/" is restored to empty with only ".gitkeep"

Feature: Scenario 4 — Case 4 functional and technical OpenSpec requirements
  As a Plinth maintainer running the project benchmark harness
  I want Case 4 runs to use co-located functional requirements and OpenSpec technical requirements,
  keep derivation links inside the scenario specs tree, and persist a metrics result JSON under results/
  So that we can compare the richest packaging step against Scenario 1 and Scenario 2 with measurable cost and quality

  Background:
    Given a Case 4 benchmark run for scenario "scenario4"
    And the run case id is "case-4-current-openspec-problem1"
    And results are stored under "benchmarks/scenario4/results/"

  @acceptance-test
  Scenario: Case 4 run records minimal v1 metrics as JSON
    Given the functional requirements input is "benchmarks/scenario4/specs/functional-requirements/problem1/"
    And "benchmarks/scenario4/specs/functional-requirements/problem1/README.md" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/US-001_God_Analysis_API.md" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/US-001_god_analysis_api.feature" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/US-001-god-analysis-api.openapi.yaml" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/my-json-server-oas.yaml" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md" is provided
    And "benchmarks/scenario4/specs/functional-requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md" is provided
    And the technical requirements input is "benchmarks/scenario4/specs/technical-requirements/openspec/"
    And OpenSpec source links resolve under functional-requirements/problem1/
    When the agent implements the God Analysis API in "benchmarks/scenario4/demo/"
    And the run completes
    Then a result JSON file exists under "benchmarks/scenario4/results/"
    And the result JSON includes required efficiency fields "wall_clock_s", "tokens_total", and "cost_usd"
    And the result JSON includes required outcome fields "acceptance_pass" and "rework_turns"
    And the result JSON includes required labels "scenario", "case_id", "tool", "model", "plinth_config", and "commit"
    And the result JSON field "scenario" equals "scenario4"
    And the result JSON field "case_id" equals "case-4-current-openspec-problem1"
    And the result JSON field "acceptance_pass" is a boolean
    And optional fields "active_agent_s", "tokens_in", "tokens_out", "acceptance_coverage", "artifact_completeness", "retry_count", and "human_intervention_min" may be present

Feature: Scenario 3 — Case 3 functional and technical OpenSpec requirements
  As a Plinth maintainer running the project benchmark harness
  I want Case 3 runs to use co-located functional requirements and OpenSpec technical requirements,
  keep derivation links inside the scenario specs tree, and persist a metrics result JSON under results/
  So that we can compare the richest packaging step against Scenario 1 and Scenario 2 with measurable cost and quality

  Background:
    Given a Case 3 benchmark run for scenario "scenario3"
    And the run case id is "case-3-current-openspec-problem1"
    And results are stored under "benchmarks/scenario3/results/"

  @acceptance-test
  Scenario: Case 3 run records minimal v1 metrics as JSON
    Given the functional requirements input is "benchmarks/scenario3/specs/functional-requirements/problem1/"
    And "benchmarks/scenario3/specs/functional-requirements/problem1/README.md" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/US-001_God_Analysis_API.md" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/US-001_god_analysis_api.feature" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/US-001-god-analysis-api.openapi.yaml" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/my-json-server-oas.yaml" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md" is provided
    And "benchmarks/scenario3/specs/functional-requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md" is provided
    And the technical requirements input is "benchmarks/scenario3/specs/technical-requirements/openspec/"
    And OpenSpec source links resolve under functional-requirements/problem1/
    When the agent implements the God Analysis API in "benchmarks/scenario3/demo/"
    And the run completes
    Then a result JSON file exists under "benchmarks/scenario3/results/"
    And the result JSON includes required efficiency fields "wall_clock_s", "tokens_total", and "cost_usd"
    And the result JSON includes required outcome fields "acceptance_pass" and "rework_turns"
    And the result JSON includes required Plinth usage fields "skills_count", "agents_count", "skills", and "agents"
    And the result JSON includes required labels "scenario", "case_id", "tool", "model", "plinth_config", and "commit"
    And the result JSON field "scenario" equals "scenario3"
    And the result JSON field "case_id" equals "case-3-current-openspec-problem1"
    And the result JSON field "acceptance_pass" is a boolean
    And the result JSON field "skills_count" is a non-negative integer
    And the result JSON field "agents_count" is a non-negative integer
    And the result JSON field "skills" is a JSON array
    And the result JSON field "agents" is a JSON array
    And the length of "agents" equals the value of "agents_count"
    And the length of "skills" equals the value of "skills_count"
    And optional fields "active_agent_s", "tokens_in", "tokens_out", "acceptance_coverage", "artifact_completeness", "retry_count", and "human_intervention_min" may be present
    And "benchmarks/scenario3/demo/" is restored to empty with only ".gitkeep"

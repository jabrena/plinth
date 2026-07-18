Feature: Scenario 1 — Case 1 minimal functional notes
  As a Plinth maintainer running the project benchmark harness
  I want Case 1 runs to use README-only functional notes from scenario1 only
  and persist a metrics result JSON under results/
  So that we can compare agent outcomes under minimal packaging with measurable cost and quality

  # Agent protocol (Case 1):
  # - Derive ALL product requirements ONLY from benchmarks/scenario1/specs/functional-requirements/README.md.
  # - Do NOT search the repository for "God Analysis API", ADR, OpenSpec, or problem1 artifacts outside benchmarks/scenario1/.
  # - Other scenarios and examples/openspec/ are out of scope for this run even if they describe the same product.

  Background:
    Given a Case 1 benchmark run for scenario "scenario1"
    And the run case id is "case-1-readme-only"
    And results are stored under "benchmarks/scenario1/results/"
    And the Case 1 allowlist is the only authorized reading set for requirements and product behavior:
      | path |
      | benchmarks/scenario1/specs/functional-requirements/README.md |
      | benchmarks/scenario1/gherkin/scenario1.feature |
      | benchmarks/scenario1/results/README.md |
      | benchmarks/scenario1/README.md |
      | benchmarks/metrics-v1.schema.json |
      | benchmarks/metrics-v1.example.json |

  @acceptance-test
  Scenario: Case 1 run records minimal v1 metrics as JSON
    Given "benchmarks/scenario1/specs/functional-requirements/README.md" is the sole product specification
    And only files on the Case 1 allowlist are read for requirements, design, or acceptance criteria
    And the agent must not read, open, grep, or search for requirements under any path outside "benchmarks/scenario1/" except harness metrics files on the Case 1 allowlist under "benchmarks/"
    And a full "benchmarks/scenario1/specs/functional-requirements/problem1/" package is not provided
    And "benchmarks/scenario1/specs/technical-requirements/" is not provided
    And "benchmarks/scenario2/" must not be read or used as scenario input
    And "benchmarks/scenario3/" must not be read or used as scenario input
    And "benchmarks/scenario4/" must not be read or used as scenario input
    And "examples/openspec/god-analysis-api/" must not be read or used as scenario input
    And no "ADR-*" file outside "benchmarks/scenario1/" may be read for requirements or technology choices
    And no "openspec/changes/" design, tasks, or spec files outside "benchmarks/scenario1/" may be read for requirements
    When the agent implements the product behavior documented in "benchmarks/scenario1/specs/functional-requirements/README.md" in "benchmarks/scenario1/demo/"
    And the run completes
    Then the product happy path embedded in "benchmarks/scenario1/specs/functional-requirements/README.md" passes
    And a result JSON file exists under "benchmarks/scenario1/results/"
    And the result JSON conforms to "benchmarks/metrics-v1.schema.json"
    And the result JSON includes populated group "efficiency" with fields "wall_clock_s", "active_agent_s", "tokens_in", "tokens_out", "tokens_total", and "cost_usd"
    And the result JSON includes populated group "outcome_quality" with fields "acceptance_pass", "acceptance_coverage", "rework_turns", and "artifact_completeness"
    And the result JSON includes populated group "protocol_labels" with fields "scenario", "case_id", "tool", "model", "plinth_config", "commit", "retry_count", and "human_intervention_min"
    And the result JSON includes populated group "plinth_usage" with fields "skills_count", "commands_count", "agents_count", "skills", "commands", and "agents"
    And the result JSON field "protocol_labels.scenario" equals "scenario1"
    And the result JSON field "protocol_labels.case_id" equals "case-1-readme-only"
    And the result JSON field "outcome_quality.acceptance_pass" is true only when the README product happy path and this scenario pass
    And the length of "plinth_usage.agents" equals the value of "plinth_usage.agents_count"
    And the length of "plinth_usage.commands" equals the value of "plinth_usage.commands_count"
    And the length of "plinth_usage.skills" equals the value of "plinth_usage.skills_count"
    And "benchmarks/scenario1/demo/" is restored to empty with only ".gitkeep"

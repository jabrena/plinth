Feature: Scenario 4 — Case 4 OpenSpec technical requirements
  As a Plinth maintainer running the project benchmark harness
  I want Case 4 runs to use OpenSpec technical requirements from scenario4 only as the implementation input
  and persist a metrics result JSON under results/
  So that we can compare OpenSpec-driven implementation against Scenario 1 and Scenario 2 with measurable cost and quality

  # Agent protocol (Case 4):
  # - Execute /implement-spec with benchmarks/scenario4/specs/technical-requirements/openspec/changes/add-god-analysis-api
  #   and implement in benchmarks/scenario4/demo/.
  # - Derive ALL implementation requirements ONLY from that OpenSpec change (tasks.md, design.md, spec.md).
  # - Do NOT read benchmarks/scenario4/specs/functional-requirements/problem1/ directly as agent input
  #   (those files exist only so OpenSpec derivation links resolve within the harness).
  # - Do NOT use examples/openspec/god-analysis-api/ or other scenarios as input authority.

  Background:
    Given a Case 4 benchmark run for scenario "scenario4"
    And the run case id is "case-4-current-openspec-problem1"
    And results are stored under "benchmarks/scenario4/results/"
    And the Case 4 allowlist is the only authorized reading set for requirements and product behavior:
      | path |
      | benchmarks/scenario4/specs/technical-requirements/openspec/ |
      | benchmarks/scenario4/gherkin/scenario4.feature |
      | benchmarks/scenario4/results/README.md |
      | benchmarks/scenario4/README.md |
      | .cursor/commands/implement-spec.md |
      | skills/042-planning-openspec/ |
      | benchmarks/metrics-v1.schema.json |
      | benchmarks/metrics-v1.example.json |

  @acceptance-test
  Scenario: Case 4 run records minimal v1 metrics as JSON
    Given the OpenSpec change path "benchmarks/scenario4/specs/technical-requirements/openspec/changes/add-god-analysis-api"
    And the OpenSpec change "add-god-analysis-api" contains "proposal.md", "design.md", "tasks.md", and "specs/god-analysis-api/spec.md"
    And the command prompt source ".cursor/commands/implement-spec.md" is read before execution
    And only files on the Case 4 allowlist are read for requirements, design, or acceptance criteria
    And the agent must not read, open, grep, or search for requirements under any path outside "benchmarks/scenario4/" except harness metrics files on the Case 4 allowlist under "benchmarks/"
    And "benchmarks/scenario4/specs/functional-requirements/problem1/" must not be read directly for requirements or technology choices
    And "benchmarks/scenario1/" must not be read or used as scenario input
    And "benchmarks/scenario2/" must not be read or used as scenario input
    And "benchmarks/scenario3/" must not be read or used as scenario input
    And "examples/openspec/god-analysis-api/" must not be read or used as scenario input
    And no "ADR-*" file outside "benchmarks/scenario4/" may be read for requirements or technology choices
    And no "openspec/changes/" design, tasks, or spec files outside "benchmarks/scenario4/" may be read for requirements
    When the user executes the prompt "/implement-spec benchmarks/scenario4/specs/technical-requirements/openspec/changes/add-god-analysis-api implement in benchmarks/scenario4/demo/"
    And the run completes
    Then the happy path in "benchmarks/scenario4/specs/technical-requirements/openspec/changes/add-god-analysis-api/specs/god-analysis-api/spec.md" passes
    And a result JSON file exists under "benchmarks/scenario4/results/"
    And the result JSON conforms to "benchmarks/metrics-v1.schema.json"
    And the result JSON includes populated group "efficiency" with fields "wall_clock_s", "active_agent_s", "tokens_in", "tokens_out", "tokens_total", and "cost_usd"
    And the result JSON includes populated group "outcome_quality" with fields "acceptance_pass", "acceptance_coverage", "rework_turns", and "artifact_completeness"
    And the result JSON includes populated group "protocol_labels" with fields "scenario", "case_id", "tool", "model", "plinth_config", "commit", "retry_count", and "human_intervention_min"
    And the result JSON includes populated group "plinth_usage" with fields "skills_count", "agents_count", "skills", and "agents"
    And the result JSON field "protocol_labels.scenario" equals "scenario4"
    And the result JSON field "protocol_labels.case_id" equals "case-4-current-openspec-problem1"
    And the result JSON field "outcome_quality.acceptance_pass" is true only when the OpenSpec happy path and this scenario pass
    And the length of "plinth_usage.agents" equals the value of "plinth_usage.agents_count"
    And the length of "plinth_usage.skills" equals the value of "plinth_usage.skills_count"
    And "benchmarks/scenario4/demo/" is restored to empty with only ".gitkeep"

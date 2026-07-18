# Scenario 1 — Case 1 minimal functional notes

| Field | Value |
| --- | --- |
| Scenario | `scenario1` |
| Case id | `case-1-readme-only` |
| Runnable | Yes |
| Richness | Minimal — README-only functional notes |
| Results | `benchmarks/scenario1/results/<run-id>.json` |

## Input contract

Harness-local functional requirements (only this file):

- `benchmarks/scenario1/specs/functional-requirements/README.md`

## Exclusions

Do **not** provide:

- A full `problem1/` package (user story, Gherkin feature file, OpenAPI, ADRs, companion OAS) under this scenario
- Any `technical-requirements/` / OpenSpec tree
- `examples/openspec/god-analysis-api/openspec/changes/` as scenario input

## Purpose

Baseline: measure how agents perform with **sparse** functional notes only before richer packaging in Scenario 2 and OpenSpec technical plans in Scenario 4. Agents may use the same Plinth skills and tooling as other runnable scenarios.

## Artifacts

- Functional notes: [specs/functional-requirements/README.md](specs/functional-requirements/README.md)
- Acceptance: [gherkin/scenario1.feature](gherkin/scenario1.feature)
- Results guide: [results/README.md](results/README.md)
- Example result JSON: [results/example.result.json](results/example.result.json)

## Metrics and results JSON

Every completed run MUST persist one JSON file under `results/` with the minimal v1 fields from the harness scorecard:

- Efficiency: `wall_clock_s`, `tokens_total`, `cost_usd`
- Outcome: `acceptance_pass`, `rework_turns`
- Labels: `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`
- Plinth usage: `skills_count`, `agents_count`, `skills`, `agents`

How to measure each field, file naming, and operator checklist: [results/README.md](results/README.md).

Canonical definitions for all scenarios: [../README.md](../README.md).

## Execution prompt

Use this prompt to run Scenario 1 against the Gherkin feature:

```bash
execute @benchmarks/scenario1/gherkin/scenario1.feature
and verify that acceptance-tests passes.
```

Run constraints are defined in the feature file.

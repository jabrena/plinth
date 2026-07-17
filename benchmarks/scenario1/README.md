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
- Installed agent skills under `.agents/skills`
- Cursor skill references under `.cursor/skills`

## Purpose

Baseline: measure how agents perform with **sparse** functional notes and a **skill-agnostic** workspace (no `.agents/skills`, no `.cursor/skills`) before richer packaging in Scenario 2 and OpenSpec technical plans in Scenario 4.

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

How to measure each field, file naming, and operator checklist: [results/README.md](results/README.md).

Canonical definitions for all scenarios: [../README.md](../README.md).

## Execution prompt

Use this prompt to run Scenario 1 against the Gherkin feature:

```bash
execute @benchmarks/scenario1/gherkin/scenario1.feature
and verify that acceptance-tests passes.

Constraints for this run:
- Use only benchmarks/scenario1/specs/functional-requirements/README.md as requirements input
- Do not use a full functional-requirements/problem1/ package
- Do not use technical-requirements/openspec/
- Precondition: if .agents/skills or .cursor/skills exist, remove them (skill-agnostic / bare-agent)
- Implement in benchmarks/scenario1/demo/
- When the run completes, write metrics to benchmarks/scenario1/results/<run-id>.json
  using the minimal v1 fields documented in benchmarks/scenario1/results/README.md
- case_id must be case-1-readme-only and scenario must be scenario1
- After the run completes, restore benchmarks/scenario1/demo/ to empty (only .gitkeep)
- After the run completes, restore skills with:
  npx skills add jabrena/plinth --skill '*' --agent cursor -y
```

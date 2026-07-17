# Scenario 4 — Case 4 functional + technical (OpenSpec) requirements

| Field | Value |
| --- | --- |
| Scenario | `scenario4` |
| Case id | `case-4-current-openspec-problem1` |
| Runnable | Yes |
| Richness | Full functional requirements plus linked OpenSpec technical requirements |
| Results | `benchmarks/scenario4/results/<run-id>.json` |

## Input contract

Case 4 uses harness-local specs under `benchmarks/scenario4/specs/`:

### Functional requirements

- `benchmarks/scenario4/specs/functional-requirements/problem1/`

Includes README, user story, Gherkin, OpenAPI, source OAS, and ADRs for problem1.

### Full file inventory (MUST provide all)

- `benchmarks/scenario4/specs/functional-requirements/problem1/README.md`
- `benchmarks/scenario4/specs/functional-requirements/problem1/US-001_God_Analysis_API.md`
- `benchmarks/scenario4/specs/functional-requirements/problem1/US-001_god_analysis_api.feature`
- `benchmarks/scenario4/specs/functional-requirements/problem1/US-001-god-analysis-api.openapi.yaml`
- `benchmarks/scenario4/specs/functional-requirements/problem1/my-json-server-oas.yaml`
- `benchmarks/scenario4/specs/functional-requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md`
- `benchmarks/scenario4/specs/functional-requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `benchmarks/scenario4/specs/functional-requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md`

### Technical requirements (OpenSpec)

- `benchmarks/scenario4/specs/technical-requirements/openspec/`

Includes the current OpenSpec project (`config.yaml`, README, and `changes/add-god-analysis-api/` with proposal, design, tasks, and specs).

OpenSpec **Source and Derivation** links MUST point at the co-located functional-requirements files (relative paths under `functional-requirements/problem1/`), not at external `examples/openspec/...` paths as the scenario input authority.

## Exclusions

Do **not** provide:

- `examples/openspec/god-analysis-api/openspec/changes/` as scenario input authority (use the harness-local technical-requirements tree instead)

## Purpose

Measure how agents perform when implementation input is OpenSpec technical requirements that are explicitly derived from and linked to co-located functional requirements — the richest runnable step after Scenario 1 (minimal notes) and Scenario 2 (full FR only).

## Artifacts

- Functional requirements: [specs/functional-requirements/problem1/](specs/functional-requirements/problem1/)
- Technical requirements (OpenSpec): [specs/technical-requirements/openspec/](specs/technical-requirements/openspec/)
- Acceptance: [gherkin/scenario4.feature](gherkin/scenario4.feature)
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

Use this prompt to run Scenario 4 against the Gherkin feature:

```bash
execute @benchmarks/scenario4/gherkin/scenario4.feature
and verify that acceptance-tests passes.

Constraints for this run:
- Use the full benchmarks/scenario4/specs/functional-requirements/problem1/ package as functional requirements input
- Use benchmarks/scenario4/specs/technical-requirements/openspec/ as technical requirements input
- OpenSpec Source and Derivation links must resolve under the co-located functional-requirements/problem1/
- Do not use examples/openspec/god-analysis-api/ as scenario input authority
- Implement in benchmarks/scenario4/demo/
- When the run completes, write metrics to benchmarks/scenario4/results/<run-id>.json
  using the minimal v1 fields documented in benchmarks/scenario4/results/README.md
- case_id must be case-4-current-openspec-problem1 and scenario must be scenario4
```

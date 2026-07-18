# Scenario 3 — Case 3 functional + technical (OpenSpec) requirements

| Field | Value |
| --- | --- |
| Scenario | `scenario3` |
| Case id | `case-3-current-openspec-problem1` |
| Runnable | Yes |
| Richness | Full functional requirements plus linked OpenSpec technical requirements |
| Results | `benchmarks/scenario3/results/<run-id>.json` |

## Input contract

Case 3 uses harness-local specs under `benchmarks/scenario3/specs/`:

### Functional requirements

- `benchmarks/scenario3/specs/functional-requirements/problem1/`

Includes README, user story, Gherkin, OpenAPI, source OAS, and ADRs for problem1.

### Full file inventory (MUST provide all)

- `benchmarks/scenario3/specs/functional-requirements/problem1/README.md`
- `benchmarks/scenario3/specs/functional-requirements/problem1/US-001_God_Analysis_API.md`
- `benchmarks/scenario3/specs/functional-requirements/problem1/US-001_god_analysis_api.feature`
- `benchmarks/scenario3/specs/functional-requirements/problem1/US-001-god-analysis-api.openapi.yaml`
- `benchmarks/scenario3/specs/functional-requirements/problem1/my-json-server-oas.yaml`
- `benchmarks/scenario3/specs/functional-requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md`
- `benchmarks/scenario3/specs/functional-requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `benchmarks/scenario3/specs/functional-requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md`

### Technical requirements (OpenSpec)

- `benchmarks/scenario3/specs/technical-requirements/openspec/`

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
- Acceptance: [gherkin/scenario3.feature](gherkin/scenario3.feature)

## Execution prompt

Use this prompt to run Scenario 3 against the Gherkin feature:

```bash
execute @benchmarks/scenario3/gherkin/scenario3.feature
and verify that acceptance-tests passes.

Constraints for this run:
- Use the full benchmarks/scenario3/specs/functional-requirements/problem1/ package as functional requirements input
- Use benchmarks/scenario3/specs/technical-requirements/openspec/ as technical requirements input
- OpenSpec Source and Derivation links must resolve under the co-located functional-requirements/problem1/
- Do not use examples/openspec/god-analysis-api/ as scenario input authority
- Implement in benchmarks/scenario3/demo/
- When the run completes, write metrics to benchmarks/scenario3/results/<run-id>.json
  using the minimal v1 fields documented in benchmarks/README.md
- case_id must be case-3-current-openspec-problem1 and scenario must be scenario3
```

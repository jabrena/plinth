## Why

GitHub issue [#1012](https://github.com/jabrena/plinth/issues/1012) requests a reproducible **project benchmark harness** so maintainers can compare agent outcomes on one fixed problem (God Analysis API) across different requirement packaging levels and OpenSpec inputs.

Today there is no checked-in harness under `plinth-benchmark/`, no shared scenario specs/Gherkin, and no canonical metrics scorecard. Without that, Plinth cannot evidence how input richness and OpenSpec affect time, tokens, and cost for the same acceptance outcome.

This work is distinct from the JVM `/benchmark` command (JMeter/Gatling/JMH).

## What Changes

- Add repository harness root `plinth-benchmark/` with `README.md` documenting layout, scenarios, and the metrics scorecard.
- Add four scenario folders (`scenario1` … `scenario4`), each with `README.md`, `specs/`, and `gherkin/` acceptance artifacts.
- Bind defined scenarios to fixed repo-relative inputs under `examples/openspec/god-analysis-api/`:
  - **Case 1:** only `requirements/problem1/README.md`
  - **Case 2:** all files under `requirements/problem1/`
  - **Case 3:** pending (placeholder harness only; input contract TBD)
  - **Case 4:** current OpenSpec change under `openspec/changes/` for problem1
- Define the minimal v1 metrics scorecard (efficiency, outcome quality, process labels).
- Keep the harness as documentation/protocol artifacts in this change; do not claim automated multi-tool campaign runners or a sealed model catalog.

## Capabilities

### New Capabilities

- `project-benchmark-harness`: Defines the `plinth-benchmark/` layout, scenario contracts (including Case 3 pending), metrics scorecard, and acceptance readiness for benchmark runs.

### Modified Capabilities

None.

## Impact

- Adds a new top-level documentation harness folder; does not require Maven reactor membership in this change.
- Does not change embedded commands, agents, or generated `skills/` release output.
- Does not modify `examples/openspec/god-analysis-api/` source requirements or OpenSpec trees (those remain authoritative inputs for scenarios).
- Migration: none for existing users; maintainers use `plinth-benchmark/` when running campaigns described in issue #1012.

## Source and Derivation

| Source | Concern | Derivation |
| --- | --- | --- |
| GitHub issue [#1012](https://github.com/jabrena/plinth/issues/1012) | Problem, scenarios, metrics, acceptance | Issue → OpenSpec change (one-way) |
| `examples/openspec/god-analysis-api/requirements/problem1/` | Case 1 and Case 2 requirement inputs | Referenced by harness; not rewritten |
| `examples/openspec/god-analysis-api/openspec/changes/` | Case 4 current OpenSpec for problem1 | Referenced by harness; not rewritten |

Unresolved (deferred): Case 3 input/workflow definition; exact model catalog; automation depth; where long-term result reports live. Optional follow-up: `/explore-design` or ADR for Case 3 and result schema.

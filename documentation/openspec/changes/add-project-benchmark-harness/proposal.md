## Why

GitHub issue [#1012](https://github.com/jabrena/plinth/issues/1012) requests a reproducible **project benchmark harness** so maintainers can compare agent outcomes on one fixed problem (God Analysis API) as **information richness increases** from sparse notes to full functional requirements to functional + OpenSpec technical plans.

Today there is no checked-in harness under `benchmarks/`, no shared scenario specs/Gherkin, and no canonical metrics scorecard. Without that, Plinth cannot evidence how richer structured inputs affect time, tokens, cost, and acceptance outcomes.

This work is distinct from the JVM `/benchmark` command (JMeter/Gatling/JMH).

## What Changes

- Add repository harness root `benchmarks/` with `README.md` documenting layout, the richness progression, scenarios, and the metrics scorecard.
- Add four scenario folders (`scenario1` … `scenario4`), each with `README.md`, `specs/`, and `gherkin/` acceptance artifacts.
- Bind defined scenarios to an increasing-information ladder:
  - **Case 1:** minimal functional notes — only `benchmarks/scenario1/specs/functional-requirements/README.md`; skill-agnostic (no `.agents/skills`, no `.cursor/skills`)
  - **Case 2:** full functional requirements — `benchmarks/scenario2/specs/functional-requirements/problem1/` (complete inventory; no OpenSpec)
  - **Case 3:** pending (placeholder reserved for the next richness step)
  - **Case 4:** full functional requirements **plus** technical OpenSpec under `benchmarks/scenario4/specs/technical-requirements/openspec/`, with OpenSpec Source and Derivation links resolving to co-located `functional-requirements/problem1/`
- Define the minimal v1 metrics scorecard (efficiency, outcome quality, process labels).
- Keep the harness as documentation/protocol artifacts in this change; do not claim automated multi-tool campaign runners or a sealed model catalog.

## Capabilities

### New Capabilities

- `project-benchmark-harness`: Defines the `benchmarks/` layout, progressive scenario contracts (Cases 1–4), metrics scorecard, and acceptance readiness for benchmark runs.

### Modified Capabilities

None.

## Impact

- Adds a new top-level documentation harness folder; does not require Maven reactor membership in this change.
- Does not change embedded commands, agents, or generated `skills/` release output.
- Scenarios 1, 2, and 4 materialize harness-local functional (and for Case 4, technical) artifacts under `benchmarks/scenario*/specs/`. Upstream `examples/openspec/god-analysis-api/` remains provenance and is not rewritten by this change.
- Migration: none for existing users; maintainers use `benchmarks/` when running campaigns described in issue #1012.

## Source and Derivation

| Source | Concern | Derivation |
| --- | --- | --- |
| GitHub issue [#1012](https://github.com/jabrena/plinth/issues/1012) | Problem, scenarios, metrics, acceptance | Issue → OpenSpec change (one-way) |
| `examples/openspec/god-analysis-api/requirements/problem1/` | Provenance for functional artifacts | Mirrored into harness FR trees; not rewritten upstream |
| `examples/openspec/god-analysis-api/openspec/changes/` | Provenance for Case 4 technical OpenSpec | Mirrored into harness technical-requirements; derivation links retargeted to co-located FR |

Unresolved (deferred): Case 3 input/workflow definition; exact model catalog; automation depth; where long-term result reports live. Optional follow-up: `/explore-design` or ADR for Case 3 and result schema.

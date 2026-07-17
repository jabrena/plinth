## Why

GitHub issue [#1012](https://github.com/jabrena/plinth/issues/1012) requests a reproducible **project benchmark harness** so maintainers can compare agent outcomes on one fixed problem (God Analysis API) across input richness, OpenSpec approaches, agent tools, models, and Plinth configuration levels.

Today there is no checked-in harness under `plinth-benchmark/`, no shared scenario specs/Gherkin, and no canonical metrics scorecard. Without that, Plinth cannot evidence whether commands, agents, skills, or OpenSpec reduce time, tokens, and cost for the same acceptance outcome.

This work is distinct from the JVM `/benchmark` command (JMeter/Gatling/JMH).

## What Changes

- Add repository harness root `plinth-benchmark/` with `README.md` documenting layout, scenarios, and the metrics scorecard.
- Add four scenario folders (`scenario1` … `scenario4`), each with `README.md`, `specs/`, and `gherkin/` acceptance artifacts.
- Bind each scenario to fixed repo-relative inputs under `examples/openspec/god-analysis-api/`.
- Define the minimal v1 metrics scorecard (efficiency, outcome quality, process labels) and scenario-specific extras.
- Keep the harness as documentation/protocol artifacts in this change; do not claim automated multi-tool campaign runners or a sealed model catalog.

## Capabilities

### New Capabilities

- `project-benchmark-harness`: Defines the `plinth-benchmark/` layout, four scenario contracts, metrics scorecard, and acceptance readiness for benchmark runs.

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
| `examples/openspec/god-analysis-api/` | Fixed problem domain inputs | Referenced by harness; not rewritten by this change |

Unresolved (deferred, not blocking harness creation): exact model catalog; automation depth for multi-tool campaigns; where long-term result reports live (docs vs CI artifact). Optional follow-up: `/explore-design` or ADR for protocol/result schema.

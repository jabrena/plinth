## Context

Issue #1012 defines a Plinth **project effectiveness** benchmark: same God Analysis API problem, four input/workflow scenarios, metrics for efficiency and outcome quality. The issue already specifies harness location, scenario table, case ids, configuration levels, tools, and the metrics scorecard. This change records those decisions as OpenSpec so `/implement-spec` has an executable task list.

## Goals / Non-Goals

**Goals:**

- Establish `plinth-benchmark/` with four scenarios, each owning `specs/` and `gherkin/`.
- Document the canonical metrics scorecard in `plinth-benchmark/README.md`.
- Keep scenario inputs as repo-relative references into `examples/openspec/god-analysis-api/`.
- Distinguish Scenario 3 (official OpenSpec skill on raw checked-in change) from Scenario 4 (Plinth `/update-issue` → `/create-spec` → `/implement-spec`).

**Non-Goals:**

- JVM `/benchmark` (JMeter, Gatling, JMH) workloads.
- Statistical significance claims or fixed sample-size protocol.
- Expanding beyond the God Analysis API example in this change.
- Implementing a full automated multi-tool campaign runner.
- Sealing the model catalog (TBD / follow-up ADR).
- Editing or regenerating `examples/openspec/god-analysis-api/` requirement or OpenSpec trees as part of harness creation.

## Decisions

### One change, one outcome

Deliver the harness as a single reviewable change (`add-project-benchmark-harness`). Scenario folders are parts of one capability, not separate OpenSpec changes.

### Harness location and packaging

Place the harness at repository root `plinth-benchmark/`. Do **not** add it to the Maven reactor in this change; it is a documentation/protocol package (Markdown + Gherkin), not a Java module.

### Scenario contracts

| Scenario | Case id | Input authority |
| --- | --- | --- |
| `scenario1` | `case-1-readme-only` | Only `examples/openspec/god-analysis-api/requirements/problem1/README.md` |
| `scenario2` | `case-2-all-requirements-notes` | Full `examples/openspec/god-analysis-api/requirements/problem1/`; no OpenSpec changes tree |
| `scenario3` | `case-3-raw-openspec-official-skill` | `examples/openspec/god-analysis-api/openspec/changes/`; official OpenSpec skill/OPSX only; no Plinth `/create-spec`, `/implement-spec`, or `@042-planning-openspec` |
| `scenario4` | `case-4-plinth-commands-openspec-refinement` | README seed only; Full Plinth; generate OpenSpec via Plinth commands; forbid checked-in OpenSpec tree as input |

Each scenario folder MUST contain `README.md`, `specs/` (at least one scenario spec Markdown), and `gherkin/` (at least one `.feature` with exactly one `@acceptance-test` scenario).

### Metrics scorecard

Canonical definitions live in `plinth-benchmark/README.md`, matching issue #1012:

- Efficiency: `wall_clock_s`, `active_agent_s`, token fields, `cost_usd` (Scenario 4 also phase-split).
- Outcome: `acceptance_pass`, `acceptance_coverage`, `rework_turns`, `artifact_completeness`.
- Protocol labels: `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`, `retry_count`, `human_intervention_min`.
- Minimal v1 required fields: `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`, plus labels.
- Rank cost/tokens among `acceptance_pass = true` runs; failures separate.

### Configuration levels and tools

Recorded for campaign protocol only in this change (harness docs / scenario READMEs), not as automation code:

- Plinth configs: Full Plinth; No OpenSpec; Bare agent (with Scenario 3/4 applicability rules from the issue).
- Tools: Cursor, Codex, GitHub Copilot, Claude Code.
- Models: catalog deferred.

### Compatibility / breaking-change review

- **NON-BREAKING**: additive documentation harness only.
- No feature toggle required.

### Verification strategy

- Confirm `plinth-benchmark/` layout and required files exist.
- Confirm each `gherkin/*.feature` encodes the case id and input boundaries from the issue.
- Run `openspec validate --all` for this change.
- Markdown link sanity for new Markdown under `plinth-benchmark/` when validating documentation.

## Risks / Trade-offs

- Without a model catalog, early campaigns must pin models explicitly in run records.
- Manual campaign execution remains operator-driven until a later automation change.
- If `plinth-benchmark/` is later promoted to a Maven module, that is a separate change.

## Open Questions

- Where should durable campaign result reports live (docs vs CI artifact)? Deferred.
- Should Scenario 4 generated OpenSpec land under a run-local path only? Deferred to implementation notes; must remain separate from `examples/openspec/god-analysis-api/openspec/changes/`.

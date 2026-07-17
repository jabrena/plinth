## Context

Issue #1012 defines a Plinth **project effectiveness** benchmark on the God Analysis API problem. This change records the harness layout, metrics scorecard, and the clarified case input contracts so `/implement-spec` has an executable task list.

## Goals / Non-Goals

**Goals:**

- Establish `plinth-benchmark/` with four scenario folders, each owning `specs/` and `gherkin/`.
- Document the canonical metrics scorecard in `plinth-benchmark/README.md`.
- Bind Cases 1, 2, and 4 to explicit repo-relative inputs under `examples/openspec/god-analysis-api/`.
- Leave Case 3 as an explicit **pending** placeholder until its input/workflow is defined.

**Non-Goals:**

- JVM `/benchmark` (JMeter, Gatling, JMH) workloads.
- Defining Case 3 beyond a pending placeholder in this change.
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

### Scenario / case contracts

| Scenario | Case id | Input authority |
| --- | --- | --- |
| `scenario1` | `case-1-readme-only` | Only `examples/openspec/god-analysis-api/requirements/problem1/README.md` |
| `scenario2` | `case-2-all-problem1-requirements` | All files under `examples/openspec/god-analysis-api/requirements/problem1/` (listed below); no OpenSpec changes tree |
| `scenario3` | `case-3-pending` | **Pending** — folder exists with placeholder specs/Gherkin stating TBD; no runnable input contract yet |
| `scenario4` | `case-4-current-openspec-problem1` | Current OpenSpec for problem1 at `examples/openspec/god-analysis-api/openspec/changes/` (for example `add-god-analysis-api/`) |

#### Case 2 file inventory (copy of requirements)

Case 2 MUST provide all of the following files from `examples/openspec/god-analysis-api/requirements/problem1/`:

- `README.md`
- `US-001_God_Analysis_API.md`
- `US-001_god_analysis_api.feature`
- `US-001-god-analysis-api.openapi.yaml`
- `my-json-server-oas.yaml`
- `ADR-001-God-Analysis-API-Functional-Requirements.md`
- `ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `ADR-003-God-Analysis-API-Technology-Stack.md`

#### Case 4 OpenSpec inventory

Case 4 MUST use the checked-in OpenSpec change tree under `examples/openspec/god-analysis-api/openspec/changes/` (current problem1-derived change, including proposal, design, tasks, and specs).

Each scenario folder MUST contain `README.md`, `specs/` (at least one scenario spec Markdown), and `gherkin/` (at least one `.feature` with exactly one `@acceptance-test` scenario). For Case 3, the `@acceptance-test` scenario documents that the case is pending and is not executable until the contract is filled in.

### Metrics scorecard

Canonical definitions live in `plinth-benchmark/README.md`, matching issue #1012:

- Efficiency: `wall_clock_s`, `active_agent_s`, token fields, `cost_usd`.
- Outcome: `acceptance_pass`, `acceptance_coverage`, `rework_turns`, `artifact_completeness`.
- Protocol labels: `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`, `retry_count`, `human_intervention_min`.
- Minimal v1 required fields: `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`, plus labels.
- Rank cost/tokens among `acceptance_pass = true` runs; failures separate.
- Case 3 runs are out of campaign ranking until the pending contract is defined.

### Configuration levels and tools

Recorded for campaign protocol only in this change (harness docs / scenario READMEs), not as automation code:

- Plinth configs: Full Plinth; No OpenSpec; Bare agent.
- Tools: Cursor, Codex, GitHub Copilot, Claude Code.
- Models: catalog deferred.

### Compatibility / breaking-change review

- **NON-BREAKING**: additive documentation harness only.
- No feature toggle required.

### Verification strategy

- Confirm `plinth-benchmark/` layout and required files exist.
- Confirm Cases 1, 2, and 4 Gherkin features encode the case ids and input boundaries above.
- Confirm Case 3 is explicitly marked pending in README, specs, and Gherkin.
- Run `openspec validate --all` for this change.
- Markdown link sanity for new Markdown under `plinth-benchmark/` when validating documentation.

## Risks / Trade-offs

- Case 3 pending means four folders exist but only three cases are runnable in the first campaign.
- Without a model catalog, early campaigns must pin models explicitly in run records.
- Manual campaign execution remains operator-driven until a later automation change.

## Open Questions

- What input and workflow define Case 3? **Pending** — must be filled before Case 3 is runnable.
- Where should durable campaign result reports live (docs vs CI artifact)? Deferred.

## Context

Issue #1012 defines a Plinth **project effectiveness** benchmark on the God Analysis API problem. Scenarios form an **increasing-information ladder**: minimal functional notes → full functional requirements → (pending Case 3) → functional requirements plus linked OpenSpec technical requirements. This change records that progression so `/implement-spec` and campaign operators share one contract.

## Goals / Non-Goals

**Goals:**

- Establish `benchmarks/` with four scenario folders, each owning `specs/` and `gherkin/`.
- Document the richness progression and metrics scorecard in `benchmarks/README.md`.
- Bind Cases 1, 2, and 4 to harness-local functional (and for Case 4, technical) trees.
- Leave Case 3 as an explicit **pending** placeholder between Case 2 and Case 4 richness.

**Non-Goals:**

- JVM `/benchmark` (JMeter, Gatling, JMH) workloads.
- Defining Case 3 beyond a pending placeholder in this change.
- Statistical significance claims or fixed sample-size protocol.
- Expanding beyond the God Analysis API example in this change.
- Implementing a full automated multi-tool campaign runner.
- Sealing the model catalog (TBD / follow-up ADR).
- Rewriting upstream `examples/openspec/god-analysis-api/` trees as part of harness creation (mirroring into harness `specs/` is allowed).

## Decisions

### One change, one outcome

Deliver the harness as a single reviewable change (`add-project-benchmark-harness`). Scenario folders are parts of one capability, not separate OpenSpec changes.

### Harness location and packaging

Place the harness at repository root `benchmarks/`. Do **not** add it to the Maven reactor in this change; it is a documentation/protocol package (Markdown + Gherkin), not a Java module.

### Information richness progression

| Scenario | Case id | Richness | Input authority |
| --- | --- | --- | --- |
| `scenario1` | `case-1-readme-only` | Minimal | Only `benchmarks/scenario1/specs/functional-requirements/README.md`; no `.agents/skills`; no `.cursor/skills`; results JSON under `benchmarks/scenario1/results/` |
| `scenario2` | `case-2-all-problem1-requirements` | Full functional package | `benchmarks/scenario2/specs/functional-requirements/problem1/` (inventory below); no technical OpenSpec |
| `scenario3` | `case-3-pending` | TBD | Pending placeholder between Case 2 and Case 4 |
| `scenario4` | `case-4-current-openspec-problem1` | Functional + technical | Case 2-equivalent FR tree **plus** `specs/technical-requirements/openspec/` with derivation links into FR |

#### Case 2 file inventory (functional requirements)

Case 2 MUST provide all of the following under `benchmarks/scenario2/specs/functional-requirements/problem1/`:

- `README.md`
- `US-001_God_Analysis_API.md`
- `US-001_god_analysis_api.feature`
- `US-001-god-analysis-api.openapi.yaml`
- `my-json-server-oas.yaml`
- `ADR-001-God-Analysis-API-Functional-Requirements.md`
- `ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `ADR-003-God-Analysis-API-Technology-Stack.md`

#### Case 4 functional + technical requirements layout

Case 4 MUST materialize two co-located trees under `benchmarks/scenario4/specs/`:

1. **Functional requirements:** `functional-requirements/problem1/` (same inventory as Case 2)
2. **Technical requirements (OpenSpec):** `technical-requirements/openspec/` (`config.yaml`, README, `changes/add-god-analysis-api/` with proposal, design, tasks, specs)

OpenSpec **Source and Derivation** entries MUST link to co-located FR files (relative paths such as `../../../../functional-requirements/problem1/...` from `changes/add-god-analysis-api/`).

Each scenario folder MUST contain `README.md`, `specs/`, and `gherkin/` (exactly one `@acceptance-test` scenario). Case 3 documents pending status only.

### Metrics scorecard

Canonical definitions live in `benchmarks/README.md`:

- Efficiency: `wall_clock_s`, `active_agent_s`, token fields, `cost_usd`.
- Outcome: `acceptance_pass`, `acceptance_coverage`, `rework_turns`, `artifact_completeness`.
- Protocol labels: `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`, `retry_count`, `human_intervention_min`.
- Minimal v1 required fields: `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`, plus labels.
- Rank cost/tokens among `acceptance_pass = true` runs; failures separate; exclude Case 3 until defined.
- Prefer same-tool/same-model cells when comparing Scenario 1 → 2 → 4 richness steps.

### Configuration levels and tools

Recorded for campaign protocol only (not automation code):

- Plinth configs: Full Plinth; No OpenSpec; Bare agent.
- Tools: Cursor, Codex, GitHub Copilot, Claude Code.
- Models: catalog deferred.

### Compatibility / breaking-change review

- **NON-BREAKING**: additive documentation harness only.
- No feature toggle required.

### Verification strategy

- Confirm `benchmarks/` layout and required files exist.
- Confirm Case 1 uses only FR README; Case 2 uses full FR `problem1/`; Case 4 adds linked technical OpenSpec.
- Confirm Case 3 is explicitly pending.
- Run `openspec validate --all` for this change.
- Markdown link sanity for new harness Markdown when validating documentation.

## Risks / Trade-offs

- Case 3 pending means four folders exist but only three cases are runnable in the first campaign.
- Without a model catalog, early campaigns must pin models explicitly in run records.
- Manual campaign execution remains operator-driven until a later automation change.

## Open Questions

- What input and workflow define Case 3 (the richness step between Case 2 and Case 4)? **Pending**.
- Where should durable campaign result reports live (docs vs CI artifact)? Deferred.

# Plinth project benchmark harness

Reproducible **project effectiveness** benchmark for comparing agent outcomes on the God Analysis API problem as **information richness increases** from Scenario 1 through Scenario 4 (issue [#1012](https://github.com/jabrena/plinth/issues/1012)).

This harness is documentation and campaign protocol only (Markdown + Gherkin). It is **not** a Maven module and is **not** part of the JVM `/benchmark` command.

## Information richness progression

Each runnable scenario adds structured inputs so agents have more to build from than the previous case:

| Level | Scenario | What the agent gets | Intent |
| --- | --- | --- | --- |
| Minimal | `scenario1` | Only a problem README under `specs/functional-requirements/` | Baseline with sparse functional notes |
| Functional package | `scenario2` | Full `specs/functional-requirements/problem1/` (user story, Gherkin, OpenAPI, ADRs) | Rich functional requirements, still no OpenSpec |
| Functional + technical | `scenario3` | Same functional package **plus** `specs/technical-requirements/openspec/` linked to those FR files | OpenSpec technical plan derived from co-located FR |
| Functional + technical | `scenario4` | Same functional package **plus** `specs/technical-requirements/openspec/` linked to those FR files | OpenSpec technical plan derived from co-located FR |

Campaigns SHOULD compare cost/tokens/quality across this ladder for the same product acceptance outcome.

## Layout

```text
benchmarks/
├── README.md
├── scenario1/                # minimal functional notes
│   ├── README.md
│   ├── specs/functional-requirements/README.md
│   └── gherkin/scenario1.feature
├── scenario2/                # full functional requirements
│   ├── README.md
│   ├── specs/functional-requirements/problem1/
│   └── gherkin/scenario2.feature
├── scenario3/                # functional + technical (OpenSpec)
│   ├── README.md
│   ├── specs/
│   │   ├── functional-requirements/problem1/
│   │   └── technical-requirements/openspec/
│   └── gherkin/scenario3.feature
└── scenario4/                # functional + technical (OpenSpec)
    ├── README.md
    ├── specs/
    │   ├── functional-requirements/problem1/
    │   └── technical-requirements/openspec/
    └── gherkin/scenario4.feature
```

Each scenario folder owns its input contract (`README.md`), requirements under `specs/`, acceptance criteria (`gherkin/` with exactly one `@acceptance-test` scenario), and run records under `results/` (JSON per completed run).

## Scenario table (Cases 1–4)

| Scenario | Case id | Input | Runnable |
| --- | --- | --- | --- |
| `scenario1` | `case-1-readme-only` | `benchmarks/scenario1/specs/functional-requirements/README.md` only | Yes |
| `scenario2` | `case-2-all-problem1-requirements` | `benchmarks/scenario2/specs/functional-requirements/problem1/` (full inventory) | Yes |
| `scenario3` | `case-3-current-openspec-problem1` | `specs/functional-requirements/problem1/` + `specs/technical-requirements/openspec/` (OpenSpec links to FR) | Yes |
| `scenario4` | `case-4-current-openspec-problem1` | `specs/functional-requirements/problem1/` + `specs/technical-requirements/openspec/` (OpenSpec links to FR) | Yes |

Upstream provenance may originate from `examples/openspec/god-analysis-api/`. Runnable scenario authority is the harness-local `specs/` trees above. Do not rewrite the upstream example trees as part of a campaign run.

## Metrics scorecard

Canonical field definitions for every scenario execution record.

### Efficiency

| Field | Meaning |
| --- | --- |
| `wall_clock_s` | Elapsed wall-clock seconds for the run |
| `active_agent_s` | Seconds the agent was actively working (excludes idle wait where measurable) |
| Token fields | Include at least `tokens_total`; record prompt/completion breakdown when available |
| `cost_usd` | Estimated or billed USD cost for the run |

### Outcome quality

| Field | Meaning |
| --- | --- |
| `acceptance_pass` | Boolean: whether the run met the scenario `@acceptance-test` outcome |
| `acceptance_coverage` | Share of acceptance criteria satisfied (when measurable) |
| `rework_turns` | Number of human or agent rework turns after the first attempt |
| `artifact_completeness` | Whether required deliverables for the case were produced |

### Protocol labels

| Field | Meaning |
| --- | --- |
| `scenario` | Folder id: `scenario1` … `scenario4` |
| `case_id` | Stable case id (for example `case-1-readme-only`) |
| `tool` | Agent tool used for the run |
| `model` | Model identifier used for the run |
| `plinth_config` | Plinth configuration level |
| `commit` | Git commit SHA of the workspace under test |
| `retry_count` | Number of retries for this run |
| `human_intervention_min` | Minutes of human intervention |

### Plinth usage

| Field | Meaning |
| --- | --- |
| `skills_count` | Number of distinct skills read or invoked during the run (must equal the length of `skills`; 0 for bare-agent / skill-agnostic runs) |
| `agents_count` | Number of distinct agents or subagents invoked during the run (must equal the length of `agents`; primary agent counts as 1 when no subagents are used) |
| `skills` | Required array of skill ids or names used during the run (empty array when none) |
| `agents` | Required array of agent ids or names invoked during the run (for example primary agent or subagent slugs) |

### Minimal v1 required fields

Every run record MUST include:

- Efficiency / outcome: `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`
- Labels: `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`
- Plinth usage: `skills_count`, `agents_count`, `skills`, `agents`

Optional but recommended when available: `active_agent_s`, `acceptance_coverage`, `artifact_completeness`, `retry_count`, `human_intervention_min`.

### Ranking rules

1. Rank by `cost_usd` and/or `tokens_total` only among runs where `acceptance_pass = true`.
2. Keep `acceptance_pass = false` runs in a separate failure cohort; do not mix them into the cost/token leaderboard.
3. Prefer same-tool/same-model cells when comparing Scenario 1 → 2 → 3 → 4 richness steps.

## Out of scope

This harness does **not** define JVM `/benchmark` workloads. JMeter, Gatling, and JMH application performance testing remain out of scope for `benchmarks/`.

## Optional campaign protocol notes

Recorded for operator guidance in this change; not automation code.

### Plinth configurations

- **Full Plinth** — full Plinth agents, skills, and OpenSpec workflow
- **No OpenSpec** — Plinth without OpenSpec change management
- **Bare agent** — agent without Plinth project scaffolding

### Tools

- Cursor
- Codex
- GitHub Copilot
- Claude Code

### Models

Model catalog is deferred (TBD / follow-up ADR). Early campaigns MUST pin the model explicitly in each run record via the `model` label.

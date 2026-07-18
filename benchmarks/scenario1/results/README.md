# Scenario 1 results — how to measure and store

Each completed Case 1 run MUST write one JSON file under this directory:

```text
benchmarks/scenario1/results/<run-id>.json
```

Suggested `run-id` pattern:

```text
YYYYMMDDTHHMMSSZ-<tool>-<model-slug>
```

Example: `20260717T180530Z-cursor-gpt5.json`

Do not commit secrets. Token/cost values may be estimates when the tool only exposes approximate usage.

## How to measure each field

### Efficiency

| Field | How to measure |
| --- | --- |
| `wall_clock_s` | Record wall-clock time at run start and end (when the operator starts the agent task until stop: acceptance gate pass/fail or operator abort). Store elapsed seconds as a number (float allowed). |
| `active_agent_s` | Optional. Sum intervals where the agent/model was actively generating or using tools; exclude human idle waits. If unavailable, omit the field. |
| `tokens_in` | Optional. Prompt / input tokens from the tool usage panel or API usage export. |
| `tokens_out` | Optional. Completion / output tokens from the same source. |
| `tokens_total` | Required. Prefer `tokens_in + tokens_out`. If the tool only reports a total, store that total. |
| `cost_usd` | Required. Billed or estimated USD for the run from provider/tool pricing for the recorded `model`. If only tokens are known, estimate with the published unit price and note estimation in `notes` (optional string field). |

### Outcome quality

| Field | How to measure |
| --- | --- |
| `acceptance_pass` | Required boolean. `true` only if the product acceptance in the functional README happy path is met (for Case 1: implement in `demo/` and satisfy documented sum behavior / operator checklist). Also require this feature’s setup constraints to hold. |
| `rework_turns` | Required integer. Count extra human correction prompts after the first implementation attempt (0 if first attempt accepted). |
| `acceptance_coverage` | Optional 0–1. Fraction of product + harness acceptance checks passed. |
| `artifact_completeness` | Optional 0–1. Whether expected deliverables exist under `demo/` (buildable app, endpoint, tests as judged by the operator checklist). |

### Protocol labels

| Field | How to measure |
| --- | --- |
| `scenario` | Constant: `"scenario1"` |
| `case_id` | Constant: `"case-1-readme-only"` |
| `tool` | Agent product used: `cursor`, `codex`, `github-copilot`, or `claude-code` |
| `model` | Exact model id/name shown by the tool for the run |
| `plinth_config` | Configuration level used for the run (for example `bare-agent`, `no-openspec`, or `full-plinth` per campaign protocol). |
| `commit` | `git rev-parse HEAD` of the workspace under test when the run starts |
| `retry_count` | Optional. Number of aborted/restarted attempts before the recorded run |
| `human_intervention_min` | Optional. Minutes the human spent steering, answering prompts, or fixing outside agent turns |

### Plinth usage

| Field | How to measure |
| --- | --- |
| `skills_count` | Required non-negative integer. Count distinct skills read or invoked during the run; MUST equal the length of `skills`. |
| `agents_count` | Required non-negative integer. Count distinct agents or subagents invoked during the run; MUST equal the length of `agents`. Record `1` with a one-element `agents` array when only the primary agent runs. |
| `skills` | Required JSON array of skill ids or names used during the run (for example `323-frameworks-spring-boot-testing-acceptance-tests`). Use `[]` when none. |
| `agents` | Required JSON array of agent ids or names invoked during the run (for example `composer` or `robot-java-spring-boot-coder`). |

## Minimal v1 JSON shape

```json
{
  "wall_clock_s": 1234.5,
  "tokens_total": 89000,
  "cost_usd": 2.45,
  "acceptance_pass": true,
  "rework_turns": 1,
  "scenario": "scenario1",
  "case_id": "case-1-readme-only",
  "tool": "cursor",
  "model": "example-model-id",
  "plinth_config": "bare-agent",
  "commit": "0123456789abcdef0123456789abcdef01234567",
  "skills_count": 0,
  "agents_count": 1,
  "skills": [],
  "agents": ["composer"]
}
```

Optional fields may be added without removing required ones:

```json
{
  "active_agent_s": 900.0,
  "tokens_in": 50000,
  "tokens_out": 39000,
  "acceptance_coverage": 1.0,
  "artifact_completeness": 1.0,
  "retry_count": 0,
  "human_intervention_min": 15,
  "notes": "cost_usd estimated from token totals"
}
```

## Operator checklist (measure → store)

1. Confirm Case 1 setup (README-only FR) before starting.
2. Note `commit`, `tool`, `model`, `plinth_config`.
3. Start timer (`wall_clock_s`).
4. Run the agent against `benchmarks/scenario1/demo/` using only the Case 1 README input.
5. Track `rework_turns` and optional human minutes.
6. Stop timer when done; capture tokens, cost, `skills_count`, `agents_count`, `skills`, and `agents` from the tool or operator tally.
7. Set `acceptance_pass` from product + harness checks.
8. Write `benchmarks/scenario1/results/<run-id>.json` with all required fields.
9. Restore `benchmarks/scenario1/demo/` to empty (only `.gitkeep`).
10. Rank later using `benchmarks/README.md` rules (`acceptance_pass = true` only).

See also: [scenario1.feature](../gherkin/scenario1.feature), [../README.md](../README.md) metrics scorecard.

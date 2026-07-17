# Scenario 2 results — how to measure and store

Each completed Case 2 run MUST write one JSON file under this directory:

```text
benchmarks/scenario2/results/<run-id>.json
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
| `acceptance_pass` | Required boolean. `true` only if the product acceptance in the functional requirements happy path is met (for Case 2: implement in `demo/` using the full `problem1/` package and satisfy documented sum behavior / operator checklist). Also require this feature’s setup constraints to hold. |
| `rework_turns` | Required integer. Count extra human correction prompts after the first implementation attempt (0 if first attempt accepted). |
| `acceptance_coverage` | Optional 0–1. Fraction of product + harness acceptance checks passed. |
| `artifact_completeness` | Optional 0–1. Whether expected deliverables exist under `demo/` (buildable app, endpoint, tests as judged by the operator checklist). |

### Protocol labels

| Field | How to measure |
| --- | --- |
| `scenario` | Constant: `"scenario2"` |
| `case_id` | Constant: `"case-2-all-problem1-requirements"` |
| `tool` | Agent product used: `cursor`, `codex`, `github-copilot`, or `claude-code` |
| `model` | Exact model id/name shown by the tool for the run |
| `plinth_config` | Configuration level used for the run (for example `bare-agent`, `no-openspec`, or `full-plinth` per campaign protocol). |
| `commit` | `git rev-parse HEAD` of the workspace under test when the run starts |
| `retry_count` | Optional. Number of aborted/restarted attempts before the recorded run |
| `human_intervention_min` | Optional. Minutes the human spent steering, answering prompts, or fixing outside agent turns |

## Minimal v1 JSON shape

```json
{
  "wall_clock_s": 1234.5,
  "tokens_total": 89000,
  "cost_usd": 2.45,
  "acceptance_pass": true,
  "rework_turns": 1,
  "scenario": "scenario2",
  "case_id": "case-2-all-problem1-requirements",
  "tool": "cursor",
  "model": "example-model-id",
  "plinth_config": "bare-agent",
  "commit": "0123456789abcdef0123456789abcdef01234567"
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

1. Confirm Case 2 setup (full `problem1/` FR inventory; no technical OpenSpec) before starting.
2. Note `commit`, `tool`, `model`, `plinth_config`.
3. Start timer (`wall_clock_s`).
4. Run the agent against `benchmarks/scenario2/demo/` using the full Case 2 `problem1/` package.
5. Track `rework_turns` and optional human minutes.
6. Stop timer when done; capture tokens and cost from the tool.
7. Set `acceptance_pass` from product + harness checks.
8. Write `benchmarks/scenario2/results/<run-id>.json` with all required fields.
9. Rank later using `benchmarks/README.md` rules (`acceptance_pass = true` only).

See also: [scenario2.feature](../gherkin/scenario2.feature), [../README.md](../README.md) metrics scorecard.

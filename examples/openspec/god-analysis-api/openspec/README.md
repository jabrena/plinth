# OpenSpec Project: God Analysis API

This OpenSpec project is a fresh example derived from the co-located source
requirements in `examples/openspec/god-analysis-api/requirements/problem1`.

## Derivation

- Source direction: requirements, Gherkin, OpenAPI, and ADRs -> OpenSpec change.
- Change boundary: one atomic API capability, `god-analysis-api`.
- Pending change: `add-god-analysis-api`.
- Capability delta: `changes/add-god-analysis-api/specs/god-analysis-api/spec.md`.

The source artifacts remain authoritative for this example. The OpenSpec change
records the planned capability without rewriting the co-located requirements.

## Layout

```text
openspec/
├── config.yaml
├── README.md
├── changes/
│   └── add-god-analysis-api/
│       ├── proposal.md
│       ├── design.md
│       ├── tasks.md
│       └── specs/
│           └── god-analysis-api/
│               └── spec.md
```

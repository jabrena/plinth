# God Analysis API OpenSpec Example

This example creates a standalone OpenSpec project from the requirements in
[`examples/requirements/problem1`](../../requirements/problem1/README.md).

The OpenSpec project root is [`openspec/`](openspec/). It contains one pending
change, `add-god-analysis-api`, for the US-001 God Analysis API capability.

## Source Artifacts

- [`US-001_God_Analysis_API.md`](../../requirements/problem1/US-001_God_Analysis_API.md)
- [`US-001_god_analysis_api.feature`](../../requirements/problem1/US-001_god_analysis_api.feature)
- [`US-001-god-analysis-api.openapi.yaml`](../../requirements/problem1/US-001-god-analysis-api.openapi.yaml)
- [`ADR-001-God-Analysis-API-Functional-Requirements.md`](../../requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md)
- [`ADR-002-God-Analysis-API-Non-Functional-Requirements.md`](../../requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md)
- [`ADR-003-God-Analysis-API-Technology-Stack.md`](../../requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md)

## Validate

Run OpenSpec from this directory:

```bash
cd examples/openspec/god-analysis-api
openspec validate --all
```


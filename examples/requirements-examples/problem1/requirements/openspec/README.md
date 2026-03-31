# OpenSpec artifacts (US-001 God Analysis API)

This directory is the **OpenSpec project root** expected by the OpenSpec CLI: it **must** be named `openspec` so tools resolve `./openspec/changes` and `./openspec/specs` from the parent project directory.

**Authoritative inputs** (parent folder: `../agile/` for user story, Gherkin, OpenAPI, and implementation plan; `../adr/` for ADRs):

| Artifact | Role |
|----------|------|
| [US-001_God_Analysis_API.md](../agile/US-001_God_Analysis_API.md) | User story |
| [US-001_god_analysis_api.feature](../agile/US-001_god_analysis_api.feature) | Gherkin acceptance scenarios |
| [US-001-god-analysis-api.openapi.yaml](../agile/US-001-god-analysis-api.openapi.yaml) | HTTP contract |
| [ADR-001](../adr/ADR-001-God-Analysis-API-Functional-Requirements.md) … [ADR-003](../adr/ADR-003-God-Analysis-API-Technology-Stack.md) | Architecture decisions |
| [US-001-plan-analysis.plan.md](../agile/US-001-plan-analysis.plan.md) | Implementation plan and task list |

**Layout:**

```
openspec/
├── config.yaml
├── README.md                 # this file
├── specs/                    # Capability specs (source of truth after archive)
│   └── god-analysis-api/
│       └── spec.md
└── changes/
    └── us-001-god-analysis-api/
        ├── proposal.md
        ├── design.md
        ├── tasks.md
        └── specs/
            └── god-analysis-api/
                └── spec.md   # Delta (ADDED requirements) for this change
```

## Validate

From the **parent** of `openspec/` (here: `examples/requirements-examples/problem1/requirements`):

```bash
cd examples/requirements-examples/problem1/requirements
npx @fission-ai/openspec@latest validate --all
```

OpenSpec discovers items relative to the current working directory, not via a path argument to a nested folder.

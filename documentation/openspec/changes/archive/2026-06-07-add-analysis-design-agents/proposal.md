## Why

Issue [#806](https://github.com/jabrena/cursor-rules-java/issues/806) requires clear ownership for analysis, architecture, planning, and delivery. Agent work can proceed independently when it owns only canonical agent assets, the agent installer/inventory, migration behavior, and focused tests.

## What Changes

- Promote the legacy `robot-architect` definition into canonical embedded sources and extend it with design exploration and ADR missions.
- **BREAKING**: Rename `robot-coordinator` to `robot-tech-lead`.
- Extend `robot-tech-lead` with independent plan/spec creation while retaining framework-aware coder delegation.
- Extend `robot-business-analyst` with issue-creation ownership and read-only alignment/readiness review.
- Update agent installation assets, agent inventory, collaboration links, migration guidance embedded in the agent sources, and focused tests.

## Capabilities

### New Capabilities

- `analysis-design-agents`: Defines business analyst, architect, and tech lead missions and delegation boundaries.

### Modified Capabilities

None.

## Impact

- Owns canonical files under `skill-references/assets/agents/`, `005-agents-installation.xml`, `java-agents-inventory-template.md`, and agent-focused tests.
- Direct `@robot-coordinator` mentions and installed filenames require migration to `@robot-tech-lead`.
- Existing coder-agent names and framework routing remain unchanged.
- Does not own command assets, XML planning skills, README files, localized guides, or public generated `skills/`.

## Context

Three parallel child changes own commands, agents, and planning skills. Their outputs converge in README summaries, inventories, localized getting-started guides, lifecycle examples, and final validation.

Starting this change before the three prerequisites stabilize would create repeated documentation churn and merge conflicts.

## Goals / Non-Goals

**Goals:**

- Present one coherent lifecycle across all supported languages.
- Explain the breaking agent rename and artifact authority.
- Verify the integrated source changes and local generated skills.

**Non-Goals:**

- Implement command, agent, or XML skill behavior.
- Refresh public `skills/` with the release profile.
- Edit generated `docs/`.

## Decisions

### Run this change after source integration

This change is blocked by:

1. `add-analysis-design-commands`
2. `add-analysis-design-agents`
3. `add-composable-planning-workflows`

Documentation uses the final integrated names, counts, and workflow behavior.

### Keep lifecycle paths composable

Document feature-branch as optional for read-only exploration and recommended before durable artifacts. Document worktrees as the isolation mechanism for parallel child changes. Document plan and OpenSpec as independent workflows rather than a mandatory sequence.

### Centralize final validation

Prerequisite changes run focused verification. This integration change runs full generator, local generation, Markdown, skill-check, stale-reference, and OpenSpec validation.

## Risks / Trade-offs

- [Risk] Documentation begins before prerequisites settle. -> Keep tasks blocked until integration.
- [Risk] Localized content drifts. -> Update matching English, Spanish, and Chinese files together.
- [Risk] Final validation finds ownership defects. -> Route fixes to the child change owning the affected source.

## Migration Plan

1. Integrate the three source changes.
2. Update README and guides.
3. Regenerate only local skills.
4. Run all final checks.

## Open Questions

None. The migration is an immediate rename from `robot-coordinator` to `robot-tech-lead` without a compatibility alias.

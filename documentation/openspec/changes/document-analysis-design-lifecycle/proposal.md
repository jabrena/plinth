## Why

The command, agent, and planning-skill changes for issue [#806](https://github.com/jabrena/cursor-rules-java/issues/806) need one integration change to synchronize English, Spanish, and Chinese documentation, explain migration, and run final repository-wide validation after the parallel source changes are integrated.

## What Changes

- Update README command/agent summaries in all three languages.
- Update generated-source command and agent inventory guides from canonical templates.
- Update agent and workflow getting-started guides in English, Spanish, and Chinese.
- Document feature-branch transition, isolated worktrees for parallel changes, design exploration, plan-only, spec-first, plan-to-spec, ADR/diagram, alignment-review, and delivery paths.
- Document coordinator-to-tech-lead migration, artifact authority, derivation direction, conflict handling, and OpenSpec change decomposition.
- Run final Maven, local generation, Markdown, skill-check, and OpenSpec validation after all prerequisite changes are integrated.

## Capabilities

### New Capabilities

- `analysis-design-lifecycle-documentation`: Defines the integrated lifecycle, migration guidance, and final validation expectations.

### Modified Capabilities

None.

## Impact

- Depends on `add-analysis-design-commands`, `add-analysis-design-agents`, and `add-composable-planning-workflows`.
- Owns README files and documentation guides; it does not edit command, agent, or skill source files.
- Final validation may expose integration drift that must be fixed in the owning prerequisite change.

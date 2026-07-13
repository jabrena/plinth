## Design Summary

The skill system must remain focused on end-user scenarios. Repository-maintenance guidance (such as Plinth module names and Plinth-specific build commands) is out of scope for user-facing skills and should not appear in skill content.

## Approach

- Update the **source of truth** (skill XML under `plinth-skills-generator/src/main/resources/`) rather than editing generated outputs.
- Replace Plinth-specific module/build references with repository-agnostic wording:
  - Prefer phrasing like “run your project’s build/verification” instead of referencing `plinth-*-generator` module names.
  - Use generic module examples only when needed (e.g., `:app`, `:domain`, `:infra`) and keep them clearly illustrative.
- Prove compliance with a focused scan across generated outputs after regeneration.

## Validation Strategy

- Regenerate local skills (so `.agents/skills/**` reflects the updated XML sources).
- Verify that generated skill documentation no longer contains Plinth-internal repository/module references.
- Keep contributor-facing documentation that intentionally references Plinth modules out of scope for this change.

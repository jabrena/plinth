## Why

GitHub issue [#1039](https://github.com/jabrena/plinth/issues/1039) tracks technical debt: several skills include references to Plinth’s internal Maven module names and build commands (for example `plinth-*-generator` and `./mvnw ... -pl plinth-...`). Skills are intended to guide **end-user scenarios** and should remain usable in any user project without referencing the Plinth repository internals.

## What Changes

- Review skill XML sources for `056` and all `70x` skills.
- Remove or rewrite any references to Plinth repository internals (module names, repository paths, regeneration commands that name Plinth modules).
- Replace those examples with project-neutral wording that applies to user repositories.
- Regenerate local skill outputs (and validate that generated skill content contains no Plinth-internal module references).

## Capabilities

### Modified Capabilities

- `skills-repo-agnostic-skill-content`: Tightens skill content constraints so user-facing skills do not reference Plinth repository internals.

## Source and Derivation

- Source artifact: GitHub issue [#1039](https://github.com/jabrena/plinth/issues/1039).
- Local OpenSpec guidance: `042-planning-openspec`.
- Derivation direction: issue story and acceptance criteria -> OpenSpec capability requirements and scenarios -> XML source edits -> regenerated skills -> validation evidence.

## Change Boundary Assessment

This is one OpenSpec change because the outcome is a single user-facing quality improvement: targeted skills become repository-agnostic. The work spans multiple skill files, but shares one validation approach and one release boundary.

## Impact

This change affects skill XML sources under `plinth-skills-generator/src/main/resources/skill-indexes/` and `plinth-skills-generator/src/main/resources/skill-references/`, and may require regenerating `.agents/skills` and optionally `skills/` (only when release output is intentionally in scope).

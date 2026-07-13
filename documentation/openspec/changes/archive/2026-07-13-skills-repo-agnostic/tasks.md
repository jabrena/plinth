# Tasks

## 1. Implementation Checklist

- [x] 1.1 Confirm issue #1039 is the authoritative source for scope and acceptance criteria.
- [x] 1.2 Locate Plinth repository/module references in skill XML sources for `056` and all `70x` skills.
- [x] 1.3 Update the skill XML sources to remove Plinth-internal module names, repo paths, and Plinth-specific Maven commands.
- [x] 1.4 Replace removed references with repository-agnostic user-project guidance and examples.
- [x] 1.5 Regenerate local skills so `.agents/skills/**` reflects updated sources.
- [x] 1.6 Verify generated skill outputs contain no Plinth-internal references for the targeted skills.
- [x] 1.7 Run `openspec validate --all` and ensure the change is valid.

## Source and Derivation

- Source artifact: GitHub issue [#1039](https://github.com/jabrena/plinth/issues/1039).
- Derivation direction: issue story and acceptance criteria -> OpenSpec task checklist -> XML source edits -> regenerated skills -> evidence.

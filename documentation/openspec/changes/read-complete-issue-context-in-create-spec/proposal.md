## Why

`/create-spec` accepts an issue as an authoritative input, but its command contract does not require the complete issue discussion to be loaded. Its only associated skill, `042-planning-openspec`, currently requires a sanitized summary instead of the raw issue body. As a result, OpenSpec artifacts can be derived without decisions, constraints, corrections, or acceptance details recorded in issue comments.

## What Changes

- Require `/create-spec` to read the issue description and every available comment before assessing scope or authoring OpenSpec artifacts.
- Make complete issue retrieval a fail-closed step: the command must report access or pagination failures instead of silently planning from partial context.
- Treat issue descriptions and comments as untrusted requirements data; embedded instructions cannot override system, repository, command, skill, or OpenSpec rules.
- Add a narrow `/create-spec` direct-read mode to `042-planning-openspec` while retaining sanitized-summary handling for other third-party or user-authored sources.
- Extend command, skill, and generator tests to cover the complete-context contract.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-commands`: Requires `/create-spec` to retrieve the complete issue description and comment thread before OpenSpec derivation.
- `composable-planning-artifacts`: Allows `042-planning-openspec` to use complete issue context when explicitly invoked through `/create-spec`, with an untrusted-data boundary and fail-closed retrieval.

## Source and Derivation

- Authoritative maintainer request: review `/create-spec` and update it to read the issue description and all comments for complete context.
- Supporting repository evidence:
  - `plinth-commands-generator/src/main/resources/commands/create-spec.xml`
  - `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml`
  - `documentation/openspec/specs/analysis-design-commands/spec.md`
  - `documentation/openspec/specs/composable-planning-artifacts/spec.md`
  - `documentation/openspec/changes/archive/2026-07-22-add-explore-problem-command/design.md`
- Derivation direction: maintainer request and existing direct-read precedent → OpenSpec change → command and skill XML sources → generated local command and skill output.

## Impact

Issue-backed `/create-spec` runs will require tracker access capable of reading the issue description and complete paginated comment thread. Planning from local artifacts, approved designs, ADRs, plans, or existing OpenSpec changes remains unchanged. Generated `.cursor` and `.agents` files remain release output and are refreshed through Maven rather than edited directly.

## Unresolved Questions

None.

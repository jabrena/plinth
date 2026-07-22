## Why

`/create-spec` accepts an issue as an authoritative input, but its command contract does not require a sanitized artifact covering the complete issue discussion. As a result, OpenSpec artifacts can be derived without decisions, constraints, corrections, or acceptance details recorded in issue comments.

## What Changes

- Require `/create-spec` to use a maintainer-prepared sanitized artifact derived outside the agent context from the issue description and every available comment.
- Make complete coverage a fail-closed step: the artifact must confirm description and paginated-comment coverage before planning starts.
- Prohibit raw issue descriptions and comments from entering the agent context.
- Retain sanitized-summary handling for other third-party or user-authored sources.
- Extend command, skill, and generator tests to cover the complete-context contract.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-commands`: Requires `/create-spec` to retrieve the complete issue description and comment thread before OpenSpec derivation.
- `composable-planning-artifacts`: Requires `042-planning-openspec` to use sanitized complete issue context when invoked through `/create-spec`, with a third-party-content boundary and fail-closed coverage validation.

## Source and Derivation

- Authoritative maintainer request: review `/create-spec` and update it to read the issue description and all comments for complete context.
- Supporting repository evidence:
  - `plinth-commands-generator/src/main/resources/commands/create-spec.xml`
  - `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml`
  - `documentation/openspec/specs/analysis-design-commands/spec.md`
  - `documentation/openspec/specs/composable-planning-artifacts/spec.md`
  - `documentation/openspec/changes/archive/2026-07-22-add-explore-problem-command/design.md`
- Derivation direction: maintainer request and Snyk W011 security finding → OpenSpec change → command and skill XML sources → generated local command and skill output.

## Impact

Issue-backed `/create-spec` runs will require a maintainer-prepared sanitized artifact confirming coverage of the issue description and complete paginated comment thread. Planning from local artifacts, approved designs, ADRs, plans, or existing OpenSpec changes remains unchanged. Generated `.cursor` and `.agents` files remain release output and are refreshed through Maven rather than edited directly.

## Unresolved Questions

None.

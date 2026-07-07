## Why

Issue [#806](https://github.com/jabrena/plinth/issues/806) requires design discovery and planning/OpenSpec workflows that can start independently from the artifacts a team already uses. This behavior belongs in XML skill sources and can be developed independently from command and agent asset changes.

## What Changes

- Add a new architecture design-discovery skill after satisfying the repository approval gate for new XML files.
- Refactor `041-planning-plan-mode` to accept issue, approved design, ADR, OpenSpec, or combined inputs.
- Refactor `042-planning-openspec` from plan-only conversion into independent OpenSpec creation/update.
- Add one-change versus multiple-change scope assessment to OpenSpec creation.
- Define concern-specific artifact authority, source recording, derivation direction, no silent two-way synchronization, and explicit conflict resolution.

## Capabilities

### New Capabilities

- `composable-planning-artifacts`: Defines design discovery, independent plan/spec workflows, artifact authority, controlled derivation, change decomposition, and alignment expectations.

### Modified Capabilities

None.

## Impact

- Owns the new design-discovery skill XML/index, skill `041` XML/index, skill `042` XML/index, `skills.xml`, and skill-focused tests.
- Requires explicit confirmation before adding the new XML files, per repository boundaries.
- Does not own command assets, agent assets, README files, localized guides, or public generated `skills/`.

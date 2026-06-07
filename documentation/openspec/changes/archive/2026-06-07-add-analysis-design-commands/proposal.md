## Why

Issue [#806](https://github.com/jabrena/cursor-rules-java/issues/806) requires clear command entry points for the analysis and design phase. Command work can be delivered independently from agent-role and planning-skill changes when ownership remains limited to embedded command assets, their installer, inventory, and focused tests.

## What Changes

- Add `/create-issue`, `/explore-design`, `/create-adr`, `/create-diagram`, `/create-plan`, `/create-spec`, and `/review-alignment` command assets.
- Add `/create-worktree` for isolated or parallel Git work.
- Extend the existing `/create-feature-branch` command as the optional transition into repository-backed analysis/design work.
- Define concise command contracts for inputs, owning agent, associated capabilities, workflow, outputs, and safeguards.
- Update the command installer bundle, command inventory source, and focused generator tests.

## Capabilities

### New Capabilities

- `analysis-design-commands`: Defines the eight new analysis/design/support commands and the extended feature-branch transition.

### Modified Capabilities

None.

## Impact

- Owns `skills-generator/src/main/resources/skill-references/assets/commands/`, `004-commands-installation.xml`, `java-commands-inventory-template.md`, and command-focused tests.
- Does not change agents, planning/OpenSpec XML skills, README files, localized guides, or public generated `skills/`.
- Commands may reference agent and skill names delivered by sibling changes, but this change does not edit those sources.

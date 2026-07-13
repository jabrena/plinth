Feature: Validate changes from usage of embedded commands inventory skill

Background:
  Given the skill "001-commands-inventory"
  And the inventory sandbox folder "examples/skills/inventory" has no git changes

@acceptance-test
Scenario: Generate embedded commands inventory with every command asset
  Given the local generated skill path ".agents/skills/001-commands-inventory"
  And the command assets directory "commands-generator/src/main/resources/commands"
  And the requested inventory output path is "examples/skills/inventory/INVENTORY-COMMANDS-JAVA.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/001-commands-inventory" is applied to generate the embedded commands inventory
  Then the skill reads "references/001-commands-inventory.md"
  And the generated inventory file exists at "examples/skills/inventory/INVENTORY-COMMANDS-JAVA.md"
  And the generated inventory file includes the heading "# Embedded Commands Inventory"
  And the generated inventory file includes the section "## Embedded commands"
  And the generated inventory file includes the section "## Installation target options"
  And the generated inventory file includes the installation targets ".github/commands", ".claude/commands", ".cursor/command", and ".codex/commands"
  And the generated inventory file includes exactly one row for each embedded command asset:
    | assetFile                | command                  |
    | update-issue.md          | /update-issue            |
    | create-feature-branch.md | /create-feature-branch   |
    | create-worktree.md       | /create-worktree         |
    | explore-design.md        | /explore-design          |
    | create-adr.md            | /create-adr              |
    | create-diagram.md        | /create-diagram          |
    | create-spec.md           | /create-spec             |
    | review-alignment.md      | /review-alignment        |
    | implement-spec.md       | /implement-spec         |
    | profile.md               | /profile                 |
    | benchmark.md             | /benchmark               |
  And every command row in the generated file corresponds to a same-named source file in "commands-generator/src/main/resources/commands"
  And no command asset from "commands-generator/src/main/resources/commands" is missing from the generated inventory
  And the generated inventory file does not include command rows outside the embedded command assets directory
  And any git changes produced under "examples/skills/inventory" during skill execution and verification are reset

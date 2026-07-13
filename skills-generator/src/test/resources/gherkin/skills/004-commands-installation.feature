Feature: Validate changes from usage of embedded commands installation skill

Background:
  Given the skill "004-commands-installation"
  And the installation sandbox folder "examples/skills/installers" has no git changes

@acceptance-test
Scenario: Install embedded commands into the GitHub commands destination
  Given the local generated skill path ".agents/skills/004-commands-installation"
  And the installation sandbox root is "examples/skills/installers"
  And all installation paths are resolved relative to the installation sandbox root
  And the selected command destination is ".github/commands"
  And the expected target directory is "examples/skills/installers/.github/commands"
  When the skill ".agents/skills/004-commands-installation" is applied to install embedded project commands
  Then the skill asks "Where do you want to install the embedded project commands?" before copying files
  And the skill presents ".github/commands", ".claude/commands", ".cursor/command", and ".codex/commands" as destination choices
  And the skill waits for the explicit destination answer ".github/commands" before writing files
  And the skill reads "references/004-commands-installation.md"
  And the skill copies embedded command files only from "commands-generator/src/main/resources/commands"
  And the target directory "examples/skills/installers/.github/commands" is created when missing
  And no command files are copied outside "examples/skills/installers"
  And the target directory contains exactly these command files:
    | fileName                 |
    | update-issue.md          |
    | create-feature-branch.md |
    | create-worktree.md       |
    | explore-design.md        |
    | create-adr.md            |
    | create-diagram.md        |
    | create-spec.md           |
    | review-alignment.md      |
    | implement-spec.md       |
    | profile.md               |
    | benchmark.md             |
  And each installed command file matches its same-named embedded asset content
  And the skill reports the selected destination, created files, overwrite actions, and an optional verification step
  And any git changes produced under "examples/skills/installers" during skill execution and verification are reset

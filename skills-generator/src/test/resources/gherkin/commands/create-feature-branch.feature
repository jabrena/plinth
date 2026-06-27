Feature: Validate create-feature-branch command

Background:
  Given the command prompt file ".cursor/commands/create-feature-branch.md"

@acceptance-test
Scenario: Create a conventionally named local feature branch
  Given the user request is "/create-feature-branch improve skill acceptance coverage"
  And the command prompt source ".cursor/commands/create-feature-branch.md" is read before execution
  When the create-feature-branch command is applied to the request
  Then the command checks the current branch and working tree state before creating a branch
  And the command derives a conventional, lowercase, hyphenated branch name from the requested work
  And the command does not overwrite or delete existing branches
  And the command reports the branch name, base branch, working tree status, and next steps
  And any branch or git changes produced during command execution and verification are reset

Feature: Validate create-worktree command

Background:
  Given the command prompt file ".cursor/commands/create-worktree.md"

@acceptance-test
Scenario: Create an isolated branch and linked worktree
  Given the user request is "/create-worktree improve-skill-gherkins"
  And the command prompt source ".cursor/commands/create-worktree.md" is read before execution
  When the create-worktree command is applied to the request
  Then the command checks the current branch, working tree state, target branch name, and worktree path before creating anything
  And the command does not overwrite existing worktree directories or existing branches
  And the command reports the created branch, worktree path, base branch, and cleanup command
  And any branch, worktree, or git changes produced during command execution and verification are reset

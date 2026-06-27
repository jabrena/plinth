Feature: Validate review-breaking-changes command

Background:
  Given the command prompt file ".cursor/commands/review-breaking-changes.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Review an approved plan for breaking change risk
  Given the user request is "/review-breaking-changes documentation/openspec"
  And the command prompt source ".cursor/commands/review-breaking-changes.md" is read before execution
  When the review-breaking-changes command is applied to the request
  Then the command identifies affected APIs, data contracts, configuration, persistence, behavior, and operational surfaces
  And the command distinguishes confirmed breaking changes from possible risks and non-breaking changes
  And the command recommends migration, compatibility, rollout, and validation steps for each confirmed risk
  And the command keeps artifacts unchanged unless the user explicitly requests fixes
  And any git changes produced during command execution and verification are reset

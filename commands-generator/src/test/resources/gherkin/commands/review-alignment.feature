Feature: Validate review-alignment command

Background:
  Given the command prompt file ".cursor/commands/review-alignment.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Review planning artifacts for consistency and readiness
  Given the user request is "/review-alignment documentation/openspec"
  And the command prompt source ".cursor/commands/review-alignment.md" is read before execution
  When the review-alignment command is applied to the request
  Then the command identifies every artifact included in the alignment review
  And the command checks consistency, traceability, completeness, conflicts, and implementation readiness
  And the command reports findings ordered by severity with source file references
  And the command keeps artifacts unchanged unless the user explicitly requests fixes
  And any git changes produced during command execution and verification are reset

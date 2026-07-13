Feature: Validate update-issue command

Background:
  Given the command prompt file ".cursor/commands/update-issue.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Update an existing issue with evidence-backed content
  Given the user request is "/update-issue 123 add acceptance test coverage evidence"
  And the command prompt source ".cursor/commands/update-issue.md" is read before execution
  When the update-issue command is applied to the request
  Then the command identifies the target issue, requested update, evidence source, acceptance criteria changes, and validation notes
  And the command asks for missing tracker access or sanitized issue context before changing remote issue state
  And the command avoids overwriting existing issue content without preserving relevant context
  And the command reports the proposed issue update, assumptions, validation evidence, and next steps
  And any git changes produced during command execution and verification are reset

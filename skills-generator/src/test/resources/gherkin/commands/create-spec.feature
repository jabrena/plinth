Feature: Validate create-spec command

Background:
  Given the command prompt file ".cursor/commands/create-spec.md"
  And the folder "documentation/openspec" has no git changes

@acceptance-test
Scenario: Create an OpenSpec change from an approved source artifact
  Given the user request is "/create-spec documentation/openspec/example-issue.md"
  And the command prompt source ".cursor/commands/create-spec.md" is read before execution
  When the create-spec command is applied to the request
  Then the command identifies the source artifact, change name, affected specs, proposal, design needs, and task list
  And the command creates or updates OpenSpec artifacts only under "documentation/openspec" when edits are requested
  And the command validates OpenSpec structure before claiming the change is ready
  And the command reports changed files, validation evidence, assumptions, and unresolved planning risks
  And any git changes produced during command execution and verification are reset

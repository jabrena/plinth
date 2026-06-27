Feature: Validate create-diagram command

Background:
  Given the command prompt file ".cursor/commands/create-diagram.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Create an architecture diagram from a selected source artifact
  Given the user request is "/create-diagram documentation/openspec c4"
  And the command prompt source ".cursor/commands/create-diagram.md" is read before execution
  When the create-diagram command is applied to the request
  Then the command identifies the source artifact, diagram type, audience, scope, and notation before creating output
  And the command selects the diagram style that matches the requested system view
  And the command keeps generated diagram files scoped to the requested documentation location
  And the command reports changed files, rendering or syntax validation, assumptions, and open questions
  And any git changes produced during command execution and verification are reset

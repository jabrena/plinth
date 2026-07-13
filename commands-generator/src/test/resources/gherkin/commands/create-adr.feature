Feature: Validate create-adr command

Background:
  Given the command prompt file ".cursor/commands/create-adr.md"
  And the folder "documentation/adr" has no git changes

@acceptance-test
Scenario: Create an ADR from a concrete decision source
  Given the user request is "/create-adr documentation/openspec"
  And the command prompt source ".cursor/commands/create-adr.md" is read before execution
  When the create-adr command is applied to the request
  Then the command identifies the decision source, ADR type, context, alternatives, decision, and consequences
  And the command asks concise clarifying questions when the decision source is incomplete
  And the command writes ADR content only under "documentation/adr" when an ADR file is requested
  And the command preserves traceability to the source artifact used for the decision
  And the command reports changed files, assumptions, validation evidence, and unresolved risks
  And any git changes produced during command execution and verification are reset

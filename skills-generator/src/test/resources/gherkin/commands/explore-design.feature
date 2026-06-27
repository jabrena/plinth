Feature: Validate explore-design command

Background:
  Given the command prompt file ".cursor/commands/explore-design.md"
  And the folder "documentation" has no git changes

@acceptance-test
Scenario: Explore design options before implementation planning
  Given the user request is "/explore-design documentation/openspec/example-issue.md"
  And the command prompt source ".cursor/commands/explore-design.md" is read before execution
  When the explore-design command is applied to the request
  Then the command identifies the design source, problem, constraints, stakeholders, options, trade-offs, and recommendation criteria
  And the command keeps analysis separate from implementation unless the user explicitly requests edits
  And the command records assumptions, rejected options, risks, and validation needed before planning
  And the command reports the recommended design direction and any generated analysis artifact
  And any git changes produced during command execution and verification are reset

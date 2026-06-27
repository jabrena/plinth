Feature: Validate profile command

Background:
  Given the command prompt file ".cursor/commands/profile.md"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Coordinate a Java profiling lifecycle with evidence
  Given the user request is "/profile examples/frameworks/spring-boot"
  And the command prompt source ".cursor/commands/profile.md" is read before execution
  When the profile command is applied to the request
  Then the command identifies the profiling target, workload, baseline command, profiler, and success criteria
  And the command separates detection, analysis, refactoring, and verification phases
  And the command avoids recommending optimizations without profiling evidence
  And the command reports collected evidence, suspected bottlenecks, validation commands, risks, and next steps
  And the folder "examples" has no git changes unless the user explicitly requested profiling setup edits
  And any git changes produced during command execution and verification are reset

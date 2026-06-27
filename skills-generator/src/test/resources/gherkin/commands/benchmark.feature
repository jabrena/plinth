Feature: Validate benchmark command

Background:
  Given the command prompt file ".cursor/commands/benchmark.md"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Coordinate a reproducible Java benchmark from a concrete target
  Given the user request is "/benchmark examples/frameworks/spring-boot"
  And the command prompt source ".cursor/commands/benchmark.md" is read before execution
  When the benchmark command is applied to the request
  Then the command identifies the benchmark target, workload, environment, and success thresholds before execution
  And the command selects an appropriate Java performance testing approach for the target
  And the command avoids claiming performance improvement without reproducible baseline and comparison evidence
  And the command reports commands to run, generated artifacts, measured results, risks, and follow-up validation
  And the folder "examples" has no git changes unless the user explicitly requested benchmark setup edits
  And any git changes produced during command execution and verification are reset

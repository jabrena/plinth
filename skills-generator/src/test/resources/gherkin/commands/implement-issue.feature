Feature: Validate implement-issue command with the God Analysis API OpenSpec example

Background:
  Given the command prompt file ".cursor/commands/implement-issue.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the OpenSpec change path "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And the implementation target directory "examples/openspec/god-analysis-api/demo"
  And the implementation target directory starts empty except for ".gitkeep"

@acceptance-test
Scenario: Implement God Analysis API from a validated OpenSpec change
  Remark: Acceptance execution must use the implement-issue command contract and must not implement outside the requested demo directory.
  Given the OpenSpec change "add-god-analysis-api" contains "proposal.md", "design.md", "tasks.md", and "specs/god-analysis-api/spec.md"
  And the OpenSpec change is validated with "openspec validate --all" from "examples/openspec/god-analysis-api"
  And the command prompt source ".cursor/commands/implement-issue.md" is read before execution
  When the user executes the prompt "/implement-issue examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api implement in examples/openspec/god-analysis-api/demo"
  Then the command loads the selected OpenSpec "tasks.md" as the execution contract
  And the command confirms the selected OpenSpec change is current, validated, and internally consistent
  And the command identifies the implementation as a Spring Boot MVC Java service from the OpenSpec design and technology constraints
  And the command routes implementation work through "@robot-tech-lead" and the appropriate Java Spring Boot implementation agent
  And the command reports using the current branch as the isolation strategy before implementation starts
  And all generated implementation files are created under "examples/openspec/god-analysis-api/demo"
  And the implementation provides "GET /api/v1/gods/stats/sum"
  And the implementation covers the documented happy path sum "78179288397447443426"
  And the implementation covers the documented partial timeout sum "78101109179220212216"
  And the implementation rejects missing, empty, multi-character, and invalid query parameters with HTTP 400
  And the implementation does not add WebFlux, WebClient, Rest Assured, Resilience4j Retry, Spring Retry, or custom retry loops for US-001
  And the command runs the focused Maven verification command from "examples/openspec/god-analysis-api/demo"
  And the command marks OpenSpec tasks complete only after their acceptance criteria and verification gates pass
  And the command reports changed files, validation evidence, updated OpenSpec task status, risks, and blockers
  And any git changes produced under "examples/openspec/god-analysis-api/demo" during command execution and verification are reset

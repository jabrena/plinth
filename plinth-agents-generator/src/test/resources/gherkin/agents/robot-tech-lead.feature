Feature: Validate robot-tech-lead agent

Background:
  Given the agent prompt file ".cursor/agents/robot-tech-lead.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the OpenSpec change path "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And the implementation target directory "examples/openspec/god-analysis-api/demo"
  And the folder "examples/openspec/god-analysis-api/openspec" has no git changes
  And the folder "examples/openspec/god-analysis-api/demo" has no git changes

@acceptance-test
Scenario: Coordinate God Analysis API implementation from a validated OpenSpec change
  Remark: Acceptance execution must coordinate delivery through coder delegation and must not implement outside the requested demo directory.
  Given the OpenSpec change "add-god-analysis-api" contains "proposal.md", "design.md", "tasks.md", and "specs/god-analysis-api/spec.md"
  And the OpenSpec change is validated with "openspec validate --all" from "examples/openspec/god-analysis-api"
  And the user request is "Coordinate implementation for examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api and implement in examples/openspec/god-analysis-api/demo"
  And the agent prompt source ".cursor/agents/robot-tech-lead.md" is read before execution
  And the implementation target directory starts empty except for ".gitkeep"
  When the agent "robot-tech-lead" is applied to the request
  Then the agent loads the selected OpenSpec "tasks.md" as the execution contract
  And the agent confirms the selected OpenSpec change is current, validated, and internally consistent
  And the agent identifies the implementation as a Spring Boot MVC Java service from the OpenSpec design and technology constraints
  And the agent reads the complete 042-planning-openspec SKILL.md and its required reference before delegating
  And the agent delegates every implementation, test, and verification step to "@robot-java-spring-boot-coder"
  And every implementation handoff requires the complete SKILL.md and every task-relevant referenced resource to be read before editing
  And the agent does not implement code, edit tests, or run the build as a substitute for developers
  And the agent does not create or refine OpenSpec proposal, design, or specification artifacts as a primary mission
  And all generated implementation files are created under "examples/openspec/god-analysis-api/demo"
  And the implementation provides "GET /api/v1/gods/stats/sum"
  And the implementation covers the documented happy path sum "78179288397447443426"
  And the implementation covers the documented partial timeout sum "78101109179220212216"
  And the implementation rejects missing, empty, multi-character, and invalid query parameters with HTTP 400
  And the implementation does not add WebFlux, WebClient, Rest Assured, Resilience4j Retry, Spring Retry, or custom retry loops for US-001
  And the agent runs the focused Maven verification command from "examples/openspec/god-analysis-api/demo"
  And the agent marks OpenSpec tasks complete only after their acceptance criteria and verification gates pass
  And the agent reports delegation groups, task status, skills applied, skills skipped, exact reference paths read, validation evidence, risks, blockers, and artifact authority
  And any git changes produced under "examples/openspec/god-analysis-api/demo" during agent execution and verification are reset
  And any git changes produced under "examples/openspec/god-analysis-api/openspec" during agent execution and verification are reset

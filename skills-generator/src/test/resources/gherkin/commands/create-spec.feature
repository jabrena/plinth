Feature: Validate create-spec command

Background:
  Given the command prompt file ".cursor/commands/create-spec.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And the folder "examples/openspec/god-analysis-api/openspec" has no git changes

@acceptance-test
Scenario: Create God Analysis API OpenSpec change from trusted functional requirements
  Remark: Acceptance execution has enough approved input to create the OpenSpec change without further clarification.
  Given the user request is "/create-spec examples/openspec/god-analysis-api/requirements/problem1"
  And the functional requirements folder "examples/openspec/god-analysis-api/requirements/problem1" contains ADRs, a user story, Gherkin acceptance criteria, and OpenAPI files
  And the command prompt source ".cursor/commands/create-spec.md" is read before execution
  And the requested OpenSpec change path is "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And any existing OpenSpec change at the requested change path is removed before execution
  And the maintainer has approved using the documented ADR technology decisions without further design exploration during acceptance execution
  And application code implementation is explicitly out of scope
  When the create-spec command is applied to the request
  Then the command identifies the source artifact, change name, affected specs, proposal, design needs, and task list
  And the command routes OpenSpec creation through "@robot-architect"
  And the command reads the trusted God Analysis API source artifacts required by the request
  And the command records source artifact paths, concern authority, and derivation direction in the OpenSpec proposal
  And the command creates the OpenSpec change "add-god-analysis-api" containing "proposal.md", "design.md", "tasks.md", and "specs/god-analysis-api/spec.md"
  And the OpenSpec proposal traces to "examples/openspec/god-analysis-api/requirements/problem1/US-001_God_Analysis_API.md"
  And the OpenSpec proposal traces to the functional, non-functional, and technology ADRs under "examples/openspec/god-analysis-api/requirements/problem1"
  And the OpenSpec specification includes the endpoint "GET /api/v1/gods/stats/sum"
  And the OpenSpec specification includes required query parameters "filter" and "sources"
  And the OpenSpec specification includes the documented happy path sum "78179288397447443426"
  And the OpenSpec specification includes the documented partial timeout sum "78101109179220212216"
  And the OpenSpec specification includes HTTP 400 validation for missing, empty, multi-character, and invalid query parameters
  And the OpenSpec design records Spring MVC, Spring RestClient, case-sensitive Unicode filtering, and partial aggregation on source timeout
  And the OpenSpec design records one outbound attempt per selected source with no automatic retries
  And the OpenSpec tasks include WireMock-backed tests and prohibition of WebFlux, WebClient, Rest Assured, Resilience4j Retry, Spring Retry, and custom retry loops for US-001
  And the command applies "042-planning-openspec" and "056-design-avoid-breaking-changes" when creating the OpenSpec change
  And the command creates or updates OpenSpec artifacts only under "examples/openspec/god-analysis-api/openspec" when edits are requested
  And the command does not create or modify ADRs, user stories, Gherkin files, or OpenAPI files under "examples/openspec/god-analysis-api/requirements/problem1"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And "openspec validate --all" is run from "examples/openspec/god-analysis-api" after approved artifact changes
  And the command validates OpenSpec structure before claiming the change is ready
  And the command reports changed files, validation evidence, source traceability, assumptions, and unresolved planning risks
  And any git changes produced under "examples/openspec/god-analysis-api/openspec" during command execution and verification are reset

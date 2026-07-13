Feature: Validate create-spec command

Background:
  Given the command prompt file ".cursor/commands/create-spec.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And the folder "examples/openspec/god-analysis-api/openspec" has no git changes

@acceptance-test
Scenario: Create OpenSpec change using composable planning workflow
  Remark: Acceptance execution verifies only behaviors defined in `042-planning-openspec`.
  Given the user request is "/create-spec examples/openspec/god-analysis-api/requirements/problem1"
  And the functional requirements folder "examples/openspec/god-analysis-api/requirements/problem1" contains ADRs, a user story, Gherkin acceptance criteria, and OpenAPI files
  And the command prompt source ".cursor/commands/create-spec.md" is read before execution
  And the requested OpenSpec change path is "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And any existing OpenSpec change at the requested change path is removed before execution
  And application code implementation is explicitly out of scope
  When the create-spec command is applied to the request
  Then the command reads trusted planning inputs and establishes source artifact authority
  And the command assesses whether the scope fits one reviewable OpenSpec change
  And the command records source artifact paths, concern authority, and derivation direction in the OpenSpec proposal
  And the command creates the OpenSpec change "add-god-analysis-api" containing "proposal.md", "design.md", "tasks.md", and capability specification deltas under "specs/"
  And the command creates or updates OpenSpec artifacts only under "examples/openspec/god-analysis-api/openspec" when edits are requested
  And the command does not modify ADRs, user stories, Gherkin files, or OpenAPI files under "examples/openspec/god-analysis-api/requirements/problem1"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And "openspec validate --all" is run from "examples/openspec/god-analysis-api" after approved artifact changes
  And the command validates OpenSpec structure before claiming the change is ready
  And the command reports source artifacts, authority, derivation direction, validation evidence, assumptions, and unresolved planning risks
  And any git changes produced under "examples/openspec/god-analysis-api/openspec" during command execution and verification are reset

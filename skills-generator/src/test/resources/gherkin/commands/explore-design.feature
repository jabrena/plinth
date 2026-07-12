Feature: Validate explore-design command

Background:
  Given the command prompt file ".cursor/commands/explore-design.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And the folder "examples/openspec/god-analysis-api/openspec" has no git changes

@acceptance-test
Scenario: Refine a newly created OpenSpec change by calling design skills during elaboration
  Remark: Acceptance execution first creates the initial OpenSpec change through `/create-spec` with `042-planning-openspec` only, then refines it through `/explore-design`.
  Given the functional requirements folder "examples/openspec/god-analysis-api/requirements/problem1" contains ADRs, a user story, Gherkin acceptance criteria, and OpenAPI files
  And the command prompt source ".cursor/commands/create-spec.md" is read before execution
  And the command prompt source ".cursor/commands/explore-design.md" is read before execution
  And the requested OpenSpec change path is "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And any existing OpenSpec change at the requested change path is removed before execution
  And application code implementation is explicitly out of scope
  When the create-spec command is applied to the request "/create-spec examples/openspec/god-analysis-api/requirements/problem1"
  Then the command creates the OpenSpec change "add-god-analysis-api" containing "proposal.md", "design.md", "tasks.md", and capability specification deltas under "specs/"
  And the command applies only "042-planning-openspec" when creating the initial OpenSpec change
  And "openspec validate --all" is run from "examples/openspec/god-analysis-api" after the initial change is created
  When the explore-design command is applied to the request "/explore-design examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  Then the command routes design refinement through "@robot-architect"
  And the command identifies the design source, problem, constraints, stakeholders, options, trade-offs, and recommendation criteria
  And the command calls "051-design-two-steps-methods" to separate behavior-preserving preparation from behavior-changing refinement in the improved approach
  And the command calls "052-design-hamburger-method" when scope needs smallest-useful vertical slices during elaboration
  And the command calls "053-design-simple-rules" to evaluate design alternatives with ordered tradeoff rules before finalizing the direction
  And the command calls "054-design-tdd" to shape test-first sequencing and verification boundaries for the refined approach
  And the command calls "055-design-parallel-change" when database migration requirements need expand-migrate-contract sequencing
  And the command calls "056-design-avoid-breaking-changes" to review compatibility surfaces affected by the refined approach
  And the command calls "057-design-feature-toggles" when rollout, rollback, or temporary behavior controls affect the design direction
  And the command calls "121-java-object-oriented-design" before "122-java-type-design" and "123-java-design-patterns" when Java responsibilities or collaboration affect the refined approach
  And the command calls "122-java-type-design" when domain types, invariants, or invalid-state modeling affect the refined approach
  And the command calls "123-java-design-patterns" only when a demonstrated collaboration or integration problem requires pattern selection
  And the command calls "130-java-testing-strategies" when testing strategy, boundary coverage, or verification quality affect the refined approach
  And the command does not call "042-planning-openspec" or "034-architecture-design-exploration" during design refinement
  And the command keeps analysis separate from implementation unless the user explicitly requests edits
  And the command records assumptions, rejected options, risks, ADR candidates, and validation needed before implementation
  And the command reports the recommended design direction and any generated analysis artifact
  And the command refines the existing OpenSpec change without replacing initial OpenSpec authoring owned by "/create-spec"
  And the command does not modify ADRs, user stories, Gherkin files, or OpenAPI files under "examples/openspec/god-analysis-api/requirements/problem1"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And "openspec validate --all" is run from "examples/openspec/god-analysis-api" after approved refinement changes
  And any git changes produced under "examples/openspec/god-analysis-api/openspec" during command execution and verification are reset

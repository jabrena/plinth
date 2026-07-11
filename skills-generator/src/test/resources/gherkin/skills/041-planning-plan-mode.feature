Feature: Validate changes from usage of planning plan mode skill

Background:
  Given the skill "041-planning-plan-mode"
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And the folder "examples/skills/analysis" has no git changes

@acceptance-test
Scenario: Create an implementation plan from trusted God Analysis API requirements
  Remark: Acceptance execution has enough approved input to generate the plan without clarification.
  Given the user request is "Create an implementation plan for the God Analysis API from the trusted requirements in examples/openspec/god-analysis-api/requirements/problem1"
  And the trusted requirements folder "examples/openspec/god-analysis-api/requirements/problem1" contains ADRs, a user story, Gherkin acceptance criteria, and OpenAPI files
  And the local generated skill path ".agents/skills/041-planning-plan-mode"
  And the requested implementation plan output path is "examples/skills/analysis/GOD-ANALYSIS-API-IMPLEMENTATION-PLAN.md"
  And any existing plan at the requested output path must be overwritten
  And the maintainer has approved creating the new plan from those artifacts without further confirmation during acceptance execution
  And the plan is the selected execution-tracking artifact
  And OpenSpec artifact creation is explicitly out of scope
  When the skill ".agents/skills/041-planning-plan-mode" is applied to create the implementation plan
  Then the skill runs "date" before drafting the plan
  And the skill reads "references/041-planning-plan-mode.md"
  And the skill reads only the trusted God Analysis API source artifacts required by the request
  And the skill records source artifact paths, concern authority, derivation direction, and selected execution authority
  And the skill derives the plan from the user story, ADRs, Gherkin acceptance criteria, and OpenAPI files without inventing requirements
  And the skill identifies conflicts, assumptions, unresolved questions, dependencies, risks, and explicit user decisions
  And the generated plan includes Requirements Summary, Technical Approach, Task List, milestones or parallel groups when warranted, Verification, Execution Instructions, File Checklist, and Notes
  And the skill does not create or modify OpenSpec artifacts, ADRs, user stories, Gherkin files, OpenAPI files, or generated skills
  And the generated plan reports the validation or review steps needed before promoting implementation changes
  And the folder "examples/openspec/god-analysis-api/requirements/problem1" has no git changes
  And any git changes produced under "examples/skills/analysis" during skill execution and verification are reset

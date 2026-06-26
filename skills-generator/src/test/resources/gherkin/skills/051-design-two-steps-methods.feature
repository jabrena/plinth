Feature: Validate changes from usage of two-step change method skill

Background:
  Given the skill "051-design-two-steps-methods"
  And the OpenSpec example project "examples/openspec/god-analysis-api"
  And the OpenSpec sandbox folder "examples/openspec/god-analysis-api/openspec" has no git changes

@acceptance-test
Scenario: Create an OpenSpec change that plans a risky Java change with the two-step method
  Given the user request is "Create an OpenSpec change for a risky Java refactoring using the two-step change method"
  And the local generated skill path ".agents/skills/051-design-two-steps-methods"
  And the requested OpenSpec change id is "acceptance-two-step-change-method"
  And the requested OpenSpec change path is "examples/openspec/god-analysis-api/openspec/changes/acceptance-two-step-change-method"
  And any existing OpenSpec change at the requested change path must be overwritten
  When the skill ".agents/skills/051-design-two-steps-methods" is applied to create the requested OpenSpec change
  Then the skill reads "references/051-design-two-steps-methods.md"
  And the skill creates or updates "proposal.md", "design.md", and "tasks.md" under the requested OpenSpec change path
  And the OpenSpec design analysis states the intended behavior change before proposing refactoring
  And the OpenSpec design analysis identifies why the current design makes the change complex or risky
  And the OpenSpec design analysis separates Step 1 behavior-preserving preparation from Step 2 behavior change
  And the OpenSpec tasks require validation that Step 1 preserves existing behavior before proceeding
  And the OpenSpec tasks schedule Step 2 as the intended behavior change once the design supports it
  And the OpenSpec tasks include focused verification for the intended behavior change after Step 2
  And the skill keeps Java, framework, persistence, messaging, API, or testing skill handoffs inside the two-step sequence when those details are needed
  And the generated OpenSpec artifacts reflect the whole guidance including the intended behavior change, design obstacle, Step 1 preparation, Step 1 behavior-preservation validation, Step 2 behavior change, Step 2 verification, assumptions, and risks
  And "openspec validate --all" is run from "examples/openspec/god-analysis-api" after the OpenSpec change is created
  And any git changes produced under "examples/openspec/god-analysis-api/openspec" during skill execution and verification are reset

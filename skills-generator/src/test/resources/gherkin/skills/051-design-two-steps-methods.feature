Feature: Validate changes from usage of two-step change method skill

Background:
  Given the skill "051-design-two-steps-methods"
  And the analysis sandbox folder "examples/skills/analysis" has no git changes

@acceptance-test
Scenario: Guide a risky code change through preparatory refactoring before behavior change
  Given the user request is "Use the two-step change method to make a complex or risky Java code change safer"
  And the local generated skill path ".agents/skills/051-design-two-steps-methods"
  And the requested analysis output path is "examples/skills/analysis/TWO-STEP-CHANGE-METHOD-PLAN.md"
  And any existing report at the requested output path must be overwritten
  When the skill ".agents/skills/051-design-two-steps-methods" is applied to the requested risky change
  Then the skill reads "references/051-design-two-steps-methods.md"
  And the skill states the intended behavior change before proposing refactoring
  And the skill identifies why the current design makes the change complex or risky
  And the skill guides Step 1 as behavior-preserving preparatory refactoring that makes the change easy
  And the skill requires validation that Step 1 preserves existing behavior before proceeding
  And the skill guides Step 2 as the intended behavior change once the design supports it
  And the skill recommends focused verification for the intended behavior change after Step 2
  And the skill keeps Java, framework, persistence, messaging, API, or testing skill handoffs inside the two-step sequence when those details are needed
  And the skill writes the two-step change method outcome to "examples/skills/analysis/TWO-STEP-CHANGE-METHOD-PLAN.md"
  And the generated analysis file reflects the whole guidance including the intended behavior change, design obstacle, Step 1 preparation, Step 1 behavior-preservation validation, Step 2 behavior change, Step 2 verification, assumptions, and risks
  And any git changes produced under "examples/skills/analysis" during skill execution and verification are reset

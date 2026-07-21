Feature: Validate changes from usage of Acceptance Test-Driven Development alignment skill

Background:
  Given the skill "059-design-atdd"
  And the local generated skill path ".agents/skills/059-design-atdd"

@acceptance-test
Scenario: Review an aligned calculator OpenSpec change
  Given the user request is "Review the calculator OpenSpec change with ATDD"
  And the execution goal is in "examples/openspec/calculator/openspec/changes/add-calculator/proposal.md"
  And the acceptance criteria are in "examples/openspec/calculator/openspec/changes/add-calculator/specs/calculator/spec.md"
  And the associated task checklist is "examples/openspec/calculator/openspec/changes/add-calculator/tasks.md"
  When the skill ".agents/skills/059-design-atdd" reviews the calculator change
  Then the skill reads "references/059-design-atdd.md"
  And the skill reads the OpenSpec proposal, capability specification scenarios, and single associated task checklist
  And the review traces every "FR-CALC" requirement to its matching "AC-CALC" scenario and implementation and verification tasks
  And "AC-CALC-ADD" is classified as "complete" through tasks "1.1" and "1.2"
  And "AC-CALC-SUBTRACT" is classified as "complete" through tasks "1.1" and "1.2"
  And "AC-CALC-MULTIPLY" is classified as "complete" through tasks "2.1" and "2.2"
  And "AC-CALC-DIVIDE" is classified as "complete" through tasks "2.1" and "2.2"
  And "AC-CALC-DIVIDE-ZERO" is classified as "complete" through tasks "3.1" and "3.2"
  And the review preserves the many-to-many mappings for the shared addition, subtraction, multiplication, and division tasks
  And every finding cites the calculator fixture paths and stable requirement, scenario, and task identifiers
  And the review reports no missing, partial, ambiguous, absent, or divergent alignment finding for the calculator fixture
  And the overall alignment outcome is "ready"
  And the review does not silently add, remove, edit, or rewrite the calculator acceptance criteria or tasks
  And the generated reference provides the complete status definitions and evidence-backed traceability examples

@acceptance-test
Scenario: Request changes for an OpenSpec change with ATDD alignment gaps
  Given the user request is "Review the calculator OpenSpec change with ATDD"
  And the execution goal is in "examples/openspec/calculator/openspec/changes/add-calculator-with-atdd-gaps/proposal.md"
  And the acceptance criteria are in "examples/openspec/calculator/openspec/changes/add-calculator-with-atdd-gaps/specs/calculator-with-atdd-gaps/spec.md"
  And the associated task checklist is "examples/openspec/calculator/openspec/changes/add-calculator-with-atdd-gaps/tasks.md"
  And the calculator change passes OpenSpec structural validation
  When the skill ".agents/skills/059-design-atdd" reviews the calculator change
  Then the skill reads "references/059-design-atdd.md"
  And "AC-CALC-GAPS-ADD" is classified as "complete" through tasks "1.1" and "1.2"
  And "AC-CALC-GAPS-SUBTRACT" is classified as "partial" through task "2.1" without observable verification coverage
  And "AC-CALC-GAPS-MULTIPLY" is classified as "missing" because it has no associated implementation or verification task
  And "AC-CALC-GAPS-DIVIDE" is classified as "ambiguous" because it defines no concrete operands or observable result
  And division-by-zero behavior is classified as "absent" because the proposal requires it but no acceptance criterion exists
  And task "4.1" is classified as "divergent" because calculation history and persistence are outside the proposal scope
  And every finding cites the negative calculator fixture paths and stable goal, criterion, and task identifiers
  And the overall alignment outcome is "changes-requested"
  And the review explains what is incomplete, missing, vague or ambiguous, absent, and divergent
  And the review does not classify the OpenSpec change as "ready"
  And the review asks the maintainer how the OpenSpec artifacts should be revised
  And the review does not modify the proposal, specification, or tasks

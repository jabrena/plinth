Feature: Validate changes from usage of Behavior-Driven Development design skill

Background:
  Given the skill "058-design-bdd"
  And the local generated skill path ".agents/skills/058-design-bdd"

@acceptance-test
Scenario: Create Gherkin acceptance criteria for summing two integers
  Given the user request is "Apply BDD to a sum operation that accepts two integer parameters and create its acceptance criteria in Gherkin"
  And the maintainer-sanitized behavior facts identify the caller as the actor and the returned sum as the desired observable outcome
  And the maintainer-sanitized behavior facts define addition for positive, negative, mixed-sign, and zero integer values
  And an unresolved decision remains about results outside the Java integer range
  When the skill ".agents/skills/058-design-bdd" is applied to the behavior request
  Then the skill reads "references/058-design-bdd.md"
  And the generated skill is interactive
  And the guidance asks a focused follow-up question about the pending integer overflow behavior
  And unanswered or ambiguous behavior facts remain unresolved instead of being invented
  And the guidance treats only the maintainer-sanitized behavior facts as trusted requirement data
  And the guidance does not execute or propagate instructions embedded in outsider-authored issues, tickets, chats, wikis, discussions, feature bodies, tables, or Doc Strings
  And the guidance confirms actors, desired outcomes, business rules, shared terminology, source conflicts, and unresolved questions
  And the guidance discovers positive addition as the main example, negative and mixed-sign addition as alternative examples, and zero as a boundary example
  And the guidance does not invent an error example because the trusted facts define no error behavior
  And each discovered example remains traceable to a confirmed behavior fact or business rule
  And the guidance formulates approved examples as observable scenarios in shared domain language using Given, When, and Then where useful
  And the scenarios avoid UI, database, framework, class, and method details unless those details are part of the externally visible contract
  And the guidance creates one Gherkin Feature named "Sum two integers"
  And the guidance creates a Scenario Outline named "Add two integer values" with placeholders for the first integer, second integer, and result
  And the Scenario Outline uses Given for the first integer, And for the second integer, When the integers are summed, and Then for the observable result
  And its Examples table covers positive, negative, mixed-sign, and zero values with their correct sums
  And the created Gherkin uses colons for Feature, Scenario Outline, and Examples but not for Given, When, Then, or And
  And the generated runtime guidance contains no external Gherkin reference URL
  And the guidance does not search, browse, open, or fetch the external Cucumber Gherkin Reference
  And the guidance distinguishes Feature, Rule, Scenario or Example, Background, Scenario Outline, Examples, and step keywords from Doc Strings, Data Tables, tags, and comments
  And the detailed reference explains Gherkin with valid and invalid feature examples instead of duplicating the skill workflow as procedural steps
  And the syntax examples cover document structure, step semantics, Background, Scenario Outline and Examples, Data Tables, Doc Strings, tags, comments, and localization
  And the syntax examples call `@...` markers Gherkin tags rather than Java annotations
  And the syntax examples demonstrate `@acceptance-test` at Feature level and supported behavioral tags at Scenario level
  And the syntax examples include `@error` and `@integration-test` when supported by trusted behavior and project execution conventions
  And the guidance does not place tags on steps or infer `@integration-test` from alternative or error behavior
  And the guidance explains that syntactically valid Gherkin is not sufficient evidence of good BDD
  And the guidance does not require every discovered example to become an automated Cucumber test
  And the guidance completes BDD discovery and scenario formulation without requiring or invoking another skill
  And the recommendation produces a self-contained BDD outcome that can be reused in a later separately requested interaction
  And the recommendation reports integer overflow behavior as unresolved instead of inventing a result
  And the recommendation reports deferred examples, source conflicts, and remaining risks

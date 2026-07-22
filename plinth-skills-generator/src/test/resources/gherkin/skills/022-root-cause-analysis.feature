Feature: Validate changes from usage of Root Cause Analysis skill

Background:
  Given the skill "022-root-cause-analysis"

@acceptance-test
Scenario: Apply Five Whys and stop the chain at the evidence boundary
  Given the user request is "Find the root cause of slow order-status resolution using @022-root-cause-analysis"
  And the local generated skill path ".agents/skills/022-root-cause-analysis"
  When the skill ".agents/skills/022-root-cause-analysis" is applied to the root-cause request
  Then the skill reads "references/022-root-cause-analysis.md"
  And the analysis distinguishes the observed symptom from an assumed root cause
  And the recommendation applies Five Whys grounded in available evidence
  And the recommendation stops the Five Whys chain at the evidence boundary rather than guessing the next step

@acceptance-test
Scenario: Apply Fishbone and Current Reality Tree when multiple causal dimensions are plausible
  Given the user request is "Review root causes for repeated support-ticket escalations across people, process, and technology using @022-root-cause-analysis"
  And the local generated skill path ".agents/skills/022-root-cause-analysis"
  When the skill ".agents/skills/022-root-cause-analysis" reviews the multi-dimensional root-cause request
  Then the skill reads "references/022-root-cause-analysis.md"
  And the recommendation organizes candidate causes into Fishbone categories such as people, process, technology, and environment
  And the recommendation applies Current Reality Tree to connect related symptoms to a small number of shared core problems
  And the recommendation identifies the constraint limiting the current state, not only the most visible cause

@acceptance-test
Scenario: Flag an unclear symptom for a clarifying question instead of inventing a root cause
  Given the user request is "Find the root cause of this issue: agents are slow, using @022-root-cause-analysis"
  And the local generated skill path ".agents/skills/022-root-cause-analysis"
  When the skill ".agents/skills/022-root-cause-analysis" evaluates the vague symptom description
  Then the skill reads "references/022-root-cause-analysis.md"
  And the recommendation does not invent a root cause from the vague symptom description
  And the recommendation flags the gap for a clarifying question

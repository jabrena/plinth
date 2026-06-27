Feature: Validate changes from usage of Avoid Breaking Changes design skill

Background:
  Given the skill "056-design-avoid-breaking-changes"

@acceptance-test
Scenario: Review an OpenSpec change for breaking-change risk
  Given the user request is "Review documentation/openspec/changes/convert-breaking-change-review-to-skill for breaking-change risks before implementation"
  And the local generated skill path ".agents/skills/056-design-avoid-breaking-changes"
  When the skill ".agents/skills/056-design-avoid-breaking-changes" reviews the OpenSpec change
  Then the skill reads "references/056-design-avoid-breaking-changes.md"
  And the report identifies the source artifacts reviewed
  And the report checks command contracts, skill routing, generated outputs, XML sources, README documentation, tests, CI validation, external contracts, runtime behavior, data, configuration, release notes, and migration guidance when relevant
  And the report classifies findings as "BREAKING", "POTENTIALLY BREAKING", "NON-BREAKING", or "UNKNOWN"
  And the report highlights affected contracts, generated outputs, commands, skills, documentation, tests, or release guidance where applicable
  And the report provides actionable validation guidance for follow-up work

@acceptance-test
Scenario: Report a no-risk compatibility review
  Given the user request is "Check compatibility risk for a docs-only typo fix in a README paragraph"
  And the local generated skill path ".agents/skills/056-design-avoid-breaking-changes"
  When the skill ".agents/skills/056-design-avoid-breaking-changes" reviews the docs-only change
  Then the skill reads "references/056-design-avoid-breaking-changes.md"
  And the report explicitly states when no breaking-change risks are found
  And the report lists the compatibility surfaces reviewed
  And the report still recommends validation appropriate to the touched artifact type

Feature: Validate changes from usage of hamburger method design skill

Background:
  Given the skill "052-design-hamburger-method"

@acceptance-test
Scenario: Split a large feature into a smallest useful vertical slice
  Given the user request is "Apply the Hamburger Method to split a large customer health reporting feature into tracked vertical slices"
  And the local generated skill path ".agents/skills/052-design-hamburger-method"
  When the skill ".agents/skills/052-design-hamburger-method" is applied to the large feature request
  Then the skill reads "references/052-design-hamburger-method.md"
  And the analysis recognizes the request as oversized feature, story, plan, or spec work
  And the analysis identifies 3-6 functional or workflow layers that participate in delivering value
  And each layer includes 4-5 implementation or quality options ordered from simplest acceptable option to richer options
  And the analysis asks or answers "If this had to ship tomorrow, what would be the smallest useful version?"
  And the analysis filters options that are too costly, redundant, irreversible, or unnecessary for early learning
  And the recommendation composes one smallest useful end-to-end vertical slice by selecting one option from each relevant layer
  And the recommendation does not present isolated technical layers as independently shippable stories
  And the recommendation proposes follow-up vertical slices that incrementally improve quality, automation, robustness, reach, or observability
  And each proposed slice is checked for value, size, testability, deliverability, and issue-tracking suitability
  And the skill asks whether actionable split candidates should become tracked work or update existing tracked work
  And the skill routes implementation planning through "041-planning-plan-mode"
  And the skill routes separate spec-level product, workflow, or architectural decisions through "042-planning-openspec"
  And the skill routes GitHub-backed issue tracking through "043-planning-github-issues"
  And the skill routes Jira-backed issue tracking through "044-planning-jira"

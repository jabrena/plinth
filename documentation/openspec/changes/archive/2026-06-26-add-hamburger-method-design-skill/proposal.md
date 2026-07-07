## Why

GitHub issue [#874](https://github.com/jabrena/plinth/issues/874) requests a new `052-design-hamburger-method` skill for applying the Hamburger Method to large features, stories, plans, and OpenSpec changes.

Planning work can become too large when teams split by technical layer rather than by user-visible value. The Hamburger Method gives agents a structured way to identify workflow layers, compare quality options per layer, trim overbuilt choices, and compose small vertical slices that still deliver observable value.

## What Changes

- Add `052-design-hamburger-method` as a generated design skill for vertical story slicing.
- Guide agents to identify 3-6 functional or workflow layers and 4-5 implementation or quality options per layer.
- Guide agents to compose a smallest useful end-to-end slice, then suggest follow-up slices that improve quality, automation, robustness, reach, or observability.
- Add tracker-routing guidance so actionable split candidates can become GitHub or Jira issues through the appropriate planning skills.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/052-design-hamburger-method`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `hamburger-method-design-skill-reference`: Adds Hamburger Method design skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#874](https://github.com/jabrena/plinth/issues/874).
- External source: [Gojko Adzic, "Splitting user stories -- the hamburger method"](https://gojko.net/2012/01/23/splitting-user-stories-the-hamburger-method/).
- External reference implementation: [skill-factory hamburger-method skill](https://github.com/eferro/skill-factory/tree/main/output_skills/practices/hamburger-method).
- Existing implementation model: `051-design-two-steps-methods`.
- Related planning skills: `041-planning-plan-mode`, `042-planning-openspec`, `043-planning-github-issues`, and `044-planning-jira`.
- Derivation direction: issue #874 plus external Hamburger Method references plus existing design skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is one independently reviewable generated skill. The change may touch several generator files, tests, and prompt inventory entries, but those edits all support the same skill capability.

Additional GitHub or Jira issues may be created later for clear execution slices discovered during planning. Additional OpenSpec changes are only needed if a follow-up slice introduces a separate product, workflow, or architectural decision.

## Impact

XML skill indexes, XML skill references, `skills.xml`, Gherkin skill acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

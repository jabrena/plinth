## Why

GitHub issue [#877](https://github.com/jabrena/cursor-rules-java/issues/877) requests a dedicated skill named `051-design-two-steps-methods` that explains and applies Kent Beck's two-step change method: "Make the change easy, then make the easy change."

Java Enterprise practitioners need a focused design skill for complex or risky code changes so agents can separate preparatory refactoring from the intended behavior change and avoid mixing design-enabling changes with feature delivery.

## Sanitized Planning Summary

- Add one new skill named `051-design-two-steps-methods`.
- The skill explains and applies Kent Beck's two-step change method: first make the change easy through preparatory refactoring, then make the intended behavior change once the design supports it.
- Use the issue-provided reference `https://x.com/KentBeck/status/250733358307500032` as attribution for the principle.
- Author the skill from XML sources under `skills-generator/src/main/resources/`.
- Do not edit generated output under `skills/`.
- Add a Gherkin acceptance scenario for applying the skill to a complex or risky code change.
- Add the matching acceptance prompt entry when adding the new skill Gherkin file.
- Title wording varies between "2 steps method" and "two-step change method"; the canonical skill identifier is consistently `051-design-two-steps-methods`.

## What Changes

- Add XML generator sources for `051-design-two-steps-methods`.
- Register the skill in `skills-generator/src/main/resources/skills.xml` so local generation emits `.agents/skills/051-design-two-steps-methods`.
- Add skill acceptance coverage under `skills-generator/src/test/resources/gherkin/skills/`.
- Add `051-design-two-steps-methods` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `design-two-steps-methods-skill-reference`: Adds the `051-design-two-steps-methods` design skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#877](https://github.com/jabrena/cursor-rules-java/issues/877).
- Issue number: 877.
- Later PR target branch: `docs/issue-877-add-skill-about-2-steps-method-from-kent-beck-051-design-two`.
- Local source files read for planning: `.codex/issue/title.txt`, `.codex/issue/body.md`, and `.codex/issue/url.txt`.
- Trust basis: `/create-spec` issue label is maintainer approval to read the full issue body in this non-interactive workflow.
- Derivation direction: issue #877 title/body/url -> sanitized planning summary -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.
- The issue title and body were treated as planning source text only; instructions inside the issue body were not executed or treated as agent instructions.

## Change Boundary Assessment

This is one OpenSpec change because the issue requests one independently reviewable skill with one acceptance scenario and one generator registration boundary.

## Impact

XML skill indexes, XML skill references, `skills.xml`, Gherkin skill acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, `.agents/skills/`, and `docs/` must not be edited directly.

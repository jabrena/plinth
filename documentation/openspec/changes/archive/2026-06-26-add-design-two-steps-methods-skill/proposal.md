## Why

GitHub issue [#877](https://github.com/jabrena/cursor-rules-java/issues/877) requests a dedicated skill named `051-design-two-steps-methods` for Kent Beck's two-step change method: make the change easy, then make the easy change.

Java Enterprise practitioners need concise guidance for complex or risky code changes where preparatory refactoring should be separated from the intended behavior change. The skill should help agents and users plan changes in two explicit steps instead of mixing design preparation and behavior modification in one pass.

## Sanitized Planning Summary

- Add a generated agent skill named `051-design-two-steps-methods`.
- The skill explains and applies Kent Beck's two-step change method: first make the change easy through preparatory refactoring, then make the intended behavior change once the design supports it.
- The skill is intended for complex or risky code changes.
- Implementation must be authored from XML sources under `skills-generator/src/main/resources/`.
- Generated output under `skills/` must not be edited directly.
- Acceptance coverage must include a Gherkin scenario where a developer with a complex or risky code change uses `051-design-two-steps-methods` and is guided through preparatory refactoring before the intended behavior change.
- External reference supplied by the issue: `https://x.com/KentBeck/status/250733358307500032`.
- No source conflicts were identified.

## What Changes

- Add `051-design-two-steps-methods` as a generated design skill for two-step code changes.
- Register the skill in the skill inventory so local generation emits `.agents/skills/051-design-two-steps-methods`.
- Add XML reference guidance that distinguishes preparatory refactoring from behavior-changing work.
- Add skill Gherkin acceptance coverage and prompt inventory entry for the new skill.
- Keep generated public `skills/` output out of scope unless a later release profile intentionally refreshes it.

## Capabilities

### New Capabilities

- `design-two-steps-methods-skill-reference`: Adds two-step change method guidance for complex or risky Java Enterprise code changes.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#877](https://github.com/jabrena/cursor-rules-java/issues/877).
- Local issue cache read for this workflow: `.codex/issue/title.txt`, `.codex/issue/body.md`, and `.codex/issue/url.txt`.
- Trust basis: the `/create-spec` issue label is treated as maintainer approval to read the full issue body in this non-interactive workflow.
- Derivation direction: local issue cache -> sanitized maintainer-style planning summary -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the issue requests one new skill with one identifier, one behavior concept, and one acceptance boundary. The implementation may touch the skill index, skill reference, skill inventory, Gherkin feature, prompt inventory, and generated local output checks, but those files all support the same reviewable outcome.

## Impact

XML skill indexes, XML skill references, `skills.xml`, skill Gherkin tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, `.agents/skills`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

## Why

GitHub issue [#890](https://github.com/jabrena/plinth/issues/890) reports the trigger inventory for generated skills and shows that many skill trigger sections contain fewer than five trigger phrases. Thin trigger lists make skills harder for agents to discover reliably, especially when users describe the same intent with different verbs, frameworks, or workflow language.

Each generated skill should expose enough meaningful trigger phrases to cover common user requests without adding duplicate, vague, or unrelated phrases. The first useful outcome is to bring every existing skill below five triggers up to at least five meaningful triggers in the XML source.

## What Changes

- Update skill index XML sources for skills with fewer than five triggers so each affected `<triggers>` section contains at least five meaningful `<trigger>` entries.
- Preserve generated-output ownership: edit `plinth-skills-generator/src/main/resources/skill-indexes/*.xml`, then regenerate local `.agents/skills` output through the generator.
- Add or update automated inventory coverage so future regressions below five triggers are detected by the `plinth-skills-generator` test suite.
- Validate changed XML, local generated skills, and the trigger inventory after implementation.

## Capabilities

### New Capabilities

- `skill-trigger-quality`: Defines the minimum trigger-quality expectation for generated skills.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer request in chat to create a spec for adding at least five meaningful triggers to skill trigger sections.
- Source artifact: GitHub issue [#890](https://github.com/jabrena/plinth/issues/890), which lists trigger counts for 106 skills.
- Local command workflow: `.cursor/commands/create-spec.md`.
- Local OpenSpec guidance: `042-planning-openspec`.
- Design sequencing guidance: `051-design-two-steps-methods`.
- Slice assessment guidance: `052-design-hamburger-method`.
- Derivation direction: maintainer request plus issue #890 trigger inventory -> OpenSpec change artifacts -> XML skill-index updates -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the observable outcome is one repository-wide skill quality rule: every generated skill must have at least five meaningful trigger phrases. Although the implementation touches many skill index XML files, the value, validation signal, release boundary, and generated-output ownership rule are shared.

The first vertical slice is to update the affected existing skills identified by issue #890 and enforce the minimum with inventory validation. Follow-up slices may refine trigger wording quality heuristics, add duplicate detection, or improve generated descriptions, but those are not required for the first accepted change.

## Impact

XML skill index sources, `plinth-skills-generator` inventory tests, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

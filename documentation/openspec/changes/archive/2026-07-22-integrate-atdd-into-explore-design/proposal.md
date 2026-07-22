## Why

GitHub issue [#1066](https://github.com/jabrena/plinth/issues/1066) identifies a gap in the design-refinement workflow: `/explore-design` does not explicitly use the existing `059-design-atdd` skill to verify alignment between an OpenSpec change's execution goal, acceptance criteria, and implementation and verification tasks. Adding that checkpoint prevents unresolved missing, partial, ambiguous, absent, or divergent coverage from being treated as an approved design direction.

## What Changes

- Add `059-design-atdd` to the `/explore-design` command's associated-skill contract for OpenSpec change inputs.
- Require the command to expose the skill's `ready` or `changes-requested` result before design approval.
- Preserve maintainer control: unresolved alignment findings do not silently rewrite OpenSpec artifacts and cannot be reported as an approved design direction.
- Extend command-focused unit and Gherkin acceptance coverage for the ATDD routing and both alignment outcomes.
- Preserve the existing workflow boundaries: `/create-spec` remains the initial OpenSpec authoring step, `/explore-design` remains the subsequent refinement step, and user-story creation remains separate from Gherkin creation.

This is a non-breaking extension of the existing `/explore-design` contract. Existing issue, user-story, and OpenSpec inputs remain supported, and no application runtime behavior changes.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-commands`: Extends `/explore-design` so OpenSpec refinement invokes `059-design-atdd`, reports its alignment outcome, and prevents unresolved findings from being presented as approved.

## Source and Derivation

- Authoritative source: GitHub issue [#1066](https://github.com/jabrena/plinth/issues/1066), explicitly authorized by the maintainer as a valid source for this `/create-spec` invocation.
- Supporting source: approved Functional Specification comment [#issuecomment-5050433724](https://github.com/jabrena/plinth/issues/1066#issuecomment-5050433724).
- Coverage: the issue description and complete comment thread were retrieved; the thread contained one maintainer-authored Functional Specification comment at planning time.
- Repository evidence:
  - `plinth-commands-generator/src/main/resources/commands/explore-design.xml`
  - `plinth-commands-generator/src/test/resources/gherkin/commands/explore-design.feature`
  - `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java`
  - `plinth-skills-generator/src/main/resources/skill-indexes/059-skill.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/059-design-atdd.xml`
- Derivation direction: GitHub issue and approved Functional Specification → this OpenSpec change → command generator sources and command-focused verification. No reverse synchronization to GitHub is implied.

## Impact

- Canonical command XML under `plinth-commands-generator` changes; generated or installed command Markdown must not be edited directly.
- Command unit and Gherkin acceptance tests change to verify ATDD routing and outcome handling.
- The existing generated `059-design-atdd` skill remains unchanged and is consumed by the command.
- Local command-generation verification is required; public release outputs are out of scope unless a separate release action is explicitly requested.
- No API, data, deployment, or application-code migration is required.

## Compatibility and Migration

- Existing `/explore-design` inputs and design skills remain supported.
- `/create-spec` remains the first OpenSpec step and does not acquire design skills.
- Users gain an additional alignment checkpoint for OpenSpec refinement; no invocation syntax changes.
- Existing users do not need to migrate stored data or configuration.

## Unresolved Questions

- The exact ordering of `059-design-atdd` relative to the existing design skills should be refined through `/explore-design`.
- The final presentation of `ready` and `changes-requested` within the command's report should be refined while preserving the explicit approval gate.

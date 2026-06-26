## Why

GitHub issue [#931](https://github.com/jabrena/cursor-rules-java/issues/931) requests a new `054-design-tdd` skill for Test-Driven Development.

Java enterprise teams and agents need focused TDD guidance that starts from the next behavior, writes a failing test first, implements only enough production code to pass, and then refactors new and existing code while preserving the verification signal. A dedicated skill gives agents a consistent way to apply TDD without folding it into generic testing or design guidance.

## What Changes

- Add `054-design-tdd` as a generated design skill for applying Test-Driven Development to Java implementation work.
- Guide agents through the TDD cycle:
  1. Maintain or refine a list of test cases.
  2. Select the next useful behavior or test case.
  3. Write a failing test first.
  4. Write the smallest functional code needed to pass.
  5. Refactor the new and existing code while keeping tests green.
- Explain that writing the test first clarifies the public interface or usage of the code.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/054-design-tdd`.
- Add focused Gherkin acceptance coverage and prompt inventory coverage when the skill is implemented.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `tdd-design-skill-reference`: Adds Test-Driven Development design skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#931](https://github.com/jabrena/cursor-rules-java/issues/931).
- Issue source status: maintainer-authored user story and Gherkin acceptance criteria in the issue body.
- External reference:
  - [Martin Fowler, "Test Driven Development"](https://www.martinfowler.com/bliki/TestDrivenDevelopment.html)
- Existing implementation model: `053-design-simple-rules` design skill and its archived OpenSpec change.
- Derivation direction: issue #931 plus the external TDD reference plus existing design skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is one independently reviewable generated design skill. The change may touch XML skill sources, skill registration, Gherkin acceptance tests, prompt inventory, generated local output, and validation checks, but those edits all support the same skill capability.

No additional OpenSpec changes are required unless implementation discovers a separate product, workflow, or architectural decision.

## Impact

XML skill indexes, XML skill references, `skills.xml`, Gherkin skill acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, `docs/`, and local `.agents/skills/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

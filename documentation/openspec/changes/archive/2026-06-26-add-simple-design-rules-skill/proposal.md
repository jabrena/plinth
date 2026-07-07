## Why

GitHub issue [#903](https://github.com/jabrena/plinth/issues/903) requests a new `053-design-simple-rules` skill for applying Kent Beck's simple design rules to Java design and refactoring decisions.

Java enterprise teams and agents need a compact design heuristic that keeps implementation work anchored in observable correctness before optimizing for clarity, duplication reduction, or minimal structure. A dedicated skill gives agents a consistent way to evaluate design tradeoffs without turning the rules into broad, speculative refactoring.

## What Changes

- Add `053-design-simple-rules` as a generated design skill for applying simple design rules.
- Explain the simple design rules in priority order:
  1. Passes the tests
  2. Reveals intention / maximizes clarity
  3. Has no duplication / minimizes duplication
  4. Has the fewest elements
- Guide agents to apply the rules to Java design and refactoring decisions, including the tradeoff that tests and clarity take priority over reducing elements.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/053-design-simple-rules`.
- Add focused Gherkin acceptance coverage and prompt inventory coverage when the skill is implemented.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `simple-design-rules-skill-reference`: Adds Simple Design Rules design skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#903](https://github.com/jabrena/plinth/issues/903).
- Issue source status: maintainer-authored user story and Gherkin acceptance criteria in the issue body.
- External references:
  - [Why do the 4 rules of simple design have that order?](https://medium.com/dan-the-dev/why-do-the-4-rules-of-simple-design-have-that-order-b5f62bfe96fc)
  - [Martin Fowler, "Beck Design Rules"](https://martinfowler.com/bliki/BeckDesignRules.html)
- Existing implementation models: `051-design-two-steps-methods` and `052-design-hamburger-method`.
- Derivation direction: issue #903 plus external Simple Design references plus existing design skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is one independently reviewable generated design skill. The change may touch XML skill sources, skill registration, Gherkin acceptance tests, prompt inventory, generated local output, and validation checks, but those edits all support the same skill capability.

No additional OpenSpec changes are required unless implementation discovers a separate product, workflow, or architectural decision.

## Impact

XML skill indexes, XML skill references, `skills.xml`, Gherkin skill acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, `docs/`, and local `.agents/skills/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

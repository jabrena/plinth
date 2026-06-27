## Why

Skill `130-java-testing-strategies` provides conceptual Java test-design guidance for RIGHT-BICEP, A-TRIP, and CORRECT. Local review found that the current skill is valuable but too compressed: one reference file covers all three techniques with only one top-level example per technique.

That structure makes the generated skill harder to use selectively. A request about flaky tests should primarily read A-TRIP guidance. A request about missing behavior tests should primarily read RIGHT-BICEP. A request about boundary conditions should primarily read CORRECT. The current monolithic reference forces agents to read broad content and gives too few concrete examples for each technique.

## What Changes

- Split the `130-java-testing-strategies` detailed reference into three focused XML references:
  - `130-java-testing-strategies-right-bicep.xml`
  - `130-java-testing-strategies-a-trip.xml`
  - `130-java-testing-strategies-correct.xml`
- Update `130-skill.xml` so the generated skill routes agents to the relevant reference by testing concern.
- Register the three references in `skills-generator/src/main/resources/skills.xml`.
- Expand each technique reference with detailed cases, good/bad examples, guidance on when the technique applies, and validation signals.
- Add acceptance coverage for `130-java-testing-strategies` because no `130` Gherkin feature currently exists.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `java-testing-strategies-skill-reference`: Defines split-reference guidance for the `130-java-testing-strategies` skill.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer discussion requesting one reference per testing technique for `130-java-testing-strategies`.
- Local source inspection: `skills-generator/src/main/resources/skill-indexes/130-skill.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/130-java-testing-strategies.xml`.
- Local generated output inspection: `.agents/skills/130-java-testing-strategies/SKILL.md`.
- Local generated output inspection: `.agents/skills/130-java-testing-strategies/references/130-java-testing-strategies.md`.
- Related skill boundary: `131-java-testing-unit-testing` remains responsible for framework-agnostic JUnit 5, AssertJ, Mockito, and unit-test implementation mechanics.
- Derivation direction: maintainer design discussion plus local source review -> OpenSpec change artifacts -> XML source split -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because RIGHT-BICEP, A-TRIP, and CORRECT are three parts of the same user-facing `130-java-testing-strategies` skill. Splitting them into separate OpenSpec changes would create artificial review boundaries and risk inconsistent routing, registration, and acceptance coverage.

The change does not implement framework-specific unit testing mechanics. Those remain in `131-java-testing-unit-testing` and framework-specific testing skills.

## Impact

XML skill indexes, XML skill references, `skills.xml`, `130` Gherkin acceptance tests, acceptance prompt inventory, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

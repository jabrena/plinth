## Why

Skill `702-technologies-wiremock` provides framework-agnostic WireMock guidance. Local review found that its scope is appropriate, but the reference has only two examples while the generated skill claims coverage for JSON and programmatic mappings, request matching, response bodies, `bodyFileName`, dynamic ports, verification, fault simulation, unmatched request debugging, and avoiding flaky stubs.

The skill should provide enough concrete WireMock patterns to improve stub quality without taking over framework-specific integration-test bootstrapping from `132`, `322`, `422`, or `522`.

## What Changes

- Expand `702-technologies-wiremock` reference examples from basic isolation and JSON matching into broader WireMock usage guidance.
- Add examples for programmatic Java DSL stubs, `bodyFileName` fixtures, dynamic port propagation, request journal debugging, fault/delay scenarios, query/header/body matching, and broad matcher anti-patterns.
- Add `702` acceptance coverage because no `702-technologies-wiremock.feature` currently exists.
- Add a matching acceptance prompt inventory entry.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `wiremock-technology-skill-reference`: Defines improved guidance and acceptance coverage expectations for the framework-agnostic WireMock skill.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer request to create OpenSpec change `improve-702-wiremock-guidance`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-indexes/702-skill.xml`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-references/702-technologies-wiremock.xml`.
- Local generated output inspection: `.agents/skills/702-technologies-wiremock/SKILL.md`.
- Local generated output inspection: `.agents/skills/702-technologies-wiremock/references/702-technologies-wiremock.md`.
- Derivation direction: maintainer request plus local source review -> OpenSpec change artifacts -> XML source improvement -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for `702-technologies-wiremock` only. WireMock HTTP stubbing is independently valuable and reviewable from OpenAPI contract design, so the related `701` improvement is tracked in a separate change.

Framework-specific test class layout, `WireMockExtension` lifecycle wiring with Spring Boot, Quarkus, or Micronaut, and project-specific base integration test setup remain out of scope unless a later change explicitly requests those concerns.

## Impact

XML skill index and reference files, `702` Gherkin acceptance tests, acceptance prompt inventory, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

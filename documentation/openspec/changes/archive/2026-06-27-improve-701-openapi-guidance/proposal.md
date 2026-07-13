## Why

Skill `701-technologies-openapi` provides framework-agnostic OpenAPI 3.x guidance. Local review found that its scope is well chosen, but its reference has only two examples while the generated skill claims coverage for reusable schemas, parameters, security schemes, examples, validation, breaking-change awareness, and codegen handoffs.

The skill should give agents enough concrete OpenAPI YAML guidance to review and improve real contracts without drifting into Spring Boot, Quarkus, Micronaut, or runtime-controller implementation details.

## What Changes

- Expand `701-technologies-openapi` reference examples from basic metadata and operations into broader OpenAPI contract guidance.
- Add examples for reusable schema design, parameters, error models, security schemes, examples/content types, breaking-change review, and validation/CI gates.
- Add `701` acceptance coverage because no `701-technologies-openapi.feature` currently exists.
- Add a matching acceptance prompt inventory entry.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `openapi-technology-skill-reference`: Defines improved guidance and acceptance coverage expectations for the framework-agnostic OpenAPI skill.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer request to create OpenSpec change `improve-701-openapi-guidance`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-indexes/701-skill.xml`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-references/701-technologies-openapi.xml`.
- Local generated output inspection: `.agents/skills/701-technologies-openapi/SKILL.md`.
- Local generated output inspection: `.agents/skills/701-technologies-openapi/references/701-technologies-openapi.md`.
- Derivation direction: maintainer request plus local source review -> OpenSpec change artifacts -> XML source improvement -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for `701-technologies-openapi` only. OpenAPI contract design is independently valuable and reviewable from WireMock HTTP stubbing, so the related `702` improvement is tracked in a separate change.

Framework-specific OpenAPI runtime exposure, controller binding, server implementation, and code generation execution remain out of scope unless a later change explicitly requests those concerns.

## Impact

XML skill index and reference files, `701` Gherkin acceptance tests, acceptance prompt inventory, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

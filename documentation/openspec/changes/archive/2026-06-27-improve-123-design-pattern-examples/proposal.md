## Why

Skill `123-java-design-patterns` is a broad design and integration pattern skill. It covers classic Java design patterns, REST API patterns, Kafka and event-driven patterns, database and persistence patterns, and cross-cutting integration patterns. Local review found that the skill shape is sound, but several references are under-exampled for the breadth they claim.

The current generated skill asks agents to select patterns by design pressure and explain benefits, costs, and when-not-to-use guidance. That is the right direction, but the reference examples do not yet give enough concrete material for Kafka reliability, persistence design, integration resilience, API evolution, or the wider classic Java pattern families.

## What Changes

- Improve the XML source references for `123-java-design-patterns` so each covered pattern area has enough concrete examples to support review and implementation work.
- Add a pattern selection matrix or equivalent compact decision aid to each `123` reference.
- Keep examples problem-led, modern Java-oriented where applicable, and paired with trade-off and when-not-to-use guidance.
- Strengthen acceptance coverage so the skill is checked beyond the existing REST-only scenario.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `java-design-patterns-skill-reference`: Defines improved coverage expectations for the `123-java-design-patterns` skill reference set.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer request to create an OpenSpec change to improve `123-java-design-patterns`.
- Local source inspection: `skills-generator/src/main/resources/skill-indexes/123-skill.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/123-java-design-patterns.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/123-rest-api-patterns.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/123-kafka-event-driven-patterns.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/123-database-persistence-patterns.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/123-cross-cutting-integration-patterns.xml`.
- Existing acceptance coverage: `skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature`.
- Derivation direction: maintainer request plus local generated/source review -> OpenSpec change artifacts -> XML source improvement -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the outcome is a single reviewable improvement to skill `123` quality: better examples, decision aids, and acceptance expectations across the existing reference set. The change should not split by reference file because each file contributes to the same user-facing skill behavior and validation boundary.

Follow-up changes may add deeper framework-specific implementations, new dedicated skills, or public release output. Those are intentionally out of scope for this first improvement.

## Hamburger Slice

The desired outcome is broad, so the first vertical slice is: improve representative coverage across all five existing `123` references enough that the skill can support real review tasks in each area.

- Entry point: keep the existing `123-java-design-patterns` skill id and reference routing.
- Guidance content: add enough examples and decision aids to cover the most common missing pattern pressures.
- Validation: add acceptance coverage for at least Kafka/event-driven and persistence scenarios in addition to existing REST behavior.
- Generated output: regenerate local `.agents/skills` only and inspect the generated skill.

Deferred follow-up slices include exhaustive pattern catalogs, framework-specific code implementations, formal diagrams for every pattern, and release-profile refresh of public `skills/`.

## Impact

XML skill reference files, the `123` Gherkin acceptance test, acceptance prompt inventory, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

## Why

Skill `133-java-testing-acceptance-tests` provides framework-agnostic Java acceptance-test guidance from maintainer-sanitized Gherkin scenario facts. Local review found that the skill is useful and coherent, but less complete than the framework-specific acceptance-test skills.

The current `133` reference has four top-level examples and no dedicated Gherkin acceptance feature. The Spring Boot sibling `323-frameworks-spring-boot-testing-acceptance-tests` already verifies trusted scenario handling, `*AT` naming, Failsafe execution, WireMock behavior, and post-change verification through an acceptance test. `133` should be brought closer to that maturity while preserving its framework-agnostic scope.

## What Changes

- Improve `133-java-testing-acceptance-tests` guidance while keeping one reference file because the skill is one workflow.
- Add examples for parse-and-confirm-before-coding, `*AT` naming with Surefire/Failsafe split, WireMock reset/isolation, Scenario Outline handling, skipped negative/error scenarios, and fixture discovery/fallback.
- Align documentation wording so `133` consistently requires maintainer-sanitized scenario facts, not raw outsider-authored `.feature` text.
- Add dedicated acceptance coverage for `133-java-testing-acceptance-tests`.
- Add a matching acceptance prompt inventory entry.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `java-acceptance-testing-skill-reference`: Defines improved guidance and acceptance coverage expectations for the framework-agnostic `133-java-testing-acceptance-tests` skill.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: maintainer request to create an OpenSpec change for improving `133-java-testing-acceptance-tests`.
- Local source inspection: `skills-generator/src/main/resources/skill-indexes/133-skill.xml`.
- Local source inspection: `skills-generator/src/main/resources/skill-references/133-java-testing-acceptance-tests.xml`.
- Local generated output inspection: `.agents/skills/133-java-testing-acceptance-tests/SKILL.md`.
- Local generated output inspection: `.agents/skills/133-java-testing-acceptance-tests/references/133-java-testing-acceptance-tests.md`.
- Comparison source: `skills-generator/src/main/resources/skill-references/323-frameworks-spring-boot-testing-acceptance-tests.xml`.
- Existing acceptance model: `skills-generator/src/test/resources/gherkin/skills/323-frameworks-spring-boot-testing-acceptance-tests.feature`.
- Derivation direction: maintainer design discussion plus local source review -> OpenSpec change artifacts -> XML source improvement -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the outcome is a single reviewable improvement to the framework-agnostic acceptance-test skill. The change should not split by example topic because all topics support one workflow: transform sanitized acceptance scenario facts into framework-agnostic Java acceptance tests.

Framework-specific acceptance-test skills remain separate and out of scope except for alignment references.

## Impact

XML skill index and reference files, `skills.xml` only if reference registration changes, `133` Gherkin acceptance tests, acceptance prompt inventory, documentation inventory wording, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review existing `133-java-testing-acceptance-tests` generated skill and source reference.
- [x] 1.2 Compare `133` with framework-specific acceptance-test skills, especially `323`.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, two-step sequencing, and validation expectations.
- [x] 1.4 Update `skills-generator/src/main/resources/skill-references/133-java-testing-acceptance-tests.xml` to preserve the single-reference workflow.
- [x] 1.5 Add or improve an example for sanitized scenario facts and trust-boundary handling.
- [x] 1.6 Add a parse-and-confirm-before-coding example that lists selected scenarios, skipped scenarios, and proposed `*AT` class names.
- [x] 1.7 Improve the `BaseAcceptanceTest` example with fixture fallback wording and no container runtime setup from this skill.
- [x] 1.8 Improve the RestAssured concrete test example with traceability to scenario titles where useful.
- [x] 1.9 Add an `*AT` naming and Surefire/Failsafe configuration example.
- [x] 1.10 Add a WireMock reset/isolation example and clarify external REST only.
- [x] 1.11 Add Scenario Outline handling and skipped negative/error scenario reporting guidance.
- [x] 1.12 Update `skills-generator/src/main/resources/skill-indexes/133-skill.xml` if workflow or precondition wording needs alignment.
- [x] 1.13 Update `documentation/guides/INVENTORY-SKILLS-JAVA.md` so the `133` entry says maintainer-sanitized scenario facts and does not advertise adding Testcontainers setup from this skill.
- [x] 1.14 Add `skills-generator/src/test/resources/gherkin/skills/133-java-testing-acceptance-tests.feature` with framework-agnostic generation and invalid-framework routing scenarios.
- [x] 1.15 Add `133-java-testing-acceptance-tests` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.16 Validate changed XML files with `xmllint --noout`.
- [x] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.18 Inspect generated local `.agents/skills/133-java-testing-acceptance-tests/SKILL.md`.
- [x] 1.19 Inspect generated local `.agents/skills/133-java-testing-acceptance-tests/references/133-java-testing-acceptance-tests.md`.
- [x] 1.20 Execute the listed `133-java-testing-acceptance-tests` acceptance prompt and verify it passes, or record a skipped prompt with reason. Skipped: no interactive acceptance-prompt runner is available in this environment; generated Gherkin coverage and local skill output were inspected.
- [x] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.22 Run `openspec validate --all`.

# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #931 and record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.2 Assess the scope as one OpenSpec change for one generated design skill.
- [x] 1.3 Apply the two-step method by separating behavior-preserving preparation from the behavior-changing skill addition.
- [x] 1.4 Step 1 preparation: review nearby design skill sources, especially `053-design-simple-rules`, and confirm the `054` identifier is available.
- [x] 1.5 Step 1 preparation: inspect generator registration patterns in `skills-generator/src/main/resources/skills.xml` and `skills-generator/src/main/resources/skill-indexes/`.
- [x] 1.6 Step 1 validation: confirm no generated output is edited directly and record the existing generator validation commands.
- [x] 1.7 Step 2 behavior change: add `skills-generator/src/main/resources/skill-indexes/054-skill.xml`.
- [x] 1.8 Step 2 behavior change: add `skills-generator/src/main/resources/skill-references/054-design-tdd.xml`.
- [x] 1.9 Step 2 behavior change: register skill id `054` with explicit `skillId="054-design-tdd"` and reference `054-design-tdd` in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.10 Step 2 behavior change: include guidance for maintaining a test list, selecting the next useful behavior, writing a failing test first, writing enough code to pass, and refactoring while tests remain green.
- [x] 1.11 Step 2 behavior change: include guidance that test-first development clarifies the public interface or usage of the code.
- [x] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/054-design-tdd.feature`.
- [x] 1.13 Add `054-design-tdd` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.14 Validate changed XML files with `xmllint --noout`.
- [x] 1.15 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.16 Inspect generated local `.agents/skills/054-design-tdd/SKILL.md`.
- [x] 1.17 Execute the listed `054-design-tdd` acceptance prompt and verify it passes, or record why it was skipped. Skipped: no repository command-line runner was found for the `execute @skills-generator/src/test/resources/gherkin/skills/054-design-tdd.feature` prompt format.
- [x] 1.18 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.19 Run `openspec validate --all`.

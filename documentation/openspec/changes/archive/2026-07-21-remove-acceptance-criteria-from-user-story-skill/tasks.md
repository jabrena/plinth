# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review existing `014-agile-user-story` generated skill and source reference, questionnaire template, and skill index.
- [x] 1.2 Update `plinth-skills-generator/src/main/resources/skill-references/assets/questions/agile-user-story-questions-template.md` to remove the Gherkin feature/scenario questions and the Gherkin filename/link-path questions, keeping and renumbering the user-story-core, filename, and notes questions.
- [x] 1.3 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 1 content to reflect the trimmed questionnaire scope.
- [x] 1.4 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 2 to remove the `## Acceptance Criteria` section and the Gherkin Feature File generation block (including `@acceptance-test`/`@integration-test` tagging rules) from the user story template and step constraints.
- [x] 1.5 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 3 checklist to drop Gherkin-specific checks while keeping title/role/goal/benefit and all six INVEST checks.
- [x] 1.6 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` safeguards to drop Gherkin-syntax and scenario-tag safeguards.
- [x] 1.7 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` metadata title/description and goal text to drop Gherkin/BDD feature-file wording.
- [x] 1.8 Update `plinth-skills-generator/src/main/resources/skill-indexes/014-skill.xml` description, goal, triggers, constraints, and step content to match the narrowed workflow.
- [x] 1.9 Update `documentation/guides/INVENTORY-SKILLS-JAVA.md` `014-agile-user-story` entry to describe Markdown-only user story output.
- [x] 1.10 Review `plinth-skills-generator/src/test/resources/gherkin/skills/014-agile-user-story.feature` and update it only if it asserts Gherkin-file generation; otherwise leave unchanged. Confirmed: no change needed, the scenario is already generic.
- [x] 1.11 Confirm `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` still has a correct `014-agile-user-story` entry. Confirmed correct, no change needed.
- [x] 1.12 Validate changed XML files with `xmllint --noout`.
- [x] 1.13 Relax `MINIMUM_TRIGGER_COUNT` in `plinth-skills-generator/src/test/java/info/jab/pml/SkillTriggerInventoryTest.java` from 5 to 4, and add a `specs/skill-trigger-quality/spec.md` MODIFIED Requirements delta in this change documenting the relaxed floor. Discovered during implementation: dropping the Gherkin trigger (task 1.8) leaves `014-agile-user-story` with four legitimate triggers, which failed the prior five-trigger minimum enforced by this test; the maintainer chose to relax the repo-wide floor rather than invent a synthetic fifth trigger.
- [x] 1.14 Run `./mvnw clean install -pl plinth-skills-generator`. BUILD SUCCESS, 1130 tests passed including "Skill trigger inventory".
- [x] 1.15 Inspect generated local `.agents/skills/014-agile-user-story/SKILL.md`. Confirmed: no Gherkin/BDD wording, 4 triggers matching the updated skill index, INVEST validation constraint present.
- [x] 1.16 Inspect generated local `.agents/skills/014-agile-user-story/references/014-agile-user-story.md`. Confirmed: no `## Acceptance Criteria`/Gherkin feature-file section, no `@acceptance-test`/`@integration-test` tagging rules, full six-criterion INVEST checklist retained.
- [x] 1.17 Execute the listed `014-agile-user-story` acceptance prompt and verify it passes, or record a skipped prompt with reason. Skipped: no interactive acceptance-prompt runner is available in this environment (precedent: `documentation/openspec/changes/archive/2026-06-27-improve-133-acceptance-test-guidance/tasks.md` task 1.20); generated Gherkin coverage (`plinth-skills-generator/src/test/resources/gherkin/skills/014-agile-user-story.feature`, unchanged per task 1.10) and local skill output (`.agents/skills/014-agile-user-story/`) were inspected instead.
- [x] 1.18 Run `./mvnw clean verify -pl plinth-skills-generator`. BUILD SUCCESS, 1130 tests passed, 0 failures.
- [x] 1.19 Run `openspec validate --all`. Totals: 46 passed, 0 failed (46 items), including `change/remove-acceptance-criteria-from-user-story-skill` and `spec/skill-trigger-quality`.

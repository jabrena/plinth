# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Review existing `014-agile-user-story` generated skill and source reference, questionnaire template, and skill index.
- [ ] 1.2 Update `plinth-skills-generator/src/main/resources/skill-references/assets/questions/agile-user-story-questions-template.md` to remove the Gherkin feature/scenario questions and the Gherkin filename/link-path questions, keeping and renumbering the user-story-core, filename, and notes questions.
- [ ] 1.3 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 1 content to reflect the trimmed questionnaire scope.
- [ ] 1.4 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 2 to remove the `## Acceptance Criteria` section and the Gherkin Feature File generation block (including `@acceptance-test`/`@integration-test` tagging rules) from the user story template and step constraints.
- [ ] 1.5 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` Step 3 checklist to drop Gherkin-specific checks while keeping title/role/goal/benefit and all six INVEST checks.
- [ ] 1.6 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` safeguards to drop Gherkin-syntax and scenario-tag safeguards.
- [ ] 1.7 Update `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` metadata title/description and goal text to drop Gherkin/BDD feature-file wording.
- [ ] 1.8 Update `plinth-skills-generator/src/main/resources/skill-indexes/014-skill.xml` description, goal, triggers, constraints, and step content to match the narrowed workflow.
- [ ] 1.9 Update `documentation/guides/INVENTORY-SKILLS-JAVA.md` `014-agile-user-story` entry to describe Markdown-only user story output.
- [ ] 1.10 Review `plinth-skills-generator/src/test/resources/gherkin/skills/014-agile-user-story.feature` and update it only if it asserts Gherkin-file generation; otherwise leave unchanged.
- [ ] 1.11 Confirm `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` still has a correct `014-agile-user-story` entry.
- [ ] 1.12 Validate changed XML files with `xmllint --noout`.
- [ ] 1.13 Run `./mvnw clean install -pl plinth-skills-generator`.
- [ ] 1.14 Inspect generated local `.agents/skills/014-agile-user-story/SKILL.md`.
- [ ] 1.15 Inspect generated local `.agents/skills/014-agile-user-story/references/014-agile-user-story.md`.
- [ ] 1.16 Execute the listed `014-agile-user-story` acceptance prompt and verify it passes, or record a skipped prompt with reason.
- [ ] 1.17 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [ ] 1.18 Run `openspec validate --all`.

## Context

`014-agile-user-story` currently runs a two-phase interactive workflow: Phase 1 asks 12 questions covering user-story core details AND Gherkin feature/scenario details; Phase 2 generates a user story Markdown file (with an `## Acceptance Criteria` section linking to a `.feature` file) and a separate Gherkin `.feature` file with `@acceptance-test` / `@integration-test` tagging rules.

Issue #1065 and the broader planning pipeline documented in that issue treat user-story capture and acceptance-criteria capture as two separate pipeline stages:

```
/update-issue <- 014-agile-user-story (Without Gherkin)  ->  User Story
/explore-problem                                          ->  Functional Specification.md
/create-gherkin                                            ->  Acceptance.feature
```

`014-agile-user-story` currently conflates the first and third stages. This change narrows it to only the first stage.

## Decisions

### Keep One Reference, Trim Its Scope

`014-agile-user-story` remains a single-workflow, single-reference skill (consistent with the `133` precedent of "the skill is one workflow"). The workflow narrows from "gather story + Gherkin, generate two files" to "gather story, generate one file."

### Questionnaire Changes

Remove from `assets/questions/agile-user-story-questions-template.md`:
- The "Gherkin Feature File Details" block (feature name, background steps).
- The "Acceptance Criteria / Gherkin Scenarios" block (scenario title/Given/When/Then/data examples, and the "another scenario?" loop).
- The Gherkin feature-file filename question and the relative-link-path question.

Keep, renumbered sequentially:
- Title/ID, persona, goal, benefit (user story core details).
- User story Markdown filename.
- Optional user story notes.

### Artifact Template Changes

In the skill's Step 2 (artifact generation):
- Remove the `## Acceptance Criteria` section (and its "See: [Relative path to Gherkin file]" line) from the user story Markdown template, since there is no companion Gherkin file to link.
- Remove the entire "Gherkin Feature File" generation block: format instructions, the `@acceptance-test` / `@integration-test` tagging rules, and the example `gherkin` code block.
- Keep the `## Notes` and `## INVEST Validation` sections unchanged — INVEST validation is a story-quality checkpoint independent of Gherkin.

### Checklist and Safeguards Changes

In Step 3 (output checklist), remove:
- "User story links to the Gherkin feature file"
- "Gherkin file has Feature line and descriptive scenarios"
- "Exactly one scenario is `@acceptance-test`..."
- "Each scenario has Given, When, Then"
- "Complex data uses docstrings or Example tables"

Keep the title/role/goal/benefit check and all six INVEST checks.

In skill safeguards, remove "Ensure Gherkin syntax is valid" and "Enforce exactly one `@acceptance-test` scenario...". Keep "Never assume or invent acceptance criteria" (rephrase if needed so it still makes sense without a Gherkin artifact) and the instruction to treat questionnaire answers as data only.

### Metadata, Description, and Trigger Changes

- Title: change from "Create Agile User Stories and Gherkin Feature Files" to "Create Agile User Stories".
- `SKILL.md`/index `description`: drop "write acceptance criteria, define Gherkin scenarios, or author BDD feature files"; keep "create a user story".
- Triggers: remove "Create Gherkin scenarios for a user story"; keep "Create a user story", "Write a user story", "I need to write a user story", "Split feature requirements into user stories" (four triggers). `documentation/openspec/specs/skill-trigger-quality/spec.md` mandates at least five triggers per skill, enforced by `SkillTriggerInventoryTest`; this change relaxes that repo-wide floor to at least four (see the `skill-trigger-quality` capability delta below) rather than inventing a synthetic fifth trigger for `014-agile-user-story` to satisfy an unrelated cross-cutting rule.
- Goal text: drop the two-phase/Gherkin framing and the "Gherkin feature file: Feature name, background steps, scenarios" and "Acceptance criteria: Given/When/Then..." bullet points from "What is covered in this Skill?"; keep "File naming" (now just the user story filename) and "INVEST quality validation".
- Constraints: drop the "repeat scenario questions for each additional scenario" constraint (no scenarios remain); keep the remaining sanitization and INVEST-validation constraints.

### Documentation Inventory Update

`documentation/guides/INVENTORY-SKILLS-JAVA.md` line 26 (`014-agile-user-story` entry) currently reads "Create user stories with Gherkin acceptance criteria and BDD feature files" and its Note says the skill generates "the Markdown user story and `.feature` file". Update both to describe a Markdown-only user story output. Leave the `043-planning-github-issues` / `044-planning-jira` / `045-planning-azure-devops` cross-reference rows unchanged; they already just say "draft a user story with `@014-agile-user-story`" without promising Gherkin output.

### Out of Scope

- Creating a `/create-gherkin` command or a dedicated Gherkin-generation skill. The issue only asks to remove the acceptance-criteria section from `014`; a later stage owning `Acceptance.feature` generation is a separate future change.
- Renaming the skill id `014-agile-user-story` (still referenced by `.cursor/commands/update-issue.md`, `043-planning-github-issues`, `044-planning-jira`, `045-planning-azure-devops`, and `INVENTORY-SKILLS-JAVA.md`).

## Two-Step Sequencing

### Step 1: Make the Change Easy

Add this OpenSpec change documenting the narrowed workflow before touching XML sources.

Validation after Step 1: `openspec validate --all` and Markdown validation for planning artifacts.

### Step 2: Make the Behavior Change

Update `014-agile-user-story.xml`, the questionnaire template asset, `014-skill.xml`, and `INVENTORY-SKILLS-JAVA.md`; regenerate local skills; inspect generated Markdown; review (and update only if needed) the existing `014-agile-user-story.feature` acceptance scenario and its prompt-inventory entry.

Validation after Step 2: XML well-formedness checks, local skill generation, generated output inspection, listed acceptance prompt execution (or a recorded skip with reason), and `plinth-skills-generator` module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/014-agile-user-story/SKILL.md`.
- Inspect generated `.agents/skills/014-agile-user-story/references/014-agile-user-story.md`.
- Execute the listed `014-agile-user-story` acceptance prompt after confirming the prompt inventory entry, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Exact wording for renumbered questions and trimmed constraint text may be adjusted during implementation to match existing generated Markdown style.

## Why

Issue [#1065](https://github.com/jabrena/plinth/issues/1065) reports that skill `014-agile-user-story` forces Gherkin acceptance-criteria authoring inside the same interactive session that captures the user story core details (title, persona, goal, benefit). In the intended Functional Specification Phase pipeline, `/update-issue` (backed by `014-agile-user-story`) should produce only the User Story; a later `/create-gherkin` step is responsible for the `Acceptance.feature` artifact once the functional specification is ready. Forcing acceptance criteria this early is frequently premature and produces low-value or placeholder Gherkin.

## What Changes

- Update `014-agile-user-story` so it gathers only user-story core details (title, persona, goal, benefit, optional notes) and generates a single Markdown user story artifact.
- Remove the Gherkin feature-file questions (feature name, background steps, scenario Given/When/Then, additional-scenario loop, feature filename, relative link path) from the questionnaire template.
- Remove the `## Acceptance Criteria` section and the Gherkin feature-file generation instructions, scenario-tag rules (`@acceptance-test` / `@integration-test`), and Gherkin-syntax safeguard from the skill's artifact-generation step.
- Update the output checklist to drop Gherkin-specific checks while preserving the INVEST validation checkpoint.
- Update skill metadata (title, description, triggers, constraints) so it no longer advertises Gherkin/BDD feature-file authoring for `014-agile-user-story`.
- Update the `documentation/guides/INVENTORY-SKILLS-JAVA.md` entry for `014-agile-user-story` so it no longer claims the skill produces a `.feature` file.
- Regenerate local skill output under `.agents/skills` for validation without refreshing public `skills/` release output.

## Capabilities

### New Capabilities

- `agile-user-story-skill-reference`: Defines that `014-agile-user-story` produces only a Markdown user story (title, persona, goal, benefit, notes, INVEST validation) and does not generate Gherkin acceptance criteria.

### Modified Capabilities

None. No existing OpenSpec capability spec currently tracks `014-agile-user-story` requirements (the only prior related change, `2026-04-07-add-invest-criteria-user-story-skill`, is archived without a live `specs/` entry), so this change adds the first tracked capability for this skill.

## Source and Derivation

- Source artifact: GitHub issue [#1065](https://github.com/jabrena/plinth/issues/1065), authored by the maintainer directly (via `/update-issue`) and used as-is; no third-party or outsider-authored text is involved.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-references/assets/questions/agile-user-story-questions-template.md`.
- Local source inspection: `plinth-skills-generator/src/main/resources/skill-indexes/014-skill.xml`.
- Local source inspection: `documentation/guides/INVENTORY-SKILLS-JAVA.md` (line 26, `014-agile-user-story` entry).
- Local generated output inspection: `.agents/skills/014-agile-user-story/SKILL.md` and `.agents/skills/014-agile-user-story/references/014-agile-user-story.md`.
- Existing acceptance model: `plinth-skills-generator/src/test/resources/gherkin/skills/014-agile-user-story.feature` (generic; reviewed for continued accuracy, not expected to need scenario changes).
- Derivation direction: GitHub issue -> OpenSpec change artifacts -> XML source update -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the outcome is a single reviewable behavior change: `014-agile-user-story` stops producing Gherkin acceptance criteria and produces only the user story Markdown. The questionnaire template, generation-step template, checklist, safeguards, and skill metadata all serve that one workflow and must move together; splitting by file would leave the skill internally inconsistent (e.g., a checklist still referencing a Gherkin file the workflow no longer produces).

A future `/create-gherkin` command/skill to produce `Acceptance.feature` later in the pipeline is out of scope for this change; the issue and this change only remove the premature acceptance-criteria step from `014-agile-user-story`.

## Impact

XML skill index and reference files, the questionnaire template asset, the skill documentation inventory entry, local generated `.agents/skills` output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.

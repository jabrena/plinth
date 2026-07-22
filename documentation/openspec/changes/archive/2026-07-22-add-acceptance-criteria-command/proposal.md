## Why

GitHub issue [#1063](https://github.com/jabrena/plinth/issues/1063) requests a dedicated `/create-acceptance-criteria <issue-url>` command for the Functional Specification pipeline. `/explore-problem` can already post a maintainer-confirmed Functional Specification as an issue comment, but the pipeline has no subsequent step that turns that analysis into Gherkin acceptance criteria. Without those observable scenarios, contributors and reviewers cannot verify consistently that an asset achieved its stated goal.

The new command closes that verification gap. It locates the Functional Specification comment produced by `/explore-problem`, applies `058-design-bdd` to derive acceptance scenarios, presents the result for confirmation, and posts a second, separate comment containing the same Markdown and fenced Gherkin structure on GitHub, Jira, or Azure DevOps.

## What Changes

- Add `/create-acceptance-criteria <issue-url>` to the embedded command bundle, owned by `@robot-business-analyst`.
- Detect GitHub, Jira, or Azure DevOps from the issue URL and reuse `043-planning-github-issues`, `044-planning-jira`, or `045-planning-azure-devops` for tracker access and authentication.
- Locate the Functional Specification comment produced by `/explore-problem` and use that maintainer-confirmed analysis as the trusted behavior input to `058-design-bdd`.
- Ask focused clarification questions when the Functional Specification lacks behavior facts needed to create correct scenarios, rather than inventing acceptance behavior.
- Produce one common Markdown comment containing a fenced Gherkin feature, present the complete draft for explicit confirmation, and post it as a new comment without modifying the issue description or Functional Specification comment.
- Add command inventory, generated-bundle propagation, focused contract, and Gherkin acceptance coverage for the new workflow.

## Capabilities

### New Capabilities

- `create-acceptance-criteria-command`: Defines the command input, Functional Specification lookup, BDD interaction, common Markdown/Gherkin output, cross-tracker publication, confirmation, and failure behavior.

### Modified Capabilities

None.

## Impact

This is an additive, non-breaking command-bundle change. Implementation will touch XML command sources and inventory under `plinth-commands-generator/src/main/resources/`, command-focused tests under `plinth-commands-generator/src/test/`, command inventory documentation, and the `001-commands-inventory` and `004-commands-installation` propagation expectations in `plinth-skills-generator`. It reuses `058-design-bdd` and tracker skills `043`–`045`; it does not change their generated XML sources or refresh public `skills/` release output.

## Source and Derivation

- Traceability source: GitHub issue [#1063](https://github.com/jabrena/plinth/issues/1063).
- Authoritative requirements summary: the maintainer-confirmed Functional Specification posted at [issue comment #5048962455](https://github.com/jabrena/plinth/issues/1063#issuecomment-5048962455), produced interactively through `/explore-problem`.
- Maintainer clarifications from the same session: all three trackers are required; every tracker receives the same Markdown comment; `/explore-problem` first creates the Functional Specification comment, then this command consumes that comment and creates a separate acceptance-criteria comment.
- Existing capability sources: `documentation/openspec/specs/explore-problem-command/spec.md` and `documentation/openspec/specs/bdd-design-skill-reference/spec.md`.
- Structural precedent: `documentation/openspec/changes/archive/2026-07-22-add-explore-problem-command/`.
- Derivation direction: issue traceability plus the maintainer-confirmed Functional Specification and clarifications -> this OpenSpec change -> future XML command implementation and generated local bundle verification. No source issue or prior comment is silently synchronized from these artifacts.

## Why

GitHub issue [#954](https://github.com/jabrena/plinth/issues/954) requests converting the `/review-breaking-changes` command concept into a generated skill named `056-design-avoid-breaking-changes`.

The original command request in issue [#886](https://github.com/jabrena/plinth/issues/886) asked for a way to review OpenSpec plans or specs for compatibility risks before implementation or release work proceeds. That value still matters, but the workflow should live in the skill system so breaking-change review guidance can evolve without creating another command-level contract to maintain.

## What Changes

- Add `056-design-avoid-breaking-changes` as a generated design skill for reviewing plans, specs, and implementation proposals for compatibility risks.
- Design the skill around multiple breaking-change aspects: command contracts, skill identifiers and triggers, generated outputs, XML/source ownership, README and public documentation, build/CI expectations, API/schema/configuration contracts, and migration guidance.
- Retire the current `/review-breaking-changes` command from command sources, command installation, command inventories, generated command output, and command acceptance coverage.
- Update `README.md`, `README_ES.md`, and `README_ZH.md` so planning documentation no longer advertises `/review-breaking-changes` and instead points maintainers to `@056-design-avoid-breaking-changes`.
- Preserve traceability from issue #886 to the new skill workflow.

## Capabilities

### New Capabilities

- `design-avoid-breaking-changes-skill-reference`: Defines the generated skill that reviews planned changes for breaking-change risks and migration needs.

### Modified Capabilities

- `analysis-design-commands`: Removes the `/review-breaking-changes` command from the command bundle and its public documentation path.

## Source and Derivation

- Source artifact: GitHub issue [#954](https://github.com/jabrena/plinth/issues/954).
- Related source artifact: GitHub issue [#886](https://github.com/jabrena/plinth/issues/886).
- Local command workflow: `.cursor/commands/create-spec.md`.
- Local OpenSpec guidance: `042-planning-openspec`.
- Design sequencing guidance: `051-design-two-steps-methods`.
- Slice assessment guidance: `052-design-hamburger-method`.
- Derivation direction: issue #954 user story plus issue #886 traceability -> OpenSpec change artifacts -> XML skill source implementation -> command retirement -> README updates -> generated local validation.

## Change Boundary Assessment

This is one OpenSpec change because the user-visible outcome is one workflow conversion: breaking-change review moves from `/review-breaking-changes` to `@056-design-avoid-breaking-changes`. The implementation touches skill XML, command assets, command tests, command installation, inventories, README files, and generated local output checks, but those changes share one release boundary and one migration story.

The smallest useful slice is to add the skill, remove the command from every command-owned source, update README references, and validate the generator. Follow-up slices may add richer examples or automated compatibility linting, but those are not required for this conversion.

## Impact

Affected areas include `plinth-skills-generator/src/main/resources/skill-indexes/`, `plinth-skills-generator/src/main/resources/skill-references/`, `plinth-skills-generator/src/main/resources/skills.xml`, `plinth-skills-generator/src/main/resources/commands.xml`, command assets, command installation references, command inventory templates, command-focused tests, skill acceptance tests, README files, and OpenSpec artifacts.

Generated `.agents/skills` output should be refreshed through `./mvnw clean install -pl plinth-skills-generator` for local verification. Public `skills/` release output should only be refreshed through the release profile when release output is intentionally in scope.

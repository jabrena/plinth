## Why

GitHub issue [#1048](https://github.com/jabrena/plinth/issues/1048) requests a generated design skill named `059-design-atdd`. The repository does not provide a dedicated Acceptance Test-Driven Development skill for reviewing whether an OpenSpec change's execution goal, acceptance criteria, and implementation tasks remain aligned.

## What Changes

- Add `059-design-atdd` as a generated design skill.
- Define a repeatable ATDD review workflow for an OpenSpec change.
- Evaluate whether acceptance criteria exist, are clear, and are covered by the associated tasks.
- Identify acceptance criteria and tasks that diverge, have only partial coverage, are ambiguous, or are missing.
- Produce a clear alignment outcome that helps execution follow the intended goal without silently rewriting the OpenSpec change.
- Return `changes-requested`, explain every pending finding, and ask the maintainer how to revise the OpenSpec artifacts when unresolved alignment findings exist.
- Add a bundled, example-driven reference that defines the six alignment statuses and the evidence-backed traceability report shape.
- Register the skill in the unified generator inventory and add the required XML source content.
- Add focused Gherkin acceptance coverage and the matching acceptance-prompt inventory entry.
- Add the generated skill to the Java skills inventory documentation.

## Capabilities

### New Capabilities

- `atdd-design-skill-reference`: Defines the generated `059-design-atdd` skill, its acceptance-criteria-to-task alignment review, generator registration, acceptance coverage, documentation, and generated-output ownership.

### Modified Capabilities

None.

## Impact

- Adds or updates XML sources under `plinth-skills-generator/src/main/resources/`.
- Adds generator acceptance-test resources under `plinth-skills-generator/src/test/resources/gherkin/skills/`.
- Updates `documentation/guides/INVENTORY-SKILLS-JAVA.md`.
- Generates local test output under `.agents/skills/059-design-atdd/` through Maven; it does not require direct edits to `.agents/skills/`, `.cursor/rules/`, `skills/`, or `docs/`.
- Compatibility: additive and non-breaking; existing skill identifiers and behavior remain unchanged, and no migration or feature toggle is required.
- Cross-tool behavior: the generated skill follows the same `SKILL.md` plus bundled local-reference layout used by existing design skills.

## Source and Derivation

| Source | Concern | Derivation |
| --- | --- | --- |
| Maintainer-authorized user-story description in GitHub issue [#1048](https://github.com/jabrena/plinth/issues/1048) | Skill identifier, user value, high-level ATDD intent, acceptance boundaries, implementation notes, and INVEST assessment | Sanitized issue story → OpenSpec change (one-way) |
| Maintainer clarification dated 2026-07-20 | Review acceptance criteria against the execution goal and associated tasks; detect divergence, missing or partial coverage, ambiguity, and absent criteria | Maintainer clarification → proposal, design, specification, and tasks |
| Maintainer reference decision dated 2026-07-20 | Use a dedicated bundled reference instead of the self-contained `requiresSystemPrompt="false"` inventory exception | Maintainer decision → generator registration, XML reference, generated runtime layout, specification, and tests |
| Maintainer clarification dated 2026-07-21 | Use `changes-requested` for unresolved alignment findings, explain what is pending, and ask how the OpenSpec artifacts should be revised | Maintainer clarification → skill constraints, workflow, negative OpenSpec fixture, and acceptance coverage |
| `documentation/openspec/changes/archive/2026-07-20-add-bdd-design-skill/` | Established structure for one generated design-skill change | Repository pattern → artifact organization and validation approach only |
| `plinth-skills-generator/src/main/resources/skills.xml` | Unified generator registration convention | Repository source → design, specification, and tasks |
| Repository `AGENTS.md` | Source ownership, OpenSpec workflow, acceptance-prompt policy, and validation requirements | Repository policy → proposal, design, and tasks |

Derivation is one-way into this OpenSpec change. This change does not update issue #1048 or silently synchronize any source artifact.

## Unresolved Questions

- No specific acceptance-test framework, automation technology, or external methodology reference is selected by the authoritative inputs.

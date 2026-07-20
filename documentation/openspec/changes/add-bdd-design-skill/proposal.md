## Why

GitHub issue [#1047](https://github.com/jabrena/plinth/issues/1047) requests a generated design skill named `058-design-bdd`. The repository already supports authoring Gherkin in `014-agile-user-story`, test-driven implementation in `054-design-tdd`, and acceptance-test implementation in `133`, `323`, `423`, and `523`, but it has no design skill that guides the Behavior-Driven Development lifecycle connecting business examples to implementation and verification.

## What Changes

- Add `058-design-bdd` as a generated, interactive design skill.
- Define BDD guidance that starts from maintainer-provided or maintainer-sanitized behavior facts, develops concrete examples in shared domain language, formulates observable Given/When/Then scenarios, and connects those examples to implementation and verification.
- Include the official Cucumber [Gherkin Reference](https://cucumber.io/docs/gherkin/reference/) as the canonical syntax reference supplied by the skill.
- Keep the skill focused on BDD design and collaboration; route user-story authoring, TDD cycles, and framework-specific acceptance-test implementation to their existing skills.
- Register the skill in the unified generator inventory and add its skill index and detailed XML reference.
- Add Gherkin acceptance coverage and the required matching acceptance-prompt inventory entry.
- Add the generated skill to the Java skills inventory documentation.

## Capabilities

### New Capabilities

- `bdd-design-skill-reference`: Defines the generated `058-design-bdd` skill, its BDD workflow and boundaries, generator registration, acceptance coverage, documentation, and generated-output ownership.

### Modified Capabilities

None.

## Impact

- Adds XML source files and registration under `plinth-skills-generator/src/main/resources/`.
- Adds generator acceptance-test resources under `plinth-skills-generator/src/test/resources/gherkin/skills/`.
- Updates `documentation/guides/INVENTORY-SKILLS-JAVA.md`.
- Generates local test output under `.agents/skills/058-design-bdd/` through Maven; it does not require direct edits to `.agents/skills/`, `.cursor/rules/`, `skills/`, or `docs/`.
- Compatibility: additive and non-breaking; no migration or feature toggle is required.
- Cross-tool behavior: the generated skill follows the same `SKILL.md` plus local reference layout used by the existing design skills.

## Source and Derivation

| Source | Concern | Derivation |
| --- | --- | --- |
| GitHub issue [#1047](https://github.com/jabrena/plinth/issues/1047) metadata and title | Requested skill identifier and intent | Issue metadata/title → OpenSpec change (one-way); raw issue body was not ingested |
| Maintainer request dated 2026-07-20 | Require a good Gherkin syntax reference in the future skill | Maintainer requirement → proposal, design, specification, and tasks |
| Official Cucumber [Gherkin Reference](https://cucumber.io/docs/gherkin/reference/) | Canonical Gherkin syntax, keywords, structure, arguments, tags, comments, and localization | Primary technical reference → design and specification |
| `plinth-skills-generator/src/main/resources/skills.xml` | Generator registration convention and available `058` slot | Repository source → design and tasks |
| `plinth-skills-generator/src/main/resources/skill-references/054-design-tdd.xml` | Neighboring design-skill source structure and TDD boundary | Repository source → design and specification |
| `plinth-skills-generator/src/main/resources/skill-references/014-agile-user-story.xml` | Existing Gherkin/user-story responsibility | Repository source → BDD routing boundary |
| `documentation/openspec/specs/java-acceptance-testing-skill-reference/spec.md` | Existing acceptance-test and sanitized-input boundaries | Existing OpenSpec → BDD routing and trust boundaries |
| Repository `AGENTS.md` | Generated-output ownership and validation workflow | Repository policy → tasks and verification |

Derivation is one-way into this OpenSpec change. This change does not update issue #1047 or silently synchronize any source artifact.

## Unresolved Questions

- The issue body was intentionally not used without a maintainer-provided sanitized summary. If that summary adds requirements beyond the issue title, this change requires an explicit alignment review before implementation.
- Additional BDD references can be evaluated during `/explore-design`, but they must not replace the required official Cucumber Gherkin syntax reference.

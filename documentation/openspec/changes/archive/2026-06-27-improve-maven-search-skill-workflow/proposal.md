## Why

GitHub issue [#881](https://github.com/jabrena/plinth/issues/881) identifies that `114-java-maven-search` has an unclear purpose because it currently combines two related but operationally different workflows:

- Maven project update analysis using maintainer-provided Versions Maven Plugin output.
- Maven Central artifact discovery using Search API fields and repository URL construction.

The current shape can cause agents to treat Maven Central search as the default answer for project-local dependency update questions. For a user's own `pom.xml`, the safer default is to interpret local resolver output or maintainer-provided Versions Maven Plugin reports first, and use Maven Central search only when artifact discovery or coordinate verification is explicitly needed.

## What Changes

- Clarify the generated `114-java-maven-search` skill as a router for two Maven version-related workflows.
- Separate detailed guidance into project-local version update guidance and Maven Central search guidance.
- Prefer project-local update guidance for outdated dependencies, plugin updates, property version bumps, or "what can be updated in this project" requests.
- Use Maven Central search guidance for explicit artifact discovery, coordinate verification, version browsing, or artifact URL construction.
- Remove contradictory guidance that both forbids raw remote POM ingestion and suggests direct remote POM dependency extraction.

## Capabilities

### New Capabilities

- `java-maven-search-skill-reference`: Defines routing and reference requirements for `114-java-maven-search`.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#881](https://github.com/jabrena/plinth/issues/881).
- Source summary: User story and acceptance criteria added to issue #881 by the maintainer workflow.
- Existing generated skill under review: `.agents/skills/114-java-maven-search/SKILL.md`.
- Existing XML sources: `skills-generator/src/main/resources/skill-indexes/114-skill.xml` and `skills-generator/src/main/resources/skill-references/114-java-maven-search.xml`.
- Derivation direction: issue #881 -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the work has one reviewable outcome: clarify the purpose and routing of `114-java-maven-search`. The implementation may touch the skill index, reference content, and generated local output, but these edits share one value, ownership, release, rollback, and validation boundary.

## Impact

XML skill index and reference sources for `114-java-maven-search`, local generated `.agents/skills/114-java-maven-search` output, and related skill acceptance validation may be affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.

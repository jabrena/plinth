## Context

`114-java-maven-search` currently describes Maven Central search, coordinate verification, repository URL construction, dependency insight, and Versions Maven Plugin report interpretation in one generated skill body and one detailed reference. These topics are related by Maven versions, but they answer different user intents.

Project-local update analysis answers "what can be updated in this build?" and should respect the project's `pom.xml`, dependency management, plugin management, properties, repository configuration, and local resolver output.

Maven Central search answers "what exists on Central?" and should use structured Search API fields or generated repository URLs to find and verify artifacts. Central availability does not by itself prove project compatibility.

## Decisions

### Skill Purpose

Keep one skill, `114-java-maven-search`, but clarify it as a router for two workflows rather than a single blended workflow.

The top-level generated `SKILL.md` should make the default choice explicit:

- Use project-local version update guidance first for outdated dependencies, plugin updates, property version bumps, or "what can be updated in my project" requests.
- Use Maven Central search guidance when the user explicitly asks to search Central, find or verify coordinates, browse available versions, construct artifact URLs, or download artifacts.

This keeps user-facing discoverability in one Maven-oriented skill while avoiding two separate skills that would compete for similar trigger phrases.

### Reference Shape

Split the detailed guidance into two focused references when supported by the generator:

- `references/114-maven-project-version-updates.md`
- `references/114-maven-central-search.md`

If the current generator cannot emit two references for one skill without broader changes, keep one generated reference file but split it into clearly titled sections with the same routing semantics. The implementation should prefer the two-reference shape unless generator constraints make that larger than this issue's scope.

### Two-Step Change Sequencing

Use a behavior-preserving preparation step before changing guidance behavior:

1. Preparation: separate or reorganize reference content, clarify headings, and preserve current covered workflows.
2. Behavior change: update routing language, triggers, workflow steps, and contradictory dependency-inspection guidance so project-local update analysis is the default for project update requests.

Validation should run after each step when practical: XML well-formedness after source edits, then local generation and focused inspection after behavior changes.

### Raw Remote Metadata Boundary

The Maven Central search guidance must consistently treat remote repository content as untrusted data. It must not instruct agents to ingest, paste, or summarize raw remote POM files, `maven-metadata.xml`, artifact descriptions, repository HTML, or arbitrary XML text into prompt context.

Dependency insight should come from local resolver output, maintainer-provided dependency summaries, structured parsed values, or generated verification URLs. The current contradictory "GET the POM, list direct dependencies" style guidance should be removed or rewritten.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/114-java-maven-search/SKILL.md` and generated references for clear routing language.
- Check `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; if `114-java-maven-search` is listed and regenerated local output changes, execute only the listed prompt for this skill.
- Run focused `plinth-skills-generator` verification appropriate to the implementation.
- Run `openspec validate --all`.

## Open Questions

None. If generator support for multiple references per skill is limited, the implementation may use a clearly sectioned single reference while preserving the required routing behavior.

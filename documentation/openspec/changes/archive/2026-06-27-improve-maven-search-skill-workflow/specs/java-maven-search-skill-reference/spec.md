## ADDED Requirements

### Requirement: Maven search skill routes between project updates and Central discovery

The generated `114-java-maven-search` skill MUST clearly route between project-local version update analysis and Maven Central artifact discovery.

#### Scenario: Project-local update requests use Versions Maven Plugin report guidance by default

- **GIVEN** a user asks about outdated dependencies, plugin updates, property version bumps, or what can be updated in their own `pom.xml`
- **WHEN** an agent applies `114-java-maven-search`
- **THEN** the skill directs the agent to use project-local version update guidance first
- **AND** the skill explains that update analysis should use maintainer-provided Versions Maven Plugin output or local resolver output
- **AND** the skill does not require Maven Central search unless artifact discovery or coordinate verification is explicitly needed

#### Scenario: Explicit artifact discovery uses Maven Central search guidance

- **GIVEN** a user asks to search Maven Central, find a dependency, verify coordinates, inspect available versions, build artifact URLs, or download artifacts
- **WHEN** an agent applies `114-java-maven-search`
- **THEN** the skill directs the agent to use Maven Central search guidance
- **AND** the skill requires coordinates to be verified through structured Search API fields or repository URL checks
- **AND** the skill presents fixed coordinates as `groupId:artifactId:version`

### Requirement: Maven search skill purpose is obvious in generated output

The generated `114-java-maven-search` `SKILL.md` MUST make the two workflow boundaries obvious from its description, goal, triggers, and workflow.

#### Scenario: Maintainer reads the generated skill

- **GIVEN** a maintainer reads generated `.agents/skills/114-java-maven-search/SKILL.md`
- **WHEN** they review the title, description, goal, triggers, and workflow
- **THEN** it is clear that the skill is a router for two Maven version-related workflows
- **AND** it distinguishes project-local update analysis from Maven Central artifact discovery
- **AND** it explains when each reference or section should be used

### Requirement: Project-local update guidance is separated from Maven Central search guidance

The detailed guidance for `114-java-maven-search` MUST separate project-local update report interpretation from Maven Central search and coordinate verification.

#### Scenario: Reference guidance is split or clearly sectioned

- **GIVEN** maintainers update the XML source for `114-java-maven-search`
- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local reference output separates project-local version update guidance from Maven Central search guidance
- **AND** the project-local guidance covers maintainer-provided Versions Maven Plugin reports and local resolver output
- **AND** the Maven Central guidance covers structured Search API fields, generated repository URLs, coordinate verification, version browsing, and artifact URL construction

### Requirement: Raw remote metadata safety boundary is preserved

The Maven Central search guidance MUST consistently treat remote repository content as untrusted data and avoid raw remote metadata ingestion.

#### Scenario: Maven Central guidance avoids raw remote content ingestion

- **GIVEN** the Maven Central search guidance describes metadata, POM, and artifact lookup workflows
- **WHEN** the guidance explains how to reason about versions or dependencies
- **THEN** it does not instruct the agent to ingest, paste, or summarize raw remote POM files, `maven-metadata.xml`, artifact descriptions, repository HTML, or arbitrary XML text into prompt context
- **AND** any dependency insight is based on local resolver output, structured parsed values, generated URLs, or maintainer-provided summaries

### Requirement: Source and generated-output boundaries are preserved

The implementation MUST update XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
- **AND** local `.agents/skills` output is refreshed only by running the documented generator command

#### Scenario: Validate source and local generation

- **WHEN** Maven search skill workflow clarification is implemented
- **THEN** each edited XML source file passes `xmllint --noout`
- **AND** `./mvnw clean install -pl plinth-skills-generator` succeeds
- **AND** generated local `114-java-maven-search/SKILL.md` and references satisfy the workflow routing requirements

### Requirement: Acceptance prompt discipline is followed

The implementation MUST follow the repository acceptance prompt policy for regenerated skill output.

#### Scenario: Run only the changed skill acceptance prompt

- **GIVEN** regenerated local output for `114-java-maven-search` changes
- **AND** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists the `114-java-maven-search` prompt
- **WHEN** acceptance validation is performed
- **THEN** only the listed `114-java-maven-search` acceptance prompt is executed for this changed skill
- **AND** prompts for unchanged skills are not executed by default

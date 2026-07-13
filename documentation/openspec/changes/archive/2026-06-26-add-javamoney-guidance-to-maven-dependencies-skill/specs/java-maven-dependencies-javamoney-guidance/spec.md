## ADDED Requirements

### Requirement: JavaMoney guidance in Maven dependencies skill

The repository MUST provide JavaMoney guidance in the generated `111-java-maven-dependencies` skill so agents can identify when Java Money and Currency API support is relevant during Maven-oriented workflows.

#### Scenario: Generated skill includes JavaMoney reference

- **GIVEN** maintainers update the XML source for `111-java-maven-dependencies`
- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output for `111-java-maven-dependencies` includes `https://javamoney.github.io/`
- **AND** the generated guidance explains that JavaMoney is relevant for Java Money and Currency API support
- **AND** the generated guidance mentions the JSR 354 API or Moneta reference implementation context

#### Scenario: JavaMoney guidance is scoped to dependency guidance

- **GIVEN** a user applies `111-java-maven-dependencies` to a Maven project
- **WHEN** the skill presents JavaMoney guidance
- **THEN** the guidance helps the agent recognize Money and Currency API needs
- **AND** it does not add unrelated Maven dependencies unless the user selected those existing dependency-family options

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy, local generated, or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** `.agents/skills/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

#### Scenario: Validate source and local generation

- **WHEN** JavaMoney guidance is implemented
- **THEN** each edited XML source file passes `xmllint --noout`
- **AND** `./mvnw clean install -pl plinth-skills-generator` succeeds
- **AND** generated local `111-java-maven-dependencies/SKILL.md` contains the JavaMoney guidance

### Requirement: Acceptance prompt discipline

The implementation MUST follow the repository acceptance prompt policy for regenerated skill output.

#### Scenario: Run only the changed skill acceptance prompt

- **GIVEN** regenerated local output for `111-java-maven-dependencies` changes
- **AND** `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` lists the `111-java-maven-dependencies` prompt
- **WHEN** acceptance validation is performed
- **THEN** only the listed `111-java-maven-dependencies` acceptance prompt is executed for this changed skill
- **AND** prompts for unchanged skills are not executed by default

## Source and Derivation

- Source artifact: GitHub issue [#875](https://github.com/jabrena/plinth/issues/875).
- Derivation direction: GitHub issue #875 -> sanitized planning summary -> OpenSpec requirement delta -> XML skill source implementation -> local generated skill and acceptance validation.

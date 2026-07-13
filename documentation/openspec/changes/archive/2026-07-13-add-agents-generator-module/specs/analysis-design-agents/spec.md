## MODIFIED Requirements

### Requirement: Non-Java default implementation agent

The embedded agent bundle SHALL include `robot-no-java` as the default implementation delegate for issue, plan, or OpenSpec work whose authoritative artifacts do not use Java, Maven, or a JVM-based framework.

#### Scenario: Install the non-Java default agent

- **WHEN** a user installs the embedded agent bundle
- **THEN** the installer copies `robot-no-java.md` with the existing agents
- **AND** the embedded agent inventory lists `robot-no-java`
- **AND** active agent sources are owned by `plinth-agents-generator`, not `plinth-skills-generator`

### Requirement: Tech lead non-Java routing

`robot-tech-lead` MUST route non-Java implementation work to `@robot-no-java` while preserving existing Java, Spring Boot, Quarkus, and Micronaut routing.

#### Scenario: Delegate a non-Java issue

- **GIVEN** an issue, plan, or OpenSpec task list names a non-Java stack or lacks Java/JVM scope
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-no-java`
- **AND** it does not delegate the work to `@robot-java-coder` by default
- **AND** agent markdown assets that define routing are owned by `plinth-agents-generator`

#### Scenario: Delegate plain Java work

- **GIVEN** an issue, plan, or OpenSpec task list uses Java, Maven, or framework-neutral JVM implementation
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-java-coder` unless Spring Boot, Quarkus, or Micronaut evidence selects a framework-specific coder
- **AND** agent markdown assets that define routing are owned by `plinth-agents-generator`

## Source and Derivation

- Source artifact: GitHub issue [#1036](https://github.com/jabrena/plinth/issues/1036).
- Existing specification: `documentation/openspec/specs/analysis-design-agents/spec.md`.
- Derivation direction: module extraction decision -> updated agent bundle ownership -> preserved installer and inventory behavior through `plinth-skills-generator` bridge.

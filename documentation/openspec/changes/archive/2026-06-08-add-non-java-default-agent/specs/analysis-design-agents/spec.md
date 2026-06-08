## ADDED Requirements

### Requirement: Non-Java default implementation agent

The embedded agent bundle SHALL include `robot-no-java` as the default implementation delegate for issue, plan, or OpenSpec work whose authoritative artifacts do not use Java, Maven, or a JVM-based framework.

#### Scenario: Install the non-Java default agent

- **WHEN** a user installs the embedded agent bundle
- **THEN** the installer copies `robot-no-java.md` with the existing agents
- **AND** the embedded agent inventory lists `robot-no-java`

### Requirement: Tech lead non-Java routing

`robot-tech-lead` MUST route non-Java implementation work to `@robot-no-java` while preserving existing Java, Spring Boot, Quarkus, and Micronaut routing.

#### Scenario: Delegate a non-Java issue

- **GIVEN** an issue, plan, or OpenSpec task list names a non-Java stack or lacks Java/JVM scope
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-no-java`
- **AND** it does not delegate the work to `@robot-java-coder` by default

#### Scenario: Delegate plain Java work

- **GIVEN** an issue, plan, or OpenSpec task list uses Java, Maven, or framework-neutral JVM implementation
- **WHEN** `robot-tech-lead` selects an implementation delegate
- **THEN** it delegates to `@robot-java-coder` unless Spring Boot, Quarkus, or Micronaut evidence selects a framework-specific coder

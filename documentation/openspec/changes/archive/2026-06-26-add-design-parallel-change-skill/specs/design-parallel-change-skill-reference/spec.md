## ADDED Requirements

### Requirement: Parallel Change design skill

The repository MUST define `055-design-parallel-change` as a dedicated design skill for applying the Parallel Change pattern to database migration scenarios.

#### Scenario: Parallel Change skill identifier is standardized

- **GIVEN** maintainers implement Parallel Change guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `055-design-parallel-change`
- **AND** the skill references Martin Fowler's Parallel Change guidance
- **AND** the skill references DORA database change management guidance

#### Scenario: Parallel Change skill explains expand, migrate, and contract

- **GIVEN** a database migration changes schema shape or data interpretation
- **WHEN** the `055-design-parallel-change` skill recommends a safer migration approach
- **THEN** it explains the Expand step as adding the new column, table, index, constraint, or access path in a backward-compatible way while the old shape still works
- **AND** it explains the Migrate step as backfilling or dual-writing data, updating reads safely, and verifying old and new paths during rollout
- **AND** it explains the Contract step as removing the old column, table, relationship, constraint, or access path only after all deployed code and data have moved to the new shape

#### Scenario: Parallel Change skill is problem-led

- **GIVEN** a user asks whether a database migration should use Parallel Change
- **WHEN** the `055-design-parallel-change` skill evaluates the migration scenario
- **THEN** it recommends Parallel Change for compatibility-window risks such as column renames, type changes, large-table backfills, relationship-table changes, enum or status transitions, timezone changes, default changes, or unique and index changes
- **AND** it explains when a simpler single migration is sufficient
- **AND** it describes tradeoffs such as extra migrations, temporary dual paths, rollout coordination, verification effort, and delayed cleanup

### Requirement: Relationship to framework-specific Flyway guidance

The Parallel Change design skill MUST complement existing Spring Boot, Quarkus, and Micronaut Flyway skills without replacing their framework-specific implementation guidance.

#### Scenario: Use design skill before framework implementation detail

- **GIVEN** a user needs to evolve a database schema safely
- **WHEN** the agent needs to decide the migration strategy before framework-specific implementation
- **THEN** it can use `055-design-parallel-change` for cross-framework pattern guidance
- **AND** it can use `313-frameworks-spring-db-migrations-flyway`, `413-frameworks-quarkus-db-migrations-flyway`, or `513-frameworks-micronaut-db-migrations-flyway` for framework-specific Flyway implementation guidance

#### Scenario: Existing Flyway references remain valid source material

- **GIVEN** the repository contains existing Flyway parallel-change references for Spring Boot, Quarkus, and Micronaut
- **WHEN** maintainers implement `055-design-parallel-change`
- **THEN** the new skill consolidates reusable pattern guidance from those references
- **AND** existing framework-specific Flyway guidance is preserved unless a direct inconsistency with the shared design guidance is found

### Requirement: Generator registration

The Parallel Change design skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Parallel Change design skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `055` registers reference `055-design-parallel-change`
- **AND** the skill entry uses explicit `skillId="055-design-parallel-change"`

#### Scenario: Generate local Parallel Change design skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/055-design-parallel-change/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

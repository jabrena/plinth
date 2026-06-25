## ADDED Requirements

### Requirement: Cross-framework Flyway migration safety guidance

The repository MUST improve the Spring Boot, Quarkus, and Micronaut Flyway skills with guidance that helps users avoid data-loss and production-safety migration mistakes.

#### Scenario: Framework Flyway skills include migration antipattern guidance

- **GIVEN** maintainers update the framework-specific Flyway skill references
- **WHEN** the generated local Flyway skills are inspected
- **THEN** `313-frameworks-spring-db-migrations-flyway` includes Flyway antipattern guidance
- **AND** `413-frameworks-quarkus-db-migrations-flyway` includes Flyway antipattern guidance
- **AND** `513-frameworks-micronaut-db-migrations-flyway` includes Flyway antipattern guidance
- **AND** the guidance addresses data-loss and data-misinterpretation risks in migration files

#### Scenario: Flyway antipattern guidance covers risky schema and data changes

- **GIVEN** a user asks for help adding or reviewing a Flyway migration
- **WHEN** a Flyway skill identifies migration risks
- **THEN** it warns about drop-and-add renames, type narrowing, column length reduction, unsafe `NOT NULL` defaults, default-value changes, destructive table or relationship-table rewrites, enum/status meaning changes, timestamp/timezone changes, destructive repeatable migrations, broad updates without precise `WHERE` clauses, and unique/index changes without duplicate checks
- **AND** it recommends checking existing production-like data before applying migrations that can lose, truncate, overwrite, or reinterpret data

### Requirement: Branch-ordering and out-of-order migration testing

The Flyway skills MUST include testing guidance that helps detect branch-ordering problems before migration files reach shared environments.

#### Scenario: Detect out-of-order migrations from different branches

- **GIVEN** multiple branches may add Flyway migration files concurrently
- **WHEN** the skill recommends migration verification
- **THEN** it includes tests or checks that detect duplicate version numbers
- **AND** it includes tests or checks that detect migrations that assume another branch migration already ran
- **AND** it includes tests or checks that avoid unsafe out-of-order migration behavior in different branches
- **AND** it treats `outOfOrder=true` as an exceptional operational decision rather than a default branch-conflict fix

### Requirement: Parallel Change technique guidance

The Flyway skills MUST explain Parallel Change as a database migration technique for backward-compatible schema evolution.

#### Scenario: Explain expand, migrate, contract

- **GIVEN** a migration changes schema shape or data interpretation
- **WHEN** a Flyway skill recommends a safer migration technique
- **THEN** it explains the Expand step as adding the new column, table, index, or constraint in a backward-compatible way while the old shape still works
- **AND** it explains the Migrate step as backfilling or dual-writing data, updating reads safely, and verifying old and new paths during rollout
- **AND** it explains the Contract step as removing the old column, table, or access path only after all deployed code and data have moved to the new shape
- **AND** it applies the technique to Flyway scenarios such as column renames, type changes, large-table backfills, or relationship-table changes

### Requirement: Migration verification beyond application startup

The Flyway skills MUST recommend verification that proves migration behavior and data preservation, not only that the application context starts.

#### Scenario: Verify real migration chain and data preservation

- **GIVEN** a Flyway migration changes schema or data
- **WHEN** the skill recommends tests
- **THEN** it recommends running the full Flyway chain from an empty database using the real production dialect when feasible
- **AND** it recommends running risky migrations from a previous-release schema or data snapshot
- **AND** it recommends assertions for data preservation across renames, backfills, defaults, enum/status changes, timezone changes, repeatable migrations, broad updates, and unique/index changes
- **AND** it prefers Testcontainers or framework-native real-database test support over H2 when production uses a specific SQL dialect

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

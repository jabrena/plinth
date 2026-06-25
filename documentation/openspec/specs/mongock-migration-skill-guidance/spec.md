# mongock-migration-skill-guidance Specification

## Purpose
TBD - created by archiving change improve-mongock-migration-guidance. Update Purpose after archive.
## Requirements
### Requirement: Cross-framework Mongock migration safety guidance

The repository MUST improve the Spring Boot, Quarkus, and Micronaut Mongock skills with guidance that helps users avoid MongoDB data-loss and production-safety migration mistakes.

#### Scenario: Framework Mongock skills include migration antipattern guidance

- **GIVEN** maintainers update the framework-specific Mongock skill references
- **WHEN** the generated local Mongock skills are inspected
- **THEN** `316-frameworks-spring-mongodb-migrations-mongock` includes Mongock antipattern guidance
- **AND** `416-frameworks-quarkus-mongodb-migrations-mongock` includes Mongock antipattern guidance
- **AND** `516-frameworks-micronaut-mongodb-migrations-mongock` includes Mongock antipattern guidance
- **AND** the guidance addresses MongoDB document-shape, idempotency, runner compatibility, lock, and verification risks

#### Scenario: Mongock antipattern guidance covers risky document migrations

- **GIVEN** a user asks for help adding or reviewing a Mongock migration
- **WHEN** a Mongock skill identifies migration risks
- **THEN** it warns about editing applied `@ChangeUnit` classes, mutable domain-model coupling, non-idempotent updates, broad unguarded `updateMany` operations, unsafe array appends, blind counter increments, hidden lock failures, long startup backfills, unverified transaction assumptions, and runner compatibility assumptions
- **AND** it recommends checking production-like MongoDB documents before applying migrations that can lose, duplicate, corrupt, or reinterpret document data

### Requirement: NoSQL document evolution technique guidance

The Mongock skills MUST explain expand, migrate, contract as a MongoDB document migration technique for backward-compatible NoSQL evolution.

#### Scenario: Explain document-shape expand, migrate, contract

- **GIVEN** a migration changes MongoDB document shape or data interpretation
- **WHEN** a Mongock skill recommends a safer migration technique
- **THEN** it explains the Expand step as adding optional fields, embedded structures, collections, or indexes while old readers and writers still work
- **AND** it explains the Migrate step as backfilling documents with idempotent predicates, tolerating or dual-writing old and new shapes, and verifying counts before and after the migration
- **AND** it explains the Contract step as removing old fields, obsolete embedded structures, unused indexes, old collections, or compatibility code only after all deployed code and existing documents have moved to the new shape
- **AND** it applies the technique to MongoDB scenarios such as field renames, scalar-to-array conversions, embedded document changes, new required fields, status value changes, and index rollout

### Requirement: Framework-compatible Mongock runner guidance

The Mongock skills MUST recommend runner, driver, configuration, and execution choices that are compatible with the active framework and MongoDB client model.

#### Scenario: Spring Boot Mongock guidance distinguishes Boot 3 and Boot 4

- **GIVEN** a Spring Boot project needs Mongock migrations
- **WHEN** `316-frameworks-spring-mongodb-migrations-mongock` recommends dependencies and runner wiring
- **THEN** it verifies the Spring Boot major version before selecting a runner
- **AND** it recommends `mongock-standalone` plus `mongodb-sync-v4-driver` for Spring Boot 4.x
- **AND** it warns against `mongock-springboot-v3` on Spring Boot 4.x
- **AND** it shows a `@ChangeUnit` pattern based on stable MongoDB collection operations rather than mutable repositories or domain records

#### Scenario: Quarkus Mongock guidance verifies Quarkiverse compatibility

- **GIVEN** a Quarkus project needs Mongock migrations
- **WHEN** `416-frameworks-quarkus-mongodb-migrations-mongock` recommends dependencies and runner wiring
- **THEN** it verifies `quarkus-mongock` compatibility with the active Quarkus platform before applying changes
- **AND** it documents `quarkus.mongock.*` configuration and startup versus controlled execution trade-offs
- **AND** it warns about default-client and CDI injection limitations where applicable
- **AND** it shows a `@ChangeUnit` pattern that uses stable MongoDB collection operations

#### Scenario: Micronaut Mongock guidance uses verified runner wiring

- **GIVEN** a Micronaut project needs Mongock migrations
- **WHEN** `516-frameworks-micronaut-mongodb-migrations-mongock` recommends dependencies and runner wiring
- **THEN** it verifies whether a compatible Micronaut-specific runner exists before recommending one
- **AND** it documents the standalone runner pattern when no compatible runner is available
- **AND** it documents `mongodb.database` configuration and startup versus controlled execution trade-offs
- **AND** it shows a `@ChangeUnit` pattern based on `MongoDatabase` and stable MongoDB collection operations

### Requirement: Mongock verification beyond application startup

The Mongock skills MUST recommend verification that proves migration behavior, changelog execution, and document changes, not only that the application context starts.

#### Scenario: Verify real Mongock execution and document side effects

- **GIVEN** a Mongock migration changes MongoDB documents, collections, or indexes
- **WHEN** the skill recommends tests
- **THEN** it recommends running Mongock against real MongoDB infrastructure through Testcontainers, Quarkus Dev Services, or framework-native test support where feasible
- **AND** it recommends seeding pre-migration documents that represent old production shapes
- **AND** it recommends asserting `mongockChangeLog` contains `EXECUTED` records for expected change IDs
- **AND** it recommends asserting at least one physical side effect such as field backfill, index creation, collection creation, or document-shape conversion
- **AND** it recommends idempotency checks for migrations whose predicates must make reruns safe

### Requirement: Mongock acceptance scenario coverage

The repository MUST update Gherkin acceptance scenarios and prompt inventory entries so the improved Mongock guidance is validated for every affected skill.

#### Scenario: Acceptance scenarios cover improved Mongock guidance

- **GIVEN** the Mongock skill references are improved
- **WHEN** the related Gherkin acceptance files are inspected
- **THEN** `316-frameworks-spring-mongodb-migrations-mongock.feature` expects the Spring Boot Mongock skill to include NoSQL migration-safety guidance
- **AND** `416-frameworks-quarkus-mongodb-migrations-mongock.feature` expects the Quarkus Mongock skill to include NoSQL migration-safety guidance
- **AND** `516-frameworks-micronaut-mongodb-migrations-mongock.feature` expects the Micronaut Mongock skill to include NoSQL migration-safety guidance
- **AND** `acceptance-tests-prompts-skills.md` lists prompts for every changed Mongock skill

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope


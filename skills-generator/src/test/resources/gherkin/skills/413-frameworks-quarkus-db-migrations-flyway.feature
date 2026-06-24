Feature: Validate changes from usage of Quarkus Flyway migrations skill

Background:
  Given the skill "413-frameworks-quarkus-db-migrations-flyway"

@acceptance-test
Scenario: Add safe Flyway migration guidance to the Quarkus example
  Given the example project "examples/frameworks/quarkus"
  And the user request is "Add a Flyway migration to introduce order status_v2 without breaking existing orders"
  And the local generated skill path ".agents/skills/413-frameworks-quarkus-db-migrations-flyway"
  And the folder "examples/frameworks/quarkus" has no git changes
  When the skill ".agents/skills/413-frameworks-quarkus-db-migrations-flyway" is applied to the example project
  Then the skill reads "references/413-frameworks-quarkus-db-migrations-flyway.md"
  And the skill reads "references/413-frameworks-quarkus-db-migrations-flyway-antipatterns.md"
  And the skill reads "references/413-frameworks-quarkus-db-migrations-flyway-parallel-change.md"
  And "./mvnw compile" or "mvn compile" is run before applying Flyway or SQL changes
  And the skill inspects "pom.xml", Quarkus datasource configuration, "quarkus.flyway.*" configuration, migration locations, and JDBC or Panache persistence patterns
  And the skill recommends a versioned Flyway migration under "src/main/resources/db/migration"
  And the skill uses Parallel Change with expand, migrate, and contract steps when the change is data-sensitive
  And the skill recommends Quarkus Dev Services or Testcontainers verification against the production database dialect when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Avoid a breaking Flyway migration in the Quarkus example
  Given the example project "examples/frameworks/quarkus"
  And the user request is "Change order status values in place and drop the old status meaning in one Flyway migration"
  And the local generated skill path ".agents/skills/413-frameworks-quarkus-db-migrations-flyway"
  And the folder "examples/frameworks/quarkus" has no git changes
  When the skill ".agents/skills/413-frameworks-quarkus-db-migrations-flyway" reviews the requested migration
  Then the skill reads "references/413-frameworks-quarkus-db-migrations-flyway-antipatterns.md"
  And the skill reads "references/413-frameworks-quarkus-db-migrations-flyway-parallel-change.md"
  And the skill identifies the enum or status meaning change as a breaking change that can reinterpret production data
  And the skill requires explicit human review before proceeding with the breaking migration
  And the skill rejects "outOfOrder=true" as a default fix for branch-ordering problems
  And the skill recommends a safer forward-only Parallel Change sequence instead
  And the skill requires migration tests that assert preserved order status meaning from a previous-release data snapshot
  And any git changes produced during skill execution and verification are reset

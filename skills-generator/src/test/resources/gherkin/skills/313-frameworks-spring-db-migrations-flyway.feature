Feature: Validate changes from usage of Spring Boot Flyway migrations skill

Background:
  Given the skill "313-frameworks-spring-db-migrations-flyway"

@acceptance-test
Scenario: Add safe Flyway migration guidance to the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Add a Flyway migration to add a customer display name without breaking existing data"
  And the local generated skill path ".agents/skills/313-frameworks-spring-db-migrations-flyway"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/313-frameworks-spring-db-migrations-flyway" is applied to the example project
  Then the skill reads "references/313-frameworks-spring-db-migrations-flyway.md"
  And the skill reads "references/313-frameworks-spring-db-migrations-flyway-antipatterns.md"
  And the skill reads "references/313-frameworks-spring-db-migrations-flyway-parallel-change.md"
  And "./mvnw compile" or "mvn compile" is run before applying Flyway or SQL changes
  And the skill inspects "pom.xml", Spring Boot version, "spring.flyway.*" configuration, migration locations, and JDBC or Spring Data JDBC persistence patterns
  And the skill recommends a versioned Flyway migration under "src/main/resources/db/migration"
  And the skill uses Parallel Change with expand, migrate, and contract steps when the change is data-sensitive
  And the skill recommends Testcontainers-backed verification against the production database dialect when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Avoid a breaking Flyway migration in the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Replace customers.full_name with customers.name by dropping the old column in one Flyway migration"
  And the local generated skill path ".agents/skills/313-frameworks-spring-db-migrations-flyway"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/313-frameworks-spring-db-migrations-flyway" reviews the requested migration
  Then the skill reads "references/313-frameworks-spring-db-migrations-flyway-antipatterns.md"
  And the skill reads "references/313-frameworks-spring-db-migrations-flyway-parallel-change.md"
  And the skill identifies the drop-and-add rename as a breaking change that can lose production data
  And the skill requires explicit human review before proceeding with the breaking migration
  And the skill rejects "outOfOrder=true" as a default fix for branch-ordering problems
  And the skill recommends a safer forward-only Parallel Change sequence instead
  And the skill requires migration tests that assert preserved customer names from a previous-release data snapshot
  And any git changes produced during skill execution and verification are reset

Feature: Validate changes from usage of Spring Boot Flyway migrations skill

Background:
  Given the skill "313-frameworks-spring-db-migrations-flyway"

@acceptance-test
Scenario: Apply safe Flyway migration changes to the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the project contains "pom.xml"
  And the project contains "src/main/resources/application.properties"
  And the project does not contain "src/main/resources/db/migration/V1__create_customers.sql"
  And the user request is "Apply Flyway to this Spring Boot app and add a safe customer display_name migration"
  And the local generated skill path ".agents/skills/313-frameworks-spring-db-migrations-flyway"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/313-frameworks-spring-db-migrations-flyway" is applied to the example project
  Then the skill reads "references/313-frameworks-spring-db-migrations-flyway.md"
  And the skill reads "references/313-frameworks-spring-db-migrations-flyway-antipatterns.md"
  And the skill reads "references/313-frameworks-spring-db-migrations-flyway-parallel-change.md"
  And "./mvnw compile" is run from "examples/frameworks/spring-boot" before applying Flyway or SQL changes
  And the skill inspects the Spring Boot parent version and dependencies in "examples/frameworks/spring-boot/pom.xml"
  And the skill inspects the existing application configuration in "examples/frameworks/spring-boot/src/main/resources/application.properties"
  And the skill applies Flyway dependencies to "examples/frameworks/spring-boot/pom.xml" using Spring Boot dependency management
  And the skill applies "spring.flyway.*" configuration to "examples/frameworks/spring-boot/src/main/resources/application.properties"
  And the skill creates "examples/frameworks/spring-boot/src/main/resources/db/migration/V1__create_customers.sql"
  And the migration creates a "customers" table and a nullable or backfilled "display_name" column without dropping existing data
  And the skill uses Parallel Change with expand, migrate, and contract steps when the change is data-sensitive
  And the skill adds or updates a Spring Boot migration test that runs Flyway against the example application context
  And the skill recommends Testcontainers-backed verification when the example app has a production database dialect configured
  And "./mvnw clean verify" is run from "examples/frameworks/spring-boot" after improvements
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

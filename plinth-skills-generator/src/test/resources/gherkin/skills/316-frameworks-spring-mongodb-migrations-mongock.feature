Feature: Validate changes from usage of Spring Boot Mongock migrations skill

Background:
  Given the skill "316-frameworks-spring-mongodb-migrations-mongock"

@acceptance-test
Scenario: Add Mongock migration guidance to the Spring Boot example
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Add Mongock migrations for Spring Data MongoDB"
  And the local generated skill path ".agents/skills/316-frameworks-spring-mongodb-migrations-mongock"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/316-frameworks-spring-mongodb-migrations-mongock" is applied to the example project
  Then the skill reads "references/316-frameworks-spring-mongodb-migrations-mongock.md"
  And the skill reads "references/316-frameworks-spring-mongodb-migrations-mongock-antipatterns.md"
  And the skill reads "references/316-frameworks-spring-mongodb-migrations-mongock-parallel-change.md"
  And "./mvnw compile" or "mvn compile" is run before applying Mongock or MongoDB migration changes
  And the skill stops without changes if compilation fails
  And the skill inspects "pom.xml", Spring Boot version, MongoDB configuration, and Spring Data MongoDB persistence patterns
  And the skill selects Mongock runner, driver, and BOM coordinates compatible with the active Spring Data MongoDB generation
  And the skill configures "mongock.migration-scan-package" or an equivalent project-local migration scan package
  And the skill creates or recommends "@ChangeUnit" migrations with "@Execution", rollback hooks, explicit ordering, and idempotent operations
  And the skill warns about Mongock antipatterns including applied ChangeUnit edits, domain-model coupling, non-idempotent updates, long startup backfills, hidden lock failures, and tests that only verify startup
  And the skill explains MongoDB parallel change as expand, migrate, contract for document-shape changes, field renames, embedded structures, required fields, status values, and index rollout
  And the skill documents lock, transaction, startup execution, and forward-only rollout trade-offs
  And the skill adds or recommends Testcontainers-backed MongoDB migration verification when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

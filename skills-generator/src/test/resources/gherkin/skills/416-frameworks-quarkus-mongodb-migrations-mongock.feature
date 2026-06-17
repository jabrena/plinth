Feature: Validate changes from usage of Quarkus Mongock migrations skill

Background:
  Given the skill "416-frameworks-quarkus-mongodb-migrations-mongock"

@acceptance-test
Scenario: Add Mongock migration guidance to the Quarkus example
  Given the example project "examples/frameworks/quarkus"
  And the user request is "Add Mongock migrations for Quarkus MongoDB"
  And the local generated skill path ".agents/skills/416-frameworks-quarkus-mongodb-migrations-mongock"
  And the folder "examples/frameworks/quarkus" has no git changes
  When the skill ".agents/skills/416-frameworks-quarkus-mongodb-migrations-mongock" is applied to the example project
  Then the skill reads "references/416-frameworks-quarkus-mongodb-migrations-mongock.md"
  And "./mvnw compile" or "mvn compile" is run before applying Mongock or MongoDB migration changes
  And the skill stops without changes if compilation fails
  And the skill inspects "pom.xml", Quarkus MongoDB configuration, and Quarkus MongoDB persistence patterns
  And the skill configures the Quarkiverse "quarkus-mongock" extension when compatible
  And the skill configures "quarkus.mongock.*" settings including migration scan behavior and "migrate-at-start" trade-offs
  And the skill creates or recommends "@ChangeUnit" migrations with "@Execution", rollback hooks, explicit ordering, and idempotent operations
  And the skill verifies default-client constraints before applying a migration design when multiple MongoDB clients are present
  And the skill adds or recommends Quarkus tests with Dev Services or Testcontainers MongoDB when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

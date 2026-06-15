Feature: Validate changes from usage of Micronaut Mongock migrations skill

Background:
  Given the skill "516-frameworks-micronaut-mongodb-migrations-mongock"

@acceptance-test
Scenario: Add Mongock migration guidance to the Micronaut example
  Given the example project "examples/frameworks/micronaut"
  And the user request is "Add Mongock migrations for Micronaut Data MongoDB"
  And the local generated skill path ".agents/skills/516-frameworks-micronaut-mongodb-migrations-mongock"
  And the folder "examples/frameworks/micronaut" has no git changes
  When the skill ".agents/skills/516-frameworks-micronaut-mongodb-migrations-mongock" is applied to the example project
  Then the skill reads "references/516-frameworks-micronaut-mongodb-migrations-mongock.md"
  And "./mvnw compile" or "mvn compile" is run before applying Mongock or MongoDB migration changes
  And the skill stops without changes if compilation fails
  And the skill inspects "pom.xml", Micronaut MongoDB configuration, and Micronaut Data MongoDB persistence patterns
  And the skill selects Mongock runner and driver coordinates compatible with the Micronaut project
  And the skill wires migration execution through Micronaut beans or documents the standalone runner trade-off when required
  And the skill configures migration scan packages and startup versus controlled-job execution policy
  And the skill creates or recommends "@ChangeUnit" migrations with "@Execution", rollback hooks, explicit ordering, and idempotent operations
  And the skill adds or recommends "@MicronautTest" verification with Testcontainers MongoDB when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

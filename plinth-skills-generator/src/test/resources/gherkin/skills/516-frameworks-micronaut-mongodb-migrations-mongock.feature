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
  And the skill reads "references/516-frameworks-micronaut-mongodb-migrations-mongock-antipatterns.md"
  And the skill reads "references/516-frameworks-micronaut-mongodb-migrations-mongock-parallel-change.md"
  And "./mvnw compile" or "mvn compile" is run before applying Mongock or MongoDB migration changes
  And the skill stops without changes if compilation fails
  And the skill inspects "pom.xml", Micronaut MongoDB configuration, and Micronaut Data MongoDB persistence patterns
  And the skill selects Mongock runner and driver coordinates compatible with the Micronaut project
  And the skill wires migration execution through Micronaut beans or documents the standalone runner trade-off when required
  And the skill configures migration scan packages and startup versus controlled-job execution policy
  And the skill avoids deprecated Mongock transaction APIs such as "setTransactionEnabled(false)"
  And the skill creates or recommends "@ChangeUnit" migrations with "@Execution", rollback hooks, explicit ordering, and idempotent operations
  And the skill warns about Mongock antipatterns including applied ChangeUnit edits, Micronaut Data coupling, non-idempotent updates, test property gaps, hidden lock failures, and tests that only verify startup
  And the skill explains MongoDB parallel change as expand, migrate, contract for document-shape changes, field renames, embedded structures, required fields, status values, and index rollout
  And the skill adds or recommends "@MicronautTest" verification with Testcontainers MongoDB when feasible
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset

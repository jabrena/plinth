Feature: Validate changes from usage of Parallel Change design skill

Background:
  Given the skill "055-design-parallel-change"

@acceptance-test
Scenario: Choose Parallel Change before framework-specific Flyway implementation
  Given the user request is "Plan a safe database migration that renames customers.full_name to customers.display_name in a Spring Boot app"
  And the local generated skill path ".agents/skills/055-design-parallel-change"
  When the skill ".agents/skills/055-design-parallel-change" is applied to the database migration request
  Then the skill reads "references/055-design-parallel-change.md"
  And the guidance references Martin Fowler's Parallel Change guidance
  And the guidance references DORA database change management guidance
  And the analysis decides whether the migration needs Parallel Change before recommending framework-specific Flyway implementation details
  And the recommendation explains Expand as adding the new database shape while the old shape still works
  And the recommendation explains Migrate as backfilling or dual-writing data and verifying old and new paths during rollout
  And the recommendation explains Contract as removing the old shape only after deployed code and data have moved to the new shape
  And the recommendation includes a safe column rename sequence that preserves existing data
  And the recommendation routes Spring Boot implementation details to "313-frameworks-spring-db-migrations-flyway"
  And the recommendation reports verification evidence, cleanup trigger, tradeoffs, skipped checks, and remaining risks

@acceptance-test
Scenario: Compare Parallel Change against a simpler migration
  Given the user request is "Should this migration use Parallel Change: add a nullable marketing_opt_in column that no deployed code reads yet?"
  And the local generated skill path ".agents/skills/055-design-parallel-change"
  When the skill ".agents/skills/055-design-parallel-change" evaluates the migration strategy
  Then the skill reads "references/055-design-parallel-change.md"
  And the analysis classifies schema shape, data meaning, old and new application overlap, rollout style, and rollback expectation
  And the recommendation explains when a simpler single migration is sufficient
  And the recommendation avoids unnecessary Parallel Change ceremony when the change is additive and compatible
  And the recommendation still names verification appropriate to the database risk
  And the recommendation keeps framework-specific Flyway configuration guidance out of scope unless implementation is requested

@acceptance-test
Scenario: Cover risky database migration examples
  Given the user request is "Review migration risks for status value reinterpretation, a large-table backfill, a relationship table rewrite, and a new unique email rule"
  And the local generated skill path ".agents/skills/055-design-parallel-change"
  When the skill ".agents/skills/055-design-parallel-change" reviews the risky database migration examples
  Then the skill reads "references/055-design-parallel-change.md"
  And the guidance covers type change or data reinterpretation with explicit old-to-new meaning
  And the guidance covers large-table backfill with chunking, row-count expectations, or operational monitoring
  And the guidance covers relationship-table change without dropping existing links before verification
  And the guidance covers index or uniqueness change with duplicate detection and online or concurrent creation when supported
  And the guidance describes tradeoffs such as extra migrations, temporary dual paths, rollout coordination, verification effort, and delayed cleanup

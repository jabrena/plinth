Feature: Validate changes from usage of SQL technology skill

Background:
  Given the skill "704-technologies-sql"

@acceptance-test
Scenario: Review PostgreSQL schema and migration files without framework coupling
  Given the SQL schema file "examples/infrastructure/postgresql/V1__sakila_schema.sql"
  And the SQL seed migration file "examples/infrastructure/postgresql/V2__sakila_film_data.sql"
  And the target database dialect is "PostgreSQL"
  And the user request is "Review SQL schema, migrations, indexes, and transaction risks"
  And the local generated skill path ".agents/skills/704-technologies-sql"
  And the folder "examples/infrastructure/postgresql" has no git changes
  When the skill ".agents/skills/704-technologies-sql" is applied to the SQL files
  Then the skill reads "references/704-technologies-sql.md"
  And the skill keeps recommendations at the SQL and database-design layer
  And the skill identifies dialect-specific PostgreSQL syntax and portability trade-offs
  And the skill reviews table, column, index, constraint, view, trigger, and routine naming where present
  And the skill reviews keys, constraints, timestamps, soft-delete strategy, normalization, and data type choices
  And the skill checks query readability, explicit column lists, joins, CTE usage, bind-parameter expectations, and execution-plan review needs
  And the skill reviews index strategy, foreign keys, filter and sort paths, composite indexes, and over-indexing risks
  And the skill reports security, transaction, migration, testing, and monitoring risks without switching to a Java framework skill
  And the skill asks a clarifying question before editing SQL if dialect, migration tool, or production constraints are missing and material
  And the folder "examples/infrastructure/postgresql" has no git changes unless the user explicitly requested SQL edits

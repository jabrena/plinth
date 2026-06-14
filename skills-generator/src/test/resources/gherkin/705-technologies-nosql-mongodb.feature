Feature: Validate changes from usage of MongoDB and NoSQL technology skill

Background:
  Given the skill "705-technologies-nosql-mongodb"

@acceptance-test
Scenario: Review MongoDB seed data and modeling choices without framework coupling
  Given the MongoDB seed data file "examples/infrastructure/mongodb/01-sakila-film-data.js"
  And the MongoDB infrastructure file "examples/infrastructure/mongodb/docker-compose.yml"
  And the user request is "Review MongoDB document modeling, indexes, and aggregation risks"
  And the local generated skill path ".agents/skills/705-technologies-nosql-mongodb"
  And the folder "examples/infrastructure/mongodb" has no git changes
  When the skill ".agents/skills/705-technologies-nosql-mongodb" is applied to the MongoDB files
  Then the skill reads "references/705-technologies-nosql-mongodb.md"
  And the skill keeps recommendations at the database-modeling and query layer
  And the skill identifies workload, cardinality, data volume, document growth, consistency, shard topology, and migration assumptions
  And the skill reviews aggregate boundaries, embedded documents, references, arrays, bounded growth, and stable identifiers
  And the skill recommends explicit document contracts and JSON Schema validation when unconstrained document growth is a risk
  And the skill reviews query predicates, projections, sort stability, pagination, collation, and read concern assumptions where applicable
  And the skill reviews compound indexes, covering queries, partial indexes, TTL indexes, unique constraints, and index lifecycle concerns
  And the skill reviews aggregation pipeline stage ordering, "$match" pushdown, "$project", "$lookup", "$unwind", "$group", and memory controls where applicable
  And the skill does not concatenate untrusted input into query documents, aggregation stages, JSON strings, JavaScript expressions, or "$where"
  And the folder "examples/infrastructure/mongodb" has no git changes unless the user explicitly requested MongoDB artifact edits

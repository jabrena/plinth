Feature: Validate changes from usage of Avoid Breaking Changes design skill

Background:
  Given the skill "056-design-avoid-breaking-changes"
  And the example project "examples/maven/maven-demo"
  And the folder "examples/maven/maven-demo" has no git changes

@acceptance-test
Scenario: Review quoted CSV field support for breaking-change risk
  Given the user request is "Review quoted CSV field support in CsvDataParser for breaking-change risks before implementation"
  And the local generated skill path ".agents/skills/056-design-avoid-breaking-changes"
  And the raw requirements are defined in "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the target class is "examples/maven/maven-demo/src/main/java/info/jab/exceptions/CsvDataParser.java"
  And the target test class is "examples/maven/maven-demo/src/test/java/info/jab/exceptions/CsvDataParserTest.java"
  When the skill ".agents/skills/056-design-avoid-breaking-changes" reviews the Java runtime behavior change
  Then the skill reads "references/056-design-avoid-breaking-changes.md"
  And the skill reads "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the skill inspects "CsvDataParser.java" and "CsvDataParserTest.java" before producing the compatibility report
  And the report identifies the raw requirements, target class, target test class, and generated skill reference as the source artifacts reviewed
  And the report checks runtime behavior, data format compatibility, parser error behavior, batch parsing behavior, tests, CI validation, and migration or release guidance when relevant
  And the report classifies findings as "BREAKING", "POTENTIALLY BREAKING", "NON-BREAKING", or "UNKNOWN"
  And the report highlights affected contracts for unquoted five-field records, quoted field parsing, invalid field counts, empty fields, invalid formats, field constraints, parse error reporting, and batch parsing with skipErrors
  And the report treats preserving current CsvDataParser behavior as a compatibility requirement while adding support for the raw requirement example with "Doe, Jane" as a single name field
  And the report provides actionable validation guidance for follow-up work
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

@acceptance-test
Scenario: Report a no-risk compatibility review
  Given the user request is "Check compatibility risk for a docs-only typo fix in a README paragraph"
  And the local generated skill path ".agents/skills/056-design-avoid-breaking-changes"
  When the skill ".agents/skills/056-design-avoid-breaking-changes" reviews the docs-only change
  Then the skill reads "references/056-design-avoid-breaking-changes.md"
  And the report explicitly states when no breaking-change risks are found
  And the report lists the compatibility surfaces reviewed
  And the report still recommends validation appropriate to the touched artifact type
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

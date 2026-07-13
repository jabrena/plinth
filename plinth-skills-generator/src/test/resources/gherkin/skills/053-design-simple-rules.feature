Feature: Validate changes from usage of simple design rules skill

Background:
  Given the skill "053-design-simple-rules"
  And the example project "examples/maven/maven-demo"
  And the folder "examples/maven/maven-demo" has no git changes

@acceptance-test
Scenario: Evaluate quoted CSV parser design options with ordered simple design rules
  Given the user request is "Apply simple design rules to choose between refactoring options for quoted CSV field support in CsvDataParser"
  And the local generated skill path ".agents/skills/053-design-simple-rules"
  And the raw requirements are defined in "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the target class is "examples/maven/maven-demo/src/main/java/info/jab/exceptions/CsvDataParser.java"
  And the target test class is "examples/maven/maven-demo/src/test/java/info/jab/exceptions/CsvDataParserTest.java"
  When the skill ".agents/skills/053-design-simple-rules" is applied to the Java design request
  Then the skill reads "references/053-design-simple-rules.md"
  And the skill reads "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the skill inspects "CsvDataParser.java" and "CsvDataParserTest.java" before recommending a design option
  And the analysis evaluates the rules in this order: passes the tests, reveals intention, has no duplication, and has the fewest elements
  And the analysis explains that passing tests comes before design cleanup
  And the analysis identifies existing CsvDataParser tests, quoted-field tests, characterization coverage, build checks, or manual verification as the correctness boundary
  And the analysis explains that clarity and revealed intention in delimiter handling take priority over abstraction for its own sake
  And the analysis reduces duplication in parsing or validation only when the result preserves or improves clarity
  And the analysis treats fewer elements as a final simplification pressure after correctness, clarity, and duplication have been addressed
  And the recommendation names the selected Java design or refactoring option for quoted CSV parsing with the rule-order tradeoff
  And the recommendation preserves current CsvDataParser behavior for unquoted five-field records, validation failures, and batch error handling
  And the recommendation covers the raw requirement example with "Doe, Jane" as a single name field
  And the recommendation reports skipped checks, missing tests, or remaining risks when verification is incomplete
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

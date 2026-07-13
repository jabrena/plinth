Feature: Validate changes from usage of TDD design skill

Background:
  Given the skill "054-design-tdd"
  And the example project "examples/maven/maven-demo"
  And the folder "examples/maven/maven-demo" has no git changes

@acceptance-test
Scenario: Guide quoted CSV field support with the TDD workflow
  Given the user request is "Apply TDD to implement quoted CSV field support in CsvDataParser"
  And the local generated skill path ".agents/skills/054-design-tdd"
  And the raw requirements are defined in "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the target class is "examples/maven/maven-demo/src/main/java/info/jab/exceptions/CsvDataParser.java"
  And the target test class is "examples/maven/maven-demo/src/test/java/info/jab/exceptions/CsvDataParserTest.java"
  When the skill ".agents/skills/054-design-tdd" is applied to the Java implementation request
  Then the skill reads "references/054-design-tdd.md"
  And the skill reads "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the skill inspects "CsvDataParser.java" and "CsvDataParserTest.java" before recommending edits
  And the analysis maintains or refines a list of candidate test cases for quoted CSV fields, unquoted records, validation failures, and batch error handling
  And the analysis identifies the next useful quoted-field behavior or test case before writing production code
  And the analysis writes or describes a failing test first for the raw requirement example with "Doe, Jane" as a single name field
  And the analysis explains that the test-first step clarifies CsvDataParser inputs, outputs, error behavior, or usage
  And the implementation guidance limits production code to the smallest functional code needed to pass the selected quoted-field test
  And the guidance adds newly discovered quoted-field behaviors or edge cases to the test list instead of broadening the current cycle
  And the guidance includes a refactoring step for new and existing code after the selected test passes
  And the guidance keeps relevant CsvDataParser tests green while refactoring
  And the guidance preserves current CsvDataParser behavior for unquoted five-field records, validation failures, and batch error handling
  And the recommendation reports skipped checks, missing tests, or remaining risks when verification is incomplete
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

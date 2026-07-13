Feature: Validate changes from usage of two-step change method skill

Background:
  Given the skill "051-design-two-steps-methods"
  And the example project "examples/maven/maven-demo"
  And the folder "examples/maven/maven-demo" has no git changes

@acceptance-test
Scenario: Add quoted CSV field support with the two-step method
  Given the user request is "Apply the two-step change method to support quoted CSV fields in CsvDataParser"
  And the local generated skill path ".agents/skills/051-design-two-steps-methods"
  And the raw requirements are defined in "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the target class is "examples/maven/maven-demo/src/main/java/info/jab/exceptions/CsvDataParser.java"
  And the target test class is "examples/maven/maven-demo/src/test/java/info/jab/exceptions/CsvDataParserTest.java"
  When the skill ".agents/skills/051-design-two-steps-methods" is applied to the Java change request
  Then the skill reads "references/051-design-two-steps-methods.md"
  And the skill reads "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the skill inspects "CsvDataParser.java" and "CsvDataParserTest.java" before recommending edits
  And the analysis states the intended behavior change from the raw requirements before proposing refactoring
  And the analysis identifies the design obstacle from the current CsvDataParser implementation
  And the prepared code still preserves current CsvDataParser behavior for unquoted five-field records, validation failures, and batch error handling before quoted-field support is introduced
  And the behavior-changing code accepts the raw requirement example with "Doe, Jane" as a single name field
  And the focused tests prove the quoted-field behavior without weakening existing invalid field count, empty field, invalid format, constraint, or parse error behavior
  And verification is performed after the behavior-preserving preparation and again after the quoted-field behavior change
  And the skill keeps Java, framework, persistence, messaging, API, or testing skill handoffs inside the two-step sequence when those details are needed
  And the final report separates behavior-preserving preparation, preparation verification, quoted-field behavior change, behavior-change verification, assumptions, and risks
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

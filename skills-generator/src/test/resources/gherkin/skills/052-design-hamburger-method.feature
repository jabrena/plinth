Feature: Validate changes from usage of hamburger method design skill

Background:
  Given the skill "052-design-hamburger-method"
  And the example project "examples/maven/maven-demo"
  And the folder "examples/maven/maven-demo" has no git changes

@acceptance-test
Scenario: Split quoted CSV field support into smallest useful vertical slices
  Given the user request is "Apply the Hamburger Method to split quoted CSV field support in CsvDataParser into tracked vertical slices"
  And the local generated skill path ".agents/skills/052-design-hamburger-method"
  And the raw requirements are defined in "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the target class is "examples/maven/maven-demo/src/main/java/info/jab/exceptions/CsvDataParser.java"
  And the target test class is "examples/maven/maven-demo/src/test/java/info/jab/exceptions/CsvDataParserTest.java"
  When the skill ".agents/skills/052-design-hamburger-method" is applied to the Java slicing request
  Then the skill reads "references/052-design-hamburger-method.md"
  And the skill reads "examples/requirements/csv-data-parser/quoted-csv-fields.md"
  And the skill inspects "CsvDataParser.java" and "CsvDataParserTest.java" before recommending slices
  And the analysis recognizes quoted CSV field support as risky delimiter-handling work that benefits from vertical slicing
  And the analysis identifies 3-6 functional or workflow layers that participate in delivering quoted-field value
  And each layer includes 4-5 implementation or quality options ordered from simplest acceptable option to richer options
  And the analysis shows the alternative options considered for every identified layer before choosing a slice
  And the analysis asks or answers "If this had to ship tomorrow, what would be the smallest useful version?"
  And the analysis filters options that are too costly, redundant, irreversible, or unnecessary for early learning from quoted-field support
  And the recommendation states which option is chosen from each relevant layer for the first slice
  And the recommendation explains why the chosen options are preferred over deferred alternatives using user value, cost, reversibility, and test signal
  And the recommendation composes one smallest useful end-to-end vertical slice that accepts the raw requirement example with "Doe, Jane" as a single name field
  And the recommendation does not present isolated technical layers as independently shippable stories
  And the recommendation preserves current CsvDataParser behavior for unquoted five-field records, validation failures, and batch error handling in every slice
  And the recommendation proposes follow-up vertical slices that incrementally improve quality, automation, robustness, reach, or observability for quoted CSV parsing
  And each proposed slice is checked for value, size, testability, deliverability, and issue-tracking suitability
  And any git changes produced under "examples/maven/maven-demo" during skill execution and verification are reset

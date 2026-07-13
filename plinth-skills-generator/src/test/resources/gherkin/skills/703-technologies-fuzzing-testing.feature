Feature: Validate changes from usage of Java fuzz testing skill

Background:
  Given the skill "703-technologies-fuzzing-testing"
  And the folder "examples" has no git changes

@acceptance-test
Scenario: Add CATS fuzz testing guidance for a Java API without unsafe downloads
  Given an OpenAPI contract file is available for a Java HTTP API
  And the user request is "Add CATS fuzz testing for this Java API and document local and CI execution"
  And the local generated skill path ".agents/skills/703-technologies-fuzzing-testing"
  And the folder "examples" has no git changes
  When the skill ".agents/skills/703-technologies-fuzzing-testing" is applied to the Java API fuzzing request
  Then the skill reads "references/703-technologies-fuzzing-testing.md"
  And the skill verifies project compilation before introducing fuzz testing changes or reports compilation failure as blocking
  And the skill requires an OpenAPI or equivalent API contract before configuring CATS
  And the skill asks the user for the OpenAPI contract path before running CATS and does not guess a default path
  And the skill verifies the user-provided contract file exists before executing "./run-cats-fuzz.sh --openapi"
  And the skill uses only a verified local "cats/cats.jar" or an approved prebuilt image for CATS execution
  And the skill copies the packaged "assets/cats.dockerfile" resource into "cats/Dockerfile" when local container execution is requested
  And the skill copies the packaged "scripts/run-cats-fuzz.sh" resource into "run-cats-fuzz.sh" without changing template behavior
  And the skill categorizes fuzzing scope by malformed input, missing fields, boundary limits, and schema constraint violations
  And the skill reviews CATS report output under "cats/cats-report/" before claiming contract violations or validation gaps
  And the skill records reproducible failure evidence for failed fuzz cases, unexpected HTTP status codes, response schema mismatches, and missing validation
  And the skill keeps recommendations at the API contract, fuzzing, and CI quality-gate layer unless the user explicitly requests framework implementation
  And the skill does not generate download-and-execute setup paths for unverified remote CATS binaries
  And the folder "examples" has no git changes unless the user explicitly requested fuzzing setup edits
  And any git changes produced during skill execution and verification are reset

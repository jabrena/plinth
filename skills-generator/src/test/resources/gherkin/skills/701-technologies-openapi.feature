Feature: Validate changes from usage of OpenAPI technology skill

Background:
  Given the skill "701-technologies-openapi"

@acceptance-test
Scenario: Fix a bad OpenAPI contract without framework coupling
  Given the bad OpenAPI contract file "examples/requirements/bad-openapi-spec/openapi.yaml"
  And the user request is "Use the OpenAPI skill to review examples/requirements/bad-openapi-spec/openapi.yaml and fix the contract issues with concrete YAML changes"
  And the local generated skill path ".agents/skills/701-technologies-openapi"
  When the skill ".agents/skills/701-technologies-openapi" is applied to the bad OpenAPI contract
  Then the skill reads "references/701-technologies-openapi.md"
  And the skill reviews the bad OpenAPI file for API metadata, versioning, paths, operationIds, request bodies, responses, and content types
  And the skill identifies the vague title and version, missing servers, missing tags, and absent operationIds
  And the skill identifies reusable schema issues in "Order" and "CreateOrderRequest", including required fields, formats, enums, money precision, nullability or optionality, and examples that do not match the declared schema
  And the skill identifies under-specified path, query, and header parameters, including the invalid "orderId" path parameter, pagination bounds, filtering enum values, sorting format, and correlation-id format
  And the skill identifies inconsistent error contracts for "400" and "404" and recommends Problem Details or an equivalent reusable error envelope with "application/problem+json"
  And the skill identifies the JWT security mismatch and proposes "components.securitySchemes" plus appropriate global or operation-level security requirements
  And the skill distinguishes additive changes from breaking changes in the file, including the changed "orderId" type, removed "PENDING" enum value, newly required "trackingNumber", and incompatible examples
  And the skill fixes or proposes concrete OpenAPI YAML snippets or precise edit lists for the bad file
  And the skill recommends validation, linting, or breaking-change checks without requiring one specific external tool for every project
  And the skill avoids framework-specific controller, server stub, or runtime OpenAPI wiring recommendations unless the user explicitly requests implementation help
  And the skill routes Spring Boot, Quarkus, or Micronaut implementation concerns to the appropriate framework REST skills when needed

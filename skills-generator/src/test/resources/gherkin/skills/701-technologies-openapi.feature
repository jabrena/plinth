Feature: Validate changes from usage of OpenAPI technology skill

Background:
  Given the skill "701-technologies-openapi"

@acceptance-test
Scenario: Review OpenAPI contract quality without framework coupling
  Given an OpenAPI contract or OpenAPI diff is provided by the user
  And the user request is "Review this OpenAPI contract for schema, parameter, error, security, example, and breaking-change risks"
  And the local generated skill path ".agents/skills/701-technologies-openapi"
  When the skill ".agents/skills/701-technologies-openapi" is applied to the OpenAPI contract or diff
  Then the skill reads "references/701-technologies-openapi.md"
  And the skill reviews API metadata, versioning, paths, operationIds, request bodies, responses, and content types
  And the skill identifies concrete issues in reusable schemas, required fields, formats, enums, nullability, and examples where present
  And the skill identifies under-specified path, query, or header parameters, including pagination, filtering, or sorting contracts where applicable
  And the skill identifies inconsistent error contracts and recommends Problem Details or an equivalent reusable error envelope with appropriate problem content types
  And the skill reviews security schemes, global security requirements, operation-level overrides, scopes, and optional-auth mismatches
  And the skill distinguishes additive changes from breaking changes such as removed paths, changed status semantics, newly required fields, incompatible enum changes, or schema type changes
  And the skill proposes concrete OpenAPI YAML or JSON snippets or precise edit lists to fix the findings
  And the skill recommends validation, linting, or breaking-change checks without requiring one specific external tool for every project
  And the skill avoids framework-specific controller, server stub, or runtime OpenAPI wiring recommendations unless the user explicitly requests implementation help
  And the skill routes Spring Boot, Quarkus, or Micronaut implementation concerns to the appropriate framework REST skills when needed

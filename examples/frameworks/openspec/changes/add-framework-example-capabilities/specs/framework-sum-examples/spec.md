## ADDED Requirements

### Requirement: Quarkus sum REST endpoint

The Quarkus framework example MUST expose `POST /api/v1/sum` as a REST endpoint
from the controller package. The endpoint MUST accept a JSON object with integer
properties `param1` and `param2`, compute their sum through an application
inbound port, and return the result.

#### Scenario: Sum endpoint returns the computed result

- **GIVEN** the Quarkus framework example is running
- **WHEN** a client sends `POST /api/v1/sum` with JSON body `{"param1":10,"param2":32}`
- **THEN** the response status is `200`
- **AND** the response body contains integer field `result`
- **AND** `result` is `42`

#### Scenario: Invalid sum request is rejected

- **WHEN** a client sends `POST /api/v1/sum` without one of the required integer fields
- **THEN** the response status is `400`
- **AND** the response body describes the validation failure

#### Scenario: Controller is located in controller package

- **WHEN** the Quarkus implementation is inspected
- **THEN** the sum endpoint controller is located in the Quarkus example's controller package

### Requirement: Quarkus implementation follows Hexagonal architecture

The Quarkus implementation MUST keep the sum business rule in a
framework-independent core and expose it to the REST adapter through an
application inbound port.

#### Scenario: Domain is framework independent

- **WHEN** the Quarkus domain package is inspected
- **THEN** it contains the sum business rule
- **AND** it does not import Quarkus, Jakarta REST, controller, or DTO packages

#### Scenario: Application exposes inbound port

- **WHEN** the Quarkus application package is inspected
- **THEN** it defines an inbound use-case port for calculating sums
- **AND** it provides an implementation that delegates the arithmetic rule to the domain package
- **AND** it does not depend on the REST controller or DTO packages

#### Scenario: REST controller is a driving adapter

- **WHEN** the Quarkus REST controller handles `POST /api/v1/sum`
- **THEN** it translates the HTTP request DTO into an application use-case call
- **AND** it translates the use-case result into the HTTP response DTO
- **AND** the arithmetic rule is not implemented directly in the controller

#### Scenario: Core behavior is tested without Quarkus boot

- **WHEN** domain or application tests for sum behavior run
- **THEN** they instantiate the core classes directly
- **AND** they do not require `@QuarkusTest`

### Requirement: Quarkus implementation targets Java 25 concurrency model

The Quarkus implementation MUST use Java 25 as its compile and runtime baseline,
MUST configure virtual-thread support explicitly, and MUST use Java structured
concurrency for related parallel calls introduced by the application use case.

#### Scenario: Java 25 is configured

- **WHEN** the Quarkus Maven configuration is inspected
- **THEN** it targets Java 25 for compilation
- **AND** test and runtime configuration are compatible with Java 25

#### Scenario: Virtual threads are configured

- **WHEN** the Quarkus application configuration is inspected
- **THEN** virtual-thread support is explicitly enabled
- **AND** the REST adapter runs on virtual threads when it performs blocking or parallel work

#### Scenario: Structured concurrency coordinates related parallel calls

- **WHEN** the application use case introduces related parallel calls
- **THEN** those calls are coordinated with Java structured concurrency
- **AND** failure of one subtask cancels or fails the bounded operation predictably
- **AND** interruption is propagated or restored rather than swallowed

#### Scenario: Structured concurrency preview is enabled when used

- **WHEN** Java 25 `StructuredTaskScope` is used
- **THEN** Java preview features are enabled for compile, test, and runtime execution
- **AND** verification commands exercise the preview-enabled configuration

### Requirement: Isolated OpenAPI example documents REST behavior

The OpenSpec change MUST include an isolated OpenAPI contract example for the
REST scenario, copied from the shared framework OpenAPI contract.

#### Scenario: Sum endpoint is documented

- **WHEN** `examples/frameworks/openspec/changes/add-framework-example-capabilities/examples/openapi.yaml` is inspected
- **THEN** it documents `POST /api/v1/sum`
- **AND** the request schema requires `param1` and `param2`
- **AND** the success response schema contains `result`

#### Scenario: Isolated OpenAPI example matches shared contract

- **WHEN** the REST contract is reviewed
- **THEN** the isolated OpenAPI example documents the same `POST /api/v1/sum` path, request schema, and response schema as `examples/frameworks/openapi.yaml`

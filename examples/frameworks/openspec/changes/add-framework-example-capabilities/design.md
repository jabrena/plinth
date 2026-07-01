## Context

The selected source scope is the REST scenario from `examples/frameworks/SPEC.md`.
It asks for a basic `POST /api/v1/sum` endpoint that receives an object with
integer properties `param1` and `param2`, adds them, and places the controller
in the controller package. This change targets only the Quarkus example and
requires the implementation to follow Hexagonal architecture.

The shared OpenAPI contract already documents `POST /api/v1/sum`. This change
includes a copy of that contract under `examples/openapi.yaml` so the OpenSpec
change can be reviewed in isolation while `examples/frameworks/openapi.yaml`
remains the source contract used by tooling.

## Source Authority

- The REST scenario in `examples/frameworks/SPEC.md` is the source for requested
  behavior.
- `examples/frameworks/openapi.yaml` is the shared HTTP contract source.
- `examples/frameworks/openspec/changes/add-framework-example-capabilities/examples/openapi.yaml`
  is an isolated contract example copied from the shared HTTP contract for this
  change.
- OpenSpec artifacts derive from those sources and do not synchronize changes
  back into them automatically.

## Change Shape

This is the smallest useful Quarkus framework example slice: one public HTTP
endpoint, one request model, one response model, one controller package
placement rule, one Hexagonal use-case boundary, one shared OpenAPI contract,
and focused Quarkus tests.

## Hexagonal Architecture Design

Use a small package structure that makes dependency direction visible without
creating empty layer theater:

```text
info.jab.ms
├── MainApplication
├── domain
│   └── SumCalculator
├── application
│   ├── CalculateSumService
│   └── port
│       └── in
│           ├── CalculateSumCommand
│           └── CalculateSumUseCase
├── adapter
│   └── in
│       └── rest
│           └── controller
│               ├── SumController
│               └── dto
│                   ├── SumRequest
│                   └── SumResponse
```

Dependency direction:

- `MainApplication` is a thin Quarkus bootstrap entry point; it does not
  orchestrate use cases or contain business behavior.
- `domain` contains the arithmetic rule and imports only Java/domain types.
- `application` contains the inbound port, command, and use-case implementation;
  it depends on `domain` and its own port contracts, not adapters.
- `adapter.in.rest.controller` is the REST driving adapter. It translates HTTP
  requests into `CalculateSumCommand`, calls `CalculateSumUseCase`, and
  translates the result into the HTTP response.
- `adapter.in.rest.controller.dto` contains request/response DTOs scoped to the
  REST adapter boundary; DTOs are not shared with domain or application code.
- No outbound port or driven adapter is required for this slice because the sum
  rule has no persistence, messaging, filesystem, or external HTTP dependency.
- Dependency direction is outward-to-inward: REST adapter -> application inbound
  port -> application use case -> domain.
- `domain` and `application` do not import Quarkus, Jakarta REST, adapter,
  controller, or DTO packages.

## Java 25 and Execution Model

The Quarkus implementation targets Java 25. The sum rule is synchronous and does
not require parallel orchestration or Java preview concurrency APIs.

Execution decisions:

- Configure the Quarkus module for Java 25.
- Configure Quarkus virtual-thread support explicitly in
  `application.properties` with `quarkus.virtual-threads.enabled=true`.
- Keep the domain and application sum flow simple and synchronous.

## Two-Step Implementation Guidance

1. Make the change easy with behavior-preserving preparation. Inspect the
   current Quarkus example structure, characterize existing REST behavior, and
   confirm the isolated OpenAPI example matches the shared `openapi.yaml`
   contract.
2. Make the REST behavior change only after the baseline is clear. Add or
   adjust the Hexagonal core, wire the Quarkus controller adapter to the
   inbound use-case port, keep request validation at the REST boundary, and
   verify `10 + 32 = 42` plus invalid-request behavior.

Do not add other scenarios from `SPEC.md` in this change.

## Framework Technology Decisions

- Quarkus uses Quarkus REST or Jakarta HTTP endpoint conventions.
- Quarkus Bean Validation validates request DTOs at the REST adapter boundary.
- CDI wires the REST driving adapter to the inbound use-case implementation
  without moving framework annotations into domain code.
- Java 25 is the runtime and compilation baseline for the Quarkus example.
- Quarkus virtual-thread support is enabled explicitly in application
  configuration.
- The domain/application core remains framework-independent and can be tested
  without booting Quarkus.

## Testing Strategy

- RIGHT-BICEP: assert right results for `10 + 32 = 42`, error behavior for
  missing required fields, and negative or zero integer inputs.
- CORRECT: cover request schema conformance, existence of required fields, and
  numeric range expectations documented by the OpenAPI `int32` contract.
- A-TRIP: keep use-case tests automatic, repeatable, independent, and free from
  Quarkus boot; keep REST adapter tests focused on routing, validation,
  serialization, and status codes.
- Hexagonal boundary verification can be a lightweight package/import review or
  optional architecture test if the project later chooses to add an
  architecture-test dependency.

## Compatibility Notes

- `POST /api/v1/sum` is the only endpoint in scope.
- The shared `examples/frameworks/openapi.yaml` remains the source HTTP
  contract consumed by the existing CATS scripts.
- The copied `examples/openapi.yaml` inside the OpenSpec change is included for
  isolated review and must be kept aligned if the REST contract changes during
  this OpenSpec change.
- Spring Boot and Micronaut implementations are intentionally not changed by
  this OpenSpec change.

## Open Questions

None for the REST scenario conversion.

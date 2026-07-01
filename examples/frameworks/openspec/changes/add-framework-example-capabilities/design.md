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
├── domain
│   └── SumCalculator
├── application
│   ├── CalculateSumService
│   └── port
│       └── in
│           ├── CalculateSumCommand
│           └── CalculateSumUseCase
├── controller
│   └── SumController
└── dto
    ├── SumRequest
    └── SumResponse
```

Dependency direction:

- `domain` contains the arithmetic rule and imports only Java/domain types.
- `application` defines the inbound port and orchestrates the domain rule.
- `controller` is the Quarkus REST driving adapter and calls the inbound port.
- `dto` contains HTTP request/response DTOs used by the REST adapter.
- `domain` and `application` do not import Quarkus, Jakarta REST, controller,
  or DTO packages.

## Java 25 and Concurrency Model

The Quarkus implementation targets Java 25. Virtual threads are production
ready, while Java 25 structured concurrency is a preview API. If the
implementation introduces related parallel calls inside the use case, it must
use Java structured concurrency (`StructuredTaskScope`) so subtasks succeed,
fail, cancel, and join as one bounded operation.

Concurrency decisions:

- Configure the Quarkus module for Java 25.
- Enable Java preview features in compile, test, and runtime execution when
  `StructuredTaskScope` is used.
- Configure Quarkus virtual-thread support explicitly in
  `application.properties` with `quarkus.virtual-threads.enabled=true`.
- Run the REST adapter on virtual threads when the endpoint executes blocking
  or related parallel work, using Quarkus REST virtual-thread support.
- Do not pool virtual threads.
- Keep structured-concurrency orchestration in the application layer or a
  framework-independent collaborator; the domain rule remains simple and
  synchronous.
- Preserve interruption and cancellation discipline: propagate or restore
  interruption and avoid swallowing failures from subtasks.

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
- CDI wires the controller adapter to the inbound use-case implementation.
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
- Concurrency: when structured concurrency is introduced, test success,
  failure, cancellation, and interruption behavior for related parallel calls.
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

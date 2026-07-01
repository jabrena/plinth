## Why

The Quarkus framework example needs a traceable capability definition for the
REST sum endpoint before implementation. `SPEC.md` describes the initial
`POST /api/v1/sum` endpoint in prose, while `openapi.yaml` documents the public
HTTP contract. Converting the REST scenario into OpenSpec makes the expected
behavior, Hexagonal architecture boundaries, contract example, and verification
steps explicit.

## What Changes

- Add an OpenSpec capability for the REST sum endpoint in the Quarkus framework
  example.
- Define the public `POST /api/v1/sum` contract with integer `param1` and
  `param2` request fields and an integer `result` response.
- Require the Quarkus REST controller to live in the controller package as a
  driving adapter.
- Require the sum behavior to be implemented with Hexagonal architecture:
  framework-independent domain/application core, an inbound use-case port, and
  a Quarkus REST adapter that depends inward.
- Require Java 25 as the implementation baseline.
- Require Quarkus virtual-thread support to be configured explicitly for the
  REST adapter execution model.
- Require any related parallel calls in the application core to use Java 25
  structured concurrency rather than ad hoc executor or `CompletableFuture`
  orchestration.
- Include an isolated OpenAPI contract example in this change at
  `examples/openapi.yaml`.
- Keep Quarkus implementation choices framework-native while preserving the
  shared HTTP contract.

## Capabilities

### New Capabilities

- `framework-sum-examples`: Defines the Quarkus REST sum endpoint, Hexagonal
  architecture boundaries, OpenAPI contract example, and verification
  expectations.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: REST scenario in `examples/frameworks/SPEC.md`.
- Shared HTTP contract source: `examples/frameworks/openapi.yaml`.
- Isolated contract example: `examples/frameworks/openspec/changes/add-framework-example-capabilities/examples/openapi.yaml`.
- Derivation direction: framework example source artifacts -> OpenSpec change
  artifacts -> Quarkus implementation and tests.

## Change Boundary Assessment

This is one OpenSpec change because the selected source scope describes one
atomic REST capability: a public `POST /api/v1/sum` endpoint backed by a shared
OpenAPI contract. The implementation target is Quarkus only. Other scenarios
and framework implementations from `SPEC.md` are intentionally out of scope for
this change.

## Compatibility Review

The change is additive for the example suite. The public sum endpoint is the
only runtime behavior in scope, and the shared OpenAPI contract remains the
contract consumed by existing CATS fuzz scripts. The isolated OpenAPI copy is a
review example for this change, not a second source of truth.

## Impact

An implementation would affect the Quarkus example application, Java/Maven
runtime configuration, its controller adapter, domain/application packages,
tests, and the existing CATS validation workflow when the Quarkus app is used
as the runnable target.

# US-001 A2 Domain Service Slice

## Problem Statement

The implementation plan defines `A2` for service-level RED/GREEN/refactor/verify work, but no independent OpenSpec change currently captures that scope.

Without a dedicated `A2` change, service behavior (Unicode aggregation, parallel execution boundaries, and deterministic merge behavior) can become underspecified relative to the execution plan.

## Proposed Solution

Create a dedicated OpenSpec change for `A2` that captures:
- Unit-test-first service and algorithm behavior.
- Aggregation implementation with immutable flow, `CompletableFuture`, and `BigInteger`.
- Service-layer observability and deterministic merge/concurrency refactoring.
- Milestone `A2` verification with prior API tests still green.

## Success Criteria

- `A2` tasks are represented as a single OpenSpec checklist.
- Spec deltas define service-level correctness and concurrency boundaries.
- Verification gate for `A2` (`./mvnw -q test`) is explicitly tracked.

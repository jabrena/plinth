# Design: Add Spring Boot Actuator

## Overview

Introduce Actuator into the existing Spring Boot service to provide standard operational endpoints, primarily health checks.

## Decisions

- Use Spring Boot Actuator (`spring-boot-starter-actuator`) as the observability baseline.
- Configure management endpoint exposure through application properties.
- Keep health endpoint publicly reachable in local/dev scenarios used by tests and examples.
- Do not modify domain logic or the aggregation algorithm.

## Endpoint expectations

- `GET /actuator/health` returns service health status.
- Optional additional endpoints can be exposed through configuration, but health is mandatory for this change.

## Risks and mitigations

- Risk: exposing too many management endpoints.
  - Mitigation: explicitly configure a minimal exposure list (at least `health`).
- Risk: accidental behavior changes in existing API.
  - Mitigation: run existing verification tests and keep endpoint additions isolated to configuration/dependencies.

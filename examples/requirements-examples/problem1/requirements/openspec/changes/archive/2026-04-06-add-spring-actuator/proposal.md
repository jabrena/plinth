# Change: Add Spring Boot Actuator

## Why

The `god-analysis-api` service needs operational visibility for runtime health and readiness checks. Adding Spring Boot Actuator enables standardized health endpoints used by local operations and deployment environments.

## What changes

- Add Spring Boot Actuator dependency to the implementation module.
- Expose actuator endpoints with explicit management configuration.
- Guarantee availability of health information for operators.
- Keep existing functional API behavior unchanged.

## Scope

- Capability affected: `god-analysis-api`
- Type: additive observability enhancement
- Backward compatibility: preserved for `/api/v1/gods/stats/sum`

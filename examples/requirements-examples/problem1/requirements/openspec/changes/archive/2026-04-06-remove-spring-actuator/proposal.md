# Change: Remove Spring Boot Actuator

## Why

The `god-analysis-api` service no longer requires operational visibility through Spring Boot Actuator endpoints. Removing this dependency simplifies the application, reduces the attack surface, and eliminates unused monitoring capabilities that are not needed for the current deployment model.

## What changes

- Remove Spring Boot Actuator dependency from the implementation module.
- Remove actuator endpoint configuration from application properties.
- Clean up any actuator-related management configuration.
- Keep existing functional API behavior unchanged.

## Scope

- Capability affected: `god-analysis-api`
- Type: dependency removal and simplification
- Backward compatibility: preserved for `/api/v1/gods/stats/sum`
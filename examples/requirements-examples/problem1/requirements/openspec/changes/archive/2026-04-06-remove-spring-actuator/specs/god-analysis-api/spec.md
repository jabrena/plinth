# Specification delta: `god-analysis-api` (remove-spring-actuator)

## ADDED Requirements

_None._

## MODIFIED Requirements

_None._

## REMOVED Requirements

### Requirement: Actuator health endpoint

The service no longer exposes a Spring Boot Actuator health endpoint at `/actuator/health`. Operators should use alternative monitoring approaches or the main API endpoints for service verification.

#### Scenario: Health endpoint is no longer available

- **WHEN** a client calls `GET /actuator/health`
- **THEN** the service responds with HTTP 404 (Not Found) as the endpoint does not exist

### Requirement: Management endpoint configuration

The service no longer defines management endpoint exposure configuration for actuator endpoints, as the actuator dependency has been removed.

#### Scenario: Management endpoints are not configured

- **WHEN** the application starts
- **THEN** no actuator management endpoints are available or configured
# Design: Remove Spring Boot Actuator

## Overview

Remove Spring Boot Actuator from the existing Spring Boot service to eliminate unused operational endpoints and simplify the application dependencies.

## Decisions

- Remove Spring Boot Actuator dependency (`spring-boot-starter-actuator`) from Maven pom.xml.
- Remove actuator-related configuration from application properties.
- Clean up any management endpoint configuration that was added for actuator.
- Do not modify domain logic or the aggregation algorithm.

## Endpoint changes

- Remove `GET /actuator/health` and any other actuator endpoints.
- Ensure no references to actuator endpoints remain in configuration or tests.
- Verify that application startup and functionality remain unchanged after removal.

## Risks and mitigations

- Risk: breaking tests that depend on actuator endpoints.
  - Mitigation: review and update any tests that reference `/actuator/*` endpoints.
- Risk: accidental behavior changes in existing API.
  - Mitigation: run existing verification tests to ensure `/api/v1/gods/stats/sum` continues to work.
- Risk: removing configuration that affects other parts of the application.
  - Mitigation: only remove actuator-specific configuration, leave other Spring Boot settings intact.
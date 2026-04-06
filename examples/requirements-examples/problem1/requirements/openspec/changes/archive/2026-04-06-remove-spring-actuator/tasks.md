# Tasks: Remove Spring Boot Actuator

## OpenSpec task list

- [x] Remove `spring-boot-starter-actuator` dependency from the implementation module
- [x] Remove actuator-related configuration from application properties
- [x] Verify `GET /actuator/health` returns HTTP 404 (Not Found) after removal
- [x] Verify existing `god-analysis-api` behavior remains unchanged
- [x] Update any tests that reference actuator endpoints
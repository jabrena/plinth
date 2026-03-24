# Contributor Quickstart Guide

## Your role

You are an expert Java backend engineer specializing in APIs, services, and databases.

- You understand Spring Boot, REST API design, and backend service architecture
- You help build and maintain Java backend applications with focus on API development
- You work with databases, external service integrations, and backend business logic
- You apply best practices for testing, error handling, and performance optimization

## Tech stack

- **Language:** Java 26
- **Build:** Maven (wrapper: `./mvnw`)
- **Framework:** Spring Boot 4.0.4
- **Key Dependencies:** Spring Web, Spring Actuator; outbound HTTP via Spring `RestClient` with connect/read timeouts from configuration (no Resilience4j retries for US-001)
- **Testing:** JUnit 5, Spring `RestClient` (acceptance tests), WireMock, Spring Boot Test
- **Monitoring:** Spring Boot Actuator

## File structure

- `src/main/java/info/jab/ms/` – Main application source code using `info.jab.ms` package (WRITE here for business logic)
- `src/main/resources/` – Configuration files and resources (WRITE here for config)
- `src/test/java/info/jab/ms/` – Test source code using `info.jab.ms` package (WRITE here for tests)
- `target/` – Generated build output (READ only, never edit directly)
- `pom.xml` – Maven project configuration (WRITE here for dependencies)
- `.mvn/` – Maven wrapper configuration (READ only)

### Package Structure
- **Base package:** `info.jab.ms`

## Commands

```bash
# Build and test the application
./mvnw clean verify

# Run the application locally
./mvnw spring-boot:run

# Run only unit tests
./mvnw test

# Run only integration tests
./mvnw failsafe:integration-test

# Package the application
./mvnw clean package

# Clean build artifacts
./mvnw clean
```

## Git workflow

- **Conventional Commits**: Use conventional commit format for all commit messages
- Format: `type(scope): description`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Examples:
  - `feat(api): add god stats aggregation endpoint`
  - `fix(controller): resolve timeout handling in external API calls`
  - `test(integration): add acceptance tests for filtering logic`
  - `docs(readme): update API documentation`

## Boundaries

- ✅ **Always do:** Run `./mvnw clean verify` before committing changes, write tests for new functionality, follow REST API best practices, handle errors gracefully, use proper HTTP status codes
- ⚠️ **Ask first:** Adding new dependencies to pom.xml, changing application configuration, modifying API contracts, adding new external service integrations
- 🚫 **Never do:** Edit target/ directory directly, commit secrets or credentials, skip tests before promoting changes, break existing API contracts without versioning

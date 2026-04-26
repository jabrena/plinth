---
name: 303-frameworks-spring-boot-validation
description: Use when you need to design, review, or improve validation in Spring Boot applications — including Bean Validation on request DTOs, @Valid/@Validated at API boundaries, custom constraints, method validation, and consistent validation error handling.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Spring Boot Validation Guidelines

## Role

You are a Senior software engineer with extensive experience in Spring Boot and Jakarta Bean Validation

## Goal

Enforce validation at the boundaries of Spring Boot applications so invalid input is rejected early, errors are predictable for clients, and domain/service layers receive valid data. Prefer declarative Bean Validation annotations and centralized error handling over ad-hoc if/else checks in controllers.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Validate request DTOs at the boundary

### Example 1: Validate request DTOs at the boundary

Title: Use Bean Validation annotations and `@Valid` in controllers
Description: Keep validation declarative in DTOs and controller signatures. Return structured 400 responses for violations through centralized exception handling.

**Good example:**

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
class UserController {
    @PostMapping
    ResponseEntity<String> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok("created");
    }
}

record CreateUserRequest(
    @NotBlank String username,
    @Email String email,
    @Size(min = 8, max = 128) String password
) { }
```

**Bad example:**

```java
@RestController
class BadUserController {
    @PostMapping("/users")
    String create(@RequestBody CreateUserRequest request) {
        if (request.email() == null || !request.email().contains("@")) {
            throw new IllegalArgumentException("bad email");
        }
        return "created";
    }
}
record CreateUserRequest(String username, String email, String password) { }
```

## Output Format

- **ANALYZE** controller and service boundaries for missing or inconsistent validation
- **APPLY** declarative Bean Validation rules with `@Valid` / `@Validated` and custom constraints where needed
- **STANDARDIZE** validation error responses with centralized exception handling
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not bypass boundary validation in public API endpoints
- Do not leak internal exception details in validation responses
- Prefer small and incremental validation refactors
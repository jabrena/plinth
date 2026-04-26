---
name: 503-frameworks-micronaut-validation
description: Use when you need to design, review, or improve validation in Micronaut applications — including Bean Validation annotations, @Valid at controller boundaries, custom constraints, and predictable validation error responses.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Micronaut Validation Guidelines

## Role

You are a Senior software engineer with extensive experience in Micronaut and Jakarta Bean Validation

## Goal

Ensure Micronaut APIs validate inputs at boundaries, enforce clear constraint rules, and return stable validation error responses without leaking implementation details.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Validate input using @Valid and Bean Validation

### Example 1: Validate input using @Valid and Bean Validation

Title: Keep constraints in DTOs and validate at controller entry points
Description: Use `@Body @Valid` in controller methods and annotate request DTOs with Jakarta constraints. Avoid manual string checks spread across endpoints.

**Good example:**

```java
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller("/users")
class UserController {
    @Post
    HttpResponse<String> create(@Body @Valid CreateUserRequest request) {
        return HttpResponse.ok("created");
    }
}

record CreateUserRequest(@NotBlank String username, @Email String email) { }
```

**Bad example:**

```java
@Controller("/users")
class BadUserController {
    @Post
    String create(@Body CreateUserRequest request) {
        if (request.email() == null || !request.email().contains("@")) {
            throw new IllegalArgumentException("invalid email");
        }
        return "created";
    }
}
record CreateUserRequest(String username, String email) { }
```

## Output Format

- **ANALYZE** missing validation at controller and service boundaries
- **APPLY** Bean Validation rules with `@Valid` / custom constraints where needed
- **STANDARDIZE** validation error response handling
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not rely on ad-hoc per-endpoint checks as the primary validation strategy
- Do not expose internal exception details to API clients
- Prefer incremental changes and re-verify frequently
---
name: 304-frameworks-spring-boot-security
description: Use when you need to design, review, or improve security in Spring Boot applications — including Spring Security configuration, authentication/authorization, endpoint protection, method security, and secure handling of sensitive data.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Spring Boot Security Guidelines

## Role

You are a Senior software engineer with extensive experience in Spring Security and secure API design

## Goal

Apply secure-by-default Spring Boot practices: enforce authentication and authorization consistently, define least-privilege access control, and avoid sensitive data exposure in logs and API responses.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Secure endpoint and method access

### Example 1: Secure endpoint and method access

Title: Use SecurityFilterChain and method-level authorization
Description: Define explicit security policies for public and protected routes. Use role-based authorization for privileged operations.

**Good example:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(registry -> registry
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .build();
    }
}
```

**Bad example:**

```java
@Configuration
class InsecureConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(registry -> registry.anyRequest().permitAll())
            .build();
    }
}
```

## Output Format

- **ANALYZE** current authentication and authorization boundaries
- **APPLY** secure defaults, route protection, and least-privilege policies
- **HARDEN** logging/error behavior to avoid sensitive data leakage
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not weaken endpoint protection to bypass failing tests
- Do not expose tokens, secrets, or credentials in logs/responses
- Prefer incremental security changes with verification between steps
---
name: 401-frameworks-quarkus-core
description: Use when you need to review, improve, or build Quarkus applications — including @QuarkusMain entry points, CDI scopes (@ApplicationScoped, @Singleton, @Dependent), constructor injection, @ConfigMapping and SmallRye Config, profiles (%dev, %test, %prod), build-time vs runtime configuration, lifecycle (@Startup, @PreDestroy), health and metrics hooks, and test-friendly bean design. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus Core Guidelines

Apply Quarkus core guidelines for CDI beans, configuration, profiles, and lifecycle.

**What is covered in this Skill?**

- @QuarkusMain and application entry (when custom main is needed)
- CDI scopes: @ApplicationScoped, @Singleton, @Dependent
- Constructor injection with @Inject
- @ConfigMapping and structured configuration
- Profile-specific properties (%dev, %test, %prod) and @IfBuildProfile
- Startup and shutdown observers (@Startup, @PreDestroy)
- Operational hooks: health, metrics (when extensions are present)
- Alignment with fast startup and native-image constraints

**Scope:** Apply recommendations based on the reference rules and good/bad code examples.

## Constraints

Before applying any Quarkus changes, ensure the project compiles. If compilation fails, stop immediately. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately — compilation failure is a blocking condition
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed rules, good/bad patterns, and constraints

## When to use this skill

- Review Java code for Quarkus application structure and CDI
- Apply best practices for Quarkus configuration and beans

## Reference

For detailed guidance, examples, and constraints, see [references/401-frameworks-quarkus-core.md](references/401-frameworks-quarkus-core.md).

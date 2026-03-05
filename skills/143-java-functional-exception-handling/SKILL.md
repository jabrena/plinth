---
name: 143-java-functional-exception-handling
description: Use when you need to apply functional exception handling best practices in Java — including replacing exception overuse with Optional and VAVR Either types, designing error type hierarchies using sealed classes and enums, implementing monadic error composition pipelines, establishing functional control flow patterns, and reserving exceptions only for truly exceptional system-level failures. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java Functional Exception handling Best Practices

Identify and apply functional exception handling best practices in Java to improve error clarity, maintainability, and performance by eliminating exception overuse in favour of monadic error types.

**Prerequisites:** Run `./mvnw validate` or `mvn validate` before applying any changes. If validation fails, **stop immediately** — do not proceed until the project is in a valid state. Also confirm the VAVR dependency (`io.vavr:vavr`) and SLF4J are present when introducing `Either` types.

**Core areas:** `Optional<T>` for nullable values over throwing `NullPointerException` or `NotFoundException`, VAVR `Either<L,R>` for predictable business-logic failures, `CompletableFuture<T>` for async error handling, sealed classes and records for rich error type hierarchies with exhaustive pattern matching, enum-based error types for simple failure cases, functional composition with `flatMap`/`map`/`peek`/`peekLeft` for chaining operations that can fail, structured logging at appropriate severity levels (warn/info for business failures, error for system failures), checked vs unchecked exception discipline, and exception chaining with full causal context when exceptions are unavoidable.

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed good/bad examples, constraints, and safeguards for each functional exception handling pattern.

## Reference

For detailed guidance, examples, and constraints, see [references/143-java-functional-exception-handling.md](references/143-java-functional-exception-handling.md).

---
name: 143-java-functional-exception-handling
description: Use when you need to apply functional exception handling best practices in Java — including replacing exception overuse with Optional and VAVR Either types, designing error type hierarchies using sealed classes and enums, implementing monadic error composition pipelines, establishing functional control flow patterns, and reserving exceptions only for truly exceptional system-level failures. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Functional Exception handling Best Practices

Identify and apply functional exception handling best practices in Java to improve error clarity, maintainability, and performance by eliminating exception overuse in favour of monadic error types.

**What is covered in this Skill?**

- `Optional<T>` for nullable values over throwing `NullPointerException` or `NotFoundException`
- VAVR `Either<L,R>` for predictable business-logic failures
- `CompletableFuture<T>` for async error handling
- Sealed classes and records for rich error type hierarchies with exhaustive pattern matching
- Enum-based error types for simple failure cases
- Functional composition: `flatMap`/`map`/`peek`/`peekLeft` for chaining operations that can fail
- Structured logging: warn/info for business failures, error for system failures
- Checked vs unchecked exception discipline
- Exception chaining with full causal context when exceptions are unavoidable

**Scope:** The reference is organized by examples (good/bad code patterns) for each core area. Apply recommendations based on applicable examples.

## Workflow

1. **Validate** — run `./mvnw validate` and confirm the project is in a valid state
2. **Read reference** — review the reference for good/bad patterns across all seven example areas
3. **Identify exception patterns** — scan for exception-based control flow, null returns, and missing error context
4. **Apply functional approach** — replace with `Optional<T>` for nullable values, `Either<L,R>` for business failures, and monadic composition (`flatMap`/`map`)
5. **Verify** — run `./mvnw clean verify` to confirm all changes pass and VAVR dependency is present

## Quick Reference

**Either for business-logic failures (preferred over exceptions):**

```java
import io.vavr.control.Either;

public Either<TransferError, TransferSuccess> transferMoney(
        Account from, Account to, BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        return Either.left(new TransferError("INVALID_AMOUNT",
            "Transfer amount must be positive"));
    }
    if (from.getBalance().compareTo(amount) < 0) {
        return Either.left(new TransferError("INSUFFICIENT_FUNDS",
            String.format("Required %s, available %s", amount, from.getBalance())));
    }
    return Either.right(performTransfer(from, to, amount));
}
```

**Optional for nullable values (preferred over NullPointerException):**

```java
public Optional<User> findUserById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
        return Optional.empty();
    }
    return userRepository.findById(id);
}
```

## Constraints

Before applying any functional exception handling changes, ensure the project validates. When introducing Either types, confirm the VAVR dependency (io.vavr:vavr) and SLF4J are present.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any changes
- **SAFETY**: If validation fails, stop immediately — do not proceed until the project is in a valid state
- **DEPENDENCY**: When introducing `Either` types, confirm VAVR (`io.vavr:vavr`) and SLF4J are present
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each functional exception handling pattern

## When to use this skill

- Review Java code for functional exception handling
- Apply best practices for functional exception handling in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/143-java-functional-exception-handling.md](references/143-java-functional-exception-handling.md).

---
name: 123-java-exception-handling
description: Use when you need to apply Java exception handling best practices — including using specific exception types, managing resources with try-with-resources, securing exception messages, preserving error context via exception chaining, validating inputs early with fail-fast principles, handling thread interruption correctly, documenting exceptions with @throws, enforcing logging policy, translating exceptions at API boundaries, managing retries and idempotency, enforcing timeouts, attaching suppressed exceptions, and propagating failures in async/reactive code. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Exception Handling Guidelines

Identify and apply robust Java exception handling practices to improve error clarity, security, debuggability, and system reliability.

**What is covered in this Skill?**

- Specific exception types instead of generic `Exception`/`RuntimeException`
- try-with-resources for automatic resource cleanup
- Secure exception messages that avoid information leakage
- Exception chaining to preserve full error context
- Early input validation with `IllegalArgumentException`/`NullPointerException`
- `InterruptedException` handling with interrupted-status restoration
- `@throws` JavaDoc documentation, fail-fast principle
- Structured logging with correlation IDs, avoiding log-and-throw duplication
- API boundary translation via centralized exception mappers
- Bounded retry with backoff for idempotent operations only
- Timeout enforcement with deadline propagation
- `Throwable#addSuppressed` for secondary cleanup failures
- Never catching `Throwable`/`Error`
- Observability via error metrics
- Failure propagation in async `CompletionStage` code

**Scope:** The reference is organized by examples (good/bad code patterns) for each core area. Apply recommendations based on applicable examples.

## Workflow

1. **Compile first** — Run `./mvnw compile` or `mvn compile`. If compilation fails, stop immediately.
2. **Read the reference** — Review the detailed good/bad examples for each exception handling pattern before making changes.
3. **Identify issues** — Analyze Java code for exception handling anti-patterns: generic catches, missing resource cleanup, information leakage, swallowed exceptions, missing exception chaining, and log-and-throw duplication.
4. **Apply improvements incrementally** — Implement exception handling best practices one pattern at a time, validating after each change.
5. **Verify** — Run `./mvnw clean verify` to confirm all tests pass and no regressions were introduced.

## Quick Reference

Try-with-resources for automatic resource cleanup:

```java
// GOOD: Resources are automatically closed, even on exception
public String readFile(Path filePath) throws FileProcessingException {
    try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    } catch (NoSuchFileException e) {
        throw new FileProcessingException("File not found: " + filePath.getFileName(), e);
    } catch (IOException e) {
        throw new FileProcessingException("Failed to read file", e);
    }
}

// BAD: Manual resource management risks leaks on exception
BufferedReader reader = Files.newBufferedReader(filePath);
String content = reader.lines().collect(Collectors.joining("\n"));
reader.close();  // never reached if lines() throws
```

## Constraints

Before applying any exception handling changes, ensure the project compiles. If compilation fails, stop immediately — do not proceed until resolved. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any changes
- **SAFETY**: If compilation fails, stop immediately — do not proceed until the project is in a valid state
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each exception handling pattern

## When to use this skill

- Exception handling
- Use try-with-resources in Java code
- Create exception chaining in Java code
- Apply fail-fast validation in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/123-java-exception-handling.md](references/123-java-exception-handling.md).

---
name: 123-java-exception-handling
description: Use when you need to apply Java exception handling best practices — including using specific exception types, managing resources with try-with-resources, securing exception messages, preserving error context via exception chaining, validating inputs early with fail-fast principles, handling thread interruption correctly, documenting exceptions with @throws, enforcing logging policy, translating exceptions at API boundaries, managing retries and idempotency, enforcing timeouts, attaching suppressed exceptions, and propagating failures in async/reactive code. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java Exception Handling Guidelines

Identify and apply robust Java exception handling practices to improve error clarity, security, debuggability, and system reliability.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any changes. If compilation fails, **stop immediately** — do not proceed until the project is in a valid state.

**Core areas:** Specific exception types instead of generic `Exception`/`RuntimeException`, try-with-resources for automatic resource cleanup, secure exception messages that avoid information leakage, exception chaining to preserve full error context, early input validation with `IllegalArgumentException`/`NullPointerException`, `InterruptedException` handling with interrupted-status restoration, `@throws` JavaDoc documentation, fail-fast principle, structured logging with correlation IDs avoiding log-and-throw duplication, API boundary translation via centralized exception mappers, bounded retry with backoff for idempotent operations only, timeout enforcement with deadline propagation, `Throwable#addSuppressed` for secondary cleanup failures, never catching `Throwable`/`Error`, observability via error metrics, and failure propagation in async `CompletionStage` code.

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed good/bad examples, constraints, and safeguards for each exception handling pattern.

## Reference

For detailed guidance, examples, and constraints, see [references/123-java-exception-handling.md](references/123-java-exception-handling.md).

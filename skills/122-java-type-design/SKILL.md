---
name: 122-java-type-design
description: Use when you need to review, improve, or refactor Java code for type design quality — including establishing clear type hierarchies, applying consistent naming conventions, eliminating primitive obsession with domain-specific value objects, leveraging generic type parameters, creating type-safe wrappers, designing fluent interfaces, ensuring precision-appropriate numeric types (BigDecimal for financial calculations), and improving type contrast through interfaces and method signature alignment. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Type Design Thinking in Java

Review and improve Java code using comprehensive type design principles that apply typography concepts to code structure and organization for maximum clarity and maintainability.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any change. If compilation fails, **stop immediately** and do not proceed — compilation failure is a blocking condition.

**Core areas:** Clear type hierarchies (nested static classes, logical structure), consistent naming conventions (domain-driven patterns, uniform interface/implementation naming), strategic whitespace for readability, type-safe wrappers (value objects replacing primitive obsession, EmailAddress, Money), generic type parameters (flexible reusable types, bounded parameters), domain-specific fluent interfaces (builder pattern, method chaining), type "weights" (conceptual importance assignment — core domain vs supporting vs utility), type contrast through interfaces (contract vs implementation separation), aligned method signatures (consistent parameter and return types across related classes), self-documenting code (clear descriptive names), BigDecimal for precision-sensitive calculations (financial/monetary operations), and strategic type selection (Optional, Set vs List, interfaces over concrete types).

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed examples, good/bad patterns, and constraints.

## Reference

For detailed guidance, examples, and constraints, see [references/122-java-type-design.md](references/122-java-type-design.md).

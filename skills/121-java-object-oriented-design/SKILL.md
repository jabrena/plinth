---
name: 121-java-object-oriented-design
description: Use when you need to review, improve, or refactor Java code for object-oriented design quality — including applying SOLID, DRY, and YAGNI principles, improving class and interface design, fixing OOP concept misuse (encapsulation, inheritance, polymorphism), identifying and resolving code smells (God Class, Feature Envy, Data Clumps), or improving object creation patterns, method design, and exception handling. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java Object-Oriented Design Guidelines

Review and improve Java code using comprehensive object-oriented design guidelines and refactoring practices.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any change. If compilation fails, **stop immediately** and do not proceed — compilation failure is a blocking condition.

**Core areas:** Fundamental design principles (SOLID, DRY, YAGNI), class and interface design (composition over inheritance, immutability, accessibility minimization, accessor methods), core OOP concepts (encapsulation, inheritance, polymorphism), object creation patterns (static factory methods, Builder pattern, Singleton, dependency injection, avoiding unnecessary objects), OOD code smells (God Class, Feature Envy, Inappropriate Intimacy, Refused Bequest, Shotgun Surgery, Data Clumps), method design (parameter validation, defensive copies, careful signatures, empty collections over nulls, Optional usage), and exception handling (checked vs. runtime exceptions, standard exceptions, failure-capture messages, no silent ignoring).

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed examples, good/bad patterns, and constraints.

## Reference

For detailed guidance, examples, and constraints, see [references/121-java-object-oriented-design.md](references/121-java-object-oriented-design.md).

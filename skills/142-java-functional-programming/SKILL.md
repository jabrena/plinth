---
name: 142-java-functional-programming
description: Use when you need to apply functional programming principles in Java — including writing immutable objects and Records, pure functions, functional interfaces, lambda expressions, Stream API pipelines, Optional for null safety, function composition, higher-order functions, pattern matching for instanceof and switch, sealed classes/interfaces for controlled hierarchies, Stream Gatherers for custom operations, currying/partial application, effect boundary separation, and concurrent-safe functional patterns. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Functional Programming rules

Identify and apply functional programming principles in Java to improve immutability, expressiveness, and maintainability.

**What is covered in this Skill?**

- Immutable objects and Records (JEP 395)
- Pure functions free of side effects
- Functional interfaces: `Function`, `Predicate`, `Consumer`, `Supplier`, custom `@FunctionalInterface`
- Lambda expressions and method references
- Stream API: filter/map/reduce pipelines, parallel streams, `toUnmodifiable*` collectors
- `Optional` idiomatic usage: `map`/`flatMap`/`filter`/`orElse*` over `isPresent()`+`get()`
- Function composition: `andThen`/`compose`
- Higher-order functions: memoization, currying, partial application
- Pattern Matching for `instanceof` and `switch` (Java 21)
- Sealed classes and interfaces (Java 17) for exhaustive domain hierarchies
- Switch Expressions (Java 14), Stream Gatherers (JEP 461)
- Effect-boundary separation: side effects at edges, pure core logic
- Immutable collections: `List.of()`, `Collectors.toUnmodifiableList()`

**Scope:** The reference is organized by examples (good/bad code patterns) for each core area. Apply recommendations based on applicable examples.

## Workflow

1. **Compile** — Run `./mvnw compile` to ensure the project builds before making changes
2. **Read the reference** — Review the good/bad examples in the reference file for functional programming patterns relevant to the code
3. **Identify opportunities** — Scan for mutable state, imperative loops replaceable with streams, raw null checks replaceable with `Optional`, and classes convertible to Records
4. **Apply improvements** — Replace mutable classes with Records, convert imperative loops to Stream pipelines, use `Optional` idiomatically with `map`/`flatMap`/`orElse`, and apply pattern matching where applicable
5. **Verify** — Run `./mvnw clean verify` to confirm all changes compile and tests pass

## Quick Reference

Stream API pipeline pattern:

```java
List<Integer> evenSquares = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .collect(Collectors.toUnmodifiableList());
```

Records for immutable data transfer:

```java
public record Person(String name, int age) {
    public Person {
        if (age < 0) throw new IllegalArgumentException();
    }
}
```

## Constraints

Before applying any functional programming changes, ensure the project compiles. If compilation fails, stop immediately — do not proceed until the project compiles successfully. Verify that maven-compiler-plugin source/target supports the Java features being used.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any changes
- **SAFETY**: If compilation fails, stop immediately — do not proceed until the project compiles successfully
- **VERIFY**: Verify maven-compiler-plugin source/target supports the Java features being used
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each functional programming pattern

## When to use this skill

- Review Java code for functional programming
- Apply best practices for functional programming in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/142-java-functional-programming.md](references/142-java-functional-programming.md).

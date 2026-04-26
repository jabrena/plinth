---
name: 126-java-high-performance
description: Use when you need to improve Java performance comprehensively in code — including runtime-aware coding choices, allocation reduction, CPU hot-path optimization, concurrency and backpressure patterns, efficient I/O parsing/serialization logic, and persistence/caching implementation patterns. This should trigger for requests such as Review Java code for high performance; Optimize Java hot path; Reduce Java allocations; Improve Java latency/throughput. Part of cursor-rules-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Java rules for High Performance

Identify and apply practical Java high-performance techniques using a measure-first approach, with emphasis on allocation reduction, data layout, concurrency discipline, and evidence-based validation.

**What is covered in this Skill?**

- Measure-first workflow for Java code optimization
- JVM/runtime-aware coding guidance
- Allocation reduction techniques with bad/good patterns
- CPU hot-path simplification and loop-level efficiency patterns
- Concurrency/backpressure and timeout/cancellation discipline
- I/O, parsing, and serialization efficiency patterns
- Persistence/query and caching strategy guidance
- Java-centric decision workflow: keep/revert based on measured impact

**Scope:** Practical optimization in application code and APIs. Apply only where profiling indicates real bottlenecks.

## Constraints

Performance optimization must be evidence-driven and safe, focused on Java code changes that preserve correctness and maintainability.

- **MEASURE-FIRST**: Establish baseline behavior and identify Java code hot paths before optimization
- **NO PREMATURE OPTIMIZATION**: Only optimize code paths identified by profiling evidence
- **BEFORE APPLYING**: Read the reference for bad/good examples and measurement workflow
- **EDGE CASE**: If hotspot evidence is unclear, ask clarifying questions before changing code

## When to use this skill

- Review Java code for high performance
- Optimize Java hot path
- Reduce Java allocations
- Improve Java latency
- Improve Java throughput

## Workflow

1. **Identify Java hotspot and baseline behavior**

Confirm the performance-sensitive Java path and baseline behavior before changing code.

2. **Read high-performance reference and locate bottlenecks**

Read `references/126-java-high-performance.md`, identify hotspots across allocation, CPU, concurrency, I/O, data access, and caching (Java code and API boundaries only).

3. **Apply targeted optimizations**

Implement minimal, evidence-backed changes across relevant domains (allocation, CPU, concurrency, I/O, and persistence/caching in Java code).

4. **Validate and compare code-level outcomes**

Compare before/after behavior and keep only Java code changes with meaningful, verified gains.

## Reference

For detailed guidance, examples, and constraints, see [references/126-java-high-performance.md](references/126-java-high-performance.md).

---
name: 163-java-profiling-refactor
description: Use when you need to refactor Java code based on profiling analysis findings — including reviewing docs/profiling-problem-analysis and docs/profiling-solutions, identifying specific performance bottlenecks, and implementing targeted code changes to address CPU, memory, or threading issues. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Profiling Workflow / Step 3 / Refactor code to fix issues

Implement refactoring based on profiling analysis: review profiling-problem-analysis-YYYYMMDD.md and profiling-solutions-YYYYMMDD.md, identify specific performance bottlenecks, and refactor code to fix them. Ensure all tests pass after changes.

**What is covered in this Skill?**

- Review analysis notes: docs/profiling-problem-analysis-YYYYMMDD.md, docs/profiling-solutions-YYYYMMDD.md
- Identify specific bottlenecks from the documented findings
- Refactor code to address CPU hotspots, memory leaks, threading issues, or other performance problems
- Run verification: ./mvnw clean verify or mvn clean verify

**Scope:** Changes must pass all tests. Apply fixes incrementally and verify after each significant change.

## Workflow

1. **Review analysis docs**: Read `docs/profiling-problem-analysis-YYYYMMDD.md` and `docs/profiling-solutions-YYYYMMDD.md` to understand documented findings
2. **Identify bottlenecks**: Categorize issues by type — CPU hotspots, memory leaks, threading problems, or I/O contention
3. **Apply incremental fixes**: Refactor one bottleneck at a time, starting with the highest-impact issue
4. **Run tests**: Execute `./mvnw clean verify` or `mvn clean verify` after each fix to ensure no regressions
5. **Verify performance**: Confirm the refactoring addresses the profiled issue; re-profile if necessary

## Quick Reference

**Common refactoring pattern — replacing inefficient string concatenation in a hot path:**

```java
// BAD: String concatenation in a loop (creates many intermediate objects)
String result = "";
for (String item : items) {
    result += item + ", ";
}

// GOOD: StringBuilder for hot-path string assembly
StringBuilder sb = new StringBuilder();
for (String item : items) {
    sb.append(item).append(", ");
}
String result = sb.toString();
```

## Constraints

Verify that changes pass all tests before considering the refactoring complete.

- **MANDATORY**: Run `./mvnw clean verify` or `mvn clean verify` after applying refactoring
- **SAFETY**: If tests fail, fix issues before proceeding
- **BEFORE APPLYING**: Read the analysis and solutions documents for specific recommendations

## When to use this skill

- Refactor performance
- Optimize hot path
- Performance refactoring

## Reference

For detailed guidance, examples, and constraints, see [references/163-java-profiling-refactor.md](references/163-java-profiling-refactor.md).

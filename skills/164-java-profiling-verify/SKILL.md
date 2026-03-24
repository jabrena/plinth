---
name: 164-java-profiling-verify
description: Use when you need to verify Java performance optimizations by comparing profiling results before and after refactoring — including baseline validation, post-refactoring report generation, quantitative before/after metrics comparison, side-by-side flamegraph analysis, regression detection, or creating profiling-comparison-analysis and profiling-final-results documentation. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Profiling Workflow / Step 4 / Verify results

Verify performance optimizations through rigorous before/after comparison: ensure baseline and post-refactoring profiling data use identical test conditions, generate post-refactoring reports, compare metrics (memory, CPU, GC, threading), perform side-by-side flamegraph analysis, document findings in profiling-comparison-analysis-YYYYMMDD.md and profiling-final-results-YYYYMMDD.md, and validate success criteria.

**What is covered in this Skill?**

- Pre-refactoring baseline: run profiler with same load before changes
- Post-refactoring: generate new reports with identical test conditions
- Comparison: memory (leaks, allocations, GC), CPU (hotspots, contention), visual flamegraph comparison
- Documentation: profiler/docs/profiling-comparison-analysis-YYYYMMDD.md, profiler/docs/profiling-final-results-YYYYMMDD.md
- File naming: baseline/after suffixes, timestamp-based organization
- Validation: verify reports exist, compare metrics, identify regressions

**Scope:** Identical test conditions are critical. Document test scenarios. Validate application runs with refactored code before generating new reports.

## Workflow

1. **Validate baseline exists** — Confirm pre-refactoring profiling results are available and complete. If not, capture baseline using `profiler/run-with-profiler.sh` before proceeding.
2. **Generate post-refactoring reports** — Run the profiler with identical test conditions, load patterns, and duration as the baseline session.
3. **Compare metrics** — Perform side-by-side analysis of before/after results: memory (heap usage, allocations, GC pressure), CPU (hotspots, method execution time, thread contention), and flamegraph visual comparison (canvas height, stack depth, hot spot width).
4. **Document findings** — Create `profiling-comparison-analysis-YYYYMMDD.md` with executive summary, methodology, before/after metrics table, and recommendations. Create `profiling-final-results-YYYYMMDD.md` with production readiness checklist.
5. **Validate success criteria** — Confirm performance targets are met, no regressions were introduced, and both report sets are complete.

## Quick Reference

Before/after comparison checklist:

```markdown
## Memory Analysis
- [ ] Memory leak detection: compare heap usage patterns
- [ ] Allocation patterns: reduced object creation in flamegraphs
- [ ] GC pressure: compare frequency and duration
- [ ] Peak memory usage: maximum heap utilization

## CPU Performance
- [ ] Hot spots: compare CPU flamegraphs for resolved bottlenecks
- [ ] Method execution time: critical path improvements
- [ ] Thread contention: reduced blocking

## Metrics Table
| Metric           | Before | After | Improvement |
|------------------|--------|-------|-------------|
| Stack Depth      | X      | Y     | Z%          |
| Memory Allocs    | X      | Y     | Z%          |
| GC Frequency     | X      | Y     | Z%          |
```

## Constraints

Use identical test conditions between baseline and post-refactoring. Verify both report sets are complete. Document test scenarios.

- **CONSISTENCY**: Use identical test conditions and load patterns for baseline and post-refactoring
- **VALIDATE**: Ensure both baseline and post-refactoring reports exist and are non-empty before comparison
- **DOCUMENT**: Record test scenarios and load patterns for reproduction
- **BEFORE APPLYING**: Read the reference for comparison templates and validation steps

## When to use this skill

- Verify performance fix
- Performance benchmark

## Reference

For detailed guidance, examples, and constraints, see [references/164-java-profiling-verify.md](references/164-java-profiling-verify.md).

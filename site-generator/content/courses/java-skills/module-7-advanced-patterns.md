title=Module 7: Advanced Patterns - Composing Java Skill Workflows
type=course
status=published
date=2025-09-17
updated=2026-09-26
author=MyRobot
tags=java, skills
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- Select skills by engineering outcome instead of by familiar terminology
- Sequence skills so each phase produces trusted inputs for the next
- Distinguish independent checks that can run in parallel from dependent changes
- Preserve human decisions, repository constraints, and validation evidence
- Design a complete Java delivery workflow without bypassing skill safeguards

## 📚 Module Overview

**Duration:** 4 hours  
**Difficulty:** Advanced  
**Prerequisites:** All previous modules completed

This capstone focuses on composition. A skill owns a bounded engineering responsibility; a workflow connects those responsibilities through explicit artifacts and verification gates. The objective is not to invoke as many skills as possible, but to use the smallest coherent set that moves the codebase from evidence to a validated outcome.

## 🗺️ Learning Path

### **Lesson 7.1: Select Skills by Outcome** (45 minutes)

Start with the desired result and choose the skill whose contract matches it.

| Objective | Skill | Expected evidence or output |
|---|---|---|
| Improve Maven configuration | `110-java-maven-best-practices` | Reviewed `pom.xml` changes and Maven validation |
| Improve object modeling | `121-java-object-oriented-design` | Design findings and targeted refactoring |
| Improve type safety | `122-java-type-design` | Stronger types and reduced invalid states |
| Add unit tests | `131-java-testing-unit-testing` | Focused tests and passing test suite |
| Investigate runtime performance | `161-java-profiling-detect` | Reproducible profiling artifacts |
| Explain profiling evidence | `162-java-profiling-analyze` | Problem and solution documents |
| Apply measured performance fixes | `163-java-profiling-refactor` | Targeted code changes based on trusted analysis |
| Prove performance improvement | `164-java-profiling-verify` | Controlled before/after comparison |

#### 🔧 Hands-on Exercise 7.1

For each request, identify one primary skill and explain why adjacent skills are not yet needed:

1. “The service slows down after several hours.”
2. “This public API accepts combinations of values that are invalid.”
3. “The Maven build allows conflicting dependency versions.”
4. “The repository needs a developer guide.”

The answer should name an initial skill, its required context, and its completion evidence.

### **Lesson 7.2: Build Evidence-Driven Sequences** (75 minutes)

Dependent skills must be ordered around durable artifacts:

```text
Observe → Analyze → Decide → Change → Verify
```

The profiling family demonstrates this boundary clearly:

1. `161-java-profiling-detect` collects artifacts under realistic load.
2. `162-java-profiling-analyze` cross-references the artifacts and records prioritized solutions.
3. `163-java-profiling-refactor` accepts trusted, repository-owned analysis documents and applies targeted changes.
4. `164-java-profiling-verify` repeats equivalent conditions and reports improvements or regressions.

Skipping analysis makes the refactoring speculative. Skipping verification turns an optimization into an unproven assumption.

#### 🔧 Hands-on Exercise 7.2

Design a profiling workflow for a Java service with increasing allocation pressure. Record:

- The load scenario and success criteria
- The artifacts produced by detection
- The problem and solution documents produced by analysis
- The approved refactoring scope
- The identical conditions required for verification

### **Lesson 7.3: Combine Quality Skills Safely** (75 minutes)

Some skills can inspect the same change independently, while others must wait for an earlier decision.

```text
                 ┌─ 124-java-secure-coding ─┐
Design decision ─┼─ 125-java-concurrency ───┼─ Consolidate findings ─ Test
                 └─ 122-java-type-design ───┘
```

Parallel review is useful when checks do not modify overlapping files. Refactoring should remain coordinated and sequential when several skills would edit the same classes.

#### 🔧 Hands-on Exercise 7.3

Plan a review for a concurrent payment component:

1. Use `124-java-secure-coding` to review trust boundaries and sensitive data.
2. Use `125-java-concurrency` to review shared state and lifecycle management.
3. Use `122-java-type-design` to identify invalid domain states.
4. Consolidate findings before authorizing code changes.
5. Use `131-java-testing-unit-testing` and the project test suite to protect behavior.

Document which steps can run independently and which require an approved ordering.

### **Lesson 7.4: Design a Delivery Workflow** (75 minutes)

A robust multi-skill workflow includes:

- **Objective:** the observable outcome, not a list of tools
- **Scope:** repositories, modules, packages, or files that may change
- **Inputs:** trusted issues, specifications, profiling reports, or code
- **Decision points:** questions that require maintainer judgment
- **Handoffs:** durable artifacts consumed by later skills
- **Validation:** tests, build checks, scanners, or comparisons
- **Stop conditions:** missing evidence, failed prerequisites, or unsafe ambiguity

#### 🔧 Capstone Exercise

Create a workflow for adding a performance-sensitive Java feature:

1. Plan the change and acceptance criteria.
2. Review object and type design with `121-java-object-oriented-design` and `122-java-type-design`.
3. Implement with the relevant Java language or framework skill.
4. Add focused tests with `131-java-testing-unit-testing`.
5. Review security and concurrency where applicable.
6. Establish load with `151-java-performance-jmeter`.
7. Run the 161 → 162 → 163 → 164 profiling lifecycle only when measurements justify optimization.
8. Update project documentation with `170-java-documentation`.

Your workflow must identify the artifact or validation gate that permits each transition.

## 🏆 Module Assessment

You have completed the module when you can:

- [ ] Select a minimal set of skills for a stated outcome
- [ ] Explain the contract and evidence produced by every selected skill
- [ ] Prevent downstream work from starting without trusted inputs
- [ ] Separate parallel review from overlapping code changes
- [ ] Define build, test, security, and performance validation gates
- [ ] Record a reproducible end-to-end workflow

## 🎓 Course Completion

You now have a practical model for using Java Agent Skills:

- Skills are bounded workflows, not magic commands.
- Repository context and maintainer decisions remain authoritative.
- Generated output must be reviewed and validated.
- Evidence should flow from one phase to the next.
- Verification completes the work; implementation alone does not.

Continue by applying one complete workflow to a real Java project and retaining its decisions, artifacts, and validation results in the repository.

[Return to the course overview](index.html)

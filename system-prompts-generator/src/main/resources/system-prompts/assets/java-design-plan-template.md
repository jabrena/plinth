

## YAML Frontmatter

```yaml
---
name: <Short Plan Name>
overview: "<One-line description: what, approach, key constraints.>"
todos: []
isProject: false
---
```

## Required Sections

| Section | Purpose |
|---------|---------|
| **Title** | `# Problem N: [Name] Implementation Plan` |
| **Requirements Summary** | User story, key business rules, acceptance criteria |
| **Approach** | Named approach (e.g., London Style TDD), Mermaid diagram |
| **Task List** | Table: #, Task, Phase, TDD, Milestone, Parallel, Status |
| **Execution Instructions** | Update Status after each task before advancing |
| **File Checklist** | Order, File path |
| **Notes** | Package layout, conventions, edge cases |

## Execution Instructions (Required)

```markdown

## Execution Instructions

When executing this plan:
1. Complete the current task.
2. **Update the Task List**: set the Status column for that task (e.g., ✔ or Done).
3. **For GREEN tasks**: MUST complete the associated Verify task before proceeding.
4. **For Verify tasks**: MUST ensure all tests pass and build succeeds before proceeding.
5. **Milestone rows** (Milestone column): a milestone is evolving complete software for that slice — complete the pair of Refactor tasks (logging, then optimize config/error handling/log levels) immediately before each milestone Verify.
6. Only then proceed to the next task.
7. Repeat for all tasks. Never advance without updating the plan.

**Critical Stability Rules:**
- After every GREEN implementation task, run the verification step
- All tests must pass before proceeding to the next implementation
- If any test fails during verification, fix the issue before advancing
- Never skip verification steps - they ensure software stability

**Parallel column:** Use grouping identifiers (A1, A2, A3, etc.) to group tasks into the same delivery slice. Use when assigning agents or branches to a milestone scope.
```

## Task Phases

Setup → RED (write failing test) → GREEN (pass test) → Refactor

## London Style (Outside-In) TDD Order

1. Acceptance/integration test (RED)
2. Delegate/controller (GREEN)
3. Service unit test (RED)
4. Service implementation (GREEN)
5. Client test (RED)
6. Client implementation (GREEN)
7. Refactor — verify `mvn clean verify`

## Section Templates

### Requirements Summary
```markdown

## Requirements Summary

**User Story:** [One sentence describing the user goal.]

**Key Business Rules:**
- **[Rule name]:** [Concrete rule]
- **Expected result:** [Specific value or behavior when applicable]
```

### Approach (with Mermaid)
Include an Approach section with strategy description and a Mermaid flowchart (flowchart LR with subgraph).

### Task List Table
| # | Task | Phase | TDD | Milestone | Parallel | Status |
|---|------|-------|-----|-----------|----------|--------|
| 1 | [Setup task description] | Setup | | | A1 | |
| 2 | [Write failing test] | RED | Test | | A1 | |
| 3 | [Implement minimal solution] | GREEN | Impl | | A1 | |
| 4 | [Add logging and observability] | Refactor | | | A1 | |
| 5 | [Optimize configuration and error handling] | Refactor | | | A1 | |
| 6 | [Verify milestone completion] | Verify | | milestone | A1 | |

### File Checklist Table
| Order | File |
|-------|------|
| 1 | `path/to/File1.java` |
| 2 | `path/to/Test.java` |
| 3 | `path/to/Impl.java` |

## Plan File Path

`.cursor/plans/US-XXX-plan-analysis.plan.md`

Where XXX is the user story number or identifier (e.g., `US-001-plan-analysis.plan.md`, `US-042-plan-analysis.plan.md`).

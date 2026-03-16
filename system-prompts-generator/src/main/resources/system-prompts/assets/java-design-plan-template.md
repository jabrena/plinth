

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
| **Task List** | Table: #, Phase, Task, TDD, Status |
| **Execution Instructions** | Update Status after each task before advancing |
| **File Checklist** | Order, File path, When (TDD phase) |
| **Notes** | Package layout, conventions, edge cases |

## Execution Instructions (Required)

```markdown

## Execution Instructions

When executing this plan:
1. Complete the current task.
2. **Update the Task List**: set the Status column for that task (e.g., ✔ or Done).
3. Only then proceed to the next task.
4. Repeat for all tasks. Never advance without updating the plan.
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
| # | Phase | Task | TDD | Status |
|---|-------|------|-----|--------|
| 1 | Setup | [First task] | | |
| 2 | RED | [Write failing test] | Test | |
| 3 | GREEN | [Implement minimal solution] | Impl | |
| 4 | Refactor | [Polish, verify] | | |

### File Checklist Table
| Order | File | When (TDD) |
|-------|------|------------|
| 1 | `path/to/File1.java` | Setup |
| 2 | `path/to/Test.java` | RED — write first |
| 3 | `path/to/Impl.java` | GREEN — implement |

## Plan File Path

`.cursor/plans/YYYY-MM-DD_<plan_name>.plan.md`

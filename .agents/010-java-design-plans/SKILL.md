---
name: 010-java-design-plans
description: Use when it is creatin a plan using Plan model and enchance the creation of structured design plans in Cursor Plan mode for Java implementations. Use when the user wants to create a plan, design an implementation, structure a development plan, or use plan mode for outside-in TDD, feature implementation, or refactoring work.
---

# Java Design Plan Creation

Guides the process of creating a structured plan using Cursor Plan mode. Plans follow a consistent section structure suitable for Java feature implementation, refactoring, or API design.

---

## Plan Mode Workflow

1. **Enter Plan mode** (or use plan-related commands) before creating the plan.
2. **Gather context**: Read specs, existing code, and acceptance criteria.
3. **Draft the plan** using the structure below.
4. **Iterate**: Refine tasks, dependencies, and file checklist as needed.

---

## Plan File Structure

Plans use Markdown with YAML frontmatter. Save to `.cursor/plans/YYYY-MM-DD_<plan_name>.plan.md` (prefix with creation date).

### YAML Frontmatter

```yaml
---
name: <Short Plan Name>
overview: "<One-line description: what, approach, key constraints.>"
todos: []
isProject: false
---
```

### Required Sections

| Section | Purpose | Content |
|---------|---------|---------|
| **Title** | Problem/feature identifier | `# Problem N: [Name] Implementation Plan` |
| **Requirements Summary** | Business context | User story, key business rules, acceptance criteria |
| **Approach** | Strategy and flow | Named approach (e.g., London Style TDD), diagram (Mermaid) |
| **Task List** | Ordered implementation steps | Table with #, Phase, Task, TDD, Status |
| **Execution Instructions** | How agents must execute | Update task Status after each task before advancing |
| **File Checklist** | What files and when | Order, File path, When (TDD phase) |
| **Notes** | Extra context | Package layout, conventions, edge cases |

---

## Section Templates

### Requirements Summary

```markdown
## Requirements Summary

**User Story:** [One sentence describing the user goal.]

**Key Business Rules:**
- **[Rule name]:** [Concrete rule]
- **[Filtering / Conversion / Timeout]:** [Behavior]
- **Expected result:** [Specific value or behavior when applicable]
```

### Approach (with Diagram)

````markdown
## [Approach Name] (e.g., London Style Outside-In TDD)

[Brief description of the strategy.]

```mermaid
flowchart LR
    subgraph [Flow Name]
        A[Step 1] --> B[Step 2]
        B --> C[Step 3]
    end
```
````

### Task List Table

```markdown
## Task List ([Approach] Order)

| #   | Phase   | Task                                                         | TDD  | Status |
| --- | ------- | ------------------------------------------------------------- | ---- | ------ |
| 1   | Setup   | [First task]                                                  |      |        |
| 2   | RED     | [Write failing test]                                          | Test |        |
| 3   | GREEN   | [Implement minimal solution]                                  | Impl |        |
| 4   | Refactor| [Polish, verify]                                              |      |        |
```

**Phases:** Setup, RED (write failing test), GREEN (pass test), Refactor. Use the **Status** column to track completion (e.g., `✔`, `Done`, or `✓` when finished).

### Execution Instructions (Required)

Include this section in every plan. It reminds agents to update the task list during execution:

```markdown
## Execution Instructions

When executing this plan:
1. Complete the current task.
2. **Update the Task List**: set the Status column for that task (e.g., ✔ or Done).
3. Only then proceed to the next task.
4. Repeat for all tasks. Never advance without updating the plan.
```

### File Checklist Table

```markdown
## File Checklist ([Approach] Order)

| Order | File                                              | When (TDD)              |
| ----- | ------------------------------------------------- | ----------------------- |
| 1     | `path/to/File1.java`                              | Setup                   |
| 2     | `path/to/Test.java`                               | RED — write first       |
| 3     | `path/to/Impl.java`                               | GREEN — implement       |
```

---

## London Style (Outside-In) TDD Pattern

For feature implementation, prefer **outside-in** order:

1. **Acceptance/integration test** (RED) — defines API and expected behavior.
2. **Delegate/controller** (GREEN) — minimal wiring.
3. **Service unit test** (RED) — business logic in isolation.
4. **Service implementation** (GREEN) — with fake/stub dependencies.
5. **Client test** (RED) — external calls.
6. **Client implementation** (GREEN) — wire real client.
7. **Refactor** — remove fakes, add error handling, verify `mvn clean verify`.

---

## Plan Execution Workflow

When **executing** a plan, follow this discipline for every task:

1. **Run** the current task (e.g., Task 1).
2. **When the task finishes**, immediately update the plan file: set the Status column for that task (e.g., ✔ or Done or ✓).
3. **Then** proceed to the next task.
4. Repeat steps 1–3 for all tasks in order.

Never advance to the next task without updating the task list. This keeps progress visible and lets the plan file reflect the current state.

---

## Plan Creation Checklist

Before finalizing:

- [ ] Frontmatter has `name`, `overview`, `todos`, `isProject`.
- [ ] Requirements Summary includes user story and key business rules.
- [ ] Approach section names the strategy and includes a Mermaid diagram.
- [ ] Task list is ordered (Setup → RED → GREEN → Refactor) with Status column.
- [ ] **Execution Instructions** section is included (update Status after each task before advancing).
- [ ] File checklist maps files to TDD phases.
- [ ] Notes cover package layout, conventions, and constraints.
- [ ] Plan file path is `.cursor/plans/YYYY-MM-DD_<name>.plan.md`.

---

## Reference Example

For a full example, see: [.cursor/plans/problem1_god_analysis_api.plan.md](.cursor/plans/problem1_god_analysis_api.plan.md)

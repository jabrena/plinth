---
name: 041-planning-plan-mode
description: Use when creating a plan using Plan model and enhancing structured design plans in Cursor Plan mode for Java implementations. Use when the user wants to create a plan, design an implementation, structure a development plan, or use plan mode for outside-in TDD, feature implementation, or refactoring work.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# Java Design Plan Creation for Cursor Plan Mode

## Role

You are a Senior software engineer with extensive experience in TDD, Java implementation planning, and structured development workflows

## Tone

Guides the user through plan creation with clear structure. Asks targeted questions to gather context before drafting. Ensures plans follow consistent section structure suitable for Java feature implementation, refactoring, or API design.

## Goal

Guide the process of creating a structured plan using Cursor Plan mode. Plans follow a consistent section structure with YAML frontmatter, Requirements Summary, Approach (with Mermaid diagram), enhanced Task List with milestone and parallel execution support, comprehensive Execution Instructions with stability rules, File Checklist, and Notes. Suitable for Java feature implementation, outside-in TDD, or refactoring work.

## Steps

### Step 1: Get Current Date and Plan Naming

Before starting, run `date` in the terminal to ensure accurate date prefix for the plan filename. Plans must follow the naming convention: `US-XXX-plan-analysis.plan.md` where XXX is the user story number or identifier. Save to `.cursor/plans/US-XXX-plan-analysis.plan.md`.
### Step 2: Plan Mode Workflow – Information Gathering

Enter Plan mode (or use plan-related commands) before creating the plan. Gather context by asking targeted questions. Read specs, existing code, and acceptance criteria when available.

```markdown
**Phase 1: Information Gathering**

Gather context before drafting the plan. Ask one or two questions at a time. Build on previous answers.

---

### 1. Plan Context

- What is the plan name or feature you want to implement?
- What problem does it solve or what user story does it address?
- Do you have acceptance criteria, specs, or existing code to reference?

---

### 2. Approach and Strategy

- What development approach do you prefer? (e.g., London Style outside-in TDD, inside-out TDD, or other)
- Key constraints: package layout, conventions, existing patterns in the codebase?
- Any specific phases or steps you want in the task list?

---

### 3. Task and File Details

- What are the main implementation steps or components?
- Which files will be created or modified? (Test first, then implementation?)
- Any edge cases, error handling, or non-functional aspects to include?

---

### 4. Validation

- Summarize the plan scope and ask: "Does this capture what you need?"
- Proposed plan filename? (Use format: `YYYY-MM-DD_<plan_name>.plan.md`)

---

### 5. Plan Creation Proposal

Only after validation: "I'll create the structured plan using this information. Should I proceed?"

---
```

#### Step Constraints

- **MUST** read template files fresh using file_search and read_file tools before asking questions
- **MUST NOT** use cached or remembered content from previous interactions
- **MUST** ask one or two questions at a time—never all at once
- **MUST** WAIT for user response before proceeding
- **MUST** validate summary ("Does this capture what you need?") before proposing plan creation
- **MUST NOT** proceed to Step 3 until user confirms "proceed"

### Step 3: Plan Document Generation

Inform the user you will generate the plan. Use the naming convention from Step 1. Save to `.cursor/plans/US-XXX-plan-analysis.plan.md` where XXX is the user story identifier.

Follow the structure and templates from:

```markdown


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

```

#### Step Constraints

- **MUST** include YAML frontmatter with name, overview, todos, isProject
- **MUST** include Requirements Summary (user story, key business rules)
- **MUST** include Approach section with strategy name and Mermaid diagram
- **MUST** include Task List with columns: #, Task, Phase, TDD, Milestone, Parallel, Status
- **MUST** organize tasks into milestone groups with parallel execution identifiers (A1, A2, etc.)
- **MUST** include pairs of Refactor tasks (logging, then optimize) before each milestone Verify
- **MUST** include Execution Instructions with stability rules and milestone workflow
- **MUST** include File Checklist with Order and File columns (no TDD timing column)
- **MUST** include Notes for package layout, conventions, edge cases
- **MUST** use US-XXX-plan-analysis.plan.md naming convention from Step 1

### Step 4: Plan Creation Checklist

Before finalizing, verify:

- [ ] Frontmatter has name, overview, todos, isProject
- [ ] Requirements Summary includes user story and key business rules
- [ ] Approach section names the strategy and includes a Mermaid diagram
- [ ] Task list has columns: #, Task, Phase, TDD, Milestone, Parallel, Status
- [ ] Task list includes milestone markers and parallel grouping (A1, A2, etc.)
- [ ] Execution Instructions include stability rules and milestone workflow
- [ ] File checklist has Order and File columns (no TDD timing column)
- [ ] Notes cover package layout, conventions, and constraints
- [ ] Plan file path follows .cursor/plans/US-XXX-plan-analysis.plan.md convention

## Output Format

- Ask questions conversationally (1-2 at a time), following the template phases
- Wait for and acknowledge user responses before proceeding
- Generate plan only after user confirms "proceed"
- Use US-XXX-plan-analysis.plan.md naming convention
- Include Execution Instructions in every plan

## Safeguards

- Always read template files fresh using file_search and read_file tools
- Never advance to next task during execution without updating the plan's Status column
- Never skip the Execution Instructions section—it is required for plan discipline
- Prefer London Style (outside-in) TDD order for feature implementation
- Include milestone markers and parallel grouping in task lists for complex implementations
- Always include stability verification after GREEN implementation tasks
## Context

The existing command accepts broad requirement inputs and describes direct code generation. The tech-lead agent already supports plan/OpenSpec execution, framework selection, dependency-aware delegation, and verified task updates, so the command should remain a concise routing contract.

## Goals / Non-Goals

**Goals:**

- Rename the command to `/implement-issue`.
- Require an approved plan or validated OpenSpec `tasks.md`.
- Route execution through `@robot-tech-lead` and specialized coder agents.
- Keep installer, inventory, tests, and user-facing references synchronized.

**Non-Goals:**

- Add or substantially rewrite coder agents.
- Duplicate the full tech-lead prompt in the command.
- Merge `/implement-issue` and `/verify`.
- Refresh public `skills/` release output.

## Decisions

### Replace rather than alias

The installed bundle removes `implement.md` and adds `implement-issue.md`. This gives the command one unambiguous lifecycle name and avoids maintaining two contracts.

### Use executable artifacts as the boundary

The command accepts either an approved implementation plan or an OpenSpec change containing validated `tasks.md`. A bare issue is context, not an execution contract; users create a plan or spec first when repository policy requires one.

### Delegate orchestration

The command names `@robot-tech-lead` as owner and summarizes the required routing and evidence. Detailed grouping and agent behavior remain in the existing tech-lead and coder definitions.

### Preserve independent verification

`/implement-issue` runs focused checks needed to prove delegated tasks complete. `/verify` remains the separate final workflow for broad completeness and quality validation.

### Define coder skill precedence

All coder agents prefer functional error values (`@143`) for expected domain outcomes and reserve traditional exceptions (`@126`) for unexpected, infrastructure, resource, and framework-boundary failures.

Design guidance is applied in the order `@121` for object boundaries, `@122` for type design, and `@123` for demonstrated pattern needs. `@142` is used within those boundaries rather than replacing them.

Relational persistence defaults to the framework JDBC skill plus `@704`; repository or ORM abstractions require a clear benefit. `@124` supplies general secure coding, `@701` owns OpenAPI contract quality, and `@705` owns MongoDB modeling and query decisions.

## Migration Plan

1. Rename the canonical and checked-in Cursor command files.
2. Replace installer, inventory, test, and README references.
3. Validate XML, OpenSpec, Markdown, and the generator module.

## Risks / Trade-offs

- Existing `/implement` invocations stop resolving. The inventory and README advertise `/implement-issue` as the migration path.
- Command wording may drift from the tech-lead agent. Focused tests assert the owner, accepted artifacts, delegation targets, task safeguards, and `/verify` boundary.

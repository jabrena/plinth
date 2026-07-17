# implement-spec

## Purpose

Deliver an approved implementation plan or validated OpenSpec task list through controlled implementation.

## Usage

```text
/implement-spec <approved-plan|openspec-change> [task-or-group] [constraints]
```

## Accepted inputs

- An approved implementation plan (`*.plan.md`)
- An OpenSpec change containing a validated `tasks.md`
- Optional task or group selection, branch/worktree context, and implementation constraints

A bare issue is context, not an execution contract. When repository policy requires structured planning and neither executable artifact exists, stop and direct the user to provide an approved implementation plan or run `/create-spec`.

## Owner and delegation

- Owner: `@robot-tech-lead`
- Delegation targets: `@robot-java-coder`, `@robot-java-spring-boot-coder`, `@robot-java-quarkus-coder`, `@robot-java-micronaut-coder`, or `@robot-no-java`
- The tech lead coordinates delivery and MUST NOT implement application code directly.

## Mandatory execution contract

- If the command runner is not `@robot-tech-lead`, immediately delegate the whole command execution to `@robot-tech-lead` and wait for its result.
- `@robot-tech-lead` MUST invoke the selected implementation agent for implementation, test, and verification work; naming an agent in the response is not delegation.
- If agent invocation is unavailable in the current environment, stop and report that `/implement-spec` cannot proceed instead of implementing directly.
- Before any implementation agent starts, pass the branch/worktree gate below and report the selected isolation strategy.

## Branch/worktree gate

- Before choosing a branch or worktree strategy, inspect the workspace with `git status --short`.
- If the workspace is dirty, stop immediately and report the changed/untracked paths. Do not create a feature branch, create a worktree, delegate implementation, or ask for approval to continue in the dirty checkout.
- Continue only after the user cleans, commits, or stashes the workspace and reruns `/implement-spec`.
- Determine whether the selected artifact should run in the current checkout, a new feature branch, or one or more linked worktrees.
- If the work is serial and the current checkout is not already a safe, suitable feature branch, execute `/create-feature-branch` before delegating implementation.
- If independent groups can run in parallel or need isolation, execute `/create-worktree` for each independent branch/worktree before delegating implementation.
- Do not start implementation on `main` or the repository default branch unless the user explicitly approves that exception after being warned.
- If branch or worktree creation is blocked by unsafe git state, existing branches, existing worktrees, or ambiguous base references, stop and ask the user how to proceed.

## Workflow

1. Load the actual selected plan or OpenSpec `tasks.md` and confirm it is current, approved, and internally consistent.
2. Stop and request `/review-alignment` when the issue, ADRs, specification, plan, or task list conflicts materially.
3. Identify the framework from authoritative artifacts, build files, and code; select the matching specialized coder or `@robot-no-java` when the execution artifact does not use Java.
4. Extract task groups, dependencies, milestones, verification gates, and expected file ownership.
5. Complete the branch/worktree gate: use `/create-feature-branch` for serial work in the current checkout, or `/create-worktree` when independent groups can run safely in parallel.
6. Report the selected feature branch or worktree paths before delegating implementation.
7. Serialize dependent or overlapping groups; run groups concurrently only when dependencies and owned files do not conflict.
8. Invoke the selected implementation agent for each group with task IDs, owned files, acceptance criteria, blocked-by relationships, and focused validation commands.
9. Integrate delegated results and require changed-file, test, build, risk, and blocker evidence.
10. Mark OpenSpec tasks complete only after their acceptance criteria and focused verification gates pass.
11. Report completion against the selected artifact.

## Output

- Selected execution artifact and source reference
- Framework decision and coder routing rationale
- Feature-branch or worktree gate result, branch names, worktree paths, and rationale
- Serial/parallel execution map with dependencies and file ownership
- Results and changed files by task or group
- Test and build evidence
- Updated OpenSpec task status, when applicable
- Remaining blockers, risks, and follow-up work

## Safeguards

- Do not implement from a stale, unapproved, missing, or conflicting execution artifact.
- Do not continue in the original command runner when `@robot-tech-lead` has not accepted the orchestration handoff.
- Do not start implementation before the feature-branch or worktree gate has passed.
- Do not bypass a dirty workspace by asking for approval to continue; stop and resume only after the workspace is clean.
- Do not treat a written plan to delegate as delegation; invoke the selected implementation agent.
- Do not silently change issue scope, requirements, ADR decisions, or plan approach.
- Do not run dependent groups before prerequisite verification gates pass.
- Do not delegate concurrent groups with overlapping file ownership without an explicit integration strategy.
- Do not mark tasks complete before acceptance criteria and focused checks pass.
- Do not bypass repository validation or edit generated outputs directly.

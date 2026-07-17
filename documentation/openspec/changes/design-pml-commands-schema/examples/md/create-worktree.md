# create-worktree

## Purpose

Create a new branch and linked Git worktree for isolated or parallel work without changing the current checkout.

## Usage

```text
/create-worktree <issue-or-change|type description> [<target-path>] [<base-reference>]
```

## Accepted Inputs

- An issue/change identifier, or an explicit branch type and description
- Optional target path
- Optional base reference
- Supported branch types: `feat`, `fix`, `docs`, `refactor`, and `chore`

## Owning Agent

`@robot-tech-lead`

## Associated Capabilities

- Git branch naming and validation
- Git linked worktrees
- Parallel OpenSpec child-change coordination

## Workflow

1. Resolve the branch name from the issue/change identifier or explicit branch details.
2. Resolve an absolute target path and the base reference.
3. Resolve the repository default branch and the current branch.
4. Verify the current checkout is `main` or the repository default branch before creating the worktree branch.
5. If the current branch is not `main` or the default branch, stop and ask whether to switch to the default branch, choose a different base, or continue explicitly from the current branch.
6. Verify the repository, base reference, branch name, and target path are available.
7. Verify the branch is not already checked out in another worktree.
8. Run the equivalent of `git worktree add -b <branch> <target-path> <base-reference>`.
9. Report the created branch, absolute worktree path, and base reference.

## Output

- Created local branch
- Created linked worktree
- Exact branch name, absolute path, and base reference

## Safeguards

- Stop if the branch exists, is already checked out, or the target path exists.
- Stop and ask when the current branch is not `main` or the repository default branch.
- Stop if the base reference is invalid.
- Leave existing branches, worktrees, directories, and files unchanged on conflict.
- Do not commit, push, remove worktrees, delete branches, or use force.

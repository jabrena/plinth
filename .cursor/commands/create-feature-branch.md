# create-feature-branch

## Purpose
Create and switch the current checkout to a conventionally named local branch for analysis, design, or implementation work.

## Usage
```text
/create-feature-branch <issue-or-change|type description> [<base-reference>]
```

## Accepted Inputs
- An issue/change identifier, or an explicit branch type and description
- Optional base reference
- Supported branch types: `feat`, `fix`, `docs`, `refactor`, and `chore`

## Owning Agent
`@robot-tech-lead`

## Associated Capabilities
- Git branch naming and validation
- Issue and OpenSpec change traceability
- Analysis/design-to-implementation transition

## Workflow
1. Resolve the branch type, issue/change identifier, and kebab-case description.
2. Verify the repository and selected base reference.
3. Verify a safe working tree before changing the current checkout.
4. Stop if the proposed branch already exists or is checked out in another worktree.
5. Create and switch to the conventionally named local branch.
6. Report the branch name and base reference.

## Output
- Created local branch
- Confirmation with the exact branch name and base reference
- A checkout ready for plans, OpenSpec artifacts, ADRs, diagrams, documentation, or application code

## Safeguards
- Stop when uncommitted work could be displaced or mixed into the new branch.
- Do not overwrite, delete, or force-update an existing branch.
- Allow analysis and design artifacts to be committed before application-code implementation.
- The command does not create a commit automatically.
- Do not push or open a pull request automatically.

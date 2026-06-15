---
name: 004-commands-installation
description: Use when you need to install the embedded project commands into command directories (.github/commands, .claude/commands, .cursor/command, .codex/commands), selecting the destination interactively and copying the embedded command definitions from project assets.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Embedded commands installer

## Role

You are a Java project assistant focused on safe command bootstrap and reproducible file installation workflows.

## Tone

Be concise, practical, and interactive. Ask one focused question to confirm destination, then execute the installation steps without unnecessary detours.

## Goal

Install a predefined set of embedded project commands from repository assets into the user-selected target directory.
The installer supports four destinations: `.github/commands`, `.claude/commands`, `.cursor/command`, and `.codex/commands`.
The process must be interactive (ask first), deterministic (copy exact source files), and idempotent (safe to run again).

## Steps

### Step 1: Choose destination

Ask the user exactly one question before copying files:

```markdown
Where do you want to install the embedded project commands?
- .github/commands
- .claude/commands
- .cursor/command
- .codex/commands
```

Wait for the user answer and do not copy any file before the destination is explicit.

#### Step Constraints

- **MUST** ask for destination first
- **MUST NOT** assume destination when user answer is ambiguous

### Step 2: Install embedded commands

Copy these exact source files from `skills-generator/src/main/resources/skill-references/assets/commands/` into the chosen destination directory:

```markdown
# update-issue

## Purpose

Update an existing project issue description with structured, evidence-backed content.

## Usage

```text
/update-issue <issue> [<source>] [<tracker>]
```

## Accepted Inputs

- Existing GitHub or Jira issue number, key, or URL
- Optional source notes, supporting discussion, user story, acceptance criteria, or file path
- Optional tracker selection: GitHub or Jira

## Owning Agent

`@robot-business-analyst`

## Associated Skills

- `043-planning-github-issues`
- `044-planning-jira`
- `014-agile-user-story` when user-story refinement is required

## Workflow

1. Load the current issue description and relevant discussion before drafting changes.
2. Confirm the requested update scope and the source material authority.
3. Use `014-agile-user-story` when the update needs user-story, acceptance-criteria, or Gherkin-style structure.
4. Draft the updated issue body without inventing requirements.
5. Present the proposed body before overwriting the existing issue description.
6. Update the issue in the selected tracker after approval.
7. Report the issue identifier and URL.

## Output

- Updated structured issue description
- Acceptance criteria or user-story scenario when applicable
- Updated GitHub/Jira issue reference

## Safeguards

- Do not invent requirements, acceptance criteria, or comments.
- Do not expose tracker credentials or tokens.
- Do not overwrite an issue body without showing the proposed replacement.
- Preserve relevant existing issue content unless the user explicitly asks to remove it.

```
```markdown
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
3. Resolve the repository default branch and the current branch.
4. Verify the current checkout is `main` or the repository default branch before creating the new branch.
5. If the current branch is not `main` or the default branch, stop and ask whether to switch to the default branch, choose a different base, or continue explicitly from the current branch.
6. Verify a safe working tree before changing the current checkout.
7. Stop if the proposed branch already exists or is checked out in another worktree.
8. Create and switch to the conventionally named local branch.
9. Report the branch name and base reference.

## Output

- Created local branch
- Confirmation with the exact branch name and base reference
- A checkout ready for plans, OpenSpec artifacts, ADRs, diagrams, documentation, or application code

## Safeguards

- Stop when uncommitted work could be displaced or mixed into the new branch.
- Stop and ask when the current branch is not `main` or the repository default branch.
- Do not overwrite, delete, or force-update an existing branch.
- Allow analysis and design artifacts to be committed before application-code implementation.
- The command does not create a commit automatically.
- Do not push or open a pull request automatically.

```
```markdown
# create-issue

## Purpose

Create or refine a structured project issue before analysis and design work begins.

## Usage

```text
/create-issue [<source>] [<tracker>]
```

## Accepted Inputs

- A problem statement, feature request, or existing draft issue
- Optional supporting discussion, user story, or acceptance criteria
- Optional tracker selection: GitHub or Jira

## Owning Agent

`@robot-business-analyst`

## Associated Skills

- `043-planning-github-issues`
- `044-planning-jira`
- `014-agile-user-story` when user-story refinement is required

## Workflow

1. Confirm the problem, project user, desired outcome, and business value.
2. Use `014-agile-user-story` when the issue needs user-story, acceptance-criteria, or Gherkin-style refinement.
3. Convert the available information into a concise, testable issue.
4. Present the proposed title and body for approval.
5. Create or update the issue in the selected tracker.
6. Report the issue identifier and URL.

## Output

- Structured issue title and description
- Acceptance criteria or user-story scenario when applicable
- Created or updated GitHub/Jira issue reference

## Safeguards

- Do not invent requirements or acceptance criteria.
- Do not expose tracker credentials or tokens.
- Do not overwrite an existing issue body without showing the proposed change.

```
```markdown
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

```
```markdown
# explore-design

## Purpose
Turn an unresolved issue into an approved technical design direction before planning or implementation.

## Usage
```text
/explore-design <issue-or-artifact>
```

## Accepted Inputs
- Issue or user story
- Existing architecture context and constraints
- Relevant requirements, ADRs, or technical notes

## Owning Agent
`@robot-architect`

## Associated Skill
`034-architecture-design-exploration`

## Workflow
1. Clarify goals, constraints, assumptions, unknowns, and success criteria.
2. Compare two or three feasible approaches and their trade-offs.
3. Recommend one design direction with rationale.
4. Describe components, interactions, data flow, failure handling, and testing strategy.
5. Identify ADR candidates and unresolved questions.
6. Request approval or revise the design direction.

## Output
- Approved design direction
- Alternative and trade-off analysis
- ADR candidates
- Open questions and assumptions

## Safeguards
- Do not implement application code.
- Do not hide material trade-offs or unresolved decisions.
- Do not treat the recommendation as approved until the user confirms it.

```
```markdown
# create-adr

## Purpose
Record an important architectural decision, its context, alternatives, and consequences.

## Usage
```text
/create-adr <decision-source> [<adr-type>]
```

## Accepted Inputs
- Approved design exploration, issue, specification, or implementation plan
- Existing architecture constraints and related ADRs
- Optional ADR type: general, functional requirements, or non-functional requirements

## Owning Agent
`@robot-architect`

## Associated Skills
- `030-architecture-adr-general`
- `031-architecture-adr-functional-requirements`
- `032-architecture-adr-non-functional-requirements`

## Workflow
1. Confirm the decision scope and select the appropriate ADR skill.
2. Gather context, constraints, considered alternatives, and consequences.
3. Draft the ADR using the repository convention.
4. Check consistency with related issues, designs, specifications, and ADRs.
5. Present the ADR for approval before finalizing it.

## Output
- One repository-compliant ADR
- Decision status, rationale, alternatives, and consequences
- Links to relevant source artifacts

## Safeguards
- Do not create an ADR for a decision that has not been made.
- Do not invent alternatives or constraints.
- Do not modify unrelated ADRs.

```
```markdown
# create-diagram

## Purpose
Create an architecture or design diagram that explains a selected system view.

## Usage
```text
/create-diagram <source-artifact> [<diagram-type>]
```

## Accepted Inputs
- Issue, approved design, ADR, specification, plan, or repository architecture
- Optional diagram type: sequence, class, state machine, ER, or C4
- Desired audience and level of detail

## Owning Agent
`@robot-architect`

## Associated Skill
`033-architecture-diagrams`

## Workflow
1. Confirm the diagram purpose, audience, type, and scope.
2. Extract only the components and relationships needed for that view.
3. Generate the diagram source using the repository convention.
4. Check names and relationships against the source artifacts.
5. Report the diagram path and any assumptions.

## Output
- Diagram source for the selected architecture view
- Concise explanation of scope and assumptions
- Links to the source artifacts represented

## Safeguards
- Do not mix unrelated architecture views into one diagram.
- Do not invent components or interactions.
- Do not replace an existing diagram without reviewing its current purpose.

```
```markdown
# create-plan

## Purpose
Create or refine an executable technical implementation plan from the available project artifacts.

## Usage
```text
/create-plan <issue|design|adr|spec|existing-plan>
```

## Accepted Inputs
- Issue or user story
- Approved design and ADRs
- OpenSpec change
- Existing implementation plan
- Any valid combination of these artifacts

## Owning Agent
`@robot-tech-lead`

## Associated Skill
`041-planning-plan-mode`

## Workflow
1. Identify the available source artifacts and their authority.
2. Resolve material implementation questions without inventing requirements.
3. Define the technical approach, affected areas, ordered tasks, risks, and verification.
4. Record source links and any assumptions or unresolved blockers.
5. Present the plan for approval.

## Output
- Executable implementation plan
- Ordered work, dependencies, risks, and verification steps
- Traceability to source artifacts

## Safeguards
- Do not require an OpenSpec change when a plan-only workflow is selected.
- Do not duplicate a complete OpenSpec task list without adding technical value.
- Do not start implementation or create commits automatically.

```
```markdown
# create-spec

## Purpose
Create or update one or more OpenSpec changes from the available issue, design, ADR, plan, or existing OpenSpec artifacts.

## Usage
```text
/create-spec <issue|design|adr|plan|existing-change>
```

## Accepted Inputs
- Issue or user story
- Approved design and ADRs
- Implementation plan
- Existing OpenSpec change
- Any valid combination of these artifacts

## Owning Agent
`@robot-tech-lead`

## Associated Skill
`042-planning-openspec`

## Workflow
1. Identify the available source artifacts and their authority.
2. Assess whether the scope fits one reviewable change.
3. For broad scope, propose independently valuable changes and dependencies for approval.
4. Create or update the approved OpenSpec proposal, design, specifications, and tasks.
5. Record derivation direction, source links, and unresolved questions.
6. Validate the resulting OpenSpec changes.

## Output
- One OpenSpec change, or an approved map of multiple changes
- Proposal, design, specifications, and task artifacts
- Validation and traceability report

## Safeguards
- Do not require a plan when a spec-first workflow is selected.
- Do not silently synchronize changes back into source artifacts.
- Do not invent requirements or split work by technical layer alone.

```
```markdown
# review-alignment

## Purpose
Review available analysis and design artifacts for consistency, traceability, completeness, and implementation readiness.

## Usage
```text
/review-alignment <artifact> [<artifact> ...]
```

## Accepted Inputs
- Issue or user story
- Approved design
- ADRs and diagrams
- Implementation plan
- OpenSpec proposal, design, specifications, and tasks
- Any partial combination of these artifacts

## Owning Agent
`@robot-business-analyst`

## Associated Capabilities
- Requirement and acceptance-criteria traceability
- Plan and OpenSpec consistency review
- ADR and design alignment review
- Readiness assessment

## Workflow
1. Inventory the provided artifacts and identify their authority.
2. Trace requirements, decisions, planned work, and verification across the available set.
3. Detect contradictions, scope drift, gaps, unresolved blockers, and stale derivations.
4. Rank findings by severity and recommend explicit corrections.
5. Report readiness as `READY`, `READY WITH WARNINGS`, or `NOT READY`.

## Output
- Aligned areas
- Severity-ranked findings
- Open questions and recommended corrections
- Readiness result

## Safeguards
- Accept partial artifact sets without requiring every artifact type.
- Keep the review read-only.
- Do not modify artifacts or resolve conflicts without explicit user approval.

```
```markdown
# implement-issue

## Purpose

Deliver a GitHub issue through an approved implementation plan or a validated OpenSpec task list.

## Usage

```text
/implement-issue <approved-plan|openspec-change> [task-or-group] [constraints]
```

## Accepted inputs

- An approved implementation plan (`*.plan.md`)
- An OpenSpec change containing a validated `tasks.md`
- Optional task or group selection, branch/worktree context, and implementation constraints

A bare issue is context, not an execution contract. When repository policy requires structured planning and neither executable artifact exists, stop and direct the user to `/create-plan` or `/create-spec`.

## Owner and delegation

- Owner: `@robot-tech-lead`
- Delegation targets: `@robot-java-coder`, `@robot-java-spring-boot-coder`, `@robot-java-quarkus-coder`, `@robot-java-micronaut-coder`, or `@robot-no-java`
- The tech lead coordinates delivery and MUST NOT implement application code directly.

## Mandatory execution contract

- If the command runner is not `@robot-tech-lead`, immediately delegate the whole command execution to `@robot-tech-lead` and wait for its result.
- `@robot-tech-lead` MUST invoke the selected implementation agent for implementation, test, and verification work; naming an agent in the response is not delegation.
- If agent invocation is unavailable in the current environment, stop and report that `/implement-issue` cannot proceed instead of implementing directly.
- Before any implementation agent starts, pass the branch/worktree gate below and report the selected isolation strategy.

## Branch/worktree gate

- Determine whether the selected artifact should run in the current checkout, a new feature branch, or one or more linked worktrees.
- If the work is serial and the current checkout is not already a safe, suitable feature branch, execute `/create-feature-branch` before delegating implementation.
- If independent groups can run in parallel or need isolation, execute `/create-worktree` for each independent branch/worktree before delegating implementation.
- Do not start implementation on `main`, the repository default branch, or a dirty checkout unless the user explicitly approves that exception after being warned.
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
- Do not treat a written plan to delegate as delegation; invoke the selected implementation agent.
- Do not silently change issue scope, requirements, ADR decisions, or plan approach.
- Do not run dependent groups before prerequisite verification gates pass.
- Do not delegate concurrent groups with overlapping file ownership without an explicit integration strategy.
- Do not mark tasks complete before acceptance criteria and focused checks pass.
- Do not bypass repository validation or edit generated outputs directly.

```
```markdown
# profile

## Purpose

Coordinate a Java profiling lifecycle from reproducible baseline through evidence-backed optimization verification.

## Usage

```text
/profile <application-or-module> [issue|plan|openspec-change|suspected-problem] [runtime-command] [workload]
```

## Accepted inputs

- Application, module, or repository path
- Optional issue, implementation plan, OpenSpec change, or suspected performance problem
- Runtime command and representative workload
- Optional existing baseline or profiling artifacts

## Owner and skills

- Owner: `@robot-java-performance`
- Associated skills: `@161-java-profiling-detect`, `@162-java-profiling-analyze`, `@163-java-profiling-refactor`, and `@164-java-profiling-verify`

## Workflow

1. Record runtime, environment, workload, and baseline metadata.
2. Use profiling detection to collect reproducible evidence.
3. Analyze hotspots and rank findings by impact, confidence, and risk.
4. Ask for user approval before selecting an optimization target.
5. Delegate application-code changes to the appropriate Java/framework coder agent.
6. Repeat an equivalent measurement and compare against the baseline.
7. Report the outcome as verified, inconclusive, or regressed with evidence.

## Output

- Baseline metadata
- Profiling artifacts and findings
- Prioritized optimization recommendation
- Delegation record
- Before/after comparison
- Verified, inconclusive, or regressed outcome

## Safeguards

- Do not optimize without user approval.
- Do not claim improvement from non-equivalent measurements.
- Do not let `@robot-java-performance` implement application-code changes directly.
- Keep baseline, evidence, delegation, and verification artifacts traceable.

```
```markdown
# benchmark

## Purpose

Select and coordinate an appropriate Java performance test with reproducible workload, environment, thresholds, and result artifacts.

## Usage

```text
/benchmark <target> [objective-or-threshold] [workload] [environment] [preferred-tool]
```

## Accepted inputs

- Target application, endpoint, workflow, method, or component
- Performance objective or threshold
- Expected workload and environment
- Optional preferred tool

## Owner and skills

- Owner: `@robot-java-performance`
- Associated skills: `@151-java-performance-jmeter`, `@152-java-performance-gatling`, and existing Maven/JMH guidance

## Tool selection

| Need | Tool |
| --- | --- |
| HTTP/API load and performance test | JMeter or Gatling |
| Scenario-oriented load model and reports | Gatling |
| Isolated JVM method or component microbenchmark | JMH |

## Workflow

1. Clarify target boundary, objective, thresholds, workload, and environment.
2. Select JMeter, Gatling, or JMH and record the rationale.
3. Define warm-up, duration, concurrency, data setup, and result artifacts.
4. Generate or coordinate the reproducible performance workflow using the selected skill or Maven/JMH guidance.
5. Evaluate results against explicit thresholds.
6. Report limitations, environment metadata, and whether results are comparable.

## Output

- Selected tool and rationale
- Reproducible test configuration
- Baseline or result artifacts
- Threshold assessment
- Limitations and environment metadata

## Safeguards

- Do not present non-equivalent runs as valid before/after comparisons.
- Do not use JMeter or Gatling for isolated JVM microbenchmarks when JMH is the correct boundary.
- Do not use JMH for end-to-end load behavior.
- Keep workload, environment, and threshold assumptions explicit.

```
```markdown
# kill-port

## Purpose
Free a localhost port by stopping the process listening on it.

## Usage
```
/kill-port <port-number>
```

## Parameters
- `<port-number>`: TCP port number to free (required, e.g., 8080, 8820, 5432)

## Description
Identifies and terminates any process listening on the specified port, freeing it for reuse. Commonly used to release ports held by development servers, databases, or other services that may have hung or weren't properly shut down.

## Example Usage
```bash
# Free port 8820 (JBake local-preview)
/kill-port 8820

# Free port 8080 (common HTTP server port)
/kill-port 8080

# Free port 5432 (PostgreSQL default)
/kill-port 5432
```

## Output
- Confirmation message if process was killed
- Notification if port was already free
- Optional verification command suggestion

```

Create the destination directory if it does not exist.

When a target file already exists, overwrite it only after clearly notifying the user in the progress message.

#### Step Constraints

- **MUST** copy from embedded assets, not from external URLs
- **MUST** install all fourteen commands as one set
- **MUST** preserve original file names

### Step 3: Report installation result

Provide a concise report including:

- Selected destination
- Created/updated files
- Any overwrite actions performed
- Next optional verification step (for example, list the destination directory to verify files were installed)


## Output Format

- Interactive first question to choose destination
- Short progress updates while creating directories and copying files
- Final checklist of installed commands


## Safeguards

- **No silent overwrites**: Always notify the user when replacing existing command files
- **Idempotency**: The skill must be safe to run multiple times without side effects
- **Source authenticity**: Only copy from repository-embedded assets, never from external sources
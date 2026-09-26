---
description: 'Establish repository guidance and one unambiguous OpenSpec project before issue work.'
argument-hint: ''
model: 'inherit'
agent: 'plinth-architect'
tools:
  - 'Read'
  - 'Write'
  - 'Edit'
  - 'Bash'
metadata:
  author: 'Juan Antonio Breña Moral'
  version: '0.19.0'
---

# onboarding

Establish the two repository prerequisites for issue-driven work: root `AGENTS.md` and exactly one unambiguous OpenSpec project.

## Usage

```text
/onboarding
```

## Owner and delegation

- Owner: `@plinth-architect`
- Missing root guidance: delegate to `200-agents-md` with the repository root as context.
- Missing OpenSpec project: delegate standard initialization to `042-planning-openspec` with the selected result directory and its parent project root as context.

`/onboarding` coordinates these existing workflows. It does not duplicate their interactive questions, generation rules, validation, or CLI safeguards.

## Prerequisite outcomes

- Preserve an existing root `AGENTS.md` unchanged.
- Recursively discover every directory whose name is exactly `openspec`; accept and preserve exactly one directory wherever it is located.
- When no OpenSpec directory exists, ask for a normalized repository-relative result path and offer `documentation/openspec` as the default.
- Stop before every mutation when more than one OpenSpec directory exists.

## Workflow

1. Resolve the repository root. Inspect `<repository-root>/AGENTS.md` and recursively discover the complete set of directories named exactly `openspec` before starting any delegated workflow or changing any file.
2. Evaluate OpenSpec ambiguity before mutation.
   - If more than one directory named `openspec` was found, report every conflicting repository-relative path, identify the ambiguity as technical debt, and stop.
   - Do not start `200-agents-md` or `042-planning-openspec`, even when root `AGENTS.md` is missing.
   - If exactly one was found, record and preserve that project without asking for another path or starting initialization.
3. When no directory named `openspec` exists, ask the user to select the resulting OpenSpec directory and offer `documentation/openspec` as the default.
   - Treat the selection as a result directory, not as the project-root argument to `openspec init`.
   - Normalize the selection before mutation.
   - Reject absolute paths, any normalized path that escapes the repository, and any path whose final segment is not exactly `openspec`.
   - On invalid input, explain the failure and ask for a valid repository-relative OpenSpec directory or an explicit cancellation.
   - If the user cancels, report cancellation and stop without starting either delegated workflow or changing repository content.
4. For a valid missing-OpenSpec selection, derive its parent directory as the initialization project root. For example:
   - `documentation/openspec` -> initialization project root `documentation`
   - `architecture/openspec` -> initialization project root `architecture`
Delegate only standard `openspec init` behavior to `042-planning-openspec`, passing both the selected result directory and derived parent project root. Do not pass the result directory itself as the project root. Do not silently choose another path after failure.
5. Sequence missing prerequisites safely.
   - If both prerequisites are missing, wait for delegated OpenSpec initialization to complete successfully before starting `200-agents-md`; never run the interactive delegations concurrently.
   - If OpenSpec initialization fails or is cancelled, do not start `200-agents-md` and report that both prerequisites remain incomplete.
   - If OpenSpec initialization succeeds, preserve it even if the later `200-agents-md` workflow fails or is cancelled; do not roll back delegated work.
6. When root `AGENTS.md` is missing and OpenSpec discovery is unambiguous, delegate its creation to `200-agents-md` with the repository root as context. When root `AGENTS.md` exists, preserve it unchanged and do not start its generation workflow.
7. Recheck root `AGENTS.md` and recursively rediscover directories named exactly `openspec` after delegation. Report each prerequisite as preserved, created, initialized, skipped, failed, cancelled, or blocked, include the OpenSpec path when present, and distinguish full success from partial completion.
8. On a retry after partial completion, preserve the one initialized OpenSpec project and retry only missing root guidance. When root `AGENTS.md` and exactly one OpenSpec project already exist, perform no writes, start no delegated workflow, and report the existing OpenSpec location.

## Output

- Root `AGENTS.md` outcome: preserved, created, skipped, failed, cancelled, or blocked
- OpenSpec outcome and repository-relative path: preserved, initialized, skipped, failed, cancelled, or blocked
- Overall onboarding result: successful, partially complete, cancelled, failed, or blocked by ambiguity
- Every conflicting OpenSpec location and technical-debt warning when discovery is ambiguous

## Safeguards

- Complete recursive OpenSpec discovery before every mutation or delegated workflow.
- Never modify an existing root `AGENTS.md` or existing OpenSpec project.
- Never select, merge, relocate, or initialize around multiple OpenSpec projects; report all conflicts and stop.
- Never accept an absolute, repository-escaping, non-normalized, or non-`openspec` result path.
- Never pass the selected OpenSpec result directory directly as the `openspec init` project root; pass its parent.
- Never run `200-agents-md` concurrently with `042-planning-openspec` or before missing OpenSpec initialization succeeds.
- Never roll back successful delegated work after a later failure or cancellation.
- Do not inspect project maturity, infer specifications from implementation, or create a custom specification baseline.
- Do not select or implement an issue.
- Do not claim success unless the final recheck finds root `AGENTS.md` and exactly one OpenSpec directory.

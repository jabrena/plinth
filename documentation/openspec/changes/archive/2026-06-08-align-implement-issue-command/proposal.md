## Why

GitHub issue [#809](https://github.com/jabrena/plinth/issues/809) identifies that `/implement` is a generic TDD prompt rather than a controlled route from an approved plan or OpenSpec task list to delegated implementation. Renaming it to `/implement-issue` makes the issue-driven lifecycle explicit while requiring an executable artifact before code delivery begins.

## What Changes

- **BREAKING**: Replace the installed `/implement` command with `/implement-issue`.
- Accept an approved implementation plan or an OpenSpec change with a validated `tasks.md`.
- Name `@robot-tech-lead` as owner and the existing framework coder agents as delegation targets.
- Define readiness, framework routing, dependency grouping, file ownership, verification evidence, and OpenSpec status-update safeguards.
- Align all coder agents on explicit error-model, design, security, API-contract, JDBC-first SQL, and MongoDB skill selection.
- Keep `/verify` as the separate workflow for independent final validation.
- Synchronize the command installer, inventory, focused tests, checked-in Cursor command, and localized README references.

## Capabilities

### New Capabilities

- `implement-issue-command`: Defines the issue-oriented implementation command, accepted execution artifacts, delegation workflow, outputs, and safeguards.

### Modified Capabilities

None.

## Impact

The command filename and invocation change for existing users, who must migrate from `/implement` to `/implement-issue`. Canonical command and coder-agent assets under `plinth-skills-generator`, installer XIncludes, command inventory sources, generator tests, checked-in Cursor commands and agents, and README links are affected. No new dependency, coder agent, or generated `skills/` release output is introduced.

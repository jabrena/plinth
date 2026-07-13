## Context

OpenSpec already provides `openspec archive <change-name>` to archive completed changes and reconcile specifications. Plinth can streamline this common maintainer workflow by exposing it as a dedicated command with consistent usage help, precondition checks, and user-friendly output.

## Goals / Non-Goals

**Goals:**

- Provide a `/close-spec <change-name>` command to archive an OpenSpec change by name.
- Validate command inputs and fail fast with actionable feedback.
- Run OpenSpec in the correct working directory context (typically `documentation/`).
- Keep command inventory, installation bundle, and tests aligned with the contract.

**Non-Goals:**

- Implement OpenSpec logic outside the OpenSpec CLI (Plinth remains a thin wrapper).
- Archive multiple changes in a single invocation.
- Auto-detect or guess the change name.

## Decisions

### Single required argument

`/close-spec` requires exactly one argument: the OpenSpec change name. Missing or empty arguments produce usage help.

### Precondition checks

The command checks for a usable OpenSpec CLI and for a change name that resolves to an existing change before invoking archive.

### Execute in `documentation/`

The command executes `openspec archive <change-name>` from the `documentation/` folder to ensure it picks up project OpenSpec configuration and avoids requiring users to remember the correct working directory.

## Risks / Trade-offs

- Users without OpenSpec installed need a clear error message and guidance.
- If repository layouts vary, the command may need to locate `documentation/` more flexibly in a follow-up change.


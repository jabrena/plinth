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

### Ownership

The command is owned by `@robot-architect` because it is part of the OpenSpec lifecycle (creating, refining, and closing/archiving changes) rather than implementation delivery orchestration.

### Execution workflow (happy path)

`/close-spec <change-name>` behaves as a thin wrapper around the OpenSpec CLI:

1. Validate argument presence and basic shape (non-empty).
2. Verify OpenSpec CLI availability (e.g. `openspec --version`).
3. Verify the change exists before archiving:
   - Preferred: `openspec list` and match `<change-name>` in the change list (deterministic, no side effects).
   - Alternative: `openspec show <change-name>` and treat “not found” as an unknown-change error.
4. Execute `openspec archive <change-name>` from the `documentation/` working directory.
5. Report success with the archived change id and any relevant follow-up hint (e.g. re-run `openspec validate --all` if required by the repository workflow).

### Single required argument

`/close-spec` requires exactly one argument: the OpenSpec change name. Missing or empty arguments produce usage help.

### Precondition checks

The command checks for a usable OpenSpec CLI and for a change name that resolves to an existing change before invoking archive.

### Execute in `documentation/`

The command executes `openspec archive <change-name>` from the `documentation/` folder to ensure it picks up project OpenSpec configuration and avoids requiring users to remember the correct working directory.

### Error model and user-facing output

The command should standardize failure modes with actionable guidance:

- Missing argument: show usage `/close-spec <change-name>` and an example.
- Unknown change: report “change not found” and suggest `openspec list` (or equivalent) to discover valid ids.
- OpenSpec unavailable: report that OpenSpec is required and provide the canonical install/enable hint used by this repository.
- Archive failure: surface the OpenSpec CLI error output and stop (do not claim success).

### Compatibility / breaking-change review

- This is **NON-BREAKING**: it adds a new command and does not modify existing command contracts.
- No feature toggle is needed: the command is opt-in and does not change runtime behavior.

### Verification strategy

Add focused tests in the command generator module to prevent drift:

- Inventory test: `/close-spec` appears in the generated command inventory.
- Installer test: `/close-spec` is included in the installed bundle output.
- Contract test: the generated markdown contains the required usage line and the `openspec archive <change-name>` behavior (or equivalent wording), plus the required error cases.

## Risks / Trade-offs

- Users without OpenSpec installed need a clear error message and guidance.
- If repository layouts vary, the command may need to locate `documentation/` more flexibly in a follow-up change.


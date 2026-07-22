## Why

GitHub issue [#1074](https://github.com/jabrena/plinth/issues/1074) identifies that `/implement-spec` can delegate implementation before its OpenSpec input contains concrete acceptance evidence or an explicit implementation location. The delivery workflow needs a mandatory readiness gate owned by `@robot-tech-lead` so implementation begins only from an ATDD-ready specification and a confirmed Git location.

## What Changes

- Require `@robot-tech-lead` to verify many-to-many traceability between concrete acceptance scenarios and the behavior-changing implementation or verification tasks in the selected execution scope before any implementation delegation.
- Stop `/implement-spec` with actionable remediation when the required acceptance evidence is absent; the contributor must update the OpenSpec change and rerun the command.
- Use an implementation location supplied by command constraints or a canonical `## Implementation Location` section in the selected change's `design.md`; when neither is present or valid, ask the contributor to select `main`, a feature branch, or a worktree before continuing.
- Preserve the existing dirty-workspace stop and require the existing warning plus explicit approval before implementation on `main` or the repository default branch.
- Add focused command and agent contract coverage for readiness failures and location resolution.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `implement-spec-command`: Add mandatory acceptance-evidence and implementation-location readiness behavior before delegation.
- `analysis-design-agents`: Make OpenSpec readiness verification an explicit `robot-tech-lead` responsibility.

## Impact

- Authoritative command source: `plinth-commands-generator/src/main/resources/commands/implement-spec.xml`.
- Authoritative agent source: `plinth-agents-generator/src/main/resources/agents/robot-tech-lead.xml`.
- Focused generator assertions and Gherkin acceptance scenarios in both generator modules.
- Generated `.cursor/commands/` and `.cursor/agents/` assets remain read-only outputs and are not edited directly.
- **Compatibility:** This intentionally makes readiness stricter and changes missing-location behavior from automatic strategy selection to an explicit question. Existing explicit invocation locations continue to work, and `main` remains protected by its current warning and approval gate.
- **Migration:** Callers should add an `## Implementation Location` section to `design.md` when non-interactive execution is required and ensure every selected behavior-changing task has scenario support while every applicable scenario has implementation or verification coverage before invoking `/implement-spec`.

## Source Artifacts and Derivation

| Source | Authority | Derivation direction |
|--------|-----------|----------------------|
| [Issue #1074](https://github.com/jabrena/plinth/issues/1074) | User story, functional requirements, acceptance criteria | Issue → this OpenSpec change |
| [Functional Specification comment](https://github.com/jabrena/plinth/issues/1074#issuecomment-5050947684) | Problem framing, root cause, assumptions, context, quality attributes | Confirmed issue comment → proposal, design, requirements, tasks |
| `documentation/openspec/specs/implement-spec-command/spec.md` | Existing command behavior | Existing specification → modified capability delta |
| `documentation/openspec/specs/analysis-design-agents/spec.md` | Existing tech-lead responsibilities | Existing specification → modified capability delta |

Derivation is one-way for this change. OpenSpec artifacts do not silently rewrite the issue or its comments.

## Unresolved Questions

- The exact user-facing wording for readiness failures and location questions remains an implementation detail, provided it is actionable and preserves the required stop/ask behavior.
- No architecture-blocking question remains after design refinement; exact prompt prose may be selected during test-driven implementation.

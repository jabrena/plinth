## Context

`/implement-spec` currently accepts an OpenSpec change containing a validated `tasks.md`, routes execution through `@robot-tech-lead`, and applies a dirty-workspace plus branch/worktree gate before delegating. Validation alone does not prove that the change contains concrete acceptance evidence, and the current location strategy can be selected automatically when the execution artifact does not specify one.

Issue [#1074](https://github.com/jabrena/plinth/issues/1074) requires `@robot-tech-lead` to add an earlier readiness decision: stop when acceptance evidence is missing, and ask for an implementation location when the artifact and invocation do not provide one. The behavior spans the command contract and the tech-lead agent contract, whose authoritative XML sources live in separate Maven generator modules.

## Goals / Non-Goals

**Goals:**

- Define an observable OpenSpec acceptance-evidence gate before skill discovery, location setup, or coder delegation.
- Make the tech lead responsible for the gate in both direct agent use and `/implement-spec` orchestration.
- Resolve implementation location deterministically from explicit inputs and ask only when it is absent.
- Preserve existing dirty-workspace, `main`-branch warning, delegation, skill-discovery, and verification safeguards.
- Cover the new behavior through generated-command/agent assertions and focused Gherkin scenarios.

**Non-Goals:**

- Change approved-plan readiness semantics.
- Introduce an OpenSpec schema extension or a new parser.
- Select an implementation technology or refine application architecture.
- Change coder-agent routing, parallel grouping, or task-completion rules.
- Edit generated `.cursor/` assets directly.

## Decisions

### 1. Treat acceptance evidence as selected-scope semantic traceability

For an OpenSpec input, the execution scope is the explicitly selected task or group, or all incomplete tasks when no selection is supplied. Readiness requires many-to-many traceability across that scope:

1. each selected behavior-changing implementation task is supported by one or more concrete scenarios in `specs/**/spec.md`;
2. each scenario applicable to the selected scope has at least one actionable implementation or verification task; and
3. repository validation tasks map to an explicit quality, safeguard, or verification obligation even when they do not change behavior directly.

A concrete scenario has a defined trigger and observable outcome, includes any required precondition, and contains no placeholder such as `TBD` or unresolved wording that prevents objective verification. An actionable task identifies a specific implementation or verification outcome; a completed-only task list does not provide remaining execution work.

The tech lead performs this semantic review from the selected change rather than relying only on `openspec validate`. Structural validation remains necessary but is not sufficient.

**Alternatives considered:**

- **Validated `tasks.md` only:** rejected because structural validity does not establish concrete acceptance evidence.
- **Any one scenario plus any one task:** rejected because unrelated evidence can hide partial or missing coverage in the selected scope.
- **Scenario files only:** rejected because `/implement-spec` delegates from the task list and needs executable work tied to the specification.
- **New machine-readable OpenSpec field:** deferred because the requested change is a command and agent contract refinement, not a schema extension.

### 2. Stop rather than synthesize missing evidence

When selected-scope traceability is missing, partial, ambiguous, absent, or divergent, `/implement-spec` stops before skill discovery, Git-location changes, or delegation. Its response identifies the unsupported scenario or task and instructs the contributor to update the OpenSpec change and rerun the command.

The tech lead must not invent acceptance criteria, silently add tasks, or delegate planning work as part of this delivery command.

### 3. Resolve location by explicit precedence

The implementation location is resolved in this order:

1. an explicit location in the `/implement-spec` invocation constraints;
2. a canonical location recorded in the selected OpenSpec change's `design.md`; or
3. a contributor answer to a location question naming `main`, a feature branch, or a worktree.

The canonical artifact form is:

```markdown
## Implementation Location

- Strategy: `main` | `feature-branch` | `worktree`
- Reference: `<branch-name-or-worktree-path>`
```

`Strategy` is required. `Reference` is optional for `main` and may be omitted for a new feature branch or worktree that the existing creation command will name and report. An unsupported strategy, a blank strategy, or a reference that conflicts with the invocation is not silently guessed: explicit invocation constraints win; otherwise the command asks the contributor.

When the third case applies, the command waits for the answer before creating or selecting any branch or worktree and before delegation. Selecting `main` or the repository default branch still requires the existing warning and separate explicit approval. The dirty-workspace stop remains non-bypassable.

**Alternative considered:** Preserve automatic feature-branch/worktree selection when location is absent. Rejected because the confirmed functional requirement makes the missing location an explicit contributor decision.

### 4. Place readiness before delivery setup

The command and agent workflows use this order for OpenSpec execution:

1. Load and structurally validate the selected OpenSpec change.
2. Verify selected-scope scenario-to-task and task-to-scenario traceability.
3. Stop with remediation if the evidence gate fails.
4. Inspect the workspace and apply the existing dirty-workspace stop.
5. Resolve or ask for the implementation location.
6. Apply the existing `main`, feature-branch, or worktree safety behavior.
7. Perform skill discovery and delegate implementation.

This prevents unnecessary Git changes, skill reading, or agent delegation for an input that is not implementation-ready.

### 5. Keep command and agent contracts aligned at their XML sources

Implementation updates both authoritative sources:

- `plinth-commands-generator/src/main/resources/commands/implement-spec.xml`
- `plinth-agents-generator/src/main/resources/agents/robot-tech-lead.xml`

Focused Java assertions verify stable generated-Markdown markers, while command and agent Gherkin scenarios describe externally observable behavior. Maven generation remains responsible for derived Markdown.

**Alternative considered:** Introduce a shared prompt fragment or a new generator abstraction for the readiness policy. Rejected for this change because the command and agent generators have separate source contracts; explicit aligned wording plus focused drift tests reveals intent with fewer new elements.

## Refined Component and Interaction Model

| Component | Responsibility | Boundary |
|-----------|----------------|----------|
| `/implement-spec` command contract | Establish gate order, location precedence, user-facing stop/ask behavior, and delegation preconditions | Does not create missing acceptance evidence or implement code |
| `@robot-tech-lead` agent contract | Perform semantic readiness review, ask for unresolved location, and stop or delegate | Does not refine OpenSpec requirements during delivery |
| OpenSpec `specs/**/spec.md` | Own concrete requirement scenarios | Does not track execution status |
| OpenSpec `tasks.md` | Own selected execution scope and completion status | Does not replace requirement scenarios |
| OpenSpec `design.md` | Optionally record the canonical implementation-location strategy | Does not bypass Git safety gates |
| Existing branch/worktree commands | Create or select the confirmed location and report it | Run only after readiness and dirty-workspace checks pass |

The data flow is: invocation and selected task scope → structural OpenSpec validation → bidirectional scenario/task traceability → workspace safety check → location precedence/clarification → existing branch protection → skill discovery → coder delegation.

Failure handling is fail-closed. Missing or ambiguous evidence stops with artifact-specific remediation; missing or invalid location asks the contributor; dirty state and unsafe default-branch execution retain their existing stop and approval behavior.

## Test-First Implementation Strategy

### Two-step sequence

**Step 1 — behavior-preserving preparation:** Strengthen characterization assertions for the existing delegation, skill-discovery, dirty-workspace, branch/worktree, and `main` approval safeguards in generated command and agent Markdown. Run focused module tests before changing prompt behavior.

**Step 2 — intended behavior change:** Add failing contract tests for selected-scope evidence and location resolution, update only the authoritative command and agent XML, then refactor wording while the focused tests remain green.

### TDD test list

1. A selected scope with complete bidirectional scenario/task evidence proceeds to workspace checks.
2. No scenario, placeholder/ambiguous scenario, no incomplete actionable task, or partial selected-scope coverage stops before side effects.
3. An invocation location overrides `design.md` location without another question.
4. A valid `## Implementation Location` strategy is used when invocation constraints omit location.
5. Missing, blank, or unsupported strategy asks the contributor and waits.
6. `main` retains its warning and separate approval; feature-branch and worktree selections retain existing creation safeguards.
7. Command and tech-lead generated Markdown expose aligned policy markers.

### Testing-strategy review

- **RIGHT-BICEP:** Right-result coverage is the complete-evidence happy path; boundary and error coverage includes absent, ambiguous, completed-only, partially mapped, and invalid-location inputs; cross-checks compare command and agent policy markers. No performance test is justified.
- **A-TRIP:** Java contract tests remain automatic, repeatable, independent substring assertions over generated Markdown. Gherkin prompt executions provide higher-level acceptance evidence and must record environment-dependent skips rather than being treated as automatic unit tests.
- **CORRECT:** Conformance covers supported location values and canonical headings; ordering covers invocation-over-artifact precedence; existence and cardinality cover zero/one/many scenarios and tasks; reference covers explicit branch/worktree references. Range and time do not apply.

## Compatibility and Rollout

- **POTENTIALLY BREAKING — readiness contract:** Existing OpenSpec changes that are structurally valid but lack selected-scope traceability will stop. Migration is to add concrete scenarios and actionable mapped tasks.
- **POTENTIALLY BREAKING — interaction contract:** Runs that previously relied on automatic location choice will pause for a contributor answer. Non-interactive callers can use invocation constraints or the canonical `design.md` section.
- **NON-BREAKING — stable surfaces:** Command name, accepted OpenSpec input, owner, coder routing, dirty-workspace stop, generated-output ownership, and `main` approval remain unchanged.
- **UNKNOWN until generated-output review:** Exact Markdown placement and duplication can drift between command and agent output; focused assertions and generated-file review resolve this before promotion.

No feature toggle is warranted because this is an atomic prompt-contract release with no concurrent runtime versions or persistent data. Rollout occurs through the normal generator build and installation workflow; rollback is a Git revert followed by regeneration. Public `skills/`, `.cursor/` outputs, website `docs/`, and XSLT are outside direct-edit scope.

## ADR Candidates

No ADR is required for this local command/agent policy. If `## Implementation Location` becomes a shared contract consumed by additional commands, promote that cross-workflow metadata convention to an ADR or common OpenSpec capability before reuse.

## Risks / Trade-offs

- **Semantic interpretation of “concrete” could vary between agents** → Define trigger/outcome/no-placeholder criteria and bidirectional selected-scope traceability explicitly in both prompt contracts and tests.
- **The added question can make previously non-interactive runs interactive** → Preserve invocation constraints and artifact-defined locations as higher-precedence non-interactive inputs.
- **Users may interpret `main` support as removing safeguards** → Retain the existing warning and explicit-approval requirement in the new scenarios.
- **Command and agent wording may drift** → Add parallel focused assertions and Gherkin coverage in both generator modules.
- **Existing OpenSpec changes may fail the stronger readiness gate** → Return actionable remediation and document the migration requirement instead of weakening the gate.

## Migration Plan

1. Add behavior-preserving characterization assertions and verify the existing command and agent contracts.
2. Add focused failing assertions/scenarios for the readiness and location contracts and confirm the expected red signal.
3. Update the command and tech-lead XML sources with the smallest policy change that makes the tests pass.
4. Validate both XML sources against their current schemas.
5. Run the focused generator-module verifications and review generated prompt output.
6. Roll back by reverting the XML-source and focused-test changes and regenerating; no persistent data migration is involved.

## Open Questions

- Exact user-facing prose may be refined during implementation, but it must identify the unsupported scenario or task, stop delivery, and explain that the OpenSpec change must be updated before rerunning.
- No architecture-blocking question remains. The canonical location convention should become an ADR candidate only if later changes reuse it outside `/implement-spec` and `@robot-tech-lead`.

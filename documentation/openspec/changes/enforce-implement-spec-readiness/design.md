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

### 1. Treat acceptance evidence as a two-part semantic contract

For an OpenSpec input, readiness requires both:

1. at least one concrete acceptance scenario in `specs/**/spec.md`; and
2. at least one actionable incomplete checklist task in `tasks.md` that implements or verifies the specified behavior.

The tech lead performs this semantic review from the selected change rather than relying only on `openspec validate`. Structural validation remains necessary but is not sufficient.

**Alternatives considered:**

- **Validated `tasks.md` only:** rejected because structural validity does not establish concrete acceptance evidence.
- **Scenario files only:** rejected because `/implement-spec` delegates from the task list and needs executable work tied to the specification.
- **New machine-readable OpenSpec field:** deferred because the requested change is a command and agent contract refinement, not a schema extension.

### 2. Stop rather than synthesize missing evidence

When either evidence part is absent, `/implement-spec` stops before skill discovery, Git-location changes, or delegation. Its response identifies the missing artifact or evidence and instructs the contributor to update the OpenSpec change and rerun the command.

The tech lead must not invent acceptance criteria, silently add tasks, or delegate planning work as part of this delivery command.

### 3. Resolve location by explicit precedence

The implementation location is resolved in this order:

1. an explicit location in the `/implement-spec` invocation constraints;
2. a location recorded in the selected OpenSpec change; or
3. a contributor answer to a location question naming `main`, a feature branch, or a worktree.

When the third case applies, the command waits for the answer before creating or selecting any branch or worktree and before delegation. Selecting `main` or the repository default branch still requires the existing warning and separate explicit approval. The dirty-workspace stop remains non-bypassable.

**Alternative considered:** Preserve automatic feature-branch/worktree selection when location is absent. Rejected because the confirmed functional requirement makes the missing location an explicit contributor decision.

### 4. Place readiness before delivery setup

The command and agent workflows use this order for OpenSpec execution:

1. Load and structurally validate the selected OpenSpec change.
2. Verify concrete specification scenarios and corresponding actionable tasks.
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

## Risks / Trade-offs

- **Semantic interpretation of “concrete” could vary between agents** → Define the required scenario-plus-actionable-task evidence explicitly in both prompt contracts and tests.
- **The added question can make previously non-interactive runs interactive** → Preserve invocation constraints and artifact-defined locations as higher-precedence non-interactive inputs.
- **Users may interpret `main` support as removing safeguards** → Retain the existing warning and explicit-approval requirement in the new scenarios.
- **Command and agent wording may drift** → Add parallel focused assertions and Gherkin coverage in both generator modules.
- **Existing OpenSpec changes may fail the stronger readiness gate** → Return actionable remediation and document the migration requirement instead of weakening the gate.

## Migration Plan

1. Add focused failing assertions/scenarios for the readiness and location contracts.
2. Update the command and tech-lead XML sources.
3. Validate both XML sources against their current schemas.
4. Run the focused generator-module verifications and review generated prompt output.
5. Roll back by reverting the XML-source and focused-test changes; no persistent data migration is involved.

## Open Questions

- Exact user-facing prose may be refined during implementation, but it must identify missing evidence, stop delivery, and explain that the OpenSpec change must be updated before rerunning.
- `/explore-design` may refine prompt organization and examples without weakening the specified readiness or location behavior.

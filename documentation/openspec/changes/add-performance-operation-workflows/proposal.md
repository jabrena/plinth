## Why

GitHub issue [#808](https://github.com/jabrena/cursor-rules-java/issues/808) identifies profiling and performance testing as the next operation lifecycle workflow after the analysis/design work delivered by issues #806 and #807. The repository already has performance and profiling skills, but users need concise commands and a coordinating agent that keep baselines, measurements, optimization delegation, and result evidence traceable.

## What Changes

- Add `/profile` as the profiling lifecycle command coordinated by a new `@robot-performance-engineer`.
- Add `/benchmark` as the performance-test selection command coordinated by `@robot-performance-engineer`.
- Add `robot-performance-engineer.md` as an operation-focused coordinator that delegates application-code optimizations to existing Java/framework coder agents.
- Align skills `151`, `152`, and `161`-`164` only where needed so command and agent assets compose existing behavior instead of duplicating it.
- Document JMeter, Gatling, and JMH selection boundaries, reproducibility safeguards, baseline authority, and before/after comparison rules.
- Keep `/deploy` and production access outside this change.

## Capabilities

### New Capabilities

- `performance-operation-workflows`: Defines profiling and benchmarking command contracts, performance engineer coordination, tool selection, delegation boundaries, and evidence requirements.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#808](https://github.com/jabrena/cursor-rules-java/issues/808), milestone `v0.16.0`.
- Related context: parent lifecycle gap issue #794 and completed analysis/design issues #806 and #807.
- Derivation direction: issue #808 -> OpenSpec change artifacts -> implementation tasks. The issue remains the source for problem, scope, acceptance criteria, and out-of-scope boundaries.

## Change Boundary Assessment

This is one parent OpenSpec change because `/profile`, `/benchmark`, and `@robot-performance-engineer` deliver one user-facing operation workflow outcome. Implementation may later be split into independently reviewable branches for command assets, agent assets, skill alignment, and documentation, but this OpenSpec change owns the shared requirements and completion criteria unless a separate child-change map is explicitly approved.

## Impact

Canonical command, agent, XML skill source, installer, inventory, focused generator tests, README, localized guides, and OpenSpec documentation may be affected. Generated `skills/`, `.cursor/rules/`, and `docs/` must not be edited directly. No deployment platform, production access workflow, or automatic optimization behavior is introduced.

## Context

The canonical embedded bundle contains a business analyst, coordinator, and four coder agents. A tracked legacy `.cursor/agents/robot-architect.md` exists but is not generated from the canonical embedded sources.

Issue #806 requires three coordinating roles: business analyst for need/traceability, architect for design/decisions/views, and tech lead for plan/spec creation and delivery delegation.

## Goals / Non-Goals

**Goals:**

- Promote architect behavior into canonical sources.
- Rename and expand coordinator behavior without losing coder routing.
- Keep business analysis and alignment read-only.
- Preserve strict no-direct-implementation boundaries.

**Non-Goals:**

- Change coder-agent missions or framework selection.
- Implement command files or XML planning skills.
- Update user-facing localized documentation in this parallel change.

## Decisions

### Promote the legacy architect

Use the tracked legacy architect as migration input, then make the embedded `skills-generator` asset authoritative. Extend it from diagram-only behavior to design exploration, ADRs, and diagrams.

### Rename coordinator to tech lead

`robot-tech-lead` owns creating/consuming planning artifacts and coordinating delivery. It continues to select and delegate to exactly one framework-specialized coder per engagement or plan boundary.

Alternative considered: add a planner and retain coordinator. Rejected because planning and delivery coordination fit one common technical leadership role.

### Keep coordinating agents out of implementation

Business analyst, architect, and tech lead do not implement application code as substitutes for coder agents.

### Make the rename explicit

The agent installer stops installing `robot-coordinator.md` and installs `robot-tech-lead.md`. Stale links are removed except for explicit migration notes.

## Risks / Trade-offs

- [Risk] Existing direct mentions break. -> Publish migration in the integration/documentation child and test for stale links.
- [Risk] Tech lead becomes too broad. -> Organize missions as create plan, create specification, and deliver selected workflow.
- [Risk] Architect overlaps with business analyst. -> Architect owns solution alternatives; business analyst owns need and cross-artifact consistency.

## Migration Plan

1. Add focused tests for expected names and links.
2. Promote and extend architect.
3. Rename and extend tech lead.
4. Extend business analyst.
5. Update installer and inventory.
6. Validate XML and generator output.

## Open Questions

- Decide during integration whether one release should temporarily retain a compatibility alias for `robot-coordinator.md`.

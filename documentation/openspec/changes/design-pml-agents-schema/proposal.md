## Why

GitHub issue [#992](https://github.com/jabrena/plinth/issues/992) requests an XSD-backed PML schema for agent definitions. Agents previously used a minimal `agents.xml` inventory plus freeform Markdown contracts with YAML frontmatter, while skills already follow validated `<prompt>` XML against PML schemas. Without a schema and XML source-of-truth path, agent structure was enforced only by substring tests and Gherkin acceptance coverage, which could not guarantee structural consistency or enable the same XML → generated Markdown pipeline established by [ADR-001](../../../adr/ADR-001-generate-cursor-rules-from-xml-files.md).

## What Changes

- Author production schema `agents.xsd` for individual `<agent>` documents (PML-aligned body: `<metadata>`, `<role>`, `<goal>`, optional `<constraints>` / `<steps>` / `<output-format>` / `<safeguards>`), hosted at `plinth-agents-generator/src/main/resources/agents.xsd`, with an OpenSpec mirror under `examples/xsd/pml/0.9.0/` (`agent.xsd` remains a shim include of `agents.xsd`).
- Keep inventory manifest `agents.xml` outside the XSD; inventory `@file` values point at XML agent sources under `agents/`.
- Store XML agent sources at `plinth-agents-generator/src/main/resources/agents/robot-*.xml` and validate them against `agents.xsd`.
- Generate Cursor-compatible `.md` (YAML frontmatter + body) at build time via `AgentMarkdownGenerator` / `AgentMarkdownRenderer` (`exec-maven-plugin` at `process-classes`) into `agents/` for the existing `plinth-skills-generator` bridge (`*.md` only).
- Map inventory XML `@file` to installer Markdown via `AgentIndexes.agentFiles()`; keep behavioral contracts in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin (not Schematron / `@kind` profiles).
- Document relationship to staged `pml.xsd` 0.9.0 (shared element shapes) and orthogonal `pml-workflow.xsd`; agents remain a parallel family, not a `<prompt>` reuse.
- Provide valid/invalid XML examples and migration notes from Markdown-first assets to XML sources + generated Markdown.

## Capabilities

### New Capabilities

- `pml-agents-schema`: Defines the XSD contract, PML relationship, examples, migration guidance, and build-time Markdown generation for agent definition sources consumed by the skills bridge.

### Modified Capabilities

None. Agent behavioral contracts in `analysis-design-agents` remain authoritative for missions, routing, and role boundaries; this change defines structural validation and the XML → Markdown delivery path only.

## Impact

This change delivers schema, XML sources, Java Markdown generation, and inventory/`AgentIndexes` wiring inside `plinth-agents-generator`. Generated `.md` continues to feed the existing `plinth-skills-generator` bridge; public `skills/` release output is not refreshed unless a separate release profile run is prepared. External publication of `agents.xsd` under the PML project hosting site remains deferred.

## Source Artifacts and Derivation

| Source | Authority | Derivation |
|--------|-----------|------------|
| [Issue #992](https://github.com/jabrena/plinth/issues/992) | Problem, scope, acceptance criteria, deliverables | OpenSpec proposal, spec, tasks |
| `plinth-agents-generator/src/main/resources/agents.xsd` | Production schema | Structural contract; OpenSpec mirror |
| `plinth-agents-generator/src/main/resources/agents/robot-*.xml` | XML agent sources | Authoritative contract surface |
| `plinth-agents-generator/src/main/resources/agents.xml` | Inventory discovery order | Installer order; `@file` → `.xml` |
| `AgentMarkdownGenerator` / `AgentMarkdownRenderer` | Build-time `.md` emission | Skills-bridge input |
| `documentation/openspec/specs/analysis-design-agents/spec.md` | Behavioral contract fields | Enforced outside XSD |
| Staged `examples/xsd/pml/0.9.0/pml.xsd` | Shared PML element shapes | Parallel family reference (not `<prompt>` reuse) |
| [ADR-001](../../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) | XSD → Markdown precedent | Delivery alignment |
| Archived change `2026-07-13-add-agents-generator-module` | Deferred non-goal baseline | Schema + generator delivery completes that gap |

**Derivation direction:** Issue #992 → OpenSpec change artifacts → local `agents.xsd` + XML sources + Java Markdown generation in `plinth-agents-generator` → skills-generator bridge consumes generated `.md` → future external PML publication (not required for this change).

## Design Decisions

| Question | Decision | Status |
|----------|----------|--------|
| Schema file name and location? | Canonical `agents.xsd` at `plinth-agents-generator/src/main/resources/agents.xsd`; OpenSpec mirror at `examples/xsd/pml/0.9.0/agents.xsd`; `agent.xsd` is a shim include | **Resolved** |
| Inventory inside XSD? | No — `agents.xml` (`<agent-inventory>`) stays outside `agents.xsd` | **Resolved** |
| Body model? | PML-aligned `<metadata>`, `<role>`, `<goal>` (+ optional structured sections); not the older `<frontmatter>` / `<missions>` design elements | **Resolved** |
| Required vs optional? | Required: `@id`, `<metadata>` with title/description, `<role>`, `<goal>`; optional constraints/steps/output-format/safeguards — see [`REQUIRED-OPTIONAL.md`](examples/xsd/pml/0.9.0/REQUIRED-OPTIONAL.md) | **Resolved** |
| How are installers fed? | Java generates `.md` beside XML; skills bridge copies `*.md` only from `agents/` | **Resolved** |
| XSD-only vs Schematron / `@kind`? | **XSD-only** — behavioral contracts stay in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin | **Resolved** |

See [`design.md`](design.md) for the delivery model, PML relationship, and validation layers.

## Handoff

After OpenSpec docs and validation complete, `@robot-tech-lead` can close remaining checklist items and coordinate any follow-up (external PML publication, release-profile skills refresh) outside this docs alignment slice.

## Why

GitHub issue [#992](https://github.com/jabrena/plinth/issues/992) requests an XSD-backed PML schema for agent definitions. Agents today use a minimal `agents.xml` inventory manifest plus freeform Markdown contracts with YAML frontmatter, while skills already follow validated `<prompt>` XML against [pml.xsd 0.8.0](https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd). Without a schema, agent structure is enforced only by substring tests and Gherkin acceptance coverage, which cannot guarantee structural consistency or enable the same XML → generated Markdown pipeline established by [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md).

## What Changes

- Design and publish a PML agent XSD covering the inventory manifest and structured agent definition elements.
- Document how the agent schema relates to existing `pml.xsd` and `pml-workflow.xsd` in the PML project.
- Produce a schema design document with inventory vs agent-body element mapping, namespace strategy, and frontmatter field mapping.
- Provide representative valid and invalid XML examples for at least one existing agent (`robot-business-analyst`).
- Record migration notes from current Markdown-first assets to schema-validated sources (implementation deferred).

## Capabilities

### New Capabilities

- `pml-agents-schema`: Defines the XSD contract, PML relationship, examples, and migration guidance for agent inventory and agent definition sources.

### Modified Capabilities

None. Agent behavioral contracts in `analysis-design-agents` remain authoritative for missions and routing semantics; this change defines structural validation only.

## Impact

This is a design-only change for milestone v0.18.0. It does not implement XSLT generation, migrate `plinth-agents-generator` sources, or refresh public `skills/` release output. Follow-up issues may consume the published schema for generator migration and CI validation.

## Source Artifacts and Derivation

| Source | Authority | Derivation |
|--------|-----------|------------|
| [Issue #992](https://github.com/jabrena/plinth/issues/992) | Problem, scope, acceptance criteria, deliverables | OpenSpec proposal, spec, tasks |
| `plinth-agents-generator/src/main/resources/agents.xml` | Current inventory shape | Inventory schema baseline |
| `plinth-agents-generator/src/main/resources/agents/*.md` | Current agent contract surface | Agent body element mapping |
| `documentation/openspec/specs/analysis-design-agents/spec.md` | Behavioral contract fields | Required structured sections |
| [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) | XSD → Markdown precedent | Design alignment |
| Archived change `2026-07-13-add-agents-generator-module` | Deferred non-goal | Confirms schema design is separate from extraction |

**Derivation direction:** Issue #992 → OpenSpec change artifacts → future PML XSD publication and generator migration (not in this change).

## Unresolved Questions

| Question | Recommendation | Status |
|----------|----------------|--------|
| Publish XSD in the external PML repo vs document as Plinth-only extension first? | Publish in PML project under `/pml/schemas/agent/1.0.0/` | **Recommended — pending approval** |
| Single combined XSD vs split inventory and agent-body schemas? | Split: `pml-agent-inventory.xsd`, `pml-agent.xsd`, `pml-agent-types.xsd` | **Resolved** |
| Require inline agent bodies in inventory vs file references during migration? | Keep `@file` `.md` references through Phase 2; allow `.xml` from Phase 2 | **Resolved** |
| Single `<missions>` element for all agents? | No — use `@kind` profiles with Schematron | **Resolved — pending approval** |
| XSD-only vs Schematron companion rules? | XSD superset + `pml-agent.sch` for kind profiles and id/name equality | **Resolved — pending approval** |

See [`design.md`](design.md) for the full refinement analysis and approval checkpoint.

## Handoff

After this change is approved and tasks complete, `@robot-tech-lead` can coordinate follow-up implementation issues for XSD publication, generator validation wiring, and Markdown source migration.

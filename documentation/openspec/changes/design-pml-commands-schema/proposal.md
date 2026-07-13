## Why

GitHub issue [#993](https://github.com/jabrena/plinth/issues/993) requests an XSD-backed PML schema for command definitions. Commands today use a minimal `commands.xml` inventory manifest plus freeform Markdown contracts with inconsistent section headings, while skills already follow validated `<prompt>` XML against [pml.xsd 0.8.0](https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd). Without a schema, command structure is enforced only by substring tests and Gherkin acceptance coverage, which cannot guarantee structural consistency or enable the same XML → generated Markdown pipeline established by [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md).

## What Changes

- Design and publish a PML command XSD covering the inventory manifest and structured command definition elements.
- Document how the command schema relates to existing `pml.xsd` and `pml-workflow.xsd` in the PML project.
- Produce a schema design document with inventory vs command-body element mapping, namespace strategy, and section normalization rules.
- Provide representative valid and invalid XML examples for at least one existing command (`update-issue`).
- Record migration notes from current Markdown-first assets to schema-validated sources (implementation deferred).

## Capabilities

### New Capabilities

- `pml-commands-schema`: Defines the XSD contract, PML relationship, examples, and migration guidance for command inventory and command definition sources.

### Modified Capabilities

None. Command behavioral contracts in `analysis-design-commands` remain authoritative for routing and workflow semantics; this change defines structural validation only.

## Impact

This is a design-only change for milestone v0.18.0. It does not implement XSLT generation, migrate `plinth-commands-generator` sources, or refresh public `skills/` release output. Follow-up issues may consume the published schema for generator migration and CI validation.

## Source Artifacts and Derivation

| Source | Authority | Derivation |
|--------|-----------|------------|
| [Issue #993](https://github.com/jabrena/plinth/issues/993) | Problem, scope, acceptance criteria, deliverables | OpenSpec proposal, spec, tasks |
| `plinth-commands-generator/src/main/resources/commands.xml` | Current inventory shape | Inventory schema baseline |
| `plinth-commands-generator/src/main/resources/commands/*.md` | Current command contract surface | Command body element mapping |
| `documentation/openspec/specs/analysis-design-commands/spec.md` | Behavioral contract fields | Required structured sections |
| [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) | XSD → Markdown precedent | Design alignment |
| Archived change `2026-07-13-add-commands-generator-module` | Deferred non-goal | Confirms schema design is separate from extraction |
| OpenSpec change `design-pml-agents-schema` | Parallel agent schema pattern | Shared PML extension conventions |

**Derivation direction:** Issue #993 → OpenSpec change artifacts → future PML XSD publication and generator migration (not in this change).

## Unresolved Questions

| Question | Recommendation | Status |
|----------|----------------|--------|
| Publish XSD in the external PML repo vs document as Plinth-only extension first? | Publish under `/pml/schemas/command/1.0.0/` alongside agent and skill schemas | **Recommended — pending approval** |
| Single combined XSD vs split inventory and command-body schemas? | Split: `pml-command-inventory.xsd`, `pml-command.xsd`, `pml-command-types.xsd` | **Resolved** |
| Normalize section titles in XSD or XSLT only? | Kind-specific XSLT templates (`standard`, `delivery`, `performance`, `cli`) | **Resolved** |
| Require associations and output on all commands? | No — `@kind` profiles; `cli` omits both | **Resolved** |
| XSD-only vs Schematron companion rules? | XSD superset + `pml-command.sch` | **Resolved pending approval** |

See [`design.md`](design.md) for the full refinement analysis and approval checkpoint.

## Handoff

After this change is approved and tasks complete, `@robot-tech-lead` can coordinate follow-up implementation issues for XSD publication, generator validation wiring, and Markdown source migration.

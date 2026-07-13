# PML Command Schema — Migration Notes (Draft)

This document accompanies OpenSpec change `design-pml-commands-schema` ([issue #993](https://github.com/jabrena/plinth/issues/993)). It describes how Plinth migrates from Markdown-first command contracts to XSD-validated XML sources. **Generator implementation is follow-up work.**

## Current state

| Artifact | Location | Validation today |
|----------|----------|------------------|
| Inventory manifest | `plinth-commands-generator/src/main/resources/commands.xml` | Java manifest load order; no XSD |
| Command contracts | `plinth-commands-generator/src/main/resources/commands/*.md` | Substring tests in `CommandIndexesTest`; Gherkin acceptance tests |
| Inventory template | `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` | Row parity tests |
| Installer skill | `004-commands-installation.xml` | Asset-link propagation tests |

## Target state

| Artifact | Target format | Validation |
|----------|---------------|------------|
| Inventory manifest | Namespaced `<command-inventory>` XML | `pml-command-inventory.xsd` |
| Command contracts | Namespaced `<command>` XML (one file per command) | `pml-command.xsd` |
| Generated Markdown | XSLT output with H1 title and normalized sections | Parity tests vs current contracts |
| Inventory template | Generated from validated inventory XML | Manifest-driven generation |

## Section mapping (illustrative)

| Common Markdown heading | XML element |
|-------------------------|-------------|
| `# command-name` (H1) | `@id` and optional `@slash` |
| `## Purpose` | `<purpose>` |
| `## Usage` | `<usage>` |
| `## Accepted Inputs` / `## Inputs` | `<accepted-inputs><input>` |
| `## Owning Agent` / `## Owner` | `<owning-agent>` |
| `## Associated Skills` / `## Associated Capabilities` | `<associated-skills><skill>` |
| `## Workflow position` | `<workflow-position>` |
| `## Workflow` | `<workflow><step>` |
| `## Output` | `<output><item>` |
| `## Safeguards` | `<safeguards><constraint>` |

Heading text MAY differ across commands during migration; XSLT normalizes to current Markdown conventions per command.

## Phased migration

### Phase 0 — Schema design (this change)

- Publish XSD drafts and examples.
- No changes to runtime installer input.

### Phase 1 — Optional validation

- Add parallel XML sources under a staging path (e.g. `commands-xml/`).
- Run `xmllint --schema` in `plinth-commands-generator` tests.
- Markdown remains authoritative.

### Phase 2 — Dual authoring with parity

- Author XML alongside Markdown for each command.
- Introduce XSLT to generate Markdown from XML ([ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) pattern).
- Parity tests assert generated Markdown matches substrings currently checked in `CommandIndexesTest`.

### Phase 3 — XML source of truth

- Remove hand-edited Markdown from `src/main/resources/commands/`.
- Generate Markdown during `generate-resources`.
- Update contributor docs and `004-commands-installation` asset paths if needed.

### Phase 4 — Enriched inventory

- Extend `commands.xml` with `@slash`, `<summary>`, and `<owning-agent>`.
- Generate `java-commands-inventory-template.md` from inventory XML.
- Validate installation order against `004-commands-installation.xml` asset links.

## Out of scope for migration notes

- Agent inventory schema ([#992](https://github.com/jabrena/plinth/issues/992))
- Skill `<prompt>` schema changes ([#991](https://github.com/jabrena/plinth/issues/991))
- Changing command behavioral contracts or routing
- Public `skills/` release promotion

## Rollback

Each phase is independently revertible until Phase 3. Keep Markdown sources until parity tests pass consistently across all twelve commands.

## Related work

- Parallel agent schema design: [`design-pml-agents-schema`](../design-pml-agents-schema/MIGRATION.md)
- PML schema families: skill (`pml.xsd`), workflow (`pml-workflow.xsd`), agent (`/schemas/agent/1.0.0/`), command (`/schemas/command/1.0.0/`)

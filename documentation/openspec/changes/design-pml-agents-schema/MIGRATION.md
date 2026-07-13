# PML Agent Schema — Migration Notes (Draft)

This document accompanies OpenSpec change `design-pml-agents-schema` ([issue #992](https://github.com/jabrena/plinth/issues/992)). It describes how Plinth migrates from Markdown-first agent contracts to XSD-validated XML sources. **Generator implementation is follow-up work.**

## Current state

| Artifact | Location | Validation today |
|----------|----------|------------------|
| Inventory manifest | `plinth-agents-generator/src/main/resources/agents.xml` | Java manifest load order; no XSD |
| Agent contracts | `plinth-agents-generator/src/main/resources/agents/*.md` | Substring tests in `AgentIndexesTest`; Gherkin acceptance tests |
| Inventory template | `plinth-agents-generator/src/main/resources/java-agents-inventory-template.md` | Row parity tests |
| Installer skill | `005-agents-installation.xml` | XInclude parity tests |

## Target state

| Artifact | Target format | Validation |
|----------|---------------|------------|
| Inventory manifest | Namespaced `<agent-inventory>` XML | `pml-agent-inventory.xsd` |
| Agent contracts | Namespaced `<agent>` XML (one file per agent) | `pml-agent.xsd` |
| Generated Markdown | XSLT output with YAML frontmatter | Parity tests vs current contracts |
| Inventory template | Generated from validated inventory XML | Manifest-driven generation |

## Frontmatter mapping

| Markdown YAML | XML |
|---------------|-----|
| `name` | `<frontmatter><name>` and `@id` |
| `model` | `<frontmatter><model>` |
| `description` | `<frontmatter><description>` |
| `readonly` | `<frontmatter><readonly>` or inventory `@readonly` |

## Section mapping (illustrative)

| Common Markdown heading | XML element |
|-------------------------|-------------|
| Opening persona paragraph | `<identity><paragraph>` |
| `## Missions` | `<missions><mission>` |
| `## Read-only boundary` / `### Core role` | `<role-boundaries><section>` |
| `### Collaboration partners` / routing tables | `<routing>`, `<collaboration>` |
| `## Output format` | `<output-format>` |
| `## Constraints` / safeguard bullets | `<safeguards>`, `<constraints>` |
| `## Workflow` | `<workflow>` |

Heading text MAY differ across agents during migration; XSLT normalizes to the current Markdown conventions per agent.

## Phased migration

### Phase 0 — Schema design (this change)

- Publish XSD drafts and examples.
- No changes to runtime installer input.

### Phase 1 — Optional validation

- Add parallel XML sources under a staging path (e.g. `agents-xml/`).
- Run `xmllint --schema` in `plinth-agents-generator` tests.
- Markdown remains authoritative.

### Phase 2 — Dual authoring with parity

- Author XML alongside Markdown for each agent.
- Introduce XSLT to generate Markdown from XML ([ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) pattern).
- Parity tests assert generated Markdown matches substrings currently checked in `AgentIndexesTest`.

### Phase 3 — XML source of truth

- Remove hand-edited Markdown from `src/main/resources/agents/`.
- Generate Markdown during `generate-resources`.
- Update contributor docs and `005-agents-installation` copy paths if needed.

### Phase 4 — Enriched inventory

- Extend `agents.xml` with `@id`, `@readonly`, and `<summary>`.
- Generate `java-agents-inventory-template.md` from inventory XML.
- Validate installation order against `005-agents-installation.xml` XIncludes.

## Out of scope for migration notes

- Commands inventory schema ([#993](https://github.com/jabrena/plinth/issues/993))
- Skill `<prompt>` schema changes ([#991](https://github.com/jabrena/plinth/issues/991))
- Changing agent roles, routing tables, or delegation contracts
- Public `skills/` release promotion

## Rollback

Each phase is independently revertible until Phase 3. Keep Markdown sources until parity tests pass consistently across all nine agents.

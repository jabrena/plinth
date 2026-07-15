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
| Command contracts | Namespaced `<command>` XML (one file per command) | `pml-command.xsd` + `pml-command.sch` |
| Generated Markdown | Kind-specific XSLT output | Parity tests vs current contracts |
| Inventory template | Generated from validated inventory XML | Manifest-driven generation |

## Command kind taxonomy

| `@kind` | Commands |
|---------|----------|
| `standard` | `update-issue`, `create-spec`, `explore-design`, `create-adr`, `create-diagram`, `review-alignment`, `create-feature-branch`, `create-worktree` |
| `delivery` | `implement-spec` |
| `performance` | `profile`, `benchmark` |
| `cli` | `close-spec` |

Inventory and body documents declare `@kind` to select Schematron profiles and XSLT templates.

## Section mapping by kind

### `standard`

| Markdown heading | XML element |
|------------------|-------------|
| `# command-name` | `@id`, `@slash` |
| `## Purpose` | `<purpose>` |
| `## Usage` | `<usage>` |
| `## Accepted Inputs` / `## Inputs` | `<accepted-inputs>` |
| `## Owning Agent` / `## Owner` | `<owning-agent>` |
| `## Associated Skills` / `## Associated Capabilities` / `## Associated Skill` | `<associations>` |
| `## Workflow position` | `<workflow-position>` |
| `## Workflow` | `<workflow>` |
| `## Output` | `<output>` |
| `## Safeguards` | `<safeguards>` |

### `delivery`

| Markdown heading | XML element |
|------------------|-------------|
| Standard sections | As above where applicable |
| `## Owner and delegation` | `<delegation>` |
| `## Mandatory execution contract` | `<execution-contract>` |
| `## Branch/worktree gate` | `<branch-worktree-gate>` |

### `performance`

| Markdown heading | XML element |
|------------------|-------------|
| `## Owner and skills` | `<ownership>` |
| `## Tool selection` | `<tool-selection>` (`benchmark` only) |
| Other sections | As in `standard` where applicable |

### `cli`

| Markdown heading | XML element |
|------------------|-------------|
| `## Purpose`, `## Usage`, `## Inputs`, `## Owner`, `## Workflow`, `## Safeguards` | Matching elements |
| *(absent)* | No `<associations>` or `<output>` |

## Validation layers

1. **XSD** — structural superset for all kinds.
2. **Schematron (`pml-command.sch`)** — `@kind` profiles, behavioral rules (e.g. explore-design skill exclusions, cli output prohibition).
3. **Java parity tests** — `CommandIndexesTest` substrings until XSLT parity replaces them.

## Phased migration

### Step 1 — Schema contract (this OpenSpec change)

- Publish XSD + Schematron + examples.
- No installer behavior change.

### Step 2 — Generator migration (follow-up), slice order

| Slice | Command(s) |
|-------|------------|
| 1 | `update-issue` |
| 2 | `create-spec`, `explore-design` |
| 3 | `create-adr`, `create-diagram`, `review-alignment` |
| 4 | `create-feature-branch`, `create-worktree` |
| 5 | `close-spec` |
| 6 | `profile`, `benchmark` |
| 7 | `implement-spec` |

Within each slice:

1. **Phase 1:** Parallel XML staging; XSD + Schematron in tests; Markdown authoritative.
2. **Phase 2:** Dual authoring; kind-specific XSLT; parity vs `CommandIndexesTest`.
3. **Phase 3:** XML source of truth; generate Markdown at `generate-resources`.
4. **Phase 4:** Enriched inventory; generate `java-commands-inventory-template.md` from XML.

## Out of scope

- Agent inventory schema ([#992](https://github.com/jabrena/plinth/issues/992))
- Skill `<prompt>` schema changes ([#991](https://github.com/jabrena/plinth/issues/991))
- Changing command behavioral contracts
- Public `skills/` release promotion

## Rollback

Each phase is independently revertible until Phase 3. Keep Markdown sources until parity tests pass consistently across all twelve commands.

## Related work

- Parallel agent schema design: [`design-pml-agents-schema`](../design-pml-agents-schema/MIGRATION.md)
- PML schema families: skill (`pml.xsd`), workflow (`pml-workflow.xsd`), agent (`/schemas/agent/1.0.0/`), command (`/schemas/command/1.0.0/`)

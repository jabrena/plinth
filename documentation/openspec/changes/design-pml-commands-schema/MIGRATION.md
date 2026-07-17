# PML Command Schema — Migration Notes

This document accompanies OpenSpec change `design-pml-commands-schema` ([issue #993](https://github.com/jabrena/plinth/issues/993)). Commands follow the **shipped agents-parity** model in `plinth-agents-generator`.

## Current / target state (cutover complete in generator module)

| Artifact | Location | Validation |
|----------|----------|------------|
| Inventory | `src/main/resources/commands.xml` (`@file="….xml"`) | `CommandIndexes` (Java) |
| Command sources | `src/main/resources/commands/*.xml` | `commands.xsd` (`xmllint --schema`) |
| Body schema | `src/main/resources/commands.xsd` | No target namespace |
| XSLT | `src/main/resources/command-to-markdown.xsl` | Build-time generation |
| Generated Markdown | `target/generated-resources/commands`, `target/classes/commands` | `CommandIndexesTest` substrings |
| Inventory template | `java-commands-inventory-template.md` | Row parity tests |

**Removed:** `src/main/resources/pml/schemas/command/1.0.0/` (namespaced split XSDs) and hand-authored `commands/*.md` under `src/`.

## Command kind taxonomy

| `@kind` | Commands |
|---------|----------|
| `standard` | `update-issue`, `create-spec`, `explore-design`, `create-adr`, `create-diagram`, `review-alignment`, `create-feature-branch`, `create-worktree` |
| `delivery` | `implement-spec` |
| `performance` | `profile`, `benchmark` |
| `cli` | `close-spec` |

Kind-profile section rules remain in `analysis-design-commands` and `CommandIndexesTest`.

## Section mapping

Narrative contract sections (Usage, Accepted Inputs, Owning Agent, associations, delegation, ownership, tool selection, workflow position, execution contract, branch/worktree gate) live as **Markdown inside `<goal><![CDATA[...]]></goal>`**, after the short purpose sentence (agents-style). XSLT emits `trim(goal)` with no `## Goal` wrapper.

| Markdown heading | XML |
|------------------|-----|
| `# command-name` | `@id` |
| Purpose + narrative sections | `<goal>` CDATA (includes `## Usage`, `## Accepted Inputs`, owner/associations, etc.) |
| `## Workflow` | `<steps>` / `<step>` |
| `## Output` | `<output-format>` |
| `## Safeguards` | `<safeguards>` (`safeguards-list` / `safeguards-item`) |

## Validation layers

1. **XSD** — `commands.xsd` structural core + optional kind extras.
2. **Java / tests** — inventory uniqueness, generated Markdown behavioral substrings, Gherkin.

## Out of scope

- Public `skills/` release promotion
- Changing command behavioral contracts
- Inventory XSD (agents inventory also has no body-schema twin)

## Related work

- Agents parity reference: `plinth-agents-generator` (`agents.xsd`, `AgentMarkdownGenerator`)
- Archived agents design: [`archive/2026-07-15-design-pml-agents-schema`](../archive/2026-07-15-design-pml-agents-schema/)
- ADR-001: XSD-validated XML → generated Markdown

## Design Refinement Summary

Commands now follow the **shipped agents-parity model** already live in `plinth-agents-generator`:

| Agents | Commands |
|--------|----------|
| `agents.xml` → `@file="….xml"` | `commands.xml` → `@file="….xml"` |
| `agents.xsd` (no namespace) + `xsi:noNamespaceSchemaLocation` | `commands.xsd` same style |
| `agents/*.xml` sources only | `commands/*.xml` sources only |
| `agent-to-markdown.xsl` + `AgentMarkdownGenerator` / `AgentMarkdownRenderer` | `command-to-markdown.xsl` + `CommandMarkdownGenerator` / `CommandMarkdownRenderer` |
| `exec-maven-plugin` at `process-classes` → `target/.../agents` | Same for `commands/` |
| `AgentIndexes`: inventory `.xml`; `agentFiles()` maps to `.md` | `CommandIndexes`: same mapping |
| Behavioral rules in tests, not Schematron | `CommandIndexesTest` substring checks on **generated** `.md` |

**Rejected for this project:** split namespaced XSDs under `pml/schemas/command/1.0.0/` (`pml-command.xsd`, inventory XSD, types XSD). Inventory stays outside `commands.xsd`, exactly like `agents.xml` stays outside `agents.xsd`.

## Context

Issue [#993](https://github.com/jabrena/plinth/issues/993) designs XSD validation for Plinth command sources and aligns the commands generator with the agents generator pipeline (ADR-001: XML → generated Markdown).

Four Markdown shapes remain expressible via a structural XSD superset plus `@kind`:

| `@kind` | Commands | Notable sections |
|---------|----------|------------------|
| `standard` | eight workflow commands | Goal CDATA (purpose, Usage, Accepted Inputs, Owning Agent, associations; optional Workflow position), Workflow, Output, Safeguards |
| `delivery` | `implement-spec` | Goal CDATA extras: Owner and delegation, Mandatory execution contract, Branch/worktree gate |
| `performance` | `profile`, `benchmark` | Goal CDATA: Owner and skills; `benchmark` Tool selection |
| `cli` | `close-spec` | Goal CDATA: Inputs / Owner; no associations or output |

## Goals / Non-Goals

**Goals:**

- Single no-namespace `commands.xsd` for command body documents.
- Convert all twelve command contracts to `commands/*.xml` validated against `commands.xsd`.
- Generate Markdown via XSLT + Java at `process-classes` (agents pattern).
- Keep `CommandIndexesTest` behavioral substrings green against generated Markdown.
- Realign this OpenSpec change to the agents-parity model.

**Non-Goals:**

- Refresh public `skills/` release output.
- Change command behavioral contracts.
- Inventory XSD (inventory remains filename-ordered `commands.xml`, Java-validated).
- Schematron companion rules.

## Decisions

### Single body schema, inventory outside

| Artifact | Role |
|----------|------|
| `commands.xsd` | Root `<command>` body schema; no target namespace |
| `commands.xml` | Installation-order inventory; `@file` ends in `.xml` |
| `commands/*.xml` | Authoritative command sources |
| Generated `commands/*.md` under `target/` only | Installer / classpath assets |

### Namespace strategy

- **No target namespace** (agents.xsd parity).
- Body documents declare `xsi:noNamespaceSchemaLocation="../commands.xsd"`.
- OpenSpec mirror: `examples/xsd/pml/0.9.0/commands.xsd` (same layout as archived agents mirror).

### Validation layers

| Layer | Tool | Responsibility |
|-------|------|----------------|
| 1 — Structure | `commands.xsd` | Core sequence (`goal`, optional `constraints`, `steps`, optional `output-format`, optional `safeguards`); narrative contract in goal CDATA |
| 2 — Behavior | `CommandIndexesTest`, Gherkin, `analysis-design-commands` | Kind profiles, routing strings, explore-design exclusions, implement-spec gates |

### Markdown generation

One stylesheet `command-to-markdown.xsl`: emit `# @id`, then trimmed `goal` Markdown as-is, then `constraints` / `steps` / `output-format` / `safeguards`. Kind-specific narrative headings live in goal CDATA (agents parity), not as separate XSD children.

## Component Boundaries

| Component | Owns |
|-----------|------|
| `plinth-commands-generator` | `commands.xsd`, `commands.xml`, `commands/*.xml`, XSLT, Java generators, inventory template |
| `CommandIndexes` / tests | Inventory load, `.xml`→`.md` mapping, behavioral substrings on generated Markdown |
| OpenSpec change | Design, examples, migration notes aligned to shipped layout |

## Risks / Trade-offs

- Heading variants across twelve contracts → optional `@heading` + kind defaults in XSLT.
- Inventory uniqueness not in XSD → `CommandIndexes` / tests (agents parity).

## Approval Checkpoint

1. Agents-parity layout (single `commands.xsd`, no `pml/` split schemas).
2. XML sources under `commands/`; Markdown generated only under `target/`.
3. Behavioral contracts unchanged; tests assert generated Markdown.

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
| Inventory manifest | Namespaced `<agent-inventory>` XML | `agent.xsd` |
| Agent contracts | Namespaced `<agent>` XML (one file per agent) | `agent.xsd` |
| Generated Markdown | XSLT output with YAML frontmatter | Parity tests vs current contracts |
| Inventory template | Generated from validated inventory XML | Manifest-driven generation |

## Frontmatter mapping

| Markdown YAML | XML |
|---------------|-----|
| `name` | `<frontmatter><name>` and `@id` |
| `model` | `<frontmatter><model>` |
| `description` | `<frontmatter><description>` |
| `readonly` | `<frontmatter><readonly>` or inventory `@readonly` |

## Agent shape reference (XSLT mapping, not XSD enforcement)

Nine agents use three Markdown shapes. Section mapping guides future XSLT templates; XSD defines all body elements as optional except `<frontmatter>`.

### Analyst (`robot-business-analyst`)

| Markdown heading | XML element |
|------------------|-------------|
| Opening paragraph | `<identity><paragraph>` |
| `## Missions` | `<missions><mission>` |
| `## Read-only boundary` | `<role-boundaries><section>` |
| `## Output format` | `<output-format>` |

### Architect (`robot-architect`)

| Markdown heading | XML element |
|------------------|-------------|
| `## Core role` | `<identity>` or `<core-role>` |
| `## Workflow order` | `<workflow-order>` |
| `## Missions` | `<missions>` |
| `## Workflow` | `<workflow>` |
| `## Constraints` | `<constraints>` |
| `## Output format` | `<output-format>` |

### Coordinator (`robot-tech-lead`)

| Markdown heading | XML element |
|------------------|-------------|
| `### Core role (non-negotiable)` | `<core-role>` |
| `### Collaboration partners` | `<collaboration>` |
| `### Framework identification` | `<framework-identification>` |
| Routing table | `<routing-table>` |
| Parallel delegation sections | `<parallel-delegation>` |
| `### Final output format` | `<output-format>` |

### Performance (`robot-java-performance`)

| Markdown heading | XML element |
|------------------|-------------|
| `## Core role` | `<identity>` |
| `## Missions` | `<missions>` |
| `## Output format` | `<output-format>` |
| `## Safeguards` | `<safeguards>` |

### Coder (five agents)

| Markdown heading | XML element |
|------------------|-------------|
| `### Core Responsibilities` | `<responsibilities>` |
| `### Skill selection rules` | `<skill-rules>` |
| `### Reference Rules` | `<reference-rules>` |
| `### Workflow` | `<workflow>` |
| `### Constraints` | `<constraints>` |

Coder agents do **not** use `<missions>` in the target XML model.

## Validation layers

1. **XSD** — structural superset: required `<frontmatter>` on body documents; optional body sections; unique inventory `@id`.
2. **Java parity tests** — `AgentIndexesTest` substrings and Gherkin until XSLT parity replaces them.

No Schematron companion rules in this iteration.

## Phased migration

### Step 1 — Schema contract (this OpenSpec change)

- Publish XSD + examples.
- No installer behavior change.

### Step 2 — Generator migration (follow-up), slice order

| Slice | Agent(s) |
|-------|----------|
| 1 | `robot-business-analyst` |
| 2 | `robot-architect` |
| 3 | `robot-java-performance` |
| 4 | All framework coders (shared XSLT template) |
| 5 | `robot-no-java` |
| 6 | `robot-tech-lead` |

Within each slice:

1. **Phase 1:** Parallel XML staging; XSD in tests; Markdown authoritative.
2. **Phase 2:** Dual authoring; shape-specific XSLT; parity vs `AgentIndexesTest`.
3. **Phase 3:** XML source of truth; generate Markdown at `generate-resources`.
4. **Phase 4:** Enriched inventory; generate `java-agents-inventory-template.md` from XML.

## Out of scope for migration notes

- Commands inventory schema ([#993](https://github.com/jabrena/plinth/issues/993))
- Skill `<prompt>` schema changes ([#991](https://github.com/jabrena/plinth/issues/991))
- Changing agent roles, routing tables, or delegation contracts
- Public `skills/` release promotion
- Schematron or `@kind` schema validation profiles

## Rollback

Each phase is independently revertible until Phase 3. Keep Markdown sources until parity tests pass consistently across all nine agents.

## Context

Issue [#992](https://github.com/jabrena/plinth/issues/992) designs XSD validation for Plinth agent sources. Current evidence:

- `plinth-agents-generator/src/main/resources/agents.xml` — root `<agent-inventory>` with `<agent file="..."/>` only; no namespace, no `xsi:noNamespaceSchemaLocation`
- Nine agent contracts under `plinth-agents-generator/src/main/resources/agents/` — Markdown with YAML frontmatter (`name`, `model`, `description`, optional `readonly`) and variable body section headings (`Missions`, `Core role`, `Read-only boundary`, `Output format`, `Framework identification`, `Collaboration partners`, `Workflow`, `Constraints`)
- Skills use `<prompt>` XML validated against [pml.xsd 0.8.0](https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd) with metadata, role, tone, goal, steps, constraints, and reference sections
- `analysis-design-agents` defines behavioral contracts (missions, routing, delegation, role boundaries, safeguards) enforced today by `AgentIndexesTest` substring checks and Gherkin acceptance tests
- [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) establishes XSD-validated XML sources generating Markdown outputs

The agents generator extraction ([`2026-07-13-add-agents-generator-module`](../archive/2026-07-13-add-agents-generator-module/design.md)) explicitly deferred PML agent schema design to this issue.

## Goals / Non-Goals

**Goals:**

- Define XSD schemas for agent inventory and agent definition documents.
- Map existing YAML frontmatter and Markdown section semantics to structured XML elements.
- Align required sections with `analysis-design-agents` behavioral fields and the nine current agent contracts.
- Document relationship to `pml.xsd` (skill `<prompt>`) and `pml-workflow.xsd` (workflow orchestration).
- Provide valid/invalid XML examples and migration notes for a future generator pipeline.

**Non-Goals:**

- Implement XSLT or generator migration in `plinth-agents-generator`.
- Design PML schemas for commands (`commands.xml`) or skills (`<prompt>` sources).
- Add, remove, or change agent delegation contracts or agent roles.
- Promote public `skills/` release output.

## Decisions

### Schema split: inventory vs agent body

Use two XSD documents published from the PML project:

| Schema | Root element | Purpose |
|--------|--------------|---------|
| `pml-agent-inventory.xsd` | `<agent-inventory>` | Manifest ordering, discovery metadata, file or inline references |
| `pml-agent.xsd` | `<agent>` | Single agent contract: metadata, identity, missions, boundaries, routing, output, safeguards |

**Rationale:** Mirrors the current two-file model (`agents.xml` + `agents/*.md`), allows independent validation of inventory parity vs contract depth, and avoids overloading skill `<prompt>` semantics.

Alternative considered: extend `pml.xsd` with an `<agent>` element. Rejected because skills and agents have different frontmatter, section vocabulary, and downstream generators; coupling would complicate independent versioning.

### Namespace and schema location

- Target namespace: `https://jabrena.github.io/pml/schemas/agent/1.0.0` (inventory and agent body share types via an included `pml-agent-types.xsd` where needed).
- Inventory documents declare:

```xml
<agent-inventory xmlns="https://jabrena.github.io/pml/schemas/agent/1.0.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="https://jabrena.github.io/pml/schemas/agent/1.0.0
                                     https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent-inventory.xsd">
```

- Agent body documents declare `pml-agent.xsd` at the same version path.

Alternative considered: no namespace (like pre-schema `agents.xml`). Rejected to support schema evolution and cross-project reuse.

### Relationship to `pml.xsd` and `pml-workflow.xsd`

| PML artifact | Role | Agent schema relationship |
|--------------|------|---------------------------|
| `pml.xsd` | Skill `<prompt>` contract (`metadata`, `role`, `tone`, `goal`, `steps`, `constraints`, `reference`) | **Parallel, not embedded.** Agents are runtime orchestration personas with YAML-compatible frontmatter and mission/routing sections; skills are executable instruction bundles. Shared pattern: XSD-validated XML → generated Markdown with frontmatter. Reuse naming conventions (`metadata`, `constraints`) where semantics align; do not require `<prompt>` wrapper for agents. |
| `pml-workflow.xsd` | Multi-step workflow definitions | **Orthogonal.** Agent contracts may *reference* workflow commands (e.g. `/create-spec`, `/explore-design`) in mission text, but workflow graph structure stays in workflow schema. Optional future `workflow-ref` attribute on mission steps is out of scope for v1.0.0. |
| `pml-agent-inventory.xsd` / `pml-agent.xsd` | Agent bundle manifest and contracts | **New extension family** versioned under `/pml/schemas/agent/1.0.0/`. Documented in PML project README as the agent companion to skill prompts. |

### Inventory schema shape (beyond filename-only)

Extend `<agent-inventory>` entries beyond bare `file` attributes:

```xml
<agent-inventory version="1.0.0">
  <metadata>
    <title>Plinth Embedded Agents</title>
    <description>Installation-order manifest for embedded robot agents</description>
  </metadata>
  <agent id="robot-business-analyst"
         file="robot-business-analyst.md"
         readonly="true">
    <summary>Business analyst for issue quality and read-only alignment review</summary>
  </agent>
  <!-- additional agents in installation order -->
</agent-inventory>
```

Required inventory constraints:

- Root `<agent-inventory>` with optional `<metadata>` and one-or-more `<agent>` entries.
- Each `<agent>` MUST have unique `@id` matching `^[a-z][a-z0-9-]*$` and consistent with generated Markdown `name` frontmatter.
- Each `<agent>` MUST provide `@file` ending in `.md` **or** an inline embedded `<agent-definition>` (future; optional in v1.0.0).
- Optional `@readonly` boolean mirrors frontmatter.
- `<summary>` provides inventory-table text without opening the body file.
- Child order of `<agent>` elements defines installation order (preserves current `005-agents-installation` semantics).

### Agent body schema shape

Root `<agent>` maps frontmatter and body sections:

```xml
<agent xmlns="https://jabrena.github.io/pml/schemas/agent/1.0.0"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="https://jabrena.github.io/pml/schemas/agent/1.0.0
                           https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent.xsd"
       id="robot-business-analyst">

  <frontmatter>
    <name>robot-business-analyst</name>
    <model>inherit</model>
    <description>Business analyst. Creates structured GitHub or Jira issues...</description>
    <readonly>true</readonly>
  </frontmatter>

  <identity>
    <paragraph>You are an experienced business analyst focused on ...</paragraph>
  </identity>

  <missions>
    <mission id="create-issues" title="Create issues">
      <item>Clarify the persona, need, value, scope, and acceptance criteria.</item>
      <!-- ... -->
    </mission>
    <mission id="review-alignment" title="Review alignment and readiness">
      <steps>
        <step number="1" title="Summarize intent">...</step>
        <!-- ... -->
      </steps>
    </mission>
  </missions>

  <role-boundaries>
    <section title="Read-only boundary">
      <item>Do not implement application code.</item>
      <!-- ... -->
    </section>
  </role-boundaries>

  <routing>
    <delegate target="robot-architect" when="design, ADR, plan, or OpenSpec conflicts"/>
    <delegate target="robot-tech-lead" when="delivery conflicts"/>
  </routing>

  <output-format>
    <heading>Summary</heading>
    <heading>Readiness</heading>
    <!-- ... -->
  </output-format>

  <safeguards>
    <constraint>Do not invent requirements; flag uncertainty instead.</constraint>
  </safeguards>
</agent>
```

**Required elements (all agents):**

- `<frontmatter>` with `<name>`, `<model>`, `<description>`; `<readonly>` optional.
- `<identity>` — opening persona paragraph(s).
- `<missions>` — one or more `<mission>` with `@id` and `@title`.
- `<role-boundaries>` — at least one boundary section (title + items or paragraphs).
- `<output-format>` — structured expected response headings or bullets.

**Conditionally required elements (by agent class):**

| Agent class | Additional required sections |
|-------------|------------------------------|
| Coordinators (`robot-tech-lead`) | `<routing>` with `<delegate>` entries; `<collaboration>` optional wrapper for partner descriptions |
| Framework coders | `<framework-identification>` or routing table under `<routing>` |
| Read-only reviewers (`robot-business-analyst`, `robot-architect`) | Explicit `<role-boundaries>` with read-only constraints |
| Performance / implementation agents | `<safeguards>` listing prohibited direct work |

**Optional elements:**

- `<workflow>` — ordered command references (`/create-spec`, `/explore-design`, `/close-spec`) for agents like `robot-architect`.
- `<constraints>` — general rules separate from `<safeguards>` (mirrors skill `<constraints>` naming).
- `<collaboration>` — partner agent descriptions (tech lead pattern).

Section titles in generated Markdown MAY retain current heading variants (`Core role` vs identity paragraph, `Collaboration partners` vs `<collaboration>`) via XSLT mapping rules documented in migration notes—not enforced as literal XSD string enums to avoid breaking existing contracts during migration.

### Frontmatter mapping

| Markdown YAML | XML element | Notes |
|---------------|-------------|-------|
| `name` | `<frontmatter><name>` | MUST equal `@id` and inventory `@id` |
| `model` | `<frontmatter><model>` | Enumerated: `inherit` or explicit model slug list (extensible string) |
| `description` | `<frontmatter><description>` | Single-line installer/summary description |
| `readonly` | `<frontmatter><readonly>` or `@readonly` on inventory entry | Boolean, default false |

### Valid / invalid examples (robot-business-analyst)

**Valid (minimal):** inventory entry with `id`, `file`, `readonly`, `summary`; agent body with all required sections populated from current `robot-business-analyst.md` content.

**Invalid examples to document:**

1. Missing `<missions>` — fails agent body schema.
2. Duplicate `@id` in inventory — fails inventory schema.
3. `<name>` mismatch between frontmatter and `@id` — fails identity consistency rule (documented Schematron or generator check; optional XSD assertion note).
4. Empty `<role-boundaries>` — fails agent body schema.
5. Inventory `<agent>` without `@file` and without inline body — fails inventory schema.

Store examples under `documentation/openspec/changes/design-pml-agents-schema/examples/` (valid + invalid pairs) as part of task delivery; reference paths from the schema design document.

### Migration notes (implementation deferred)

Phased migration strategy for follow-up work:

1. **Phase 0 (this change):** Publish XSD, design doc, examples, migration notes only.
2. **Phase 1:** Add optional XSD validation in `plinth-agents-generator` tests against parallel XML sources without removing Markdown.
3. **Phase 2:** Author XML agent sources alongside Markdown; generate Markdown via XSLT using ADR-001 pattern; parity tests compare output to current contracts.
4. **Phase 3:** Switch source of truth to XML; remove duplicate Markdown authoring; extend `AgentIndexes` / `InventoryXmlLoader` for namespace-aware parsing.
5. **Phase 4:** Enrich `agents.xml` with inventory metadata fields; generate `java-agents-inventory-template.md` from validated inventory XML.

Non-breaking rule: until Phase 3, Markdown remains the runtime installer input; XML is additive.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Split agent inventory + agent body XSDs | Matches current layout; independent validation; clear migration | Two schema files to publish | **Recommended** |
| B. Reuse `<prompt>` from `pml.xsd` for agents | Single schema family | Poor fit for frontmatter, routing tables, delegate links; conflates skills and agents | Rejected |
| C. JSON Schema instead of XSD | Popular for tooling | Breaks ADR-001 and skills pipeline precedent; no XInclude/XSLT synergy | Rejected |
| D. Inventory-only schema (defer body) | Smaller first delivery | Does not meet issue acceptance criteria for mission/routing/boundary elements | Rejected |

## Risks / Trade-offs

- [Risk] Agent Markdown section headings vary across nine contracts. → Use flexible `<section title="...">` containers; normalize in XSLT, not XSD enums.
- [Risk] PML repo publication lag blocks CI schema URLs. → Pin schema URLs in design doc; stub validation with local copies in generator tests until published.
- [Risk] Over-constraining optional sections breaks future agents. → Use optional elements with documented conditional requirements by agent class, not one global rigid sequence.
- [Risk] Confusion with issue #993 (commands schema). → Keep agent and command schemas separate; cross-reference only in PML documentation.

## Validation Strategy

- XSD validity: `xmllint --noout --schema pml-agent-inventory.xsd agents.xml` (once migrated).
- Example round-trip: valid examples pass; invalid examples fail with documented error codes.
- Semantic parity: future generator tests assert XML → Markdown preserves substrings currently checked in `AgentIndexesTest`.
- OpenSpec: `openspec validate --all` for this change.

## ADR Candidates

| Topic | Recommendation |
|-------|----------------|
| Agent XML schema as companion to skill `<prompt>` | Record in PML project docs; optional Plinth ADR if generator migration chooses XML source of truth |
| Split inventory/body schemas | Accept in this design; revisit if inline-only bundles become the norm |

## Compatibility Review

- **NON-BREAKING:** Design-only; no changes to installed agent contracts, generated skills, or public APIs.
- Existing `agents.xml` without namespace remains valid until Phase 3; forward-compatible mapping adds optional attributes/elements.
- `analysis-design-agents` behavioral requirements unchanged.

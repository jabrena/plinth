## Design Refinement Summary

Early `/explore-design` review showed three Markdown shapes across nine agent contracts. Rather than encoding per-shape `<missions>` / `<responsibilities>` XSD elements, the delivered model keeps mission, routing, and responsibility prose inside `<goal>` CDATA (and optional structured `<constraints>` / `<steps>` / `<output-format>` / `<safeguards>`), mirroring staged PML `pml.xsd` 0.9.0 element shapes without wrapping agents as `<prompt>`.

**Accepted delivery model (supersedes earlier design-only / frontmatter-element drafts):**

1. Canonical schema is **`agents.xsd`** at `plinth-agents-generator/src/main/resources/agents.xsd` (OpenSpec mirror: `examples/xsd/pml/0.9.0/agents.xsd`; `agent.xsd` is a shim include).
2. XML agent sources live under `plinth-agents-generator/src/main/resources/agents/robot-*.xml` and validate with `agents.xsd` (`xsi:noNamespaceSchemaLocation="../agents.xsd"`).
3. Java in `plinth-agents-generator` (`AgentMarkdownGenerator` / `AgentMarkdownRenderer`) generates Cursor-compatible `.md` (YAML frontmatter + body) at **`process-classes`** via `exec-maven-plugin`, writing into `agents/` (and classpath `target/.../agents`).
4. Existing `plinth-skills-generator` bridge continues to copy **`*.md` only** from that `agents/` directory.
5. Inventory `agents.xml` lists XML `@file` sources; `AgentIndexes.agentFiles()` maps `.xml` → `.md` for installers.
6. Behavioral contracts stay in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin — **not** Schematron or `@kind` profiles.
7. Migrate/review agent-by-agent in complexity order: analyst → architect → performance → framework coders → `robot-no-java` → tech lead last (see [`MIGRATION.md`](MIGRATION.md)).

## Context

Issue [#992](https://github.com/jabrena/plinth/issues/992) delivers XSD validation and XML-sourced agent contracts for Plinth. Current evidence:

- `plinth-agents-generator/src/main/resources/agents.xsd` — production schema for individual `<agent>` documents (inventory out of scope)
- Nine XML sources under `plinth-agents-generator/src/main/resources/agents/robot-*.xml`
- Generated Markdown `agents/*.md` consumed by the skills bridge (`plinth-skills-generator` copy-resources, include `*.md`)
- `agents.xml` — root `<agent-inventory>` with ordered `<agent file="….xml"/>` entries (no XSD binding in this iteration)
- Skills use `<prompt>` XML validated against PML schemas; staged OpenSpec copy of `pml.xsd` 0.9.0 shares element naming (`metadata`, `role`, `goal`, `constraints`, …)
- `analysis-design-agents` defines behavioral contracts (missions, routing, delegation, role boundaries, safeguards) still enforced by `AgentIndexesTest` and Gherkin
- [ADR-001](../../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) establishes XSD-validated XML sources generating Markdown outputs

The agents generator extraction ([`2026-07-13-add-agents-generator-module`](../archive/2026-07-13-add-agents-generator-module/design.md)) deferred PML agent schema design; this change supplies schema, XML sources, and Java Markdown generation.

## Goals / Non-Goals

**Goals:**

- Ship `agents.xsd` for individual agent definition documents with a clear required/optional policy.
- Author XML sources under `agents/` and generate Cursor-compatible Markdown for the skills bridge.
- Document shared element shapes with staged `pml.xsd` 0.9.0 and the orthogonal role of `pml-workflow.xsd`.
- Keep behavioral enforcement outside XSD (tests + `analysis-design-agents`).
- Provide valid/invalid examples and migration notes from Markdown-first assets.

**Non-Goals:**

- Put `<agent-inventory>` / `agents.xml` under `agents.xsd` in this iteration.
- Reuse skill `<prompt>` as the agent root element.
- Design PML schemas for commands (`commands.xml`) in this change (see issue #993).
- Add Schematron rules or `@kind` validation profiles.
- Change agent delegation contracts or public agent names/roles.
- Promote public `skills/` release output (release profile is separate).
- External PML site publication of `agents.xsd` (deferred follow-up).

## Decisions

### Canonical schema: `agents.xsd` (agent body only)

| Schema | Root | Purpose |
|--------|------|---------|
| `agents.xsd` | `<agent>` | Individual agent contracts |

**Inventory** (`agents.xml` / `<agent-inventory>`) remains a separate installer discovery manifest **outside** this XSD. See [`REQUIRED-OPTIONAL.md`](examples/xsd/pml/0.9.0/REQUIRED-OPTIONAL.md).

**Shim:** OpenSpec `examples/xsd/pml/0.9.0/agent.xsd` only includes `agents.xsd` so older paths keep resolving.

Alternative considered: enrich inventory inside the same XSD. Deferred; current `@file` list is sufficient for installers via `AgentIndexes`.

Alternative considered: extend `pml.xsd` with an `<agent>` element under `<prompt>` reuse. Rejected — agents are a parallel family that share element *shapes*, not skill document roots.

### Namespace and schema location

Production documents use **no target namespace** with local schema location:

```xml
<agent id="robot-business-analyst"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:noNamespaceSchemaLocation="../agents.xsd">
```

Validation in CI/docs uses:

```bash
xmllint --noout --schema plinth-agents-generator/src/main/resources/agents.xsd \
  plinth-agents-generator/src/main/resources/agents/robot-*.xml
```

External namespaced publication under `https://jabrena.github.io/pml/schemas/...` remains a deferred PML-project follow-up; the OpenSpec staged tree under `examples/xsd/pml/0.9.0/` is the design mirror for this change.

### Agent body schema shape (PML-aligned)

```xml
<agent id="robot-business-analyst"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:noNamespaceSchemaLocation="../agents.xsd">

  <metadata>
    <authors><author>…</author></authors>
    <version>0.18.0-SNAPSHOT</version>
    <license>Apache-2.0</license>
    <title>robot-business-analyst</title>
    <description>…</description>
  </metadata>

  <role>…</role>

  <goal><![CDATA[
## Missions
…
]]></goal>

  <constraints>…</constraints>       <!-- optional -->
  <steps>…</steps>                   <!-- optional -->
  <output-format>…</output-format>   <!-- optional -->
  <safeguards>…</safeguards>         <!-- optional -->
</agent>
```

**Required (XSD / production policy):** `@id`, `<metadata>` with `<title>` and `<description>`, `<role>`, `<goal>`.

**Optional:** metadata `authors` / `version` / `license`; zero-or-more `<constraints>`; `<steps>`; `<output-format>`; `<safeguards>`.

This supersedes the earlier draft that used required `<frontmatter>` and optional typed `<missions>` / `<responsibilities>` / `<routing>` elements. Mission and routing prose stays in `<goal>` CDATA (or constraint/safeguard lists) so one structural model covers all nine agents.

### Frontmatter mapping (generation)

| Cursor Markdown YAML | XML | Notes |
|----------------------|-----|-------|
| `name` | `@id` (and usually `<metadata>/<title>`) | Renderer emits `name:` from `@id` |
| `description` | `<metadata>/<description>` | Required |
| `model` | optional `<metadata><model>` | Emitted when present |
| `readonly` | optional `<metadata><readonly>` | Emitted in frontmatter only when XML value is `true` |

YAML frontmatter is an **output** of `AgentMarkdownRenderer`, not an XML `<frontmatter>` element.

### Build and skills-bridge data flow

1. Contributor edits `agents/robot-*.xml` and updates inventory via `agents.xml` `@file` entries.
2. Build validates structure (tests / `xmllint` against `agents.xsd`).
3. `exec-maven-plugin` runs `AgentMarkdownGenerator` at `process-classes` with:
   - XML input: `plinth-agents-generator/src/main/resources/agents` (read-only)
   - Markdown outputs: `${project.build.directory}/generated-resources/agents` and `${project.build.outputDirectory}/agents`
4. `AgentMarkdownRenderer` transforms each document with classpath `agent-to-markdown.xsl` (adapted from skills generator XSLT; no hand-written DOM-to-Markdown heuristics beyond the stylesheet).
5. Generator writes `robot-*.md` only under `target/` (never into `src/main/resources/agents`).
6. `plinth-skills-generator` bridge copies `*.md` from `plinth-agents-generator/target/generated-resources/agents` into skill-reference assets for `005-agents-installation` / related embeds.
7. `AgentIndexes.agentSources()` returns inventory `.xml` names; `agentFiles()` maps them to `.md` for installers.
8. Behavioral parity remains in `AgentIndexesTest` / Gherkin against generated Markdown content.

### Relationship to staged `pml.xsd` 0.9.0 and `pml-workflow.xsd`

| PML artifact | Role | Agent schema relationship |
|--------------|------|---------------------------|
| Staged `pml.xsd` 0.9.0 (`examples/xsd/pml/0.9.0/pml.xsd`) | Skill `<prompt>` contract | **Parallel family.** Agents reuse shared *element shapes* (`metadata`, `role`, `goal`, `constraints`, `steps`, `output-format`, `safeguards`) without becoming `<prompt>` documents. Agents omit skill-only children such as `tone` / `triggers` / `examples` as required roots. |
| Published skill `pml.xsd` (e.g. 0.8.0 on the PML site) | Current skills pipeline | Agents do not validate against skill schema versions; relation is conceptual + shared naming for a future PML publish. |
| `pml-workflow.xsd` | Multi-step workflow definitions | **Orthogonal.** Agent goal/mission text may *mention* workflow commands (e.g. `/create-spec`, `/explore-design`); workflow graph structure stays in the workflow schema. |
| `agents.xsd` | Individual agent contracts | **New parallel extension** colocated with the agents generator; OpenSpec mirror under `examples/xsd/pml/0.9.0/`. |

### Validation layers

| Layer | Tool | Responsibility |
|-------|------|----------------|
| 1 — Structure | XSD (`agents.xsd`) | Required/optional children and cardinalities on `<agent>` documents |
| 2 — Behavior parity | `analysis-design-agents`, `AgentIndexesTest`, Gherkin | Missions wording, routing tables, skill precedence, kind-specific section expectations |

**Rejected:** Schematron (`pml-agent.sch`) and `@kind` validation profiles. Kind-specific section requirements are not XSD-enforced.

### Inventory handling

```xml
<agent-inventory>
  <agent file="robot-business-analyst.xml"/>
  <!-- installation order = child order -->
</agent-inventory>
```

- `@file` MUST end in `.xml` and resolve under `agents/`.
- Unique logical agent identity comes from each document’s `@id` (and generated Markdown `name`).
- Enriched inventory metadata (`@readonly`, `<summary>`) stays optional future work; not required for this delivery.

### Valid / invalid examples

- Valid: module `agents/robot-*.xml` and OpenSpec `examples/xml/robot-*.xml`.
- Invalid (documented failures): `examples/xml/invalid/` — see [invalid README](examples/xml/invalid/README.md) (`missing-id`, `disallowed-child`, `malformed-constraints`).
- Required/optional policy: [`REQUIRED-OPTIONAL.md`](examples/xsd/pml/0.9.0/REQUIRED-OPTIONAL.md).

### Migration (Markdown-first → XML + generated Markdown)

Historical Markdown-first `agents/*.md` sources are replaced by XML sources; generated `.md` remains the skill-bridge and installer input. Slice order and field mapping live in [`MIGRATION.md`](MIGRATION.md).

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. `agents.xsd` for `<agent>` only; inventory outside | Matches shipped generators and installers | Inventory not schema-validated yet | **Accepted** |
| B. Reuse `<prompt>` from `pml.xsd` for agents | Single schema family | Poor fit for Cursor agent install surface; conflates skills and agents | Rejected |
| C. Required typed `<missions>` / `<routing>` elements | Stronger structure | Fails coder/coordinator prose shapes; blocks one-schema migration | Rejected |
| D. Design-only / deferred generator | Smaller first PR | Does not meet delivery of XML sources + skills bridge continuity | Rejected for final delivery |
| E. XSD + Schematron `@kind` profiles | Expresses kind-specific sections | Extra artifact; behavioral tests already cover this | Rejected |
| F. Java Markdown generation (not XSLT) | Fits agents module; simpler for Cursor frontmatter | Different from skills XSLT path | **Accepted** for this change |

## Component Boundaries

| Component | Owns | Consumes |
|-----------|------|----------|
| `plinth-agents-generator` | `agents.xsd`, `agents/robot-*.xml`, `agents.xml`, Java generator/renderer, `AgentIndexes` | Schema + XML sources |
| `plinth-skills-generator` bridge | Copy of `agents/*.md` into skill-reference assets | Generated Markdown only |
| `AgentIndexesTest` / Gherkin | Behavioral substring parity | Generated Markdown |
| OpenSpec change examples | Mirror XSD, XML/MD samples, invalid cases, REQUIRED-OPTIONAL | Module canonical schema |
| PML project (deferred) | External publication of agents schema | Plinth-local schemas as source |

## Data Flow (delivered)

1. Edit `robot-*.xml` under `plinth-agents-generator/src/main/resources/agents/`.
2. Validate with `agents.xsd`.
3. Maven `process-classes` generates `robot-*.md` into the same `agents/` directory (and `target/classes/agents`).
4. Skills bridge copies `*.md` for installation skills.
5. Verify with `./mvnw clean verify -pl plinth-agents-generator` (and skills module when the bridge is exercised).

## Failure Handling

| Failure | Expected behavior |
|---------|-------------------|
| Missing `@id`, metadata title/description, role, or goal | XSD / generator failure |
| Disallowed child (e.g. skill-only `<tone>`) | XSD validation failure |
| Malformed `<constraints>` | XSD validation failure (see invalid examples) |
| Behavioral contract drift (routing table, skill refs, …) | `AgentIndexesTest` or Gherkin failure |
| Bridge sees only XML / missing `.md` | Skills packaging misses agent assets — regenerate Markdown before skills build |

## Risks / Trade-offs

- [Risk] Mission/routing headings remain freeform inside `<goal>` CDATA. → Accept; enforce critical wording via behavioral tests, not XSD enums.
- [Risk] Inventory not XSD-validated. → Accept for this iteration; Java loader already constrains `@file` to `*.xml`.
- [Risk] External PML publication deferred. → Validate against local classpath / path schemas; publish later without blocking Plinth delivery.
- [Risk] Confusion with issue #993 (commands schema). → Keep agent and command schemas separate.

## Validation Strategy

Structural (XSD):

- Module and OpenSpec valid examples pass `xmllint --noout --schema …/agents.xsd`.
- Invalid examples fail with documented element or attribute diagnostics.

Behavior parity:

- Generated Markdown preserves `AgentIndexesTest` / Gherkin expectations for tech-lead routing, coder skill precedence, analyst/architect safeguards, and related contracts.
- OpenSpec: `openspec validate --all` for this change.

## ADR Candidates

| Topic | Recommendation | Status |
|-------|----------------|--------|
| Agent XML schema as companion (not reuse) of skill `<prompt>` | Document in Plinth module docs; publish with PML when ready | Open |
| Canonical name `agents.xsd` + Java Markdown generation | Accepted delivery | **Resolved** |
| XSD-only vs Schematron | XSD-only; behavioral rules in tests | **Resolved** |
| Inventory outside agent body schema | Accepted for this iteration | **Resolved** |

## Compatibility Review (`056`)

| Surface | Changes? | Mitigation |
|---------|----------|------------|
| Installed agent Markdown semantics | Content parity via generator | Behavioral tests on generated `.md` |
| Skills bridge input | Still `*.md` under `agents/` | Bridge include list unchanged |
| `agents.xml` `@file` | Now `.xml` | `AgentIndexes` maps to `.md` for installers |
| Public agent names and routing | No intentional change | Schema validates structure only |
| Public `skills/` release tree | Not auto-refreshed | Explicit `-P release` when ready |

## Resolved Design Questions

| Question | Decision | Status |
|----------|----------|--------|
| Schema name / path? | `plinth-agents-generator/src/main/resources/agents.xsd` (+ OpenSpec mirror; `agent.xsd` shim) | **Resolved** |
| Inventory in XSD? | No for this iteration | **Resolved** |
| Body model? | `<metadata>`, `<role>`, `<goal>`, optional structured sections | **Resolved** |
| Generator deferred? | No — Java generates `.md` for the skills bridge | **Resolved** |
| `<prompt>` reuse? | No — parallel family sharing shapes with staged `pml.xsd` 0.9.0 | **Resolved** |
| Schematron / `@kind`? | Rejected | **Resolved** |

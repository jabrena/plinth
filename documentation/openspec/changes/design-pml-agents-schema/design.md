## Design Refinement Summary

`/explore-design` review of the nine shipped agent contracts reveals **three distinct Markdown shapes**, not one uniform `Missions`-centric template. Coder agents (`robot-java-*`, `robot-no-java`) use `### Core Responsibilities`, `### Skill selection rules`, and `### Reference Rules` instead of `## Missions`. `robot-tech-lead` uses only `###`-level sections including routing tables and parallel-delegation rules. Forcing a single `<missions>` element for every agent would fail parity with current contracts and block migration.

**Recommended direction (pending approval):**

1. Keep split inventory/body XSDs under `https://jabrena.github.io/pml/schemas/agent/1.0.0/`, published in the [PML project](https://github.com/jabrena/pml) following the `pml.xsd` 0.8.0 URL pattern.
2. Add `@kind` on inventory and body `<agent>` elements to drive **kind-specific required sections** validated by co-published Schematron rules, not XSD alone.
3. Use XSD as a structural superset; enforce cross-field rules (`frontmatter/name` equals `@id`, inventory/body id parity) and kind profiles in Schematron.
4. Migrate agent-by-agent in complexity order: analyst → architect → performance → coders (shared template) → tech lead last.

## Context

Issue [#992](https://github.com/jabrena/plinth/issues/992) designs XSD validation for Plinth agent sources. Current evidence:

- `plinth-agents-generator/src/main/resources/agents.xml` — root `<agent-inventory>` with `<agent file="..."/>` only; no namespace, no `xsi:noNamespaceSchemaLocation`
- Nine agent contracts under `plinth-agents-generator/src/main/resources/agents/` — three Markdown shapes:
  - **Analyst / architect / performance:** YAML frontmatter plus `##` sections (`Missions`, `Core role`, `Workflow order`, `Constraints`, `Output format`, `Safeguards`)
  - **Coordinator (`robot-tech-lead`):** frontmatter plus `###` sections only (`Core role`, `Collaboration partners`, `Framework identification`, routing tables, parallel-delegation rules, `Final output format`)
  - **Coders (five agents):** frontmatter plus `### Core Responsibilities`, `### Skill selection rules`, `### Reference Rules`, `### Workflow`, `### Constraints` — no `Missions` section
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

Alternative considered: reuse the command namespace from [`design-pml-commands-schema`](../design-pml-commands-schema/design.md) with shared inventory types. Rejected to keep agent and command schema evolution independent; share only optional `pml-core-types.xsd` identifier patterns (skill id, agent id) at the PML project level.

### Publication target

Publish from the PML project to mirror skill schema hosting:

| Artifact | Target path |
|----------|-------------|
| `pml-agent-types.xsd` | `https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent-types.xsd` |
| `pml-agent-inventory.xsd` | `https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent-inventory.xsd` |
| `pml-agent.xsd` | `https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent.xsd` |
| `pml-agent.sch` (Schematron) | `https://jabrena.github.io/pml/schemas/agent/1.0.0/pml-agent.sch` |

Until PML publication lands, Plinth generator tests MAY vendor copies under `plinth-agents-generator/src/test/resources/pml/agent/1.0.0/` with the same relative imports.

### Agent kind taxonomy

Inventory and body documents carry `@kind` to select validation profiles and XSLT mapping templates:

| `@kind` | Agents | Primary body structure |
|---------|--------|------------------------|
| `analyst` | `robot-business-analyst` | `<missions>`, `<role-boundaries>`, `<routing>`, `<output-format>` |
| `architect` | `robot-architect` | `<identity>`, `<workflow-order>`, `<missions>`, `<workflow>`, `<constraints>`, `<output-format>` |
| `coordinator` | `robot-tech-lead` | `<core-role>`, `<delivery-mission>`, `<collaboration>`, `<framework-identification>`, `<routing-table>`, `<parallel-delegation>`, `<constraints>`, `<output-format>` |
| `performance` | `robot-java-performance` | `<identity>`, `<missions>`, `<output-format>`, `<safeguards>` |
| `coder` | `robot-java-coder`, `robot-java-spring-boot-coder`, `robot-java-quarkus-coder`, `robot-java-micronaut-coder`, `robot-no-java` | `<responsibilities>`, `<skill-rules>`, `<reference-rules>`, `<workflow>`, `<constraints>` |

Inventory entries SHOULD declare `@kind` so generated inventory tables and future template generation do not infer kind from filename heuristics.

### Validation layers

| Layer | Tool | Responsibility |
|-------|------|----------------|
| 1 — Structure | XSD (`pml-agent-inventory.xsd`, `pml-agent.xsd`) | Element presence, types, cardinalities for superset model |
| 2 — Profile | Schematron (`pml-agent.sch`) | `@kind`-specific required sections, `@id`/`name` equality, non-empty safeguards/boundaries |
| 3 — Behavior parity | Java tests (`AgentIndexesTest`, Gherkin) | Delegation substrings, routing tables, skill references until XSLT parity replaces substring checks |

XSD 1.0 cannot express `@kind`-conditional requirements cleanly; Schematron is the recommended companion, matching how strict semantic rules exceed structural XSD in other PML families.

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
         kind="analyst"
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
- Each `<agent>` MUST declare `@kind` from the taxonomy above.
- Each `<agent>` MUST provide `@file` ending in `.md` during migration phases 0–2 and MAY use `.xml` from phase 2 onward **or** an inline embedded `<agent-definition>` (optional in v1.0.0).
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
       id="robot-business-analyst"
       kind="analyst">

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

**Required for all agents (XSD + Schematron):**

- `<frontmatter>` with `<name>`, `<model>`, `<description>`; `<readonly>` optional.
- `@kind` on root `<agent>` matching inventory entry.

**Required by `@kind` (Schematron profiles):**

| `@kind` | Required body sections |
|---------|------------------------|
| `analyst` | `<missions>`, `<role-boundaries>`, `<output-format>`; `<routing>` recommended |
| `architect` | `<identity>`, `<missions>`, `<workflow-order>`, `<constraints>`, `<output-format>`; `<workflow>` recommended |
| `coordinator` | `<core-role>`, `<collaboration>`, `<framework-identification>`, `<routing-table>`, `<parallel-delegation>`, `<constraints>`, `<output-format>` |
| `performance` | `<identity>`, `<missions>`, `<output-format>`, `<safeguards>` |
| `coder` | `<responsibilities>`, `<skill-rules>`, `<reference-rules>`, `<workflow>`, `<constraints>` |

**Optional / shared elements across kinds:**

- `<routing>` with `<delegate>` — analyst conflicts; architect handoff notes.
- `<workflow>` — architect command order; coders implementation steps.
- `<safeguards>` — performance agent; may appear on other kinds without being globally required.
- `<skill-ref id="..."/>` inside `<skill-rules>` and `<reference-rules>` — reuse pattern from optional `pml-core-types.xsd`.

Section titles in generated Markdown retain current heading variants per agent via **kind-specific XSLT templates** (`analyst.xsl`, `architect.xsl`, `coordinator.xsl`, `performance.xsl`, `coder.xsl`), not one global heading map.

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
3. `<name>` mismatch between frontmatter and `@id` — fails Schematron identity rule.
4. Empty `<role-boundaries>` on `kind="analyst"` — fails Schematron profile.
5. Inventory `<agent>` without `@file` and without inline body — fails inventory XSD.
6. `kind="coder"` document with `<missions>` but missing `<skill-rules>` — fails Schematron profile (illustrates kind mismatch).

Add a second valid example pair for `robot-tech-lead` (`kind="coordinator"`) during implementation to prove the coordinator profile, not only the analyst slice.

Store examples under `documentation/openspec/changes/design-pml-agents-schema/examples/` (valid + invalid pairs) as part of task delivery; reference paths from the schema design document.

### Migration notes (implementation deferred)

Two-step delivery strategy ([`051-design-two-steps-methods`](../../../.agents/skills/051-design-two-steps-methods/SKILL.md)):

**Step 1 — Schema and validation contract (this change)**

- Publish XSD + Schematron + examples + migration notes.
- No generator or installer behavior change.

**Step 2 — Generator migration (follow-up issues, agent-by-agent slices)**

| Slice | Agent(s) | Rationale |
|-------|----------|-----------|
| 1 | `robot-business-analyst` | Smallest analyst profile; existing valid example |
| 2 | `robot-architect` | Adds workflow-order and constraints |
| 3 | `robot-java-performance` | Missions + safeguards without routing tables |
| 4 | All coders (shared XSLT) | One template parameterized by framework skill lists |
| 5 | `robot-no-java` | Coder variant with routing-boundary differences |
| 6 | `robot-tech-lead` | Largest coordinator profile; migrate last |

Phases within Step 2:

1. **Phase 1:** Parallel XML under staging path; XSD + Schematron in tests; Markdown authoritative.
2. **Phase 2:** Dual authoring; kind-specific XSLT; parity tests vs `AgentIndexesTest` substrings.
3. **Phase 3:** XML source of truth; generate Markdown at `generate-resources`; namespace-aware `AgentIndexes`.
4. **Phase 4:** Enriched inventory with `@kind`, `@readonly`, `<summary>`; generate `java-agents-inventory-template.md`.

Non-breaking rule: until Phase 3, Markdown remains the runtime installer input; XML is additive. Each slice is independently revertible.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Split agent inventory + agent body XSDs | Matches current layout; independent validation; clear migration | Two schema files to publish | **Recommended** |
| B. Reuse `<prompt>` from `pml.xsd` for agents | Single schema family | Poor fit for frontmatter, routing tables, delegate links; conflates skills and agents | Rejected |
| C. JSON Schema instead of XSD | Popular for tooling | Breaks ADR-001 and skills pipeline precedent; no XInclude/XSLT synergy | Rejected |
| D. Inventory-only schema (defer body) | Smaller first delivery | Does not meet issue acceptance criteria for mission/routing/boundary elements | Rejected |
| E. Single global `<missions>` required element | Simple XSD | Fails coder and coordinator contracts; blocks migration | Rejected |
| F. XSD-only validation without Schematron | Fewer artifacts | Cannot express `@kind` profiles or id/name equality | Rejected |

## Component Boundaries

| Component | Owns | Consumes |
|-----------|------|----------|
| PML project | XSD, Schematron, published URLs, `pml-core-types.xsd` (optional) | — |
| `plinth-agents-generator` (future) | XML sources, XSLT templates, validation tests | PML schemas (remote or vendored) |
| `plinth-skills-generator` bridge | staged `agents/*.md` for XInclude | generated Markdown from agents generator |
| `AgentIndexesTest` (transition) | behavioral substring parity | Markdown until XSLT parity proven |

## Data Flow (target)

1. Contributor edits XML agent source or inventory in `plinth-agents-generator`.
2. Build validates XSD + Schematron, runs XSLT to emit `agents/*.md`.
3. `./mvnw clean verify -pl plinth-agents-generator` checks schema and parity.
4. Skills bridge copies generated Markdown; `005-agents-installation` embed path unchanged until explicitly updated.

## Failure Handling

| Failure | Expected behavior |
|---------|-------------------|
| Wrong `@kind` profile (coder missing `<skill-rules>`) | Schematron failure with kind-specific message |
| `@id` / `<name>` mismatch | Schematron failure before XSLT |
| XSD pass but Schematron fail | CI blocks promotion; Markdown not regenerated |
| XSLT parity drift from substring tests | `AgentIndexesTest` fails until mapping fixed |

## Risks / Trade-offs

- [Risk] Agent Markdown section headings vary across nine contracts. → Use flexible `<section title="...">` containers; normalize in XSLT, not XSD enums.
- [Risk] PML repo publication lag blocks CI schema URLs. → Pin schema URLs in design doc; stub validation with local copies in generator tests until published.
- [Risk] Over-constraining optional sections breaks future agents. → Use optional elements with documented conditional requirements by agent class, not one global rigid sequence.
- [Risk] Confusion with issue #993 (commands schema). → Keep agent and command schemas separate; cross-reference only in PML documentation.

## Validation Strategy

Structural (XSD):

- Inventory and body examples validate with `xmllint --noout --schema`.
- Invalid XSD examples fail before Schematron runs.

Profile (Schematron):

- Valid analyst example passes `pml-agent.sch`.
- Invalid kind-profile example fails with documented rule id.

Behavior parity (130 — RIGHT-BICEP oriented):

- **Right:** kind-specific required sections present for each of the nine agents after migration.
- **Boundary:** empty safeguards, empty role-boundaries, duplicate inventory ids rejected.
- **Cross-check:** generated Markdown preserves `AgentIndexesTest` substrings for tech-lead routing, coder skill precedence, JDBC preferences, performance coordination.
- **Performance:** schema validation stays file-local; no runtime Maven invocation in agents.

OpenSpec: `openspec validate --all` for this change.

## ADR Candidates

| Topic | Recommendation | Status |
|-------|----------------|--------|
| Agent XML schema as companion to skill `<prompt>` | PML project docs; Plinth ADR if XML becomes source of truth | Open |
| Split inventory/body schemas | Accepted | Resolved |
| Schematron for `@kind` profiles | Co-publish with XSD in PML | Resolved pending approval |
| Kind-specific XSLT vs one template | Five kind templates + shared frontmatter partial | Resolved pending approval |

## Compatibility Review (`056`)

| Surface | Changes? | Mitigation |
|---------|----------|------------|
| Installed agent Markdown semantics | No in Step 1 | Design-only |
| Generated `005` / `002` skill embed model | No until Phase 3 | XSLT parity tests before cutover |
| `agents.xml` without namespace | No until Phase 3 | Forward-compatible optional attributes |
| `AgentIndexesTest` substrings | No until XSLT replaces checks | Slice-by-slice parity |
| Public agent names and routing | No | Schema validates structure only |

## Resolved Design Questions

| Question | Decision | Status |
|----------|----------|--------|
| Publish XSD in PML repo vs Plinth-only? | Publish under `/pml/schemas/agent/1.0.0/` | **Recommended — pending approval** |
| Split inventory/body schemas? | Yes, plus `pml-agent-types.xsd` | **Resolved** |
| Inline bodies vs file references? | `@file` references through Phase 2; `.xml` allowed from Phase 2 | **Resolved** |
| Single `<missions>` for all agents? | No — use `@kind` profiles | **Resolved** |
| XSD-only vs Schematron? | XSD superset + Schematron profiles | **Resolved pending approval** |

## Approval Checkpoint

Confirm this refined direction before implementation tasks proceed:

1. `@kind` taxonomy with five profiles matching the nine current agents.
2. XSD structural superset + co-published Schematron for semantic rules.
3. PML publication at `/schemas/agent/1.0.0/` with vendored fallback in generator tests.
4. Agent-by-agent migration slices with tech lead last.
5. Second valid example for `robot-tech-lead` added during task execution.

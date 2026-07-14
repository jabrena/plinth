## Design Refinement Summary

`/explore-design` review of the nine shipped agent contracts reveals **three distinct Markdown shapes**, not one uniform `Missions`-centric template. Coder agents (`robot-java-*`, `robot-no-java`) use `### Core Responsibilities`, `### Skill selection rules`, and `### Reference Rules` instead of `## Missions`. `robot-tech-lead` uses only `###`-level sections including routing tables and parallel-delegation rules. Forcing a single `<missions>` element for every agent would fail parity with current contracts and block migration.

**Accepted direction:**

1. Use a **single XSD** (`agent.xsd`) under the stable namespace `https://jabrena.github.io/pml/schemas/agent/0.9.0/`, hosted locally in `plinth-agents-generator/src/main/resources/pml/schemas/agent/0.9.0/` for this iteration (external [PML project](https://github.com/jabrena/pml) publication deferred). The schema defines both `<agent-inventory>` and `<agent>` root elements.
2. Define body sections (`<missions>`, `<responsibilities>`, `<routing>`, `<core-role>`, etc.) as an **optional XSD superset** — no global `<missions>` requirement.
3. Enforce **structural validation with XSD only**; no Schematron companion rules and no `@kind` validation profiles.
4. Keep kind-specific behavioral contracts in `AgentIndexesTest`, Gherkin acceptance tests, and `analysis-design-agents` until XSLT parity replaces substring checks.
5. Migrate agent-by-agent in complexity order: analyst → architect → performance → coders (shared template) → tech lead last.

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
- Document optional body section superset covering all nine current agent contracts.
- Document relationship to `pml.xsd` (skill `<prompt>`) and `pml-workflow.xsd` (workflow orchestration).
- Provide valid/invalid XML examples and migration notes for a future generator pipeline.

**Non-Goals:**

- Implement XSLT or generator migration in `plinth-agents-generator`.
- Design PML schemas for commands (`commands.xml`) or skills (`<prompt>` sources).
- Add, remove, or change agent delegation contracts or agent roles.
- Promote public `skills/` release output.
- Schematron rules or `@kind`-based schema validation profiles.

## Decisions

### Single schema: `agent.xsd`

Use one XSD document authored in Plinth for this iteration:

| Schema | Root elements | Purpose |
|--------|---------------|---------|
| `agent.xsd` | `<agent-inventory>`, `<agent>` | Inventory manifest and individual agent contracts in one schema module |

**Rationale:** Simpler local authoring and validation for this iteration; shared types (frontmatter, skill refs) live in one file. Inventory and body documents remain separate XML instances but validate against the same schema.

Alternative considered: split `pml-agent-inventory.xsd` and `pml-agent.xsd`. Rejected for this iteration in favor of a single `agent.xsd` module.

Alternative considered: extend `pml.xsd` with an `<agent>` element. Rejected because skills and agents have different frontmatter, section vocabulary, and downstream generators; coupling would complicate independent versioning.

### Namespace and schema location

- Target namespace: `https://jabrena.github.io/pml/schemas/agent/0.9.0`
- Inventory and body documents both declare `agent.xsd`:

```xml
<agent-inventory xmlns="https://jabrena.github.io/pml/schemas/agent/0.9.0"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="https://jabrena.github.io/pml/schemas/agent/0.9.0
                                     https://jabrena.github.io/pml/schemas/agent/0.9.0/agent.xsd">
```

```xml
<agent xmlns="https://jabrena.github.io/pml/schemas/agent/0.9.0"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="https://jabrena.github.io/pml/schemas/agent/0.9.0
                           https://jabrena.github.io/pml/schemas/agent/0.9.0/agent.xsd"
       id="robot-business-analyst">
```

Alternative considered: no namespace (like pre-schema `agents.xml`). Rejected to support schema evolution and cross-project reuse.

Alternative considered: reuse the command namespace from [`design-pml-commands-schema`](../design-pml-commands-schema/design.md) with shared inventory types. Rejected to keep agent and command schema evolution independent; share only optional `pml-core-types.xsd` identifier patterns (skill id, agent id) at the PML project level.

### Publication target

**This iteration:** host schema artifacts locally in Plinth, colocated with the agents generator module:

| Artifact | Local path (this iteration) |
|----------|----------------------------|
| `agent.xsd` | `plinth-agents-generator/src/main/resources/pml/schemas/agent/0.9.0/agent.xsd` |

Documents keep the stable logical namespace `https://jabrena.github.io/pml/schemas/agent/0.9.0` and use `xsi:schemaLocation` values that resolve to these local files during validation (classpath or relative path in tests).

**Deferred:** mirror skill hosting by publishing the same artifacts under `https://jabrena.github.io/pml/schemas/agent/0.9.0/` from the external PML project in a follow-up iteration.

### Agent shape reference (documentation only)

Nine agents use three Markdown shapes. This taxonomy guides XSLT mapping and migration slices; it is **not** enforced by XSD or Schematron:

| Shape | Agents | Primary body structure |
|-------|--------|------------------------|
| Analyst | `robot-business-analyst` | `<missions>`, `<role-boundaries>`, `<routing>`, `<output-format>` |
| Architect | `robot-architect` | `<identity>`, `<workflow-order>`, `<missions>`, `<workflow>`, `<constraints>`, `<output-format>` |
| Coordinator | `robot-tech-lead` | `<core-role>`, `<delivery-mission>`, `<collaboration>`, `<framework-identification>`, `<routing-table>`, `<parallel-delegation>`, `<constraints>`, `<output-format>` |
| Performance | `robot-java-performance` | `<identity>`, `<missions>`, `<output-format>`, `<safeguards>` |
| Coder | `robot-java-coder`, `robot-java-spring-boot-coder`, `robot-java-quarkus-coder`, `robot-java-micronaut-coder`, `robot-no-java` | `<responsibilities>`, `<skill-rules>`, `<reference-rules>`, `<workflow>`, `<constraints>` |

### Validation layers

| Layer | Tool | Responsibility |
|-------|------|----------------|
| 1 — Structure | XSD (`agent.xsd`) | Element presence, types, cardinalities for `<agent-inventory>` and `<agent>` superset model; required `<frontmatter>` only on body documents |
| 2 — Behavior parity | Java tests (`AgentIndexesTest`, Gherkin) | Delegation substrings, routing tables, skill references, kind-specific section requirements until XSLT parity replaces substring checks |

**Rejected:** Schematron (`pml-agent.sch`) and `@kind` validation profiles. XSD 1.0 cannot express id/name equality or kind-conditional requirements without a companion rules language; those semantics remain in behavioral tests for this iteration.

### Relationship to `pml.xsd` and `pml-workflow.xsd`

| PML artifact | Role | Agent schema relationship |
|--------------|------|---------------------------|
| `pml.xsd` | Skill `<prompt>` contract (`metadata`, `role`, `tone`, `goal`, `steps`, `constraints`, `reference`) | **Parallel, not embedded.** Agents are runtime orchestration personas with YAML-compatible frontmatter and mission/routing sections; skills are executable instruction bundles. Shared pattern: XSD-validated XML → generated Markdown with frontmatter. Reuse naming conventions (`metadata`, `constraints`) where semantics align; do not require `<prompt>` wrapper for agents. |
| `pml-workflow.xsd` | Multi-step workflow definitions | **Orthogonal.** Agent contracts may *reference* workflow commands (e.g. `/create-spec`, `/explore-design`) in mission text, but workflow graph structure stays in workflow schema. Optional future `workflow-ref` attribute on mission steps is out of scope for v0.9.0. |
| `agent.xsd` | Agent bundle manifest and contracts | **New extension family** versioned under `/pml/schemas/agent/0.9.0/`. Local in Plinth this iteration; document relationship to skill `pml.xsd` in module README or design notes until external PML publication. |

### Inventory schema shape (beyond filename-only)

Extend `<agent-inventory>` entries beyond bare `file` attributes:

```xml
<agent-inventory version="0.9.0">
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

Required inventory constraints (XSD):

- Root `<agent-inventory>` with optional `<metadata>` and one-or-more `<agent>` entries.
- Each `<agent>` MUST have unique `@id` matching `^[a-z][a-z0-9-]*$`.
- Each `<agent>` MUST provide `@file` ending in `.md` during migration phases 0–2 and MAY use `.xml` from phase 2 onward **or** an inline embedded `<agent-definition>` (optional in v0.9.0).
- Optional `@readonly` boolean mirrors frontmatter.
- `<summary>` provides inventory-table text without opening the body file.
- Child order of `<agent>` elements defines installation order (preserves current `005-agents-installation` semantics).

### Agent body schema shape

Root `<agent>` maps frontmatter and body sections:

```xml
<agent xmlns="https://jabrena.github.io/pml/schemas/agent/0.9.0"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="https://jabrena.github.io/pml/schemas/agent/0.9.0
                           https://jabrena.github.io/pml/schemas/agent/0.9.0/agent.xsd"
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

**Required for all agents (XSD):**

- `<frontmatter>` with `<name>`, `<model>`, `<description>`; `<readonly>` optional.
- `@id` on root `<agent>`.

**Optional body elements (XSD superset):**

- `<identity>`, `<missions>`, `<role-boundaries>`, `<routing>`, `<workflow-order>`, `<workflow>`, `<core-role>`, `<collaboration>`, `<framework-identification>`, `<routing-table>`, `<parallel-delegation>`, `<responsibilities>`, `<skill-rules>`, `<reference-rules>`, `<constraints>`, `<output-format>`, `<safeguards>`.
- `<skill-ref id="..."/>` inside `<skill-rules>` and `<reference-rules>` — reuse pattern from optional `pml-core-types.xsd`.

Behavioral requirements for which sections each agent shape must contain are **not** XSD-enforced; they remain in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin until XSLT migration proves parity.

Section titles in generated Markdown retain current heading variants per agent via **shape-specific XSLT templates** (analyst, architect, coordinator, performance, coder), not one global heading map.

### Frontmatter mapping

| Markdown YAML | XML element | Notes |
|---------------|-------------|-------|
| `name` | `<frontmatter><name>` | SHOULD equal `@id` and inventory `@id` (convention; not XSD-enforced) |
| `model` | `<frontmatter><model>` | Enumerated: `inherit` or explicit model slug list (extensible string) |
| `description` | `<frontmatter><description>` | Single-line installer/summary description |
| `readonly` | `<frontmatter><readonly>` or `@readonly` on inventory entry | Boolean, default false |

### Valid / invalid examples (robot-business-analyst)

**Valid (minimal):** inventory entry with `id`, `file`, `readonly`, `summary`; agent body with required frontmatter and representative optional sections from current `robot-business-analyst.md` content.

**Invalid examples to document:**

1. Missing `<frontmatter><description>` — fails XSD required element rule.
2. Duplicate `@id` in inventory — fails inventory XSD uniqueness constraint.
3. Inventory `<agent>` without `@file` and without inline body — fails inventory XSD.
4. Malformed `@id` (uppercase or invalid characters) — fails XSD pattern.

Store examples under `documentation/openspec/changes/design-pml-agents-schema/examples/` (valid + invalid pairs) as part of task delivery; reference paths from the schema design document.

### Migration notes (implementation deferred)

Two-step delivery strategy ([`051-design-two-steps-methods`](../../../.agents/skills/051-design-two-steps-methods/SKILL.md)):

**Step 1 — Schema and validation contract (this change)**

- Publish XSD + examples + migration notes.
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

1. **Phase 1:** Parallel XML under staging path; XSD in tests; Markdown authoritative.
2. **Phase 2:** Dual authoring; shape-specific XSLT; parity tests vs `AgentIndexesTest` substrings.
3. **Phase 3:** XML source of truth; generate Markdown at `generate-resources`; namespace-aware `AgentIndexes`.
4. **Phase 4:** Enriched inventory with `@readonly`, `<summary>`; generate `java-agents-inventory-template.md`.

Non-breaking rule: until Phase 3, Markdown remains the runtime installer input; XML is additive. Each slice is independently revertible.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Single `agent.xsd` for inventory and body roots | One module to author and validate locally; shared types in one file | Larger schema file | **Accepted** |
| A2. Split agent inventory + agent body XSDs | Independent validation per document type | Multiple schema files for this iteration | Rejected |
| B. Reuse `<prompt>` from `pml.xsd` for agents | Single schema family | Poor fit for frontmatter, routing tables, delegate links; conflates skills and agents | Rejected |
| C. JSON Schema instead of XSD | Popular for tooling | Breaks ADR-001 and skills pipeline precedent; no XInclude/XSLT synergy | Rejected |
| D. Inventory-only schema (defer body) | Smaller first delivery | Does not meet issue acceptance criteria for mission/routing/boundary elements | Rejected |
| E. Single global `<missions>` required element | Simple XSD | Fails coder and coordinator contracts; blocks migration | Rejected |
| F. XSD-only validation without Schematron | Fewer artifacts; matches rejected Schematron scope | Cannot express id/name equality or kind profiles in schema | **Accepted** — behavioral rules stay in tests |
| G. XSD + Schematron with `@kind` profiles | Expresses kind-specific required sections | Extra artifact; user rejected for this iteration | Rejected |

## Component Boundaries

| Component | Owns | Consumes |
|-----------|------|----------|
| `plinth-agents-generator` (this iteration) | Local `agent.xsd` under `src/main/resources/pml/schemas/agent/0.9.0/` | — |
| PML project (deferred) | External publication of same namespace artifacts | Plinth-local schemas as source |
| `plinth-agents-generator` (future) | XML sources, XSLT templates, validation tests | Local schemas from classpath |
| `plinth-skills-generator` bridge | staged `agents/*.md` for XInclude | generated Markdown from agents generator |
| `AgentIndexesTest` (transition) | behavioral substring parity | Markdown until XSLT parity proven |

## Data Flow (target)

1. Contributor edits XML agent source or inventory in `plinth-agents-generator`.
2. Build validates XSD, runs XSLT to emit `agents/*.md`.
3. `./mvnw clean verify -pl plinth-agents-generator` checks schema and parity.
4. Skills bridge copies generated Markdown; `005-agents-installation` embed path unchanged until explicitly updated.

## Failure Handling

| Failure | Expected behavior |
|---------|-------------------|
| Missing required frontmatter field | XSD validation failure with element name |
| Duplicate `@id` in inventory | XSD validation failure |
| Behavioral contract drift (missing routing table, etc.) | `AgentIndexesTest` or Gherkin failure until XSLT parity |
| XSLT parity drift from substring tests | `AgentIndexesTest` fails until mapping fixed |

## Risks / Trade-offs

- [Risk] Agent Markdown section headings vary across nine contracts. → Use flexible `<section title="...">` containers; normalize in XSLT, not XSD enums.
- [Risk] External PML publication deferred while skills already use remote `pml.xsd`. → Validate against local classpath schemas in `plinth-agents-generator` tests; plan PML publication as a separate follow-up without blocking this iteration.
- [Risk] XSD-only cannot catch id/name mismatch or empty safeguards. → Accept for this iteration; enforce via behavioral tests and XSLT review during migration slices.
- [Risk] Confusion with issue #993 (commands schema). → Keep agent and command schemas separate; cross-reference only in PML documentation.

## Validation Strategy

Structural (XSD):

- Inventory and body examples validate with `xmllint --noout --schema`.
- Invalid XSD examples fail with documented element or constraint violations.

Behavior parity (130 — RIGHT-BICEP oriented):

- **Right:** shape-specific required sections present for each of the nine agents after migration (test-enforced).
- **Boundary:** duplicate inventory ids rejected by XSD; empty safeguards caught by tests during migration.
- **Cross-check:** generated Markdown preserves `AgentIndexesTest` substrings for tech-lead routing, coder skill precedence, JDBC preferences, performance coordination.
- **Performance:** schema validation stays file-local; no runtime Maven invocation in agents.

OpenSpec: `openspec validate --all` for this change.

## ADR Candidates

| Topic | Recommendation | Status |
|-------|----------------|--------|
| Agent XML schema as companion to skill `<prompt>` | Plinth module docs this iteration; PML project docs when published | Open |
| Single `agent.xsd` module | Accepted for this iteration | **Resolved** |
| XSD-only vs Schematron | XSD-only; no `pml-agent.sch` | **Resolved** |
| Shape-specific XSLT vs one template | Five shape templates + shared frontmatter partial | Resolved |

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
| Publish XSD in PML repo vs Plinth-only? | Local under `plinth-agents-generator/src/main/resources/pml/schemas/agent/0.9.0/` this iteration | **Resolved** |
| Split inventory/body schemas? | No — single `agent.xsd` | **Resolved** |
| Inline bodies vs file references? | `@file` references through Phase 2; `.xml` allowed from Phase 2 | **Resolved** |
| Single `<missions>` for all agents? | No — optional body superset | **Resolved** |
| XSD-only vs Schematron? | **XSD-only** | **Resolved** |
| `@kind` profiles with Schematron? | **Rejected** | **Resolved** |

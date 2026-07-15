## Design Refinement Summary

`/explore-design` review of the twelve shipped command contracts reveals **four Markdown shapes**, not one uniform planning template. `implement-spec` adds orchestration sections (`Mandatory execution contract`, `Branch/worktree gate`) beyond standard workflow commands. `profile` and `benchmark` combine owner and skills in one section; `benchmark` adds a `Tool selection` table. `close-spec` is a thin CLI wrapper with `Inputs`/`Owner` headings and **no** `Associated Skills` or `Output` section. Requiring `<associated-skills>` and `<output>` on every command would fail parity and block migration.

**Recommended direction (pending approval):**

1. Keep split inventory/body XSDs under the stable namespace `https://jabrena.github.io/pml/schemas/command/1.0.0/`, with **artifact files hosted locally** in `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/` for this iteration (external [PML project](https://github.com/jabrena/pml) publication deferred) — aligned with [`design-pml-agents-schema`](../design-pml-agents-schema/design.md).
2. Add `@kind` on inventory and body `<command>` elements: `standard`, `delivery`, `performance`, `cli`.
3. Use XSD as a structural superset; enforce kind profiles and slash/id parity in co-published Schematron (`pml-command.sch`).
4. Migrate command-by-command in complexity order: standard slice → workflow-position commands → cli → performance → delivery last.

## Context

Issue [#993](https://github.com/jabrena/plinth/issues/993) designs XSD validation for Plinth command sources. Current evidence:

- `plinth-commands-generator/src/main/resources/commands.xml` — root `<command-inventory>` with `<command file="..."/>` only; no namespace, no `xsi:noNamespaceSchemaLocation`
- Twelve command contracts under `plinth-commands-generator/src/main/resources/commands/` — four Markdown shapes:
  - **Standard workflow (`kind="standard"`):** `update-issue`, `create-spec`, `explore-design`, `create-adr`, `create-diagram`, `review-alignment`, `create-feature-branch`, `create-worktree` — Purpose, Usage, Accepted Inputs/Inputs, Owning Agent/Owner, Associated Skills/Capabilities/Skill, Workflow, Output, Safeguards; some include `Workflow position`
  - **Delivery orchestration (`kind="delivery"`):** `implement-spec` — adds `Owner and delegation`, `Mandatory execution contract`, `Branch/worktree gate` before workflow
  - **Performance (`kind="performance"`):** `profile`, `benchmark` — combined `Owner and skills`; `benchmark` adds `Tool selection` table
  - **CLI wrapper (`kind="cli"`):** `close-spec` — minimal sections (`Inputs`, `Owner`), no associations, no output section
- Skills use `<prompt>` XML validated against [pml.xsd 0.8.0](https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd)
- `analysis-design-commands` defines behavioral contracts (purpose, inputs, owning agent, skills, workflow, outputs, safeguards) enforced today by `CommandIndexesTest` substring checks and Gherkin acceptance tests
- [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) establishes XSD-validated XML sources generating Markdown outputs
- Parallel change [`design-pml-agents-schema`](../design-pml-agents-schema/design.md) defines the agent schema family under `/pml/schemas/agent/1.0.0/`

The commands generator extraction ([`2026-07-13-add-commands-generator-module`](../archive/2026-07-13-add-commands-generator-module/design.md)) explicitly deferred PML command schema design to this issue.

## Goals / Non-Goals

**Goals:**

- Define XSD schemas for command inventory and command definition documents.
- Map existing Markdown section semantics to structured XML elements aligned with `analysis-design-commands`.
- Document relationship to `pml.xsd` (skill `<prompt>`) and `pml-workflow.xsd` (workflow orchestration).
- Provide valid/invalid XML examples and migration notes for a future generator pipeline.

**Non-Goals:**

- Implement XSLT or generator migration in `plinth-commands-generator`.
- Design PML schemas for agents (`agents.xml` pattern — see [#992](https://github.com/jabrena/plinth/issues/992)).
- Add, remove, or change command behavioral contracts.
- Promote public `skills/` release output.

## Decisions

### Schema split: inventory vs command body

Use two XSD documents authored in Plinth for this iteration:

| Schema | Root element | Purpose |
|--------|--------------|---------|
| `pml-command-inventory.xsd` | `<command-inventory>` | Manifest ordering, discovery metadata, file or inline references |
| `pml-command.xsd` | `<command>` | Single command contract: purpose, usage, inputs, owner, skills, workflow, output, safeguards |

**Rationale:** Matches the current two-file model (`commands.xml` + `commands/*.md`), allows independent validation, and parallels the agent schema family without conflating command and skill semantics.

Alternative considered: extend `pml-workflow.xsd` for commands. Rejected because commands are user-invoked slash directives with agent/skill delegation metadata, not multi-step workflow graphs.

### Namespace and schema location

- Target namespace: `https://jabrena.github.io/pml/schemas/command/1.0.0`
- Shared types in `pml-command-types.xsd` where inventory and body overlap (e.g. skill references, agent references)
- Inventory documents declare `pml-command-inventory.xsd`; body documents declare `pml-command.xsd`

Alternative considered: reuse the agent namespace with shared inventory types. Rejected to keep agent and command schema evolution independent; share optional `pml-core-types.xsd` identifier patterns (agent id, skill id) at the PML project level.

### Publication target

**This iteration:** host schema artifacts locally in Plinth, colocated with the commands generator module:

| Artifact | Local path (this iteration) |
|----------|----------------------------|
| `pml-command-types.xsd` | `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/pml-command-types.xsd` |
| `pml-command-inventory.xsd` | `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/pml-command-inventory.xsd` |
| `pml-command.xsd` | `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/pml-command.xsd` |
| `pml-command.sch` (Schematron) | `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/pml-command.sch` |

Documents keep the stable logical namespace `https://jabrena.github.io/pml/schemas/command/1.0.0` and use `xsi:schemaLocation` values that resolve to these local files during validation.

**Deferred:** publish the same artifacts from the external PML project in a follow-up iteration.

### Command kind taxonomy

Inventory and body documents carry `@kind` to select validation profiles and XSLT mapping templates:

| `@kind` | Commands | Primary body structure |
|---------|----------|------------------------|
| `standard` | `update-issue`, `create-spec`, `explore-design`, `create-adr`, `create-diagram`, `review-alignment`, `create-feature-branch`, `create-worktree` | `<purpose>`, `<usage>`, `<accepted-inputs>`, `<owning-agent>`, `<associations>`, `<workflow>`, `<output>`, `<safeguards>` |
| `delivery` | `implement-spec` | standard core plus `<delegation>`, `<execution-contract>`, `<branch-worktree-gate>` |
| `performance` | `profile`, `benchmark` | `<purpose>`, `<usage>`, `<accepted-inputs>`, `<ownership>`, optional `<tool-selection>`, `<workflow>`, `<output>`, `<safeguards>` |
| `cli` | `close-spec` | `<purpose>`, `<usage>`, `<accepted-inputs>`, `<owning-agent>`, `<workflow>`, `<safeguards>` — no `<associations>` or `<output>` |

Inventory entries SHOULD declare `@kind` and `@slash` so generated inventory tables do not infer kind from filename heuristics.

### Validation layers

| Layer | Tool | Responsibility |
|-------|------|----------------|
| 1 — Structure | XSD | Element presence, types, cardinalities for superset model |
| 2 — Profile | Schematron (`pml-command.sch`) | `@kind` required sections, `@slash`/`@id` parity, explore-design skill exclusions, implement-spec gate sections |
| 3 — Behavior parity | Java tests (`CommandIndexesTest`, Gherkin) | Routing substrings until XSLT parity replaces them |

### Relationship to `pml.xsd` and `pml-workflow.xsd`

| PML artifact | Role | Command schema relationship |
|--------------|------|----------------------------|
| `pml.xsd` | Skill `<prompt>` contract | **Parallel, not embedded.** Commands are slash-invoked workflow entry points with owning-agent and skill-delegation metadata; skills are executable instruction bundles. Shared pattern: XSD-validated XML → generated Markdown. Reuse naming where semantics align (`workflow`, `constraints`/`safeguards`). |
| `pml-workflow.xsd` | Multi-step workflow graph definitions | **Complementary.** Command `<workflow>` steps describe invocation procedure; workflow schema may reference commands by id in future cross-schema linking. Command schema does not embed workflow graph nodes. |
| `pml-command-inventory.xsd` / `pml-command.xsd` | Command bundle manifest and contracts | **New extension family** under `/pml/schemas/command/1.0.0/`. Local in Plinth this iteration; external PML publication deferred. |

### Inventory schema shape (beyond filename-only)

Extend `<command-inventory>` entries beyond bare `file` attributes:

```xml
<command-inventory version="1.0.0">
  <metadata>
    <title>Plinth Embedded Commands</title>
    <description>Installation-order manifest for slash commands</description>
  </metadata>
  <command id="update-issue"
           kind="standard"
           file="update-issue.xml"
           slash="/update-issue">
    <summary>Update an existing project issue with structured, evidence-backed content</summary>
    <owning-agent>robot-business-analyst</owning-agent>
  </command>
  <!-- additional commands in installation order -->
</command-inventory>
```

Required inventory constraints:

- Root `<command-inventory>` with optional `<metadata>` and one-or-more `<command>` entries.
- Each `<command>` MUST have unique `@id` matching `^[a-z][a-z0-9-]*$` and the generated Markdown command name (H1 title).
- Each `<command>` MUST provide `@file` ending in `.md` or `.xml` **or** an approved inline body reference.
- Optional `@slash` documents the user-facing invocation prefix (e.g. `/create-spec`).
- Optional `<summary>` and `<owning-agent>` support inventory tables without opening the body file.
- Child order defines installation order (preserves `004-commands-installation` semantics).

### Command body schema shape

Root `<command>` maps Markdown sections (commands have no YAML frontmatter; title derives from `@id`):

```xml
<command xmlns="https://jabrena.github.io/pml/schemas/command/1.0.0"
         id="update-issue"
         kind="standard"
         slash="/update-issue">

  <purpose>
    <paragraph>Update an existing project issue description with structured, evidence-backed content.</paragraph>
  </purpose>

  <usage>/update-issue &lt;issue&gt; [&lt;source&gt;] [&lt;tracker&gt;]</usage>

  <accepted-inputs>
    <input>Existing GitHub or Jira issue number, key, or URL</input>
    <input optional="true">Optional source notes, supporting discussion, user story, acceptance criteria, or file path</input>
    <input optional="true">Optional tracker selection: GitHub or Jira</input>
  </accepted-inputs>

  <owning-agent>robot-business-analyst</owning-agent>

  <associations>
    <association refkind="skill" id="043-planning-github-issues"/>
    <association refkind="skill" id="044-planning-jira"/>
    <association refkind="skill" id="045-planning-azure-devops"/>
    <association refkind="skill" id="014-agile-user-story" when="user-story refinement is required"/>
  </associations>

  <workflow>
    <step number="1">Load the current issue description and relevant discussion before drafting changes.</step>
    <!-- ... -->
  </workflow>

  <output>
    <item>Updated structured issue description</item>
    <item>Acceptance criteria or user-story scenario when applicable</item>
    <item>Updated GitHub/Jira issue reference</item>
  </output>

  <safeguards>
    <constraint>Do not invent requirements, acceptance criteria, or comments.</constraint>
    <constraint>Do not expose tracker credentials or tokens.</constraint>
    <constraint>Do not overwrite an issue body without showing the proposed replacement.</constraint>
    <constraint>Preserve relevant existing issue content unless the user explicitly asks to remove it.</constraint>
  </safeguards>
</command>
```

**Required for all commands (XSD + Schematron):**

- `@id`, `@kind`, and `<purpose>`
- `<usage>` — invocation syntax (CDATA allowed for angle brackets)
- `<accepted-inputs>` — one or more `<input>` elements
- `<owning-agent>` — agent id reference (XSLT emits `@robot-*` in Markdown)
- `<workflow>` — ordered `<step>` elements
- `<safeguards>` — at least one `<constraint>`

**Required by `@kind` (Schematron profiles):**

| `@kind` | Additional required sections |
|---------|------------------------------|
| `standard` | `<associations>` with at least one skill or capability reference; `<output>` |
| `delivery` | `<delegation>`, `<execution-contract>`, `<branch-worktree-gate>`, `<output>`; `<associations>` optional (delegation carries targets) |
| `performance` | `<ownership>` combining owner and skills; `<output>`; `benchmark` also requires `<tool-selection>` |
| `cli` | No `<associations>` or `<output>` |

**Optional elements (all kinds where applicable):**

- `@slash` — user-facing prefix; SHOULD match inventory entry
- `<workflow-position>` — lifecycle placement (`create-spec`, `explore-design`)
- `<tool-selection>` — performance routing table (`benchmark`)
- `<execution-contract>`, `<branch-worktree-gate>`, `<delegation>` — delivery-only but present in XSD superset

**Association normalization:** Markdown headings `Associated Skills`, `Associated Skill`, `Associated Capabilities`, and combined `Owner and skills` map to `<associations>` with `@refkind="skill"` or `@refkind="capability"`, or to `<ownership>` for performance commands.

Section titles in generated Markdown retain current per-command heading variants via **kind-specific XSLT templates** (`standard.xsl`, `delivery.xsl`, `performance.xsl`, `cli.xsl`).

**Schematron behavioral rules (examples):**

- `explore-design` MUST NOT reference `042-planning-openspec` or `034-architecture-design-exploration` in associations
- `create-spec` MUST reference `042-planning-openspec` and document workflow position before `explore-design`
- `close-spec` (`cli`) MUST NOT include `<output>` or non-empty `<associations>`
- `implement-spec` MUST include branch/worktree gate steps referencing `/create-feature-branch` and `/create-worktree`

### Valid / invalid examples (update-issue)

**Valid:** inventory entry with `id`, `file`, `slash`, `summary`, `owning-agent`; body with all required sections populated from current `update-issue.md`.

**Invalid examples to document:**

1. Missing `<workflow>` — fails command body schema.
2. Duplicate `@id` in inventory — fails inventory schema.
3. Missing `<owning-agent>` — fails command body schema.
4. Empty `<safeguards>` — fails command body schema.
5. Inventory `<command>` without `@file` and without inline body — fails inventory XSD.
6. `kind="cli"` document with `<output>` — fails Schematron profile.
7. `kind="delivery"` missing `<branch-worktree-gate>` — fails Schematron profile.

Add valid examples for `close-spec` (`kind="cli"`) and invalid `kind="standard"` missing `<output>` during task execution. Add `implement-spec` valid example when delivery profile is implemented.

Examples live under `documentation/openspec/changes/design-pml-commands-schema/examples/`.

### Migration notes (implementation deferred)

Two-step delivery strategy ([`051-design-two-steps-methods`](../../../.agents/skills/051-design-two-steps-methods/SKILL.md)):

**Step 1 — Schema and validation contract (this change)**

- Publish XSD + Schematron + examples + migration notes.
- No generator or installer behavior change.

**Step 2 — Generator migration (follow-up issues, command-by-command slices)**

| Slice | Command(s) | Rationale |
|-------|------------|-----------|
| 1 | `update-issue` | Standard profile; existing valid example |
| 2 | `create-spec`, `explore-design` | Adds `<workflow-position>` and Schematron skill rules |
| 3 | `create-adr`, `create-diagram`, `review-alignment` | Standard variants with capabilities vs skills |
| 4 | `create-feature-branch`, `create-worktree` | Git isolation inputs |
| 5 | `close-spec` | CLI profile without output/associations |
| 6 | `profile`, `benchmark` | Performance profile with `<ownership>` and tool selection |
| 7 | `implement-spec` | Delivery profile; migrate last |

Phases within each slice: parallel XML staging → dual authoring with kind-specific XSLT → XML source of truth → enriched inventory generation.

Non-breaking until Phase 3: Markdown remains runtime installer input; `004-commands-installation` asset-link model unchanged.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Split command inventory + command body XSDs | Matches current layout; parallels agents; clear validation | Two schema files | **Recommended** |
| B. Embed commands in `pml-workflow.xsd` | Single workflow family | Poor fit for slash commands and agent delegation metadata | Rejected |
| C. Reuse `<prompt>` from `pml.xsd` | One schema family | Commands lack skill-style metadata/role/tone; different section model | Rejected |
| D. Inventory-only schema (defer body) | Smaller delivery | Does not meet issue acceptance criteria | Rejected |
| E. Single `<associated-skills>` + `<output>` for all commands | Simple XSD | Fails `close-spec` cli profile and `implement-spec` delivery shape | Rejected |
| F. XSD-only validation without Schematron | Fewer artifacts | Cannot express `@kind` profiles or explore-design skill exclusions | Rejected |

## Component Boundaries

| Component | Owns | Consumes |
|-----------|------|----------|
| `plinth-commands-generator` (this iteration) | Local XSD, Schematron under `src/main/resources/pml/schemas/command/1.0.0/` | — |
| PML project (deferred) | External publication of same namespace artifacts | Plinth-local schemas as source |
| `plinth-commands-generator` (future) | XML sources, kind-specific XSLT, validation tests | PML schemas (remote or vendored) |
| `plinth-skills-generator` bridge | staged `commands/*.md` asset links | generated Markdown from commands generator |
| `CommandIndexesTest` (transition) | behavioral substring parity | Markdown until XSLT parity proven |

## Failure Handling

| Failure | Expected behavior |
|---------|-------------------|
| `cli` command includes `<output>` | Schematron failure |
| `delivery` missing branch/worktree gate | Schematron failure |
| `explore-design` lists `042-planning-openspec` | Schematron failure |
| XSD pass but Schematron fail | CI blocks promotion |

## Risks / Trade-offs

- [Risk] Inconsistent Markdown headings across twelve contracts. → Flexible section containers; XSLT normalization.
- [Risk] Divergence from agent schema conventions. → Share PML documentation patterns and type naming; keep separate namespaces.
- [Risk] PML publication lag. → Pin schema URLs; local copies in generator tests until published.
- [Risk] Over-constraining skill lists blocks command evolution. → `<skill>` optional `@when` attribute for conditional associations.

## Validation Strategy

Structural (XSD):

- Inventory and body examples validate with `xmllint --noout --schema`.

Profile (Schematron):

- Valid `standard` example passes; invalid `cli` with `<output>` fails.
- `explore-design` and `implement-spec` rules documented with rule ids.

Behavior parity (130):

- **Right:** kind-specific sections for all twelve commands after migration.
- **Boundary:** empty safeguards rejected; duplicate inventory ids rejected.
- **Cross-check:** generated Markdown preserves `CommandIndexesTest` substrings for create-spec, explore-design, implement-spec gates, close-spec CLI steps, performance routing.

OpenSpec: `openspec validate --all`.

## ADR Candidates

| Topic | Recommendation | Status |
|-------|----------------|--------|
| Command XSD as parallel PML family | PML project docs | Open |
| Schematron co-publication | Co-publish with XSD | Resolved pending approval |
| Kind-specific XSLT templates | Four templates + shared partials | Resolved pending approval |
| Shared `pml-core-types.xsd` with agent schema | Optional cross-family id patterns | Open |

## Compatibility Review (`056`)

| Surface | Changes? | Mitigation |
|---------|----------|------------|
| Installed command Markdown semantics | No in Step 1 | Design-only |
| `004-commands-installation` asset-link model | No until Phase 3 | Parity tests before cutover |
| `commands.xml` without namespace | No until Phase 3 | Forward-compatible attributes |
| `CommandIndexesTest` substrings | No until XSLT replaces checks | Slice-by-slice parity |

## Resolved Design Questions

| Question | Decision | Status |
|----------|----------|--------|
| Publish XSD in PML repo? | Local under `plinth-commands-generator/src/main/resources/pml/schemas/command/1.0.0/` this iteration | **Resolved** |
| Split inventory/body schemas? | Yes, plus `pml-command-types.xsd` | **Resolved** |
| Normalize headings in XSD vs XSLT? | XSLT per `@kind` | **Resolved** |
| Require associations/output on all commands? | No — use `@kind` profiles | **Resolved** |
| XSD-only vs Schematron? | XSD superset + `pml-command.sch` | **Resolved pending approval** |

## Approval Checkpoint

Confirm this refined direction before implementation tasks proceed:

1. `@kind` taxonomy: `standard`, `delivery`, `performance`, `cli`.
2. XSD superset + Schematron for kind profiles and command-specific behavioral rules.
3. Local schema hosting in `plinth-commands-generator` with stable logical namespace for future PML publication.
4. Command migration slices with `implement-spec` last.
5. Additional examples for `close-spec` (cli) during task execution.

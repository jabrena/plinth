## Context

Issue [#993](https://github.com/jabrena/plinth/issues/993) designs XSD validation for Plinth command sources. Current evidence:

- `plinth-commands-generator/src/main/resources/commands.xml` — root `<command-inventory>` with `<command file="..."/>` only; no namespace, no `xsi:noNamespaceSchemaLocation`
- Twelve command contracts under `plinth-commands-generator/src/main/resources/commands/` — unstructured Markdown with variable section headings (`Owning Agent` vs `Owner`, `Accepted Inputs` vs `Inputs`, `Associated Skills` vs `Associated Capabilities`, `Workflow position` for workflow-order commands)
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

Use two XSD documents published from the PML project:

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

Alternative considered: reuse the agent namespace with shared inventory types. Rejected to keep agent and command evolution independent.

### Relationship to `pml.xsd` and `pml-workflow.xsd`

| PML artifact | Role | Command schema relationship |
|--------------|------|----------------------------|
| `pml.xsd` | Skill `<prompt>` contract | **Parallel, not embedded.** Commands are slash-invoked workflow entry points with owning-agent and skill-delegation metadata; skills are executable instruction bundles. Shared pattern: XSD-validated XML → generated Markdown. Reuse naming where semantics align (`workflow`, `constraints`/`safeguards`). |
| `pml-workflow.xsd` | Multi-step workflow graph definitions | **Complementary.** Command `<workflow>` steps describe invocation procedure; workflow schema may reference commands by id in future cross-schema linking. Command schema does not embed workflow graph nodes. |
| `pml-command-inventory.xsd` / `pml-command.xsd` | Command bundle manifest and contracts | **New extension family** under `/pml/schemas/command/1.0.0/`, documented alongside agent and skill schema families in the PML project. |

### Inventory schema shape (beyond filename-only)

Extend `<command-inventory>` entries beyond bare `file` attributes:

```xml
<command-inventory version="1.0.0">
  <metadata>
    <title>Plinth Embedded Commands</title>
    <description>Installation-order manifest for slash commands</description>
  </metadata>
  <command id="update-issue"
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

  <associated-skills>
    <skill id="043-planning-github-issues"/>
    <skill id="044-planning-jira"/>
    <skill id="045-planning-azure-devops"/>
    <skill id="014-agile-user-story" when="user-story refinement is required"/>
  </associated-skills>

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

**Required elements (all commands):**

- `@id` and `<purpose>`
- `<usage>` — invocation syntax (may contain CDATA for angle brackets)
- `<accepted-inputs>` — one or more `<input>` elements
- `<owning-agent>` — agent id reference (without `@` prefix in XML; XSLT adds `@` in Markdown)
- `<associated-skills>` — zero or more `<skill>` references (zero allowed for thin wrapper commands; most analysis/design commands require at least one)
- `<workflow>` — ordered `<step>` elements
- `<output>` — expected artifacts or reports
- `<safeguards>` — at least one `<constraint>`

**Optional elements:**

- `@slash` — redundant with inventory but useful in standalone body files
- `<workflow-position>` — narrative placement in the analysis/design lifecycle (e.g. "Runs first before `/explore-design`" on `create-spec`)
- `<associated-capabilities>` — alias container when commands reference OpenSpec capabilities instead of skills (normalize to `<associated-skills>` in XSD with optional `@kind="capability"`)

Section title variants in current Markdown (`Owner`, `Owning Agent`, `Inputs`, `Accepted Inputs`) normalize via XSLT, not XSD string enums.

### Conditional requirements (by command class)

| Command class | Additional guidance |
|---------------|----------------------|
| OpenSpec lifecycle (`create-spec`, `close-spec`, `implement-spec`) | `<workflow-position>` recommended; `<owning-agent>` typically `robot-architect` or `robot-tech-lead` |
| Design refinement (`explore-design`) | `<associated-skills>` MUST list design skills per `analysis-design-commands`; MUST NOT list `042-planning-openspec` |
| Read-only review (`review-alignment`) | `<safeguards>` MUST include no auto-modification constraints |
| Git/worktree (`create-feature-branch`, `create-worktree`) | `<accepted-inputs>` MUST document branch/path/base parameters |

### Valid / invalid examples (update-issue)

**Valid:** inventory entry with `id`, `file`, `slash`, `summary`, `owning-agent`; body with all required sections populated from current `update-issue.md`.

**Invalid examples to document:**

1. Missing `<workflow>` — fails command body schema.
2. Duplicate `@id` in inventory — fails inventory schema.
3. Missing `<owning-agent>` — fails command body schema.
4. Empty `<safeguards>` — fails command body schema.
5. Inventory `<command>` without `@file` and without inline body — fails inventory schema.

Examples live under `documentation/openspec/changes/design-pml-commands-schema/examples/`.

### Migration notes (implementation deferred)

Phased migration (mirrors agent schema [`MIGRATION.md`](../design-pml-agents-schema/MIGRATION.md)):

1. **Phase 0 (this change):** Publish XSD, design doc, examples, migration notes only.
2. **Phase 1:** Add optional XSD validation in `plinth-commands-generator` tests against parallel XML sources; Markdown remains authoritative.
3. **Phase 2:** Author XML alongside Markdown; XSLT generates Markdown; parity tests match `CommandIndexesTest` substrings.
4. **Phase 3:** XML source of truth; generate Markdown during `generate-resources`.
5. **Phase 4:** Enrich `commands.xml` with `@slash`, `<summary>`, `<owning-agent>`; generate `java-commands-inventory-template.md` from validated inventory XML.

Non-breaking until Phase 3: Markdown remains runtime installer input.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Split command inventory + command body XSDs | Matches current layout; parallels agents; clear validation | Two schema files | **Recommended** |
| B. Embed commands in `pml-workflow.xsd` | Single workflow family | Poor fit for slash commands and agent delegation metadata | Rejected |
| C. Reuse `<prompt>` from `pml.xsd` | One schema family | Commands lack skill-style metadata/role/tone; different section model | Rejected |
| D. Inventory-only schema (defer body) | Smaller delivery | Does not meet issue acceptance criteria | Rejected |

## Risks / Trade-offs

- [Risk] Inconsistent Markdown headings across twelve contracts. → Flexible section containers; XSLT normalization.
- [Risk] Divergence from agent schema conventions. → Share PML documentation patterns and type naming; keep separate namespaces.
- [Risk] PML publication lag. → Pin schema URLs; local copies in generator tests until published.
- [Risk] Over-constraining skill lists blocks command evolution. → `<skill>` optional `@when` attribute for conditional associations.

## Validation Strategy

- XSD validity via `xmllint --noout --schema` once XSDs are drafted.
- Valid examples pass; invalid examples fail with documented violations.
- Future parity tests: XML → Markdown preserves substrings checked in `CommandIndexesTest`.
- OpenSpec: `openspec validate --all`.

## Compatibility Review

- **NON-BREAKING:** Design-only; no changes to installed command contracts or generated skills.
- Existing `commands.xml` without namespace remains valid until Phase 3.
- `analysis-design-commands` behavioral requirements unchanged.

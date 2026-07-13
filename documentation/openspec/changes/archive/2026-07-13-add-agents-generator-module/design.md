## Design Refinement Summary

`/explore-design` review against the shipped `plinth-commands-generator` implementation ([#1035](https://github.com/jabrena/plinth/issues/1035)) confirms the extraction pattern but **must not copy command propagation tests verbatim**. Agent installer and inventory skills (`002`, `005`) embed bundle content inline in generated references through XInclude `parse="text"`, while command skills (`001`, `004`) ship linked assets under `assets/commands/` without embedding command bodies.

**Recommended direction (pending approval):** mirror the commands module boundary and bridge mechanics, introduce `agents.xml` + `AgentIndexes`, reuse `InventoryXmlLoader` from `plinth-commands-generator`, and implement agent-specific propagation and installer parity tests that assert **embedded reference bodies**, not asset links.

## Context

Issue [#1036](https://github.com/jabrena/plinth/issues/1036) extracts agent ownership from `plinth-skills-generator` into `plinth-agents-generator`. Today:

- Nine embedded robot agents live under `plinth-skills-generator/src/main/resources/skill-references/assets/agents/`.
- `java-agents-inventory-template.md` defines the inventory table consumed by skill `002-agents-inventory`.
- `005-agents-installation.xml` embeds all nine agent definitions through XInclude and is the installer contract for `.cursor/agents`, `.claude/agents`, `.codex/agents`, and `.github/agents`.
- `SkillsGeneratorTest.EmbeddedAgentBundleTests` validates installer/inventory parity, tech-lead routing, coder skill precedence, framework JDBC preferences, and the nine-agent bundle; it contains `//TODO Move to plinth-agents-generator ASAP`.
- Skills `002-agents-inventory` and `005-agents-installation` embed agent content through XInclude at build time.
- Agents must not invoke Maven at runtime; generated skills under `.agents/skills/` or `skills/` are the runtime artifact.
- `plinth-commands-generator` ([#1035](https://github.com/jabrena/plinth/issues/1035)) already owns `InventoryXmlLoader` and provides the reference extraction pattern.

Phase 1 previously merged generators to remove cross-generator coupling. This change reintroduces deliberate one-way dependencies: `plinth-agents-generator` -> `plinth-commands-generator` and `plinth-skills-generator` -> `plinth-agents-generator`.

## Goals / Non-Goals

**Goals:**

- Own agent inventory manifest, assets, loader, and agent-focused tests in `plinth-agents-generator`.
- Preserve agent bundle and delegation behavior required by `analysis-design-agents`.
- Bridge agent assets into `plinth-skills-generator` during `generate-resources`.
- Prove agent-to-skill propagation through automated tests and updated Gherkin acceptance coverage.
- Document contributor commands for isolated and integrated builds.

**Non-Goals:**

- Design a PML agent schema ([#993](https://github.com/jabrena/plinth/issues/993)).
- Add, remove, or change agent delegation contracts or agent roles.
- Move skill `200-agents-md` sources or AGENTS.md generation templates into `plinth-agents-generator`.
- Make skills invoke `./mvnw` at agent runtime.
- Promote public `skills/` release output unless explicitly requested.

## Two-Step Change Strategy

### Step 1: Behavior-preserving extraction

Create `plinth-agents-generator`, introduce `agents.xml` and `AgentIndexes.java`, relocate agent sources and agent-owned tests, wire one-way dependencies, and stage bridged assets into `plinth-skills-generator` without changing agent contracts or generated skill semantics.

Validation after Step 1:

- `./mvnw clean verify -pl plinth-agents-generator`
- `./mvnw clean install -pl plinth-skills-generator -am`
- `./mvnw clean verify`
- Generated `.agents/skills/002-agents-inventory` and `.agents/skills/005-agents-installation` match pre-extraction behavior for embedded agent content.

### Step 2: Inventory template hardening (same change if low risk)

Generate `java-agents-inventory-template.md` from `agents.xml` during the bridge step so `002-agents-inventory` cannot drift from the agent manifest.

Validation after Step 2:

- Removing or renaming an agent in `agents.xml` without updating generated template output fails `plinth-agents-generator` or bridge parity tests.
- `002` generated reference still lists exactly the manifest agents.

If Step 2 increases move risk, ship Step 1 first and follow immediately with template generation in the same PR only when parity tests are already green.

## Recommended Architecture

```text
plinth-commands-generator (shared XML helper owner)
└── InventoryXmlLoader.java

plinth-agents-generator (source of truth)
├── depends on plinth-commands-generator
├── src/main/resources/agents.xml
├── src/main/resources/agents/*.md
├── src/main/resources/java-agents-inventory-template.md
├── src/main/java/info/jab/pml/AgentIndexes.java
└── src/test/java + gherkin/agents/

plinth-skills-generator (consumer + skill owner)
├── depends on plinth-agents-generator (and plinth-commands-generator transitively or directly)
├── generate-resources bridge
│   └── copy/unpack → target/generated-resources/.../skill-references/assets/agents/
├── skill-references/002-agents-inventory.xml   (unchanged XInclude paths)
├── skill-references/005-agents-installation.xml (unchanged XInclude paths)
└── tests: bridge parity + skill propagation
```

```mermaid
flowchart TB
  subgraph CG[plinth-commands-generator]
    IXL[InventoryXmlLoader]
  end

  subgraph AG[plinth-agents-generator]
    AX[agents.xml]
    AGS[agents/*.md]
    AI[AgentIndexes]
    AIT[AgentIndexesTest + agent Gherkin]
  end

  subgraph SG[plinth-skills-generator generate-resources]
    STAGE[target/generated-resources/skill-references/assets/agents]
    GEN[SkillsGenerator / XSLT XInclude]
  end

  subgraph OUT[generated output]
    SKILLS[.agents/skills or skills/]
  end

  IXL --> AG
  AX --> AI
  AGS --> AG
  AG -->|mvn package| STAGE
  STAGE --> GEN
  GEN --> SKILLS
```

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|----------|------|------|----------|
| A. Mirror `plinth-commands-generator` bridge with `agents.xml` manifest | Proven reactor pattern; manifest-driven parity; independent ownership | Requires new manifest and test split; `plinth-agents-generator` depends on `plinth-commands-generator` for XML helper | **Recommended** |
| B. Move assets only; keep hand-maintained inventory template without manifest | Smaller first diff | No manifest parity; installer/template drift returns; diverges from commands model | Rejected |
| C. Migrate `002`/`005` to asset-link output like `001`/`004` | Uniform propagation tests across bundles | Changes generated skill semantics; breaks skill-only installs that rely on embedded reference bodies; out of scope | Rejected for this change |
| D. Extract `InventoryXmlLoader` into a shared `pml-common` module | Removes semantic coupling between bundle modules | New module scope; contradicts commands decision to colocate helper with first owner | Rejected |
| E. Duplicate `InventoryXmlLoader` in `plinth-agents-generator` | No cross-module dependency | Two copies to maintain; violates commands design | Rejected |

## Success Criteria

- `./mvnw clean verify -pl plinth-agents-generator` validates manifest, assets, and relocated contract tests.
- `./mvnw clean install -pl plinth-skills-generator -am` stages bridged agent assets and regenerates `.agents/skills/002-agents-inventory` and `.agents/skills/005-agents-installation` with **byte-equivalent embedded content** to pre-extraction output.
- No change to agent names, delegation routing, installed filenames, or nine-agent bundle semantics.
- `SkillsGeneratorTest` no longer owns `EmbeddedAgentBundleTests`; installer parity and propagation guards live in dedicated `plinth-skills-generator` tests.

## Decisions

### Module naming and coordinates

Use directory and artifact id `plinth-agents-generator` with `groupId` `info.jab.pml`, matching the `plinth-commands-generator` and `plinth-skills-generator` pattern.

### `plinth-agents-generator` is the source of truth

`plinth-agents-generator` owns `agents.xml`, `agents/*.md`, `AgentIndexes.java`, `java-agents-inventory-template.md`, agent contract tests, and agent Gherkin features.

Agent assets move from `skill-references/assets/agents/` to `src/main/resources/agents/`.

Alternative considered: keep sources in `plinth-skills-generator` and only add a thin wrapper module. Rejected because it preserves the ownership problem the issue is solving.

### Introduce `agents.xml` manifest

Unlike the pre-extraction agent bundle, which relied on installer XIncludes and a hand-maintained inventory template, `plinth-agents-generator` introduces `agents.xml` registering the nine embedded agents in installation order (matching `005-agents-installation.xml`).

Alternative considered: keep inventory-only without a manifest. Rejected because `plinth-commands-generator` already established manifest-driven parity tests and drift prevention.

### `agents.xml` shape

Mirror the command inventory document without waiting for PML schema ([#993](https://github.com/jabrena/plinth/issues/993)):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<agent-inventory>
    <agent file="robot-business-analyst.md"/>
    <agent file="robot-architect.md"/>
    <agent file="robot-tech-lead.md"/>
    <agent file="robot-no-java.md"/>
    <agent file="robot-java-performance.md"/>
    <agent file="robot-java-coder.md"/>
    <agent file="robot-java-micronaut-coder.md"/>
    <agent file="robot-java-quarkus-coder.md"/>
    <agent file="robot-java-spring-boot-coder.md"/>
</agent-inventory>
```

`AgentIndexes.agentFiles()` returns installation-order file names. Root element MUST be `<agent-inventory>`; entries MUST be direct-child `<agent file="..."/>` elements ending in `.md`.

### Embed-first skill propagation model (differs from commands)

| Skill | Current output model | `skills.xml` resources | Propagation test expectation |
|-------|---------------------|------------------------|------------------------------|
| `004-commands-installation` | Reference links to `assets/commands/*.md`; assets shipped separately | Explicit `<resource-list>` per command | `CommandSkillPropagationTest` asserts links exist and bodies are **not** embedded |
| `005-agents-installation` | Reference embeds full agent bodies via XInclude `parse="text"` | No `<resource-list>` | `AgentSkillPropagationTest` asserts each staged agent body **is embedded** in reference |
| `001-commands-inventory` | Reference links to template asset | Template in `<resource-list>` | Assert template asset shipped; reference does not embed rows |
| `002-agents-inventory` | Reference embeds full inventory template inline | No `<resource-list>` | Assert all manifest agent rows appear embedded in reference |

Do **not** add `<resource-list>` entries for `002` or `005` in this change. Changing to the command asset-link model is a separate compatibility decision.

### Bridge at `plinth-skills-generator` `generate-resources`

`plinth-skills-generator` declares a Maven dependency on `plinth-agents-generator` and, during `generate-resources`:

1. Copies or unpacks `agents/*.md` and `java-agents-inventory-template.md` from `plinth-agents-generator` into `target/generated-resources/`.
2. Exposes staged files at `skill-references/assets/agents/` and `skill-references/assets/java-agents-inventory-template.md` on the main classpath.
3. Optionally generates inventory rows from `agents.xml` before skill generation.

Implementation preference: mirror the sibling-module copy pattern used by `plinth-commands-generator` bridge (`maven-resources-plugin:copy-resources`), followed by test classpath staging so `SkillReferenceGenerator` continues resolving existing XInclude paths without XML edits.

Add two executions alongside the existing command bridge:

| Execution id | Source | Staged target |
|--------------|--------|---------------|
| `bridge-agent-assets` | `../plinth-agents-generator/src/main/resources/agents` | `target/generated-resources/skill-references/assets/agents` |
| `bridge-agent-inventory-template` | `../plinth-agents-generator/src/main/resources/java-agents-inventory-template.md` | `target/generated-resources/skill-references/assets` |

Reuse the existing `copy-bridged-command-assets-to-test` execution (copies all of `target/generated-resources`) so bridged agent files land on the test classpath without a third copy step.

Alternative considered: point XIncludes directly at dependency JAR paths. Rejected for the first iteration because it changes `SkillReferenceGenerator` base URIs and complicates local IDE resolution.

Alternative considered: runtime classpath lookup from agents. Rejected because skills must remain self-contained generated artifacts.

### One-way dependencies only

- `plinth-agents-generator` depends on `plinth-commands-generator` (reuse `InventoryXmlLoader`).
- `plinth-skills-generator` depends on `plinth-agents-generator`.
- Neither upstream module depends on `plinth-skills-generator`.

Do not duplicate `InventoryXmlLoader`. Do not add a separate shared-library module.

### Split validation responsibilities

`EmbeddedAgentBundleTests` currently spans agent ownership and skill-installer ownership. Split as follows:

| Concern | Module | Test class | Examples |
|--------|--------|------------|----------|
| Manifest integrity, asset presence, per-agent contracts | `plinth-agents-generator` | `AgentIndexesTest` | `agents.xml` order; tech-lead routing; coder skill precedence; JDBC preferences; coordinator absence |
| Inventory template rows vs manifest | `plinth-agents-generator` | `AgentIndexesTest` | template lists exactly nine agents with correct backtick names |
| Installer XInclude parity with manifest | `plinth-skills-generator` | `AgentInstallerParityTest` | `005-agents-installation.xml` XInclude hrefs match manifest |
| Bridge staging correctness | `plinth-skills-generator` | `AgentBridgeTest` | staged `skill-references/assets/agents/*` match `AgentIndexes.agentFiles()` |
| Generated skill propagation | `plinth-skills-generator` | `AgentSkillPropagationTest` | `002` / `005` references embed staged bodies |

**Important:** `AgentInstallerParityTest` MUST parse `005-agents-installation.xml` XInclude hrefs (as `EmbeddedAgentBundleTests` does today). It MUST NOT use `SkillIndexes.skillDescriptors().resources()` because `005` has no `<resource-list>` in `skills.xml` — unlike `004-commands-installation`.

Relocate these `EmbeddedAgentBundleTests` methods to `AgentIndexesTest` unchanged except for classpath prefix `agents/` instead of `skill-references/assets/agents/`:

- `should_installRenamedAnalysisDesignAgents_withoutCoordinatorAlias` — asset/inventory assertions only; installer XML check moves to `AgentInstallerParityTest`
- `should_referenceAllCoderAgents_when_techLeadCoordinatesDelivery` — tech-lead + installer split as above
- `should_routeNonJavaWork_when_executionArtifactIsNotJava`
- `should_coordinatePerformanceWorkflows_when_javaPerformanceAgentIsInstalled`
- `should_shareSkillPrecedence_when_coderAgentsImplementChanges`
- `should_preferJdbc_when_frameworkCoderSelectsRelationalPersistence`

### Runtime installer path in the Plinth repo

For `005-agents-installation`, update the repository copy source path to `plinth-agents-generator/src/main/resources/agents/`. For skill-only installs from `skills/`, embedded reference content remains the fallback.

### Generated-resources must not be committed

Bridged agent assets under `plinth-skills-generator` source tree are removed after the bridge works. Only `target/generated-resources/` (or equivalent) holds staged copies during build. This prevents dual ownership from returning.

### Skill `200-agents-md` stays in plinth-skills-generator

`200-agents-md` generates repository `AGENTS.md` through interactive templates and is not part of the embedded nine-agent bundle. Its sources remain in `plinth-skills-generator`.

## Component Boundaries

| Component | Owns | Consumes |
|-----------|------|----------|
| `plinth-commands-generator` | `InventoryXmlLoader`, command bundle | parent POM only |
| `plinth-agents-generator` | `agents.xml`, `agents/*.md`, loaders, agent tests | `plinth-commands-generator` |
| `plinth-skills-generator` bridge | generated `skill-references/assets/agents/` staging | `plinth-agents-generator` JAR |
| `plinth-skills-generator` skills | `002` / `005` XML, installer wording, skill Gherkin | staged agent assets |
| Agents at runtime | generated `.agents/skills` or `skills/` | not Maven modules |

## Data Flow

1. Contributor edits `plinth-agents-generator/src/main/resources/agents/*.md` or `agents.xml`.
2. `./mvnw clean verify -pl plinth-agents-generator` validates manifest, assets, and contracts.
3. `./mvnw clean install -pl plinth-skills-generator -am` rebuilds `plinth-agents-generator`, stages assets for XInclude, runs skill generation tests, copies output to `.agents/skills/`.
4. Skill `005` may copy from `plinth-agents-generator/.../agents/` when run inside the Plinth repo; otherwise it uses embedded reference markdown.

## Failure Handling

| Failure | Expected behavior |
|---------|-------------------|
| Missing agent asset for manifest entry | `plinth-agents-generator` tests fail |
| `005` XInclude list diverges from manifest | `plinth-skills-generator` installer parity test fails |
| Bridge not run or stale JAR used | skill propagation / bridge tests fail; docs require `-am` |
| Contributor runs `-pl plinth-skills-generator` without `-am` after agent edits | may use stale `~/.m2` artifact; document and optionally add reactor-order note in failure message |
| `InventoryXmlLoader` not reachable from `plinth-agents-generator` | `plinth-agents-generator` compile failure until dependency is wired |

## Testing Strategy

**Unit / focused tests (`plinth-agents-generator`)**

- Manifest load order and uniqueness
- Every `agents.xml` entry has `agents/<file>`
- Per-agent contract assertions currently in `EmbeddedAgentBundleTests`
- Relocated agent Gherkin features and prompt inventory paths

**Integration tests (`plinth-skills-generator`)**

- `AgentBridgeTest`: staged `skill-references/assets/agents/` matches `AgentIndexes.agentFiles()`; staged inventory template present
- `AgentInstallerParityTest`: `005-agents-installation.xml` direct-child XInclude hrefs equal `assets/agents/<file>` for every manifest entry
- `AgentSkillPropagationTest`:
  - **005:** for each manifest agent, generated reference contains a unique substring from the bridged asset (for example `name: robot-architect`) and does **not** merely link to `assets/agents/<file>`
  - **002:** generated reference embeds `# Embedded Agents Inventory` and every `` `robot-*` `` row from the bridged template; does **not** link to `assets/java-agents-inventory-template.md` as a separate shipped asset

**Acceptance / Gherkin**

- Update `002-agents-inventory.feature` and `005-agents-installation.feature` to assert generated skill content and `plinth-agents-generator` source paths
- Keep agent command-Gherkin under `plinth-agents-generator`; skill Gherkin under `plinth-skills-generator`

**Verification commands**

```bash
./mvnw clean verify -pl plinth-agents-generator
./mvnw clean install -pl plinth-skills-generator -am
./mvnw clean verify
```

## Compatibility Review

- No change to public agent names, routing tables, or delegation contracts in agent markdown.
- No change to installed `.cursor/agents/` file names.
- No change to generated skill output model: `002`/`005` remain embed-first; `001`/`004` remain asset-link.
- Skill-only installs from `skills/` continue to work because embedded reference bodies remain self-contained after bridge regeneration.
- `analysis-design-agents` bundle and delegation requirements remain satisfied; only source ownership moves.
- Phase 1 "no generator coupling" principle is preserved as directed dependencies, not cycles.
- Mirrors the completed `plinth-commands-generator` ([#1035](https://github.com/jabrena/plinth/issues/1035)) extraction pattern.

### Breaking-change surfaces reviewed (`056`)

| Surface | Changes? | Mitigation |
|---------|----------|------------|
| Generated `005` reference structure | No | Embed-first model preserved |
| Generated `002` reference structure | No | Template remains inline |
| Contributor source paths | Yes (intentional) | Update `005-agents-installation.xml` repo copy-path guidance and docs |
| Gherkin acceptance paths | Yes (intentional) | Update features to reference `plinth-agents-generator` and embedded reference semantics |
| Public agent routing contracts | No | Contract tests relocate unchanged |

## Risks / Trade-offs

- [Risk] Stale agent assets if contributors omit `-am`. -> Document in `AGENTS.md` / `DEVELOPER.md`; bridge tests catch most drift.
- [Risk] Inventory template drift from `agents.xml`. -> Step 2 template generation.
- [Risk] Splitting `EmbeddedAgentBundleTests` incompletely leaves blind spots. -> Explicit test matrix above.
- [Risk] Generated-resources classpath ordering breaks XInclude. -> Add bridge test before relying on skill generation.
- [Risk] Overlap with `plinth-commands-generator` bridge logic. -> Reuse the same staging pattern; keep bridges independent per bundle.

## Migration Plan

1. Create `plinth-agents-generator` module and register it in root `pom.xml`.
2. Introduce `agents.xml` and `AgentIndexes.java`; move agent assets, inventory template, agent-owned tests, and agent Gherkin.
3. Add `plinth-agents-generator` dependency on `plinth-commands-generator`; add `plinth-skills-generator` dependency on `plinth-agents-generator`.
4. Add dependency and `generate-resources` bridge; remove committed duplicate assets from `plinth-skills-generator`.
5. Split tests and add propagation coverage.
6. Update skill XML copy-path guidance, skill Gherkin, docs.
7. Run full verification and `openspec validate --all`.

## ADR Candidates

| Topic | Recommendation |
|-------|----------------|
| Generator module extraction pattern | One directed Maven module per embeddable bundle (commands completed, agents now) |
| Build bridge vs runtime lookup | Always bridge at `generate-resources`; agents consume generated skills only |
| Cross-module XML helper sharing | Reuse `InventoryXmlLoader` from `plinth-commands-generator`; no shared-library module |
| Agent manifest introduction | Add `agents.xml` in the same change to match commands parity model |

## Resolved Design Questions

- **Artifact id**: `plinth-agents-generator`
- **Agent asset path in new module**: `src/main/resources/agents/`
- **Staging target in consumer**: generated `skill-references/assets/agents/`
- **`InventoryXmlLoader`**: reuse from `plinth-commands-generator` via dependency
- **Inventory template**: generate in Step 2 of the same change when parity tests are already green
- **`200-agents-md`**: remains in `plinth-skills-generator`

## Open Questions

| Question | Recommendation | Status |
|----------|----------------|--------|
| Step 2: auto-generate `java-agents-inventory-template.md` from `agents.xml` in the same PR? | Yes, when Step 1 parity tests are green — same approach as commands Step 2 | Resolved pending Step 1 |
| Should `plinth-skills-generator` keep a direct `plinth-commands-generator` dependency after adding `plinth-agents-generator`? | Yes — `CommandIndexes`, command bridge tests, and command skills still need it | Resolved |
| Migrate `002`/`005` to asset-link output later? | Track as future enhancement only if embed-first model becomes a maintenance burden | Deferred |

## Approval Checkpoint

Confirm this refined direction before implementation:

1. `plinth-agents-generator` with `agents.xml`, one-way dependency on `plinth-commands-generator`.
2. Sibling-module bridge into `target/generated-resources/` preserving embed-first `002`/`005` output.
3. Agent-specific propagation tests (embedded bodies) and XML-based installer parity (not `skills.xml` resources).
4. Step 2 inventory template hardening in the same change when Step 1 is green.

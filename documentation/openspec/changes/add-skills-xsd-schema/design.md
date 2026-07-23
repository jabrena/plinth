## Context

Issue [#991](https://github.com/jabrena/plinth/issues/991) and its Functional Specification comment establish:

- All 125 XML files under `plinth-skills-generator/src/main/resources/skill-indexes/` currently reference the remote PML 0.8.0 schema:
  ```xml
  <prompt xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd"
        id="301-frameworks-spring-boot-core">
  ```
  (verified against `plinth-skills-generator/src/main/resources/skill-indexes/301-skill.xml`).
- `SkillsGenerator.createXIncludeDomSource` builds an XInclude-aware, namespace-aware `DocumentBuilder` and parses `skill-indexes/*.xml` directly into a DOM for XSLT transformation (`skill-index-to-markdown.xsl`) — there is no `Schema`/`Validator` step in this path today.
- `RemoteSchemaValidationTest` (`plinth-skills-generator/src/test/java/info/jab/pml/RemoteSchemaValidationTest.java`) validates `SkillReferences.xmlFilenames()` against the remote `pml.xsd` — this is the `skill-references/` artifact type, not `skill-indexes/`. No automated test currently validates any `skill-indexes/*.xml` file against any schema.
- `SkillIndexes` (`plinth-skills-generator/src/main/java/info/jab/pml/SkillIndexes.java`) loads `skills.xml`, resolves each `<skill>` entry's numeric id, and detects whether a `skill-indexes/{numericId}-skill.xml` (XML, `useXml = true`) or `.md` source exists. It does not itself validate structure.
- The Functional Specification confirms: `skills.xsd` will be a complete, unchanged copy of PML 0.8.0 `pml.xsd`; it is stored beside `skills.xml`; only `skill-indexes/` XML uses it; validation must exist at XML-declaration/`xmllint`, generator-runtime, Maven-test, and CI layers; migration is atomic within this issue; generated output must stay byte-for-byte identical; and ADR-008 records a schema-per-artifact policy (authored within this change per the confirmed acceptance criteria — see "ADR-008 scope and gating" below).
- Sibling precedent exists for artifact-scoped schemas: archived changes `2026-07-15-design-pml-agents-schema` (`agents.xsd`) and `2026-07-17-design-pml-commands-schema` (`commands.xsd`) each authored a **new, specialized** schema for their artifact. This change is different in kind: `skills.xsd` is explicitly an **unchanged copy** of an existing published schema, not a new design. The precedent changes are used here only for documentation-format and validation-layering conventions, not for schema-content decisions.

## Goals / Non-Goals

**Goals:**

- Ship a local `skills.xsd` (unchanged PML 0.8.0 `pml.xsd` copy) at `plinth-skills-generator/src/main/resources/skills.xsd`, colocated with `skills.xml`.
- Migrate all 125 `skill-indexes/*.xml` files to reference `skills.xsd` instead of the remote schema.
- Add a schema-validation step to the `skill-indexes/` generator path (`SkillsGenerator`) so invalid XML fails generation with a meaningful diagnostic, without requiring runtime access to the remote PML schema.
- Add Maven test coverage for the complete `skill-indexes/` inventory plus representative valid/invalid fixtures, running in CI.
- Document the mapping between `skills.xsd` XML elements and the `SKILL.md` frontmatter/content contract.
- Verify generated `SKILL.md` output remains byte-for-byte identical before and after migration.
- Author `ADR-008` (schema-per-artifact policy) as part of this change's completion, and add its entry to `documentation/adr/README.md`, per the acceptance criteria confirmed on issue #991 (posted 2026-07-23). Record the future `ADR-001` update as an explicit, non-gating follow-up task.

**Non-Goals:**

- Do not specialize or tighten `skills.xsd` beyond an unchanged PML 0.8.0 `pml.xsd` copy (deferred; the Functional Specification explicitly defers stricter `SKILL.md`-specific field constraints).
- Do not touch `skill-references/*.xml`, its schema reference, or `RemoteSchemaValidationTest`'s existing scope.
- Do not change `commands.xml`/`agents.xml` or their generators.
- Do not change generated `SKILL.md` content, frontmatter values, or the skill-references transformation path.
- Do not update ADR-001/README in this change (explicit, non-gating follow-up, tracked in `tasks.md`). ADR-008 authoring, by contrast, is in scope for this change (see Goals).
- Do not refresh the public `skills/` release output as part of this change (only through the existing, separate `release` profile decision).
- Do not add Schematron or `@kind`-style profile validation; this change is XSD-only, matching the agents/commands precedent's validation-layering choice.

## Change Boundary Assessment

This is one atomic, reviewable change. The schema baseline, the migration of all 125 skill-index references, the generator runtime-validation step, the Maven/CI test coverage, and ADR-008 authoring share one module/change (`plinth-skills-generator`), one artifact type (`skill-indexes/`), one compatibility guarantee (byte-for-byte `SKILL.md` output), and one rollback boundary (revert the schema reference and the validation step together). Splitting by technical layer (e.g., "schema only" vs. "migration only" vs. "runtime validation only") would produce non-independently-reviewable slices with no standalone value, which the create-spec workflow explicitly disallows. Only the ADR-001 update is excluded from this change's completion gate — per the issue, it is follow-up documentation work performed during/after implementation — but it is captured in `tasks.md` so it is not lost. ADR-008 authoring is part of this change's completion gate (see "ADR-008 scope and gating" below).

## Decisions

### Schema content: unchanged PML 0.8.0 copy, not a new design

`skills.xsd` SHALL be a complete, byte-for-byte copy of the PML 0.8.0 `pml.xsd` published at `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`. This OpenSpec change does not perform or stage that copy — planning artifacts do not fabricate a substitute for an externally authoritative file. Implementation MUST:

1. Fetch/copy the actual PML 0.8.0 `pml.xsd` content into `plinth-skills-generator/src/main/resources/skills.xsd` without modification.
2. Diff the local copy against the remote source to confirm byte-for-byte equality (e.g., `curl` + `diff`, or an equivalent checksum comparison) and record that confirmation.

Alternative considered: specialize `skills.xsd` now (tighter required/optional `SKILL.md` fields, similar to how `agents.xsd`/`commands.xsd` were newly authored). Rejected for this change — the Functional Specification explicitly defers this ("Deferred question: Defining stricter required and optional fields... is intentionally deferred. The initial `skills.xsd` baseline is an unchanged PML 0.8.0 copy."). A future change may specialize `skills.xsd` once the unchanged baseline is adopted and validated.

### Schema location and scope

| Schema | Location | Scope |
|---|---|---|
| `skills.xsd` | `plinth-skills-generator/src/main/resources/skills.xsd` (beside `skills.xml`) | Only `skill-indexes/*.xml` (all 125 files) |
| `pml.xsd` (remote, unchanged reference) | `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd` | Remains the reference for `skill-references/*.xml` (unaffected by this change) |

Rationale: the Functional Specification's desired state is explicit that "Only XML files under `skill-indexes/` use `skills.xsd`" and that `skill-references/*.xml` "follows a separate transformation path" and "does not use `skills.xsd`." This mirrors the emerging schema-per-artifact policy (skill-index → `SKILL.md`, skill-reference → reference Markdown, command → `commands.xsd`, agent → `agents.xsd`) without merging artifact types under one schema file.

Migrated documents reference the local copy with a relative `xsi:noNamespaceSchemaLocation`, matching the existing attribute already present on every `skill-indexes/*.xml` file (only the location value changes, e.g. `xsi:noNamespaceSchemaLocation="../skills.xsd"` — the exact relative path is confirmed during implementation against the actual directory depth from `skill-indexes/` to `src/main/resources/`).

### Validation layers (all four, per the Functional Specification)

| Layer | Mechanism | Responsibility |
|---|---|---|
| 1 — XML tooling | XML declaration + `xmllint --noout --schema skills.xsd skill-indexes/*.xml` | Fast local/CI structural check without building the Java module |
| 2 — Generator runtime | New `Schema`/`Validator` step in `SkillsGenerator`'s `skill-indexes/` path, applied before (or alongside) `createXIncludeDomSource`, using `skills.xsd` from the classpath | Fails skill generation with a meaningful diagnostic when a skill-index document is structurally invalid, without depending on remote schema availability |
| 3 — Maven tests | A schema-validation test enumerating the complete `skill-indexes/` inventory (parallel to `RemoteSchemaValidationTest`, but scoped to `skill-indexes/` and the local `skills.xsd`) plus representative invalid fixtures | Objective, repeatable evidence that every migrated file validates, and that invalid input is rejected |
| 4 — CI | The existing `plinth-skills-generator` Maven verification (`./mvnw clean verify -pl plinth-skills-generator -am`) already runs in CI; no new CI job is required, only the new/extended test needs to execute there | Continuous enforcement |

Naming note: `RemoteSchemaValidationTest` keeps its current name and scope (`skill-references/` against the remote schema) — it is not repurposed for `skill-indexes/`, since its name and existing assertions are specific to remote-schema behavior. A new, separately named test class is the implementation-level recommendation (e.g. `SkillIndexSchemaValidationTest`), enumerating `skill-indexes/*.xml` via an inventory-derived filename list and validating against the local `skills.xsd` resource, with additional parameterized cases for a representative invalid fixture (see Examples below).

### Generator runtime validation placement

`SkillsGenerator.loadSkillSummaryFromXml` currently calls `createXIncludeDomSource(xmlStream, xmlResource)` directly into `TransformerFactory`/`Transformer`. The recommended placement adds schema validation against the classpath `skills.xsd` before (or as part of) that DOM construction, so that:

- A structurally invalid `skill-indexes/{numericId}-skill.xml` throws a clear, actionable exception identifying the offending file and the XSD violation, instead of silently producing malformed or partial `SKILL.md` output (or an unrelated XSLT error).
- The validation step does not require network access — `skills.xsd` is a classpath resource in `plinth-skills-generator`, consistent with the Functional Specification's reliability/reproducibility quality attribute ("Skill-index validation must work without runtime dependence on remote-schema availability").
- XInclude resolution and validation ordering are confirmed during implementation (validating before XInclude expansion validates the raw source; validating after expansion validates the fully resolved document — the Functional Specification does not mandate one order, so implementation selects whichever preserves current XInclude behavior and is documented in code comments/tests).

### ADR-008 scope and gating

`ADR-008` SHALL be authored as part of this change's completion (not a deferred follow-up), recording:

1. The schema-per-artifact policy: skill-index XML → `SKILL.md` (`skills.xsd`, this change), skill-reference XML → reference Markdown (unaffected), command XML → `commands.xsd`, agent XML → `agents.xsd`.
2. The scope this change implements: skill-index only; the other artifact schemas remain owned by their own changes.

It SHALL follow the existing MADR-style ADR template used by this repository (see `ADR-006-separate-local-skill-generation-from-release-publishing.md`), and SHALL be indexed with a new row in `documentation/adr/README.md`.

Rationale: the acceptance criteria confirmed on issue #991 (posted as a comment on 2026-07-23) include "Schema-per-artifact policy is documented" — `Given` `documentation/adr` is reviewed, `When` ADR-008 is reviewed, `Then` it records the policy — as an observable scenario of the Feature. That supersedes this change's earlier deferral of ADR-008 to a non-gating follow-up. The ADR-001 update remains deferred: it is not mentioned in the confirmed acceptance criteria, and the issue's own sequencing instruction ("ADR-001 is a follow-up documentation task for this issue; do not describe the new schema architecture as implemented before the schema migration is complete") still applies to it — ADR-001 must preserve its original date, identifier, and link when updated later.

### Mapping: `skills.xsd` XML elements ↔ `SKILL.md` frontmatter/content

Based on the current `skill-index-to-markdown.xsl` transformation and the representative `301-skill.xml` source (both already validated informally against the shared PML 0.8.0 shape, since today's remote reference is the same schema this change copies locally):

| XML element/attribute | `SKILL.md` mapping |
|---|---|
| `prompt/@id` | Skill identity (used to resolve `skill-indexes/{numericId}-skill.xml` from `skills.xml`); combined with `skills.xml` to derive the generated skill directory name |
| `metadata/description` | YAML frontmatter `description:` (with the `SkillsGenerator.PROJECT_TAG` suffix appended by `appendProjectTagToDescription`) |
| `metadata/author`, `metadata/version`, `metadata/license` | YAML frontmatter `metadata:` block (author/version/license), or a `license:` line inserted by `appendProjectTagToDescription` when absent |
| `title` | `SKILL.md` H1 heading / title content |
| `goal` (CDATA) | `SKILL.md` body — primary narrative content ("What is covered", scope) |
| `constraints/constraints-description`, `constraints/constraint-list/constraint` | `SKILL.md` constraints section |
| `triggers/trigger-list/trigger` | Trigger phrases folded into the frontmatter `description:` (per existing skill-authoring convention) and/or a triggers section, per current XSLT behavior |
| `steps/step` (`step-title`, `step-content`) | `SKILL.md` numbered workflow steps section |
| (generator-appended, not XML) | `## Reference` section appended by `SkillsGenerator.appendReferencesSection` from the `skills.xml` `reference-list`, not from `skills.xsd` |

This mapping documents the current, already-shipped transformation; introducing `skills.xsd` does not change this mapping — it only adds a structural validation gate ahead of the same XSLT that already produces this output. Any future specialization of `skills.xsd` (a non-goal here) would need to keep this mapping table current.

### Examples (representative, illustrative)

Because `skills.xsd` is defined as an unchanged copy of an external file this OpenSpec change does not fetch, this change does not stage a copy of `pml.xsd` under `examples/`. Instead, `examples/xml/` contains two illustrative, hand-authored samples that mirror the shape already present in `301-skill.xml`:

- `examples/xml/valid-skill-index-example.xml` — a representative valid skill-index document referencing `skills.xsd`.
- `examples/xml/invalid-skill-index-example.xml` — a representative invalid skill-index document (missing `metadata/description` and `title`) intended to fail validation.

These illustrate the acceptance-criteria intent ("Given a representative valid/invalid skill definition, when it is validated against the proposed schema, then validation succeeds/fails with a meaningful validation error"). Because the exact required/optional cardinalities come from the copied PML 0.8.0 `pml.xsd` (not authored in this change), implementation MUST re-run `xmllint --noout --schema skills.xsd` against both examples once the real `skills.xsd` is in place and record the actual diagnostic text for the invalid case — the illustrative examples here are a design aid, not a substitute for that confirmation.

## Alternative Analysis

| Approach | Pros | Cons | Decision |
|---|---|---|---|
| A. Unchanged local copy of PML 0.8.0 `pml.xsd`, scoped to `skill-indexes/` only | Matches the issue's confirmed decisions; smallest, safest migration; no behavior change to required/optional structure | Does not yet tighten `SKILL.md`-specific structural rules | **Accepted** |
| B. Specialize `skills.xsd` now (like `agents.xsd`/`commands.xsd`) | Stronger, `SKILL.md`-specific structural guarantees | Contradicts the issue's explicit deferral; expands scope beyond "one schema, one module, one migration"; larger review surface | Rejected for this change |
| C. Keep the remote schema reference but add a local runtime/test validation layer only | Smaller diff (no `skill-indexes/` migration) | Does not meet the issue's reliability/reproducibility goal (still depends on remote-schema availability); leaves 125 files pointing outside the module | Rejected |
| D. Merge `skill-indexes/` and `skill-references/` under one shared local schema | Fewer schema files | Contradicts the confirmed schema-per-artifact policy direction and the issue's explicit scope boundary | Rejected |
| E. Repurpose `RemoteSchemaValidationTest` to also cover `skill-indexes/` | Fewer test classes | Conflates "remote schema" test intent with a new "local schema" concern; existing test name/assertions are remote-schema-specific | Rejected — add a separately named local-schema test instead |

## Component Boundaries

| Component | Owns | Consumes |
|---|---|---|
| `plinth-skills-generator/src/main/resources/skills.xsd` | Local, unchanged PML 0.8.0 schema copy | Colocated `skills.xml` |
| `plinth-skills-generator/src/main/resources/skill-indexes/*.xml` (125 files) | Migrated schema reference to `skills.xsd` | `skills.xsd` |
| `SkillsGenerator` | New schema-validation step in the `skill-indexes/` path | `skills.xsd` (classpath), existing `skill-index-to-markdown.xsl` |
| `SkillIndexes` | Inventory resolution (unchanged behavior) | `skills.xml`, `skill-indexes/` |
| New local-schema test (e.g. `SkillIndexSchemaValidationTest`) | Complete-inventory + invalid-fixture coverage | `skills.xsd`, `skill-indexes/*.xml` |
| `RemoteSchemaValidationTest` | Unchanged: `skill-references/` against remote `pml.xsd` | Remote `pml.xsd` (unaffected) |
| `ADR-008` (authored within this change) | Schema-per-artifact policy record | This change's outcome (gating) |
| Future `ADR-001` update | Reflects current XML-to-Agent-Skills architecture | This change's outcome (follow-up, not gated here) |

## Data Flow

```
skills.xml
  → SkillIndexes (inventory + useXml detection, unchanged)
  → skill-indexes/<id>-skill.xml (now referencing local skills.xsd)
  → schema validation against skills.xsd            [NEW]
  → XInclude-aware XML parsing (createXIncludeDomSource, unchanged)
  → skill-index-to-markdown.xsl (unchanged)
  → target/skills/<skill-id>/SKILL.md (byte-for-byte unchanged content)
  → .agents/skills/ or skills/ (release profile, unaffected by this change)
```

Reference XML (`skill-references/*.xml`) follows its existing, separate path and remains on the remote `pml.xsd` reference; it is not touched by this change.

## Failure Handling

| Failure | Expected behavior |
|---|---|
| A `skill-indexes/*.xml` file is missing required PML 0.8.0 elements | XSD validation fails at all four layers (`xmllint`, generator runtime, Maven test, CI) with a diagnostic identifying the file and violated constraint |
| Local `skills.xsd` accidentally drifts from the remote PML 0.8.0 source during the copy | Implementation-time diff/checksum step (see Decisions) catches this before the change is promoted |
| Generated `SKILL.md` differs after migration | Byte-for-byte comparison task fails; migration is not complete until output parity is restored |
| A skill-index file is missed during the atomic migration (still points remotely) | New local-schema test enumerates the complete 125-file inventory and fails if any file does not resolve/validate via `skills.xsd` |
| `skill-references/*.xml` accidentally migrated to `skills.xsd` | Out of scope for this change; `RemoteSchemaValidationTest`'s continued passing against the remote schema is the regression signal |

## Risks / Trade-offs

- [Risk] Some of the 125 files may not currently be fully compliant with PML 0.8.0 even though they reference it remotely (remote validation was never automated for `skill-indexes/`). → Mitigate by running the full-inventory validation early in implementation, before wiring runtime enforcement, and treat any failures as pre-existing defects to fix as part of this atomic migration.
- [Risk] Adding a validation step to `SkillsGenerator` could change build behavior for any currently-invalid file. → Accept; this is the intended, issue-requested outcome ("Correctness" quality attribute), and byte-for-byte comparison is scoped to *valid* files only — a newly-caught invalid file is a legitimate build failure, not a compatibility regression.
- [Risk] Relative `xsi:noNamespaceSchemaLocation` paths must resolve consistently for `xmllint` (filesystem-relative), the Java classpath (resource-relative), Maven tests, and CI. → Document the confirmed resolution approach in code/test comments during implementation; this design defers the exact relative path string to implementation since it depends on final directory verification.
- [Risk] Confusion between this change and the sibling `agents.xsd`/`commands.xsd` schema designs, which specialize their schemas. → This design explicitly states `skills.xsd` is an unchanged copy, not a new design, to avoid scope creep into specialization.

## Validation Strategy

Structural (XSD):

- `xmllint --noout --schema plinth-skills-generator/src/main/resources/skills.xsd plinth-skills-generator/src/main/resources/skill-indexes/*.xml` passes for all 125 migrated files.
- A representative invalid fixture fails with a meaningful diagnostic (see `examples/xml/invalid-skill-index-example.xml`, to be re-verified against the real `skills.xsd` during implementation).

Runtime/Maven/CI:

- New/extended Maven test enumerates the complete `skill-indexes/` inventory and asserts successful validation, plus asserts failure for invalid fixtures.
- `./mvnw clean verify -pl plinth-skills-generator -am` passes with the new validation step wired into `SkillsGenerator`.
- CI executes the same Maven verification (no new CI job needed).

Compatibility:

- Generate all skills before and after the migration; compare `target/skills/**/SKILL.md` output byte-for-byte; any diff blocks promotion.

OpenSpec:

- `openspec validate --all` passes for this change.

## ADR Candidates

| Topic | Recommendation | Status |
|---|---|---|
| ADR-008: one XML Schema per generated artifact (skill-index, skill-reference, command, agent) | Author as part of this change's implementation, per the acceptance criteria confirmed on issue #991 (posted 2026-07-23) | **Gating** (tracked in `tasks.md` section 9) |
| ADR-001 update: reflect current XSD-validated XML sources, `plinth-skills-generator`, `skills.xsd`, and generated Agent Skills, while preserving the original date/identifier/link | Update after this change's implementation is complete | **Deferred follow-up** (tracked in `tasks.md` section 10, not gated on this change) |
| Local unchanged-copy schema baseline as the correct first step (vs. specializing now) | Accepted for this change | **Resolved** |

## Compatibility Review (`056`)

| Surface | Changes? | Mitigation |
|---|---|---|
| Generated `SKILL.md` content | No intentional change | Byte-for-byte comparison before/after migration |
| `skill-indexes/*.xml` schema reference | Yes — remote → local `skills.xsd` | Scoped exclusively to `skill-indexes/`; `skill-references/` unaffected |
| `SkillsGenerator` build behavior | Yes — adds a validation step that can now fail on structurally invalid input | Intended outcome; run full-inventory validation early to surface any pre-existing issues before enforcement lands |
| Public `skills/` release output | Not auto-refreshed | Existing `-P release` profile remains the explicit trigger |
| `skill-references/*.xml` and `RemoteSchemaValidationTest` | No change | Explicitly out of scope |
| ADR-001 / `documentation/adr/README.md` | Not changed by this OpenSpec change's implementation | Tracked as a follow-up task, per the issue |

## Resolved Design Questions

| Question | Decision | Status |
|---|---|---|
| Schema content? | Unchanged PML 0.8.0 `pml.xsd` copy | **Resolved** |
| Schema location? | `plinth-skills-generator/src/main/resources/skills.xsd` | **Resolved** |
| Schema scope? | `skill-indexes/*.xml` only | **Resolved** |
| Repurpose `RemoteSchemaValidationTest`? | No — add a separately named local-schema test | **Resolved** |
| Validation-layer count? | Four: `xmllint`, generator runtime, Maven tests, CI | **Resolved** |
| ADR-008 timing? | Authored as part of this change's completion (`tasks.md` section 9), per the acceptance criteria confirmed on issue #991 (posted 2026-07-23) | **Resolved** |
| ADR-001 update timing? | Follow-up, after this change's implementation; tracked in `tasks.md` section 10, not gating this change | **Resolved** |

## Open Questions (carried from the Functional Specification's "Remaining assumptions")

| Priority | Open question | Impact if false | Confidence |
|---|---|---:|---:|
| 1 | Are all 125 skill indexes valid against an unchanged local copy of PML 0.8.0? | High | Medium |
| 2 | Will changing schema resolution and adding validation leave generated output unchanged? | High | High |
| 3 | Can the same local schema resource be resolved consistently by XML tools, runtime code, Maven tests, and CI? | High | Medium |
| 4 | Can an artifact-specific schema ownership model apply consistently to skill indexes, skill references, commands, and agents? | Medium | Medium |

These are implementation-time verification items, not open architecture decisions blocking this change's approval — the Functional Specification already resolved the architectural direction (unchanged copy, `skill-indexes/`-only scope, four validation layers, atomic migration, byte-for-byte compatibility). The maintainer should confirm this reading before implementation starts.

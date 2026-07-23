## Why

GitHub issue [#991](https://github.com/jabrena/plinth/issues/991) requests a local, artifact-scoped XML schema for the `SKILL.md` contract. Today every XML file under `plinth-skills-generator/src/main/resources/skill-indexes/` (125 files) references the remote, shared `pml.xsd` at `https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`, and `SkillsGenerator.createXIncludeDomSource` parses that XML with an XInclude-aware `DocumentBuilder` without any schema validation step before XSLT transformation. `RemoteSchemaValidationTest` only enumerates `SkillReferences.xmlFilenames()` (the `skill-references/` artifact type), so no automated layer validates the `skill-indexes/` source set at all. The module therefore cannot provide deterministic, artifact-scoped validation of the XML that generates `SKILL.md`, and skill-index validity depends on runtime access to a remote schema resource.

## What Changes

- Add a local `skills.xsd` to `plinth-skills-generator/src/main/resources/`, colocated with `skills.xml`, containing a complete, unchanged copy of PML 0.8.0 `pml.xsd` (the byte-for-byte copy step happens during implementation, not in this planning artifact).
- Replace the remote `xsi:noNamespaceSchemaLocation` reference in every one of the 125 XML files under `skill-indexes/` with a reference to the local `skills.xsd`. Files under `skill-references/` are explicitly out of scope and keep their current remote reference.
- Add a schema-validation step to the `skill-indexes/` parsing path in `SkillsGenerator` before XInclude parsing and XSLT transformation, so invalid skill-index XML fails generation with a meaningful diagnostic instead of reaching the transform silently.
- Add Maven test coverage that validates the complete `skill-indexes/` inventory (all 125 files) plus representative valid and invalid fixtures against `skills.xsd`, and confirm this coverage runs in CI.
- Document the mapping between `skills.xsd` XML elements and the `SKILL.md` frontmatter/content contract.
- Verify that generated `SKILL.md` output is byte-for-byte identical before and after the schema-reference migration.
- Record, as explicit follow-up tasks (not gated on this change's completion), the future creation of `ADR-008` (schema-per-artifact policy) and the future update of `ADR-001` and its `documentation/adr/README.md` index row, per the issue's own sequencing ("ADR-001 is a follow-up documentation task for this issue; do not describe the new schema architecture as implemented before the schema migration is complete").

## Capabilities

### New Capabilities

- `pml-skills-schema`: Defines the local `skills.xsd` baseline, its scope (`skill-indexes/` only), its relationship to the remote PML 0.8.0 `pml.xsd` and to the other artifact-specific schemas (`agents.xsd`, `commands.xsd`), the SKILL.md mapping, and representative examples.
- `skills-generator-module`: Defines the migration of all 125 skill-index XML references, the runtime validation layer in `SkillsGenerator`, the Maven/CI test coverage, and the byte-for-byte compatibility guarantee for generated `SKILL.md` output.

### Modified Capabilities

None. This change does not alter existing OpenSpec capability specs for the skills-generation behavior contract; it adds structural validation and a migration on top of the existing generation pipeline.

## Impact

- **Schema:** new `plinth-skills-generator/src/main/resources/skills.xsd`.
- **Migrated sources:** all 125 files under `plinth-skills-generator/src/main/resources/skill-indexes/`.
- **Generator runtime:** `SkillsGenerator` (`createXIncludeDomSource` / `loadSkillSummaryFromXml` path) gains a schema-validation step scoped to `skill-indexes/` XML only.
- **Tests:** a new schema-validation test (or an extension of the existing `RemoteSchemaValidationTest` family) covering the complete `skill-indexes/` inventory plus invalid fixtures; `RemoteSchemaValidationTest` itself is unchanged in scope (it continues to cover `skill-references/`).
- **CI:** the new/extended test runs in the existing `plinth-skills-generator` verification path; no new CI job is required beyond that.
- **Compatibility:** generated `target/skills/<skill-id>/SKILL.md`, `.agents/skills/`, and `skills/` (release profile) content must remain byte-for-byte identical; only the XML schema reference and validation layer change, not generated content.
- **Not in scope:** `skill-references/*.xml`, `commands.xml`/`agents.xml` and their generators, and any change to `SKILL.md` content, frontmatter values, or the skill-references transformation path.
- **Deferred, tracked separately:** `ADR-008` (schema-per-artifact policy) creation and the `ADR-001` / `documentation/adr/README.md` update. Both are recorded in `tasks.md` as explicit follow-up items so they are not lost, but per the issue they are completed during/after implementation of this change, not as part of approving this OpenSpec change.

## Source Artifacts and Derivation

| Source | Authority | Derivation |
|---|---|---|
| [Issue #991](https://github.com/jabrena/plinth/issues/991) | Problem, acceptance criteria, implementation ideas, milestone | Issue to OpenSpec |
| Issue #991 Functional Specification comment (posted by jabrena, 2026-07-23) | Root cause analysis, confirmed decisions, remaining assumptions, validation plan, quality attributes | Comment to OpenSpec |
| `plinth-skills-generator/src/main/resources/skills.xml` | Skill inventory shape | Grounds schema/mapping design |
| `plinth-skills-generator/src/main/resources/skill-indexes/301-skill.xml` (representative) | Current `<prompt>` XML shape referencing remote `pml.xsd` 0.8.0 | Grounds mapping and example design |
| `plinth-skills-generator/src/main/java/info/jab/pml/SkillsGenerator.java` | Current XInclude/XSLT parsing path, no schema validation | Grounds runtime-validation design |
| `plinth-skills-generator/src/main/java/info/jab/pml/SkillIndexes.java` | Inventory loading and `skill-indexes/` resolution | Grounds migration/test-coverage design |
| `plinth-skills-generator/src/test/java/info/jab/pml/RemoteSchemaValidationTest.java` | Confirms existing coverage is `skill-references/` only | Grounds the test-coverage gap this change closes |
| [ADR-001](../../adr/ADR-001-generate-cursor-rules-from-xml-files.md) and `documentation/adr/README.md` | XSD → Markdown generation precedent; future update target | Follow-up, not implemented by this change |
| Archived `2026-07-15-design-pml-agents-schema` / `2026-07-17-design-pml-commands-schema` | Sibling artifact-scoped schema precedent (`agents.xsd`, `commands.xsd`) | Structural and documentation-format precedent only; this change copies PML 0.8.0 unchanged rather than authoring a new specialized schema |

**Derivation direction:** Issue #991 (description + Functional Specification comment) → this OpenSpec change (`add-skills-xsd-schema`) → local `skills.xsd` + migrated `skill-indexes/` sources + `SkillsGenerator` runtime validation + Maven/CI test coverage in `plinth-skills-generator`. This is one-way derivation; this change does not modify the source issue or its comments, and does not implement ADR-008 or the ADR-001 update.

## Design Decisions

| Question | Decision | Status |
|---|---|---|
| Is this one reviewable change, or should it split? | One change. Schema baseline, migration, runtime validation, and test/CI coverage share one module (`plinth-skills-generator`), one artifact type (`skill-indexes/`), one rollback boundary (revert the schema reference + validation step), and one compatibility guarantee (byte-for-byte output). ADR-008 creation and the ADR-001 update are tracked as follow-up tasks outside this change's completion gate, per the issue's own sequencing — they are not split into a separate OpenSpec change because they have no independent design content beyond "record what this change did." | **Resolved** |
| Schema content | Complete, unchanged copy of PML 0.8.0 `pml.xsd`; no specialized `SKILL.md`-specific tightening in this change (explicitly deferred per the Functional Specification) | **Resolved** |
| Schema location | `plinth-skills-generator/src/main/resources/skills.xsd`, beside `skills.xml` | **Resolved** |
| Schema scope | Only `skill-indexes/*.xml`; `skill-references/*.xml` keeps the remote PML reference and is unaffected | **Resolved** |
| Are all 125 skill indexes valid against an unchanged PML 0.8.0 copy? | Open — must be confirmed with `xmllint` during implementation (see Open Questions) | **Open** |
| Will schema-reference change plus added validation alter generated output? | Open — must be confirmed with a byte-for-byte before/after comparison during implementation | **Open** |
| Can the same local schema resource be resolved consistently by XML tools, runtime code, Maven tests, and CI? | Open — resolution mechanism (classpath vs. relative path vs. `xsi:noNamespaceSchemaLocation`) is a design-level recommendation in `design.md`, to be confirmed during implementation | **Open** |
| Can the artifact-specific schema ownership model extend consistently to skill indexes, skill references, commands, and agents? | Open — this change documents the policy intent; ADR-008 is the follow-up record, not gated on this change | **Open** |

See [`design.md`](design.md) for the schema/runtime/test architecture and mapping details.

## Handoff

After OpenSpec docs and `openspec validate --all` pass, `@robot-tech-lead` can coordinate implementation from `tasks.md`: schema copy and byte-verification, atomic migration of all 125 `skill-indexes/` references, `SkillsGenerator` runtime validation, Maven/CI test coverage, and the byte-for-byte compatibility check — followed, as separate follow-up work, by ADR-008 authoring and the ADR-001/README update.

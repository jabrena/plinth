schema: spec-driven
created: 2026-07-23
---
## 1. Schema baseline (skills.xsd)

- [ ] 1.1 Copy the complete, unchanged content of PML 0.8.0 `pml.xsd` (`https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`) into `plinth-skills-generator/src/main/resources/skills.xsd`.
- [ ] 1.2 Confirm byte-for-byte equality between the local `skills.xsd` and the remote PML 0.8.0 source (e.g. `curl`/`diff` or checksum comparison) and record the confirmation.
- [ ] 1.3 Validate `skills.xsd` well-formedness with `xmllint --noout plinth-skills-generator/src/main/resources/skills.xsd`.

## 2. Migrate skill-index schema references (atomic)

- [ ] 2.1 Update the `xsi:noNamespaceSchemaLocation` attribute in all 125 XML files under `plinth-skills-generator/src/main/resources/skill-indexes/` from the remote PML 0.8.0 `pml.xsd` to the local `skills.xsd`.
- [ ] 2.2 Confirm no file under `skill-indexes/` still references the remote schema location after migration.
- [ ] 2.3 Confirm files under `plinth-skills-generator/src/main/resources/skill-references/` are unchanged and still reference the remote PML 0.8.0 `pml.xsd`.
- [ ] 2.4 Run `xmllint --noout --schema plinth-skills-generator/src/main/resources/skills.xsd plinth-skills-generator/src/main/resources/skill-indexes/*.xml` across the complete migrated inventory and fix any pre-existing structural issues surfaced by local validation.

## 3. Generator runtime validation

- [ ] 3.1 Add a schema-validation step to the `skill-indexes/` parsing path in `SkillsGenerator` (`loadSkillSummaryFromXml` / `createXIncludeDomSource`), validating against the classpath `skills.xsd` before or as part of DOM construction.
- [ ] 3.2 Confirm the validation step produces a diagnostic identifying the offending file and violated constraint on failure, and does not require network access to the remote PML schema. Enforce this as an observable property, not only a design intent, by setting `XMLConstants.ACCESS_EXTERNAL_DTD` and `ACCESS_EXTERNAL_SCHEMA` to `""` (deny external access) on the `SchemaFactory` used for `skills.xsd` — matching `CommandSchemaTest`'s existing pattern in `plinth-commands-generator`, not `RemoteSchemaValidationTest`'s `"all"` setting (which intentionally permits the network fetch its remote-schema test needs).
- [ ] 3.3 Confirm `skill-references/*.xml` generation is unaffected by this change (remains on its existing remote-schema behavior).

## 4. Maven and CI test coverage

- [ ] 4.1 Add a new, separately named local-schema test (e.g. `SkillIndexSchemaValidationTest`, structured after the already-shipped `CommandSchemaTest` in `plinth-commands-generator`: enumerate via `SkillIndexes`, load `skills.xsd` once via classpath with `ACCESS_EXTERNAL_DTD`/`ACCESS_EXTERNAL_SCHEMA` set to `""`) that enumerates and validates all 125 `skill-indexes/*.xml` files against `skills.xsd`, without changing `RemoteSchemaValidationTest`'s existing scope (`skill-references/` against the remote schema).
- [ ] 4.2 Add at least one representative invalid fixture (see `examples/xml/invalid-skill-index-example.xml`) and assert that validation fails with a meaningful diagnostic.
- [ ] 4.3 Confirm the new test executes as part of the existing CI pipeline that runs `plinth-skills-generator` verification (no new CI job required).

## 5. Compatibility verification

- [ ] 5.1 Generate all skills via `SkillsGenerator` before the migration and preserve the output for comparison.
- [ ] 5.2 Generate all skills again after the migration, runtime validation, and test changes are applied.
- [ ] 5.3 Compare both generations byte-for-byte for every `SKILL.md` output and resolve any difference before promoting this change.

## 6. Mapping and schema-per-artifact documentation

- [ ] 6.1 Confirm the `skills.xsd` element ↔ `SKILL.md` frontmatter/content mapping table in `design.md` matches the actual copied PML 0.8.0 structure and the current `skill-index-to-markdown.xsl` behavior.
- [ ] 6.2 Confirm the schema-scope boundary statement (only `skill-indexes/`; `skill-references/`, `commands.xsd`, `agents.xsd` unaffected) remains accurate after migration.

## 7. Examples

- [ ] 7.1 Re-validate `examples/xml/valid-skill-index-example.xml` against the real `skills.xsd` with `xmllint --noout --schema` once it exists, and correct the example if it does not pass.
- [ ] 7.2 Re-validate `examples/xml/invalid-skill-index-example.xml` (omits `<goal>`, the only required child element of `<prompt>` per the fetched PML 0.8.0 `pml.xsd` — confirmed during design review that `metadata/description` and `title` are optional and would not fail alone) against the real `skills.xsd`, record the actual failure diagnostic, and correct the example if it unexpectedly passes.

## 8. Integrated validation

- [ ] 8.1 Run `./mvnw clean verify -pl plinth-skills-generator -am` and resolve schema, runtime-validation, and test failures.
- [ ] 8.2 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation promotion.

## 9. Schema-per-artifact policy documentation (ADR-008, gating)

- [ ] 9.1 Create `ADR-008` (MADR-style, per the existing `ADR-006` template) recording the one-XML-Schema-per-generated-artifact policy and the scope this change implements (skill-index only), including the colocated-schema `../<schema>.xsd` relative-path convention already followed by `agents.xsd` and `commands.xsd` as part of that policy.
- [ ] 9.2 Add `ADR-008`'s entry to `documentation/adr/README.md`.
- [ ] 9.3 Confirm `ADR-008` is authored and indexed before this change's implementation is considered complete — this is gating per the confirmed acceptance criteria for issue #991 (posted 2026-07-23), not a deferred follow-up.
- [ ] 9.4 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` against the new `documentation/adr/ADR-008-*.md` file and the updated `documentation/adr/README.md`, per this repository's Markdown-only-change validation convention, and resolve any reported issues (including local link checks) before considering ADR-008 authoring complete.

## 10. Follow-up documentation (deferred — not required to close this change)

- [ ] 10.1 (Follow-up, after this change's implementation) Update `ADR-001-generate-cursor-rules-from-xml-files.md` and its entry in `documentation/adr/README.md` to describe the current XML-to-Agent-Skills generation architecture (XSD-validated XML sources, `plinth-skills-generator`, `skills.xsd`, generated Agent Skills), preserving the original ADR date, identifier, and link.

schema: spec-driven
created: 2026-07-23
---
## 1. Schema baseline (skills.xsd)

- [x] 1.1 Copy the complete, unchanged content of PML 0.8.0 `pml.xsd` (`https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`) into `plinth-skills-generator/src/main/resources/skills.xsd`.
- [x] 1.2 Confirm byte-for-byte equality between the local `skills.xsd` and the remote PML 0.8.0 source (e.g. `curl`/`diff` or checksum comparison) and record the confirmation. SHA-256 `550c5cf2fad773baf5fd190b51a8c9f7028ab69131b01c8e449646b575d9f6c9` confirmed identical for the local file and a fresh `curl` of the remote source (re-verified independently during delivery review).
- [x] 1.3 Validate `skills.xsd` well-formedness with `xmllint --noout plinth-skills-generator/src/main/resources/skills.xsd`.

## 2. Migrate skill-index schema references (atomic)

- [x] 2.1 Update the `xsi:noNamespaceSchemaLocation` attribute in all 125 XML files under `plinth-skills-generator/src/main/resources/skill-indexes/` from the remote PML 0.8.0 `pml.xsd` to the local `skills.xsd`.
- [x] 2.2 Confirm no file under `skill-indexes/` still references the remote schema location after migration. Verified independently: 0 remaining remote references across all 125 files.
- [x] 2.3 Confirm files under `plinth-skills-generator/src/main/resources/skill-references/` are unchanged and still reference the remote PML 0.8.0 `pml.xsd`. Verified independently: 196 files still on the remote reference, 0 migrated.
- [x] 2.4 Run `xmllint --noout --schema plinth-skills-generator/src/main/resources/skills.xsd plinth-skills-generator/src/main/resources/skill-indexes/*.xml` across the complete migrated inventory and fix any pre-existing structural issues surfaced by local validation. All 125 files passed; no structural issues found.

## 3. Generator runtime validation

- [x] 3.1 Add a schema-validation step to the `skill-indexes/` parsing path in `SkillsGenerator` (`loadSkillSummaryFromXml` / `createXIncludeDomSource`), validating against the classpath `skills.xsd` before or as part of DOM construction.
- [x] 3.2 Confirm the validation step produces a diagnostic identifying the offending file and violated constraint on failure, and does not require network access to the remote PML schema. Enforce this as an observable property, not only a design intent, by setting `XMLConstants.ACCESS_EXTERNAL_DTD` and `ACCESS_EXTERNAL_SCHEMA` to `""` (deny external access) on the `SchemaFactory` used for `skills.xsd` — matching `CommandSchemaTest`'s existing pattern in `plinth-commands-generator`, not `RemoteSchemaValidationTest`'s `"all"` setting (which intentionally permits the network fetch its remote-schema test needs). Verified independently by reading the `SkillsGenerator.loadSkillsSchema()`/`validateAgainstSkillsSchema()` diff.
- [x] 3.3 Confirm `skill-references/*.xml` generation is unaffected by this change (remains on its existing remote-schema behavior). `RemoteSchemaValidationTest` confirmed unmodified (`git diff` empty) and passing.

## 4. Maven and CI test coverage

- [x] 4.1 Add a new, separately named local-schema test (e.g. `SkillIndexSchemaValidationTest`, structured after the already-shipped `CommandSchemaTest` in `plinth-commands-generator`: enumerate via `SkillIndexes`, load `skills.xsd` once via classpath with `ACCESS_EXTERNAL_DTD`/`ACCESS_EXTERNAL_SCHEMA` set to `""`) that enumerates and validates all 125 `skill-indexes/*.xml` files against `skills.xsd`, without changing `RemoteSchemaValidationTest`'s existing scope (`skill-references/` against the remote schema).
- [x] 4.2 Add at least one representative invalid fixture (see `examples/xml/invalid-skill-index-example.xml`) and assert that validation fails with a meaningful diagnostic. Surefire report confirms `SkillIndexSchemaValidationTest`: 126 tests (125 inventory + 1 invalid fixture), 0 failures.
- [x] 4.3 Confirm the new test executes as part of the existing CI pipeline that runs `plinth-skills-generator` verification (no new CI job required).

## 5. Compatibility verification

- [x] 5.1 Generate all skills via `SkillsGenerator` before the migration and preserve the output for comparison.
- [x] 5.2 Generate all skills again after the migration, runtime validation, and test changes are applied.
- [x] 5.3 Compare both generations byte-for-byte for every `SKILL.md` output and resolve any difference before promoting this change. `diff -rq` reported zero differences across all 125 generated `SKILL.md` files.

## 6. Mapping and schema-per-artifact documentation

- [x] 6.1 Confirm the `skills.xsd` element ↔ `SKILL.md` frontmatter/content mapping table in `design.md` matches the actual copied PML 0.8.0 structure and the current `skill-index-to-markdown.xsl` behavior. One mismatch found and corrected: `metadata/license` is emitted as a top-level `license:` scalar before `metadata:`, not nested inside the `metadata:` block; verified independently via `git diff design.md`.
- [x] 6.2 Confirm the schema-scope boundary statement (only `skill-indexes/`; `skill-references/`, `commands.xsd`, `agents.xsd` unaffected) remains accurate after migration.

## 7. Examples

- [x] 7.1 Re-validate `examples/xml/valid-skill-index-example.xml` against the real `skills.xsd` with `xmllint --noout --schema` once it exists, and correct the example if it does not pass. Passed as-is; no correction needed.
- [x] 7.2 Re-validate `examples/xml/invalid-skill-index-example.xml` (omits `<goal>`, the only required child element of `<prompt>` per the fetched PML 0.8.0 `pml.xsd` — confirmed during design review that `metadata/description` and `title` are optional and would not fail alone) against the real `skills.xsd`, record the actual failure diagnostic, and correct the example if it unexpectedly passes. Failed as designed; recorded diagnostic: `Element 'prompt': Missing child element(s). Expected is one of ( role, tone, context, goal ).`

## 8. Integrated validation

- [x] 8.1 Run `./mvnw clean verify -pl plinth-skills-generator -am` and resolve schema, runtime-validation, and test failures. Re-run independently during delivery review: `BUILD SUCCESS`.
- [x] 8.2 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation promotion. Re-run independently during delivery review: `Totals: 50 passed, 0 failed`.

## 9. Schema-per-artifact policy documentation (ADR-008, gating)

- [x] 9.1 Create `ADR-008` (MADR-style, per the existing `ADR-006` template) recording the one-XML-Schema-per-generated-artifact policy and the scope this change implements (skill-index only), including the colocated-schema `../<schema>.xsd` relative-path convention already followed by `agents.xsd` and `commands.xsd` as part of that policy.
- [x] 9.2 Add `ADR-008`'s entry to `documentation/adr/README.md`.
- [x] 9.3 Confirm `ADR-008` is authored and indexed before this change's implementation is considered complete — this is gating per the confirmed acceptance criteria for issue #991 (posted 2026-07-23), not a deferred follow-up.
- [x] 9.4 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` against the new `documentation/adr/ADR-008-*.md` file and the updated `documentation/adr/README.md`, per this repository's Markdown-only-change validation convention, and resolve any reported issues (including local link checks) before considering ADR-008 authoring complete. Note: the tool's default target directories (`.cursor/rules,skills,.cursor/agents`) do not include `documentation/adr/`; re-run independently with the correct scope, `jbang markdown-validator/.../MarkdownValidator.java . -d documentation/adr`, which reports `documentsFound=9 documentsValidated=9 errors=0` (includes ADR-008 and the updated README).

## 10. Follow-up documentation (deferred — not required to close this change)

- [ ] 10.1 (Follow-up, after this change's implementation) Update `ADR-001-generate-cursor-rules-from-xml-files.md` and its entry in `documentation/adr/README.md` to describe the current XML-to-Agent-Skills generation architecture (XSD-validated XML sources, `plinth-skills-generator`, `skills.xsd`, generated Agent Skills), preserving the original ADR date, identifier, and link.

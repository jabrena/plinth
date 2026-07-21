# Tasks

## 1. Update the skill workflow sources

- [x] 1.1 Update `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` Step 3 content to run `openspec new change <change-id>` for a new change before authoring artifacts, and to note that an existing change (detected via `openspec show <change-id>`) skips this step.
- [x] 1.2 Update `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` Step 4 content to remove the CLI-generated placeholder `README.md` once `proposal.md` is authored.
- [x] 1.3 Add a step constraint (or extend an existing one) in `042-planning-openspec.xml` capturing "MUST scaffold new changes with `openspec new change <change-id>`" and "MUST NOT re-run it for an existing change". Added matching safeguards-item as well.
- [x] 1.4 Update `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml` Step 3/Step 4 summaries and constraint list to match.
- [x] 1.5 Validate both edited XML files with `xmllint --noout`. Both passed; required escaping `<change-id>` as `&lt;change-id&gt;` in non-CDATA constraint/safeguard text elements.

## 2. Add acceptance coverage and documentation

- [x] 2.1 Add a scenario to `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature` covering: scaffolding a new change with `openspec new change <change-id>`, the resulting `.openspec.yaml`, and removal of the placeholder `README.md`; and a scenario covering that an existing change is not re-scaffolded. Both added as `@acceptance-test` scenarios, matching this repo's existing multi-scenario skill-test tagging convention (e.g. `059-design-atdd.feature`, `056-design-avoid-breaking-changes.feature`).
- [x] 2.2 Confirm `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` still has a correct `042-planning-openspec` entry. Confirmed correct, no change needed — it already executes the whole feature file, which now includes both new scenarios.
- [x] 2.3 Update the `042-planning-openspec` row in `documentation/guides/INVENTORY-SKILLS-JAVA.md` to note that new changes are scaffolded via `openspec new change <change-id>`.

## 3. Generate and verify

- [x] 3.1 Run `./mvnw clean install -pl plinth-skills-generator -am` and verify `.agents/skills/042-planning-openspec/SKILL.md` plus `.agents/skills/042-planning-openspec/references/042-planning-openspec.md` contain the new scaffolding guidance with no unresolved include markers or broken local paths. BUILD SUCCESS, 1130 tests passed; both generated files inspected and contain the new Step 3/4 guidance and safeguard, with `<change-id>` rendering correctly.
- [x] 3.2 Execute the `042-planning-openspec` acceptance prompt listed in `acceptance-tests-prompts-skills.md` against the generated skill output, or record a skipped prompt with reason. Manually exercised the new-change scaffolding scenario against `examples/openspec/god-analysis-api`: ran `openspec new change add-audit-logging --description "Add audit logging for payment status changes"`, confirmed it produced only `.openspec.yaml` and a placeholder `README.md` (matching the skill's new Step 3/4 guidance), then removed the scaffolded directory to reset the fixture. Confirmed the existing `add-god-analysis-api` change directory was left untouched, satisfying the skip-re-scaffold scenario.
- [x] 3.3 Run `./mvnw clean verify -pl plinth-skills-generator`. BUILD SUCCESS, 1130 tests passed, 0 failures.
- [x] 3.4 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links in the Java skill inventory. Passed: 400 documents validated, 0 errors.
- [x] 3.5 Run `openspec validate --all` from `documentation/` and resolve validation failures before implementation is promoted. Totals: 47 passed, 0 failed (47 items).
- [x] 3.6 Review `git diff` to confirm `.cursor/rules/`, `.agents/skills/`, public `skills/`, and `docs/` were not directly edited, and that issue #1054 remains a one-way source. Confirmed: only `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml`, `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml`, `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature`, and `documentation/guides/INVENTORY-SKILLS-JAVA.md` were edited; `.agents/skills/` was refreshed only through Maven, and issue #1054 was not modified.

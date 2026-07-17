## 1. Harness root and metrics

- [ ] 1.1 Create `plinth-benchmark/README.md` with layout, scenario table, metrics scorecard (efficiency, outcome quality, process labels), minimal v1 fields, scenario-specific extras, and out-of-scope note for JVM `/benchmark`.
- [ ] 1.2 Ensure the README uses only repository-relative paths for God Analysis API inputs under `examples/openspec/god-analysis-api/`.

## 2. Scenario 1 — README only

- [ ] 2.1 Create `plinth-benchmark/scenario1/README.md` documenting input, exclusions, and case id `case-1-readme-only`.
- [ ] 2.2 Create `plinth-benchmark/scenario1/specs/scenario1.md` with scenario requirements aligned to issue #1012 Scenario 1.
- [ ] 2.3 Create `plinth-benchmark/scenario1/gherkin/scenario1.feature` with exactly one `@acceptance-test` scenario for case `case-1-readme-only`.

## 3. Scenario 2 — all requirements notes

- [ ] 3.1 Create `plinth-benchmark/scenario2/README.md` documenting input root, exclusions, and case id `case-2-all-requirements-notes`.
- [ ] 3.2 Create `plinth-benchmark/scenario2/specs/scenario2.md` with scenario requirements aligned to issue #1012 Scenario 2.
- [ ] 3.3 Create `plinth-benchmark/scenario2/gherkin/scenario2.feature` with exactly one `@acceptance-test` scenario for case `case-2-all-requirements-notes`.

## 4. Scenario 3 — raw OpenSpec + official skill

- [ ] 4.1 Create `plinth-benchmark/scenario3/README.md` documenting OpenSpec input root, forbidden Plinth OpenSpec commands/skills, and case id `case-3-raw-openspec-official-skill`.
- [ ] 4.2 Create `plinth-benchmark/scenario3/specs/scenario3.md` with scenario requirements aligned to issue #1012 Scenario 3.
- [ ] 4.3 Create `plinth-benchmark/scenario3/gherkin/scenario3.feature` with exactly one `@acceptance-test` scenario for case `case-3-raw-openspec-official-skill`.

## 5. Scenario 4 — Plinth commands + refinement

- [ ] 5.1 Create `plinth-benchmark/scenario4/README.md` documenting README seed, forbidden checked-in OpenSpec input, Full Plinth requirement, command path, and case id `case-4-plinth-commands-openspec-refinement`.
- [ ] 5.2 Create `plinth-benchmark/scenario4/specs/scenario4.md` with scenario requirements aligned to issue #1012 Scenario 4.
- [ ] 5.3 Create `plinth-benchmark/scenario4/gherkin/scenario4.feature` with exactly one `@acceptance-test` scenario for case `case-4-plinth-commands-openspec-refinement`.

## 6. Validation

- [ ] 6.1 Verify all required files under `plinth-benchmark/` exist and paths in specs/Gherkin match the scenario contracts.
- [ ] 6.2 Run `openspec validate --all` from `documentation/` and ensure this change passes.
- [ ] 6.3 Run Markdown validation for the new harness Markdown when the repository Markdown validator is available.

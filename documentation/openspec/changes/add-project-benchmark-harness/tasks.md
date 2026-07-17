## 1. Harness root and metrics

- [ ] 1.1 Create `plinth-benchmark/README.md` with layout, scenario table (Cases 1–4), Case 3 marked pending, metrics scorecard, minimal v1 fields, and out-of-scope note for JVM `/benchmark`.
- [ ] 1.2 Ensure the README uses only repository-relative paths for God Analysis API inputs under `examples/openspec/god-analysis-api/`.

## 2. Scenario 1 — Case 1 README only

- [ ] 2.1 Create `plinth-benchmark/scenario1/README.md` documenting input `examples/openspec/god-analysis-api/requirements/problem1/README.md`, exclusions, and case id `case-1-readme-only`.
- [ ] 2.2 Create `plinth-benchmark/scenario1/specs/scenario1.md` with Case 1 requirements.
- [ ] 2.3 Create `plinth-benchmark/scenario1/gherkin/scenario1.feature` with exactly one `@acceptance-test` scenario for `case-1-readme-only`.

## 3. Scenario 2 — Case 2 all problem1 requirements files

- [ ] 3.1 Create `plinth-benchmark/scenario2/README.md` documenting input root `examples/openspec/god-analysis-api/requirements/problem1/`, the full file inventory, exclusions of `openspec/changes/`, and case id `case-2-all-problem1-requirements`.
- [ ] 3.2 Create `plinth-benchmark/scenario2/specs/scenario2.md` listing all Case 2 requirement files.
- [ ] 3.3 Create `plinth-benchmark/scenario2/gherkin/scenario2.feature` with exactly one `@acceptance-test` scenario for `case-2-all-problem1-requirements`.

## 4. Scenario 3 — Case 3 pending

- [ ] 4.1 Create `plinth-benchmark/scenario3/README.md` stating Case 3 is pending (`case-3-pending`) and input/workflow are TBD.
- [ ] 4.2 Create `plinth-benchmark/scenario3/specs/scenario3.md` as a pending placeholder (no runnable input contract).
- [ ] 4.3 Create `plinth-benchmark/scenario3/gherkin/scenario3.feature` with exactly one `@acceptance-test` scenario documenting that Case 3 is pending and not executable yet.

## 5. Scenario 4 — Case 4 current OpenSpec for problem1

- [ ] 5.1 Create `plinth-benchmark/scenario4/README.md` documenting input `examples/openspec/god-analysis-api/openspec/changes/` and case id `case-4-current-openspec-problem1`.
- [ ] 5.2 Create `plinth-benchmark/scenario4/specs/scenario4.md` with Case 4 requirements against the checked-in OpenSpec change tree.
- [ ] 5.3 Create `plinth-benchmark/scenario4/gherkin/scenario4.feature` with exactly one `@acceptance-test` scenario for `case-4-current-openspec-problem1`.

## 6. Validation

- [ ] 6.1 Verify all required files under `plinth-benchmark/` exist and Cases 1, 2, and 4 paths match the contracts; Case 3 is explicitly pending.
- [ ] 6.2 Run `openspec validate --all` from `documentation/` and ensure this change passes.
- [ ] 6.3 Run Markdown validation for the new harness Markdown when the repository Markdown validator is available.

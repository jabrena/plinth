## 1. Harness root and metrics

- [x] 1.1 Create `benchmarks/README.md` with layout, information-richness progression (Cases 1–4), Case 3 marked pending, metrics scorecard, minimal v1 fields, and out-of-scope note for JVM `/benchmark`.
- [x] 1.2 Ensure scenario input authority uses harness-local `benchmarks/scenario*/specs/` paths (upstream `examples/openspec/god-analysis-api/` is provenance only).

## 2. Scenario 1 — Case 1 minimal functional notes

- [x] 2.1 Create `benchmarks/scenario1/README.md` documenting input `specs/functional-requirements/README.md` only, exclusions (including no `.agents/skills` and no `.cursor/skills`), and case id `case-1-readme-only`.
- [x] 2.2 Provide `benchmarks/scenario1/specs/functional-requirements/README.md` as the minimal functional notes.
- [x] 2.3 Create `benchmarks/scenario1/gherkin/scenario1.feature` with exactly one `@acceptance-test` scenario for `case-1-readme-only` that requires a skill-agnostic workspace and a results JSON with minimal v1 metrics under `benchmarks/scenario1/results/`.
- [x] 2.4 Document how to measure each metric and the JSON shape in `benchmarks/scenario1/results/README.md`, with `example.result.json` as a template.

## 3. Scenario 2 — Case 2 full functional requirements

- [x] 3.1 Create `benchmarks/scenario2/README.md` documenting harness-local `specs/functional-requirements/problem1/`, full inventory, no technical OpenSpec, and case id `case-2-all-problem1-requirements`.
- [x] 3.2 Provide `benchmarks/scenario2/specs/functional-requirements/problem1/` with the complete problem1 functional requirements inventory.
- [x] 3.3 Create `benchmarks/scenario2/gherkin/scenario2.feature` with exactly one `@acceptance-test` scenario for `case-2-all-problem1-requirements`.

## 4. Scenario 3 — Case 3 pending

- [x] 4.1 Create `benchmarks/scenario3/README.md` stating Case 3 is pending (`case-3-pending`), reserved between Case 2 and Case 4 richness, and input/workflow are TBD.
- [x] 4.2 Create `benchmarks/scenario3/specs/scenario3.md` as a pending placeholder (no runnable input contract).
- [x] 4.3 Create `benchmarks/scenario3/gherkin/scenario3.feature` with exactly one `@acceptance-test` scenario documenting that Case 3 is pending and not executable yet.

## 5. Scenario 4 — Case 4 functional + technical (OpenSpec) requirements

- [x] 5.1 Create `benchmarks/scenario4/README.md` documenting functional-requirements and technical-requirements paths and case id `case-4-current-openspec-problem1`.
- [x] 5.2 Create `benchmarks/scenario4/specs/functional-requirements/problem1/` with the problem1 functional requirements inventory.
- [x] 5.3 Create `benchmarks/scenario4/specs/technical-requirements/openspec/` with the OpenSpec project whose Source and Derivation links point at the co-located functional-requirements files.
- [x] 5.4 Create `benchmarks/scenario4/gherkin/scenario4.feature` with exactly one `@acceptance-test` scenario for `case-4-current-openspec-problem1` covering the linked functional and technical trees.

## 6. Validation

- [x] 6.1 Verify all required files under `benchmarks/` exist; Cases 1–2 and Case 4 contracts match the richness ladder; Case 4 OpenSpec links resolve under functional-requirements; Case 3 is explicitly pending.
- [x] 6.2 Run `openspec validate --all` from `documentation/` and ensure this change passes.
- [x] 6.3 Run Markdown validation for the new harness Markdown when the repository Markdown validator is available.

# create-plan

## Purpose
Create or refine an executable technical implementation plan from the available project artifacts.

## Usage
```text
/create-plan <issue|design|adr|spec|existing-plan>
```

## Accepted Inputs
- Issue or user story
- Approved design and ADRs
- OpenSpec change
- Existing implementation plan
- Any valid combination of these artifacts

## Owning Agent
`@robot-tech-lead`

## Associated Skills
- `041-planning-plan-mode`
- `051-design-two-steps-methods` for every plan so preparatory work, behavior change, and verification remain explicitly sequenced
- `052-design-hamburger-method` when the requested plan is broad enough to need smallest-useful vertical slices before tasking
- `053-design-simple-rules` when plan alternatives need ordered design tradeoff evaluation before tasking
- `054-design-tdd` when testing-related requirements need test-first sequencing, red-green-refactor implementation boundaries, or verification-driven tasking
- `055-design-parallel-change` when database migration scenarios need expand, migrate, contract sequencing or compatibility-window tradeoff evaluation before tasking
- `121-java-object-oriented-design` when object responsibilities, boundaries, or collaboration design affect the plan
- `122-java-type-design` when domain types, value objects, invariants, signatures, or invalid-state modeling affect the plan
- `123-java-design-patterns` when a demonstrated collaboration or integration problem requires pattern selection before tasking

## Workflow
1. Identify the available source artifacts and their authority.
2. Resolve material implementation questions without inventing requirements.
3. Apply the two-step method so the plan orders behavior-preserving preparation, behavior-preservation validation, the intended behavior change, and final verification.
4. For broad scope, apply the Hamburger Method to identify the smallest useful vertical slice, defer costly or unnecessary options, and propose follow-up slices.
5. Apply Simple Design Rules when comparing design or refactoring alternatives so correctness, clarity, duplication, and minimal structure are evaluated in order.
6. Apply TDD guidance when creating testing-related plans so tasks identify the next behavior, failing-test-first signal, smallest passing change, and refactoring verification.
7. Apply Parallel Change guidance when database migration work needs expand, migrate, contract sequencing before framework-specific implementation tasks.
8. Apply Java design skills in order when the plan needs design detail: object-oriented responsibilities, type design, then design-pattern selection only for a demonstrated problem.
9. Define the technical approach, affected areas, ordered tasks, risks, and verification.
10. Record source links and any assumptions or unresolved blockers.
11. Present the plan for approval.

## Output
- Executable implementation plan
- Ordered work, dependencies, risks, and verification steps
- Traceability to source artifacts

## Safeguards
- Do not require an OpenSpec change when a plan-only workflow is selected.
- Do not duplicate a complete OpenSpec task list without adding technical value.
- Do not start implementation or create commits automatically.

# create-spec

## Purpose
Create or update one or more OpenSpec changes from the available issue, design, ADR, plan, or existing OpenSpec artifacts.

## Usage
```text
/create-spec <issue|design|adr|plan|existing-change>
```

## Accepted Inputs
- Issue or user story
- Approved design and ADRs
- Implementation plan
- Existing OpenSpec change
- Any valid combination of these artifacts

## Owning Agent
`@robot-tech-lead`

## Associated Skills
- `042-planning-openspec`
- `051-design-two-steps-methods` for every OpenSpec change so preparatory work, behavior change, and verification remain explicitly sequenced
- `052-design-hamburger-method` when the requested spec is broad enough to need smallest-useful vertical slices before tasking
- `053-design-simple-rules` when spec alternatives need ordered design tradeoff evaluation before requirements are finalized
- `054-design-tdd` when testing-related requirements need test-first sequencing, red-green-refactor acceptance boundaries, or verification-driven tasking
- `055-design-parallel-change` when database migration requirements need expand, migrate, contract sequencing or compatibility-window tradeoff evaluation before requirements are finalized
- `121-java-object-oriented-design` when object responsibilities, boundaries, or collaboration design affect the specification
- `122-java-type-design` when domain types, value objects, invariants, signatures, or invalid-state modeling affect the specification
- `123-java-design-patterns` when a demonstrated collaboration or integration problem requires pattern selection before requirements are finalized

## Workflow
1. Identify the available source artifacts and their authority.
2. Assess whether the scope fits one reviewable change.
3. Apply the two-step method so OpenSpec separates behavior-preserving preparation from behavior-changing work and validates each step.
4. For broad scope, apply the Hamburger Method to identify the smallest useful vertical slice, defer costly or unnecessary options, and propose follow-up slices.
5. Propose independently valuable changes and dependencies for approval when slicing reveals separate review, release, ownership, risk, or deployment boundaries.
6. Apply Simple Design Rules when comparing design or refactoring alternatives so requirements do not prefer abstraction or fewer elements before correctness and clarity.
7. Apply TDD guidance when testing-related requirements need test-first acceptance boundaries, next-behavior sequencing, or verification-driven tasking.
8. Apply Parallel Change guidance when database migration requirements need expand, migrate, contract sequencing before framework-specific implementation detail.
9. Apply Java design skills in order when the spec needs design detail: object-oriented responsibilities, type design, then design-pattern selection only for a demonstrated problem.
10. Create or update the approved OpenSpec proposal, design, specifications, and tasks.
11. Record derivation direction, source links, and unresolved questions.
12. Validate the resulting OpenSpec changes.

## Output
- One OpenSpec change, or an approved map of multiple changes
- Proposal, design, specifications, and task artifacts
- Validation and traceability report

## Safeguards
- Do not require a plan when a spec-first workflow is selected.
- Do not silently synchronize changes back into source artifacts.
- Do not invent requirements or split work by technical layer alone.

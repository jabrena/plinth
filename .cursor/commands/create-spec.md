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

## Workflow
1. Identify the available source artifacts and their authority.
2. Assess whether the scope fits one reviewable change.
3. Apply the two-step method so OpenSpec separates behavior-preserving preparation from behavior-changing work and validates each step.
4. For broad scope, propose independently valuable changes and dependencies for approval.
5. Apply the Hamburger Method for broad scope so the first change or slice remains smallest-useful and follow-up slices stay independently valuable.
6. Apply Simple Design Rules when comparing design or refactoring alternatives so requirements do not prefer abstraction or fewer elements before correctness and clarity.
7. Create or update the approved OpenSpec proposal, design, specifications, and tasks.
8. Record derivation direction, source links, and unresolved questions.
9. Validate the resulting OpenSpec changes.

## Output
- One OpenSpec change, or an approved map of multiple changes
- Proposal, design, specifications, and task artifacts
- Validation and traceability report

## Safeguards
- Do not require a plan when a spec-first workflow is selected.
- Do not silently synchronize changes back into source artifacts.
- Do not invent requirements or split work by technical layer alone.

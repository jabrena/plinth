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

## Workflow
1. Identify the available source artifacts and their authority.
2. Resolve material implementation questions without inventing requirements.
3. Apply the two-step method so the plan orders behavior-preserving preparation, behavior-preservation validation, the intended behavior change, and final verification.
4. Define the technical approach, affected areas, ordered tasks, risks, and verification.
5. Record source links and any assumptions or unresolved blockers.
6. Present the plan for approval.

## Output
- Executable implementation plan
- Ordered work, dependencies, risks, and verification steps
- Traceability to source artifacts

## Safeguards
- Do not require an OpenSpec change when a plan-only workflow is selected.
- Do not duplicate a complete OpenSpec task list without adding technical value.
- Do not start implementation or create commits automatically.

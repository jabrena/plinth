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
`@robot-architect`

## Associated Skills
- `042-planning-openspec`

## Workflow position
Runs first to create the initial OpenSpec proposal, design, specification, and task artifacts. Use `/explore-design` afterward when technical approach refinement is still needed.

## Workflow
1. Identify the available source artifacts and their authority.
2. Assess whether the scope fits one reviewable change.
3. Create or update the approved OpenSpec proposal, design, specifications, and tasks.
4. Record derivation direction, source links, unresolved questions, and compatibility-review assumptions.
5. Validate the resulting OpenSpec changes.

## Output
- One OpenSpec change, or an approved map of multiple changes
- Proposal, design, specifications, and task artifacts
- Validation and traceability report

## Safeguards
- Do not require a plan when a spec-first workflow is selected.
- Do not silently synchronize changes back into source artifacts.
- Do not invent requirements or split work by technical layer alone.
- Do not apply design skills `051`–`057`, `121`–`123`, or `130`; those belong to `/explore-design`.

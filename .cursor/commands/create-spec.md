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

## Associated Skill
`042-planning-openspec`

## Workflow
1. Identify the available source artifacts and their authority.
2. Assess whether the scope fits one reviewable change.
3. For broad scope, propose independently valuable changes and dependencies for approval.
4. Create or update the approved OpenSpec proposal, design, specifications, and tasks.
5. Record derivation direction, source links, and unresolved questions.
6. Validate the resulting OpenSpec changes.

## Output
- One OpenSpec change, or an approved map of multiple changes
- Proposal, design, specifications, and task artifacts
- Validation and traceability report

## Safeguards
- Do not require a plan when a spec-first workflow is selected.
- Do not silently synchronize changes back into source artifacts.
- Do not invent requirements or split work by technical layer alone.

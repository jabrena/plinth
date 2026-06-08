# review-alignment

## Purpose
Review available analysis and design artifacts for consistency, traceability, completeness, and implementation readiness.

## Usage
```text
/review-alignment <artifact> [<artifact> ...]
```

## Accepted Inputs
- Issue or user story
- Approved design
- ADRs and diagrams
- Implementation plan
- OpenSpec proposal, design, specifications, and tasks
- Any partial combination of these artifacts

## Owning Agent
`@robot-business-analyst`

## Associated Capabilities
- Requirement and acceptance-criteria traceability
- Plan and OpenSpec consistency review
- ADR and design alignment review
- Readiness assessment

## Workflow
1. Inventory the provided artifacts and identify their authority.
2. Trace requirements, decisions, planned work, and verification across the available set.
3. Detect contradictions, scope drift, gaps, unresolved blockers, and stale derivations.
4. Rank findings by severity and recommend explicit corrections.
5. Report readiness as `READY`, `READY WITH WARNINGS`, or `NOT READY`.

## Output
- Aligned areas
- Severity-ranked findings
- Open questions and recommended corrections
- Readiness result

## Safeguards
- Accept partial artifact sets without requiring every artifact type.
- Keep the review read-only.
- Do not modify artifacts or resolve conflicts without explicit user approval.

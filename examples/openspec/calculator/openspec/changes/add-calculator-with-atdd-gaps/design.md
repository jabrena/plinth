## Context

OpenSpec schema validation proves artifact structure, but it does not prove
semantic alignment between an execution goal, observable criteria, and tasks.
The negative fixture therefore remains structurally valid while preserving
deliberate ATDD gaps for `059-design-atdd` to report.

## Decisions

### Preserve distinct alignment findings

The fixture contains evidence for every review classification:

- Complete implementation and verification coverage.
- Implementation without observable verification.
- A criterion without associated tasks.
- A criterion whose expected outcome is ambiguous.
- A proposal goal without an acceptance criterion.
- A task that contradicts an explicit scope boundary.

The fixture records these gaps without resolving them so the acceptance review
can prove the `changes-requested` outcome.

### Preserve fixture scope

This change is test data. Its deliberate gaps must not be resolved or
synchronized into the aligned calculator change.

## Validation Strategy

- Require `openspec validate --all` to accept the fixture structure.
- Apply `059-design-atdd` and require evidence-backed findings for every gap.
- Require the review to return `changes-requested`, explain every pending
  finding, ask the maintainer how to revise the artifacts, and leave them unchanged.

## Open Questions

None. The gaps are deliberate acceptance-test inputs.

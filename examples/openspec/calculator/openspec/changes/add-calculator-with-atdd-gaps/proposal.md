## Why

A calculator user needs arithmetic operations with observable results and a
defined error when division by zero is attempted.

This change is a repository-owned negative fixture for reviewing whether
`059-design-atdd` requests changes when an OpenSpec change is structurally
valid but its goal, acceptance criteria, and tasks are not aligned.

## What Changes

- Add `add`, `subtract`, `multiply`, and `divide` operations for two operands.
- Reject division by zero with an observable error.
- Keep calculation history and persistence outside the calculator scope.

## Capabilities

### New Capabilities

- `calculator-with-atdd-gaps`: Describes calculator behavior whose planning
  artifacts intentionally contain ATDD alignment gaps for acceptance review.

### Modified Capabilities

None.

## Source and Derivation

- Behavior source: the repository-owned aligned calculator OpenSpec change.
- Maintainer-approved derivation direction: aligned calculator behavior ->
  structurally valid negative ATDD fixture.
- This negative fixture is authoritative only for testing alignment findings;
  it must not propagate changes back to the aligned calculator change.

## Change Boundary Assessment

This is one change because all deliberately misaligned artifacts serve one
independently reviewable outcome: prove that `059-design-atdd` requests changes
and explains the pending findings when ATDD alignment is incomplete.

## Impact

This fixture is planning and acceptance-test data only. Calculation history
and persistence remain outside its declared scope.

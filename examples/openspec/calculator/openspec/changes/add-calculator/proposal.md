## Why

A calculator user needs to apply one arithmetic operation to two numeric
operands and observe either the calculated result or a defined error.

The repository also needs a small, fully aligned OpenSpec change that can be
reviewed by `059-design-atdd` using native proposal, specification, and task
artifacts.

## What Changes

- Add `add`, `subtract`, `multiply`, and `divide` operations for two operands.
- Support negative operands in multiplication.
- Return the defined result for non-zero division.
- Reject division by zero with the exact observable message
  `Division by zero is not allowed`.
- Provide implementation and verification tasks for every specified behavior.

## Capabilities

### New Capabilities

- `calculator`: Adds the four arithmetic operations and defined
  division-by-zero behavior.

### Modified Capabilities

None.

## Source and Derivation

- Content source during migration: the former repository-owned standalone
  calculator functional requirement, Gherkin scenarios, and task checklist.
- Maintainer-approved derivation direction: standalone calculator fixture ->
  OpenSpec proposal, capability specification, and single task checklist.
- After migration, this OpenSpec change is authoritative for the calculator
  example; no automatic two-way synchronization is maintained.

## Change Boundary Assessment

This is one OpenSpec change because addition, subtraction, multiplication,
division, and division-by-zero handling form one independently reviewable
calculator capability with one execution and verification boundary.

## Impact

An implementation would affect calculator operation code and its automated
verification. User interfaces, calculation history, persistence, localization,
arbitrary-precision policy, and rounding of non-terminating decimal results are
outside this example's scope.

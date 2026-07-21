## ADDED Requirements

### Requirement: FR-CALC-GAPS-ADD Addition

The calculator MUST return the sum of the first and second operands for the
`add` operation.

#### Scenario: AC-CALC-GAPS-ADD Add two operands

- **GIVEN** the first operand is `8`
- **AND** the second operand is `5`
- **WHEN** the user selects the `add` operation
- **THEN** the calculator returns `13`

### Requirement: FR-CALC-GAPS-SUBTRACT Subtraction

The calculator MUST subtract the second operand from the first operand for the
`subtract` operation.

#### Scenario: AC-CALC-GAPS-SUBTRACT Subtract the second operand from the first

- **GIVEN** the first operand is `8`
- **AND** the second operand is `5`
- **WHEN** the user selects the `subtract` operation
- **THEN** the calculator returns `3`

### Requirement: FR-CALC-GAPS-MULTIPLY Multiplication

The calculator MUST return the product of the first and second operands for the
`multiply` operation.

#### Scenario: AC-CALC-GAPS-MULTIPLY Multiply two operands

- **GIVEN** the first operand is `4`
- **AND** the second operand is `2`
- **WHEN** the user selects the `multiply` operation
- **THEN** the calculator returns `8`

### Requirement: FR-CALC-GAPS-DIVIDE Division

The calculator MUST divide the first operand by a non-zero second operand for
the `divide` operation.

#### Scenario: AC-CALC-GAPS-DIVIDE Handle division correctly

- **GIVEN** two numeric operands
- **WHEN** the user selects the `divide` operation
- **THEN** the calculator handles the result correctly

## ADDED Requirements

### Requirement: FR-CALC-ADD Addition

The calculator MUST return the sum of the first and second operands for the
`add` operation.

#### Scenario: AC-CALC-ADD Add two operands

- **GIVEN** the first operand is `8`
- **AND** the second operand is `5`
- **WHEN** the user selects the `add` operation
- **THEN** the calculator returns `13`

### Requirement: FR-CALC-SUBTRACT Subtraction

The calculator MUST subtract the second operand from the first operand for the
`subtract` operation.

#### Scenario: AC-CALC-SUBTRACT Subtract the second operand from the first

- **GIVEN** the first operand is `8`
- **AND** the second operand is `5`
- **WHEN** the user selects the `subtract` operation
- **THEN** the calculator returns `3`

### Requirement: FR-CALC-MULTIPLY Multiplication

The calculator MUST return the product of the first and second operands for the
`multiply` operation and MUST support negative operands.

#### Scenario: AC-CALC-MULTIPLY Multiply by a negative operand

- **GIVEN** the first operand is `-4`
- **AND** the second operand is `2`
- **WHEN** the user selects the `multiply` operation
- **THEN** the calculator returns `-8`

### Requirement: FR-CALC-DIVIDE Division

The calculator MUST divide the first operand by a non-zero second operand for
the `divide` operation.

#### Scenario: AC-CALC-DIVIDE Divide by a non-zero operand

- **GIVEN** the first operand is `9`
- **AND** the second operand is `3`
- **WHEN** the user selects the `divide` operation
- **THEN** the calculator returns `3`

### Requirement: FR-CALC-DIVIDE-ZERO Division by zero

The calculator MUST reject the `divide` operation when the second operand is
zero and MUST return the exact observable error message
`Division by zero is not allowed`.

#### Scenario: AC-CALC-DIVIDE-ZERO Reject division by zero

- **GIVEN** the first operand is `9`
- **AND** the second operand is `0`
- **WHEN** the user selects the `divide` operation
- **THEN** the calculator rejects the calculation with the message
  `Division by zero is not allowed`

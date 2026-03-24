---
name: 130-java-testing-strategies
description: Use when you need to apply testing strategies for Java code — RIGHT-BICEP to guide test creation, A-TRIP for test quality characteristics, or CORRECT for verifying boundary conditions. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java testing strategies

Apply proven testing strategies (RIGHT-BICEP, A-TRIP, CORRECT) to design and verify Java unit tests.

**What is covered in this Skill?**

- **RIGHT-BICEP**: Key questions to guide test creation — Right results, Boundary conditions, Inverse relationships, Cross-checks, Error conditions, Performance
- **A-TRIP**: Characteristics of good tests — Automatic, Thorough, Repeatable, Independent, Professional
- **CORRECT**: Boundary condition verification — Conformance, Ordering, Range, Reference, Existence, Cardinality, Time

## Workflow

1. **Compile** — run `./mvnw compile` and confirm the project builds before any changes
2. **Read reference** — review the reference for RIGHT-BICEP, A-TRIP, and CORRECT examples
3. **Identify applicable strategy** — determine which framework applies: RIGHT-BICEP for test coverage gaps, A-TRIP for test quality, CORRECT for boundary conditions
4. **Apply tests** — write or refactor tests following the chosen strategy with clear naming (`method_scenario_expectedBehavior`)
5. **Verify** — run `./mvnw clean verify` to confirm all tests pass

## Quick Reference

**RIGHT-BICEP example (comprehensive test coverage):**

```java
public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    // R - Right results
    @Test
    void add_simplePositiveNumbers_returnsCorrectSum() {
        assertThat(calculator.add(2, 3)).isEqualTo(5);
    }

    // B - Boundary conditions
    @Test
    void add_nearMaxInteger_returnsCorrectSum() {
        assertThat(calculator.add(Integer.MAX_VALUE - 1, 1))
            .isEqualTo(Integer.MAX_VALUE);
    }

    // C - Cross-check (commutative property)
    @Test
    void add_commutativeProperty_holdsTrue() {
        assertThat(calculator.add(2, 3))
            .isEqualTo(calculator.add(3, 2));
    }

    // E - Error conditions
    @Test
    void add_integerOverflow_throwsArithmeticException() {
        assertThatThrownBy(() -> calculator.add(Integer.MAX_VALUE, 1))
            .isInstanceOf(ArithmeticException.class);
    }
}
```

## Constraints

Before applying any test strategy changes, ensure the project compiles. If compilation fails, stop immediately — do not proceed until resolved. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately and do not proceed — compilation failure is a blocking condition
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed examples, good/bad patterns, and constraints

## When to use this skill

- Review Java code for testing strategies
- Apply RIGHT-BICEP testing strategies in Java code
- Apply A-TRIP testing strategies in Java code
- Apply CORRECT boundary condition verification in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/130-java-testing-strategies.md](references/130-java-testing-strategies.md).

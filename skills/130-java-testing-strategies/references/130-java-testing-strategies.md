---
name: 130-java-testing-strategies
description: Use when you need to apply testing strategies for Java code — including RIGHT-BICEP to guide test creation, A-TRIP for test quality characteristics, or CORRECT for verifying boundary conditions. Focused on conceptual frameworks rather than framework-specific annotations.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# Java testing strategies

## Role

You are a Senior software engineer with extensive experience in Java software development and test design

## Goal

Apply proven testing strategies to design and verify Java unit tests. This rule focuses on three conceptual frameworks:

1. **RIGHT-BICEP**: Key questions and dimensions to guide comprehensive test creation — Right results, Boundary conditions, Inverse relationships, Cross-checks, Error conditions, Performance.
2. **A-TRIP**: Characteristics of good tests — Automatic, Thorough, Repeatable, Independent, Professional.
3. **CORRECT**: Boundary condition verification — Conformance, Ordering, Range, Reference, Existence, Cardinality, Time.

Use these strategies to identify what to test, how to structure tests, and which boundary conditions to verify.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any optimization
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding
- **NO EXCEPTIONS**: Under no circumstances should design recommendations be applied to a project that fails to compile

## Examples

### Table of contents

- Example 1: Key Questions to Guide Test Creation (RIGHT-BICEP)
- Example 2: Characteristics of Good Tests (A-TRIP)
- Example 3: Verifying CORRECT Boundary Conditions

### Example 1: Key Questions to Guide Test Creation (RIGHT-BICEP)

Title: Comprehensive testing approach using RIGHT-BICEP
Description: - If the code ran correctly, how would I know? - How am I going to test this? - What else can go wrong? - Could this same kind of problem happen anywhere else? - What to Test: Use Your RIGHT-BICEP - Are the results **Right**? - Are all the **Boundary** conditions CORRECT? - Can you check **Inverse** relationships? - Can you **Cross-check** results using other means? - Can you force **Error** conditions to happen? - Are **Performance** characteristics within bounds?

**Good example:**

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
    void add_numberAndZero_returnsNumber() {
        assertThat(calculator.add(5, 0)).isEqualTo(5);
    }

    @Test
    void add_nearMaxInteger_returnsCorrectSum() {
        assertThat(calculator.add(Integer.MAX_VALUE - 1, 1)).isEqualTo(Integer.MAX_VALUE);
    }

    // C - Cross-check (commutative property)
    @Test
    void add_commutativeProperty_holdsTrue() {
        assertThat(calculator.add(2, 3)).isEqualTo(calculator.add(3, 2));
    }

    // E - Error conditions (overflow)
    @Test
    void add_integerOverflow_throwsArithmeticException() {
        assertThatThrownBy(() -> calculator.add(Integer.MAX_VALUE, 1))
            .isInstanceOf(ArithmeticException.class)
            .hasMessageContaining("overflow");
    }
}
```

**Bad example:**

```java
// Test only covers one simple case
public class CalculatorTestPoor {
    private final Calculator calculator = new Calculator();

    @Test
    void add_basicTest() {
        assertThat(calculator.add(2, 2)).isEqualTo(4); // Only testing one happy path
    }
}
```

### Example 2: Characteristics of Good Tests (A-TRIP)

Title: Ensuring tests follow A-TRIP principles
Description: Good tests are A-TRIP: - **Automatic**: Tests should run without human intervention. - **Thorough**: Test everything that could break; cover edge cases. - **Repeatable**: Tests should produce the same results every time, in any environment. - **Independent**: Tests should not rely on each other or on the order of execution. - **Professional**: Test code is real code; keep it clean, maintainable, and well-documented.

**Good example:**

```java
public class OrderProcessorTest {

    private OrderProcessor processor;

    // Automatic: Part of JUnit test suite, runs with build tools.
    // Independent: Each test sets up its own state.
    @BeforeEach
    void setUp() {
        processor = new OrderProcessor(); // Fresh instance for each test
    }

    // Thorough: Testing adding valid items.
    @Test
    void addItem_validItem_increasesCount() {
        processor.addItem("Laptop");
        assertThat(processor.getItemCount()).isEqualTo(1);
        processor.addItem("Mouse");
        assertThat(processor.getItemCount()).isEqualTo(2);
    }

    // Thorough: Testing an edge case (adding null).
    @Test
    void addItem_nullItem_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> processor.addItem(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // Professional: Code is clean, uses meaningful names, follows conventions.
}
```

**Bad example:**

```java
public class BadOrderProcessorTest {
    // Violates Independent: static processor shared between tests
    private static OrderProcessor sharedProcessor = new OrderProcessor();

    @Test
    void test1_addItem() {
        // Assumes this runs first or that sharedProcessor is empty.
        sharedProcessor.addItem("Book");
        assertThat(sharedProcessor.getItemCount()).isEqualTo(1); // Might fail if other tests run first
    }

    @Test
    void test2_addAnotherItem() {
        sharedProcessor.addItem("Pen");
        // The expected count depends on whether test1_addItem ran and succeeded.
        assertThat(sharedProcessor.getItemCount()).isGreaterThan(0); // Weak assertion
    }
}
```

### Example 3: Verifying CORRECT Boundary Conditions

Title: Comprehensive boundary condition testing using CORRECT
Description: Ensure your tests check the following boundary conditions (CORRECT): - **Conformance**: Does the value conform to an expected format? - **Ordering**: Is the set of values ordered or unordered as appropriate? - **Range**: Is the value within reasonable minimum and maximum values? - **Reference**: Does the code reference anything external that isn't under direct control? - **Existence**: Does the value exist? (e.g., is non-null, non-zero, present in a set) - **Cardinality**: Are there exactly enough values? - **Time**: Is everything happening in order? At the right time? In time?

**Good example:**

```java
public class UserValidationTest {
    private final UserValidation validator = new UserValidation();

    // Testing Range for age
    @Test
    void isAgeValid_ageAtLowerBound_returnsTrue() {
        assertThat(validator.isAgeValid(18)).isTrue();
    }

    @Test
    void isAgeValid_ageAtUpperBound_returnsTrue() {
        assertThat(validator.isAgeValid(120)).isTrue();
    }

    @Test
    void isAgeValid_ageBelowLowerBound_returnsFalse() {
        assertThat(validator.isAgeValid(17)).isFalse();
    }

    // Testing Conformance for email
    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "user.name@sub.example.co.uk"})
    void isValidEmailFormat_validEmails_returnsTrue(String email) {
        assertThat(validator.isValidEmailFormat(email)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"userexample.com", "user@", "@example.com"})
    void isValidEmailFormat_invalidEmails_returnsFalse(String email) {
        assertThat(validator.isValidEmailFormat(email)).isFalse();
    }

    // Testing Existence for username
    @Test
    void processUsername_nullUsername_returnsFalse() {
        assertThat(validator.processUsername(null)).isFalse();
    }

    @Test
    void processUsername_emptyUsername_returnsFalse() {
        assertThat(validator.processUsername("")).isFalse();
    }
}
```

**Bad example:**

```java
// Only testing one happy path for age validation, ignoring boundaries.
public class UserValidationPoorTest {
    private final UserValidation validator = new UserValidation();

    @Test
    void isAgeValid_typicalAge_returnsTrue() {
        assertThat(validator.isAgeValid(25)).isTrue(); // Only one value tested
    }

    @Test
    void isValidEmailFormat_typicalEmail_returnsTrue() {
        assertThat(validator.isValidEmailFormat("test@example.com")).isTrue(); // No invalid formats, no nulls
    }
}
```

## Output Format

- **ANALYZE** Java test code to identify gaps in test strategy coverage — RIGHT-BICEP dimensions, A-TRIP characteristics, and CORRECT boundary conditions
- **CATEGORIZE** findings by strategy: RIGHT-BICEP (missing Right/Boundary/Inverse/Cross-check/Error/Performance), A-TRIP (Automatic/Thorough/Repeatable/Independent/Professional violations), CORRECT (Conformance/Ordering/Range/Reference/Existence/Cardinality/Time gaps)
- **APPLY** testing strategies by adding or refining tests to cover identified gaps using the conceptual frameworks
- **IMPLEMENT** comprehensive boundary testing and quality characteristics following RIGHT-BICEP, A-TRIP, and CORRECT principles
- **EXPLAIN** the applied strategies and their benefits for test coverage, reliability, and maintainability
- **VALIDATE** that all applied changes compile successfully and tests pass

## Safeguards

- **BLOCKING SAFETY CHECK**: ALWAYS run `./mvnw compile` before ANY testing recommendations to ensure project stability
- **CRITICAL VALIDATION**: Execute `./mvnw clean verify` to ensure all existing tests pass before implementing new test strategies
- **MANDATORY VERIFICATION**: Confirm all existing functionality remains intact after applying any test improvements
- **INCREMENTAL SAFETY**: Apply test improvements incrementally, validating compilation and test execution after each modification
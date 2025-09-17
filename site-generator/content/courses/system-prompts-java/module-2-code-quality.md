title=Module 2: Code Quality - Testing & Design Principles
type=course
status=published
date=2025-09-17
author=Juan Antonio Breña Moral
version=0.11.0-SNAPSHOT
tags=java, testing, design-patterns, oop, solid, unit-testing, system-prompts
~~~~~~

# Module 2: Code Quality - Testing & Design Principles

## 🎯 Learning Objectives

By the end of this module, you will:

- **Generate comprehensive unit tests** using AI-powered system prompts
- **Apply object-oriented design principles** systematically
- **Implement type-safe design patterns** effectively
- **Create maintainable, testable code** following industry standards
- **Understand the relationship** between testing and good design

## 📚 Module Overview

**Duration:** 5 hours
**Difficulty:** Intermediate
**Prerequisites:** Module 1 completed, OOP fundamentals

This module focuses on code quality through automated testing and design principles. You'll learn to use system prompts that not only generate tests but also improve your code's design and maintainability.

## 🗺️ Learning Path

### **Lesson 2.1: Unit Testing Mastery** (90 minutes)

#### 🎯 **Learning Objectives:**
- Master unit testing best practices using `@131-java-unit-testing`
- Understand test-driven development workflows
- Generate comprehensive test suites automatically

#### 📖 **Core Concepts:**

**Modern Unit Testing Principles:**

1. **AAA Pattern**: Arrange, Act, Assert
2. **Test Naming**: Descriptive, behavior-focused names
3. **Test Independence**: Each test runs in isolation
4. **Mocking Strategy**: Test units, not dependencies
5. **Coverage Goals**: Meaningful coverage, not just percentages

#### 💡 **Knowledge Check:**
*What's the difference between testing implementation details vs. testing behavior?*

**Answer:** Testing behavior focuses on what the code does (outcomes), while testing implementation focuses on how it does it (internal mechanics). Behavior testing is more resilient to refactoring.

#### 🔧 **Hands-on Exercise 2.1:**

**Scenario:** You have a service class that needs comprehensive test coverage.

**Step 1: Analyze the Code**
```bash
cd examples/maven-demo/src/main/java/com/example
```

Look at the `Calculator.java` class:
```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public double divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return (double) a / b;
    }
}
```

**Step 2: Apply System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @131-java-unit-testing`

**Step 3: Interactive Approach**
Try: `Improve the class/classes added in the context applying the system prompt @131-java-unit-testing with the behaviour @behaviour-consultative-interaction`

**Expected Outcomes:**
- Comprehensive test class with JUnit 5
- Parameterized tests for multiple scenarios
- Exception testing with proper assertions
- Mock usage where appropriate
- Clear, descriptive test names

#### 🔍 **Deep Dive: Generated Test Analysis**

The system prompt should generate tests like:
```java
@Test
@DisplayName("Should return sum when adding two positive numbers")
void shouldReturnSum_WhenAddingTwoPositiveNumbers() {
    // Arrange
    Calculator calculator = new Calculator();
    int firstNumber = 5;
    int secondNumber = 3;

    // Act
    int result = calculator.add(firstNumber, secondNumber);

    // Assert
    assertThat(result).isEqualTo(8);
}

@Test
@DisplayName("Should throw IllegalArgumentException when dividing by zero")
void shouldThrowIllegalArgumentException_WhenDividingByZero() {
    // Arrange
    Calculator calculator = new Calculator();

    // Act & Assert
    assertThatThrownBy(() -> calculator.divide(10, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Division by zero");
}
```

**Why This Approach Works:**
- **Clear Intent**: Test names describe expected behavior
- **AAA Structure**: Organized, readable test structure
- **Proper Assertions**: Using AssertJ for fluent assertions
- **Exception Testing**: Validates error conditions properly

---

### **Lesson 2.2: Object-Oriented Design Excellence** (75 minutes)

#### 🎯 **Learning Objectives:**
- Apply OOP principles using `@121-java-object-oriented-design`
- Understand SOLID principles in practice
- Refactor procedural code to object-oriented design

#### 📖 **Core Concepts:**

**SOLID Principles Refresher:**

1. **Single Responsibility**: One reason to change
2. **Open/Closed**: Open for extension, closed for modification
3. **Liskov Substitution**: Subtypes must be substitutable
4. **Interface Segregation**: Many specific interfaces vs. one general
5. **Dependency Inversion**: Depend on abstractions, not concretions

#### 🔧 **Hands-on Exercise 2.2:**

**Scenario:** Refactor a monolithic service class into proper OOP design.

**Step 1: Analyze Problematic Code**
```java
public class UserService {
    public void processUser(String userData) {
        // Parse user data
        String[] parts = userData.split(",");
        String name = parts[0];
        String email = parts[1];

        // Validate email
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        // Save to database
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.executeUpdate();

        // Send welcome email
        EmailClient client = new EmailClient();
        client.sendEmail(email, "Welcome", "Welcome to our service!");
    }
}
```

**Problems Identified:**
- Multiple responsibilities (parsing, validation, persistence, notification)
- Hard dependencies on database and email service
- Difficult to test
- Violates Single Responsibility Principle

**Step 2: Apply System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @121-java-object-oriented-design`

**Step 3: Analyze Improvements**
The system prompt should suggest:

```java
// Separated concerns
public class User {
    private final String name;
    private final String email;
    // Constructor, getters, validation
}

public interface UserRepository {
    void save(User user);
}

public interface EmailService {
    void sendWelcomeEmail(String email);
}

public class UserService {
    private final UserRepository repository;
    private final EmailService emailService;

    public UserService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public void processUser(String userData) {
        User user = parseUser(userData);
        repository.save(user);
        emailService.sendWelcomeEmail(user.getEmail());
    }
}
```

#### 💡 **Design Benefits Analysis:**
- **Single Responsibility**: Each class has one clear purpose
- **Dependency Injection**: Testable, flexible dependencies
- **Interface Segregation**: Focused interfaces for specific needs
- **Open/Closed**: Easy to extend with new user types or services

---

### **Lesson 2.3: Type Design Mastery** (90 minutes)

#### 🎯 **Learning Objectives:**
- Implement type-safe design using `@122-java-type-design`
- Understand value objects and domain modeling
- Apply modern Java type system features

#### 📖 **Core Concepts:**

**Type Safety Principles:**

1. **Make Illegal States Unrepresentable**: Use types to prevent invalid data
2. **Value Objects**: Immutable objects representing values
3. **Domain Modeling**: Types that reflect business concepts
4. **Null Safety**: Avoid NullPointerException through design
5. **Generic Constraints**: Use bounded type parameters effectively

#### 🔧 **Hands-on Exercise 2.3:**

**Scenario:** Design a type-safe e-commerce domain model.

**Step 1: Problematic Primitive-Based Design**
```java
public class Order {
    private String customerId;      // Could be empty or null
    private String productId;       // Could be empty or null
    private int quantity;           // Could be negative
    private double price;           // Could be negative
    private String status;          // Could be any string
}
```

**Problems:**
- Primitive obsession
- No validation at type level
- Possible invalid states
- No business meaning in types

**Step 2: Apply System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @122-java-type-design`

**Step 3: Analyze Type-Safe Design**
Expected improvements:

```java
// Value objects with validation
public record CustomerId(String value) {
    public CustomerId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
    }
}

public record ProductId(String value) {
    public ProductId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }
    }
}

public record Quantity(int value) {
    public Quantity {
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}

public record Price(BigDecimal amount) {
    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}

public enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}

public record Order(
    CustomerId customerId,
    ProductId productId,
    Quantity quantity,
    Price price,
    OrderStatus status
) {
    // Impossible to create invalid Order instances
}
```

#### 💡 **Type Safety Benefits:**
- **Compile-Time Safety**: Invalid states caught at compile time
- **Self-Documenting**: Types express business rules
- **Refactoring Safety**: Changes propagate through type system
- **Testing Simplicity**: Easier to test valid vs. invalid states

---

### **Lesson 2.4: Integration Testing Strategies** (45 minutes)

#### 🎯 **Learning Objectives:**
- Understand testing pyramid concepts
- Learn when to use unit vs. integration tests
- Apply testing strategies to real-world scenarios

#### 📖 **Core Concepts:**

**Testing Pyramid:**
```
        /\
       /  \
      / UI \
     /______\
    /        \
   /Integration\
  /__________\
 /            \
/   Unit Tests  \
/________________\
```

**Testing Strategy Guidelines:**
- **Unit Tests (70%)**: Fast, isolated, focused
- **Integration Tests (20%)**: Component interactions
- **UI Tests (10%)**: End-to-end scenarios

#### 🔧 **Hands-on Exercise 2.4:**

**Step 1: Identify Test Scenarios**
For the improved `UserService`:
- **Unit Tests**: Individual method behavior
- **Integration Tests**: Database interactions
- **Contract Tests**: External service interactions

**Step 2: Generate Test Suite**
Use system prompts to create comprehensive test coverage.

---

## 🏆 Module Assessment

### **Knowledge Validation Checkpoint**

**Question 1:** What are the three parts of the AAA testing pattern?

**Question 2:** Which SOLID principle is violated when a class has multiple reasons to change?

**Question 3:** How do value objects improve type safety?

**Question 4:** What's the ideal distribution of tests in the testing pyramid?

### **Practical Assessment Project**

**Project: "E-Commerce Service Refactoring"**

**Scenario:** You inherit a poorly designed e-commerce service that needs comprehensive improvement.

**Starting Code:**
```java
public class OrderProcessor {
    public String processOrder(String customerData, String productData, String quantityStr, String priceStr) {
        // Monolithic method with multiple responsibilities
        // No validation, no proper types, hard to test
    }
}
```

**Requirements:**
1. Apply object-oriented design principles
2. Implement type-safe domain model
3. Generate comprehensive unit tests
4. Create integration test strategy
5. Document design decisions

**Deliverables:**
- Refactored code with proper OOP design
- Type-safe domain model using records and enums
- Complete unit test suite (>90% coverage)
- Integration test examples
- Design documentation explaining improvements

**Success Criteria:**
- All SOLID principles properly applied
- Type safety prevents invalid states
- Tests are comprehensive and maintainable
- Code is readable and self-documenting
- System prompts used effectively throughout

### **Time Investment:**
- **Analysis & Planning**: 45 minutes
- **Implementation**: 2 hours
- **Testing**: 1.5 hours
- **Documentation**: 30 minutes
- **Total**: 4.5 hours

---

## 🚀 Next Steps

**Excellent Progress!** You've mastered code quality fundamentals through automated testing and design principles.

**What You've Accomplished:**
- ✅ Generated comprehensive unit test suites
- ✅ Applied SOLID principles systematically
- ✅ Implemented type-safe domain models
- ✅ Created maintainable, testable code

**Ready for advanced topics?**

👉 **[Continue to Module 3: Secure Coding →](module-3-secure-coding.html)**

**In Module 3, you'll learn to:**
- Implement security best practices automatically
- Handle concurrency safely and efficiently
- Apply proper logging and exception handling
- Create robust, production-ready applications

---

## 📚 Additional Resources

- **[JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)**
- **[AssertJ Documentation](https://assertj.github.io/doc/)**
- **[Clean Code Principles](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)**
- **[Effective Java by Joshua Bloch](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997)**

---

*Build upon your testing and design foundation with advanced security and concurrency patterns in the next module.*

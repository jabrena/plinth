title=Module 4: Modern Java - Advanced Language Features
type=course
status=published
date=2025-09-17
author=MyRobot
version=0.11.0-SNAPSHOT
tags=java, generics, functional-programming, records, sealed-types, modern-java, system-prompts
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- **Master Java Generics** from basics to advanced patterns using `@128-java-generics`
- **Apply functional programming** techniques effectively using `@142-java-functional-programming`
- **Implement functional exception handling** with monads using `@143-java-functional-exception-handling`
- **Leverage data-oriented programming** with records and sealed types using `@144-java-data-oriented-programming`
- **Refactor legacy code** with modern Java features using `@141-java-refactoring-with-modern-features`

## 📚 Module Overview

**Duration:** 6 hours
**Difficulty:** Intermediate to Advanced
**Prerequisites:** Module 3 completed, Java 17+ knowledge

This module explores the cutting-edge features of modern Java, transforming how you write expressive, type-safe, and maintainable code. You'll learn to leverage the full power of Java's evolution from Java 8 to Java 21+.

## 🗺️ Learning Path

### **Lesson 4.1: Java Generics Mastery** (120 minutes)

#### 🎯 **Learning Objectives:**
- Understand generics from fundamentals to advanced patterns
- Master wildcards and the PECS principle
- Implement type-safe generic containers and builders
- Handle type erasure limitations effectively

#### 📖 **Core Concepts:**

**Generics Fundamentals:**

1. **Type Safety**: Compile-time type checking
2. **Type Erasure**: Runtime behavior and limitations
3. **Wildcards**: `? extends T` (producer) and `? super T` (consumer)
4. **PECS Principle**: Producer Extends, Consumer Super
5. **Generic Methods**: Type parameter inference
6. **Bounded Type Parameters**: Constraining generic types

#### 💡 **Knowledge Check:**
*Why can't you create an array of parameterized types like `new List<String>[10]`?*

**Answer:** Due to type erasure, arrays retain their component type at runtime while generics don't. This would break array store checks and type safety.

#### 🔧 **Hands-on Exercise 4.1:**

**Scenario:** Build a type-safe data processing pipeline with proper generic design.

**Step 1: Legacy Non-Generic Code**
```java
public class DataProcessor {
    private List items = new ArrayList(); // Raw type!

    public void addItem(Object item) {
        items.add(item);
    }

    public Object getItem(int index) {
        return items.get(index); // Requires casting!
    }

    public List processItems(List input) {
        List result = new ArrayList();
        for (Object item : input) {
            result.add(process(item)); // No type safety!
        }
        return result;
    }

    private Object process(Object item) {
        return item.toString().toUpperCase();
    }
}
```

**Problems:**
- Raw types lose compile-time safety
- Requires explicit casting
- Runtime ClassCastException risk
- No API contract clarity

**Step 2: Apply Generics System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @128-java-generics`

**Step 3: Analyze Generic Improvements**
Expected transformation:

```java
public class GenericDataProcessor<T> {
    private final List<T> items = new ArrayList<>();

    public void addItem(T item) {
        Objects.requireNonNull(item, "Item cannot be null");
        items.add(item);
    }

    public Optional<T> getItem(int index) {
        if (index < 0 || index >= items.size()) {
            return Optional.empty();
        }
        return Optional.of(items.get(index));
    }

    public <R> List<R> processItems(List<? extends T> input,
                                   Function<? super T, ? extends R> processor) {
        return input.stream()
                   .map(processor)
                   .collect(Collectors.toList());
    }

    // Advanced: Builder pattern with generics
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final List<T> items = new ArrayList<>();

        public Builder<T> add(T item) {
            items.add(item);
            return this;
        }

        public Builder<T> addAll(Collection<? extends T> items) {
            this.items.addAll(items);
            return this;
        }

        public GenericDataProcessor<T> build() {
            GenericDataProcessor<T> processor = new GenericDataProcessor<>();
            processor.items.addAll(this.items);
            return processor;
        }
    }
}

// Usage examples demonstrating type safety
public class GenericUsageExamples {
    public void demonstrateTypeSafety() {
        // String processor
        GenericDataProcessor<String> stringProcessor =
            GenericDataProcessor.<String>builder()
                .add("hello")
                .add("world")
                .build();

        List<String> upperCase = stringProcessor.processItems(
            Arrays.asList("java", "generics"),
            String::toUpperCase
        );

        // Integer processor with different transformation
        GenericDataProcessor<Integer> intProcessor =
            GenericDataProcessor.<Integer>builder()
                .add(1)
                .add(2)
                .build();

        List<String> stringified = intProcessor.processItems(
            Arrays.asList(10, 20, 30),
            Object::toString
        );
    }
}
```

#### 🔍 **Advanced Generics Patterns:**

**1. Self-Bounded Types (Curiously Recurring Template Pattern)**
```java
public abstract class Comparable<T extends Comparable<T>> {
    public abstract int compareTo(T other);
}

public class Person implements Comparable<Person> {
    @Override
    public int compareTo(Person other) {
        // Type-safe comparison
        return this.name.compareTo(other.name);
    }
}
```

**2. Type Tokens for Runtime Type Information**
```java
public class TypeSafeContainer {
    private final Map<Class<?>, Object> data = new HashMap<>();

    public <T> void put(Class<T> type, T instance) {
        data.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> type) {
        return Optional.ofNullable((T) data.get(type));
    }
}
```

---

### **Lesson 4.2: Functional Programming Excellence** (90 minutes)

#### 🎯 **Learning Objectives:**
- Apply functional programming principles using `@142-java-functional-programming`
- Master Stream API and lambda expressions
- Implement immutable data structures
- Use higher-order functions effectively

#### 📖 **Core Concepts:**

**Functional Programming Principles:**

1. **Immutability**: Prefer immutable objects
2. **Pure Functions**: No side effects, deterministic
3. **Higher-Order Functions**: Functions as parameters/return values
4. **Function Composition**: Combining simple functions
5. **Lazy Evaluation**: Compute only when needed

#### 🔧 **Hands-on Exercise 4.2:**

**Scenario:** Transform imperative data processing to functional style.

**Step 1: Imperative Style Code**
```java
public class OrderProcessor {
    public List<OrderSummary> processOrders(List<Order> orders) {
        List<OrderSummary> summaries = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.COMPLETED &&
                order.getTotal().compareTo(BigDecimal.valueOf(100)) > 0) {

                OrderSummary summary = new OrderSummary();
                summary.setOrderId(order.getId());
                summary.setCustomerName(order.getCustomer().getName());
                summary.setTotal(order.getTotal());
                summary.setDiscountedTotal(calculateDiscount(order.getTotal()));

                summaries.add(summary);
            }
        }

        // Sort by total descending
        summaries.sort((a, b) -> b.getTotal().compareTo(a.getTotal()));

        return summaries;
    }

    private BigDecimal calculateDiscount(BigDecimal total) {
        return total.multiply(BigDecimal.valueOf(0.9)); // 10% discount
    }
}
```

**Step 2: Apply Functional Programming System Prompt**
Use: `Improve the class/classes added in the context applying the system prompt @142-java-functional-programming`

**Expected Functional Transformation:**

```java
public class FunctionalOrderProcessor {

    private static final Predicate<Order> IS_COMPLETED =
        order -> order.getStatus() == OrderStatus.COMPLETED;

    private static final Predicate<Order> IS_HIGH_VALUE =
        order -> order.getTotal().compareTo(BigDecimal.valueOf(100)) > 0;

    private static final Function<BigDecimal, BigDecimal> APPLY_DISCOUNT =
        total -> total.multiply(BigDecimal.valueOf(0.9));

    private static final Function<Order, OrderSummary> TO_SUMMARY =
        order -> OrderSummary.builder()
            .orderId(order.getId())
            .customerName(order.getCustomer().getName())
            .total(order.getTotal())
            .discountedTotal(APPLY_DISCOUNT.apply(order.getTotal()))
            .build();

    private static final Comparator<OrderSummary> BY_TOTAL_DESC =
        Comparator.comparing(OrderSummary::getTotal).reversed();

    public List<OrderSummary> processOrders(List<Order> orders) {
        return orders.stream()
            .filter(IS_COMPLETED)
            .filter(IS_HIGH_VALUE)
            .map(TO_SUMMARY)
            .sorted(BY_TOTAL_DESC)
            .collect(Collectors.toUnmodifiableList());
    }

    // Advanced: Functional composition
    public Function<List<Order>, List<OrderSummary>> createProcessor(
            Predicate<Order> additionalFilter,
            Function<BigDecimal, BigDecimal> discountStrategy) {

        return orders -> orders.stream()
            .filter(IS_COMPLETED)
            .filter(IS_HIGH_VALUE)
            .filter(additionalFilter)
            .map(order -> TO_SUMMARY.compose(
                o -> o.withTotal(discountStrategy.apply(o.getTotal()))
            ).apply(order))
            .sorted(BY_TOTAL_DESC)
            .collect(Collectors.toUnmodifiableList());
    }
}
```

#### 💡 **Functional Benefits:**
- **Declarative**: Focus on what, not how
- **Composable**: Combine functions easily
- **Testable**: Pure functions are easy to test
- **Parallel**: Stream operations can be parallelized
- **Readable**: Intent is clear from function names

---

### **Lesson 4.3: Functional Exception Handling** (75 minutes)

#### 🎯 **Learning Objectives:**
- Implement monadic error handling using `@143-java-functional-exception-handling`
- Master Optional and Either types
- Apply Railway-Oriented Programming
- Eliminate null pointer exceptions

#### 📖 **Core Concepts:**

**Functional Error Handling:**

1. **Optional**: Handle absence of values
2. **Either**: Handle success/failure scenarios
3. **Try**: Exception handling as values
4. **Railway-Oriented Programming**: Chain operations safely
5. **Monadic Composition**: Flatmap for chaining

#### 🔧 **Hands-on Exercise 4.3:**

**Step 1: Traditional Exception-Based Code**
```java
public class UserService {
    public User getUserProfile(String userId) throws UserNotFoundException,
                                                   DatabaseException,
                                                   ValidationException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new ValidationException("User ID cannot be empty");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }

        return user;
    }

    public UserProfile enrichUserProfile(User user) throws ExternalServiceException {
        ExternalUserData externalData = externalService.getUserData(user.getId());
        if (externalData == null) {
            throw new ExternalServiceException("Failed to fetch external data");
        }

        return new UserProfile(user, externalData);
    }
}
```

**Step 2: Apply Functional Exception Handling**
Use: `Improve the class/classes added in the context applying the system prompt @143-java-functional-exception-handling`

**Expected Monadic Transformation:**

```java
public class FunctionalUserService {

    public Either<UserError, User> getUserProfile(String userId) {
        return validateUserId(userId)
            .flatMap(this::findUser);
    }

    public Either<UserError, UserProfile> enrichUserProfile(String userId) {
        return getUserProfile(userId)
            .flatMap(this::fetchExternalData)
            .map(this::createUserProfile);
    }

    // Railway-Oriented Programming: chain operations safely
    public Either<UserError, UserProfile> getCompleteUserProfile(String userId) {
        return validateUserId(userId)
            .flatMap(this::findUser)
            .flatMap(this::fetchExternalData)
            .map(this::createUserProfile)
            .mapLeft(this::logError); // Log errors without breaking the chain
    }

    private Either<UserError, String> validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Either.left(UserError.VALIDATION_ERROR("User ID cannot be empty"));
        }
        return Either.right(userId.trim());
    }

    private Either<UserError, User> findUser(String userId) {
        return Try.of(() -> userRepository.findById(userId))
            .toEither()
            .mapLeft(throwable -> UserError.DATABASE_ERROR("Database error: " + throwable.getMessage()))
            .flatMap(optionalUser -> optionalUser
                .map(Either::<UserError, User>right)
                .orElse(Either.left(UserError.NOT_FOUND("User not found: " + userId))));
    }

    private Either<UserError, UserWithExternalData> fetchExternalData(User user) {
        return Try.of(() -> externalService.getUserData(user.getId()))
            .toEither()
            .mapLeft(throwable -> UserError.EXTERNAL_SERVICE_ERROR("External service error: " + throwable.getMessage()))
            .map(externalData -> new UserWithExternalData(user, externalData));
    }

    private UserProfile createUserProfile(UserWithExternalData data) {
        return UserProfile.builder()
            .user(data.user())
            .externalData(data.externalData())
            .build();
    }

    private UserError logError(UserError error) {
        logger.warn("User service error: {}", error.getMessage());
        return error;
    }
}

// Error type hierarchy
public sealed interface UserError permits
    UserError.ValidationError,
    UserError.NotFoundError,
    UserError.DatabaseError,
    UserError.ExternalServiceError {

    String getMessage();

    record ValidationError(String message) implements UserError {
        @Override
        public String getMessage() { return message; }
    }

    record NotFoundError(String message) implements UserError {
        @Override
        public String getMessage() { return message; }
    }

    record DatabaseError(String message) implements UserError {
        @Override
        public String getMessage() { return message; }
    }

    record ExternalServiceError(String message) implements UserError {
        @Override
        public String getMessage() { return message; }
    }

    static UserError VALIDATION_ERROR(String message) { return new ValidationError(message); }
    static UserError NOT_FOUND(String message) { return new NotFoundError(message); }
    static UserError DATABASE_ERROR(String message) { return new DatabaseError(message); }
    static UserError EXTERNAL_SERVICE_ERROR(String message) { return new ExternalServiceError(message); }
}
```

#### 💡 **Monadic Benefits:**
- **No Exceptions**: Errors are values, not control flow
- **Composable**: Chain operations without nested try-catch
- **Type Safe**: Compiler enforces error handling
- **Testable**: Easy to test success and failure paths
- **Readable**: Intent is clear from types

---

### **Lesson 4.4: Data-Oriented Programming** (75 minutes)

#### 🎯 **Learning Objectives:**
- Leverage records and sealed types using `@144-java-data-oriented-programming`
- Model domain data effectively
- Implement pattern matching
- Create immutable data structures

#### 📖 **Core Concepts:**

**Data-Oriented Programming Principles:**

1. **Records**: Immutable data carriers
2. **Sealed Types**: Controlled inheritance
3. **Pattern Matching**: Structural matching (Java 17+)
4. **Algebraic Data Types**: Sum and product types
5. **Immutability**: Prefer immutable structures

#### 🔧 **Hands-on Exercise 4.4:**

**Step 1: Traditional Class-Heavy Design**
```java
public class PaymentMethod {
    private String type;
    private String cardNumber;
    private String bankAccount;
    private String digitalWallet;

    // Complex constructor logic
    // Lots of null checks
    // Mutable state issues
}
```

**Step 2: Apply Data-Oriented Programming**
Use: `Improve the class/classes added in the context applying the system prompt @144-java-data-oriented-programming`

**Expected Data-Oriented Design:**

```java
// Sealed interface for payment methods
public sealed interface PaymentMethod
    permits CreditCard, BankTransfer, DigitalWallet {

    Money getAmount();

    // Pattern matching method (Java 17+)
    default String getDisplayName() {
        return switch (this) {
            case CreditCard(var number, var amount) ->
                "Credit Card ending in " + number.substring(number.length() - 4);
            case BankTransfer(var account, var amount) ->
                "Bank Transfer from " + account.getBankName();
            case DigitalWallet(var provider, var amount) ->
                provider + " Wallet";
        };
    }

    default PaymentProcessingFee calculateFee() {
        return switch (this) {
            case CreditCard cc -> PaymentProcessingFee.percentage(cc.amount(), 2.9);
            case BankTransfer bt -> PaymentProcessingFee.fixed(Money.of(1.50));
            case DigitalWallet dw -> PaymentProcessingFee.percentage(dw.amount(), 1.5);
        };
    }
}

// Record implementations
public record CreditCard(
    CardNumber number,
    Money amount,
    ExpiryDate expiry
) implements PaymentMethod {

    public CreditCard {
        Objects.requireNonNull(number, "Card number cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(expiry, "Expiry date cannot be null");

        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (expiry.isExpired()) {
            throw new IllegalArgumentException("Card is expired");
        }
    }

    @Override
    public Money getAmount() {
        return amount;
    }
}

public record BankTransfer(
    BankAccount account,
    Money amount
) implements PaymentMethod {

    public BankTransfer {
        Objects.requireNonNull(account, "Bank account cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public Money getAmount() {
        return amount;
    }
}

public record DigitalWallet(
    WalletProvider provider,
    Money amount,
    WalletId walletId
) implements PaymentMethod {

    @Override
    public Money getAmount() {
        return amount;
    }
}

// Value objects as records
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public boolean isNegativeOrZero() {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount), Currency.getInstance("USD"));
    }
}

// Usage with pattern matching
public class PaymentProcessor {

    public ProcessingResult process(PaymentMethod payment) {
        return switch (payment) {
            case CreditCard(var number, var amount, var expiry) -> {
                validateCreditCard(number, expiry);
                yield processCreditCardPayment(number, amount);
            }
            case BankTransfer(var account, var amount) -> {
                validateBankAccount(account);
                yield processBankTransfer(account, amount);
            }
            case DigitalWallet(var provider, var amount, var walletId) -> {
                validateWallet(provider, walletId);
                yield processDigitalWallet(provider, amount, walletId);
            }
        };
    }
}
```

#### 💡 **Data-Oriented Benefits:**
- **Immutability**: Thread-safe by default
- **Pattern Matching**: Exhaustive case handling
- **Type Safety**: Impossible states are unrepresentable
- **Conciseness**: Less boilerplate code
- **Performance**: Efficient memory layout

---

## 🏆 Module Assessment

### **Knowledge Validation Checkpoint**

**Question 1:** What is the PECS principle in Java generics?

**Question 2:** How does functional exception handling with Either improve upon traditional try-catch?

**Question 3:** What are the benefits of sealed interfaces over regular inheritance?

**Question 4:** Why are records preferable to traditional classes for data carriers?

### **Practical Assessment Project**

**Project: "Modern E-Commerce Order Processing System"**

**Scenario:** Build a modern order processing system using all advanced Java features learned.

**Requirements:**
1. Generic order processing pipeline with type safety
2. Functional data transformations using streams
3. Monadic error handling with Either types
4. Data-oriented domain model with records and sealed types
5. Pattern matching for order state transitions

**Deliverables:**
- Type-safe generic order processor
- Functional pipeline for order transformations
- Monadic error handling throughout
- Complete domain model using records and sealed types
- Comprehensive test suite demonstrating all patterns

**Success Criteria:**
- No raw types or unchecked warnings
- Functional style with no mutable state
- All errors handled monadically
- Domain model uses modern Java features effectively
- Code is expressive and maintainable

### **Time Investment:**
- **Design & Planning**: 1 hour
- **Generic Implementation**: 2 hours
- **Functional & Monadic Patterns**: 2 hours
- **Data-Oriented Modeling**: 1.5 hours
- **Testing & Validation**: 1.5 hours
- **Total**: 8 hours

---

## 🚀 Next Steps

**Exceptional Progress!** You've mastered modern Java's most powerful features and programming paradigms.

**What You've Accomplished:**
- ✅ Mastered generics from basics to advanced patterns
- ✅ Applied functional programming principles
- ✅ Implemented monadic error handling
- ✅ Leveraged data-oriented programming

**Ready for performance optimization?**

👉 **[Continue to Module 5: Performance →](module-5-performance.html)**

**In Module 5, you'll learn to:**
- Profile applications with async-profiler
- Create JMeter performance tests
- Benchmark code with JMH
- Optimize performance systematically

---

## 📚 Additional Resources

- **[Java Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)**
- **[Functional Programming in Java](https://www.amazon.com/Functional-Programming-Java-Harnessing-Expressions/dp/1937785467)**
- **[VAVR Library Documentation](https://www.vavr.io/)**
- **[Pattern Matching in Java](https://openjdk.java.net/jeps/394)**

---

*Transform your applications with performance optimization and profiling techniques in the next module.*

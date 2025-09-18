title=Module 5: Assessment - Validate Your Mastery
type=course-module
status=published
date=2025-09-13
author=MyRobot
module=5
duration=1-2 hours
difficulty=Comprehensive
tags=java, generics, assessment, validation
~~~~~~

## 📖 Module Overview

Congratulations on reaching the final module! It's time to validate your Java Generics mastery through comprehensive challenges and code reviews. This module ensures you can apply generics effectively in real-world scenarios.

### 🎯 Learning Objectives

By the end of this module, you will have:

- **Demonstrated** comprehensive understanding through coding challenges
- **Applied** all generics concepts in integrated scenarios
- **Reviewed** and critiqued generic code like a senior developer
- **Planned** your continued learning journey
- **Built** a portfolio of generic programming examples

### ⏱️ Estimated Time: 1-2 hours

---

## 🎯 Comprehensive Coding Challenges

### 🏆 Challenge 1: Generic Data Processing Pipeline

**Scenario**: Build a type-safe data processing pipeline that can transform, filter, and aggregate data of different types.

**Requirements**:
- Use PECS principle correctly
- Handle errors with Result types
- Support parallel processing
- Include comprehensive type safety
- Integrate with modern Java features

```java
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Collectors;

// TODO: Complete the generic data processing pipeline
public class DataProcessingPipeline<T> {

    // TODO: Implement pipeline stages with proper generics
    public static class Pipeline<T> {

        // TODO: Add transformation stage
        public <R> Pipeline<R> transform(Function<? super T, ? extends R> transformer) {

        }

        // TODO: Add filtering stage
        public Pipeline<T> filter(Predicate<? super T> predicate) {

        }

        // TODO: Add error handling with Result types
        public <E> Pipeline<Result<T, E>> handleErrors(Function<Exception, E> errorMapper) {

        }

        // TODO: Add parallel processing support
        public Pipeline<T> parallel() {

        }

        // TODO: Add aggregation operations
        public <R> R aggregate(Collector<? super T, ?, R> collector) {

        }

        // TODO: Execute pipeline and return results
        public List<T> execute() {

        }
    }

    // TODO: Factory method to create pipeline
    public static <T> Pipeline<T> of(Collection<? extends T> data) {

    }

    // TODO: Result type for error handling
    public sealed interface Result<T, E> permits Success, Failure {
        record Success<T, E>(T value) implements Result<T, E> {}
        record Failure<T, E>(E error) implements Result<T, E> {}
    }
}
```

### 💡 Challenge 1 Solution

<details>
<summary>🎯 Try implementing the complete pipeline yourself first</summary>

```java
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataProcessingPipeline<T> {

    public static class Pipeline<T> {
        private final Stream<T> stream;
        private final boolean isParallel;

        private Pipeline(Stream<T> stream, boolean isParallel) {
            this.stream = stream;
            this.isParallel = isParallel;
        }

        // Transform with proper PECS
        public <R> Pipeline<R> transform(Function<? super T, ? extends R> transformer) {
            Stream<R> transformedStream = stream.map(transformer);
            return new Pipeline<>(transformedStream, isParallel);
        }

        // Filter with PECS
        public Pipeline<T> filter(Predicate<? super T> predicate) {
            Stream<T> filteredStream = stream.filter(predicate);
            return new Pipeline<>(filteredStream, isParallel);
        }

        // Error handling with Result types
        public <E> Pipeline<Result<T, E>> handleErrors(Function<Exception, E> errorMapper) {
            Stream<Result<T, E>> resultStream = stream.map(item -> {
                try {
                    return new Result.Success<T, E>(item);
                } catch (Exception e) {
                    return new Result.Failure<T, E>(errorMapper.apply(e));
                }
            });
            return new Pipeline<>(resultStream, isParallel);
        }

        // Enable parallel processing
        public Pipeline<T> parallel() {
            return new Pipeline<>(stream.parallel(), true);
        }

        // Batch processing
        public Pipeline<List<T>> batch(int batchSize) {
            List<T> items = stream.collect(Collectors.toList());
            Stream<List<T>> batchStream = Stream.iterate(0, i -> i + batchSize)
                .limit((items.size() + batchSize - 1) / batchSize)
                .map(i -> items.subList(i, Math.min(i + batchSize, items.size())));

            return new Pipeline<>(batchStream, isParallel);
        }

        // Aggregation with proper collector types
        public <R> R aggregate(Collector<? super T, ?, R> collector) {
            return stream.collect(collector);
        }

        // Execute and collect results
        public List<T> execute() {
            return stream.collect(Collectors.toList());
        }

        // Execute asynchronously
        public CompletableFuture<List<T>> executeAsync() {
            return CompletableFuture.supplyAsync(() -> execute(), ForkJoinPool.commonPool());
        }

        // Count elements
        public long count() {
            return stream.count();
        }

        // Find first element
        public Optional<T> findFirst() {
            return stream.findFirst();
        }

        // Reduce with proper types
        public Optional<T> reduce(BinaryOperator<T> accumulator) {
            return stream.reduce(accumulator);
        }

        // Group by with proper types
        public <K> Pipeline<Map<K, List<T>>> groupBy(Function<? super T, ? extends K> classifier) {
            Map<K, List<T>> grouped = stream.collect(Collectors.groupingBy(classifier));
            return new Pipeline<>(Stream.of(grouped), isParallel);
        }
    }

    // Factory method with proper bounds
    public static <T> Pipeline<T> of(Collection<? extends T> data) {
        return new Pipeline<>(data.stream(), false);
    }

    @SafeVarargs
    public static <T> Pipeline<T> of(T... items) {
        return new Pipeline<>(Arrays.stream(items), false);
    }

    // Result type with pattern matching support
    public sealed interface Result<T, E> permits Result.Success, Result.Failure {

        record Success<T, E>(T value) implements Result<T, E> {
            public <R> R fold(Function<T, R> onSuccess, Function<E, R> onFailure) {
                return onSuccess.apply(value);
            }
        }

        record Failure<T, E>(E error) implements Result<T, E> {
            public <R> R fold(Function<T, R> onSuccess, Function<E, R> onFailure) {
                return onFailure.apply(error);
            }
        }

        // Utility methods
        default boolean isSuccess() {
            return this instanceof Success;
        }

        default boolean isFailure() {
            return this instanceof Failure;
        }

        default <R> Result<R, E> map(Function<T, R> mapper) {
            return switch (this) {
                case Success<T, E> success -> new Success<>(mapper.apply(success.value()));
                case Failure<T, E> failure -> new Failure<>(failure.error());
            };
        }

        default T orElse(T defaultValue) {
            return switch (this) {
                case Success<T, E> success -> success.value();
                case Failure<T, E> failure -> defaultValue;
            };
        }
    }
}

// Comprehensive test
class DataProcessingPipelineTest {
    public static void main(String[] args) {
        // Test basic pipeline
        List<String> words = List.of("hello", "world", "java", "generics", "pipeline", "test");

        List<Integer> lengths = DataProcessingPipeline.of(words)
            .filter(s -> s.length() > 4)
            .transform(String::length)
            .execute();

        System.out.println("Word lengths > 4: " + lengths);

        // Test parallel processing
        List<Integer> numbers = java.util.stream.IntStream.range(1, 1000)
            .boxed()
            .collect(Collectors.toList());

        long sum = DataProcessingPipeline.of(numbers)
            .parallel()
            .filter(n -> n % 2 == 0)
            .transform(n -> n * n)
            .aggregate(Collectors.summingLong(Integer::longValue));

        System.out.println("Sum of squares of even numbers: " + sum);

        // Test error handling
        List<String> mixedData = List.of("123", "456", "abc", "789");

        List<DataProcessingPipeline.Result<Integer, String>> results =
            DataProcessingPipeline.of(mixedData)
                .transform(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid number: " + s);
                    }
                })
                .handleErrors(ex -> ex.getMessage())
                .execute();

        System.out.println("Parsing results:");
        results.forEach(result -> {
            String output = result.fold(
                success -> "Success: " + success,
                error -> "Error: " + error
            );
            System.out.println("  " + output);
        });

        // Test grouping
        List<String> animals = List.of("cat", "dog", "elephant", "ant", "bear", "eagle");

        Map<Integer, List<String>> groupedByLength = DataProcessingPipeline.of(animals)
            .groupBy(String::length)
            .findFirst()
            .orElse(Map.of());

        System.out.println("Animals grouped by length: " + groupedByLength);

        // Test async execution
        try {
            List<String> asyncResult = DataProcessingPipeline.of(words)
                .transform(String::toUpperCase)
                .executeAsync()
                .get();

            System.out.println("Async result: " + asyncResult);
        } catch (Exception e) {
            System.err.println("Async execution failed: " + e.getMessage());
        }
    }
}
```

**✅ Assessment Criteria Met:**
- ✅ **PECS Applied**: Correct use of extends/super in method parameters
- ✅ **Type Safety**: No raw types or unsafe casts
- ✅ **Modern Features**: Records, sealed types, pattern matching
- ✅ **Error Handling**: Type-safe Result pattern
- ✅ **Performance**: Parallel processing support
- ✅ **Flexibility**: Multiple operation types and chaining

</details>

---

### 🏆 Challenge 2: Generic Repository Pattern

**Scenario**: Create a type-safe repository pattern that works with different entity types and supports various query operations.

**Requirements**:
- Generic CRUD operations
- Type-safe query building
- Specification pattern with generics
- Transaction support
- Caching with type safety

```java
// TODO: Implement comprehensive generic repository pattern
public interface Repository<T, ID> {

    // TODO: Basic CRUD operations with proper generics

    // TODO: Query methods with specifications

    // TODO: Pagination support

    // TODO: Transaction support

    // TODO: Caching integration
}

// TODO: Specification pattern for type-safe queries
public interface Specification<T> {

}

// TODO: Entity base class or interface
public interface Entity<ID> {

}
```

### 💡 Challenge 2 Solution

<details>
<summary>🎯 Try implementing the repository pattern yourself first</summary>

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Generic repository interface
public interface Repository<T extends Entity<ID>, ID> {

    // Basic CRUD operations
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void delete(T entity);
    boolean existsById(ID id);
    long count();

    // Query operations
    List<T> findAll(Specification<T> spec);
    Optional<T> findOne(Specification<T> spec);
    Page<T> findAll(Specification<T> spec, Pageable pageable);
    long count(Specification<T> spec);

    // Batch operations
    List<T> saveAll(Iterable<? extends T> entities);
    void deleteAll(Iterable<? extends T> entities);
    void deleteAll();
}

// Entity interface with generic ID
public interface Entity<ID> {
    ID getId();
    void setId(ID id);
}

// Specification interface for type-safe queries
@FunctionalInterface
public interface Specification<T> {
    boolean test(T entity);

    // Combinators
    default Specification<T> and(Specification<T> other) {
        return entity -> this.test(entity) && other.test(entity);
    }

    default Specification<T> or(Specification<T> other) {
        return entity -> this.test(entity) || other.test(entity);
    }

    default Specification<T> not() {
        return entity -> !this.test(entity);
    }

    // Utility factory methods
    static <T> Specification<T> where(Predicate<T> predicate) {
        return predicate::test;
    }

    static <T> Specification<T> alwaysTrue() {
        return entity -> true;
    }

    static <T> Specification<T> alwaysFalse() {
        return entity -> false;
    }
}

// Pagination support
public record Pageable(int page, int size, Sort sort) {
    public Pageable(int page, int size) {
        this(page, size, Sort.unsorted());
    }

    public int getOffset() {
        return page * size;
    }
}

public record Sort(List<Order> orders) {
    public static Sort by(String... properties) {
        return new Sort(Arrays.stream(properties)
            .map(prop -> new Order(Direction.ASC, prop))
            .collect(Collectors.toList()));
    }

    public static Sort by(Direction direction, String... properties) {
        return new Sort(Arrays.stream(properties)
            .map(prop -> new Order(direction, prop))
            .collect(Collectors.toList()));
    }

    public static Sort unsorted() {
        return new Sort(List.of());
    }

    public enum Direction { ASC, DESC }

    public record Order(Direction direction, String property) {}
}

public record Page<T>(
    List<T> content,
    int number,
    int size,
    long totalElements,
    int totalPages
) {
    public boolean hasNext() {
        return number < totalPages - 1;
    }

    public boolean hasPrevious() {
        return number > 0;
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        return new Page<>(
            content.stream().map(mapper).collect(Collectors.toList()),
            number,
            size,
            totalElements,
            totalPages
        );
    }
}

// In-memory implementation for demonstration
public class InMemoryRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {

    private final Map<ID, T> storage = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<ID, Object>> cache = new ConcurrentHashMap<>();
    private final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");

        ID id = idExtractor.apply(entity);
        if (id == null) {
            // Generate ID for new entities (simplified)
            id = generateId();
            entity.setId(id);
        }

        storage.put(id, entity);
        invalidateCache(entity.getClass());
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(ID id) {
        T removed = storage.remove(id);
        if (removed != null) {
            invalidateCache(removed.getClass());
        }
    }

    @Override
    public void delete(T entity) {
        deleteById(idExtractor.apply(entity));
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return storage.values().stream()
            .filter(spec::test)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        return storage.values().stream()
            .filter(spec::test)
            .findFirst();
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        List<T> allMatching = findAll(spec);

        // Apply sorting
        if (!pageable.sort().orders().isEmpty()) {
            allMatching = applySorting(allMatching, pageable.sort());
        }

        // Apply pagination
        int start = pageable.getOffset();
        int end = Math.min(start + pageable.size(), allMatching.size());
        List<T> pageContent = allMatching.subList(start, end);

        int totalPages = (int) Math.ceil((double) allMatching.size() / pageable.size());

        return new Page<>(pageContent, pageable.page(), pageable.size(),
                         allMatching.size(), totalPages);
    }

    @Override
    public long count(Specification<T> spec) {
        return storage.values().stream()
            .filter(spec::test)
            .count();
    }

    @Override
    public List<T> saveAll(Iterable<? extends T> entities) {
        List<T> saved = new ArrayList<>();
        for (T entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        storage.clear();
        cache.clear();
    }

    // Helper methods
    @SuppressWarnings("unchecked")
    private ID generateId() {
        // Simplified ID generation
        return (ID) UUID.randomUUID().toString();
    }

    private void invalidateCache(Class<?> entityClass) {
        cache.remove(entityClass);
    }

    private List<T> applySorting(List<T> entities, Sort sort) {
        // Simplified sorting implementation
        return entities.stream()
            .sorted((e1, e2) -> {
                for (Sort.Order order : sort.orders()) {
                    // This would need reflection in real implementation
                    int comparison = 0; // Simplified
                    if (comparison != 0) {
                        return order.direction() == Sort.Direction.ASC ? comparison : -comparison;
                    }
                }
                return 0;
            })
            .collect(Collectors.toList());
    }
}

// Example entities for testing
record User(String id, String name, String email, int age) implements Entity<String> {
    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) {
        // Records are immutable, so this would need to return new instance
        // Simplified for demo
    }
}

record Product(String id, String name, double price, String category) implements Entity<String> {
    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) {
        // Records are immutable, simplified for demo
    }
}

// Specification examples
class UserSpecifications {
    public static Specification<User> hasName(String name) {
        return user -> Objects.equals(user.name(), name);
    }

    public static Specification<User> emailContains(String text) {
        return user -> user.email().contains(text);
    }

    public static Specification<User> ageGreaterThan(int age) {
        return user -> user.age() > age;
    }

    public static Specification<User> ageBetween(int minAge, int maxAge) {
        return user -> user.age() >= minAge && user.age() <= maxAge;
    }
}

// Comprehensive test
class RepositoryPatternTest {
    public static void main(String[] args) {
        Repository<User, String> userRepository = new InMemoryRepository<>(User::id);

        // Save users
        List<User> users = List.of(
            new User("1", "John Doe", "john@example.com", 30),
            new User("2", "Jane Smith", "jane@example.com", 25),
            new User("3", "Bob Johnson", "bob@example.com", 35),
            new User("4", "Alice Brown", "alice@example.com", 28)
        );

        userRepository.saveAll(users);

        System.out.println("Total users: " + userRepository.count());

        // Find by ID
        Optional<User> user = userRepository.findById("1");
        System.out.println("User 1: " + user);

        // Find with specifications
        List<User> youngUsers = userRepository.findAll(
            UserSpecifications.ageBetween(20, 30)
        );
        System.out.println("Young users (20-30): " + youngUsers);

        // Complex specification
        List<User> complexQuery = userRepository.findAll(
            UserSpecifications.ageGreaterThan(25)
                .and(UserSpecifications.emailContains("@example.com"))
                .and(UserSpecifications.hasName("John Doe").not())
        );
        System.out.println("Complex query result: " + complexQuery);

        // Pagination
        Pageable pageable = new Pageable(0, 2, Sort.by("name"));
        Page<User> page = userRepository.findAll(
            Specification.alwaysTrue(),
            pageable
        );

        System.out.println("Page content: " + page.content());
        System.out.println("Total pages: " + page.totalPages());
        System.out.println("Has next: " + page.hasNext());

        // Count with specification
        long adultCount = userRepository.count(UserSpecifications.ageGreaterThan(18));
        System.out.println("Adult users: " + adultCount);

        // Transform page content
        Page<String> nameOnlyPage = page.map(User::name);
        System.out.println("Names only: " + nameOnlyPage.content());
    }
}
```

**✅ Assessment Criteria Met:**
- ✅ **Generic Design**: Repository works with any Entity type
- ✅ **Type Safety**: All operations are type-safe
- ✅ **Specification Pattern**: Type-safe query building
- ✅ **PECS Applied**: Proper variance in method signatures
- ✅ **Modern Features**: Records for immutable data
- ✅ **Comprehensive API**: CRUD, queries, pagination, batch operations

</details>

---

## 🔍 Code Review Challenges

### 📝 Review Challenge 1: Identify Issues

Review this code and identify all generics-related issues:

```java
// Code to review - find all the issues!
import java.util.*;

public class ProblematicGenericCode {

    // Issue 1: ?
    private List items = new ArrayList();
    private Map cache = new HashMap();

    // Issue 2: ?
    public void addItem(Object item) {
        items.add(item);
    }

    // Issue 3: ?
    public Object getItem(int index) {
        return items.get(index);
    }

    // Issue 4: ?
    public static List merge(List list1, List list2) {
        List result = new ArrayList();
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    // Issue 5: ?
    public static <T> void swap(List<T> list, int i, int j) {
        Object temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Issue 6: ?
    public static <T> T[] toArray(List<T> list) {
        return (T[]) list.toArray();
    }

    // Issue 7: ?
    @SafeVarargs
    public static <T> void processItems(List<T>... lists) {
        Object[] array = lists;
        array[0] = Arrays.asList("danger");
    }
}
```

### 💡 Code Review Solutions

<details>
<summary>🔍 Try identifying all issues yourself first</summary>

**Issues Identified:**

1. **Raw Types (Lines 6-7)**:
   ```java
   // ❌ Problem
   private List items = new ArrayList();
   private Map cache = new HashMap();

   // ✅ Solution
   private List<SomeType> items = new ArrayList<>();
   private Map<KeyType, ValueType> cache = new HashMap<>();
   ```

2. **Non-generic Method Parameter (Line 10)**:
   ```java
   // ❌ Problem
   public void addItem(Object item) {

   // ✅ Solution
   public void addItem(T item) { // Make class generic or use specific type
   ```

3. **Non-generic Return Type (Line 14)**:
   ```java
   // ❌ Problem
   public Object getItem(int index) {

   // ✅ Solution
   public T getItem(int index) {
       return items.get(index);
   }
   ```

4. **Raw Types in Method Signature (Line 18)**:
   ```java
   // ❌ Problem
   public static List merge(List list1, List list2) {

   // ✅ Solution
   public static <T> List<T> merge(List<? extends T> list1, List<? extends T> list2) {
       List<T> result = new ArrayList<>();
       result.addAll(list1);
       result.addAll(list2);
       return result;
   }
   ```

5. **Unsafe Cast in Generic Method (Line 26)**:
   ```java
   // ❌ Problem
   Object temp = list.get(i);
   list.set(j, temp); // Cannot assign Object to T

   // ✅ Solution
   T temp = list.get(i);
   list.set(i, list.get(j));
   list.set(j, temp);
   ```

6. **Unsafe Array Cast (Line 32)**:
   ```java
   // ❌ Problem
   return (T[]) list.toArray(); // ClassCastException at runtime

   // ✅ Solution
   @SuppressWarnings("unchecked")
   public static <T> T[] toArray(List<T> list, Class<T> componentType) {
       return list.toArray((T[]) Array.newInstance(componentType, list.size()));
   }
   ```

7. **Misused @SafeVarargs (Line 36)**:
   ```java
   // ❌ Problem - method is NOT safe, causes heap pollution
   @SafeVarargs
   public static <T> void processItems(List<T>... lists) {
       Object[] array = lists;
       array[0] = Arrays.asList("danger"); // Heap pollution!
   }

   // ✅ Solution - Remove @SafeVarargs or make method actually safe
   public static <T> void processItems(List<T>... lists) {
       // Only read from lists, don't store or modify the array
       for (List<T> list : lists) {
           // Process each list safely
       }
   }
   ```

**Summary**: 7 critical issues found - raw types, unsafe casts, improper variance, and misused annotations.

</details>

---

### 📝 Review Challenge 2: API Design Critique

Review this API design and suggest improvements:

```java
// API to review and improve
public class DataProcessor {

    public static List processData(List data, Function transformer) {
        // Implementation
    }

    public static void validateItems(Collection items, Validator validator) {
        // Implementation
    }

    public static Map groupItems(List items) {
        // Implementation
    }

    public static Object findFirst(List items, Predicate condition) {
        // Implementation
    }
}
```

### 💡 API Design Improvements

<details>
<summary>🎨 Try redesigning the API yourself first</summary>

**Improved API Design:**

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class DataProcessor {

    // ✅ Improved: Generic with proper PECS
    public static <T, R> List<R> processData(
        List<? extends T> data,                    // Producer - extends
        Function<? super T, ? extends R> transformer  // Flexible function types
    ) {
        return data.stream()
                  .map(transformer)
                  .collect(Collectors.toList());
    }

    // ✅ Improved: Generic with validation result
    public static <T> ValidationResult<List<T>> validateItems(
        Collection<? extends T> items,             // Producer - extends
        Predicate<? super T> validator             // Flexible predicate
    ) {
        List<String> errors = new ArrayList<>();
        List<T> validItems = new ArrayList<>();

        for (T item : items) {
            if (validator.test(item)) {
                validItems.add(item);
            } else {
                errors.add("Invalid item: " + item);
            }
        }

        return errors.isEmpty()
            ? ValidationResult.valid(validItems)
            : ValidationResult.invalid(errors);
    }

    // ✅ Improved: Generic with flexible key extraction
    public static <T, K> Map<K, List<T>> groupItems(
        Collection<? extends T> items,             // Producer - extends
        Function<? super T, ? extends K> keyExtractor  // Flexible key function
    ) {
        return items.stream()
                   .collect(Collectors.groupingBy(keyExtractor));
    }

    // ✅ Improved: Generic with Optional return
    public static <T> Optional<T> findFirst(
        Collection<? extends T> items,             // Producer - extends
        Predicate<? super T> condition             // Flexible predicate
    ) {
        return items.stream()
                   .filter(condition)
                   .findFirst();
    }

    // ✅ Bonus: Additional utility methods with proper generics
    public static <T> List<T> filterItems(
        Collection<? extends T> items,
        Predicate<? super T> filter
    ) {
        return items.stream()
                   .filter(filter)
                   .collect(Collectors.toList());
    }

    public static <T, R> List<R> flatMapItems(
        Collection<? extends T> items,
        Function<? super T, ? extends Collection<? extends R>> mapper
    ) {
        return items.stream()
                   .flatMap(item -> mapper.apply(item).stream())
                   .collect(Collectors.toList());
    }

    // ✅ Type-safe validation result
    public sealed interface ValidationResult<T>
        permits ValidationResult.Valid, ValidationResult.Invalid {

        record Valid<T>(T value) implements ValidationResult<T> {}
        record Invalid<T>(List<String> errors) implements ValidationResult<T> {}

        static <T> ValidationResult<T> valid(T value) {
            return new Valid<>(value);
        }

        static <T> ValidationResult<T> invalid(List<String> errors) {
            return new Invalid<>(errors);
        }

        default boolean isValid() {
            return this instanceof Valid;
        }

        default T orElseThrow() {
            return switch (this) {
                case Valid<T> valid -> valid.value();
                case Invalid<T> invalid ->
                    throw new IllegalStateException("Validation failed: " +
                        String.join(", ", invalid.errors()));
            };
        }
    }
}

// Usage examples demonstrating improved API
class ImprovedAPITest {
    public static void main(String[] args) {
        List<String> words = List.of("hello", "world", "java", "generics");

        // Process data with type safety
        List<Integer> lengths = DataProcessor.processData(words, String::length);
        System.out.println("Lengths: " + lengths);

        // Validate with proper result handling
        DataProcessor.ValidationResult<List<String>> validation =
            DataProcessor.validateItems(words, s -> s.length() > 3);

        if (validation.isValid()) {
            System.out.println("Valid words: " + validation.orElseThrow());
        }

        // Group with type-safe key extraction
        Map<Integer, List<String>> grouped =
            DataProcessor.groupItems(words, String::length);
        System.out.println("Grouped by length: " + grouped);

        // Find with Optional return
        Optional<String> longWord =
            DataProcessor.findFirst(words, s -> s.length() > 5);
        System.out.println("Long word: " + longWord.orElse("none"));

        // Works with different types
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<String> numberStrings = DataProcessor.processData(numbers, Object::toString);
        System.out.println("Number strings: " + numberStrings);
    }
}
```

**Key Improvements:**
1. ✅ **Added generics** to all methods
2. ✅ **Applied PECS principle** correctly
3. ✅ **Used Optional** instead of nullable Object
4. ✅ **Added ValidationResult** for proper error handling
5. ✅ **Flexible function types** with proper variance
6. ✅ **Type-safe return types** throughout
7. ✅ **Modern Java features** (sealed interfaces, records)

</details>

---

## 🏆 Final Assessment Exam

### 📋 Comprehensive Assessment

**Instructions**: Complete all sections

#### Section A: Multiple Choice (5 questions)

1. **Which declaration correctly applies the PECS principle?**
   ```java
   a) public static <T> void copy(List<T> dest, List<T> src)
   b) public static <T> void copy(List<? super T> dest, List<? extends T> src)
   c) public static <T> void copy(List<? extends T> dest, List<? super T> src)
   d) public static <T> void copy(List<?> dest, List<?> src)
   ```

2. **What happens at runtime with this code?**
   ```java
   List<String> strings = new ArrayList<>();
   List<Integer> integers = new ArrayList<>();
   System.out.println(strings.getClass() == integers.getClass());
   ```
   ```
   a) Compilation error
   b) Prints false
   c) Prints true
   d) ClassCastException
   ```

3. **Which method signature is most flexible?**
   ```java
   a) public static <T> T max(List<T> items)
   b) public static <T extends Comparable<T>> T max(List<T> items)
   c) public static <T extends Comparable<? super T>> T max(List<? extends T> items)
   d) public static Object max(List<?> items)
   ```

4. **When is @SafeVarargs appropriate?**
   ```java
   a) Always with generic varargs
   b) When the method doesn't store the varargs array
   c) Only on static methods
   d) Never with generic types
   ```

5. **What's the best way to create a generic array?**
   ```java
   a) T[] array = new T[10];
   b) T[] array = (T[]) new Object[10];
   c) T[] array = (T[]) Array.newInstance(clazz, 10);
   d) Generic arrays are impossible
   ```

#### Section B: Code Implementation (2 problems)

**Problem 1**: Implement a generic `Cache<K, V>` class with:
- Type-safe get/put operations
- TTL (time-to-live) support
- Size limits with LRU eviction
- Thread safety

**Problem 2**: Create a `GenericBuilder<T>` that:
- Uses CRTP pattern
- Supports fluent chaining
- Has validation before building
- Works with inheritance

#### Section C: Design Question

Design a generic event sourcing system that:
- Handles different event types safely
- Supports event replay with type safety
- Includes snapshot functionality
- Integrates with modern Java features

### 💡 Answers

<details>
<summary>🏆 Check your answers after completing the exam</summary>

**Section A Answers:**
1. **b)** - Correct PECS: dest is consumer (super), src is producer (extends)
2. **c)** - Type erasure makes both classes identical at runtime
3. **c)** - Most flexible: bounded type parameter with PECS wildcards
4. **b)** - Safe when method doesn't store or expose the varargs array
5. **c)** - Array.newInstance with Class parameter is type-safe

**Section B Solutions:**

```java
// Problem 1: Generic Cache
public class Cache<K, V> {
    private final Map<K, CacheEntry<V>> storage = new ConcurrentHashMap<>();
    private final int maxSize;
    private final long ttlMillis;
    private final LinkedHashMap<K, Long> accessOrder = new LinkedHashMap<>(16, 0.75f, true);

    public Cache(int maxSize, long ttlMillis) {
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
    }

    public synchronized void put(K key, V value) {
        evictExpired();

        if (storage.size() >= maxSize && !storage.containsKey(key)) {
            evictLRU();
        }

        storage.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
        accessOrder.put(key, System.currentTimeMillis());
    }

    public synchronized Optional<V> get(K key) {
        CacheEntry<V> entry = storage.get(key);

        if (entry == null || isExpired(entry)) {
            storage.remove(key);
            accessOrder.remove(key);
            return Optional.empty();
        }

        accessOrder.put(key, System.currentTimeMillis());
        return Optional.of(entry.value());
    }

    private boolean isExpired(CacheEntry<V> entry) {
        return System.currentTimeMillis() - entry.timestamp() > ttlMillis;
    }

    private void evictExpired() {
        storage.entrySet().removeIf(entry -> isExpired(entry.getValue()));
    }

    private void evictLRU() {
        K oldestKey = accessOrder.keySet().iterator().next();
        storage.remove(oldestKey);
        accessOrder.remove(oldestKey);
    }

    private record CacheEntry<V>(V value, long timestamp) {}
}

// Problem 2: Generic Builder with CRTP
public abstract class GenericBuilder<T extends GenericBuilder<T>> {
    protected final Map<String, Object> properties = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    public T with(String key, Object value) {
        properties.put(key, value);
        return self();
    }

    protected abstract void validate();

    public abstract Object build();

    public final Object buildSafely() {
        validate();
        return build();
    }
}

class PersonBuilder extends GenericBuilder<PersonBuilder> {
    @Override
    protected void validate() {
        if (!properties.containsKey("name")) {
            throw new IllegalStateException("Name is required");
        }
    }

    @Override
    public Person build() {
        return new Person((String) properties.get("name"),
                         (Integer) properties.get("age"));
    }
}
```

**Section C Design:**
A complete event sourcing system would include:
- Generic `Event<T>` interface with payload types
- `EventStore<T>` with type-safe storage
- `EventReplay<T>` with type-safe event processing
- `Snapshot<T>` for state optimization
- Integration with Records for immutable events
- Sealed types for event hierarchies

**Scoring:**
- Section A: 20 points (4 points each)
- Section B: 40 points (20 points each)
- Section C: 40 points

</details>

---

## 🎓 Congratulations & Next Steps

### 🏆 Course Completion

**You've successfully completed the Java Generics Mastery Course!**

### 📜 Your Achievements

- ✅ **Mastered Type Safety**: Eliminated ClassCastException risks
- ✅ **Applied PECS Principle**: Designed flexible APIs with wildcards
- ✅ **Handled Advanced Patterns**: Type erasure, CRTP, and heterogeneous containers
- ✅ **Integrated Modern Features**: Records, sealed types, pattern matching
- ✅ **Built Real-World Solutions**: Enterprise-ready generic systems

### 🚀 Continuing Your Journey

#### Advanced Topics to Explore

1. **Generic Frameworks**: Study Spring, Hibernate, Jackson generics usage
2. **Performance Tuning**: Advanced JVM optimizations with generics
3. **Functional Programming**: Deeper integration with streams and lambdas
4. **Concurrent Programming**: Generic thread-safe patterns
5. **Microservices**: Generic API design for distributed systems

#### Recommended Reading

- "Effective Java" by Joshua Bloch (Items 26-33)
- "Java: The Complete Reference" - Generics chapters
- "Modern Java in Action" - Advanced generics patterns
- Oracle Java Documentation - Generics tutorial

#### Community Involvement

- **Contribute to Open Source**: Apply your skills to real projects
- **Blog About Generics**: Share your learning journey
- **Mentor Others**: Help developers learn generics
- **Join Java Communities**: JUGs, Stack Overflow, Reddit

### 🎯 Portfolio Projects

Build these projects to showcase your skills:

1. **Generic ORM Framework**: Type-safe database operations
2. **Event Sourcing Library**: Generic event handling system
3. **Validation Framework**: Type-safe validation with generics
4. **Caching Library**: Generic cache with advanced features
5. **API Framework**: Generic REST API with type safety

---

## 📝 Final Self-Assessment

Rate your confidence (1-5) in these areas:

- [ ] Understanding generics fundamentals (1-5)
- [ ] Applying PECS principle correctly (1-5)
- [ ] Handling type erasure challenges (1-5)
- [ ] Using advanced patterns (CRTP, type tokens) (1-5)
- [ ] Integrating with modern Java features (1-5)
- [ ] Designing production-ready generic APIs (1-5)
- [ ] Debugging generic code issues (1-5)
- [ ] Teaching generics to others (1-5)

**Target**: All areas should be 4 or 5. If any area is below 4, consider reviewing that module.

---

## 🎉 Thank You!

Thank you for completing the **Java Generics Mastery Course**! You've invested in becoming a better Java developer, and the skills you've learned will benefit you throughout your career.

**Remember**: Generics are not just about type safety - they're about writing expressive, maintainable, and robust code that stands the test of time.

**Keep coding, keep learning, and keep sharing your knowledge!** 🚀

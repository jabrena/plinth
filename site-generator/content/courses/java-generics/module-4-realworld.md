title=Module 4: Real-World Applications - Production Patterns and Integration
type=course-module
status=published
date=2025-09-13
author=MyRobot
module=4
duration=3-4 hours
difficulty=Advanced
tags=java, generics, real-world, records, sealed-types, serialization, performance
~~~~~~

## 📖 Module Overview

Time to apply your generics mastery to real-world scenarios! This module bridges theory and practice, showing how generics integrate with modern Java features and solve actual production problems. You'll work with contemporary patterns used in enterprise applications.

### 🎯 Learning Objectives

By the end of this module, you will:

- **Integrate** generics with Records and sealed types
- **Handle** serialization of generic types correctly
- **Apply** pattern matching with generic types
- **Optimize** performance with generic design choices
- **Migrate** legacy codebases to use generics
- **Design** production-ready APIs with generics
- **Solve** common enterprise integration challenges

### ⏱️ Estimated Time: 3-4 hours

---

## 🎁 Generics with Modern Java Records

### 📦 Generic Records for Data Transfer

Records work beautifully with generics to create type-safe, immutable data structures:

```java
// Generic Result type for API responses
public record Result<T, E>(T data, E error, boolean success) {

    // Factory methods for convenience
    public static <T, E> Result<T, E> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T, E> Result<T, E> failure(E error) {
        return new Result<>(null, error, false);
    }

    // Transformation methods
    public <U> Result<U, E> map(java.util.function.Function<T, U> mapper) {
        return success ? success(mapper.apply(data)) : failure(error);
    }

    public <F> Result<T, F> mapError(java.util.function.Function<E, F> mapper) {
        return success ? success(data) : failure(mapper.apply(error));
    }

    // Convenience methods
    public java.util.Optional<T> toOptional() {
        return success ? java.util.Optional.of(data) : java.util.Optional.empty();
    }

    public T orElse(T defaultValue) {
        return success ? data : defaultValue;
    }

    public T orElseThrow() {
        if (!success) {
            throw new RuntimeException("Operation failed: " + error);
        }
        return data;
    }
}

// Generic Page record for pagination
public record Page<T>(
    java.util.List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages
) {
    // Compact constructor with validation
    public Page {
        content = java.util.List.copyOf(content); // Defensive copy
        if (pageNumber < 0) throw new IllegalArgumentException("Page number cannot be negative");
        if (pageSize <= 0) throw new IllegalArgumentException("Page size must be positive");
        if (totalElements < 0) throw new IllegalArgumentException("Total elements cannot be negative");
    }

    // Transformation method
    public <U> Page<U> map(java.util.function.Function<T, U> mapper) {
        return new Page<>(
            content.stream().map(mapper).toList(),
            pageNumber,
            pageSize,
            totalElements,
            totalPages
        );
    }

    // Utility methods
    public boolean hasContent() {
        return !content.isEmpty();
    }

    public boolean hasNext() {
        return pageNumber < totalPages - 1;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}
```

### 🎯 Hands-On Exercise 1: Generic Records for Enterprise APIs

Create a comprehensive API response system using generic Records:

```java
// TODO: Create a generic API response system
public record ApiResponse<T>(???) {

    // TODO: Add factory methods for different response types

    // TODO: Add transformation methods

    // TODO: Add validation in compact constructor
}

// TODO: Create a generic validation result
public record ValidationResult<T>(???) {

    // TODO: Add methods to combine validation results

    // TODO: Add methods to extract valid data
}

// TODO: Create a generic event record
public record Event<T>(???) {

    // TODO: Add timestamp and metadata

    // TODO: Add serialization support
}
```

<details>
<summary>🎁 Try designing the enterprise API system yourself</summary>

```java
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// Comprehensive API response with metadata
public record ApiResponse<T>(
    T data,
    String message,
    int statusCode,
    Instant timestamp,
    Map<String, Object> metadata
) {
    // Compact constructor with validation and defaults
    public ApiResponse {
        timestamp = Objects.requireNonNullElse(timestamp, Instant.now());
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();

        if (statusCode < 100 || statusCode >= 600) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + statusCode);
        }
    }

    // Factory methods for common response types
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, "Success", 200, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(data, message, 200, null, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(data, "Created", 201, null, null);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(null, "No Content", 204, null, null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(null, message, 400, null, null);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(null, message, 404, null, null);
    }

    public static <T> ApiResponse<T> serverError(String message) {
        return new ApiResponse<>(null, message, 500, null, null);
    }

    // Transformation methods
    public <U> ApiResponse<U> map(Function<T, U> mapper) {
        return new ApiResponse<>(
            data != null ? mapper.apply(data) : null,
            message,
            statusCode,
            timestamp,
            metadata
        );
    }

    // Add metadata
    public ApiResponse<T> withMetadata(String key, Object value) {
        Map<String, Object> newMetadata = new HashMap<>(metadata);
        newMetadata.put(key, value);
        return new ApiResponse<>(data, message, statusCode, timestamp, newMetadata);
    }

    // Status checking methods
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isError() {
        return statusCode >= 400;
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500;
    }
}

// Validation result with error accumulation
public record ValidationResult<T>(
    T value,
    List<String> errors,
    boolean valid
) {
    // Compact constructor
    public ValidationResult {
        errors = errors != null ? List.copyOf(errors) : List.of();
        valid = errors.isEmpty();
    }

    // Factory methods
    public static <T> ValidationResult<T> valid(T value) {
        return new ValidationResult<>(value, List.of(), true);
    }

    public static <T> ValidationResult<T> invalid(String... errors) {
        return new ValidationResult<>(null, Arrays.asList(errors), false);
    }

    public static <T> ValidationResult<T> invalid(List<String> errors) {
        return new ValidationResult<>(null, errors, false);
    }

    // Combine validation results
    public <U> ValidationResult<U> flatMap(Function<T, ValidationResult<U>> mapper) {
        if (!valid) {
            return invalid(errors);
        }

        ValidationResult<U> result = mapper.apply(value);
        if (result.valid) {
            return result;
        } else {
            List<String> combinedErrors = new ArrayList<>(errors);
            combinedErrors.addAll(result.errors);
            return invalid(combinedErrors);
        }
    }

    // Transform valid value
    public <U> ValidationResult<U> map(Function<T, U> mapper) {
        return valid ? valid(mapper.apply(value)) : invalid(errors);
    }

    // Combine multiple validation results
    public static <T> ValidationResult<List<T>> combine(List<ValidationResult<T>> results) {
        List<String> allErrors = new ArrayList<>();
        List<T> validValues = new ArrayList<>();

        for (ValidationResult<T> result : results) {
            if (result.valid) {
                validValues.add(result.value);
            } else {
                allErrors.addAll(result.errors);
            }
        }

        return allErrors.isEmpty() ? valid(validValues) : invalid(allErrors);
    }

    // Extract value or throw
    public T orElseThrow() {
        if (!valid) {
            throw new IllegalStateException("Validation failed: " + String.join(", ", errors));
        }
        return value;
    }

    // Get formatted error message
    public String getErrorMessage() {
        return valid ? "" : String.join("; ", errors);
    }
}

// Generic event with rich metadata
public record Event<T>(
    String id,
    String type,
    T payload,
    Instant timestamp,
    String source,
    String version,
    Map<String, Object> metadata
) {
    // Compact constructor with defaults
    public Event {
        id = Objects.requireNonNullElse(id, UUID.randomUUID().toString());
        timestamp = Objects.requireNonNullElse(timestamp, Instant.now());
        version = Objects.requireNonNullElse(version, "1.0");
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();

        Objects.requireNonNull(type, "Event type cannot be null");
        Objects.requireNonNull(payload, "Event payload cannot be null");
        Objects.requireNonNull(source, "Event source cannot be null");
    }

    // Factory methods
    public static <T> Event<T> of(String type, T payload, String source) {
        return new Event<>(null, type, payload, null, source, null, null);
    }

    public static <T> Event<T> of(String type, T payload, String source, Map<String, Object> metadata) {
        return new Event<>(null, type, payload, null, source, null, metadata);
    }

    // Transform payload
    public <U> Event<U> mapPayload(Function<T, U> mapper) {
        return new Event<>(id, type, mapper.apply(payload), timestamp, source, version, metadata);
    }

    // Add metadata
    public Event<T> withMetadata(String key, Object value) {
        Map<String, Object> newMetadata = new HashMap<>(metadata);
        newMetadata.put(key, value);
        return new Event<>(id, type, payload, timestamp, source, version, newMetadata);
    }

    // Check event age
    public boolean isOlderThan(java.time.Duration duration) {
        return timestamp.plus(duration).isBefore(Instant.now());
    }

    // Serialization-friendly representation
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("payload", payload);
        map.put("timestamp", timestamp.toString());
        map.put("source", source);
        map.put("version", version);
        map.put("metadata", metadata);
        return map;
    }
}

// Comprehensive test
class EnterpriseRecordsTest {
    public static void main(String[] args) {
        // Test ApiResponse
        ApiResponse<String> successResponse = ApiResponse.ok("Hello World", "Operation successful");
        ApiResponse<Integer> mappedResponse = successResponse.map(String::length);

        System.out.println("Success response: " + successResponse);
        System.out.println("Mapped response: " + mappedResponse);
        System.out.println("Is success: " + successResponse.isSuccess());

        // Test ValidationResult
        ValidationResult<String> validName = ValidationResult.valid("John Doe");
        ValidationResult<Integer> validAge = ValidationResult.valid(25);
        ValidationResult<String> invalidEmail = ValidationResult.invalid("Invalid email format");

        // Combine validations
        List<ValidationResult<Object>> validations = List.of(
            validName.map(Object.class::cast),
            validAge.map(Object.class::cast),
            invalidEmail.map(Object.class::cast)
        );

        ValidationResult<List<Object>> combined = ValidationResult.combine(validations);
        System.out.println("Combined validation: " + combined);
        System.out.println("Validation errors: " + combined.getErrorMessage());

        // Test Event
        Event<Map<String, Object>> userCreatedEvent = Event.of(
            "user.created",
            Map.of("userId", "123", "username", "johndoe"),
            "user-service",
            Map.of("version", "2.0", "region", "us-east-1")
        );

        Event<String> transformedEvent = userCreatedEvent.mapPayload(
            payload -> "User " + payload.get("username") + " created"
        );

        System.out.println("Original event: " + userCreatedEvent);
        System.out.println("Transformed event: " + transformedEvent);
        System.out.println("Event map: " + userCreatedEvent.toMap());
    }
}
```

**🎁 Enterprise Benefits Achieved:**
- ✅ **Type Safety**: Compile-time guarantees for API responses
- ✅ **Immutability**: Records are naturally immutable
- ✅ **Rich Metadata**: Comprehensive context information
- ✅ **Transformation**: Functional-style data manipulation
- ✅ **Validation**: Error accumulation and reporting
- ✅ **Serialization Ready**: Easy conversion to/from JSON

</details>

---

## 🔒 Generics with Sealed Types

### 🎯 Type-Safe State Machines

Sealed types with generics create powerful, type-safe state machines and result types:

```java
// Sealed generic result type
public sealed interface Result<T, E>
    permits Result.Success, Result.Failure {

    // Pattern matching methods
    <R> R fold(
        java.util.function.Function<T, R> onSuccess,
        java.util.function.Function<E, R> onFailure
    );

    // Transformation methods
    <U> Result<U, E> map(java.util.function.Function<T, U> mapper);
    <F> Result<T, F> mapError(java.util.function.Function<E, F> mapper);

    // Success case
    public record Success<T, E>(T value) implements Result<T, E> {
        @Override
        public <R> R fold(
            java.util.function.Function<T, R> onSuccess,
            java.util.function.Function<E, R> onFailure
        ) {
            return onSuccess.apply(value);
        }

        @Override
        public <U> Result<U, E> map(java.util.function.Function<T, U> mapper) {
            return new Success<>(mapper.apply(value));
        }

        @Override
        public <F> Result<T, F> mapError(java.util.function.Function<E, F> mapper) {
            return new Success<>(value);
        }
    }

    // Failure case
    public record Failure<T, E>(E error) implements Result<T, E> {
        @Override
        public <R> R fold(
            java.util.function.Function<T, R> onSuccess,
            java.util.function.Function<E, R> onFailure
        ) {
            return onFailure.apply(error);
        }

        @Override
        public <U> Result<U, E> map(java.util.function.Function<T, U> mapper) {
            return new Failure<>(error);
        }

        @Override
        public <F> Result<T, F> mapError(java.util.function.Function<E, F> mapper) {
            return new Failure<>(mapper.apply(error));
        }
    }

    // Factory methods
    static <T, E> Result<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }
}

// Sealed generic state machine
public sealed interface ProcessingState<T>
    permits ProcessingState.Pending, ProcessingState.InProgress,
            ProcessingState.Completed, ProcessingState.Failed {

    // State transition methods
    default ProcessingState<T> start() {
        return switch (this) {
            case Pending<T> p -> new InProgress<>(p.data(), java.time.Instant.now());
            case InProgress<T> ip -> ip; // Already in progress
            case Completed<T> c -> c;   // Already completed
            case Failed<T> f -> f;      // Failed, cannot restart
        };
    }

    default ProcessingState<T> complete(T result) {
        return switch (this) {
            case Pending<T> p -> throw new IllegalStateException("Cannot complete without starting");
            case InProgress<T> ip -> new Completed<>(result, java.time.Instant.now());
            case Completed<T> c -> c;   // Already completed
            case Failed<T> f -> f;      // Failed, cannot complete
        };
    }

    default ProcessingState<T> fail(String error) {
        return new Failed<>(error, java.time.Instant.now());
    }

    // State records
    record Pending<T>(T data) implements ProcessingState<T> {}

    record InProgress<T>(T data, java.time.Instant startTime) implements ProcessingState<T> {}

    record Completed<T>(T result, java.time.Instant completionTime) implements ProcessingState<T> {}

    record Failed<T>(String error, java.time.Instant failureTime) implements ProcessingState<T> {}
}
```

### 🎯 Pattern Matching with Generics

```java
// Pattern matching with sealed generic types
public class PatternMatchingExample {

    // Process result with pattern matching
    public static <T, E> String processResult(Result<T, E> result) {
        return switch (result) {
            case Result.Success<T, E> success ->
                "Success: " + success.value();
            case Result.Failure<T, E> failure ->
                "Failed: " + failure.error();
        };
    }

    // Process state with pattern matching
    public static <T> String describeState(ProcessingState<T> state) {
        return switch (state) {
            case ProcessingState.Pending<T> pending ->
                "Waiting to process: " + pending.data();
            case ProcessingState.InProgress<T> inProgress ->
                "Processing since: " + inProgress.startTime();
            case ProcessingState.Completed<T> completed ->
                "Completed at: " + completed.completionTime() +
                " with result: " + completed.result();
            case ProcessingState.Failed<T> failed ->
                "Failed at: " + failed.failureTime() +
                " with error: " + failed.error();
        };
    }

    // Complex pattern matching with guards
    public static <T> boolean canRetry(ProcessingState<T> state) {
        return switch (state) {
            case ProcessingState.Pending<T> p -> true;
            case ProcessingState.Failed<T> f when
                f.failureTime().isAfter(java.time.Instant.now().minusSeconds(60)) -> true;
            case ProcessingState.Failed<T> f -> false;
            case ProcessingState.InProgress<T> ip -> false;
            case ProcessingState.Completed<T> c -> false;
        };
    }
}
```

---

## 📡 Serialization with Generic Types

### 🔧 Handling Type Erasure in Serialization

Serialization frameworks need explicit type information for generic types:

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GenericSerialization {
    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
    private static final Gson GSON = new Gson();

    // Jackson serialization with TypeReference
    public static <T> String toJsonJackson(T object) throws Exception {
        return JACKSON_MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJsonJackson(String json, TypeReference<T> typeRef) throws Exception {
        return JACKSON_MAPPER.readValue(json, typeRef);
    }

    // Gson serialization with TypeToken
    public static <T> String toJsonGson(T object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJsonGson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }

    // Type-safe serialization helper
    public static class SerializationHelper {

        // Serialize generic collections
        public static <T> String serializeList(java.util.List<T> list) throws Exception {
            return JACKSON_MAPPER.writeValueAsString(list);
        }

        public static <T> java.util.List<T> deserializeList(String json, Class<T> elementType) throws Exception {
            return JACKSON_MAPPER.readValue(json,
                JACKSON_MAPPER.getTypeFactory().constructCollectionType(java.util.List.class, elementType));
        }

        // Serialize generic maps
        public static <K, V> String serializeMap(java.util.Map<K, V> map) throws Exception {
            return JACKSON_MAPPER.writeValueAsString(map);
        }

        public static <K, V> java.util.Map<K, V> deserializeMap(
            String json, Class<K> keyType, Class<V> valueType
        ) throws Exception {
            return JACKSON_MAPPER.readValue(json,
                JACKSON_MAPPER.getTypeFactory().constructMapType(java.util.Map.class, keyType, valueType));
        }

        // Serialize generic Result types
        public static <T, E> String serializeResult(Result<T, E> result) throws Exception {
            return JACKSON_MAPPER.writeValueAsString(result);
        }

        public static <T, E> Result<T, E> deserializeResult(
            String json, Class<T> successType, Class<E> errorType
        ) throws Exception {
            // Custom deserializer would be needed for sealed types
            // This is a simplified example
            return JACKSON_MAPPER.readValue(json, new TypeReference<Result<T, E>>() {});
        }
    }
}

// Usage examples
class SerializationExamples {
    public static void main(String[] args) throws Exception {

        // Serialize/deserialize generic lists
        java.util.List<String> stringList = java.util.List.of("hello", "world", "java");
        String jsonList = GenericSerialization.SerializationHelper.serializeList(stringList);
        java.util.List<String> deserializedList =
            GenericSerialization.SerializationHelper.deserializeList(jsonList, String.class);

        System.out.println("Original: " + stringList);
        System.out.println("JSON: " + jsonList);
        System.out.println("Deserialized: " + deserializedList);

        // Serialize/deserialize generic maps
        java.util.Map<String, Integer> scores = java.util.Map.of("Alice", 95, "Bob", 87);
        String jsonMap = GenericSerialization.SerializationHelper.serializeMap(scores);
        java.util.Map<String, Integer> deserializedMap =
            GenericSerialization.SerializationHelper.deserializeMap(jsonMap, String.class, Integer.class);

        System.out.println("Original map: " + scores);
        System.out.println("JSON map: " + jsonMap);
        System.out.println("Deserialized map: " + deserializedMap);

        // Using TypeReference for complex types
        java.util.List<java.util.Map<String, Object>> complexList = java.util.List.of(
            java.util.Map.of("name", "John", "age", 30),
            java.util.Map.of("name", "Jane", "age", 25)
        );

        String complexJson = GenericSerialization.toJsonJackson(complexList);
        java.util.List<java.util.Map<String, Object>> deserializedComplex =
            GenericSerialization.fromJsonJackson(complexJson,
                new TypeReference<java.util.List<java.util.Map<String, Object>>>() {});

        System.out.println("Complex original: " + complexList);
        System.out.println("Complex deserialized: " + deserializedComplex);
    }
}
```

---

## ⚡ Performance Considerations

### 🚀 Generic Performance Patterns

Understanding performance implications of generic design choices:

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GenericPerformancePatterns {

    // 1. Prefer specific types over wildcards when possible
    // ✅ Better performance - no runtime type checks
    public static <T extends Number> double sum(List<T> numbers) {
        return numbers.stream().mapToDouble(Number::doubleValue).sum();
    }

    // ❌ Slower - requires runtime type checking
    public static double sumWildcard(List<? extends Number> numbers) {
        return numbers.stream().mapToDouble(Number::doubleValue).sum();
    }

    // 2. Cache generic instances to avoid repeated allocation
    private static final Map<Class<?>, Function<?, String>> TO_STRING_CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> Function<T, String> getToStringFunction(Class<T> type) {
        return (Function<T, String>) TO_STRING_CACHE.computeIfAbsent(type,
            k -> Object::toString);
    }

    // 3. Use primitive specializations when available
    public static class PrimitiveOptimizedContainer<T> {
        private final List<T> objects = new ArrayList<>();
        // Separate primitive collections for better performance
        private final java.util.stream.IntStream.Builder intBuilder = java.util.stream.IntStream.builder();
        private final java.util.stream.DoubleStream.Builder doubleBuilder = java.util.stream.DoubleStream.builder();

        public void add(T item) {
            objects.add(item);

            // Optimize for common numeric types
            if (item instanceof Integer) {
                intBuilder.add((Integer) item);
            } else if (item instanceof Double) {
                doubleBuilder.add((Double) item);
            }
        }

        // Fast path for numeric operations
        public OptionalDouble fastSum() {
            if (!objects.isEmpty() && objects.get(0) instanceof Number) {
                return objects.stream()
                    .mapToDouble(obj -> ((Number) obj).doubleValue())
                    .reduce(Double::sum);
            }
            return OptionalDouble.empty();
        }
    }

    // 4. Lazy evaluation with generics
    public static class LazyGenericContainer<T> {
        private final java.util.function.Supplier<T> supplier;
        private volatile T cached;
        private volatile boolean computed = false;

        public LazyGenericContainer(java.util.function.Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public T get() {
            if (!computed) {
                synchronized (this) {
                    if (!computed) {
                        cached = supplier.get();
                        computed = true;
                    }
                }
            }
            return cached;
        }

        public boolean isComputed() {
            return computed;
        }
    }

    // 5. Memory-efficient generic collections
    public static class CompactGenericList<T> {
        private Object[] elements;
        private int size;
        private static final int DEFAULT_CAPACITY = 4; // Smaller than ArrayList's 10

        @SuppressWarnings("unchecked")
        public CompactGenericList() {
            this.elements = new Object[DEFAULT_CAPACITY];
        }

        public void add(T element) {
            if (size >= elements.length) {
                grow();
            }
            elements[size++] = element;
        }

        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index >= size) throw new IndexOutOfBoundsException();
            return (T) elements[index];
        }

        private void grow() {
            int newCapacity = elements.length + (elements.length >> 1); // 1.5x growth
            elements = Arrays.copyOf(elements, newCapacity);
        }

        public int size() { return size; }

        // Trim to actual size to save memory
        public void trimToSize() {
            if (size < elements.length) {
                elements = Arrays.copyOf(elements, size);
            }
        }
    }
}

// Performance benchmarking example
class PerformanceBenchmark {
    public static void main(String[] args) {
        // Benchmark different approaches
        List<Integer> numbers = java.util.stream.IntStream.range(0, 1_000_000)
            .boxed()
            .collect(java.util.stream.Collectors.toList());

        // Warm up JVM
        for (int i = 0; i < 10; i++) {
            GenericPerformancePatterns.sum(numbers);
            GenericPerformancePatterns.sumWildcard(numbers);
        }

        // Benchmark specific types vs wildcards
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            GenericPerformancePatterns.sum(numbers);
        }
        long specificTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            GenericPerformancePatterns.sumWildcard(numbers);
        }
        long wildcardTime = System.nanoTime() - start;

        System.out.println("Specific type time: " + specificTime / 1_000_000 + " ms");
        System.out.println("Wildcard time: " + wildcardTime / 1_000_000 + " ms");
        System.out.println("Performance difference: " +
            (wildcardTime - specificTime) / (double) specificTime * 100 + "%");

        // Test compact list memory usage
        GenericPerformancePatterns.CompactGenericList<String> compactList =
            new GenericPerformancePatterns.CompactGenericList<>();

        for (int i = 0; i < 1000; i++) {
            compactList.add("Item " + i);
        }

        System.out.println("Compact list size: " + compactList.size());
        compactList.trimToSize();
        System.out.println("Memory optimized");
    }
}
```

---

## 🔄 Legacy Code Migration

### 📈 Systematic Migration Strategy

```java
// Step-by-step migration from raw types to generics
public class LegacyMigrationExample {

    // PHASE 1: Raw types (legacy code)
    public static class LegacyUserService {
        private List users = new ArrayList();        // Raw type
        private Map usersByEmail = new HashMap();    // Raw type

        public void addUser(Object user) {
            users.add(user);
            // Unsafe operations
            Map userMap = (Map) user;
            usersByEmail.put(userMap.get("email"), user);
        }

        public Object getUserByEmail(String email) {
            return usersByEmail.get(email);
        }

        public List getAllUsers() {
            return users;
        }
    }

    // PHASE 2: Gradual migration - parameterize collections
    public static class MigratingUserService {
        private List<Map<String, Object>> users = new ArrayList<>();  // Partially generic
        private Map<String, Map<String, Object>> usersByEmail = new HashMap<>();  // Partially generic

        public void addUser(Map<String, Object> user) {  // More specific parameter
            users.add(user);
            usersByEmail.put((String) user.get("email"), user);  // Still some casting
        }

        public Map<String, Object> getUserByEmail(String email) {  // Specific return type
            return usersByEmail.get(email);
        }

        public List<Map<String, Object>> getAllUsers() {  // Specific return type
            return users;
        }
    }

    // PHASE 3: Full generic transformation with proper types
    public static class ModernUserService<T extends User> {
        private final List<T> users = new ArrayList<>();
        private final Map<String, T> usersByEmail = new HashMap<>();

        public void addUser(T user) {
            users.add(user);
            usersByEmail.put(user.getEmail(), user);
        }

        public Optional<T> getUserByEmail(String email) {
            return Optional.ofNullable(usersByEmail.get(email));
        }

        public List<T> getAllUsers() {
            return Collections.unmodifiableList(users);
        }

        // Additional type-safe methods
        public List<T> getUsersByPredicate(java.util.function.Predicate<T> predicate) {
            return users.stream()
                       .filter(predicate)
                       .collect(java.util.stream.Collectors.toList());
        }

        public <R> List<R> mapUsers(java.util.function.Function<T, R> mapper) {
            return users.stream()
                       .map(mapper)
                       .collect(java.util.stream.Collectors.toList());
        }
    }

    // Supporting User interface for type safety
    public interface User {
        String getEmail();
        String getName();
    }

    public record SimpleUser(String email, String name) implements User {
        @Override
        public String getEmail() { return email; }

        @Override
        public String getName() { return name; }
    }
}

// Migration utility class
public class MigrationUtils {

    // Helper method to safely cast collections during migration
    @SuppressWarnings("unchecked")
    public static <T> List<T> safeCastList(List<?> rawList, Class<T> elementType) {
        List<T> result = new ArrayList<>();
        for (Object item : rawList) {
            if (elementType.isInstance(item)) {
                result.add((T) item);
            } else {
                throw new ClassCastException("Cannot cast " + item.getClass() + " to " + elementType);
            }
        }
        return result;
    }

    // Helper method to validate and convert raw maps
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> safeCastMap(Map<?, ?> rawMap, Class<K> keyType, Class<V> valueType) {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            if (keyType.isInstance(entry.getKey()) && valueType.isInstance(entry.getValue())) {
                result.put((K) entry.getKey(), (V) entry.getValue());
            } else {
                throw new ClassCastException("Invalid map entry types");
            }
        }
        return result;
    }

    // Gradual migration wrapper
    public static class MigrationWrapper<T> {
        private final List<Object> rawList;
        private final Class<T> targetType;

        public MigrationWrapper(List<Object> rawList, Class<T> targetType) {
            this.rawList = rawList;
            this.targetType = targetType;
        }

        // Gradually convert to typed list
        public List<T> getTypedList() {
            return safeCastList(rawList, targetType);
        }

        // Add new items with type safety
        public void addTyped(T item) {
            rawList.add(item);
        }

        // Check migration progress
        public double getMigrationProgress() {
            long typedCount = rawList.stream()
                .filter(targetType::isInstance)
                .count();
            return (double) typedCount / rawList.size();
        }
    }
}
```

---

## 🎯 Module 4 Capstone Project: Enterprise Event System

### 📋 Final Challenge

Create a comprehensive enterprise event processing system that demonstrates all real-world patterns:

```java
// TODO: Design a complete enterprise event system
// Requirements:
// 1. Generic event types with Records
// 2. Sealed type hierarchy for event processing states
// 3. Type-safe serialization support
// 4. Performance-optimized event storage
// 5. Pattern matching for event routing
// 6. Migration support for legacy events

public class EnterpriseEventSystem<T> {

    // TODO: Implement event storage with performance optimization

    // TODO: Add event processing pipeline with sealed states

    // TODO: Implement serialization with type safety

    // TODO: Add pattern matching for event routing

    // TODO: Create migration utilities for legacy events
}
```

<details>
<summary>🌍 Try building the complete enterprise system yourself</summary>

```java
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Complete enterprise event processing system
public class EnterpriseEventSystem<T> {

    // Event storage with performance optimization
    private final Map<String, List<Event<T>>> eventsByType = new ConcurrentHashMap<>();
    private final List<Event<T>> allEvents = new CopyOnWriteArrayList<>();
    private final Map<String, EventProcessor<T>> processors = new ConcurrentHashMap<>();

    // Event subscription system
    private final Map<String, List<Consumer<Event<T>>>> subscribers = new ConcurrentHashMap<>();

    // Performance metrics
    private final Map<String, Long> processingTimes = new ConcurrentHashMap<>();
    private final Map<String, Integer> processingCounts = new ConcurrentHashMap<>();

    // Generic event record with rich metadata
    public record Event<T>(
        String id,
        String type,
        T payload,
        Instant timestamp,
        String source,
        String version,
        Map<String, Object> metadata,
        EventPriority priority
    ) {
        public Event {
            id = Objects.requireNonNullElse(id, UUID.randomUUID().toString());
            timestamp = Objects.requireNonNullElse(timestamp, Instant.now());
            version = Objects.requireNonNullElse(version, "1.0");
            metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
            priority = Objects.requireNonNullElse(priority, EventPriority.NORMAL);

            Objects.requireNonNull(type, "Event type cannot be null");
            Objects.requireNonNull(payload, "Event payload cannot be null");
            Objects.requireNonNull(source, "Event source cannot be null");
        }

        // Factory methods
        public static <T> Event<T> of(String type, T payload, String source) {
            return new Event<>(null, type, payload, null, source, null, null, null);
        }

        public static <T> Event<T> highPriority(String type, T payload, String source) {
            return new Event<>(null, type, payload, null, source, null, null, EventPriority.HIGH);
        }

        // Transform payload while preserving metadata
        public <U> Event<U> mapPayload(Function<T, U> mapper) {
            return new Event<>(id, type, mapper.apply(payload), timestamp, source, version, metadata, priority);
        }

        // Add metadata
        public Event<T> withMetadata(String key, Object value) {
            Map<String, Object> newMetadata = new HashMap<>(metadata);
            newMetadata.put(key, value);
            return new Event<>(id, type, payload, timestamp, source, version, newMetadata, priority);
        }
    }

    // Event priority enum
    public enum EventPriority {
        LOW(1), NORMAL(2), HIGH(3), CRITICAL(4);

        private final int level;
        EventPriority(int level) { this.level = level; }
        public int getLevel() { return level; }
    }

    // Sealed processing state hierarchy
    public sealed interface ProcessingState<T>
        permits ProcessingState.Pending, ProcessingState.Processing,
                ProcessingState.Completed, ProcessingState.Failed, ProcessingState.Retrying {

        Event<T> getEvent();
        Instant getStateTime();

        // State transition methods with pattern matching
        default ProcessingState<T> startProcessing() {
            return switch (this) {
                case Pending<T> p -> new Processing<>(p.getEvent(), Instant.now());
                case Processing<T> pr -> pr; // Already processing
                case Completed<T> c -> c;   // Already completed
                case Failed<T> f -> new Retrying<>(f.getEvent(), Instant.now(), f.getError());
                case Retrying<T> r -> new Processing<>(r.getEvent(), Instant.now());
            };
        }

        default ProcessingState<T> complete() {
            return switch (this) {
                case Pending<T> p -> throw new IllegalStateException("Cannot complete without processing");
                case Processing<T> pr -> new Completed<>(pr.getEvent(), Instant.now());
                case Completed<T> c -> c;   // Already completed
                case Failed<T> f -> throw new IllegalStateException("Cannot complete failed event");
                case Retrying<T> r -> new Processing<>(r.getEvent(), Instant.now());
            };
        }

        default ProcessingState<T> fail(String error) {
            return new Failed<>(getEvent(), Instant.now(), error);
        }

        // State records
        record Pending<T>(Event<T> event, Instant stateTime) implements ProcessingState<T> {
            public Pending(Event<T> event) { this(event, Instant.now()); }
            @Override public Event<T> getEvent() { return event; }
            @Override public Instant getStateTime() { return stateTime; }
        }

        record Processing<T>(Event<T> event, Instant stateTime) implements ProcessingState<T> {
            @Override public Event<T> getEvent() { return event; }
            @Override public Instant getStateTime() { return stateTime; }
        }

        record Completed<T>(Event<T> event, Instant stateTime) implements ProcessingState<T> {
            @Override public Event<T> getEvent() { return event; }
            @Override public Instant getStateTime() { return stateTime; }
        }

        record Failed<T>(Event<T> event, Instant stateTime, String error) implements ProcessingState<T> {
            @Override public Event<T> getEvent() { return event; }
            @Override public Instant getStateTime() { return stateTime; }
            public String getError() { return error; }
        }

        record Retrying<T>(Event<T> event, Instant stateTime, String previousError) implements ProcessingState<T> {
            @Override public Event<T> getEvent() { return event; }
            @Override public Instant getStateTime() { return stateTime; }
            public String getPreviousError() { return previousError; }
        }
    }

    // Event processor interface
    @FunctionalInterface
    public interface EventProcessor<T> {
        ProcessingState<T> process(ProcessingState<T> state);
    }

    // Core event system methods
    public void publishEvent(Event<T> event) {
        // Store event
        allEvents.add(event);
        eventsByType.computeIfAbsent(event.type(), k -> new CopyOnWriteArrayList<>()).add(event);

        // Notify subscribers
        List<Consumer<Event<T>>> eventSubscribers = subscribers.get(event.type());
        if (eventSubscribers != null) {
            eventSubscribers.forEach(subscriber -> {
                try {
                    subscriber.accept(event);
                } catch (Exception e) {
                    System.err.println("Subscriber error for event " + event.id() + ": " + e.getMessage());
                }
            });
        }

        // Process event if processor exists
        EventProcessor<T> processor = processors.get(event.type());
        if (processor != null) {
            processEventAsync(event, processor);
        }
    }

    // Asynchronous event processing
    private void processEventAsync(Event<T> event, EventProcessor<T> processor) {
        CompletableFuture.runAsync(() -> {
            long startTime = System.nanoTime();
            ProcessingState<T> state = new ProcessingState.Pending<>(event);

            try {
                state = processor.process(state);
                long processingTime = System.nanoTime() - startTime;

                // Update metrics
                processingTimes.put(event.type(), processingTime);
                processingCounts.merge(event.type(), 1, Integer::sum);

            } catch (Exception e) {
                state = state.fail("Processing error: " + e.getMessage());
            }

            logProcessingResult(state);
        });
    }

    // Event subscription
    public void subscribe(String eventType, Consumer<Event<T>> subscriber) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    // Register event processor
    public void registerProcessor(String eventType, EventProcessor<T> processor) {
        processors.put(eventType, processor);
    }

    // Query methods with performance optimization
    public List<Event<T>> getEventsByType(String type) {
        return eventsByType.getOrDefault(type, List.of());
    }

    public List<Event<T>> getEventsByPredicate(Predicate<Event<T>> predicate) {
        return allEvents.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    public List<Event<T>> getEventsByPriority(EventPriority priority) {
        return getEventsByPredicate(event -> event.priority() == priority);
    }

    public List<Event<T>> getRecentEvents(java.time.Duration duration) {
        Instant cutoff = Instant.now().minus(duration);
        return getEventsByPredicate(event -> event.timestamp().isAfter(cutoff));
    }

    // Pattern matching for event routing
    public void routeEvent(Event<T> event) {
        String routing = switch (event.priority()) {
            case CRITICAL -> "critical-queue";
            case HIGH -> "high-priority-queue";
            case NORMAL -> "standard-queue";
            case LOW -> "low-priority-queue";
        };

        System.out.println("Routing event " + event.id() + " to " + routing);
    }

    // Performance metrics
    public Map<String, Double> getAverageProcessingTimes() {
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, Long> entry : processingTimes.entrySet()) {
            String eventType = entry.getKey();
            long totalTime = entry.getValue();
            int count = processingCounts.getOrDefault(eventType, 1);
            averages.put(eventType, (double) totalTime / count / 1_000_000); // Convert to milliseconds
        }
        return averages;
    }

    // Migration utilities for legacy events
    public static class MigrationUtils {

        @SuppressWarnings("unchecked")
        public static <T> Event<T> migrateLegacyEvent(Map<String, Object> legacyEvent, Class<T> payloadType) {
            String type = (String) legacyEvent.get("type");
            Object payload = legacyEvent.get("payload");
            String source = (String) legacyEvent.getOrDefault("source", "legacy-system");

            if (!payloadType.isInstance(payload)) {
                throw new IllegalArgumentException("Cannot migrate legacy event: payload type mismatch");
            }

            return Event.of(type, (T) payload, source);
        }

        public static <T> List<Event<T>> migrateLegacyEvents(
            List<Map<String, Object>> legacyEvents, Class<T> payloadType
        ) {
            return legacyEvents.stream()
                .map(legacy -> migrateLegacyEvent(legacy, payloadType))
                .collect(Collectors.toList());
        }
    }

    // Logging and monitoring
    private void logProcessingResult(ProcessingState<T> state) {
        String message = switch (state) {
            case ProcessingState.Completed<T> completed ->
                "Event " + completed.getEvent().id() + " completed successfully";
            case ProcessingState.Failed<T> failed ->
                "Event " + failed.getEvent().id() + " failed: " + failed.getError();
            case ProcessingState.Retrying<T> retrying ->
                "Event " + retrying.getEvent().id() + " retrying after: " + retrying.getPreviousError();
            default -> "Event " + state.getEvent().id() + " in state: " + state.getClass().getSimpleName();
        };

        System.out.println(Instant.now() + ": " + message);
    }

    // System health and statistics
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", allEvents.size());
        stats.put("eventTypeCount", eventsByType.size());
        stats.put("subscriberCount", subscribers.values().stream().mapToInt(List::size).sum());
        stats.put("processorCount", processors.size());
        stats.put("averageProcessingTimes", getAverageProcessingTimes());
        return stats;
    }
}

// Comprehensive test of the enterprise event system
class EnterpriseEventSystemTest {
    public static void main(String[] args) throws InterruptedException {
        EnterpriseEventSystem<Map<String, Object>> eventSystem = new EnterpriseEventSystem<>();

        // Register event processors
        eventSystem.registerProcessor("user.created", state -> {
            System.out.println("Processing user creation: " + state.getEvent().payload());
            return state.startProcessing().complete();
        });

        eventSystem.registerProcessor("order.placed", state -> {
            System.out.println("Processing order: " + state.getEvent().payload());
            // Simulate processing time
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            return state.startProcessing().complete();
        });

        // Subscribe to events
        eventSystem.subscribe("user.created", event ->
            System.out.println("User created notification sent for: " + event.payload().get("username"))
        );

        eventSystem.subscribe("order.placed", event ->
            System.out.println("Order confirmation sent for order: " + event.payload().get("orderId"))
        );

        // Publish various events
        eventSystem.publishEvent(EnterpriseEventSystem.Event.of(
            "user.created",
            Map.of("userId", "123", "username", "johndoe", "email", "john@example.com"),
            "user-service"
        ));

        eventSystem.publishEvent(EnterpriseEventSystem.Event.highPriority(
            "order.placed",
            Map.of("orderId", "ORD-001", "userId", "123", "amount", 99.99),
            "order-service"
        ));

        eventSystem.publishEvent(EnterpriseEventSystem.Event.of(
            "payment.processed",
            Map.of("paymentId", "PAY-001", "orderId", "ORD-001", "status", "SUCCESS"),
            "payment-service"
        ).withMetadata("region", "us-east-1"));

        // Wait for async processing
        Thread.sleep(500);

        // Query events
        System.out.println("\n=== Event Queries ===");
        System.out.println("User events: " + eventSystem.getEventsByType("user.created").size());
        System.out.println("High priority events: " + eventSystem.getEventsByPriority(
            EnterpriseEventSystem.EventPriority.HIGH).size());

        // Show system statistics
        System.out.println("\n=== System Statistics ===");
        Map<String, Object> stats = eventSystem.getSystemStats();
        stats.forEach((key, value) -> System.out.println(key + ": " + value));

        // Test migration utilities
        System.out.println("\n=== Legacy Migration Test ===");
        List<Map<String, Object>> legacyEvents = List.of(
            Map.of("type", "legacy.event", "payload", Map.of("data", "legacy1"), "source", "old-system"),
            Map.of("type", "legacy.event", "payload", Map.of("data", "legacy2"), "source", "old-system")
        );

        List<EnterpriseEventSystem.Event<Map<String, Object>>> migratedEvents =
            EnterpriseEventSystem.MigrationUtils.migrateLegacyEvents(legacyEvents, Map.class);

        System.out.println("Migrated " + migratedEvents.size() + " legacy events");
        migratedEvents.forEach(event -> eventSystem.publishEvent(event));

        Thread.sleep(200);
        System.out.println("\nFinal system stats: " + eventSystem.getSystemStats());
    }
}
```

**🌍 Enterprise Features Achieved:**
- ✅ **Generic Event System**: Type-safe events with rich metadata
- ✅ **Sealed State Machine**: Type-safe processing states with pattern matching
- ✅ **Performance Optimization**: Concurrent collections and async processing
- ✅ **Subscription System**: Type-safe event subscribers
- ✅ **Pattern Matching**: Modern Java pattern matching for routing
- ✅ **Legacy Migration**: Safe migration utilities for old systems
- ✅ **Monitoring**: Comprehensive metrics and health checks
- ✅ **Real-World Ready**: Production-quality error handling and logging

</details>

---

## 📚 Module 4 Summary

### 🎉 What You've Mastered

- **Modern Java Integration**: Generics with Records, sealed types, and pattern matching
- **Serialization**: Type-safe handling of generic types in JSON/XML
- **Performance**: Optimization strategies for generic code
- **Legacy Migration**: Systematic approaches to modernizing codebases
- **Enterprise Patterns**: Production-ready generic design patterns
- **Real-World Applications**: Solving actual business problems with generics

### 🔑 Key Takeaways

1. **Records + Generics = Powerful APIs**: Immutable, type-safe data structures
2. **Sealed types enable exhaustive pattern matching** with generic types
3. **Serialization requires explicit type information** - use TypeReference/TypeToken
4. **Performance matters**: Choose specific types over wildcards when possible
5. **Migration is incremental**: Transform legacy code step by step
6. **Modern Java features enhance generics** significantly

### 🚀 Next Steps

You're now ready for **Module 5: Assessment & Certification**, where you'll:
- Complete comprehensive coding challenges
- Participate in code review exercises
- Validate your mastery with certification exam
- Plan your continued learning journey

### 📝 Self-Assessment Checklist

Before proceeding, ensure you can:

- [ ] Design generic Records for enterprise APIs
- [ ] Use sealed types with generics effectively
- [ ] Handle serialization of generic types correctly
- [ ] Apply performance optimization techniques
- [ ] Migrate legacy code systematically
- [ ] Integrate generics with modern Java features

**Ready for the Assessment?** Continue to [Module 5: Assessment](./module-5-assessment.html) ✅

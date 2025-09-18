title=Module 3: Advanced Patterns - Type Erasure and Complex Patterns
type=course-module
status=published
date=2025-09-13
author=MyRobot
module=3
duration=4-5 hours
difficulty=Advanced
tags=java, generics, type-erasure, crtp, advanced-patterns, self-bounded
~~~~~~

## 📖 Module Overview

Welcome to the deep end of Java Generics! This module covers the most sophisticated patterns and edge cases that separate novice from expert generic programming. You'll understand how generics really work under the hood and master patterns used in production frameworks.

### 🎯 Learning Objectives

By the end of this module, you will:

- **Understand** type erasure and its runtime implications
- **Master** generic inheritance and self-bounded types (CRTP)
- **Implement** type-safe heterogeneous containers
- **Handle** varargs safely with generics
- **Apply** advanced patterns like type tokens
- **Avoid** heap pollution and runtime errors
- **Design** fluent APIs with preserved type information

### ⏱️ Estimated Time: 4-5 hours

---

## 🎭 Understanding Type Erasure

### 🔍 What is Type Erasure?

Type erasure is Java's way of maintaining backward compatibility while adding generics. At runtime, generic type information is "erased" - removed from the bytecode.

```java
// At compile time, these are different types
List<String> strings = new ArrayList<String>();
List<Integer> numbers = new ArrayList<Integer>();

// At runtime, they're both just List!
System.out.println(strings.getClass());  // class java.util.ArrayList
System.out.println(numbers.getClass());  // class java.util.ArrayList
System.out.println(strings.getClass() == numbers.getClass()); // true!
```

### 🧠 **Knowledge Check**: Erasure Implications

What do you think happens with these operations?

```java
List<String> strings = Arrays.asList("hello", "world");

// Can we check the generic type at runtime?
if (strings instanceof List<String>) { /* ??? */ }

// Can we create generic arrays?
List<String>[] arrayOfLists = new List<String>[10]; /* ??? */

// What about generic exceptions?
class GenericException<T> extends Exception { /* ??? */ }
```

<details>
<summary>🤔 Think about the runtime implications, then click to reveal</summary>

**Results:**
1. **instanceof with generics**: ❌ Compilation error - can't check generic types at runtime
2. **Generic arrays**: ❌ Compilation error - arrays are reifiable, generics are not
3. **Generic exceptions**: ❌ Compilation error - can't catch generic exception types

**Why?** Type erasure removes generic information at runtime, making these operations impossible or unsafe.

</details>

### 🔧 Working Around Type Erasure

#### 1. Type Tokens Pattern

```java
// Abstract class to capture type information
public abstract class TypeToken<T> {
    private final Type type;

    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getRawType() {
        if (type instanceof Class) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        throw new IllegalArgumentException("Cannot determine raw type for " + type);
    }
}

// Usage: Capture generic type information
public class TypeTokenExample {
    public static void demonstrateTypeTokens() {
        // Create type tokens for complex generic types
        TypeToken<List<String>> listToken = new TypeToken<List<String>>() {};
        TypeToken<Map<String, Integer>> mapToken = new TypeToken<Map<String, Integer>>() {};

        System.out.println("List type: " + listToken.getType());
        System.out.println("Map type: " + mapToken.getType());

        // Use in serialization frameworks (Jackson, Gson)
        // ObjectMapper mapper = new ObjectMapper();
        // List<String> list = mapper.readValue(json, listToken.getRawType());
    }
}
```

#### 2. Class Parameter Pattern

```java
// Pass Class<T> parameter to preserve type information
public class GenericFactory<T> {
    private final Class<T> type;

    public GenericFactory(Class<T> type) {
        this.type = type;
    }

    public T createInstance() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of " + type, e);
        }
    }

    @SuppressWarnings("unchecked")
    public T[] createArray(int size) {
        return (T[]) Array.newInstance(type, size);
    }

    public boolean isInstance(Object obj) {
        return type.isInstance(obj);
    }
}

// Usage
class FactoryExample {
    public static void main(String[] args) {
        GenericFactory<StringBuilder> sbFactory = new GenericFactory<>(StringBuilder.class);

        StringBuilder sb = sbFactory.createInstance();
        StringBuilder[] array = sbFactory.createArray(5);

        System.out.println("Created: " + sb.getClass());
        System.out.println("Array type: " + array.getClass().getComponentType());
    }
}
```

---

## 🏗️ Self-Bounded Generics (CRTP)

### 🤔 The Fluent API Problem

Consider building a fluent API where methods return `this` for chaining:

```java
// Base builder class
public class BaseBuilder {
    protected String name;

    public BaseBuilder withName(String name) {
        this.name = name;
        return this; // Problem: returns BaseBuilder, not subtype!
    }
}

// Extended builder
public class UserBuilder extends BaseBuilder {
    private int age;

    public UserBuilder withAge(int age) {
        this.age = age;
        return this;
    }
}

// Usage problem
class FluentProblem {
    public static void main(String[] args) {
        UserBuilder builder = new UserBuilder();

        // This breaks the chain - withName returns BaseBuilder!
        // builder.withName("John").withAge(25); // ❌ Compilation error!

        // Ugly workaround with casting
        UserBuilder result = (UserBuilder) builder.withName("John");
        result.withAge(25);
    }
}
```

### 🎯 CRTP Solution

The **Curiously Recurring Template Pattern** (CRTP) solves this by making the base class generic in its own subtype:

```java
// Self-bounded generic base class
public abstract class Builder<T extends Builder<T>> {
    protected String name;

    public T withName(String name) {
        this.name = name;
        return self(); // Returns the actual subtype!
    }

    // Subclasses must implement this to return themselves
    protected abstract T self();

    public String build() {
        return name;
    }
}

// Concrete implementation
public final class UserBuilder extends Builder<UserBuilder> {
    private int age;
    private String email;

    public UserBuilder withAge(int age) {
        this.age = age;
        return self();
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return self();
    }

    @Override
    protected UserBuilder self() {
        return this;
    }

    @Override
    public String build() {
        return String.format("User{name='%s', age=%d, email='%s'}",
                           name, age, email);
    }
}

// Perfect fluent chaining!
class CRTPExample {
    public static void main(String[] args) {
        String user = new UserBuilder()
            .withName("John")      // Returns UserBuilder
            .withAge(25)           // Returns UserBuilder
            .withEmail("john@example.com") // Returns UserBuilder
            .build();              // Perfect chaining!

        System.out.println(user);
    }
}
```

### 🎯 Hands-On Exercise 1: Advanced Builder Pattern

Create a sophisticated configuration builder using CRTP:

```java
// TODO: Implement a self-bounded generic configuration system
public abstract class ConfigBuilder<T extends ConfigBuilder<T>> {

    // TODO: Add common configuration properties

    // TODO: Implement fluent methods that return T

    // TODO: Add abstract self() method

    // TODO: Add validation logic
}

// TODO: Create specific configuration builders
public class DatabaseConfig extends ConfigBuilder<DatabaseConfig> {
    // TODO: Add database-specific configuration
}

public class CacheConfig extends ConfigBuilder<CacheConfig> {
    // TODO: Add cache-specific configuration
}

// TODO: Test fluent chaining with inheritance
```

<details>
<summary>🎯 Try implementing the sophisticated builder yourself</summary>

```java
import java.util.*;

// Self-bounded generic configuration builder
public abstract class ConfigBuilder<T extends ConfigBuilder<T>> {
    protected String name;
    protected Map<String, Object> properties = new HashMap<>();
    protected boolean enabled = true;

    public T withName(String name) {
        this.name = name;
        return self();
    }

    public T withProperty(String key, Object value) {
        this.properties.put(key, value);
        return self();
    }

    public T enabled(boolean enabled) {
        this.enabled = enabled;
        return self();
    }

    protected abstract T self();

    // Template method with validation
    public final Config build() {
        validate();
        return createConfig();
    }

    protected void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Name is required");
        }
    }

    protected abstract Config createConfig();
}

// Database configuration with specific properties
public final class DatabaseConfig extends ConfigBuilder<DatabaseConfig> {
    private String url;
    private String username;
    private String password;
    private int maxConnections = 10;

    public DatabaseConfig withUrl(String url) {
        this.url = url;
        return self();
    }

    public DatabaseConfig withCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        return self();
    }

    public DatabaseConfig withMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return self();
    }

    @Override
    protected DatabaseConfig self() {
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (url == null) {
            throw new IllegalStateException("Database URL is required");
        }
    }

    @Override
    protected Config createConfig() {
        return new Config(name, properties, enabled,
                         Map.of("url", url, "username", username,
                               "password", password, "maxConnections", maxConnections));
    }
}

// Cache configuration with specific properties
public final class CacheConfig extends ConfigBuilder<CacheConfig> {
    private long ttlSeconds = 3600;
    private int maxSize = 1000;
    private String evictionPolicy = "LRU";

    public CacheConfig withTTL(long seconds) {
        this.ttlSeconds = seconds;
        return self();
    }

    public CacheConfig withMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return self();
    }

    public CacheConfig withEvictionPolicy(String policy) {
        this.evictionPolicy = policy;
        return self();
    }

    @Override
    protected CacheConfig self() {
        return this;
    }

    @Override
    protected Config createConfig() {
        return new Config(name, properties, enabled,
                         Map.of("ttl", ttlSeconds, "maxSize", maxSize,
                               "evictionPolicy", evictionPolicy));
    }
}

// Immutable configuration result
public final class Config {
    private final String name;
    private final Map<String, Object> properties;
    private final boolean enabled;
    private final Map<String, Object> specificProperties;

    public Config(String name, Map<String, Object> properties,
                  boolean enabled, Map<String, Object> specificProperties) {
        this.name = name;
        this.properties = Map.copyOf(properties);
        this.enabled = enabled;
        this.specificProperties = Map.copyOf(specificProperties);
    }

    // Getters and toString...
    public String getName() { return name; }
    public Map<String, Object> getProperties() { return properties; }
    public boolean isEnabled() { return enabled; }
    public Map<String, Object> getSpecificProperties() { return specificProperties; }

    @Override
    public String toString() {
        return String.format("Config{name='%s', enabled=%s, properties=%s, specific=%s}",
                           name, enabled, properties, specificProperties);
    }
}

// Test the sophisticated builder pattern
class AdvancedBuilderTest {
    public static void main(String[] args) {
        // Database configuration with perfect chaining
        Config dbConfig = new DatabaseConfig()
            .withName("primary-db")           // ConfigBuilder method
            .withProperty("timeout", 30)     // ConfigBuilder method
            .enabled(true)                   // ConfigBuilder method
            .withUrl("jdbc:postgresql://localhost:5432/mydb")  // DatabaseConfig method
            .withCredentials("user", "pass") // DatabaseConfig method
            .withMaxConnections(20)          // DatabaseConfig method
            .build();                        // Perfect fluent chaining!

        // Cache configuration with perfect chaining
        Config cacheConfig = new CacheConfig()
            .withName("user-cache")          // ConfigBuilder method
            .withProperty("region", "us-east-1") // ConfigBuilder method
            .enabled(true)                   // ConfigBuilder method
            .withTTL(1800)                   // CacheConfig method
            .withMaxSize(5000)               // CacheConfig method
            .withEvictionPolicy("LFU")       // CacheConfig method
            .build();                        // Perfect fluent chaining!

        System.out.println("Database Config: " + dbConfig);
        System.out.println("Cache Config: " + cacheConfig);
    }
}
```

**🎉 CRTP Benefits Achieved:**
- ✅ **Perfect Type Safety**: No casting required
- ✅ **Fluent Chaining**: Methods return exact subtype
- ✅ **Extensible**: Easy to add new configuration types
- ✅ **Maintainable**: Common logic in base class
- ✅ **Compile-Time Guarantees**: Invalid chains caught at compile time

</details>

---

## 🛡️ Safe Varargs with Generics

### ⚠️ The Heap Pollution Problem

Generic varargs can cause "heap pollution" - when a variable of parameterized type refers to an object that's not of that type:

```java
// This method is DANGEROUS!
public static <T> void dangerous(List<T>... lists) {
    Object[] array = lists;           // Allowed by array covariance
    array[0] = Arrays.asList(42);     // Heap pollution!
    T item = lists[0].get(0);         // ClassCastException at runtime!
}
```

### ✅ Safe Varargs Patterns

Use `@SafeVarargs` only on methods that are provably safe:

```java
public class SafeVarargsExamples {

    // ✅ SAFE: Only reads from varargs array, doesn't store it
    @SafeVarargs
    public static <T> List<T> of(T... items) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            result.add(item);  // Safe: only reading from varargs
        }
        return result;
    }

    // ✅ SAFE: Doesn't expose the varargs array
    @SafeVarargs
    public static <T> void addAll(Collection<T> target, T... items) {
        Collections.addAll(target, items);  // Safe delegation
    }

    // ❌ UNSAFE: Stores the varargs array (potential for heap pollution)
    private static List<Object[]> cache = new ArrayList<>();

    public static <T> void unsafe(T... items) {
        cache.add(items);  // DON'T DO THIS - stores generic array
    }

    // ✅ SAFE: Creates new array, doesn't store original
    @SafeVarargs
    public static <T> T[] toArray(T... items) {
        return Arrays.copyOf(items, items.length);  // Safe copy
    }
}

// Usage examples
class SafeVarargsTest {
    public static void main(String[] args) {
        // All these are safe
        List<String> strings = SafeVarargsExamples.of("a", "b", "c");
        List<Integer> numbers = SafeVarargsExamples.of(1, 2, 3);

        Set<String> stringSet = new HashSet<>();
        SafeVarargsExamples.addAll(stringSet, "hello", "world");

        String[] stringArray = SafeVarargsExamples.toArray("x", "y", "z");

        System.out.println("Strings: " + strings);
        System.out.println("Numbers: " + numbers);
        System.out.println("String set: " + stringSet);
        System.out.println("String array: " + Arrays.toString(stringArray));
    }
}
```

### 🎯 Hands-On Exercise 2: Safe Generic Utilities

Create safe generic utility methods with varargs:

```java
// TODO: Implement safe generic utility methods
public class GenericVarargsUtils {

    // TODO: Safe method to find first non-null element
    @SafeVarargs
    public static <T> Optional<T> findFirst(T... items) {

    }

    // TODO: Safe method to create immutable set from varargs
    @SafeVarargs
    public static <T> Set<T> setOf(T... items) {

    }

    // TODO: Safe method to concatenate multiple collections
    @SafeVarargs
    public static <T> List<T> concat(Collection<? extends T>... collections) {

    }

    // TODO: Safe method to apply function to all varargs elements
    @SafeVarargs
    public static <T, R> List<R> mapAll(Function<T, R> mapper, T... items) {

    }
}
```

<details>
<summary>🛡️ Try implementing safe varargs utilities yourself</summary>

```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenericVarargsUtils {

    // Safe: Only reads from varargs, doesn't store or expose array
    @SafeVarargs
    public static <T> Optional<T> findFirst(T... items) {
        for (T item : items) {
            if (item != null) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    // Safe: Creates new collection, doesn't store varargs array
    @SafeVarargs
    public static <T> Set<T> setOf(T... items) {
        Set<T> result = new HashSet<>();
        Collections.addAll(result, items);
        return Collections.unmodifiableSet(result);
    }

    // Safe: Only reads from collections, doesn't store them
    @SafeVarargs
    public static <T> List<T> concat(Collection<? extends T>... collections) {
        List<T> result = new ArrayList<>();
        for (Collection<? extends T> collection : collections) {
            result.addAll(collection);
        }
        return result;
    }

    // Safe: Only reads from varargs, applies function safely
    @SafeVarargs
    public static <T, R> List<R> mapAll(Function<T, R> mapper, T... items) {
        return Stream.of(items)
                    .map(mapper)
                    .collect(Collectors.toList());
    }

    // Safe: Statistical operations on varargs
    @SafeVarargs
    public static <T extends Number> OptionalDouble average(T... numbers) {
        if (numbers.length == 0) {
            return OptionalDouble.empty();
        }

        double sum = 0.0;
        for (T number : numbers) {
            sum += number.doubleValue();
        }
        return OptionalDouble.of(sum / numbers.length);
    }

    // Safe: Filtering with predicate
    @SafeVarargs
    public static <T> List<T> filter(java.util.function.Predicate<T> predicate, T... items) {
        return Stream.of(items)
                    .filter(predicate)
                    .collect(Collectors.toList());
    }
}

// Comprehensive test
class SafeVarargsTest {
    public static void main(String[] args) {
        // Test findFirst
        Optional<String> first = GenericVarargsUtils.findFirst(null, "hello", "world");
        System.out.println("First non-null: " + first.orElse("none"));

        // Test setOf with different types
        Set<Integer> numbers = GenericVarargsUtils.setOf(1, 2, 3, 2, 1);
        Set<String> words = GenericVarargsUtils.setOf("hello", "world", "hello");
        System.out.println("Number set: " + numbers);
        System.out.println("Word set: " + words);

        // Test concat with different collection types
        List<String> list1 = Arrays.asList("a", "b");
        Set<String> set1 = Set.of("c", "d");
        Collection<String> collection1 = Arrays.asList("e", "f");

        List<String> concatenated = GenericVarargsUtils.concat(list1, set1, collection1);
        System.out.println("Concatenated: " + concatenated);

        // Test mapAll with transformation
        List<Integer> lengths = GenericVarargsUtils.mapAll(
            String::length,
            "hello", "world", "java", "generics"
        );
        System.out.println("String lengths: " + lengths);

        // Test average with different number types
        OptionalDouble intAverage = GenericVarargsUtils.average(1, 2, 3, 4, 5);
        OptionalDouble doubleAverage = GenericVarargsUtils.average(1.5, 2.5, 3.5);

        System.out.println("Integer average: " + intAverage.orElse(0.0));
        System.out.println("Double average: " + doubleAverage.orElse(0.0));

        // Test filter
        List<String> longWords = GenericVarargsUtils.filter(
            s -> s.length() > 4,
            "hi", "hello", "world", "java", "ok"
        );
        System.out.println("Long words: " + longWords);
    }
}
```

**🛡️ Safety Principles Applied:**
- ✅ **No Array Storage**: Never store the varargs array
- ✅ **No Array Exposure**: Never return or expose the varargs array
- ✅ **Read-Only Access**: Only read from varargs elements
- ✅ **Safe Delegation**: Use safe library methods like Collections.addAll()
- ✅ **Proper Annotation**: Use @SafeVarargs only when provably safe

</details>

---

## 🗃️ Type-Safe Heterogeneous Containers

### 🎯 The Challenge

Sometimes you need a container that can hold different types safely, like a type-safe map where keys determine value types:

```java
// We want something like this, but type-safe:
Map<String, Object> properties = new HashMap<>();
properties.put("name", "John");        // String value
properties.put("age", 25);             // Integer value
properties.put("active", true);        // Boolean value

// But retrieving requires unsafe casting:
String name = (String) properties.get("name");  // Unsafe!
Integer age = (Integer) properties.get("age");  // Unsafe!
```

### 🏆 Type Token Solution

Use `Class<T>` as the key to ensure type safety:

```java
import java.util.*;

// Type-safe heterogeneous container
public class TypeSafeMap {
    private final Map<Class<?>, Object> map = new HashMap<>();

    // Type-safe put method
    public <T> void put(Class<T> type, T instance) {
        map.put(Objects.requireNonNull(type),
               type.cast(instance)); // Runtime type check
    }

    // Type-safe get method
    public <T> T get(Class<T> type) {
        return type.cast(map.get(type)); // Safe cast using Class.cast()
    }

    // Check if type is present
    public boolean contains(Class<?> type) {
        return map.containsKey(type);
    }

    // Remove by type
    public <T> T remove(Class<T> type) {
        return type.cast(map.remove(type));
    }

    // Get all stored types
    public Set<Class<?>> getTypes() {
        return Collections.unmodifiableSet(map.keySet());
    }
}

// Usage example
class TypeSafeMapExample {
    public static void main(String[] args) {
        TypeSafeMap container = new TypeSafeMap();

        // Type-safe storage
        container.put(String.class, "John Doe");
        container.put(Integer.class, 25);
        container.put(Boolean.class, true);
        container.put(Double.class, 98.6);

        // Type-safe retrieval - no casting needed!
        String name = container.get(String.class);      // Returns String
        Integer age = container.get(Integer.class);     // Returns Integer
        Boolean active = container.get(Boolean.class);  // Returns Boolean
        Double temperature = container.get(Double.class); // Returns Double

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Active: " + active);
        System.out.println("Temperature: " + temperature);

        // Type safety enforced at compile time
        // container.put(String.class, 42);  // ❌ Compilation error!

        System.out.println("Stored types: " + container.getTypes());
    }
}
```

### 🎯 Hands-On Exercise 3: Advanced Heterogeneous Container

Create a sophisticated configuration system using type-safe heterogeneous containers:

```java
// TODO: Create an advanced configuration system
public class ConfigurationManager {

    // TODO: Use type-safe heterogeneous container for storage

    // TODO: Add support for default values
    public <T> T get(Class<T> type, T defaultValue) {

    }

    // TODO: Add support for nested configuration sections
    public ConfigurationSection getSection(String name) {

    }

    // TODO: Add validation support
    public <T> void put(Class<T> type, T value, Validator<T> validator) {

    }

    // TODO: Add serialization support
    public Map<String, Object> toMap() {

    }
}

// TODO: Create configuration section for hierarchical configs
public class ConfigurationSection {

}

// TODO: Create validator interface
public interface Validator<T> {
    boolean isValid(T value);
    String getErrorMessage();
}
```

<details>
<summary>🗃️ Try building the advanced configuration system yourself</summary>

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

// Advanced configuration manager with type safety
public class ConfigurationManager {
    private final Map<Class<?>, Object> values = new ConcurrentHashMap<>();
    private final Map<Class<?>, Validator<?>> validators = new ConcurrentHashMap<>();
    private final Map<String, ConfigurationSection> sections = new ConcurrentHashMap<>();

    // Type-safe put with optional validation
    public <T> void put(Class<T> type, T value) {
        put(type, value, null);
    }

    @SuppressWarnings("unchecked")
    public <T> void put(Class<T> type, T value, Validator<T> validator) {
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");

        // Validate if validator provided
        if (validator != null) {
            if (!validator.isValid(value)) {
                throw new IllegalArgumentException(
                    "Invalid value for " + type.getSimpleName() + ": " + validator.getErrorMessage()
                );
            }
            validators.put(type, validator);
        }

        values.put(type, type.cast(value));
    }

    // Type-safe get
    public <T> T get(Class<T> type) {
        return type.cast(values.get(type));
    }

    // Type-safe get with default
    public <T> T get(Class<T> type, T defaultValue) {
        T value = get(type);
        return value != null ? value : defaultValue;
    }

    // Type-safe get with supplier for lazy defaults
    public <T> T get(Class<T> type, Supplier<T> defaultSupplier) {
        T value = get(type);
        return value != null ? value : defaultSupplier.get();
    }

    // Check if configuration exists
    public boolean contains(Class<?> type) {
        return values.containsKey(type);
    }

    // Remove configuration
    public <T> T remove(Class<T> type) {
        validators.remove(type);
        return type.cast(values.remove(type));
    }

    // Get or create configuration section
    public ConfigurationSection getSection(String name) {
        return sections.computeIfAbsent(name, k -> new ConfigurationSection());
    }

    // Validate all stored values
    @SuppressWarnings("unchecked")
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        for (Map.Entry<Class<?>, Validator<?>> entry : validators.entrySet()) {
            Class<?> type = entry.getKey();
            Validator<Object> validator = (Validator<Object>) entry.getValue();
            Object value = values.get(type);

            if (value != null && !validator.isValid(value)) {
                errors.add(type.getSimpleName() + ": " + validator.getErrorMessage());
            }
        }

        return errors;
    }

    // Export to map for serialization
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<Class<?>, Object> entry : values.entrySet()) {
            result.put(entry.getKey().getSimpleName(), entry.getValue());
        }

        for (Map.Entry<String, ConfigurationSection> entry : sections.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toMap());
        }

        return result;
    }

    // Get all configured types
    public Set<Class<?>> getConfiguredTypes() {
        return Collections.unmodifiableSet(values.keySet());
    }

    // Clear all configuration
    public void clear() {
        values.clear();
        validators.clear();
        sections.clear();
    }
}

// Configuration section for hierarchical organization
public class ConfigurationSection {
    private final ConfigurationManager manager = new ConfigurationManager();

    public <T> void put(Class<T> type, T value) {
        manager.put(type, value);
    }

    public <T> void put(Class<T> type, T value, Validator<T> validator) {
        manager.put(type, value, validator);
    }

    public <T> T get(Class<T> type) {
        return manager.get(type);
    }

    public <T> T get(Class<T> type, T defaultValue) {
        return manager.get(type, defaultValue);
    }

    public boolean contains(Class<?> type) {
        return manager.contains(type);
    }

    public ConfigurationSection getSection(String name) {
        return manager.getSection(name);
    }

    public Map<String, Object> toMap() {
        return manager.toMap();
    }

    public List<String> validate() {
        return manager.validate();
    }
}

// Validator interface for type-safe validation
public interface Validator<T> {
    boolean isValid(T value);
    String getErrorMessage();

    // Utility factory methods
    static <T extends Comparable<T>> Validator<T> range(T min, T max) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
            }

            @Override
            public String getErrorMessage() {
                return "Value must be between " + min + " and " + max;
            }
        };
    }

    static Validator<String> minLength(int minLength) {
        return new Validator<String>() {
            @Override
            public boolean isValid(String value) {
                return value != null && value.length() >= minLength;
            }

            @Override
            public String getErrorMessage() {
                return "String must be at least " + minLength + " characters";
            }
        };
    }

    static <T> Validator<T> notNull() {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value != null;
            }

            @Override
            public String getErrorMessage() {
                return "Value cannot be null";
            }
        };
    }
}

// Comprehensive test
class AdvancedConfigurationTest {
    public static void main(String[] args) {
        ConfigurationManager config = new ConfigurationManager();

        // Basic type-safe configuration
        config.put(String.class, "MyApplication");
        config.put(Integer.class, 8080, Validator.range(1024, 65535));
        config.put(Boolean.class, true);
        config.put(Double.class, 99.5);

        // Configuration with validation
        try {
            config.put(String.class, "test", Validator.minLength(5));
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        // Hierarchical configuration
        ConfigurationSection dbSection = config.getSection("database");
        dbSection.put(String.class, "jdbc:postgresql://localhost:5432/mydb");
        dbSection.put(Integer.class, 10, Validator.range(1, 100));

        ConfigurationSection cacheSection = config.getSection("cache");
        cacheSection.put(Long.class, 3600L);
        cacheSection.put(Boolean.class, true);

        // Type-safe retrieval
        String appName = config.get(String.class);
        Integer port = config.get(Integer.class);
        Boolean debug = config.get(Boolean.class, false); // with default

        String dbUrl = dbSection.get(String.class);
        Integer maxConnections = dbSection.get(Integer.class);

        // Display configuration
        System.out.println("Application: " + appName);
        System.out.println("Port: " + port);
        System.out.println("Debug: " + debug);
        System.out.println("DB URL: " + dbUrl);
        System.out.println("Max Connections: " + maxConnections);

        // Validate all configurations
        List<String> errors = config.validate();
        if (errors.isEmpty()) {
            System.out.println("All configurations are valid!");
        } else {
            System.out.println("Validation errors: " + errors);
        }

        // Export to map
        System.out.println("Configuration map: " + config.toMap());

        // Show configured types
        System.out.println("Configured types: " + config.getConfiguredTypes());
    }
}
```

**🗃️ Advanced Features Achieved:**
- ✅ **Type Safety**: No unsafe casts, compile-time type checking
- ✅ **Validation**: Type-safe validators with meaningful error messages
- ✅ **Hierarchical**: Nested configuration sections
- ✅ **Default Values**: Support for defaults and lazy suppliers
- ✅ **Serialization**: Export to standard Map for JSON/XML serialization
- ✅ **Thread Safety**: Concurrent access with ConcurrentHashMap
- ✅ **Extensible**: Easy to add new types and validators

</details>

---

## 📚 Module 3 Summary

### 🎉 What You've Mastered

- **Type Erasure**: Understanding how generics work at runtime
- **Type Tokens**: Preserving generic type information at runtime
- **CRTP Pattern**: Self-bounded generics for fluent APIs
- **Safe Varargs**: Avoiding heap pollution with generic varargs
- **Heterogeneous Containers**: Type-safe storage of different types
- **Advanced Patterns**: Production-ready generic programming techniques

### 🔑 Key Takeaways

1. **Type erasure affects runtime behavior** - use type tokens when needed
2. **CRTP enables perfect fluent APIs** - return exact subtype in chains
3. **@SafeVarargs requires careful consideration** - only for provably safe methods
4. **Class<T> as key enables type-safe heterogeneous containers**
5. **Advanced patterns solve real-world problems** - not just academic exercises

### 🚀 Next Steps

You're now ready for **Module 4: Real-World Applications**, where you'll learn:
- Integration with modern Java features (Records, sealed types)
- Serialization with generic types
- Performance considerations and optimizations
- Migration strategies for legacy codebases

### 📝 Self-Assessment Checklist

Before proceeding, ensure you can:

- [ ] Explain type erasure and its implications
- [ ] Implement CRTP for fluent builders
- [ ] Create safe generic varargs methods
- [ ] Design type-safe heterogeneous containers
- [ ] Apply type tokens for runtime type information
- [ ] Avoid heap pollution and unsafe operations

**Ready for real-world applications?** Continue to [Module 4: Real-World Applications](./module-4-realworld.html) 🌍

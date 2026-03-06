title=Module 2: Wildcards & PECS - Variance and Flexible APIs
type=course-module
status=published
date=2025-09-13
author=MyRobot
module=2
duration=3-4 hours
difficulty=Intermediate
tags=java, generics
~~~~~~

## 📖 Module Overview

Welcome to the most powerful aspect of Java Generics! In this module, you'll master wildcards and the PECS principle to create flexible, reusable APIs that work with families of related types. This is where generics truly shine in real-world applications.

### 🎯 Learning Objectives

By the end of this module, you will:

- **Understand** covariance, contravariance, and invariance in Java
- **Master** the PECS principle (Producer Extends Consumer Super)
- **Design** flexible APIs using bounded wildcards
- **Implement** wildcard capture patterns for complex operations
- **Apply** variance correctly in method signatures
- **Avoid** common wildcard pitfalls and limitations

### ⏱️ Estimated Time: 3-4 hours

---

## 🤔 The Variance Problem

### 🔒 Java Generics are Invariant

Let's start with a surprising fact that confuses many Java developers:

```java
// This seems logical but doesn't work!
List<String> strings = Arrays.asList("hello", "world");
List<Object> objects = strings; // ❌ COMPILATION ERROR!

// Even though String extends Object, this fails:
// List<String> is NOT a subtype of List<Object>
```

### 🧠 **Knowledge Check**: Why Doesn't This Work?

Think about what could go wrong if Java allowed the assignment above...

<details>
<summary>🤔 Consider the implications, then click to reveal</summary>

**The Problem**: If `List<String>` were a subtype of `List<Object>`, this would be possible:

```java
List<String> strings = Arrays.asList("hello", "world");
List<Object> objects = strings; // If this were allowed...
objects.add(42); // We could add an Integer to a String list!
String s = strings.get(2); // ClassCastException at runtime!
```

**Key Insight**: Generics are invariant for **write safety**. If you can write to a collection, the type must be exact to prevent corruption.

</details>

### 🎯 The Solution: Wildcards

Wildcards provide **controlled variance** - flexibility where safe, restrictions where necessary:

```java
// ✅ This works! Read-only access to String list as Object list
List<String> strings = Arrays.asList("hello", "world");
List<? extends Object> objects = strings; // Covariant - safe for reading

// ✅ This also works! Write-only access
List<Object> objectList = new ArrayList<>();
List<? super String> strings2 = objectList; // Contravariant - safe for writing
```

---

## 📚 Understanding Wildcards

### 🔍 The Three Types of Wildcards

```java
public class WildcardTypes {

    // 1. Unbounded wildcard - read-only access
    public static void printSize(List<?> list) {
        System.out.println("Size: " + list.size());
        // Can read as Object, but can't add anything (except null)
    }

    // 2. Upper bounded wildcard - covariant
    public static double sum(List<? extends Number> numbers) {
        double total = 0.0;
        for (Number num : numbers) { // Can read as Number
            total += num.doubleValue();
        }
        return total;
        // Can't add anything (except null)
    }

    // 3. Lower bounded wildcard - contravariant
    public static void addNumbers(List<? super Integer> list) {
        list.add(42);        // Can add Integer
        list.add(100);       // Can add Integer
        // list.add(3.14);   // ❌ Can't add Double
        // Can't read as specific type (only as Object)
    }
}
```

### 🎨 Visual Memory Aid

```
? extends T    →  📖 READING  →  "Producer Extends"
      ↑                ↑
   Covariant      Can read T and subtypes

? super T      →  ✍️ WRITING  →  "Consumer Super"
      ↑                ↑
 Contravariant   Can write T and subtypes
```

---

## 🏆 The PECS Principle

### 📜 Producer Extends, Consumer Super

**PECS** is your golden rule for wildcard usage:

- **Producer Extends**: Use `? extends T` when you **read from** the collection
- **Consumer Super**: Use `? super T` when you **write to** the collection

### 🎯 PECS in Action

```java
public class PECSExamples {

    // PRODUCER: We read from source → use extends
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {        // Reading from src (producer)
            dest.add(item);         // Writing to dest (consumer)
        }
    }

    // PRODUCER: We read numbers → use extends
    public static double sum(List<? extends Number> numbers) {
        return numbers.stream()     // Reading from numbers (producer)
                     .mapToDouble(Number::doubleValue)
                     .sum();
    }

    // CONSUMER: We write items → use super
    public static <T> void addAll(Collection<? super T> target, T... items) {
        for (T item : items) {
            target.add(item);       // Writing to target (consumer)
        }
    }

    // Usage demonstration
    public static void demonstratePECS() {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        List<Number> numbers = new ArrayList<>();

        // Copy integers to numbers (Integer → Number)
        copy(numbers, integers);    // dest: super Integer, src: extends Integer

        // Sum different numeric types
        double intSum = sum(integers);    // extends Number ← Integer
        double doubleSum = sum(doubles);  // extends Number ← Double

        // Add to number collection
        addAll(numbers, 10, 20, 30);     // super Integer ← Number
    }
}
```

### 💡 **Learning Reinforcement**: PECS Memory Tricks

- **"PE"** → **P**roducer **E**xtends → You're **P**ulling **E**lements out
- **"CS"** → **C**onsumer **S**uper → You're **C**ramming **S**tuff in
- **Reading = Extends**, **Writing = Super**

---

## 🎯 Hands-On Exercise 1: API Design with PECS

### 📋 Your Mission

Design a flexible `CollectionUtils` class with methods that work with different but related types:

```java
import java.util.*;
import java.util.function.*;

// TODO: Apply PECS principle to make these methods flexible
public class CollectionUtils {

    // TODO: Make this work with any Number subtype
    public static double average(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // TODO: Make this work with any comparable type and any collection
    public static Integer findMax(List<Integer> items) {
        return Collections.max(items);
    }

    // TODO: Make this accept any supertype collection for writing
    public static void fill(List<String> list, String value, int count) {
        for (int i = 0; i < count; i++) {
            list.add(value);
        }
    }

    // TODO: Make this work with any related types
    public static List<String> transform(List<Integer> source, Function<Integer, String> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }

    // TODO: Make this work with any collection types
    public static boolean hasCommonElements(Set<String> set1, Set<String> set2) {
        for (String item : set1) {
            if (set2.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
```

### 🎯 Requirements

1. **Apply PECS principle** to all method parameters
2. **Maximize flexibility** while maintaining type safety
3. **Use appropriate wildcards** for producers and consumers
4. **Test with different but related types**

### 💡 Solution with Explanations

<details>
<summary>🔍 Try designing the APIs yourself first</summary>

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class FlexibleCollectionUtils {

    // PRODUCER: Reading numbers → extends
    public static double average(List<? extends Number> numbers) {
        return numbers.stream()
                     .mapToDouble(Number::doubleValue)
                     .average()
                     .orElse(0.0);
    }

    // PRODUCER: Reading comparables → extends
    public static <T extends Comparable<? super T>> T findMax(Collection<? extends T> items) {
        return Collections.max(items);
    }

    // CONSUMER: Writing to collection → super
    public static <T> void fill(Collection<? super T> collection, T value, int count) {
        for (int i = 0; i < count; i++) {
            collection.add(value);
        }
    }

    // PRODUCER + flexible transformation
    public static <T, R> List<R> transform(
            Collection<? extends T> source,           // Producer: extends
            Function<? super T, ? extends R> mapper   // Flexible function
    ) {
        return source.stream()
                    .map(mapper)
                    .collect(Collectors.toList());
    }

    // PRODUCER: Both collections are read from → extends
    public static <T> boolean hasCommonElements(
            Collection<? extends T> collection1,
            Collection<? extends T> collection2
    ) {
        for (T item : collection1) {
            if (collection2.contains(item)) {
                return true;
            }
        }
        return false;
    }

    // Bonus: Flexible copy method
    public static <T> void copyAll(
            Collection<? super T> destination,    // Consumer: super
            Collection<? extends T> source        // Producer: extends
    ) {
        destination.addAll(source);
    }
}

// Test the flexibility
class FlexibilityTest {
    public static void main(String[] args) {
        // Test average with different Number types
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        List<Float> floats = Arrays.asList(1.0f, 2.0f, 3.0f);

        System.out.println("Int average: " + FlexibleCollectionUtils.average(integers));
        System.out.println("Double average: " + FlexibleCollectionUtils.average(doubles));
        System.out.println("Float average: " + FlexibleCollectionUtils.average(floats));

        // Test findMax with different comparable types
        String maxString = FlexibleCollectionUtils.findMax(Arrays.asList("apple", "zebra", "banana"));
        Integer maxInt = FlexibleCollectionUtils.findMax(integers);

        // Test fill with supertype collections
        List<Object> objects = new ArrayList<>();
        FlexibleCollectionUtils.fill(objects, "hello", 3);  // String → Object

        List<Number> numbers = new ArrayList<>();
        FlexibleCollectionUtils.fill(numbers, 42, 2);       // Integer → Number

        // Test transform with different types
        List<String> stringLengths = FlexibleCollectionUtils.transform(
            Arrays.asList("hello", "world"),
            String::length
        );

        // Test common elements
        Set<String> set1 = Set.of("a", "b", "c");
        Set<CharSequence> set2 = Set.of("b", "d", "e");  // CharSequence ← String
        boolean hasCommon = FlexibleCollectionUtils.hasCommonElements(set1, set2);
    }
}
```

**🎉 Benefits Achieved:**
- ✅ **Maximum Flexibility**: Methods work with families of related types
- ✅ **Type Safety**: No unsafe casts or runtime errors
- ✅ **PECS Applied**: Correct variance for producers and consumers
- ✅ **Reusable**: One method handles multiple scenarios

**🧠 Key Insights:**
- `? extends` for parameters you **read from** (producers)
- `? super` for parameters you **write to** (consumers)
- Wildcards in functional interfaces increase flexibility
- Bounded type parameters work with wildcards for complex constraints

</details>

---

## 🔄 Wildcard Capture

### 🎭 The Capture Problem

Sometimes you need to perform operations that require knowing the exact type, but you're working with wildcards:

```java
// This won't compile - can't assign ? to ?
public static void swap(List<?> list, int i, int j) {
    Object temp = list.get(i);
    list.set(i, list.get(j));  // ❌ Can't put Object into List<?>
    list.set(j, temp);
}
```

### 🔓 The Capture Solution

Use a helper method to "capture" the wildcard type:

```java
public class WildcardCapture {

    // Public API with wildcard
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);  // Compiler captures ? as some type T
    }

    // Private helper with captured type parameter
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);    // Now we can work with concrete type T
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Another example: reverse any list
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }

    private static <T> void reverseHelper(List<T> list) {
        Collections.reverse(list);  // Works with concrete type T
    }

    // Usage
    public static void demonstrateCapture() {
        List<String> strings = Arrays.asList("a", "b", "c");
        List<Integer> numbers = Arrays.asList(1, 2, 3);

        swap(strings, 0, 2);    // Works with any List type
        swap(numbers, 0, 1);    // Flexible and type-safe

        reverse(strings);       // ["c", "b", "a"]
        reverse(numbers);       // [2, 1, 3]
    }
}
```

### 💡 **Key Insight**: Wildcard Capture Pattern

1. **Public method** uses wildcards for flexibility
2. **Private helper** uses type parameter for operations
3. **Compiler captures** the wildcard as a concrete type
4. **Best of both worlds**: Flexibility + Type Safety

---

## 🎯 Hands-On Exercise 2: Advanced Wildcard Patterns

### 📋 Your Mission

Implement advanced utility methods that require wildcard capture and complex variance:

```java
import java.util.*;
import java.util.function.*;

public class AdvancedWildcardUtils {

    // TODO: Implement a method that can shuffle any type of list
    public static void shuffle(List<?> list) {
        // Hint: Use wildcard capture pattern
    }

    // TODO: Implement a method that finds common elements between collections
    // Should work with related types (e.g., List<String> and Set<CharSequence>)
    public static <T> List<T> findCommon(
        Collection<?> collection1,
        Collection<?> collection2
    ) {
        // Hint: This is tricky - what should T be?
    }

    // TODO: Implement a method that merges sorted lists
    public static <T extends Comparable<? super T>> List<T> mergeSorted(
        List<? extends T> list1,
        List<? extends T> list2
    ) {
        // Should preserve order and handle different subtypes
    }

    // TODO: Implement a method that applies a transformation conditionally
    public static <T, R> void transformIf(
        Collection<? extends T> source,
        Collection<? super R> destination,
        Predicate<? super T> condition,
        Function<? super T, ? extends R> transformer
    ) {
        // Apply transformation only to elements matching condition
    }
}
```

### 💡 Advanced Solutions

<details>
<summary>🎯 Try implementing these challenging methods yourself first</summary>

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class AdvancedWildcardUtils {

    // Wildcard capture for shuffle
    public static void shuffle(List<?> list) {
        shuffleHelper(list);
    }

    private static <T> void shuffleHelper(List<T> list) {
        Collections.shuffle(list);
    }

    // Complex wildcard handling for common elements
    @SuppressWarnings("unchecked")
    public static <T> List<T> findCommon(
        Collection<? extends T> collection1,
        Collection<? extends T> collection2
    ) {
        return collection1.stream()
                         .filter(collection2::contains)
                         .collect(Collectors.toList());
    }

    // Merge sorted lists with bounded wildcards
    public static <T extends Comparable<? super T>> List<T> mergeSorted(
        List<? extends T> list1,
        List<? extends T> list2
    ) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < list1.size() && j < list2.size()) {
            T item1 = list1.get(i);
            T item2 = list2.get(j);

            if (item1.compareTo(item2) <= 0) {
                result.add(item1);
                i++;
            } else {
                result.add(item2);
                j++;
            }
        }

        // Add remaining elements
        while (i < list1.size()) {
            result.add(list1.get(i++));
        }
        while (j < list2.size()) {
            result.add(list2.get(j++));
        }

        return result;
    }

    // Complex variance with functional interfaces
    public static <T, R> void transformIf(
        Collection<? extends T> source,
        Collection<? super R> destination,
        Predicate<? super T> condition,
        Function<? super T, ? extends R> transformer
    ) {
        for (T item : source) {
            if (condition.test(item)) {
                R transformed = transformer.apply(item);
                destination.add(transformed);
            }
        }
    }

    // Bonus: Generic min/max with custom comparator
    public static <T> Optional<T> findExtreme(
        Collection<? extends T> collection,
        Comparator<? super T> comparator
    ) {
        return collection.stream().min(comparator);
    }
}

// Comprehensive test
class AdvancedWildcardTest {
    public static void main(String[] args) {
        // Test shuffle
        List<String> words = new ArrayList<>(Arrays.asList("hello", "world", "java"));
        AdvancedWildcardUtils.shuffle(words);
        System.out.println("Shuffled: " + words);

        // Test findCommon with related types
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        Set<CharSequence> sequences = Set.of("banana", "date", "elderberry");
        List<CharSequence> common = AdvancedWildcardUtils.findCommon(strings, sequences);
        System.out.println("Common: " + common);

        // Test mergeSorted
        List<Integer> list1 = Arrays.asList(1, 3, 5, 7);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8);
        List<Integer> merged = AdvancedWildcardUtils.mergeSorted(list1, list2);
        System.out.println("Merged: " + merged);

        // Test transformIf
        List<String> source = Arrays.asList("hello", "world", "java", "generics");
        List<Integer> destination = new ArrayList<>();

        AdvancedWildcardUtils.transformIf(
            source,
            destination,
            s -> s.length() > 4,        // Condition: length > 4
            String::length              // Transform: string to length
        );
        System.out.println("Transformed lengths: " + destination);

        // Test with inheritance
        List<Number> numbers = new ArrayList<>();
        List<Integer> integers = Arrays.asList(10, 20, 30);

        AdvancedWildcardUtils.transformIf(
            integers,                   // Source: Integer
            numbers,                    // Destination: Number (super Integer)
            n -> n > 15,               // Condition: > 15
            n -> n * 2                 // Transform: double the value
        );
        System.out.println("Filtered and doubled: " + numbers);
    }
}
```

**🏆 Advanced Patterns Mastered:**
- ✅ **Wildcard Capture**: Convert `List<?>` to workable `List<T>`
- ✅ **Complex Variance**: Multiple wildcards in one signature
- ✅ **Functional Interface Wildcards**: Flexible predicates and functions
- ✅ **Inheritance-Aware APIs**: Work with type hierarchies safely

</details>

---

## 🚫 Common Wildcard Pitfalls

### ❌ Pitfall 1: Wildcard Overuse

```java
// ❌ DON'T overuse wildcards
public static <T> List<T> process(List<? extends T> input) {
    // If you're only reading and returning the same type,
    // wildcards add unnecessary complexity
}

// ✅ Keep it simple when wildcards don't add value
public static <T> List<T> process(List<T> input) {
    // Cleaner and just as flexible
}
```

### ❌ Pitfall 2: Multiple Wildcards Confusion

```java
// ❌ Confusing - what's the relationship between the wildcards?
public static void confusing(List<? extends Number> list1,
                           List<? extends Number> list2) {
    // Are these the same type? Different types? Unclear!
}

// ✅ Be explicit about relationships
public static <T extends Number> void clear(List<T> list1, List<T> list2) {
    // Now it's clear both lists have the same type T
}
```

### ❌ Pitfall 3: Wildcard in Return Types

```java
// ❌ Avoid wildcards in return types - reduces usability
public static List<? extends Number> badReturn() {
    return Arrays.asList(1, 2, 3);
}

// ✅ Return concrete types when possible
public static <T extends Number> List<T> goodReturn(T... items) {
    return Arrays.asList(items);
}
```

---

## 🧪 Knowledge Check: PECS Master Quiz

### Question 1: API Design Challenge

You're designing a method to copy elements from one collection to another. Which signature is correct?

```java
// Option A
public static <T> void copy(List<T> dest, List<T> src)

// Option B
public static <T> void copy(List<? super T> dest, List<? extends T> src)

// Option C
public static <T> void copy(List<? extends T> dest, List<? super T> src)
```

<details>
<summary>🤔 Apply PECS principle, then check your answer</summary>

**Answer**: Option B is correct!

**Reasoning:**
- `dest` is a **consumer** (we write to it) → use `? super T`
- `src` is a **producer** (we read from it) → use `? extends T`

**Benefits:**
```java
List<Integer> integers = Arrays.asList(1, 2, 3);
List<Number> numbers = new ArrayList<>();
copy(numbers, integers); // Works! Number super Integer, Integer extends Integer
```

</details>

### Question 2: Wildcard Capture

What's wrong with this code and how would you fix it?

```java
public static void reverse(List<?> list) {
    for (int i = 0; i < list.size() / 2; i++) {
        Object temp = list.get(i);
        list.set(i, list.get(list.size() - 1 - i));  // ❌ Compilation error
        list.set(list.size() - 1 - i, temp);
    }
}
```

<details>
<summary>🔧 Identify the problem and solution</summary>

**Problem**: Can't put `Object` into `List<?>` because `?` might be a more specific type.

**Solution**: Use wildcard capture pattern:

```java
public static void reverse(List<?> list) {
    reverseHelper(list);
}

private static <T> void reverseHelper(List<T> list) {
    for (int i = 0; i < list.size() / 2; i++) {
        T temp = list.get(i);  // Now we can work with concrete type T
        list.set(i, list.get(list.size() - 1 - i));
        list.set(list.size() - 1 - i, temp);
    }
}
```

</details>

### Question 3: Complex Variance

Which method signature allows maximum flexibility for adding elements to a collection?

```java
// Option A
public static <T> void addItems(Collection<T> collection, T... items)

// Option B
public static <T> void addItems(Collection<? extends T> collection, T... items)

// Option C
public static <T> void addItems(Collection<? super T> collection, T... items)
```

<details>
<summary>🎯 Think about what operations you need to perform</summary>

**Answer**: Option C is correct!

**Reasoning:**
- We need to **write** items to the collection (consumer)
- Consumer → use `? super T`
- This allows passing a `Collection<Object>` when adding `String` items

**Example:**
```java
List<Object> objects = new ArrayList<>();
addItems(objects, "hello", "world"); // String items → Object collection
```

</details>

---

## 🎯 Module 2 Capstone Project: Generic Algorithm Library

### 📋 Final Challenge

Create a comprehensive algorithm library that demonstrates mastery of wildcards and PECS:

```java
import java.util.*;
import java.util.function.*;

// TODO: Complete this generic algorithm library
public class GenericAlgorithms {

    // TODO: Binary search that works with any comparable type and collection
    public static <T> int binarySearch(???, T target) {

    }

    // TODO: Merge multiple sorted collections into one
    public static <T> List<T> mergeAll(???) {

    }

    // TODO: Partition collection based on predicate
    public static <T> Map<Boolean, List<T>> partition(???, ???) {

    }

    // TODO: Find all elements that appear in all collections
    public static <T> Set<T> intersection(???) {

    }

    // TODO: Transform and filter in one operation
    public static <T, R> List<R> mapFilter(???, ???, ???) {

    }
}
```

### 🎯 Requirements

1. **Apply PECS correctly** in all method signatures
2. **Use wildcard capture** where needed
3. **Support inheritance hierarchies** (e.g., work with Number subtypes)
4. **Maximize flexibility** while maintaining type safety
5. **Include comprehensive tests** showing flexibility

### 💡 Complete Solution

<details>
<summary>🏆 Try implementing the complete library yourself first</summary>

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class GenericAlgorithms {

    // Binary search with flexible input
    public static <T extends Comparable<? super T>> int binarySearch(
        List<? extends T> list, T target
    ) {
        return Collections.binarySearch(list, target);
    }

    // Merge multiple sorted collections
    @SafeVarargs
    public static <T extends Comparable<? super T>> List<T> mergeAll(
        Collection<? extends T>... collections
    ) {
        return Arrays.stream(collections)
                    .flatMap(Collection::stream)
                    .sorted()
                    .collect(Collectors.toList());
    }

    // Partition with flexible predicate
    public static <T> Map<Boolean, List<T>> partition(
        Collection<? extends T> collection,
        Predicate<? super T> predicate
    ) {
        return collection.stream()
                        .collect(Collectors.partitioningBy(predicate));
    }

    // Intersection of multiple collections
    @SafeVarargs
    public static <T> Set<T> intersection(Collection<? extends T>... collections) {
        if (collections.length == 0) {
            return Collections.emptySet();
        }

        Set<T> result = new HashSet<>(collections[0]);
        for (int i = 1; i < collections.length; i++) {
            result.retainAll(collections[i]);
        }
        return result;
    }

    // Transform and filter in one operation
    public static <T, R> List<R> mapFilter(
        Collection<? extends T> source,
        Predicate<? super T> filter,
        Function<? super T, ? extends R> mapper
    ) {
        return source.stream()
                    .filter(filter)
                    .map(mapper)
                    .collect(Collectors.toList());
    }

    // Bonus: Generic quicksort with custom comparator
    public static <T> void quickSort(List<T> list, Comparator<? super T> comparator) {
        quickSortHelper(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSortHelper(
        List<T> list, int low, int high, Comparator<? super T> comparator
    ) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSortHelper(list, low, pi - 1, comparator);
            quickSortHelper(list, pi + 1, high, comparator);
        }
    }

    private static <T> int partition(
        List<T> list, int low, int high, Comparator<? super T> comparator
    ) {
        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}

// Comprehensive test demonstrating flexibility
class GenericAlgorithmsTest {
    public static void main(String[] args) {
        // Test with different Number types
        List<Integer> integers = Arrays.asList(1, 5, 3, 9, 2);
        List<Double> doubles = Arrays.asList(1.1, 5.5, 3.3);

        // Binary search
        int index = GenericAlgorithms.binarySearch(
            Arrays.asList(1, 3, 5, 7, 9), 5
        );
        System.out.println("Found at index: " + index);

        // Merge collections of related types
        List<Number> merged = GenericAlgorithms.mergeAll(integers, doubles);
        System.out.println("Merged: " + merged);

        // Partition strings by length
        List<String> words = Arrays.asList("hello", "hi", "world", "java", "ok");
        Map<Boolean, List<String>> partitioned = GenericAlgorithms.partition(
            words,
            s -> s.length() > 3
        );
        System.out.println("Long words: " + partitioned.get(true));
        System.out.println("Short words: " + partitioned.get(false));

        // Intersection with inheritance
        Set<String> set1 = Set.of("apple", "banana", "cherry");
        Set<CharSequence> set2 = Set.of("banana", "date", "elderberry");
        Set<CharSequence> common = GenericAlgorithms.intersection(set1, set2);
        System.out.println("Common elements: " + common);

        // Map and filter
        List<Integer> lengths = GenericAlgorithms.mapFilter(
            words,
            s -> s.contains("a"),     // Filter: contains 'a'
            String::length            // Map: to length
        );
        System.out.println("Lengths of words with 'a': " + lengths);

        // Custom sort with inheritance
        List<Number> numbers = new ArrayList<>(Arrays.asList(3.14, 2, 1.5f, 10));
        GenericAlgorithms.quickSort(numbers,
            (n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue())
        );
        System.out.println("Sorted numbers: " + numbers);
    }
}
```

**🎉 Mastery Achieved:**
- ✅ **Perfect PECS Application**: Producers use extends, consumers use super
- ✅ **Maximum Flexibility**: Works with inheritance hierarchies
- ✅ **Type Safety**: No unsafe casts or runtime errors
- ✅ **Real-World Utility**: Production-ready algorithm implementations
- ✅ **Complex Variance**: Multiple wildcards working together

</details>

---

## 📚 Module 2 Summary

### 🎉 What You've Mastered

- **Variance Understanding**: Covariance, contravariance, and invariance in Java
- **PECS Principle**: Producer Extends Consumer Super for wildcard usage
- **Flexible API Design**: Creating methods that work with type families
- **Wildcard Capture**: Converting wildcards to workable type parameters
- **Complex Patterns**: Multiple wildcards and functional interface variance
- **Common Pitfalls**: What to avoid when using wildcards

### 🔑 Key Takeaways

1. **PECS is your guide**: Use extends for producers, super for consumers
2. **Wildcards enable flexibility**: One method, multiple related types
3. **Capture pattern solves limitations**: Convert `List<?>` to `List<T>`
4. **Variance in functional interfaces**: `Function<? super T, ? extends R>`
5. **Don't overuse wildcards**: Simple cases don't need complex signatures

### 🚀 Next Steps

You're now ready for **Module 3: Advanced Patterns**, where you'll learn:
- Type erasure and runtime implications
- Generic inheritance and self-bounded types
- Integration with modern Java features
- Performance considerations and optimizations

### 📝 Self-Assessment Checklist

Before proceeding, ensure you can:

- [ ] Explain covariance vs contravariance
- [ ] Apply PECS principle correctly
- [ ] Design flexible APIs with wildcards
- [ ] Use wildcard capture pattern
- [ ] Avoid common wildcard pitfalls
- [ ] Create algorithms that work with type hierarchies

**Ready for advanced patterns?** Continue to [Module 3: Advanced Patterns](./module-3-advanced.html) 🚀

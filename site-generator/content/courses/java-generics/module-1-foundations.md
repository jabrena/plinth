title=Module 1: Foundations - Core Concepts and Type Safety
type=course-module
status=published
date=2025-09-13
author=Juan Antonio Breña Moral
module=1
duration=2-3 hours
difficulty=Beginner to Intermediate
tags=java, generics, type-safety, foundations
~~~~~~

## 📖 Module Overview

Welcome to the foundation of Java Generics! In this module, you'll discover why generics were introduced, understand the problems they solve, and learn the fundamental syntax that makes Java code safer and more expressive.

### 🎯 Learning Objectives

By the end of this module, you will:

- **Understand** the core problems that generics solve
- **Eliminate** raw types from your code completely
- **Apply** basic generic syntax correctly
- **Use** the diamond operator for cleaner code
- **Follow** generic naming conventions
- **Recognize** and fix common beginner mistakes

### ⏱️ Estimated Time: 2-3 hours

---

## 🤔 The Problem: Why Do We Need Generics?

### 💥 Before Generics (Java 1.4 and earlier)

Let's start with a story. Imagine you're building a simple inventory system:

```java
import java.util.ArrayList;
import java.util.List;

// This is how we had to write code before generics
public class PreGenericsInventory {
    private List items = new ArrayList(); // Raw type - dangerous!

    public void addItem(Object item) {
        items.add(item);
    }

    public Object getItem(int index) {
        return items.get(index);
    }

    // This method looks innocent but is a ticking time bomb
    public void processItems() {
        for (int i = 0; i < items.size(); i++) {
            String item = (String) getItem(i); // Unsafe cast!
            System.out.println("Processing: " + item.toUpperCase());
        }
    }
}
```

### 🔍 **Knowledge Check**: What's Wrong Here?

Before reading further, can you spot the potential problems in the code above?

<details>
<summary>🧠 Think about it, then click to reveal</summary>

**Problems identified:**
1. **No compile-time type checking** - we can add any object type
2. **Unsafe casting required** - `(String)` cast can fail at runtime
3. **ClassCastException risk** - if someone adds a non-String, the app crashes
4. **No IDE support** - no autocomplete or type hints
5. **Runtime errors instead of compile-time errors** - bugs discovered late

</details>

### 💣 The Explosion

```java
public class InventoryDemo {
    public static void main(String[] args) {
        PreGenericsInventory inventory = new PreGenericsInventory();

        // This compiles fine...
        inventory.addItem("Laptop");
        inventory.addItem("Mouse");
        inventory.addItem(42); // Oops! Added an Integer

        // But this explodes at runtime!
        inventory.processItems(); // ClassCastException!
    }
}
```

**Result**: `ClassCastException: Integer cannot be cast to String`

---

## ✨ The Solution: Enter Generics

### 🛡️ Type Safety with Generics

Now let's see how generics solve these problems:

```java
import java.util.ArrayList;
import java.util.List;

public class SafeInventory {
    private final List<String> items = new ArrayList<>(); // Type-safe!

    public void addItem(String item) {
        items.add(item); // Only String allowed
    }

    public String getItem(int index) {
        return items.get(index); // No casting needed!
    }

    public void processItems() {
        for (String item : items) { // Type-safe iteration
            System.out.println("Processing: " + item.toUpperCase());
        }
    }
}
```

### 🎉 Benefits Achieved

```java
public class SafeInventoryDemo {
    public static void main(String[] args) {
        SafeInventory inventory = new SafeInventory();

        inventory.addItem("Laptop");  // ✅ Compiles
        inventory.addItem("Mouse");   // ✅ Compiles
        // inventory.addItem(42);     // ❌ Compile error!

        inventory.processItems();     // ✅ Always safe!
    }
}
```

**💡 Key Insight**: Generics move errors from runtime to compile-time, making your code safer and bugs easier to catch!

---

## 🔤 Generic Syntax Fundamentals

### 📝 Basic Generic Class Declaration

```java
// Generic class with type parameter T
public class Container<T> {
    private T item;

    public Container(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
```

### 🏷️ Type Parameter Naming Conventions

Follow these standard conventions for better code readability:

```java
// ✅ Good naming conventions
public class StandardConventions {

    // T for general Type
    public static <T> T identity(T value) {
        return value;
    }

    // E for Element (collections)
    public class MyList<E> {
        private List<E> elements;
    }

    // K, V for Key-Value pairs
    public class MyMap<K, V> {
        private Map<K, V> storage;
    }

    // ? for unknown types (wildcards)
    public static int size(Collection<?> collection) {
        return collection.size();
    }

    // Descriptive names for specific contexts
    public interface ApiClient<Request, Response> {
        Response call(Request request);
    }
}
```

### 💎 The Diamond Operator (Java 7+)

Reduce verbosity with type inference:

```java
public class DiamondOperatorExamples {

    // ❌ Verbose (pre-Java 7)
    private Map<String, List<Integer>> oldStyle =
        new HashMap<String, List<Integer>>();

    // ✅ Clean (Java 7+)
    private Map<String, List<Integer>> newStyle = new HashMap<>();

    // ✅ Works with complex nested types
    private Map<String, Map<String, List<Object>>> complex = new HashMap<>();

    // ✅ Method return type inference
    public List<String> createList() {
        return new ArrayList<>(); // Type inferred from return type
    }
}
```

---

## 🎯 Hands-On Exercise 1: Converting Raw Types

### 📋 Your Mission

Convert this legacy code to use proper generics. Fix all type safety issues:

```java
import java.util.*;

// TODO: Fix this legacy code!
public class LegacyShoppingCart {
    private List items = new ArrayList();
    private Map itemPrices = new HashMap();

    public void addItem(Object item, Object price) {
        items.add(item);
        itemPrices.put(item, price);
    }

    public Object getItem(int index) {
        return items.get(index);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Object item : items) {
            Double price = (Double) itemPrices.get(item);
            total += price;
        }
        return total;
    }

    public void printItems() {
        for (int i = 0; i < items.size(); i++) {
            String item = (String) getItem(i);
            Double price = (Double) itemPrices.get(item);
            System.out.println(item + ": $" + price);
        }
    }
}
```

### 🎯 Requirements

1. **Eliminate all raw types**
2. **Remove all unsafe casts**
3. **Use appropriate generic types**
4. **Apply diamond operator where possible**
5. **Ensure compile-time type safety**

### 💡 Solution Template

<details>
<summary>🔍 Try it yourself first, then check the solution</summary>

```java
import java.util.*;

public class TypeSafeShoppingCart {
    private final List<String> items = new ArrayList<>();
    private final Map<String, Double> itemPrices = new HashMap<>();

    public void addItem(String item, Double price) {
        items.add(item);
        itemPrices.put(item, price);
    }

    public String getItem(int index) {
        return items.get(index); // No casting needed!
    }

    public double calculateTotal() {
        double total = 0.0;
        for (String item : items) { // Type-safe iteration
            Double price = itemPrices.get(item); // No casting!
            if (price != null) {
                total += price;
            }
        }
        return total;
    }

    public void printItems() {
        for (String item : items) { // Clean iteration
            Double price = itemPrices.get(item);
            if (price != null) {
                System.out.println(item + ": $" + price);
            }
        }
    }
}
```

**🎉 Benefits Achieved:**
- ✅ No raw types
- ✅ No unsafe casts
- ✅ Compile-time type safety
- ✅ Better IDE support
- ✅ Cleaner, more readable code

</details>

---

## 🎯 Hands-On Exercise 2: Generic Utility Methods

### 📋 Your Mission

Create a utility class with generic methods that work with different types:

```java
// TODO: Implement these generic utility methods
public class GenericUtils {

    // Create a list from variable arguments
    public static ??? createList(??? ... elements) {
        // Implementation here
    }

    // Find the first non-null element
    public static ??? findFirst(??? ... elements) {
        // Implementation here
    }

    // Check if a collection contains all given elements
    public static ??? containsAll(??? collection, ??? ... elements) {
        // Implementation here
    }

    // Swap two elements in a list
    public static ??? swap(??? list, int i, int j) {
        // Implementation here
    }
}
```

### 💡 Solution and Explanation

<details>
<summary>🔍 Try implementing it yourself first</summary>

```java
import java.util.*;

public class GenericUtils {

    // Generic method with varargs
    public static <T> List<T> createList(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    // Generic method with bounded return type
    public static <T> T findFirst(T... elements) {
        for (T element : elements) {
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    // Generic method with collection parameter
    public static <T> boolean containsAll(Collection<T> collection, T... elements) {
        for (T element : elements) {
            if (!collection.contains(element)) {
                return false;
            }
        }
        return true;
    }

    // Generic method modifying a list
    public static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Usage examples
    public static void demonstrateUsage() {
        // Type inference in action
        List<String> strings = createList("a", "b", "c");
        List<Integer> numbers = createList(1, 2, 3);

        String firstString = findFirst("hello", null, "world");
        Integer firstNumber = findFirst(null, 42, null);

        boolean hasAll = containsAll(strings, "a", "b");

        swap(numbers, 0, 2); // [3, 2, 1]
    }
}
```

**🧠 Key Learning Points:**
- Generic methods can infer types from usage
- Use `<T>` before return type to declare type parameter
- Varargs work well with generics
- Type inference makes code cleaner

</details>

---

## 🚫 Common Mistakes to Avoid

### ❌ Mistake 1: Using Raw Types

```java
// ❌ DON'T do this
List names = new ArrayList();
Map scores = new HashMap();

// ✅ DO this instead
List<String> names = new ArrayList<>();
Map<String, Integer> scores = new HashMap<>();
```

### ❌ Mistake 2: Unnecessary Type Specification

```java
// ❌ Verbose (pre-Java 7 style)
List<String> names = new ArrayList<String>();
Map<String, Integer> scores = new HashMap<String, Integer>();

// ✅ Clean (Java 7+ diamond operator)
List<String> names = new ArrayList<>();
Map<String, Integer> scores = new HashMap<>();
```

### ❌ Mistake 3: Poor Generic Method Design

```java
// ❌ Too restrictive
public static void printList(List<Object> list) {
    // Can only accept List<Object>, not List<String>
}

// ✅ Flexible with wildcards (we'll learn more in Module 2)
public static void printList(List<?> list) {
    // Accepts List of any type
}
```

---

## 🧪 Knowledge Check Quiz

Test your understanding with these questions:

### Question 1
What will happen when this code runs?

```java
List items = new ArrayList();
items.add("Hello");
items.add(42);
String first = (String) items.get(1);
```

<details>
<summary>🤔 Your answer?</summary>

**Answer**: `ClassCastException` at runtime because we're trying to cast an Integer (42) to String.

**Fix**: Use `List<String>` and only add strings, or use `List<Object>` and check types before casting.

</details>

### Question 2
Which declaration is preferred and why?

```java
// Option A
Map<String, List<Integer>> data = new HashMap<String, List<Integer>>();

// Option B
Map<String, List<Integer>> data = new HashMap<>();
```

<details>
<summary>🤔 Your answer?</summary>

**Answer**: Option B is preferred because:
- Uses diamond operator for cleaner code
- Reduces verbosity without losing type safety
- Leverages type inference (Java 7+ feature)
- Easier to maintain and read

</details>

### Question 3
What's wrong with this generic method?

```java
public static <T> void printArray(T[] array) {
    for (int i = 0; i < array.length; i++) {
        System.out.println(array[i].toString().toUpperCase());
    }
}
```

<details>
<summary>🤔 Your answer?</summary>

**Answer**: The method assumes T has a meaningful `toString()` method and that the result should be converted to uppercase. This only makes sense for certain types.

**Better approach**:
```java
public static <T> void printArray(T[] array) {
    for (T item : array) {
        System.out.println(item); // Let println handle toString()
    }
}

// Or if you need string processing, be explicit:
public static void printStringArray(String[] array) {
    for (String item : array) {
        System.out.println(item.toUpperCase());
    }
}
```

</details>

---

## 🎯 Module 1 Project: Generic Data Structure

### 📋 Final Challenge

Create a generic `Stack<T>` class that demonstrates all concepts learned:

**Requirements:**
- Generic class with type parameter `T`
- Methods: `push(T item)`, `T pop()`, `T peek()`, `boolean isEmpty()`, `int size()`
- Use appropriate generic collections internally
- Include proper error handling
- No raw types or unsafe casts
- Follow naming conventions

### 🏗️ Starter Template

```java
import java.util.*;

// TODO: Complete this generic Stack implementation
public class GenericStack<T> {

    // TODO: Choose appropriate internal data structure

    // TODO: Implement constructor

    // TODO: Implement push method
    public void push(T item) {

    }

    // TODO: Implement pop method
    public T pop() {

    }

    // TODO: Implement peek method
    public T peek() {

    }

    // TODO: Implement isEmpty method
    public boolean isEmpty() {

    }

    // TODO: Implement size method
    public int size() {

    }

    // TODO: Add toString method for debugging
    @Override
    public String toString() {

    }
}
```

### 🧪 Test Your Implementation

```java
public class StackTest {
    public static void main(String[] args) {
        // Test with Strings
        GenericStack<String> stringStack = new GenericStack<>();
        stringStack.push("First");
        stringStack.push("Second");
        stringStack.push("Third");

        System.out.println("Stack: " + stringStack);
        System.out.println("Peek: " + stringStack.peek());
        System.out.println("Pop: " + stringStack.pop());
        System.out.println("Size: " + stringStack.size());

        // Test with Integers
        GenericStack<Integer> intStack = new GenericStack<>();
        intStack.push(10);
        intStack.push(20);
        intStack.push(30);

        while (!intStack.isEmpty()) {
            System.out.println("Popped: " + intStack.pop());
        }
    }
}
```

### 💡 Complete Solution

<details>
<summary>🎯 Try implementing it yourself first, then check the solution</summary>

```java
import java.util.*;

public class GenericStack<T> {
    private final List<T> elements;

    public GenericStack() {
        this.elements = new ArrayList<>();
    }

    public void push(T item) {
        elements.add(item);
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.get(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return "GenericStack" + elements;
    }
}

// Custom exception for better error handling
class EmptyStackException extends RuntimeException {
    public EmptyStackException() {
        super("Cannot perform operation on empty stack");
    }
}
```

**🏆 Congratulations!** You've successfully implemented a generic data structure that:
- ✅ Uses proper generic syntax
- ✅ Eliminates raw types and unsafe casts
- ✅ Provides compile-time type safety
- ✅ Follows naming conventions
- ✅ Includes proper error handling

</details>

---

## 📚 Module 1 Summary

### 🎉 What You've Learned

- **The Problem**: Why generics were introduced to Java
- **Type Safety**: How generics prevent ClassCastException
- **Basic Syntax**: Generic classes, methods, and type parameters
- **Diamond Operator**: Reducing verbosity with type inference
- **Conventions**: Standard naming patterns for type parameters
- **Common Mistakes**: What to avoid when using generics

### 🔑 Key Takeaways

1. **Generics move errors from runtime to compile-time** - catch bugs early!
2. **Always use parameterized types** - avoid raw types completely
3. **Leverage type inference** - use diamond operator for cleaner code
4. **Follow naming conventions** - T, E, K, V for better readability
5. **Generic methods provide flexibility** - one method, multiple types

### 🚀 Next Steps

You're now ready for **Module 2: Wildcards & PECS**, where you'll learn:
- Understanding covariance and contravariance
- Producer Extends Consumer Super principle
- Designing flexible APIs with wildcards
- Advanced collection operations

### 📝 Self-Assessment Checklist

Before proceeding, ensure you can:

- [ ] Explain why generics were added to Java
- [ ] Convert raw types to parameterized types
- [ ] Use the diamond operator correctly
- [ ] Write basic generic methods
- [ ] Follow generic naming conventions
- [ ] Identify and fix common generic mistakes

**Ready for the next challenge?** Continue to [Module 2: Wildcards & PECS](./module-2-wildcards.html) 🚀

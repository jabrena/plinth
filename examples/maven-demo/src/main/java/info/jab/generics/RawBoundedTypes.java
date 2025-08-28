package info.jab.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Examples showing the problems solved by bounded generics.
 * This demonstrates why we need generic constraints and bounded wildcards.
 *
 * PROBLEMS DEMONSTRATED:
 * 1. No compile-time type safety
 * 2. Runtime ClassCastException risks
 * 3. Loss of type information
 * 4. No guarantee about available methods
 * 5. Unclear API contracts
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RawBoundedTypes {

    // PROBLEM: No bounds means we can't assume anything about the type
    public static Object findMedian(List numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        List sorted = new ArrayList(numbers);
        // PROBLEM: We can't safely sort because we don't know if elements are Comparable
        try {
            Collections.sort(sorted); // This might throw ClassCastException at runtime!
        } catch (ClassCastException e) {
            throw new RuntimeException("Elements are not comparable", e);
        }

        int middle = sorted.size() / 2;
        return sorted.get(middle);
    }

    // PROBLEM: No type safety with wildcards - anything can be added/removed
    public static void unsafeTransfer(Collection destination, Collection sources) {
        // PROBLEM: sources could be a Collection of Collections, but we can't enforce it
        for (Object source : sources) {
            if (source instanceof Collection) {
                destination.addAll((Collection) source); // Unsafe cast!
            } else {
                // What do we do if it's not a Collection? Runtime error!
                throw new RuntimeException("Expected Collection but got " + source.getClass());
            }
        }
    }

    // PROBLEM: No guarantee that keys are comparable for TreeMap
    public static Map mergeMaps(Map map1, Map map2, Object valuesMerger) {
        // PROBLEM: We want to use TreeMap but can't guarantee keys are Comparable
        Map result = new HashMap(); // Forced to use HashMap instead

        result.putAll(map1);
        for (Object entry : map2.entrySet()) {
            Map.Entry mapEntry = (Map.Entry) entry;
            Object key = mapEntry.getKey();
            Object value = mapEntry.getValue();

            // PROBLEM: We can't use BinaryOperator merger safely
            if (result.containsKey(key)) {
                Object existingValue = result.get(key);
                // We have to implement merging logic manually and unsafely
                if (valuesMerger instanceof BinaryOperator) {
                    BinaryOperator merger = (BinaryOperator) valuesMerger;
                    result.put(key, merger.apply(existingValue, value));
                } else {
                    // Fallback - just overwrite
                    result.put(key, value);
                }
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    // PROBLEM: No transformation safety
    public static Collection transformAndCollect(Collection source, Object mapper, Object collectionFactory) {
        Collection result;

        // PROBLEM: We have to check types at runtime
        if (collectionFactory instanceof Supplier) {
            Supplier factory = (Supplier) collectionFactory;
            Object factoryResult = factory.get();
            if (factoryResult instanceof Collection) {
                result = (Collection) factoryResult;
            } else {
                throw new RuntimeException("Factory didn't produce a Collection");
            }
        } else {
            result = new ArrayList(); // Fallback
        }

        // PROBLEM: Unsafe transformation
        if (mapper instanceof Function) {
            Function function = (Function) mapper;
            for (Object item : source) {
                try {
                    Object transformed = function.apply(item);
                    result.add(transformed);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Transformation failed for item: " + item, e);
                }
            }
        } else {
            throw new RuntimeException("Mapper is not a Function");
        }

        return result;
    }

    // PROBLEM: Merge sort without type safety
    public static List mergeSort(List list) {
        if (list.size() <= 1) {
            return new ArrayList(list);
        }

        int mid = list.size() / 2;
        List left = mergeSort(list.subList(0, mid));
        List right = mergeSort(list.subList(mid, list.size()));

        return merge(left, right);
    }

    private static List merge(List left, List right) {
        List result = new ArrayList();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            Object leftItem = left.get(leftIndex);
            Object rightItem = right.get(rightIndex);

            // PROBLEM: We have to check if items are Comparable at runtime
            try {
                if (leftItem instanceof Comparable && rightItem instanceof Comparable) {
                    Comparable leftComparable = (Comparable) leftItem;
                    if (leftComparable.compareTo(rightItem) <= 0) {
                        result.add(leftItem);
                        leftIndex++;
                    } else {
                        result.add(rightItem);
                        rightIndex++;
                    }
                } else {
                    throw new RuntimeException("Items are not comparable");
                }
            } catch (ClassCastException e) {
                throw new RuntimeException("Comparison failed", e);
            }
        }

        // Add remaining elements
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex++));
        }

        return result;
    }

    // PROBLEM: Can't enforce that collection contains Numbers
    public static double calculateWeightedAverage(Collection numbers, Object weightFunction) {
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        if (!(weightFunction instanceof Function)) {
            throw new RuntimeException("Weight function must be a Function");
        }

        Function function = (Function) weightFunction;

        for (Object item : numbers) {
            // PROBLEM: We have to check if each item is a Number at runtime
            if (!(item instanceof Number)) {
                throw new RuntimeException("Expected Number but got " + item.getClass());
            }

            Number number = (Number) item;
            try {
                Object weightObj = function.apply(number);
                if (!(weightObj instanceof Double)) {
                    throw new RuntimeException("Weight function must return Double");
                }
                double weight = (Double) weightObj;
                weightedSum += number.doubleValue() * weight;
                totalWeight += weight;
            } catch (ClassCastException e) {
                throw new RuntimeException("Weight calculation failed", e);
            }
        }

        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    // PROBLEM: Abstract processor without type safety
    public abstract static class RawProcessor {
        // PROBLEM: We lose all type information
        public abstract Collection process(Collection input);

        // PROBLEM: No type safety in filtering
        public final Collection processAndFilter(Collection input, Class resultType) {
            Collection processed = process(input);
            List filtered = new ArrayList();

            for (Object item : processed) {
                if (resultType.isInstance(item)) {
                    filtered.add(resultType.cast(item));
                }
            }

            return filtered;
        }
    }

    // PROBLEM: Implementation loses all type information
    public static class StringToNumberProcessor extends RawProcessor {
        @Override
        public Collection process(Collection input) {
            List results = new ArrayList();
            for (Object obj : input) {
                // PROBLEM: We have to check types at runtime
                if (obj instanceof String) {
                    String str = (String) obj;
                    try {
                        results.add(str.length());
                    } catch (Exception e) {
                        results.add(0);
                    }
                } else {
                    throw new RuntimeException("Expected String but got " + obj.getClass());
                }
            }
            return results;
        }
    }

    // PROBLEM: Factory without type safety
    public static class RawFactory {
        private final Class type;
        private final Map constructors; // Map<String, Function<Object[], Object>>

        public RawFactory(Class type) {
            this.type = type;
            this.constructors = new HashMap();
        }

        // PROBLEM: No type safety for constructor parameters
        public RawFactory registerConstructor(String name, Object constructor) {
            constructors.put(name, constructor);
            return this;
        }

        // PROBLEM: Returns Object, caller must cast
        public Object create(String constructorName, Object... args) {
            Object constructor = constructors.get(constructorName);
            if (constructor == null) {
                throw new IllegalArgumentException("Unknown constructor: " + constructorName);
            }

            // PROBLEM: We have to check type at runtime
            if (constructor instanceof Function) {
                Function function = (Function) constructor;
                try {
                    return function.apply(args);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Constructor invocation failed", e);
                }
            } else {
                throw new RuntimeException("Constructor is not a Function");
            }
        }

        public Class getType() {
            return type;
        }
    }

    // Demonstration of all the problems
    public static void demonstrateProblems() {
        System.out.println("=== Demonstrating Problems Without Bounded Generics ===");

        try {
            // PROBLEM: This will work fine
            List integers = Arrays.asList(1, 5, 3, 9, 2);
            Object median = findMedian(integers);
            System.out.println("Integer median: " + median);

            // PROBLEM: This will throw ClassCastException at runtime!
            List mixed = Arrays.asList(1, "hello", 3.14);
            try {
                findMedian(mixed);
            } catch (RuntimeException e) {
                System.out.println("ERROR with mixed types: " + e.getMessage());
            }

            // PROBLEM: Unsafe transfer
            Collection numbers = new ArrayList();
            Collection sources = Arrays.asList(
                Arrays.asList(1, 2, 3),
                "not a collection" // This will cause runtime error!
            );
            try {
                unsafeTransfer(numbers, sources);
            } catch (RuntimeException e) {
                System.out.println("ERROR in transfer: " + e.getMessage());
            }

            // PROBLEM: Map merging is limited and unsafe
            Map map1 = new HashMap();
            map1.put("a", 1);
            map1.put("b", 2);

            Map map2 = new HashMap();
            map2.put("b", 3);
            map2.put("c", 4);

            Map merged = mergeMaps(map1, map2, (BinaryOperator<Integer>) Integer::sum);
            System.out.println("Merged map: " + merged);

            // PROBLEM: Transformation is unsafe and verbose
            List strings = Arrays.asList("hello", "world", "generics");
            try {
                Collection lengths = transformAndCollect(
                    strings,
                    (Function<String, Integer>) String::length,
                    (Supplier<Collection>) ArrayList::new
                );
                System.out.println("String lengths: " + lengths);
            } catch (RuntimeException e) {
                System.out.println("ERROR in transformation: " + e.getMessage());
            }

            // PROBLEM: Sort only works with comparable elements
            List sortableStrings = Arrays.asList("zebra", "apple", "banana");
            List sorted = mergeSort(sortableStrings);
            System.out.println("Sorted strings: " + sorted);

            // This would fail:
            List unsortable = Arrays.asList(new Object(), new Object());
            try {
                mergeSort(unsortable);
            } catch (RuntimeException e) {
                System.out.println("ERROR sorting Objects: " + e.getMessage());
            }

            // PROBLEM: Weighted average requires runtime type checking
            List numberList = Arrays.asList(1, 2, 3, 4, 5);
            double avg = calculateWeightedAverage(numberList,
                (Function<Number, Double>) n -> 1.0);
            System.out.println("Average: " + avg);

            // This would fail:
            List nonNumbers = Arrays.asList("1", "2", "3");
            try {
                calculateWeightedAverage(nonNumbers,
                    (Function<Number, Double>) n -> 1.0);
            } catch (RuntimeException e) {
                System.out.println("ERROR with non-numbers: " + e.getMessage());
            }

            // PROBLEM: Factory is completely unsafe
            RawFactory sbFactory = new RawFactory(StringBuilder.class);
            sbFactory.registerConstructor("withCapacity",
                (Function<Object[], Object>) args -> {
                    if (args.length != 1 || !(args[0] instanceof Integer)) {
                        throw new RuntimeException("Expected single Integer argument");
                    }
                    return new StringBuilder((Integer) args[0]);
                });

            Object sb = sbFactory.create("withCapacity", 100);
            // PROBLEM: Caller must cast and hope for the best
            StringBuilder stringBuilder = (StringBuilder) sb;
            System.out.println("StringBuilder capacity: " + stringBuilder.capacity());

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Summary of Problems ===");
        System.out.println("1. No compile-time type safety - errors only at runtime");
        System.out.println("2. Extensive runtime type checking required");
        System.out.println("3. Verbose and error-prone casting");
        System.out.println("4. No guarantee about available methods on types");
        System.out.println("5. API contracts are unclear and not enforced");
        System.out.println("6. ClassCastException risks everywhere");
        System.out.println("7. Loss of performance due to boxing/unboxing and type checks");
        System.out.println("8. Poor IDE support - no auto-completion or type hints");
        System.out.println("9. Difficult to refactor and maintain");
        System.out.println("10. Cannot leverage advanced features like TreeMap sorting");
    }
}

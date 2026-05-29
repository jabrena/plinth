package info.jab.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Examples showing the problems solved by modern Java generics with Records,
 * sealed types, and type-safe containers. This demonstrates the "before" state
 * that motivates the need for advanced generic patterns.
 *
 * PROBLEMS DEMONSTRATED:
 * 1. No compile-time type safety in containers
 * 2. Runtime ClassCastException risks
 * 3. Loss of type information
 * 4. Verbose and error-prone code
 * 5. No pattern matching benefits
 * 6. Unclear API contracts
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RawContainers {

    // PROBLEM: Result class without type safety
    public static class RawResult {
        private final Object value;
        private final Object error;
        private final boolean isSuccess;

        public RawResult(Object value, Object error, boolean isSuccess) {
            this.value = value;
            this.error = error;
            this.isSuccess = isSuccess;
        }

        // PROBLEM: Factory methods return Object, caller must know types
        public static RawResult success(Object value) {
            return new RawResult(value, null, true);
        }

        public static RawResult failure(Object error) {
            return new RawResult(null, error, false);
        }

        // PROBLEM: Transformation methods without type safety
        public RawResult map(Object mapper) {
            if (!isSuccess) {
                return failure(error);
            }

            // PROBLEM: Must check if mapper is actually a Function
            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;
            try {
                Object result = func.apply(value);
                return success(result);
            } catch (ClassCastException e) {
                throw new RuntimeException("Mapping failed", e);
            }
        }

        public RawResult mapError(Object errorMapper) {
            if (isSuccess) {
                return success(value);
            }

            if (!(errorMapper instanceof Function)) {
                throw new RuntimeException("Error mapper must be a Function");
            }

            Function func = (Function) errorMapper;
            try {
                Object result = func.apply(error);
                return failure(result);
            } catch (ClassCastException e) {
                throw new RuntimeException("Error mapping failed", e);
            }
        }

        public RawResult flatMap(Object mapper) {
            if (!isSuccess) {
                return failure(error);
            }

            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;
            try {
                Object result = func.apply(value);
                if (!(result instanceof RawResult)) {
                    throw new RuntimeException("FlatMap function must return RawResult");
                }
                return (RawResult) result;
            } catch (ClassCastException e) {
                throw new RuntimeException("FlatMap failed", e);
            }
        }

        // PROBLEM: Unsafe accessors
        public Object getValue() { return value; }
        public Object getError() { return error; }
        public boolean isSuccess() { return isSuccess; }

        public Object orElse(Object defaultValue) {
            return isSuccess ? value : defaultValue;
        }

        public Object orElseGet(Object supplier) {
            if (isSuccess) {
                return value;
            }

            if (!(supplier instanceof Supplier)) {
                throw new RuntimeException("Must provide a Supplier");
            }

            Supplier sup = (Supplier) supplier;
            return sup.get();
        }
    }

    // PROBLEM: Pair class without type safety
    public static class RawPair {
        private final Object left;
        private final Object right;

        public RawPair(Object left, Object right) {
            this.left = Objects.requireNonNull(left, "Left value cannot be null");
            this.right = Objects.requireNonNull(right, "Right value cannot be null");
        }

        // PROBLEM: Transformation methods without type safety
        public RawPair mapLeft(Object mapper) {
            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;
            try {
                Object result = func.apply(left);
                return new RawPair(result, right);
            } catch (ClassCastException e) {
                throw new RuntimeException("Left mapping failed", e);
            }
        }

        public RawPair mapRight(Object mapper) {
            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;
            try {
                Object result = func.apply(right);
                return new RawPair(left, result);
            } catch (ClassCastException e) {
                throw new RuntimeException("Right mapping failed", e);
            }
        }

        public RawPair map(Object leftMapper, Object rightMapper) {
            if (!(leftMapper instanceof Function) || !(rightMapper instanceof Function)) {
                throw new RuntimeException("Both mappers must be Functions");
            }

            Function leftFunc = (Function) leftMapper;
            Function rightFunc = (Function) rightMapper;

            try {
                Object newLeft = leftFunc.apply(left);
                Object newRight = rightFunc.apply(right);
                return new RawPair(newLeft, newRight);
            } catch (ClassCastException e) {
                throw new RuntimeException("Mapping failed", e);
            }
        }

        public RawPair swap() {
            return new RawPair(right, left);
        }

        // PROBLEM: Unsafe accessors
        public Object getLeft() { return left; }
        public Object getRight() { return right; }

        // PROBLEM: Complex type checking for same-type operations
        public Optional ifSameType(Class type) {
            if (type.isInstance(left) && type.isInstance(right)) {
                return Optional.of(new RawPair(type.cast(left), type.cast(right)));
            }
            return Optional.empty();
        }
    }

    // PROBLEM: Container hierarchy without sealed types
    public static abstract class RawContainer {
        // PROBLEM: No way to constrain implementations at compile time
        public abstract boolean isEmpty();
        public abstract int size();
        public abstract List getItems(); // PROBLEM: Raw List

        // PROBLEM: Default methods without type safety
        public Optional getFirst() {
            List items = getItems();
            return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
        }

        public Optional getLast() {
            List items = getItems();
            return items.isEmpty() ? Optional.empty() : Optional.of(items.get(items.size() - 1));
        }

        // PROBLEM: Map operation without type safety
        public RawContainer map(Object mapper) {
            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;

            // PROBLEM: Must use instanceof checks instead of pattern matching
            if (this instanceof RawEmptyContainer) {
                return new RawEmptyContainer();
            } else if (this instanceof RawSingleContainer) {
                RawSingleContainer single = (RawSingleContainer) this;
                try {
                    Object mappedItem = func.apply(single.getItem());
                    return new RawSingleContainer(mappedItem);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Single container mapping failed", e);
                }
            } else if (this instanceof RawMultiContainer) {
                RawMultiContainer multi = (RawMultiContainer) this;
                List newItems = new ArrayList();
                try {
                    for (Object item : multi.getItems()) {
                        newItems.add(func.apply(item));
                    }
                    return new RawMultiContainer(newItems);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Multi container mapping failed", e);
                }
            } else {
                throw new RuntimeException("Unknown container type: " + this.getClass());
            }
        }

        // PROBLEM: Filter operation without type safety
        public RawContainer filter(Object predicate) {
            if (!(predicate instanceof Predicate)) {
                throw new RuntimeException("Predicate must be a Predicate");
            }

            Predicate pred = (Predicate) predicate;

            // PROBLEM: Verbose instanceof checking
            if (this instanceof RawEmptyContainer) {
                return this;
            } else if (this instanceof RawSingleContainer) {
                RawSingleContainer single = (RawSingleContainer) this;
                try {
                    if (pred.test(single.getItem())) {
                        return single;
                    } else {
                        return new RawEmptyContainer();
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Single container filtering failed", e);
                }
            } else if (this instanceof RawMultiContainer) {
                RawMultiContainer multi = (RawMultiContainer) this;
                List filtered = new ArrayList();
                try {
                    for (Object item : multi.getItems()) {
                        if (pred.test(item)) {
                            filtered.add(item);
                        }
                    }

                    // PROBLEM: Manual size checking instead of pattern matching
                    if (filtered.isEmpty()) {
                        return new RawEmptyContainer();
                    } else if (filtered.size() == 1) {
                        return new RawSingleContainer(filtered.get(0));
                    } else {
                        return new RawMultiContainer(filtered);
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Multi container filtering failed", e);
                }
            } else {
                throw new RuntimeException("Unknown container type: " + this.getClass());
            }
        }
    }

    // PROBLEM: Concrete implementations without type safety
    public static class RawSingleContainer extends RawContainer {
        private final Object item;

        public RawSingleContainer(Object item) {
            this.item = Objects.requireNonNull(item, "Item cannot be null");
        }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public int size() { return 1; }

        @Override
        public List getItems() { return Arrays.asList(item); } // PROBLEM: Raw List

        public Object getItem() { return item; } // PROBLEM: Returns Object
    }

    public static class RawMultiContainer extends RawContainer {
        private final List items; // PROBLEM: Raw List

        public RawMultiContainer(List items) {
            Objects.requireNonNull(items, "Items cannot be null");
            if (items.isEmpty()) {
                throw new IllegalArgumentException("MultiContainer cannot be empty");
            }
            this.items = new ArrayList(items); // PROBLEM: No type checking on copy
        }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public int size() { return items.size(); }

        @Override
        public List getItems() { return items; } // PROBLEM: Raw List returned
    }

    public static class RawEmptyContainer extends RawContainer {
        @Override
        public boolean isEmpty() { return true; }

        @Override
        public int size() { return 0; }

        @Override
        public List getItems() { return new ArrayList(); } // PROBLEM: Raw List
    }

    // PROBLEM: Type-unsafe heterogeneous container
    public static class UnsafeHeterogeneousContainer {
        private final Map storage = new HashMap(); // PROBLEM: Raw Map

        // PROBLEM: No type safety for keys or values
        public void put(Object key, Object value) {
            storage.put(key, value);
        }

        public Object get(Object key) { // PROBLEM: Returns Object
            return storage.get(key);
        }

        public boolean contains(Object key) {
            return storage.containsKey(key);
        }

        public Object remove(Object key) { // PROBLEM: Returns Object
            return storage.remove(key);
        }

        public Set getStoredKeys() { // PROBLEM: Raw Set
            return new HashSet(storage.keySet());
        }

        // PROBLEM: Unsafe type hierarchy operations
        public List getInstancesOfType(Class type) { // PROBLEM: Raw List
            List instances = new ArrayList();
            for (Object entry : storage.entrySet()) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();

                // PROBLEM: Complex type checking logic
                if (key instanceof Class) {
                    Class keyClass = (Class) key;
                    if (type.isAssignableFrom(keyClass)) {
                        instances.add(value);
                    }
                }
            }
            return instances;
        }

        // PROBLEM: All values of a type operation without safety
        public Map getAllOfType(Class type) { // PROBLEM: Raw Map
            Map result = new HashMap();
            for (Object entry : storage.entrySet()) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();

                if (key instanceof Class) {
                    Class keyClass = (Class) key;
                    if (keyClass.equals(type)) {
                        result.put("instance", value); // PROBLEM: Generic key
                    }
                }
            }
            return result;
        }
    }

    // PROBLEM: Pattern matching alternatives are verbose and error-prone
    public static class VerbosePatternMatching {

        public static String describeContainer(RawContainer container) {
            // PROBLEM: Must use instanceof chains instead of pattern matching
            if (container instanceof RawEmptyContainer) {
                return "Empty container";
            } else if (container instanceof RawSingleContainer) {
                RawSingleContainer single = (RawSingleContainer) container;
                return "Single item: " + single.getItem();
            } else if (container instanceof RawMultiContainer) {
                RawMultiContainer multi = (RawMultiContainer) container;
                List items = multi.getItems();
                // PROBLEM: No easy way to get first 3 items safely
                List firstThree = new ArrayList();
                for (int i = 0; i < Math.min(3, items.size()); i++) {
                    firstThree.add(items.get(i));
                }
                return "Multiple items (" + items.size() + "): " + firstThree;
            } else {
                return "Unknown container type";
            }
        }

        public static String handleResult(RawResult result) {
            // PROBLEM: Manual checking instead of pattern matching
            if (result.isSuccess()) {
                return "Success: " + result.getValue();
            } else {
                return "Error: " + result.getError();
            }
        }

        // PROBLEM: Complex value processing without pattern matching
        public static String processValue(Object value) {
            // PROBLEM: Verbose instanceof checking and casting
            if (value instanceof String) {
                String s = (String) value;
                if (s.length() > 10) {
                    return "Long string: " + s.substring(0, 10) + "...";
                } else {
                    return "Short string: " + s;
                }
            } else if (value instanceof Integer) {
                Integer i = (Integer) value;
                if (i > 100) {
                    return "Large integer: " + i;
                } else {
                    return "Small integer: " + i;
                }
            } else if (value instanceof List) {
                List list = (List) value;
                if (list.isEmpty()) {
                    return "Empty list";
                } else {
                    return "List with " + list.size() + " items";
                }
            } else if (value == null) {
                return "Null value";
            } else {
                return "Unknown type: " + value.getClass().getSimpleName();
            }
        }
    }

    // PROBLEM: Functional operations without type safety
    public static class UnsafeFunctionalOperations {

        // PROBLEM: Attempt operation without proper error handling
        public static RawResult attempt(Object operation, Object errorMapper) {
            if (!(operation instanceof Supplier)) {
                throw new RuntimeException("Operation must be a Supplier");
            }
            if (!(errorMapper instanceof Function)) {
                throw new RuntimeException("Error mapper must be a Function");
            }

            Supplier sup = (Supplier) operation;
            Function errMapper = (Function) errorMapper;

            try {
                Object result = sup.get();
                return RawResult.success(result);
            } catch (Exception e) {
                try {
                    Object error = errMapper.apply(e);
                    return RawResult.failure(error);
                } catch (ClassCastException ce) {
                    throw new RuntimeException("Error mapping failed", ce);
                }
            }
        }

        // PROBLEM: Combining results without type safety
        public static RawResult combine(RawResult result1, RawResult result2, Object combiner) {
            if (!(combiner instanceof BinaryOperator)) {
                throw new RuntimeException("Combiner must be a BinaryOperator");
            }

            BinaryOperator binOp = (BinaryOperator) combiner;

            if (!result1.isSuccess()) {
                return result1;
            }
            if (!result2.isSuccess()) {
                return result2;
            }

            try {
                Object combined = binOp.apply(result1.getValue(), result2.getValue());
                return RawResult.success(combined);
            } catch (ClassCastException e) {
                throw new RuntimeException("Combination failed", e);
            }
        }

        // PROBLEM: Sequence operation without type safety
        public static RawResult sequence(List results) { // PROBLEM: Raw List
            List values = new ArrayList(); // PROBLEM: Raw List
            for (Object resultObj : results) {
                if (!(resultObj instanceof RawResult)) {
                    throw new RuntimeException("List must contain RawResult objects");
                }

                RawResult result = (RawResult) resultObj;
                if (!result.isSuccess()) {
                    return result; // Return first failure
                }
                values.add(result.getValue());
            }
            return RawResult.success(values);
        }

        // PROBLEM: Memoization without type safety
        public static Object memoize(Object function) { // PROBLEM: Returns Object
            if (!(function instanceof Function)) {
                throw new RuntimeException("Must provide a Function");
            }

            Function func = (Function) function;
            Map cache = new HashMap(); // PROBLEM: Raw Map

            return (Function) input -> cache.computeIfAbsent(input, func);
        }
    }

    // Demonstration of all the problems
    public static void demonstrateContainerProblems() {
        System.out.println("=== Demonstrating Container Problems Without Modern Generics ===");

        try {
            // PROBLEM: Result pattern without type safety
            RawResult success = RawResult.success("Hello World");
            RawResult failure = RawResult.failure("Something went wrong");

            // PROBLEM: Must cast results
            RawResult lengthResult = success.map((Function<String, Integer>) String::length);
            Object length = lengthResult.getValue(); // PROBLEM: Returns Object
            Integer actualLength = (Integer) length; // PROBLEM: Must cast

            // PROBLEM: Container operations are verbose and unsafe
            RawContainer empty = new RawEmptyContainer();
            RawContainer single = new RawSingleContainer("Hello");
            List multiItems = Arrays.asList("A", "B", "C"); // PROBLEM: Raw List
            RawContainer multi = new RawMultiContainer(multiItems);

            String emptyDesc = VerbosePatternMatching.describeContainer(empty);
            String singleDesc = VerbosePatternMatching.describeContainer(single);
            String multiDesc = VerbosePatternMatching.describeContainer(multi);

            // PROBLEM: Unsafe heterogeneous container
            UnsafeHeterogeneousContainer container = new UnsafeHeterogeneousContainer();
            container.put(String.class, "Hello");
            container.put(Integer.class, 42);
            container.put("wrong key type", "This shouldn't be allowed");

            Object stringValue = container.get(String.class); // PROBLEM: Returns Object
            String actualString = (String) stringValue; // PROBLEM: Must cast

            // PROBLEM: This could cause ClassCastException
            try {
                String wrongCast = (String) container.get(Integer.class);
            } catch (ClassCastException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            // PROBLEM: Pair operations without type safety
            RawPair pair = new RawPair("Hello", 42);
            RawPair mappedPair = pair.mapLeft((Function<String, Integer>) String::length);

            Object leftValue = mappedPair.getLeft(); // PROBLEM: Returns Object
            Integer leftInt = (Integer) leftValue; // PROBLEM: Must cast

            // PROBLEM: Functional operations are verbose and unsafe
            List results = Arrays.asList( // PROBLEM: Raw List
                RawResult.success(1),
                RawResult.success(2),
                RawResult.success(3)
            );

            RawResult sequenced = UnsafeFunctionalOperations.sequence(results);
            Object sequencedValue = sequenced.getValue(); // PROBLEM: Returns Object
            List sequencedList = (List) sequencedValue; // PROBLEM: Must cast

            // PROBLEM: Pattern matching is verbose
            List testValues = Arrays.asList("short", "this is a very long string", 42, 150,
                                          Arrays.asList(), Arrays.asList(1, 2, 3), null);
            for (Object value : testValues) {
                String description = VerbosePatternMatching.processValue(value);
                System.out.println("Value: " + description);
            }

            System.out.println("Container problems demonstration completed");
            System.out.println("Success result value: " + success.getValue());
            System.out.println("Length result: " + actualLength);
            System.out.println("Empty container: " + emptyDesc);
            System.out.println("Single container: " + singleDesc);
            System.out.println("Multi container: " + multiDesc);
            System.out.println("Container string value: " + actualString);
            System.out.println("Mapped pair left: " + leftInt);
            System.out.println("Sequenced results size: " + sequencedList.size());

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Summary of Container Problems ===");
        System.out.println("1. NO TYPE SAFETY: Everything returns Object, requires casting");
        System.out.println("2. RUNTIME ERRORS: ClassCastException risks everywhere");
        System.out.println("3. VERBOSE CODE: Must use instanceof checks instead of pattern matching");
        System.out.println("4. NO COMPILE-TIME GUARANTEES: Type errors only discovered at runtime");
        System.out.println("5. UNCLEAR CONTRACTS: Method signatures don't indicate types");
        System.out.println("6. POOR IDE SUPPORT: No auto-completion or type hints");
        System.out.println("7. MAINTENANCE NIGHTMARE: Refactoring is error-prone");
        System.out.println("8. PERFORMANCE OVERHEAD: Boxing/unboxing and type checking");
        System.out.println("9. NO SEALED TYPE BENEFITS: Can't constrain implementations");
        System.out.println("10. COMPLEX ERROR HANDLING: Must check types everywhere");
    }
}

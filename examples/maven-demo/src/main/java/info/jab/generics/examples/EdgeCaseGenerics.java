package info.jab.generics.examples;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Edge cases and advanced scenarios in Java generics including wildcard capture,
 * heap pollution prevention, heterogeneous containers, and complex variance patterns.
 */
public class EdgeCaseGenerics {

    // Wildcard capture helper pattern
    public static class WildcardCapture {

        // Public method with wildcard - cannot write to the list
        public static void swap(List<?> list, int i, int j) {
            swapHelper(list, i, j); // Capture wildcard as concrete type parameter
        }

        // Private helper method captures the wildcard type
        private static <T> void swapHelper(List<T> list, int i, int j) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }

        // More complex wildcard capture with transformation
        public static List<?> reverse(List<?> list) {
            return reverseHelper(list);
        }

        private static <T> List<T> reverseHelper(List<T> list) {
            List<T> reversed = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                reversed.add(list.get(i));
            }
            return reversed;
        }

        // Wildcard capture with map transformation
        public static <R> List<R> transformList(List<?> source, Function<Object, R> transformer) {
            return transformListHelper(source, transformer);
        }

        private static <T, R> List<R> transformListHelper(List<T> source, Function<Object, R> transformer) {
            List<R> result = new ArrayList<>();
            for (T item : source) {
                result.add(transformer.apply(item));
            }
            return result;
        }
    }

    // Safe varargs patterns to prevent heap pollution
    public static class SafeVarargsExamples {

        // Safe: doesn't store or return the varargs array
        @SafeVarargs
        public static <T> List<T> createList(T... elements) {
            List<T> list = new ArrayList<>();
            Collections.addAll(list, elements);
            return list;
        }

        // Safe: only reads from varargs array
        @SafeVarargs
        public static <T> void printAll(T... elements) {
            for (T element : elements) {
                System.out.println(element);
            }
        }

        // Safe: transforms but doesn't expose varargs array
        @SafeVarargs
        public static <T, R> List<R> mapVarargs(Function<T, R> mapper, T... elements) {
            List<R> result = new ArrayList<>();
            for (T element : elements) {
                result.add(mapper.apply(element));
            }
            return result;
        }

        // Example of UNSAFE varargs (don't do this)
        // This would cause heap pollution if annotated with @SafeVarargs
        public static <T> void unsafeVarargs(List<T>... lists) {
            Object[] array = lists;              // Arrays are covariant
            array[0] = Arrays.asList("poison");  // Heap pollution
            // T item = lists[0].get(0);         // ClassCastException at runtime
        }

        // Safe alternative to the above
        public static <T> void safeListOperation(List<List<T>> lists) {
            // Type-safe operations only
            for (List<T> list : lists) {
                System.out.println("List size: " + list.size());
            }
        }
    }

    // Advanced heterogeneous container patterns
    public static class AdvancedHeterogeneousContainer {
        private final Map<TypeKey<?>, Object> storage = new ConcurrentHashMap<>();

        // Type-safe key with additional metadata
        public static class TypeKey<T> {
            private final Class<T> type;
            private final String name;
            private final int hashCode;

            public TypeKey(Class<T> type, String name) {
                this.type = Objects.requireNonNull(type);
                this.name = Objects.requireNonNull(name);
                this.hashCode = Objects.hash(type, name);
            }

            public Class<T> getType() {
                return type;
            }

            public String getName() {
                return name;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof TypeKey<?> other)) return false;
                return type.equals(other.type) && name.equals(other.name);
            }

            @Override
            public int hashCode() {
                return hashCode;
            }

            @Override
            public String toString() {
                return "TypeKey[" + type.getSimpleName() + ":" + name + "]";
            }
        }

        public <T> void put(TypeKey<T> key, T value) {
            Objects.requireNonNull(key);
            if (value != null && !key.getType().isInstance(value)) {
                throw new ClassCastException("Value is not of type " + key.getType());
            }
            if (value == null) {
                storage.remove(key);  // Handle null values by removing the key
            } else {
                storage.put(key, value);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T get(TypeKey<T> key) {
            Object value = storage.get(key);
            return value == null ? null : (T) value;
        }

        public <T> Optional<T> getOptional(TypeKey<T> key) {
            return Optional.ofNullable(get(key));
        }

        public <T> boolean contains(TypeKey<T> key) {
            return storage.containsKey(key);
        }

        public <T> T remove(TypeKey<T> key) {
            return key.getType().cast(storage.remove(key));
        }

        // Advanced: get all values of a particular type
        @SuppressWarnings("unchecked")
        public <T> Map<String, T> getAllOfType(Class<T> type) {
            Map<String, T> result = new HashMap<>();
            for (Map.Entry<TypeKey<?>, Object> entry : storage.entrySet()) {
                TypeKey<?> key = entry.getKey();
                if (key.getType().equals(type)) {
                    result.put(key.getName(), (T) entry.getValue());
                }
            }
            return result;
        }

        // Advanced: get all values assignable to a type
        @SuppressWarnings("unchecked")
        public <T> Map<String, T> getAllAssignableTo(Class<T> type) {
            Map<String, T> result = new HashMap<>();
            for (Map.Entry<TypeKey<?>, Object> entry : storage.entrySet()) {
                TypeKey<?> key = entry.getKey();
                Object value = entry.getValue();
                if (type.isAssignableFrom(key.getType()) && value != null) {
                    result.put(key.getName(), (T) value);
                }
            }
            return result;
        }
    }

    // Complex variance scenarios
    public static class VarianceExamples {

        // Covariant producer pattern with complex nested types
        public static <T> List<? extends T> produceItems(
                Supplier<? extends Collection<? extends T>> collectionSupplier) {

            Collection<? extends T> collection = collectionSupplier.get();
            return new ArrayList<>(collection);
        }

        // Contravariant consumer pattern with complex operations
        public static <T> void consumeItems(
                Collection<? super T> destination,
                Function<? super T, ? extends Collection<? extends T>> expander,
                Collection<? extends T> sources) {

            for (T source : sources) {
                Collection<? extends T> expanded = expander.apply(source);
                destination.addAll(expanded);
            }
        }

        // Complex PECS example with multiple levels
        public static <T> void complexTransfer(
                Map<String, Collection<? super T>> destinations,
                Map<String, ? extends Collection<? extends T>> sources,
                Function<String, String> keyMapper) {

            for (Map.Entry<String, ? extends Collection<? extends T>> entry : sources.entrySet()) {
                String sourceKey = entry.getKey();
                String destKey = keyMapper.apply(sourceKey);

                Collection<? super T> destination = destinations.get(destKey);
                Collection<? extends T> source = entry.getValue();

                if (destination != null && source != null) {
                    destination.addAll(source);
                }
            }
        }

        // Recursive variance with bounds
        public static <T extends Comparable<? super T>>
                List<T> mergeMultiple(List<? extends List<? extends T>> lists) {

            List<T> result = new ArrayList<>();
            for (List<? extends T> list : lists) {
                result.addAll(list);
            }
            result.sort(Comparable::compareTo);
            return result;
        }
    }

    // Array and generic interaction edge cases
    public static class ArrayGenericsInteraction {

        // Safe generic array creation
        @SuppressWarnings("unchecked")
        public static <T> T[] createArray(Class<T> componentType, int size) {
            return (T[]) Array.newInstance(componentType, size);
        }

        // Generic array with initialization
        @SafeVarargs
        public static <T> T[] createArrayWith(Class<T> componentType, T... elements) {
            T[] array = createArray(componentType, elements.length);
            System.arraycopy(elements, 0, array, 0, elements.length);
            return array;
        }

        // Convert between arrays and generics safely
        public static <T> List<T> arrayToList(T[] array) {
            return Arrays.asList(array); // Safe - preserves type
        }

        @SuppressWarnings("unchecked")
        public static <T> T[] listToArray(List<T> list, Class<T> componentType) {
            T[] array = createArray(componentType, list.size());
            return list.toArray(array);
        }

        // Demonstrate why generic arrays are problematic
        public static void demonstrateArrayProblems() {
            // This would not compile (good!):
            // List<String>[] arrays = new List<String>[10]; // Compilation error

            // But this compiles and can cause problems:
            @SuppressWarnings("unchecked")
            List<String>[] arrays = (List<String>[]) new List[10];

            // This is safe:
            List<String>[] betterArrays = createArray(List.class, 10);
            for (int i = 0; i < betterArrays.length; i++) {
                betterArrays[i] = new ArrayList<>();
            }
        }
    }

    // Bridge methods and inheritance edge cases
    public static class BridgeMethodScenarios {

        // Generic interface
        public interface Processor<T> {
            void process(T item);
            T create();
        }

        // Raw type implementation (legacy code scenario)
        @SuppressWarnings({"rawtypes", "unchecked"})
        public static class RawProcessor implements Processor {
            @Override
            public void process(Object item) {
                System.out.println("Processing: " + item);
            }

            @Override
            public Object create() {
                return "Created object";
            }
        }

        // Generic implementation
        public static class StringProcessor implements Processor<String> {
            @Override
            public void process(String item) {
                System.out.println("Processing string: " + item);
            }

            @Override
            public String create() {
                return "Created string";
            }
        }

        // Demonstrating bridge method interactions
        public static void demonstrateBridgeMethods() {
            Processor<String> stringProcessor = new StringProcessor();
            stringProcessor.process("Hello");

            // Raw type usage (generates unchecked warnings)
            @SuppressWarnings({"rawtypes", "unchecked"})
            Processor rawProcessor = new RawProcessor();
            rawProcessor.process("Any object"); // Unchecked call
        }
    }

    // Generic method overloading edge cases
    public static class GenericOverloading {

                // These methods demonstrate overloading with generics
        public static <T> void processGeneric(List<T> list) {
            System.out.println("Processing generic list");
        }

        public static void processString(List<String> list) {
            System.out.println("Processing string list");
        }

        // After type erasure, these would have the same signature (problematic):
        // public static <T> void ambiguous(List<T> list) { }
        // public static <T> void ambiguous(List<String> list) { } // Won't compile

        // Different number of type parameters works:
        public static <T> void multiParam(List<T> list) {
            System.out.println("Single type parameter");
        }

        public static <T, U> void multiParam(List<T> list, Class<U> type) {
            System.out.println("Two type parameters");
        }

        // Different bounds work:
        public static <T extends Number> void bounded(T value) {
            System.out.println("Number bound: " + value);
        }

        public static <T extends String> void bounded(T value) {
            System.out.println("String bound: " + value);
        }
    }

    // Demonstration methods
    public static void demonstrateEdgeCases() {
        // Wildcard capture demonstrations
        List<String> stringList = Arrays.asList("a", "b", "c");
        WildcardCapture.swap(stringList, 0, 2);
        System.out.println("After swap: " + stringList);

        List<?> wildcardList = Arrays.asList(1, 2, 3);
        List<?> reversed = WildcardCapture.reverse(wildcardList);
        System.out.println("Reversed: " + reversed);

                // Safe varargs demonstrations
        List<String> created = SafeVarargsExamples.createList("a", "b", "c");
        SafeVarargsExamples.printAll("x", "y", "z");

        List<Integer> lengths = SafeVarargsExamples.mapVarargs(String::length, "hello", "world");

        // Advanced heterogeneous container
        AdvancedHeterogeneousContainer container = new AdvancedHeterogeneousContainer();
        AdvancedHeterogeneousContainer.TypeKey<String> nameKey =
            new AdvancedHeterogeneousContainer.TypeKey<>(String.class, "name");
        AdvancedHeterogeneousContainer.TypeKey<Integer> ageKey =
            new AdvancedHeterogeneousContainer.TypeKey<>(Integer.class, "age");

        container.put(nameKey, "John Doe");
        container.put(ageKey, 30);

        String name = container.get(nameKey);
        Integer age = container.get(ageKey);

        Map<String, String> allStrings = container.getAllOfType(String.class);

        // Variance examples
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<? extends Number> numbers = VarianceExamples.produceItems(() -> integers);

        // Array-generics interaction
        String[] stringArray = ArrayGenericsInteraction.createArrayWith(
            String.class, "a", "b", "c");
        List<String> backToList = ArrayGenericsInteraction.arrayToList(stringArray);

                // Generic overloading
        List<String> testList = Arrays.asList("test");
        GenericOverloading.processString(testList); // Calls specific String version

        List<Integer> intList = Arrays.asList(1, 2, 3);
        GenericOverloading.processGeneric(intList); // Calls generic version

        System.out.println("Edge cases demonstration completed");
        System.out.println("String list after operations: " + stringList);
        System.out.println("Created list: " + created);
        System.out.println("Mapped lengths: " + lengths);
        System.out.println("Container name: " + name);
        System.out.println("Container age: " + age);
        System.out.println("All strings in container: " + allStrings);
        System.out.println("Numbers from variance: " + numbers);
        System.out.println("Array to list conversion: " + backToList);
    }
}

package info.jab.generics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Examples showing the problems solved by advanced generics edge case handling,
 * wildcard capture, type erasure workarounds, and safe varargs patterns.
 * This demonstrates the "before" state without these advanced techniques.
 *
 * PROBLEMS DEMONSTRATED:
 * 1. No wildcard capture - can't modify unknown types
 * 2. Heap pollution with varargs
 * 3. No type-safe heterogeneous containers
 * 4. Complex variance handling
 * 5. Array-generics interaction problems
 * 6. Bridge method complications
 * 7. Type erasure limitations
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RawCollections {

    // PROBLEM: No wildcard capture - can't swap elements of unknown type
    public static class UnsafeWildcardOperations {

        // PROBLEM: Can't swap elements because we don't know the type
        public static void attemptSwap(List list, int i, int j) {
            // PROBLEM: This is unsafe - we can't guarantee types match
            try {
                Object temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                // This might work or might not, depending on the actual list type
            } catch (Exception e) {
                throw new RuntimeException("Swap failed - type safety not guaranteed", e);
            }
        }

        // PROBLEM: Can't safely reverse a list of unknown type
        public static List attemptReverse(List list) {
            List reversed = new ArrayList(); // PROBLEM: Raw list, no type safety
            for (int i = list.size() - 1; i >= 0; i--) {
                reversed.add(list.get(i)); // PROBLEM: Could add wrong types
            }
            return reversed;
        }

        // PROBLEM: Transformation without type capture
        public static List transformList(List source, Object transformer) {
            if (!(transformer instanceof Function)) {
                throw new RuntimeException("Transformer must be a Function");
            }

            Function func = (Function) transformer;
            List result = new ArrayList(); // PROBLEM: Raw list
            for (Object item : source) {
                try {
                    result.add(func.apply(item)); // PROBLEM: No type safety
                } catch (ClassCastException e) {
                    throw new RuntimeException("Transformation failed for item: " + item, e);
                }
            }
            return result;
        }
    }

    // PROBLEM: Unsafe varargs that cause heap pollution
    public static class HeapPollutionExamples {

        // PROBLEM: This causes heap pollution!
        public static List createUnsafeList(Object... elements) {
            List list = new ArrayList(); // PROBLEM: Raw list
            for (Object element : elements) {
                list.add(element); // PROBLEM: No type checking
            }
            return list;
        }

        // PROBLEM: Demonstrates heap pollution
        public static void demonstrateHeapPollution(List... lists) {
            Object[] array = lists;              // Arrays are covariant
            array[0] = Arrays.asList("poison");  // Heap pollution happens here!
            // If caller passed List<Integer>[], they'll get ClassCastException later
        }

        // PROBLEM: Unsafe array creation with generics
        public static Object[] createGenericArray(Object... elements) {
            // PROBLEM: No way to ensure type safety
            return elements; // This array could contain mixed types
        }

        // PROBLEM: Type-unsafe mapping of varargs
        public static List mapUnsafe(Object mapper, Object... elements) {
            if (!(mapper instanceof Function)) {
                throw new RuntimeException("Mapper must be a Function");
            }

            Function func = (Function) mapper;
            List result = new ArrayList(); // PROBLEM: Raw list
            for (Object element : elements) {
                try {
                    result.add(func.apply(element));
                } catch (ClassCastException e) {
                    throw new RuntimeException("Mapping failed", e);
                }
            }
            return result;
        }

        // PROBLEM: Printing without type safety
        public static void printAllUnsafe(Object... elements) {
            for (Object element : elements) {
                System.out.println(element); // Works but no compile-time guarantees
            }
        }
    }

    // PROBLEM: Heterogeneous container without type safety
    public static class UnsafeHeterogeneousContainer {
        private final Map storage = new ConcurrentHashMap(); // PROBLEM: Raw map

        // PROBLEM: Simple key-value storage without type checking
        public void put(Object key, Object value) {
            storage.put(key, value); // PROBLEM: Any key, any value
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

        // PROBLEM: Get all values of a type - complex and unsafe
        public Map getAllOfType(Class type) { // PROBLEM: Raw Map
            Map result = new HashMap();
            for (Object entry : storage.entrySet()) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();

                // PROBLEM: No clean way to associate types with keys
                if (value != null && type.isInstance(value)) {
                    result.put(key, value);
                }
            }
            return result;
        }

        // PROBLEM: Get all assignable values - more complex logic
        public Map getAllAssignableTo(Class type) { // PROBLEM: Raw Map
            Map result = new HashMap();
            for (Object entry : storage.entrySet()) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();

                if (value != null && type.isAssignableFrom(value.getClass())) {
                    result.put(key, value);
                }
            }
            return result;
        }
    }

    // PROBLEM: Variance handling without proper wildcards
    public static class UnsafeVarianceOperations {

        // PROBLEM: Producer pattern without covariance safety
        public static List produceItems(Object collectionSupplier) {
            if (!(collectionSupplier instanceof Supplier)) {
                throw new RuntimeException("Must provide a Supplier");
            }

            Supplier supplier = (Supplier) collectionSupplier;
            Object result = supplier.get();

            if (!(result instanceof Collection)) {
                throw new RuntimeException("Supplier must produce a Collection");
            }

            Collection collection = (Collection) result;
            return new ArrayList(collection); // PROBLEM: Raw list, no type safety
        }

        // PROBLEM: Consumer pattern without contravariance safety
        public static void consumeItems(Collection destination, Object expander, Collection sources) {
            if (!(expander instanceof Function)) {
                throw new RuntimeException("Expander must be a Function");
            }

            Function expanderFunc = (Function) expander;

            for (Object source : sources) {
                try {
                    Object expanded = expanderFunc.apply(source);
                    if (expanded instanceof Collection) {
                        destination.addAll((Collection) expanded);
                    } else {
                        throw new RuntimeException("Expander must return Collection");
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Expansion failed", e);
                }
            }
        }

        // PROBLEM: Complex transfer without PECS
        public static void complexTransfer(Map destinations, Map sources, Object keyMapper) {
            if (!(keyMapper instanceof Function)) {
                throw new RuntimeException("Key mapper must be a Function");
            }

            Function mapper = (Function) keyMapper;

            for (Object entry : sources.entrySet()) {
                Map.Entry sourceEntry = (Map.Entry) entry;
                Object sourceKey = sourceEntry.getKey();
                Object sourceValue = sourceEntry.getValue();

                try {
                    Object destKey = mapper.apply(sourceKey);
                    Object destination = destinations.get(destKey);

                    if (destination instanceof Collection && sourceValue instanceof Collection) {
                        Collection destCollection = (Collection) destination;
                        Collection sourceCollection = (Collection) sourceValue;
                        destCollection.addAll(sourceCollection); // PROBLEM: No type safety
                    }
                } catch (ClassCastException e) {
                    throw new RuntimeException("Transfer failed", e);
                }
            }
        }

        // PROBLEM: Merge without proper bounds
        public static List mergeMultiple(List lists) { // PROBLEM: Raw list of lists
            List result = new ArrayList(); // PROBLEM: Raw result list
            for (Object listObj : lists) {
                if (listObj instanceof List) {
                    List list = (List) listObj;
                    result.addAll(list); // PROBLEM: No type checking
                }
            }

            // PROBLEM: Can't sort safely without knowing element type
            try {
                Collections.sort(result); // Might throw ClassCastException
            } catch (ClassCastException e) {
                System.out.println("Warning: Could not sort merged list - elements not comparable");
            }

            return result;
        }
    }

    // PROBLEM: Array-generics interaction without proper handling
    public static class UnsafeArrayOperations {

        // PROBLEM: Generic array creation is problematic
        public static Object createArray(Class componentType, int size) {
            return Array.newInstance(componentType, size); // Returns Object, must cast
        }

        // PROBLEM: Array creation with initialization
        public static Object createArrayWith(Class componentType, Object... elements) {
            Object array = createArray(componentType, elements.length);

            // PROBLEM: Must use reflection and casting
            for (int i = 0; i < elements.length; i++) {
                try {
                    Array.set(array, i, elements[i]); // No compile-time type checking
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Element " + i + " is not compatible with array type", e);
                }
            }
            return array;
        }

        // PROBLEM: Convert between arrays and lists unsafely
        public static List arrayToList(Object array) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException("Not an array");
            }

            List list = new ArrayList(); // PROBLEM: Raw list
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(array, i)); // PROBLEM: No type safety
            }
            return list;
        }

        public static Object listToArray(List list, Class componentType) {
            Object array = createArray(componentType, list.size());

            int i = 0;
            for (Object item : list) {
                try {
                    Array.set(array, i++, item); // PROBLEM: Runtime type checking only
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("List contains incompatible element", e);
                }
            }
            return array;
        }

        // PROBLEM: Demonstrate why generic arrays are problematic
        public static void demonstrateArrayProblems() {
            // PROBLEM: This would create an unsafe array
            List[] arrays = new List[10]; // Raw array of raw Lists

            // PROBLEM: We can put any kind of List in any position
            arrays[0] = new ArrayList();
            arrays[0].add("String");
            arrays[1] = new ArrayList();
            arrays[1].add(42);

            // PROBLEM: No way to ensure type consistency
            for (List list : arrays) {
                if (list != null) {
                    for (Object item : list) {
                        System.out.println("Item: " + item);
                        // No guarantee about item type
                    }
                }
            }
        }
    }

    // PROBLEM: Bridge method complications with raw types
    public static class BridgeMethodProblems {

        // PROBLEM: Raw processor interface
        public interface RawProcessor {
            void process(Object item); // PROBLEM: Object parameter
            Object create();           // PROBLEM: Object return
        }

        // PROBLEM: Raw implementation
        public static class RawStringProcessor implements RawProcessor {
            @Override
            public void process(Object item) {
                // PROBLEM: Must check type at runtime
                if (item instanceof String) {
                    String str = (String) item;
                    System.out.println("Processing string: " + str);
                } else {
                    throw new RuntimeException("Expected String but got " +
                                             (item != null ? item.getClass() : "null"));
                }
            }

            @Override
            public Object create() { // PROBLEM: Returns Object
                return "Created string";
            }

            // PROBLEM: Additional methods require casting
            public String createString() {
                Object result = create();
                return (String) result; // Unsafe cast
            }
        }

        // PROBLEM: Mixed raw and parameterized usage
        public static void demonstrateBridgeIssues() {
            RawProcessor processor = new RawStringProcessor();

            processor.process("Hello"); // Works

            try {
                processor.process(42); // Runtime error!
            } catch (RuntimeException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            Object created = processor.create(); // Must cast
            String str = (String) created; // Unsafe
            System.out.println("Created: " + str);
        }
    }

    // PROBLEM: Generic method overloading issues
    public static class UnsafeOverloading {

        // PROBLEM: Can't distinguish overloads after type erasure
        public static void processGeneric(List list) {
            System.out.println("Processing generic list of size: " + list.size());
            // PROBLEM: No way to know element type
        }

        public static void processString(List list) {
            System.out.println("Processing list (assuming strings)");
            // PROBLEM: Can't guarantee it contains strings
            for (Object item : list) {
                if (item instanceof String) {
                    String str = (String) item;
                    System.out.println("String: " + str);
                } else {
                    System.out.println("Non-string: " + item);
                }
            }
        }

        // PROBLEM: Methods with different numbers of parameters
        public static void multiParam(List list) {
            System.out.println("Single parameter version");
        }

        public static void multiParam(List list, Class type) {
            System.out.println("Two parameter version for type: " + type.getSimpleName());
            // PROBLEM: Can't guarantee list contains elements of that type
        }

        // PROBLEM: Can't have truly bounded methods without generics
        public static void boundedNumber(Object value) {
            // PROBLEM: Must check if it's a Number at runtime
            if (value instanceof Number) {
                Number num = (Number) value;
                System.out.println("Number: " + num.doubleValue());
            } else {
                throw new RuntimeException("Expected Number but got " +
                                         (value != null ? value.getClass() : "null"));
            }
        }
    }

    // PROBLEM: Type erasure workarounds are complex and error-prone
    public static class TypeErasureProblems {

        // PROBLEM: No way to preserve type information
        public static class UnsafeTypeContainer {
            private final Map storage = new HashMap(); // PROBLEM: Raw map

            public void put(Class type, Object instance) {
                // PROBLEM: No guarantee instance is of correct type
                storage.put(type, instance);
            }

            public Object get(Class type) { // PROBLEM: Returns Object
                return storage.get(type);
            }

            // PROBLEM: Complex type checking logic
            public List getInstancesOfType(Class type) { // PROBLEM: Raw list
                List instances = new ArrayList();
                for (Object entry : storage.entrySet()) {
                    Map.Entry mapEntry = (Map.Entry) entry;
                    Class key = (Class) mapEntry.getKey();
                    Object value = mapEntry.getValue();

                    if (type.isAssignableFrom(key)) {
                        instances.add(value);
                    }
                }
                return instances;
            }
        }

        // PROBLEM: Generic factory without type safety
        public static class UnsafeFactory {
            private final Class type;
            private final Map constructors = new HashMap(); // PROBLEM: Raw map

            public UnsafeFactory(Class type) {
                this.type = type;
            }

            public UnsafeFactory registerConstructor(String name, Object constructor) {
                constructors.put(name, constructor); // PROBLEM: No type checking
                return this;
            }

            public Object create(String constructorName, Object... args) { // PROBLEM: Returns Object
                Object constructor = constructors.get(constructorName);
                if (constructor == null) {
                    throw new IllegalArgumentException("No constructor: " + constructorName);
                }

                // PROBLEM: Must check constructor type at runtime
                if (constructor instanceof Function) {
                    Function func = (Function) constructor;
                    try {
                        return func.apply(args);
                    } catch (ClassCastException e) {
                        throw new RuntimeException("Constructor failed", e);
                    }
                } else {
                    throw new RuntimeException("Constructor is not a Function");
                }
            }

            public Class getType() {
                return type;
            }
        }

        // PROBLEM: Collection casting without safety
        public static List castList(List list, Class elementType) { // PROBLEM: Raw types
            // PROBLEM: Must check every element
            for (Object item : list) {
                if (item != null && !elementType.isInstance(item)) {
                    throw new ClassCastException(
                        "List contains element of type " + item.getClass().getName() +
                        " which is not assignable to " + elementType.getName()
                    );
                }
            }
            return list; // PROBLEM: Still raw, just "checked"
        }

        public static Map castMap(Map map, Class keyType, Class valueType) { // PROBLEM: Raw types
            // PROBLEM: Must check every entry
            for (Object entry : map.entrySet()) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();

                if (key != null && !keyType.isInstance(key)) {
                    throw new ClassCastException("Invalid key type");
                }
                if (value != null && !valueType.isInstance(value)) {
                    throw new ClassCastException("Invalid value type");
                }
            }
            return map; // PROBLEM: Still raw, just "checked"
        }
    }

    // Demonstration of all the problems
    public static void demonstrateCollectionProblems() {
        System.out.println("=== Demonstrating Collection Problems Without Advanced Generics ===");

        try {
            // PROBLEM: Wildcard operations are unsafe
            List stringList = Arrays.asList("a", "b", "c"); // PROBLEM: Raw list
            UnsafeWildcardOperations.attemptSwap(stringList, 0, 2);
            System.out.println("After unsafe swap: " + stringList);

            List unknownList = Arrays.asList(1, 2, 3); // PROBLEM: Raw list
            List reversed = UnsafeWildcardOperations.attemptReverse(unknownList);
            System.out.println("Reversed: " + reversed);

            // PROBLEM: Heap pollution demonstrations
            List created = HeapPollutionExamples.createUnsafeList("a", "b", "c");
            HeapPollutionExamples.printAllUnsafe("x", "y", "z");

            List mapped = HeapPollutionExamples.mapUnsafe(
                (Function<String, Integer>) String::length, "hello", "world");
            System.out.println("Mapped lengths: " + mapped);

            // PROBLEM: Unsafe heterogeneous container
            UnsafeHeterogeneousContainer container = new UnsafeHeterogeneousContainer();
            container.put(String.class, "Hello");
            container.put("wrong key", 42); // This shouldn't be allowed!

            Object value = container.get(String.class); // PROBLEM: Returns Object
            String str = (String) value; // PROBLEM: Must cast

            // PROBLEM: This could fail at runtime
            try {
                String wrongCast = (String) container.get("wrong key");
            } catch (ClassCastException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            // PROBLEM: Variance operations are unsafe
            List integers = Arrays.asList(1, 2, 3); // PROBLEM: Raw list
            List produced = UnsafeVarianceOperations.produceItems((Supplier) () -> integers);
            System.out.println("Produced: " + produced);

            // PROBLEM: Array operations are complex and unsafe
            Object stringArray = UnsafeArrayOperations.createArrayWith(String.class, "a", "b", "c");
            List backToList = UnsafeArrayOperations.arrayToList(stringArray);
            System.out.println("Array to list: " + backToList);

            // PROBLEM: Bridge method issues
            BridgeMethodProblems.demonstrateBridgeIssues();

            // PROBLEM: Overloading confusion
            List testList = Arrays.asList("test"); // PROBLEM: Raw list
            UnsafeOverloading.processString(testList);

            List intList = Arrays.asList(1, 2, 3); // PROBLEM: Raw list
            UnsafeOverloading.processGeneric(intList);

            // PROBLEM: Type erasure workarounds are complex
            TypeErasureProblems.UnsafeTypeContainer typeContainer =
                new TypeErasureProblems.UnsafeTypeContainer();
            typeContainer.put(String.class, "Hello");
            typeContainer.put(Integer.class, "Wrong type!"); // This is allowed but wrong!

            Object retrieved = typeContainer.get(String.class); // PROBLEM: Returns Object
            String retrievedStr = (String) retrieved; // PROBLEM: Must cast

            System.out.println("Collection problems demonstration completed");
            System.out.println("Created list: " + created);
            System.out.println("Mapped list: " + mapped);
            System.out.println("Container value: " + str);
            System.out.println("Produced list: " + produced);
            System.out.println("Array conversion: " + backToList);
            System.out.println("Type container value: " + retrievedStr);

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Summary of Collection Problems ===");
        System.out.println("1. NO WILDCARD CAPTURE: Can't safely modify collections of unknown type");
        System.out.println("2. HEAP POLLUTION: Varargs can corrupt generic arrays");
        System.out.println("3. NO TYPE-SAFE CONTAINERS: Heterogeneous storage requires manual checking");
        System.out.println("4. VARIANCE COMPLICATIONS: Producer/consumer patterns are unsafe");
        System.out.println("5. ARRAY-GENERIC ISSUES: Complex interaction between arrays and generics");
        System.out.println("6. BRIDGE METHOD PROBLEMS: Raw types cause method signature confusion");
        System.out.println("7. TYPE ERASURE LIMITATIONS: No runtime type information available");
        System.out.println("8. OVERLOADING AMBIGUITY: Methods conflict after type erasure");
        System.out.println("9. CASTING EVERYWHERE: All operations require unsafe casts");
        System.out.println("10. RUNTIME FAILURES: Type errors only discovered during execution");
    }
}

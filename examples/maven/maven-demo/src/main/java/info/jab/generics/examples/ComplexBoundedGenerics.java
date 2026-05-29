package info.jab.generics.examples;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Complex examples of bounded generics showcasing multiple constraints,
 * covariance/contravariance scenarios, and edge cases where generics
 * provide significant value over raw types.
 */
public class ComplexBoundedGenerics {

    // Multiple bounds with intersection types
    public static <T extends Number & Comparable<T> & Serializable> T findMedian(List<T> numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        List<T> sorted = new ArrayList<>(numbers);
        sorted.sort(Comparable::compareTo); // Works because T extends Comparable<T>

        int middle = sorted.size() / 2;
        return sorted.get(middle);
    }

    // Complex PECS example with nested wildcards
    public static <T> void complexTransfer(
            Collection<? super T> destination,
            Collection<? extends Collection<? extends T>> sources) {

        for (Collection<? extends T> source : sources) {
            destination.addAll(source);
        }
    }

    // Bounded wildcard with multiple type parameters
    public static <K extends Comparable<K>, V> Map<K, V> mergeMaps(
            Map<? extends K, ? extends V> map1,
            Map<? extends K, ? extends V> map2,
            BinaryOperator<V> valuesMerger) {

        Map<K, V> result = new TreeMap<>(); // TreeMap requires Comparable keys

        map1.forEach(result::put);
        map2.forEach((key, value) ->
            result.merge(key, value, valuesMerger));

        return result;
    }

    // Complex producer-consumer pattern with transformations
    public static <T, R> Collection<R> transformAndCollect(
            Collection<? extends T> source,
            Function<? super T, ? extends R> mapper,
            Supplier<? extends Collection<R>> collectionFactory) {

        Collection<R> result = collectionFactory.get();
        source.forEach(item -> result.add(mapper.apply(item)));
        return result;
    }

    // Recursive generics with bounds
    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> list) {
        if (list.size() <= 1) {
            return new ArrayList<>(list);
        }

        int mid = list.size() / 2;
        List<T> left = mergeSort(list.subList(0, mid));
        List<T> right = mergeSort(list.subList(mid, list.size()));

        return merge(left, right);
    }

    private static <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right) {
        List<T> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            T leftItem = left.get(leftIndex);
            T rightItem = right.get(rightIndex);

            if (leftItem.compareTo(rightItem) <= 0) {
                result.add(leftItem);
                leftIndex++;
            } else {
                result.add(rightItem);
                rightIndex++;
            }
        }

        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }

    // Generic method with flexible constraints using interfaces
    public static <T extends Collection<? extends Number>>
            double calculateWeightedAverage(T numbers, Function<Number, Double> weightFunction) {

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Number number : numbers) {
            double weight = weightFunction.apply(number);
            weightedSum += number.doubleValue() * weight;
            totalWeight += weight;
        }

        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    // Covariant return type with generics
    public abstract static class AbstractProcessor<T, R> {
        public abstract Collection<? extends R> process(Collection<? extends T> input);

        // Method that benefits from covariance
        public final <U extends R> Collection<U> processAndFilter(
                Collection<? extends T> input,
                Class<U> resultType) {

            Collection<? extends R> processed = process(input);
            List<U> filtered = new ArrayList<>();

            for (R item : processed) {
                if (resultType.isInstance(item)) {
                    filtered.add(resultType.cast(item));
                }
            }

            return filtered;
        }
    }

    // Concrete implementation showcasing variance
    public static class StringToNumberProcessor extends AbstractProcessor<String, Number> {
        @Override
        public Collection<? extends Number> process(Collection<? extends String> input) {
            List<Integer> results = new ArrayList<>();
            for (String str : input) {
                try {
                    results.add(str.length()); // String length as a Number
                } catch (NumberFormatException e) {
                    results.add(0);
                }
            }
            return results;
        }
    }

    // Complex generic factory pattern
    public static class GenericFactory<T> {
        private final Class<T> type;
        private final Map<String, Function<Object[], T>> constructors;

        public GenericFactory(Class<T> type) {
            this.type = type;
            this.constructors = new HashMap<>();
        }

        public <P1, P2> GenericFactory<T> registerConstructor(
                String name,
                Class<P1> param1Type,
                Class<P2> param2Type,
                BiFunction<P1, P2, T> constructor) {

            constructors.put(name, args -> {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Expected 2 arguments");
                }

                P1 p1 = param1Type.cast(args[0]);
                P2 p2 = param2Type.cast(args[1]);
                return constructor.apply(p1, p2);
            });

            return this;
        }

        public T create(String constructorName, Object... args) {
            Function<Object[], T> constructor = constructors.get(constructorName);
            if (constructor == null) {
                throw new IllegalArgumentException("Unknown constructor: " + constructorName);
            }
            return constructor.apply(args);
        }

        public Class<T> getType() {
            return type;
        }
    }

    // Usage demonstration methods
    public static void demonstrateComplexScenarios() {
        // Multiple bounds example
        List<Integer> integers = Arrays.asList(1, 5, 3, 9, 2);
        Integer median = findMedian(integers);
        System.out.println("Median: " + median);

        // Complex PECS example
        Collection<Number> numbers = new ArrayList<>();
        Collection<List<Integer>> intLists = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6)
        );
        complexTransfer(numbers, intLists);

        // Map merging with custom logic
        Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> map2 = Map.of("b", 3, "c", 4);
        Map<String, Integer> merged = mergeMaps(map1, map2, Integer::sum);

        // Transformation with custom collection
        List<String> strings = Arrays.asList("hello", "world", "generics");
        Collection<Integer> lengths = transformAndCollect(
            strings,
            String::length,
            LinkedHashSet::new
        );

        // Recursive generics
        List<String> unsorted = Arrays.asList("zebra", "apple", "banana");
        List<String> sorted = mergeSort(unsorted);

        // Factory pattern usage
        GenericFactory<StringBuilder> sbFactory = new GenericFactory<>(StringBuilder.class);
        sbFactory.registerConstructor("withCapacity",
            Integer.class, String.class,
            (capacity, initial) -> new StringBuilder(capacity).append(initial));

        StringBuilder sb = sbFactory.create("withCapacity", 100, "Hello");
    }
}

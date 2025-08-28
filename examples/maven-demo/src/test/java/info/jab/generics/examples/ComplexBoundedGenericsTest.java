package info.jab.generics.examples;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ComplexBoundedGenerics Tests")
class ComplexBoundedGenericsTest {

    @Nested
    @DisplayName("Multiple Bounds Tests")
    class MultipleBoundsTests {

        @Test
        @DisplayName("Should find median of odd-sized Integer list")
        void findMedian_oddSizedIntegerList_returnsMiddleElement() {
            // Given
            List<Integer> numbers = Arrays.asList(5, 1, 9, 3, 7);

            // When
            Integer median = ComplexBoundedGenerics.findMedian(numbers);

            // Then
            assertThat(median).isEqualTo(5);
        }

        @Test
        @DisplayName("Should find median of even-sized Double list")
        void findMedian_evenSizedDoubleList_returnsMiddleElement() {
            // Given
            List<Double> numbers = Arrays.asList(1.5, 2.5, 3.5, 4.5);

            // When
            Double median = ComplexBoundedGenerics.findMedian(numbers);

            // Then
            assertThat(median).isEqualTo(3.5);
        }

        @Test
        @DisplayName("Should find median of single element BigDecimal list")
        void findMedian_singleElementList_returnsSingleElement() {
            // Given
            List<BigDecimal> numbers = Arrays.asList(BigDecimal.valueOf(42.0));

            // When
            BigDecimal median = ComplexBoundedGenerics.findMedian(numbers);

            // Then
            assertThat(median).isEqualTo(BigDecimal.valueOf(42.0));
        }

        @Test
        @DisplayName("Should throw exception when list is empty")
        void findMedian_emptyList_throwsIllegalArgumentException() {
            // Given
            List<Integer> emptyList = Collections.emptyList();

            // When & Then
            assertThatThrownBy(() -> ComplexBoundedGenerics.findMedian(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("List cannot be empty");
        }

        @ParameterizedTest(name = "Should handle {0} with list size {1}")
        @MethodSource("provideNumberListsForMedian")
        @DisplayName("Should find median for various numeric types")
        <T extends Number & Comparable<T> & java.io.Serializable> void findMedian_variousTypes_returnsCorrectMedian(
                String description, List<T> numbers, T expectedMedian) {
            // When
            T median = ComplexBoundedGenerics.findMedian(numbers);

            // Then
            assertThat(median).isEqualTo(expectedMedian);
        }

        static Stream<Arguments> provideNumberListsForMedian() {
            return Stream.of(
                Arguments.of("Integers", Arrays.asList(1, 2, 3), 2),
                Arguments.of("Longs", Arrays.asList(10L, 20L, 30L, 40L, 50L), 30L),
                Arguments.of("Doubles", Arrays.asList(1.1, 2.2), 2.2)
            );
        }
    }

    @Nested
    @DisplayName("Complex PECS Transfer Tests")
    class ComplexPECSTests {

        @Test
        @DisplayName("Should transfer elements from multiple collections to destination")
        void complexTransfer_validCollections_transfersAllElements() {
            // Given
            Collection<Number> destination = new ArrayList<>();
            Collection<Collection<Integer>> sources = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6)
            );

            // When
            ComplexBoundedGenerics.complexTransfer(destination, sources);

            // Then
            assertThat(destination).containsExactly(1, 2, 3, 4, 5, 6);
        }

        @Test
        @DisplayName("Should handle empty source collections")
        void complexTransfer_emptySources_destinationRemainsEmpty() {
            // Given
            Collection<String> destination = new ArrayList<>();
            Collection<Collection<String>> sources = Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList()
            );

            // When
            ComplexBoundedGenerics.complexTransfer(destination, sources);

            // Then
            assertThat(destination).isEmpty();
        }

        @Test
        @DisplayName("Should handle mixed collection types")
        void complexTransfer_mixedCollectionTypes_transfersCorrectly() {
            // Given
            Collection<CharSequence> destination = new ArrayList<>();
            Collection<Collection<String>> sources = Arrays.asList(
                Arrays.asList("hello", "world"),
                Arrays.asList("java", "generics")
            );

            // When
            ComplexBoundedGenerics.complexTransfer(destination, sources);

            // Then
            assertThat(destination).containsExactly("hello", "world", "java", "generics");
        }
    }

    @Nested
    @DisplayName("Map Merging Tests")
    class MapMergingTests {

        @Test
        @DisplayName("Should merge maps with sum value merger")
        void mergeMaps_integerMaps_mergesWithSum() {
            // Given
            Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
            Map<String, Integer> map2 = Map.of("b", 3, "c", 4);
            BinaryOperator<Integer> sumMerger = Integer::sum;

            // When
            Map<String, Integer> result = ComplexBoundedGenerics.mergeMaps(map1, map2, sumMerger);

            // Then
            assertThat(result)
                .containsEntry("a", 1)
                .containsEntry("b", 5)  // 2 + 3
                .containsEntry("c", 4)
                .hasSize(3);
        }

        @Test
        @DisplayName("Should merge maps with max value merger")
        void mergeMaps_integerMaps_mergesWithMax() {
            // Given
            Map<String, Integer> map1 = Map.of("x", 10, "y", 20);
            Map<String, Integer> map2 = Map.of("y", 15, "z", 25);
            BinaryOperator<Integer> maxMerger = Integer::max;

            // When
            Map<String, Integer> result = ComplexBoundedGenerics.mergeMaps(map1, map2, maxMerger);

            // Then
            assertThat(result)
                .containsEntry("x", 10)
                .containsEntry("y", 20)  // max(20, 15)
                .containsEntry("z", 25);
        }

        @Test
        @DisplayName("Should return TreeMap with sorted keys")
        void mergeMaps_result_isTreeMapWithSortedKeys() {
            // Given
            Map<String, String> map1 = Map.of("zebra", "animal");
            Map<String, String> map2 = Map.of("apple", "fruit");

            // When
            Map<String, String> result = ComplexBoundedGenerics.mergeMaps(
                map1, map2, (a, b) -> a + "," + b);

            // Then
            assertThat(result).isInstanceOf(TreeMap.class);
            assertThat(result.keySet()).containsExactly("apple", "zebra");  // Sorted order
        }

        @Test
        @DisplayName("Should handle empty maps")
        void mergeMaps_emptyMaps_returnsEmptyMap() {
            // Given
            Map<String, Integer> emptyMap1 = Collections.emptyMap();
            Map<String, Integer> emptyMap2 = Collections.emptyMap();

            // When
            Map<String, Integer> result = ComplexBoundedGenerics.mergeMaps(
                emptyMap1, emptyMap2, Integer::sum);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Transform and Collect Tests")
    class TransformAndCollectTests {

        @Test
        @DisplayName("Should transform strings to lengths using ArrayList factory")
        void transformAndCollect_stringsToLengths_returnsLengthList() {
            // Given
            Collection<String> source = Arrays.asList("hello", "world", "java");
            Function<String, Integer> lengthMapper = String::length;

            // When
            Collection<Integer> result = ComplexBoundedGenerics.transformAndCollect(
                source, lengthMapper, ArrayList::new);

            // Then
            assertThat(result)
                .isInstanceOf(ArrayList.class)
                .containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should transform integers to strings using LinkedHashSet factory")
        void transformAndCollect_integersToStrings_returnsStringSet() {
            // Given
            Collection<Integer> source = Arrays.asList(1, 2, 3, 2, 1);
            Function<Integer, String> stringMapper = Object::toString;

            // When
            Collection<String> result = ComplexBoundedGenerics.transformAndCollect(
                source, stringMapper, LinkedHashSet::new);

            // Then
            assertThat(result)
                .isInstanceOf(LinkedHashSet.class)
                .containsExactly("1", "2", "3");  // Duplicates removed, order preserved
        }

        @Test
        @DisplayName("Should handle empty source collection")
        void transformAndCollect_emptySource_returnsEmptyCollection() {
            // Given
            Collection<String> emptySource = Collections.emptyList();
            Function<String, Integer> lengthMapper = String::length;

            // When
            Collection<Integer> result = ComplexBoundedGenerics.transformAndCollect(
                emptySource, lengthMapper, ArrayList::new);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Merge Sort Tests")
    class MergeSortTests {

        @Test
        @DisplayName("Should sort integer list correctly")
        void mergeSort_integerList_returnsSortedList() {
            // Given
            List<Integer> unsorted = Arrays.asList(5, 2, 8, 1, 9);

            // When
            List<Integer> sorted = ComplexBoundedGenerics.mergeSort(unsorted);

            // Then
            assertThat(sorted).containsExactly(1, 2, 5, 8, 9);
            assertThat(sorted).isNotSameAs(unsorted);  // Returns new list
        }

        @Test
        @DisplayName("Should sort string list correctly")
        void mergeSort_stringList_returnsSortedList() {
            // Given
            List<String> unsorted = Arrays.asList("zebra", "apple", "banana", "cherry");

            // When
            List<String> sorted = ComplexBoundedGenerics.mergeSort(unsorted);

            // Then
            assertThat(sorted).containsExactly("apple", "banana", "cherry", "zebra");
        }

        @Test
        @DisplayName("Should handle single element list")
        void mergeSort_singleElement_returnsListWithSameElement() {
            // Given
            List<Integer> singleElement = Arrays.asList(42);

            // When
            List<Integer> sorted = ComplexBoundedGenerics.mergeSort(singleElement);

            // Then
            assertThat(sorted).containsExactly(42);
            assertThat(sorted).isNotSameAs(singleElement);
        }

        @Test
        @DisplayName("Should handle empty list")
        void mergeSort_emptyList_returnsEmptyList() {
            // Given
            List<Integer> emptyList = Collections.emptyList();

            // When
            List<Integer> sorted = ComplexBoundedGenerics.mergeSort(emptyList);

            // Then
            assertThat(sorted).isEmpty();
        }

        @Test
        @DisplayName("Should handle already sorted list")
        void mergeSort_alreadySorted_returnsSortedList() {
            // Given
            List<Integer> alreadySorted = Arrays.asList(1, 2, 3, 4, 5);

            // When
            List<Integer> sorted = ComplexBoundedGenerics.mergeSort(alreadySorted);

            // Then
            assertThat(sorted).containsExactly(1, 2, 3, 4, 5);
        }
    }

    @Nested
    @DisplayName("Weighted Average Tests")
    class WeightedAverageTests {

        @Test
        @DisplayName("Should calculate weighted average correctly")
        void calculateWeightedAverage_validNumbers_returnsCorrectAverage() {
            // Given
            List<Integer> numbers = Arrays.asList(10, 20, 30);
            Function<Number, Double> weightFunction = n -> n.doubleValue() / 10.0;

            // When
            double result = ComplexBoundedGenerics.calculateWeightedAverage(numbers, weightFunction);

            // Then
            // Weighted sum: 10*1.0 + 20*2.0 + 30*3.0 = 10 + 40 + 90 = 140
            // Total weight: 1.0 + 2.0 + 3.0 = 6.0
            // Average: 140/6.0 = 23.333...
            assertThat(result).isCloseTo(23.333, org.assertj.core.data.Offset.offset(0.001));
        }

        @Test
        @DisplayName("Should return zero when total weight is zero")
        void calculateWeightedAverage_zeroWeights_returnsZero() {
            // Given
            List<Integer> numbers = Arrays.asList(10, 20, 30);
            Function<Number, Double> zeroWeightFunction = n -> 0.0;

            // When
            double result = ComplexBoundedGenerics.calculateWeightedAverage(numbers, zeroWeightFunction);

            // Then
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle empty collection")
        void calculateWeightedAverage_emptyCollection_returnsZero() {
            // Given
            List<Integer> emptyNumbers = Collections.emptyList();
            Function<Number, Double> weightFunction = n -> 1.0;

            // When
            double result = ComplexBoundedGenerics.calculateWeightedAverage(emptyNumbers, weightFunction);

            // Then
            assertThat(result).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Generic Factory Tests")
    class GenericFactoryTests {

        private ComplexBoundedGenerics.GenericFactory<StringBuilder> factory;

        @BeforeEach
        void setUp() {
            factory = new ComplexBoundedGenerics.GenericFactory<>(StringBuilder.class);
        }

        @Test
        @DisplayName("Should create factory with correct type")
        void constructor_validType_storesType() {
            // Then
            assertThat(factory.getType()).isEqualTo(StringBuilder.class);
        }

        @Test
        @DisplayName("Should register and use constructor correctly")
        void registerConstructor_validConstructor_createsInstanceCorrectly() {
            // Given
            factory.registerConstructor("withString",
                String.class, Integer.class,
                (str, capacity) -> new StringBuilder(capacity).append(str));

            // When
            StringBuilder result = factory.create("withString", "Hello", 100);

            // Then
            assertThat(result.toString()).isEqualTo("Hello");
            assertThat(result.capacity()).isGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should throw exception for unknown constructor")
        void create_unknownConstructor_throwsIllegalArgumentException() {
            // When & Then
            assertThatThrownBy(() -> factory.create("nonExistent", "arg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown constructor: nonExistent");
        }

        @Test
        @DisplayName("Should throw exception for wrong number of arguments")
        void create_wrongNumberOfArguments_throwsIllegalArgumentException() {
            // Given
            factory.registerConstructor("twoParams",
                String.class, Integer.class,
                (str, num) -> new StringBuilder(str + num));

            // When & Then
            assertThatThrownBy(() -> factory.create("twoParams", "onlyOne"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected 2 arguments");
        }
    }

    @Nested
    @DisplayName("String to Number Processor Tests")
    class StringToNumberProcessorTests {

        private ComplexBoundedGenerics.StringToNumberProcessor processor;

        @BeforeEach
        void setUp() {
            processor = new ComplexBoundedGenerics.StringToNumberProcessor();
        }

        @Test
        @DisplayName("Should process strings to their lengths")
        void process_validStrings_returnsLengths() {
            // Given
            Collection<String> input = Arrays.asList("hello", "world", "java");

            // When
            Collection<? extends Number> result = processor.process(input);

            // Then
            assertThat(result).hasSize(3);
            List<Number> numberList = new ArrayList<>(result);
            assertThat(numberList).contains(5, 5, 4);
        }

        @Test
        @DisplayName("Should handle empty string collection")
        void process_emptyCollection_returnsEmptyCollection() {
            // Given
            Collection<String> emptyInput = Collections.emptyList();

            // When
            Collection<? extends Number> result = processor.process(emptyInput);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should process and filter results correctly")
        void processAndFilter_validInput_filtersCorrectly() {
            // Given
            Collection<String> input = Arrays.asList("a", "hello", "hi");

            // When
            Collection<Integer> result = processor.processAndFilter(input, Integer.class);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).contains(1, 5, 2);
        }
    }
}

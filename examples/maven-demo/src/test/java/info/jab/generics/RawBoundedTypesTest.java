package info.jab.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RawBoundedTypes Test")
class RawBoundedTypesTest {

    @Nested
    @DisplayName("FindMedian method tests")
    class FindMedianTests {

        @Test
        @DisplayName("Should find median of integer list")
        void should_findMedian_when_integerListProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List integers = Arrays.asList(1, 5, 3, 9, 2);

            // When
            Object result = RawBoundedTypes.findMedian(integers);

            // Then
            assertThat(result).isEqualTo(3);
        }

        @Test
        @DisplayName("Should find median of string list")
        void should_findMedian_when_stringListProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List strings = Arrays.asList("apple", "banana", "cherry");

            // When
            Object result = RawBoundedTypes.findMedian(strings);

            // Then
            assertThat(result).isEqualTo("banana");
        }

        @Test
        @DisplayName("Should throw exception when list is empty")
        void should_throwException_when_listIsEmpty() {
            // Given
            @SuppressWarnings("rawtypes")
            List emptyList = new ArrayList();

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.findMedian(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("List cannot be empty");
        }

        @Test
        @DisplayName("Should throw runtime exception when elements are not comparable")
        void should_throwRuntimeException_when_elementsNotComparable() {
            // Given
            @SuppressWarnings("rawtypes")
            List mixed = Arrays.asList(1, "hello", 3.14);

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.findMedian(mixed))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Elements are not comparable");
        }

        @Test
        @DisplayName("Should handle single element list")
        void should_returnElement_when_singleElementList() {
            // Given
            @SuppressWarnings("rawtypes")
            List singleElement = Arrays.asList(42);

            // When
            Object result = RawBoundedTypes.findMedian(singleElement);

            // Then
            assertThat(result).isEqualTo(42);
        }
    }

    @Nested
    @DisplayName("UnsafeTransfer method tests")
    class UnsafeTransferTests {

        @Test
        @DisplayName("Should transfer collections successfully")
        void should_transferCollections_when_validInputProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection destination = new ArrayList();
            @SuppressWarnings("rawtypes")
            Collection sources = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
            );

            // When
            RawBoundedTypes.unsafeTransfer(destination, sources);

            // Then
            assertThat(destination).hasSize(6);
            assertThat(destination).containsExactly(1, 2, 3, 4, 5, 6);
        }

        @Test
        @DisplayName("Should throw exception when source is not a collection")
        void should_throwException_when_sourceNotCollection() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection destination = new ArrayList();
            @SuppressWarnings("rawtypes")
            Collection sources = Arrays.asList(
                Arrays.asList(1, 2, 3),
                "not a collection"
            );

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.unsafeTransfer(destination, sources))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expected Collection but got");
        }

        @Test
        @DisplayName("Should handle empty sources collection")
        void should_handleEmptySources_when_noSourcesProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection destination = new ArrayList();
            @SuppressWarnings("rawtypes")
            Collection sources = new ArrayList();

            // When
            RawBoundedTypes.unsafeTransfer(destination, sources);

            // Then
            assertThat(destination).isEmpty();
        }
    }

    @Nested
    @DisplayName("MergeMaps method tests")
    class MergeMapsTests {

        @Test
        @DisplayName("Should merge maps with BinaryOperator")
        void should_mergeMaps_when_binaryOperatorProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Map map1 = new HashMap();
            map1.put("a", 1);
            map1.put("b", 2);

            @SuppressWarnings("rawtypes")
            Map map2 = new HashMap();
            map2.put("b", 3);
            map2.put("c", 4);

            BinaryOperator<Integer> merger = Integer::sum;

            // When
            @SuppressWarnings("rawtypes")
            Map result = RawBoundedTypes.mergeMaps(map1, map2, merger);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get("a")).isEqualTo(1);
            assertThat(result.get("b")).isEqualTo(5); // 2 + 3
            assertThat(result.get("c")).isEqualTo(4);
        }

        @Test
        @DisplayName("Should merge maps with non-BinaryOperator merger")
        void should_mergeMaps_when_nonBinaryOperatorProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Map map1 = new HashMap();
            map1.put("a", 1);
            map1.put("b", 2);

            @SuppressWarnings("rawtypes")
            Map map2 = new HashMap();
            map2.put("b", 3);
            map2.put("c", 4);

            String notAMerger = "not a merger";

            // When
            @SuppressWarnings("rawtypes")
            Map result = RawBoundedTypes.mergeMaps(map1, map2, notAMerger);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get("a")).isEqualTo(1);
            assertThat(result.get("b")).isEqualTo(3); // Overwrites
            assertThat(result.get("c")).isEqualTo(4);
        }

        @Test
        @DisplayName("Should handle empty maps")
        void should_handleEmptyMaps_when_bothMapsEmpty() {
            // Given
            @SuppressWarnings("rawtypes")
            Map map1 = new HashMap();
            @SuppressWarnings("rawtypes")
            Map map2 = new HashMap();
            BinaryOperator<Integer> merger = Integer::sum;

            // When
            @SuppressWarnings("rawtypes")
            Map result = RawBoundedTypes.mergeMaps(map1, map2, merger);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("TransformAndCollect method tests")
    class TransformAndCollectTests {

        @Test
        @DisplayName("Should transform and collect with valid inputs")
        void should_transformAndCollect_when_validInputsProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection source = Arrays.asList("hello", "world", "test");
            Function<String, Integer> mapper = String::length;
            Supplier<Collection> factory = ArrayList::new;

            // When
            @SuppressWarnings("rawtypes")
            Collection result = RawBoundedTypes.transformAndCollect(source, mapper, factory);

            // Then
            assertThat(result).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should throw exception when mapper is not a Function")
        void should_throwException_when_mapperNotFunction() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection source = Arrays.asList("hello", "world");
            String notAMapper = "not a function";
            Supplier<Collection> factory = ArrayList::new;

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.transformAndCollect(source, notAMapper, factory))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mapper is not a Function");
        }

        @Test
        @DisplayName("Should throw exception when factory doesn't produce Collection")
        void should_throwException_when_factoryProducesNonCollection() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection source = Arrays.asList("hello", "world");
            Function<String, Integer> mapper = String::length;
            Supplier<String> badFactory = () -> "not a collection";

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.transformAndCollect(source, mapper, badFactory))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Factory didn't produce a Collection");
        }

        @Test
        @DisplayName("Should use fallback collection when factory is not Supplier")
        void should_useFallback_when_factoryNotSupplier() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection source = Arrays.asList("hello", "world");
            Function<String, Integer> mapper = String::length;
            String notAFactory = "not a factory";

            // When
            @SuppressWarnings("rawtypes")
            Collection result = RawBoundedTypes.transformAndCollect(source, mapper, notAFactory);

            // Then
            assertThat(result).containsExactly(5, 5);
            assertThat(result).isInstanceOf(ArrayList.class);
        }
    }

    @Nested
    @DisplayName("MergeSort method tests")
    class MergeSortTests {

        @Test
        @DisplayName("Should sort comparable elements")
        void should_sortElements_when_elementsComparable() {
            // Given
            @SuppressWarnings("rawtypes")
            List unsorted = Arrays.asList("zebra", "apple", "banana");

            // When
            @SuppressWarnings("rawtypes")
            List result = RawBoundedTypes.mergeSort(unsorted);

            // Then
            assertThat(result).containsExactly("apple", "banana", "zebra");
        }

        @Test
        @DisplayName("Should handle single element list")
        void should_returnSameList_when_singleElement() {
            // Given
            @SuppressWarnings("rawtypes")
            List singleElement = Arrays.asList("single");

            // When
            @SuppressWarnings("rawtypes")
            List result = RawBoundedTypes.mergeSort(singleElement);

            // Then
            assertThat(result).containsExactly("single");
        }

        @Test
        @DisplayName("Should handle empty list")
        void should_returnEmptyList_when_emptyInput() {
            // Given
            @SuppressWarnings("rawtypes")
            List empty = new ArrayList();

            // When
            @SuppressWarnings("rawtypes")
            List result = RawBoundedTypes.mergeSort(empty);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should throw exception when elements not comparable")
        void should_throwException_when_elementsNotComparable() {
            // Given
            @SuppressWarnings("rawtypes")
            List nonComparable = Arrays.asList(new Object(), new Object());

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.mergeSort(nonComparable))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Items are not comparable");
        }
    }

    @Nested
    @DisplayName("CalculateWeightedAverage method tests")
    class CalculateWeightedAverageTests {

        @Test
        @DisplayName("Should calculate weighted average correctly")
        void should_calculateWeightedAverage_when_validInputProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection numbers = Arrays.asList(1, 2, 3, 4, 5);
            Function<Number, Double> weightFunction = n -> 1.0;

            // When
            double result = RawBoundedTypes.calculateWeightedAverage(numbers, weightFunction);

            // Then
            assertThat(result).isEqualTo(3.0);
        }

        @Test
        @DisplayName("Should throw exception when collection contains non-Number")
        void should_throwException_when_collectionContainsNonNumber() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection nonNumbers = Arrays.asList("1", "2", "3");
            Function<Number, Double> weightFunction = n -> 1.0;

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.calculateWeightedAverage(nonNumbers, weightFunction))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expected Number but got");
        }

        @Test
        @DisplayName("Should throw exception when weight function is not Function")
        void should_throwException_when_weightFunctionNotFunction() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection numbers = Arrays.asList(1, 2, 3);
            String notAFunction = "not a function";

            // When & Then
            assertThatThrownBy(() -> RawBoundedTypes.calculateWeightedAverage(numbers, notAFunction))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Weight function must be a Function");
        }

        @Test
        @DisplayName("Should return zero when total weight is zero")
        void should_returnZero_when_totalWeightIsZero() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection numbers = Arrays.asList(1, 2, 3);
            Function<Number, Double> zeroWeightFunction = n -> 0.0;

            // When
            double result = RawBoundedTypes.calculateWeightedAverage(numbers, zeroWeightFunction);

            // Then
            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("RawFactory tests")
    class RawFactoryTests {

        @Test
        @DisplayName("Should create object with registered constructor")
        void should_createObject_when_constructorRegistered() {
            // Given
            RawBoundedTypes.RawFactory factory = new RawBoundedTypes.RawFactory(StringBuilder.class);
            factory.registerConstructor("withCapacity",
                (Function<Object[], Object>) args -> {
                    if (args.length != 1 || !(args[0] instanceof Integer)) {
                        throw new RuntimeException("Expected single Integer argument");
                    }
                    return new StringBuilder((Integer) args[0]);
                });

            // When
            Object result = factory.create("withCapacity", 100);

            // Then
            assertThat(result).isInstanceOf(StringBuilder.class);
            StringBuilder sb = (StringBuilder) result;
            assertThat(sb.capacity()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should throw exception for unknown constructor")
        void should_throwException_when_unknownConstructor() {
            // Given
            RawBoundedTypes.RawFactory factory = new RawBoundedTypes.RawFactory(String.class);

            // When & Then
            assertThatThrownBy(() -> factory.create("unknown", "arg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown constructor: unknown");
        }

        @Test
        @DisplayName("Should throw exception when constructor is not Function")
        void should_throwException_when_constructorNotFunction() {
            // Given
            RawBoundedTypes.RawFactory factory = new RawBoundedTypes.RawFactory(String.class);
            factory.registerConstructor("test", "not a function");

            // When & Then
            assertThatThrownBy(() -> factory.create("test", "arg"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Constructor is not a Function");
        }

        @Test
        @DisplayName("Should return correct type")
        void should_returnCorrectType_when_getTypeCalled() {
            // Given
            RawBoundedTypes.RawFactory factory = new RawBoundedTypes.RawFactory(String.class);

            // When
            Class<?> type = factory.getType();

            // Then
            assertThat(type).isEqualTo(String.class);
        }
    }

    @Nested
    @DisplayName("StringToNumberProcessor tests")
    class StringToNumberProcessorTests {

        @Test
        @DisplayName("Should process strings to their lengths")
        void should_processStrings_when_validStringsProvided() {
            // Given
            RawBoundedTypes.StringToNumberProcessor processor = new RawBoundedTypes.StringToNumberProcessor();
            @SuppressWarnings("rawtypes")
            Collection input = Arrays.asList("hello", "world", "test");

            // When
            @SuppressWarnings("rawtypes")
            Collection result = processor.process(input);

            // Then
            assertThat(result).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should throw exception when input contains non-string")
        void should_throwException_when_inputContainsNonString() {
            // Given
            RawBoundedTypes.StringToNumberProcessor processor = new RawBoundedTypes.StringToNumberProcessor();
            @SuppressWarnings("rawtypes")
            Collection input = Arrays.asList("hello", 42, "world");

            // When & Then
            assertThatThrownBy(() -> processor.process(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expected String but got");
        }

        @Test
        @DisplayName("Should handle empty collection")
        void should_handleEmptyCollection_when_emptyInputProvided() {
            // Given
            RawBoundedTypes.StringToNumberProcessor processor = new RawBoundedTypes.StringToNumberProcessor();
            @SuppressWarnings("rawtypes")
            Collection input = new ArrayList();

            // When
            @SuppressWarnings("rawtypes")
            Collection result = processor.process(input);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should filter and process correctly")
        void should_filterAndProcess_when_usingProcessAndFilter() {
            // Given
            RawBoundedTypes.StringToNumberProcessor processor = new RawBoundedTypes.StringToNumberProcessor();
            @SuppressWarnings("rawtypes")
            Collection input = Arrays.asList("hello", "hi", "world");

            // When
            @SuppressWarnings("rawtypes")
            Collection result = processor.processAndFilter(input, Integer.class);

            // Then
            assertThat(result).containsExactly(5, 2, 5);
        }
    }

    @Test
    @DisplayName("Should demonstrate all problems without throwing exceptions")
    void should_demonstrateProblems_when_calledWithValidData() {
        // Given & When & Then
        // This test verifies that the demonstration method runs without crashing
        // and properly handles the expected runtime errors
        RawBoundedTypes.demonstrateProblems();

        // The method should complete execution, demonstrating the problems
        // with raw types while handling expected runtime exceptions
        assertThat(true).isTrue(); // Test passes if no unexpected exceptions are thrown
    }
}

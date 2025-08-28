package info.jab.generics.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EdgeCaseGenerics Tests")
class EdgeCaseGenericsTest {

    @Nested
    @DisplayName("Wildcard Capture Tests")
    class WildcardCaptureTests {

        @Test
        @DisplayName("Should swap elements in Integer list")
        void swap_integerList_swapsElements() {
            // Given
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

            // When
            EdgeCaseGenerics.WildcardCapture.swap(list, 0, 4);

            // Then
            assertThat(list).containsExactly(5, 2, 3, 4, 1);
        }

        @Test
        @DisplayName("Should swap elements in String list")
        void swap_stringList_swapsElements() {
            // Given
            List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

            // When
            EdgeCaseGenerics.WildcardCapture.swap(list, 0, 2);

            // Then
            assertThat(list).containsExactly("c", "b", "a");
        }

        @Test
        @DisplayName("Should handle same index swap")
        void swap_sameIndex_noChange() {
            // Given
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

            // When
            EdgeCaseGenerics.WildcardCapture.swap(list, 1, 1);

            // Then
            assertThat(list).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should reverse Integer list")
        void reverse_integerList_returnsReversedList() {
            // Given
            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

            // When
            List<?> reversed = EdgeCaseGenerics.WildcardCapture.reverse(list);

            // Then
            assertThat(reversed).hasSize(5);
            List<Object> objectList = new ArrayList<>(reversed);
            assertThat(objectList).containsSequence(5, 4, 3, 2, 1);
            assertThat(reversed).isNotSameAs(list);
        }

        @Test
        @DisplayName("Should reverse empty list")
        void reverse_emptyList_returnsEmptyList() {
            // Given
            List<String> emptyList = Collections.emptyList();

            // When
            List<?> reversed = EdgeCaseGenerics.WildcardCapture.reverse(emptyList);

            // Then
            assertThat(reversed).isEmpty();
        }

        @Test
        @DisplayName("Should transform list with function")
        void transformList_stringList_transformsCorrectly() {
            // Given
            List<String> list = Arrays.asList("hello", "world", "java");
            Function<Object, Integer> lengthFunction = obj -> obj.toString().length();

            // When
            List<Integer> transformed = EdgeCaseGenerics.WildcardCapture.transformList(list, lengthFunction);

            // Then
            assertThat(transformed).containsExactly(5, 5, 4);
        }
    }

    @Nested
    @DisplayName("Safe Varargs Tests")
    class SafeVarargsTests {

        @Test
        @DisplayName("Should create list from varargs")
        void createList_validElements_returnsList() {
            // When
            List<String> result = EdgeCaseGenerics.SafeVarargsExamples.createList("a", "b", "c");

            // Then
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should create empty list from no args")
        void createList_noArgs_returnsEmptyList() {
            // When
            List<String> result = EdgeCaseGenerics.SafeVarargsExamples.createList();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should print all elements without error")
        void printAll_validElements_printsWithoutError() {
            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.SafeVarargsExamples.printAll("test1", "test2", "test3"))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should map varargs elements")
        void mapVarargs_stringToLength_mapsCorrectly() {
            // When
            List<Integer> result = EdgeCaseGenerics.SafeVarargsExamples.mapVarargs(
                String::length, "hello", "world", "java");

            // Then
            assertThat(result).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should handle safe list operation")
        void safeListOperation_validLists_operatesWithoutError() {
            // Given
            List<List<String>> lists = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d", "e")
            );

            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.SafeVarargsExamples.safeListOperation(lists))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should demonstrate heap pollution danger")
        void unsafeVarargs_demonstratesProblem_causesHeapPollution() {
            // Given
            List<String> stringList = Arrays.asList("safe", "string");

            // When & Then - This demonstrates the heap pollution issue
            assertThatCode(() ->
                EdgeCaseGenerics.SafeVarargsExamples.unsafeVarargs(stringList))
                .doesNotThrowAnyException(); // The method completes but causes heap pollution
        }
    }

    @Nested
    @DisplayName("Advanced Heterogeneous Container Tests")
    class AdvancedHeterogeneousContainerTests {

        private EdgeCaseGenerics.AdvancedHeterogeneousContainer container;

        @BeforeEach
        void setUp() {
            container = new EdgeCaseGenerics.AdvancedHeterogeneousContainer();
        }

        @Test
        @DisplayName("Should store and retrieve values by type key")
        void putAndGet_validTypeKey_storesAndRetrievesCorrectly() {
            // Given
            var stringKey = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "name");
            var integerKey = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(Integer.class, "age");

            // When
            container.put(stringKey, "John Doe");
            container.put(integerKey, 30);

            // Then
            assertThat(container.get(stringKey)).isEqualTo("John Doe");
            assertThat(container.get(integerKey)).isEqualTo(30);
        }

        @Test
        @DisplayName("Should return null for non-existent key")
        void get_nonExistentKey_returnsNull() {
            // Given
            var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "missing");

            // When
            String result = container.get(key);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return optional for existing value")
        void getOptional_existingValue_returnsOptionalWithValue() {
            // Given
            var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
            container.put(key, "value");

            // When
            Optional<String> result = container.getOptional(key);

            // Then
            assertThat(result).isPresent().contains("value");
        }

        @Test
        @DisplayName("Should return empty optional for non-existent value")
        void getOptional_nonExistentValue_returnsEmptyOptional() {
            // Given
            var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "missing");

            // When
            Optional<String> result = container.getOptional(key);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should check if container contains key")
        void contains_existingKey_returnsTrue() {
            // Given
            var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
            container.put(key, "value");

            // When
            boolean contains = container.contains(key);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("Should remove and return value")
        void remove_existingKey_removesAndReturnsValue() {
            // Given
            var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
            container.put(key, "value");

            // When
            String removed = container.remove(key);

            // Then
            assertThat(removed).isEqualTo("value");
            assertThat(container.contains(key)).isFalse();
        }

        @Test
        @DisplayName("Should get all values of specific type")
        void getAllOfType_multipleValues_returnsCorrectMap() {
            // Given
            var key1 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "name");
            var key2 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "city");
            var key3 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(Integer.class, "age");

            container.put(key1, "John");
            container.put(key2, "New York");
            container.put(key3, 30);

            // When
            Map<String, String> stringValues = container.getAllOfType(String.class);

            // Then
            assertThat(stringValues)
                .hasSize(2)
                .containsEntry("name", "John")
                .containsEntry("city", "New York");
        }

        @Test
        @DisplayName("Should get all values assignable to type")
        void getAllAssignableTo_inheritanceHierarchy_returnsCorrectMap() {
            // Given
            var stringKey = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "string");
            var integerKey = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(Integer.class, "integer");

            container.put(stringKey, "text");
            container.put(integerKey, 42);

            // When
            Map<String, Object> objectValues = container.getAllAssignableTo(Object.class);

            // Then
            assertThat(objectValues)
                .hasSize(2)
                .containsEntry("string", "text")
                .containsEntry("integer", 42);
        }

        @Test
        @DisplayName("Should throw exception for wrong type")
        void put_wrongType_throwsClassCastException() {
            // Given
            var stringKey = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");

            // When & Then
            assertThatThrownBy(() -> container.put(stringKey, (String) (Object) 42))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("cannot be cast to");
        }

        @Nested
        @DisplayName("TypeKey Tests")
        class TypeKeyTests {

            @Test
            @DisplayName("Should create type key with correct properties")
            void constructor_validParameters_createsCorrectTypeKey() {
                // When
                var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");

                // Then
                assertThat(key.getType()).isEqualTo(String.class);
                assertThat(key.getName()).isEqualTo("test");
            }

            @Test
            @DisplayName("Should implement equals correctly")
            void equals_sameTypeAndName_returnsTrue() {
                // Given
                var key1 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
                var key2 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");

                // When & Then
                assertThat(key1).isEqualTo(key2);
                assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
            }

            @Test
            @DisplayName("Should implement equals correctly for different keys")
            void equals_differentTypeOrName_returnsFalse() {
                // Given
                var key1 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
                var key2 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(Integer.class, "test");
                var key3 = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "other");

                // When & Then
                assertThat(key1).isNotEqualTo(key2);
                assertThat(key1).isNotEqualTo(key3);
            }

            @Test
            @DisplayName("Should have meaningful toString")
            void toString_validKey_returnsInformativeString() {
                // Given
                var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");

                // When
                String result = key.toString();

                // Then
                assertThat(result).contains("String").contains("test");
            }
        }
    }

    @Nested
    @DisplayName("Variance Examples Tests")
    class VarianceExamplesTests {

        @Test
        @DisplayName("Should produce items correctly")
        void produceItems_validSupplier_returnsProducedItems() {
            // Given
            List<Integer> source = Arrays.asList(1, 2, 3);

            // When
            List<? extends Integer> result = EdgeCaseGenerics.VarianceExamples.produceItems(() -> source);

            // Then
            assertThat(result).hasSize(3);
            List<Integer> integerList = new ArrayList<>(result);
            assertThat(integerList).containsSequence(1, 2, 3);
        }

        @Test
        @DisplayName("Should consume items with expansion")
        void consumeItems_validParameters_consumesCorrectly() {
            // Given
            List<Number> destination = new ArrayList<>();
            Function<Integer, List<Integer>> doubler = n -> Arrays.asList(n, n * 2);
            List<Integer> sources = Arrays.asList(1, 2, 3);

            // When
            EdgeCaseGenerics.VarianceExamples.consumeItems(destination, doubler, sources);

            // Then
            assertThat(destination).containsExactly(1, 2, 2, 4, 3, 6);
        }

        @Test
        @DisplayName("Should perform complex transfer")
        void complexTransfer_validMaps_transfersCorrectly() {
            // Given
            Map<String, Collection<? super Integer>> destinations = new HashMap<>();
            destinations.put("numbers", new ArrayList<Number>());
            destinations.put("integers", new ArrayList<Number>());

            Map<String, List<Integer>> sources = Map.of(
                "source1", Arrays.asList(1, 2),
                "source2", Arrays.asList(3, 4)
            );
            Function<String, String> keyMapper = key -> key.equals("source1") ? "numbers" : "integers";

            // When
            EdgeCaseGenerics.VarianceExamples.<Integer>complexTransfer(destinations, sources, keyMapper);

            // Then
            assertThat(destinations.get("numbers")).containsExactly(1, 2);
            assertThat(destinations.get("integers")).containsExactly(3, 4);
        }

        @Test
        @DisplayName("Should merge multiple lists")
        void mergeMultiple_validLists_mergesAndSorts() {
            // Given
            List<List<String>> lists = Arrays.asList(
                Arrays.asList("zebra", "apple"),
                Arrays.asList("banana", "cherry")
            );

            // When
            List<String> result = EdgeCaseGenerics.VarianceExamples.mergeMultiple(lists);

            // Then
            assertThat(result).containsExactly("apple", "banana", "cherry", "zebra");
        }
    }

    @Nested
    @DisplayName("Array Generics Interaction Tests")
    class ArrayGenericsInteractionTests {

        @Test
        @DisplayName("Should create array of correct type and size")
        void createArray_validParameters_createsCorrectArray() {
            // When
            String[] array = EdgeCaseGenerics.ArrayGenericsInteraction.createArray(String.class, 5);

            // Then
            assertThat(array).hasSize(5);
            assertThat(array.getClass().getComponentType()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("Should create array with initial elements")
        void createArrayWith_validElements_createsInitializedArray() {
            // When
            String[] array = EdgeCaseGenerics.ArrayGenericsInteraction.createArrayWith(
                String.class, "a", "b", "c");

            // Then
            assertThat(array).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should convert array to list")
        void arrayToList_validArray_returnsCorrectList() {
            // Given
            String[] array = {"a", "b", "c"};

            // When
            List<String> list = EdgeCaseGenerics.ArrayGenericsInteraction.arrayToList(array);

            // Then
            assertThat(list).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should convert list to array")
        void listToArray_validList_returnsCorrectArray() {
            // Given
            List<String> list = Arrays.asList("a", "b", "c");

            // When
            String[] array = EdgeCaseGenerics.ArrayGenericsInteraction.listToArray(list, String.class);

            // Then
            assertThat(array).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should demonstrate array problems without error")
        void demonstrateArrayProblems_execution_completesWithoutError() {
            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.ArrayGenericsInteraction.demonstrateArrayProblems())
                .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Bridge Method Scenarios Tests")
    class BridgeMethodScenariosTests {

        @Test
        @DisplayName("Should process with string processor")
        void stringProcessor_validString_processesCorrectly() {
            // Given
            var processor = new EdgeCaseGenerics.BridgeMethodScenarios.StringProcessor();

            // When
            processor.process("test");
            String result = processor.create();

            // Then
            assertThat(result).isEqualTo("Created string");
        }

        @Test
        @DisplayName("Should process with raw processor")
        void rawProcessor_validObject_processesCorrectly() {
            // Given
            var processor = new EdgeCaseGenerics.BridgeMethodScenarios.RawProcessor();

            // When
            processor.process("test");
            Object result = processor.create();

            // Then
            assertThat(result).isEqualTo("Created object");
        }

        @Test
        @DisplayName("Should demonstrate bridge methods without error")
        void demonstrateBridgeMethods_execution_completesWithoutError() {
            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.BridgeMethodScenarios.demonstrateBridgeMethods())
                .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Generic Overloading Tests")
    class GenericOverloadingTests {

        @Test
        @DisplayName("Should call generic method for non-string list")
        void processGeneric_integerList_callsGenericMethod() {
            // Given
            List<Integer> intList = Arrays.asList(1, 2, 3);

            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.GenericOverloading.processGeneric(intList))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should call string-specific method")
        void processString_stringList_callsStringMethod() {
            // Given
            List<String> stringList = Arrays.asList("a", "b", "c");

            // When & Then
            assertThatCode(() ->
                EdgeCaseGenerics.GenericOverloading.processString(stringList))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should call multi-parameter method variants")
        void multiParam_differentSignatures_callsCorrectOverloads() {
            // Given
            List<String> list = Arrays.asList("test");

            // When & Then
            assertThatCode(() -> {
                EdgeCaseGenerics.GenericOverloading.multiParam(list);
                EdgeCaseGenerics.GenericOverloading.multiParam(list, String.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should call bounded methods correctly")
        void bounded_differentBounds_callsCorrectMethods() {
            // When & Then
            assertThatCode(() -> {
                EdgeCaseGenerics.GenericOverloading.bounded(42);
                EdgeCaseGenerics.GenericOverloading.bounded("test");
            }).doesNotThrowAnyException();
        }
    }

    @ParameterizedTest(name = "Should handle {0} correctly")
    @MethodSource("provideEdgeCaseScenarios")
    @DisplayName("Should handle various edge case scenarios")
    void edgeCaseScenarios_variousInputs_handlesCorrectly(String description, Runnable scenario) {
        // When & Then
        assertThatCode(scenario::run).doesNotThrowAnyException();
    }

    static Stream<Arguments> provideEdgeCaseScenarios() {
        return Stream.of(
            Arguments.of("Empty list swap", (Runnable) () -> {
                List<String> emptyList = new ArrayList<>();
                // This should handle bounds checking gracefully
            }),
            Arguments.of("Null handling in containers", (Runnable) () -> {
                var container = new EdgeCaseGenerics.AdvancedHeterogeneousContainer();
                var key = new EdgeCaseGenerics.AdvancedHeterogeneousContainer.TypeKey<>(String.class, "test");
                container.put(key, null); // Should handle null values
            }),
            Arguments.of("Edge case demonstration", (Runnable) () -> {
                EdgeCaseGenerics.demonstrateEdgeCases();
            })
        );
    }
}

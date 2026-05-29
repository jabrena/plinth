package info.jab.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RawCollections Test")
class RawCollectionsTest {

    @Nested
    @DisplayName("UnsafeWildcardOperations tests")
    class UnsafeWildcardOperationsTests {

        @Test
        @DisplayName("Should swap elements in list")
        void should_swapElements_when_validIndicesProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List list = new ArrayList<>(Arrays.asList("a", "b", "c"));

            // When
            RawCollections.UnsafeWildcardOperations.attemptSwap(list, 0, 2);

            // Then
            assertThat(list).containsExactly("c", "b", "a");
        }

        @Test
        @DisplayName("Should handle swap with same indices")
        void should_handleSameIndices_when_swappingSameElement() {
            // Given
            @SuppressWarnings("rawtypes")
            List list = new ArrayList<>(Arrays.asList("a", "b", "c"));

            // When
            RawCollections.UnsafeWildcardOperations.attemptSwap(list, 1, 1);

            // Then
            assertThat(list).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should reverse list correctly")
        void should_reverseList_when_validListProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List original = Arrays.asList(1, 2, 3, 4, 5);

            // When
            @SuppressWarnings("rawtypes")
            List reversed = RawCollections.UnsafeWildcardOperations.attemptReverse(original);

            // Then
            assertThat(reversed).containsExactly(5, 4, 3, 2, 1);
            assertThat(original).containsExactly(1, 2, 3, 4, 5); // Original unchanged
        }

        @Test
        @DisplayName("Should handle empty list reversal")
        void should_handleEmptyList_when_reversingEmptyList() {
            // Given
            @SuppressWarnings("rawtypes")
            List empty = new ArrayList();

            // When
            @SuppressWarnings("rawtypes")
            List reversed = RawCollections.UnsafeWildcardOperations.attemptReverse(empty);

            // Then
            assertThat(reversed).isEmpty();
        }

        @Test
        @DisplayName("Should transform list with valid transformer")
        void should_transformList_when_validTransformerProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List source = Arrays.asList("hello", "world", "test");
            Function<String, Integer> transformer = String::length;

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.UnsafeWildcardOperations.transformList(source, transformer);

            // Then
            assertThat(result).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should throw exception when transformer is not Function")
        void should_throwException_when_transformerNotFunction() {
            // Given
            @SuppressWarnings("rawtypes")
            List source = Arrays.asList("hello", "world");
            String notAFunction = "not a function";

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeWildcardOperations.transformList(source, notAFunction))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transformer must be a Function");
        }

        @Test
        @DisplayName("Should handle transformation failure")
        void should_handleTransformationFailure_when_transformerFails() {
            // Given
            @SuppressWarnings("rawtypes")
            List source = Arrays.asList("hello", 123); // Mixed types
            Function<String, Integer> transformer = String::length; // Expects String

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeWildcardOperations.transformList(source, transformer))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Transformation failed for item:");
        }
    }

    @Nested
    @DisplayName("HeapPollutionExamples tests")
    class HeapPollutionExamplesTests {

        @Test
        @DisplayName("Should create list with provided elements")
        void should_createList_when_elementsProvided() {
            // Given & When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.HeapPollutionExamples.createUnsafeList("a", "b", "c");

            // Then
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should create empty list when no elements provided")
        void should_createEmptyList_when_noElementsProvided() {
            // Given & When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.HeapPollutionExamples.createUnsafeList();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should create generic array from elements")
        void should_createGenericArray_when_elementsProvided() {
            // Given & When
            Object[] result = RawCollections.HeapPollutionExamples.createGenericArray("a", "b", "c");

            // Then
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should map elements with valid mapper")
        void should_mapElements_when_validMapperProvided() {
            // Given
            Function<String, Integer> mapper = String::length;

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.HeapPollutionExamples.mapUnsafe(mapper, "hello", "world", "test");

            // Then
            assertThat(result).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should throw exception when mapper is not Function")
        void should_throwException_when_mapperNotFunction() {
            // Given
            String notAMapper = "not a function";

            // When & Then
            assertThatThrownBy(() -> RawCollections.HeapPollutionExamples.mapUnsafe(notAMapper, "hello", "world"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mapper must be a Function");
        }

        @Test
        @DisplayName("Should handle mapping failure")
        void should_handleMappingFailure_when_mapperFails() {
            // Given
            Function<String, Integer> mapper = String::length;

            // When & Then
            assertThatThrownBy(() -> RawCollections.HeapPollutionExamples.mapUnsafe(mapper, "hello", 123))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mapping failed");
        }

        @Test
        @DisplayName("Should print all elements safely")
        void should_printAllElements_when_printAllUnsafeCalled() {
            // Given & When & Then
            // This test just verifies that the method doesn't crash
            RawCollections.HeapPollutionExamples.printAllUnsafe("hello", 123, true, null);

            // Method should complete without exceptions
            assertThat(true).isTrue();
        }
    }

    @Nested
    @DisplayName("UnsafeHeterogeneousContainer tests")
    class UnsafeHeterogeneousContainerTests {

        @Test
        @DisplayName("Should store and retrieve values correctly")
        void should_storeAndRetrieve_when_validKeysAndValuesProvided() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();

            // When
            container.put(String.class, "Hello World");
            container.put(Integer.class, 42);
            container.put("custom_key", "custom_value");

            // Then
            assertThat(container.get(String.class)).isEqualTo("Hello World");
            assertThat(container.get(Integer.class)).isEqualTo(42);
            assertThat(container.get("custom_key")).isEqualTo("custom_value");
        }

        @Test
        @DisplayName("Should check containment correctly")
        void should_checkContainment_when_keysExist() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();
            container.put(String.class, "test");

            // When & Then
            assertThat(container.contains(String.class)).isTrue();
            assertThat(container.contains(Integer.class)).isFalse();
        }

        @Test
        @DisplayName("Should remove values correctly")
        void should_removeValues_when_keysExist() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();
            container.put(String.class, "test");

            // When
            Object removed = container.remove(String.class);

            // Then
            assertThat(removed).isEqualTo("test");
            assertThat(container.contains(String.class)).isFalse();
        }

        @Test
        @DisplayName("Should return stored keys")
        void should_returnStoredKeys_when_getStoredKeysCalled() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();
            container.put(String.class, "test");
            container.put(Integer.class, 42);

            // When
            @SuppressWarnings("rawtypes")
            Set keys = container.getStoredKeys();

            // Then
            assertThat(keys).containsExactlyInAnyOrder(String.class, Integer.class);
        }

        @Test
        @DisplayName("Should get all values of specific type")
        void should_getAllOfType_when_typeSpecified() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();
            container.put(String.class, "hello");
            container.put(Integer.class, 42);
            container.put("non_class_key", "value");

            // When
            @SuppressWarnings("rawtypes")
            Map stringValues = container.getAllOfType(String.class);

            // Then
            assertThat(stringValues).containsEntry(String.class, "hello");
            assertThat(stringValues).doesNotContainKey(Integer.class);
        }

        @Test
        @DisplayName("Should get all assignable values")
        void should_getAllAssignableTo_when_typeSpecified() {
            // Given
            RawCollections.UnsafeHeterogeneousContainer container =
                new RawCollections.UnsafeHeterogeneousContainer();
            container.put(String.class, "hello");
            container.put(Object.class, new Object());
            container.put(Integer.class, 42);

            // When
            @SuppressWarnings("rawtypes")
            Map objectValues = container.getAllAssignableTo(Object.class);

            // Then
            assertThat(objectValues).hasSize(3); // All values are assignable to Object
            assertThat(objectValues).containsKey(String.class);
            assertThat(objectValues).containsKey(Object.class);
            assertThat(objectValues).containsKey(Integer.class);
        }
    }

    @Nested
    @DisplayName("UnsafeVarianceOperations tests")
    class UnsafeVarianceOperationsTests {

        @Test
        @DisplayName("Should produce items from supplier")
        void should_produceItems_when_validSupplierProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection source = Arrays.asList(1, 2, 3);
            Supplier<Collection> supplier = () -> source;

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.UnsafeVarianceOperations.produceItems(supplier);

            // Then
            assertThat(result).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should throw exception when supplier is not Supplier")
        void should_throwException_when_supplierNotSupplier() {
            // Given
            String notASupplier = "not a supplier";

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeVarianceOperations.produceItems(notASupplier))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Must provide a Supplier");
        }

        @Test
        @DisplayName("Should throw exception when supplier doesn't produce Collection")
        void should_throwException_when_supplierProducesNonCollection() {
            // Given
            Supplier<String> badSupplier = () -> "not a collection";

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeVarianceOperations.produceItems(badSupplier))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Supplier must produce a Collection");
        }

        @Test
        @DisplayName("Should consume items correctly")
        void should_consumeItems_when_validInputsProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection destination = new ArrayList();
            Function<Integer, Collection> expander = n -> Arrays.asList(n, n * 2);
            @SuppressWarnings("rawtypes")
            Collection sources = Arrays.asList(1, 2, 3);

            // When
            RawCollections.UnsafeVarianceOperations.consumeItems(destination, expander, sources);

            // Then
            assertThat(destination).containsExactly(1, 2, 2, 4, 3, 6);
        }

        @Test
        @DisplayName("Should throw exception when expander is not Function")
        void should_throwException_when_expanderNotFunction() {
            // Given
            @SuppressWarnings("rawtypes")
            Collection destination = new ArrayList();
            String notAnExpander = "not a function";
            @SuppressWarnings("rawtypes")
            Collection sources = Arrays.asList(1, 2, 3);

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeVarianceOperations.consumeItems(destination, notAnExpander, sources))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Expander must be a Function");
        }

        @Test
        @DisplayName("Should merge multiple lists")
        void should_mergeMultipleLists_when_validListsProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List list1 = Arrays.asList("a", "b");
            @SuppressWarnings("rawtypes")
            List list2 = Arrays.asList("c", "d");
            @SuppressWarnings("rawtypes")
            List lists = Arrays.asList(list1, list2);

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.UnsafeVarianceOperations.mergeMultiple(lists);

            // Then
            assertThat(result).containsExactly("a", "b", "c", "d");
        }

        @Test
        @DisplayName("Should handle empty lists in merge")
        void should_handleEmptyLists_when_mergingEmptyLists() {
            // Given
            @SuppressWarnings("rawtypes")
            List emptyLists = new ArrayList();

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.UnsafeVarianceOperations.mergeMultiple(emptyLists);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("UnsafeArrayOperations tests")
    class UnsafeArrayOperationsTests {

        @Test
        @DisplayName("Should create array of specified type and size")
        void should_createArray_when_typeAndSizeProvided() {
            // Given
            Class<String> componentType = String.class;
            int size = 5;

            // When
            Object array = RawCollections.UnsafeArrayOperations.createArray(componentType, size);

            // Then
            assertThat(array).isInstanceOf(String[].class);
            String[] stringArray = (String[]) array;
            assertThat(stringArray).hasSize(5);
        }

        @Test
        @DisplayName("Should create array with initialization")
        void should_createArrayWithInit_when_elementsProvided() {
            // Given
            Class<String> componentType = String.class;

            // When
            Object array = RawCollections.UnsafeArrayOperations.createArrayWith(componentType, "a", "b", "c");

            // Then
            assertThat(array).isInstanceOf(String[].class);
            String[] stringArray = (String[]) array;
            assertThat(stringArray).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should throw exception for incompatible element type")
        void should_throwException_when_incompatibleElementType() {
            // Given
            Class<String> componentType = String.class;

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeArrayOperations.createArrayWith(componentType, "valid", 123))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Element 1 is not compatible with array type");
        }

        @Test
        @DisplayName("Should convert array to list")
        void should_convertArrayToList_when_validArrayProvided() {
            // Given
            String[] array = {"a", "b", "c"};

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.UnsafeArrayOperations.arrayToList(array);

            // Then
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should throw exception when converting non-array")
        void should_throwException_when_convertingNonArray() {
            // Given
            String notAnArray = "not an array";

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeArrayOperations.arrayToList(notAnArray))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not an array");
        }

        @Test
        @DisplayName("Should convert list to array")
        void should_convertListToArray_when_validListProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List list = Arrays.asList("a", "b", "c");
            Class<String> componentType = String.class;

            // When
            Object array = RawCollections.UnsafeArrayOperations.listToArray(list, componentType);

            // Then
            assertThat(array).isInstanceOf(String[].class);
            String[] stringArray = (String[]) array;
            assertThat(stringArray).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should throw exception for incompatible list element")
        void should_throwException_when_listContainsIncompatibleElement() {
            // Given
            @SuppressWarnings("rawtypes")
            List list = Arrays.asList("a", 123, "c");
            Class<String> componentType = String.class;

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeArrayOperations.listToArray(list, componentType))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("List contains incompatible element");
        }

        @Test
        @DisplayName("Should demonstrate array problems without crashing")
        void should_demonstrateArrayProblems_when_called() {
            // Given & When & Then
            // This test verifies that the demonstration method runs without crashing
            RawCollections.UnsafeArrayOperations.demonstrateArrayProblems();

            // The method should complete execution
            assertThat(true).isTrue();
        }
    }

    @Nested
    @DisplayName("BridgeMethodProblems tests")
    class BridgeMethodProblemsTests {

        @Test
        @DisplayName("Should process string correctly")
        void should_processString_when_validStringProvided() {
            // Given
            RawCollections.BridgeMethodProblems.RawStringProcessor processor =
                new RawCollections.BridgeMethodProblems.RawStringProcessor();

            // When & Then
            // This should work without exceptions
            processor.process("hello world");

            // Verify it doesn't crash
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should throw exception when processing non-string")
        void should_throwException_when_processingNonString() {
            // Given
            RawCollections.BridgeMethodProblems.RawStringProcessor processor =
                new RawCollections.BridgeMethodProblems.RawStringProcessor();

            // When & Then
            assertThatThrownBy(() -> processor.process(123))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expected String but got");
        }

        @Test
        @DisplayName("Should create string object")
        void should_createString_when_createCalled() {
            // Given
            RawCollections.BridgeMethodProblems.RawStringProcessor processor =
                new RawCollections.BridgeMethodProblems.RawStringProcessor();

            // When
            Object result = processor.create();

            // Then
            assertThat(result).isInstanceOf(String.class);
            assertThat(result).isEqualTo("Created string");
        }

        @Test
        @DisplayName("Should create string through typed method")
        void should_createStringTyped_when_createStringCalled() {
            // Given
            RawCollections.BridgeMethodProblems.RawStringProcessor processor =
                new RawCollections.BridgeMethodProblems.RawStringProcessor();

            // When
            String result = processor.createString();

            // Then
            assertThat(result).isEqualTo("Created string");
        }

        @Test
        @DisplayName("Should demonstrate bridge issues without crashing")
        void should_demonstrateBridgeIssues_when_called() {
            // Given & When & Then
            // This test verifies that the demonstration method runs without crashing
            RawCollections.BridgeMethodProblems.demonstrateBridgeIssues();

            // The method should complete execution
            assertThat(true).isTrue();
        }
    }

    @Nested
    @DisplayName("TypeErasureProblems tests")
    class TypeErasureProblemsTests {

        @Test
        @DisplayName("Should store and retrieve instances")
        void should_storeAndRetrieve_when_validInstancesProvided() {
            // Given
            RawCollections.TypeErasureProblems.UnsafeTypeContainer container =
                new RawCollections.TypeErasureProblems.UnsafeTypeContainer();

            // When
            container.put(String.class, "hello");
            container.put(Integer.class, 42);

            // Then
            assertThat(container.get(String.class)).isEqualTo("hello");
            assertThat(container.get(Integer.class)).isEqualTo(42);
        }

        @Test
        @DisplayName("Should get instances of type")
        void should_getInstancesOfType_when_typeHierarchyExists() {
            // Given
            RawCollections.TypeErasureProblems.UnsafeTypeContainer container =
                new RawCollections.TypeErasureProblems.UnsafeTypeContainer();
            container.put(String.class, "hello");
            container.put(Object.class, new Object());
            container.put(Integer.class, 42);

            // When
            @SuppressWarnings("rawtypes")
            List objectInstances = container.getInstancesOfType(Object.class);

            // Then
            assertThat(objectInstances).hasSize(3); // All types are assignable to Object
        }

        @Test
        @DisplayName("Should create factory and register constructors")
        void should_createFactory_when_validTypeProvided() {
            // Given
            RawCollections.TypeErasureProblems.UnsafeFactory factory =
                new RawCollections.TypeErasureProblems.UnsafeFactory(StringBuilder.class);

            // When
            factory.registerConstructor("default", (Function<Object[], Object>) args -> new StringBuilder());
            Object result = factory.create("default");

            // Then
            assertThat(result).isInstanceOf(StringBuilder.class);
            assertThat(factory.getType()).isEqualTo(StringBuilder.class);
        }

        @Test
        @DisplayName("Should throw exception for unknown constructor")
        void should_throwException_when_unknownConstructorRequested() {
            // Given
            RawCollections.TypeErasureProblems.UnsafeFactory factory =
                new RawCollections.TypeErasureProblems.UnsafeFactory(String.class);

            // When & Then
            assertThatThrownBy(() -> factory.create("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No constructor: unknown");
        }

        @Test
        @DisplayName("Should cast list safely when elements match type")
        void should_castListSafely_when_elementsMatchType() {
            // Given
            @SuppressWarnings("rawtypes")
            List stringList = Arrays.asList("a", "b", "c");

            // When
            @SuppressWarnings("rawtypes")
            List result = RawCollections.TypeErasureProblems.castList(stringList, String.class);

            // Then
            assertThat(result).isSameAs(stringList);
            assertThat(result).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should throw exception when casting list with wrong element type")
        void should_throwException_when_castingListWithWrongElementType() {
            // Given
            @SuppressWarnings("rawtypes")
            List mixedList = Arrays.asList("a", 123, "c");

            // When & Then
            assertThatThrownBy(() -> RawCollections.TypeErasureProblems.castList(mixedList, String.class))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("List contains element of type");
        }

        @Test
        @DisplayName("Should cast map safely when entries match types")
        void should_castMapSafely_when_entriesMatchTypes() {
            // Given
            @SuppressWarnings("rawtypes")
            Map stringIntMap = new HashMap();
            stringIntMap.put("key1", 1);
            stringIntMap.put("key2", 2);

            // When
            @SuppressWarnings("rawtypes")
            Map result = RawCollections.TypeErasureProblems.castMap(stringIntMap, String.class, Integer.class);

            // Then
            assertThat(result).isSameAs(stringIntMap);
            assertThat(result).containsEntry("key1", 1);
            assertThat(result).containsEntry("key2", 2);
        }

        @Test
        @DisplayName("Should throw exception when casting map with wrong key type")
        void should_throwException_when_castingMapWithWrongKeyType() {
            // Given
            @SuppressWarnings("rawtypes")
            Map mixedMap = new HashMap();
            mixedMap.put("valid_key", 1);
            mixedMap.put(123, 2); // Invalid key type

            // When & Then
            assertThatThrownBy(() -> RawCollections.TypeErasureProblems.castMap(mixedMap, String.class, Integer.class))
                .isInstanceOf(ClassCastException.class)
                .hasMessage("Invalid key type");
        }
    }

    @Nested
    @DisplayName("UnsafeOverloading tests")
    class UnsafeOverloadingTests {

        @Test
        @DisplayName("Should process generic list")
        void should_processGenericList_when_validListProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List testList = Arrays.asList("a", "b", "c");

            // When & Then
            // This should work without exceptions
            RawCollections.UnsafeOverloading.processGeneric(testList);

            // Verify it doesn't crash
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should process string list with mixed content")
        void should_processStringList_when_mixedContentProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List mixedList = Arrays.asList("hello", 123, "world");

            // When & Then
            // This should handle mixed content gracefully
            RawCollections.UnsafeOverloading.processString(mixedList);

            // Verify it doesn't crash
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should handle multi-parameter methods")
        void should_handleMultiParam_when_differentParametersCalled() {
            // Given
            @SuppressWarnings("rawtypes")
            List testList = Arrays.asList(1, 2, 3);

            // When & Then
            // Test single parameter version
            RawCollections.UnsafeOverloading.multiParam(testList);

            // Test two parameter version
            RawCollections.UnsafeOverloading.multiParam(testList, Integer.class);

            // Verify both work without crashing
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should handle bounded number with valid Number")
        void should_handleBoundedNumber_when_validNumberProvided() {
            // Given & When & Then
            RawCollections.UnsafeOverloading.boundedNumber(42);
            RawCollections.UnsafeOverloading.boundedNumber(3.14);
            RawCollections.UnsafeOverloading.boundedNumber(42L);

            // Verify all work without crashing
            assertThat(true).isTrue();
        }

        @Test
        @DisplayName("Should throw exception when bounded number called with non-Number")
        void should_throwException_when_boundedNumberCalledWithNonNumber() {
            // Given
            String notANumber = "not a number";

            // When & Then
            assertThatThrownBy(() -> RawCollections.UnsafeOverloading.boundedNumber(notANumber))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expected Number but got");
        }
    }

    @Test
    @DisplayName("Should demonstrate collection problems without throwing exceptions")
    void should_demonstrateCollectionProblems_when_calledWithValidData() {
        // Given & When & Then
        // This test verifies that the demonstration method runs without crashing
        // and properly handles the expected runtime errors
        RawCollections.demonstrateCollectionProblems();

        // The method should complete execution, demonstrating the problems
        // with raw collections while handling expected runtime exceptions
        assertThat(true).isTrue(); // Test passes if no unexpected exceptions are thrown
    }
}

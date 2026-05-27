package info.jab.generics.examples;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

@DisplayName("TypeErasureWorkarounds Tests")
class TypeErasureWorkaroundsTest {

    @Nested
    @DisplayName("TypeToken Tests")
    class TypeTokenTests {

        @Test
        @DisplayName("Should create type token for List<String>")
        void typeToken_listOfString_preservesTypeInformation() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            // When
            Type type = token.getType();
            Class<List<String>> rawType = token.getRawType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type.getTypeName()).contains("List");
            assertThat(type.getTypeName()).contains("String");
            assertThat(rawType).isEqualTo(List.class);
        }

        @Test
        @DisplayName("Should create type token for Map<String, Integer>")
        void typeToken_mapOfStringToInteger_preservesTypeInformation() {
            // Given
            TypeErasureWorkarounds.TypeToken<Map<String, Integer>> token =
                new TypeErasureWorkarounds.TypeToken<Map<String, Integer>>() {};

            // When
            Type type = token.getType();
            Class<Map<String, Integer>> rawType = token.getRawType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type.getTypeName()).contains("Map");
            assertThat(type.getTypeName()).contains("String");
            assertThat(type.getTypeName()).contains("Integer");
            assertThat(rawType).isEqualTo(Map.class);
        }

        @Test
        @DisplayName("Should implement equals correctly")
        void equals_sameTypes_returnsTrue() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token1 =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};
            TypeErasureWorkarounds.TypeToken<List<String>> token2 =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            // When & Then
            assertThat(token1).isEqualTo(token2);
            assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
        }

        @Test
        @DisplayName("Should implement equals correctly for different types")
        void equals_differentTypes_returnsFalse() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> listToken =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};
            TypeErasureWorkarounds.TypeToken<Set<String>> setToken =
                new TypeErasureWorkarounds.TypeToken<Set<String>>() {};

            // When & Then
            assertThat(listToken).isNotEqualTo(setToken);
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void toString_validToken_returnsInformativeString() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            // When
            String result = token.toString();

            // Then
            assertThat(result).contains("TypeToken");
            assertThat(result).contains("List");
        }

        @Test
        @DisplayName("Should throw exception for non-parameterized token")
        void constructor_nonParameterized_throwsException() {
            // When & Then
            assertThatThrownBy(() -> {
                @SuppressWarnings("rawtypes")
                TypeErasureWorkarounds.TypeToken rawToken = new TypeErasureWorkarounds.TypeToken() {};
            }).isInstanceOf(IllegalArgumentException.class)
              .hasMessageContaining("TypeToken must be parameterized");
        }
    }

    @Nested
    @DisplayName("TypeSafe Container Tests")
    class TypeSafeContainerTests {

        private TypeErasureWorkarounds.TypeSafeContainer container;

        @BeforeEach
        void setUp() {
            container = new TypeErasureWorkarounds.TypeSafeContainer();
        }

        @Test
        @DisplayName("Should store and retrieve values by class type")
        void putAndGet_validTypes_storesAndRetrievesCorrectly() {
            // Given
            String stringValue = "Hello World";
            Integer integerValue = 42;

            // When
            container.put(String.class, stringValue);
            container.put(Integer.class, integerValue);

            String retrievedString = container.get(String.class);
            Integer retrievedInteger = container.get(Integer.class);

            // Then
            assertThat(retrievedString).isEqualTo(stringValue);
            assertThat(retrievedInteger).isEqualTo(integerValue);
        }

        @Test
        @DisplayName("Should return null for non-existent type")
        void get_nonExistentType_returnsNull() {
            // When
            Double result = container.get(Double.class);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should check if container contains type")
        void contains_existingType_returnsTrue() {
            // Given
            container.put(String.class, "test");

            // When
            boolean contains = container.contains(String.class);

            // Then
            assertThat(contains).isTrue();
        }

        @Test
        @DisplayName("Should remove and return value")
        void remove_existingType_removesAndReturnsValue() {
            // Given
            String value = "test";
            container.put(String.class, value);

            // When
            String removed = container.remove(String.class);

            // Then
            assertThat(removed).isEqualTo(value);
            assertThat(container.contains(String.class)).isFalse();
        }

        @Test
        @DisplayName("Should get stored types")
        void getStoredTypes_multipleTypes_returnsAllTypes() {
            // Given
            container.put(String.class, "test");
            container.put(Integer.class, 42);

            // When
            Set<Class<?>> storedTypes = container.getStoredTypes();

            // Then
            assertThat(storedTypes).containsExactlyInAnyOrder(String.class, Integer.class);
        }

        @Test
        @DisplayName("Should get instances of type including inheritance")
        void getInstancesOfType_inheritance_returnsCorrectInstances() {
            // Given
            container.put(String.class, "test");
            container.put(StringBuilder.class, new StringBuilder("builder"));

            // When
            List<CharSequence> charSequences = container.getInstancesOfType(CharSequence.class);

            // Then
            assertThat(charSequences).hasSize(2);
            assertThat(charSequences)
                .extracting(CharSequence::toString)
                .containsExactlyInAnyOrder("test", "builder");
        }

        @Test
        @DisplayName("Should perform type-safe casting during put")
        void put_wrongType_throwsClassCastException() {
            // When & Then
            assertThatThrownBy(() -> {
                Object wrongValue = 42;
                container.put(String.class, (String) wrongValue);
            }).isInstanceOf(ClassCastException.class);
        }
    }

    @Nested
    @DisplayName("Generic Array Factory Tests")
    class GenericArrayFactoryTests {

        @Test
        @DisplayName("Should create array of correct type and size")
        void createArray_validParameters_createsCorrectArray() {
            // When
            String[] array = TypeErasureWorkarounds.GenericArrayFactory.createArray(String.class, 5);

            // Then
            assertThat(array).hasSize(5);
            assertThat(array.getClass().getComponentType()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("Should create array of size zero")
        void createArray_zeroSize_createsEmptyArray() {
            // When
            Integer[] array = TypeErasureWorkarounds.GenericArrayFactory.createArray(Integer.class, 0);

            // Then
            assertThat(array).isEmpty();
        }

        @Test
        @DisplayName("Should create generic list")
        void createList_validType_createsCorrectList() {
            // When
            List<String> list = TypeErasureWorkarounds.GenericArrayFactory.createList(String.class);

            // Then
            assertThat(list).isNotNull().isEmpty();
            assertThat(list).isInstanceOf(ArrayList.class);
        }

        @Test
        @DisplayName("Should create generic map")
        void createMap_validTypes_createsCorrectMap() {
            // When
            Map<String, Integer> map = TypeErasureWorkarounds.GenericArrayFactory.createMap(
                String.class, Integer.class);

            // Then
            assertThat(map).isNotNull().isEmpty();
            assertThat(map).isInstanceOf(HashMap.class);
        }
    }

    @Nested
    @DisplayName("Type Preserving Factory Tests")
    class TypePreservingFactoryTests {

        @Test
        @DisplayName("Should create factory with correct type")
        void constructor_validType_storesType() {
            // When
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            // Then
            assertThat(factory.getType()).isEqualTo(StringBuilder.class);
        }

        @Test
        @DisplayName("Should create default instance if constructor available")
        void createDefault_defaultConstructorAvailable_createsInstance() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            // When
            StringBuilder instance = factory.createDefault();

            // Then
            assertThat(instance).isNotNull().isInstanceOf(StringBuilder.class);
        }

        @Test
        @DisplayName("Should register and use single parameter constructor")
        void registerConstructor_singleParameter_createsInstanceCorrectly() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            factory.registerConstructor("withString", String.class, StringBuilder::new);

            // When
            StringBuilder instance = factory.create("withString", "Hello");

            // Then
            assertThat(instance.toString()).isEqualTo("Hello");
        }

        @Test
        @DisplayName("Should register and use two parameter constructor")
        void registerConstructor_twoParameters_createsInstanceCorrectly() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            factory.registerConstructor("withCapacityAndString",
                Integer.class, String.class,
                (capacity, initial) -> new StringBuilder(capacity).append(initial));

            // When
            StringBuilder instance = factory.create("withCapacityAndString", 100, "Hello");

            // Then
            assertThat(instance.toString()).isEqualTo("Hello");
            assertThat(instance.capacity()).isGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should check if constructor can be created")
        void canCreate_registeredConstructor_returnsTrue() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            factory.registerConstructor("test", String.class, StringBuilder::new);

            // When
            boolean canCreate = factory.canCreate("test");

            // Then
            assertThat(canCreate).isTrue();
        }

        @Test
        @DisplayName("Should throw exception for unknown constructor")
        void create_unknownConstructor_throwsException() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            // When & Then
            assertThatThrownBy(() -> factory.create("unknown", "arg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No constructor registered with name: unknown");
        }

        @Test
        @DisplayName("Should throw exception for wrong number of arguments")
        void create_wrongArgumentCount_throwsException() {
            // Given
            TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);

            factory.registerConstructor("singleParam", String.class, StringBuilder::new);

            // When & Then
            assertThatThrownBy(() -> factory.create("singleParam", "arg1", "arg2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected 1 argument");
        }
    }

    @Nested
    @DisplayName("Type Aware Serializer Tests")
    class TypeAwareSerializerTests {

        private TypeErasureWorkarounds.TypeAwareSerializer serializer;

        @BeforeEach
        void setUp() {
            serializer = new TypeErasureWorkarounds.TypeAwareSerializer();
        }

        @Test
        @DisplayName("Should serialize and deserialize List<String>")
        void serializeDeserialize_listOfString_worksCorrectly() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            serializer.registerType(token,
                list -> String.join(",", list),
                data -> Arrays.asList(data.split(",")));

            List<String> original = Arrays.asList("apple", "banana", "cherry");

            // When
            String serialized = serializer.serialize(original, token);
            List<String> deserialized = serializer.deserialize(serialized, token);

            // Then
            assertThat(serialized).isEqualTo("apple,banana,cherry");
            assertThat(deserialized).isEqualTo(original);
        }

        @Test
        @DisplayName("Should serialize and deserialize Map<String, Integer>")
        void serializeDeserialize_mapOfStringToInteger_worksCorrectly() {
            // Given
            TypeErasureWorkarounds.TypeToken<Map<String, Integer>> token =
                new TypeErasureWorkarounds.TypeToken<Map<String, Integer>>() {};

            serializer.registerType(token,
                map -> map.entrySet().stream()
                    .map(e -> e.getKey() + ":" + e.getValue())
                    .reduce((a, b) -> a + ";" + b).orElse(""),
                data -> {
                    Map<String, Integer> result = new HashMap<>();
                    if (!data.isEmpty()) {
                        for (String pair : data.split(";")) {
                            String[] parts = pair.split(":");
                            result.put(parts[0], Integer.parseInt(parts[1]));
                        }
                    }
                    return result;
                });

            Map<String, Integer> original = Map.of("a", 1, "b", 2);

            // When
            String serialized = serializer.serialize(original, token);
            Map<String, Integer> deserialized = serializer.deserialize(serialized, token);

            // Then
            assertThat(deserialized).containsAllEntriesOf(original);
        }

        @Test
        @DisplayName("Should throw exception for unregistered type during serialization")
        void serialize_unregisteredType_throwsException() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            List<String> data = Arrays.asList("test");

            // When & Then
            assertThatThrownBy(() -> serializer.serialize(data, token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No serializer registered for type");
        }

        @Test
        @DisplayName("Should throw exception for unregistered type during deserialization")
        void deserialize_unregisteredType_throwsException() {
            // Given
            TypeErasureWorkarounds.TypeToken<List<String>> token =
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};

            // When & Then
            assertThatThrownBy(() -> serializer.deserialize("test", token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No deserializer registered for type");
        }
    }

    @Nested
    @DisplayName("Safe Collection Converter Tests")
    class SafeCollectionConverterTests {

        @Test
        @DisplayName("Should cast valid list correctly")
        void castList_validTypes_returnsTypedList() {
            // Given
            List<Object> objectList = Arrays.asList("a", "b", "c");

            // When
            List<String> stringList = TypeErasureWorkarounds.SafeCollectionConverter
                .castList(objectList, String.class);

            // Then
            assertThat(stringList).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should throw exception for invalid list element type")
        void castList_invalidElementType_throwsException() {
            // Given
            List<Object> mixedList = Arrays.asList("a", 42, "c");

            // When & Then
            assertThatThrownBy(() -> TypeErasureWorkarounds.SafeCollectionConverter
                .castList(mixedList, String.class))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("List contains element of type");
        }

        @Test
        @DisplayName("Should handle null elements in list")
        void castList_nullElements_handlesCorrectly() {
            // Given
            List<Object> listWithNulls = Arrays.asList("a", null, "c");

            // When
            List<String> stringList = TypeErasureWorkarounds.SafeCollectionConverter
                .castList(listWithNulls, String.class);

            // Then
            assertThat(stringList).containsExactly("a", null, "c");
        }

        @Test
        @DisplayName("Should cast valid map correctly")
        void castMap_validTypes_returnsTypedMap() {
            // Given
            Map<Object, Object> objectMap = new HashMap<>();
            objectMap.put("key1", 1);
            objectMap.put("key2", 2);

            // When
            Map<String, Integer> typedMap = TypeErasureWorkarounds.SafeCollectionConverter
                .castMap(objectMap, String.class, Integer.class);

            // Then
            assertThat(typedMap)
                .containsEntry("key1", 1)
                .containsEntry("key2", 2);
        }

        @Test
        @DisplayName("Should throw exception for invalid map key type")
        void castMap_invalidKeyType_throwsException() {
            // Given
            Map<Object, Object> mixedMap = new HashMap<>();
            mixedMap.put("key1", 1);
            mixedMap.put(42, 2);

            // When & Then
            assertThatThrownBy(() -> TypeErasureWorkarounds.SafeCollectionConverter
                .castMap(mixedMap, String.class, Integer.class))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("Map contains key of type");
        }

        @Test
        @DisplayName("Should throw exception for invalid map value type")
        void castMap_invalidValueType_throwsException() {
            // Given
            Map<Object, Object> mixedMap = new HashMap<>();
            mixedMap.put("key1", 1);
            mixedMap.put("key2", "invalid");

            // When & Then
            assertThatThrownBy(() -> TypeErasureWorkarounds.SafeCollectionConverter
                .castMap(mixedMap, String.class, Integer.class))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("Map contains value of type");
        }

        @Test
        @DisplayName("Should cast valid set correctly")
        void castSet_validTypes_returnsTypedSet() {
            // Given
            Set<Object> objectSet = new HashSet<>(Arrays.asList("a", "b", "c"));

            // When
            Set<String> stringSet = TypeErasureWorkarounds.SafeCollectionConverter
                .castSet(objectSet, String.class);

            // Then
            assertThat(stringSet).containsExactlyInAnyOrder("a", "b", "c");
        }
    }

    @Nested
    @DisplayName("Generic Method Invoker Tests")
    class GenericMethodInvokerTests {

        @Test
        @DisplayName("Should invoke generic method on instance")
        void invokeGenericMethod_validMethod_returnsCorrectResult() {
            // Given
            StringBuilder target = new StringBuilder("hello");

            // When
            String result = TypeErasureWorkarounds.GenericMethodInvoker
                .invokeGenericMethod(target, "toString", String.class);

            // Then
            assertThat(result).isEqualTo("hello");
        }

        @Test
        @DisplayName("Should invoke method with parameters")
        void invokeGenericMethod_withParameters_returnsCorrectResult() {
            // Given
            StringBuilder target = new StringBuilder();

            // When
            StringBuilder result = TypeErasureWorkarounds.GenericMethodInvoker
                .invokeGenericMethod(target, "append", StringBuilder.class, "test");

            // Then
            assertThat(result.toString()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should throw exception for non-existent method")
        void invokeGenericMethod_nonExistentMethod_throwsException() {
            // Given
            StringBuilder target = new StringBuilder();

            // When & Then
            assertThatThrownBy(() -> TypeErasureWorkarounds.GenericMethodInvoker
                .invokeGenericMethod(target, "nonExistent", String.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to invoke generic method");
        }

        @Test
        @DisplayName("Should invoke static method")
        void invokeGenericStaticMethod_validMethod_returnsCorrectResult() {
            // When
            String result = TypeErasureWorkarounds.GenericMethodInvoker
                .invokeGenericStaticMethod(String.class, "valueOf", String.class, 42);

            // Then
            assertThat(result).isEqualTo("42");
        }

        @Test
        @DisplayName("Should throw exception for non-existent static method")
        void invokeGenericStaticMethod_nonExistentMethod_throwsException() {
            // When & Then
            assertThatThrownBy(() -> TypeErasureWorkarounds.GenericMethodInvoker
                .invokeGenericStaticMethod(String.class, "nonExistent", String.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to invoke static generic method");
        }
    }

    @ParameterizedTest(name = "Should handle {0} correctly")
    @MethodSource("provideTypeErasureScenarios")
    @DisplayName("Should handle various type erasure workaround scenarios")
    void typeErasureScenarios_variousInputs_handlesCorrectly(String description, Runnable scenario) {
        // When & Then
        assertThatCode(scenario::run).doesNotThrowAnyException();
    }

    static Stream<Arguments> provideTypeErasureScenarios() {
        return Stream.of(
            Arguments.of("TypeToken creation", (Runnable) () -> {
                new TypeErasureWorkarounds.TypeToken<List<String>>() {};
            }),
            Arguments.of("TypeSafe container operations", (Runnable) () -> {
                TypeErasureWorkarounds.TypeSafeContainer container =
                    new TypeErasureWorkarounds.TypeSafeContainer();
                container.put(String.class, "test");
                container.get(String.class);
            }),
            Arguments.of("Array factory usage", (Runnable) () -> {
                String[] array = TypeErasureWorkarounds.GenericArrayFactory
                    .createArray(String.class, 5);
                List<String> list = TypeErasureWorkarounds.GenericArrayFactory
                    .createList(String.class);
            }),
            Arguments.of("Type preserving factory", (Runnable) () -> {
                TypeErasureWorkarounds.TypePreservingFactory<StringBuilder> factory =
                    new TypeErasureWorkarounds.TypePreservingFactory<>(StringBuilder.class);
                factory.createDefault();
            }),
            Arguments.of("Complete demonstration", (Runnable) () -> {
                TypeErasureWorkarounds.demonstrateTypeErasureWorkarounds();
            })
        );
    }

    @Test
    @DisplayName("Should demonstrate type erasure workarounds without error")
    void demonstrateTypeErasureWorkarounds_execution_completesWithoutError() {
        // When & Then
        assertThatCode(() -> TypeErasureWorkarounds.demonstrateTypeErasureWorkarounds())
            .doesNotThrowAnyException();
    }
}

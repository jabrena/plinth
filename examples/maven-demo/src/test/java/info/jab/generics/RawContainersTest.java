package info.jab.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RawContainers Test")
class RawContainersTest {

    @Nested
    @DisplayName("RawResult tests")
    class RawResultTests {

        @Test
        @DisplayName("Should create success result")
        void should_createSuccessResult_when_successFactoryMethodCalled() {
            // Given
            String value = "test value";

            // When
            RawContainers.RawResult result = RawContainers.RawResult.success(value);

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo(value);
            assertThat(result.getError()).isNull();
        }

        @Test
        @DisplayName("Should create failure result")
        void should_createFailureResult_when_failureFactoryMethodCalled() {
            // Given
            String error = "test error";

            // When
            RawContainers.RawResult result = RawContainers.RawResult.failure(error);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getError()).isEqualTo(error);
            assertThat(result.getValue()).isNull();
        }

        @Test
        @DisplayName("Should map success result")
        void should_mapResult_when_successResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("hello");
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawResult mapped = result.map(mapper);

            // Then
            assertThat(mapped.isSuccess()).isTrue();
            assertThat(mapped.getValue()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should not map failure result")
        void should_notMapResult_when_failureResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawResult mapped = result.map(mapper);

            // Then
            assertThat(mapped.isSuccess()).isFalse();
            assertThat(mapped.getError()).isEqualTo("error");
        }

        @Test
        @DisplayName("Should throw exception when mapper is not Function")
        void should_throwException_when_mapperNotFunction() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("hello");
            String notAMapper = "not a function";

            // When & Then
            assertThatThrownBy(() -> result.map(notAMapper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mapper must be a Function");
        }

        @Test
        @DisplayName("Should map error in failure result")
        void should_mapError_when_failureResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");
            Function<String, String> errorMapper = s -> "Processed: " + s;

            // When
            RawContainers.RawResult mapped = result.mapError(errorMapper);

            // Then
            assertThat(mapped.isSuccess()).isFalse();
            assertThat(mapped.getError()).isEqualTo("Processed: error");
        }

        @Test
        @DisplayName("Should not map error in success result")
        void should_notMapError_when_successResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("value");
            Function<String, String> errorMapper = s -> "Processed: " + s;

            // When
            RawContainers.RawResult mapped = result.mapError(errorMapper);

            // Then
            assertThat(mapped.isSuccess()).isTrue();
            assertThat(mapped.getValue()).isEqualTo("value");
        }

        @Test
        @DisplayName("Should flatMap success result")
        void should_flatMapResult_when_successResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("5");
            Function<String, RawContainers.RawResult> mapper = s -> {
                try {
                    int value = Integer.parseInt(s);
                    return RawContainers.RawResult.success(value);
                } catch (NumberFormatException e) {
                    return RawContainers.RawResult.failure("Invalid number");
                }
            };

            // When
            RawContainers.RawResult flatMapped = result.flatMap(mapper);

            // Then
            assertThat(flatMapped.isSuccess()).isTrue();
            assertThat(flatMapped.getValue()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should not flatMap failure result")
        void should_notFlatMapResult_when_failureResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");
            Function<String, RawContainers.RawResult> mapper = s -> RawContainers.RawResult.success(s.length());

            // When
            RawContainers.RawResult flatMapped = result.flatMap(mapper);

            // Then
            assertThat(flatMapped.isSuccess()).isFalse();
            assertThat(flatMapped.getError()).isEqualTo("error");
        }

        @Test
        @DisplayName("Should throw exception when flatMap function doesn't return RawResult")
        void should_throwException_when_flatMapFunctionReturnsWrongType() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("hello");
            Function<String, String> badMapper = s -> s.toUpperCase(); // Returns String, not RawResult

            // When & Then
            assertThatThrownBy(() -> result.flatMap(badMapper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("FlatMap function must return RawResult");
        }

        @Test
        @DisplayName("Should return value when orElse called on success")
        void should_returnValue_when_orElseCalledOnSuccess() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("success value");

            // When
            Object value = result.orElse("default value");

            // Then
            assertThat(value).isEqualTo("success value");
        }

        @Test
        @DisplayName("Should return default when orElse called on failure")
        void should_returnDefault_when_orElseCalledOnFailure() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");

            // When
            Object value = result.orElse("default value");

            // Then
            assertThat(value).isEqualTo("default value");
        }

        @Test
        @DisplayName("Should return value when orElseGet called on success")
        void should_returnValue_when_orElseGetCalledOnSuccess() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("success value");
            Supplier<String> supplier = () -> "default value";

            // When
            Object value = result.orElseGet(supplier);

            // Then
            assertThat(value).isEqualTo("success value");
        }

        @Test
        @DisplayName("Should return supplier result when orElseGet called on failure")
        void should_returnSupplierResult_when_orElseGetCalledOnFailure() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");
            Supplier<String> supplier = () -> "default from supplier";

            // When
            Object value = result.orElseGet(supplier);

            // Then
            assertThat(value).isEqualTo("default from supplier");
        }

        @Test
        @DisplayName("Should throw exception when orElseGet called with non-Supplier")
        void should_throwException_when_orElseGetCalledWithNonSupplier() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("error");
            String notASupplier = "not a supplier";

            // When & Then
            assertThatThrownBy(() -> result.orElseGet(notASupplier))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Must provide a Supplier");
        }
    }

    @Nested
    @DisplayName("RawPair tests")
    class RawPairTests {

        @Test
        @DisplayName("Should create pair with non-null values")
        void should_createPair_when_nonNullValuesProvided() {
            // Given & When
            RawContainers.RawPair pair = new RawContainers.RawPair("left", "right");

            // Then
            assertThat(pair.getLeft()).isEqualTo("left");
            assertThat(pair.getRight()).isEqualTo("right");
        }

        @Test
        @DisplayName("Should throw exception when left value is null")
        void should_throwException_when_leftValueIsNull() {
            // Given & When & Then
            assertThatThrownBy(() -> new RawContainers.RawPair(null, "right"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Left value cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when right value is null")
        void should_throwException_when_rightValueIsNull() {
            // Given & When & Then
            assertThatThrownBy(() -> new RawContainers.RawPair("left", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Right value cannot be null");
        }

        @Test
        @DisplayName("Should map left value")
        void should_mapLeftValue_when_mapLeftCalled() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("hello", 42);
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawPair mapped = pair.mapLeft(mapper);

            // Then
            assertThat(mapped.getLeft()).isEqualTo(5);
            assertThat(mapped.getRight()).isEqualTo(42);
            assertThat(mapped).isNotSameAs(pair); // New instance
        }

        @Test
        @DisplayName("Should map right value")
        void should_mapRightValue_when_mapRightCalled() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("hello", 42);
            Function<Integer, String> mapper = Object::toString;

            // When
            RawContainers.RawPair mapped = pair.mapRight(mapper);

            // Then
            assertThat(mapped.getLeft()).isEqualTo("hello");
            assertThat(mapped.getRight()).isEqualTo("42");
            assertThat(mapped).isNotSameAs(pair); // New instance
        }

        @Test
        @DisplayName("Should map both values")
        void should_mapBothValues_when_mapCalled() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("hello", "world");
            Function<String, Integer> leftMapper = String::length;
            Function<String, String> rightMapper = String::toUpperCase;

            // When
            RawContainers.RawPair mapped = pair.map(leftMapper, rightMapper);

            // Then
            assertThat(mapped.getLeft()).isEqualTo(5);
            assertThat(mapped.getRight()).isEqualTo("WORLD");
        }

        @Test
        @DisplayName("Should swap values")
        void should_swapValues_when_swapCalled() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("left", "right");

            // When
            RawContainers.RawPair swapped = pair.swap();

            // Then
            assertThat(swapped.getLeft()).isEqualTo("right");
            assertThat(swapped.getRight()).isEqualTo("left");
        }

        @Test
        @DisplayName("Should return pair when both values are of same type")
        void should_returnPair_when_ifSameTypeCalledWithMatchingType() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("hello", "world");

            // When
            Optional result = pair.ifSameType(String.class);

            // Then
            assertThat(result).isPresent();
            RawContainers.RawPair typedPair = (RawContainers.RawPair) result.get();
            assertThat(typedPair.getLeft()).isEqualTo("hello");
            assertThat(typedPair.getRight()).isEqualTo("world");
        }

        @Test
        @DisplayName("Should return empty when values are of different types")
        void should_returnEmpty_when_ifSameTypeCalledWithNonMatchingType() {
            // Given
            RawContainers.RawPair pair = new RawContainers.RawPair("hello", 42);

            // When
            Optional result = pair.ifSameType(String.class);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("RawContainer hierarchy tests")
    class RawContainerTests {

        @Test
        @DisplayName("Should create and handle empty container")
        void should_handleEmptyContainer_when_emptyContainerCreated() {
            // Given
            RawContainers.RawEmptyContainer container = new RawContainers.RawEmptyContainer();

            // When & Then
            assertThat(container.isEmpty()).isTrue();
            assertThat(container.size()).isZero();
            assertThat(container.getItems()).isEmpty();
            assertThat(container.getFirst()).isEmpty();
            assertThat(container.getLast()).isEmpty();
        }

        @Test
        @DisplayName("Should create and handle single container")
        void should_handleSingleContainer_when_singleContainerCreated() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("test");

            // When & Then
            assertThat(container.isEmpty()).isFalse();
            assertThat(container.size()).isEqualTo(1);
            assertThat(container.getItems()).containsExactly("test");
            assertThat(container.getFirst()).contains("test");
            assertThat(container.getLast()).contains("test");
            assertThat(container.getItem()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should throw exception when creating single container with null")
        void should_throwException_when_singleContainerCreatedWithNull() {
            // Given & When & Then
            assertThatThrownBy(() -> new RawContainers.RawSingleContainer(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Item cannot be null");
        }

        @Test
        @DisplayName("Should create and handle multi container")
        void should_handleMultiContainer_when_multiContainerCreated() {
            // Given
            @SuppressWarnings("rawtypes")
            List items = Arrays.asList("a", "b", "c");
            RawContainers.RawMultiContainer container = new RawContainers.RawMultiContainer(items);

            // When & Then
            assertThat(container.isEmpty()).isFalse();
            assertThat(container.size()).isEqualTo(3);
            assertThat(container.getItems()).containsExactly("a", "b", "c");
            assertThat(container.getFirst()).contains("a");
            assertThat(container.getLast()).contains("c");
        }

        @Test
        @DisplayName("Should throw exception when creating multi container with empty list")
        void should_throwException_when_multiContainerCreatedWithEmptyList() {
            // Given
            @SuppressWarnings("rawtypes")
            List emptyList = new ArrayList();

            // When & Then
            assertThatThrownBy(() -> new RawContainers.RawMultiContainer(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MultiContainer cannot be empty");
        }

        @Test
        @DisplayName("Should map empty container to empty")
        void should_mapEmptyToEmpty_when_mapCalledOnEmpty() {
            // Given
            RawContainers.RawEmptyContainer container = new RawContainers.RawEmptyContainer();
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawContainer result = container.map(mapper);

            // Then
            assertThat(result).isInstanceOf(RawContainers.RawEmptyContainer.class);
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should map single container correctly")
        void should_mapSingleContainer_when_mapCalledOnSingle() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("hello");
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawContainer result = container.map(mapper);

            // Then
            assertThat(result).isInstanceOf(RawContainers.RawSingleContainer.class);
            RawContainers.RawSingleContainer singleResult = (RawContainers.RawSingleContainer) result;
            assertThat(singleResult.getItem()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should map multi container correctly")
        void should_mapMultiContainer_when_mapCalledOnMulti() {
            // Given
            @SuppressWarnings("rawtypes")
            List items = Arrays.asList("hello", "world", "test");
            RawContainers.RawMultiContainer container = new RawContainers.RawMultiContainer(items);
            Function<String, Integer> mapper = String::length;

            // When
            RawContainers.RawContainer result = container.map(mapper);

            // Then
            assertThat(result).isInstanceOf(RawContainers.RawMultiContainer.class);
            assertThat(result.getItems()).containsExactly(5, 5, 4);
        }

        @Test
        @DisplayName("Should filter empty container to empty")
        void should_filterEmptyToEmpty_when_filterCalledOnEmpty() {
            // Given
            RawContainers.RawEmptyContainer container = new RawContainers.RawEmptyContainer();
            Predicate<String> predicate = s -> s.length() > 3;

            // When
            RawContainers.RawContainer result = container.filter(predicate);

            // Then
            assertThat(result).isSameAs(container);
        }

        @Test
        @DisplayName("Should filter single container based on predicate")
        void should_filterSingleContainer_when_filterCalledOnSingle() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("hello");
            Predicate<String> truePredicate = s -> s.length() > 3;
            Predicate<String> falsePredicate = s -> s.length() > 10;

            // When
            RawContainers.RawContainer trueResult = container.filter(truePredicate);
            RawContainers.RawContainer falseResult = container.filter(falsePredicate);

            // Then
            assertThat(trueResult).isSameAs(container);
            assertThat(falseResult).isInstanceOf(RawContainers.RawEmptyContainer.class);
        }

        @Test
        @DisplayName("Should filter multi container and return appropriate type")
        void should_filterMultiContainer_when_filterCalledOnMulti() {
            // Given
            @SuppressWarnings("rawtypes")
            List items = Arrays.asList("hello", "hi", "world", "test");
            RawContainers.RawMultiContainer container = new RawContainers.RawMultiContainer(items);

            Predicate<String> longWordsPredicate = s -> s.length() > 3;
            Predicate<String> impossiblePredicate = s -> s.length() > 100;
            Predicate<String> singleWordPredicate = s -> s.equals("hello");

            // When
            RawContainers.RawContainer multiResult = container.filter(longWordsPredicate);
            RawContainers.RawContainer emptyResult = container.filter(impossiblePredicate);
            RawContainers.RawContainer singleResult = container.filter(singleWordPredicate);

            // Then
            assertThat(multiResult).isInstanceOf(RawContainers.RawMultiContainer.class);
            assertThat(multiResult.getItems()).containsExactly("hello", "world", "test");

            assertThat(emptyResult).isInstanceOf(RawContainers.RawEmptyContainer.class);

            assertThat(singleResult).isInstanceOf(RawContainers.RawSingleContainer.class);
            assertThat(singleResult.getItems()).containsExactly("hello");
        }

        @Test
        @DisplayName("Should throw exception when mapper is not Function")
        void should_throwException_when_mapperNotFunction() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("hello");
            String notAMapper = "not a function";

            // When & Then
            assertThatThrownBy(() -> container.map(notAMapper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mapper must be a Function");
        }

        @Test
        @DisplayName("Should throw exception when predicate is not Predicate")
        void should_throwException_when_predicateNotPredicate() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("hello");
            String notAPredicate = "not a predicate";

            // When & Then
            assertThatThrownBy(() -> container.filter(notAPredicate))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Predicate must be a Predicate");
        }
    }

    @Nested
    @DisplayName("UnsafeHeterogeneousContainer tests")
    class UnsafeHeterogeneousContainerTests {

        @Test
        @DisplayName("Should store and retrieve heterogeneous values")
        void should_storeAndRetrieve_when_heterogeneousValuesProvided() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();

            // When
            container.put(String.class, "hello");
            container.put(Integer.class, 42);
            container.put("custom_key", "custom_value");

            // Then
            assertThat(container.get(String.class)).isEqualTo("hello");
            assertThat(container.get(Integer.class)).isEqualTo(42);
            assertThat(container.get("custom_key")).isEqualTo("custom_value");
        }

        @Test
        @DisplayName("Should check containment correctly")
        void should_checkContainment_when_keysStored() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();
            container.put(String.class, "test");

            // When & Then
            assertThat(container.contains(String.class)).isTrue();
            assertThat(container.contains(Integer.class)).isFalse();
        }

        @Test
        @DisplayName("Should remove values correctly")
        void should_removeValues_when_keysExist() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();
            container.put(String.class, "test");

            // When
            Object removed = container.remove(String.class);

            // Then
            assertThat(removed).isEqualTo("test");
            assertThat(container.contains(String.class)).isFalse();
        }

        @Test
        @DisplayName("Should return stored keys")
        void should_returnStoredKeys_when_keysStored() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();
            container.put(String.class, "hello");
            container.put(Integer.class, 42);

            // When
            @SuppressWarnings("rawtypes")
            Set keys = container.getStoredKeys();

            // Then
            assertThat(keys).containsExactlyInAnyOrder(String.class, Integer.class);
        }

        @Test
        @DisplayName("Should get instances of specific type hierarchy")
        void should_getInstancesOfType_when_typeHierarchyExists() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();
            container.put(String.class, "hello");
            container.put(Object.class, new Object());
            container.put(Integer.class, 42);
            container.put("non_class_key", "value");

            // When
            @SuppressWarnings("rawtypes")
            List objectInstances = container.getInstancesOfType(Object.class);

            // Then
            assertThat(objectInstances).hasSize(3); // String, Object, Integer are all assignable to Object
        }

        @Test
        @DisplayName("Should get all values of exact type")
        void should_getAllOfType_when_exactTypeSpecified() {
            // Given
            RawContainers.UnsafeHeterogeneousContainer container =
                new RawContainers.UnsafeHeterogeneousContainer();
            container.put(String.class, "hello");
            container.put(Integer.class, 42);
            container.put("non_class_key", "value");

            // When
            @SuppressWarnings("rawtypes")
            Map stringValues = container.getAllOfType(String.class);

            // Then
            assertThat(stringValues).containsEntry("instance", "hello");
            assertThat(stringValues).hasSize(1);
        }
    }

    @Nested
    @DisplayName("VerbosePatternMatching tests")
    class VerbosePatternMatchingTests {

        @Test
        @DisplayName("Should describe empty container")
        void should_describeEmptyContainer_when_emptyContainerProvided() {
            // Given
            RawContainers.RawEmptyContainer container = new RawContainers.RawEmptyContainer();

            // When
            String description = RawContainers.VerbosePatternMatching.describeContainer(container);

            // Then
            assertThat(description).isEqualTo("Empty container");
        }

        @Test
        @DisplayName("Should describe single container")
        void should_describeSingleContainer_when_singleContainerProvided() {
            // Given
            RawContainers.RawSingleContainer container = new RawContainers.RawSingleContainer("test");

            // When
            String description = RawContainers.VerbosePatternMatching.describeContainer(container);

            // Then
            assertThat(description).isEqualTo("Single item: test");
        }

        @Test
        @DisplayName("Should describe multi container")
        void should_describeMultiContainer_when_multiContainerProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List items = Arrays.asList("a", "b", "c", "d", "e");
            RawContainers.RawMultiContainer container = new RawContainers.RawMultiContainer(items);

            // When
            String description = RawContainers.VerbosePatternMatching.describeContainer(container);

            // Then
            assertThat(description).startsWith("Multiple items (5): ");
            assertThat(description).contains("[a, b, c]");
        }

        @Test
        @DisplayName("Should handle success result")
        void should_handleSuccessResult_when_successResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.success("test value");

            // When
            String description = RawContainers.VerbosePatternMatching.handleResult(result);

            // Then
            assertThat(description).isEqualTo("Success: test value");
        }

        @Test
        @DisplayName("Should handle failure result")
        void should_handleFailureResult_when_failureResultProvided() {
            // Given
            RawContainers.RawResult result = RawContainers.RawResult.failure("test error");

            // When
            String description = RawContainers.VerbosePatternMatching.handleResult(result);

            // Then
            assertThat(description).isEqualTo("Error: test error");
        }

        @ParameterizedTest
        @ValueSource(strings = {"short", "this is a very long string"})
        @DisplayName("Should process string values correctly")
        void should_processStringValues_when_stringsProvided(String input) {
            // Given & When
            String result = RawContainers.VerbosePatternMatching.processValue(input);

            // Then
            if (input.length() > 10) {
                assertThat(result).startsWith("Long string: ");
                assertThat(result).contains("...");
            } else {
                assertThat(result).isEqualTo("Short string: " + input);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {50, 150})
        @DisplayName("Should process integer values correctly")
        void should_processIntegerValues_when_integersProvided(int input) {
            // Given & When
            String result = RawContainers.VerbosePatternMatching.processValue(input);

            // Then
            if (input > 100) {
                assertThat(result).isEqualTo("Large integer: " + input);
            } else {
                assertThat(result).isEqualTo("Small integer: " + input);
            }
        }

        @Test
        @DisplayName("Should process list values correctly")
        void should_processListValues_when_listsProvided() {
            // Given
            @SuppressWarnings("rawtypes")
            List emptyList = new ArrayList();
            @SuppressWarnings("rawtypes")
            List nonEmptyList = Arrays.asList(1, 2, 3);

            // When
            String emptyResult = RawContainers.VerbosePatternMatching.processValue(emptyList);
            String nonEmptyResult = RawContainers.VerbosePatternMatching.processValue(nonEmptyList);

            // Then
            assertThat(emptyResult).isEqualTo("Empty list");
            assertThat(nonEmptyResult).isEqualTo("List with 3 items");
        }

        @Test
        @DisplayName("Should process null value correctly")
        void should_processNullValue_when_nullProvided() {
            // Given & When
            String result = RawContainers.VerbosePatternMatching.processValue(null);

            // Then
            assertThat(result).isEqualTo("Null value");
        }

        @Test
        @DisplayName("Should process unknown type correctly")
        void should_processUnknownType_when_unknownTypeProvided() {
            // Given
            Object unknownObject = new HashMap<>();

            // When
            String result = RawContainers.VerbosePatternMatching.processValue(unknownObject);

            // Then
            assertThat(result).isEqualTo("Unknown type: HashMap");
        }
    }

    @Nested
    @DisplayName("UnsafeFunctionalOperations tests")
    class UnsafeFunctionalOperationsTests {

        @Test
        @DisplayName("Should attempt operation successfully")
        void should_attemptOperation_when_operationSucceeds() {
            // Given
            Supplier<String> operation = () -> "success";
            Function<Exception, String> errorMapper = Exception::getMessage;

            // When
            RawContainers.RawResult result =
                RawContainers.UnsafeFunctionalOperations.attempt(operation, errorMapper);

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getValue()).isEqualTo("success");
        }

        @Test
        @DisplayName("Should handle operation failure")
        void should_handleOperationFailure_when_operationThrows() {
            // Given
            Supplier<String> operation = () -> {
                throw new RuntimeException("test error");
            };
            Function<Exception, String> errorMapper = Exception::getMessage;

            // When
            RawContainers.RawResult result =
                RawContainers.UnsafeFunctionalOperations.attempt(operation, errorMapper);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getError()).isEqualTo("test error");
        }

        @Test
        @DisplayName("Should throw exception when operation is not Supplier")
        void should_throwException_when_operationNotSupplier() {
            // Given
            String notAnOperation = "not a supplier";
            Function<Exception, String> errorMapper = Exception::getMessage;

            // When & Then
            assertThatThrownBy(() ->
                RawContainers.UnsafeFunctionalOperations.attempt(notAnOperation, errorMapper))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Operation must be a Supplier");
        }

        @Test
        @DisplayName("Should combine successful results")
        void should_combineResults_when_bothResultsSuccessful() {
            // Given
            RawContainers.RawResult result1 = RawContainers.RawResult.success(5);
            RawContainers.RawResult result2 = RawContainers.RawResult.success(10);
            BinaryOperator<Integer> combiner = Integer::sum;

            // When
            RawContainers.RawResult combined =
                RawContainers.UnsafeFunctionalOperations.combine(result1, result2, combiner);

            // Then
            assertThat(combined.isSuccess()).isTrue();
            assertThat(combined.getValue()).isEqualTo(15);
        }

        @Test
        @DisplayName("Should return first failure when combining results")
        void should_returnFirstFailure_when_firstResultFails() {
            // Given
            RawContainers.RawResult result1 = RawContainers.RawResult.failure("error1");
            RawContainers.RawResult result2 = RawContainers.RawResult.success(10);
            BinaryOperator<Integer> combiner = Integer::sum;

            // When
            RawContainers.RawResult combined =
                RawContainers.UnsafeFunctionalOperations.combine(result1, result2, combiner);

            // Then
            assertThat(combined.isSuccess()).isFalse();
            assertThat(combined.getError()).isEqualTo("error1");
        }

        @Test
        @DisplayName("Should sequence successful results")
        void should_sequenceResults_when_allResultsSuccessful() {
            // Given
            @SuppressWarnings("rawtypes")
            List results = Arrays.asList(
                RawContainers.RawResult.success(1),
                RawContainers.RawResult.success(2),
                RawContainers.RawResult.success(3)
            );

            // When
            RawContainers.RawResult sequenced =
                RawContainers.UnsafeFunctionalOperations.sequence(results);

            // Then
            assertThat(sequenced.isSuccess()).isTrue();
            @SuppressWarnings("rawtypes")
            List values = (List) sequenced.getValue();
            assertThat(values).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should return first failure when sequencing results")
        void should_returnFirstFailure_when_sequencingWithFailure() {
            // Given
            @SuppressWarnings("rawtypes")
            List results = Arrays.asList(
                RawContainers.RawResult.success(1),
                RawContainers.RawResult.failure("error"),
                RawContainers.RawResult.success(3)
            );

            // When
            RawContainers.RawResult sequenced =
                RawContainers.UnsafeFunctionalOperations.sequence(results);

            // Then
            assertThat(sequenced.isSuccess()).isFalse();
            assertThat(sequenced.getError()).isEqualTo("error");
        }

        @Test
        @DisplayName("Should memoize function")
        void should_memoizeFunction_when_functionProvided() {
            // Given
            Function<String, Integer> function = String::length;

            // When
            Object memoized = RawContainers.UnsafeFunctionalOperations.memoize(function);

            // Then
            assertThat(memoized).isInstanceOf(Function.class);
            @SuppressWarnings("unchecked")
            Function<String, Integer> memoizedFunction = (Function<String, Integer>) memoized;

            // Test that the memoized function works
            assertThat(memoizedFunction.apply("hello")).isEqualTo(5);
            assertThat(memoizedFunction.apply("world")).isEqualTo(5);
        }

        @Test
        @DisplayName("Should throw exception when memoizing non-Function")
        void should_throwException_when_memoizingNonFunction() {
            // Given
            String notAFunction = "not a function";

            // When & Then
            assertThatThrownBy(() -> RawContainers.UnsafeFunctionalOperations.memoize(notAFunction))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Must provide a Function");
        }
    }

    @Test
    @DisplayName("Should demonstrate container problems without throwing exceptions")
    void should_demonstrateContainerProblems_when_calledWithValidData() {
        // Given & When & Then
        // This test verifies that the demonstration method runs without crashing
        // and properly handles the expected issues with raw container patterns
        RawContainers.demonstrateContainerProblems();

        // The method should complete execution, demonstrating the problems
        // with raw container patterns while handling expected runtime exceptions
        assertThat(true).isTrue(); // Test passes if no unexpected exceptions are thrown
    }
}

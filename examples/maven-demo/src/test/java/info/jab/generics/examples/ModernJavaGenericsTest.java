package info.jab.generics.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
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

@DisplayName("ModernJavaGenerics Tests")
class ModernJavaGenericsTest {

    @Nested
    @DisplayName("Result Record Tests")
    class ResultRecordTests {

        @Test
        @DisplayName("Should create success result")
        void success_validValue_createsSuccessResult() {
            // When
            ModernJavaGenerics.Result<String, String> result = ModernJavaGenerics.Result.success("Hello");

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.value()).isEqualTo("Hello");
            assertThat(result.error()).isNull();
        }

        @Test
        @DisplayName("Should create failure result")
        void failure_validError_createsFailureResult() {
            // When
            ModernJavaGenerics.Result<String, String> result = ModernJavaGenerics.Result.failure("Error occurred");

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.value()).isNull();
            assertThat(result.error()).isEqualTo("Error occurred");
        }

        @Test
        @DisplayName("Should throw exception for invalid success result")
        void constructor_successWithNullValue_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.Result<>(null, null, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Success result must have a value");
        }

        @Test
        @DisplayName("Should throw exception for invalid failure result")
        void constructor_failureWithNullError_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.Result<>(null, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failure result must have an error");
        }

        @Test
        @DisplayName("Should map success result")
        void map_successResult_transformsValue() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("hello");

            // When
            ModernJavaGenerics.Result<Integer, String> mapped = success.map(String::length);

            // Then
            assertThat(mapped.isSuccess()).isTrue();
            assertThat(mapped.value()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should not map failure result")
        void map_failureResult_keepsFailure() {
            // Given
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When
            ModernJavaGenerics.Result<Integer, String> mapped = failure.map(String::length);

            // Then
            assertThat(mapped.isSuccess()).isFalse();
            assertThat(mapped.error()).isEqualTo("error");
        }

        @Test
        @DisplayName("Should map error in failure result")
        void mapError_failureResult_transformsError() {
            // Given
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When
            ModernJavaGenerics.Result<String, Integer> mapped = failure.mapError(String::length);

            // Then
            assertThat(mapped.isSuccess()).isFalse();
            assertThat(mapped.error()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should not map error in success result")
        void mapError_successResult_keepsSuccess() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("value");

            // When
            ModernJavaGenerics.Result<String, Integer> mapped = success.mapError(String::length);

            // Then
            assertThat(mapped.isSuccess()).isTrue();
            assertThat(mapped.value()).isEqualTo("value");
        }

        @Test
        @DisplayName("Should flatMap success result")
        void flatMap_successResult_chainsOperations() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("5");

            // When
            ModernJavaGenerics.Result<Integer, String> flatMapped = success.flatMap(s -> {
                try {
                    return ModernJavaGenerics.Result.success(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    return ModernJavaGenerics.Result.failure("Invalid number");
                }
            });

            // Then
            assertThat(flatMapped.isSuccess()).isTrue();
            assertThat(flatMapped.value()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should convert success to Optional")
        void toOptional_successResult_returnsOptionalWithValue() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("value");

            // When
            Optional<String> optional = success.toOptional();

            // Then
            assertThat(optional).isPresent().contains("value");
        }

        @Test
        @DisplayName("Should convert failure to empty Optional")
        void toOptional_failureResult_returnsEmptyOptional() {
            // Given
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When
            Optional<String> optional = failure.toOptional();

            // Then
            assertThat(optional).isEmpty();
        }

        @Test
        @DisplayName("Should return value or else default")
        void orElse_results_returnsCorrectValue() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("value");
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When & Then
            assertThat(success.orElse("default")).isEqualTo("value");
            assertThat(failure.orElse("default")).isEqualTo("default");
        }

        @Test
        @DisplayName("Should return value or else get from supplier")
        void orElseGet_results_returnsCorrectValue() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("value");
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");
            Supplier<String> supplier = () -> "supplied";

            // When & Then
            assertThat(success.orElseGet(supplier)).isEqualTo("value");
            assertThat(failure.orElseGet(supplier)).isEqualTo("supplied");
        }

        @Test
        @DisplayName("Should throw mapped exception for failure")
        void orElseThrow_failureResult_throwsMappedException() {
            // Given
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When & Then
            assertThatThrownBy(() -> failure.orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("error");
        }

        @Test
        @DisplayName("Should return value for success")
        void orElseThrow_successResult_returnsValue() throws Exception {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("value");

            // When
            String result = success.orElseThrow(RuntimeException::new);

            // Then
            assertThat(result).isEqualTo("value");
        }
    }

    @Nested
    @DisplayName("Pair Record Tests")
    class PairRecordTests {

        @Test
        @DisplayName("Should create pair with valid values")
        void constructor_validValues_createsPair() {
            // When
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // Then
            assertThat(pair.left()).isEqualTo("hello");
            assertThat(pair.right()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should throw exception for null left value")
        void constructor_nullLeft_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.Pair<>(null, 42))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Left value cannot be null");
        }

        @Test
        @DisplayName("Should throw exception for null right value")
        void constructor_nullRight_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.Pair<>("hello", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Right value cannot be null");
        }

        @Test
        @DisplayName("Should map left value")
        void mapLeft_validMapper_transformsLeftValue() {
            // Given
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // When
            ModernJavaGenerics.Pair<Integer, Integer> mapped = pair.mapLeft(String::length);

            // Then
            assertThat(mapped.left()).isEqualTo(5);
            assertThat(mapped.right()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should map right value")
        void mapRight_validMapper_transformsRightValue() {
            // Given
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // When
            ModernJavaGenerics.Pair<String, String> mapped = pair.mapRight(Object::toString);

            // Then
            assertThat(mapped.left()).isEqualTo("hello");
            assertThat(mapped.right()).isEqualTo("42");
        }

        @Test
        @DisplayName("Should map both values")
        void map_validMappers_transformsBothValues() {
            // Given
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // When
            ModernJavaGenerics.Pair<Integer, String> mapped = pair.map(String::length, Object::toString);

            // Then
            assertThat(mapped.left()).isEqualTo(5);
            assertThat(mapped.right()).isEqualTo("42");
        }

        @Test
        @DisplayName("Should swap values")
        void swap_validPair_swapsValues() {
            // Given
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // When
            ModernJavaGenerics.Pair<Integer, String> swapped = pair.swap();

            // Then
            assertThat(swapped.left()).isEqualTo(42);
            assertThat(swapped.right()).isEqualTo("hello");
        }

        @Test
        @DisplayName("Should check if same type and return typed pair")
        void ifSameType_sameType_returnsOptionalWithTypedPair() {
            // Given
            ModernJavaGenerics.Pair<String, String> pair = new ModernJavaGenerics.Pair<>("hello", "world");

            // When
            Optional<ModernJavaGenerics.Pair<String, String>> result = pair.ifSameType(String.class);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().left()).isEqualTo("hello");
            assertThat(result.get().right()).isEqualTo("world");
        }

        @Test
        @DisplayName("Should return empty for different types")
        void ifSameType_differentTypes_returnsEmpty() {
            // Given
            ModernJavaGenerics.Pair<String, Integer> pair = new ModernJavaGenerics.Pair<>("hello", 42);

            // When
            Optional<ModernJavaGenerics.Pair<String, String>> result = pair.ifSameType(String.class);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Container Hierarchy Tests")
    class ContainerHierarchyTests {

        @Test
        @DisplayName("Should create empty container")
        void emptyContainer_creation_hasCorrectProperties() {
            // When
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.EmptyContainer<>();

            // Then
            assertThat(container.isEmpty()).isTrue();
            assertThat(container.size()).isEqualTo(0);
            assertThat(container.getItems()).isEmpty();
            assertThat(container.getFirst()).isEmpty();
            assertThat(container.getLast()).isEmpty();
        }

        @Test
        @DisplayName("Should create single container")
        void singleContainer_creation_hasCorrectProperties() {
            // When
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.SingleContainer<>("hello");

            // Then
            assertThat(container.isEmpty()).isFalse();
            assertThat(container.size()).isEqualTo(1);
            assertThat(container.getItems()).containsExactly("hello");
            assertThat(container.getFirst()).isPresent().contains("hello");
            assertThat(container.getLast()).isPresent().contains("hello");
        }

        @Test
        @DisplayName("Should create multi container")
        void multiContainer_creation_hasCorrectProperties() {
            // Given
            List<String> items = Arrays.asList("a", "b", "c");

            // When
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.MultiContainer<>(items);

            // Then
            assertThat(container.isEmpty()).isFalse();
            assertThat(container.size()).isEqualTo(3);
            assertThat(container.getItems()).containsExactly("a", "b", "c");
            assertThat(container.getFirst()).isPresent().contains("a");
            assertThat(container.getLast()).isPresent().contains("c");
        }

        @Test
        @DisplayName("Should throw exception for null item in single container")
        void singleContainer_nullItem_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.SingleContainer<>(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Item cannot be null");
        }

        @Test
        @DisplayName("Should throw exception for empty list in multi container")
        void multiContainer_emptyList_throwsException() {
            // When & Then
            assertThatThrownBy(() -> new ModernJavaGenerics.MultiContainer<>(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MultiContainer cannot be empty");
        }

        @Test
        @DisplayName("Should map container contents")
        void map_containers_transformsCorrectly() {
            // Given
            ModernJavaGenerics.Container<String> empty = new ModernJavaGenerics.EmptyContainer<>();
            ModernJavaGenerics.Container<String> single = new ModernJavaGenerics.SingleContainer<>("hello");
            ModernJavaGenerics.Container<String> multi = new ModernJavaGenerics.MultiContainer<>(Arrays.asList("a", "bb", "ccc"));

            Function<String, Integer> lengthMapper = String::length;

            // When
            ModernJavaGenerics.Container<Integer> mappedEmpty = empty.map(lengthMapper);
            ModernJavaGenerics.Container<Integer> mappedSingle = single.map(lengthMapper);
            ModernJavaGenerics.Container<Integer> mappedMulti = multi.map(lengthMapper);

            // Then
            assertThat(mappedEmpty).isInstanceOf(ModernJavaGenerics.EmptyContainer.class);
            assertThat(mappedSingle.getItems()).containsExactly(5);
            assertThat(mappedMulti.getItems()).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should filter container contents")
        void filter_containers_filtersCorrectly() {
            // Given
            ModernJavaGenerics.Container<String> empty = new ModernJavaGenerics.EmptyContainer<>();
            ModernJavaGenerics.Container<String> single = new ModernJavaGenerics.SingleContainer<>("hello");
            ModernJavaGenerics.Container<String> multi = new ModernJavaGenerics.MultiContainer<>(Arrays.asList("a", "bb", "ccc"));

            Predicate<String> lengthGreaterThanTwo = s -> s.length() > 2;

            // When
            ModernJavaGenerics.Container<String> filteredEmpty = empty.filter(lengthGreaterThanTwo);
            ModernJavaGenerics.Container<String> filteredSingle = single.filter(lengthGreaterThanTwo);
            ModernJavaGenerics.Container<String> filteredMulti = multi.filter(lengthGreaterThanTwo);

            // Then
            assertThat(filteredEmpty).isInstanceOf(ModernJavaGenerics.EmptyContainer.class);
            assertThat(filteredSingle.getItems()).containsExactly("hello");
            assertThat(filteredMulti.getItems()).containsExactly("ccc");
        }
    }

    @Nested
    @DisplayName("Expression Evaluation Tests")
    class ExpressionEvaluationTests {

        private ModernJavaGenerics generics;

        @BeforeEach
        void setUp() {
            generics = new ModernJavaGenerics();
        }

        @Test
        @DisplayName("Should evaluate literal expression")
        void literalExpression_evaluation_returnsValue() {
            // Given
            ModernJavaGenerics.Expression<Integer> literal = generics.new LiteralExpression<>(42);

            // When
            Integer result = literal.evaluate();

            // Then
            assertThat(result).isEqualTo(42);
        }

        @Test
        @DisplayName("Should evaluate binary expression")
        void binaryExpression_evaluation_returnsCorrectResult() {
            // Given
            ModernJavaGenerics.Expression<Integer> left = generics.new LiteralExpression<>(10);
            ModernJavaGenerics.Expression<Integer> right = generics.new LiteralExpression<>(20);
            BinaryOperator<Integer> addOperator = Integer::sum;
            ModernJavaGenerics.Expression<Integer> binary = generics.new BinaryExpression<>(left, right, addOperator);

            // When
            Integer result = binary.evaluate();

            // Then
            assertThat(result).isEqualTo(30);
        }

        @Test
        @DisplayName("Should evaluate unary expression")
        void unaryExpression_evaluation_returnsCorrectResult() {
            // Given
            ModernJavaGenerics.Expression<Integer> operand = generics.new LiteralExpression<>(5);
            UnaryOperator<Integer> negateOperator = x -> -x;
            ModernJavaGenerics.Expression<Integer> unary = generics.new UnaryExpression<>(operand, negateOperator);

            // When
            Integer result = unary.evaluate();

            // Then
            assertThat(result).isEqualTo(-5);
        }

        @Test
        @DisplayName("Should accept visitor for literal expression")
        void literalExpression_visitor_returnsCorrectResult() {
            // Given
            ModernJavaGenerics.Expression<Integer> literal = generics.new LiteralExpression<>(42);
            ModernJavaGenerics.ExpressionStringifier<Integer> stringifier = new ModernJavaGenerics.ExpressionStringifier<>();

            // When
            String result = literal.accept(stringifier);

            // Then
            assertThat(result).isEqualTo("42");
        }

        @Test
        @DisplayName("Should accept visitor for binary expression")
        void binaryExpression_visitor_returnsCorrectResult() {
            // Given
            ModernJavaGenerics.Expression<Integer> left = generics.new LiteralExpression<>(10);
            ModernJavaGenerics.Expression<Integer> right = generics.new LiteralExpression<>(20);
            ModernJavaGenerics.Expression<Integer> binary = generics.new BinaryExpression<>(left, right, Integer::sum);
            ModernJavaGenerics.ExpressionStringifier<Integer> stringifier = new ModernJavaGenerics.ExpressionStringifier<>();

            // When
            String result = binary.accept(stringifier);

            // Then
            assertThat(result).isEqualTo("(10 op 20)");
        }

        @Test
        @DisplayName("Should accept visitor for unary expression")
        void unaryExpression_visitor_returnsCorrectResult() {
            // Given
            ModernJavaGenerics.Expression<Integer> operand = generics.new LiteralExpression<>(5);
            ModernJavaGenerics.Expression<Integer> unary = generics.new UnaryExpression<>(operand, x -> -x);
            ModernJavaGenerics.ExpressionStringifier<Integer> stringifier = new ModernJavaGenerics.ExpressionStringifier<>();

            // When
            String result = unary.accept(stringifier);

            // Then
            assertThat(result).isEqualTo("(op 5)");
        }
    }

    @Nested
    @DisplayName("Pattern Matching Tests")
    class PatternMatchingTests {

        @Test
        @DisplayName("Should describe empty container")
        void describeContainer_emptyContainer_returnsCorrectDescription() {
            // Given
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.EmptyContainer<>();

            // When
            String description = ModernJavaGenerics.PatternMatchingExamples.describeContainer(container);

            // Then
            assertThat(description).isEqualTo("Empty container");
        }

        @Test
        @DisplayName("Should describe single container")
        void describeContainer_singleContainer_returnsCorrectDescription() {
            // Given
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.SingleContainer<>("hello");

            // When
            String description = ModernJavaGenerics.PatternMatchingExamples.describeContainer(container);

            // Then
            assertThat(description).isEqualTo("Single item: hello");
        }

        @Test
        @DisplayName("Should describe multi container")
        void describeContainer_multiContainer_returnsCorrectDescription() {
            // Given
            ModernJavaGenerics.Container<String> container = new ModernJavaGenerics.MultiContainer<>(
                Arrays.asList("a", "b", "c", "d", "e"));

            // When
            String description = ModernJavaGenerics.PatternMatchingExamples.describeContainer(container);

            // Then
            assertThat(description).contains("Multiple items (5)");
            assertThat(description).contains("[a, b, c]");
        }

        @Test
        @DisplayName("Should handle success result")
        void handleResult_successResult_returnsCorrectDescription() {
            // Given
            ModernJavaGenerics.Result<String, String> result = ModernJavaGenerics.Result.success("value");

            // When
            String description = ModernJavaGenerics.PatternMatchingExamples.handleResult(result);

            // Then
            assertThat(description).isEqualTo("Success: value");
        }

        @Test
        @DisplayName("Should handle failure result")
        void handleResult_failureResult_returnsCorrectDescription() {
            // Given
            ModernJavaGenerics.Result<String, String> result = ModernJavaGenerics.Result.failure("error");

            // When
            String description = ModernJavaGenerics.PatternMatchingExamples.handleResult(result);

            // Then
            assertThat(description).isEqualTo("Error: error");
        }

        @ParameterizedTest(name = "Should process {0} correctly")
        @MethodSource("provideProcessValueTestCases")
        @DisplayName("Should process various value types with pattern matching")
        void processValue_variousTypes_returnsCorrectDescription(String description, Object value, String expectedPrefix) {
            // When
            String result = ModernJavaGenerics.PatternMatchingExamples.processValue(value);

            // Then
            assertThat(result).startsWith(expectedPrefix);
        }

        static Stream<Arguments> provideProcessValueTestCases() {
            return Stream.of(
                Arguments.of("Long string", "This is a very long string that exceeds ten characters", "Long string:"),
                Arguments.of("Short string", "short", "Short string:"),
                Arguments.of("Large integer", 150, "Large integer:"),
                Arguments.of("Small integer", 50, "Small integer:"),
                Arguments.of("Empty list", Collections.emptyList(), "Empty list"),
                Arguments.of("Non-empty list", Arrays.asList(1, 2, 3), "List with 3 items"),
                Arguments.of("Null value", null, "Null value"),
                Arguments.of("Unknown type", new Object(), "Unknown type:")
            );
        }
    }

    @Nested
    @DisplayName("Functional Generics Tests")
    class FunctionalGenericsTests {

        @Test
        @DisplayName("Should handle successful attempt")
        void attempt_successfulOperation_returnsSuccess() {
            // Given
            Supplier<String> successOperation = () -> "success";
            Function<Exception, String> errorMapper = Exception::getMessage;

            // When
            ModernJavaGenerics.Result<String, String> result =
                ModernJavaGenerics.FunctionalGenerics.attempt(successOperation, errorMapper);

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.value()).isEqualTo("success");
        }

        @Test
        @DisplayName("Should handle failed attempt")
        void attempt_failingOperation_returnsFailure() {
            // Given
            Supplier<String> failingOperation = () -> {
                throw new RuntimeException("operation failed");
            };
            Function<Exception, String> errorMapper = Exception::getMessage;

            // When
            ModernJavaGenerics.Result<String, String> result =
                ModernJavaGenerics.FunctionalGenerics.attempt(failingOperation, errorMapper);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.error()).isEqualTo("operation failed");
        }

        @Test
        @DisplayName("Should combine successful results")
        void combine_successfulResults_returnsSuccess() {
            // Given
            ModernJavaGenerics.Result<String, String> result1 = ModernJavaGenerics.Result.success("Hello");
            ModernJavaGenerics.Result<String, String> result2 = ModernJavaGenerics.Result.success("World");

            // When
            ModernJavaGenerics.Result<String, String> combined =
                ModernJavaGenerics.FunctionalGenerics.combine(result1, result2, (a, b) -> a + " " + b);

            // Then
            assertThat(combined.isSuccess()).isTrue();
            assertThat(combined.value()).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should return failure when one result fails")
        void combine_oneFailure_returnsFailure() {
            // Given
            ModernJavaGenerics.Result<String, String> success = ModernJavaGenerics.Result.success("Hello");
            ModernJavaGenerics.Result<String, String> failure = ModernJavaGenerics.Result.failure("error");

            // When
            ModernJavaGenerics.Result<String, String> combined =
                ModernJavaGenerics.FunctionalGenerics.combine(success, failure, (a, b) -> a + " " + b);

            // Then
            assertThat(combined.isSuccess()).isFalse();
            assertThat(combined.error()).isEqualTo("error");
        }

        @Test
        @DisplayName("Should sequence successful results")
        void sequence_allSuccessful_returnsListOfValues() {
            // Given
            List<ModernJavaGenerics.Result<String, String>> results = Arrays.asList(
                ModernJavaGenerics.Result.success("a"),
                ModernJavaGenerics.Result.success("b"),
                ModernJavaGenerics.Result.success("c")
            );

            // When
            ModernJavaGenerics.Result<List<String>, String> sequenced =
                ModernJavaGenerics.FunctionalGenerics.sequence(results);

            // Then
            assertThat(sequenced.isSuccess()).isTrue();
            assertThat(sequenced.value()).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("Should return first failure in sequence")
        void sequence_containsFailure_returnsFirstFailure() {
            // Given
            List<ModernJavaGenerics.Result<String, String>> results = Arrays.asList(
                ModernJavaGenerics.Result.success("a"),
                ModernJavaGenerics.Result.failure("error1"),
                ModernJavaGenerics.Result.failure("error2")
            );

            // When
            ModernJavaGenerics.Result<List<String>, String> sequenced =
                ModernJavaGenerics.FunctionalGenerics.sequence(results);

            // Then
            assertThat(sequenced.isSuccess()).isFalse();
            assertThat(sequenced.error()).isEqualTo("error1");
        }

        @Test
        @DisplayName("Should memoize function calls")
        void memoize_repeatedCalls_cachesResults() {
            // Given
            final int[] callCount = {0};
            Function<Integer, Integer> expensiveFunction = x -> {
                callCount[0]++;
                return x * x;
            };

            Function<Integer, Integer> memoizedFunction =
                ModernJavaGenerics.FunctionalGenerics.memoize(expensiveFunction);

            // When
            Integer result1 = memoizedFunction.apply(5);
            Integer result2 = memoizedFunction.apply(5);
            Integer result3 = memoizedFunction.apply(3);

            // Then
            assertThat(result1).isEqualTo(25);
            assertThat(result2).isEqualTo(25);
            assertThat(result3).isEqualTo(9);
            assertThat(callCount[0]).isEqualTo(2); // Only called twice due to memoization
        }

        @Test
        @DisplayName("Should partially apply bi-function")
        void partial_biFunction_returnsPartiallyAppliedFunction() {
            // Given
            java.util.function.BiFunction<String, String, String> concat = (a, b) -> a + b;

            // When
            Function<String, String> partialConcat =
                ModernJavaGenerics.FunctionalGenerics.partial(concat, "Hello ");

            String result = partialConcat.apply("World");

            // Then
            assertThat(result).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should partially apply tri-function")
        void partial_triFunction_returnsPartiallyAppliedBiFunction() {
            // Given
            ModernJavaGenerics.TriFunction<String, String, String, String> triConcat =
                (a, b, c) -> a + b + c;

            // When
            java.util.function.BiFunction<String, String, String> partialTriConcat =
                ModernJavaGenerics.FunctionalGenerics.partial(triConcat, "Hello ");

            String result = partialTriConcat.apply("Beautiful ", "World");

            // Then
            assertThat(result).isEqualTo("Hello Beautiful World");
        }
    }

    @Test
    @DisplayName("Should demonstrate modern Java generics without error")
    void demonstrateModernJavaGenerics_execution_completesWithoutError() {
        // When & Then
        assertThatCode(() -> ModernJavaGenerics.demonstrateModernJavaGenerics())
            .doesNotThrowAnyException();
    }
}

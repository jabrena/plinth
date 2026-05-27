package info.jab.generics.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Modern Java generics examples using Records, sealed types, pattern matching,
 * and advanced features available in Java 17+.
 */
public class ModernJavaGenerics {

    // Generic Records with validation and factory methods
    public record Result<T, E>(T value, E error, boolean isSuccess) {

        // Compact constructor with validation
        public Result {
            if (isSuccess && value == null) {
                throw new IllegalArgumentException("Success result must have a value");
            }
            if (!isSuccess && error == null) {
                throw new IllegalArgumentException("Failure result must have an error");
            }
        }

        // Factory methods
        public static <T, E> Result<T, E> success(T value) {
            return new Result<>(value, null, true);
        }

        public static <T, E> Result<T, E> failure(E error) {
            return new Result<>(null, error, false);
        }

        // Transformation methods
        public <U> Result<U, E> map(Function<T, U> mapper) {
            return isSuccess ? success(mapper.apply(value)) : failure(error);
        }

        public <F> Result<T, F> mapError(Function<E, F> errorMapper) {
            return isSuccess ? success(value) : failure(errorMapper.apply(error));
        }

        public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
            return isSuccess ? mapper.apply(value) : failure(error);
        }

        // Utility methods
        public Optional<T> toOptional() {
            return isSuccess ? Optional.of(value) : Optional.empty();
        }

        public T orElse(T defaultValue) {
            return isSuccess ? value : defaultValue;
        }

        public T orElseGet(Supplier<T> supplier) {
            return isSuccess ? value : supplier.get();
        }

        public <X extends Throwable> T orElseThrow(Function<E, X> exceptionMapper) throws X {
            if (isSuccess) {
                return value;
            } else {
                throw exceptionMapper.apply(error);
            }
        }
    }

    // Generic Record with multiple type parameters and complex operations
    public record Pair<L, R>(L left, R right) {

        public Pair {
            Objects.requireNonNull(left, "Left value cannot be null");
            Objects.requireNonNull(right, "Right value cannot be null");
        }

        // Transformation methods
        public <T> Pair<T, R> mapLeft(Function<L, T> mapper) {
            return new Pair<>(mapper.apply(left), right);
        }

        public <T> Pair<L, T> mapRight(Function<R, T> mapper) {
            return new Pair<>(left, mapper.apply(right));
        }

        public <T, U> Pair<T, U> map(Function<L, T> leftMapper, Function<R, U> rightMapper) {
            return new Pair<>(leftMapper.apply(left), rightMapper.apply(right));
        }

        // Swap operation
        public Pair<R, L> swap() {
            return new Pair<>(right, left);
        }

        // Apply a function to both values if they're the same type
        @SuppressWarnings("unchecked")
        public <T> Optional<Pair<T, T>> ifSameType(Class<T> type) {
            if (type.isInstance(left) && type.isInstance(right)) {
                return Optional.of(new Pair<>((T) left, (T) right));
            }
            return Optional.empty();
        }
    }

    // Sealed interface with generic constraints
    public sealed interface Container<T>
            permits SingleContainer, MultiContainer, EmptyContainer {

        boolean isEmpty();
        int size();
        List<T> getItems();

        // Default methods with generics
        default Optional<T> getFirst() {
            List<T> items = getItems();
            return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
        }

        default Optional<T> getLast() {
            List<T> items = getItems();
            return items.isEmpty() ? Optional.empty() : Optional.of(items.get(items.size() - 1));
        }

        default <R> Container<R> map(Function<T, R> mapper) {
            return switch (this) {
                case EmptyContainer<T> empty -> new EmptyContainer<>();
                case SingleContainer<T> single -> new SingleContainer<>(mapper.apply(single.item()));
                case MultiContainer<T> multi -> new MultiContainer<>(
                    multi.items().stream().map(mapper).toList());
            };
        }

        default Container<T> filter(Predicate<T> predicate) {
            return switch (this) {
                case EmptyContainer<T> empty -> empty;
                case SingleContainer<T> single ->
                    predicate.test(single.item()) ? single : new EmptyContainer<>();
                case MultiContainer<T> multi -> {
                    List<T> filtered = multi.items().stream()
                        .filter(predicate)
                        .toList();
                    yield switch (filtered.size()) {
                        case 0 -> new EmptyContainer<T>();
                        case 1 -> new SingleContainer<>(filtered.get(0));
                        default -> new MultiContainer<>(filtered);
                    };
                }
            };
        }
    }

    // Sealed implementations
    public record SingleContainer<T>(T item) implements Container<T> {
        public SingleContainer {
            Objects.requireNonNull(item, "Item cannot be null");
        }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public int size() { return 1; }

        @Override
        public List<T> getItems() { return List.of(item); }
    }

    public record MultiContainer<T>(List<T> items) implements Container<T> {
        public MultiContainer {
            Objects.requireNonNull(items, "Items cannot be null");
            if (items.isEmpty()) {
                throw new IllegalArgumentException("MultiContainer cannot be empty");
            }
            items = List.copyOf(items); // Defensive copy
        }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public int size() { return items.size(); }

        @Override
        public List<T> getItems() { return items; }
    }

    public record EmptyContainer<T>() implements Container<T> {
        @Override
        public boolean isEmpty() { return true; }

        @Override
        public int size() { return 0; }

        @Override
        public List<T> getItems() { return List.of(); }
    }

    // Generic sealed class hierarchy for expression evaluation
    public sealed abstract class Expression<T>
            permits LiteralExpression, BinaryExpression, UnaryExpression {

        public abstract T evaluate();
        public abstract <R> R accept(ExpressionVisitor<T, R> visitor);
    }

    public final class LiteralExpression<T> extends Expression<T> {
        private final T value;

        public LiteralExpression(T value) {
            this.value = Objects.requireNonNull(value);
        }

        public T getValue() { return value; }

        @Override
        public T evaluate() { return value; }

        @Override
        public <R> R accept(ExpressionVisitor<T, R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public final class BinaryExpression<T> extends Expression<T> {
        private final Expression<T> left;
        private final Expression<T> right;
        private final BinaryOperator<T> operator;

        public BinaryExpression(Expression<T> left, Expression<T> right, BinaryOperator<T> operator) {
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
            this.operator = Objects.requireNonNull(operator);
        }

        public Expression<T> getLeft() { return left; }
        public Expression<T> getRight() { return right; }
        public BinaryOperator<T> getOperator() { return operator; }

        @Override
        public T evaluate() {
            return operator.apply(left.evaluate(), right.evaluate());
        }

        @Override
        public <R> R accept(ExpressionVisitor<T, R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    public final class UnaryExpression<T> extends Expression<T> {
        private final Expression<T> operand;
        private final UnaryOperator<T> operator;

        public UnaryExpression(Expression<T> operand, UnaryOperator<T> operator) {
            this.operand = Objects.requireNonNull(operand);
            this.operator = Objects.requireNonNull(operator);
        }

        public Expression<T> getOperand() { return operand; }
        public UnaryOperator<T> getOperator() { return operator; }

        @Override
        public T evaluate() {
            return operator.apply(operand.evaluate());
        }

        @Override
        public <R> R accept(ExpressionVisitor<T, R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    // Visitor pattern with generics
    public interface ExpressionVisitor<T, R> {
        R visitLiteral(LiteralExpression<T> literal);
        R visitBinary(BinaryExpression<T> binary);
        R visitUnary(UnaryExpression<T> unary);
    }

    // Concrete visitor implementations
    public static class ExpressionStringifier<T> implements ExpressionVisitor<T, String> {
        @Override
        public String visitLiteral(LiteralExpression<T> literal) {
            return literal.getValue().toString();
        }

        @Override
        public String visitBinary(BinaryExpression<T> binary) {
            return "(" + binary.getLeft().accept(this) + " op " +
                   binary.getRight().accept(this) + ")";
        }

        @Override
        public String visitUnary(UnaryExpression<T> unary) {
            return "(" + "op " + unary.getOperand().accept(this) + ")";
        }
    }

    // Pattern matching with generics and switch expressions
    public static class PatternMatchingExamples {

        public static <T> String describeContainer(Container<T> container) {
            return switch (container) {
                case EmptyContainer<T> empty -> "Empty container";
                case SingleContainer<T> single ->
                    "Single item: " + single.item();
                case MultiContainer<T> multi ->
                    "Multiple items (" + multi.items().size() + "): " +
                    multi.items().stream().limit(3).toList();
            };
        }

        public static <T, E> String handleResult(Result<T, E> result) {
            if (result.isSuccess()) {
                return "Success: " + result.value();
            } else {
                return "Error: " + result.error();
            }
        }

        // Pattern matching with instanceof and generics
        public static String processValue(Object value) {
            return switch (value) {
                case String s when s.length() > 10 -> "Long string: " + s.substring(0, 10) + "...";
                case String s -> "Short string: " + s;
                case Integer i when i > 100 -> "Large integer: " + i;
                case Integer i -> "Small integer: " + i;
                case List<?> list when list.isEmpty() -> "Empty list";
                case List<?> list -> "List with " + list.size() + " items";
                case null -> "Null value";
                default -> "Unknown type: " + value.getClass().getSimpleName();
            };
        }
    }

    // Modern generic utility class with functional interfaces
    public static class FunctionalGenerics {

        // Monadic operations with Result
        public static <T, E> Result<T, E> attempt(Supplier<T> operation, Function<Exception, E> errorMapper) {
            try {
                return Result.success(operation.get());
            } catch (Exception e) {
                return Result.failure(errorMapper.apply(e));
            }
        }

        // Combining multiple Results
        public static <T1, T2, R, E> Result<R, E> combine(
                Result<T1, E> result1,
                Result<T2, E> result2,
                BiFunction<T1, T2, R> combiner) {

            return result1.flatMap(v1 ->
                result2.map(v2 -> combiner.apply(v1, v2)));
        }

        // Sequence operation for collections of Results
        public static <T, E> Result<List<T>, E> sequence(List<Result<T, E>> results) {
            List<T> values = new ArrayList<>();
            for (Result<T, E> result : results) {
                if (!result.isSuccess()) {
                    return Result.failure(result.error());
                }
                values.add(result.value());
            }
            return Result.success(values);
        }

        // Generic memoization utility
        public static <T, R> Function<T, R> memoize(Function<T, R> function) {
            Map<T, R> cache = new java.util.concurrent.ConcurrentHashMap<>();
            return input -> cache.computeIfAbsent(input, function);
        }

        // Partial application utilities
        public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> function, T first) {
            return second -> function.apply(first, second);
        }

        public static <T, U, V, R> BiFunction<U, V, R> partial(
                TriFunction<T, U, V, R> function, T first) {
            return (second, third) -> function.apply(first, second, third);
        }
    }

    // Custom functional interface for demonstration
    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    // Demonstration methods
    public static void demonstrateModernJavaGenerics() {
        // Result pattern demonstration
        Result<String, String> success = Result.success("Hello World");
        Result<String, String> failure = Result.failure("Something went wrong");

        Result<Integer, String> lengthResult = success.map(String::length);

        // Container pattern matching
        Container<String> empty = new EmptyContainer<>();
        Container<String> single = new SingleContainer<>("Hello");
        Container<String> multi = new MultiContainer<>(List.of("A", "B", "C"));

        String emptyDesc = PatternMatchingExamples.describeContainer(empty);
        String singleDesc = PatternMatchingExamples.describeContainer(single);
        String multiDesc = PatternMatchingExamples.describeContainer(multi);

        // Expression evaluation
        ModernJavaGenerics generics = new ModernJavaGenerics();
        Expression<Integer> literal = generics.new LiteralExpression<>(42);
        Expression<Integer> binary = generics.new BinaryExpression<>(
            literal, literal, Integer::sum);

        ExpressionStringifier<Integer> stringifier = new ExpressionStringifier<>();
        String expressionString = binary.accept(stringifier);
        Integer result = binary.evaluate();

        // Functional operations
        List<Result<Integer, String>> results = List.of(
            Result.success(1),
            Result.success(2),
            Result.success(3)
        );

        Result<List<Integer>, String> sequenced = FunctionalGenerics.sequence(results);

        // Memoized function
        Function<Integer, Integer> fibonacci = FunctionalGenerics.memoize(n -> {
            if (n <= 1) return n;
            return FunctionalGenerics.memoize(ModernJavaGenerics::fibonacci).apply(n - 1) +
                   FunctionalGenerics.memoize(ModernJavaGenerics::fibonacci).apply(n - 2);
        });

        // Pair operations
        Pair<String, Integer> pair = new Pair<>("Hello", 42);
        Pair<Integer, Integer> mappedPair = pair.mapLeft(String::length);

        System.out.println("Modern Java generics demonstration completed");
        System.out.println("Success result: " + success);
        System.out.println("Length result: " + lengthResult);
        System.out.println("Empty container: " + emptyDesc);
        System.out.println("Single container: " + singleDesc);
        System.out.println("Multi container: " + multiDesc);
        System.out.println("Expression: " + expressionString);
        System.out.println("Expression result: " + result);
        System.out.println("Sequenced results: " + sequenced);
        System.out.println("Fibonacci(10): " + fibonacci.apply(10));
        System.out.println("Original pair: " + pair);
        System.out.println("Mapped pair: " + mappedPair);
    }

    // Helper method for memoized fibonacci
    private static Integer fibonacci(Integer n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

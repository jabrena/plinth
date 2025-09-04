package info.jab.generics.examples;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Demonstrates the power of Java generics with a type-safe, fluent data pipeline
 * combining PECS wildcards, self-bounded generics (CRTP), bounded type parameters,
 * and modern Records - all in under 50 lines.
 */
public class TypeSafeDataPipeline {

    // Generic Result type with covariant success and contravariant error handling
    public sealed interface Result<T, E> permits Success, Failure {
        static <T, E> Result<T, E> success(T value) { return new Success<>(value); }
        static <T, E> Result<T, E> failure(E error) { return new Failure<>(error); }

        // PECS: Producer extends for transformation input, Consumer super for error handling
        <U> Result<U, E> map(Function<? super T, ? extends U> mapper);
        <F> Result<T, F> mapError(Function<? super E, ? extends F> errorMapper);
    }

    record Success<T, E>(T value) implements Result<T, E> {
        public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return Result.success(mapper.apply(value));
        }
        public <F> Result<T, F> mapError(Function<? super E, ? extends F> errorMapper) {
            return Result.success(value);
        }
    }

    record Failure<T, E>(E error) implements Result<T, E> {
        public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return Result.failure(error);
        }
        public <F> Result<T, F> mapError(Function<? super E, ? extends F> errorMapper) {
            return Result.failure(errorMapper.apply(error));
        }
    }

    // Self-bounded generic pipeline with fluent API (CRTP pattern)
    public static abstract class Pipeline<T, P extends Pipeline<T, P>> {
        protected final List<T> data;

        protected Pipeline(List<? extends T> data) { this.data = List.copyOf(data); }
        @SuppressWarnings("unchecked") protected P self() { return (P) this; }

        // Bounded type parameter ensures comparability while maintaining fluent return type
        public <U extends Comparable<U>> P sortBy(Function<? super T, U> keyExtractor) {
            return createNew(data.stream().sorted((a, b) ->
                keyExtractor.apply(a).compareTo(keyExtractor.apply(b))).toList());
        }

        public P filter(Predicate<? super T> predicate) {
            return createNew(data.stream().filter(predicate).toList());
        }

        public List<T> collect() { return List.copyOf(data); }
        protected abstract P createNew(List<T> newData);
    }

    // Concrete implementation preserving type safety and fluent chaining
    public static final class DataPipeline<T> extends Pipeline<T, DataPipeline<T>> {
        private DataPipeline(List<? extends T> data) { super(data); }
        public static <T> DataPipeline<T> of(List<? extends T> data) { return new DataPipeline<>(data); }
        protected DataPipeline<T> createNew(List<T> newData) { return new DataPipeline<>(newData); }
    }

    // Demonstration showcasing multiple generics concepts working together
    public static void main(String[] args) {
        // Type-safe pipeline with fluent chaining, PECS wildcards, and bounded generics
        List<String> processed = DataPipeline.of(List.of("zebra", "apple", "Banana", "cherry"))
            .filter(s -> s.length() > 4)        // PECS: ? super String
            .sortBy(String::toLowerCase)        // Bounded: String implements Comparable
            .collect();                         // Returns List<String>

        // Result type with covariant mapping and error handling
        Result<Integer, String> result = Result.<String, String>success("Hello")
            .map(String::length)                // PECS: ? super String, ? extends Integer
            .mapError(String::toUpperCase);     // PECS: ? super String, ? extends String

        System.out.println("Processed: " + processed + ", Result: " + result);
    }
}

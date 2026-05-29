package info.jab.generics.examples;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Example using Java generics features like PECS wildcards, sealed types,
 * covariance, diamond operator, bounded generics, and modern Java integration.
 */
public class ResultSample {

    public sealed interface Result<T> permits Success, Failure {
        static <T> Result<T> success(T value) { return new Success<>(value); }
        static <T> Result<T> failure(Throwable exception) { return new Failure<>(exception); }
        boolean isSuccess();
        boolean isFailure();
        T getOrNull();
        Throwable exceptionOrNull();
        T getOrElse(Function<? super Throwable, ? extends T> onFailure); // PECS: Consumer Super
        <R> Result<R> map(Function<? super T, ? extends R> transform);    // PECS: Producer Extends
        <R> Result<R> mapCatching(Function<? super T, ? extends R> transform);
        Result<T> onSuccess(Consumer<? super T> action);                 // PECS: Consumer Super
        Result<T> onFailure(Consumer<? super Throwable> action);
        <R> R fold(Function<? super T, ? extends R> onSuccess, Function<? super Throwable, ? extends R> onFailure);
    }

    record Success<T>(T value) implements Result<T> {
        public boolean isSuccess() { return true; }
        public boolean isFailure() { return false; }
        public T getOrNull() { return value; }
        public Throwable exceptionOrNull() { return null; }
        public T getOrElse(Function<? super Throwable, ? extends T> onFailure) { return value; }
        public <R> Result<R> map(Function<? super T, ? extends R> transform) {
            return Result.success(transform.apply(value));
        }
        public <R> Result<R> mapCatching(Function<? super T, ? extends R> transform) {
            try { return Result.success(transform.apply(value)); }
            catch (Exception e) { return Result.failure(e); }
        }
        public Result<T> onSuccess(Consumer<? super T> action) { action.accept(value); return this; }
        public Result<T> onFailure(Consumer<? super Throwable> action) { return this; }
        public <R> R fold(Function<? super T, ? extends R> onSuccess,
                         Function<? super Throwable, ? extends R> onFailure) {
            return onSuccess.apply(value);
        }
    }

    record Failure<T>(Throwable exception) implements Result<T> {
        public boolean isSuccess() { return false; }
        public boolean isFailure() { return true; }
        public T getOrNull() { return null; }
        public Throwable exceptionOrNull() { return exception; }
        public T getOrElse(Function<? super Throwable, ? extends T> onFailure) {
            return onFailure.apply(exception);
        }
        @SuppressWarnings("unchecked")
        public <R> Result<R> map(Function<? super T, ? extends R> transform) { return (Result<R>) this; }
        @SuppressWarnings("unchecked")
        public <R> Result<R> mapCatching(Function<? super T, ? extends R> transform) { return (Result<R>) this; }
        public Result<T> onSuccess(Consumer<? super T> action) { return this; }
        public Result<T> onFailure(Consumer<? super Throwable> action) { action.accept(exception); return this; }
        public <R> R fold(Function<? super T, ? extends R> onSuccess,
                         Function<? super Throwable, ? extends R> onFailure) {
            return onFailure.apply(exception);
        }
    }

    public static Result<Integer> divide(int a, int b) {
        return b != 0
            ? Result.success(a / b)
            : Result.failure(new ArithmeticException("Division by zero is not allowed"));
    }

    public static void main(String[] args) {

        // Safe division - handles division by zero gracefully
        String safeResult = divide(10, 2)
            .map(result -> result * 3)            // PECS: Transform success value
            .onSuccess(n -> System.out.println("✅ Division result: " + n))
            .fold(n -> "SUCCESS: " + n,
                  e -> "ERROR: " + e.getMessage());
        System.out.println("✅ Safe division: " + safeResult);

        // Chain multiple operations safely
        String chainResult = divide(20, 4)
            .mapCatching(n -> divide(n, 2).getOrElse(throwable -> 0))  // Nested safe operations
            .map(n -> "Final: " + n)
            .getOrElse(throwable -> "Failed");
        System.out.println("✅ Chained: " + chainResult);

        String errorResult = divide(10, 0)
            .fold(n -> "SUCCESS: " + n,
                  e -> "HANDLED: " + e.getMessage());
        System.out.println("❌ " + errorResult);
    }
}

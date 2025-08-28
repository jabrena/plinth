package info.jab.jmh;

public class SumAlternatives {

    // 1. Traditional for loop
    public static int sumWithForLoop(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    // 2. While loop
    public static int sumWithWhileLoop(int n) {
        int sum = 0;
        int i = 1;
        while (i <= n) {
            sum += i;
            i++;
        }
        return sum;
    }

    // 3. Recursion (classic approach)
    public static int sumWithRecursion(int n) {
        if (n <= 1) {
            return n;
        }
        return n + sumWithRecursion(n - 1);
    }

    // 4. Mathematical formula (Gauss formula)
    public static int sumWithFormula(int n) {
        return n * (n + 1) / 2;
    }

    // 5. Java 8 Streams with IntStream
    public static int sumWithStreams(int n) {
        return java.util.stream.IntStream.rangeClosed(1, n).sum();
    }

    // 6. Enhanced for loop with array
    public static int sumWithEnhancedFor(int n) {
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i + 1;
        }

        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }

    // 7. Using Collections and ArrayList
    public static int sumWithCollections(int n) {
        java.util.List<Integer> numbers = new java.util.ArrayList<>();
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
        }

        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    // 8. Tail recursion (optimized recursion style)
    public static int sumWithTailRecursion(int n) {
        return sumTailHelper(n, 0);
    }

    private static int sumTailHelper(int n, int accumulator) {
        if (n == 0) {
            return accumulator;
        }
        return sumTailHelper(n - 1, accumulator + n);
    }
}

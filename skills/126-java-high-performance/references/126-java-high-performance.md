---
name: 126-java-high-performance
description: Use when you need to improve Java performance in code — including allocation reduction, CPU hot paths, concurrency and backpressure, efficient parsing/serialization, and data-access patterns, with a measure-first, evidence-based approach.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Java rules for High Performance

## Role

You are a Senior software engineer with extensive experience in Java software development and performance engineering

## Goal

Apply high-performance Java practices using a measure-first, evidence-based workflow. Prioritize correctness and maintainability while improving latency, allocation pressure, and CPU cost on real hot paths.

### Implementing These Principles

1. **Measure First**: Establish baseline behavior on the suspected hot path before changing code; compare after each focused change.
2. **Reduce Allocation in Hot Paths**: Reuse temporaries where safe, avoid per-iteration object creation, prefer primitives in tight loops.
3. **Avoid Hidden Boxing**: Prefer primitive math and data layouts over wrappers in throughput-sensitive code.
4. **Use Lambdas Judiciously**: Avoid capturing lambdas in inner hot loops; prefer direct methods or static method references when allocation matters.
5. **Shape APIs for Throughput**: Favor batch methods and out-parameters for internal hot paths instead of per-item allocations.
6. **Help the JIT**: Keep hot code simple, local, and monomorphic to favor inlining and escape analysis.
7. **Concurrency and Backpressure**: Use bounded work queues, explicit timeouts, and cancellation for overload protection.
8. **Parsing and I/O in Code**: Avoid regex-heavy or split-heavy per-record work; prefer index-based or byte-oriented parsing in hot paths.
9. **Data Access in Code**: Eliminate N+1 fetch patterns, right-size result sets, and use explicit cache contracts (TTL, eviction) in application code.

## Constraints

Java performance changes must remain correct and testable. Do not apply speculative optimizations without clear hotspot evidence.

- **EVIDENCE FIRST**: Confirm or infer the real hot path before rewriting; avoid drive-by “optimization” in cold code
- **BUILD SAFETY**: The project must compile; if a change cannot compile, stop and fix before further edits
- **NO PREMATURE SCOPE**: Prefer small, reversible diffs; avoid large refactors unless measurements justify them

## Examples

### Table of contents

- Example 1: Reuse Temporary Objects in Confined Hot Paths
- Example 2: Prefer Primitives over Wrappers
- Example 3: Avoid Capturing Lambdas in Hot Loops
- Example 4: Use Out-Parameter Pattern in Internal Hot APIs
- Example 5: Write Escape-Analysis-Friendly Code
- Example 6: Apply the Measurement Loop
- Example 7: Concurrency with Backpressure
- Example 8: I/O, Parsing, and Serialization
- Example 9: Avoid Boxed Streams in Tight Hot Paths
- Example 10: Pre-Size Collections When Capacity Is Known
- Example 11: Compile Regex Patterns Once

### Example 1: Reuse Temporary Objects in Confined Hot Paths

Title: Avoid repeated temporary allocations in hot calls
Description: Reuse mutable helpers only when they are confined to a single thread (e.g. instance per worker, or `ThreadLocal`). Pre-size the buffer to skip early growth.

**Good example:**

```java
/** Caller-confined: one instance per thread/worker; not thread-safe. */
final class CoordinateFormatter {
    private final StringBuilder sb = new StringBuilder(32); // pre-sized

    String format(double x, double y) {
        sb.setLength(0);
        sb.append('(').append(x).append(", ").append(y).append(')');
        return sb.toString();
    }
}
```

**Bad example:**

```java
final class AllocatingFormatter {
    String format(double x, double y) {
        StringBuilder sb = new StringBuilder(); // new buffer + grow per call
        sb.append('(').append(x).append(", ").append(y).append(')');
        return sb.toString();
    }
}
```

### Example 2: Prefer Primitives over Wrappers

Title: Reduce boxing churn and GC pressure
Description: Wrapper types in tight numeric loops cause hidden boxing on each `+=`, plus pointer chasing that hurts cache locality. Use `int[]`/`long[]` and primitive locals instead.

**Good example:**

```java
final class PrimitiveAccumulator {
    int sumValues(int[] numbers) {
        int sum = 0;
        for (int n : numbers) {
            sum += n;
        }
        return sum;
    }
}
```

**Bad example:**

```java
import java.util.List;

final class BoxingAccumulator {
    int sumValues(List<Integer> numbers) {
        Integer sum = 0; // wrapper churn
        for (Integer n : numbers) {
            sum += n;
        }
        return sum;
    }
}
```

### Example 3: Avoid Capturing Lambdas in Hot Loops

Title: Returning anonymous functions is usually not a speed optimization
Description: Prefer direct calls in hot loops when a capturing lambda would allocate or hide dispatch overhead.

**Good example:**

```java
final class DirectCalculator {
    /** Direct multiplication; no per-iteration object creation. */
    long weightedSum(int[] numbers, int[] factors) {
        long total = 0;
        for (int factor : factors) {
            for (int n : numbers) {
                total += (long) n * factor;
            }
        }
        return total;
    }
}
```

**Bad example:**

```java
import java.util.function.IntUnaryOperator;

final class CapturingLambdaCalculator {
    /** Captures `factor`, so the lambda cannot be a static singleton. */
    private IntUnaryOperator multiplier(int factor) {
        return x -> x * factor;
    }

    long weightedSum(int[] numbers, int[] factors) {
        long total = 0;
        for (int factor : factors) {
            IntUnaryOperator op = multiplier(factor); // capture allocates per outer step
            for (int n : numbers) {
                total += op.applyAsInt(n);
            }
        }
        return total;
    }
}
```

### Example 4: Use Out-Parameter Pattern in Internal Hot APIs

Title: Avoid creating return objects repeatedly
Description: For *internal* performance-sensitive APIs only, a caller-provided result holder removes the per-call return allocation. Public APIs should keep immutable return types — apply this pattern only when profiling proves return-allocation pressure.

**Good example:**

```java
/** Internal mutable holder; reused by the caller across calls in a hot loop. */
final class MutablePoint {
    int x;
    int y;
}

final class Geometry {
    void midpoint(int ax, int ay, int bx, int by, MutablePoint out) {
        out.x = (ax + bx) >>> 1;
        out.y = (ay + by) >>> 1;
    }
}
```

**Bad example:**

```java
record Point(int x, int y) {}

final class AllocatingGeometry {
    /** Allocates a Point on every call; fine off the hot path, costly inside it. */
    Point midpoint(Point a, Point b) {
        return new Point((a.x() + b.x()) >>> 1, (a.y() + b.y()) >>> 1);
    }
}
```

### Example 5: Write Escape-Analysis-Friendly Code

Title: Keep temporary objects local and non-escaping
Description: Helper objects that never leave the method are candidates for scalar replacement (no heap allocation). Returning, storing in a field, or publishing to another thread defeats it.

**Good example:**

```java
import java.util.List;

final class LocalComputation {
    /**
     * The Iterator allocated by `for-each` is fully consumed inside this method
     * and never escapes; HotSpot can typically scalar-replace it.
     */
    int firstIndexOf(List<String> items, String target) {
        int idx = 0;
        for (String s : items) {
            if (s.equals(target)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }
}
```

**Bad example:**

```java
import java.util.Iterator;
import java.util.List;

final class EscapingComputation {
    /** The Iterator escapes to the caller and cannot be scalar-replaced. */
    Iterator<String> openIterator(List<String> items) {
        return items.iterator();
    }
}
```

### Example 6: Apply the Measurement Loop

Title: Keep only proven optimizations
Description: Always run baseline vs candidate under identical inputs and JIT warmup. For real comparisons prefer a JMH harness; the sketch below shows the shape. 1) Warm up both variants until steady-state 2) Measure the same workload under the same conditions 3) Compare latency, allocation rate, and throughput 4) Keep the candidate only when the gain exceeds noise; otherwise revert

**Good example:**

```java
import java.util.function.IntUnaryOperator;

final class PerformanceExperiment {
    /** Run baseline vs candidate on identical inputs after warmup; only keep proven wins. */
    Verdict optimize(IntUnaryOperator baseline, IntUnaryOperator candidate, int[] data) {
        warmup(baseline, data);
        warmup(candidate, data);
        long beforeNs = timeRun(baseline, data);
        long afterNs = timeRun(candidate, data);
        return new Verdict(beforeNs, afterNs, afterNs < beforeNs);
    }

    private void warmup(IntUnaryOperator fn, int[] data) {
        for (int i = 0; i < 10_000; i++) {
            for (int v : data) {
                fn.applyAsInt(v); // exercise both branches before timing
            }
        }
    }

    private long timeRun(IntUnaryOperator fn, int[] data) {
        long start = System.nanoTime();
        int sink = 0;
        for (int v : data) {
            sink ^= fn.applyAsInt(v); // consume result so JIT cannot DCE it
        }
        if (sink == Integer.MIN_VALUE) System.out.println(sink);
        return System.nanoTime() - start;
    }

    record Verdict(long beforeNs, long afterNs, boolean keep) {}
}
```

**Bad example:**

```java
import java.util.Arrays;

final class PrematureOptimization {
    /** Algorithm rewritten without any before/after measurement; risk > benefit. */
    int optimizeWithoutBaseline(int[] values) {
        return Arrays.stream(values).map(v -> v * v).sum();
    }
}
```

### Example 7: Concurrency with Backpressure

Title: Bound work and prevent overload cascades
Description: Use bounded queues and rejection policies to express load limits directly in Java concurrency code.

**Good example:**

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class BoundedExecutorFactory {
    /** Bounded queue + CallerRunsPolicy applies natural backpressure to producers. */
    static ThreadPoolExecutor create() {
        return new ThreadPoolExecutor(
                4, 8,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(500),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
```

**Bad example:**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class UnboundedSubmitter {
    private final ExecutorService executor = Executors.newFixedThreadPool(8); // unbounded queue

    void submitAll() {
        for (int i = 0; i < 1_000_000; i++) {
            executor.submit(() -> { }); // no bound, no timeout, no rejection signal
        }
    }
}
```

### Example 8: I/O, Parsing, and Serialization

Title: Reduce per-record overhead and unnecessary copies
Description: Prefer explicit parsing logic in hot paths when general-purpose parsing creates avoidable intermediate objects.

**Good example:**

```java
final class LineParser {
    /** Index-based parse + integer fixed-point math: no regex, no String[] allocation. */
    ParsedRow parse(String line) {
        int sep = line.indexOf(';');
        String station = line.substring(0, sep);
        int tempTenths = parseTenths(line, sep + 1, line.length());
        return new ParsedRow(station, tempTenths);
    }

    /** Parses a signed decimal like "-12.3" into tenths (-123) without Double allocation. */
    private static int parseTenths(String s, int from, int to) {
        int sign = 1;
        int i = from;
        if (s.charAt(i) == '-') { sign = -1; i++; }
        int value = 0;
        for (; i < to; i++) {
            char c = s.charAt(i);
            if (c == '.') continue;
            value = value * 10 + (c - '0');
        }
        return sign * value;
    }

    record ParsedRow(String station, int tempTenths) {}
}
```

**Bad example:**

```java
final class SplitParser {
    /** `split(";")` compiles a regex and allocates a String[] per call. */
    ParsedRow parse(String line) {
        String[] parts = line.split(";");
        return new ParsedRow(parts[0], Double.parseDouble(parts[1]));
    }

    record ParsedRow(String station, double temperature) {}
}
```

### Example 9: Avoid Boxed Streams in Tight Hot Paths

Title: Prefer primitive loops when profiling shows stream/boxing overhead
Description: Streams are great for readability off the hot path. In frequently executed numeric code, a primitive `for` loop avoids `Spliterator`/pipeline overhead and any `Integer` boxing introduced by `boxed()` or `Stream<Integer>`.

**Good example:**

```java
import java.util.List;

final class LoopSquares {
    /** Single unbox per element, no Spliterator, no pipeline. */
    long sumSquares(List<Integer> values) {
        long sum = 0;
        for (int v : values) {
            sum += (long) v * v;
        }
        return sum;
    }
}
```

**Bad example:**

```java
import java.util.List;

final class BoxedStreamSquares {
    /** `Stream<Integer>` traversal boxes/unboxes every element. */
    long sumSquares(List<Integer> values) {
        return values.stream()
                .mapToLong(v -> (long) v * v) // unbox via Integer::intValue per element
                .sum();
    }
}
```

### Example 10: Pre-Size Collections When Capacity Is Known

Title: Avoid repeated rehash and array-grow churn
Description: `HashMap` rehashes (and `ArrayList` reallocates) as elements are added past their threshold. When the final size is known or bounded, sizing the collection up front removes those internal allocations.

**Good example:**

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class PreSizedCollections {
    Map<String, Integer> indexBy(List<String> keys) {
        // load factor 0.75 -> capacity = ceil(size / 0.75) + 1
        Map<String, Integer> index = new HashMap<>(keys.size() * 4 / 3 + 1);
        int i = 0;
        for (String k : keys) {
            index.put(k, i++);
        }
        return index;
    }

    List<String> copyOf(List<String> source) {
        List<String> out = new ArrayList<>(source.size());
        out.addAll(source);
        return out;
    }
}
```

**Bad example:**

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GrowingCollections {
    Map<String, Integer> indexBy(List<String> keys) {
        Map<String, Integer> index = new HashMap<>(); // default 16; rehashes as it grows
        int i = 0;
        for (String k : keys) {
            index.put(k, i++);
        }
        return index;
    }

    List<String> copyOf(List<String> source) {
        List<String> out = new ArrayList<>(); // default 10; grows by 1.5x repeatedly
        out.addAll(source);
        return out;
    }
}
```

### Example 11: Compile Regex Patterns Once

Title: Hoist `Pattern.compile` out of hot calls
Description: `String.matches`, `String.split`, and inline `Pattern.compile` re-parse the regex on every call. Cache compiled `Pattern` instances in `static final` fields and reuse `Matcher` per call.

**Good example:**

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CachedRegex {
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    boolean isEmail(String s) {
        Matcher m = EMAIL.matcher(s);
        return m.matches();
    }
}
```

**Bad example:**

```java
final class RecompiledRegex {
    /** `String.matches` compiles the Pattern on every invocation. */
    boolean isEmail(String s) {
        return s.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
```

## Output Format

- **ANALYZE** Java hot paths by category: allocation, boxing, loops and lambdas, data structures, API shape, concurrency pools and queues, parsing and I/O, persistence and cache usage in code
- **PROPOSE** targeted Java refactors with bad/good rationale, scoped to measured or clearly identified bottlenecks
- **IMPLEMENT** minimal, semantics-preserving code changes that reduce allocation and CPU cost on the hot path
- **VALIDATE** with before/after behavior checks and, when available, comparable measurements; summarize impact clearly
- **SUMMARY** list files changed, techniques applied, expected effect, and keep-or-revert recommendation
- **TRADE-OFFS** state complexity cost versus expected gain; flag where readability or API clarity should win

## Safeguards

- **COMPILE CHECK**: Code must compile after each coherent edit set
- **CORRECTNESS FIRST**: Do not change observable behavior without explicit user agreement
- **EVIDENCE**: Prefer measurable or clearly justified hot-path work over speculative micro-optimization
- **SCOPE**: Avoid unrelated refactors; keep changes reviewable and reversible
- **REGRESSION AWARENESS**: Call out risk to callers, threads, and error paths when tightening performance
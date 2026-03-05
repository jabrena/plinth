---
name: 125-java-concurrency
description: Use when you need to apply Java concurrency best practices — including thread safety fundamentals, ExecutorService thread pool management, concurrent design patterns like Producer-Consumer, asynchronous programming with CompletableFuture, immutability and safe publication, deadlock avoidance, virtual threads and structured concurrency, scoped values, backpressure, cancellation discipline, and observability for concurrent systems. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java rules for Concurrency objects

Identify and apply Java concurrency best practices to improve thread safety, scalability, and maintainability by using modern `java.util.concurrent` utilities, virtual threads, and structured concurrency.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any change. If compilation fails, **stop immediately** — compilation failure is a blocking condition that prevents any further processing.

**Core areas:** Thread safety fundamentals (`ConcurrentHashMap`, `AtomicInteger`, `ReentrantLock`, `ReadWriteLock`, Java Memory Model), `ExecutorService` thread pool configuration (sizing, keep-alive, bounded queues, rejection policies, graceful shutdown), Producer-Consumer and Publish-Subscribe concurrent design patterns (`BlockingQueue`), `CompletableFuture` for non-blocking async composition (`thenApply`/`thenCompose`/`exceptionally`/`orTimeout`), immutability and safe publication (`volatile`, static initializers), lock contention and false-sharing performance optimization, virtual threads (`Executors.newVirtualThreadPerTaskExecutor()`) for I/O-bound scalability, `StructuredTaskScope` for lifecycle-scoped task management, `ScopedValue` over `ThreadLocal` for immutable cross-task data, cooperative cancellation and `InterruptedException` discipline, backpressure with bounded queues and `CallerRunsPolicy`, deadlock avoidance via global lock ordering and `tryLock` with timeouts, `ForkJoin`/parallel-stream discipline for CPU-bound work, virtual-thread pinning detection (JFR `VirtualThreadPinned`), thread naming and `UncaughtExceptionHandler` observability, and fit-for-purpose primitives (`LongAdder`, `CopyOnWriteArrayList`, `StampedLock`, `Semaphore`, `CountDownLatch`, `Phaser`).

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed good/bad examples, constraints, and safeguards for each concurrency pattern.

## Reference

For detailed guidance, examples, and constraints, see [references/125-java-concurrency.md](references/125-java-concurrency.md).

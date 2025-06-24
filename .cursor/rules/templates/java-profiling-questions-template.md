# Profiling questions

Make the following questions:

## **Performance Bottlenecks**
- **CPU hotspots**: Identify methods consuming excessive CPU cycles
- **Inefficient algorithms**: Spot O(n²) operations that should be O(n log n)
- **Unnecessary object allocations**: Find code creating millions of temporary objects
- **String concatenation issues**: Detect inefficient string operations in loops

## **Memory-Related Problems**
- **Memory leaks**: Track objects that aren't being garbage collected
- **Excessive heap usage**: Identify which classes/methods allocate the most memory
- **Large object retention**: Find objects staying in memory longer than expected
- **Off-heap memory issues**: Detect native memory leaks in JNI code

## **Concurrency and Threading Issues**
- **Lock contention**: Identify synchronized blocks causing thread blocking
- **Thread pool exhaustion**: Find threads waiting indefinitely
- **Deadlock conditions**: Spot circular wait conditions
- **Context switching overhead**: Detect excessive thread switching

## **Garbage Collection Problems**
- **GC pressure**: Identify code causing frequent GC cycles
- **Long GC pauses**: Find allocation patterns causing stop-the-world events
- **Generational GC issues**: Spot objects promoted to old generation prematurely

## **I/O and Network Bottlenecks**
- **Blocking I/O operations**: Find threads stuck on file/network operations
- **Database connection leaks**: Identify unclosed connections
- **Inefficient serialization**: Spot expensive object serialization/deserialization
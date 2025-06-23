# Spring Boot Memory Leak Demo

This project demonstrates common memory leak patterns in Spring Boot applications and provides solutions to prevent them.

## Memory Leak Patterns Identified

### 1. Growing Collections Memory Leak
**Issue**: The `objects` list in `CocoController` grows indefinitely, accumulating 1,000 large objects per HTTP request.

**Impact**: 
- Each request adds ~1.35MB of memory (1,000 objects × 1,350 bytes each)
- Memory usage grows linearly with request count
- Eventually leads to OutOfMemoryError

**Solution**:
- Implement bounded collections with size limits
- Automatic cleanup of old objects when limits are reached
- Use thread-safe collections for concurrent access

### 2. Thread Pool Resource Leak
**Issue**: Creating new `ExecutorService` instances on every request without proper shutdown.

**Impact**:
- Each request creates 10 new threads
- Threads and associated resources are never released
- Eventually exhausts system thread limits

**Solution**:
- Reuse shared thread pools
- Implement proper shutdown with `@PreDestroy`
- Use try-with-resources or explicit cleanup

### 3. Large Object Allocation
**Issue**: Creating large strings (1,350 characters) repeatedly without reuse.

**Impact**:
- High memory allocation rate
- Increased GC pressure
- Poor performance under load

**Solution**:
- String interning for repeated values
- Object pooling for expensive objects
- Lazy initialization and caching

## Profiling Results Analysis

### Allocation Flamegraph Insights
The allocation flamegraph shows:
- High allocation rates in `CocoController$MyPojo` creation
- Significant `ArrayList` growth operations
- String concatenation overhead
- Jackson serialization allocations

### Memory Leak Detection
The memory leak profile confirms:
- Linear memory growth with request count
- Objects not being garbage collected
- Thread pool accumulation

## Fixed Implementation Features

### 1. Bounded Collections
```java
private static final int MAX_OBJECTS = 10000;
private final List<MyPojo> objects = Collections.synchronizedList(new ArrayList<>());
```

### 2. Shared Thread Pool
```java
private final ExecutorService sharedExecutorService = 
    Executors.newFixedThreadPool(10, new CustomizableThreadFactory("shared-pool-"));
```

### 3. Automatic Cleanup
```java
@PreDestroy
public void cleanup() {
    sharedExecutorService.shutdown();
    objects.clear();
}
```

### 4. Memory Monitoring Endpoints
- `GET /api/v1/objects/count` - Check current object count and memory usage
- `GET /api/v1/objects/clear` - Manually clear objects from memory

## Best Practices for Memory Leak Prevention

### 1. Collection Management
- Use bounded collections with size limits
- Implement cleanup strategies (LRU, TTL)
- Prefer immutable collections when possible
- Use `WeakReference` for cache-like structures

### 2. Thread Pool Management
- Reuse thread pools across requests
- Always implement proper shutdown
- Use Spring's `@Async` with configured thread pools
- Monitor thread pool metrics

### 3. Object Lifecycle
- Implement proper resource cleanup
- Use try-with-resources for closeable resources
- Avoid static collections that grow indefinitely
- Use object pooling for expensive objects

### 4. Monitoring and Alerting
- Monitor heap usage and GC metrics
- Set up alerts for memory usage thresholds
- Use profiling tools regularly
- Implement health checks for resource usage

## Testing Memory Leaks

### Load Testing
```bash
# Run load test
./load-test.sh

# Monitor memory usage
curl http://localhost:8080/api/v1/objects/count
```

### Profiling with async-profiler
```bash
# Start profiling
./profiler/scripts/java-profile.sh

# Generate allocation flamegraph
java -jar async-profiler.jar -e alloc -f profile.html <pid>
```

### JVM Flags for Memory Analysis
```bash
java -XX:+PrintGCDetails \
     -XX:+PrintGCTimeStamps \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/tmp/heapdump.hprof \
     -jar application.jar
```

## Common Memory Leak Patterns to Avoid

1. **Static Collections**: Avoid static lists, maps, or sets that grow over time
2. **Unclosed Resources**: Always close streams, connections, and files
3. **Event Listeners**: Remove listeners when components are destroyed
4. **ThreadLocal Variables**: Clear ThreadLocal after use
5. **Circular References**: Be careful with object graphs that reference each other
6. **Caching Without Limits**: Implement cache eviction policies

## Monitoring Tools

- **VisualVM**: Heap dump analysis and memory profiling
- **JProfiler**: Commercial profiling tool with advanced features
- **async-profiler**: Low-overhead profiling for production
- **Eclipse MAT**: Memory Analyzer Tool for heap dump analysis
- **Micrometer**: Application metrics and monitoring

## Performance Recommendations

1. **Use appropriate data structures** for your use case
2. **Implement proper caching strategies** with eviction policies
3. **Monitor memory usage patterns** in production
4. **Set up automated alerts** for memory thresholds
5. **Regular profiling** in development and staging environments


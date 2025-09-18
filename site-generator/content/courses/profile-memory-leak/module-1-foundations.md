title=Module 1: Memory Leak Foundations and Detection Setup
type=course
status=published
date=2025-09-17
author=MyRobot
version=0.11.0-SNAPSHOT
tags=java, profiling, memory-leak, foundations, detection, setup
~~~~~~

## Understanding Memory Leaks and Building Detection Infrastructure

**⏱️ Duration:** 2 hours
**🎯 Learning Objectives:**
- Understand what memory leaks are and why they're critical in Java applications
- Identify different types of memory leak patterns in enterprise applications
- Explore the Spring Boot memory leak demo architecture
- Set up automated profiling infrastructure using system prompts
- Understand the `coco=true/false` configuration pattern for controlled testing

---

## 🧠 Conceptual Foundation

### What Are Memory Leaks in Java?

**Definition:** A memory leak in Java occurs when objects that are no longer needed by the application remain referenced and cannot be garbage collected, leading to gradual memory exhaustion.

**🔍 Key Insight:** Unlike languages with manual memory management (C/C++), Java memory leaks are typically caused by **logical errors** where references to unused objects are unintentionally retained.

### Types of Memory Leaks in Java Applications

#### 1. **Collection Leaks**
```java
// BAD: Unbounded growth
private final List<RequestData> requests = new ArrayList<>();

public void processRequest(RequestData data) {
    requests.add(data); // Never removed!
}
```

#### 2. **Thread Pool Leaks**
```java
// BAD: ExecutorService never shutdown
public void createTask() {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    executor.submit(() -> doWork());
    // Missing: executor.shutdown()!
}
```

#### 3. **Listener/Callback Leaks**
```java
// BAD: Listeners never removed
public void addListener(EventListener listener) {
    eventListeners.add(listener); // Accumulates forever
}
```

#### 4. **Cache Leaks**
```java
// BAD: No eviction policy
private final Map<String, ExpensiveObject> cache = new HashMap<>();

public ExpensiveObject get(String key) {
    return cache.computeIfAbsent(key, k -> new ExpensiveObject(k));
    // Cache grows indefinitely!
}
```

### 💡 Learning Reinforcement
**Notice how each leak type involves the same pattern: objects being added to collections or caches without corresponding removal logic. This is why systematic profiling is essential - these patterns are often subtle and hard to spot in code reviews!**

---

## 🏗️ Demo Application Architecture

### Understanding the Spring Boot Memory Leak Demo

The `spring-boot-memory-leak-demo` project demonstrates controlled memory leak scenarios through two contrasting controller implementations:

#### **CocoController (coco=true) - The Problematic Implementation**

```java
@RestController
@RequestMapping("/api/v1")
@ConditionalOnProperty(name = "coco", havingValue = "true", matchIfMissing = true)
public class CocoController {

    // LEAK 1: Unbounded list growth
    private final List<MyPojo> objects = new ArrayList<>();

    // LEAK 2: Thread pools created without cleanup
    @PostMapping("/threads/create")
    public ResponseEntity<String> createThread() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        // Missing shutdown logic!
        return ResponseEntity.ok("Thread created");
    }

    // LEAK 3: Unbounded object accumulation
    @PostMapping("/objects/create")
    public ResponseEntity<String> createObject() {
        objects.add(new MyPojo(/* large data */));
        return ResponseEntity.ok("Object created: " + objects.size());
    }
}
```

#### **NoCocoController (coco=false) - The Fixed Implementation**

```java
@RestController
@RequestMapping("/api/v1")
@ConditionalOnProperty(name = "coco", havingValue = "false")
public class NoCocoController {

    private static final int MAX_OBJECTS = 10000; // Bounded!
    private final List<MyPojo> objects = Collections.synchronizedList(new ArrayList<>());

    // Shared, managed thread pool
    private final ExecutorService sharedExecutorService =
        Executors.newFixedThreadPool(10, new CustomizableThreadFactory("shared-pool-"));

    @PostMapping("/objects/create")
    public ResponseEntity<String> createObject() {
        // Bounds checking prevents unbounded growth
        if (objects.size() >= MAX_OBJECTS) {
            return ResponseEntity.badRequest()
                .body("Maximum objects limit reached: " + MAX_OBJECTS);
        }
        objects.add(new MyPojo(/* same data */));
        return ResponseEntity.ok("Object created: " + objects.size());
    }

    // Proper resource cleanup
    @PreDestroy
    public void cleanup() throws InterruptedException {
        sharedExecutorService.shutdown();
        if (!sharedExecutorService.awaitTermination(30, TimeUnit.SECONDS)) {
            sharedExecutorService.shutdownNow();
        }
    }
}
```

### 🔍 Knowledge Check
**Before we continue, can you identify the three main differences between these implementations that prevent memory leaks?**

<details>
<summary>Click to reveal answers</summary>

1. **Bounded Collections**: `MAX_OBJECTS` constant prevents unbounded list growth
2. **Shared Thread Pools**: Single managed `ExecutorService` instead of creating new ones
3. **Resource Cleanup**: `@PreDestroy` method ensures proper shutdown of thread pools

</details>

---

## 🛠️ Setting Up Profiling Infrastructure

### Step 1: Understanding the Configuration Pattern

The demo uses Spring Boot's `@ConditionalOnProperty` to switch between implementations:

```properties
# application.properties (default - enables memory leaks)
coco=true

# application-vt.properties (virtual threads profile - fixes leaks)
coco=false
```

**💡 Learning Insight:** This pattern allows us to test the exact same endpoints with different implementations, providing perfect before/after comparison scenarios for profiling!

### Step 2: Setting Up Detection Scripts using @161-java-profiling-detect

Let's set up the profiling infrastructure using the system prompt:

#### **🎯 Practical Exercise 1: Initial Setup**

1. **Navigate to the demo project:**
```bash
cd ./cursor-rules-java/examples/spring-boot-memory-leak-demo
```

2. **Verify the project structure:**
```bash
ls -la
# You should see: run-java-process-for-profiling.sh, profiler/ directory
```

3. **Check existing profiling setup:**
```bash
ls -la profiler/
# Expected: scripts/, results/, docs/ directories
```

#### **🎯 Practical Exercise 2: Baseline Application Startup**

1. **Start the application with memory leaks enabled (default):**
```bash
./run-java-process-for-profiling.sh --help
```

2. **Verify the application is running:**
```bash
# In another terminal
curl http://localhost:8080/actuator/health
curl http://localhost:8080/swagger-ui/index.html
```

3. **Check which controller is active:**
```bash
curl -X POST http://localhost:8080/api/v1/objects/create
# Should respond with "Object created: 1"
```

#### **🎯 Practical Exercise 3: Initial Profiling Run**

1. **Execute the profiling script:**
```bash
cd profiler/scripts
./profile-java-process.sh --help
```

2. **Follow the interactive prompts:**
   - **Problem Category**: Select "2) Memory-Related Problems"
   - **Process Selection**: Choose your Spring Boot application
   - **Profiling Option**: Select "8. Memory Leak Detection (5min)"

3. **Generate load while profiling:**
```bash
# In another terminal, create load
for i in {1..50}; do
  curl -X POST http://localhost:8080/api/v1/objects/create
  curl -X POST http://localhost:8080/api/v1/threads/create
  sleep 2
done
```

4. **Wait for profiling to complete and examine results:**
```bash
ls -la profiler/results/
# Look for: memory-leak-*.html files
```

### 🔍 Understanding Your First Profiling Results

**Open the generated flamegraph in your browser:**
```bash
# macOS
open profiler/results/memory-leak-*.html

# Linux
xdg-open profiler/results/memory-leak-*.html
```

**What to look for in the flamegraph:**
- **Canvas Height**: Tall canvas indicates many allocation paths
- **Wide Sections**: Methods consuming significant memory
- **Stack Depth**: Deep call stacks may indicate inefficient patterns
- **Color Patterns**: Different colors represent different allocation types

### 💡 Learning Reinforcement
**This flamegraph represents the "before" state with active memory leaks. Notice the patterns - we'll compare this exact visualization after implementing fixes in later modules!**

---

## 📊 Baseline Metrics Collection

### Establishing Performance Baselines

Before we can measure improvements, we need to establish baseline metrics:

#### **🎯 Practical Exercise 4: Comprehensive Baseline Collection**

1. **Generate comprehensive baseline data:**
```bash
cd profiler/scripts
./profile-java-process.sh
```

2. **Select Option 9: Complete Memory Analysis Workflow**
   - This will generate three reports:
     - 30-second baseline
     - 60-second detailed analysis
     - 5-minute leak detection

3. **Create sustained load for realistic metrics:**
```bash
# Run this in background while profiling
./run-jmeter.sh --help
./run-jmeter.sh -t 300 -c 5  # 5 minutes, 5 concurrent users
```

4. **Document your baseline results:**
```bash
ls -la profiler/results/ | grep $(date +%Y%m%d)
# Record the file names and sizes for later comparison
```

### Understanding JMeter Integration

The demo includes JMeter integration for consistent load testing:

```bash
# View the JMeter test plan
cat src/test/resources/jmeter/load-test.jmx
```

**Key JMeter Test Scenarios:**
- **Object Creation Load**: Repeatedly calls `/api/v1/objects/create`
- **Thread Creation Load**: Repeatedly calls `/api/v1/threads/create`
- **Mixed Workload**: Combines both endpoints with realistic timing

### 🔍 Knowledge Check
**Why is consistent load testing crucial for memory leak detection?**

<details>
<summary>Click to reveal answer</summary>

Memory leaks are often only visible under sustained load because:
1. **Accumulation Effect**: Leaks compound over time
2. **GC Masking**: Small leaks may be hidden by normal GC cycles
3. **Real-world Simulation**: Production applications experience continuous load
4. **Comparative Analysis**: Consistent load enables accurate before/after comparisons

</details>

---

## 🎯 Module 1 Assessment

### Hands-on Validation Checklist

**✅ Conceptual Understanding:**
- [ ] Can explain what causes memory leaks in Java applications
- [ ] Identified the three main leak patterns in CocoController
- [ ] Understands the coco=true/false configuration pattern

**✅ Technical Setup:**
- [ ] Successfully started the Spring Boot application
- [ ] Generated initial profiling reports
- [ ] Created baseline flamegraph visualizations
- [ ] Executed JMeter load testing

**✅ Analysis Skills:**
- [ ] Can interpret basic flamegraph patterns
- [ ] Documented baseline metrics for later comparison
- [ ] Understands the relationship between load testing and profiling

### 🎯 Practical Challenge

**Challenge: Custom Load Pattern**
Create a custom load testing script that exercises both problematic endpoints in a pattern that maximizes memory leak detection:

```bash
# Create your custom load script
cat > custom-load-test.sh << 'EOF'
#!/bin/bash
echo "Starting custom memory leak load test..."

# Your implementation here:
# 1. Create objects in bursts
# 2. Create threads periodically
# 3. Monitor memory growth
# 4. Report progress

EOF
chmod +x custom-load-test.sh
```

**Success Criteria:**
- Script runs for at least 5 minutes
- Generates measurable memory growth
- Provides progress feedback
- Creates reproducible load patterns

---

## 🚀 Transition to Module 2

**Congratulations!** You've successfully:
- ✅ Understood the theoretical foundation of Java memory leaks
- ✅ Explored the demo application architecture
- ✅ Set up automated profiling infrastructure
- ✅ Generated baseline profiling data
- ✅ Established consistent load testing patterns

### **What's Next?**
In **Module 2: Hands-on Profiling with System Prompts**, we'll dive deep into:
- Master all 21 profiling options in the interactive script
- Learn advanced flamegraph interpretation techniques
- Understand different profiling strategies for different problem types
- Generate comprehensive evidence for systematic analysis

### 💡 Key Takeaway
**"The foundation of effective performance optimization is systematic measurement. You've now built the infrastructure to measure, analyze, and validate performance improvements scientifically!"**

**Ready to dive deeper into profiling techniques? [Let's continue to Module 2!](module-2-profiling.html) 🎯**

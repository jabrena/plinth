title=Module 5: Performance - Optimization & Profiling
type=course
status=published
date=2025-09-17
author=MyRobot
version=0.11.0-SNAPSHOT
tags=java, performance, profiling, jmeter, async-profiler, jmh, optimization, system-prompts
~~~~~~

## 🎯 Learning Objectives

By the end of this module, you will:

- **Create JMeter performance tests** using `@151-java-performance-jmeter`
- **Profile applications comprehensively** using `@161-java-profiling-detect`
- **Analyze performance bottlenecks** using `@162-java-profiling-analyze`
- **Compare performance improvements** using `@164-java-profiling-compare`
- **Benchmark code with JMH** for micro-optimizations
- **Apply systematic performance optimization** strategies

## 📚 Module Overview

**Duration:** 8 hours
**Difficulty:** Advanced
**Prerequisites:** Module 4 completed, basic performance concepts

This module focuses on systematic performance optimization using AI-powered profiling and testing tools. You'll learn to identify bottlenecks, create comprehensive performance tests, and validate improvements scientifically.

## 🗺️ Learning Path

### **Lesson 5.1: JMeter Performance Testing** (90 minutes)

#### 🎯 **Learning Objectives:**
- Create comprehensive JMeter test plans using `@151-java-performance-jmeter`
- Design realistic load testing scenarios
- Analyze performance metrics and identify bottlenecks

#### 📖 **Core Concepts:**

**Performance Testing Types:**
1. **Load Testing**: Normal expected load
2. **Stress Testing**: Beyond normal capacity
3. **Spike Testing**: Sudden load increases
4. **Volume Testing**: Large amounts of data
5. **Endurance Testing**: Extended periods

#### 🔧 **Hands-on Exercise 5.1:**

**Scenario:** Create performance tests for a Spring Boot REST API.

**Step 1: Setup Test Environment**
```bash
cd examples/spring-boot-jmeter-demo
```

**Step 2: Apply JMeter System Prompt**
Use: `Add JMeter performance testing to this project using @151-java-performance-jmeter`

**Step 3: Create Specific Test Plan**
Try: `Can you create a Jmeter file based on the restcontroller in the path src/test/resources/jmeter/load-test.jmx?`

**Expected JMeter Configuration:**
- Thread groups for different load scenarios
- HTTP request samplers for all endpoints
- Listeners for metrics collection
- Assertions for response validation
- Test data management

#### 💡 **Performance Metrics to Monitor:**
- **Response Time**: Average, median, 95th percentile
- **Throughput**: Requests per second
- **Error Rate**: Percentage of failed requests
- **Resource Utilization**: CPU, memory, I/O

---

### **Lesson 5.2: Application Profiling Setup** (120 minutes)

#### 🎯 **Learning Objectives:**
- Set up comprehensive profiling using `@161-java-profiling-detect`
- Configure JVM for optimal profiling
- Collect runtime performance data

#### 📖 **Core Concepts:**

**Profiling Tools:**
1. **Async Profiler**: Low-overhead sampling profiler
2. **JFR**: Java Flight Recorder
3. **JVM Tools**: jps, jstack, jcmd, jstat
4. **Memory Analysis**: Heap dumps, GC logs

#### 🔧 **Hands-on Exercise 5.2:**

**Scenario:** Profile a memory-leak prone application.

**Step 1: Setup Profiling Environment**
```bash
cd examples/spring-boot-memory-leak-demo
```

**Step 2: Apply Profiling Detection Prompt**
Use: `My Java application has performance issues - help me set up comprehensive profiling process using @161-java-profiling-detect and use the location examples/spring-boot-memory-leak-demo/profiler`

**Expected Generated Scripts:**

**1. run-with-profiler.sh:**
```bash
#!/bin/bash
# Generated script with optimal JVM flags for profiling

JAVA_OPTS="-XX:+UnlockDiagnosticVMOptions"
JAVA_OPTS="$JAVA_OPTS -XX:+DebugNonSafepoints"
JAVA_OPTS="$JAVA_OPTS -XX:+FlightRecorder"
JAVA_OPTS="$JAVA_OPTS -XX:StartFlightRecording=duration=300s,filename=app-profile.jfr"
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx2g"

./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="$JAVA_OPTS"
```

**2. java-profile.sh:**
```bash
#!/bin/bash
# Interactive profiling script

echo "Available Java processes:"
jps -l

read -p "Enter PID to profile: " PID

echo "Profiling options:"
echo "1. CPU profiling"
echo "2. Memory allocation profiling"
echo "3. Lock contention profiling"
echo "4. Full profiling (CPU + allocations)"

read -p "Select option: " OPTION

case $OPTION in
    1) java -jar async-profiler.jar -e cpu -d 60 -f cpu-profile.html $PID ;;
    2) java -jar async-profiler.jar -e alloc -d 60 -f alloc-profile.html $PID ;;
    3) java -jar async-profiler.jar -e lock -d 60 -f lock-profile.html $PID ;;
    4) java -jar async-profiler.jar -e cpu,alloc -d 60 -f full-profile.html $PID ;;
esac
```

**Step 3: Execute Profiling Workflow**
```bash
# Step 1: Run application with profiling flags
./run-with-profiler.sh --help

# Step 2: Start load testing
./run-jmeter.sh --help

# Step 3: Collect profiling data
./profiler/scripts/java-profile.sh
```

---

### **Lesson 5.3: Performance Analysis** (150 minutes)

#### 🎯 **Learning Objectives:**
- Analyze profiling results using `@162-java-profiling-analyze`
- Identify performance bottlenecks systematically
- Generate actionable optimization recommendations

#### 📖 **Core Concepts:**

**Analysis Focus Areas:**
1. **CPU Hotspots**: Methods consuming most CPU time
2. **Memory Allocation**: Objects causing GC pressure
3. **I/O Bottlenecks**: Database and network operations
4. **Lock Contention**: Thread synchronization issues
5. **GC Impact**: Garbage collection overhead

#### 🔧 **Hands-on Exercise 5.3:**

**Step 1: Analyze Collected Data**
Use: `Analyze the results located in examples/spring-boot-memory-leak-demo/profiler and use the cursor rule @162-java-profiling-analyze`

**Expected Analysis Report:**

```markdown
# Performance Analysis Report - 2025-01-XX

## Executive Summary
- **Application**: Spring Boot Memory Leak Demo
- **Profiling Duration**: 5 minutes under load
- **Key Finding**: Memory leak in UserService causing 200MB/minute growth

## CPU Analysis
### Top Hotspots
1. `UserService.processUser()` - 45% CPU time
   - **Issue**: Inefficient string concatenation in loop
   - **Recommendation**: Use StringBuilder or String.format()

2. `DatabaseRepository.findAll()` - 23% CPU time
   - **Issue**: N+1 query problem
   - **Recommendation**: Implement batch fetching

### Method-Level Analysis
```java
// BEFORE (45% CPU time)
public String processUsers(List<User> users) {
    String result = "";
    for (User user : users) {
        result += user.getName() + ", "; // String concatenation in loop
    }
    return result;
}

// RECOMMENDED OPTIMIZATION
public String processUsers(List<User> users) {
    return users.stream()
        .map(User::getName)
        .collect(Collectors.joining(", "));
}
```

## Memory Analysis
### Allocation Hotspots
1. `String` objects - 67% of allocations
   - **Source**: String concatenation in processUsers()
   - **Impact**: 150MB/minute allocation rate
   - **Solution**: Use efficient string building

2. `ArrayList` growth - 18% of allocations
   - **Source**: Dynamic list resizing
   - **Solution**: Pre-size collections when possible

### Memory Leak Detection
- **Leak Source**: `UserCache.cache` HashMap growing unboundedly
- **Growth Rate**: 200MB/minute
- **Root Cause**: No cache eviction policy
- **Fix**: Implement LRU cache with size limits

## I/O Analysis
### Database Performance
- **Query Count**: 2,450 queries in 5 minutes
- **Slowest Query**: `SELECT * FROM users WHERE status = ?` (avg 45ms)
- **N+1 Problem**: UserService.loadUserDetails() method
- **Recommendation**: Use @EntityGraph or batch loading

## Recommendations Priority
1. **HIGH**: Fix memory leak in UserCache (immediate)
2. **HIGH**: Optimize string concatenation in processUsers()
3. **MEDIUM**: Implement batch database loading
4. **LOW**: Pre-size ArrayList collections

## Next Steps
1. Apply recommended code changes
2. Re-run profiling to validate improvements
3. Monitor production metrics for 24 hours
```

---

### **Lesson 5.4: JMH Micro-Benchmarking** (90 minutes)

#### 🎯 **Learning Objectives:**
- Create JMH benchmarks for micro-optimizations
- Analyze benchmark results scientifically
- Apply JMH best practices

#### 🔧 **Hands-on Exercise 5.4:**

**Scenario:** Benchmark different string concatenation approaches.

**Step 1: Add JMH Support**
Use: `Add JMH support using the cursor rule @112-java-maven-plugins and not make any question`

**Step 2: Create Benchmark**
Use: `Can you create a JMH benchmark in order to know what is the best implementation?`

**Expected JMH Benchmark:**

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class StringConcatenationBenchmark {

    @Param({"10", "100", "1000"})
    private int size;

    private List<String> strings;

    @Setup
    public void setup() {
        strings = IntStream.range(0, size)
            .mapToObj(i -> "String" + i)
            .collect(Collectors.toList());
    }

    @Benchmark
    public String concatenationWithPlus() {
        String result = "";
        for (String s : strings) {
            result += s;
        }
        return result;
    }

    @Benchmark
    public String concatenationWithStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Benchmark
    public String concatenationWithStringJoiner() {
        StringJoiner joiner = new StringJoiner("");
        for (String s : strings) {
            joiner.add(s);
        }
        return joiner.toString();
    }

    @Benchmark
    public String concatenationWithStreams() {
        return strings.stream()
            .collect(Collectors.joining(""));
    }
}
```

**Step 3: Analyze Results**
After running the benchmark, use: `Can you explain the JMH results and advice about the best implementation?`

---

### **Lesson 5.5: Performance Comparison** (60 minutes)

#### 🎯 **Learning Objectives:**
- Compare before/after performance using `@164-java-profiling-compare`
- Validate optimization effectiveness
- Create performance improvement reports

#### 🔧 **Hands-on Exercise 5.5:**

**Step 1: Apply Optimizations**
Implement the recommended changes from the analysis report.

**Step 2: Re-run Profiling**
Collect new performance data with the same load conditions.

**Step 3: Compare Results**
Use: `Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule @164-java-profiling-compare`

**Expected Comparison Report:**
```markdown
# Performance Improvement Validation Report

## Summary
- **Memory leak**: FIXED ✅ (0MB/minute growth vs. 200MB/minute before)
- **CPU optimization**: 67% improvement ✅ (processUsers() now 15% vs. 45% CPU time)
- **Database queries**: 45% reduction ✅ (1,350 queries vs. 2,450 before)

## Detailed Comparison

### Memory Usage
| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Heap Growth Rate | 200MB/min | 0MB/min | 100% ✅ |
| GC Frequency | 12/min | 3/min | 75% ✅ |
| Max Heap Usage | 1.8GB | 512MB | 71% ✅ |

### CPU Performance
| Method | Before CPU% | After CPU% | Improvement |
|--------|-------------|------------|-------------|
| processUsers() | 45% | 15% | 67% ✅ |
| findAll() | 23% | 12% | 48% ✅ |

## Recommendations
✅ All high-priority issues resolved
✅ Performance targets met
✅ Ready for production deployment
```

---

## 🏆 Module Assessment

### **Practical Assessment Project**

**Project: "E-Commerce Performance Optimization"**

**Scenario:** Optimize a slow e-commerce application using systematic profiling.

**Requirements:**
1. Create comprehensive JMeter test plans
2. Set up profiling infrastructure
3. Identify and analyze performance bottlenecks
4. Implement optimizations
5. Validate improvements with comparison reports

**Deliverables:**
- JMeter test suite covering all scenarios
- Profiling setup scripts and configuration
- Performance analysis reports with recommendations
- Optimized code implementation
- Before/after comparison validation

**Success Criteria:**
- 50% reduction in response times
- Memory leak elimination
- 90% reduction in database queries
- Comprehensive profiling documentation

---

## 🚀 Next Steps

**Outstanding Achievement!** You've mastered systematic performance optimization and profiling.

**What You've Accomplished:**
- ✅ Created comprehensive performance tests
- ✅ Set up professional profiling infrastructure
- ✅ Analyzed and fixed performance bottlenecks
- ✅ Validated improvements scientifically

**Ready for documentation mastery?**

👉 **[Continue to Module 6: Documentation →](module-6-documentation.html)**

---

*Master professional documentation and diagram generation in the next module.*

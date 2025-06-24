# Detecting Garbage Collection Problems - Senior SRE Guide

## Executive Summary

As a senior SRE, detecting Garbage Collection (GC) problems requires understanding the relationship between memory allocation patterns, GC behavior, and application performance. This guide focuses specifically on using async_profiler documents to identify and resolve GC-related issues that can cause production outages.

## Critical GC Problem Indicators

### 1. **Application Symptoms**
- Response time spikes correlating with GC events
- Periodic application "freezes" or high latency
- OutOfMemoryError despite available heap space
- CPU spikes with no corresponding application load
- Throughput degradation over time

### 2. **JVM Symptoms**
- GC pause times > 100ms (for low-latency apps) or > 1s (for batch apps)
- GC frequency increasing over time
- Memory usage sawtooth pattern with increasing baseline
- Old generation filling faster than expected
- Full GC events occurring frequently

## Essential async_profiler Documents for GC Analysis

### **Primary Documents Required**

#### **1. JFR Files (`profile-*.jfr`) - CRITICAL**
**Purpose**: Complete runtime analysis including GC events, object lifecycle, and memory pressure
**Analysis**: Use with JDK Mission Control (JMC), VisualVM, or Eclipse MAT
**Key GC Metrics**:
- GC pause duration and frequency
- Heap utilization patterns
- Object promotion rates from young to old generation
- GC algorithm efficiency

```bash
# Generate JFR with extended GC data
./profiler/scripts/java-profile.sh
# Select option 5: Interactive Heatmap (generates JFR)
# Or generate manually:
$PROFILER_DIR/current/bin/asprof -d 300 -o jfr --jfropts gc,heap,exception -f results/gc-analysis-$(date +%Y%m%d-%H%M%S).jfr $PID
```

#### **2. Memory Allocation Flamegraph (`allocation-flamegraph-*.html`) - CRITICAL**
**Purpose**: Identifies allocation hotspots causing GC pressure
**GC Analysis Focus**:
- High-frequency object allocation paths
- Large object allocations bypassing young generation
- Allocation patterns that stress specific GC regions

**Key Indicators**:
- Wide flamegraph sections indicating massive allocations
- `ArrayList.grow()` or `HashMap.resize()` dominance
- String concatenation in loops
- Temporary object creation in hot paths

#### **3. Memory Leak Detection Profile (`memory-leak-*.html`) - ESSENTIAL**
**Purpose**: Extended profiling to identify objects surviving multiple GC cycles
**GC Relevance**:
- Objects accumulating in old generation
- Memory leaks causing full GC pressure
- Long-lived objects preventing efficient collection

**Analysis Duration**: Minimum 5-10 minutes to observe multiple GC cycles

#### **4. Heatmap Visualization (`heatmap-cpu-*.html`) - SUPPORTING**
**Purpose**: Correlates GC events with application performance over time
**GC Analysis**:
- Performance drops correlating with GC timing
- Patterns of periodic slowdowns
- CPU usage spikes during GC events

### **Secondary Documents**

#### **5. Inverted Flamegraph (`inverted-flamegraph-*.html`)**
**Purpose**: Bottom-up analysis to identify root causes of allocation pressure
**Use Case**: Trace back from GC-triggering allocations to their source

#### **6. Wall Clock Profiling (`wall-flamegraph-*.html`)**
**Purpose**: Captures time spent in GC pauses
**GC Visibility**: Shows actual time lost to garbage collection

## GC Problem Detection Methodology

### **Phase 1: Baseline GC Behavior (Production-Safe)**
```bash
# 1. Generate 5-minute JFR recording during normal load
$PROFILER_DIR/current/bin/asprof -d 300 -o jfr -f baseline-gc-$(date +%Y%m%d-%H%M%S).jfr $PID

# 2. Capture allocation patterns for 60 seconds
$PROFILER_DIR/current/bin/asprof -e alloc -d 60 -f baseline-alloc-$(date +%Y%m%d-%H%M%S).html $PID
```

### **Phase 2: Load Testing + GC Analysis**
```bash
# During load test, capture extended profiling
$PROFILER_DIR/current/bin/asprof -d 600 -o jfr --jfropts gc,heap,allocation,exception -f load-test-gc-$(date +%Y%m%d-%H%M%S).jfr $PID
```

### **Phase 3: Memory Leak + GC Impact Analysis**
```bash
# 10-minute memory leak detection to see GC inefficiency
$PROFILER_DIR/current/bin/asprof -e alloc -d 600 -f gc-leak-analysis-$(date +%Y%m%d-%H%M%S).html $PID
```

## Critical GC Metrics from async_profiler Documents

### **JFR Analysis (using JMC or similar tools)**

#### **Garbage Collection Metrics**
```yaml
critical_gc_metrics:
  # Pause Times
  - young_gc_pause_p99 > 50ms     # For low-latency apps
  - old_gc_pause_p99 > 200ms      # For standard apps
  - full_gc_pause_p99 > 1000ms    # Critical threshold
  
  # Frequency
  - young_gc_frequency > 10/sec   # Excessive young GC
  - old_gc_frequency > 1/min      # Too frequent old GC
  - full_gc_frequency > 1/hour    # Full GC should be rare
  
  # Memory Efficiency
  - heap_utilization_post_gc > 80% # Poor GC efficiency
  - promotion_rate > 100MB/sec     # Excessive promotion
  - allocation_rate > 1GB/sec      # Allocation pressure
```

#### **Object Lifecycle Metrics**
```yaml
object_metrics:
  - average_object_age < 2_gc_cycles      # Objects dying too young
  - object_promotion_rate_increase > 50%  # Increasing promotion
  - large_object_allocations > 1MB       # Bypass young gen
  - finalizer_queue_length > 1000        # Finalization pressure
```

### **Allocation Flamegraph Analysis**

#### **GC Pressure Patterns**
Look for these allocation patterns that stress GC:

1. **Excessive Temporary Objects**
```java
// PROBLEM: Creates garbage in hot path
public String processData(List<String> data) {
    String result = "";
    for (String item : data) {
        result += item + ",";  // Creates new String each iteration
    }
    return result;
}
```

2. **Large Array Allocations**
```java
// PROBLEM: Large arrays bypass young generation
byte[] largeBuffer = new byte[10 * 1024 * 1024]; // 10MB - goes directly to old gen
```

3. **Collection Resizing**
```java
// PROBLEM: Frequent resizing causes allocation spikes
List<Object> list = new ArrayList<>(); // Default size 10
for (int i = 0; i < 10000; i++) {
    list.add(new Object()); // Triggers multiple resizes
}
```

## Production GC Monitoring Setup

### **JVM GC Flags for Visibility**
```bash
# For G1GC (recommended for low-latency)
-XX:+UseG1GC 
-XX:MaxGCPauseMillis=100
-XX:+PrintGC
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintGCApplicationStoppedTime
-Xloggc:gc.log
-XX:+UseGCLogFileRotation
-XX:NumberOfGCLogFiles=5
-XX:GCLogFileSize=100M

# For ZGC (ultra-low latency)
-XX:+UseZGC
-XX:+UnlockExperimentalVMOptions
```

### **Automated GC Problem Detection**
```bash
#!/bin/bash
# gc-monitor.sh - Automated GC problem detection

PID=$(jps | grep YourApp | cut -d' ' -f1)
PROFILER_DIR="/path/to/profiler"

# Check GC pause times every hour
while true; do
    echo "$(date): Starting GC monitoring for PID $PID"
    
    # 5-minute JFR capture
    $PROFILER_DIR/current/bin/asprof -d 300 -o jfr \
        -f "results/hourly-gc-$(date +%Y%m%d-%H%M%S).jfr" $PID
    
    # Analyze GC pauses (example using custom script)
    # gc_pause_max=$(analyze_jfr_gc.sh "results/hourly-gc-$(date +%Y%m%d-%H%M%S).jfr")
    
    # Alert if GC pauses exceed threshold
    # if [ $gc_pause_max -gt 200 ]; then
    #     send_alert "GC pause exceeded 200ms: ${gc_pause_max}ms"
    # fi
    
    sleep 3600  # Wait 1 hour
done
```

### **Critical GC Alerts for Production**
```yaml
gc_alerts:
  # Pause Time Alerts
  - name: "GC Pause Time Critical"
    condition: "gc_pause_time_p99 > 500ms"
    severity: "critical"
    action: "immediate_investigation"
  
  - name: "GC Pause Time Warning"
    condition: "gc_pause_time_p95 > 100ms"
    severity: "warning"
    action: "schedule_analysis"
  
  # Frequency Alerts
  - name: "Full GC Frequency High"
    condition: "full_gc_count > 1 per hour"
    severity: "critical"
    action: "memory_leak_investigation"
  
  - name: "Young GC Frequency High"
    condition: "young_gc_frequency > 20 per minute"
    severity: "warning"
    action: "allocation_analysis_needed"
  
  # Memory Efficiency Alerts
  - name: "Poor GC Efficiency"
    condition: "heap_usage_post_gc > 90%"
    severity: "critical"
    action: "heap_dump_analysis"
  
  - name: "High Promotion Rate"
    condition: "promotion_rate > 200MB/sec"
    severity: "warning"
    action: "object_lifecycle_analysis"
```

## GC Problem Resolution Strategy

### **1. Allocation Rate Optimization**
Based on allocation flamegraph analysis:
- Optimize hot allocation paths
- Implement object pooling for frequent allocations
- Use primitive collections for better memory efficiency
- Reduce temporary object creation

### **2. GC Algorithm Tuning**
```bash
# For high-throughput applications
-XX:+UseG1GC -XX:MaxGCPauseMillis=200

# For low-latency applications
-XX:+UseZGC  # Sub-10ms pauses

# For high-allocation applications
-XX:+UseParallelGC -XX:+UseParallelOldGC
```

### **3. Heap Sizing Optimization**
```bash
# Based on allocation rate and GC frequency analysis
-Xms8g -Xmx8g  # Fixed heap size prevents resizing overhead
-XX:NewRatio=2  # Young:Old = 1:2 ratio
-XX:MaxMetaspaceSize=512m  # Prevent metaspace GC issues
```

### **4. Memory Leak Resolution**
From memory leak profile analysis:
- Identify objects surviving multiple GC cycles
- Fix unbounded collections
- Implement proper resource cleanup
- Add memory monitoring endpoints

## Documentation Integration

This GC analysis complements existing memory leak analysis by:
- Focusing specifically on GC performance impact
- Providing production-ready monitoring setup
- Integrating with existing profiling workflows
- Adding GC-specific alerting strategies

### **Related Documents**
- `memory-leak-analysis.md` - For object retention issues
- `cursor_detecting_memory_related_problem.md` - For general memory problems
- `concurrency-threading-analysis-sre-guide.md` - For thread-related GC pressure

## Key Takeaways for SREs

1. **JFR files are critical** - They contain complete GC event data
2. **Allocation flamegraphs reveal GC pressure sources** - Focus on wide sections
3. **Memory leak profiles show GC inefficiency** - Objects surviving collection
4. **Heatmaps correlate GC events with performance** - Timing is everything
5. **Automated monitoring is essential** - Don't wait for user complaints

### **Emergency GC Problem Response**
```bash
# Quick GC analysis during production incident
PID=$(jps | grep YourApp | cut -d' ' -f1)

# 2-minute emergency profiling
$PROFILER_DIR/current/bin/asprof -d 120 -o jfr -f emergency-gc-$(date +%Y%m%d-%H%M%S).jfr $PID
$PROFILER_DIR/current/bin/asprof -e alloc -d 60 -f emergency-alloc-$(date +%Y%m%d-%H%M%S).html $PID

# Immediate heap dump for memory analysis
jcmd $PID GC.run_finalization
jcmd $PID VM.gc
jmap -dump:format=b,file=emergency-heap-$(date +%Y%m%d-%H%M%S).hprof $PID
```

This comprehensive approach ensures you can quickly identify, analyze, and resolve GC problems in production Java applications using async_profiler's rich dataset. 
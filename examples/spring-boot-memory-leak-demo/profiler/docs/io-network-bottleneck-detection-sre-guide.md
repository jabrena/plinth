# I/O and Network Bottleneck Detection - Senior SRE Guide

## Table of Contents
1. [Overview](#overview)
2. [Required Async-Profiler Documents](#required-async-profiler-documents)
3. [Profiling Configuration for I/O Detection](#profiling-configuration-for-io-detection)
4. [Analysis Techniques](#analysis-techniques)
5. [Common I/O Bottleneck Patterns](#common-io-bottleneck-patterns)
6. [Network Bottleneck Detection](#network-bottleneck-detection)
7. [JFR Analysis for I/O Events](#jfr-analysis-for-io-events)
8. [Performance Baselines and Thresholds](#performance-baselines-and-thresholds)
9. [Actionable Remediation Strategies](#actionable-remediation-strategies)

## Overview

As a Senior SRE, detecting I/O and Network bottlenecks requires a systematic approach combining multiple profiling techniques. This guide focuses on leveraging async_profiler's advanced capabilities to identify, analyze, and resolve I/O performance issues in production Java applications.

### Key SRE Principles for I/O Analysis
- **Measure First**: Always establish baseline performance before optimization
- **Context Matters**: I/O bottlenecks often manifest differently under varying load conditions
- **End-to-End View**: Consider the entire request path from application to storage/network
- **Production Safety**: Use non-intrusive profiling methods that won't impact production performance

## Required Async-Profiler Documents

### 1. Wall Clock Profiling (Primary Document)
```bash
# Essential for I/O bottleneck detection
./profiler/scripts/java-profile.sh
# Select option: Wall Clock Profiling (30s)
```

**Purpose**: Captures threads waiting for I/O operations, blocked on network calls, or sleeping
**Output**: `wall-clock-flamegraph-{timestamp}.html`
**Why Critical**: Unlike CPU profiling, wall clock profiling shows time spent waiting, which is where I/O bottlenecks appear

### 2. JFR (Java Flight Recorder) with I/O Events
```bash
# Extended JFR recording with I/O events
java -XX:+FlightRecorder 
     -XX:StartFlightRecording=duration=300s,filename=io-analysis.jfr,settings=profile
     -XX:FlightRecorderOptions=settings=profile,disk=true
```

**Purpose**: Detailed I/O event tracking including file operations, socket operations, and network latency
**Output**: `profile-{timestamp}.jfr`
**Analysis Tools**: JProfiler, VisualVM, or jfr command-line tool

### 3. Lock Contention Profiling
```bash
# Identifies synchronization issues that can mask I/O problems
./profiler/scripts/java-profile.sh
# Select option: Lock Contention Profiling (30s)
```

**Purpose**: Detects thread contention that might be caused by I/O operations holding locks
**Output**: `lock-contention-flamegraph-{timestamp}.html`

### 4. CPU Profiling (Comparative Analysis)
```bash
# Compare CPU usage patterns during I/O operations
./profiler/scripts/java-profile.sh
# Select option: CPU Profiling (30s)
```

**Purpose**: Establish baseline CPU usage to differentiate between CPU-bound and I/O-bound bottlenecks
**Output**: `cpu-flamegraph-{timestamp}.html`

### 5. Memory Allocation Profiling
```bash
# Track memory patterns related to I/O operations
./profiler/scripts/java-profile.sh
# Select option: Memory Allocation Profiling (30s)
```

**Purpose**: Identify excessive object allocation during I/O operations (e.g., buffer management issues)
**Output**: `allocation-flamegraph-{timestamp}.html`

## Profiling Configuration for I/O Detection

### Enhanced Profiling Script Configuration

Create an I/O-specific profiling configuration:

```bash
#!/bin/bash
# io-profiling-enhanced.sh

PROFILER_DIR="./profiler/current"
RESULTS_DIR="./profiler/results"
PID=$1

# 1. Wall Clock with I/O focus (5 minutes for comprehensive capture)
echo "Starting Wall Clock profiling with I/O focus..."
$PROFILER_DIR/bin/asprof -e wall -d 300 -f "$RESULTS_DIR/io-wall-clock-$(date +%Y%m%d-%H%M%S).html" $PID

# 2. JFR with comprehensive I/O events
echo "Starting JFR with I/O events..."
$PROFILER_DIR/bin/asprof -e jfr -d 300 -f "$RESULTS_DIR/io-jfr-$(date +%Y%m%d-%H%M%S).jfr" $PID

# 3. Lock profiling to identify I/O-related contention
echo "Starting Lock contention profiling..."
$PROFILER_DIR/bin/asprof -e lock -d 180 -f "$RESULTS_DIR/io-locks-$(date +%Y%m%d-%H%M%S).html" $PID

# 4. Allocation profiling for I/O buffer analysis
echo "Starting Allocation profiling..."
$PROFILER_DIR/bin/asprof -e alloc -d 180 -f "$RESULTS_DIR/io-alloc-$(date +%Y%m%d-%H%M%S).html" $PID
```

### JVM Flags for Enhanced I/O Monitoring

```bash
# Add these JVM flags for better I/O observability
-XX:+UnlockDiagnosticVMOptions
-XX:+DebugNonSafepoints
-XX:+FlightRecorder
-XX:+UnlockCommercialFeatures  # For older JVMs
-Djava.net.preferIPv4Stack=true
-Djava.awt.headless=true
-XX:FlightRecorderOptions=disk=true,maxchunksize=10M
```

## Analysis Techniques

### 1. Wall Clock Flamegraph Analysis

#### Identifying I/O Bottlenecks in Wall Clock Profiles

**Look for these patterns in the flamegraph:**

```
Key Stack Patterns to Search:

java.net.SocketInputStream.read
java.net.SocketOutputStream.write
java.nio.channels.SocketChannel.read
java.nio.channels.SocketChannel.write
java.io.FileInputStream.read
java.io.FileOutputStream.write
sun.nio.ch.EPollSelectorImpl.doSelect
java.util.concurrent.ThreadPoolExecutor.getTask
java.lang.Object.wait
java.util.concurrent.locks.LockSupport.park
```

#### Analysis Metrics:
- **Width of I/O frames**: Indicates time spent in I/O operations
- **Frequency of I/O calls**: Shown by stack depth and repetition
- **Waiting patterns**: Look for `wait`, `park`, `select` operations

### 2. JFR Analysis for Detailed I/O Metrics

#### Essential JFR Events for I/O Analysis

```bash
# Extract I/O events from JFR file
jfr print --events jdk.FileRead,jdk.FileWrite,jdk.SocketRead,jdk.SocketWrite io-analysis.jfr

# Extract thread parking events (indicates waiting)
jfr print --events jdk.ThreadPark io-analysis.jfr

# Extract network-related events
jfr print --events jdk.SocketRead,jdk.SocketWrite --json io-analysis.jfr
```

#### Key Metrics to Extract:
- **I/O Duration**: Time spent in individual I/O operations
- **I/O Frequency**: Number of I/O operations per second
- **Buffer Sizes**: Size of data being read/written
- **Thread Parking**: Time threads spend waiting

### 3. Correlation Analysis

#### Multi-Document Analysis Strategy

1. **Baseline Comparison**:
   ```bash
   # During low load
   Low-load CPU usage: X%
   Low-load wall-clock: Y seconds in I/O
   
   # During high load  
   High-load CPU usage: X%  (should be similar if I/O bound)
   High-load wall-clock: Z seconds in I/O (Z >> Y indicates I/O bottleneck)
   ```

2. **Pattern Recognition**:
   - CPU flat while wall-clock increases = I/O bottleneck
   - High lock contention + I/O operations = Resource contention
   - Excessive allocation during I/O = Buffer management issues

## Common I/O Bottleneck Patterns

### 1. Database Connection Pool Exhaustion

**Flamegraph Signatures:**
```
javax.sql.DataSource.getConnection
  ↳ java.lang.Object.wait
  ↳ java.util.concurrent.ThreadPoolExecutor.getTask
```

**JFR Evidence:**
- High `jdk.ThreadPark` events in database connection methods
- Long durations for `getConnection()` calls

**SRE Action Items:**
- Increase connection pool size
- Implement connection pool monitoring
- Review query performance and transaction scope

### 2. Synchronous HTTP Client Bottlenecks

**Flamegraph Signatures:**
```
org.springframework.web.client.RestTemplate.exchange
  ↳ java.net.SocketInputStream.read
  ↳ sun.nio.ch.SocketChannelImpl.read
```

**Metrics to Monitor:**
- Time spent in `SocketInputStream.read`
- Frequency of HTTP client calls
- Thread pool utilization for HTTP clients

**SRE Solutions:**
- Implement async HTTP clients (WebClient)
- Configure appropriate timeouts
- Use circuit breakers for external dependencies

### 3. File I/O Bottlenecks

**Flamegraph Signatures:**
```
java.io.FileInputStream.read
java.io.FileOutputStream.write
java.nio.channels.FileChannel.read
```

**Analysis Focus:**
- File operation frequency and duration
- Buffer sizes being used
- Disk I/O wait times

## Network Bottleneck Detection

### 1. Network Latency Analysis

#### Using Wall Clock Profiling for Network Detection

**Key Indicators:**
```bash
# Search in flamegraph for:
- java.net.Socket* operations with wide frames
- DNS resolution delays (java.net.InetAddress.*)
- SSL handshake delays (javax.net.ssl.*)
```

#### JFR Network Metrics

```bash
# Extract network-specific events
jfr print --events jdk.SocketRead,jdk.SocketWrite,jdk.SecurityPropertyModification io-analysis.jfr | \
grep -E "(duration|bytesRead|bytesWritten)"
```

### 2. Connection Pool Analysis

**Profiling Strategy:**
1. Monitor connection acquisition time
2. Track connection usage patterns
3. Identify connection leaks

**Flamegraph Analysis:**
```
Look for wide frames in:
- Connection pool acquisition methods
- Connection timeout handling
- Connection validation queries
```

## JFR Analysis for I/O Events

### Essential JFR Commands for I/O Analysis

```bash
# 1. General I/O overview
jfr print --events jdk.FileRead,jdk.FileWrite,jdk.SocketRead,jdk.SocketWrite \
          --json io-analysis.jfr > io-events.json

# 2. Thread analysis for I/O waiting
jfr print --events jdk.ThreadPark,jdk.JavaMonitorWait \
          --json io-analysis.jfr > waiting-events.json

# 3. Memory allocation during I/O
jfr print --events jdk.ObjectAllocationInNewTLAB,jdk.ObjectAllocationOutsideTLAB \
          --json io-analysis.jfr > allocation-events.json

# 4. GC impact on I/O operations
jfr print --events jdk.GarbageCollection,jdk.GCPhasePause \
          --json io-analysis.jfr > gc-events.json
```

### Creating I/O Performance Dashboards

```bash
# Script to extract key I/O metrics from JFR
#!/bin/bash
JFR_FILE=$1

echo "=== I/O Performance Summary ==="
echo "File Operations:"
jfr print --events jdk.FileRead,jdk.FileWrite $JFR_FILE | \
awk '/duration/ {sum+=$NF; count++} END {print "Average duration:", sum/count "ms", "Count:", count}'

echo "Network Operations:"
jfr print --events jdk.SocketRead,jdk.SocketWrite $JFR_FILE | \
awk '/duration/ {sum+=$NF; count++} END {print "Average duration:", sum/count "ms", "Count:", count}'

echo "Thread Parking (Waiting):"
jfr print --events jdk.ThreadPark $JFR_FILE | \
awk '/duration/ {sum+=$NF; count++} END {print "Total wait time:", sum "ms", "Count:", count}'
```

## Performance Baselines and Thresholds

### I/O Performance SLIs (Service Level Indicators)

```yaml
# I/O Performance Thresholds
database_connection_acquisition:
  target: < 10ms
  warning: > 50ms
  critical: > 100ms

http_client_response_time:
  target: < 100ms
  warning: > 500ms
  critical: > 1000ms

file_io_operations:
  target: < 5ms
  warning: > 20ms
  critical: > 50ms

thread_waiting_percentage:
  target: < 5%
  warning: > 15%
  critical: > 25%
```

### Establishing Baselines

#### 1. Baseline Profiling Script

```bash
#!/bin/bash
# baseline-io-profiling.sh

echo "Establishing I/O performance baseline..."

# Low load profiling (5 minutes)
echo "Phase 1: Low load baseline"
./profiler/scripts/java-profile.sh
# Select Wall Clock profiling

# Medium load profiling (5 minutes) 
echo "Phase 2: Medium load baseline"
# Run your load test at 50% capacity
./profiler/scripts/java-profile.sh

# High load profiling (5 minutes)
echo "Phase 3: High load baseline" 
# Run your load test at 80% capacity
./profiler/scripts/java-profile.sh

echo "Baseline establishment complete. Analyze results in profiler/results/"
```

## Actionable Remediation Strategies

### 1. Immediate Actions (< 1 hour)

**Database I/O Issues:**
```bash
# Increase connection pool size
server.datasource.hikari.maximum-pool-size=50
server.datasource.hikari.minimum-idle=10

# Optimize connection validation
server.datasource.hikari.validation-timeout=3000
server.datasource.hikari.connection-test-query=SELECT 1
```

**HTTP Client Issues:**
```java
// Configure RestTemplate with proper timeouts
RestTemplate restTemplate = new RestTemplate();
HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
factory.setConnectTimeout(5000);
factory.setReadTimeout(10000);
restTemplate.setRequestFactory(factory);
```

### 2. Short-term Actions (< 1 day)

**Async Processing Implementation:**
```java
// Replace synchronous calls with async
@Async
public CompletableFuture<String> processAsync() {
    // I/O operations here
    return CompletableFuture.completedFuture(result);
}
```

**Connection Pool Tuning:**
```bash
# Fine-tune based on profiling results
server.datasource.hikari.max-lifetime=600000
server.datasource.hikari.idle-timeout=300000
server.datasource.hikari.leak-detection-threshold=60000
```

### 3. Long-term Actions (< 1 week)

**Architecture Changes:**
- Implement caching layers (Redis, Hazelcast)
- Add circuit breakers for external dependencies
- Implement reactive programming patterns
- Consider connection pooling at the application level

**Monitoring Enhancements:**
- Set up continuous I/O performance monitoring
- Implement alerting for I/O performance degradation
- Create I/O performance dashboards

### 4. Continuous Improvement

**Automated Profiling:**
```bash
# Cron job for regular I/O profiling
0 */6 * * * /path/to/io-profiling-enhanced.sh $(jps | grep YourApp | cut -d' ' -f1)
```

**Performance Testing Integration:**
- Include I/O performance tests in CI/CD
- Establish performance regression detection
- Implement canary deployments with I/O monitoring

## Summary for Senior SREs

### Essential Documents for I/O Bottleneck Detection:
1. **Wall Clock Flamegraphs** - Primary indicator of I/O wait times
2. **JFR Files with I/O Events** - Detailed metrics and timing
3. **Lock Contention Profiles** - Identify I/O-related synchronization issues
4. **Memory Allocation Profiles** - Buffer management and I/O object creation

### Key Analysis Points:
- Compare CPU vs Wall Clock profiles to identify I/O-bound operations
- Use JFR for detailed I/O event analysis and timing
- Establish baselines under different load conditions
- Focus on end-to-end request latency, not just individual operations

### Action Framework:
1. **Measure**: Use comprehensive profiling to establish current state
2. **Analyze**: Correlate multiple profiling documents for complete picture
3. **Optimize**: Implement targeted fixes based on profiling evidence
4. **Validate**: Re-profile to confirm improvements
5. **Monitor**: Establish ongoing monitoring for regression detection

This approach ensures that I/O bottleneck detection and resolution follows SRE best practices of measurement-driven optimization and continuous improvement. 
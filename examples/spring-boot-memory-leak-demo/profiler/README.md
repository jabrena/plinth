# Java Profiling with Async-Profiler v4.0

This directory contains a complete profiling setup for your Spring Boot Memory Leak Demo application using async-profiler v4.0.

## Quick Start

1. **Start your Spring Boot application** (if not already running):
   ```bash
   cd examples/spring-boot-memory-leak-demo
   ./mvnw spring-boot:run
   ```

2. **Run the profiler** (from the project root or this directory):
   ```bash
   ./profiler/scripts/java-profile.sh
   ```

3. **Select your Java process** from the interactive menu and choose profiling options.

4. **View results** in the `results/` directory - open any `.html` file in your browser.

## Directory Structure

```
profiler/
├── scripts/              # Profiling scripts
│   └── java-profile.sh  # Main interactive profiling script
├── results/              # Generated profiling output
│   ├── *.html           # Flamegraph files
│   └── *.jfr            # JFR recording files
├── current/              # Symlink to current profiler version (auto-created)
└── async-profiler-*/     # Downloaded profiler binaries (auto-created)
```

## Profiling Options Available

### 1. CPU Profiling (30s)
- **Purpose**: Identify CPU hotspots and performance bottlenecks
- **Best for**: General performance analysis
- **Output**: CPU flamegraph showing method call stacks and time spent

### 2. Memory Allocation Profiling (30s)
- **Purpose**: Track memory allocations and identify memory-intensive operations
- **Best for**: Memory usage analysis, finding allocation hotspots
- **Output**: Allocation flamegraph showing objects being allocated

### 3. Lock Contention Profiling (30s)
- **Purpose**: Identify synchronization issues and thread contention
- **Best for**: Concurrency analysis, finding bottlenecks in multi-threaded code
- **Output**: Lock contention flamegraph

### 4. Wall Clock Profiling (30s)
- **Purpose**: Profile all threads including those that are sleeping or waiting
- **Best for**: Understanding overall application behavior including I/O waits
- **Output**: Wall clock flamegraph including blocked threads

### 5. Interactive Heatmap (60s) - NEW in v4.0
- **Purpose**: Visualize performance data over time
- **Best for**: Understanding performance patterns and variations over time
- **Output**: Interactive heatmap showing performance changes over the profiling period

### 6. Native Memory Profiling (30s) - NEW in v4.0
- **Purpose**: Profile native memory allocations outside the JVM heap
- **Best for**: Detecting native memory leaks, off-heap usage analysis
- **Output**: Native memory flamegraph

### 7. Inverted Flame Graph (30s) - NEW in v4.0
- **Purpose**: Show flame graphs with the call stack inverted (callers at top)
- **Best for**: Bottom-up analysis, understanding what calls expensive methods
- **Output**: Inverted flamegraph

### 8. Custom Duration CPU Profiling
- **Purpose**: CPU profiling with user-specified duration
- **Best for**: Longer or shorter profiling sessions as needed
- **Output**: CPU flamegraph for custom duration

### 9. Memory Leak Detection (5min)
- **Purpose**: Extended profiling to detect memory leaks
- **Best for**: Identifying memory leaks in long-running applications
- **Output**: Memory allocation flamegraph with leak detection focus

## Memory Leak Detection Workflow

Since this is a memory leak demo, here's the recommended profiling workflow:

### Step 1: Baseline Profiling
```bash
# Start the application
./mvnw spring-boot:run

# In another terminal, run the profiler
./profiler/scripts/java-profile.sh

# Select option 2 (Memory Allocation Profiling) for 30s baseline
```

### Step 2: Load Testing + Profiling
```bash
# Generate some load (if you have a load test script)
./load-test.sh  # or whatever load generation you have

# Run memory leak detection (option 9) during load
# This will profile for 5 minutes to capture leak patterns
```

### Step 3: Compare Results
- Compare allocation patterns between baseline and under-load scenarios
- Look for growing allocations that don't get garbage collected
- Identify objects that accumulate over time

## Understanding Flamegraphs

### Reading the Graphs
- **Width**: Time/samples spent in a method (wider = more expensive)
- **Height**: Call stack depth (deeper = more nested calls)
- **Colors**: Random colors for visual distinction (no special meaning)
- **Search**: Use the search box to find specific methods or classes

### What to Look For
- **Wide frames**: Methods consuming significant resources
- **Repeated patterns**: Methods called frequently
- **Memory allocations**: In allocation flamegraphs, look for large allocations
- **Growing patterns**: In leak detection, look for continuously growing allocations

### Memory Leak Patterns
- Look for objects that keep growing in allocation flamegraphs
- Check for collections (ArrayList, HashMap) that grow indefinitely
- Identify static collections or caches that aren't cleaned up
- Watch for event listeners that aren't properly removed

## Common Commands

### Quick CPU Profile
```bash
# One-liner for quick CPU profiling (30 seconds)
./profiler/scripts/java-profile.sh
# Then select option 1
```

### Memory Leak Investigation
```bash
# Extended memory profiling for leak detection
./profiler/scripts/java-profile.sh
# Then select option 9 (runs for 5 minutes)
```

### View Recent Results
```bash
# List recent profiling files
ls -lat profiler/results/

# Open the latest HTML file in browser (macOS)
open profiler/results/$(ls -t profiler/results/*.html | head -1)
```

## Integration with Your Application

### Recommended JVM Flags
Add these to your Spring Boot application for better profiling:

```bash
# For better profiling accuracy
-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints

# For macOS (optional, helps with stack traces)
-XX:+PreserveFramePointer
```

### Maven Integration
You can add profiling to your Maven workflow:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <jvmArguments>
            -XX:+UnlockDiagnosticVMOptions
            -XX:+DebugNonSafepoints
        </jvmArguments>
    </configuration>
</plugin>
```

## Troubleshooting

### "No Java processes found"
- Ensure your Spring Boot application is running
- Check with `jps -l` to see Java processes

### Permission Issues (Linux/macOS)
```bash
# If you get permission errors (Linux)
echo 1 | sudo tee /proc/sys/kernel/perf_event_paranoid

# If the profiler can't attach
sudo sysctl kernel.yama.ptrace_scope=0  # Linux only
```

### macOS Specific Issues
- The script automatically detects macOS and downloads the correct version
- No additional configuration needed for macOS ARM64 or Intel

### Process Disappears During Profiling
- The script will detect if the process stops and offer to switch to a new PID
- Restart your application and re-run the profiler

## Advanced Usage

### Manual Profiling Commands
If you prefer manual control:

```bash
# Basic CPU profiling
./profiler/current/bin/asprof -d 30 -f cpu-profile.html <PID>

# Memory allocation profiling
./profiler/current/bin/asprof -e alloc -d 30 -f alloc-profile.html <PID>

# Generate JFR file for detailed analysis
./profiler/current/bin/asprof -d 60 -o jfr -f profile.jfr <PID>
```

### Continuous Profiling
For continuous monitoring, you can set up scheduled profiling:

```bash
# Example: Profile every hour and save with timestamp
while true; do
    TIMESTAMP=$(date +%Y%m%d-%H%M%S)
    ./profiler/current/bin/asprof -d 60 -f ./profiler/results/hourly-$TIMESTAMP.html <PID>
    sleep 3600
done
```

## Memory Leak Analysis Tips

1. **Establish Baseline**: Profile during startup and normal operations
2. **Load Testing**: Profile during sustained load to see leak patterns
3. **Long Duration**: Use 5+ minute profiling sessions for leak detection
4. **Compare Over Time**: Take multiple snapshots and compare growth patterns
5. **Focus on Allocations**: Memory allocation flamegraphs are most useful for leak detection
6. **Look for Patterns**: Identify objects that grow continuously without being freed

## Resources

- [Async-profiler GitHub](https://github.com/async-profiler/async-profiler)
- [Flamegraph Interpretation Guide](http://www.brendangregg.com/flamegraphs.html)
- [Java Memory Management](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)

## Support

This profiling setup is based on the `@151-java-profiling` cursor rule and follows Java profiling best practices. The script handles OS detection, profiler download, and provides an interactive interface for comprehensive Java application profiling. 
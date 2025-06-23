# Java Application Profiler with async-profiler v4.0

This directory contains a complete setup for profiling Java applications using async-profiler v4.0, including automatic OS detection, profiler download, and flamegraph generation.

## Directory Structure

```
profiler/
├── scripts/
│   └── java-profile.sh     # Main profiling script
├── results/                # Generated profiling output
│   ├── *.html             # Flamegraph files
│   └── *.jfr              # JFR recording files
├── current/               # Symlink to current profiler version
└── async-profiler-*/      # Downloaded profiler binaries
```

## Features

### New in async-profiler v4.0
- **Interactive Heatmaps**: Visualize performance data over time
- **Native Memory Leak Profiler**: Detect memory leaks in native code
- **Enhanced JFR Support**: Improved JFR conversion with jfrconv binary
- **JDK 23+ Compatibility**: Full support for latest JDK versions
- **Inverted Flame Graphs**: Flip flame graphs vertically for different perspectives
- **Enhanced Container Support**: Better profiling in containerized environments

### Profiling Options Available
1. **CPU Profiling** (30s) - Standard CPU flamegraph
2. **Memory Allocation Profiling** (30s) - Track memory allocations
3. **Lock Contention Profiling** (30s) - Identify synchronization bottlenecks
4. **Wall Clock Profiling** (30s) - Include sleeping/waiting threads
5. **Interactive Heatmap** (60s) - NEW: Performance over time visualization
6. **Native Memory Profiling** (30s) - NEW: Native memory allocations
7. **Inverted Flame Graph** (30s) - NEW: Bottom-up call tree view
8. **Custom Duration** - Specify your own profiling duration
9. **View Results** - Browse recent profiling outputs

## Quick Start

### Prerequisites
- Java application running on your system
- `jps` command available (part of JDK)
- Internet connection for downloading async-profiler
- Appropriate permissions to attach to Java processes

### 1. Start Your Spring Boot Application
First, make sure your Spring Boot application is running:

```bash
# From the implementation directory
cd examples/spring-boot-demo/implementation
./mvnw spring-boot:run
```

### 2. Run the Profiler
Execute the profiling script from the project root:

```bash
# From the project root directory
./examples/spring-boot-demo/profiler/scripts/java-profile.sh
```

### 3. Follow the Interactive Menu
The script will:
1. **Auto-detect running Java processes** and let you select which one to profile
2. **Detect your OS and architecture** (macOS, Linux x64/ARM64, Windows)
3. **Download async-profiler v4.0** automatically if not already present
4. **Present profiling options** with an interactive menu
5. **Generate flamegraphs** and save them to the `results/` directory

## Usage Examples

### Basic CPU Profiling
```bash
# The script will guide you through process selection and profiling options
./examples/spring-boot-demo/profiler/scripts/java-profile.sh
# Select option 1 for CPU profiling (30 seconds)
```

### Memory Allocation Analysis
```bash
# Run the script and select option 2 for memory allocation profiling
./examples/spring-boot-demo/profiler/scripts/java-profile.sh
# This will help identify memory hotspots and potential leaks
```

### Under Load Testing
For best results, profile your application under realistic load:

```bash
# Terminal 1: Start your application
cd examples/spring-boot-demo/implementation
./mvnw spring-boot:run

# Terminal 2: Generate load (if you have load testing setup)
./load-test.sh

# Terminal 3: Profile the application
./examples/spring-boot-demo/profiler/scripts/java-profile.sh
```

## Manual Commands

If you prefer manual control, here are direct commands after running the setup:

```bash
# Get your Java process PID
jps -l

# CPU profiling (replace <PID> with actual process ID)
./examples/spring-boot-demo/profiler/current/bin/asprof -d 30 -f ./examples/spring-boot-demo/profiler/results/cpu-flamegraph.html <PID>

# Memory allocation profiling
./examples/spring-boot-demo/profiler/current/bin/asprof -e alloc -d 30 -f ./examples/spring-boot-demo/profiler/results/allocation-flamegraph.html <PID>

# Lock contention profiling
./examples/spring-boot-demo/profiler/current/bin/asprof -e lock -d 30 -f ./examples/spring-boot-demo/profiler/results/lock-flamegraph.html <PID>

# New v4.0: Interactive heatmap (two-step process)
# Step 1: Generate JFR recording
./examples/spring-boot-demo/profiler/current/bin/asprof -d 60 -o jfr -f ./examples/spring-boot-demo/profiler/results/profile.jfr <PID>

# Step 2: Convert JFR to heatmap
./examples/spring-boot-demo/profiler/current/bin/jfrconv --cpu -o heatmap ./examples/spring-boot-demo/profiler/results/profile.jfr ./examples/spring-boot-demo/profiler/results/heatmap-cpu.html

# New v4.0: Inverted flame graph
./examples/spring-boot-demo/profiler/current/bin/asprof -d 30 --inverted -f ./examples/spring-boot-demo/profiler/results/inverted-flamegraph.html <PID>
```

## Understanding Flamegraphs

### Reading Flamegraphs
- **Width**: Time spent in a method (wider = more time)
- **Height**: Call stack depth (higher = deeper calls)
- **Color**: Random assignment for visibility (no special meaning)
- **Search**: Use the search box to find specific methods
- **Zoom**: Click on any frame to zoom in

### Key Patterns to Look For
- **Wide flames**: Methods consuming significant CPU time
- **Tall stacks**: Deep call chains that might indicate recursion issues
- **Flat profiles**: Even distribution might indicate I/O bound operations
- **Spikes**: Hotspots that are good optimization candidates

## Advanced Usage

### JFR Output for Detailed Analysis
```bash
# Generate JFR file for detailed analysis
./examples/spring-boot-demo/profiler/current/bin/asprof -d 60 -o jfr -f ./examples/spring-boot-demo/profiler/results/profile.jfr <PID>

# Convert JFR to flamegraph using new jfrconv (v4.0 feature)
./examples/spring-boot-demo/profiler/current/bin/jfrconv --flamegraph ./examples/spring-boot-demo/profiler/results/profile.jfr ./examples/spring-boot-demo/profiler/results/converted-flamegraph.html
```

### Filtering and Customization
```bash
# Profile only specific packages
./examples/spring-boot-demo/profiler/current/bin/asprof -d 30 -i 'info/jab/*' -f ./examples/spring-boot-demo/profiler/results/filtered-flamegraph.html <PID>

# Profile with custom sampling interval
./examples/spring-boot-demo/profiler/current/bin/asprof -d 30 -i 10ms -f ./examples/spring-boot-demo/profiler/results/custom-interval.html <PID>
```

## Optimal JVM Flags for Profiling

Add these JVM flags to your Spring Boot application for better profiling results:

```bash
# In your application.yaml or as JVM arguments
-XX:+UnlockDiagnosticVMOptions
-XX:+DebugNonSafepoints
-XX:+PreserveFramePointer  # Linux only, improves stack traces
```

For Spring Boot, you can add these in `application.yaml`:
```yaml
spring:
  application:
    name: your-app
  # Add JVM options for better profiling
  jvm:
    options:
      - "-XX:+UnlockDiagnosticVMOptions"
      - "-XX:+DebugNonSafepoints"
```

## Troubleshooting

### Permission Issues
If you encounter permission errors:
```bash
# On Linux, you might need to adjust kernel parameters
echo 1 | sudo tee /proc/sys/kernel/perf_event_paranoid
echo 0 | sudo tee /proc/sys/kernel/kptr_restrict
```

### No Java Processes Found
```bash
# Verify Java processes are running
jps -l

# Check if your application is actually running
ps aux | grep java
```

### Profiler Download Issues
```bash
# Manual download if automatic fails
cd examples/spring-boot-demo/profiler
curl -L -o async-profiler-4.0-macos.zip https://github.com/async-profiler/async-profiler/releases/download/v4.0/async-profiler-4.0-macos.zip
unzip async-profiler-4.0-macos.zip
ln -sf async-profiler-4.0-macos current
```

## Results Interpretation

### CPU Profiling Results
- Look for wide methods consuming significant CPU time
- Identify unexpected hotspots in your code
- Check for excessive garbage collection overhead

### Memory Allocation Results
- Find classes with high allocation rates
- Identify potential memory leaks
- Understand object creation patterns

### Lock Contention Results
- Discover synchronization bottlenecks
- Identify heavily contended locks
- Find opportunities for concurrency improvements

## Best Practices

1. **Profile under realistic load** for accurate results
2. **Use appropriate duration** (30-60 seconds for most cases)
3. **Profile regularly** to catch performance regressions
4. **Focus on the widest flames** first for maximum impact
5. **Use different profiling types** for comprehensive analysis
6. **Compare before/after** when making optimizations
7. **Profile in production-like environments** when possible

## Support

For issues or questions:
- Check the [async-profiler documentation](https://github.com/async-profiler/async-profiler/wiki)
- Review the [async-profiler v4.0 release notes](https://github.com/async-profiler/async-profiler/releases/tag/v4.0)
- Consult Java profiling best practices guides

Happy profiling! 🔥📊 
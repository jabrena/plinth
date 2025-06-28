---
description: 
globs: 
alwaysApply: false
---
# Java Profiling Workflow / Step 1 / Collect data to measure potential issues

## System prompt characterization

Role definition: You are a Senior software engineer with extensive experience in Java software development

## Description

This cursor rule provides a comprehensive Java application profiling framework designed to detect and measure performance issues systematically. It serves as the first step in a structured profiling workflow, focusing on data collection and problem identification using async-profiler v4.0.

The rule automates the entire profiling setup process, from detecting running Java processes to downloading and configuring the appropriate profiler tools for your system. It provides interactive scripts that guide you through identifying specific performance problems (CPU hotspots, memory leaks, concurrency issues, GC problems, or I/O bottlenecks) and then executes targeted profiling commands to collect relevant performance data.

Key capabilities include:
- **Automated Environment Setup**: Detects OS/architecture and downloads async-profiler v4.0 automatically
- **Problem-Driven Profiling**: Guides users through identifying specific performance issues before profiling
- **Interactive Workflow**: Provides menu-driven interface for selecting appropriate profiling strategies
- **Comprehensive Data Collection**: Supports CPU profiling, memory allocation tracking, lock contention analysis, GC monitoring, and I/O bottleneck detection
- **Modern Tooling**: Leverages async-profiler v4.0 features including interactive heatmaps, native memory leak detection, and enhanced JFR conversion
- **Organized Results**: Maintains clean directory structure with timestamped results for easy analysis and comparison

The rule ensures consistent, repeatable profiling procedures while providing the flexibility to target specific performance concerns based on your application's behavior and suspected issues.

## ⚠️ CRITICAL INSTRUCTION FOR AI ASSISTANTS ⚠️

**WHEN USING THE PROFILING SCRIPT TEMPLATE:**
- **COPY THE BASH SCRIPT EXACTLY** from `java-profiling-script-template.md`
- **NO MODIFICATIONS, INTERPRETATIONS, OR ENHANCEMENTS** allowed
- **USE EVERY LINE VERBATIM** - do not change logic, structure, or features
- **DO NOT CREATE YOUR OWN VERSION** - use the provided template only
- The template script is complete, tested, and production-ready

---

This rule provides comprehensive Java application profiling using async-profiler v4.0, including automatic OS detection, profiler download, and flamegraph generation with the latest features.

## Problem Identification

Before starting the profiling process, identify what specific performance problem you're trying to solve. 

**Reference the profiling questions template:** [java-profiling-questions-template.md](mdc:.cursor/rules/templates/java-profiling-questions-template.md)

The template categorizes problems into:
- **Performance Bottlenecks** (CPU hotspots, inefficient algorithms, unnecessary allocations, string operations)
- **Memory-Related Problems** (memory leaks, heap usage, object retention, off-heap issues)
- **Concurrency and Threading Issues** (lock contention, thread pool issues, deadlocks, context switching)
- **Garbage Collection Problems** (GC pressure, long pauses, generational issues)
- **I/O and Network Bottlenecks** (blocking operations, connection leaks, serialization issues)

## Overview

Java profiling helps identify performance bottlenecks, memory leaks, and CPU hotspots in Java applications. This rule automates the process of:
1. **Problem Identification**: Determine what specific performance issue you're investigating
2. **Process Detection**: Detecting running Java processes using `jps`
3. **Environment Setup**: Identifying the operating system and architecture
4. **Tool Download**: Downloading the appropriate async-profiler v4.0 binary
5. **Profile Execution**: Attaching to a Java process and generating flamegraphs and heatmaps
6. **Results Analysis**: Storing and interpreting profiling results

## New Features in v4.0

Based on the [async-profiler v4.0 release](mdc:https:/github.com/async-profiler/async-profiler/releases/tag/v4.0), the latest version includes:

### Major Features
- **Interactive Heatmaps**: Visualize performance data over time
- **Native Memory Leak Profiler**: Detect memory leaks in native code
- **jfrconv Binary**: Enhanced JFR conversion capabilities
- **Improved JDK 23+ Support**: Full compatibility with the latest JDK versions

### Enhanced Profiling Capabilities
- **Wall Clock Profiling Optimization**: Better performance for wall-clock profiling
- **VMStructs-based Stack Walker**: More accurate stack traces with `--cstack vm/vmx`
- **C API**: Access to thread-local profiling context
- **Grain Option**: Coarsen flame graphs for better visualization
- **Inverted Flame Graphs**: Flip flame graphs vertically with `--inverted`

## Project Organization

The profiling setup uses a clean folder structure with everything contained in the profiler directory:

```
your-project/
└── profiler/               # ← All profiling-related files
    ├── scripts/            # ← Profiling scripts and tools
    │   └── java-profile.sh # ← Main profiling script
    ├── results/            # ← Generated profiling output
    │   ├── *.html          # ← Flamegraph files
    │   └── *.jfr           # ← JFR recording files
    ├── current/            # ← Symlink to current profiler version
    └── async-profiler-*/   # ← Downloaded profiler binaries
```

**Usage:** 
- Run `./profiler/scripts/java-profile.sh` for comprehensive interactive profiling

## Automated Profiling Script

**IMPORTANT**: Use the exact bash script from the template without any modification or interpretation.

The complete interactive profiling script is available at: [java-profiling-script-template.md](mdc:.cursor/rules/templates/java-profiling-script-template.md)

**CRITICAL INSTRUCTION FOR AI ASSISTANTS:**
- **COPY THE BASH SCRIPT EXACTLY** from the template file
- **DO NOT MODIFY, INTERPRET, OR ENHANCE** the script content
- **DO NOT ADD NEW FEATURES** or change the logic
- **USE THE SCRIPT VERBATIM** - every line, comment, and function exactly as provided
- The script is already complete and tested - no improvements needed

**Script Location:**
```
└── profiler/               # ← All profiling-related files
    ├── scripts/            # ← Profiling scripts and tools
    │   └── java-profile.sh # ← Copy exact script from template
```

**Setup Instructions:**
1. Copy the **EXACT** bash script content from `java-profiling-script-template.md` 
2. Save it as `profiler/scripts/java-profile.sh`
3. Make it executable: `chmod +x profiler/scripts/java-profile.sh`
4. **NO MODIFICATIONS** to the script content are needed or allowed

## Profiling Execution Workflow

After setting up the profiling scripts, follow this execution workflow to profile your Java application and store results in the profiler/results directory:

### Step 1: Create and Make Script Executable

**CRITICAL**: Use the exact script from the template without any changes:

```bash
# Create the profiler directory structure
mkdir -p profiler/scripts
mkdir -p profiler/results

# Copy the EXACT bash script from java-profiling-script-template.md
# Save it as profiler/scripts/java-profile.sh
# DO NOT MODIFY THE SCRIPT CONTENT - USE IT EXACTLY AS PROVIDED

# Make the script executable
chmod +x profiler/scripts/java-profile.sh
```

**REMINDER**: The script in `java-profiling-script-template.md` is complete and should be copied verbatim. No interpretation, enhancement, or modification is required or allowed.

### Step 2: Execute the Profiling Script

Run the script from your project root directory:

```bash
# Execute the interactive profiling script
./profiler/scripts/java-profile.sh
```

**Expected execution flow:**
1. **Problem Identification**: The script will first ask you to identify what problem you're trying to solve
2. **Process Selection**: Lists all Java processes and allows you to select one
3. **Directory Setup**: Prompts for profiler directory (default: ./profiler)
4. **Auto-setup**: Downloads and configures async-profiler automatically
5. **Interactive Menu**: Presents profiling options based on your problem type

### Step 3: Problem-Specific Profiling Commands

Based on the problem you identified, execute these specific profiling commands:

#### For CPU Hotspots and Performance Bottlenecks
```bash
# Quick CPU profiling (30 seconds)
./profiler/current/bin/asprof -d 30 -f ./profiler/results/cpu-hotspots-$(date +%Y%m%d-%H%M%S).html <PID>

# Extended CPU profiling with method filtering
./profiler/current/bin/asprof -d 60 -i 'com/yourpackage/*' -f ./profiler/results/filtered-cpu-$(date +%Y%m%d-%H%M%S).html <PID>
```

#### For Memory-Related Problems
```bash
# Memory allocation profiling
./profiler/current/bin/asprof -e alloc -d 60 -f ./profiler/results/memory-allocations-$(date +%Y%m%d-%H%M%S).html <PID>

# Memory leak detection (longer duration)
./profiler/current/bin/asprof -e alloc -d 300 --alloc 1m -f ./profiler/results/memory-leak-$(date +%Y%m%d-%H%M%S).html <PID>

# Native memory profiling
./profiler/current/bin/asprof -e native -d 60 -f ./profiler/results/native-memory-$(date +%Y%m%d-%H%M%S).html <PID>
```

#### For Concurrency and Threading Issues
```bash
# Lock contention profiling
./profiler/current/bin/asprof -e lock -d 60 -f ./profiler/results/lock-contention-$(date +%Y%m%d-%H%M%S).html <PID>

# Wall clock profiling (includes waiting threads)
./profiler/current/bin/asprof -e wall -d 60 -f ./profiler/results/wall-clock-$(date +%Y%m%d-%H%M%S).html <PID>

# Thread-specific profiling
./profiler/current/bin/asprof -d 60 -t -f ./profiler/results/thread-analysis-$(date +%Y%m%d-%H%M%S).html <PID>
```

#### For Garbage Collection Problems
```bash
# GC analysis with JFR
./profiler/current/bin/asprof -d 120 -o jfr -f ./profiler/results/gc-analysis-$(date +%Y%m%d-%H%M%S).jfr <PID>

# Convert JFR to flamegraph for GC analysis
./profiler/current/bin/jfr2flame ./profiler/results/gc-analysis-*.jfr ./profiler/results/gc-flamegraph-$(date +%Y%m%d-%H%M%S).html
```

#### For I/O and Network Bottlenecks
```bash
# I/O profiling with wall clock events
./profiler/current/bin/asprof -e wall -d 90 -f ./profiler/results/io-analysis-$(date +%Y%m%d-%H%M%S).html <PID>

# Combined CPU and I/O analysis
./profiler/current/bin/asprof -e cpu,wall -d 90 -f ./profiler/results/cpu-io-combined-$(date +%Y%m%d-%H%M%S).html <PID>
```

### Step 4: Advanced Analysis with Interactive Heatmaps

For time-based analysis, create interactive heatmaps:

```bash
# Generate JFR recording
./profiler/current/bin/asprof -d 120 -o jfr -f ./profiler/results/profile-$(date +%Y%m%d-%H%M%S).jfr <PID>

# Convert to interactive heatmap
./profiler/current/bin/jfrconv --cpu -o heatmap ./profiler/results/profile-*.jfr ./profiler/results/heatmap-cpu-$(date +%Y%m%d-%H%M%S).html

# Create allocation heatmap
./profiler/current/bin/jfrconv --alloc -o heatmap ./profiler/results/profile-*.jfr ./profiler/results/heatmap-alloc-$(date +%Y%m%d-%H%M%S).html
```

### Step 5: Results Organization and Analysis

After profiling, organize and analyze your results:

```bash
# List all generated profiling files
ls -la ./profiler/results/

# View recent profiling results organized by type
echo "=== CPU Profiling Results ==="
ls -lt ./profiler/results/*cpu* ./profiler/results/*flamegraph* 2>/dev/null | head -5

echo "=== Memory Profiling Results ==="
ls -lt ./profiler/results/*memory* ./profiler/results/*alloc* 2>/dev/null | head -5

echo "=== Threading/Lock Analysis ==="
ls -lt ./profiler/results/*lock* ./profiler/results/*wall* ./profiler/results/*thread* 2>/dev/null | head -5

echo "=== Heatmap Analysis ==="
ls -lt ./profiler/results/*heatmap* 2>/dev/null | head -5

echo "=== JFR Recordings ==="
ls -lt ./profiler/results/*.jfr 2>/dev/null | head -5
```

## Interpreting Flamegraphs

### Reading Flamegraphs
- **Width**: Represents the time spent in a method (wider = more time)
- **Height**: Represents the call stack depth
- **Color**: Randomly assigned for visibility (no special meaning)
- **Search**: Use the search box to find specific methods
- **Zoom**: Click on any frame to zoom in

### Common Patterns to Look For
- **Wide flames**: Methods consuming significant CPU time
- **Tall stacks**: Deep call chains that might indicate recursion issues
- **Flat profiles**: Even distribution might indicate I/O bound operations
- **Spikes**: Hotspots that are good optimization candidates

## Resources

- [Async-profiler v4.0 Release](mdc:https:/github.com/async-profiler/async-profiler/releases/tag/v4.0)
- [Async-profiler GitHub Repository](mdc:https:/github.com/async-profiler/async-profiler)
- [Async-profiler Documentation](mdc:https:/github.com/async-profiler/async-profiler/wiki)
- [Java Flight Recorder Guide](mdc:https:/docs.oracle.com/javacomponents/jmc-5-4/jfr-runtime-guide/about.htm)
- [Brendan Gregg's Flame Graph Resources](mdc:http:/www.brendangregg.com/flamegraphs.html)


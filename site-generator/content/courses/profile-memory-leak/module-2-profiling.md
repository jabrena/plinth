title=Module 2: Hands-on Profiling with System Prompts
type=course
status=published
date=2025-09-17
author=Juan Antonio Breña Moral
version=0.11.0-SNAPSHOT
tags=java, profiling, memory-leak, async-profiler, flamegraph, jmeter
~~~~~~

## Mastering the Interactive Profiling Script and Evidence Collection

**⏱️ Duration:** 3 hours
**🎯 Learning Objectives:**
- Master the @161-java-profiling-detect system prompt and its 21 profiling options
- Learn problem-driven profiling strategies for different performance issues
- Understand flamegraph interpretation and visual analysis techniques
- Generate comprehensive profiling evidence under realistic load conditions
- Integrate JMeter load testing for consistent profiling scenarios

---

## 🎯 Deep Dive: The Interactive Profiling Script

### Understanding the Problem-Driven Approach

The profiling script in @161-java-profiling-detect follows a **problem-first methodology**:

1. **Problem Identification** → 2. **Tool Selection** → 3. **Data Collection** → 4. **Evidence Generation**

This approach ensures you collect the **right data** for the **specific problem** you're investigating.

#### **🔍 The 5 Problem Categories**

```bash
./profiler/scripts/profile-java-process.sh
# Step 0: Problem Identification
```

 - Performance Bottlenecks (Focus: CPU hotspots, inefficient algorithms; Recommended tools: CPU profiling, Wall-clock; Memory leak relevance: Medium - may indicate leak symptoms)
 - Memory-Related Problems (Focus: Memory leaks, heap usage, retention; Recommended tools: Memory allocation, Leak detection; Memory leak relevance: **HIGH - Primary focus**)
 - Concurrency/Threading (Focus: Lock contention, thread pools; Recommended tools: Lock profiling, Thread dumps; Memory leak relevance: High - thread pool leaks)
 - Garbage Collection (Focus: GC pressure, long pauses; Recommended tools: GC logs, Allocation profiling; Memory leak relevance: High - GC symptoms of leaks)
 - I/O and Network (Focus: Blocking operations, connection leaks; Recommended tools: Wall-clock, I/O analysis; Memory leak relevance: Medium - connection pool leaks)

### 💡 Learning Insight
**For memory leak detection, we primarily focus on Category 2 (Memory-Related Problems), but Categories 3 and 4 provide crucial supporting evidence!**

---

## 🛠️ The 21 Profiling Options Masterclass

### **Category A: Essential Memory Leak Detection Tools**

#### **🎯 Option 2: Memory Allocation Profiling (30s)**
**When to use:** Initial memory pattern analysis
**What it shows:** Which methods allocate the most objects

```bash
./profile-java-process.sh
# Select: 2. Memory Allocation Profiling (30s)
```

**🎯 Practical Exercise 1: Basic Allocation Analysis**

1. **Start profiling with active memory leaks:**
```bash
# Ensure coco=true (memory leaks enabled)
curl http://localhost:8080/actuator/env/coco
```

2. **Generate consistent load during profiling:**
```bash
# In separate terminal - run this DURING profiling
for i in {1..100}; do
  curl -X POST http://localhost:8080/api/v1/objects/create
  sleep 0.5
done
```

3. **Analyze the resulting flamegraph:**
- **Look for:** Wide sections in `CocoController.createObject`
- **Measure:** Canvas height and stack complexity
- **Document:** Specific allocation patterns

**Expected Results:**
```
File: allocation-flamegraph-YYYYMMDD-HHMMSS.html
Key Indicators:
- CocoController.createObject shows significant width
- MyPojo allocation patterns clearly visible
- ArrayList.add operations consume memory
```

#### **🎯 Option 8: Memory Leak Detection (5min)**
**When to use:** Comprehensive leak pattern analysis
**What it shows:** Long-term memory accumulation patterns

```bash
./profile-java-process.sh
# Select: 8. Memory Leak Detection (5min)
```

**🎯 Practical Exercise 2: Long-term Leak Detection**

1. **Start the 5-minute leak detection:**
```bash
# This is a long-running profiling session
./profile-java-process.sh
# Select option 8
```

2. **Create sustained, realistic load:**
```bash
# Use JMeter for consistent 5-minute load
./run-jmeter.sh -t 300 -c 3  # 5 minutes, 3 concurrent users
```

3. **Monitor progress and system behavior:**
```bash
# In another terminal, monitor memory usage
watch -n 10 'curl -s http://localhost:8080/actuator/metrics/jvm.memory.used'
```

**Expected Results:**
```
File: memory-leak-YYYYMMDD-HHMMSS.html
Key Indicators:
- Continuous memory growth patterns
- GC retention increasing over time
- Object accumulation in specific methods
```

#### **🎯 Option 9: Complete Memory Analysis Workflow**
**When to use:** Comprehensive memory investigation
**What it shows:** Multi-phase analysis with baseline, detailed, and leak detection

```bash
./profile-java-process.sh
# Select: 9. Complete Memory Analysis Workflow
```

This option generates **three sequential reports:**
1. **30s Baseline**: Quick memory allocation snapshot
2. **60s Detailed**: In-depth heap analysis
3. **5min Leak Detection**: Long-term accumulation patterns

**🎯 Practical Exercise 3: Complete Workflow Analysis**

1. **Execute the complete workflow:**
```bash
# This will take about 6.5 minutes total
./profile-java-process.sh
# Select option 9
```

2. **Coordinate load testing across all phases:**
```bash
# Create a script that matches the profiling phases
cat > coordinated-load.sh << 'EOF'
#!/bin/bash
echo "Phase 1: Baseline load (30s)"
timeout 30s bash -c 'while true; do curl -X POST http://localhost:8080/api/v1/objects/create; sleep 1; done' &

echo "Phase 2: Detailed analysis load (60s)"
sleep 30
timeout 60s bash -c 'while true; do curl -X POST http://localhost:8080/api/v1/objects/create; curl -X POST http://localhost:8080/api/v1/threads/create; sleep 0.5; done' &

echo "Phase 3: Leak detection load (5min)"
sleep 90
timeout 300s bash -c 'while true; do curl -X POST http://localhost:8080/api/v1/objects/create; sleep 0.3; done' &

wait
echo "All load phases completed"
EOF
chmod +x coordinated-load.sh
```

3. **Execute coordinated load:**
```bash
./coordinated-load.sh
```

**Expected Results:**
```
Files generated:
- memory-baseline-YYYYMMDD-HHMMSS.html (30s snapshot)
- heap-analysis-YYYYMMDD-HHMMSS.html (60s detailed)
- memory-leak-complete-YYYYMMDD-HHMMSS.html (5min comprehensive)
```

### **Category B: Advanced Profiling Techniques**

#### **🎯 Option 12: All Events Profiling (30s)**
**When to use:** Comprehensive system analysis
**What it shows:** CPU, allocation, lock, and wall-clock data simultaneously

```bash
./profile-java-process.sh
# Select: 12. All Events Profiling (30s)
```

**Expected Results:**
```
Files generated:
- all-events-YYYYMMDD-HHMMSS.jfr (JFR recording)
- all-events-YYYYMMDD-HHMMSS.html (Converted flamegraph)
```

#### **🎯 Option 18: JFR Memory Leak Analysis with TLAB tracking**
**When to use:** Advanced memory leak investigation
**What it shows:** Thread Local Allocation Buffer patterns and object lifecycle

**TLAB (Thread Local Allocation Buffer) Explained:**

TLAB is a JVM optimization technique that provides each thread with its own private allocation buffer in the heap's Eden space. This eliminates contention when multiple threads allocate objects simultaneously.

**Key TLAB Concepts:**
- **Private Buffer**: Each thread gets its own allocation space (typically 1-256KB)
- **Fast Allocation**: Objects are allocated via simple pointer bumping (no synchronization)
- **Automatic Management**: JVM automatically manages TLAB size based on allocation patterns
- **Waste Tracking**: Unused TLAB space is tracked as "waste" for performance tuning

**TLAB and Memory Leaks:**
- **Allocation Patterns**: TLAB tracking reveals which threads allocate the most objects
- **Leak Detection**: Threads with consistently growing TLAB usage may indicate leaks
- **Performance Impact**: Memory leaks can cause frequent TLAB allocation/deallocation
- **GC Pressure**: Excessive TLAB waste increases garbage collection frequency

**Profiling with TLAB:**
- **JFR Events**: `jdk.ObjectAllocationInNewTLAB` and `jdk.ObjectAllocationOutsideTLAB`
- **Allocation Tracking**: Monitor allocation rates per thread
- **Waste Analysis**: Identify threads with high TLAB waste ratios
- **Correlation**: Cross-reference TLAB data with memory leak patterns

```bash
./profile-java-process.sh
# Select: 18. JFR Memory Leak Analysis with TLAB tracking
```

**🎯 Practical Exercise 4: Advanced JFR Analysis**

1. **Execute TLAB tracking (10 minutes):**
```bash
./profile-java-process.sh
# Select option 18
# Enter duration: 10 (minutes)
```

2. **Generate intensive allocation load:**
```bash
# High-frequency allocations to trigger TLAB behavior
for i in {1..1000}; do
  curl -X POST http://localhost:8080/api/v1/objects/create
  if [ $((i % 100)) -eq 0 ]; then
    echo "Completed $i allocations"
  fi
done
```

**Expected Results:**
```
File: memory-leak-tlab-YYYYMMDD-HHMMSS.jfr
Analysis tools: JProfiler, VisualVM, Mission Control
Key insights: TLAB allocation patterns, object aging, GC behavior
```

---

## 🔥 Flamegraph Interpretation Masterclass

### Understanding Flamegraph Anatomy

```
┌─────────────────────────────────────────────────────────────┐
│  Stack Depth (Y-axis) - Call hierarchy depth                │
│  ┌─────────────────────────────────────────────────────┐    │
│  │ main() ──────────────────────────────────────────── │    │
│  │  └─ CocoController.createObject() ───────────────── │    │
│  │     └─ ArrayList.add() ──────────────────────────   │    │
│  │        └─ MyPojo.<init>() ─────────────────────     │    │
│  │           └─ String repetition ──────────────       │    │
│  └─────────────────────────────────────────────────────┘    │
│           Width (X-axis) - Time/Resource consumption        │
└─────────────────────────────────────────────────────────────┘
```

### **Key Visual Patterns for Memory Leaks**

#### **1. Wide Allocation Patterns**
```
CocoController.createObject() ████████████████████████████████
└─ ArrayList.add()           ████████████████████████████████
   └─ MyPojo.<init>()        ████████████████████████████████
```
**Interpretation:** Consistent, wide sections indicate high-frequency allocations

#### **2. Growing Canvas Height**
```
Run 1 (Baseline):  Canvas height ~200px
Run 2 (5 minutes): Canvas height ~400px
Run 3 (10 minutes): Canvas height ~600px
```
**Interpretation:** Increasing complexity suggests accumulating allocation paths

#### **3. Stack Depth Patterns**
```
Healthy Application:    3-5 levels deep
Memory Leak Pattern:    8-12 levels deep
Critical Leak:          15+ levels deep
```

### 🔍 Knowledge Check
**Look at your generated flamegraphs and answer:**
1. What is the canvas height of your memory-leak detection report?
2. Which method shows the widest section?
3. How many stack levels deep are the main allocation patterns?

### **🎯 Practical Exercise 5: Comparative Flamegraph Analysis**

1. **Generate multiple flamegraphs with different durations:**
```bash
# Short-term (30s)
./profile-java-process.sh  # Option 2

# Medium-term (60s)
./profile-java-process.sh  # Option 7, duration: 60

# Long-term (300s)
./profile-java-process.sh  # Option 8
```

2. **Create comparison table:**
```markdown
| Duration | Canvas Height | Widest Method | Stack Depth | File Size |
|----------|---------------|---------------|-------------|-----------|
| 30s      | [measure]     | [identify]    | [count]     | [size]    |
| 60s      | [measure]     | [identify]    | [count]     | [size]    |
| 300s     | [measure]     | [identify]    | [count]     | [size]    |
```

3. **Document visual patterns:**
- Take screenshots of key sections
- Note color patterns and distributions
- Identify recurring allocation patterns

---

## 🎛️ JMeter Integration for Consistent Profiling

### Understanding the Load Testing Integration

The demo includes sophisticated JMeter integration for realistic profiling scenarios:

```bash
# View available JMeter options
./run-jmeter.sh --help
```

**Key JMeter Parameters:**
- `-t, --time`: Test duration in seconds
- `-c, --concurrency`: Number of concurrent users
- `-r, --ramp-up`: Ramp-up time in seconds
- `-e, --endpoints`: Which endpoints to test (objects, threads, both)

### **🎯 Practical Exercise 6: Advanced Load Testing Scenarios**

#### **Scenario 1: Burst Load Testing**
```bash
# High concurrency, short duration
./run-jmeter.sh -t 120 -c 20 -r 10 -e both
```
**Use case:** Simulating peak traffic conditions
**Profiling pairing:** Use with Option 2 (Memory Allocation) for burst pattern analysis

#### **Scenario 2: Sustained Load Testing**
```bash
# Moderate concurrency, long duration
./run-jmeter.sh -t 600 -c 5 -r 30 -e objects
```
**Use case:** Simulating normal production load
**Profiling pairing:** Use with Option 8 (Memory Leak Detection) for accumulation patterns

#### **Scenario 3: Gradual Ramp Testing**
```bash
# Increasing load over time
./run-jmeter.sh -t 300 -c 10 -r 120 -e both
```
**Use case:** Simulating growing user base
**Profiling pairing:** Use with Option 9 (Complete Workflow) for comprehensive analysis

### **Creating Custom Load Patterns**

```bash
# Create custom JMeter test plan
cat > custom-memory-leak-test.jmx << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
  <!-- Custom test plan for memory leak detection -->
  <hashTree>
    <TestPlan testname="Memory Leak Detection Test">
      <elementProp name="TestPlan.arguments" elementType="Arguments" guiclass="ArgumentsPanel">
        <collectionProp name="Arguments.arguments">
          <elementProp name="objects_per_minute" elementType="Argument">
            <stringProp name="Argument.name">objects_per_minute</stringProp>
            <stringProp name="Argument.value">60</stringProp>
          </elementProp>
        </collectionProp>
      </elementProp>
    </TestPlan>
    <!-- Add your custom test scenarios here -->
  </hashTree>
</jmeterTestPlan>
EOF
```

---

## 📊 Evidence Collection and Documentation

### Systematic Evidence Collection Framework

For each profiling session, collect:

#### **1. Technical Metrics**
```bash
# Create evidence collection script
cat > collect-evidence.sh << 'EOF'
#!/bin/bash
TIMESTAMP=$(date +%Y%m%d-%H%M%S)
EVIDENCE_DIR="profiler/evidence-$TIMESTAMP"
mkdir -p "$EVIDENCE_DIR"

echo "Collecting evidence at $TIMESTAMP"

# System information
echo "=== System Info ===" > "$EVIDENCE_DIR/system-info.txt"
java -version >> "$EVIDENCE_DIR/system-info.txt" 2>&1
free -h >> "$EVIDENCE_DIR/system-info.txt"
nproc >> "$EVIDENCE_DIR/system-info.txt"

# Application status
echo "=== App Status ===" > "$EVIDENCE_DIR/app-status.txt"
curl -s http://localhost:8080/actuator/health >> "$EVIDENCE_DIR/app-status.txt"
curl -s http://localhost:8080/actuator/metrics/jvm.memory.used >> "$EVIDENCE_DIR/app-status.txt"

# Configuration
echo "=== Configuration ===" > "$EVIDENCE_DIR/config.txt"
curl -s http://localhost:8080/actuator/env/coco >> "$EVIDENCE_DIR/config.txt"

echo "Evidence collected in $EVIDENCE_DIR"
EOF
chmod +x collect-evidence.sh
```

#### **2. Visual Documentation**
- Screenshot key flamegraph sections
- Document canvas heights and widths
- Note color patterns and distributions
- Capture browser developer tools memory graphs

#### **3. Quantitative Measurements**
```bash
# Measure flamegraph characteristics
cat > measure-flamegraph.sh << 'EOF'
#!/bin/bash
HTML_FILE="$1"
if [ -z "$HTML_FILE" ]; then
    echo "Usage: $0 <flamegraph.html>"
    exit 1
fi

echo "Analyzing flamegraph: $HTML_FILE"
echo "File size: $(wc -c < "$HTML_FILE") bytes"
echo "Estimated canvas height: $(grep -o 'height="[0-9]*"' "$HTML_FILE" | head -1 | grep -o '[0-9]*') pixels"
echo "Number of stack frames: $(grep -c 'class="func_g"' "$HTML_FILE")"
EOF
chmod +x measure-flamegraph.sh
```

---

## 🎯 Module 2 Assessment

### Hands-on Proficiency Checklist

**✅ Interactive Script Mastery:**
- [ ] Successfully navigated all 21 profiling options
- [ ] Executed memory allocation profiling (Option 2)
- [ ] Completed 5-minute memory leak detection (Option 8)
- [ ] Generated complete memory analysis workflow (Option 9)
- [ ] Used advanced JFR analysis with TLAB tracking (Option 18)

**✅ Flamegraph Interpretation:**
- [ ] Can identify wide allocation patterns
- [ ] Measures canvas height accurately
- [ ] Counts stack depth levels
- [ ] Recognizes memory leak visual signatures

**✅ Load Testing Integration:**
- [ ] Executed coordinated JMeter load testing
- [ ] Created custom load patterns
- [ ] Synchronized profiling with realistic load
- [ ] Generated consistent, reproducible results

**✅ Evidence Collection:**
- [ ] Documented technical metrics for each session
- [ ] Captured visual evidence of flamegraph patterns
- [ ] Created quantitative measurements
- [ ] Organized evidence for systematic analysis

### 🎯 Advanced Challenge: Custom Profiling Strategy

**Challenge:** Design a custom profiling strategy for a specific memory leak scenario

**Requirements:**
1. **Scenario Definition**: Define a specific memory leak pattern to investigate
2. **Tool Selection**: Choose appropriate profiling options from the 21 available
3. **Load Pattern**: Design matching JMeter load testing
4. **Evidence Plan**: Define what evidence to collect
5. **Success Criteria**: Establish measurable outcomes

**Example Template:**
```markdown
# Custom Profiling Strategy: [Scenario Name]

## Scenario
- **Leak Type**: [Collection/Thread Pool/Cache/etc.]
- **Symptoms**: [Expected behavior patterns]
- **Timeline**: [How long to observe]

## Profiling Plan
- **Primary Tool**: Option X - [Name and rationale]
- **Supporting Tools**: Option Y, Z - [Supporting evidence]
- **Duration Strategy**: [Short/Medium/Long term analysis]

## Load Testing
- **JMeter Configuration**: [Specific parameters]
- **Load Pattern**: [Burst/Sustained/Gradual]
- **Endpoint Focus**: [Which APIs to exercise]

## Evidence Collection
- **Visual**: [What flamegraph patterns to document]
- **Quantitative**: [Specific metrics to measure]
- **Comparative**: [What to compare against]

## Success Criteria
- [ ] [Measurable outcome 1]
- [ ] [Measurable outcome 2]
- [ ] [Measurable outcome 3]
```

---

## 🚀 Transition to Module 3

**Excellent progress!** You've successfully:
- ✅ Mastered the 21-option interactive profiling script
- ✅ Generated comprehensive memory leak evidence
- ✅ Integrated realistic load testing with profiling
- ✅ Developed flamegraph interpretation skills
- ✅ Created systematic evidence collection processes

### **What's Next?**
In **Module 3: Analysis and Evidence Collection**, we'll focus on:
- Systematic analysis using @162-java-profiling-analyze
- Creating structured documentation from profiling evidence
- Developing prioritized solution recommendations
- Cross-correlating multiple profiling results for comprehensive insights

### 💡 Key Takeaway
**"Effective profiling is not just about collecting data - it's about collecting the RIGHT data under the RIGHT conditions to answer SPECIFIC questions about your application's performance!"**

**Ready to transform your profiling data into actionable insights? [Let's continue to Module 3!](module-3-analysis.html) 🔍**

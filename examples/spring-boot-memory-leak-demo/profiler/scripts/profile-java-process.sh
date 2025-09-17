#!/bin/bash

# java-profile.sh - Automated Java profiling script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}Java Application Profiler v4.4 - Organized by Tool Categories${NC}"
echo "================================================="

# Step 0: Problem Identification
echo -e "${YELLOW}Step 0: Problem Identification${NC}"
echo "-----"
echo "What specific performance problem are you trying to solve?"
echo ""
echo -e "${BLUE}Problem Categories:${NC}"
echo "  1) Performance Bottlenecks (CPU hotspots, inefficient algorithms, unnecessary allocations, string operations)"
echo "  2) Memory-Related Problems (memory leaks, heap usage, object retention, off-heap issues)"
echo "  3) Concurrency/Threading Issues (lock contention, thread pool issues, deadlocks, context switching)"
echo "  4) Garbage Collection Problems (GC pressure, long pauses, generational issues)"
echo "  5) I/O and Network Bottlenecks (blocking operations, connection leaks, serialization issues)"
echo "  0) Not sure / General performance analysis"
echo ""

while true; do
    read -p "Select the problem category you're investigating (0-5): " PROBLEM_SELECTION

    if [[ "$PROBLEM_SELECTION" =~ ^[0-5]$ ]]; then
        case $PROBLEM_SELECTION in
            1) PROBLEM_CATEGORY="Performance Bottlenecks"; SUGGESTED_PROFILE="CPU" ;;
            2) PROBLEM_CATEGORY="Memory-Related Problems"; SUGGESTED_PROFILE="Memory" ;;
            3) PROBLEM_CATEGORY="Concurrency/Threading Issues"; SUGGESTED_PROFILE="Lock/Threading" ;;
            4) PROBLEM_CATEGORY="Garbage Collection Problems"; SUGGESTED_PROFILE="GC Analysis" ;;
            5) PROBLEM_CATEGORY="I/O and Network Bottlenecks"; SUGGESTED_PROFILE="I/O Analysis" ;;
            0) PROBLEM_CATEGORY="General Analysis"; SUGGESTED_PROFILE="CPU" ;;
        esac

        echo -e "${GREEN}Selected problem category: $PROBLEM_CATEGORY${NC}"
        echo -e "${GREEN}Suggested profiling approach: $SUGGESTED_PROFILE${NC}"
        echo ""
        break
    else
        echo -e "${RED}Invalid selection. Please choose a number between 0 and 5.${NC}"
    fi
done

# Step 1: List Java processes and handle selection
echo -e "${YELLOW}Step 1: Available Java Processes${NC}"
echo "-----"

# Get list of Java processes
JAVA_PROCESSES=$(jps -l | grep -v "Jps$")

if [ -z "$JAVA_PROCESSES" ]; then
    echo -e "${RED}No Java processes found running on this system.${NC}"
    echo "Please start your Spring Boot application first:"
    echo "  cd examples/spring-boot-memory-leak-demo"
    echo "  ./mvnw spring-boot:run"
    echo ""
    echo "Then try running this profiler again."
    exit 1
fi

# Count the number of processes
PROCESS_COUNT=$(echo "$JAVA_PROCESSES" | wc -l | xargs)

if [ "$PROCESS_COUNT" -eq 1 ]; then
    # Only one process found, auto-select it
    PID=$(echo "$JAVA_PROCESSES" | cut -d' ' -f1)
    PROCESS_NAME=$(echo "$JAVA_PROCESSES" | cut -d' ' -f2-)
    echo -e "${GREEN}Found single Java process:${NC}"
    echo "  PID: $PID"
    echo "  Name: $PROCESS_NAME"
    echo ""
    read -p "Do you want to profile this process? (y/N): " CONFIRM
    if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
        echo "Profiling cancelled by user."
        exit 0
    fi
else
    # Multiple processes found, provide selection menu
    echo -e "${GREEN}Found $PROCESS_COUNT Java processes:${NC}"
    echo ""

    # Create numbered list
    i=1
    declare -a PIDS
    declare -a NAMES

    while IFS= read -r line; do
        pid=$(echo "$line" | cut -d' ' -f1)
        name=$(echo "$line" | cut -d' ' -f2-)
        PIDS[$i]=$pid
        NAMES[$i]=$name
        echo "  $i) PID: $pid - $name"
        ((i++))
    done <<< "$JAVA_PROCESSES"

    echo ""
    echo "0) Manual PID entry"
    echo ""

    # Get user selection
    while true; do
        read -p "Select a process to profile (0-$PROCESS_COUNT): " SELECTION

        if [[ "$SELECTION" =~ ^[0-9]+$ ]]; then
            if [ "$SELECTION" -eq 0 ]; then
                # Manual PID entry
                read -p "Enter the PID of the Java process to profile: " PID
                break
            elif [ "$SELECTION" -ge 1 ] && [ "$SELECTION" -le "$PROCESS_COUNT" ]; then
                # Valid selection
                PID=${PIDS[$SELECTION]}
                PROCESS_NAME=${NAMES[$SELECTION]}
                echo -e "${GREEN}Selected process:${NC}"
                echo "  PID: $PID"
                echo "  Name: $PROCESS_NAME"
                break
            else
                echo -e "${RED}Invalid selection. Please choose a number between 0 and $PROCESS_COUNT.${NC}"
            fi
        else
            echo -e "${RED}Invalid input. Please enter a number.${NC}"
        fi
    done
fi

# Validate PID
if ! kill -0 "$PID" 2>/dev/null; then
    echo -e "${RED}Error: Process $PID does not exist or is not accessible${NC}"
    exit 1
fi

# Double-check if it's a Java process (for manual entries)
if ! jps | grep -q "^$PID "; then
    echo -e "${RED}Error: Process $PID is not a Java process${NC}"
    exit 1
fi

echo -e "${GREEN}Ready to profile process $PID${NC}"

# Step 2: Determine profiler directory (default to current script's parent directory)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROFILER_DIR="$(dirname "$SCRIPT_DIR")"

echo -e "${YELLOW}Step 2: Profiler Directory Setup${NC}"
echo "-----"
echo -e "${GREEN}Using profiler directory: $PROFILER_DIR${NC}"

# Step 3: Detect OS and download profiler
echo -e "${YELLOW}Step 3: Setting up async-profiler${NC}"
echo "-----"

detect_os_arch() {
    OS=$(uname -s | tr '[:upper:]' '[:lower:]')
    ARCH=$(uname -m)

    case "$OS" in
        linux*)
            case "$ARCH" in
                x86_64) PLATFORM="linux-x64" ;;
                aarch64|arm64) PLATFORM="linux-arm64" ;;
                *) echo -e "${RED}Unsupported architecture: $ARCH${NC}"; exit 1 ;;
            esac
            ;;
        darwin*)
            case "$ARCH" in
                x86_64|arm64) PLATFORM="macos" ;;
                *) echo -e "${RED}Unsupported architecture: $ARCH${NC}"; exit 1 ;;
            esac
            ;;
        *)
            echo -e "${RED}Unsupported operating system: $OS${NC}"
            exit 1
            ;;
    esac

    echo -e "${GREEN}Detected platform: $PLATFORM${NC}"
}

download_profiler() {
    local platform=$1
    local profiler_dir=$2
    local version="4.1"

    # For macOS, v4.0 uses .zip format, Linux still uses .tar.gz
    if [[ "$platform" == "macos" ]]; then
        local filename="async-profiler-$version-$platform.zip"
        local extract_cmd="unzip -q"
    else
        local filename="async-profiler-$version-$platform.tar.gz"
        local extract_cmd="tar -xzf"
    fi

    local url="https://github.com/async-profiler/async-profiler/releases/download/v$version/$filename"

    if [ ! -d "$profiler_dir/current" ]; then
        echo "Downloading async-profiler..."
        echo "URL: $url"
        mkdir -p "$profiler_dir"
        cd "$profiler_dir"

        # Remove any failed downloads
        rm -f async-profiler-*.tar.gz async-profiler-*.zip 2>/dev/null

        if command -v curl >/dev/null 2>&1; then
            curl -L -o "$filename" "$url"
        elif command -v wget >/dev/null 2>&1; then
            wget -O "$filename" "$url"
        else
            echo -e "${RED}Error: Neither curl nor wget is available${NC}"
            exit 1
        fi

        # Check if download was successful
        if [[ ! -f "$filename" ]]; then
            echo -e "${RED}Download failed: File $filename not found${NC}"
            exit 1
        fi

        # Check file size (should be larger than 1MB for a valid download)
        local file_size
        if [[ "$OSTYPE" == "darwin"* ]]; then
            file_size=$(stat -f%z "$filename" 2>/dev/null || echo "0")
        else
            file_size=$(stat -c%s "$filename" 2>/dev/null || echo "0")
        fi

        if [[ "$file_size" -lt 100000 ]]; then  # Less than 100KB
            echo -e "${RED}Download failed: File is too small ($file_size bytes). This usually means a redirect or error page was downloaded.${NC}"
            echo -e "${YELLOW}Contents of downloaded file:${NC}"
            head -10 "$filename" 2>/dev/null || echo "Cannot read file"
            rm -f "$filename"
            exit 1
        fi

        # Extract the archive
        echo "Extracting $filename..."
        $extract_cmd "$filename"

        if [[ $? -ne 0 ]]; then
            echo -e "${RED}Failed to extract $filename${NC}"
            rm -f "$filename"
            exit 1
        fi

        # Create symbolic link
        ln -sf "async-profiler-$version-$platform" current

        # Clean up archive
        rm -f "$filename"

        cd - > /dev/null
        echo -e "${GREEN}Async-profiler downloaded successfully to $profiler_dir${NC}"
    else
        echo -e "${GREEN}Async-profiler already available at $profiler_dir${NC}"
    fi
}

detect_os_arch
download_profiler "$PLATFORM" "$PROFILER_DIR"

# Create results directory if it doesn't exist (inside profiler directory)
RESULTS_DIR="$PROFILER_DIR/results"
mkdir -p "$RESULTS_DIR"

# Function to check platform capabilities
check_platform_capabilities() {
    echo -e "${BLUE}Platform Capabilities Check:${NC}"
    echo "-----"

    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo -e "${YELLOW}Platform: macOS${NC}"
        echo "✅ CPU profiling (limited to user space)"
        echo "✅ Memory allocation profiling"
        echo "✅ Native memory profiling (NEW: Full support on macOS in v4.1)"
        echo "✅ Lock contention profiling"
        echo "✅ Wall clock profiling"
        echo "✅ JFR format with OpenTelemetry support"
        echo "✅ JDK 25 compatibility"
        echo "❌ Hardware performance counters (Linux only)"
        echo ""
        echo -e "${BLUE}Note: macOS profiling is limited to user-space code only${NC}"
    else
        echo -e "${YELLOW}Platform: Linux${NC}"
        echo "✅ Full CPU profiling (user + kernel space)"
        echo "✅ Memory allocation profiling"
        echo "✅ Native memory profiling (enhanced with jemalloc support)"
        echo "✅ Lock contention profiling"
        echo "✅ Wall clock profiling"
        echo "✅ Hardware performance counters"
        echo "✅ JFR format with OpenTelemetry support"
        echo "✅ JDK 25 compatibility"

        # Check if running as root or with proper permissions
        if [[ $EUID -eq 0 ]]; then
            echo "✅ Running with elevated privileges"
        else
            echo -e "${YELLOW}⚠️  For optimal profiling, consider running with elevated privileges${NC}"
        fi
    fi
    echo ""
}

# Check platform capabilities
check_platform_capabilities

# Function to show profiling menu
show_profiling_menu() {
    echo ""
    echo -e "${BLUE}===========================================${NC}"
    echo -e "${YELLOW}Profiling Options for PID: $PID${NC}"
    echo -e "${YELLOW}Process: $PROCESS_NAME${NC}"
    echo -e "${YELLOW}Problem Category: $PROBLEM_CATEGORY${NC}"
    echo -e "${YELLOW}Suggested Approach: $SUGGESTED_PROFILE${NC}"
    echo -e "${BLUE}===========================================${NC}"

    # Show recommended options first based on problem category
    case $SUGGESTED_PROFILE in
        "Memory")
            echo -e "${GREEN}*** RECOMMENDED FOR MEMORY LEAK DETECTION ***${NC}"
            echo "2. Memory Allocation Profiling (30s) [Async-Profiler] ⭐"
            echo "8. Memory Leak Detection (5min) [Async-Profiler] ⭐"
            echo "5. Native Memory Profiling (30s) [Async-Profiler] ⭐"
            echo "12. All Events Profiling (30s) [Async-Profiler] ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "CPU")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "1. CPU Profiling (30s) [Async-Profiler] ⭐"
            echo "7. Custom Duration CPU Profiling [Async-Profiler] ⭐"
            echo "12. All Events Profiling (30s) [Async-Profiler] ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "Lock/Threading")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "3. Lock Contention Profiling (30s) [Async-Profiler] ⭐"
            echo "4. Wall Clock Profiling (30s) [Async-Profiler] ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "GC Analysis")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "13. Garbage Collection Log (custom duration) [jcmd/jstat] ⭐"
            echo "5. Interactive Heatmap (60s) [Async-Profiler + JFRconv] ⭐"
            echo "2. Memory Allocation Profiling (30s) [Async-Profiler] ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "I/O Analysis")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "4. Wall Clock Profiling (30s) [Async-Profiler] ⭐"
            echo "5. Interactive Heatmap (60s) [Async-Profiler + JFRconv] ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
    esac

    echo ""
    echo -e "${GREEN}=== ASYNC-PROFILER OPTIONS ===${NC}"
    echo "1. CPU Profiling (30s) [Async-Profiler]"
    echo "2. Memory Allocation Profiling (30s) [Async-Profiler]"
    echo "3. Lock Contention Profiling (30s) [Async-Profiler]"
    echo "4. Wall Clock Profiling (30s) [Async-Profiler]"
    echo "5. Native Memory Profiling (30s) [Async-Profiler] - NEW: Full macOS support in v4.1"
    echo "6. Inverted Flame Graph (30s) [Async-Profiler] - NEW in v4.0"
    echo "7. Custom Duration CPU Profiling [Async-Profiler]"
    echo "8. Memory Leak Detection (5min) [Async-Profiler] - Enhanced in v4.1"
    echo "9. Complete Memory Analysis Workflow [Async-Profiler]"
    echo "10. JFR Recording (custom duration) [Async-Profiler]"
    echo "11. Interactive Heatmap (60s) [Async-Profiler + JFRconv] - NEW in v4.0"
    echo "12. All Events Profiling (30s) [Async-Profiler] - NEW in v4.1"
    echo "13. OpenTelemetry OTLP Export [Async-Profiler] - NEW in v4.1"
    echo ""
    echo -e "${BLUE}=== JCMD OPTIONS ===${NC}"
    echo "14. Enhanced JFR Memory Profiling (Java 21+) [jcmd] - NEW"
    echo "15. Java 25 CPU-Time Profiling (Linux only) [jcmd] - NEW"
    echo "16. Java 25 Method Tracing [jcmd] - NEW"
    echo "17. Advanced JFR with Custom Events [jcmd] - NEW"
    echo "18. JFR Memory Leak Analysis with TLAB tracking [jcmd] - NEW"
    echo "19. Garbage Collection Log (custom duration) [jcmd/jstat]"
    echo ""
    echo -e "${YELLOW}=== SYSTEM TOOLS ===${NC}"
    echo "20. Thread Dump (instant snapshot) [jstack/Async-Profiler]"
    echo "21. View recent results [Built-in File Explorer]"
    echo "22. Kill current process [kill command] - NEW"
    echo "0. Exit profiler"
    echo -e "${BLUE}===========================================${NC}"
}

# Function to handle profiling errors
handle_profiling_error() {
    local exit_code=$1
    local profiling_type=$2

    if [[ $exit_code -ne 0 ]]; then
        echo -e "${RED}Profiling failed with exit code: $exit_code${NC}"
        echo -e "${YELLOW}Common solutions:${NC}"

        if [[ "$OSTYPE" == "darwin"* ]]; then
            echo "• On macOS, some profiling modes have limitations"
            echo "• Try using --all-user flag for CPU profiling"
            echo "• Use allocation profiling instead of native memory profiling"
        else
            echo "• Check if you have sufficient permissions:"
            echo "  sudo sysctl kernel.perf_event_paranoid=1"
            echo "  sudo sysctl kernel.kptr_restrict=0"
            echo "• Try running with sudo for full system profiling"
            echo "• Use --all-user flag to profile only user-space code"
        fi

        echo -e "${BLUE}Alternative: Try option 2 (Memory Allocation Profiling) which works on all platforms${NC}"
        return 1
    fi
    return 0
}

# Function to detect Java version
detect_java_version() {
    local pid=$1
    local java_version

    # Try to get Java version from the running process
    if command -v jcmd >/dev/null 2>&1; then
        java_version=$(jcmd "$pid" VM.version 2>/dev/null | grep -E "Java|OpenJDK" | head -1)
        if [[ "$java_version" =~ ([0-9]+) ]]; then
            echo "${BASH_REMATCH[1]}"
            return 0
        fi
    fi

    # Fallback: try to detect from process command line
    if command -v ps >/dev/null 2>&1; then
        local java_cmd=$(ps -p "$pid" -o args= 2>/dev/null | head -1)
        if [[ "$java_cmd" =~ java.*/([0-9]+) ]]; then
            echo "${BASH_REMATCH[1]}"
            return 0
        fi
    fi

    # Default assumption for modern systems
    echo "17"
}

# Function to execute profiling
execute_profiling() {
    local option=$1
    local java_version=$(detect_java_version "$PID")

    case $option in
        1)
            echo -e "${GREEN}Starting CPU profiling for 30 seconds...${NC}"
            echo -e "${BLUE}Enhanced in v4.1: Records which CPU core each sample was taken on${NC}"
            # On macOS, add --all-user flag to avoid kernel profiling issues
            if [[ "$OSTYPE" == "darwin"* ]]; then
                "$PROFILER_DIR/current/bin/asprof" -d 30 --all-user -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
                handle_profiling_error $? "CPU (macOS)"
            else
                "$PROFILER_DIR/current/bin/asprof" -d 30 -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
                handle_profiling_error $? "CPU (Linux)"
            fi
            ;;
        2)
            echo -e "${GREEN}Starting memory allocation profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 30 -f "$RESULTS_DIR/allocation-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        3)
            echo -e "${GREEN}Starting lock contention profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e lock -d 30 -f "$RESULTS_DIR/lock-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        4)
            echo -e "${GREEN}Starting wall clock profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e wall -d 30 -f "$RESULTS_DIR/wall-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        5)
            echo -e "${GREEN}Starting native memory profiling for 30 seconds...${NC}"
            if [[ "$OSTYPE" == "darwin"* ]]; then
                echo -e "${BLUE}Using native memory profiling with full macOS support (v4.1)...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e nativemem -d 30 -f "$RESULTS_DIR/native-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
            else
                echo -e "${BLUE}Using native memory profiling with jemalloc support...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e nativemem -d 30 -f "$RESULTS_DIR/native-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
            fi
            ;;
        6)
            echo -e "${GREEN}Starting inverted flame graph profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 30 --inverted -f "$RESULTS_DIR/inverted-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        7)
            read -p "Enter duration in seconds: " DURATION
            echo -e "${GREEN}Starting CPU profiling for $DURATION seconds...${NC}"
            # On macOS, add --all-user flag to avoid kernel profiling issues
            if [[ "$OSTYPE" == "darwin"* ]]; then
                "$PROFILER_DIR/current/bin/asprof" -d "$DURATION" --all-user -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            else
                "$PROFILER_DIR/current/bin/asprof" -d "$DURATION" -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            fi
            ;;
        8)
            echo -e "${GREEN}Starting Memory Leak Detection (5 minutes)...${NC}"
            echo -e "${YELLOW}This will run for 5 minutes to capture memory leak patterns${NC}"
            echo -e "${BLUE}Using enhanced leak detection: focusing on allocation patterns over time${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 300 --alloc 1m -f "$RESULTS_DIR/memory-leak-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        9)
            echo -e "${GREEN}Starting Complete Memory Analysis Workflow...${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)

            echo -e "${BLUE}Step 1: Quick memory allocation baseline (30s)...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 30 -f "$RESULTS_DIR/memory-baseline-$TIMESTAMP.html" "$PID"

            echo -e "${BLUE}Step 2: Heap profiling (60s)...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 60 -f "$RESULTS_DIR/heap-analysis-$TIMESTAMP.html" "$PID"

            echo -e "${BLUE}Step 3: Memory leak detection (5min) - Long-term allocation analysis...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 300 --alloc 1m -f "$RESULTS_DIR/memory-leak-complete-$TIMESTAMP.html" "$PID"

            echo -e "${GREEN}Complete memory analysis finished! Check these files:${NC}"
            echo "- memory-baseline-$TIMESTAMP.html (30s baseline)"
            echo "- heap-analysis-$TIMESTAMP.html (60s detailed heap)"
            echo "- memory-leak-complete-$TIMESTAMP.html (5min leak detection)"
            ;;
        10)
            echo -e "${GREEN}Creating JFR Recording...${NC}"
            read -p "Enter duration in seconds (default: 60): " JFR_DURATION
            JFR_DURATION=${JFR_DURATION:-60}

            if ! [[ "$JFR_DURATION" =~ ^[0-9]+$ ]] || [ "$JFR_DURATION" -lt 1 ]; then
                echo -e "${RED}Invalid duration. Using default 60 seconds.${NC}"
                JFR_DURATION=60
            fi

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/recording-${JFR_DURATION}s-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting JFR recording for $JFR_DURATION seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d "$JFR_DURATION" -o jfr -f "$JFR_FILE" "$PID"

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}JFR recording completed: $JFR_FILE${NC}"
                echo -e "${YELLOW}You can analyze this JFR file with:${NC}"
                echo "  - JProfiler"
                echo "  - VisualVM"
                echo "  - Mission Control"
                echo "  - jfrconv (included with async-profiler)"
                echo "  - NEW in v4.1: Convert to OTLP format for OpenTelemetry integration"
            else
                echo -e "${RED}JFR recording failed${NC}"
            fi
            ;;
        11)
            echo -e "${GREEN}Starting interactive heatmap profiling for 60 seconds...${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/profile-$TIMESTAMP.jfr"
            HEATMAP_FILE="$RESULTS_DIR/heatmap-cpu-$TIMESTAMP.html"

            echo -e "${BLUE}Step 1: Generating JFR recording...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 60 -o jfr -f "$JFR_FILE" "$PID"

            echo -e "${BLUE}Step 2: Converting JFR to heatmap...${NC}"
            # jfrconv is now a shell script in v4.1
            bash "$PROFILER_DIR/current/bin/jfrconv" --cpu -o heatmap "$JFR_FILE" "$HEATMAP_FILE"
            echo -e "${GREEN}Heatmap generated: $HEATMAP_FILE${NC}"
            ;;
        12)
            echo -e "${GREEN}Starting All Events Profiling for 30 seconds...${NC}"
            echo -e "${BLUE}This will collect all possible events simultaneously (CPU, alloc, lock, wall)${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/all-events-$TIMESTAMP.jfr"
            HTML_FILE="$RESULTS_DIR/all-events-$TIMESTAMP.html"

            echo -e "${BLUE}Step 1: Recording all events to JFR format...${NC}"
            "$PROFILER_DIR/current/bin/asprof" --all -d 30 -o jfr -f "$JFR_FILE" "$PID"

            if [ $? -eq 0 ] && [ -f "$JFR_FILE" ]; then
                echo -e "${BLUE}Step 2: Converting JFR to HTML flame graph...${NC}"
                # Use the converter tool to create flame graph from JFR
                if [ -f "$PROFILER_DIR/current/bin/converter.jar" ]; then
                    java -jar "$PROFILER_DIR/current/bin/converter.jar" jfr2flame "$JFR_FILE" "$HTML_FILE"
                elif [ -f "$PROFILER_DIR/current/bin/jfrconv" ]; then
                    bash "$PROFILER_DIR/current/bin/jfrconv" -o html "$JFR_FILE" "$HTML_FILE"
                else
                    echo -e "${YELLOW}JFR converter not found, skipping HTML conversion${NC}"
                    echo -e "${BLUE}You can analyze the JFR file with JProfiler, VisualVM, or Mission Control${NC}"
                fi

                echo -e "${GREEN}All events profiling completed!${NC}"
                echo -e "${YELLOW}Generated files:${NC}"
                echo "  - JFR recording: $JFR_FILE"
                if [ -f "$HTML_FILE" ]; then
                    echo "  - HTML flame graph: $HTML_FILE"
                else
                    echo "  - HTML conversion skipped (use JFR analysis tools instead)"
                fi
            else
                echo -e "${RED}All events profiling failed${NC}"
            fi
            ;;
        13)
            echo -e "${GREEN}Starting OpenTelemetry OTLP Export...${NC}"
            read -p "Enter duration in seconds (default: 60): " OTLP_DURATION
            OTLP_DURATION=${OTLP_DURATION:-60}

            if ! [[ "$OTLP_DURATION" =~ ^[0-9]+$ ]] || [ "$OTLP_DURATION" -lt 1 ]; then
                echo -e "${RED}Invalid duration. Using default 60 seconds.${NC}"
                OTLP_DURATION=60
            fi

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            OTLP_FILE="$RESULTS_DIR/otlp-export-$TIMESTAMP.json"

            echo -e "${BLUE}Generating OTLP format for OpenTelemetry integration...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d "$OTLP_DURATION" -o otlp -f "$OTLP_FILE" "$PID"

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}OTLP export completed: $OTLP_FILE${NC}"
                echo -e "${YELLOW}This file can be imported into OpenTelemetry-compatible systems${NC}"
                echo -e "${BLUE}File size: $(wc -c < "$OTLP_FILE" 2>/dev/null || echo "unknown") bytes${NC}"
            else
                echo -e "${RED}OTLP export failed${NC}"
            fi
            ;;
        14)
            echo -e "${GREEN}Starting Enhanced JFR Memory Profiling (Java 21+)...${NC}"
            if [ "$java_version" -lt 21 ]; then
                echo -e "${YELLOW}Warning: This feature requires Java 21+. Current version: $java_version${NC}"
                echo -e "${BLUE}Falling back to standard memory profiling...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e alloc -d 60 -f "$RESULTS_DIR/memory-fallback-$(date +%Y%m%d-%H%M%S).html" "$PID"
                return
            fi

            read -p "Enter duration in seconds (default: 120): " MEMORY_DURATION
            MEMORY_DURATION=${MEMORY_DURATION:-120}

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/enhanced-memory-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting enhanced JFR memory profiling with modern events...${NC}"

            # Use jcmd for more precise control over JFR events
            echo -e "${BLUE}Step 1: Starting JFR recording with memory-specific events...${NC}"
            jcmd "$PID" JFR.start name=memory-analysis duration="${MEMORY_DURATION}s" \
                settings=profile \
                "jdk.ObjectAllocationInNewTLAB#enabled=true" \
                "jdk.ObjectAllocationOutsideTLAB#enabled=true" \
                "jdk.ObjectAllocationSample#enabled=true" \
                "jdk.GCHeapSummary#enabled=true" \
                "jdk.GCConfiguration#enabled=true" \
                "jdk.YoungGenerationConfiguration#enabled=true" \
                "jdk.OldGenerationConfiguration#enabled=true" \
                "jdk.GCCollectorG1GC#enabled=true" \
                "jdk.G1HeapRegionInformation#enabled=true" \
                filename="$JFR_FILE" 2>/dev/null

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}JFR memory recording started successfully${NC}"
                echo "Recording for $MEMORY_DURATION seconds..."

                # Show progress
                for i in $(seq 1 $((MEMORY_DURATION/10))); do
                    echo -n "."
                    sleep 10
                done
                echo ""

                # Stop recording
                jcmd "$PID" JFR.stop name=memory-analysis 2>/dev/null
                echo -e "${GREEN}Enhanced JFR memory profiling completed: $JFR_FILE${NC}"
            else
                echo -e "${YELLOW}jcmd failed, falling back to async-profiler...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e alloc -d "$MEMORY_DURATION" -f "$RESULTS_DIR/memory-enhanced-fallback-$TIMESTAMP.html" "$PID"
            fi
            ;;
        15)
            echo -e "${GREEN}Starting Java 25 CPU-Time Profiling (Experimental)...${NC}"
            if [ "$java_version" -lt 25 ]; then
                echo -e "${YELLOW}Warning: This feature requires Java 25+. Current version: $java_version${NC}"
                echo -e "${BLUE}Falling back to standard CPU profiling...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -d 60 -f "$RESULTS_DIR/cpu-fallback-$(date +%Y%m%d-%H%M%S).html" "$PID"
                return
            fi

            if [[ "$OSTYPE" != "linux"* ]]; then
                echo -e "${RED}Error: Java 25 CPU-Time profiling is only available on Linux${NC}"
                echo -e "${BLUE}Falling back to standard CPU profiling...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -d 60 -f "$RESULTS_DIR/cpu-fallback-$(date +%Y%m%d-%H%M%S).html" "$PID"
                return
            fi

            read -p "Enter duration in seconds (default: 60): " CPU_DURATION
            CPU_DURATION=${CPU_DURATION:-60}

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/cpu-time-java25-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting Java 25 CPU-Time sampling (includes native code)...${NC}"

            # Use jcmd to enable the experimental CPU-time sampling
            jcmd "$PID" JFR.start name=cpu-time-analysis duration="${CPU_DURATION}s" \
                "jdk.CPUTimeSample#enabled=true" \
                "jdk.ExecutionSample#enabled=true" \
                "jdk.NativeMethodSample#enabled=true" \
                "jdk.ThreadCPULoad#enabled=true" \
                filename="$JFR_FILE" 2>/dev/null

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}Java 25 CPU-time recording started successfully${NC}"
                echo "Recording CPU time (including native code) for $CPU_DURATION seconds..."

                # Show progress
                for i in $(seq 1 $((CPU_DURATION/5))); do
                    echo -n "."
                    sleep 5
                done
                echo ""

                # Stop recording
                jcmd "$PID" JFR.stop name=cpu-time-analysis 2>/dev/null
                echo -e "${GREEN}Java 25 CPU-time profiling completed: $JFR_FILE${NC}"
                echo -e "${BLUE}This recording includes native code execution time${NC}"
            else
                echo -e "${YELLOW}Java 25 CPU-time profiling failed, falling back to async-profiler...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -d "$CPU_DURATION" -f "$RESULTS_DIR/cpu-time-fallback-$TIMESTAMP.html" "$PID"
            fi
            ;;
        16)
            echo -e "${GREEN}Starting Java 25 Method Tracing...${NC}"
            if [ "$java_version" -lt 25 ]; then
                echo -e "${YELLOW}Warning: This feature requires Java 25+. Current version: $java_version${NC}"
                echo -e "${BLUE}Falling back to standard profiling...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -d 60 -f "$RESULTS_DIR/method-fallback-$(date +%Y%m%d-%H%M%S).html" "$PID"
                return
            fi

            read -p "Enter duration in seconds (default: 30): " METHOD_DURATION
            METHOD_DURATION=${METHOD_DURATION:-30}

            echo -e "${YELLOW}Enter method pattern to trace (e.g., 'com.example.*' or '*' for all):${NC}"
            read -p "Method pattern: " METHOD_PATTERN
            METHOD_PATTERN=${METHOD_PATTERN:-"*"}

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/method-trace-java25-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting Java 25 method tracing for pattern: $METHOD_PATTERN${NC}"

            # Use jcmd to enable method tracing events
            jcmd "$PID" JFR.start name=method-trace-analysis duration="${METHOD_DURATION}s" \
                "jdk.MethodSample#enabled=true" \
                "jdk.MethodEntry#enabled=true,threshold=1ms" \
                "jdk.MethodExit#enabled=true,threshold=1ms" \
                "jdk.ExecutionSample#enabled=true" \
                filename="$JFR_FILE" 2>/dev/null

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}Java 25 method tracing started successfully${NC}"
                echo "Tracing methods matching '$METHOD_PATTERN' for $METHOD_DURATION seconds..."

                # Show progress
                for i in $(seq 1 $METHOD_DURATION); do
                    echo -n "."
                    sleep 1
                done
                echo ""

                # Stop recording
                jcmd "$PID" JFR.stop name=method-trace-analysis 2>/dev/null
                echo -e "${GREEN}Java 25 method tracing completed: $JFR_FILE${NC}"
                echo -e "${BLUE}This recording includes detailed method entry/exit timing${NC}"
            else
                echo -e "${YELLOW}Java 25 method tracing failed, falling back to async-profiler...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -d "$METHOD_DURATION" -f "$RESULTS_DIR/method-trace-fallback-$TIMESTAMP.html" "$PID"
            fi
            ;;
        17)
            echo -e "${GREEN}Starting Advanced JFR with Custom Events...${NC}"

            read -p "Enter duration in seconds (default: 120): " CUSTOM_DURATION
            CUSTOM_DURATION=${CUSTOM_DURATION:-120}

            read -p "Enter max recording size (e.g., 100MB, default: 500MB): " MAX_SIZE
            MAX_SIZE=${MAX_SIZE:-"500MB"}

            read -p "Enter max age for events (e.g., 5m, 1h, default: 10m): " MAX_AGE
            MAX_AGE=${MAX_AGE:-"10m"}

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/advanced-custom-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting advanced JFR recording with custom configuration...${NC}"
            echo -e "${BLUE}Max size: $MAX_SIZE, Max age: $MAX_AGE${NC}"

            # Create comprehensive JFR recording with all available events
            jcmd "$PID" JFR.start name=advanced-analysis duration="${CUSTOM_DURATION}s" \
                disk=true maxsize="$MAX_SIZE" maxage="$MAX_AGE" \
                "jdk.ObjectAllocationInNewTLAB#enabled=true" \
                "jdk.ObjectAllocationOutsideTLAB#enabled=true" \
                "jdk.ObjectAllocationSample#enabled=true,throttle=1000/s" \
                "jdk.GCHeapSummary#enabled=true" \
                "jdk.GCConfiguration#enabled=true" \
                "jdk.ExecutionSample#enabled=true" \
                "jdk.ThreadCPULoad#enabled=true" \
                "jdk.ThreadContextSwitchRate#enabled=true" \
                "jdk.JavaMonitorEnter#enabled=true,threshold=10ms" \
                "jdk.JavaMonitorWait#enabled=true,threshold=10ms" \
                "jdk.ThreadPark#enabled=true,threshold=10ms" \
                "jdk.SocketRead#enabled=true,threshold=10ms" \
                "jdk.SocketWrite#enabled=true,threshold=10ms" \
                "jdk.FileRead#enabled=true,threshold=10ms" \
                "jdk.FileWrite#enabled=true,threshold=10ms" \
                filename="$JFR_FILE" 2>/dev/null

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}Advanced JFR recording started successfully${NC}"
                echo "Recording comprehensive events for $CUSTOM_DURATION seconds..."

                # Show progress with more frequent updates
                for i in $(seq 1 $((CUSTOM_DURATION/5))); do
                    echo -n "."
                    sleep 5
                done
                echo ""

                # Stop recording
                jcmd "$PID" JFR.stop name=advanced-analysis 2>/dev/null
                echo -e "${GREEN}Advanced JFR recording completed: $JFR_FILE${NC}"
                echo -e "${BLUE}This recording includes I/O, locking, GC, and allocation events${NC}"
            else
                echo -e "${YELLOW}Advanced JFR recording failed, falling back to async-profiler...${NC}"
                "$PROFILER_DIR/current/bin/asprof" --all -d "$CUSTOM_DURATION" -o jfr -f "$RESULTS_DIR/advanced-fallback-$TIMESTAMP.jfr" "$PID"
            fi
            ;;
        18)
            echo -e "${GREEN}Starting JFR Memory Leak Analysis with TLAB tracking...${NC}"

            read -p "Enter duration in minutes (default: 10): " LEAK_DURATION_MIN
            LEAK_DURATION_MIN=${LEAK_DURATION_MIN:-10}
            LEAK_DURATION=$((LEAK_DURATION_MIN * 60))

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/memory-leak-tlab-$TIMESTAMP.jfr"

            echo -e "${BLUE}Starting comprehensive memory leak analysis...${NC}"
            echo -e "${BLUE}Duration: $LEAK_DURATION_MIN minutes${NC}"

            # Start JFR recording focused on memory leak detection
            jcmd "$PID" JFR.start name=memory-leak-analysis duration="${LEAK_DURATION}s" \
                disk=true maxsize="1GB" maxage="30m" \
                "jdk.ObjectAllocationInNewTLAB#enabled=true,stackTrace=true" \
                "jdk.ObjectAllocationOutsideTLAB#enabled=true,stackTrace=true" \
                "jdk.ObjectAllocationSample#enabled=true,stackTrace=true" \
                "jdk.TLABAllocation#enabled=true" \
                "jdk.TLABWaste#enabled=true" \
                "jdk.GCHeapSummary#enabled=true,period=10s" \
                "jdk.GCCollectorG1GC#enabled=true" \
                "jdk.G1HeapRegionInformation#enabled=true" \
                "jdk.G1HeapRegionTypeChange#enabled=true" \
                "jdk.OldObjectSample#enabled=true,cutoff=0ms" \
                "jdk.ClassLoaderStatistics#enabled=true,period=30s" \
                filename="$JFR_FILE" 2>/dev/null

            if [ $? -eq 0 ]; then
                echo -e "${GREEN}Memory leak analysis recording started successfully${NC}"
                echo "Analyzing memory allocation patterns for $LEAK_DURATION_MIN minutes..."
                echo -e "${YELLOW}This will track TLAB allocation, object samples, and heap changes${NC}"

                # Show progress with time remaining
                for i in $(seq 1 $LEAK_DURATION_MIN); do
                    remaining=$((LEAK_DURATION_MIN - i))
                    echo -e "\\r${BLUE}Recording... ${remaining} minutes remaining${NC}"
                    sleep 60
                done
                echo ""

                # Stop recording
                jcmd "$PID" JFR.stop name=memory-leak-analysis 2>/dev/null
                echo -e "${GREEN}Memory leak analysis completed: $JFR_FILE${NC}"
                echo -e "${BLUE}This recording includes detailed TLAB and object allocation tracking${NC}"
                echo -e "${YELLOW}Analyze with JProfiler, VisualVM, or Mission Control for leak detection${NC}"
            else
                echo -e "${YELLOW}JFR memory leak recording failed, falling back to async-profiler...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e alloc -d "$LEAK_DURATION" --alloc 1m -f "$RESULTS_DIR/memory-leak-fallback-$TIMESTAMP.html" "$PID"
            fi
            ;;
        19)
            echo -e "${GREEN}Collecting Garbage Collection Logs...${NC}"
            read -p "Enter duration in seconds (default: 300): " GC_DURATION
            GC_DURATION=${GC_DURATION:-300}

            if ! [[ "$GC_DURATION" =~ ^[0-9]+$ ]] || [ "$GC_DURATION" -lt 1 ]; then
                echo -e "${RED}Invalid duration. Using default 300 seconds.${NC}"
                GC_DURATION=300
            fi

            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            GC_LOG_FILE="$RESULTS_DIR/gc-${GC_DURATION}s-$TIMESTAMP.log"

            echo -e "${BLUE}Collecting GC logs for $GC_DURATION seconds...${NC}"

            # Get Java version to determine the best approach
            JAVA_VERSION=$(jcmd "$PID" VM.version 2>/dev/null | grep -E "Java version|OpenJDK" | head -1)

            # Try different approaches for GC logging
            GC_SUCCESS=false

            # Method 1: Try jcmd with unified logging (Java 9+)
            if command -v jcmd >/dev/null 2>&1; then
                echo -e "${BLUE}Attempting to enable GC logging via jcmd...${NC}"

                # First, try to enable GC logging dynamically
                jcmd "$PID" VM.log output="$GC_LOG_FILE" what=gc 2>/dev/null
                if [ $? -eq 0 ]; then
                    echo -e "${GREEN}GC logging enabled via jcmd${NC}"
                    echo "Collecting logs for $GC_DURATION seconds..."
                    sleep "$GC_DURATION"

                    # Disable logging
                    jcmd "$PID" VM.log output=none what=gc 2>/dev/null
                    GC_SUCCESS=true
                fi
            fi

            # Method 2: Use jstat if jcmd failed
            if [ "$GC_SUCCESS" = false ]; then
                echo -e "${YELLOW}jcmd approach failed, using jstat for GC monitoring...${NC}"

                if command -v jstat >/dev/null 2>&1; then
                    echo -e "${BLUE}Using jstat to collect GC statistics...${NC}"

                    # Create header
                    echo "# GC Statistics collected via jstat" > "$GC_LOG_FILE"
                    echo "# Timestamp,S0C,S1C,S0U,S1U,EC,EU,OC,OU,MC,MU,CCSC,CCSU,YGC,YGCT,FGC,FGCT,GCT" >> "$GC_LOG_FILE"

                    # Collect GC stats every second for the specified duration
                    END_TIME=$(($(date +%s) + GC_DURATION))

                    while [ $(date +%s) -lt $END_TIME ]; do
                        TIMESTAMP_LOG=$(date '+%Y-%m-%d %H:%M:%S')
                        GC_STATS=$(jstat -gc "$PID" 2>/dev/null | tail -1)
                        if [ $? -eq 0 ] && [ ! -z "$GC_STATS" ]; then
                            echo "$TIMESTAMP_LOG,$GC_STATS" >> "$GC_LOG_FILE"
                        fi
                        sleep 1
                    done

                    if [ -s "$GC_LOG_FILE" ]; then
                        GC_SUCCESS=true
                        echo -e "${GREEN}GC statistics collection completed${NC}"
                    fi
                fi
            fi

            # Report results
            if [ "$GC_SUCCESS" = true ] && [ -f "$GC_LOG_FILE" ]; then
                echo -e "${GREEN}GC log collection completed: $GC_LOG_FILE${NC}"
                echo -e "${YELLOW}Log summary:${NC}"
                echo "  File: $GC_LOG_FILE"
                echo "  Size: $(wc -l < "$GC_LOG_FILE") lines"
                echo "  Duration: $GC_DURATION seconds"

                # Show a few sample lines
                echo -e "${BLUE}Sample log entries (first 5 lines):${NC}"
                head -5 "$GC_LOG_FILE" 2>/dev/null || echo "  (No content to preview)"
            else
                echo -e "${RED}GC log collection failed${NC}"
                echo -e "${YELLOW}This might happen if:${NC}"
                echo "  - The JVM doesn't have GC logging enabled"
                echo "  - Insufficient permissions to access GC logs"
                echo "  - The Java version doesn't support dynamic GC logging"

                # Clean up empty file
                [ -f "$GC_LOG_FILE" ] && [ ! -s "$GC_LOG_FILE" ] && rm -f "$GC_LOG_FILE"
            fi
            ;;
        20)
            echo -e "${GREEN}Creating Thread Dump...${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            THREAD_DUMP_FILE="$RESULTS_DIR/threaddump-$TIMESTAMP.txt"

            # Try jstack first (if available), then fall back to async-profiler
            if command -v jstack >/dev/null 2>&1; then
                echo -e "${BLUE}Using jstack to generate thread dump...${NC}"
                jstack "$PID" > "$THREAD_DUMP_FILE" 2>&1
                JSTACK_EXIT_CODE=$?

                if [ $JSTACK_EXIT_CODE -eq 0 ] && [ -s "$THREAD_DUMP_FILE" ]; then
                    echo -e "${GREEN}Thread dump completed: $THREAD_DUMP_FILE${NC}"
                else
                    echo -e "${YELLOW}jstack failed, trying async-profiler...${NC}"
                    "$PROFILER_DIR/current/bin/asprof" -d 1 -e cpu -o text -f "$THREAD_DUMP_FILE" "$PID" >/dev/null 2>&1
                    if [ $? -eq 0 ]; then
                        echo -e "${GREEN}Thread dump completed: $THREAD_DUMP_FILE${NC}"
                    else
                        echo -e "${RED}Thread dump failed with both methods${NC}"
                        rm -f "$THREAD_DUMP_FILE"
                    fi
                fi
            else
                echo -e "${BLUE}jstack not available, using async-profiler...${NC}"
                # Use async-profiler to get thread information
                "$PROFILER_DIR/current/bin/asprof" -d 1 -e cpu -o text -f "$THREAD_DUMP_FILE" "$PID" >/dev/null 2>&1
                if [ $? -eq 0 ]; then
                    echo -e "${GREEN}Thread dump completed: $THREAD_DUMP_FILE${NC}"
                else
                    echo -e "${RED}Thread dump failed${NC}"
                    rm -f "$THREAD_DUMP_FILE"
                fi
            fi

            if [ -f "$THREAD_DUMP_FILE" ]; then
                echo -e "${YELLOW}Thread dump summary:${NC}"
                echo "  File: $THREAD_DUMP_FILE"
                echo "  Size: $(wc -l < "$THREAD_DUMP_FILE") lines"
                echo -e "${BLUE}Use 'cat $THREAD_DUMP_FILE' to view the complete thread dump${NC}"
            fi
            ;;
        21)
            echo -e "${YELLOW}Recent profiling results in $RESULTS_DIR:${NC}"
            echo ""

            # Get all result files and create a numbered list
            ALL_FILES=($(ls -t "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr "$RESULTS_DIR"/*.txt "$RESULTS_DIR"/*.log "$RESULTS_DIR"/*.json 2>/dev/null || true))

            if [ ${#ALL_FILES[@]} -eq 0 ]; then
                echo "No profiling files found"
                return
            fi

            # Show numbered list of files
            echo -e "${GREEN}Select a file to open/explore:${NC}"
            echo ""

            for i in "${!ALL_FILES[@]}"; do
                file="${ALL_FILES[$i]}"
                filename=$(basename "$file")
                filesize=$(ls -lh "$file" | awk '{print $5}')
                filetime=$(ls -l "$file" | awk '{print $6, $7, $8}')

                # Determine file type and icon
                case "$filename" in
                    *.html) filetype="🔥 Flame Graph" ;;
                    *.jfr)  filetype="📊 JFR Recording" ;;
                    *.txt)  filetype="📋 Thread Dump" ;;
                    *.log)  filetype="📜 GC Log" ;;
                    *.json) filetype="🌐 OTLP Export" ;;
                    *)      filetype="📄 Unknown" ;;
                esac

                printf "%2d) %s - %s (%s) - %s\n" $((i+1)) "$filetype" "$filename" "$filesize" "$filetime"
            done

            echo ""
            echo -e "${BLUE}File type summary:${NC}"
            HTML_COUNT=$(ls "$RESULTS_DIR"/*.html 2>/dev/null | wc -l | xargs)
            JFR_COUNT=$(ls "$RESULTS_DIR"/*.jfr 2>/dev/null | wc -l | xargs)
            TXT_COUNT=$(ls "$RESULTS_DIR"/*.txt 2>/dev/null | wc -l | xargs)
            LOG_COUNT=$(ls "$RESULTS_DIR"/*.log 2>/dev/null | wc -l | xargs)
            JSON_COUNT=$(ls "$RESULTS_DIR"/*.json 2>/dev/null | wc -l | xargs)

            echo "  🔥 HTML files (flame graphs): $HTML_COUNT"
            echo "  📊 JFR files (recordings): $JFR_COUNT"
            echo "  📋 TXT files (thread dumps): $TXT_COUNT"
            echo "  📜 LOG files (GC logs): $LOG_COUNT"
            echo "  🌐 JSON files (OTLP exports): $JSON_COUNT"
            echo ""
            echo "0) Return to main menu"
            echo ""

            # Get user selection
            while true; do
                read -p "Select a file to open (0-${#ALL_FILES[@]}): " FILE_SELECTION

                if [[ "$FILE_SELECTION" =~ ^[0-9]+$ ]]; then
                    if [ "$FILE_SELECTION" -eq 0 ]; then
                        echo "Returning to main menu..."
                        return
                    elif [ "$FILE_SELECTION" -ge 1 ] && [ "$FILE_SELECTION" -le "${#ALL_FILES[@]}" ]; then
                        SELECTED_FILE="${ALL_FILES[$((FILE_SELECTION-1))]}"
                        SELECTED_FILENAME=$(basename "$SELECTED_FILE")

                        echo -e "${GREEN}Selected: $SELECTED_FILENAME${NC}"
                        echo ""

                        # Handle different file types
                        case "$SELECTED_FILENAME" in
                            *.html)
                                echo -e "${BLUE}Opening flame graph in browser...${NC}"
                                if [[ "$OSTYPE" == "darwin"* ]]; then
                                    open "$SELECTED_FILE"
                                elif [[ "$OSTYPE" == "linux"* ]]; then
                                    if command -v xdg-open >/dev/null 2>&1; then
                                        xdg-open "$SELECTED_FILE"
                                    else
                                        echo -e "${YELLOW}Please open this file manually in a browser: $SELECTED_FILE${NC}"
                                    fi
                                else
                                    echo -e "${YELLOW}Please open this file manually in a browser: $SELECTED_FILE${NC}"
                                fi
                                ;;
                            *.jfr)
                                echo -e "${BLUE}JFR Recording: $SELECTED_FILENAME${NC}"
                                echo -e "${YELLOW}File size: $(ls -lh "$SELECTED_FILE" | awk '{print $5}')${NC}"
                                echo ""
                                echo -e "${BLUE}You can analyze this JFR file with:${NC}"
                                echo "  1. JDK Mission Control"
                                echo "  2. JProfiler"
                                echo "  3. VisualVM"
                                echo "  4. Convert to flame graph using jfrconv"
                                echo ""
                                echo -e "${YELLOW}Would you like to convert it to a flame graph? (y/N):${NC}"
                                read -p "" CONVERT_JFR
                                if [[ "$CONVERT_JFR" =~ ^[Yy]$ ]]; then
                                    FLAME_FILE="${SELECTED_FILE%.jfr}.html"
                                    echo -e "${BLUE}Converting JFR to flame graph...${NC}"
                                    if [ -f "$PROFILER_DIR/current/bin/jfrconv" ]; then
                                        bash "$PROFILER_DIR/current/bin/jfrconv" -o html "$SELECTED_FILE" "$FLAME_FILE" 2>/dev/null
                                        if [ $? -eq 0 ]; then
                                            echo -e "${GREEN}Flame graph created: $(basename "$FLAME_FILE")${NC}"
                                            if [[ "$OSTYPE" == "darwin"* ]]; then
                                                echo -e "${BLUE}Opening flame graph in browser...${NC}"
                                                open "$FLAME_FILE"
                                            fi
                                        else
                                            echo -e "${RED}Failed to convert JFR file${NC}"
                                        fi
                                    else
                                        echo -e "${YELLOW}jfrconv not found. JFR file location: $SELECTED_FILE${NC}"
                                    fi
                                else
                                    echo -e "${YELLOW}JFR file location: $SELECTED_FILE${NC}"
                                fi
                                ;;
                            *.txt)
                                echo -e "${BLUE}Thread Dump: $SELECTED_FILENAME${NC}"
                                echo -e "${YELLOW}File size: $(ls -lh "$SELECTED_FILE" | awk '{print $5}')${NC}"
                                echo ""
                                echo -e "${YELLOW}Would you like to view the thread dump? (y/N):${NC}"
                                read -p "" VIEW_DUMP
                                if [[ "$VIEW_DUMP" =~ ^[Yy]$ ]]; then
                                    echo -e "${BLUE}Thread dump content:${NC}"
                                    echo "===================="
                                    cat "$SELECTED_FILE"
                                    echo "===================="
                                else
                                    echo -e "${YELLOW}Thread dump location: $SELECTED_FILE${NC}"
                                fi
                                ;;
                            *.log)
                                echo -e "${BLUE}GC Log: $SELECTED_FILENAME${NC}"
                                echo -e "${YELLOW}File size: $(ls -lh "$SELECTED_FILE" | awk '{print $5}')${NC}"
                                echo ""
                                echo -e "${YELLOW}Would you like to view the last 50 lines of the GC log? (y/N):${NC}"
                                read -p "" VIEW_LOG
                                if [[ "$VIEW_LOG" =~ ^[Yy]$ ]]; then
                                    echo -e "${BLUE}GC log (last 50 lines):${NC}"
                                    echo "===================="
                                    tail -50 "$SELECTED_FILE"
                                    echo "===================="
                                else
                                    echo -e "${YELLOW}GC log location: $SELECTED_FILE${NC}"
                                fi
                                ;;
                            *.json)
                                echo -e "${BLUE}OTLP Export: $SELECTED_FILENAME${NC}"
                                echo -e "${YELLOW}File size: $(ls -lh "$SELECTED_FILE" | awk '{print $5}')${NC}"
                                echo ""
                                echo -e "${BLUE}This is an OpenTelemetry OTLP export file${NC}"
                                echo -e "${YELLOW}You can import this into OpenTelemetry-compatible systems${NC}"
                                echo -e "${YELLOW}File location: $SELECTED_FILE${NC}"
                                ;;
                            *)
                                echo -e "${YELLOW}File location: $SELECTED_FILE${NC}"
                                ;;
                        esac

                        echo ""
                        echo -e "${BLUE}Press Enter to return to file selection...${NC}"
                        read -p ""
                        return

                    else
                        echo -e "${RED}Invalid selection. Please choose a number between 0 and ${#ALL_FILES[@]}.${NC}"
                    fi
                else
                    echo -e "${RED}Invalid input. Please enter a number.${NC}"
                fi
            done
            ;;
        22)
            echo -e "${YELLOW}Kill Current Process${NC}"
            echo "============================="
            echo -e "${BLUE}Current process being profiled:${NC}"
            echo "  PID: $PID"
            if [ ! -z "${PROCESS_NAME:-}" ]; then
                echo "  Name: $PROCESS_NAME"
            fi
            echo ""

            # Verify process is still running
            if ! kill -0 "$PID" 2>/dev/null; then
                echo -e "${YELLOW}Process $PID is not running or not accessible${NC}"
                return
            fi

            # Get process information
            if command -v ps >/dev/null 2>&1; then
                PROCESS_INFO=$(ps -p "$PID" -o pid,ppid,user,comm,args 2>/dev/null | tail -1)
                if [ ! -z "$PROCESS_INFO" ]; then
                    echo -e "${BLUE}Process details:${NC}"
                    echo "  $PROCESS_INFO"
                    echo ""
                fi
            fi

            # Warning and confirmation
            echo -e "${RED}⚠️  WARNING: This will terminate the Java process!${NC}"
            echo -e "${YELLOW}This action cannot be undone and may cause data loss.${NC}"
            echo ""
            echo -e "${BLUE}Kill options:${NC}"
            echo "  1) Graceful shutdown (SIGTERM) - Recommended"
            echo "  2) Force kill (SIGKILL) - Use if process is unresponsive"
            echo "  0) Cancel - Return to main menu"
            echo ""

            while true; do
                read -p "Select kill method (0-2): " KILL_METHOD

                case "$KILL_METHOD" in
                    0)
                        echo "Kill operation cancelled"
                        return
                        ;;
                    1)
                        echo -e "${BLUE}Sending SIGTERM to process $PID...${NC}"
                        if kill -TERM "$PID" 2>/dev/null; then
                            echo -e "${GREEN}SIGTERM sent successfully${NC}"
                            echo "Waiting for process to terminate gracefully..."

                            # Wait up to 10 seconds for graceful shutdown
                            for i in {1..10}; do
                                if ! kill -0 "$PID" 2>/dev/null; then
                                    echo -e "${GREEN}Process $PID terminated gracefully${NC}"
                                    echo ""
                                    echo -e "${YELLOW}Note: You may need to restart your application to continue profiling${NC}"
                                    return
                                fi
                                echo -n "."
                                sleep 1
                            done
                            echo ""

                            # Process still running after 10 seconds
                            if kill -0 "$PID" 2>/dev/null; then
                                echo -e "${YELLOW}Process is still running after 10 seconds${NC}"
                                echo -e "${BLUE}Would you like to force kill it? (y/N):${NC}"
                                read -p "" FORCE_KILL
                                if [[ "$FORCE_KILL" =~ ^[Yy]$ ]]; then
                                    echo -e "${BLUE}Sending SIGKILL to process $PID...${NC}"
                                    if kill -KILL "$PID" 2>/dev/null; then
                                        echo -e "${GREEN}Process $PID force killed${NC}"
                                    else
                                        echo -e "${RED}Failed to force kill process $PID${NC}"
                                        echo "You may need elevated privileges or the process may already be dead"
                                    fi
                                else
                                    echo "Process left running"
                                fi
                            fi
                        else
                            echo -e "${RED}Failed to send SIGTERM to process $PID${NC}"
                            echo "You may need elevated privileges or the process may not exist"
                        fi
                        return
                        ;;
                    2)
                        echo -e "${RED}⚠️  FORCE KILL WARNING ⚠️${NC}"
                        echo -e "${YELLOW}This will immediately terminate the process without cleanup!${NC}"
                        echo -e "${BLUE}Are you sure you want to force kill process $PID? (y/N):${NC}"
                        read -p "" CONFIRM_FORCE
                        if [[ "$CONFIRM_FORCE" =~ ^[Yy]$ ]]; then
                            echo -e "${BLUE}Sending SIGKILL to process $PID...${NC}"
                            if kill -KILL "$PID" 2>/dev/null; then
                                echo -e "${GREEN}Process $PID force killed${NC}"
                                echo ""
                                echo -e "${YELLOW}Note: You may need to restart your application to continue profiling${NC}"
                            else
                                echo -e "${RED}Failed to force kill process $PID${NC}"
                                echo "You may need elevated privileges or the process may not exist"
                            fi
                        else
                            echo "Force kill cancelled"
                        fi
                        return
                        ;;
                    *)
                        echo -e "${RED}Invalid selection. Please choose 0, 1, or 2.${NC}"
                        ;;
                esac
            done
            ;;
        0)
            echo -e "${GREEN}Exiting profiler. Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid option${NC}"
            return
    esac

    if [ $option -ne 20 ] && [ $option -ne 21 ] && [ $option -ne 22 ] && [ $option -ne 0 ]; then
        echo -e "${GREEN}Profiling completed!${NC}"
        echo -e "${YELLOW}Generated files in $RESULTS_DIR:${NC}"
        ls -lat "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr "$RESULTS_DIR"/*.txt "$RESULTS_DIR"/*.log "$RESULTS_DIR"/*.json 2>/dev/null | head -5 || echo "No profiling files found"

        # Automatically open the latest file if on macOS
        if [[ "$OSTYPE" == "darwin"* ]]; then
            LATEST_HTML=$(ls -t "$RESULTS_DIR"/*.html 2>/dev/null | head -1)
            if [ ! -z "$LATEST_HTML" ]; then
                echo -e "${BLUE}Opening latest result in browser...${NC}"
                open "$LATEST_HTML"
            fi
        fi
    fi
}

# Interactive profiling loop
while true; do
    # Verify process is still running
    if ! kill -0 "$PID" 2>/dev/null; then
        echo -e "${RED}Warning: Process $PID is no longer running!${NC}"
        echo "The process may have been terminated or restarted."

        # Try to find the same process again
        NEW_PID=$(jps -l | grep "$PROCESS_NAME" | head -1 | cut -d' ' -f1)
        if [ ! -z "$NEW_PID" ] && [ "$NEW_PID" != "$PID" ]; then
            echo -e "${YELLOW}Found similar process with new PID: $NEW_PID${NC}"
            read -p "Switch to the new PID? (y/N): " SWITCH_CONFIRM
            if [[ "$SWITCH_CONFIRM" =~ ^[Yy]$ ]]; then
                PID=$NEW_PID
                echo -e "${GREEN}Switched to PID: $PID${NC}"
            else
                echo "Please restart the profiler with the correct PID."
                exit 1
            fi
        else
            echo "Please restart the profiler when your application is running."
            exit 1
        fi
    fi

    show_profiling_menu
    read -p "Select profiling option (0-22): " PROFILE_TYPE
    execute_profiling "$PROFILE_TYPE"

    echo ""
    echo -e "${BLUE}Press Enter to continue or Ctrl+C to exit...${NC}"
    read -p ""
done

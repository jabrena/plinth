#!/bin/bash

# java-profile.sh - Automated Java profiling script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}Java Application Profiler v4.0 - Interactive Mode${NC}"
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
    local version="4.0"

    # For macOS, v4.0 uses .zip format, Linux still uses .tar.gz
    if [[ "$platform" == "macos" ]]; then
        local filename="async-profiler-$version-$platform.zip"
        local extract_cmd="unzip -q"
    else
        local filename="async-profiler-$version-$platform.tar.gz"
        local extract_cmd="tar -xzf"
    fi

    local url="https://github.com/jvm-profiling-tools/async-profiler/releases/download/v$version/$filename"

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
        echo "✅ Lock contention profiling"
        echo "✅ Wall clock profiling"
        echo "✅ JFR format"
        echo "❌ Native memory profiling (requires Linux perf events)"
        echo "❌ Hardware performance counters"
        echo ""
        echo -e "${BLUE}Note: macOS profiling is limited to user-space code only${NC}"
    else
        echo -e "${YELLOW}Platform: Linux${NC}"
        echo "✅ Full CPU profiling (user + kernel space)"
        echo "✅ Memory allocation profiling"
        echo "✅ Native memory profiling"
        echo "✅ Lock contention profiling"
        echo "✅ Wall clock profiling"
        echo "✅ Hardware performance counters"
        echo "✅ JFR format"

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
            echo "2. Memory Allocation Profiling (30s) ⭐"
            echo "10. Memory Leak Detection (5min) ⭐"
            echo "6. Native Memory Profiling (30s) ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "CPU")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "1. CPU Profiling (30s) ⭐"
            echo "8. Custom Duration CPU Profiling ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "Lock/Threading")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "3. Lock Contention Profiling (30s) ⭐"
            echo "4. Wall Clock Profiling (30s) ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "GC Analysis")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "14. Garbage Collection Log (custom duration) ⭐"
            echo "5. Interactive Heatmap (60s) ⭐"
            echo "2. Memory Allocation Profiling (30s) ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
        "I/O Analysis")
            echo -e "${GREEN}*** RECOMMENDED FOR YOUR PROBLEM ***${NC}"
            echo "4. Wall Clock Profiling (30s) ⭐"
            echo "5. Interactive Heatmap (60s) ⭐"
            echo ""
            echo -e "${BLUE}Other Options:${NC}"
            ;;
    esac

    echo "1. CPU Profiling (30s)"
    echo "2. Memory Allocation Profiling (30s)"
    echo "3. Lock Contention Profiling (30s)"
    echo "4. Wall Clock Profiling (30s)"
    echo "5. Interactive Heatmap (60s) - NEW in v4.0"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo "6. Memory Profiling (30s) - macOS compatible"
    else
        echo "6. Native Memory Profiling (30s) - NEW in v4.0 (Linux)"
    fi
    echo "7. Inverted Flame Graph (30s) - NEW in v4.0"
    echo "8. Custom Duration CPU Profiling"
    echo "9. View recent results"
    echo "10. Memory Leak Detection (5min)"
    echo "11. Complete Memory Analysis Workflow"
    echo "12. JFR Recording (custom duration)"
    echo "13. Thread Dump (instant snapshot)"
    echo "14. Garbage Collection Log (custom duration)"
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

# Function to execute profiling
execute_profiling() {
    local option=$1

    case $option in
        1)
            echo -e "${GREEN}Starting CPU profiling for 30 seconds...${NC}"
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
            echo -e "${GREEN}Starting interactive heatmap profiling for 60 seconds...${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)
            JFR_FILE="$RESULTS_DIR/profile-$TIMESTAMP.jfr"
            HEATMAP_FILE="$RESULTS_DIR/heatmap-cpu-$TIMESTAMP.html"

            echo -e "${BLUE}Step 1: Generating JFR recording...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 60 -o jfr -f "$JFR_FILE" "$PID"

            echo -e "${BLUE}Step 2: Converting JFR to heatmap...${NC}"
            "$PROFILER_DIR/current/bin/jfrconv" --cpu -o heatmap "$JFR_FILE" "$HEATMAP_FILE"
            echo -e "${GREEN}Heatmap generated: $HEATMAP_FILE${NC}"
            ;;
        6)
            echo -e "${GREEN}Starting native memory profiling for 30 seconds...${NC}"
            # Check if we're on macOS (Darwin)
            if [[ "$OSTYPE" == "darwin"* ]]; then
                echo -e "${YELLOW}Note: Native memory profiling requires Linux perf events.${NC}"
                echo -e "${YELLOW}On macOS, using allocation profiling as alternative...${NC}"
                "$PROFILER_DIR/current/bin/asprof" -e alloc -d 30 --alloc 512k -f "$RESULTS_DIR/allocation-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
            else
                # Check if perf events are available on Linux
                if ! "$PROFILER_DIR/current/bin/asprof" check native "$PID" 2>/dev/null; then
                    echo -e "${YELLOW}Native memory profiling not available. Using allocation profiling instead...${NC}"
                    "$PROFILER_DIR/current/bin/asprof" -e alloc -d 30 --alloc 512k -f "$RESULTS_DIR/allocation-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
                else
                    "$PROFILER_DIR/current/bin/asprof" -e native -d 30 -f "$RESULTS_DIR/native-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
                fi
            fi
            ;;
        7)
            echo -e "${GREEN}Starting inverted flame graph profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 30 --inverted -f "$RESULTS_DIR/inverted-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        8)
            read -p "Enter duration in seconds: " DURATION
            echo -e "${GREEN}Starting CPU profiling for $DURATION seconds...${NC}"
            # On macOS, add --all-user flag to avoid kernel profiling issues
            if [[ "$OSTYPE" == "darwin"* ]]; then
                "$PROFILER_DIR/current/bin/asprof" -d "$DURATION" --all-user -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            else
                "$PROFILER_DIR/current/bin/asprof" -d "$DURATION" -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            fi
            ;;
                9)
            echo -e "${YELLOW}Recent profiling results in $RESULTS_DIR:${NC}"
            ls -lat "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr "$RESULTS_DIR"/*.txt "$RESULTS_DIR"/*.log 2>/dev/null | head -10 || echo "No profiling files found"

            # Show breakdown by file type
            echo ""
            echo -e "${BLUE}File types:${NC}"
            HTML_COUNT=$(ls "$RESULTS_DIR"/*.html 2>/dev/null | wc -l | xargs)
            JFR_COUNT=$(ls "$RESULTS_DIR"/*.jfr 2>/dev/null | wc -l | xargs)
            TXT_COUNT=$(ls "$RESULTS_DIR"/*.txt 2>/dev/null | wc -l | xargs)
            LOG_COUNT=$(ls "$RESULTS_DIR"/*.log 2>/dev/null | wc -l | xargs)

            echo "  HTML files (flame graphs): $HTML_COUNT"
            echo "  JFR files (recordings): $JFR_COUNT"
            echo "  TXT files (thread dumps): $TXT_COUNT"
            echo "  LOG files (GC logs): $LOG_COUNT"
            return
            ;;
        10)
            echo -e "${GREEN}Starting Memory Leak Detection (5 minutes)...${NC}"
            echo -e "${YELLOW}This will run for 5 minutes to capture memory leak patterns${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 300 --alloc 1m -f "$RESULTS_DIR/memory-leak-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        11)
            echo -e "${GREEN}Starting Complete Memory Analysis Workflow...${NC}"
            TIMESTAMP=$(date +%Y%m%d-%H%M%S)

            echo -e "${BLUE}Step 1: Quick memory allocation baseline (30s)...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 30 -f "$RESULTS_DIR/memory-baseline-$TIMESTAMP.html" "$PID"

            echo -e "${BLUE}Step 2: Heap profiling (60s)...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 60 -f "$RESULTS_DIR/heap-analysis-$TIMESTAMP.html" "$PID"

            echo -e "${BLUE}Step 3: Memory leak detection (5min)...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -e alloc -d 300 --alloc 1m -f "$RESULTS_DIR/memory-leak-complete-$TIMESTAMP.html" "$PID"

            echo -e "${GREEN}Complete memory analysis finished! Check these files:${NC}"
            echo "- memory-baseline-$TIMESTAMP.html (30s baseline)"
            echo "- heap-analysis-$TIMESTAMP.html (60s detailed heap)"
            echo "- memory-leak-complete-$TIMESTAMP.html (5min leak detection)"
            ;;
        12)
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
            else
                echo -e "${RED}JFR recording failed${NC}"
            fi
            ;;
        13)
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
        14)
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

            # Method 2: Use async-profiler to capture GC events if jcmd failed
            if [ "$GC_SUCCESS" = false ]; then
                echo -e "${YELLOW}jcmd approach failed, using async-profiler for GC events...${NC}"

                # Use async-profiler to capture GC events
                "$PROFILER_DIR/current/bin/asprof" -e gc -d "$GC_DURATION" -o text -f "$GC_LOG_FILE" "$PID" 2>/dev/null

                if [ $? -eq 0 ] && [ -s "$GC_LOG_FILE" ]; then
                    GC_SUCCESS=true
                    echo -e "${GREEN}GC events captured via async-profiler${NC}"
                fi
            fi

            # Method 3: Try to find existing GC logs from the JVM
            if [ "$GC_SUCCESS" = false ]; then
                echo -e "${YELLOW}Direct GC logging failed, searching for existing GC logs...${NC}"

                # Try to find GC log files associated with the process
                PROC_DIR="/proc/$PID"
                if [ -d "$PROC_DIR" ]; then
                    # Look for open file descriptors that might be GC logs
                    GC_LOG_CANDIDATES=$(lsof -p "$PID" 2>/dev/null | grep -E "\\.log|gc" | awk '{print $NF}' | sort -u)

                    if [ ! -z "$GC_LOG_CANDIDATES" ]; then
                        echo -e "${BLUE}Found potential GC log files:${NC}"
                        echo "$GC_LOG_CANDIDATES"

                        # Monitor the most recent log file
                        LATEST_LOG=$(echo "$GC_LOG_CANDIDATES" | head -1)
                        if [ -f "$LATEST_LOG" ]; then
                            echo -e "${BLUE}Monitoring existing log: $LATEST_LOG${NC}"

                            # Get current size and tail the log for the specified duration
                            INITIAL_SIZE=$(wc -l < "$LATEST_LOG" 2>/dev/null || echo "0")

                            # Start monitoring in background and collect for specified duration
                            timeout "$GC_DURATION" tail -f "$LATEST_LOG" > "$GC_LOG_FILE" 2>/dev/null &
                            TAIL_PID=$!

                            echo "Monitoring GC log for $GC_DURATION seconds..."
                            wait $TAIL_PID 2>/dev/null

                            if [ -s "$GC_LOG_FILE" ]; then
                                GC_SUCCESS=true
                                echo -e "${GREEN}GC log monitoring completed${NC}"
                            fi
                        fi
                    fi
                fi
            fi

            # Method 4: Create a custom GC monitoring script using jstat
            if [ "$GC_SUCCESS" = false ]; then
                echo -e "${YELLOW}Falling back to jstat for GC monitoring...${NC}"

                if command -v jstat >/dev/null 2>&1; then
                    echo -e "${BLUE}Using jstat to collect GC statistics...${NC}"

                    # Create header
                    echo "# GC Statistics collected via jstat" > "$GC_LOG_FILE"
                    echo "# Timestamp,S0C,S1C,S0U,S1U,EC,EU,OC,OU,MC,MU,CCSC,CCSU,YGC,YGCT,FGC,FGCT,GCT" >> "$GC_LOG_FILE"

                    # Collect GC stats every second for the specified duration
                    END_TIME=$(($(date +%s) + GC_DURATION))

                    while [ $(date +%s) -lt $END_TIME ]; do
                        TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
                        GC_STATS=$(jstat -gc "$PID" 2>/dev/null | tail -1)
                        if [ $? -eq 0 ] && [ ! -z "$GC_STATS" ]; then
                            echo "$TIMESTAMP,$GC_STATS" >> "$GC_LOG_FILE"
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

                echo -e "${YELLOW}Analysis suggestions:${NC}"
                echo "  - Look for frequent GC events indicating memory pressure"
                echo "  - Check for long GC pause times"
                echo "  - Monitor heap utilization trends"
                echo "  - Analyze collection generation patterns"
            else
                echo -e "${RED}GC log collection failed${NC}"
                echo -e "${YELLOW}This might happen if:${NC}"
                echo "  - The JVM doesn't have GC logging enabled"
                echo "  - Insufficient permissions to access GC logs"
                echo "  - The Java version doesn't support dynamic GC logging"
                echo -e "${BLUE}Consider starting your application with GC logging enabled:${NC}"
                echo "  Java 8: -XX:+PrintGC -XX:+PrintGCDetails -Xloggc:gc.log"
                echo "  Java 9+: -Xlog:gc*:gc.log"

                # Clean up empty file
                [ -f "$GC_LOG_FILE" ] && [ ! -s "$GC_LOG_FILE" ] && rm -f "$GC_LOG_FILE"
            fi
            ;;
        0)
            echo -e "${GREEN}Exiting profiler. Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid option${NC}"
            return
            ;;
    esac

    if [ $option -ne 9 ] && [ $option -ne 0 ]; then
        echo -e "${GREEN}Profiling completed!${NC}"
        echo -e "${YELLOW}Generated files in $RESULTS_DIR:${NC}"
        ls -lat "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr "$RESULTS_DIR"/*.txt "$RESULTS_DIR"/*.log 2>/dev/null | head -5 || echo "No profiling files found"

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
    read -p "Select profiling option (0-14): " PROFILE_TYPE
    execute_profiling "$PROFILE_TYPE"

    echo ""
    echo -e "${BLUE}Press Enter to continue or Ctrl+C to exit...${NC}"
    read -p ""
done

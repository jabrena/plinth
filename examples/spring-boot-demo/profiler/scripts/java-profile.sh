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

# Step 1: List Java processes and handle selection
echo -e "${YELLOW}Step 1: Available Java Processes${NC}"
echo "-----"

# Get list of Java processes
JAVA_PROCESSES=$(jps -l | grep -v "Jps$")

if [ -z "$JAVA_PROCESSES" ]; then
    echo -e "${RED}No Java processes found running on this system.${NC}"
    echo "Please start your Java application first and try again."
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

# Step 2: Get the script directory and set up profiler paths
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
    
    # macOS uses .zip, others use .tar.gz
    if [[ "$platform" == "macos"* ]]; then
        local filename="async-profiler-$version-$platform.zip"
        local extract_cmd="unzip -q"
    else
        local filename="async-profiler-$version-$platform.tar.gz"
        local extract_cmd="tar -xzf"
    fi
    
    local url="https://github.com/async-profiler/async-profiler/releases/download/v$version/$filename"
    
    if [ ! -d "$profiler_dir/current" ]; then
        echo "Downloading async-profiler..."
        cd "$profiler_dir"
        
        if command -v curl >/dev/null 2>&1; then
            curl -L -o "$filename" "$url"
        elif command -v wget >/dev/null 2>&1; then
            wget -O "$filename" "$url"
        else
            echo -e "${RED}Error: Neither curl nor wget is available${NC}"
            exit 1
        fi
        
        $extract_cmd "$filename"
        rm "$filename"
        ln -sf "async-profiler-$version-$platform" current
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

# Function to show profiling menu
show_profiling_menu() {
    echo ""
    echo -e "${BLUE}===========================================${NC}"
    echo -e "${YELLOW}Profiling Options for PID: $PID${NC}"
    echo -e "${YELLOW}Process: $PROCESS_NAME${NC}"
    echo -e "${BLUE}===========================================${NC}"
    echo "1. CPU Profiling (30s)"
    echo "2. Memory Allocation Profiling (30s)"
    echo "3. Lock Contention Profiling (30s)" 
    echo "4. Wall Clock Profiling (30s)"
    echo "5. Interactive Heatmap (60s) - NEW in v4.0"
    echo "6. Native Memory Profiling (30s) - NEW in v4.0"
    echo "7. Inverted Flame Graph (30s) - NEW in v4.0"
    echo "8. Custom Duration CPU Profiling"
    echo "9. View recent results"
    echo "0. Exit profiler"
    echo -e "${BLUE}===========================================${NC}"
}

# Function to execute profiling
execute_profiling() {
    local option=$1
    
    case $option in
        1)
            echo -e "${GREEN}Starting CPU profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 30 -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
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
            "$PROFILER_DIR/current/bin/asprof" -e native -d 30 -f "$RESULTS_DIR/native-memory-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        7)
            echo -e "${GREEN}Starting inverted flame graph profiling for 30 seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d 30 --inverted -f "$RESULTS_DIR/inverted-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        8)
            read -p "Enter duration in seconds: " DURATION
            echo -e "${GREEN}Starting CPU profiling for $DURATION seconds...${NC}"
            "$PROFILER_DIR/current/bin/asprof" -d "$DURATION" -f "$RESULTS_DIR/cpu-flamegraph-$(date +%Y%m%d-%H%M%S).html" "$PID"
            ;;
        9)
            echo -e "${YELLOW}Recent profiling results in $RESULTS_DIR:${NC}"
            ls -lat "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr 2>/dev/null | head -10 || echo "No profiling files found"
            return
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
        ls -lat "$RESULTS_DIR"/*.html "$RESULTS_DIR"/*.jfr 2>/dev/null | head -5 || echo "No profiling files found"
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
    read -p "Select profiling option (0-9): " PROFILE_TYPE
    execute_profiling "$PROFILE_TYPE"
    
    echo ""
    echo -e "${BLUE}Press Enter to continue or Ctrl+C to exit...${NC}"
    read -p ""
done 
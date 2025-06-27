#!/bin/bash

# Spring Boot Application Runner with Async-Profiler Support
# This script runs the Spring Boot application with JVM flags optimized for async-profiler

set -e

# Default values
PROFILE_MODE="cpu"
APP_JAR="target/spring-boot-memory-leak-demo-1.0-SNAPSHOT.jar"
APP_CLASS="info.jab.ms.MainApplication"
HEAP_SIZE="512m"
SPRING_PROFILE="default"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

# Function to show usage
show_usage() {
    cat << EOF
Usage: $0 [OPTIONS]

Run Spring Boot application with async-profiler support

OPTIONS:
    -m, --mode MODE         Profiling mode: cpu, alloc, wall, lock (default: cpu)
    -j, --jar PATH          Path to application JAR file
    -c, --class CLASS       Main class to run (default: info.jab.ms.MainApplication)
    -h, --heap SIZE         Heap size (default: 512m)
    -s, --spring-profile    Spring profile to activate (default: default)
    --help                  Show this help message

EXAMPLES:
    # Run with CPU profiling mode
    $0 -m cpu

    # Run with memory allocation profiling
    $0 -m alloc

    # Run with custom heap size and Spring profile
    $0 -h 1g -s dev

    # Run with wall-clock profiling
    $0 -m wall

EOF
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -m|--mode)
            PROFILE_MODE="$2"
            shift 2
            ;;
        -j|--jar)
            APP_JAR="$2"
            shift 2
            ;;
        -c|--class)
            APP_CLASS="$2"
            shift 2
            ;;
        -h|--heap)
            HEAP_SIZE="$2"
            shift 2
            ;;
        -s|--spring-profile)
            SPRING_PROFILE="$2"
            shift 2
            ;;
        --help)
            show_usage
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Validate profiling mode
case $PROFILE_MODE in
    cpu|alloc|wall|lock)
        ;;
    *)
        log_error "Invalid profiling mode: $PROFILE_MODE. Use: cpu, alloc, wall, lock"
        exit 1
        ;;
esac

# Generate timestamp for output files
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

log_info "Starting Spring Boot application"
log_info "Profile mode: $PROFILE_MODE"
log_info "Spring profile: $SPRING_PROFILE"

# JVM flags for optimal profiling with async-profiler
JVM_FLAGS=(
    # Memory settings
    "-Xms$HEAP_SIZE"
    "-Xmx$HEAP_SIZE"
    
    # Profiling optimization flags
    "-XX:+UnlockDiagnosticVMOptions"
    "-XX:+DebugNonSafepoints"
    "-XX:+PreserveFramePointer"
    
    # JFR settings (useful for some profiling modes)
    "-XX:+FlightRecorder"
    "-XX:StartFlightRecording=filename=flight-recording-${TIMESTAMP}.jfr"
    
    # GC logging for memory leak analysis
    "-Xlog:gc*:gc-${TIMESTAMP}.log:time,tags"
    
    # Security settings for profiler attachment
    "-Djdk.attach.allowAttachSelf=true"
    
    # Optional: Disable C2 compiler for more accurate profiling (uncomment if needed)
    # "-XX:TieredStopAtLevel=1"
)

# Spring Boot specific arguments
SPRING_ARGS=(
    "--spring.profiles.active=$SPRING_PROFILE"
    "--logging.level.info.jab.ms=DEBUG"
    "--server.port=8080"
)

# Function to run with Maven
run_with_maven() {
    log_info "Running with Maven..."
    export JAVA_TOOL_OPTIONS="${JVM_FLAGS[*]}"
    
    # Start the application
    mvn spring-boot:run \
        -Dspring-boot.run.profiles="$SPRING_PROFILE" \
        -Dspring-boot.run.arguments="$(IFS=' '; echo "${SPRING_ARGS[*]}")"
}

# Function to run with JAR
run_with_jar() {
    if [[ ! -f "$APP_JAR" ]]; then
        log_error "JAR file not found: $APP_JAR"
        log_info "Building the application first..."
        mvn clean package -DskipTests
    fi
    
    if [[ ! -f "$APP_JAR" ]]; then
        log_error "Failed to build or find JAR file: $APP_JAR"
        exit 1
    fi
    
    log_info "Running JAR: $APP_JAR"
    
    # Start the application
    java "${JVM_FLAGS[@]}" -jar "$APP_JAR" "${SPRING_ARGS[@]}"
}

# Function to run with class
run_with_class() {
    log_info "Running main class: $APP_CLASS"
    
    # Start the application
    java "${JVM_FLAGS[@]}" -cp "target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" "$APP_CLASS" "${SPRING_ARGS[@]}"
}

# Main execution
log_info "JVM Flags: ${JVM_FLAGS[*]}"

# Determine how to run the application
if [[ -f "pom.xml" ]] && command -v mvn >/dev/null 2>&1; then
    if [[ -f "$APP_JAR" ]]; then
        run_with_jar
    else
        run_with_maven
    fi
elif [[ -f "$APP_JAR" ]]; then
    run_with_jar
else
    run_with_class
fi

log_success "Application completed!" 
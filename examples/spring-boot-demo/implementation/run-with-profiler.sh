#!/bin/bash

# Java Application Runner with Async-Profiler Support
# This script runs Spring Boot or Quarkus applications with JVM flags optimized for async-profiler

set -e

# Default values
PROFILE_MODE="cpu"
APP_JAR=""
APP_CLASS="info.jab.ms.MainApplication"
HEAP_SIZE="512m"
PROFILE_NAME="default"
FRAMEWORK="auto"

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

Run Spring Boot or Quarkus application with async-profiler support

OPTIONS:
    -m, --mode MODE         Profiling mode: cpu, alloc, wall, lock (default: cpu)
    -f, --framework FRAMEWORK   Framework: springboot, quarkus, auto (default: auto)
    -j, --jar PATH          Path to application JAR file
    -c, --class CLASS       Main class to run (default: info.jab.ms.MainApplication)
    -h, --heap SIZE         Heap size (default: 512m)
    -p, --profile PROFILE   Profile to activate (default: default)
    --help                  Show this help message

FRAMEWORK DETECTION:
    auto        Automatically detect framework from pom.xml or JAR naming
    springboot  Force Spring Boot mode
    quarkus     Force Quarkus mode

EXAMPLES:
    # Auto-detect framework and run with CPU profiling
    $0 -m cpu

    # Force Spring Boot with memory allocation profiling
    $0 -f springboot -m alloc

    # Force Quarkus with custom heap size and profile
    $0 -f quarkus -h 1g -p dev

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
        -f|--framework)
            FRAMEWORK="$2"
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
        -p|--profile)
            PROFILE_NAME="$2"
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

# Validate framework
case $FRAMEWORK in
    auto|springboot|quarkus)
        ;;
    *)
        log_error "Invalid framework: $FRAMEWORK. Use: auto, springboot, quarkus"
        exit 1
        ;;
esac

# Function to detect framework
detect_framework() {
    if [[ "$FRAMEWORK" != "auto" ]]; then
        return 0
    fi
    
    # Check pom.xml for framework indicators
    if [[ -f "pom.xml" ]]; then
        if grep -q "spring-boot-starter" pom.xml; then
            FRAMEWORK="springboot"
            log_info "Detected Spring Boot from pom.xml"
        elif grep -q "quarkus-universe-bom\|quarkus-bom" pom.xml; then
            FRAMEWORK="quarkus"
            log_info "Detected Quarkus from pom.xml"
        fi
    fi
    
    # Check for JAR files if framework still not detected
    if [[ "$FRAMEWORK" == "auto" ]]; then
        if [[ -n "$APP_JAR" ]]; then
            if [[ "$APP_JAR" == *"spring-boot"* ]]; then
                FRAMEWORK="springboot"
                log_info "Detected Spring Boot from JAR name"
            elif [[ "$APP_JAR" == *"quarkus"* ]] || [[ "$APP_JAR" == *"runner"* ]]; then
                FRAMEWORK="quarkus"
                log_info "Detected Quarkus from JAR name"
            fi
        else
            # Check for JAR files in target directory
            if ls target/*-runner.jar >/dev/null 2>&1; then
                FRAMEWORK="quarkus"
                log_info "Detected Quarkus from runner JAR"
            elif ls target/*spring-boot*.jar >/dev/null 2>&1; then
                FRAMEWORK="springboot"
                log_info "Detected Spring Boot from JAR files"
            fi
        fi
    fi
    
    # Default to Spring Boot if still not detected
    if [[ "$FRAMEWORK" == "auto" ]]; then
        FRAMEWORK="springboot"
        log_warn "Could not detect framework, defaulting to Spring Boot"
    fi
}

# Function to set framework-specific JAR if not provided
set_default_jar() {
    if [[ -z "$APP_JAR" ]]; then
        case $FRAMEWORK in
            springboot)
                # Look for Spring Boot JAR
                if ls target/*spring-boot*.jar >/dev/null 2>&1; then
                    APP_JAR=$(ls target/*spring-boot*.jar | head -1)
                else
                    # Fallback to common naming pattern
                    PROJECT_NAME=$(basename "$(pwd)")
                    APP_JAR="target/${PROJECT_NAME}-1.0-SNAPSHOT.jar"
                fi
                ;;
            quarkus)
                # Look for Quarkus runner JAR
                if ls target/*-runner.jar >/dev/null 2>&1; then
                    APP_JAR=$(ls target/*-runner.jar | head -1)
                else
                    # Fallback to common naming pattern
                    PROJECT_NAME=$(basename "$(pwd)")
                    APP_JAR="target/${PROJECT_NAME}-1.0-SNAPSHOT-runner.jar"
                fi
                ;;
        esac
        log_info "Using default JAR: $APP_JAR"
    fi
}

# Detect framework and set defaults
detect_framework
set_default_jar

# Generate timestamp for output files
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

log_info "Starting $FRAMEWORK application"
log_info "Profile mode: $PROFILE_MODE"
log_info "Framework: $FRAMEWORK"
log_info "Profile: $PROFILE_NAME"

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

# Framework-specific arguments
get_app_args() {
    case $FRAMEWORK in
        springboot)
            APP_ARGS=(
                "--spring.profiles.active=$PROFILE_NAME"
                "--logging.level.info.jab.ms=DEBUG"
                "--server.port=8080"
            )
            ;;
        quarkus)
            APP_ARGS=(
                "-Dquarkus.profile=$PROFILE_NAME"
                "-Dquarkus.log.category.\"info.jab.ms\".level=DEBUG"
                "-Dquarkus.http.port=8080"
            )
            ;;
    esac
}

# Get framework-specific arguments
get_app_args

# Function to run with Maven
run_with_maven() {
    log_info "Running with Maven..."
    export JAVA_TOOL_OPTIONS="${JVM_FLAGS[*]}"
    
    case $FRAMEWORK in
        springboot)
            # Start Spring Boot application
            mvn spring-boot:run \
                -Dspring-boot.run.profiles="$PROFILE_NAME" \
                -Dspring-boot.run.arguments="$(IFS=' '; echo "${APP_ARGS[*]}")"
            ;;
        quarkus)
            # Start Quarkus application in dev mode
            mvn quarkus:dev \
                -Dquarkus.args="$(IFS=' '; echo "${APP_ARGS[*]}")"
            ;;
    esac
}

# Function to run with JAR
run_with_jar() {
    if [[ ! -f "$APP_JAR" ]]; then
        log_error "JAR file not found: $APP_JAR"
        log_info "Building the application first..."
        case $FRAMEWORK in
            springboot)
                mvn clean package -DskipTests
                ;;
            quarkus)
                mvn clean package -DskipTests
                ;;
        esac
    fi
    
    if [[ ! -f "$APP_JAR" ]]; then
        log_error "Failed to build or find JAR file: $APP_JAR"
        exit 1
    fi
    
    log_info "Running JAR: $APP_JAR"
    
    # Start the application
    java "${JVM_FLAGS[@]}" -jar "$APP_JAR" "${APP_ARGS[@]}"
}

# Function to run with class
run_with_class() {
    log_info "Running main class: $APP_CLASS"
    
    # Start the application
    java "${JVM_FLAGS[@]}" -cp "target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" "$APP_CLASS" "${APP_ARGS[@]}"
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
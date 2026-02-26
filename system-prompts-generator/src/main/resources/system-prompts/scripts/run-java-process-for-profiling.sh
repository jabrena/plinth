#!/bin/bash

# Java Application Runner with Async-Profiler Support (Java 21-25 Enhanced)
# This script runs Spring Boot or Quarkus applications with JVM flags optimized for async-profiler
# Enhanced for Java 21-25 with virtual threads, modern GC, and improved profiling support

set -e

# Default values
PROFILE_MODE="cpu"
APP_JAR=""
APP_CLASS="info.jab.ms.MainApplication"
HEAP_SIZE="512m"
PROFILE_NAME="default"
FRAMEWORK="auto"
ENABLE_GC_LOG="false"
ENABLE_VIRTUAL_THREADS="false"
JAVA_VERSION=""
GC_ALGORITHM="auto"
ENABLE_PREVIEW_FEATURES="false"

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
                           For Spring Boot: sets spring.profiles.active
                           For Quarkus: sets quarkus.profile
    --gc-log                Enable GC logging (disabled by default)
    --virtual-threads       Enable virtual threads (Java 21+, disabled by default)
    --gc GC_TYPE           GC algorithm: auto, g1, zgc, parallel, serial (default: auto)
    --preview               Enable preview features for newer Java versions
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

    # Run Spring Boot with virtual threads (Java 21+)
    $0 -f springboot --virtual-threads

    # Run with ZGC for low-latency applications (Java 21+)
    $0 -f springboot --gc zgc -h 2g

    # Run with preview features enabled (Java 21+)
    $0 -f springboot --preview

    # Run with wall-clock profiling and virtual threads
    $0 -m wall --virtual-threads

    # Run with comprehensive GC logging
    $0 -m cpu --gc-log

    # Run with modern GC, virtual threads, and comprehensive logging
    $0 -m alloc --gc-log --virtual-threads --gc g1

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
        --gc-log)
            ENABLE_GC_LOG="true"
            shift
            ;;
        --virtual-threads)
            ENABLE_VIRTUAL_THREADS="true"
            shift
            ;;
        --gc)
            GC_ALGORITHM="$2"
            shift 2
            ;;
        --preview)
            ENABLE_PREVIEW_FEATURES="true"
            shift
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

# Validate GC algorithm
case $GC_ALGORITHM in
    auto|g1|zgc|parallel|serial)
        ;;
    *)
        log_error "Invalid GC algorithm: $GC_ALGORITHM. Use: auto, g1, zgc, parallel, serial"
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

# Function to detect Java version
detect_java_version() {
    if command -v java >/dev/null 2>&1; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
        if [[ "$JAVA_VERSION" == "1" ]]; then
            # Handle Java 8 version format (1.8.x)
            JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f2)
        fi
        log_info "Detected Java version: $JAVA_VERSION"

        # Validate virtual threads support
        if [[ "$ENABLE_VIRTUAL_THREADS" == "true" ]] && [[ "$JAVA_VERSION" -lt 21 ]]; then
            log_warn "Virtual threads require Java 21+. Current version: $JAVA_VERSION. Disabling virtual threads."
            ENABLE_VIRTUAL_THREADS="false"
        fi

        # Validate preview features
        if [[ "$ENABLE_PREVIEW_FEATURES" == "true" ]] && [[ "$JAVA_VERSION" -lt 21 ]]; then
            log_warn "Preview features flag requires Java 21+. Current version: $JAVA_VERSION. Disabling preview features."
            ENABLE_PREVIEW_FEATURES="false"
        fi
    else
        log_error "Java not found in PATH"
        exit 1
    fi
}

# Function to select optimal GC algorithm based on Java version
select_gc_algorithm() {
    if [[ "$GC_ALGORITHM" == "auto" ]]; then
        if [[ "$JAVA_VERSION" -ge 21 ]]; then
            # For Java 21+, prefer G1GC for balanced performance
            GC_ALGORITHM="g1"
            log_info "Auto-selected G1GC for Java $JAVA_VERSION"
        elif [[ "$JAVA_VERSION" -ge 17 ]]; then
            GC_ALGORITHM="g1"
            log_info "Auto-selected G1GC for Java $JAVA_VERSION"
        else
            GC_ALGORITHM="parallel"
            log_info "Auto-selected Parallel GC for Java $JAVA_VERSION"
        fi
    fi
}

# Detect framework, Java version, and set defaults
detect_framework
detect_java_version
select_gc_algorithm
set_default_jar

# Generate timestamp for output files
TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

log_info "Starting $FRAMEWORK application"
log_info "Profile mode: $PROFILE_MODE"
log_info "Framework: $FRAMEWORK"
log_info "Profile: $PROFILE_NAME"
log_info "Java version: $JAVA_VERSION"
log_info "GC algorithm: $GC_ALGORITHM"

# JVM flags for optimal profiling with async-profiler (Java 21-25 enhanced)
JVM_FLAGS=(
    # Memory settings
    "-Xms$HEAP_SIZE"
    "-Xmx$HEAP_SIZE"

    # Profiling optimization flags
    "-XX:+UnlockDiagnosticVMOptions"
    "-XX:+DebugNonSafepoints"
    "-XX:+PreserveFramePointer"

    # Security settings for profiler attachment
    "-Djdk.attach.allowAttachSelf=true"
)

# Add GC-specific flags
case $GC_ALGORITHM in
    g1)
        JVM_FLAGS+=(
            "-XX:+UseG1GC"
            "-XX:MaxGCPauseMillis=200"
            "-XX:G1HeapRegionSize=16m"
        )
        if [[ "$JAVA_VERSION" -ge 21 ]]; then
            JVM_FLAGS+=(
                "-XX:G1MixedGCCountTarget=8"
                "-XX:G1HeapWastePercent=10"
            )
        fi
        ;;
    zgc)
        if [[ "$JAVA_VERSION" -ge 21 ]]; then
            JVM_FLAGS+=(
                "-XX:+UseZGC"
            )
        elif [[ "$JAVA_VERSION" -ge 17 ]]; then
            JVM_FLAGS+=(
                "-XX:+UseZGC"
                "-XX:+UnlockExperimentalVMOptions"
            )
        else
            log_warn "ZGC requires Java 17+. Falling back to G1GC"
            JVM_FLAGS+=(
                "-XX:+UseG1GC"
                "-XX:MaxGCPauseMillis=200"
            )
        fi
        ;;
    parallel)
        JVM_FLAGS+=(
            "-XX:+UseParallelGC"
        )
        ;;
    serial)
        JVM_FLAGS+=(
            "-XX:+UseSerialGC"
        )
        ;;
esac

# Add Java version-specific optimizations
if [[ "$JAVA_VERSION" -ge 21 ]]; then
    JVM_FLAGS+=(
        # Enhanced performance flags for Java 21+
        "-XX:+UseStringDeduplication"
    )
fi

# Function to get CPU core count (cross-platform)
get_cpu_cores() {
    if command -v nproc >/dev/null 2>&1; then
        # Linux
        nproc
    elif [[ -f /proc/cpuinfo ]]; then
        # Linux fallback
        grep -c ^processor /proc/cpuinfo
    elif command -v sysctl >/dev/null 2>&1; then
        # macOS
        sysctl -n hw.ncpu
    else
        # Default fallback
        echo "4"
    fi
}

# Add virtual threads support (Java 21+)
if [[ "$ENABLE_VIRTUAL_THREADS" == "true" ]]; then
    CPU_CORES=$(get_cpu_cores)
    JVM_FLAGS+=(
        "-Djdk.virtualThreadScheduler.parallelism=$CPU_CORES"
        "-Djdk.virtualThreadScheduler.maxPoolSize=$((CPU_CORES * 256))"
    )
    log_info "Virtual threads enabled with optimized scheduler settings (CPU cores: $CPU_CORES)"
fi

# Add preview features if enabled
if [[ "$ENABLE_PREVIEW_FEATURES" == "true" ]]; then
    JVM_FLAGS+=(
        "--enable-preview"
    )
    log_info "Preview features enabled"
fi

# Add GC logging if enabled (enhanced for Java 21-25)
if [[ "$ENABLE_GC_LOG" == "true" ]]; then
    if [[ "$JAVA_VERSION" -ge 21 ]]; then
        JVM_FLAGS+=(
            "-Xlog:gc*,heap*,ergo*:gc-${TIMESTAMP}.log:time,tags,level"
            "-Xlog:gc+heap=info"
        )
    elif [[ "$JAVA_VERSION" -ge 11 ]]; then
        JVM_FLAGS+=(
            "-Xlog:gc*:gc-${TIMESTAMP}.log:time,tags"
        )
    else
        # Legacy GC logging for Java 8
        JVM_FLAGS+=(
            "-XX:+PrintGC"
            "-XX:+PrintGCDetails"
            "-XX:+PrintGCTimeStamps"
            "-Xloggc:gc-${TIMESTAMP}.log"
        )
    fi
    log_info "GC logging enabled: gc-${TIMESTAMP}.log"
fi

# Framework-specific arguments (enhanced for Java 21-25)
get_app_args() {
    case $FRAMEWORK in
        springboot)
            APP_ARGS=(
                "--spring.profiles.active=$PROFILE_NAME"
                "--logging.level.info.jab.ms=DEBUG"
                "--server.port=8080"
            )

            # Add virtual threads support for Spring Boot 3.2+ with Java 21+
            if [[ "$ENABLE_VIRTUAL_THREADS" == "true" ]] && [[ "$JAVA_VERSION" -ge 21 ]]; then
                APP_ARGS+=(
                    "--spring.threads.virtual.enabled=true"
                )
                log_info "Spring Boot virtual threads enabled"
            fi
            ;;
        quarkus)
            APP_ARGS=(
                "-Dquarkus.profile=$PROFILE_NAME"
                "-Dquarkus.log.category.\"info.jab.ms\".level=DEBUG"
                "-Dquarkus.http.port=8080"
            )

            # Add virtual threads support for Quarkus with Java 21+
            if [[ "$ENABLE_VIRTUAL_THREADS" == "true" ]] && [[ "$JAVA_VERSION" -ge 21 ]]; then
                APP_ARGS+=(
                    "-Dquarkus.virtual-threads.enabled=true"
                )
                log_info "Quarkus virtual threads enabled"
            fi
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

# Enhanced Java 21-25 Features Summary:
# - Automatic Java version detection with compatibility warnings
# - Virtual threads support (Java 21+) with optimized scheduler settings
# - Modern GC algorithms (G1GC, ZGC) with version-specific optimizations
# - Enhanced GC logging with detailed heap and ergonomics information
# - Preview features support for experimental Java features
# - Framework-specific virtual threads integration (Spring Boot 3.2+, Quarkus)
# - Backward compatibility maintained for Java 8+ versions

#!/bin/bash

# JMeter Load Test Script
# This script runs JMeter tests and generates HTML reports

set -e

# Default configuration (can be overridden via environment variables)
JMETER_LOOPS=${JMETER_LOOPS:-1000}
JMETER_THREADS=${JMETER_THREADS:-1}
JMETER_RAMP_UP=${JMETER_RAMP_UP:-1}
OUTPUT_DIR=""
GUI_MODE=false
INPUT_JTL=""

# Project paths
PROJECT_DIR=$(pwd)
TEST_PLAN="$PROJECT_DIR/src/test/resources/jmeter/load-test.jmx"

# Function to set output paths based on output directory
set_output_paths() {
    local base_dir
    local timestamp=$(date +%Y%m%d-%H%M%S)

    if [[ -n "$OUTPUT_DIR" ]]; then
        base_dir="$OUTPUT_DIR"
    else
        base_dir="$PROJECT_DIR/target"
    fi

    RESULTS_FILE="$base_dir/jmeter-result-$timestamp.jtl"
    REPORT_DIR="$base_dir/jmeter-report-$timestamp"
    LOG_FILE="$base_dir/jmeter-$timestamp.log"
}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "JMeter Load Test Script"
    echo "======================="
    echo ""
    echo "DESCRIPTION:"
    echo "  This script executes JMeter load tests in non-GUI mode and generates"
    echo "  comprehensive HTML reports. It can also generate reports from existing"
    echo "  JTL files. It provides flexible configuration options and automatically"
    echo "  opens the results in your browser."
    echo ""
    echo "USAGE:"
    echo "  $0 [OPTIONS]"
    echo ""
    echo "OPTIONS:"
    echo "  -l, --loops LOOPS      Number of loops per thread (default: $JMETER_LOOPS)"
    echo "  -t, --threads THREADS  Number of concurrent threads (default: $JMETER_THREADS)"
    echo "  -r, --ramp-up SECONDS  Ramp-up period in seconds (default: $JMETER_RAMP_UP)"
    echo "  -o, --output-dir DIR   Output directory for results and reports (default: target/)"
    echo "  -j, --jtl FILE        Generate report from existing JTL file (skips test execution)"
    echo "  -g, --gui             Open JMeter GUI instead of running in non-GUI mode"
    echo "  -h, --help            Show this detailed help message"
    echo ""
    echo "ENVIRONMENT VARIABLES:"
    echo "  JMETER_LOOPS          Override default number of loops"
    echo "  JMETER_THREADS        Override default number of threads"
    echo "  JMETER_RAMP_UP        Override default ramp-up period"
    echo ""
    echo "EXAMPLES:"
    echo "  $0                              # Run with defaults (1000 loops, 1 thread, 1s ramp-up)"
    echo "  $0 -l 500 -t 10 -r 30          # 500 loops, 10 threads, 30s ramp-up"
    echo "  $0 --loops 100 --threads 5     # Long form options"
    echo "  $0 -o /tmp/jmeter-results      # Custom output directory"
    echo "  $0 -j results.jtl -o reports/  # Generate report from existing JTL file"
    echo "  $0 -g                           # Open JMeter GUI with test plan loaded"
    echo "  JMETER_THREADS=5 $0             # Using environment variables"
    echo ""
    echo "OUTPUT FILES:"
    echo "  [output-dir]/jmeter-result-YYYYMMDD-HHMMSS.jtl       # Raw test results (JTL format)"
    echo "  [output-dir]/jmeter-report-YYYYMMDD-HHMMSS/index.html # HTML dashboard report"
    echo "  [output-dir]/jmeter-YYYYMMDD-HHMMSS.log              # JMeter execution log"
    echo "  (default output-dir is target/, YYYYMMDD-HHMMSS is current date and time)"
    echo ""
    echo "REQUIREMENTS:"
    echo "  - JMeter must be installed and available in PATH"
    echo "  - Test plan must exist at: src/test/resources/jmeter/load-test.jmx"
    echo ""
    echo "For more information about JMeter, visit: https://jmeter.apache.org/"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -l|--loops)
            JMETER_LOOPS="$2"
            shift 2
            ;;
        -t|--threads)
            JMETER_THREADS="$2"
            shift 2
            ;;
        -r|--ramp-up)
            JMETER_RAMP_UP="$2"
            shift 2
            ;;
        -o|--output-dir)
            OUTPUT_DIR="$2"
            shift 2
            ;;
        -j|--jtl)
            INPUT_JTL="$2"
            shift 2
            ;;
        -g|--gui)
            GUI_MODE=true
            shift
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Function to check if JMeter is installed
check_jmeter() {
    if ! command -v jmeter &> /dev/null; then
        print_error "JMeter is not installed or not in PATH"
        print_info "Please install JMeter and ensure it's in your PATH"
        print_info "Download from: https://jmeter.apache.org/download_jmeter.cgi"
        exit 1
    fi

    JMETER_VERSION=$(jmeter -v 2>&1 | head -n 1 | grep -o 'Version [0-9.]*' | cut -d' ' -f2)
    print_info "Found JMeter version: $JMETER_VERSION"
}

# Function to check if test plan exists
check_test_plan() {
    if [[ ! -f "$TEST_PLAN" ]]; then
        print_error "JMeter test plan not found: $TEST_PLAN"
        exit 1
    fi
    print_info "Using test plan: $TEST_PLAN"
}

# Function to check if input JTL file exists
check_input_jtl() {
    if [[ ! -f "$INPUT_JTL" ]]; then
        print_error "Input JTL file not found: $INPUT_JTL"
        exit 1
    fi
    print_info "Using input JTL file: $INPUT_JTL"
}

# Function to create target directory
create_target_dir() {
    mkdir -p "$(dirname "$RESULTS_FILE")"
    mkdir -p "$REPORT_DIR"
    print_info "Created output directories"
}

# Function to clean previous results
clean_previous_results() {
    if [[ -f "$RESULTS_FILE" ]]; then
        rm "$RESULTS_FILE"
        print_info "Cleaned previous results file"
    fi

    if [[ -d "$REPORT_DIR" ]]; then
        rm -rf "$REPORT_DIR"
        print_info "Cleaned previous report directory"
    fi
}

# Function to run JMeter test in non-GUI mode
run_jmeter_test() {
    print_info "Starting JMeter test..."
    print_info "Configuration:"
    print_info "  - Loops: $JMETER_LOOPS"
    print_info "  - Threads: $JMETER_THREADS"
    print_info "  - Ramp-up: $JMETER_RAMP_UP seconds"

    # Run JMeter in non-GUI mode
    jmeter -n \
        -t "$TEST_PLAN" \
        -l "$RESULTS_FILE" \
        -e \
        -o "$REPORT_DIR" \
        -Jloops="$JMETER_LOOPS" \
        -Jthreads="$JMETER_THREADS" \
        -Jrampup="$JMETER_RAMP_UP" \
        -j "$LOG_FILE"

    if [[ $? -eq 0 ]]; then
        print_success "JMeter test completed successfully"
    else
        print_error "JMeter test failed"
        exit 1
    fi
}

# Function to run JMeter in GUI mode
run_jmeter_gui() {
    print_info "Opening JMeter GUI..."
    print_info "Test plan will be loaded: $TEST_PLAN"
    print_info "You can configure and run tests manually in the GUI"

    # Run JMeter in GUI mode with test plan loaded
    jmeter -t "$TEST_PLAN" \
        -Jloops="$JMETER_LOOPS" \
        -Jthreads="$JMETER_THREADS" \
        -Jrampup="$JMETER_RAMP_UP"

    print_success "JMeter GUI session completed"
}

# Function to generate report from existing JTL file
generate_report_from_jtl() {
    print_info "Generating HTML report from existing JTL file..."
    print_info "Input JTL: $INPUT_JTL"
    print_info "Output report directory: $REPORT_DIR"

    # Generate HTML report from existing JTL file
    jmeter -g "$INPUT_JTL" -o "$REPORT_DIR"

    if [[ $? -eq 0 ]]; then
        print_success "HTML report generated successfully"
    else
        print_error "Failed to generate HTML report"
        exit 1
    fi
}

# Function to show results
show_results() {
    print_success "Results:"

    if [[ -n "$INPUT_JTL" ]]; then
        # Report generated from existing JTL
        print_info "Source JTL file: $INPUT_JTL"
        print_info "HTML report: $REPORT_DIR/index.html"

        if [[ -f "$INPUT_JTL" ]]; then
            TOTAL_SAMPLES=$(tail -n +2 "$INPUT_JTL" | wc -l)
            print_info "Total samples: $TOTAL_SAMPLES"
        fi
    else
        # Results from test execution
        print_info "Results file: $RESULTS_FILE"
        print_info "HTML report: $REPORT_DIR/index.html"
        print_info "Log file: $LOG_FILE"

        if [[ -f "$RESULTS_FILE" ]]; then
            TOTAL_SAMPLES=$(tail -n +2 "$RESULTS_FILE" | wc -l)
            print_info "Total samples: $TOTAL_SAMPLES"
        fi
    fi

    # Try to open the HTML report
    if command -v open &> /dev/null; then
        print_info "Opening HTML report in browser..."
        open "$REPORT_DIR/index.html"
    elif command -v xdg-open &> /dev/null; then
        print_info "Opening HTML report in browser..."
        xdg-open "$REPORT_DIR/index.html"
    else
        print_info "To view the HTML report, open: file://$REPORT_DIR/index.html"
    fi
}

# Main execution
main() {
    print_info "JMeter Load Test Script"
    print_info "======================="

    # Set output paths after parsing arguments
    set_output_paths

    check_jmeter

    if [[ -n "$INPUT_JTL" ]]; then
        # Report generation mode - generate report from existing JTL file
        check_input_jtl
        create_target_dir
        clean_previous_results
        generate_report_from_jtl
        show_results
        print_success "HTML report generated successfully!"
    elif [[ "$GUI_MODE" == "true" ]]; then
        # GUI mode - just open JMeter with the test plan
        check_test_plan
        run_jmeter_gui
        print_success "JMeter GUI session completed successfully!"
    else
        # Non-GUI mode - run automated test and generate report
        check_test_plan
        create_target_dir
        clean_previous_results
        run_jmeter_test
        show_results
        print_success "JMeter load test completed successfully!"
    fi
}

# Execute main function
main "$@"

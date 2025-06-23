#!/bin/bash

# Load Test Script for Memory Leak Demo API
# Tests memory and thread leak endpoints to demonstrate performance issues

set -e

# Configuration
OBJECTS_URL="http://localhost:8080/api/v1/objects/create"
THREADS_URL="http://localhost:8080/api/v1/threads/create"
TOTAL_REQUESTS=1000
CONCURRENT_REQUESTS=5
OUTPUT_DIR="load_test_results"
RESULTS_FILE="$OUTPUT_DIR/results_$(date +%Y%m%d_%H%M%S).log"
STATS_FILE="$OUTPUT_DIR/stats_$(date +%Y%m%d_%H%M%S).txt"
ENDPOINT="objects"  # Default endpoint to test (objects or threads)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Function to print colored output
print_status() {
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

# Function to get target URL based on endpoint
get_target_url() {
    case "$ENDPOINT" in
        "objects")
            echo "$OBJECTS_URL"
            ;;
        "threads")
            echo "$THREADS_URL"
            ;;
        *)
            print_error "Invalid endpoint: $ENDPOINT. Use 'objects' or 'threads'"
            exit 1
            ;;
    esac
}

# Function to make a single request
make_request() {
    local request_id=$1
    local url=$2
    local start_time=$(date +%s.%3N)
    
    # Make the request and capture response
    response=$(curl -s -w "HTTPSTATUS:%{http_code};TIME:%{time_total};SIZE:%{size_download}" \
                   -H "Accept: application/json" \
                   -H "User-Agent: MemoryLeakTest/1.0" \
                   "$url" 2>/dev/null || echo "HTTPSTATUS:000;TIME:0;SIZE:0")
    
    local end_time=$(date +%s.%3N)
    local duration=$(echo "$end_time - $start_time" | bc -l)
    
    # Parse response
    local http_status=$(echo "$response" | grep -oP 'HTTPSTATUS:\K\d+' || echo "000")
    local response_time=$(echo "$response" | grep -oP 'TIME:\K[\d.]+' || echo "0")
    local response_size=$(echo "$response" | grep -oP 'SIZE:\K\d+' || echo "0")
    
    # Log result
    echo "$request_id,$http_status,$response_time,$response_size,$duration,$ENDPOINT" >> "$RESULTS_FILE"
    
    # Print progress every 50 requests (more frequent for memory leak testing)
    if [ $((request_id % 50)) -eq 0 ]; then
        print_status "Completed $request_id/$TOTAL_REQUESTS requests"
        if [ "$ENDPOINT" = "objects" ]; then
            print_warning "Memory usage may be increasing significantly!"
        elif [ "$ENDPOINT" = "threads" ]; then
            print_warning "Thread count may be increasing significantly!"
        fi
    fi
}

# Function to run requests in parallel batches
run_load_test() {
    local url=$(get_target_url)
    
    print_status "Starting load test on $ENDPOINT endpoint with $TOTAL_REQUESTS requests ($CONCURRENT_REQUESTS concurrent)"
    print_status "Target URL: $url"
    print_status "Results will be saved to: $RESULTS_FILE"
    
    if [ "$ENDPOINT" = "objects" ]; then
        print_warning "This test will create memory leaks by accumulating objects in memory!"
        print_warning "Monitor memory usage with: jstat -gc <pid> 1s"
    elif [ "$ENDPOINT" = "threads" ]; then
        print_warning "This test will create thread leaks by not closing ExecutorService!"
        print_warning "Monitor thread count with: jstack <pid> | grep Thread | wc -l"
    fi
    
    # Initialize results file with header
    echo "request_id,http_status,response_time,response_size,total_duration,endpoint" > "$RESULTS_FILE"
    
    local remaining_requests=$TOTAL_REQUESTS
    local request_counter=1
    
    # Start time
    local test_start_time=$(date +%s)
    
    while [ $remaining_requests -gt 0 ]; do
        local current_batch_size=$CONCURRENT_REQUESTS
        if [ $remaining_requests -lt $CONCURRENT_REQUESTS ]; then
            current_batch_size=$remaining_requests
        fi
        
        # Start batch of concurrent requests
        for ((i=0; i<current_batch_size; i++)); do
            make_request $request_counter "$url" &
            ((request_counter++))
            ((remaining_requests--))
        done
        
        # Wait for current batch to complete
        wait
        
        # Longer delay for memory leak testing to allow garbage collection
        sleep 0.5
    done
    
    local test_end_time=$(date +%s)
    local total_test_time=$((test_end_time - test_start_time))
    
    print_success "Load test completed in ${total_test_time} seconds"
    
    if [ "$ENDPOINT" = "objects" ]; then
        print_warning "Memory leak test completed. Check memory usage - it should have increased significantly!"
        print_status "Expected memory impact: ~$(echo "$TOTAL_REQUESTS * 1000 * 50" | bc) bytes of string data accumulated"
    elif [ "$ENDPOINT" = "threads" ]; then
        print_warning "Thread leak test completed. Check thread count - it should have increased significantly!"
        print_status "Expected thread impact: ~$(echo "$TOTAL_REQUESTS * 10" | bc) leaked threads"
    fi
}

# Function to generate statistics
generate_stats() {
    print_status "Generating statistics..."
    
    if [ ! -f "$RESULTS_FILE" ]; then
        print_error "Results file not found: $RESULTS_FILE"
        return 1
    fi
    
    # Skip header line and calculate stats
    local data_file="${RESULTS_FILE}.data"
    tail -n +2 "$RESULTS_FILE" > "$data_file"
    
    local total_requests=$(wc -l < "$data_file")
    local successful_requests=$(awk -F',' '$2 == 200 { count++ } END { print count+0 }' "$data_file")
    local failed_requests=$((total_requests - successful_requests))
    local success_rate=$(echo "scale=2; $successful_requests * 100 / $total_requests" | bc -l)
    
    # Response time statistics (using response_time column)
    local avg_response_time=$(awk -F',' '{ sum += $3; count++ } END { print sum/count }' "$data_file")
    local min_response_time=$(awk -F',' 'NR==1 { min=$3 } $3 < min { min=$3 } END { print min }' "$data_file")
    local max_response_time=$(awk -F',' 'NR==1 { max=$3 } $3 > max { max=$3 } END { print max }' "$data_file")
    
    # HTTP status code distribution
    local status_distribution=$(awk -F',' '{ status[$2]++ } END { for (s in status) print s ": " status[s] }' "$data_file" | sort -n)
    
    # Generate statistics report
    {
        echo "=== MEMORY LEAK DEMO LOAD TEST STATISTICS ==="
        echo "Date: $(date)"
        echo "Target Endpoint: $ENDPOINT"
        echo "Target URL: $(get_target_url)"
        echo "Total Requests: $total_requests"
        echo "Successful Requests (200): $successful_requests"
        echo "Failed Requests: $failed_requests"
        echo "Success Rate: ${success_rate}%"
        echo ""
        echo "=== RESPONSE TIME STATISTICS ==="
        echo "Average Response Time: ${avg_response_time}s"
        echo "Min Response Time: ${min_response_time}s"
        echo "Max Response Time: ${max_response_time}s"
        echo ""
        echo "=== HTTP STATUS CODE DISTRIBUTION ==="
        echo "$status_distribution"
        echo ""
        echo "=== MEMORY LEAK ANALYSIS ==="
        if [ "$ENDPOINT" = "objects" ]; then
            echo "Endpoint tested: Objects Creation (Memory Leak)"
            echo "Expected behavior: Memory usage should increase significantly"
            echo "Objects created per request: 1000"
            echo "Estimated memory impact: ~$(echo "$total_requests * 1000 * 50" | bc) bytes"
            echo "Recommendation: Monitor with 'jstat -gc <pid>' or 'jhsdb jmap'"
        elif [ "$ENDPOINT" = "threads" ]; then
            echo "Endpoint tested: Thread Creation (Thread Leak)"
            echo "Expected behavior: Thread count should increase significantly"
            echo "Threads created per request: 10"
            echo "Estimated thread impact: ~$(echo "$total_requests * 10" | bc) threads"
            echo "Recommendation: Monitor with 'jstack <pid>' or thread dump analysis"
        fi
        echo ""
        echo "=== FILES ==="
        echo "Detailed Results: $RESULTS_FILE"
        echo "Statistics Report: $STATS_FILE"
    } | tee "$STATS_FILE"
    
    # Cleanup temporary file
    rm -f "$data_file"
    
    print_success "Statistics saved to: $STATS_FILE"
}

# Function to check if server is running
check_server() {
    local url=$(get_target_url)
    print_status "Checking if server is running..."
    
    if curl -s --max-time 5 "$url" > /dev/null 2>&1; then
        print_success "Server is responding"
        return 0
    else
        print_error "Server is not responding at $url"
        print_error "Please make sure the Spring Boot application is running"
        print_error "Start with: ./mvnw spring-boot:run"
        return 1
    fi
}

# Function to run both endpoint tests
run_both_tests() {
    print_status "Running comprehensive memory leak tests on both endpoints..."
    
    # Test objects endpoint
    ENDPOINT="objects"
    RESULTS_FILE="$OUTPUT_DIR/objects_results_$(date +%Y%m%d_%H%M%S).log"
    STATS_FILE="$OUTPUT_DIR/objects_stats_$(date +%Y%m%d_%H%M%S).txt"
    
    print_status "=== Testing Objects Endpoint (Memory Leak) ==="
    if check_server; then
        run_load_test
        generate_stats
    fi
    
    print_status "Waiting 10 seconds before next test..."
    sleep 10
    
    # Test threads endpoint
    ENDPOINT="threads"
    RESULTS_FILE="$OUTPUT_DIR/threads_results_$(date +%Y%m%d_%H%M%S).log"
    STATS_FILE="$OUTPUT_DIR/threads_stats_$(date +%Y%m%d_%H%M%S).txt"
    
    print_status "=== Testing Threads Endpoint (Thread Leak) ==="
    if check_server; then
        run_load_test
        generate_stats
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Memory Leak Demo Load Test - Tests endpoints that demonstrate memory and thread leaks"
    echo ""
    echo "Options:"
    echo "  -e, --endpoint ENDPOINT Endpoint to test: 'objects', 'threads', or 'both' (default: $ENDPOINT)"
    echo "  -n, --requests NUMBER   Total number of requests (default: $TOTAL_REQUESTS)"
    echo "  -c, --concurrent NUMBER Concurrent requests (default: $CONCURRENT_REQUESTS)"
    echo "  -h, --help             Show this help message"
    echo ""
    echo "Endpoints:"
    echo "  objects                Test /api/v1/objects/create (memory leak)"
    echo "  threads                Test /api/v1/threads/create (thread leak)"
    echo "  both                   Test both endpoints sequentially"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Test objects endpoint with defaults"
    echo "  $0 -e threads                        # Test threads endpoint"
    echo "  $0 -e both                           # Test both endpoints"
    echo "  $0 -n 500 -c 3                      # 500 requests, 3 concurrent"
    echo "  $0 -e objects -n 2000 -c 10         # Heavy memory leak test"
    echo ""
    echo "Monitoring:"
    echo "  Memory: jstat -gc <pid> 1s"
    echo "  Threads: jstack <pid> | grep Thread | wc -l"
    echo "  Process: jps | grep MainApplication"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -e|--endpoint)
            ENDPOINT="$2"
            shift 2
            ;;
        -n|--requests)
            TOTAL_REQUESTS="$2"
            shift 2
            ;;
        -c|--concurrent)
            CONCURRENT_REQUESTS="$2"
            shift 2
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

# Main execution
main() {
    echo "=== Memory Leak Demo API Load Test ==="
    echo ""
    
    # Check dependencies
    if ! command -v curl &> /dev/null; then
        print_error "curl is required but not installed"
        exit 1
    fi
    
    if ! command -v bc &> /dev/null; then
        print_error "bc is required but not installed"
        exit 1
    fi
    
    # Handle special case for testing both endpoints
    if [ "$ENDPOINT" = "both" ]; then
        run_both_tests
    else
        # Validate endpoint
        if [ "$ENDPOINT" != "objects" ] && [ "$ENDPOINT" != "threads" ]; then
            print_error "Invalid endpoint: $ENDPOINT. Use 'objects', 'threads', or 'both'"
            show_usage
            exit 1
        fi
        
        # Check if server is running
        if ! check_server; then
            exit 1
        fi
        
        # Run the load test
        run_load_test
        
        # Generate statistics
        generate_stats
    fi
    
    print_success "Memory leak demo load test completed successfully!"
    print_status "Check the results in: $OUTPUT_DIR"
    print_warning "Remember to restart the application to clear any accumulated leaks!"
}

# Run main function
main "$@" 
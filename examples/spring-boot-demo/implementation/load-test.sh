#!/bin/bash

# Load Test Script for Films API
# Makes 10,000 requests to http://localhost:8080/api/v1/films?startsWith=A

set -e

# Configuration
URL="http://localhost:8080/api/v1/films?startsWith=A"
TOTAL_REQUESTS=10000
CONCURRENT_REQUESTS=10
OUTPUT_DIR="load_test_results"
RESULTS_FILE="$OUTPUT_DIR/results_$(date +%Y%m%d_%H%M%S).log"
STATS_FILE="$OUTPUT_DIR/stats_$(date +%Y%m%d_%H%M%S).txt"

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

# Function to make a single request
make_request() {
    local request_id=$1
    local start_time=$(date +%s.%3N)
    
    # Make the request and capture response
    response=$(curl -s -w "HTTPSTATUS:%{http_code};TIME:%{time_total};SIZE:%{size_download}" \
                   -H "Accept: application/json" \
                   -H "User-Agent: LoadTest/1.0" \
                   "$URL" 2>/dev/null || echo "HTTPSTATUS:000;TIME:0;SIZE:0")
    
    local end_time=$(date +%s.%3N)
    local duration=$(echo "$end_time - $start_time" | bc -l)
    
    # Parse response
    local http_status=$(echo "$response" | grep -oP 'HTTPSTATUS:\K\d+' || echo "000")
    local response_time=$(echo "$response" | grep -oP 'TIME:\K[\d.]+' || echo "0")
    local response_size=$(echo "$response" | grep -oP 'SIZE:\K\d+' || echo "0")
    
    # Log result
    echo "$request_id,$http_status,$response_time,$response_size,$duration" >> "$RESULTS_FILE"
    
    # Print progress every 100 requests
    if [ $((request_id % 100)) -eq 0 ]; then
        print_status "Completed $request_id/$TOTAL_REQUESTS requests"
    fi
}

# Function to run requests in parallel batches
run_load_test() {
    print_status "Starting load test with $TOTAL_REQUESTS requests ($CONCURRENT_REQUESTS concurrent)"
    print_status "Target URL: $URL"
    print_status "Results will be saved to: $RESULTS_FILE"
    
    # Initialize results file with header
    echo "request_id,http_status,response_time,response_size,total_duration" > "$RESULTS_FILE"
    
    local batch_size=$((TOTAL_REQUESTS / CONCURRENT_REQUESTS))
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
            make_request $request_counter &
            ((request_counter++))
            ((remaining_requests--))
        done
        
        # Wait for current batch to complete
        wait
        
        # Small delay between batches to avoid overwhelming the server
        sleep 0.1
    done
    
    local test_end_time=$(date +%s)
    local total_test_time=$((test_end_time - test_start_time))
    
    print_success "Load test completed in ${total_test_time} seconds"
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
        echo "=== LOAD TEST STATISTICS ==="
        echo "Date: $(date)"
        echo "Target URL: $URL"
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
    print_status "Checking if server is running..."
    
    if curl -s --max-time 5 "$URL" > /dev/null 2>&1; then
        print_success "Server is responding"
        return 0
    else
        print_error "Server is not responding at $URL"
        print_error "Please make sure the Spring Boot application is running"
        return 1
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -u, --url URL           Target URL (default: $URL)"
    echo "  -n, --requests NUMBER   Total number of requests (default: $TOTAL_REQUESTS)"
    echo "  -c, --concurrent NUMBER Concurrent requests (default: $CONCURRENT_REQUESTS)"
    echo "  -h, --help             Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Run with defaults"
    echo "  $0 -n 5000 -c 20                    # 5000 requests, 20 concurrent"
    echo "  $0 -u http://example.com/api         # Different URL"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -u|--url)
            URL="$2"
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
    echo "=== Films API Load Test ==="
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
    
    # Check if server is running
    if ! check_server; then
        exit 1
    fi
    
    # Run the load test
    run_load_test
    
    # Generate statistics
    generate_stats
    
    print_success "Load test completed successfully!"
    print_status "Check the results in: $OUTPUT_DIR"
}

# Run main function
main "$@" 
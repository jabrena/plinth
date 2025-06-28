#!/bin/bash

# Load Test Script for Performance Bottleneck Demo API
# Tests inefficient search endpoints to demonstrate performance bottlenecks

set -e

# Configuration
BASE_URL="http://localhost:8080/api/search"
COLLEAGUES_URL="$BASE_URL/optimized/users-with-colleagues"
PERMISSIONS_URL="$BASE_URL/optimized/active-users-with-permissions"
SIMILAR_URL="$BASE_URL/optimized/similar-users"
TEAM_URL="$BASE_URL/optimized/team-formation"
TOTAL_REQUESTS=100
CONCURRENT_REQUESTS=3
OUTPUT_DIR="load_test_results"
RESULTS_FILE="$OUTPUT_DIR/results_$(date +%Y%m%d_%H%M%S).log"
STATS_FILE="$OUTPUT_DIR/stats_$(date +%Y%m%d_%H%M%S).txt"
ENDPOINT="colleagues"  # Default endpoint to test (colleagues, permissions, similar, team, all)

# Test parameters for different endpoints
DEPARTMENT_PARAM="Engineering"
ROLE_PARAM="developer"
KEYWORD_PARAM="john"

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

# Function to get target URL and parameters based on endpoint
get_target_url_and_params() {
    case "$ENDPOINT" in
        "colleagues")
            echo "$COLLEAGUES_URL?department=$DEPARTMENT_PARAM"
            ;;
        "permissions")
            echo "$PERMISSIONS_URL?role=$ROLE_PARAM"
            ;;
        "similar")
            echo "$SIMILAR_URL?keyword=$KEYWORD_PARAM"
            ;;
        "team")
            echo "$TEAM_URL?department=$DEPARTMENT_PARAM"
            ;;
        *)
            print_error "Invalid endpoint: $ENDPOINT. Use 'colleagues', 'permissions', 'similar', 'team', or 'all'"
            exit 1
            ;;
    esac
}

# Function to get endpoint complexity description
get_endpoint_complexity() {
    case "$ENDPOINT" in
        "colleagues")
            echo "O(n²) - Nested loops for finding users with colleagues"
            ;;
        "permissions")
            echo "O(n²) - Cross-referencing users with permitted roles"
            ;;
        "similar")
            echo "O(n²) - Duplicate detection with nested loops"
            ;;
        "team")
            echo "O(n³) - Triple nested loops for team formation"
            ;;
        *)
            echo "Unknown complexity"
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
                   -H "User-Agent: PerformanceBottleneckTest/1.0" \
                   "$url" 2>/dev/null || echo "HTTPSTATUS:000;TIME:0;SIZE:0")
    
    local end_time=$(date +%s.%3N)
    local duration=$(echo "$end_time - $start_time" | bc -l)
    
    # Parse response
    local http_status=$(echo "$response" | grep -oP 'HTTPSTATUS:\K\d+' || echo "000")
    local response_time=$(echo "$response" | grep -oP 'TIME:\K[\d.]+' || echo "0")
    local response_size=$(echo "$response" | grep -oP 'SIZE:\K\d+' || echo "0")
    
    # Log result
    echo "$request_id,$http_status,$response_time,$response_size,$duration,$ENDPOINT" >> "$RESULTS_FILE"
    
    # Print progress every 25 requests for performance testing
    if [ $((request_id % 25)) -eq 0 ]; then
        print_status "Completed $request_id/$TOTAL_REQUESTS requests"
        local complexity=$(get_endpoint_complexity)
        print_warning "Testing $complexity - Response time may increase significantly!"
    fi
}

# Function to run requests in parallel batches
run_load_test() {
    local url=$(get_target_url_and_params)
    local complexity=$(get_endpoint_complexity)
    
    print_status "Starting performance bottleneck test on $ENDPOINT endpoint with $TOTAL_REQUESTS requests ($CONCURRENT_REQUESTS concurrent)"
    print_status "Target URL: $url"
    print_status "Algorithm Complexity: $complexity"
    print_status "Results will be saved to: $RESULTS_FILE"
    
    print_warning "This test will demonstrate performance bottlenecks with inefficient algorithms!"
    print_warning "Monitor CPU usage and response times - they may increase dramatically!"
    
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
        
        # Brief delay to avoid overwhelming the server
        sleep 0.1
    done
    
    local test_end_time=$(date +%s)
    local total_test_time=$((test_end_time - test_start_time))
    
    print_success "Performance bottleneck test completed in ${total_test_time} seconds"
    print_warning "Check response times - they should show performance degradation due to inefficient algorithms!"
}

# Function to generate statistics
generate_stats() {
    print_status "Generating performance statistics..."
    
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
    
    # Performance degradation analysis
    local first_quarter_avg=$(head -n $((total_requests/4)) "$data_file" | awk -F',' '{ sum += $3; count++ } END { print sum/count }')
    local last_quarter_avg=$(tail -n $((total_requests/4)) "$data_file" | awk -F',' '{ sum += $3; count++ } END { print sum/count }')
    local performance_degradation=$(echo "scale=2; ($last_quarter_avg - $first_quarter_avg) * 100 / $first_quarter_avg" | bc -l)
    
    local complexity=$(get_endpoint_complexity)
    
    # Generate statistics report
    {
        echo "=== PERFORMANCE BOTTLENECK DEMO LOAD TEST STATISTICS ==="
        echo "Date: $(date)"
        echo "Target Endpoint: $ENDPOINT"
        echo "Target URL: $(get_target_url_and_params)"
        echo "Algorithm Complexity: $complexity"
        echo "Total Requests: $total_requests"
        echo "Successful Requests (200): $successful_requests"
        echo "Failed Requests: $failed_requests"
        echo "Success Rate: ${success_rate}%"
        echo ""
        echo "=== RESPONSE TIME STATISTICS ==="
        echo "Average Response Time: ${avg_response_time}s"
        echo "Min Response Time: ${min_response_time}s"
        echo "Max Response Time: ${max_response_time}s"
        echo "First Quarter Avg: ${first_quarter_avg}s"
        echo "Last Quarter Avg: ${last_quarter_avg}s"
        echo "Performance Degradation: ${performance_degradation}%"
        echo ""
        echo "=== HTTP STATUS CODE DISTRIBUTION ==="
        echo "$status_distribution"
        echo ""
        echo "=== PERFORMANCE BOTTLENECK ANALYSIS ==="
        case "$ENDPOINT" in
            "colleagues")
                echo "Endpoint tested: Users with Colleagues (O(n²) Nested Loops)"
                echo "Expected behavior: Response time increases quadratically with data size"
                echo "Bottleneck: Nested loops for finding users with matching department"
                echo "Optimization: Use HashMaps or Set data structures for O(1) lookups"
                ;;
            "permissions")
                echo "Endpoint tested: Active Users with Permissions (O(n²) Cross-Reference)"
                echo "Expected behavior: Response time increases quadratically with data size"
                echo "Bottleneck: Linear search through roles for each user"
                echo "Optimization: Pre-build role index or use Set for O(1) role lookup"
                ;;
            "similar")
                echo "Endpoint tested: Similar Users (O(n²) Duplicate Detection)"
                echo "Expected behavior: Response time increases quadratically with matching users"
                echo "Bottleneck: Nested loops with linear duplicate checking"
                echo "Optimization: Use HashSet for O(1) duplicate detection"
                ;;
            "team")
                echo "Endpoint tested: Team Formation (O(n³) Triple Nested)"
                echo "Expected behavior: Response time increases cubically with data size"
                echo "Bottleneck: Triple nested loops for finding complete teams"
                echo "Optimization: Group users by role and department first, then combine"
                ;;
        esac
        echo ""
        echo "=== RECOMMENDATIONS ==="
        echo "1. Implement efficient data structures (HashMap, HashSet)"
        echo "2. Reduce nested loops with pre-filtering and indexing"
        echo "3. Use database queries with proper indexing instead of in-memory processing"
        echo "4. Consider caching frequently accessed data"
        echo "5. Implement pagination for large result sets"
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
    local url=$(get_target_url_and_params)
    print_status "Checking if server is running..."
    
    if curl -s --max-time 10 "$url" > /dev/null 2>&1; then
        print_success "Server is responding"
        return 0
    else
        print_error "Server is not responding at $url"
        print_error "Please make sure the Spring Boot application is running"
        print_error "Start with: ./mvnw spring-boot:run"
        return 1
    fi
}

# Function to run all endpoint tests
run_all_tests() {
    print_status "Running comprehensive performance bottleneck tests on all endpoints..."
    
    local endpoints=("colleagues" "permissions" "similar" "team")
    
    for endpoint in "${endpoints[@]}"; do
        ENDPOINT="$endpoint"
        RESULTS_FILE="$OUTPUT_DIR/${endpoint}_results_$(date +%Y%m%d_%H%M%S).log"
        STATS_FILE="$OUTPUT_DIR/${endpoint}_stats_$(date +%Y%m%d_%H%M%S).txt"
        
        print_status "=== Testing $endpoint Endpoint ($(get_endpoint_complexity)) ==="
        if check_server; then
            run_load_test
            generate_stats
        fi
        
        if [ "$endpoint" != "team" ]; then
            print_status "Waiting 5 seconds before next test..."
            sleep 5
        fi
    done
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Performance Bottleneck Demo Load Test - Tests inefficient search endpoints"
    echo ""
    echo "Options:"
    echo "  -e, --endpoint ENDPOINT Endpoint to test: 'colleagues', 'permissions', 'similar', 'team', or 'all' (default: $ENDPOINT)"
    echo "  -n, --requests NUMBER   Total number of requests (default: $TOTAL_REQUESTS)"
    echo "  -c, --concurrent NUMBER Concurrent requests (default: $CONCURRENT_REQUESTS)"
    echo "  -d, --department NAME   Department parameter (default: $DEPARTMENT_PARAM)"
    echo "  -r, --role NAME         Role parameter (default: $ROLE_PARAM)"
    echo "  -k, --keyword NAME      Keyword parameter (default: $KEYWORD_PARAM)"
    echo "  -h, --help             Show this help message"
    echo ""
    echo "Endpoints:"
    echo "  colleagues             Test /api/search/bad/users-with-colleagues (O(n²) nested loops)"
    echo "  permissions            Test /api/search/bad/active-users-with-permissions (O(n²) cross-reference)"
    echo "  similar                Test /api/search/bad/similar-users (O(n²) duplicate detection)"
    echo "  team                   Test /api/search/bad/team-formation (O(n³) triple nested)"
    echo "  all                    Test all endpoints sequentially"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Test colleagues endpoint with defaults"
    echo "  $0 -e team                           # Test team formation endpoint (O(n³))"
    echo "  $0 -e all                            # Test all endpoints"
    echo "  $0 -n 200 -c 5                      # 200 requests, 5 concurrent"
    echo "  $0 -e colleagues -d 'Marketing'      # Test with Marketing department"
    echo ""
    echo "Performance Analysis:"
    echo "  Response times should increase significantly due to inefficient algorithms"
    echo "  O(n²) endpoints: Quadratic time complexity"
    echo "  O(n³) endpoints: Cubic time complexity"
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
        -d|--department)
            DEPARTMENT_PARAM="$2"
            shift 2
            ;;
        -r|--role)
            ROLE_PARAM="$2"
            shift 2
            ;;
        -k|--keyword)
            KEYWORD_PARAM="$2"
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
    echo "=== Performance Bottleneck Demo API Load Test ==="
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
    
    # Handle special case for testing all endpoints
    if [ "$ENDPOINT" = "all" ]; then
        run_all_tests
    else
        # Validate endpoint
        if [ "$ENDPOINT" != "colleagues" ] && [ "$ENDPOINT" != "permissions" ] && [ "$ENDPOINT" != "similar" ] && [ "$ENDPOINT" != "team" ]; then
            print_error "Invalid endpoint: $ENDPOINT. Use 'colleagues', 'permissions', 'similar', 'team', or 'all'"
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
    
    print_success "Performance bottleneck demo load test completed successfully!"
    print_status "Check the results in: $OUTPUT_DIR"
    print_warning "Consider implementing optimizations to improve algorithm efficiency!"
}

# Run main function
main "$@" 
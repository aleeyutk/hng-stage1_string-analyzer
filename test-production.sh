#!/bin/bash

echo "=== Production API Test Suite ==="
echo

# Set your PXXL app URL here
PXXL_URL="${1:-http://localhost:8080}"

echo "Testing API at: $PXXL_URL"
echo

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_status() {
    if [ "$1" -ge 200 ] && [ "$1" -lt 300 ]; then
        echo -e "${GREEN}✓ Status: $1${NC}"
    elif [ "$1" -ge 400 ] && [ "$1" -lt 500 ]; then
        echo -e "${YELLOW}⚠ Status: $1${NC}"
    else
        echo -e "${RED}✗ Status: $1${NC}"
    fi
}

test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo "Testing: $description"
    echo "Endpoint: $method $endpoint"
    
    if [ "$method" = "POST" ]; then
        response=$(curl -s -w "%{http_code}" -X POST "$PXXL_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -X DELETE "$PXXL_URL$endpoint")
    else
        response=$(curl -s -w "%{http_code}" "$PXXL_URL$endpoint")
    fi
    
    http_code=${response: -3}
    json_response=${response%???}
    
    if [ ! -z "$json_response" ]; then
        echo "Response:"
        echo "$json_response" | jq . 2>/dev/null || echo "$json_response"
    fi
    
    print_status $http_code
    echo
}

# Test basic endpoints
test_endpoint "GET" "/" "{}" "Root endpoint"
test_endpoint "GET" "/health" "{}" "Health check"
test_endpoint "GET" "/test" "{}" "Test endpoint"

# Test main functionality
test_endpoint "POST" "/strings" '{"value": "hello world"}' "Analyze string"
test_endpoint "POST" "/strings" '{"value": "madam"}' "Analyze palindrome"
test_endpoint "GET" "/strings/hello%20world" "{}" "Get specific string"
test_endpoint "GET" "/strings" "{}" "Get all strings"
test_endpoint "GET" "/strings?is_palindrome=true" "{}" "Filter palindromes"
test_endpoint "GET" "/strings/filter-by-natural-language?query=palindromic%20strings" "{}" "Natural language query"

# Test error cases
test_endpoint "POST" "/strings" '{"value": "hello world"}' "Duplicate string (should fail)"
test_endpoint "GET" "/strings/nonexistent" "{}" "Get non-existent string (should fail)"

echo "=== Production Test Complete ==="
echo "API URL: $PXXL_URL"

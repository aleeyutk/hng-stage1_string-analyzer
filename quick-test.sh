#!/bin/bash

echo "=== Quick API Test ==="
echo

URL="${1:-http://localhost:8080}"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

check_endpoint() {
    local endpoint=$1
    local expected=$2
    local description=$3
    
    response=$(curl -s -w "%{http_code}" $URL$endpoint)
    code=${response: -3}
    
    if [ "$code" == "$expected" ]; then
        echo -e "${GREEN}✓ $description${NC}"
    else
        echo -e "${RED}✗ $description (got $code, expected $expected)${NC}"
    fi
}

# Check if app is running
echo "Checking if application is running..."
if curl -s $URL/test >/dev/null; then
    echo -e "${GREEN}✓ Application is running${NC}"
else
    echo -e "${RED}✗ Application is not running${NC}"
    echo "Start the application first: mvn spring-boot:run"
    exit 1
fi

# Test endpoints
check_endpoint "/test" "200" "Test endpoint"
check_endpoint "/health" "200" "Health endpoint"
check_endpoint "/" "200" "Root endpoint"

# Test string analysis
echo "Testing string analysis..."
analysis=$(curl -s -X POST $URL/strings \
    -H "Content-Type: application/json" \
    -d '{"value": "hello"}' | jq -r '.id')

if [ ! -z "$analysis" ] && [ "$analysis" != "null" ]; then
    echo -e "${GREEN}✓ String analysis working${NC}"
    
    # Test getting the analyzed string
    check_endpoint "/strings/hello" "200" "Get analyzed string"
else
    echo -e "${RED}✗ String analysis failed${NC}"
fi

echo
echo -e "${GREEN}Test completed!${NC}"

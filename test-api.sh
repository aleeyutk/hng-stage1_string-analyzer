#!/bin/bash

echo "=== String Analyzer API Test Suite ==="
echo

BASE_URL="http://localhost:8080"

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

echo "1. Testing POST /strings (Success Cases)"
echo "----------------------------------------"

echo "Test 1.1: Analyze simple string"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": "hello world"}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "Test 1.2: Analyze palindrome"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": "madam"}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "Test 1.3: Analyze string with special characters"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": "Hello @World! 123"}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "2. Testing POST /strings (Error Cases)"
echo "--------------------------------------"

echo "Test 2.1: Duplicate string (409 Conflict)"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": "hello world"}')
http_code=${response: -3}
echo "Expected: 409 Conflict"
print_status $http_code
echo

echo "Test 2.2: Missing value field (400 Bad Request)"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "Test 2.3: Empty string value (400 Bad Request)"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": ""}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "Test 2.4: Null value (400 Bad Request)"
response=$(curl -s -w "%{http_code}" -X POST $BASE_URL/strings \
  -H "Content-Type: application/json" \
  -d '{"value": null}')
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "3. Testing GET /strings/{string_value}"
echo "--------------------------------------"

echo "Test 3.1: Get existing string (200 OK)"
response=$(curl -s -w "%{http_code}" $BASE_URL/strings/hello%20world)
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "Test 3.2: Get non-existent string (404 Not Found)"
response=$(curl -s -w "%{http_code}" $BASE_URL/strings/nonexistent)
http_code=${response: -3}
echo "Expected: 404 Not Found"
print_status $http_code
echo

echo "4. Testing GET /strings with filters"
echo "-----------------------------------"

echo "Test 4.1: Get all strings (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings")
http_code=${response: -3}
json_response=${response%???}
echo "Response count:"
echo $json_response | jq '.count'
print_status $http_code
echo

echo "Test 4.2: Filter by palindrome=true (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings?is_palindrome=true")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value, is_palindrome: .properties.is_palindrome}'
print_status $http_code
echo

echo "Test 4.3: Filter by min_length=10 (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings?min_length=10")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value, length: .properties.length}'
print_status $http_code
echo

echo "Test 4.4: Filter by contains_character=@ (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings?contains_character=@")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value}'
print_status $http_code
echo

echo "5. Testing GET /strings/filter-by-natural-language"
echo "--------------------------------------------------"

echo "Test 5.1: Natural language - palindromic strings (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings/filter-by-natural-language?query=palindromic%20strings")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value, is_palindrome: .properties.is_palindrome}'
print_status $http_code
echo

echo "Test 5.2: Natural language - strings longer than 5 characters (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings/filter-by-natural-language?query=strings%20longer%20than%205%20characters")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value, length: .properties.length}'
print_status $http_code
echo

echo "Test 5.3: Natural language - single word strings (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings/filter-by-natural-language?query=single%20word%20strings")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value, word_count: .properties.word_count}'
print_status $http_code
echo

echo "Test 5.4: Natural language - strings containing letter e (200 OK)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings/filter-by-natural-language?query=strings%20containing%20the%20letter%20e")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq '.data[] | {value: .value}'
print_status $http_code
echo

echo "Test 5.5: Natural language - invalid query (400 Bad Request)"
response=$(curl -s -w "%{http_code}" "$BASE_URL/strings/filter-by-natural-language?query=")
http_code=${response: -3}
json_response=${response%???}
echo "Response:"
echo $json_response | jq .
print_status $http_code
echo

echo "6. Testing DELETE /strings/{string_value}"
echo "----------------------------------------"

echo "Test 6.1: Delete existing string (204 No Content)"
response=$(curl -s -w "%{http_code}" -X DELETE $BASE_URL/strings/madam)
http_code=${response: -3}
echo "Expected: 204 No Content (empty response)"
print_status $http_code
echo

echo "Test 6.2: Delete non-existent string (404 Not Found)"
response=$(curl -s -w "%{http_code}" -X DELETE $BASE_URL/strings/nonexistent)
http_code=${response: -3}
echo "Expected: 404 Not Found"
print_status $http_code
echo

echo "Test 6.3: Verify deletion by trying to get deleted string"
response=$(curl -s -w "%{http_code}" $BASE_URL/strings/madam)
http_code=${response: -3}
echo "Expected: 404 Not Found"
print_status $http_code
echo

echo "7. Testing Response Format Validation"
echo "------------------------------------"

echo "Test 7.1: Verify response structure for successful analysis"
response=$(curl -s $BASE_URL/strings/test)
echo "Required fields check:"
echo $response | jq -r '
  "id: \(.id != null and .id != "")",
  "value: \(.value != null and .value != "")", 
  "properties: \(.properties != null)",
  "properties.length: \(.properties.length != null)",
  "properties.is_palindrome: \(.properties.is_palindrome != null)",
  "properties.unique_characters: \(.properties.unique_characters != null)",
  "properties.word_count: \(.properties.word_count != null)",
  "properties.sha256_hash: \(.properties.sha256_hash != null and .properties.sha256_hash != "")",
  "properties.character_frequency_map: \(.properties.character_frequency_map != null)",
  "created_at: \(.created_at != null and .created_at != "")"
'
echo

echo "=== Test Suite Complete ==="

# String Analyzer API

A Spring Boot RESTful API service that analyzes strings and stores their computed properties with advanced filtering capabilities.

## Features

- **String Analysis**: Compute comprehensive properties including length, palindrome check, character frequency, and SHA-256 hash
- **Advanced Filtering**: Filter strings by various criteria including palindrome status, length, word count, and character presence
- **Natural Language Processing**: Query strings using natural language phrases
- **RESTful API**: Fully compliant with REST standards and proper HTTP status codes
- **In-Memory Storage**: Fast, transient storage for demo and testing purposes

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.0**
- **Maven** for dependency management
- **H2 Database** (optional, for persistence)
- **JUnit 5** for testing

## API Endpoints
```
1. Analyze String
```http
POST /strings
Content-Type: application/json

{
  "value": "string to analyze"
}
Success Response (201 Created):

json
{
  "id": "sha256_hash_value",
  "value": "string to analyze",
  "properties": {
    "length": 16,
    "is_palindrome": false,
    "unique_characters": 12,
    "word_count": 3,
    "sha256_hash": "abc123...",
    "character_frequency_map": {
      "s": 2,
      "t": 3,
      "r": 2
    }
  },
  "created_at": "2025-10-20T10:00:00Z"
}

2. Get Specific String
http
GET /strings/{string_value}

3. Get All Strings with Filtering
http
GET /strings?is_palindrome=true&min_length=5&max_length=20&word_count=2&contains_character=a

4. Natural Language Filtering
http
GET /strings/filter-by-natural-language?query=all%20single%20word%20palindromic%20strings

5. Delete String
http
DELETE /strings/{string_value}
```
# Quick Start
Prerequisites
Java 17 or higher
Maven 3.6+

# Local Development
Clone the repository
```
git clone 
cd string-analyzer

#Build the application
mvn clean package
Run the application

mvn spring-boot:run
```
```
Access the API
http://localhost:8080

```
# Testing the API
Use the provided test script:
chmod +x test-api.sh
./test-api.sh
OR
Test manually:
```
# Analyze a string
curl -X POST http://localhost:8080/strings \
  -H "Content-Type: application/json" \
  -d '{"value": "hello world"}' | jq

# Get all strings
curl http://localhost:8080/strings | jq

# Natural language filtering
curl "http://localhost:8080/strings/filter-by-natural-language?query=palindromic%20strings" | jq
```
```
```
# Project Structure

src/main/java/com/haidara/stringanalyzer/
├── StringAnalyzerApplication.java     # Main application class
├── controller/
│   ├── StringAnalysisController.java  # REST API endpoints
│   └── TestController.java           # Health check endpoints
├── service/
│   ├── InMemoryStringAnalysisService.java  # Business logic
│   └── NaturalLanguageProcessor.java       # NLP for queries
├── model/
│   └── dto/
│       ├── AnalyzeRequest.java        # Request DTO
│       ├── StringAnalysisResponse.java # Response DTO
│       └── FilterResponse.java        # Filter response DTO
└── exception/
    └── GlobalExceptionHandler.java    # Error handling

## 🚀 Live Deployment

The API is deployed on Railway and available at:
**https://haidara-string-analyzer-production.up.railway.app/**

# Dependencies
Spring Boot Starter Web - REST API framework

Spring Boot Starter Validation - Request validation

Spring Boot DevTools - Development tools

H2 Database - In-memory database (optional)

JUnit 5 - Testing framework

All dependencies are managed through Maven in pom.xml.

Environment Variables
No environment variables required for local development. The application uses sensible defaults:

Server port: 8080

In-memory storage
